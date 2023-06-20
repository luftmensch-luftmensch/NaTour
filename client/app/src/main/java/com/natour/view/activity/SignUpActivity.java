/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/

package com.natour.view.activity;

// Componenti
import android.animation.Animator;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.transition.Slide;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;
import androidx.transition.TransitionSet;
import androidx.transition.TransitionValues;
import androidx.transition.Visibility;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.natour.presenter.amplify.AmplifyRequest;
import com.natour.R;
import com.natour.presenter.callbackInterface.CallBackAmplify;

import es.dmoral.toasty.Toasty;

public class SignUpActivity extends AppCompatActivity {
    // Logging
    private static final String SignUpActivity_TAG = "SignupActivity";

    // Necessarie alla signup con Amplify
    private String username, password, confermaPassword, email;

    private ConstraintLayout SignUpLayout;
    private ImageView LogoImageView;

    private ImageButton GoogleSignUpButton;
    private MaterialButton RegistratiButton;
    private TextView LoginTextView;

    private EditText UsernameEditText, EmailEditText, PasswordEditText, ConfermaPasswordEditText, ConfermaCodiceEditText;

    private ConstraintLayout TerminiCondizioniPopup;
    private MaterialTextView TerminiCondizioniTextView;
    private MaterialButton ConfermaTerminiButton, AnnullaTerminiButton;

    private ConstraintLayout ConfermaCodicePopup;
    private MaterialButton AnnullaCodiceButton, ConfermaCodiceButton;

