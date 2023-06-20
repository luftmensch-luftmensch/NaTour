/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.api.Server.controller;

import com.natour.api.Server.DTO.ChatRoomDTO;
import com.natour.api.Server.services.ChatRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** Ha il compito di servire le richieste del frontend sulla rotta {{base_url}}/api/chatroom/
 * Le request ricevono dei json
*/

@RestController
@RequestMapping(path = "api/chatroom", produces = {"application/json"})
public class ChatRoomController {
    /** Con le notazioni @Autowired e @Qualifier (associate all'oggetto chatRoomService)
     * eliminiamo il problema del bean che deve essere injected
     */
    @Autowired
    @Qualifier("mainChatRoomService")
    private ChatRoomService chatRoomService;

    /** Metodo che permette di ottenere tutte le chat room presenti sul database
     * @return Lista di chatroom
     */
    @GetMapping(path = "ChatRoomTotali")
    @ResponseBody
    public List<ChatRoomDTO> getAllChatRoomTotali(){ return this.chatRoomService.getListaChatRoomTotali(); }

    /** Metodo che permette di ottenere una lista delle chat room di uno specifico utente
     * @param utente : Stringa che permette di ricercare all'interno del database tutte le chat room che matchano la richiesta
     * @return lista di chatroom di uno stesso utente
     */
    @GetMapping(path = "ChatRoomByUtente/{idUtente}")
    @ResponseBody
    public List<ChatRoomDTO> getAllChatRoomByUtente(@PathVariable("idUtente") String utente){
        return this.chatRoomService.getListaChatRoomUtente(utente);
    }

    /** Metodo che permette di aggiungere una nuova chatroom
     * @param chatRoomDTO: Oggetto che presenta tutti i parametri necessari alla creazione di una nuova chatroom
     * @return In base allo stato della richiesta del servizio informiamo dello stato l'usufruitore della richiesta
     */
    @PostMapping(path = "aggiungiChatRoom")
    @ResponseBody
    public ResponseEntity<String> aggiungiChatRoom(@RequestBody ChatRoomDTO chatRoomDTO){
        boolean response = this.chatRoomService.creaChatRoom(chatRoomDTO);
        if(response){
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}