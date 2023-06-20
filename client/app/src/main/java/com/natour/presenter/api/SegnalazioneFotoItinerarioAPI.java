/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.presenter.api;

// Model
import com.natour.model.SegnalazioneFotoItinerario;
import java.util.List;

// Rx-Java + Retrofit
import io.reactivex.rxjava3.core.Single;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface SegnalazioneFotoItinerarioAPI {

    // Retrievial segnalazioni di una stessa immagine appartente ad un itinerario
    @GET("getSegnalazioniByFoto/{idFoto}")
    Single<List<SegnalazioneFotoItinerario>> getSegnalazioniStessaFoto(@Path("idFoto") String idFoto);

    // Aggiunta di una nuova segnalazione
    @POST("aggiungiSegnalazione")
    Single<Response<Void>> aggiungiNuovaSegnalazione(@Body SegnalazioneFotoItinerario segnalazioneFotoItinerario);
}