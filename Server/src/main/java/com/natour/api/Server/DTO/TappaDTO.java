/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.api.Server.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Classe DTO che va a mapparsi con la classe Model {@link com.natour.api.Server.model.Tappa}
 *  Ha lo scopo di semplificare la mappatura dei model e il database corrispettivo
*/

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TappaDTO {
    /** Mappatura del campo con la classe model {@link com.natour.api.Server.model.Tappa} */
    private Long idTappa;

    /** Mappatura del campo con la classe model {@link com.natour.api.Server.model.Tappa} */
    private Double longitudine, latitudine;

    /** Mappatura del campo con la classe model {@link com.natour.api.Server.model.Tappa} */
    private String itinerarioCorrispondente;
}
