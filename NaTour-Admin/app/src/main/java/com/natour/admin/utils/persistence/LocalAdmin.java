/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.admin.utils.persistence;

import androidx.annotation.NonNull;

public class LocalAdmin {
    private String id, username, urlFotoProfilo;

    // Costruttori
    public LocalAdmin() { }
    public LocalAdmin(String id, String username, String urlFotoProfilo) {
        this.id = id;
        this.username = username;
        this.urlFotoProfilo = urlFotoProfilo;
    }
    // Getter
    public String getId() { return id; }
    public String getUsername() { return username; }
    public String getUrlFotoProfilo() { return urlFotoProfilo; }
    // Setter
    public void setId(String id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setUrlFotoProfilo(String urlFotoProfilo) { this.urlFotoProfilo = urlFotoProfilo; }

    // Per il logging
    @NonNull
    @Override
    public String toString() {
        return "LocalAdmin{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", urlFotoProfilo='" + urlFotoProfilo + '\'' +
                '}';
    }
}