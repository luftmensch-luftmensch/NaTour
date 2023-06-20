package com.natour.presenter.request;

import android.util.Log;

import com.natour.model.GalleriaFotoItinerario;
import com.natour.presenter.api.GalleriaFotoItinerarioAPI;
import com.natour.presenter.callbackInterface.CallBackGalleriaFotoItinerario;
import com.natour.presenter.retrofit.GalleriaFotoItinerarioRetrofitInstance;
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
public class GalleriaFotoItinerarioRequest {
    private static final String GalleriaFoto_TAG = "GalleriaFotoItinerarioRequest";
    private GalleriaFotoItinerarioAPI galleriaFotoItinerarioAPI;

    public GalleriaFotoItinerarioRequest(){
        galleriaFotoItinerarioAPI = GalleriaFotoItinerarioRetrofitInstance.getGalleriaFotoItinerarioRetrofit(ElencoEndPoint.GALLERIAFOTOITINERARIO).create(GalleriaFotoItinerarioAPI.class);
    }

    // GET: Lista delle foto di tutti gli itinerari presenti -> da usare per la parte admin
    public void getGalleriaFotoItinerariTotali(CallBackGalleriaFotoItinerario callBackGalleriaFotoItinerario){
        galleriaFotoItinerarioAPI.getAllFotoitinerari().subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<List<GalleriaFotoItinerario>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(GalleriaFoto_TAG, "onSubscribe: Lista di foto degli itinerari totali presenti nel db");
                    }
                    @Override
                    public void onSuccess(@NonNull List<GalleriaFotoItinerario> galleriaFotoItinerarios) {
                        callBackGalleriaFotoItinerario.onSuccessList(galleriaFotoItinerarios);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        callBackGalleriaFotoItinerario.onFailure(e);
                    }
                });
    }


    // GET: Lista delle foto di uno stesso itinerario
    public void getGalleriaFotoStessoItinerario(String nomeItinerario, CallBackGalleriaFotoItinerario callBackGalleriaFotoItinerario){
        galleriaFotoItinerarioAPI.getGalleriaFotoStessoItinerario(nomeItinerario).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<List<GalleriaFotoItinerario>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(GalleriaFoto_TAG, "onSubscribe: Lista di foto di uno stesso itinerario");
                    }

                    @Override
                    public void onSuccess(@NonNull List<GalleriaFotoItinerario> galleriaFotoItinerarios) {
                        callBackGalleriaFotoItinerario.onSuccessList(galleriaFotoItinerarios);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        callBackGalleriaFotoItinerario.onFailure(e);
                    }
                });

    }

    // POST: Inserimento di una foto di un itinerario
    public void aggiungiFoto(GalleriaFotoItinerario galleriaFotoItinerario, CallBackGalleriaFotoItinerario callBackGalleriaFotoItinerario){
        galleriaFotoItinerarioAPI.inserisciNuovaFotoItinerario(galleriaFotoItinerario).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<Response<Void>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(GalleriaFoto_TAG, "onSubscribe: Aggiunta della foto di un itinerario");
                    }

                    @Override
                    public void onSuccess(@NonNull Response<Void> voidResponse) {
                        Log.d(GalleriaFoto_TAG, "onSuccess: Inserimento foto completato");
                        callBackGalleriaFotoItinerario.onSuccessResponse(voidResponse);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(GalleriaFoto_TAG, "onError: Inserimento foto fallita" + e.getLocalizedMessage());
                        callBackGalleriaFotoItinerario.onFailure(e);
                    }
                });
    }

    public void eliminaFotoItinerario(Long idFotoItinerario){
        galleriaFotoItinerarioAPI.eliminaFotoItinerario(idFotoItinerario).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(GalleriaFoto_TAG, "Eliminazione foto di un itinerario nel db");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(GalleriaFoto_TAG, "onComplete: Eliminazione foto itinerario completata con successo");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(GalleriaFoto_TAG, "onError: Eliminazione foto itinerario fallita " + e.getLocalizedMessage());
                    }
                });
    }
}