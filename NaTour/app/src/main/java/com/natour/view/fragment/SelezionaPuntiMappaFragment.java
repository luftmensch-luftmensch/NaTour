/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.view.fragment;

import org.osmdroid.config.Configuration;

import android.Manifest;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.natour.BuildConfig;
import com.natour.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.natour.utils.constants.Constants;

import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class SelezionaPuntiMappaFragment extends Fragment {
    private Context context;

    private AppBarLayout TopAppBar;
    private MaterialToolbar TopToolBar;
    private MenuItem eliminaSelezioneMenuItem;

    private MapView mappa;
    private RoadManager roadManager;
    private MapEventsReceiver receiver;
    private MapEventsOverlay mapEventsOverlay;
    private Polyline polyline;
    private FloatingActionButton confermaPuntiButton;
    private ArrayList<GeoPoint> listaGeopoint;

    // Fine Location (da utilizzare nei setting della mappa invece del Geopoint (che per il momento setta la mappa sulle coordinate dell'Uni
    // Per un esempio dare un occhiata qui -> https://github.com/lexteo13/android-fused-location-provider-example/blob/master/app/src/main/java/com/ideeastudios/example/location/fused/MainActivity.java
    private FusedLocationProviderClient fusedLocationProviderClient;

    public SelezionaPuntiMappaFragment () {
        listaGeopoint = new ArrayList<>();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_seleziona_punti_mappa, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUI(view);
	    /*
	        OSMDroid richiede la definizione di uno user agent in modo da poter utilizzare le mappe online
	        Dai log infatti viene specificato -> `OsmDroid: Please configure a relevant user agent; current value is: osmdroid`
	        Per risolvere questo problema setto lo user agent (soluzione presa -> https://github.com/k3b/APhotoManager/commit/af0975d51f68a1508fc0ee4a0e7b99fd8a82f60d)
	    */
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);

        getLocation();
    }

    private void setUI(View view) {
        TopAppBar = requireActivity().findViewById(R.id.AppBarLayout);
        TopToolBar = (MaterialToolbar) TopAppBar.getChildAt(0);

        TopToolBar.setTitle(R.string.SelezionaPuntiMappaButton);
        TopToolBar.getMenu().clear();
        TopToolBar.setNavigationIcon(R.drawable.arrow_back_24dp);
        TopToolBar.inflateMenu(R.menu.seleziona_punti_mappa_topappbar_menu);

        eliminaSelezioneMenuItem = TopToolBar.getMenu().getItem(0);

        confermaPuntiButton = requireActivity().findViewById(R.id.FloatingActionButton);
        confermaPuntiButton.setVisibility(View.VISIBLE);
        confermaPuntiButton.setImageResource(R.drawable.check_36dp);

        requireActivity().findViewById(R.id.NuovoItinerarioPopup).setVisibility(View.GONE);
        requireActivity().findViewById(R.id.NuovoItinerarioPopupOutside).setVisibility(View.GONE);
        requireActivity().findViewById(R.id.RicercaTextInput_appBar).setVisibility(View.GONE);

        mappa = view.findViewById(R.id.mapview);
        mappa.setTileSource(TileSourceFactory.MAPNIK);
        mappa.setClickable(true);
        mappa.getController().setZoom(18.0);
        mappa.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.ALWAYS);
        mappa.setMultiTouchControls(true);

        TopToolBar.setNavigationOnClickListener(click -> requireActivity().getSupportFragmentManager()
                .popBackStackImmediate());

        eliminaSelezioneMenuItem.setOnMenuItemClickListener(click -> {
            listaGeopoint.clear();
            mappa.getOverlays().clear();
            mappa.getOverlays().add(mapEventsOverlay);
            return false;
        });

        confermaPuntiButton.setOnClickListener(click -> {
            if ((listaGeopoint.isEmpty()) || (listaGeopoint.size() < 2)) {
                Toasty.info(context,
                        "Per confermare è necessario selezionare prima il punto di inizio e il punto di fine del percorso",
                        Toast.LENGTH_LONG, true).show();
            } else {
                // Prima coppia della lista geopoint costituita da {[LONGITUDINE, LATITUDINE], [LONGITUDINE, LATITUDINE]}
                Double puntoDiInizioLongitudine = listaGeopoint.get(0).getLongitude();
                Double puntoDiInizioLatitudine = listaGeopoint.get(0).getLatitude();

                // Seconda coppia
                Double puntoDiFineLongitudine = listaGeopoint.get(1).getLongitude();
                Double puntoDiFineLatitudine = listaGeopoint.get(1).getLatitude();

                passaggioANuovoItinerarioFragment(puntoDiInizioLongitudine, puntoDiInizioLatitudine, puntoDiFineLongitudine, puntoDiFineLatitudine);
            }
        });
    }

    private void getLocation(){
        int checkFineLocation = ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
        int checkCoarseLocation = ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION);

        if((checkFineLocation == Constants.PERMISSION_GRANTED) && (checkCoarseLocation == Constants.PERMISSION_GRANTED)){

            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(requireActivity(), location -> {
                if (location != null) {
                    selectGeoPoints(location.getLatitude(), location.getLongitude());
                } else {
                    Toasty.info(context,
                            "Non è stato possibile ottenere la tua posizione\n" +
                                    "Controlla di avere abilitato i permessi e il GPS",
                            Toast.LENGTH_LONG, true).show();
                }
            });
        }
    }

    private void selectGeoPoints(Double latitudine, Double longitudine) {
        // Università Federico II Monte Sant'Angelo
        //GeoPoint point = new GeoPoint(40.839317, 14.185151);

        // Posizione corrente
        GeoPoint point = new GeoPoint(latitudine, longitudine);
        mappa.getController().setCenter(point);

        receiver = new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                if (listaGeopoint.size() < 2) {
                    listaGeopoint.add(p);
                    Marker marker = new Marker(mappa);
                    marker.setPosition(p);
                    marker.setTitle(
                            getResources().getString(R.string.Latitudine) + p.getLatitude() + "\n" +
                                    getResources().getString(R.string.Longitudine) + p.getLongitude());
                    mappa.getOverlays().add(marker);
                }

                if (listaGeopoint.size() == 2) {
                    new Thread(() -> {
                        roadManager = new OSRMRoadManager(context, null);
                        Road percorso = roadManager.getRoad(listaGeopoint);
                        if (percorso.mStatus == Road.STATUS_OK) {
                            polyline = RoadManager.buildRoadOverlay(percorso, Color.RED, 3.0f);
                            mappa.getOverlays().add(polyline);
                        }
                    }).start();
                }
                return false;
            }

            @Override
            public boolean longPressHelper(GeoPoint p) {
                Toasty.info(context,
                        "Per selezionare il punto desiderato\nUtilizza un singolo tocco", Toast.LENGTH_SHORT).show();
                return false;
            }
        };
        mapEventsOverlay = new MapEventsOverlay(receiver);
        mappa.getOverlays().add(mapEventsOverlay);
    }

    private void passaggioANuovoItinerarioFragment(Double puntoDiInizioLongitudine, Double puntoDiInizioLatitudine, Double puntoDiFineLongitudine, Double puntoDiFineLatitudine){
        NuovoItinerarioFragment nuovoItinerarioFragment = new NuovoItinerarioFragment(
                puntoDiInizioLongitudine, puntoDiInizioLatitudine,
                puntoDiFineLongitudine, puntoDiFineLatitudine);

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.FragmentContainer, nuovoItinerarioFragment);
        fragmentTransaction.commit();

    }
}
