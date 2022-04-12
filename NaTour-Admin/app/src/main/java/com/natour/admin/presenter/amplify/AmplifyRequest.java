/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.admin.presenter.amplify;

import com.amplifyframework.auth.AuthUserAttribute;
import com.amplifyframework.auth.options.AuthSignOutOptions;
import com.amplifyframework.auth.result.AuthSignInResult;
import com.amplifyframework.rx.RxAmplify;
import android.util.Log;

import java.util.List;

import com.natour.admin.model.Admin;
import com.natour.admin.presenter.callbackInterface.CallBackAmplify;
import com.natour.admin.presenter.request.AdminRequest;
import com.natour.admin.utils.persistence.LocalAdmin;
import com.natour.admin.utils.persistence.LocalAdminDbManager;

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
public class AmplifyRequest {

    private static final String AmplifyRequest_TAG = "AmplifyRequest";

    // LOGIN
    public void completaLogin(String username, String password, CallBackAmplify callBackAmplify){
        RxAmplify.Auth.signIn(username, password).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<AuthSignInResult>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(AmplifyRequest_TAG, "onSubScribe: AmplifyRequestLogin");

                    }

                    @Override
                    public void onSuccess(@NonNull AuthSignInResult authSignInResult) {
                        if(authSignInResult.isSignInComplete()){
                            Log.d(AmplifyRequest_TAG,"onSuccess: Login completato con successo");
                            // In caso di avvenuto login permettiamo all'admin di accedere all'applicazione
                            callBackAmplify.onSuccess();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(AmplifyRequest_TAG, "onError: Login non completato: " + e.getLocalizedMessage());
                        // In caso di fallito login avvisiamo l'admin dell'errore
                        callBackAmplify.onFailure(e);
                    }
                });

    }

    public void insertAdminUser(LocalAdminDbManager localAdminDbManager){
        LocalAdmin adminLocale = new LocalAdmin();
        Admin admin = new Admin();
        RxAmplify.Auth.fetchUserAttributes().subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<List<AuthUserAttribute>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(AmplifyRequest_TAG, "onSubScribe: Fetch degli attibuti dell'admin loggato da inserire nel db locale");
                    }

                    @Override
                    public void onSuccess(@NonNull List<AuthUserAttribute> authUserAttributes) {
                        Log.d(AmplifyRequest_TAG, "onSuccess: Fetch degli attributi di " + RxAmplify.Auth.getCurrentUser().getUsername());

                        adminLocale.setId("1");
                        adminLocale.setUsername(RxAmplify.Auth.getCurrentUser().getUsername());
                        adminLocale.setUrlFotoProfilo(RxAmplify.Auth.getCurrentUser().getUsername() + "_url");

                        admin.setUsername(RxAmplify.Auth.getCurrentUser().getUsername());
                        admin.setUrlFotoProfilo(RxAmplify.Auth.getCurrentUser().getUsername() + "_url");

                        Log.d(AmplifyRequest_TAG, "INSERIMENTO ADMIN NEL DB " + adminLocale);

                        localAdminDbManager.insert(adminLocale);
                        localAdminDbManager.closeDB();


                        AdminRequest adminRequest = new AdminRequest();
                        adminRequest.aggiungiAdmin(admin);


                        Log.d(AmplifyRequest_TAG, "onSuccess: Inserimento completato");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(AmplifyRequest_TAG, "onError - insertAdminUser: " + e.getLocalizedMessage());
                    }
                });
    }

    // Calling signout with globalSignOut = true will invalidate all the Cognito User Pool tokens of the signed in user.
    // If the user is signed into a device, they won't be authorized to perform a task that requires a valid token
    // when a global signout is called from some other device. They need to sign in again to get valid tokens.
    public void signOutGlobale(){
        Log.d(AmplifyRequest_TAG, "SignOut globale dell'utente loggato con mail");
        RxAmplify.Auth.signOut(
                AuthSignOutOptions.builder().globalSignOut(true).build()
        );

    }
}
