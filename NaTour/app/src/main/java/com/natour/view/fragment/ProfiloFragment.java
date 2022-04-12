/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.view.fragment;

import android.Manifest;
import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Slide;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;
import androidx.transition.TransitionSet;
import androidx.transition.TransitionValues;
import androidx.transition.Visibility;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.natour.R;
import com.natour.model.Itinerario;
import com.natour.model.Utente;
import com.natour.presenter.adapter.ItinerarioRecyclerViewAdapter;
import com.natour.presenter.amplify.AmplifyS3Request;
import com.natour.presenter.callbackInterface.CallBackGeneric;
import com.natour.presenter.callbackInterface.CallBackItinerario;
import com.natour.presenter.callbackInterface.CallBackUtente;
import com.natour.presenter.request.ItinerarioRequest;
import com.natour.presenter.request.UtenteRequest;
import com.natour.utils.constants.Constants;
import com.natour.utils.handler.ApiHandler;
import com.natour.utils.handler.RecyclerItemClickListener;
import com.natour.utils.persistence.LocalUser;
import com.natour.utils.persistence.LocalUserDbManager;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import es.dmoral.toasty.Toasty;
import io.ticofab.androidgpxparser.parser.GPXParser;
import io.ticofab.androidgpxparser.parser.domain.Gpx;
import io.ticofab.androidgpxparser.parser.domain.WayPoint;
import retrofit2.Response;

public class ProfiloFragment extends Fragment {

    private static final String ProfiloFragment_TAG = "ProfiloFragment";
    private Context context;

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

    private ConstraintLayout NuovoItinerarioPopupOutside;
    private FloatingActionButton NuovoItinerarioButton;

    private CardView NuovoItinerarioPopup;
    private ExtendedFloatingActionButton SelezionaPuntiSuMappaButton, ImportaFileGPXButton;

    private Transition NuovoItinerarioButtonTransition,
            NuovoItinerarioPopupTransition, CardVisualizzaFotoTransition;
    private TransitionSet transitionSet;

    private RecyclerView utenteRecyclerView;
    private ItinerarioRecyclerViewAdapter itinerarioStessoUtenteRecyclerViewAdapter;

    // Request per i dati dell'utente
    private UtenteRequest requestUtente;

    // Request per gli itinerari dell'utente
    private ItinerarioRequest requestItinerario;
	
	// ApiHandler
	private ApiHandler apiHandler;

    // Db Manager
    private LocalUserDbManager localUserDbManager;
    private LocalUser localUser;

