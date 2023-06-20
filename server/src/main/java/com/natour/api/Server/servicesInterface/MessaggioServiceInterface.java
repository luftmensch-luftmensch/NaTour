/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.api.Server.servicesInterface;

import com.natour.api.Server.DTO.MessaggioDTO;

import java.util.List;
/** Interfaccia che ad alto livello descrive i metodi che il servizio {@link com.natour.api.Server.services.MessaggioService} dovrà implementare */
public interface MessaggioServiceInterface {

    /** Retrieval lista dei messaggi appartenti ad una stessa chat room
     * @param idChatRoom: Identificativo della chat room corrispondente
     * @return lista dei messaggi
     */
    List<MessaggioDTO> getListaMessaggiStessaChatRoom(String idChatRoom);

    /** Metodo per la creazione di un nuovo messaggio
     * @param messaggioDTO: Oggetto che contiene tutti i parametri necessari alla creazione di un nuovo messaggio
     * @return Valore booleano che ci informi della response della richiesta
     */
    boolean creaNuovoMessaggio(MessaggioDTO messaggioDTO);

    // `PUT` e `DELETE` non sono specificate tra i punti della traccia -> Creare quindi uno stub nell'interfaccia android
}