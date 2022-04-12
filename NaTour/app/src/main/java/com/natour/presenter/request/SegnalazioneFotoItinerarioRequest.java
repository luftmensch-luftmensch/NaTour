/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.presenter.request;

import android.util.Log;

import com.natour.model.SegnalazioneFotoItinerario;
import com.natour.presenter.api.SegnalazioneFotoItinerarioAPI;
import com.natour.presenter.callbackInterface.CallBackSegnalazioneFotoItinerario;
import com.natour.presenter.retrofit.SegnalazioneFotoItinerarioRetrofitInstance;
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
public class SegnalazioneFotoItinerarioRequest {
    private static final String SegnalazioneFotoItinerario_TAG = "SegnalazioneFotoItinerario_REQUEST";
    private SegnalazioneFotoItinerarioAPI segnalazioneFotoItinerarioAPI;

    public SegnalazioneFotoItinerarioRequest(){
        segnalazioneFotoItinerarioAPI = SegnalazioneFotoItinerarioRetrofitInstance
                                            .getSegnalazioneFotoItinerarioRetrofit(ElencoEndPoint.SEGNALAZIONE_FOTO_ITINERARIO)
                                            .create(SegnalazioneFotoItinerarioAPI.class);
    }

    public void getListaSegnalazioniStessaFoto(String idFotoItinerario, CallBackSegnalazioneFotoItinerario callBackSegnalazioneFotoItinerario){
        segnalazioneFotoItinerarioAPI.getSegnalazioniStessaFoto(idFotoItinerario).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<List<SegnalazioneFotoItinerario>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(SegnalazioneFotoItinerario_TAG, "onSubscribe: Lista delle segnalazioni totali di uno stessa foto");
                    }

                    @Override
                    public void onSuccess(@NonNull List<SegnalazioneFotoItinerario> segnalazioneFotoItinerarios) {
                        callBackSegnalazioneFotoItinerario.onSuccessList(segnalazioneFotoItinerarios);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        callBackSegnalazioneFotoItinerario.onFailure(e);
                    }
                });
    }

    public void aggiungiSegnalazione(SegnalazioneFotoItinerario segnalazione, CallBackSegnalazioneFotoItinerario callBackSegnalazioneFotoItinerario){
        segnalazioneFotoItinerarioAPI.aggiungiNuovaSegnalazione(segnalazione).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<Response<Void>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(SegnalazioneFotoItinerario_TAG, "onSubscribe: Aggiunta di una nuova segnalazione nel db");
                    }

                    @Override
                    public void onSuccess(@NonNull Response<Void> voidResponse) {
                        Log.d(SegnalazioneFotoItinerario_TAG, "onSuccess: Aggiunta di una nuova segnalazione completata");
                        callBackSegnalazioneFotoItinerario.onSuccessResponse(voidResponse);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(SegnalazioneFotoItinerario_TAG, "onError: Aggiunta di una nuova segnalazione fallita " + e.getLocalizedMessage());
                        callBackSegnalazioneFotoItinerario.onFailure(e);
                    }
                });

    }
}