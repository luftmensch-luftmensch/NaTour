/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.api.Server.services;

import com.natour.api.Server.DTO.SegnalazioneFotoItinerarioDTO;
import com.natour.api.Server.exceptions.ApiRequestException;
import com.natour.api.Server.model.GalleriaFotoItinerario;
import com.natour.api.Server.model.SegnalazioneFotoItinerario;
import com.natour.api.Server.repository.GalleriaFotoItinerarioRepository;
import com.natour.api.Server.repository.SegnalazioneFotoItinerarioRepository;
import com.natour.api.Server.servicesInterface.SegnalazioneFotoItinerarioServiceInterface;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/** Classe che implementa l'interfaccia {@link SegnalazioneFotoItinerarioServiceInterface} */
@Service("mainSegnalazioneFotoItinerarioService")
public class SegnalazioneFotoItinerarioService implements SegnalazioneFotoItinerarioServiceInterface {

    /** Oggetto repository necessario per le request inerenti ad {@link SegnalazioneFotoItinerario} */
    @Autowired
    private SegnalazioneFotoItinerarioRepository segnalazioneFotoItinerarioRepository;

    /** Oggetto repository necessario per le request inerenti ad {@link GalleriaFotoItinerario} */
    @Autowired
    private GalleriaFotoItinerarioRepository galleriaFotoItinerarioRepository;

    /** ModelMapper -> Necessaria per la conversione DTO  -> Entity ({@code convertDTOtoEntity}} e viceversa ({@code convertEntityToDTO})*/
    @Autowired
    private ModelMapper modelMapperSegnalazioneFotoItinerario;

    /** Metodo per la conversione da Entity a DTO
     * @param segnalazioneFotoItinerario: Oggetto da convertire
     * @return Singola segnalazioneFotoItinerarioDTO (oggetto convertire)
     */
    private SegnalazioneFotoItinerarioDTO convertEntityToDTO(SegnalazioneFotoItinerario segnalazioneFotoItinerario){
        modelMapperSegnalazioneFotoItinerario.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        modelMapperSegnalazioneFotoItinerario.getConfiguration().setAmbiguityIgnored(true);
        SegnalazioneFotoItinerarioDTO segnalazioneFotoItinerarioDTO = new SegnalazioneFotoItinerarioDTO();
        segnalazioneFotoItinerarioDTO = modelMapperSegnalazioneFotoItinerario.map(segnalazioneFotoItinerario, SegnalazioneFotoItinerarioDTO.class);
        String itinerarioCorrispondente = segnalazioneFotoItinerario.getGalleriaFotoItinerarioCorrispondente().getIdFotoItinerario();
        segnalazioneFotoItinerarioDTO.setGalleriaFotoItinerarioCorrispondente(itinerarioCorrispondente);
        return segnalazioneFotoItinerarioDTO;
    }


    /** Metodo per la conversione da DTO a Entity
     * @param segnalazioneFotoItinerarioDTO: Oggetto da convertire
     * @return Singola segnalazioneFotoItinerario (oggetto convertire)
     */
    private SegnalazioneFotoItinerario convertDTOtoEntity(SegnalazioneFotoItinerarioDTO segnalazioneFotoItinerarioDTO){
        modelMapperSegnalazioneFotoItinerario.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        SegnalazioneFotoItinerario segnalazioneFotoItinerario;
        segnalazioneFotoItinerario = modelMapperSegnalazioneFotoItinerario.map(segnalazioneFotoItinerarioDTO, SegnalazioneFotoItinerario.class);

        GalleriaFotoItinerario galleriaFotoItinerario = galleriaFotoItinerarioRepository.getById(segnalazioneFotoItinerarioDTO.getGalleriaFotoItinerarioCorrispondente());
        segnalazioneFotoItinerario.setGalleriaFotoItinerarioCorrispondente(galleriaFotoItinerario);
        return segnalazioneFotoItinerario;
    }

    /** Metodo per ottenere le segnalazioni di una stessa foto
      * @param idFotoItinerarioCorrispondente: Identificativo della foto
     * @return lista segnalazioni inerenti alla stessa foto
     */
    @Override
    public List<SegnalazioneFotoItinerarioDTO> getListaSegnalazioniStessaFoto(String idFotoItinerarioCorrispondente) {
        List<SegnalazioneFotoItinerario> segnalazioneFotoItinerarios = this.segnalazioneFotoItinerarioRepository.getListaSegnalazioniStessaFoto(idFotoItinerarioCorrispondente);
        List<SegnalazioneFotoItinerarioDTO> segnalazioneFotoItinerarioDTOS = new ArrayList<>();
        for (SegnalazioneFotoItinerario s: segnalazioneFotoItinerarios){
            segnalazioneFotoItinerarioDTOS.add(convertEntityToDTO(s));
        }
        return segnalazioneFotoItinerarioDTOS;
    }

    /** Implementazione metodo per l'aggiunta di una nuova segnalazione
     * @param segnalazioneFotoItinerarioDTO: Oggetto che contiene i parametri necessari all'inserimento di una nuova segnalazione
     * @throws ApiRequestException In caso di errore viene lanciata un eccezione che verrà gestito nei metodi che ne faranno uso
     */
    @Override
    public boolean aggiungiSegnalazione(SegnalazioneFotoItinerarioDTO segnalazioneFotoItinerarioDTO) throws ApiRequestException{
        SegnalazioneFotoItinerario segnalazioneFotoItinerario = convertDTOtoEntity(segnalazioneFotoItinerarioDTO);
        if(this.segnalazioneFotoItinerarioRepository.existsById(segnalazioneFotoItinerario.getIdSegnalazione())){
            return false;
        }
        this.segnalazioneFotoItinerarioRepository.save(segnalazioneFotoItinerario);
        return true;
    }

    /** Implementazione metodo per l'eliminazione di una segnalazione esistente
     * @param idSegnalazione: Identificativo della segnalazione
     * @throws ApiRequestException In caso di errore viene lanciata un eccezione che verrà gestito nei metodi che ne faranno uso
     */
    @Override
    public void eliminaSegnalazione(Long idSegnalazione) throws ApiRequestException{
        if(this.segnalazioneFotoItinerarioRepository.findById(idSegnalazione).isEmpty()){
            throw new ApiRequestException("Segnalazione non trovato!", HttpStatus.NOT_FOUND);
        }
        this.segnalazioneFotoItinerarioRepository.deleteById(idSegnalazione);
    }
}