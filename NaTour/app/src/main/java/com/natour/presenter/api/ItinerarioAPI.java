/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.presenter.api;

// Model
import com.natour.model.Itinerario;

import java.util.List;

// Rx-Java + Retrofit
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ItinerarioAPI {
    // INFO: Definizioni -> https://stackoverflow.com/questions/42757924/what-is-the-difference-between-observable-completable-and-single-in-rxjava
    /*
    You can think of the differences like the differences of a method that returns:

    1. Collection of Objects -> Observable
    2. Single object -> Single
    3. Method that returns no values (void method) -> Completable.
    4. Maybe -> combination of Completable & Single
    5. Flowable has backpressure support allowing downstream to request items (Backpressure is a means of handling the situation where data
    is generated faster than it can be processed)
    */

    // Getter degli itinerari totali
    @GET("ItinerariTotali")
    Single<List<Itinerario>> getAllItinerari();

    @GET("getItinerario/{itinerarioID}")
    Single<Itinerario> getSingoloItinerario(@Path("itinerarioID") String nomeItinerario);

    // Getter con filtri (più recente, nome itinerario, zona geografica, durata, stesso autore)
    @GET("ItinerariRecenti")
    Single<List<Itinerario>> getItinerariDescOrder();

    @GET("Itinerari/NomeItinerario/{filtroNomeItinerario}")
    Single<List<Itinerario>> getItinerariByNomeItinerario(@Path("filtroNomeItinerario") String nomeItinerario);

    @GET("Itinerari/ZonaGeografica/{filtroZonaGeografica}")
    Single<List<Itinerario>> getItinerariByZonaGeografica(@Path("filtroZonaGeografica") String zonaGeografica);

    @GET("Itinerari/Durata/{filtroDurata}")
    Single<List<Itinerario>> getItinerariByDurata(@Path("filtroDurata") String durata);

    @GET("getItinerariByUtente/{utenteID}")
    Single<List<Itinerario>> getItinerarioByUtente(@Path("utenteID") String username);

    @POST("aggiungiItinerario")
    Single<Response<Void>> inserisciNuovoItinerario(@Body Itinerario itinerario);

    @DELETE("eliminaItinerario/{itinerarioID}")
    Completable eliminaItinerario(@Path("itinerarioID")String nomeItinerario);

    @PUT("aggiornaItinerario")
    Completable aggiornaItinerario(@Body Itinerario itinerarioDaAggiornare);
}