    // Unchecked call to 'launch(I)' as a member of raw type 'androidx.activity.result.ActivityResultLauncher' (Di default era
    private ActivityResultLauncher<Intent> activityResultLauncherImmagineFotoProfilo;
    private ActivityResultLauncher<Intent> activityResultLauncherFileGPX;
    private ActivityResultLauncher<String> activityResultLauncherPermission;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profilo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
		setUI(view);
        localUser = getDatiutente();
        if (isAdded()) {
            retrievalDataProfilo();
            retrievalDataItinerari();
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
        MenuInferiore.getMenu().getItem(3).setChecked(true);

        CardTitolo = view.findViewById(R.id.TitoloLayout_profilo);
        UsernameTextView = CardTitolo.findViewById(R.id.TitoloTextView_card);
        FotoProfiloImageView = CardTitolo.findViewById(R.id.TitoloImageView_card);

        CardVisualizzaFotoProfilo = view.findViewById(R.id.VisualizzaFotoProfiloCardView);
        FotoProfiloZoomataImageView = CardVisualizzaFotoProfilo.findViewById(R.id.FotoProfiloImageView_visualizzaCard);
        ModificaFotoProfiloButton = CardVisualizzaFotoProfilo.findViewById(R.id.ModificaFotoProfiloButton_visualizzaCard);

        utenteRecyclerView = view.findViewById(R.id.ItinerariRecyclerView_profilo);

        SelezionaPuntiSuMappaButton = requireActivity().findViewById(R.id.SelezionaPuntiMappaButton_popup);
        ImportaFileGPXButton = requireActivity().findViewById(R.id.ImportaFileGPXButton_popup);

        NuovoItinerarioPopupOutside = requireActivity().findViewById(R.id.NuovoItinerarioPopupOutside);
        NuovoItinerarioPopupOutside.setVisibility(View.GONE);
        NuovoItinerarioPopup = requireActivity().findViewById(R.id.NuovoItinerarioPopup);
        NuovoItinerarioPopup.setVisibility(View.GONE);
        NuovoItinerarioButton = requireActivity().findViewById(R.id.FloatingActionButton);
        NuovoItinerarioButton.setVisibility(View.VISIBLE);
        NuovoItinerarioButton.setImageResource(R.drawable.add_36dp);
        DrawableCompat.setTint(
                DrawableCompat.wrap(NuovoItinerarioButton.getDrawable()),
                ContextCompat.getColor(context, R.color.tema_scuro_testi)
        );

        requireActivity().findViewById(R.id.RicercaTextInput_appBar).setVisibility(View.GONE);

        NuovoItinerarioButtonTransition = new Slide(Gravity.END)
                .setDuration(300)
                .addTarget(NuovoItinerarioButton);

        NuovoItinerarioPopupTransition = new Visibility() {
            @Nullable
            @Override
            public Animator onAppear(ViewGroup sceneRoot, View view, TransitionValues startValues, TransitionValues endValues) {
                // Coordinate del punto centrale della view
                int cx = NuovoItinerarioPopup.getWidth();
                int cy = NuovoItinerarioPopup.getHeight();

                // Specifica il tipo di animazione

                return ViewAnimationUtils
                        .createCircularReveal(NuovoItinerarioPopup, 0, 0, 0f, (float) Math.hypot(cx, cy));
            }

            @Nullable
            @Override
            public Animator onDisappear(ViewGroup sceneRoot, View view, TransitionValues startValues, TransitionValues endValues) {
                // Coordinate del punto centrale della view
                int cx = NuovoItinerarioPopup.getWidth();
                int cy = NuovoItinerarioPopup.getHeight();

                // Specifica il tipo di animazione
                return ViewAnimationUtils.createCircularReveal(NuovoItinerarioPopup, cx, cy, (float) Math.hypot(cx, cy), 0f);
            }
        }
                .setDuration(300)
                .addTarget(NuovoItinerarioPopup);

        transitionSet = new TransitionSet()
                .addTransition(NuovoItinerarioButtonTransition)
                .addTransition(NuovoItinerarioPopupTransition)
                .addListener(new Transition.TransitionListener() {
                    @Override
                    public void onTransitionStart(@NonNull Transition transition) {

                    }

                    @Override
                    public void onTransitionEnd(@NonNull Transition transition) {
                        NuovoItinerarioPopupOutside.setVisibility(NuovoItinerarioPopup.getVisibility());
                    }

                    @Override
                    public void onTransitionCancel(@NonNull Transition transition) {

                    }

                    @Override
                    public void onTransitionPause(@NonNull Transition transition) {

                    }

                    @Override
                    public void onTransitionResume(@NonNull Transition transition) {

                    }
                });

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

        NuovoItinerarioButton.setOnClickListener(click -> {
            NuovoItinerarioPopupOutside.setVisibility(View.VISIBLE);
            TransitionManager.beginDelayedTransition(BottomNavigationViewLayout, transitionSet);
            NuovoItinerarioButton.setVisibility(View.GONE);
            NuovoItinerarioPopup.setVisibility(View.VISIBLE);
        });

        NuovoItinerarioPopupOutside.setOnClickListener(click -> {
            TransitionManager.beginDelayedTransition(BottomNavigationViewLayout, transitionSet);
            NuovoItinerarioButton.setVisibility(View.VISIBLE);
            NuovoItinerarioPopup.setVisibility(View.GONE);
        });

        // Selezione punti sulla mappa interattiva
        SelezionaPuntiSuMappaButton.setOnClickListener(click -> checkPermessi());
        activityResultLauncherPermission = registerForActivityResult(new ActivityResultContracts.RequestPermission(), permesso ->{
            if(permesso){
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.FragmentContainer, new SelezionaPuntiMappaFragment(), "SelezionaPuntiMappaFragment")
                        .addToBackStack(ProfiloFragment_TAG)
                        .commit();
            }else{
                Toasty.error(context, "Per poter accedere a questa funzione, NaTour ha bisogno della tua posizione",
                        Toast.LENGTH_SHORT, true).show();
            }
        });


