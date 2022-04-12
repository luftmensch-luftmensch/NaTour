/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.admin.presenter.callbackInterface;

// Questa classe viene utilizzata in accoppiata con AmplifyS3Request per la gestione dei dati (immagini) degli admin
public interface CallBackGeneric {
    void onSuccess(String s);
    void onFailure(Throwable throwable);
}