/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.admin.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.natour.admin.R;
import com.natour.admin.utils.persistence.LocalAdminDbManager;

public class SplashScreenActivity extends AppCompatActivity {
    private Handler handler;
    private Runnable runnable;

    // Db Manager
    private LocalAdminDbManager localAdminDbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        handler = new Handler();
        runnable = () -> {

            boolean status = adminStatus();
            Intent intent;
            if(status){
                // Nel caso in cui sia TRUE (il DB è vuoto) rimandiamo all'activity di login
                intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
            }else{
                // In caso contrario invece alla Home page
                intent = new Intent(SplashScreenActivity.this, BottomNavigationViewActivity.class);
            }
            startActivity(intent);
            finish();
        };
        handler.postDelayed(runnable, 1000);
    }

    private boolean adminStatus(){
        localAdminDbManager = new LocalAdminDbManager(getApplicationContext());
        localAdminDbManager.openR();
        boolean isEmpty = localAdminDbManager.isEmpty();
        localAdminDbManager.closeDB();
        Log.d("STATO", String.valueOf(isEmpty));
        return isEmpty;
    }
}