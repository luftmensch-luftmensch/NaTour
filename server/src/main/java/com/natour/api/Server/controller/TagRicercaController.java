/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.api.Server.controller;

import com.natour.api.Server.DTO.TagRicercaDTO;
import com.natour.api.Server.services.TagRicercaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** Ha il compito di servire le richieste del frontend sulla rotta {{base_url}}/api/tagRicerca/
 *
*/

@RestController
@RequestMapping(path = "api/tagRicerca", produces = {"application/json"})
public class TagRicercaController {

    /** Con le notazioni @Autowired e @Qualifier (associate all'oggetto tagRicercaService)
     * eliminiamo il problema del bean che deve essere injected
     */
    @Autowired
    @Qualifier("mainTagRicercaService")
    private TagRicercaService tagRicercaService;

    /** Metodo che permette di ottenere la lista di tag totali presenti sul database
     * @return lista dei tag totali
     */
    @GetMapping(path = "TagRicercaTotali")
    @ResponseBody
    public List<TagRicercaDTO> getListaTagRicerca(){
        return this.tagRicercaService.getListaTagRicerca();

    }

    /** Metodo che permette di ottenere una lista di tag associati ad un itinerario
     * @param nomeItinerario : Stringa che permette di ricercare all'interno del database tutti i record che matchano questo parametro
     * @return lista di tag che appartengano allo stesso itinerario
     */
    @GetMapping(path = "getTagStessoItinerario/{idItinerario}")
    @ResponseBody
    public List<TagRicercaDTO> getListaTagRicercaStessoItinerario(@PathVariable("idItinerario") String nomeItinerario){
        return this.tagRicercaService.getTagStessoItinerario(nomeItinerario);
    }

    /** Metodo che permette di aggiungere un nuovo tag per uno specifico itinerario
     * @param tagRicercaDTO: Oggetto contenente tutti i parametri necessari all'aggiunta di un nuovo tag
     * @return restituisce una response per notificare il frontend del corretto inserimento
     */
    @PostMapping(path = "aggiungiTagRicerca")
    @ResponseBody
    public ResponseEntity<String> aggiungiTagRicerca(@RequestBody TagRicercaDTO tagRicercaDTO){
        boolean response = this.tagRicercaService.aggiungiTagRicerca(tagRicercaDTO);
        if (response){
            // Nel caso in cui sia andato tutto a buon fine ritorniamo lo status 201
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }

    /** Metodo che permette l'eliminazione di uno specifico tag
     * @param idTagRicerca: Parametro che permetta di identificare uno specifico tag
     */
    @DeleteMapping(path = "eliminaTagRicerca/{idTagRicerca}")
    @ResponseBody
    public void eliminaTagRicerca(@PathVariable("idTagRicerca") Long idTagRicerca){
        this.tagRicercaService.eliminaTagRicerca(idTagRicerca);
    }
}