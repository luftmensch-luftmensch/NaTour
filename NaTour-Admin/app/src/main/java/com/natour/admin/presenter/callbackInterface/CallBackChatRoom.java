/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.admin.presenter.callbackInterface;

import com.natour.admin.model.ChatRoom;
import java.util.List;

public interface CallBackChatRoom {
    void onSuccess(List<ChatRoom> chatRooms);
    void onFailure(Throwable throwable);
}