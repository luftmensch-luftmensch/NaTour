/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import com.natour.R;

// Fragment
import com.natour.presenter.amplify.AmplifyRequest;
import com.natour.utils.persistence.LocalUserDbManager;
import com.natour.view.fragment.ChatRoomFragment;
import com.natour.view.fragment.HomeFragment;
import com.natour.view.fragment.ProfiloFragment;
import com.natour.view.fragment.ScopriFragment;

// Material
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavigationViewActivity extends AppCompatActivity {
    private static final String BottomNavigationViewActivity_TAG = "BottomNavigationView";

    private BottomNavigationView MenuInferiore;
    private MenuItem HomeButton, ScopriButton, MessaggiButton, ProfiloButton;

    private LocalUserDbManager localUserDbManager;

    private String informazioneSulTipoDiUtente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation_view);

        Intent intent = getIntent();

        /*
            Nel caso in cui l'utente sia già loggato non abbiamo un extra `LoggedAs`
            Per evitare quindi che l'app crashi prima di settare il valore della stringa controllo che esista l'extra
        */
        if(intent.hasExtra("LoggedAs")){
            informazioneSulTipoDiUtente = intent.getStringExtra("LoggedAs");

            // In base al tipo di login (con google o tramite mail) chiamo 2 diversi metodi che gestiscano l'inserimento dell'utente locale
            if(informazioneSulTipoDiUtente.equals("NormalUser")){
                setLocalUser();
            }else {
                setLocalUSerSignedWithGoogle();
            }
        }
        Log.d(BottomNavigationViewActivity_TAG, "Tipo di utente " + informazioneSulTipoDiUtente);

        MenuInferiore = findViewById(R.id.MenuInferiore);
        HomeButton = MenuInferiore.getMenu().findItem(R.id.HomeButton);
        ScopriButton = MenuInferiore.getMenu().findItem(R.id.ScopriButton);
        MessaggiButton = MenuInferiore.getMenu().findItem(R.id.MessaggiButton);
        ProfiloButton = MenuInferiore.getMenu().findItem(R.id.ProfiloButton);

        HomeButton.setOnMenuItemClickListener(menuItem -> {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.FragmentContainer, new HomeFragment())
                    .commit();
            return false;
        });

        ScopriButton.setOnMenuItemClickListener(menuItem -> {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.FragmentContainer, new ScopriFragment())
                    .commit();
            return false;
        });

        MessaggiButton.setOnMenuItemClickListener(menuItem -> {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.FragmentContainer, new ChatRoomFragment())
                    .commit();
            return false;
        });

        ProfiloButton.setOnMenuItemClickListener(menuItem -> {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.FragmentContainer, new ProfiloFragment())
                    .commit();
            return false;
        });
    }

    private void setLocalUser(){
        localUserDbManager = new LocalUserDbManager(this.getApplicationContext());
        localUserDbManager.openW();

        boolean isEmpty = localUserDbManager.isEmpty();
        if (isEmpty) {
            Log.d(BottomNavigationViewActivity_TAG, "Il db locale non ha dati. Eseguo inserimento");
            AmplifyRequest request = new AmplifyRequest();
            request.insertLocalUser(localUserDbManager);
        } else {
            Log.d(BottomNavigationViewActivity_TAG, "Il db locale presenta già un utente");
        }
    }

    private void setLocalUSerSignedWithGoogle(){
        localUserDbManager = new LocalUserDbManager(this.getApplicationContext());
        localUserDbManager.openW();
        boolean isEmpty = localUserDbManager.isEmpty();
        if (isEmpty) {
            Log.d(BottomNavigationViewActivity_TAG, "Il db locale non ha dati. Eseguo inserimento");
            AmplifyRequest request = new AmplifyRequest();
            request.insertLocalUserSignedWithGoogle(localUserDbManager);
        } else {
            Log.d(BottomNavigationViewActivity_TAG, "Il db locale presenta già un utente");
        }
    }
}