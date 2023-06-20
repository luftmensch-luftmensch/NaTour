/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.view.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textview.MaterialTextView;
import com.natour.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputLayout;
import com.natour.model.Itinerario;
import com.natour.presenter.adapter.ItinerarioRecyclerViewAdapter;
import com.natour.presenter.callbackInterface.CallBackItinerario;
import com.natour.presenter.request.ItinerarioRequest;
import com.natour.utils.handler.ApiHandler;
import com.natour.utils.handler.RecyclerItemClickListener;

import org.osmdroid.views.MapView;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Response;

public class ScopriFragment extends Fragment {

    private static final String ScopriFragment_TAG = "ScopriFragment";
    private Context context;

    private AppBarLayout TopAppBar;
    private MaterialToolbar TopToolBar;
    private BottomNavigationView MenuInferiore;

    private TextInputLayout RicercaTextInput;
    private EditText RicercaEditText;

    private MenuItem RicercaButton, FiltriButton;
    private MaterialTextView IniziaRicercaTextView;

    //private MaterialTextView ScopriListaButton, ScopriMappaButton;

    private ApiHandler apiHandler;
    private RecyclerView ScopriListaRecyclerView;
    //private RecyclerView ScopriMappaRecyclerView;
    private ItinerarioRecyclerViewAdapter itinerarioRecyclerViewAdapter;
    //private MapView MappaRicercaItinerari;

    private List<Itinerario> listaItinerari;

    public ScopriFragment() {}

    public ScopriFragment(List<Itinerario> listaItinerari) {
        this.listaItinerari = listaItinerari;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_scopri, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUI(view);
    }

    private void setUI(View view) {
        TopAppBar = requireActivity().findViewById(R.id.AppBarLayout);
        TopToolBar = (MaterialToolbar) TopAppBar.getChildAt(0);
        MenuInferiore = requireActivity().findViewById(R.id.MenuInferiore);

        requireActivity().findViewById(R.id.FloatingActionButton).setVisibility(View.GONE);
        requireActivity().findViewById(R.id.NuovoItinerarioPopup).setVisibility(View.GONE);
        requireActivity().findViewById(R.id.NuovoItinerarioPopupOutside).setVisibility(View.GONE);

        RicercaTextInput = requireActivity().findViewById(R.id.RicercaTextInput_appBar);
        RicercaEditText = requireActivity().findViewById(R.id.RicercaEditText_appBar);

        IniziaRicercaTextView = view.findViewById(R.id.IniziaRicercaTextView_scopri);

        //ScopriListaButton = view.findViewById(R.id.ScopriListaButton);
        //ScopriMappaButton = view.findViewById(R.id.ScopriMappaButton);

        //cambiaColori(ScopriListaButton, ScopriMappaButton);

        ScopriListaRecyclerView = view.findViewById(R.id.ScopriRecyclerView_lista);
        //ScopriMappaRecyclerView = view.findViewById(R.id.ScopriRecyclerView_mappa);
        //MappaRicercaItinerari = view.findViewById(R.id.MappaRicercaItinerari);

        if (listaItinerari != null) {
            // Il messaggio di istruzioni compare solo all'inizio o se non ci sono risultati
            IniziaRicercaTextView.setVisibility((listaItinerari.size() == 0) ? View.VISIBLE : View.GONE);

            itinerarioRecyclerViewAdapter = new ItinerarioRecyclerViewAdapter(listaItinerari, context);
            if (ScopriListaRecyclerView.getVisibility() == View.VISIBLE) {
                ScopriListaRecyclerView.setAdapter(itinerarioRecyclerViewAdapter);
            } /*else {
                ScopriMappaRecyclerView.setAdapter(itinerarioRecyclerViewAdapter);
            }*/
            getSelectedItem(listaItinerari);
        } else {
            IniziaRicercaTextView.setVisibility(View.VISIBLE);
        }

        /*ScopriListaButton.setOnClickListener(click -> {
            if (ScopriListaRecyclerView.getVisibility() != View.VISIBLE) {
                cambiaColori(ScopriListaButton, ScopriMappaButton);
                ScopriListaRecyclerView.setVisibility(View.VISIBLE);
                ScopriMappaRecyclerView.setVisibility(View.GONE);
                MappaRicercaItinerari.setVisibility(View.GONE);
                ScopriListaRecyclerView.setAdapter(ScopriMappaRecyclerView.getAdapter());
            }
        });*/

        /*ScopriMappaButton.setOnClickListener(click -> {
            if (ScopriMappaRecyclerView.getVisibility() != View.VISIBLE) {
                cambiaColori(ScopriMappaButton, ScopriListaButton);
                ScopriListaRecyclerView.setVisibility(View.GONE);
                ScopriMappaRecyclerView.setVisibility(View.VISIBLE);
                MappaRicercaItinerari.setVisibility(View.VISIBLE);
                ScopriMappaRecyclerView.setAdapter(ScopriListaRecyclerView.getAdapter());
            }
        });*/

        setMenu();
    }

