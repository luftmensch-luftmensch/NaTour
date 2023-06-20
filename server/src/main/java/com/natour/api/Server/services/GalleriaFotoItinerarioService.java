/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.api.Server.services;

import com.natour.api.Server.DTO.GalleriaFotoItinerarioDTO;
import com.natour.api.Server.exceptions.ApiRequestException;
import com.natour.api.Server.model.GalleriaFotoItinerario;
import com.natour.api.Server.model.Itinerario;
import com.natour.api.Server.repository.GalleriaFotoItinerarioRepository;
import com.natour.api.Server.repository.ItinerarioRepository;
import com.natour.api.Server.servicesInterface.GalleriaFotoItinerarioServiceInterface;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
/** Classe che implementa l'interfaccia {@link GalleriaFotoItinerarioServiceInterface} */
@Service("mainGalleriaFotoItinerarioService")
public class GalleriaFotoItinerarioService implements GalleriaFotoItinerarioServiceInterface {
    /** Oggetto repository necessario per le request inerenti ad {@link GalleriaFotoItinerario} */
    @Autowired
    private GalleriaFotoItinerarioRepository repositoryItinerarioFoto;

    /** Oggetto repository necessario per le request inerenti ad {@link Itinerario} */
    @Autowired
    private ItinerarioRepository itinerarioRepository;


    /** ModelMapper -> Necessaria per la conversione DTO  -> Entity ({@code convertDTOtoEntity}} e viceversa ({@code convertEntityToDTO})*/
    @Autowired
    private ModelMapper modelMapperItinerarioFoto;

    /** Metodo per la conversione da Entity a DTO
     * @param galleriaFotoItinerario: Oggetto da convertire
     * @return Singola fotoDTO (oggetto convertito)
     */
    private GalleriaFotoItinerarioDTO convertEntitytoDTO(GalleriaFotoItinerario galleriaFotoItinerario){
        modelMapperItinerarioFoto.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        GalleriaFotoItinerarioDTO galleriaFotoItinerarioDTO = new GalleriaFotoItinerarioDTO();
        galleriaFotoItinerarioDTO = modelMapperItinerarioFoto.map(galleriaFotoItinerario, GalleriaFotoItinerarioDTO.class);
        String itinerarioProprietario = galleriaFotoItinerario.getItinerarioProprietario().getNomeItinerario();
        galleriaFotoItinerarioDTO.setItinerarioProprietario(itinerarioProprietario);
        return galleriaFotoItinerarioDTO;
    }

    /** Metodo per la conversione da Entity a DTO
     * @param itinerarioFotoDTO: Oggetto da convertire
     * @return Singola foto (oggetto convertito)
     */
    private GalleriaFotoItinerario convertDTOtoEntity(GalleriaFotoItinerarioDTO itinerarioFotoDTO){
        modelMapperItinerarioFoto.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        GalleriaFotoItinerario itinerarioFoto;
        itinerarioFoto = modelMapperItinerarioFoto.map(itinerarioFotoDTO, GalleriaFotoItinerario.class);
        Itinerario itinerario = itinerarioRepository.getById(itinerarioFotoDTO.getItinerarioProprietario());
        itinerarioFoto.setItinerarioProprietario(itinerario);
        return itinerarioFoto;
    }

    /** Metodo per ottenere la lista delle foto totali presenti sul database
     * @return lista foto totali
     */
    @Override
    public List<GalleriaFotoItinerarioDTO> getListaGalleriaFotoItinerari() {
        List<GalleriaFotoItinerario> galleriaFotoItinerarios = this.repositoryItinerarioFoto.findAll();
        List<GalleriaFotoItinerarioDTO> galleriaFotoItinerarioDTOs = new ArrayList<>();
        for(GalleriaFotoItinerario galleriaFotoItinerario: galleriaFotoItinerarios){
            galleriaFotoItinerarioDTOs.add(convertEntitytoDTO(galleriaFotoItinerario));
        }
        return galleriaFotoItinerarioDTOs;
    }

    /** Metodo per ottenere la lista delle foto appartenenti ad uno stesso itinerario
     * @param nomeItinerario: Identificativo dell'itinerario
     * @return lista foto appartenti allo stesso itinerario
     */
    @Override
    public List<GalleriaFotoItinerarioDTO> getFotoStessoItinerario(String nomeItinerario) {
        List<GalleriaFotoItinerario> galleriaFotoItinerarioList = this.repositoryItinerarioFoto.findAllByItinerario(nomeItinerario);
        List<GalleriaFotoItinerarioDTO> galleriaFotoItinerarioDTOS = new ArrayList<>();
        for(GalleriaFotoItinerario galleriaFotoItinerario: galleriaFotoItinerarioList){
            galleriaFotoItinerarioDTOS.add(convertEntitytoDTO(galleriaFotoItinerario));
        }
        return galleriaFotoItinerarioDTOS;
    }

    /** Implementazione del metodo per l'aggiunta di una nuova foto sul database
     * @param fotoItinerarioDTO: Oggetto che contiene i parametri necessari alla creazione di un nuovo itinerario
     */
    @Override
    public boolean aggiungiFotoItinerario(GalleriaFotoItinerarioDTO fotoItinerarioDTO) {
        GalleriaFotoItinerario fotoItinerario = convertDTOtoEntity(fotoItinerarioDTO);
        if(this.repositoryItinerarioFoto.existsById(fotoItinerario.getIdFotoItinerario())){
            //throw new ApiRequestException("Foto già esistente", HttpStatus.BAD_REQUEST);
            return false;
        }

        this.repositoryItinerarioFoto.save(fotoItinerario);
        return true;
    }

    /** Implementazione del metodo per l'eliminazione di una foto esistente
     * @param idFotoItinerario: Id necessario ad identificare un itinerario
     * @throws ApiRequestException In caso di errore lancia una eccezione che viene gestita nei metodi che ne fanno uso
     */
    @Override
    public void eliminaItinerario(String idFotoItinerario) throws ApiRequestException{
        if(!this.repositoryItinerarioFoto.existsById(idFotoItinerario)){
            throw new ApiRequestException("Foto itinerario non trovata", HttpStatus.BAD_REQUEST);
        }
        repositoryItinerarioFoto.deleteById(idFotoItinerario);

    }
}