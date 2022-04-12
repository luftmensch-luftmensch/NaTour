/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.admin.presenter.request;

import android.util.Log;

import com.natour.admin.model.Admin;
import com.natour.admin.presenter.api.AdminAPI;
import com.natour.admin.presenter.callbackInterface.CallBackAdmin;
import com.natour.admin.presenter.callbackInterface.CallBackStatistiche;
import com.natour.admin.presenter.retrofit.AdminRetrofitInstance;
import com.natour.admin.utils.constants.ElencoEndPoint;

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
public class AdminRequest {
    private static final String AdminRequest_TAG = "AdminRequest";
    private static AdminAPI adminAPI;

    public AdminRequest(){
        adminAPI = AdminRetrofitInstance.getAdminRetrofit(ElencoEndPoint.ADMIN).create(AdminAPI.class);
    }

    public void getSingoloAdmin(String username, CallBackAdmin callBackAdmin){
        adminAPI.getSingoloAdmin(username).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<Admin>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(AdminRequest_TAG, "onSubscribe: Get singolo admin dal db");
                    }

                    @Override
                    public void onSuccess(@NonNull Admin admin) {
                        callBackAdmin.onSuccess(admin);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        callBackAdmin.onFailure(e);
                    }
                });
    }

    public void getStatistiche(CallBackStatistiche callBackStatistiche){
        adminAPI.getStatistiche().subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<List<Integer>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(AdminRequest_TAG, "onSubscribe: Get Lista statistiche dal db");
                    }

                    @Override
                    public void onSuccess(@NonNull List<Integer> integers) {
                        callBackStatistiche.onSuccess(integers);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        callBackStatistiche.onFailure(e);

                    }
                });

    }

    public void aggiungiAdmin(Admin admin){
        adminAPI.inserisciNuovoAdmin(admin).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(AdminRequest_TAG, "onSubscribe: Inserimento admin nel db");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(AdminRequest_TAG, "onComplete: Admin inserito correttamente");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(AdminRequest_TAG, "onFailure: Errore nell'inseriemnto dell'admin nel db" + e.getLocalizedMessage());
                    }
                });
    }

    public void eliminaAdmin(String username){
        adminAPI.eliminaAdmin(username).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(AdminRequest_TAG, "onSubscribe: Eliminazione admin dal db");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(AdminRequest_TAG, "onComplete: Eliminazione dell'admin");

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(AdminRequest_TAG, "onFailure: Eliminazione fallita" + e.getLocalizedMessage());
                    }
                });
    }

    public void aggiornaDatiAdmin(Admin adminDaAggiornare){
        adminAPI.aggiornaAdmin(adminDaAggiornare).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(AdminRequest_TAG, "onSubscribe: Aggiornamento admin nel db");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(AdminRequest_TAG, "onComplete: Aggiornamento dell'admin");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(AdminRequest_TAG, "onFailure: Fallito aggiornamento dell'admin" + e.getLocalizedMessage());
                    }
                });
    }
}
