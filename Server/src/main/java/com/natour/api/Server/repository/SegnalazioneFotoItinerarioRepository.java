/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.api.Server.repository;

import com.natour.api.Server.model.SegnalazioneFotoItinerario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
/** Classe repository per la gestione dell' {@link SegnalazioneFotoItinerario} */
public interface SegnalazioneFotoItinerarioRepository extends JpaRepository<SegnalazioneFotoItinerario, Long> {
    // TODO: Aggiungere query (?)

    /** Retrieval delle segnalazioni inerenti ad una stessa foto
     * @param idFoto: Identificativo della foto
     * @return lista segnalazioni
     */
    @Query(value = "SELECT * FROM segnalazioni_foto_itinerario s where s.id_segnalazione_foto_itinerario = ?1", nativeQuery = true)
    List<SegnalazioneFotoItinerario> getListaSegnalazioniStessaFoto(String idFoto);
}
