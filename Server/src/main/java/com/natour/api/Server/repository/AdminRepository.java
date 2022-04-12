/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.api.Server.repository;

import com.natour.api.Server.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/** Classe repository per la gestione dell' {@link Admin} */
public interface AdminRepository extends JpaRepository<Admin, String> {

    /** Retrieval singolo itinerario in base al suo nome
     * @param username: Stringa che identifica l'itinerario utilizzando il suo nome
     * @return Singolo itinerario (sue informazioni)
     */
    @Query(value = "SELECT * FROM admin ad WHERE ad.username = ?1", nativeQuery = true)
    Admin findSingoloAdminByUsername(String username);
}
