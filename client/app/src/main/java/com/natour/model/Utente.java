/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Utente implements Serializable {
    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("urlFotoProfilo")
    @Expose
    private String urlFotoProfilo;

    // Construttori
    public Utente() { }
    public Utente(String username, String urlFotoProfilo) {
        this.username = username;
        this.urlFotoProfilo = urlFotoProfilo;
    }

    // Getter
    public String getUsername() { return username; }
    public String getUrlFotoProfilo() { return urlFotoProfilo; }

    // Setter
    public void setUsername(String username) { this.username = username; }
    public void setUrlFotoProfilo(String urlFotoProfilo) { this.urlFotoProfilo = urlFotoProfilo; }
}