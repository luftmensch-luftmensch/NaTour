/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.presenter.callbackInterface;

import com.natour.model.GalleriaFotoItinerario;

import java.util.List;

import retrofit2.Response;

public interface CallBackGalleriaFotoItinerario {
    void onSuccessList(List<GalleriaFotoItinerario> galleriaFotoItinerarioList);
    void onFailure(Throwable throwable);
    void onSuccessResponse(Response<Void> response);
}
