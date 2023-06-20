/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.api.Server.controller;

import com.natour.api.Server.DTO.SegnalazioneFotoItinerarioDTO;
import com.natour.api.Server.services.SegnalazioneFotoItinerarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/** Ha il compito di servire le richieste del frontend sulla rotta {{base_url}}/api/segnalazioni/
 * Le request ricevono dei json
*/

@RestController
@RequestMapping(path = "api/segnalazioni", produces = {"application/json"})
public class SegnalazioneFotoItinerarioController {
    /** Con le notazioni @Autowired e @Qualifier (associate all'oggetto segnalazioneFotoItinerarioService)
     * eliminiamo il problema del bean che deve essere injected
     */
    @Autowired
    @Qualifier("mainSegnalazioneFotoItinerarioService")
    private SegnalazioneFotoItinerarioService segnalazioneFotoItinerarioService;

    /** Metodo che permette di ottenere la lista delle segnalazioni totali relative ad una specifica foto sul database
     * @param idFoto: Parametro necessario per completare la richiesta
     * @return lista delle segnalazioni relative ad una specifica foto
     */
    @GetMapping(path = "getSegnalazioniByFoto/{idFoto}")
    @ResponseBody
    public List<SegnalazioneFotoItinerarioDTO> getAllSegnalazioniByFoto(@PathVariable("idFoto") String idFoto){
        return this.segnalazioneFotoItinerarioService.getListaSegnalazioniStessaFoto(idFoto);
    }

    /** Metodo che permetta l'aggiunta di una nuova segnalazione
     * @param segnalazioneFotoItinerarioDTO: Oggetto che contiene tutte le informazioni necessarie all'aggiutna di una nuova segnalazione
     * @return Response che informi il frontend del corretto inserimento della segnalazione
     */
    @PostMapping(path = "aggiungiSegnalazione")
    @ResponseBody
    public ResponseEntity<String> creaNuovaSegnalazione(@RequestBody SegnalazioneFotoItinerarioDTO segnalazioneFotoItinerarioDTO){
        boolean response = this.segnalazioneFotoItinerarioService.aggiungiSegnalazione(segnalazioneFotoItinerarioDTO);
        if (response){
            // Nel caso in cui sia andato tutto a buon fine ritorniamo lo status 201
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }
}