        // Selezione di un file GPX
        ImportaFileGPXButton.setOnClickListener(click -> {
            Intent selezionaFileGPX = new Intent();
            selezionaFileGPX.setType("application/gpx+xml");
            selezionaFileGPX.addCategory(Intent.CATEGORY_OPENABLE);
            selezionaFileGPX.putExtra(Intent.EXTRA_MIME_TYPES, "application/gpx+xml");
            selezionaFileGPX.setAction(Intent.ACTION_GET_CONTENT);
            activityResultLauncherFileGPX.launch(Intent.createChooser(selezionaFileGPX, "Seleziona file GPX"));
        });

        // Gestore della selezione del file per il caricamento di un nuovo itinerario
        activityResultLauncherFileGPX = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if((result.getResultCode() == Constants.RESULT_CODE_IMAGE_CHOSEN) && (result.getData() != null)){
                Intent data = result.getData();
                uploadGPXFile(data);
            }
        });

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

                // NB: nel caso in cui sulla macchina non sia configurato amplify l'app crasha
                //modificaImmagineDiProfilo(data.getData(), localUser.getUrlFotoProfilo());
            }
        });

    }

    private void checkPermessi(){
        if(ContextCompat.checkSelfPermission(context, "android.permission.ACCESS_FINE_LOCATION") == Constants.PERMISSION_DENIED){
            Log.d(ProfiloFragment_TAG, "Permesso " + "android.permission.ACCESS_FINE_LOCATION" + " negato");
            activityResultLauncherPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }else{
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.FragmentContainer, new SelezionaPuntiMappaFragment(), "SelezionaPuntiMappaFragment")
                    .addToBackStack(ProfiloFragment_TAG)
                    .commit();
        }
    }

    // Gestione dell'utente locale
    private LocalUser getDatiutente(){
        localUserDbManager = new LocalUserDbManager(context);
        localUserDbManager.openR();
        LocalUser localUser = localUserDbManager.fetchDataUser();
        localUserDbManager.closeDB();
        return localUser;
    }

    // Gestione dei dati dell'utente (Informazioni)
    private void retrievalDataProfilo(){
        requestUtente = new UtenteRequest();
        requestUtente.getSingoloUtente(localUser.getUsername(), new CallBackUtente() {
            @Override
            public void onSuccess(Utente utente) {
                Log.d(ProfiloFragment_TAG + "onSuccess", "Get dei dati dell'utente dal backend e settaggio dei campi");
                UsernameTextView.setText(utente.getUsername());
                retrievalFotoProfiloUtente(utente.getUrlFotoProfilo());
            }

            @Override
            public void onSuccessList(List<Utente> listaUtenti) {
                Log.d(ProfiloFragment_TAG + "onSuccessList", "Nulla da fare qui");
            }
            @Override
            public void onFailure(Throwable throwable) {
                Log.d(ProfiloFragment_TAG + "onFailure", "Get dei dati dell'utente dal backend fallito!" + throwable.getLocalizedMessage());
                apiHandler = new ApiHandler(throwable, getActivity());
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

    // Retrieval della foto dell'utente
    private void retrievalFotoProfiloUtente(String key){
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

    // Gestione delle informazioni relative agli itinerari appartenti all'utente
    private void retrievalDataItinerari(){
        requestItinerario = new ItinerarioRequest();
        requestItinerario.getItinerariByUtente(localUser.getUsername(), new CallBackItinerario() {
            @Override
            public void onSuccess(Itinerario itinerario) {
                // Nulla da fare qui
            }

            @Override
            public void onSuccessList(List<Itinerario> itinerari) {
                Log.d(ProfiloFragment_TAG + "onSuccessList", "Get lista di itinerari dell'utente. N° itinerari:" + itinerari.size());
                itinerarioStessoUtenteRecyclerViewAdapter = new ItinerarioRecyclerViewAdapter(itinerari, getActivity());
                utenteRecyclerView.setAdapter(itinerarioStessoUtenteRecyclerViewAdapter);
                getSelectedItem(itinerari);
            }
            @Override
            public void onFailure(Throwable throwable) {
                Log.d(ProfiloFragment_TAG, "onFailure: Get degli itinerari fallito " + throwable.getLocalizedMessage());
                apiHandler = new ApiHandler(throwable, getActivity());
            }

            @Override
            public void onSuccessResponse(Response<Void> response) {
                // Nulla da fare qui
            }
        });
    }

    // Metodo per ottenere l'id dell'itinerario selezionato ddall'utente per la visualizzazione del singolo itinerario
    private void getSelectedItem(List<Itinerario> itinerari){
        utenteRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(context, utenteRecyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Log.d(ProfiloFragment_TAG, String.valueOf(itinerari.get(position).getNomeItinerario()));
                        passaggioASingoloItinerario(itinerari.get(position).getNomeItinerario());
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        Toasty.info(context, "Per visualizzare il singolo itinerario clicca senza tenere premuto",
                                Toast.LENGTH_SHORT, true).show();
                    }
                })
        );
    }

    private void passaggioASingoloItinerario(String nomeItinerario) {
        ItinerarioFragment itinerarioFragment = new ItinerarioFragment(nomeItinerario);
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.FragmentContainer, itinerarioFragment, "ItinerarioFragment")
                .addToBackStack(ProfiloFragment_TAG)
                .commit();
    }

    // Gestione dell'upload del file GPX
    private void uploadGPXFile(Intent data) {
        GPXParser parser = new GPXParser();
        try{
            Gpx file;
            InputStream inputStream =  requireActivity().getContentResolver().openInputStream(data.getData());
            file = parser.parse(inputStream);

            int hours = file.getMetadata().getTime().getHourOfDay();

            Log.d(ProfiloFragment_TAG, "Tempo totale " + hours);
            List<WayPoint> wayPoints = file.getWayPoints();
            /*
                Analogamente a come facciamo nella gestione manuale della creazione del percorso
                prendiamo dal file GPX il primo e l'ultimo waypoint (da cui estrapoliamo i dati della longitudine e latitudine)
                e li passiamo al nuovo itinerario fragment
            */

            Double inizioLong = wayPoints.get(0).getLongitude();
            Double inizioLat = wayPoints.get(0).getLatitude();
            Double fineLong = wayPoints.get(wayPoints.size() - 1).getLongitude();
            Double fineLat = wayPoints.get(wayPoints.size() - 1).getLatitude();

            passaggioANuovoItinerarioFragment(inizioLong, inizioLat, fineLong, fineLat);

        } catch (XmlPullParserException | IOException e) {
            Toasty.error(context, "Impossibile aprire il file selezionato\nControlla che il file sia di tipo GPX",
                    Toast.LENGTH_LONG, true).show();
            Log.d(ProfiloFragment_TAG, e.getLocalizedMessage());
        }
    }

    private void passaggioANuovoItinerarioFragment(Double inizioLong, Double inizioLat, Double fineLong, Double fineLat){
        NuovoItinerarioFragment nuovoItinerarioFragment= new NuovoItinerarioFragment(inizioLong, inizioLat, fineLong, fineLat);
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.FragmentContainer, nuovoItinerarioFragment)
                .commit();
    }
}