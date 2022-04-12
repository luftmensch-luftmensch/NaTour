/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Tappa implements Serializable {

    // NB: Va passato il valore a -1 per renderlo automatico
    @SerializedName("idTracciatoGeografico")
    @Expose
    private Long idTracciatoGeografico;

    @SerializedName("longitudine")
    @Expose
    private Double longitudine;

    @SerializedName("latitudine")
    @Expose
    private Double latitudine;

    @SerializedName("itinerarioCorrispondente")
    private String itinerarioCorrispondente;

    // Construttori
    public Tappa() { }
    public Tappa(Long idTracciatoGeografico, Double longitudine,
                 Double latitudine, String itinerarioCorrispondente) {
        this.idTracciatoGeografico = idTracciatoGeografico;
        this.longitudine = longitudine;
        this.latitudine = latitudine;
        this.itinerarioCorrispondente = itinerarioCorrispondente;
    }

    // Getter
    public Long getIdTracciatoGeografico() { return idTracciatoGeografico; }
    public Double getLongitudine() { return longitudine; }
    public Double getLatitudine() { return latitudine; }
    public String getItinerarioCorrispondente() { return itinerarioCorrispondente; }

    // Setter
    public void setIdTracciatoGeografico(Long idTracciatoGeografico) { this.idTracciatoGeografico = idTracciatoGeografico; }
    public void setLongitudine(Double longitudine) { this.longitudine = longitudine; }
    public void setLatitudine(Double latitudine) { this.latitudine = latitudine; }
    public void setItinerarioCorrispondente(String itinerarioCorrispondente) { this.itinerarioCorrispondente = itinerarioCorrispondente; }
}
