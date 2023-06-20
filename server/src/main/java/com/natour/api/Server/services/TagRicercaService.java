package com.natour.api.Server.services;

import com.natour.api.Server.DTO.TagRicercaDTO;
import com.natour.api.Server.exceptions.ApiRequestException;
import com.natour.api.Server.model.Itinerario;
import com.natour.api.Server.model.TagRicerca;
import com.natour.api.Server.repository.ItinerarioRepository;
import com.natour.api.Server.repository.TagRicercaRepository;
import com.natour.api.Server.servicesInterface.TagRicercaServiceInterface;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/** Classe che implementa l'interfaccia {@link TagRicercaServiceInterface} */

@Service("mainTagRicercaService")
public class TagRicercaService implements TagRicercaServiceInterface {
    /** Oggetto repository necessario per le request inerenti ad {@link TagRicerca} */
    @Autowired
    private TagRicercaRepository repositoryTagRicerca;

    /** Oggetto repository necessario per le request inerenti ad {@link Itinerario} */
    @Autowired
    private ItinerarioRepository itinerarioRepository;

    /** ModelMapper -> Necessaria per la conversione DTO  -> Entity ({@code convertDTOtoEntity}} e viceversa ({@code convertEntityToDTO})*/
    @Autowired
    private ModelMapper modelMapperTagRicerca;

    /** Metodo per la conversione da Entity a DTO
     * @param tagRicerca: Oggetto da convertire
     * @return Singolo tagDTO (oggetto convertito)
     */
    private TagRicercaDTO convertEntityToDTO(TagRicerca tagRicerca){
        modelMapperTagRicerca.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        TagRicercaDTO tagRicercaDTO = modelMapperTagRicerca.map(tagRicerca, TagRicercaDTO.class);
        tagRicercaDTO.setItinerarioProprietarioTag(tagRicerca.getItinerarioProprietarioTag().getNomeItinerario());
        return tagRicercaDTO;
    }

    /** Metodo per la conversione da DTO a Entity
     * @param tagRicercaDTO: Oggetto da convertire
     * @return Singolo tag (oggetto convertito)
     */
    private TagRicerca convertDTOtoEntity(TagRicercaDTO tagRicercaDTO){
        modelMapperTagRicerca.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        TagRicerca tagRicerca;
        tagRicerca = modelMapperTagRicerca.map(tagRicercaDTO, TagRicerca.class);
        Itinerario itinerario = itinerarioRepository.getById(tagRicercaDTO.getItinerarioProprietarioTag());
        tagRicerca.setItinerarioProprietarioTag(itinerario);
        return tagRicerca;
    }

    /** Metodo per ottenere la lista totale di tag sul database
     * @return lista dei tag
     */
    @Override
    public List<TagRicercaDTO> getListaTagRicerca() {
        List<TagRicerca> tagRicercas = this.repositoryTagRicerca.findAll();
        List<TagRicercaDTO> tagRicercaDTOS = new ArrayList<>();
        for (TagRicerca tagRicerca: tagRicercas){
            tagRicercaDTOS.add(convertEntityToDTO(tagRicerca));
        }
        return tagRicercaDTOS;
    }

    /** Metodo per ottenere la lista dei tag di uno stesso itinerario
     * @param nomeItinerario: Identificativo dell'itinerario
     * @return lista tag stesso itinerario
     */
    @Override
    public List<TagRicercaDTO> getTagStessoItinerario(String nomeItinerario) {
        List<TagRicerca> tagRicercas = this.repositoryTagRicerca.findAllByItinerario(nomeItinerario);
        List<TagRicercaDTO> tagRicercaDTOS = new ArrayList<>();
        for (TagRicerca tagRicerca: tagRicercas){
            tagRicercaDTOS.add(convertEntityToDTO(tagRicerca));
        }
        return tagRicercaDTOS;
    }

    /** Implementazione del metodo per l'aggiunta di un nuovo tag
     * @param tagRicercaDTO: Oggetto che contiene tutti i parametri alla creazione di un nuovo tag
     * @throws ApiRequestException In caso di errore viene lanciata un eccezione che verrà gestito nei metodi che ne faranno uso
     */
    @Override
    public boolean aggiungiTagRicerca(TagRicercaDTO tagRicercaDTO) throws ApiRequestException {
        TagRicerca tagRicerca = convertDTOtoEntity(tagRicercaDTO);
        // TODO: Modificare in modo che controlli se esista già il tag con uno specifico value inerente ad un itinerario
        if(this.repositoryTagRicerca.existsById(tagRicerca.getIdTag())){
            //throw new ApiRequestException("Tag già esistente", HttpStatus.BAD_REQUEST);
            return false;
        }
        this.repositoryTagRicerca.save(tagRicerca);
        return true;
    }

    /** Implementazione del metodo per l'eliminazione di un tag esistente
     * @param idTag: Identificativo del tag
     */
    @Override
    public void eliminaTagRicerca(Long idTag) {
        if(!this.repositoryTagRicerca.existsById(idTag)){
            throw new ApiRequestException("Tag non trovato", HttpStatus.BAD_REQUEST);
        }
        repositoryTagRicerca.deleteById(idTag);
    }
}