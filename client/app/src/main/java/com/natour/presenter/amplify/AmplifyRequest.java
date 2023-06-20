/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.presenter.amplify;

// Amplify
import com.amplifyframework.auth.AuthProvider;
import com.amplifyframework.auth.AuthUserAttribute;
import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.options.AuthSignOutOptions;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.auth.result.AuthResetPasswordResult;
import com.amplifyframework.auth.result.AuthSignInResult;
import com.amplifyframework.auth.result.AuthSignUpResult;
import com.amplifyframework.rx.RxAmplify;
import com.natour.model.Utente;
import com.natour.presenter.callbackInterface.CallBackAmplify;
import com.natour.presenter.request.UtenteRequest;

import android.app.Activity;
import android.util.Log;

import java.util.List;

import com.natour.utils.persistence.LocalUser;
import com.natour.utils.persistence.LocalUserDbManager;

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
public class AmplifyRequest {
    // Per il controllo delle eccezioni lanciate da Amplify -> https://stackoverflow.com/questions/65755097/handling-aws-amplify-onerror-callback-exceptions

    private static final String AmplifyRequest_TAG = "AmplifyRequest";
    // REGISTRAZIONE
    public void completaRegistrazione(String username, String password, String email, CallBackAmplify callBackAmplify){
        // Invio dati ad Amplify
        AuthSignUpOptions options = AuthSignUpOptions.builder()
                .userAttribute(AuthUserAttributeKey.email(), email)
                .build();
        RxAmplify.Auth.signUp(username, password, options).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<AuthSignUpResult>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(AmplifyRequest_TAG, "onSubScribe: AmplifyRequestRegistrazione");
                    }

