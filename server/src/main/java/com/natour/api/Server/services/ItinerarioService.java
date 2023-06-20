/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.api.Server.services;

import com.natour.api.Server.DTO.ItinerarioDTO;
import com.natour.api.Server.exceptions.ApiRequestException;
import com.natour.api.Server.model.Itinerario;
import com.natour.api.Server.model.Utente;
import com.natour.api.Server.repository.ItinerarioRepository;
import com.natour.api.Server.repository.UtenteRepository;
import com.natour.api.Server.servicesInterface.ItinerarioServiceInterface;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/** Classe che implementa l'interfaccia {@link ItinerarioServiceInterface} */
@Service("mainItinerarioService")
public class ItinerarioService implements ItinerarioServiceInterface {

    /*
        Con la notazione @Autowired Spring permette di scoprire il bean automaticamente e
        iniettare i collaborating bean (altri bean associati)

        I bean sono gli oggetti che costituisco lo scheletro dell'applicazione.
        Sono gestiti dal Container Spirng IoC (che ha lo scopo di istanziare, configurare e assemblare gli oggetti)

        Un bean è un oggetto di cui viene istanziato, assemblato e gestito in altro modo da un Container ioC
    */

    /** Oggetto repository necessario per le request inerenti ad {@link Itinerario} */
    @Autowired
    private ItinerarioRepository repositoryItinerario;

    /** Oggetto repository necessario per le request inerenti ad {@link Utente} */
    @Autowired
    private UtenteRepository repositoryUtente;


    /** ModelMapper -> Necessaria per la conversione DTO  -> Entity ({@code convertDTOtoEntity}} e viceversa ({@code convertEntityToDTO})*/
    @Autowired
    private ModelMapper modelMapperItinerario;


    /** Metodo per la conversione da Entity a DTO
     * @param itinerario: Oggetto da convertire
     * @return Singolo itinerarioDTO (oggetto convertito
     */
    private ItinerarioDTO convertEntityToDTO(Itinerario itinerario){
        modelMapperItinerario.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);

