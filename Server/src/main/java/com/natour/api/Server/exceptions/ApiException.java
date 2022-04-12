/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.api.Server.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

/** Record per la ApiException */
@Getter
@AllArgsConstructor
public class ApiException {
    /** Il messaggio che il backend restituisce in caso di errore */
    private final String messaggio;
    /** L'Https status che il backend restituisce in caso di errore */
    private final HttpStatus httpStatus;
    /** Il timestamp che il backend restituisce in caso di errore */
    private final ZonedDateTime timestamp;
}
