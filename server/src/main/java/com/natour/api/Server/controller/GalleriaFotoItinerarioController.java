/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.api.Server.controller;

import com.natour.api.Server.DTO.GalleriaFotoItinerarioDTO;
import com.natour.api.Server.services.GalleriaFotoItinerarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** Ha il compito di servire le richieste del frontend sulla rotta {{base_url}}/api/galleriaFotoItinerario
 * Le request effettuate ricevono dei json
*/

@RestController
@RequestMapping(path = "api/galleriaFotoItinerario", produces = {"application/json"})
public class GalleriaFotoItinerarioController {

    /** Con le notazioni @Autowired e @Qualifier (associate all'oggetto galleriaFotoItinerarioService)
     * eliminiamo il problema del bean che deve essere injected
     */
    @Autowired
    @Qualifier("mainGalleriaFotoItinerarioService")
    private GalleriaFotoItinerarioService galleriaFotoItinerarioService;

    /** Metodo che permette di ottenere la lista di tutte le foto associate agli itinerari sul database
     * @return lista di foto
     */
    @GetMapping(path = "GalleriaFotoItinerarioTotali")
    @ResponseBody
    public List<GalleriaFotoItinerarioDTO> getAllFotoItinerari(){
        return  this.galleriaFotoItinerarioService.getListaGalleriaFotoItinerari();
    }

    /** Metodo che permette di ottenere la lista di tutte le foto associate ad un determinato itinerario
     * @param idItinerario: Stringa che identifica l'itinerario di cui vogliamo ottenere le foto
     * @return lista di foto di uno stesso itinerario
     */
    @GetMapping(path = "getFotoStessoItinerario/{idItinerario}")
    @ResponseBody
    public List<GalleriaFotoItinerarioDTO> getFotoStessoItinerario(@PathVariable("idItinerario") String idItinerario){
        return  this.galleriaFotoItinerarioService.getFotoStessoItinerario(idItinerario);
    }

    /** Metodo che permette l'aggiunta di una nuova foto associata ad un itinerario
     * @param itinerarioFotoDTO: Oggetto che contiene tutti i parametri necessari all'aggiunta di una nuova foto
     * @return lista degli itinerari
     */
    @PostMapping(path = "aggiungiFotoItinerario")
    @ResponseBody
    public ResponseEntity<String> aggiungiFotoItinerario(@RequestBody GalleriaFotoItinerarioDTO itinerarioFotoDTO){
        boolean response = this.galleriaFotoItinerarioService.aggiungiFotoItinerario(itinerarioFotoDTO);
        if (response){
            // Nel caso in cui sia andato tutto a buon fine ritorniamo lo status 201
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /** Metodo che permette l'eliminazione di una specifica foto
     * @param idFotoItinerario: Parametro che identifica la foto da eliminare
    */
    @DeleteMapping(path = "eliminaItinerarioFoto/{idFotoItinerario}")
    @ResponseBody
    public void eliminaFotoItinerario(@PathVariable("idFotoItinerario") String idFotoItinerario){
        this.galleriaFotoItinerarioService.eliminaItinerario(idFotoItinerario);
    }
}
