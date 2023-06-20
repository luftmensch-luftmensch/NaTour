/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.api.Server.servicesInterface;

import com.natour.api.Server.DTO.AdminDTO;
import com.natour.api.Server.model.Admin;
import java.util.List;

/** Interfaccia che ad alto livello descrive i metodi che il servizio {@link com.natour.api.Server.services.AdminService} dovrà implementare */
public interface AdminServiceInterface {

    /** Retrieval Lista Admin
     * @return lista admin totali
     */
    List<AdminDTO> getAdmins();

    /** Retrieval singolo admin
     * @param username: Parametro utilizzato per completare la richiesta
     * @return Singolo admin
     */
    AdminDTO getAdmin(String username);

    /** Metodo per la creazione di un admin
     * @param adminDTO: Oggetto che contiene tutti i parametri per aggiungere un nuovo admin
     */
    void aggiungiAdmin(AdminDTO adminDTO);

    /** Metodo per l'eliminazione di un admin
     * @param username: Stringa che ha lo scopo di identificare un utente
     */
    void eliminaAdmin(String username);

    /** Metodo per la modifica di un admin già esistente
     * @param adminDTO: Oggetto che contiene tutti i parametri per aggiungere un nuovo admin
     */
    void aggiornaAdmin(AdminDTO adminDTO);

    /** Metodo per ottenere i record dei dati necessari all'admin per visualizzare le statistiche dell'applicazione mobile
     * @return lista di interi (da utilizzare all'interno del grafico)
     */
    List<Integer> getRecordTotali();

}