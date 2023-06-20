/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.api.Server.repository;

import com.natour.api.Server.model.GalleriaFotoItinerario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/** Classe repository per la gestione dell' {@link GalleriaFotoItinerario} */
public interface GalleriaFotoItinerarioRepository extends JpaRepository<GalleriaFotoItinerario, String> {
    /** Retrieval della lista di foto relative ad un singolo itinerario
     * @param idItinerario: Stringa che identifica l'itinerario di cui vogliamo ottenere le foto
     * @return lista di foto relative ad un itinerario
     */
    @Query(value = "SELECT * FROM galleria_foto_itinerario gfi WHERE gfi.id_itinerario = ?1", nativeQuery = true)
    List<GalleriaFotoItinerario> findAllByItinerario(String idItinerario);
}
