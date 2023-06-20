/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.presenter.callbackInterface;

import com.natour.model.SegnalazioneFotoItinerario;

import retrofit2.Response;
import java.util.List;

public interface CallBackSegnalazioneFotoItinerario {
    void onSuccessList(List<SegnalazioneFotoItinerario> segnalazioneFotoItinerarios);
    void onFailure(Throwable throwable);
    void onSuccessResponse(Response<Void> response);
}
