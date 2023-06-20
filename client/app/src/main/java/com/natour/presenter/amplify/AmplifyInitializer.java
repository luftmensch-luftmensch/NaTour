/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.presenter.amplify;

import android.app.Application;
import android.util.Log;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.rx.RxAmplify;
import com.amplifyframework.storage.s3.AWSS3StoragePlugin;

public class AmplifyInitializer extends Application{
    public void onCreate() {
        super.onCreate();
        try {
            // Cognito pool
            RxAmplify.addPlugin(new AWSCognitoAuthPlugin());

            // Bucket S3
            RxAmplify.addPlugin(new AWSS3StoragePlugin());

            RxAmplify.configure(getApplicationContext());
            Log.i("MyAmplifyApp", "Amplify inizializzato correttamente");
        }catch (AmplifyException error) {
            Log.e("MyAmplifyApp", "Impossibile inizializzare Amplify", error);
        }
    }

}
