/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ChatRoom implements Serializable{
    @SerializedName("idChatRoom")
    @Expose
    private String idChatRoom;

    @SerializedName("utenteA")
    @Expose
    private String utenteA;

    @SerializedName("utenteB")
    @Expose
    private String utenteB;

    // Costruttori
    public ChatRoom() { }

    // Getter
    public String getIdChatRoom() { return idChatRoom; }
    public String getUtenteA() { return utenteA; }
    public String getUtenteB() { return utenteB; }

    // Setter
    public void setIdChatRoom(String idChatRoom) { this.idChatRoom = idChatRoom; }
    public void setUtenteA(String utenteA) { this.utenteA = utenteA; }
    public void setUtenteB(String utenteB) { this.utenteB = utenteB; }
}