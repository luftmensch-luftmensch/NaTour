/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.api.Server.servicesInterface;

import com.natour.api.Server.DTO.GalleriaFotoItinerarioDTO;

import java.util.List;
/** Interfaccia che ad alto livello descrive i metodi che il servizio {@link com.natour.api.Server.services.GalleriaFotoItinerarioService} dovrà implementare */
public interface GalleriaFotoItinerarioServiceInterface {
    /** Retrieval Lista ItinerarioFoto
     * @return lista di foto totali
     */
    List<GalleriaFotoItinerarioDTO> getListaGalleriaFotoItinerari();

    /** Retrieval Lista ItinerarioFoto di uno stesso itinerario
     * @param nomeItinerario: Identificativo dell'itinerario
     * @return lista di foto totali
     */
    List<GalleriaFotoItinerarioDTO> getFotoStessoItinerario(String nomeItinerario);

    /** Creazione foto itinerario
     * @param fotoItinerarioDTO: Oggetto che contiene i parametri necessari alla creazione di un nuovo itinerario
     * @return Valore booleano che ci informi della response della richiesta
     */
    boolean aggiungiFotoItinerario(GalleriaFotoItinerarioDTO fotoItinerarioDTO);

    /** Eliminazione itinerario dato un ID
     * @param idFotoItinerario: Id necessario ad identificare un itinerario
     */
    void eliminaItinerario(String idFotoItinerario);
}