/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.admin.view.fragment;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;
import androidx.transition.TransitionValues;
import androidx.transition.Visibility;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.natour.admin.R;
import com.natour.admin.model.Admin;
import com.natour.admin.presenter.amplify.AmplifyS3Request;
import com.natour.admin.presenter.callbackInterface.CallBackAdmin;
import com.natour.admin.presenter.callbackInterface.CallBackGeneric;
import com.natour.admin.presenter.request.AdminRequest;
import com.natour.admin.utils.constants.Constants;
import com.natour.admin.utils.persistence.LocalAdmin;
import com.natour.admin.utils.persistence.LocalAdminDbManager;

public class ProfiloFragment extends Fragment {

    private static final String ProfiloFragment_TAG = "ProfiloFragment";
    Context context;

    // Campi dell'interfaccia grafica
    private ConstraintLayout BottomNavigationViewLayout;

    private AppBarLayout TopAppBar;
    private MaterialToolbar TopToolBar;
    private BottomNavigationView MenuInferiore;

    private CardView CardTitolo;
    private ImageView FotoProfiloImageView;
    private TextView UsernameTextView;

    private CardView CardVisualizzaFotoProfilo;
    private ImageView FotoProfiloZoomataImageView;
    private ImageButton ModificaFotoProfiloButton;

    private Transition CardVisualizzaFotoTransition;

    // Request per i dati dell'admin
    private AdminRequest adminRequest;

    // Db Manager
    private LocalAdminDbManager localAdminDbManager;
    private LocalAdmin localAdmin;

