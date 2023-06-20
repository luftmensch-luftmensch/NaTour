/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.api.Server.servicesInterface;

import com.natour.api.Server.DTO.ItinerarioDTO;
import java.util.List;

/** Interfaccia che ad alto livello descrive i metodi che il servizio {@link com.natour.api.Server.services.ItinerarioService} dovrà implementare */
public interface ItinerarioServiceInterface {
    /** Retrieval lista itinerari totali
     * @return lista itinerari totali
     */
    List<ItinerarioDTO> getItinerari();

    /** Retrieval singolo Itinerario
     * @param nomeItinerario: Identificato dell'itinerario di cui si vuole ottenere informazioni
     * @return Singolo itinerario
     */
    ItinerarioDTO getSingoloItinerario(String nomeItinerario);

    /** Retrieval lista itinerari con lo stesso utente proprietario
     * @param utente: Identificativo per la ricerca degli utenti con lo stesso utente proprietario
     * @return lista itinerari
     */
    List<ItinerarioDTO> getItinerariStessoUtente(String utente);

    /** Retrieval lista itinerari dal più recente al meno recente
     * @return lista itinerari
    */
    List<ItinerarioDTO> ricercaPerRecenti();

    /** Retrieval lista itinerari che matchino un nome itinerario specifico
     * @param nomeItinerario: Identificato dell'itinerario di cui si vuole ottenere informazioni
     * @return Lista itinerari
     */
    List<ItinerarioDTO> ricercaPerNomeItinerario(String nomeItinerario);

    /** Retrieval lista itinerari con lo stesso utente proprietario
     * @param citta: Identificativo per la ricerca degli utenti con la stessa città
     * @return lista itinerari
     */
    List<ItinerarioDTO> ricercaByCitta(String citta);

    /** Retrieval lista itinerari con lo stesso utente proprietario
     * @param difficolta: Identificativo per la ricerca degli utenti con la stessa difficoltà
     * @return lista itinerari
     */
    List<ItinerarioDTO> ricercaByDifficolta(String difficolta);

    /** Retrieval lista itinerari con lo stesso utente proprietario
     * @param dislivello: Identificativo per la ricerca degli utenti con lo stesso dislivello
     * @return lista itinerari
     */
    List<ItinerarioDTO> ricercaByDislivello(Double dislivello);

    /** Retrieval lista itinerari con lo stesso utente proprietario
     * @param durata: Identificativo per la ricerca degli utenti con la stessa durata
     * @return lista itinerari
     */
    List<ItinerarioDTO> ricercaByDurata(String durata);

    /** Retrieval lista itinerari con lo stesso utente proprietario
     * @param lunghezza: Identificativo per la ricerca degli utenti con la stessa lunghezza
     * @return lista itinerari
    */
    List<ItinerarioDTO> ricercaByLunghezza(Double lunghezza);

    /** Retrieval lista itinerari con lo stesso utente proprietario
     * @param zonaGeografica: Identificativo per la ricerca degli utenti con la stessa zona geografia
     * @return lista itinerari
     */
    List<ItinerarioDTO> ricercaByZonaGeografica(String zonaGeografica);

    /** Metodo per la creazione di un nuovo itinerario
     * @param itinerarioDTO: Oggetto che contiene tutti i parametri necessari alla creazione di un nuovo itinerario
      * @return Valore booleano che ci informi della response della richiesta
     */
    boolean aggiungiItinerario(ItinerarioDTO itinerarioDTO);

    /** Metodo per l'eliminazione di un itinerario esistente dato il suo nome
     * @param nomeItinerario: Identificativo necessario all'eliminazione
     */
    void eliminaItinerario(String nomeItinerario);

    /** Modifica Itinerario esistente
     * @param itinerarioDTO: Identificativo necessario alla modifica di un itinerario esistente
     */
    void aggiornaItinerario(ItinerarioDTO itinerarioDTO);
}