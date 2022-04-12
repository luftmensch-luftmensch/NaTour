/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.api.Server.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.sql.Time;
import java.util.List;

/**
 * Modello per l'oggetto <strong>itinerario</strong>. Presenta i seguenti campi:
 * <p>
 * <strong>nomeItinerari </strong> : Indica il nome dell'itinerario corrispondente (viene utilizzato come chiave univoca dell'itinerario)
 * </p>
 * <p>
 * <strong>descrizione </strong> : Campo che permette di aggiungere una breve descrizione
 * </p>
 * <p>
 * <strong>accessoDisabili</strong> : Campo che informa dell'accesso disabili all'itinerario in questione
 * </p>
 * <p>
 * <strong>zonaGeografica </strong> : Campo che da informazioni sulla zona geografica dell'itinerario
 * </p>
 * <p>
 * <strong>difficolta</strong> : Campo che da informazioni sulla difficoltà dell'itinerario
 * </p>
 * <p>
 * <strong>citta</strong> : Campo che da informazioni sulla città dell'itinerario
 * </p>
 * <p>
 * <strong>dislivello</strong> : Campo che da informazioni sul dislivello dell'itinerario
 * </p>
 * <p>
 * <strong>lunghezza</strong> : Campo che da informazioni sulla lunghezza dell'itinerario
 * </p>
 * <p>
 * <strong>puntoDiInizioLongitudine</strong>, <strong>puntoDiInizioLatitudine</strong>, <strong>puntoDiFineLongitudine</strong>, <strong>puntoDiFineLatitudine</strong> : Coordinate dell'itinerario (inizio e fine)
 * </p>
 * <p>
 * <strong>durata</strong> : Campo che da informazioni sulla durata dell'itinerario
 * </p>
 * <p>
 * <strong>urlFotoItinerario</strong> : Campo che viene utilizzato per ottenere l'immagine di profilo dell'itinerario (Che viene salvato su S3)
 * </p>
 * <p>
 * <strong>utenteProprietario</strong> : Parametro di collegamento con l'utente
 * </p>
 * <p>
 * <strong>listaFotoItinerario</strong> : Parametro di collegamento con le foto facoltative che l'itinerario può possedere
 * </p>
 * <p>
 * <strong>listaPuntiTappe</strong> : Parametro di collegamento con le tappe facoltative che l'itinerario può possedere
 * </p>
 * <p>
 * <strong>listaTagRicerca</strong> : Parametro di collegamento con i tag facoltativi che l'itinerario può possedere
 * </p>
*/

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="itinerario") // schema="public"
public class Itinerario implements Serializable{


    //Guida per la modifica degli attributi ->  https://stackoverflow.com/questions/45782327/org-postgresql-util-psqlexception-error-column-user0-id-does-not-exist-hibe

    /** Identificativo univoco dell'itinerario */
    @Id
    @Column(name = "nomeitinerario")
    private String nomeItinerario;

    /** Campo che contiene l'informazione relativa alla descrizione dell'itinerario */
    @Column(name = "descrizione", length = 2000)
    private String descrizione;

    /** Campo che contiene l'informazione relativa all'accesso ai disabili nell'itinerario */
    @Column(name = "accesso_disabili")
    private String accessoDisabili;

    /** Campo che contiene l'informazione relativa alla zona geografica dell'itinerario */
    @Column(name = "zonageografica")
    private String zonaGeografica;

    /** Campo che contiene l'informazione relativa alla difficoltà dell'itinerario */
    @Column(name = "difficolta")
    private String difficolta;

    /** Campo che contiene l'informazione relativa alla città di appartenenza dell'itinerario */
    @Column(name = "citta")
    private String citta;

    /** Campo che contiene l'informazione relativa al dislivello dell'itinerario */
    @Column(name = "dislivello")
    private Integer dislivello;

    /** Campo che contiene l'informazione relativa alla lunghezza dell'itinerario */
    @Column(name = "lunghezza")
    private Double lunghezza;

    /** Campo che contiene l'informazione relativa al punto di inizio (longitudine) dell'itinerario */
    @Column(name ="punto_di_inizio_longitudine")
    private Double puntoDiInizioLongitudine;

    /** Campo che contiene l'informazione relativa al punto di inizio (latitudine) dell'itinerario */
    @Column(name ="punto_di_inizio_latitudine")
    private Double puntoDiInizioLatitudine;

    /** Campo che contiene l'informazione relativa al punto di fine (longitudine) dell'itinerario */
    @Column(name ="punto_di_fine_longitudine")
    private Double puntoDiFineLongitudine;

    /** Campo che contiene l'informazione relativa al punto di fine (latitudine) dell'itinerario */
    @Column(name ="punto_di_fine_latitudine")
    private Double puntoDiFineLatitudine;

    /** Campo che contiene l'informazione relativa alla durata dell'itinerario */
    @Column(name ="durata")
    private Time durata;

    /** Campo che contiene l'informazione relativa alla foto profilo dell'itinerario */
    @Column(name = "url_foto_itinerario")
    private String urlFotoItinerario;

    /** Campo che contiene l'informazione necessaria per saèere se la foto di profilo è stata segnalata (e quindi non deve essere segnalata) */
    @Column(name = "stato_foto_segnalazione")
    private String statoFotoSegnalazione;

    /**
     * Permette la mappatura (di tipo *...1) con la classe {@link Utente}.
     * (Un itinerario ha un campo un utente proprietario)
    */
    @JsonManagedReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_proprietario", nullable = false, referencedColumnName = "username")
    private Utente utenteProprietario;

    /**
     * Permette la mappatura (di tipo 1...*) con la classe {@link GalleriaFotoItinerario}
     * (Un itinerario può avere una serie di foto ad esso associate)
    */
    @JsonBackReference
    @OneToMany(mappedBy = "itinerarioProprietario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<GalleriaFotoItinerario> listaFotoItinerario;

    /**
     * Permette la mappatura (di tipo 1...*) con la classe {@link Tappa}
     * (Un itinerario può avere una serie di tappe ad esso associate)
    */
    @JsonBackReference
    @OneToMany(mappedBy = "itinerarioCorrispondente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Tappa> listaPuntiTappe;

    /**
     * Permette la mappatura (di tipo 1...*) con la classe {@link TagRicerca}
     * (Un itinerario può avere una serie di tag ad esso associati)
    */
    @JsonBackReference
    @OneToMany(mappedBy = "itinerarioProprietarioTag", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TagRicerca> listaTagRicerca;
}