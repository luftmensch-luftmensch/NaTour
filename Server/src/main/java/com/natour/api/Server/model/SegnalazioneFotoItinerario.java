/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.api.Server.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/** Modello per l'oggetto <strong>Segnalazione</strong> di una specifica foto dell'itinerario. Presenta i seguenti campi:
 * <p>
 * <strong>idSegnalazione</strong> : ID univoco di uno specifica segnalazione
 * </p>
 * <p>
 * <strong>utenteSegnalatore</strong> : Indica l'utente che ha effettuato la segnalazione
 * </p>
 * <p>
 * <strong>galleriaFotoItinerarioCorrispondente</strong> : Parametro di collegamento con la foto corrispondente
 * </p>
*/

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "segnalazioni_foto_itinerario")
public class SegnalazioneFotoItinerario {
    /** Identificativo univoco della singola segnalazione */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSegnalazione;

    /** Campo che contiene l'informazione relativa all'utente che ha segnalato la foto */
    @Column(name = "utente_segnalatore")
    private String utenteSegnalatore;

    /** Permette la mappatura con classe GalleriaFotoItinerario
     * (una foto può essere segnalata)
    */
    @JsonManagedReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_segnalazione_foto_itinerario", nullable = false, referencedColumnName = "id_foto_itinerario")
    private GalleriaFotoItinerario galleriaFotoItinerarioCorrispondente;
}