/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.presenter.callbackInterface;

import com.natour.model.ChatRoom;

import retrofit2.Response;
import java.util.List;

public interface CallBackChatRoom {
    void onSuccessList(List<ChatRoom> chatRooms);
    void onFailure(Throwable throwable);
    void onSuccessResponse(Response<Void> response);
}