/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.api.Server.services;

import com.natour.api.Server.DTO.TappaDTO;
import com.natour.api.Server.exceptions.ApiRequestException;
import com.natour.api.Server.model.Itinerario;
import com.natour.api.Server.model.Tappa;
import com.natour.api.Server.repository.ItinerarioRepository;
import com.natour.api.Server.repository.TappaRepository;
import com.natour.api.Server.servicesInterface.TappaServiceInterface;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/** Classe che implementa l'interfaccia {@link TappaServiceInterface} */

@Service("mainTappaService")
public class TappaService implements TappaServiceInterface {
    /** Oggetto repository necessario per le request inerenti ad {@link Tappa} */
    @Autowired
    private TappaRepository tappaRepository;

    /** Oggetto repository necessario per le request inerenti ad {@link Itinerario} */
    @Autowired
    private ItinerarioRepository itinerarioRepository;

    /** ModelMapper -> Necessaria per la conversione DTO  -> Entity ({@code convertDTOtoEntity}} e viceversa ({@code convertEntitytoDTO})*/
    @Autowired
    private ModelMapper modelMapperTappa;

    /** Metodo di conversione da DTO a Entity
     * @param tappaDTO: Oggetto da convertire
     * @return Singola tappa (oggetto convertito)
     */
    private Tappa convertDTOtoEntity(TappaDTO tappaDTO){
        modelMapperTappa.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);

        Tappa tappa;
        tappa = modelMapperTappa.map(tappaDTO, Tappa.class);

        Itinerario itinerario = itinerarioRepository.getById(tappaDTO.getItinerarioCorrispondente());
        tappa.setItinerarioCorrispondente(itinerario);
        return tappa;
    }

    /** Metodo di conversione da DTO a Entity
     * @param tappa: Oggetto da convertire
     * @return Singola tappaDTO (oggetto convertito)
     */
    private TappaDTO convertEntitytoDTO(Tappa tappa){
        modelMapperTappa.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        TappaDTO tappaDTO = new TappaDTO();
        tappaDTO = modelMapperTappa.map(tappa, TappaDTO.class);
        String itinerarioCorrispondente = tappa.getItinerarioCorrispondente().getNomeItinerario();
        tappaDTO.setItinerarioCorrispondente(itinerarioCorrispondente);
        return tappaDTO;
    }


    /** Implementazione del metodo per ottenere tutte le tappe totali
     * @return lista delle tappe totali
     */
    @Override
    public List<TappaDTO> getListaTappe() {
        List<Tappa> tappas = this.tappaRepository.findAll();
        List<TappaDTO> tappaDTOS = new ArrayList<>();
        for (Tappa tappa : tappas){
            tappaDTOS.add(convertEntitytoDTO(tappa));
        }
        return tappaDTOS;
    }

    /** Implementazione del metodo per ottenere tutte le tappe inerenti allo stesso itinerario
     * @param nomeItinerario: Parametro necessario all'identificazione delle tappe
     * @return lista delle tappe totali
     */
    @Override
    public List<TappaDTO> getTappeStessoItinerario(String nomeItinerario) {
        if(this.tappaRepository.findAllByItinerario(nomeItinerario).isEmpty()){
            throw new ApiRequestException("Nessuna tappa esistente per l'itinerario selezionato", HttpStatus.BAD_REQUEST);
        }
        List<Tappa> tappas = this.tappaRepository.findAllByItinerario(nomeItinerario);
        List<TappaDTO> tappaDTOS = new ArrayList<>();
        for (Tappa tappa : tappas){
            tappaDTOS.add(convertEntitytoDTO(tappa));
        }
        return tappaDTOS;
    }

    /** Implementazione del metodo per aggiungere una nuova tappa
     * @param tappaDTO: Oggetto che contiene le informazioni necessarie all'aggiunta di una nuova tappa
     * @throws ApiRequestException In caso di errore viene lanciata un eccezione che verrà gestito nei metodi che ne faranno uso
    */
    @Override
    public void aggiungiTappa(TappaDTO tappaDTO) throws ApiRequestException{
        Tappa tappa = convertDTOtoEntity(tappaDTO);
        if(this.tappaRepository.existsById(tappa.getIdTappa())){
            throw new ApiRequestException("Tappa già esistente", HttpStatus.BAD_REQUEST);
        }
        this.tappaRepository.save(tappa);
    }

    /** Implementazione del metodo per eliminare di una tappa esistente
     * @param idTappa: Identificativo della tappa da eliminare
     */
    @Override
    public void eliminaTappa(Long idTappa) {
        if(!this.tappaRepository.existsById(idTappa)){
            throw new ApiRequestException("Tappa non trovato", HttpStatus.BAD_REQUEST);
        }
        tappaRepository.deleteById(idTappa);
    }
}