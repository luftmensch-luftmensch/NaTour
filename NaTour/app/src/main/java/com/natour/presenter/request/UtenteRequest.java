/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.presenter.request;

import android.util.Log;

import com.natour.model.Utente;
import com.natour.presenter.api.UtenteAPI;
import com.natour.presenter.callbackInterface.CallBackUtente;
import com.natour.presenter.retrofit.UtenteRetrofitInstance;
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
public class UtenteRequest {
    private static final String Utente_TAG = "UtenteRequest";
    private static UtenteAPI utenteAPI;

    public UtenteRequest() {
        utenteAPI = UtenteRetrofitInstance.getUtenteRetrofit(ElencoEndPoint.UTENTE).create(UtenteAPI.class);
    }

    public void getAllUtenti(CallBackUtente callBackUtente){
        utenteAPI.getAllUtenti().subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<List<Utente>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(Utente_TAG, "onSubscribe: Lista degli utenti del db");
                    }
                    @Override
                    public void onSuccess(@NonNull List<Utente> utenti) {
                        callBackUtente.onSuccessList(utenti);
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                        callBackUtente.onFailure(e);
                    }
                });
    }
    public void getSingoloUtente(String username, CallBackUtente callBackUtente){
        utenteAPI.getSingoloUtente(username).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<Utente>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(Utente_TAG, "onSubscribe: Get singolo utente dal db");
                    }

                    @Override
                    public void onSuccess(@NonNull Utente utente) {
                        callBackUtente.onSuccess(utente);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        callBackUtente.onFailure(e);
                    }
                });
    }

    public void aggiungiUtente(Utente utente){
        utenteAPI.inserisciNuovoUtente(utente).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(Utente_TAG, "onSubscribe: Inserimento utente nel db");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(Utente_TAG,"onComplete: Utente inserito correttamente");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(Utente_TAG, "onFailure: Errore nell'inserimento dell'utente nel db" + e.getLocalizedMessage());
                    }
                });
    }

    public void eliminaUtente(String username){
        utenteAPI.eliminaUtente(username).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(Utente_TAG, "onSubscribe: Eliminazione utente dal db");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(Utente_TAG, "onComplete: Eliminazione dell'utente");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(Utente_TAG, "onFailure: Eliminazione fallita" + e.getLocalizedMessage());

                    }
                });
    }

    public void aggiornaUtente(Utente utenteDaAggiornare){
        utenteAPI.aggiornaUtente(utenteDaAggiornare).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(Utente_TAG, "onSubscribe: Aggiornamento utente nel db");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(Utente_TAG, "onComplete: Aggiornamento dell'utente");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(Utente_TAG, "onFailure: Fallito aggiornamento dell'utente" + e.getLocalizedMessage());
                    }
                });
    }
}