/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.presenter.api;

// Model
import com.natour.model.TagRicerca;


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

public interface TagRicercaAPI {
    // Retrievial Tag totali
    @GET("TagRicercaTotali")
    Single<List<TagRicerca>> getAllTags();

    // Retrievial tag di uno stesso itinerario
    @GET("getTagStessoItinerario/{idItinerario}")
    Single<List<TagRicerca>> getTagStessoItinerario(@Path("idItinerario") String nomeItinerario);

    // Aggiunta di un nuovo Tag
    @POST("aggiungiTagRicerca")
    Single<Response<Void>> aggiungiNuovoTag(@Body TagRicerca tagRicerca);

    // Eliminazione tag
    @DELETE("eliminaTagRicerca/{idTagRicerca}")
    Completable eliminaTag(@Path("idTagRicerca") Long idTagRicerca);


}
