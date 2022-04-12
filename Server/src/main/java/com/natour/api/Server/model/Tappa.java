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

/**
 * Modello per l'oggetto <strong>tappa</strong>. Presenta i seguenti campi:
 * <p>
 * <strong>idTappa</strong> : ID univico della tappa
 * </p>
 * <p>
 * <strong>longitudine</strong> : Campo che da informazioni sulla longitudine della tappa
 * </p>
 * <p>
 * <strong>latitudine</strong> : Campo che da informazioni sulla latitudine della tappa
 * </p>
 * <p>
 * <strong>itinerarioCorrispondente</strong> : Parametro di collegamento con l'itinerario corrispondente
 * </p>
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tappa")
public class Tappa {
    /** Identificativo univoco della tappa */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTappa;

    /** Campo che contiene l'informazione relativa alla longitudine della singola tappa */
    @Column(name = "longitudine")
    private Double longitudine;

    /** Campo che contiene l'informazione relativa alla latitudine della singola tappa */
    @Column(name = "latitudine")
    private Double latitudine;

    /**
     * Permette la mappatura (di tipo *...1) con la classe {@link Itinerario}
     * (Una Tappa ha un campo itinerarioCorrispondente corrispondente)
    */
    @JsonManagedReference
    @ManyToOne(fetch =  FetchType.EAGER)
    @JoinColumn(name = "id_itinerario", nullable = false, referencedColumnName = "nomeitinerario")
    private Itinerario itinerarioCorrispondente;
}