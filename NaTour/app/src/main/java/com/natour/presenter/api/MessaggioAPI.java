/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.presenter.api;

// Model
import com.natour.model.Messaggio;

import java.util.List;

// Rx-Java + Retrofit
import io.reactivex.rxjava3.core.Single;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MessaggioAPI {

    // GET lista messaggi appartententi ad una stessa chat room
    @GET("getMessaggiByChatRoom/{idChatRoom}")
    Single<List<Messaggio>> getMessaggiByChatRoom(@Path("idChatRoom") String idChatRoom);

    @POST("aggiungiMessaggio")
    Single<Response<Void>> aggiungiMessaggio(@Body Messaggio messaggioDaAggiungere);
}