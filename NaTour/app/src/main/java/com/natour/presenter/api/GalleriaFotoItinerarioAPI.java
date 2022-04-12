/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.presenter.api;

// Model
import com.natour.model.GalleriaFotoItinerario;
import java.util.List;

// Rx-Java + Retrofit
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface GalleriaFotoItinerarioAPI {
    // GET: Lista delle foto degli itinerari
    @GET("GalleriaFotoItinerarioTotali")
    Single<List<GalleriaFotoItinerario>> getAllFotoitinerari();

    // GET: Lista delle foto di uno stesso itinerario
    @GET("getFotoStessoItinerario/{idItinerario}")
    Single<List<GalleriaFotoItinerario>> getGalleriaFotoStessoItinerario(@Path("idItinerario") String idItinerario);

    // POST: Inserimento foto di un itinerario
    @POST("aggiungiFotoItinerario")
    Single<Response<Void>> inserisciNuovaFotoItinerario(@Body GalleriaFotoItinerario galleriaFotoItinerario);

    @DELETE("eliminaItinerarioFoto/{idFotoItinerario}")
    Completable eliminaFotoItinerario(@Path("idFotoItinerario") Long idFotoItinerario);
}
