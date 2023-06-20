/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.api.Server.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Classe DTO che va a mapparsi con la classe Model {@link com.natour.api.Server.model.GalleriaFotoItinerario}
 * Ha la scopo di semplificare la mappatura del model e il database corrispettivo
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GalleriaFotoItinerarioDTO {
    /** Mappatura del campo con la classe model {@link com.natour.api.Server.model.GalleriaFotoItinerario} */
    private String idFotoItinerario;
    /** Mappatura del campo con la classe model {@link com.natour.api.Server.model.GalleriaFotoItinerario} */
    private String urlFotoItinerario;
    /** Mappatura del campo con la classe model {@link com.natour.api.Server.model.GalleriaFotoItinerario} */
    private String itinerarioProprietario;
}