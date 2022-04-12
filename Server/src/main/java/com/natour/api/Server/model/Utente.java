/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.api.Server.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;


/**
 * Modello per l'oggetto <strong>utente</strong>. Presenta i seguenti campi:
 * <p>
 * <strong>username</strong> : Indica il nome dell'utente corrispondente (viene utilizzato come chiave univoca dell'utente)
 * </p>
 * <p>
 * <strong>urlFotoProfilo</strong> : Campo che viene utilizzato per ottenere l'immagine di profilo dell'utente (Che viene salvato su S3)
 * </p>
 * <p>
 * <strong>listaItinerari</strong> : Parametro di collegamento con gli itinerari che un utente puà possedere
 * </p>
 */

/*
    @Getter, @Setter, @Accessors, @NoArgsConstructor, @AllArgsConstructor sono annotazioni Lombok
    Automatizzano il processo di creazione di getter e setter, di costruttori con parametri e non
*/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="utente") // schema="public"
public class Utente implements Serializable {
    // Attributi
    /** Identificativo univoco dell'utente */
    @Id
    @Column(name = "username")
    private String username;

    /** Campo che contiene la chiave necessaria ad accedere alla foto profilo dell'utente (memorizzata sul bucket S3) */
    @Column(name = "url_foto_profilo", length = 1000)
    private String urlFotoProfilo;


    // Riferimenti -> https://stackoverflow.com/questions/26601032/default-fetch-type-for-one-to-one-many-to-one-and-one-to-many-in-hibernate

    /*
        Il FetchType.LAZY è l'opzione di default in questo caso (Porta a una ricorsione infinita). 2 soluzioni:
        1. JsonIgnore -> Con la request ignora il campo sottostante
        2. JsonBackReference (in accoppiata con JsonManagedReference) -> Analogo al JsonIgnore:
            - Alla classe padre nego la lista della classe figlia
            - Alla classe figlia do la lista della classe padre
            Ottengo quindi la lista degli utenti (senza itinerari), ma per ogni itinerario ottengo l'utente proprietario
    */

    /**
     * Permette la mappatura (di tipo 1...*) con la classe {@link Itinerario}
     * (Un itinerario può avere una serie di itinerari ad esso associati)
    */
    @JsonBackReference
    @OneToMany(mappedBy = "utenteProprietario", cascade = CascadeType.ALL, fetch =  FetchType.LAZY)
    private List<Itinerario> listaItinerari;
}
