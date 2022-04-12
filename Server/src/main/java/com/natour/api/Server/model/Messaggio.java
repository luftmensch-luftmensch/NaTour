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
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.sql.Time;
import java.io.Serializable;
import java.time.LocalDate;

/** Modello per l'oggetto <strong>messaggio</strong>: Presenta i seguenti campi:
 * <p>
 *  <strong>idMesaggio</strong> : ID univoco del mesaggio
 * </p>
 * <p>
 *  <strong>mittente</strong> : Indica il mittente del messaggio
 * </p>
 * <p>
 *  <strong>destinatario</strong> : Indica il destinatario del messaggio
 * </p>
 * <p>
 *  <strong>testoMessaggio</strong> : Indica il testo del messaggio
 * </p>
 * <p>
 *  <strong>dataInvioMessaggio</strong> : Indica la data dell'invio del messaggio
 * </p>
 * <p>
 *  <strong>oraInvioMessaggio</strong> : Indica l' ora dell'invio del messaggio
 * </p>
 * <p>
 *  <strong>chatRoomProprietaria</strong> : Parametro di collegamento con la chatroom di appartenenza
 * </p>
*/

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "messaggio")
public class Messaggio implements Serializable{
   /** Identificativo univoco del singolo messaggio */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_messaggio")
    private Long idMessaggio;

    /** Campo che contiene l'informazione relativa al mittente del messaggio */
    @Column(name = "mittente")
    private String mittente;

    /** Campo che contiene l'informazione relativa al destinatario del messaggio */
    @Column(name = "destinatario")
    private String destinatario;

    /** Campo che contiene l'informazione relativa al testo del messaggio */
    @Column(name = "testo_messaggio")
    private String testoMessaggio;

    /** Campo che contiene l'informazione relativa alla data di invio del messaggio */
    @Column(name = "data_invio_messaggio")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate dataInvioMessaggio;

    /** Campo che contiene l'informazione relativa all'ora di invio del messaggio */
    @Column(name = "ora_invio_messaggio")
    private Time oraInvioMessaggio;

    /** Permette la mappatura con la classe ChatRoom
     * (Un messaggio appartiene a una singola ChatRoom)
    */
    @JsonManagedReference
    @ManyToOne(fetch =  FetchType.EAGER)
    @JoinColumn(name = "id_chat", nullable = false, referencedColumnName = "id_chat_room")
    private ChatRoom chatRoomProprietaria;
}