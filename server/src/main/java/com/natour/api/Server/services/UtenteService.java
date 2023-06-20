/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.api.Server.services;

import com.natour.api.Server.DTO.UtenteDTO;
import com.natour.api.Server.exceptions.ApiRequestException;
import com.natour.api.Server.model.Utente;
import com.natour.api.Server.repository.UtenteRepository;
import com.natour.api.Server.servicesInterface.UtenteServiceInterface;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/** Classe che implementa l'interfaccia {@link UtenteServiceInterface} */
@Service("mainUtenteService")
public class UtenteService implements UtenteServiceInterface {

    /** Oggetto repository necessario per le request inerenti ad {@link Utente} */
    @Autowired
    private UtenteRepository repositoryUtente;

    /** ModelMapper -> Necessaria per la conversione DTO  -> Entity ({@code convertDTOtoEntity}}*/
    @Autowired
    private ModelMapper modelMapperUtente;

    /** Metodo che permette di ottenere la lista degli utenti totali
     * @return lista degli utenti totali
     */
    public List<Utente> getUtenti(){
        return this.repositoryUtente.findAll();
    }


    // Get Utente

    /** Metodo per ottenere le informazioni di un singolo utente
     * @param username: Parametro per identificare l'utente di cui si vuole conoscere le informazioni
     * @return Singolo utente (opzionale, in quanto potrebbe anche non esistere)
     */
    public Optional<Utente> getUtente(String username) {
        return this.repositoryUtente.findById(username);
    }

    /** Metodo di conversione da DTO a Entity
     * @param utenteDTO: Oggetto da convertire
     * @return Singolo utente (oggetto convertito)
     */
    private Utente convertDTOtoEntity(UtenteDTO utenteDTO){
        modelMapperUtente.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        Utente utente;
        utente = modelMapperUtente.map(utenteDTO, Utente.class);
        return utente;
    }

    /** Implementazione del metodo per aggiungere un nuovo utente
     * @param utenteDTO: Oggetto che contiene tutti i parametri per aggiungere un nuovo itinerario
     * @throws ApiRequestException In caso di errore viene lanciata un eccezione che verrà gestito nei metodi che ne faranno uso
     */
    public void aggiungiUtente(UtenteDTO utenteDTO) throws ApiRequestException{
        Utente utente = convertDTOtoEntity(utenteDTO);
        if(this.repositoryUtente.existsById(utente.getUsername())){
            throw new ApiRequestException("Utente non trovato", HttpStatus.NOT_FOUND);
        }
        repositoryUtente.save(utente);
    }

    /** Implementazione del metodo per l'eliminazione di un utente
     * @param username: Stringa che ha lo scopo di identificare un utente
     * @throws ApiRequestException In caso di errore viene lanciata un eccezione che verrà gestito nei metodi che ne faranno uso
     */
    public void eliminaUtente(String username) throws ApiRequestException{
        boolean ricercaId = repositoryUtente.existsById(username);
        if(!ricercaId){
            throw new ApiRequestException("Utente non trovato", HttpStatus.NOT_FOUND);
        }
        repositoryUtente.deleteById(username);
    }

    /** Implementazione della modifica di un utente esistente
     * @param utenteDTO: Oggetto che contiene tutti i parametri per aggiungere un nuovo itinerario
     */
    public void aggiornaUtente(UtenteDTO utenteDTO){
        Utente utente = convertDTOtoEntity(utenteDTO);
        if(repositoryUtente.findById(utente.getUsername()).isEmpty()){
            throw new ApiRequestException("Utente non trovato!", HttpStatus.NOT_FOUND);
        }
        repositoryUtente.save(utente);
    }
}