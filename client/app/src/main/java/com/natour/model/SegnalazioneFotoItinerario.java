/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class SegnalazioneFotoItinerario implements Serializable {

    // NB: Va passato il valore a -1 per renderlo automatico
    @SerializedName("idSegnalazione")
    @Expose
    private Long idSegnalazione;

    @SerializedName("utenteSegnalatore")
    @Expose
    private String utenteSegnalatore;

    @SerializedName("galleriaFotoItinerarioCorrispondente")
    @Expose
    private String galleriaFotoItinerarioCorrispondente;

    // Costruttori
    public SegnalazioneFotoItinerario() { }
    public SegnalazioneFotoItinerario(Long idSegnalazione, String utenteSegnalatore, String galleriaFotoItinerarioCorrispondente) { this.idSegnalazione = idSegnalazione; this.utenteSegnalatore = utenteSegnalatore; this.galleriaFotoItinerarioCorrispondente = galleriaFotoItinerarioCorrispondente; }

    // Getter
    public Long getIdSegnalazione() { return idSegnalazione; }
    public String getUtenteSegnalatore() { return utenteSegnalatore; }
    public String getGalleriaFotoItinerarioCorrispondente() { return galleriaFotoItinerarioCorrispondente; }

    // Setter
    public void setIdSegnalazione(Long idSegnalazione) { this.idSegnalazione = idSegnalazione; }
    public void setUtenteSegnalatore(String utenteSegnalatore) { this.utenteSegnalatore = utenteSegnalatore; }
    public void setGalleriaFotoItinerarioCorrispondente(String galleriaFotoItinerarioCorrispondente) { this.galleriaFotoItinerarioCorrispondente = galleriaFotoItinerarioCorrispondente; }
}