        ItinerarioDTO itinerarioDTO = new ItinerarioDTO();
        itinerarioDTO = modelMapperItinerario.map(itinerario, ItinerarioDTO.class);
        itinerarioDTO.setDurata(timeConverterToString(itinerario.getDurata()));
        String username = itinerario.getUtenteProprietario().getUsername();
        itinerarioDTO.setUtenteProprietario(username);
        return itinerarioDTO;
    }

    /** Metodo per la conversione da DTO a Entity
     * @param itinerarioDTO: Oggetto da convertire
     * @return Singolo itinerario (oggetto convertito
     */
    private Itinerario convertDTOtoEntity(ItinerarioDTO itinerarioDTO) {
        modelMapperItinerario.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        Itinerario itinerario;
        itinerario = modelMapperItinerario.map(itinerarioDTO, Itinerario.class);
        try {
            itinerario.setDurata(stringConverterToTime(itinerarioDTO.getDurata()));
        }catch(ParseException e){
            throw new ApiRequestException("Formato durata non valido", HttpStatus.BAD_REQUEST);
        }
        Utente utente = repositoryUtente.getById(itinerarioDTO.getUtenteProprietario());
        itinerario.setUtenteProprietario(utente);
        return itinerario;
    }


    /** Metodo ausiliario per la conversione
     * @param durata: String
     * @return Time
     * @throws ParseException In caso di errore lancia una eccezione che viene gestita nei metodi che ne fanno uso
     */
    public Time stringConverterToTime(String durata) throws ParseException {
        DateFormat formatter =  new SimpleDateFormat("HH:mm:ss");
        return new Time(formatter.parse(durata).getTime());
    }

    /** Metodo ausiliario per la conversione
     * @param time: Time
     * @return Stringa
     */
    public String timeConverterToString(Time time) {
        return time.toString();
    }


    /** Metodo per ottenere la lista totali degli itinerari
     * @return lista totale degli itinerari
     */
    @Override
    public List<ItinerarioDTO> getItinerari(){
        List<Itinerario> itinerari = this.repositoryItinerario.findAll();
        List<ItinerarioDTO> itinerarioDTO = new ArrayList<>();
        for (Itinerario i : itinerari){
            itinerarioDTO.add(convertEntityToDTO(i));
        }
        return itinerarioDTO;
    }


    /** Metodo per ottenere la lista degli itinerari appartenti allo stesso utente
     * @param utente: Identificativo per la ricerca degli utenti con lo stesso utente proprietario
     * @return lista itinerari
     */
    @Override
    public List<ItinerarioDTO> getItinerariStessoUtente(String utente){
        List<Itinerario> itinerarios = this.repositoryItinerario.findAllByUtente(utente);
        List<ItinerarioDTO> itinerarioDTOS = new ArrayList<>();
        for(Itinerario itinerario: itinerarios){
            itinerarioDTOS.add(convertEntityToDTO(itinerario));
        }
        return itinerarioDTOS;
    }


    /** Metodo per ottenere un singolo itinerario dato il nome
     * @param nomeItinerario: Identificato dell'itinerario di cui si vuole ottenere informazioni
     * @return Singolo itinerario
     */
    @Override
    public ItinerarioDTO getSingoloItinerario(String nomeItinerario){
        if(this.repositoryItinerario.findById(nomeItinerario).isEmpty()){
            throw new ApiRequestException("Itinerario non trovato!", HttpStatus.NOT_FOUND);
        }
        Itinerario itinerario = this.repositoryItinerario.findSingoloItinerarioByNomeItinerario(nomeItinerario);
        return convertEntityToDTO(itinerario);
    }

                            // FILTRI

    /** Implementazione del metodo per la ricerca degli itinerari dal più recente al meno recente
     * @return lista itinerari
     */
    @Override
    public List<ItinerarioDTO> ricercaPerRecenti(){
        List<Itinerario> itinerarios = this.repositoryItinerario.findAllByRecent();

        List<ItinerarioDTO> itinerarioDTOS = new ArrayList<>();
        for (Itinerario itinerario: itinerarios){
            itinerarioDTOS.add(convertEntityToDTO(itinerario));
        }
        return itinerarioDTOS;
    }

    /** Retrieval della lista degli itinerari che matchano (case insensitive) con la richiesta
     * @param nomeItinerario: Identificato dell'itinerario di cui si vuole ottenere informazioni
     * @return lista itinerari
     */
    @Override
    public List<ItinerarioDTO> ricercaPerNomeItinerario(String nomeItinerario) {
        List<Itinerario> itinerarios = this.repositoryItinerario.findAllByNomeItinerario(nomeItinerario);
        List<ItinerarioDTO> itinerarioDTOS = new ArrayList<>();
        for (Itinerario itinerario: itinerarios){
            itinerarioDTOS.add(convertEntityToDTO(itinerario));
        }
        return itinerarioDTOS;
    }

    /** Implementazione del metodo per la ricerca degli itinerari in base alla città
     * @param citta: Identificativo per la ricerca degli utenti con la stessa città
     * @return lista itinerari
     */
    @Override
    public List<ItinerarioDTO> ricercaByCitta(String citta) {
        List<Itinerario> itinerarios = this.repositoryItinerario.findAllByCitta(citta);
        List<ItinerarioDTO> itinerarioDTOS = new ArrayList<>();
        for(Itinerario itinerario: itinerarios){
            itinerarioDTOS.add(convertEntityToDTO(itinerario));
        }
        return itinerarioDTOS;
    }

    /** Implementazione del metodo per la ricerca degli itinerari in base alla difficoltà
     * @param difficolta: Identificativo per la ricerca degli utenti con la stessa difficoltà
     * @return lista itinerari
     */
    @Override
    public List<ItinerarioDTO> ricercaByDifficolta(String difficolta) {
        List<Itinerario> itinerarios = this.repositoryItinerario.findAllByDifficolta(difficolta);
        List<ItinerarioDTO> itinerarioDTOS = new ArrayList<>();
        for(Itinerario itinerario: itinerarios){
            itinerarioDTOS.add(convertEntityToDTO(itinerario));
        }
        return itinerarioDTOS;
    }

    /** Implementazione del metodo per la ricerca degli itinerari in base al dislivello
     * @param dislivello: Identificativo per la ricerca degli utenti con lo stesso dislivello
     * @return lista itinerari
     */
    @Override
    public List<ItinerarioDTO> ricercaByDislivello(Double dislivello) {
        List<Itinerario> itinerarios = this.repositoryItinerario.findAllByDislivello(dislivello);
        List<ItinerarioDTO> itinerarioDTOS = new ArrayList<>();
        for(Itinerario itinerario: itinerarios){
            itinerarioDTOS.add(convertEntityToDTO(itinerario));
        }
        return itinerarioDTOS;
    }

    // Filtro in base alla zona geografica

    /** Implementazione del metodo per la ricerca degli itinerari in base alla zona geografica
     * @param zonaGeografica: Identificativo per la ricerca degli utenti con la stessa zona geografia
     * @return lista itinerari
     */
    @Override
    public List<ItinerarioDTO> ricercaByZonaGeografica(String zonaGeografica) {
        List<Itinerario> itinerarios = this.repositoryItinerario.findAllByZonaGeografica(zonaGeografica);
        List<ItinerarioDTO> itinerarioDTOS = new ArrayList<>();
        for(Itinerario itinerario: itinerarios){
            itinerarioDTOS.add(convertEntityToDTO(itinerario));
        }
        return itinerarioDTOS;
    }

    // Filtro in base alla durata del percorso

    /** Implementazione del metodo per la ricerca degli itinerari in base alla durata
     * @param durata: Identificativo per la ricerca degli utenti con la stessa durata
     * @return lista itinerari
     */
    @Override
    public List<ItinerarioDTO> ricercaByDurata(String durata) {
        Time time;
        try {
            time = stringConverterToTime(durata);
        } catch (ParseException e) {
            throw new ApiRequestException("Formato durata non valido", HttpStatus.BAD_REQUEST);
        }
        List<Itinerario> itinerarios = this.repositoryItinerario.findAllByDurata(time);
        List<ItinerarioDTO> itinerarioDTOS = new ArrayList<>();
        for(Itinerario itinerario: itinerarios){
            itinerarioDTOS.add(convertEntityToDTO(itinerario));
        }
        return itinerarioDTOS;
    }

    /** Implementazione del metodo per la ricerca degli itinerari in base alla lunghezza
     * @param lunghezza: Identificativo per la ricerca degli utenti con la stessa lunghezza
     * @return lista itinerari
     */
    @Override
    public List<ItinerarioDTO> ricercaByLunghezza(Double lunghezza) {
        List<Itinerario> itinerarios = this.repositoryItinerario.findAllByLunghezza(lunghezza);
        List<ItinerarioDTO> itinerarioDTOS = new ArrayList<>();
        for(Itinerario itinerario: itinerarios){
            itinerarioDTOS.add(convertEntityToDTO(itinerario));
        }
        return itinerarioDTOS;
    }


    /** Implementazione del metodo per l'aggiunta di un nuovo itinerario
     * @param itinerarioDTO: Oggetto che contiene tutti i parametri necessari alla creazione di un nuovo itinerario
     * @throws ApiRequestException In caso di errore lancia una eccezione che viene gestita nei metodi che ne faranno uso
     */
    @Override
    public boolean aggiungiItinerario(ItinerarioDTO itinerarioDTO) throws ApiRequestException {
        Itinerario itinerario = convertDTOtoEntity(itinerarioDTO);
        String utenteProprietario = itinerario.getUtenteProprietario().getUsername();
        if(this.repositoryItinerario.existsById(itinerario.getNomeItinerario())){
            return false;
        }
        if(!repositoryUtente.existsById(utenteProprietario)){
            return false;
        }
        this.repositoryItinerario.save(itinerario);
        return true;
    }


    /** Implementazione del metodo per l'eliminazione di un itinerario esistente
     * @param nomeItinerario: Identificativo necessario all'eliminazione
     * @throws ApiRequestException In caso di errore lancia una eccezione che viene gestita nei metodi che ne faranno uso
     */
    @Override
    public void eliminaItinerario(String nomeItinerario) throws ApiRequestException{
        boolean ricercaId = repositoryItinerario.existsById(nomeItinerario);
        if (!ricercaId){
            throw new ApiRequestException("Itinerario non trovato!", HttpStatus.NOT_FOUND);
        }
        repositoryItinerario.deleteById(nomeItinerario);
    }


    /** Implementazione del metodo per la modifica di un itinerario esistente
     * @param itinerarioDTOdaAggiornare: parametro necessario alla modifica
     */
    @Override
    public void aggiornaItinerario(ItinerarioDTO itinerarioDTOdaAggiornare){
        Itinerario itinerario = convertDTOtoEntity(itinerarioDTOdaAggiornare);
        String utenteProprietario = itinerario.getUtenteProprietario().getUsername();
        if(repositoryItinerario.findById(itinerario.getNomeItinerario()).isEmpty()){
            throw new ApiRequestException("Itinerario non trovato!", HttpStatus.NOT_FOUND);
        }
        if(!repositoryUtente.existsById(utenteProprietario)){
            throw new ApiRequestException("Attenzione! L'utente che ha creato l'itinerario non esiste. Impossibile creare l'itinerario", HttpStatus.NOT_FOUND);
        }
        repositoryItinerario.save(itinerario);
    }
}