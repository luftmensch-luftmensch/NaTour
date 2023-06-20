/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.presenter.callbackInterface;

// Questa classe viene utilizzata in accoppiata con AmplifyRequest per la gestione dell'accesso degli utenti alla piattaforma
public interface CallBackAmplify {
    void onSuccess();
    void onFailure(Throwable throwable);
}
