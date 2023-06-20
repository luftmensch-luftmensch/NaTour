/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.api.Server.controller;

import com.natour.api.Server.DTO.MessaggioDTO;
import com.natour.api.Server.services.MessaggioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/** Ha il compito di servire le richieste del frontend sulla rotta {{base_url}}/api/messaggio
 * Le request ricevono dei json
*/

@RestController
@RequestMapping(path = "api/messaggio", produces = {"application/json"})
public class MessaggioController {

    /** Con le notazioni @Autowired e @Qualifier (associate all'oggetto messaggioService)
     * eliminiamo il problema del bean che deve essere injected
     */
    @Autowired
    @Qualifier("mainMessaggioService")
    private MessaggioService messaggioService;

    /** Metodo che permette di ottenere la lista dei messaggi associati ad una chat room
     * @param idChatRoom: Stringa che identifica la chat room a cui si vuole fare riferimento
     * @return Lista di messaggi
     */
    @GetMapping(path = "getMessaggiByChatRoom/{idChatRoom}")
    @ResponseBody
    public List<MessaggioDTO> getMessaggiByChatRoom(@PathVariable("idChatRoom") String idChatRoom){
        return this.messaggioService.getListaMessaggiStessaChatRoom(idChatRoom);
    }

    /** Metodo che permette di creare un nuovo messaggio
     * @param messaggioDTO: Oggetto che contiene tutti i parametri necessari all'inserimento di un nuovo messaggio
     * @return Response che permetta di informare il frontend della corretta creazione di un nuovo messaggio sul database
     */
    @PostMapping(path = "aggiungiMessaggio")
    @ResponseBody
    public ResponseEntity<String> creaNuovoMessaggio(@RequestBody MessaggioDTO messaggioDTO){
        boolean response = this.messaggioService.creaNuovoMessaggio(messaggioDTO);
        if (response){
            // Nel caso in cui sia andato tutto a buon fine ritorniamo lo status 201
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}