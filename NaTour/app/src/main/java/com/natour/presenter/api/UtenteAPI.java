/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.presenter.api;

//Model
import com.natour.model.Utente;

import java.util.List;

// Rx-Java + Retrofit
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UtenteAPI {
    @GET("UtentiTotali")
    Single<List<Utente>> getAllUtenti();

    @GET("getUtente/{utenteID}")
    Single<Utente> getSingoloUtente(@Path("utenteID") String username);

    @POST("aggiungiUtente")
    Completable inserisciNuovoUtente(@Body Utente utente);

    @PUT("aggiornaUtente")
    Completable aggiornaUtente(@Body Utente utenteDaAggiornare);

    @DELETE("eliminaUtente/{utenteID}")
    Completable eliminaUtente(@Path("utenteID") String username);
}
