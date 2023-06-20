/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.api.Server.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Classe DTO che va a mapparsi con la classe Model {@link com.natour.api.Server.model.Messaggio}
 * Ha la scopo di semplificare la mappatura del model e il database corrispettivo
*/

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessaggioDTO {
    /** Mappatura del campo con la classe model {@link com.natour.api.Server.model.Messaggio} */
    private Long idMessaggio;

    /** Mappatura del campo con la classe model {@link com.natour.api.Server.model.Messaggio} */
    private String mittente, destinatario;

    /** Mappatura del campo con la classe model {@link com.natour.api.Server.model.Messaggio} */
    private String testoMessaggio;

    /** Mappatura del campo con la classe model {@link com.natour.api.Server.model.Messaggio} */
    private String dataInvioMessaggio, oraInvioMessaggio;

    /** Mappatura del campo con la classe model {@link com.natour.api.Server.model.Messaggio} */
    private String chatRoomProprietaria;
}