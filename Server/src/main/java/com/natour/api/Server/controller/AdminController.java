/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.api.Server.controller;

import com.natour.api.Server.DTO.AdminDTO;
import com.natour.api.Server.services.AdminService;
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

/** Ha il compito di servire le richieste del frontend sulla rotta {{base_url}}/api/itinerario
 * Le request ricevono dei json
 */

@RestController
@RequestMapping(path = "api/admin", produces = {"application/json"})
public class AdminController {
    /** Con le notazioni @Autowired e @Qualifier (associate all'oggetto itinerarioService)
     * eliminiamo il problema del bean che deve essere injected
     */
    @Autowired
    @Qualifier("mainAdminService")
    private AdminService adminService;

    /** Metodo che permette di ottenere la lista di tutti gli admin presenti sul database
     * @return lista degli admin totali presenti
     */
    @GetMapping(path = "AdminTotali")
    @ResponseBody
    public List<AdminDTO> getAdminTotali(){ return  this.adminService.getAdmins(); }

    /** Metodo che permetta di ottenere le informazioni di un singolo admin
     * @param nomeUtente: Stringa che permetta di identificare l'admin di cui si voglia avere le informazioni
     * @return Informazioni dell'admin (suoi campi)
     */
    @GetMapping(path = "getAdmin/{adminID}")
    @ResponseBody
    public AdminDTO ricercaAdmin(@PathVariable("adminID") String nomeUtente){
        return this.adminService.getAdmin(nomeUtente);
    }

    /** Metodo per ottenere i record dei dati necessari all'admin per visualizzare le statistiche dell'applicazione mobile
     * @return lista di interi (da utilizzare all'interno del grafico)
     */
    @GetMapping(path = "getStatistiche")
    @ResponseBody
    public List<Integer> getStatistiche(){
        return this.adminService.getRecordTotali();
    }

    /** Metodo che permetta l'aggiunta di un nuovo admin sul database
     * @param adminDTO: Oggetto che contiene tutti i parametri necessari all'aggiunta dell'admin sul database
     */
    @PostMapping(path = "aggiungiAdmin")
    @ResponseBody
    public void aggiungiUtente(@RequestBody AdminDTO adminDTO){
        this.adminService.aggiungiAdmin(adminDTO);
    }

    /** Metodo che permetta di eliminare uno specifico admin
     * @param username: Stringa che permette di identificare l'admin che si vuole eliminare
     */
    @DeleteMapping(path = "eliminaAdmin/{adminID}")
    public void eliminaUtente(@PathVariable("adminID") String username){
        adminService.eliminaAdmin(username);
    }

    /** Metodo che permetta di aggiornare i dati di uno specifico admin
     * @param adminDaAggiornare: Oggetto che contiene tutti i parametri necessari alla modifica dell'admin sul database
     */
    @PutMapping(path = "aggiornaAdmin")
    @ResponseBody
    public void aggiornaUtente(@RequestBody AdminDTO adminDaAggiornare){
        adminService.aggiornaAdmin(adminDaAggiornare);
    }
}