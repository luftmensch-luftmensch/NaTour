/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.api.Server.repository;

import com.natour.api.Server.model.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
/** Classe repository per la gestione dell' {@link ChatRoom} */
public interface ChatRoomRepository extends JpaRepository<ChatRoom,String> {
    /** Retrieval della lista delle chatRoom a cui l'utente loggato partecipa
     * @param utente: String che identifica l'utente di cui vogliamo ottenere le chat room
     * @return lista di chatroom relative ad un singolo utente
     */
    @Query(value = "SELECT * FROM chat_room cr WHERE cr.utente_a = ?1 OR cr.utente_b = ?1", nativeQuery = true)
    List<ChatRoom> findAllByUtente(String utente);
}