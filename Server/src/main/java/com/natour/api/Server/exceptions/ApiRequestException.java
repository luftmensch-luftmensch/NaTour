/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.api.Server.exceptions;

import org.springframework.http.HttpStatus;

/** {@code ApiRequestException} estende RunTimeException con lo scopo di creare eccezioni custom da utilizzare
 * nel caso in cui le request effettuate non vadano a buon fine
*/
public class ApiRequestException extends RuntimeException{

    /**
     * {@code ApiRequestException} possiede un campo stato, che viene utilizzato per gestire
     * l'Http status
     * */
    private HttpStatus stato;

    /**
     * @param message: parametro per definire il messaggio dell'errore
     * @param status: codice di stato
     */
    public ApiRequestException(String message, HttpStatus status) {
        super(message);
        this.stato = status;

    }

    /**
     * @param message: parametro per definire il messaggio dell'errore
     * @param cause: motivo dell'errore
     */
    public ApiRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Getter per {@code ApiRequestException}
     * @return Stato
     */
    public HttpStatus getStato() { return stato; }

    /**
     * Setter dello status code di {@code ApiRequestException}
     * @param stato: Necessario per decidere che tipo di errore inviare in risposta alla request del frontend
     */
    public void setStato(HttpStatus stato) { this.stato = stato; }
}
