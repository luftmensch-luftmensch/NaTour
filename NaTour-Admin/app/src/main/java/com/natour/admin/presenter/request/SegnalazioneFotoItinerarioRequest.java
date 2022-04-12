/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.admin.presenter.request;

import android.util.Log;

import com.natour.admin.model.SegnalazioneFotoItinerario;
import com.natour.admin.presenter.api.SegnalazioneFotoItinerarioAPI;
import com.natour.admin.presenter.callbackInterface.CallBackSegnalazioneFotoItinerario;
import com.natour.admin.presenter.retrofit.SegnalazioneFotoItinerarioRetrofitInstance;
import com.natour.admin.utils.constants.ElencoEndPoint;

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

    public void getSegnalazioniTotali(CallBackSegnalazioneFotoItinerario callBackSegnalazioneFotoItinerario){
        segnalazioneFotoItinerarioAPI.getAllSegnalazioni().subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<List<SegnalazioneFotoItinerario>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(SegnalazioneFotoItinerario_TAG, "onSubscribe: Lista delle segnalazioni totali presenti del db");
                    }
                    @Override
                    public void onSuccess(@NonNull List<SegnalazioneFotoItinerario> segnalazioneFotoItinerarios) { callBackSegnalazioneFotoItinerario.onSuccess(segnalazioneFotoItinerarios); }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        callBackSegnalazioneFotoItinerario.onFailure(e);
                    }
                });
    }
}
