/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.api.Server.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/** Classe DTO che va a mapparsi con la classe Model {@link com.natour.api.Server.model.Itinerario}
 * Ha la scopo di semplificare la mappatura del model e il database corrispettivo
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItinerarioDTO {
    /** Mappatura del campo con la classe model {@link com.natour.api.Server.model.Itinerario} */
    private String nomeItinerario, descrizione, accessoDisabili, zonaGeografica, difficolta, durata;

    /** Mappatura del campo con la classe model {@link com.natour.api.Server.model.Itinerario} */
    private String citta;

    /** Mappatura del campo con la classe model {@link com.natour.api.Server.model.Itinerario} */
    private Integer dislivello;

    /** Mappatura del campo con la classe model {@link com.natour.api.Server.model.Itinerario} */
    private Double lunghezza;

    /** Mappatura del campo con la classe model {@link com.natour.api.Server.model.Itinerario} */
    private Double puntoDiInizioLongitudine, puntoDiInizioLatitudine, puntoDiFineLongitudine, puntoDiFineLatitudine;

    /** Mappatura del campo con la classe model {@link com.natour.api.Server.model.Itinerario} */
    private String utenteProprietario;

    /** Mappatura del campo con la classe model {@link com.natour.api.Server.model.Itinerario} */
    private String urlFotoItinerario;

    /** Mappatura del campo con la classe model {@link com.natour.api.Server.model.Itinerario} */
    private String statoFotoSegnalazione;
}