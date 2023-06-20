/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.api.Server.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/** Classe DTO che va a mapparsi con la classe Model {@link com.natour.api.Server.model.SegnalazioneFotoItinerario}
 *  Ha lo scopo di semplificare la mappatura dei model e il database corrispettivo
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SegnalazioneFotoItinerarioDTO {

    /** Mappatura del campo con la classe model {@link com.natour.api.Server.model.SegnalazioneFotoItinerario} */
    private Long idSegnalazione;

    /** Mappatura del campo con la classe model {@link com.natour.api.Server.model.SegnalazioneFotoItinerario} */
    private String utenteSegnalatore;

    /** Mappatura del campo con la classe model {@link com.natour.api.Server.model.SegnalazioneFotoItinerario} */
    private String galleriaFotoItinerarioCorrispondente;
}