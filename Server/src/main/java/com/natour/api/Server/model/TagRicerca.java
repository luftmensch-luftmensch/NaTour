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
import java.io.Serializable;

/** Modello per l'oggetto <strong>Tag</strong>. Presenta i seguenti campi:
 * <p>
 * <strong>idTag</strong> : ID univoco di uno specifico tag
 * </p>
 * <p>
 * <strong>value</strong> : Indica il valore del tag
 * </p>
 * <p>
 * <strong>itinerarioProprietarioTag</strong> : Parametro di collegamento con l'itinerario corrispondente
 * </p>
*/

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tag_ricerca")
public class TagRicerca implements Serializable{
    /** Identificativo univo del singolo tag */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTag;

    /** Il valore vero e proprio del tag */
    @Column(name = "value")
    private String value;

    /** Permette la mappatura con la classe {@link Itinerario}
     * (Un itinerario può avere molteplici tag)
    */
    @JsonManagedReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_itinerario_tag", referencedColumnName = "nomeitinerario")
    private Itinerario itinerarioProprietarioTag;
}