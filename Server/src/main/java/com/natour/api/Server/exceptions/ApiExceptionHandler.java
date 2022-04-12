/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.api.Server.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

/** Classe che ha il compito di Handler delle eccezioni custom per le request */
@ControllerAdvice
public class ApiExceptionHandler {

    /** Handler della nostra eccezione
     * @param e: eccezione custom (ApiRequestException)
     * @return Response generica
     */
    @ExceptionHandler(value = {ApiRequestException.class})
    public ResponseEntity<Object> handleApiRequestException(ApiRequestException e){

        // Creiamo un payload che conterrà i dettagli dell'exception
        HttpStatus badRequest = e.getStato();
        ApiException apiException = new ApiException(e.getMessage(), badRequest, ZonedDateTime.now(ZoneId.of("Z")));

        // ritorniamo la response entity
        return new ResponseEntity<>(apiException, badRequest);
    }
}
