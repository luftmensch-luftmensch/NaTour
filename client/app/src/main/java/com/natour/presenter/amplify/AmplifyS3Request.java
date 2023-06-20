/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.presenter.amplify;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.amplifyframework.rx.RxAmplify;
import com.amplifyframework.rx.RxStorageBinding;
import com.amplifyframework.storage.result.StorageGetUrlResult;
import com.amplifyframework.storage.result.StorageRemoveResult;
import com.amplifyframework.storage.result.StorageUploadInputStreamResult;
import com.natour.presenter.callbackInterface.CallBackGeneric;

import java.io.FileNotFoundException;
import java.io.InputStream;

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
public class AmplifyS3Request {
    private static final String AmplifyS3Request_TAG = "AmplifyS3Request";

    // GET: Get url dell'immagine data la chiave
    public void getUrlImmagine(String key, CallBackGeneric callBackGeneric){
        RxAmplify.Storage.getUrl(key).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<StorageGetUrlResult>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(AmplifyS3Request_TAG, "onSubscribe: Richiesta immagine dal bucket s3");
                    }

                    @Override
                    public void onSuccess(@NonNull StorageGetUrlResult storageGetUrlResult) {
                        Log.d(AmplifyS3Request_TAG, "onSuccess: Retrievial s3 url: " + storageGetUrlResult.getUrl());
                        callBackGeneric.onSuccess(String.valueOf(storageGetUrlResult.getUrl()));
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(AmplifyS3Request_TAG, "onSuccess: Retrievial s3 url fallito: " + e.getLocalizedMessage());
                        callBackGeneric.onFailure(e);
                    }
                });
    }

    // POST: Caricamento di un'immagine definendo una chiave
    public void uploadImage(Uri uri, String key, Context context, CallBackGeneric callBackGeneric){
        try{
            InputStream inputStream = context.getContentResolver().openInputStream(uri);

            RxStorageBinding.RxProgressAwareSingleOperation<StorageUploadInputStreamResult> immagineDaCaricare = RxAmplify.Storage.uploadInputStream(key, inputStream);

            immagineDaCaricare.observeResult().subscribe(new SingleObserver<StorageUploadInputStreamResult>() {
                @Override
                public void onSubscribe(@NonNull Disposable d) {

                }

                @Override
                public void onSuccess(@NonNull StorageUploadInputStreamResult storageUploadInputStreamResult) {
                    Log.d(AmplifyS3Request_TAG, " onSuccess: S3 - Upload immagine " + storageUploadInputStreamResult.getKey());
                    callBackGeneric.onSuccess(storageUploadInputStreamResult.toString());
                }

                @Override
                public void onError(@NonNull Throwable e) {
                    Log.d(AmplifyS3Request_TAG, "onError: S3 - Upload immagine fallito: " + e.getLocalizedMessage());
                    callBackGeneric.onFailure(e);
                }
            });


        }catch (FileNotFoundException f){
            Log.d(AmplifyS3Request_TAG, "Impossibile trovare l'immagine" + f.getLocalizedMessage());
        }


    }

    // DELETE: Rimozione di un'immagine data una chiave
    public void removeImage(String key){
        RxAmplify.Storage.remove(key).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<StorageRemoveResult>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(AmplifyS3Request_TAG, "onSubscribe: Eliminazione di un'immagine da s3");
                    }

                    @Override
                    public void onSuccess(@NonNull StorageRemoveResult storageRemoveResult) {
                        Log.d(AmplifyS3Request_TAG, "onSuccess: Eliminazione dell'immagine da s3 completata:" + storageRemoveResult.getKey());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(AmplifyS3Request_TAG, "onError: Eliminazione dell'immagine da s3 fallita: " + e.getLocalizedMessage());
                    }
                });
    }
}