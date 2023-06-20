package com.natour;

import android.Manifest;
import android.content.Context;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.natour.utils.constants.Constants;
import com.natour.utils.persistence.LocalUser;
import com.natour.utils.persistence.LocalUserDbManager;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class AndroidTesting {
    LocalUser localUser;
    LocalUserDbManager dbManager;

    @Test
    public void useAppContext() {
        // [1/3]
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.natour", appContext.getPackageName());
    }

    /*
        Testing dell'utilizzo del database locale.

    Testiamo il metodo eseguendo i seguenti passaggi:
        1. Inseriamo quindi un utente;
        2. Controlliamo se il il database risulti vuoto (Ci aspettiamo ovviamente che la `response` sia falsa)

    */

    public boolean inserimentoEControllo(int id, String username, String urlFotoProfilo, String isLoggedWithGoogle){
        dbManager = new LocalUserDbManager(InstrumentationRegistry.getInstrumentation().getTargetContext());
        dbManager.openW();

        // Per avere un feedback immediato durante il testing abbiamo deciso di fare uso di Log (che vengono mostrati durante l'esecuzione del testing)
        Log.d("Android Testing", "Inizio inserimento dati");
        dbManager.inserimentoDatiUtente(id, username, urlFotoProfilo, isLoggedWithGoogle);
        boolean response = dbManager.isEmpty();
        Log.d("Android Testing", "Stato del db in seguito all'inserimento: " + response);
        dbManager.closeDB();
        return response;
    }


    // Testiamo quindi il metodo precedentemente definito
    @Test
    public void testDatabaseManager(){
        inserimentoEControllo(1, "AndroidTesting", "AndroidTesting", "No");
    }
}