    private ActivityResultLauncher<Intent> activityResultLauncherImmagineFotoProfilo;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profilo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUI(view);
        localAdmin = getDatiAdmin();
        if (isAdded()) {
            retrievialDataAdmin();
        }
    }

    private void setUI(View view) {
        BottomNavigationViewLayout = requireActivity().findViewById(R.id.BottomNavigationViewActivity);

        TopAppBar = requireActivity().findViewById(R.id.AppBarLayout);
        TopToolBar = (MaterialToolbar) TopAppBar.getChildAt(0);

        TopToolBar.setTitle(R.string.ProfiloButton);
        TopToolBar.getMenu().clear();
        TopToolBar.setNavigationIcon(null);

        MenuInferiore = requireActivity().findViewById(R.id.MenuInferiore);
        MenuInferiore.getMenu().findItem(R.id.ProfiloButton).setChecked(true);

        CardTitolo = view.findViewById(R.id.TitoloLayout_profilo);
        UsernameTextView = CardTitolo.findViewById(R.id.TitoloTextView_card);
        FotoProfiloImageView = CardTitolo.findViewById(R.id.TitoloImageView_card);

        CardVisualizzaFotoProfilo = view.findViewById(R.id.VisualizzaFotoProfiloCardView);
        FotoProfiloZoomataImageView = CardVisualizzaFotoProfilo.findViewById(R.id.FotoProfiloImageView_visualizzaCard);
        ModificaFotoProfiloButton = CardVisualizzaFotoProfilo.findViewById(R.id.ModificaFotoProfiloButton_visualizzaCard);

        requireActivity().findViewById(R.id.RicercaTextInput_appBar).setVisibility(View.GONE);

        CardVisualizzaFotoTransition = new Visibility() {
            @Nullable
            @Override
            public Animator onAppear(ViewGroup sceneRoot, View view, TransitionValues startValues, TransitionValues endValues) {
                // Coordinate del punto centrale della view
                int cx = CardVisualizzaFotoProfilo.getWidth() / 2;
                int cy = CardVisualizzaFotoProfilo.getHeight() / 2;

                // Specifica il tipo di animazione
                return ViewAnimationUtils.createCircularReveal(CardVisualizzaFotoProfilo, cx, cy, 0f, (float) Math.hypot(cx, cy));
            }

            @Nullable
            @Override
            public Animator onDisappear(ViewGroup sceneRoot, View view, TransitionValues startValues, TransitionValues endValues) {
                // Coordinate del punto centrale della view
                int cx = CardVisualizzaFotoProfilo.getWidth() / 2;
                int cy = CardVisualizzaFotoProfilo.getHeight() / 2;

                // Specifica il tipo di animazione
                return ViewAnimationUtils.createCircularReveal(CardVisualizzaFotoProfilo, cx, cy, (float) Math.hypot(cx, cy), 0f);
            }
        }
                .setDuration(300)
                .addTarget(CardVisualizzaFotoProfilo);

        // Selezione / Modifica immagine di profilo
        FotoProfiloImageView.setOnClickListener(click -> {
            TransitionManager.beginDelayedTransition(
                    (ViewGroup) CardVisualizzaFotoProfilo.getParent(), CardVisualizzaFotoTransition);
            CardVisualizzaFotoProfilo.setVisibility(View.VISIBLE);
            ModificaFotoProfiloButton.setVisibility(View.VISIBLE);
        });

        FotoProfiloZoomataImageView.setOnClickListener(click -> {
            TransitionManager.beginDelayedTransition(
                    (ViewGroup) CardVisualizzaFotoProfilo.getParent(), CardVisualizzaFotoTransition);
            CardVisualizzaFotoProfilo.setVisibility(View.GONE);
            ModificaFotoProfiloButton.setVisibility(View.GONE);
        });

        ModificaFotoProfiloButton.setOnClickListener(click -> {
            // Soluzione al problema `Calling setType after setting URI in Intent constructor will clear the data`
            Intent selezionaFotoDaGalleria = new Intent();
            selezionaFotoDaGalleria.setType("image/*");
            selezionaFotoDaGalleria.setAction(Intent.ACTION_GET_CONTENT);
            activityResultLauncherImmagineFotoProfilo.launch(Intent.createChooser(selezionaFotoDaGalleria, "Seleziona immagini"));
        });

        // Gestore della selezione della foto per la modifica della foto profilo
        activityResultLauncherImmagineFotoProfilo = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Constants.RESULT_CODE_IMAGE_CHOSEN && result.getData() != null){
                // Castiamo l'activity result a un intent in modo da ottere l'uri
                Intent data = result.getData();
                Log.d(ProfiloFragment_TAG, data.getData().toString());

                //NB: nel caso in cui sulla macchina non sia configurato amplify l'app crasha
                //modificaImmagineDiProfilo(data.getData(), localAdmin.getUrlFotoProfilo());
            }
        });

    }

    // Gestione dei dati dell'admin (Informazioni)
    private void retrievialDataAdmin(){
        adminRequest = new AdminRequest();
        adminRequest.getSingoloAdmin(localAdmin.getUsername(), new CallBackAdmin() {
            @Override
            public void onSuccess(Admin admin) {
                Log.d(ProfiloFragment_TAG + "onSuccess", "Get dei dati dell'admin dal backend e settaggio dei campi");
                UsernameTextView.setText(admin.getUsername());
                Log.d(ProfiloFragment_TAG, admin.getUsername());
                // TODO: riattivare alla consegna retrievialFotoProfiloAdmin(admin.getUrlFotoProfilo());
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.d(ProfiloFragment_TAG + "onFailure", "Get dei dati dell'admin dal backend fallito!" + throwable.getLocalizedMessage());
            }
        });
    }

    // Retrieval della foto dell'admin
    private void retrievialFotoProfiloAdmin(String key){
        AmplifyS3Request amplifyS3Request = new AmplifyS3Request();
        amplifyS3Request.getUrlImmagine(key, new CallBackGeneric() {
            @Override
            public void onSuccess(String urlFotoProfilo) {
                Glide.with(context).load(urlFotoProfilo).circleCrop().into(FotoProfiloImageView);
                Glide.with(context).load(urlFotoProfilo).fitCenter().into(FotoProfiloZoomataImageView);
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    // Gestione dell'immagine di profilo
    private void modificaImmagineDiProfilo(Uri uri, String key){
        AmplifyS3Request amplifyS3Request = new AmplifyS3Request();
        amplifyS3Request.uploadImage(uri, key, context, new CallBackGeneric() {
            @Override
            public void onSuccess(String s) {
                Log.d(ProfiloFragment_TAG, "onSuccess: Modifica Immagine di profilo" + s);
            }
            @Override
            public void onFailure(Throwable throwable) {
                Log.d(ProfiloFragment_TAG, "onFailure: Modifica Immagine di profilo fallita: " + throwable.getLocalizedMessage());
            }
        });
    }

    private LocalAdmin getDatiAdmin() {
        localAdminDbManager = new LocalAdminDbManager(context);
        localAdminDbManager.openR();
        LocalAdmin localAdmin = localAdminDbManager.fetchDataUser();
        localAdminDbManager.closeDB();
        return localAdmin;
    }
}
