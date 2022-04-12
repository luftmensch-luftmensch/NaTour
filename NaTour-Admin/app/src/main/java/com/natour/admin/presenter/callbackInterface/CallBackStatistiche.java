/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.admin.presenter.callbackInterface;

import java.util.List;

// Questa classe viene utilizzata per ottenere le statistiche di utilizzo dell'applicativo
public interface CallBackStatistiche {
    void onSuccess(List<Integer> listaValoriStatistiche);
    void onFailure(Throwable throwable);
}