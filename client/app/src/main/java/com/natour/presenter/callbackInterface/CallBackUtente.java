/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.presenter.callbackInterface;

import com.natour.model.Utente;

import java.util.List;

public interface CallBackUtente {
    void onSuccess(Utente utente);
    void onSuccessList(List<Utente> listaUtenti);
    void onFailure(Throwable throwable);
}