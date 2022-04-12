/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.admin.utils.constants;

public final class Constants {
    public static final String BASE_URL = "http://192.168.1.171:9090/";
    //public static final String BASE_URL = "http://192.168.1.203:9090/";

    // NB: Sulla rete dell'UNI il traffico http viene bloccato
    //public static final String BASE_URL = "http://natour2022.ddns.net:9090/";

    public static String getApiRest(ElencoEndPoint endPoint){
        switch (endPoint){
            case ADMIN:
                return "api/admin/";
            case SEGNALAZIONE_FOTO_ITINERARIO:
                return "api/segnalazioni/";
            default:
                return "api/default/";
        }
    }

    // Costante per la selezione del caricamento dell'immagine
    public static final int RESULT_CODE_IMAGE_CHOSEN = -1;
}