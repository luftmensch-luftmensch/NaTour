/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.admin.presenter.callbackInterface;

import com.natour.admin.model.Messaggio;
import java.util.List;

public interface CallBackMessaggio {
    void onSuccess(List<Messaggio> messaggios);
    void onFailure(Throwable throwable);
}
