/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.api.Server.repository;

import com.natour.api.Server.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

/** Classe repository per la gestione dell' {@link Utente} */
public interface UtenteRepository extends JpaRepository<Utente, String> {
}