    private void setMenu() {
        TopToolBar.setTitle(R.string.ScopriButton);
        TopToolBar.getMenu().clear();
        TopToolBar.setNavigationIcon(null);
        TopToolBar.inflateMenu(R.menu.scopri_topappbar_menu);

        RicercaButton = TopToolBar.getMenu().findItem(R.id.RicercaButton);
        FiltriButton = TopToolBar.getMenu().findItem(R.id.FiltriButton);

        RicercaTextInput.setVisibility(View.GONE);

        RicercaButton.setOnMenuItemClickListener(click -> {
            iniziaRicerca();
            return false;
        });

        FiltriButton.setOnMenuItemClickListener(click -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.FragmentContainer, new FiltriFragment(), "FiltriFragment")
                    .addToBackStack(ScopriFragment_TAG)
                    .commit();
            return false;
        });
    }

    // Opzioni per la ricerca
    private void iniziaRicerca() {
        IniziaRicercaTextView.setVisibility(View.GONE);
        RicercaTextInput.setVisibility(View.VISIBLE);
        TopToolBar.setTitle(R.string.RicercaEditTextHint);

        TopToolBar.getMenu().clear();
        TopToolBar.inflateMenu(R.menu.check_topappbar_menu);
        TopToolBar.setNavigationIcon(R.drawable.arrow_back_24dp);

        MenuItem ConfermaButton = TopToolBar.getMenu().getItem(0);

        ConfermaButton.setOnMenuItemClickListener(click -> {
            retrievalItinerari();
            RicercaEditText.setText("");
            RicercaTextInput.setVisibility(View.GONE);
            setMenu();
            return false;
        });

        TopToolBar.setNavigationOnClickListener(click -> {
            RicercaEditText.setText("");
            RicercaTextInput.setVisibility(View.GONE);
            IniziaRicercaTextView.setVisibility(View.VISIBLE);
            setMenu();
        });
    }

    /*private void cambiaColori(MaterialTextView verde, MaterialTextView grigio) {
        DrawableCompat.setTint(
                DrawableCompat.wrap(verde.getBackground()),
                ContextCompat.getColor(context, R.color.verde_scuro)
        );
        verde.setTextColor(getResources().getColor(R.color.tema_scuro_testi, null));

        DrawableCompat.setTint(
                DrawableCompat.wrap(grigio.getBackground()),
                ContextCompat.getColor(context, R.color.tema_scuro_grigio_chiaro)
        );
        grigio.setTextColor(getResources().getColor(R.color.verde_scuro, null));
    }*/

    private void retrievalItinerari() {
        IniziaRicercaTextView.setVisibility(View.VISIBLE);
        ItinerarioRequest itinerarioRequest = new ItinerarioRequest();
        String ricerca = RicercaEditText.getText().toString();

        if (!ricerca.isEmpty()) {
            itinerarioRequest.getItinerariByNomeItinerario(ricerca, new CallBackItinerario() {
                @Override
                public void onSuccess(Itinerario itinerario) {
                    // nulla da fare qui
                }

                @Override
                public void onSuccessList(List<Itinerario> itinerari) {
                    // Il messaggio di istruzioni compare solo all'inizio o se non ci sono risultati
                    IniziaRicercaTextView.setVisibility((itinerari.size() == 0) ? View.VISIBLE : View.GONE);

                    itinerarioRecyclerViewAdapter = new ItinerarioRecyclerViewAdapter(itinerari, requireActivity());
                    if (ScopriListaRecyclerView.getVisibility() == View.VISIBLE) {
                        ScopriListaRecyclerView.setAdapter(itinerarioRecyclerViewAdapter);
                    } /*else {
                        ScopriMappaRecyclerView.setAdapter(itinerarioRecyclerViewAdapter);
                    }*/
                    getSelectedItem(itinerari);
                }

                @Override
                public void onFailure(Throwable throwable) {
                    apiHandler = new ApiHandler(throwable, context);
                }

                @Override
                public void onSuccessResponse(Response<Void> response) {
                    // nulla da fare qui
                }
            });
        }
    }

    // Metodo per ottenere le informazioni di un singolo itinerario a partire dalla lista che l'utente visualizza
    private void getSelectedItem(List<Itinerario> itinerari){
        ScopriListaRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(context, ScopriListaRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Log.d(ScopriFragment_TAG, String.valueOf(itinerari.get(position).getNomeItinerario()));
                        passaggioASingoloItinerario(itinerari.get(position).getNomeItinerario());
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        Toasty.info(context, "Per visualizzare il singolo itinerario clicca senza tenere premuto",
                                Toast.LENGTH_SHORT, true).show();
                    }
                })
        );

        /*ScopriMappaRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(context, ScopriMappaRecyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Log.d(ScopriFragment_TAG, String.valueOf(itinerari.get(position).getNomeItinerario()));
                        passaggioASingoloItinerario(itinerari.get(position).getNomeItinerario());
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        Toasty.info(context, "Per visualizzare il singolo itinerario clicca senza tenere premuto",
                                Toast.LENGTH_SHORT, true).show();
                    }
                })
        );*/
    }

    // Metodo per il passaggio alle info di un singolo itinerario
    private void passaggioASingoloItinerario(String nomeItinerario){
        ItinerarioFragment itinerarioFragment = new ItinerarioFragment(nomeItinerario);
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.FragmentContainer, itinerarioFragment);
        fragmentTransaction.commit();
    }
}