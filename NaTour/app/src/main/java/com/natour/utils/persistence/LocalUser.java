/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.utils.persistence;


import androidx.annotation.NonNull;

public class LocalUser {
    private int id;
    private String username, urlFotoProfilo, isLoggedWithGoogle;

    // Costruttori
    public LocalUser() { }

    public LocalUser(int id, String username, String urlFotoProfilo, String isLoggedWithGoogle) { this.id = id; this.username = username; this.urlFotoProfilo = urlFotoProfilo; this.isLoggedWithGoogle = isLoggedWithGoogle; }

    // Getter
    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getUrlFotoProfilo() { return urlFotoProfilo; }
    public String getIsLoggedWithGoogle() { return isLoggedWithGoogle; }

    // Setter
    public void setId(int id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setUrlFotoProfilo(String urlFotoProfilo) { this.urlFotoProfilo = urlFotoProfilo; }
    public void setIsLoggedWithGoogle(String isLoggedWithGoogle) { this.isLoggedWithGoogle = isLoggedWithGoogle; }

    // Per il logging
    @NonNull
    @Override
    public String toString() {
        return "LocalUser{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", urlFotoProfilo='" + urlFotoProfilo + '\'' +
                ", isLoggedWithGoogle='" + isLoggedWithGoogle + '\'' +
                '}';
    }
}
