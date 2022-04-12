/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.api.Server.services;

import com.natour.api.Server.DTO.MessaggioDTO;
import com.natour.api.Server.exceptions.ApiRequestException;
import com.natour.api.Server.model.ChatRoom;
import com.natour.api.Server.model.Messaggio;
import com.natour.api.Server.repository.ChatRoomRepository;
import com.natour.api.Server.repository.MessaggioRepository;
import com.natour.api.Server.servicesInterface.MessaggioServiceInterface;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
/** Classe che implementa l'interfaccia {@link MessaggioServiceInterface} */

@Service("mainMessaggioService")
public class MessaggioService implements MessaggioServiceInterface {
    /*
        Con la notazione @Autowired Spring permette di scoprire il bean automaticamente e
        iniettare i collaborating bean (altri bean associati)

        I bean sono gli oggetti che costituisco lo scheletro dell'applicazione.
        Sono gestiti dal Container Spirng IoC (che ha lo scopo di istanziare, configurare e assemblare gli oggetti)

        Un bean è un oggetto di cui viene istanziato, assemblato e gestito in altro modo da un Container ioC
    */

    /** Oggetto repository necessario per le request inerenti ad {@link Messaggio} */
    @Autowired
    private MessaggioRepository messaggioRepository;

    /** Oggetto repository necessario per le request inerenti ad {@link ChatRoom} */
    @Autowired
    private ChatRoomRepository chatRoomRepository;


    /** ModelMapper -> Necessaria per la conversione DTO  -> Entity ({@code convertDTOtoEntity}} e viceversa ({@code convertEntityToDTO})*/
    @Autowired
    private ModelMapper modelMapperMessaggio;

    /** Metodo per la conversione da Entity a DTO
     * @param messaggio: Oggetto da convertire
     * @return Singolo messaggioDTO (oggetto convertito)
     */
    private MessaggioDTO convertEntityToDTO(Messaggio messaggio){
        modelMapperMessaggio.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        MessaggioDTO messaggioDTO = new MessaggioDTO();
        messaggioDTO = modelMapperMessaggio.map(messaggio, MessaggioDTO.class);
        String idChatRoom = messaggio.getChatRoomProprietaria().getIdChatRoom();
        messaggioDTO.setChatRoomProprietaria(idChatRoom);
        messaggioDTO.setDataInvioMessaggio(localDateConverterToString(messaggio.getDataInvioMessaggio()));
        messaggioDTO.setOraInvioMessaggio(timeConverterToString(messaggio.getOraInvioMessaggio()));
        return messaggioDTO;
    }

    /** Metodo per la conversione da DTO a Entity
     * @param messaggioDTO: Oggetto da convertire
     * @return Singolo messaggio (oggetto convertito)
     */
    private Messaggio convertDTOtoEntity(MessaggioDTO messaggioDTO){
        modelMapperMessaggio.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        Messaggio messaggio;
        messaggio = modelMapperMessaggio.map(messaggioDTO, Messaggio.class);
        try{
            messaggio.setDataInvioMessaggio(stringConverterToLocalDate(messaggioDTO.getDataInvioMessaggio()));
            messaggio.setOraInvioMessaggio(stringConverterToTime(messaggioDTO.getOraInvioMessaggio()));
        }catch (ParseException e){
            throw new ApiRequestException("Formato durata non valido", HttpStatus.BAD_REQUEST);
        }
        ChatRoom chatRoom = chatRoomRepository.getById(messaggioDTO.getChatRoomProprietaria());
        messaggio.setChatRoomProprietaria(chatRoom);
        return messaggio;
    }

    /** Metodo ausiliario per la conversione
     * @param durata: String
     * @return Time
     * @throws ParseException In caso di errore lancia una eccezione che viene gestita nei metodi che ne fanno uso
     */
    public Time stringConverterToTime(String durata) throws ParseException {
        DateFormat formatter =  new SimpleDateFormat("HH:mm:ss");
        return new Time(formatter.parse(durata).getTime());
    }


    /** Metodo ausiliario per la conversione
     * @param time: Time
     * @return Stringa
     */
    public String timeConverterToString(Time time) { return time.toString(); }

    /** Metodo ausiliario per la conversione
     * @param giorno: Stringa
     * @return LocalDate
     * @throws  ParseException In caso di errore lancia una eccezione che viene gestita nei metodi che ne fanno uso
     */
    public LocalDate stringConverterToLocalDate(String giorno) throws ParseException{
        return LocalDate.parse(giorno, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }

    /** Metodo ausiliario per la conversione
     * @param localDate: LocaDate
     * @return String
     */
    public String localDateConverterToString(LocalDate localDate){ return localDate.toString(); }


    /** Metodo per ottenere la lista di messaggi appartenti alla stessa chat room
     * @param idChatRoom: Identificativo della chat room corrispondente
     * @return lista messaggi
     */
    @Override
    public List<MessaggioDTO> getListaMessaggiStessaChatRoom(String idChatRoom) {
        List<Messaggio> messaggios = this.messaggioRepository.getMessaggiStessaChatRoom(idChatRoom);
        List<MessaggioDTO> messaggioDTOS = new ArrayList<>();
        for(Messaggio m: messaggios){
            messaggioDTOS.add(convertEntityToDTO(m));
        }
        return messaggioDTOS;
    }

    /** Implementazione del metodo per l'aggiunta di un nuovo messaggio nel database
     * @param messaggioDTO: Oggetto che contiene tutti i parametri necessari alla creazione di un nuovo messaggio
     * @throws ApiRequestException In caso di errore lancia una eccezione che viene gestita nei metodi che ne fanno uso
     */
    @Override
    public boolean creaNuovoMessaggio(MessaggioDTO messaggioDTO) throws ApiRequestException{
        Messaggio messaggio = convertDTOtoEntity(messaggioDTO);
        if(this.messaggioRepository.existsById(messaggio.getIdMessaggio())){
            return false;
        }
        this.messaggioRepository.save(messaggio);
        return true;
    }
}