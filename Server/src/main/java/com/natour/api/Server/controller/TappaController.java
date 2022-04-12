/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.api.Server.controller;

import com.natour.api.Server.DTO.TappaDTO;
import com.natour.api.Server.services.TappaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** Ha il compito di servire le richieste del frontend sulla rotta {{base_url}}/api/tappa
 * Le request ricevono dei json
*/

@RestController
@RequestMapping(path = "api/tappa", produces = {"application/json"})
public class TappaController {

    /** Con le notazioni @Autowired e @Qualifier (associate all'oggetto tappaService)
     * eliminiamo il problema del bean che deve essere injected
    */
    @Autowired
    @Qualifier("mainTappaService")
    private TappaService tappaService;

    /** Metodo che permette di ottenere la lista di tutte le tappe totali all'interno del database
     * @return lista delle tappe totali
     */
    @GetMapping(path = "TappeTotali")
    @ResponseBody
    public List<TappaDTO> getAllTappe(){
        return this.tappaService.getListaTappe();
    }

    /** Metodo che permette di ottenere la lista di tutte le tappe totali all'interno del database di uno specifico itinerario
     * @param nomeItinerario: Stringa che permetta di identificare tutti i record che matchano la richiesta
     * @return lista delle tappe totali di uno specifico itinerario
     */
    @GetMapping(path = "getTappeStessoItinerario/{nomeItinerario}")
    @ResponseBody
    public List<TappaDTO> getTappeStessoItinerario(@PathVariable("nomeItinerario") String nomeItinerario){
        return this.tappaService.getTappeStessoItinerario(nomeItinerario);
    }

    /** Metodo che permetta l'aggiunta di una nuova tappa nel database
     * @param tappaDTO: Oggetto che contiene tutti i parametri necessari all'inserimento di una nuova tappa
     */
    @PostMapping(path = "aggiungiTappa")
    @ResponseBody
    public void aggiungiTappa(@RequestBody TappaDTO tappaDTO){
        this.tappaService.aggiungiTappa(tappaDTO);
    }

    /** Metodo che permetta l'eliminazione di una determinata tappa dal database
     *
     * @param idTappa: Parametro che identifichi la tappa da dover eliminare
     */
    @DeleteMapping(path = "eliminaTappa/{idTappa}")
    @ResponseBody
    public void eliminaTappa(@PathVariable("idTappa") Long idTappa){
        this.tappaService.eliminaTappa(idTappa);
    }
}