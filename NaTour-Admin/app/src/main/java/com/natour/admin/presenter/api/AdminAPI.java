/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.admin.presenter.api;

import java.util.List;

import com.natour.admin.model.Admin;

// Rx-Java + Retrofit
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
public interface AdminAPI {

    @GET("getAdmin/{adminID}")
    Single<Admin> getSingoloAdmin(@Path("adminID") String username);

    @GET("getStatistiche")
    Single<List<Integer>> getStatistiche();

    @POST("aggiungiAdmin")
    Completable inserisciNuovoAdmin(@Body Admin admin);

    @PUT("aggiornaAdmin")
    Completable aggiornaAdmin(@Body Admin adminDaAggiornare);

    @DELETE("eliminaAdmin/{adminID}")
    Completable eliminaAdmin(@Path("adminID") String username);
}