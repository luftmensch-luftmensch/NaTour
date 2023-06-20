/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.presenter.request;

import android.util.Log;

import com.natour.model.TagRicerca;
import com.natour.presenter.api.TagRicercaAPI;
import com.natour.presenter.callbackInterface.CallBackTagRicerca;
import com.natour.presenter.retrofit.TagRicercaRetrofitInstance;
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
import retrofit2.Response;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TagRicercaRequest {
    private static final String TagRicerca_TAG = "TagRicerca_REQUEST";
    private TagRicercaAPI tagRicercaAPI;
    //private Context context;

    public TagRicercaRequest(){
        //this.context = context;
        tagRicercaAPI = TagRicercaRetrofitInstance.getTagRicercaRetrofit(ElencoEndPoint.TAG_RICERCA).create(TagRicercaAPI.class);
    }

    // GET dei tag di uno stesso itinerario
    public void getTagsStessoItinerario(String nomeItinerari, CallBackTagRicerca callBackTagRicerca){
        tagRicercaAPI.getTagStessoItinerario(nomeItinerari).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<List<TagRicerca>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(TagRicerca_TAG, "onSubscribe: Lista dei tag di uno stesso itinerario presenti del db");
                    }

                    @Override
                    public void onSuccess(@NonNull List<TagRicerca> tagRicercas) {
                        callBackTagRicerca.onSuccessList(tagRicercas);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        callBackTagRicerca.onFailure(e);
                    }
                });
    }

    // Inserimento di un nuovo tag
    public void aggiungiTag(TagRicerca tagRicerca, CallBackTagRicerca callBackTagRicerca){
        tagRicercaAPI.aggiungiNuovoTag(tagRicerca).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<Response<Void>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(TagRicerca_TAG, "onSubscribe: Aggiunta di un tag nel db");
                    }

                    @Override
                    public void onSuccess(@NonNull Response<Void> voidResponse) {
                        Log.d(TagRicerca_TAG, "onComplete: Aggiunta di un tag nel db completato");
                        callBackTagRicerca.onSuccessResponse(voidResponse);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(TagRicerca_TAG, "onError: Errore nell'inserimento di un nuovo tag nel db fallito" + e.getLocalizedMessage());
                        callBackTagRicerca.onFailure(e);
                    }
                });

    }

    // DELETE: Eliminazione di un tag
    public void eliminaTag(Long idTagRicerca){
        tagRicercaAPI.eliminaTag(idTagRicerca).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(TagRicerca_TAG, "onSubscribe: Eliminazione del tag del db");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TagRicerca_TAG, "onComplete: Tag eliminato correttamente");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(TagRicerca_TAG, "onError: Eliminazione del tag fallita " + e.getLocalizedMessage());
                    }
                });
    }
}