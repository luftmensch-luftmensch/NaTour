/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.presenter.request;

import com.natour.model.Itinerario;
import com.natour.presenter.api.ItinerarioAPI;
import com.natour.presenter.callbackInterface.CallBackItinerario;
import com.natour.presenter.retrofit.ItinerarioRetrofitInstance;
import com.natour.utils.constants.ElencoEndPoint;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import android.util.Log;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import retrofit2.Response;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItinerarioRequest {
    private static final String Itinerario_TAG = "ItinerarioRequest";
    private static ItinerarioAPI itinerarioAPI;

    public ItinerarioRequest() {
        itinerarioAPI = ItinerarioRetrofitInstance.getItinerarioRetrofit(ElencoEndPoint.ITINERARIO).create(ItinerarioAPI.class);
    }

    public void getItinerari(CallBackItinerario callBackItinerario){
        itinerarioAPI.getAllItinerari().subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<List<Itinerario>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(Itinerario_TAG, "onSubscribe: Lista degli itinerari del db");
                    }
                    @Override
                    public void onSuccess(@NonNull List<Itinerario> itinerarios) {
                        callBackItinerario.onSuccessList(itinerarios);
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                        callBackItinerario.onFailure(e);
                    }
                });
    }

    // GET singolo itinerario
    public void getSingoloItinerario(String nomeItinerario, CallBackItinerario callBackItinerario){
        itinerarioAPI.getSingoloItinerario(nomeItinerario).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<Itinerario>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(Itinerario_TAG, "onSubscribe: Get singolo itinerario in base al nome nel db");
                    }

                    @Override
                    public void onSuccess(@NonNull Itinerario itinerario) {
                        callBackItinerario.onSuccess(itinerario);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        callBackItinerario.onFailure(e);
                    }
                });
    }

    // GET itinerari dal più recente
    public void getItinerariDescOrder(CallBackItinerario callBackItinerario){
        itinerarioAPI.getItinerariDescOrder().subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<List<Itinerario>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(Itinerario_TAG, "onSubscribe: Lista degli itinerari dal più recente del db");
                    }
                    @Override
                    public void onSuccess(@NonNull List<Itinerario> itinerarios) {
                        callBackItinerario.onSuccessList(itinerarios);
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                        callBackItinerario.onFailure(e);
                    }
                });
    }

    // Getter con filtri (più recente, nome itinerario, zona geografica, durata, stesso autore)
    public void getItinerariByNomeItinerario(String nomeItinerario, CallBackItinerario callBackItinerario){
        itinerarioAPI.getItinerariByNomeItinerario(nomeItinerario).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<List<Itinerario>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(Itinerario_TAG, "onSubscribe: Lista di itinerari in base al nome nel db");
                    }
                    @Override
                    public void onSuccess(@NonNull List<Itinerario> itinerarios) {
                        callBackItinerario.onSuccessList(itinerarios);
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                        callBackItinerario.onFailure(e);
                    }
                });
    }

    public void getItinerariByZonaGeografica(String zonaGeografica, CallBackItinerario callBackItinerario){
        itinerarioAPI.getItinerariByZonaGeografica(zonaGeografica).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<List<Itinerario>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(Itinerario_TAG, "onSubscribe: Lista di itinerari in base alla zona geografica nel db");
                    }

                    @Override
                    public void onSuccess(@NonNull List<Itinerario> itinerarios) {
                        callBackItinerario.onSuccessList(itinerarios);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        callBackItinerario.onFailure(e);
                    }
                });
    }

    public void getItinerariByDurata(String durata, CallBackItinerario callBackItinerario){
        itinerarioAPI.getItinerariByDurata(durata).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<List<Itinerario>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(Itinerario_TAG, "onSubscribe: Lista di itinerari in base alla durata nel db");
                    }
                    @Override
                    public void onSuccess(@NonNull List<Itinerario> itinerarios) {
                        callBackItinerario.onSuccessList(itinerarios);
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                        callBackItinerario.onFailure(e);
                    }
                });
    }

    public void getItinerariByUtente(String utenteProprietario, CallBackItinerario callBackItinerario){
        itinerarioAPI.getItinerarioByUtente(utenteProprietario).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<List<Itinerario>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(Itinerario_TAG, "onSubscribe: Lista di itinerari in base all'utente proprietario nel db");
                    }

                    @Override
                    public void onSuccess(@NonNull List<Itinerario> itinerarios) {
                        callBackItinerario.onSuccessList(itinerarios);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        callBackItinerario.onFailure(e);
                    }
                });
    }

    // Inserimento di un itinerario
    public void aggiungiItinerario(Itinerario itinerario, CallBackItinerario callBackItinerario){
        itinerarioAPI.inserisciNuovoItinerario(itinerario).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<Response<Void>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(Itinerario_TAG, "onSubscribe: Registrazione itinerario nel db");
                    }

                    @Override
                    public void onSuccess(@NonNull Response<Void> voidResponse) {
                        callBackItinerario.onSuccessResponse(voidResponse);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        callBackItinerario.onFailure(e);
                    }
                });

    }

    // Modifica di un itinerario pre-esistente
    public void aggiornaItinerario(Itinerario itinerario){
        itinerarioAPI.aggiornaItinerario(itinerario).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(Itinerario_TAG, "onSubscribe: Aggiornamento di un itinerario nel db");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(Itinerario_TAG, "onComplete: Itinerario aggiornato correttamente");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(Itinerario_TAG, "onError: Modifica dell'itinerario fallito " + e.getLocalizedMessage());
                    }
                });

    }

    // Eliminazione di un itinerario
    public void eliminaItinerario(String nomeItinerario){
        itinerarioAPI.eliminaItinerario(nomeItinerario).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(Itinerario_TAG, "onSubscribe Eliminazione di un itinerario dal db");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(Itinerario_TAG, "onComplete: Eliminazione dell'itinerario completato");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(Itinerario_TAG, "onError: Eliminazione dell'itinerario fallito " + e.getLocalizedMessage());
                    }
                });
    }
}