                    @Override
                    public void onSuccess(@NonNull AuthSignUpResult authSignUpResult) {
                        Log.d(AmplifyRequest_TAG, "onSuccess: Registrazione completata con successo");
                        callBackAmplify.onSuccess();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(AmplifyRequest_TAG, "onError: Registrazione non completata: " + e.getLocalizedMessage());
                        callBackAmplify.onFailure(e);
                    }
                });

    }

    // Codice di conferma per la registrazione
    public void invioCodiceDiConferma(String username, String codice, CallBackAmplify callBackAmplify){
        Log.e(AmplifyRequest_TAG, "InfoCodiceDiConferma" + username);
        Log.e(AmplifyRequest_TAG, "InfoCodiceDiConferma" + codice);

        RxAmplify.Auth.confirmSignUp(username,codice).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<AuthSignUpResult>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(AmplifyRequest_TAG, "onSubScribe: AmplifyRequestCodiceDiConferma");
                    }

                    @Override
                    public void onSuccess(@NonNull AuthSignUpResult authSignUpResult) {
                        Log.d(AmplifyRequest_TAG, "onSuccess: Registrazione confermata con successo");
                        callBackAmplify.onSuccess();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(AmplifyRequest_TAG, "onError: (Codice di conferma) Registrazione non completata: " + e.getLocalizedMessage());
                        callBackAmplify.onFailure(e);
                    }
                });

    }


    // REGISTRAZIONE-LOGIN con provider (Google)
    public void accessoConGoogle(Activity activity, CallBackAmplify callBackAmplify){
        RxAmplify.Auth.signInWithSocialWebUI(AuthProvider.google(), activity).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<AuthSignInResult>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(AmplifyRequest_TAG, "onSubScribe: AmplifyRequestGoogle");
                    }

                    @Override
                    public void onSuccess(@NonNull AuthSignInResult authSignInResult) {
                        Log.d(AmplifyRequest_TAG, "onSuccess: Accesso completato con successo");
                        if(authSignInResult.isSignInComplete()){
                            callBackAmplify.onSuccess();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(AmplifyRequest_TAG, "onError  Accesso non completato: " + e.getLocalizedMessage());
                        // In caso di errore avvisiamo l'utente
                        callBackAmplify.onFailure(e);
                    }
                });
    }

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
                            // In caso di avvenuto login permettiamo all'utente di accedere all'applicazione
                            callBackAmplify.onSuccess();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(AmplifyRequest_TAG, "onError: Login non completato: " + e.getLocalizedMessage());
                        // In caso di fallito login avvisiamo l'utente dell'errore
                        callBackAmplify.onFailure(e);
                    }
                });

    }

    public void completaRecuperoPassword(String username) {
        RxAmplify.Auth.resetPassword(username)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<AuthResetPasswordResult>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(AmplifyRequest_TAG, "onSubscribe - completaRecuperoPassword");
                    }

                    @Override
                    public void onSuccess(@NonNull AuthResetPasswordResult authResetPasswordResult) {
                        Log.d(AmplifyRequest_TAG, "onSuccess - completaRecuperoPassword: " + authResetPasswordResult);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(AmplifyRequest_TAG, "onError - completaRecuperoPassword: " + e.getLocalizedMessage());
                    }
                });
    }

    public void invioCodiceDiConfermaRecuperoPassword (String nuovaPassword, String codiceConferma) {
        RxAmplify.Auth.confirmResetPassword(nuovaPassword, codiceConferma)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(AmplifyRequest_TAG, "onSubscribe - invioCodiceDiConfermaRecuperoPassword");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(AmplifyRequest_TAG, "onComplete - invioCodiceDiConfermaRecuperoPassword");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(AmplifyRequest_TAG, "onError - invioCodiceDiConfermaRecuperoPassword: " + e.getLocalizedMessage());
                    }
                });
    }

    /*
        GOOGLE -> Cognito crea gli utenti google iniziando con google_****
        Utente classico -> Controllare l'username
    */
    public void insertLocalUser(LocalUserDbManager localUserDbManager){
        LocalUser utenteLocale = new LocalUser();
        Utente utenteDaInserire = new Utente();
        RxAmplify.Auth.fetchUserAttributes().subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<AuthUserAttribute>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(AmplifyRequest_TAG, "onSubScribe: Fetch degli attibuti dell'utente loggato da inserire nel db locale");
                    }

                    @Override
                    public void onSuccess(@NonNull List<AuthUserAttribute> authUserAttributes) {
                        Log.d(AmplifyRequest_TAG, "onSuccess: Fetch degli attributi ed inserimento dell'utente nel db locale");

                        utenteLocale.setId(1);
                        utenteLocale.setUsername(RxAmplify.Auth.getCurrentUser().getUsername());
                        utenteLocale.setUrlFotoProfilo(RxAmplify.Auth.getCurrentUser().getUsername() + "_url");
                        utenteLocale.setIsLoggedWithGoogle("NO");

                        Log.d(AmplifyRequest_TAG, "INSERIMENTO UTENTE NEL DB " + utenteLocale);

                        localUserDbManager.insert(utenteLocale);
                        localUserDbManager.closeDB();

                        utenteDaInserire.setUsername(RxAmplify.Auth.getCurrentUser().getUsername());
                        utenteDaInserire.setUrlFotoProfilo(RxAmplify.Auth.getCurrentUser().getUsername() + "_url");

                        UtenteRequest utenteRequest = new UtenteRequest();
                        utenteRequest.aggiungiUtente(utenteDaInserire);

                        Log.d(AmplifyRequest_TAG, "onSuccess: Inserimento completato");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(AmplifyRequest_TAG, "onError - insertLocalUser: " + e.getLocalizedMessage());
                    }
                });
    }

    public void insertLocalUserSignedWithGoogle(LocalUserDbManager localUserDbManager){
        LocalUser utenteLocaleWithGoogle = new LocalUser();
        Utente utenteWithGoogle = new Utente();
        RxAmplify.Auth.fetchUserAttributes().subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<List<AuthUserAttribute>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.d(AmplifyRequest_TAG, "onSubScribe: Fetch degli attibuti dell'utente loggato con google da inserire nel db locale");
                    }

                    @Override
                    public void onSuccess(@NonNull List<AuthUserAttribute> authUserAttributes) {
                        Log.d(AmplifyRequest_TAG, "onSuccess: Fetch degli attributi ed inserimento dell'utente nel db locale");
                        utenteLocaleWithGoogle.setId(1);
                        utenteLocaleWithGoogle.setUsername(RxAmplify.Auth.getCurrentUser().getUsername());
                        utenteLocaleWithGoogle.setUrlFotoProfilo(RxAmplify.Auth.getCurrentUser().getUsername() + "_url");
                        utenteLocaleWithGoogle.setIsLoggedWithGoogle("SI");
                        Log.d(AmplifyRequest_TAG, "INSERIMENTO UTENTE LOGGATO CON GOOGLE NEL DB" + utenteLocaleWithGoogle);

                        localUserDbManager.insert(utenteLocaleWithGoogle);
                        localUserDbManager.closeDB();

                        utenteWithGoogle.setUsername(RxAmplify.Auth.getCurrentUser().getUsername());
                        utenteWithGoogle.setUrlFotoProfilo(RxAmplify.Auth.getCurrentUser().getUsername()+"_url");

                        UtenteRequest utenteRequest = new UtenteRequest();
                        utenteRequest.aggiungiUtente(utenteWithGoogle);

                        Log.d(AmplifyRequest_TAG, "onSuccess: Inserimento completato");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(AmplifyRequest_TAG, "onError - insertLocalUser: " + e.getLocalizedMessage());
                    }
                });
    }


    // SIGN-OUT
    //Calling signOut without any options will just delete the local cache and keychain of the user.
    public void signOut(){
        Log.d(AmplifyRequest_TAG, "SignOut dell'utente loggato tramite Google");
        RxAmplify.Auth.signOut();
    }

    // Calling signout with globalSignOut = true will invalidate all the Cognito User Pool tokens of the signed in user.
    // If the user is signed into a device, they won't be authorized to perform a task that requires a valid token
    // when a global signout is called from some other device. They need to sign in again to get valid tokens.
    public void signOutGlobale(){
        // NB: Non funziona nel caso in cui ci si sia loggati con i provider web app (Google)
        Log.d(AmplifyRequest_TAG, "SignOut globale dell'utente loggato con mail");
        RxAmplify.Auth.signOut(
                AuthSignOutOptions.builder().globalSignOut(true).build()
        );

    }
}