/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.presenter.callbackInterface;

import com.natour.model.TagRicerca;

import java.util.List;

import retrofit2.Response;

public interface CallBackTagRicerca {
    void onSuccessList(List<TagRicerca> tags);
    void onFailure(Throwable throwable);
    void onSuccessResponse(Response<Void> response);
}