    private Transition TerminiCondizioniTransition, ConfermaCodiceTransition;
    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setUI();
    }

    private void setUI(){
        SignUpLayout = findViewById(R.id.SignUpLayout);

        LogoImageView = findViewById(R.id.LogoImageView_signup);

        UsernameEditText = findViewById(R.id.UsernameEditText_signup);
        EmailEditText = findViewById(R.id.EmailEditText_signup);
        PasswordEditText = findViewById(R.id.PasswordEditText_signup);
        ConfermaPasswordEditText = findViewById(R.id.ConfermaPasswordEditText_signup);
        ConfermaCodiceEditText = findViewById(R.id.ConfermaCodiceEditText_popup_signup);

        GoogleSignUpButton = findViewById(R.id.GoogleSignUpButton);
        RegistratiButton = findViewById(R.id.ConfermaButton_signup);
        LoginTextView = findViewById(R.id.LoginTextView);

        TerminiCondizioniPopup = findViewById(R.id.TerminiCondizioniPopup_signup);
        TerminiCondizioniTextView = findViewById(R.id.TerminiCondizioniTextView_popup_signup);
        TerminiCondizioniTextView.setMovementMethod(LinkMovementMethod.getInstance());
        ConfermaTerminiButton = findViewById(R.id.ConfermaButton_popup_signup);
        AnnullaTerminiButton = findViewById(R.id.AnnullaButton_popup_signup);

        ConfermaCodicePopup = findViewById(R.id.ConfermaCodicePopup_signup);
        AnnullaCodiceButton = findViewById(R.id.AnnullaCodiceButton_popup_signup);
        ConfermaCodiceButton = findViewById(R.id.ConfermaCodiceButton_popup_signup);

        // Animazione per TerminiCondizioniPopup
        TerminiCondizioniTransition = new Visibility() {
            @Nullable
            @Override
            public Animator onAppear(ViewGroup sceneRoot, View view, TransitionValues startValues, TransitionValues endValues) {
                // Coordinate del punto centrale della view
                int cx = TerminiCondizioniPopup.getWidth() / 2;
                int cy = TerminiCondizioniPopup.getHeight() / 2;

                // Specifica il tipo di animazione
                return ViewAnimationUtils.createCircularReveal(TerminiCondizioniPopup, cx, cy, 0f, (float) Math.hypot(cx, cy));
            }

            @Nullable
            @Override
            public Animator onDisappear(ViewGroup sceneRoot, View view, TransitionValues startValues, TransitionValues endValues) {
                // Coordinate del punto centrale della view
                int cx = TerminiCondizioniPopup.getWidth() / 2;
                int cy = TerminiCondizioniPopup.getHeight() / 2;

                // Specifica il tipo di animazione
                return ViewAnimationUtils.createCircularReveal(TerminiCondizioniPopup, cx, cy, (float) Math.hypot(cx, cy), 0f);
            }
        }
                .setDuration(300)
                .addTarget(TerminiCondizioniPopup);

        // Animazione per ConfermaCodicePopup
        ConfermaCodiceTransition = new Slide(Gravity.BOTTOM)
                .setDuration(500)
                .addTarget(ConfermaCodicePopup);

        // Per il prof. Cutugno inseriamo un easter egg che mandi al sito web c:
        LogoImageView.setOnClickListener(view -> {
            String url = "https://natour2022.netlify.app/";
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        });

        GoogleSignUpButton.setOnClickListener(view -> {
            AmplifyRequest accessoConGoogle = new AmplifyRequest();
            accessoConGoogle.accessoConGoogle(SignUpActivity.this, new CallBackAmplify() {
                @Override
                public void onSuccess() {
                    handler = new Handler();
                    runnable = () -> {
                        Toasty.success(getApplicationContext(), "Benvenuto!", Toasty.LENGTH_SHORT, true).show();
                        Intent intent = new Intent(SignUpActivity.this, BottomNavigationViewActivity.class);
                        intent.putExtra("LoggedAs", "Google");
                        view.getContext().startActivity(intent);
                    };
                }

                @Override
                public void onFailure(Throwable throwable) {
                    Log.d(SignUpActivity_TAG, "onFailure: Impossibile completare la registrazione con Google: " + throwable.getLocalizedMessage());
                    Toasty.error(getApplicationContext(), "Attenzione! Impossibile completare la registrazione",
                            Toasty.LENGTH_SHORT, true).show();

                }
            });
        });

        RegistratiButton.setOnClickListener(view -> {
            username = UsernameEditText.getText().toString();
            password = PasswordEditText.getText().toString();
            confermaPassword = ConfermaPasswordEditText.getText().toString();
            email = EmailEditText.getText().toString();

            Log.d(SignUpActivity_TAG, username + " " + password + " " + confermaPassword + " " + email);
            // Controllo dei campi inseriti dall'utente
            if(controlloCampi(username, email, password, confermaPassword)){
                AmplifyRequest registrazione = new AmplifyRequest();
                registrazione.completaRegistrazione(username, password, email, new CallBackAmplify() {
                    @Override
                    public void onSuccess() {
                        avviaAnimazione(TerminiCondizioniPopup, TerminiCondizioniTransition, true);
                        attivaCampi(false);
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.d(SignUpActivity_TAG, "onFailure: Impossibile completare la registrazione: " + throwable.getLocalizedMessage());
                        Toasty.error(getApplicationContext(), "Attenzione! Impossibile completare la registrazione\nAssicurati di non essere già registrato in piattaforma",
                                Toasty.LENGTH_SHORT, true).show();
                    }
                });
            }
        });

        AnnullaTerminiButton.setOnClickListener(click -> {
            avviaAnimazione(TerminiCondizioniPopup, TerminiCondizioniTransition, false);
            attivaCampi(true);
        });

        ConfermaTerminiButton.setOnClickListener(click -> {
            TransitionManager.beginDelayedTransition(SignUpLayout, new TransitionSet()
                    .addTransition(TerminiCondizioniTransition)
                    .addTransition(ConfermaCodiceTransition)
            );
            TerminiCondizioniPopup.setVisibility(View.GONE);
            ConfermaCodicePopup.setVisibility(View.VISIBLE);
        });

        AnnullaCodiceButton.setOnClickListener(click -> {
            ConfermaCodiceEditText.setText("");
            avviaAnimazione(ConfermaCodicePopup, ConfermaCodiceTransition, false);
            attivaCampi(true);
        });

        ConfermaCodiceButton.setOnClickListener(click -> {
            AmplifyRequest confermaCodice = new AmplifyRequest();
            String codice = ConfermaCodiceEditText.getText().toString();

            if(codice.equals("")){
                Toasty.warning( getApplicationContext(), "Attenzione! I campi segnati\nda * sono obbligatori", Toasty.LENGTH_SHORT, true).show();
            } else {
                confermaCodice.invioCodiceDiConferma(username, codice, new CallBackAmplify() {
                    @Override
                    public void onSuccess() {
                        Log.d(SignUpActivity_TAG, "Registrazione completata con successo");
                        handler = new Handler();
                        runnable = () -> {
                            UsernameEditText.setText("");
                            EmailEditText.setText("");
                            PasswordEditText.setText("");
                            ConfermaPasswordEditText.setText("");
                            ConfermaCodiceEditText.setText("");

                            Toasty.success(getApplicationContext(), "Benvenuto!", Toasty.LENGTH_SHORT, true).show();

                            Intent intent = new Intent(SignUpActivity.this, BottomNavigationViewActivity.class);
                            intent.putExtra("LoggedAs", "NormalUser");

                            click.getContext().startActivity(intent);
                        };
                        handler.postDelayed(runnable, 3000);
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.d(SignUpActivity_TAG, "onFailure: Impossibile completare la registrazione: " + throwable.getLocalizedMessage());
                        Toasty.error(getApplicationContext(), "Attenzione! Impossibile completare la registrazione",
                                Toasty.LENGTH_SHORT, true).show();
                    }
                });

                avviaAnimazione(ConfermaCodicePopup, ConfermaCodiceTransition, false);
                attivaCampi(true);
            }
        });

        // Passaggio alla pagina di login nel caso l'utente sia già registrato
        LoginTextView.setOnClickListener(view ->
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class)));
    }

    // Controllo di validità dei campi
    private boolean controlloCampi(String username, String email, String password, String confermaPassword){
        if((username.isEmpty()) || (email.isEmpty()) || (password.isEmpty()) || (confermaPassword.isEmpty())){
            Toasty.warning(this,
                    "Attenzione! I campi segnati\n" +
                            "da * sono obbligatori",
                    Toasty.LENGTH_SHORT,
                    true).show();
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toasty.warning(this,
                    "Attenzione! L'indirizzo mail inserito non è valido",
                    Toasty.LENGTH_SHORT,
                    true).show();
            return false;
        }

        if (password.length() < 8) {
            Toasty.warning(this,
                    "Attenzione! La password deve contenere almeno 8 caratteri",
                    Toasty.LENGTH_SHORT,
                    true).show();
            return false;
        }

        if(!password.matches("(.*[0-9].*)") |
                !password.matches("(.*[A-Z].*)") |
                !password.matches("^(?=.*[_.()$&@]).*$")) {
            Toasty.warning(this,
                    "Attenzione! La password deve contenere almeno " +
                            "un numero, una lettera maiuscola ed un simbolo",
                    Toasty.LENGTH_SHORT,
                    true).show();
            return false;
        }

        if(!password.equals(confermaPassword)){
            Toasty.warning(this,
                    "Attenzione! Le password non corrispondono",
                    Toasty.LENGTH_SHORT,
                    true).show();
            return false;
        }

        return true;
    }

    // Gestione dell'animazione del popup per l'inserimento del codice di conferma
    private void attivaCampi(boolean value) {
        UsernameEditText.setEnabled(value);
        EmailEditText.setEnabled(value);
        PasswordEditText.setEnabled(value);
        ConfermaPasswordEditText.setEnabled(value);

        LogoImageView.setEnabled(value);
        GoogleSignUpButton.setEnabled(value);
        RegistratiButton.setEnabled(value);
        LoginTextView.setEnabled(value);
    }

    private void avviaAnimazione(ViewGroup popup, Transition transition, boolean visible) {
        TransitionManager.beginDelayedTransition((ViewGroup) popup.getParent(), transition);
        popup.setVisibility(visible ? View.VISIBLE : View.GONE);
    }
}