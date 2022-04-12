/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class TagRicerca implements Serializable{
    // NB: Va passato il valore a -1 per renderlo automatico
    @SerializedName("idTag")
    @Expose
    private Long idTag;

    @SerializedName("value")
    @Expose
    private String value;

    @SerializedName("itinerarioProprietarioTag")
    @Expose
    private String itinerarioProprietarioTag;

    // Costruttori
    public TagRicerca() { }

    public TagRicerca(Long idTag, String value, String itinerarioProprietarioTag) {
        this.idTag = idTag;
        this.value = value;
        this.itinerarioProprietarioTag = itinerarioProprietarioTag;
    }

    // Getter
    public Long getIdTag() { return idTag; }
    public String getValue() { return value; }
    public String getItinerarioProprietarioTag() { return itinerarioProprietarioTag; }

    // Setter
    public void setIdTag(Long idTag) { this.idTag = idTag; }
    public void setValue(String value) { this.value = value; }
    public void setItinerarioProprietarioTag(String itinerarioProprietarioTag) { this.itinerarioProprietarioTag = itinerarioProprietarioTag; }
}
