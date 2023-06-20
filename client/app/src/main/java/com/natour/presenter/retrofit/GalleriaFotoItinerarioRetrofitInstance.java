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
public class GalleriaFotoItinerarioRetrofitInstance {
    private static Retrofit galleriaFotoItinerarioRetrofit = null;

    public static Retrofit getGalleriaFotoItinerarioRetrofit(ElencoEndPoint endPoint){
        if(galleriaFotoItinerarioRetrofit == null){
            galleriaFotoItinerarioRetrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL + Constants.getApiRest(endPoint))
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .build();
        }
        return galleriaFotoItinerarioRetrofit;
    }
}