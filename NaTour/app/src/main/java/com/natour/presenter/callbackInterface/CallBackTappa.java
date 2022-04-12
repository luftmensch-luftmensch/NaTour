/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.presenter.callbackInterface;

import com.natour.model.Tappa;

import java.util.List;

public interface CallBackTappa {
    void onSuccessList(List<Tappa> tappe);
    void onFailure(Throwable throwable);
}