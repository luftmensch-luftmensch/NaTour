/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.admin.presenter.api;

// Model
import com.natour.admin.model.SegnalazioneFotoItinerario;
import java.util.List;

// Rx-Java + Retrofit
import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;

public interface SegnalazioneFotoItinerarioAPI {
    // Retrievial Segnalazioni totali
    @GET("SegnalazioniTotali")
    Single<List<SegnalazioneFotoItinerario>> getAllSegnalazioni();
}