/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.presenter.callbackInterface;

import com.natour.model.Itinerario;

import java.util.List;

import retrofit2.Response;

public interface CallBackItinerario {
    void onSuccess(Itinerario itinerario);
    void onSuccessList(List<Itinerario> itinerari);
    void onFailure(Throwable throwable);
    void onSuccessResponse(Response<Void> response);
}