/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.api.Server.servicesInterface;

import com.natour.api.Server.DTO.UtenteDTO;
import com.natour.api.Server.model.Utente;
import java.util.List;
import java.util.Optional;

/** Interfaccia che ad alto livello descrive i metodi che il servizio {@link com.natour.api.Server.services.UtenteService} dovrà implementare */
public interface UtenteServiceInterface {

    /** Retrieval Lista Utenti
     * @return lista utenti totali
     */
    List<Utente> getUtenti();

    /** Retrieval singolo Utente
     * @param username: Parametro utilizzato per completare la richiesta
     * @return Singolo utente
     */
    Optional<Utente> getUtente(String username);

    /** Metodo per la creazione utente
     * @param utenteDTO: Oggetto che contiene tutti i parametri per aggiungere un nuovo utente
     */
    void aggiungiUtente(UtenteDTO utenteDTO);

    /** Metodo per l'eliminazione di un utente
     * @param username: Stringa che ha lo scopo di identificare un utente
     */
    void eliminaUtente(String username);

    /** Metodo per la modifica di un utente già esistente
     * @param utenteDTO: Oggetto che contiene tutti i parametri per aggiungere un nuovo utente
     */
    void aggiornaUtente(UtenteDTO utenteDTO);
}