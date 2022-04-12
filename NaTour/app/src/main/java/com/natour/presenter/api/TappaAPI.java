/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.presenter.api;


// Model
import com.natour.model.Tappa;

// Rx-Java + Retrofit
import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
public interface TappaAPI {

    // GET Tappe totali
    @GET("TappeTotali")
    Single<List<Tappa>> getAllTappe();

    // GET Tappe dello stesso itinerario
    @GET("getTappeStessoItinerario/{nomeItinerario}")
    Single<List<Tappa>> getTappeStessoItinerario(@Path("nomeItinerario") String nomeItinerario);

    // POST: Inserimento nuova Tappa
    @POST("aggiungiTappa")
    Completable inserisciNuovaTappa(@Body Tappa tappa);

    // DELETE: Eliminazione tappa
    @DELETE("eliminaTappa/{idTracciato}")
    Completable eliminaTappa(@Path("idTracciato") Long idTappa);
}