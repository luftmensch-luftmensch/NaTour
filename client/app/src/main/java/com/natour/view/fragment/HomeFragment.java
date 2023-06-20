/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.view.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.natour.R;

// Model
import com.natour.model.Itinerario;

// Presenter
import com.natour.presenter.amplify.AmplifyRequest;
import com.natour.presenter.callbackInterface.CallBackItinerario;
import com.natour.presenter.request.ItinerarioRequest;
import com.natour.utils.handler.ApiHandler;

import com.natour.presenter.adapter.ItinerarioRecyclerViewAdapter;
import com.natour.utils.handler.RecyclerItemClickListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.natour.utils.persistence.LocalUser;
import com.natour.utils.persistence.LocalUserDbManager;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private static final String HomeFragment_TAG = "HomeFragment";
    private Context context;

    private AppBarLayout TopAppBar;
    private MaterialToolbar TopToolBar;
    private BottomNavigationView MenuInferiore;

    private ApiHandler apiHandler;
    private RecyclerView itinerarioRecyclerView;
    private ItinerarioRecyclerViewAdapter itinerarioRecyclerViewAdapter;
    private MenuItem logoutButton;

    private LocalUserDbManager localUserDbManager;
    private LocalUser localUser;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUI(view);
        if (isAdded()) {
            retrievialData();
        }
    }

    private void setUI(View view) {
        TopAppBar = requireActivity().findViewById(R.id.AppBarLayout);
        TopToolBar = (MaterialToolbar) TopAppBar.getChildAt(0);

        TopToolBar.setTitle(R.string.HomeButton);
        TopToolBar.getMenu().clear();
        TopToolBar.setNavigationIcon(null);
        TopToolBar.inflateMenu(R.menu.homepage_topappbar_menu);
        logoutButton = TopToolBar.getMenu().findItem(R.id.LogoutButton);

        MenuInferiore = requireActivity().findViewById(R.id.MenuInferiore);
        MenuInferiore.getMenu().getItem(0).setChecked(true);

        requireActivity().findViewById(R.id.FloatingActionButton).setVisibility(View.GONE);
        requireActivity().findViewById(R.id.RicercaTextInput_appBar).setVisibility(View.GONE);
        requireActivity().findViewById(R.id.NuovoItinerarioPopup).setVisibility(View.GONE);
        requireActivity().findViewById(R.id.NuovoItinerarioPopupOutside).setVisibility(View.GONE);

        itinerarioRecyclerView = view.findViewById(R.id.HomeRecyclerView);

        // Gestione del signout dall'applicativo
        logoutButton.setOnMenuItemClickListener(click -> {
            logout();
            return false;
        });
    }

    private void retrievialData(){
        ItinerarioRequest request = new ItinerarioRequest();
        request.getItinerari(new CallBackItinerario() {
            @Override
            public void onSuccess(Itinerario itinerario) {
                // Nulla da fare qui
            }

            @Override
            public void onSuccessList(List<Itinerario> itinerari) {
                itinerarioRecyclerViewAdapter = new ItinerarioRecyclerViewAdapter(itinerari, getActivity());
                itinerarioRecyclerView.setAdapter(itinerarioRecyclerViewAdapter);
                getSelectedItem(itinerari);
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.d(HomeFragment_TAG, "onFailure: Impossibile completare la richiesta " + throwable.getLocalizedMessage());
                apiHandler = new ApiHandler(throwable, context);
            }

            @Override
            public void onSuccessResponse(Response<Void> response) {
                // Nulla da fare qui
            }
        });
    }

    private void getSelectedItem(List<Itinerario> itinerari){
        itinerarioRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(context, itinerarioRecyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Log.d(HomeFragment_TAG, String.valueOf(itinerari.get(position).getNomeItinerario()));
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
                .addToBackStack(HomeFragment_TAG)
                .commit();
    }

    /*
        In seguito alla richiesta di Sign out:
        1. Il db viene eliminato
        2. L'utente viene mandato alla schermata di registrazione
    */
    private void logout(){
        localUserDbManager = new LocalUserDbManager(context);
        localUserDbManager.openW();
        localUser = localUserDbManager.fetchDataUser();
        AmplifyRequest logoutRequest = new AmplifyRequest();

        if (localUser.getIsLoggedWithGoogle().equals("SI")){
            Log.d(HomeFragment_TAG, "Utente loggato con google");
            logoutRequest.signOut();
        } else {
            Log.d(HomeFragment_TAG, "Utente loggato con email");
            logoutRequest.signOutGlobale();
        }

        localUserDbManager.delete(1);
        localUserDbManager.closeDB();

        requireActivity().onBackPressed();
    }
}