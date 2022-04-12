/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.admin.presenter.retrofit;

import com.natour.admin.utils.constants.Constants;
import com.natour.admin.utils.constants.ElencoEndPoint;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
public class AdminRetrofitInstance {

    private static Retrofit adminRetrofit = null;

    public static Retrofit getAdminRetrofit(ElencoEndPoint endPoint){
        if(adminRetrofit == null){
            adminRetrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL + Constants.getApiRest(endPoint))
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .build();
        }
        return adminRetrofit;
    }
}
