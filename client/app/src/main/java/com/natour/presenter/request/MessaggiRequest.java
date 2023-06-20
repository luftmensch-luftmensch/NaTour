package com.natour.presenter.request;

import android.util.Log;

import com.natour.model.Messaggio;
import com.natour.presenter.api.MessaggioAPI;
import com.natour.presenter.callbackInterface.CallBackMessaggi;
import com.natour.presenter.retrofit.MessaggiRetrofitInstance;
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
public class MessaggiRequest {
    private static final String Messaggi_TAG = "MessaggiRequest";
    private MessaggioAPI messaggioAPI;

    public MessaggiRequest(){
        messaggioAPI = MessaggiRetrofitInstance.getMessaggiRetrofit(ElencoEndPoint.MESSAGGIO).create(MessaggioAPI.class);
    }

    // GET lista messaggi appartententi ad una stessa chat room
    public void getMessaggiByChatRoom(String idChatRoom, CallBackMessaggi callBackMessaggi){
        messaggioAPI.getMessaggiByChatRoom(idChatRoom).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<List<Messaggio>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(Messaggi_TAG, "onSubscribe: Lista dei messaggi totali della chat room richiesta");
                    }

                    @Override
                    public void onSuccess(@NonNull List<Messaggio> messaggios) {
                        callBackMessaggi.onSuccessList(messaggios);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        callBackMessaggi.onFailure(e);
                    }
                });
    }

    public void inviaMessaggio(Messaggio messaggio, CallBackMessaggi callBackMessaggi){
        messaggioAPI.aggiungiMessaggio(messaggio).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<Response<Void>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(Messaggi_TAG, "onSubscribe: Inserimento di un nuovo messaggio sul db");
                    }

                    @Override
                    public void onSuccess(@NonNull Response<Void> voidResponse) {
                        callBackMessaggi.onSuccessResponse(voidResponse);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        callBackMessaggi.onFailure(e);
                    }
                });
    }
}