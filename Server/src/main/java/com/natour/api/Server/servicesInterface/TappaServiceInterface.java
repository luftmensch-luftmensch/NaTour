/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.api.Server.servicesInterface;

import com.natour.api.Server.DTO.TappaDTO;

import java.util.List;
/** Interfaccia che ad alto livello descrive i metodi che il servizio {@link com.natour.api.Server.services.TappaService} dovrà implementare */
public interface TappaServiceInterface {
    /** Retrieval della lista delle tappe totali
     * @return lista delle tappe totali
    */
    List<TappaDTO> getListaTappe();

    /** Retrieval della lista delle tappe di uno stesso itinerario
     * @param nomeItinerario: Identificativo dell'itinerario di cui si vuole conoscere le tappe
     * @return lista delle tappe
     */
    List<TappaDTO> getTappeStessoItinerario(String nomeItinerario);

    /** Metodo che permette di aggiungere una nuova tappa
     * @param tappaDTO: Oggetto che contiene le informazioni necessarie all'aggiunta di una nuova tappa
    */
    void aggiungiTappa(TappaDTO tappaDTO);

    /** Metodo per l'eliminazione di una tappa
     * @param idTappa: Identificativo della tappa da eliminare
     */
    void eliminaTappa(Long idTappa);
}