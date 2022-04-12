/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.admin.presenter.api;

// Model
import com.natour.admin.model.Messaggio;

import java.util.List;

// Rx-Java + Retrofit
import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;

public interface MessaggioAPI {
    @GET("MessaggiTotali")
    Single<List<Messaggio>> getAllMessaggi();
}