/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.api.Server.services;

import com.natour.api.Server.DTO.ChatRoomDTO;
import com.natour.api.Server.exceptions.ApiRequestException;
import com.natour.api.Server.model.ChatRoom;
import com.natour.api.Server.repository.ChatRoomRepository;
import com.natour.api.Server.servicesInterface.ChatRoomServiceInterface;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
/** Classe che implementa l'interfaccia {@link ChatRoomServiceInterface} */
@Service("mainChatRoomService")
public class ChatRoomService implements ChatRoomServiceInterface {

    /*
        Con la notazione @Autowired Spring permette di scoprire il bean automaticamente e
        iniettare i collaborating bean (altri bean associati)

        I bean sono gli oggetti che costituisco lo scheletro dell'applicazione.
        Sono gestiti dal Container Spirng IoC (che ha lo scopo di istanziare, configurare e assemblare gli oggetti)

        Un bean è un oggetto di cui viene istanziato, assemblato e gestito in altro modo da un Container ioC
    */

    /** Oggetto repository necessario per le request inerenti ad {@link ChatRoom} */
    @Autowired
    private ChatRoomRepository chatRoomRepository;

    /** ModelMapper -> Necessaria per la conversione DTO  -> Entity ({@code convertDTOtoEntity}} e viceversa ({@code convertEntityToDTO})*/
    @Autowired
    private ModelMapper modelMapperChatRoom;

    /** Metodo per la conversione da DTO a Entity
     * @param chatRoomDTO: parametro da convertire
     * @return Singola chat room (oggetto convertito)
     */
    private ChatRoom convertDTOtoEntity(ChatRoomDTO chatRoomDTO){
        modelMapperChatRoom.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        ChatRoom chatRoom;
        chatRoom = modelMapperChatRoom.map(chatRoomDTO, ChatRoom.class);
        return chatRoom;
    }

    /** Metodo per la conversione da DTO a Entity
     * @param chatRoom: parametro da convertire
     * @return Singola chatroomDTO (oggetto convertito)
     */
    private ChatRoomDTO convertEntityToDTO(ChatRoom chatRoom){
        modelMapperChatRoom.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);

        ChatRoomDTO chatRoomDTO = new ChatRoomDTO();
        chatRoomDTO = modelMapperChatRoom.map(chatRoom, ChatRoomDTO.class);
        return chatRoomDTO;
    }

    /** Metodo per ottenere la lista delle chat room totali presenti sul database
     * @return lista chatroom
     */
    @Override
    public List<ChatRoomDTO> getListaChatRoomTotali() {
        List<ChatRoom> chatRooms = this.chatRoomRepository.findAll();
        List<ChatRoomDTO> chatRoomDTOS = new ArrayList<>();
        for(ChatRoom c: chatRooms){
            chatRoomDTOS.add(convertEntityToDTO(c));
        }
        return chatRoomDTOS;
    }

    /** Metodo per ottenere la lista delle chat room appartenti ad un singolo utente
     * @param utente: Identificativo dell'utente
     * @return lista chat room inerenti ad uno stesso utente
     */
    @Override
    public List<ChatRoomDTO> getListaChatRoomUtente(String utente) {
        List<ChatRoom> chatRooms = this.chatRoomRepository.findAllByUtente(utente);
        List<ChatRoomDTO> chatRoomDTOS = new ArrayList<>();
        for(ChatRoom c: chatRooms){
            chatRoomDTOS.add(convertEntityToDTO(c));
        }
        return chatRoomDTOS;
    }

    // TODO Aggiungere un controllo migliore sugli altri campi della chat Room in modo da porter rendere autogenerato l'id della chat

    /** Implementazione del metodo per l'aggiunta di una nuova chat room nel database
     * @param chatRoomDTO: Oggetto che contiene tutti i parametri necessari alla creazione di una nuova chat room
     */
    @Override
    public boolean creaChatRoom(ChatRoomDTO chatRoomDTO) {
        ChatRoom chatRoom = convertDTOtoEntity(chatRoomDTO);
        if(this.chatRoomRepository.existsById(chatRoom.getIdChatRoom())){
            //throw new ApiRequestException("Chat già esistente", HttpStatus.BAD_REQUEST);
            return false;
        }
        this.chatRoomRepository.save(chatRoom);
        return true;
    }

    // TODO: Convertire il metodo in modo che accetti direttamente una stringa?

    /** Implementazione del metodo per l'eliminazione di una chat room esistente
     * @param chatRoomDTO: Oggetto necessario all'ìdentificazione della chat room da eliminare
     * @throws ApiRequestException In caso di errore lancia una eccezione che viene gestita nei metodi che ne fanno uso
     */
    @Override
    public void eliminazioneChatRoom(ChatRoomDTO chatRoomDTO) throws ApiRequestException{
        boolean ricercaChatRoom = chatRoomRepository.existsById(chatRoomDTO.getIdChatRoom());
        if(!ricercaChatRoom){
            throw new ApiRequestException("Chat Room non trovata!", HttpStatus.BAD_REQUEST);
        }
        chatRoomRepository.deleteById(chatRoomDTO.getIdChatRoom());
    }
}