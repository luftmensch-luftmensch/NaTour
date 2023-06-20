/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.api.Server.repository;

import com.natour.api.Server.model.Tappa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
/** Classe repository per la gestione dell' {@link TappaRepository} */
public interface TappaRepository extends JpaRepository<Tappa, Long> {
    /** Retrieval delle tappe totali di un singolo itinerario
     * @param idItinerario: Stringa che identifica un singolo itinerario
     * @return lista di tappe
     */
    @Query(value = "SELECT * FROM tappa t WHERE t.id_itinerario = ?1", nativeQuery = true)
    List<Tappa> findAllByItinerario(String idItinerario);
}