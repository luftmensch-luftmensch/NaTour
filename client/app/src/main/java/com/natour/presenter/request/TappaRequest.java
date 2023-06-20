/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.presenter.request;

import android.util.Log;

import com.natour.model.Tappa;
import com.natour.presenter.api.TappaAPI;
import com.natour.presenter.callbackInterface.CallBackTappa;
import com.natour.presenter.retrofit.TappaRetrofitInstance;
import com.natour.utils.constants.ElencoEndPoint;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.CompletableObserver;
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
public class TappaRequest {
    private static String Tappa_TAG = "TappaRequest";
    private TappaAPI tappaAPI;

    public TappaRequest() {
        tappaAPI = TappaRetrofitInstance.getTappaRetrofit(ElencoEndPoint.TAPPA).create(TappaAPI.class);
    }


    // GET: Lista delle tappe totali -> da usare per la parte admin
    public void getTappe(CallBackTappa callBackTappa){
        tappaAPI.getAllTappe().subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<List<Tappa>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(Tappa_TAG, "onSubscribe: Lista delle tappe del db");
                    }
                    @Override
                    public void onSuccess(@NonNull List<Tappa> tappas) {
                        callBackTappa.onSuccessList(tappas);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        callBackTappa.onFailure(e);
                    }
                });
    }

    // GET delle tappe di uno stesso itinerario
    public void getTappeStessoItinerario(String nomeItinerario, CallBackTappa callBackTappa){
        tappaAPI.getTappeStessoItinerario(nomeItinerario).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<List<Tappa>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(Tappa_TAG, "onSubscribe: Lista delle tappe di uno stesso itinerario del db");
                    }
                    @Override
                    public void onSuccess(@NonNull List<Tappa> tappas) {
                        callBackTappa.onSuccessList(tappas);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        callBackTappa.onFailure(e);
                    }
                });
    }

    // Inserimento di una tappa
    public void aggiungiTappa(Tappa tappa){
        tappaAPI.inserisciNuovaTappa(tappa).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(Tappa_TAG, "onSubscribe: Aggiunta di una tappa del db");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(Tappa_TAG, "onComplete: Inserimento di una nuova tappa completato");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(Tappa_TAG, "onError: Errore nell'inserimento di una nuova tappa" + e.getLocalizedMessage());
                    }
                });


    }

    public void eliminaTappa(Long idTappa){
        tappaAPI.eliminaTappa(idTappa).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(Tappa_TAG, "onSubscribe: Eliminazione della tappa del db");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(Tappa_TAG, "onComplete: Tappa eliminata correttamente");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(Tappa_TAG, "onError: Eliminazione della tappa fallita " + e.getLocalizedMessage());
                    }
                });
    }
}