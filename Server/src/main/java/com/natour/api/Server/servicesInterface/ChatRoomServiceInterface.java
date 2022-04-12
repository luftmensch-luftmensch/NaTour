/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.api.Server.servicesInterface;

import com.natour.api.Server.DTO.ChatRoomDTO;

import java.util.List;

/** Interfaccia che ad alto livello descrive i metodi che il servizio {@link com.natour.api.Server.services.ChatRoomService} dovrà implementare */
public interface ChatRoomServiceInterface {
    /** Retrieval lista di tutte le chat room presenti
     * @return lista chat room totali
     */
    List<ChatRoomDTO> getListaChatRoomTotali();

    /** Retrieval lista di tutte le chat room a cui partecipa l'utente
     * @param utente: Identificativo dell'utente
     * @return lista chat room totali
     */
    List<ChatRoomDTO> getListaChatRoomUtente(String utente);

    /** Creazione Chat Room
     * @param chatRoomDTO: Oggetto che contiene tutti i parametri necessari alla creazione di una nuova chat room
     * @return Valore booleano che ci informi della response della richiesta
     */
    boolean creaChatRoom(ChatRoomDTO chatRoomDTO);

    /**  Eliminazione chat room
     * @param chatRoomDTO: Oggetto necessario all'ìdentificazione della chat room da eliminare
     */
    void eliminazioneChatRoom(ChatRoomDTO chatRoomDTO);
}