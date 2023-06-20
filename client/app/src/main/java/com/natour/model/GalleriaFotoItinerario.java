/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GalleriaFotoItinerario implements Serializable {
    @SerializedName("idFotoItinerario")
    @Expose
    private String idFotoItinerario;

    @SerializedName("urlFotoItinerario")
    @Expose
    private String urlFotoItinerario;

    // Mappatura con l'itinerario
    @SerializedName("itinerarioProprietario")
    private String itinerarioProprietario;

    // Construttori
    public GalleriaFotoItinerario() {}

    // Getter
    public String getIdFotoItinerario() { return idFotoItinerario; }
    public String getUrlFotoItinerario() { return urlFotoItinerario; }
    public String getItinerarioProprietario() { return itinerarioProprietario; }

    // Setter
    public void setIdFotoItinerario(String idFotoItinerario) { this.idFotoItinerario = idFotoItinerario; }
    public void setUrlFotoItinerario(String urlFotoItinerario) { this.urlFotoItinerario = urlFotoItinerario; }
    public void setItinerarioProprietario(String itinerarioProprietario) { this.itinerarioProprietario = itinerarioProprietario; }
}