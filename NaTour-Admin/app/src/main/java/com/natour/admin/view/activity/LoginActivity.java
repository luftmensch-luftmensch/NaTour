/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.admin.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import com.google.android.material.button.MaterialButton;
import com.natour.admin.R;
import com.natour.admin.presenter.amplify.AmplifyRequest;
import com.natour.admin.presenter.callbackInterface.CallBackAmplify;

import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity {
    private static final String LoginActivity_TAG = "LoginActivity";

    private ConstraintLayout LoginLayout;
    private ImageView LogoImageView;

    private EditText UsernameEditText, PasswordEditText;

    private MaterialButton ConfermaLoginButton;

    private String username, password;

    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setUI();
    }

    private void setUI () {
        LoginLayout = findViewById(R.id.LoginLayout);
        LogoImageView = findViewById(R.id.LogoImageView_login);

        // Utilizziamo EditText invece di TextInputLayout per la gestione dei dati inseriti dall'utente
        // -> evitiamo in questo modo di dover gestire dei NullPointerException
        UsernameEditText = findViewById(R.id.UsernameEditText_login);
        PasswordEditText = findViewById(R.id.PasswordEditText_login);

        ConfermaLoginButton = findViewById(R.id.ConfermaButton_login);

        LogoImageView.setOnClickListener(view -> {
            String url = "https://natour2022.netlify.app/";
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        });

    ConfermaLoginButton.setOnClickListener(view -> {
            username = UsernameEditText.getText().toString();
            password = PasswordEditText.getText().toString();
            Log.d(LoginActivity_TAG, "Dati dell'utente: " + username + " " +  password);
            if (controlloCampi(username, password)){
                AmplifyRequest login = new AmplifyRequest();
                login.completaLogin(username, password, new CallBackAmplify() {
                    @Override
                    public void onSuccess() {
                        handler = new Handler();
                        runnable = () -> {
                            UsernameEditText.setText("");
                            PasswordEditText.setText("");

                            Toasty.success(getApplicationContext(),
                                    "Benvenuto!",
                                    Toasty.LENGTH_SHORT,
                                    true).show();
                            startActivity(new Intent(LoginActivity.this, BottomNavigationViewActivity.class));
                        };
                        handler.postDelayed(runnable, 3000);
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        // NB: L'errore è in inglese
                        Log.d(LoginActivity_TAG, "onFailure: Impossibile completare la login: " + throwable.getLocalizedMessage());
                        Toasty.error(getApplicationContext(), "Attenzione! Impossibile completare l'accesso!",
                                Toasty.LENGTH_SHORT, true).show();

                    }
                });

            }
        });

    }

    private boolean controlloCampi(String username, String password){
        if((username.isEmpty()) || (password.isEmpty())) {
            Toasty.warning(this, "Attenzione! Campi incompleti", Toasty.LENGTH_SHORT, true).show();
            return false;
        }
        return true;
    }

    private void attivaCampi(boolean value) {
        UsernameEditText.setEnabled(value);
        PasswordEditText.setEnabled(value);
        LogoImageView.setEnabled(value);
        ConfermaLoginButton.setEnabled(value);
    }

    private void avviaAnimazione(ViewGroup popup, Transition transition, boolean visible) {
        TransitionManager.beginDelayedTransition((ViewGroup) popup.getParent(), transition);
        popup.setVisibility(visible ? View.VISIBLE : View.GONE);
    }
}