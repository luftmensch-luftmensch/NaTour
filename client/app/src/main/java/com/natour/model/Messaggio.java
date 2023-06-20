/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Messaggio implements Serializable {
    // NB: Va passato il valore a -1 per renderlo automatico
    @SerializedName("idMessaggio")
    @Expose
    private Long idMessaggio;

    @SerializedName("mittente")
    @Expose
    private String mittente;

    @SerializedName("destinatario")
    @Expose
    private String destinatario;

    @SerializedName("testoMessaggio")
    @Expose
    private String testoMessaggio;

    @SerializedName("dataInvioMessaggio")
    @Expose
    private String dataInvioMessaggio;

    @SerializedName("oraInvioMessaggio")
    @Expose
    private String oraInvioMessaggio;

    @SerializedName("chatRoomProprietaria")
    @Expose
    private String chatRoomProprietaria;

    public Messaggio() { }

    // Getter
    public Long getIdMessaggio() { return idMessaggio; }
    public String getMittente() { return mittente; }
    public String getDestinatario() { return destinatario; }
    public String getTestoMessaggio() { return testoMessaggio; }
    public String getDataInvioMessaggio() { return dataInvioMessaggio; }
    public String getOraInvioMessaggio() { return oraInvioMessaggio; }
    public String getChatRoomProprietaria() { return chatRoomProprietaria; }

    // Setter
    public void setIdMessaggio(Long idMessaggio) { this.idMessaggio = idMessaggio; }
    public void setMittente(String mittente) { this.mittente = mittente; }
    public void setDestinatario(String destinatario) { this.destinatario = destinatario; }
    public void setTestoMessaggio(String testoMessaggio) { this.testoMessaggio = testoMessaggio; }
    public void setDataInvioMessaggio(String dataInvioMessaggio) { this.dataInvioMessaggio = dataInvioMessaggio; }
    public void setOraInvioMessaggio(String oraInvioMessaggio) { this.oraInvioMessaggio = oraInvioMessaggio; }
    public void setChatRoomProprietaria(String chatRoomProprietaria) { this.chatRoomProprietaria = chatRoomProprietaria; }
}