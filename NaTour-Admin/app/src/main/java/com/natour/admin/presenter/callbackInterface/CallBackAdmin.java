/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.admin.presenter.callbackInterface;

import com.natour.admin.model.Admin;

public interface CallBackAdmin {
    void onSuccess(Admin admin);
    void onFailure(Throwable throwable);
}
