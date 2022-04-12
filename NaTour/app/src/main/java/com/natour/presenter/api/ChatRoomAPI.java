/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.presenter.api;

// Model
import com.natour.model.ChatRoom;

import java.util.List;

// Rx-Java + Retrofit
import io.reactivex.rxjava3.core.Single;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ChatRoomAPI {
    @GET("ChatRoomByUtente/{idUtente}")
    Single<List<ChatRoom>> getAllChatRoomsByUtente(@Path("idUtente") String utente);

    @POST("aggiungiChatRoom")
    Single<Response<Void>> aggiungiChatRoom(@Body ChatRoom chatRoom);
}