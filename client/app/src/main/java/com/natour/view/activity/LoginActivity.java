/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.view.activity;

// Componenti
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.rx.RxAmplify;
import com.google.android.material.button.MaterialButton;
import com.natour.R;

import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.transition.Slide;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;
import androidx.transition.TransitionSet;

// Amplify
import com.natour.presenter.amplify.AmplifyRequest;
import com.natour.presenter.callbackInterface.CallBackAmplify;

// Toasty
import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity {
    private static final String LoginActivity_TAG = "LoginActivity";

    private ConstraintLayout LoginLayout;

    private ImageView LogoImageView;
    private EditText UsernameEditText, PasswordEditText;
    private TextView RecuperoPasswordTextView;
    private ImageButton GoogleLoginButton;
    private MaterialButton ConfermaLoginButton;
    private TextView SignUpTextView;

    private ConstraintLayout RecuperoPasswordPopup;

    private EditText UsernameRecuperoPasswordEditText;

    private MaterialButton ConfermaRecuperoPasswordButton;
    private MaterialButton AnnullaRecuperoPasswordButton;

    private ConstraintLayout NuovaPasswordPopup;
    private EditText NuovaPasswordEditText;
    private EditText ConfermaCodiceEditText;
    private MaterialButton ConfermaNuovaPasswordButton;
    private MaterialButton AnnullaNuovaPasswordButton;

    private String username, password;

    private Handler handler;
    private Runnable runnable;

    private Transition RecuperoPasswordTransition, NuovaPasswordTransition;
    private TransitionSet transitionSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setUI();
    }

    private void setUI(){
        LoginLayout = findViewById(R.id.LoginLayout);
        LogoImageView = findViewById(R.id.LogoImageView_login);

        // Utilizziamo EditText invece di TextInputLayout per la gestione dei dati inseriti dall'utente
        // -> evitiamo in questo modo di dover gestire dei NullPointerException
        UsernameEditText = findViewById(R.id.UsernameEditText_login);
        PasswordEditText = findViewById(R.id.PasswordEditText_login);

        GoogleLoginButton = findViewById(R.id.GoogleLoginButton);
        RecuperoPasswordTextView = findViewById(R.id.PasswordDimenticataTextView);
        ConfermaLoginButton = findViewById(R.id.ConfermaButton_login);
        SignUpTextView = findViewById(R.id.SignUpTextView);

        RecuperoPasswordPopup = findViewById(R.id.RecuperoPasswordPopup_login);

        // Utilizziamo EditText invece di TextInputLayout per la gestione dei dati inseriti dall'utente
        // -> evitiamo in questo modo di dover gestire dei NullPointerException
        UsernameRecuperoPasswordEditText = findViewById(R.id.RecuperoPasswordEditText_popup_login);

        AnnullaRecuperoPasswordButton = findViewById(R.id.AnnullaRecuperoPasswordButton_popup_login);
        ConfermaRecuperoPasswordButton = findViewById(R.id.ConfermaRecuperoPasswordButton_popup_login);

        NuovaPasswordPopup = findViewById(R.id.NuovaPasswordPopup_login);
        NuovaPasswordEditText = findViewById(R.id.NuovaPasswordEditText_popup_login);
        ConfermaCodiceEditText = findViewById(R.id.ConfermaCodiceEditText_popup_login);
        AnnullaNuovaPasswordButton = findViewById(R.id.AnnullaNuovaPasswordButton_popup_login);
        ConfermaNuovaPasswordButton = findViewById(R.id.ConfermaNuovaPasswordButton_popup_login);

        RecuperoPasswordTransition = new Slide(Gravity.BOTTOM)
                .setDuration(500)
                .addTarget(RecuperoPasswordPopup);

        NuovaPasswordTransition = new Slide(Gravity.BOTTOM)
                .setDuration(500)
                .addTarget(NuovaPasswordPopup);

        transitionSet = new TransitionSet()
                .setOrdering(TransitionSet.ORDERING_SEQUENTIAL)
                .addTransition(RecuperoPasswordTransition)
                .addTransition(NuovaPasswordTransition);

        LogoImageView.setOnClickListener(view -> {
            String url = "https://natour2022.netlify.app/";
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        });

        RecuperoPasswordTextView.setOnClickListener(view -> {
            avviaAnimazione(RecuperoPasswordPopup, RecuperoPasswordTransition, true);
            attivaCampi(false);
        });

        AnnullaRecuperoPasswordButton.setOnClickListener(click -> {
            UsernameRecuperoPasswordEditText.setText("");
            avviaAnimazione(RecuperoPasswordPopup, RecuperoPasswordTransition, false);
            attivaCampi(true);
        });

        ConfermaRecuperoPasswordButton.setOnClickListener(click -> {
            String usernameRecuperoPassword = UsernameRecuperoPasswordEditText.getText().toString();

            if (usernameRecuperoPassword.isEmpty()) {
                Toasty.warning(this,
                        "Attenzione! Inserisci lo username prima di confermare",
                        Toasty.LENGTH_SHORT,
                        true).show();
            } else {
                AmplifyRequest amplifyRequest = new AmplifyRequest();
                amplifyRequest.completaRecuperoPassword(usernameRecuperoPassword);

                UsernameRecuperoPasswordEditText.setText("");
                TransitionManager.beginDelayedTransition(LoginLayout, transitionSet);
                RecuperoPasswordPopup.setVisibility(View.GONE);
                NuovaPasswordPopup.setVisibility(View.VISIBLE);
                attivaCampi(false);
            }
        });

        AnnullaNuovaPasswordButton.setOnClickListener(click -> {
            NuovaPasswordEditText.setText("");
            ConfermaCodiceEditText.setText("");
            avviaAnimazione(NuovaPasswordPopup, NuovaPasswordTransition, false);
            attivaCampi(true);
        });

        ConfermaNuovaPasswordButton.setOnClickListener(click -> {
            String nuovaPassword = NuovaPasswordEditText.getText().toString();
            String codiceConferma = ConfermaCodiceEditText.getText().toString();
            boolean response = controlloRecuperoPassword(nuovaPassword, codiceConferma);
            if(response){
                AmplifyRequest amplifyRequest = new AmplifyRequest();
                amplifyRequest.invioCodiceDiConfermaRecuperoPassword(nuovaPassword, codiceConferma);

                NuovaPasswordEditText.setText("");
                ConfermaCodiceEditText.setText("");
                avviaAnimazione(NuovaPasswordPopup, NuovaPasswordTransition, false);
                attivaCampi(true);
            }

        });

        GoogleLoginButton.setOnClickListener(view -> {
            AmplifyRequest accessoConGoogle = new AmplifyRequest();
            accessoConGoogle.accessoConGoogle(LoginActivity.this, new CallBackAmplify() {
                @Override
                public void onSuccess() {
                    handler = new Handler();
                    runnable = () -> {
                        Toasty.success(getApplicationContext(), "Benvenuto!", Toasty.LENGTH_SHORT, true).show();

                        // Per una migliore gestione degli inserimenti degli utenti nel db locale definiamo 2 casi (passiamo il valore all'intent)
                        Intent intent = new Intent(LoginActivity.this, BottomNavigationViewActivity.class);
                        intent.putExtra("LoggedAs", "Google");

                        view.getContext().startActivity(intent);
                    };
                    handler.postDelayed(runnable, 3000);
                }

                @Override
                public void onFailure(Throwable throwable) {
                    Log.d(LoginActivity_TAG, "onFailure: Impossibile completare l'accesso con Google: " + throwable.getLocalizedMessage());
                    Toasty.error(getApplicationContext(), "Attenzione! Impossibile completare l'accesso alla piattaforma",
                            Toasty.LENGTH_SHORT, true).show();

                }
            });

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
                        Log.d(LoginActivity_TAG, "Login avvenuto con successo. Passaggio alla Home page");
                        handler = new Handler();
                        runnable = () -> {
                            UsernameEditText.setText("");
                            PasswordEditText.setText("");

                            Toasty.success(getApplicationContext(),
                                    "Benvenuto!",
                                    Toasty.LENGTH_SHORT,
                                    true).show();
                            Intent intent = new Intent(LoginActivity.this, BottomNavigationViewActivity.class);
                            intent.putExtra("LoggedAs", "NormalUser");
                            //startActivity(new Intent(LoginActivity.this, BottomNavigationViewActivity.class));
                            view.getContext().startActivity(intent);
                        };
                        handler.postDelayed(runnable, 3000);
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.d(LoginActivity_TAG, "onFailure: Impossibile completare la login: " + throwable.getLocalizedMessage());
                        Toasty.error(getApplicationContext(), "Attenzione! Impossibile completare l'accesso!",
                                Toasty.LENGTH_SHORT, true).show();
                    }
                });
            }
        });

        SignUpTextView.setOnClickListener(view ->
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class))
        );
    }

    // Metodo che controlla che la nuova password sia valida
    private boolean controlloRecuperoPassword(String nuovaPassword, String codiceConferma){
        if ((nuovaPassword.isEmpty()) || (codiceConferma.isEmpty())) {
            Toasty.warning(this, "Attenzione! I campi segnati\nda * sono obbligatori", Toasty.LENGTH_SHORT, true).show();
            return false;
        }
       if (nuovaPassword.length() < 8) {
            Toasty.warning(this, "Attenzione! La password deve contenere almeno 8 caratteri", Toasty.LENGTH_SHORT, true).show();
            return false;
        }
       if (!nuovaPassword.matches("(.*[0-9].*)") | !nuovaPassword.matches("(.*[A-Z].*)") | !nuovaPassword.matches("^(?=.*[_.()$&@]).*$")) {
            Toasty.warning(this, "Attenzione! La password deve contenere almeno " + "un numero, una lettera maiuscola ed un simbolo", Toasty.LENGTH_SHORT, true).show();
            return false;
        }
        return true;
    }

    // Metodo per la gestione dei campi inseriti dall'utente
    private boolean controlloCampi(String username, String password){
        if((username.isEmpty()) || (password.isEmpty())) {
            Toasty.warning(this,
                    "Attenzione! I campi segnati\n" +
                            "da * sono obbligatori",
                    Toasty.LENGTH_SHORT,
                    true).show();
            return false;
        }

        return true;
    }

    // Gestione dell'animazione del popup per l'inserimento del codice di conferma
    private void attivaCampi(boolean value) {
        UsernameEditText.setEnabled(value);
        PasswordEditText.setEnabled(value);

        LogoImageView.setEnabled(value);
        GoogleLoginButton.setEnabled(value);
        ConfermaLoginButton.setEnabled(value);
        SignUpTextView.setEnabled(value);
    }

    private void avviaAnimazione(ViewGroup popup, Transition transition, boolean visible) {
        TransitionManager.beginDelayedTransition((ViewGroup) popup.getParent(), transition);
        popup.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    // Gestione dell'accesso con Google
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AWSCognitoAuthPlugin.WEB_UI_SIGN_IN_ACTIVITY_CODE) {
            RxAmplify.Auth.handleWebUISignInResponse(data);
        }
    }

}
