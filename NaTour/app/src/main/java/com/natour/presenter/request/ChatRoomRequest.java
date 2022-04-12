/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.presenter.request;

import android.util.Log;

import com.natour.model.ChatRoom;
import com.natour.presenter.api.ChatRoomAPI;
import com.natour.presenter.callbackInterface.CallBackChatRoom;
import com.natour.presenter.retrofit.ChatRoomRetrofitInstance;
import com.natour.utils.constants.ElencoEndPoint;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import retrofit2.Response;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomRequest {
    private static final String ChatRoom_TAG = "ChatRoomRequest";
    private ChatRoomAPI chatRoomAPI;
    public ChatRoomRequest(){
        chatRoomAPI = ChatRoomRetrofitInstance.getChatRoomRetrofit(ElencoEndPoint.CHAT_ROOM).create(ChatRoomAPI.class);
    }

    // GET: Lista delle chat room appartenti all'utente loggato
    public void getChatRoomsByUtente(String utente, CallBackChatRoom callBackChatRoom){
        chatRoomAPI.getAllChatRoomsByUtente(utente).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<List<ChatRoom>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(ChatRoom_TAG, "onSubscribe: Lista di tutte le chat room dell'utente loggato presenti nel db");
                    }

                    @Override
                    public void onSuccess(@NonNull List<ChatRoom> chatRooms) {
                        callBackChatRoom.onSuccessList(chatRooms);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        callBackChatRoom.onFailure(e);
                    }
                });
    }

    // Creazione di una nuova chat room
    public void aggiungiChatRoom(ChatRoom chatRoom, CallBackChatRoom callBackChatRoom){
        chatRoomAPI.aggiungiChatRoom(chatRoom).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<Response<Void>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(ChatRoom_TAG, "onSubscribe: Creazione di una chat room");
                    }

                    @Override
                    public void onSuccess(@NonNull Response<Void> voidResponse) {
                        callBackChatRoom.onSuccessResponse(voidResponse);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        callBackChatRoom.onFailure(e);

                    }
                });
    }
}
