/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.admin.utils.handler;

import android.content.Context;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.net.SocketTimeoutException;

import retrofit2.HttpException;
import es.dmoral.toasty.Toasty;

// Messaggi Toast
public class ApiHandler {
    public ApiHandler(@NotNull Throwable e, Context context){
        if(context == null){
            return;
        }

        if(e instanceof HttpException){
            switch (((HttpException) e).code()){
                case 200:
                    Toasty.success(context, "Operazione completata con successo",
                            Toast.LENGTH_SHORT, true).show();
                case 404:
                    Toasty.error(context, "Risorsa non trovata",
                            Toast.LENGTH_SHORT, true).show();
                case 500:
                    Toasty.error(context, "Risorsa temporaneamente non disponibile",
                            Toast.LENGTH_SHORT, true).show();
                default:
                    Toasty.error(context, "Qualcosa è andato storto",
                            Toast.LENGTH_SHORT, true).show();
            }
        } else if(e instanceof SocketTimeoutException){
            Toasty.error(context, "Imppoosibile completare la richiesta\nConnessione al server fallita",
                    Toasty.LENGTH_SHORT, true).show();
        }
    }
}
