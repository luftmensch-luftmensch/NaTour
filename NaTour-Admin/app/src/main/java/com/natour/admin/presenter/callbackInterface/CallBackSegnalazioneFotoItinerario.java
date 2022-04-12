/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.admin.presenter.callbackInterface;

import com.natour.admin.model.SegnalazioneFotoItinerario;
import java.util.List;

public interface CallBackSegnalazioneFotoItinerario {
    void onSuccess(List<SegnalazioneFotoItinerario> segnalazioneFotoItinerarios);
    void onFailure(Throwable throwable);
}