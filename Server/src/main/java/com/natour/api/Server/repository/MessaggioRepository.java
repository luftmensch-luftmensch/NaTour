/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.api.Server.repository;

import com.natour.api.Server.model.Messaggio;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;

import java.util.List;
/** Classe repository per la gestione dell' {@link Messaggio} */
public interface MessaggioRepository extends JpaRepository<Messaggio, Long> {
    // TODO: Gestire l'ordinamento con un ORDER BY

    /** Retrieval dei messaggi appartenti a una stessa chat room
     * @param chatRoom: Stringa che ha lo scopo di identificare una chat room
     * @return lista di messaggi
     */
    @Query(value = "SELECT * FROM messaggio m WHERE m.id_chat = ?1", nativeQuery = true)
    List<Messaggio> getMessaggiStessaChatRoom(String chatRoom);
}