/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.api.Server.controller;

import com.natour.api.Server.DTO.UtenteDTO;
import com.natour.api.Server.model.Utente;
import com.natour.api.Server.services.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

/** Ha il compito di servire le richieste del frontend sulla rotta {{base_url}}/api/utente
 * Le request ricevono dei json
*/

@RestController
@RequestMapping(path = "api/utente", produces = {"application/json"})
public class UtenteController {
    /** Con le notazioni @Autowired e @Qualifier (associate all'oggetto utenteService)
     * eliminiamo il problema del bean che deve essere injected
     */
    @Autowired
    @Qualifier("mainUtenteService")
    private UtenteService utenteService;

    /** Metodo che permette di ottenere la lista di tutti gli utenti presenti sul database
     * @return lista degli utenti totali presenti
     */
    @GetMapping(path = "UtentiTotali")
    @ResponseBody
    public List<Utente> getUtentiTotali(){ return  this.utenteService.getUtenti(); }

    /** Metodo che permetta di ottenere le informazioni di un singolo utente
     * @param nomeUtente: Stringa che permetta di identificare l'utente di cui si voglia avere le informazioni
     * @return Informazioni dell'utente (suoi campi)
     */
    @GetMapping(path = "getUtente/{utenteID}")
    @ResponseBody
    public Optional<Utente> ricercaUtente(@PathVariable("utenteID") String nomeUtente){
        return this.utenteService.getUtente(nomeUtente);
    }

    /** Metodo che permetta l'aggiunta di un nuovo utente sul database
     * @param utenteDTO: Oggetto che contiene tutti i parametri necessari all'aggiunta dell'utente sul database
     */
    @PostMapping(path = "aggiungiUtente")
    @ResponseBody
    public void aggiungiUtente(@RequestBody UtenteDTO utenteDTO){
        this.utenteService.aggiungiUtente(utenteDTO);
    }

    /** Metodo che permetta di eliminare uno specifico utente
     * @param username: Stringa che permette di identificare l'utente che si vuole eliminare
     */
    @DeleteMapping(path = "eliminaUtente/{utenteID}")
    public void eliminaUtente(@PathVariable("utenteID") String username){
        utenteService.eliminaUtente(username);
    }

    /** Metodo che permetta di aggiornare i dati di uno specifico utente
     * @param utenteDaAggiornare: Oggetto che contiene tutti i parametri necessari alla modifica dell'utente sul database
    */
    @PutMapping(path = "aggiornaUtente")
    @ResponseBody
    public void aggiornaUtente(@RequestBody UtenteDTO utenteDaAggiornare){
        utenteService.aggiornaUtente(utenteDaAggiornare);
    }
}