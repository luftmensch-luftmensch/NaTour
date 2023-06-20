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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

/** Modello per l'oggetto <strong>GalleriaFotoItinerario</strong>. Presenta i seguenti campi:
 * <p>
 * <strong>idFotoItinerario</strong> : ID univoco di uno specifica foto inerente un itinerario
 * </p>
 * <p>
 * <strong>urlFotoItinerario</strong> : Indica il parametro associato all'immagine salvata su S3
 * </p>
 * <p>
 * <strong>itinerarioProprietario</strong> : Parametro di collegamento con l'itinerario corrispondente
 * </p>
 * <p>
 * <strong>listaSegnalazioni</strong> : Parametro di collegamento con le segnalazioni che l'itinerario può avere
 * </p>
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="galleria_foto_itinerario")
public class GalleriaFotoItinerario {
    /** Identificativo di una singola foto che appartiene a uno specifico itinerario */
    @Id
    @Column(name = "id_foto_itinerario")
    private String idFotoItinerario;

    /** Campo che contiene la chiave necessaria ad accedere alla foto (memorizzata sul bucket S3) */
    @Column(name = "url_foto_itinerario")
    private String urlFotoItinerario;

    /** Permette la mappatura con classe Itinerario
     * (Una foto ha un campo itinerarioProprietario corrispondente)
    */
    @JsonManagedReference
    @ManyToOne(fetch =  FetchType.EAGER)
    @JoinColumn(name = "id_itinerario", nullable = false, referencedColumnName = "nomeitinerario")
    private Itinerario itinerarioProprietario;

    /** Permette la mappatura con la classe Segnalazioni
     * (Una foto può avere delle segnalazioni)
    */
    @JsonBackReference
    @OneToMany(mappedBy = "galleriaFotoItinerarioCorrispondente", cascade = CascadeType.ALL, fetch =  FetchType.LAZY)
    private List<SegnalazioneFotoItinerario> listaSegnalazioni;
}