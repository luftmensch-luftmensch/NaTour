package com.natour.api.Server.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/** Modello per l'oggetto <strong>ChatRoom</strong>. Presenta i seguenti campi:
 * <p>
 * <strong>idChatRoom</strong> : ID univoco di uno specifica chat
 * </p>
 * <p>
 * <strong>utenteA</strong> : Indica uno dei 2 utenti che partecipano alla chatroom
 * </p>
 * <p>
 * <strong>utenteB</strong> : Indica uno dei 2 utenti che partecipano alla chatroom
 * </p>
 * <p>
 * <strong>listaMessaggi</strong> : Parametro di collegamento con la lista di messaggi appartenti alla chatroom
 * </p>
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "chat_room")
public class ChatRoom implements Serializable{

    /*
    Alla creazione della ChatRoom settiamo il suo id come mix
    dei 2 utenti che partecipano alla chat
    TODO: Sostituire l'idChatRoom con uno autogenerato
    */

    /** Identificativo della singola chat room */
    @Id
    @Column(name = "id_chat_room")
    private String idChatRoom;

    /** Identificativo di uno dei 2 utenti che possono partecipare alla chatroom */
    @Column(name = "utente_a")
    private String utenteA;

    /** Identificativo di uno dei 2 utenti che possono partecipare alla chatroom */
    @Column(name = "utente_b")
    private String utente_B;

    /** Permette la mappatura con la classe Messaggio
     * (una ChatRoom contiene una serie di messaggi)
    */
    @JsonBackReference
    @OneToMany(mappedBy = "chatRoomProprietaria", cascade = CascadeType.ALL, fetch =  FetchType.LAZY)
    private List<Messaggio> listaMessaggi;
}