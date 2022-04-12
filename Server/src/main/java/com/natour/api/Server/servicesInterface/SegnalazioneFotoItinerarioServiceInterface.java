/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.api.Server.servicesInterface;

import com.natour.api.Server.DTO.SegnalazioneFotoItinerarioDTO;

import java.util.List;
/** Interfaccia che ad alto livello descrive i metodi che il servizio {@link com.natour.api.Server.services.SegnalazioneFotoItinerarioService} dovrà implementare */
public interface SegnalazioneFotoItinerarioServiceInterface {
    /** Retrieval lista segnalazioni inerenti alla stessa foto
     * @param idFotoItinerarioCorrispondente: Identificativo della foto
     * @return lista segnalazioni inerenti ad una singola foto
     */
    List<SegnalazioneFotoItinerarioDTO> getListaSegnalazioniStessaFoto(String idFotoItinerarioCorrispondente);

    /** Metodo per l'aggiunta di una nuova segnalazione
     * @param segnalazioneFotoItinerarioDTO: Oggetto che contiene i parametri necessari all'inserimento di una nuova segnalazione
     * @return Valore booleano che ci informi della response della richiesta
     */
    boolean aggiungiSegnalazione(SegnalazioneFotoItinerarioDTO segnalazioneFotoItinerarioDTO);

    /** Metodo per l'eliminazione di una segnalazione esistente
     * @param idSegnalazione: Identificativo della segnalazione
     */
    void eliminaSegnalazione(Long idSegnalazione);
}