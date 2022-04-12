/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.presenter.retrofit;

import com.natour.utils.constants.Constants;
import com.natour.utils.constants.ElencoEndPoint;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
public class ItinerarioRetrofitInstance {
    private static Retrofit itinerarioRetrofit = null;

    public static Retrofit getItinerarioRetrofit(ElencoEndPoint endPoint){
        if(itinerarioRetrofit == null){
            itinerarioRetrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL + Constants.getApiRest(endPoint))
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .build();
        }
        return itinerarioRetrofit;
    }
}
