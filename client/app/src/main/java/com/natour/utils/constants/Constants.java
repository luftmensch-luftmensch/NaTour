/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.utils.constants;

public final class Constants {
    public static final String BASE_URL = "http://192.168.1.203:9090/";
    //public static final String BASE_URL = "http://192.168.1.171:9090/";

    // NB: Sulla rete dell'UNI il traffico http viene bloccato
    //public static final String BASE_URL = "http://natour2022.ddns.net:9090/";

    public static String getApiRest(ElencoEndPoint endPoint){
        switch (endPoint){
            case CHAT_ROOM:
                return "api/chatroom/";
            case GALLERIAFOTOITINERARIO:
                return "api/galleriaFotoItinerario/";
            case ITINERARIO:
                return "api/itinerario/";
            case MESSAGGIO:
                return "api/messaggio/";
            case UTENTE:
                return "api/utente/";
            case SEGNALAZIONE_FOTO_ITINERARIO:
                return "api/segnalazioni/";
            case TAPPA:
                return "api/tappa/";
            case TAG_RICERCA:
                return "api/tagRicerca/";
            default:
                return "api/default";
        }
    }

    // Costanti per ChatSingolaAdapter
    public static final int VIEW_MESSAGGIO_INVIATO = 0;
    public static final int VIEW_MESSAGGIO_RICEVUTO = 1;

    // Costante per la selezione del caricamento dell'immagine
    public static final int RESULT_CODE_IMAGE_CHOSEN = -1;

    // Costanti per la gestione dei permessi dell'applicazione
    public static final int PERMISSION_GRANTED = 0;
    public static final int PERMISSION_DENIED = -1;
}