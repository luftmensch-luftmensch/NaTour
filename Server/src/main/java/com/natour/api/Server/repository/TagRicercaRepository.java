/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.api.Server.repository;

import com.natour.api.Server.model.TagRicerca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
/** Classe repository per la gestione dell' {@link TagRicerca} */
public interface TagRicercaRepository extends JpaRepository<TagRicerca, Long> {
    /** Retrieval della lista di tag relativi ad uno stesso itinerario
     * @param idItinerario: Stringa che identifica un itinerario
     * @return lista di tag relativi ad uno stesso itinerario
     */
    @Query(value = "SELECT * FROM tag_ricerca tr WHERE tr.id_itinerario_tag = ?1", nativeQuery = true)
    List<TagRicerca> findAllByItinerario(String idItinerario);
}
