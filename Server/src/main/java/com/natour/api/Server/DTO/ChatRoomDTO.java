/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.api.Server.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Classe DTO che va a mapparsi con la classe Model {@link com.natour.api.Server.model.ChatRoom}
 * Ha la scopo di semplificare la mappatura del model e il database corrispettivo
*/

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomDTO {
    /** Mappatura del campo con la classe model {@link com.natour.api.Server.model.ChatRoom} */
    private String idChatRoom;
    /** Mappatura del campo con la classe model {@link com.natour.api.Server.model.ChatRoom} */
    private String utenteA, utenteB;
}
