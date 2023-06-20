package com.natour.view.fragment;

import android.animation.Animator;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;
import androidx.transition.TransitionValues;
import androidx.transition.Visibility;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;
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
import com.natour.utils.handler.ApiHandler;
import com.natour.utils.handler.RecyclerItemClickListener;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Response;

public class VisualizzaInfoProfiloFragment extends Fragment {

    private static final String VisualizzaInfoProfiloFragment_TAG = "VisualizzaInfoProfiloFragment";
    private Context context;

    private AppBarLayout TopAppBar;
    private MaterialToolbar TopToolBar;

    private CardView CardTitolo;
    private ImageView FotoProfiloImageView;
    private TextView UsernameTextView;

    private RecyclerView utenteRecyclerView;
    private ItinerarioRecyclerViewAdapter itinerarioStessoUtenteRecyclerViewAdapter;

    private CardView CardVisualizzaFotoProfilo;
    private ImageView FotoProfiloZoomataImageView;
    private ImageButton ModificaFotoProfiloButton;

    private Transition CardVisualizzaFotoTransition;

    // Request per i dati dell'utente
    private String username;
    private UtenteRequest requestUtente;

    // Request per gli itinerari dell'utente
    private ItinerarioRequest requestItinerario;

    // ApiHandler
    private ApiHandler apiHandler;

    public VisualizzaInfoProfiloFragment () {

    }

    public VisualizzaInfoProfiloFragment (String username) {
        this.username = username;
    }

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
        if (isAdded()) {
            retrievalDataProfilo();
            retrievalDataItinerari();
        }
    }

    private void setUI(View view) {
        TopAppBar = requireActivity().findViewById(R.id.AppBarLayout);
        TopToolBar = (MaterialToolbar) TopAppBar.getChildAt(0);

        TopToolBar.setTitle(R.string.ProfiloButton);
        TopToolBar.getMenu().clear();
        TopToolBar.setNavigationIcon(R.drawable.arrow_back_24dp);

        requireActivity().findViewById(R.id.FloatingActionButton).setVisibility(View.GONE);
        requireActivity().findViewById(R.id.RicercaTextInput_appBar).setVisibility(View.GONE);
        requireActivity().findViewById(R.id.NuovoItinerarioPopup).setVisibility(View.GONE);
        requireActivity().findViewById(R.id.NuovoItinerarioPopupOutside).setVisibility(View.GONE);

        CardTitolo = view.findViewById(R.id.TitoloLayout_profilo);
        UsernameTextView = CardTitolo.findViewById(R.id.TitoloTextView_card);
        FotoProfiloImageView = CardTitolo.findViewById(R.id.TitoloImageView_card);

        utenteRecyclerView = view.findViewById(R.id.ItinerariRecyclerView_profilo);

        CardVisualizzaFotoProfilo = view.findViewById(R.id.VisualizzaFotoProfiloCardView);
        FotoProfiloZoomataImageView = CardVisualizzaFotoProfilo.findViewById(R.id.FotoProfiloImageView_visualizzaCard);
        ModificaFotoProfiloButton = CardVisualizzaFotoProfilo.findViewById(R.id.ModificaFotoProfiloButton_visualizzaCard);

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

        TopToolBar.setNavigationOnClickListener(click -> requireActivity().getSupportFragmentManager()
                .popBackStackImmediate());

        // Zoom immagine di profilo
        FotoProfiloImageView.setOnClickListener(click -> {
            TransitionManager.beginDelayedTransition(
                    (ViewGroup) CardVisualizzaFotoProfilo.getParent(), CardVisualizzaFotoTransition);
            CardVisualizzaFotoProfilo.setVisibility(View.VISIBLE);
            ModificaFotoProfiloButton.setVisibility(View.GONE);
        });

        FotoProfiloZoomataImageView.setOnClickListener(click -> {
            TransitionManager.beginDelayedTransition(
                    (ViewGroup) CardVisualizzaFotoProfilo.getParent(), CardVisualizzaFotoTransition);
            CardVisualizzaFotoProfilo.setVisibility(View.GONE);
        });
    }

    private void retrievalDataProfilo(){
        requestUtente = new UtenteRequest();
        requestUtente.getSingoloUtente(username, new CallBackUtente() {
            @Override
            public void onSuccess(Utente utente) {
                Log.d(VisualizzaInfoProfiloFragment_TAG + "onSuccess", "Get dei dati dell'utente dal backend e settaggio dei campi");
                UsernameTextView.setText(utente.getUsername());
                retrievalFotoProfiloUtente(utente.getUrlFotoProfilo());
            }

            @Override
            public void onSuccessList(List<Utente> listaUtenti) {
                Log.d(VisualizzaInfoProfiloFragment_TAG + "onSuccessList", "Nulla da fare qui");
            }
            @Override
            public void onFailure(Throwable throwable) {
                Log.d(VisualizzaInfoProfiloFragment_TAG + "onFailure", "Get dei dati dell'utente dal backend fallito!" + throwable.getLocalizedMessage());
                apiHandler = new ApiHandler(throwable, getActivity());
            }
        });

    }

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

    private void retrievalDataItinerari(){
        requestItinerario = new ItinerarioRequest();
        requestItinerario.getItinerariByUtente(username, new CallBackItinerario() {
            @Override
            public void onSuccess(Itinerario itinerario) {
                // Nulla da fare qui
            }

            @Override
            public void onSuccessList(List<Itinerario> itinerari) {
                Log.d(VisualizzaInfoProfiloFragment_TAG + "onSuccessList", "Get lista di itinerari dell'utente. N° itinerari:" + itinerari.size());
                itinerarioStessoUtenteRecyclerViewAdapter = new ItinerarioRecyclerViewAdapter(itinerari, getActivity());
                utenteRecyclerView.setAdapter(itinerarioStessoUtenteRecyclerViewAdapter);
                getSelectedItem(itinerari);
            }
            @Override
            public void onFailure(Throwable throwable) {
                Log.d(VisualizzaInfoProfiloFragment_TAG, "onFailure: Get degli itinerari fallito " + throwable.getLocalizedMessage());
                apiHandler = new ApiHandler(throwable, getActivity());
            }

            @Override
            public void onSuccessResponse(Response<Void> response) {
                // Nulla da fare qui
            }
        });
    }

    private void getSelectedItem(List<Itinerario> itinerari){
        utenteRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(context, utenteRecyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Log.d(VisualizzaInfoProfiloFragment_TAG, String.valueOf(itinerari.get(position).getNomeItinerario()));
                        passaggioASingoloItinerario(itinerari.get(position).getNomeItinerario());
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        Toasty.info(context, "Per visualizzare il singolo itinerario clicca senza tenere premuto",
                                Toast.LENGTH_SHORT, true).show();
                    }
                })
        );
    }

    private void passaggioASingoloItinerario(String nomeItinerario){
        ItinerarioFragment itinerarioFragment = new ItinerarioFragment(nomeItinerario);
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.FragmentContainer, itinerarioFragment, "ItinerarioFragment")
                .addToBackStack(VisualizzaInfoProfiloFragment_TAG)
                .commit();
    }
}
