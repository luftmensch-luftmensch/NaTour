/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.admin.view.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.natour.admin.R;
import com.natour.admin.presenter.amplify.AmplifyRequest;
import com.natour.admin.utils.persistence.LocalAdminDbManager;
import com.natour.admin.view.fragment.ProfiloFragment;
import com.natour.admin.view.fragment.SegnalazioniFragment;
import com.natour.admin.view.fragment.StatisticheFragment;

public class BottomNavigationViewActivity extends AppCompatActivity {
    private static final String BottomNavigationViewActivity_TAG = "BottomNavigationViewActivity";

    private BottomNavigationView MenuInferiore;
    private MenuItem ProfiloButton, StatisticheButton, SegnalazioniButton;

    private LocalAdminDbManager localAdminDbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation_view);
        setLocalAdmin();

        MenuInferiore = findViewById(R.id.MenuInferiore);
        ProfiloButton = MenuInferiore.getMenu().findItem(R.id.ProfiloButton);
        StatisticheButton = MenuInferiore.getMenu().findItem(R.id.StatisticheButton);
        SegnalazioniButton = MenuInferiore.getMenu().findItem(R.id.SegnalazioniButton);

        ProfiloButton.setOnMenuItemClickListener(menuItem -> {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.FragmentContainer, new ProfiloFragment())
                    .commit();
            return false;
        });

        StatisticheButton.setOnMenuItemClickListener(menuItem -> {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.FragmentContainer, new StatisticheFragment())
                    .commit();
            return false;
        });

        SegnalazioniButton.setOnMenuItemClickListener(menuItem -> {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.FragmentContainer, new SegnalazioniFragment())
                    .commit();
            return false;
        });
    }

    private void setLocalAdmin(){
        localAdminDbManager = new LocalAdminDbManager(this.getApplicationContext());
        localAdminDbManager.openW();

        boolean isEmpty = localAdminDbManager.isEmpty();
        if(isEmpty){
            Log.d(BottomNavigationViewActivity_TAG, "Il db locale non ha dati. Eseguo inserimento");
            AmplifyRequest request = new AmplifyRequest();
            request.insertAdminUser(localAdminDbManager);
        }else{
            Log.d(BottomNavigationViewActivity_TAG, "Il db locale presenta già un utente");
        }
    }
}
