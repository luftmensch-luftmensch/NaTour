/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.api.Server.servicesInterface;

import com.natour.api.Server.DTO.TagRicercaDTO;
import java.util.List;

/** Interfaccia che ad alto livello descrive i metodi che il servizio {@link com.natour.api.Server.services.TagRicercaService} dovrà implementare */
public interface TagRicercaServiceInterface {
    /** Retrieval lista dei tag totali sul database
     * @return lista di tag
     */
    List<TagRicercaDTO> getListaTagRicerca();

    /** Retrieval lista di tag appartenti allo stesso itinerario
     * @param nomeItinerario: Identificativo dell'itinerario
     * @return lista dei tag
     */
    List<TagRicercaDTO> getTagStessoItinerario(String nomeItinerario);

    // POST: Creazione di un nuovo Tag

    /** Metodo per la creazione di un nuovo tag
     * @param tagRicercaDTO: Oggetto che contiene tutti i parametri alla creazione di un nuovo tag
     * @return Valore booleano che ci informi della response della richiesta
     */
    boolean aggiungiTagRicerca(TagRicercaDTO tagRicercaDTO);

    /** Metodo per l'eliminazione di un tag esistente
     * @param idTag: Identificativo del tag
     */
    void eliminaTagRicerca(Long idTag);
}