/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.admin.presenter.callbackInterface;

import com.natour.admin.model.Itinerario;
import java.util.List;

public interface CallBackItinerario {
    void onSuccess(List<Itinerario> itinerarios);
    void onFailure(Throwable throwable);
}
