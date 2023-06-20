/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Itinerario implements Serializable {
    // Attributi
    @SerializedName("nomeItinerario")
    @Expose
    private String nomeItinerario;

    @SerializedName("descrizione")
    @Expose
    private String descrizione;

    @SerializedName("accessoDisabili")
    @Expose
    private String accessoDisabili;

    @SerializedName("zonaGeografica")
    @Expose
    private String zonaGeografica;

    @SerializedName("difficolta")
    @Expose
    private String difficolta;

    @SerializedName("puntoDiInizioLongitudine")
    @Expose
    private Double puntoDiInizioLongitudine;

    @SerializedName("puntoDiInizioLatitudine")
    @Expose
    private Double puntoDiInizioLatitudine;

    @SerializedName("puntoDiFineLongitudine")
    @Expose
    private Double puntoDiFineLongitudine;

    @SerializedName("puntoDiFineLatitudine")
    @Expose
    private Double puntoDiFineLatitudine;

    @SerializedName("durata")
    @Expose
    private String durata;

    @SerializedName("citta")
    @Expose
    private String citta;

    @SerializedName("dislivello")
    private Integer dislivello;

    @SerializedName("lunghezza")
    @Expose
    private Double lunghezza;


    @SerializedName("utenteProprietario")
    private String utenteProprietario;

    @SerializedName("urlFotoItinerario")
    @Expose
    private String urlFotoItinerario;

    @SerializedName("statoFotoSegnalazione")
    @Expose
    private String statoFotoSegnalazione;

    // Construttori
    public Itinerario() { }
    public Itinerario(String nomeItinerario, String descrizione, String accessoDisabili,
                      String zonaGeografica, String difficolta, Double puntoDiInizioLongitudine,
                      Double puntoDiInizioLatitudine, Double puntoDiFineLongitudine,
                      Double puntoDiFineLatitudine, String durata, String utenteProprietario, String urlFotoItinerario) {
        this.nomeItinerario = nomeItinerario;
        this.descrizione = descrizione;
        this.accessoDisabili = accessoDisabili;
        this.zonaGeografica = zonaGeografica;
        this.difficolta = difficolta;
        this.puntoDiInizioLongitudine = puntoDiInizioLongitudine;
        this.puntoDiInizioLatitudine = puntoDiInizioLatitudine;
        this.puntoDiFineLongitudine = puntoDiFineLongitudine;
        this.puntoDiFineLatitudine = puntoDiFineLatitudine;
        this.durata = durata;
        this.utenteProprietario = utenteProprietario;
        this.urlFotoItinerario = urlFotoItinerario;
    }




    // Getter
    public String getNomeItinerario() { return nomeItinerario; }
    public String getDescrizione() { return descrizione; }
    public String getAccessoDisabili() { return accessoDisabili; }
    public String getZonaGeografica() { return zonaGeografica; }
    public String getDifficolta() { return difficolta; }
    public String getDurata() { return durata; }
    public String getUtenteProprietario() { return utenteProprietario; }
    public String getUrlFotoItinerario() { return urlFotoItinerario; }
    public String getCitta() { return citta; }
    public Integer getDislivello() { return dislivello; }
    public Double getLunghezza() { return lunghezza; }
    public String getStatoFotoSegnalazione() { return statoFotoSegnalazione; }

    public Double getPuntoDiInizioLongitudine() { return puntoDiInizioLongitudine; }
    public Double getPuntoDiInizioLatitudine() { return puntoDiInizioLatitudine; }
    public Double getPuntoDiFineLongitudine() { return puntoDiFineLongitudine; }
    public Double getPuntoDiFineLatitudine() { return puntoDiFineLatitudine; }



    // Setter
    public void setNomeItinerario(String nomeItinerario) { this.nomeItinerario = nomeItinerario; }
    public void setDescrizione(String descrizione) { this.descrizione = descrizione; }
    public void setAccessoDisabili(String accessoDisabili) { this.accessoDisabili = accessoDisabili; }
    public void setZonaGeografica(String zonaGeografica) { this.zonaGeografica = zonaGeografica; }
    public void setDifficolta(String difficolta) { this.difficolta = difficolta; }
    public void setDurata(String durata) { this.durata = durata; }

    public void setCitta(String citta) { this.citta = citta; }
    public void setDislivello(Integer dislivello) { this.dislivello = dislivello; }
    public void setLunghezza(Double lunghezza) { this.lunghezza = lunghezza; }

    public void setUtenteProprietario(String utenteProprietario) { this.utenteProprietario = utenteProprietario; }
    public void setUrlFotoItinerario(String urlFotoItinerario) { this.urlFotoItinerario = urlFotoItinerario; }

    public void setPuntoDiInizioLongitudine(Double puntoDiInizioLongitudine) { this.puntoDiInizioLongitudine = puntoDiInizioLongitudine; }
    public void setPuntoDiInizioLatitudine(Double puntoDiInizioLatitudine) { this.puntoDiInizioLatitudine = puntoDiInizioLatitudine; }
    public void setPuntoDiFineLongitudine(Double puntoDiFineLongitudine) { this.puntoDiFineLongitudine = puntoDiFineLongitudine; }
    public void setPuntoDiFineLatitudine(Double puntoDiFineLatitudine) { this.puntoDiFineLatitudine = puntoDiFineLatitudine; }
    public void setStatoFotoSegnalazione(String statoFotoSegnalazione) { this.statoFotoSegnalazione = statoFotoSegnalazione; }
}
