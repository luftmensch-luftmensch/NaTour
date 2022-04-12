/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.api.Server.repository;

import com.natour.api.Server.model.Itinerario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Time;
import java.util.List;
/** Classe repository per la gestione dell' {@link Itinerario} */
public interface ItinerarioRepository extends JpaRepository<Itinerario, String>{

    /** Retrieval deli itinerari appartenti ad un singolo utente
     * @param creatoreItinerario: Stringa che identifica l'utente di cui vogliamo ottenere gli itinerari
     * @return lista di itinerari di uno stesso utente
    */
    @Query(value = "SELECT * FROM itinerario i WHERE i.id_proprietario = ?1", nativeQuery = true)
    List<Itinerario> findAllByUtente(String creatoreItinerario);

    /** Retrieval degli itinerari dal più recente al meno recente
     * @return lista di itinerari
     */
    @Query(value = "select * from itinerario i ORDER by i.nomeitinerario DESC", nativeQuery = true)
    List<Itinerario> findAllByRecent();

    /** Retrieval singolo itinerario in base al suo nome
     * @param nomeItinerario: Stringa che identifica l'itinerario utilizzando il suo nome
     * @return Singolo itinerario (sue informazioni)
     */
    @Query(value = "SELECT * FROM itinerario i WHERE i.nomeitinerario = ?1", nativeQuery = true)
    Itinerario findSingoloItinerarioByNomeItinerario(String nomeItinerario);

    /** Retrieval della lista degli itinerari che matchano (case insensitive) con la richiesta
     * @param nomeItinerario: Stringa che identifica l'itinerario utilizzando il suo nome
     * @return lista itinerari
     */
    @Query(value = "SELECT * FROM itinerario i WHERE i.nomeitinerario ILIKE %?1%", nativeQuery = true)
    List<Itinerario> findAllByNomeItinerario(String nomeItinerario);

    /** Retrieval degli itinerari in base alla città
      * @param citta: Stringa che identifica gli itinerari utilizzando la città
     * @return lista itinerari
     */
    @Query(value = "SELECT * FROM itinerario i WHERE i.citta = ?1", nativeQuery = true)
    List<Itinerario> findAllByCitta(String citta);

    /** Retrieval degli itinerari in base alla città
     * @param difficolta: Stringa che identifica gli itinerari utilizzando la difficoltà
     * @return lista itinerari
     */
    @Query(value = "SELECT * FROM itinerario i WHERE i.difficolta = ?1", nativeQuery = true)
    List<Itinerario> findAllByDifficolta(String difficolta);

    /** Retrieval degli itinerari in base alla città
     * @param dislivello: Stringa che identifica gli itinerari utilizzando la dislivello
     * @return lista itinerari
     */
    @Query(value = "SELECT * FROM itinerario i WHERE i.dislivello = ?1", nativeQuery = true)
    List<Itinerario> findAllByDislivello(Double dislivello);

    /** Retrieval degli itinerari in base alla città
     * @param durata: Stringa che identifica gli itinerari utilizzando la durata
     * @return lista itinerari
     */
    @Query(value = "SELECT * FROM itinerario i WHERE i.durata = ?1", nativeQuery = true)
    List<Itinerario> findAllByDurata(Time durata);

    /** Retrieval degli itinerari in base alla città
     * @param lunghezza: Stringa che identifica gli itinerari utilizzando la lunghezza
     * @return lista itinerari
     */
    @Query(value = "SELECT * FROM itinerario i WHERE i.lunghezza = ?1", nativeQuery = true)
    List<Itinerario> findAllByLunghezza(Double lunghezza);

    /** Retrieval degli itinerari in base alla città
     * @param zonaGeografica: Stringa che identifica gli itinerari utilizzando la zona geografica
     * @return lista itinerari
     */
    @Query(value = "SELECT * FROM itinerario i WHERE i.zonageografica = ?1", nativeQuery = true)
    List<Itinerario> findAllByZonaGeografica(String zonaGeografica);
}
