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
 * Modello per l'oggetto <strong>admin</strong>. Presenta i seguenti campi:
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
@Table(name="admin")
public class Admin {

    // Attributi
    /** Identificativo univoco dell'admin */
    @Id
    @Column(name = "username")
    private String username;

    /** Campo che contiene la chiave necessaria ad accedere alla foto profilo dell'admin (memorizzata sul bucket S3) */
    @Column(name = "url_foto_profilo", length = 1000)
    private String urlFotoProfilo;
}
