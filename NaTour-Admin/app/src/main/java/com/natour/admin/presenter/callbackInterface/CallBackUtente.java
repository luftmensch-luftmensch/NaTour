/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.admin.presenter.callbackInterface;

import com.natour.admin.model.Utente;
import java.util.List;

public interface CallBackUtente {
    void onSuccess(List<Utente> listaUtenti);
    void onFailure(Throwable throwable);
}
