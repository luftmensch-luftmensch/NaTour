package com.natour.view.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.natour.BuildConfig;
import com.natour.R;

import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;

public class VisualizzaInfoMappaFragment extends Fragment {

    private Context context;

    private AppBarLayout TopAppBar;
    private MaterialToolbar TopToolBar;

    private MapView mappa;
    private RoadManager roadManager;
    private MapEventsReceiver receiver;
    private Polyline polyline;
    private ArrayList<GeoPoint> listaGeopoint;

    private Double puntoDiInizioLongitudine, puntoDiInizioLatitudine;
    private Double puntoDiFineLongitudine, puntoDiFineLatitudine;


    public VisualizzaInfoMappaFragment(Double puntoDiInizioLongitudine, Double puntoDiInizioLatitudine, Double puntoDiFineLongitudine, Double puntoDiFineLatitudine) {
        this.puntoDiInizioLongitudine = puntoDiInizioLongitudine;
        this.puntoDiInizioLatitudine = puntoDiInizioLatitudine;
        this.puntoDiFineLongitudine = puntoDiFineLongitudine;
        this.puntoDiFineLatitudine = puntoDiFineLatitudine;

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
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);
        setUI(view);
    }

    private void setUI(View view) {
        TopAppBar = requireActivity().findViewById(R.id.AppBarLayout);
        TopToolBar = (MaterialToolbar) TopAppBar.getChildAt(0);

        TopToolBar.setTitle(R.string.SelezionaPuntiMappaButton);
        TopToolBar.getMenu().clear();
        TopToolBar.setNavigationIcon(R.drawable.arrow_back_24dp);

        requireActivity().findViewById(R.id.FloatingActionButton).setVisibility(View.GONE);
        requireActivity().findViewById(R.id.NuovoItinerarioPopup).setVisibility(View.GONE);
        requireActivity().findViewById(R.id.NuovoItinerarioPopupOutside).setVisibility(View.GONE);
        requireActivity().findViewById(R.id.RicercaTextInput_appBar).setVisibility(View.GONE);

        mappa = view.findViewById(R.id.mapview);
        mappa.setTileSource(TileSourceFactory.MAPNIK);
        mappa.setClickable(true);
        mappa.getController().setZoom(18.0);
        mappa.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.ALWAYS);
        mappa.setMultiTouchControls(true);

        // Caricamento dei punti sulla mappa
        GeoPoint puntoDiInizio = new GeoPoint(puntoDiInizioLatitudine, puntoDiInizioLongitudine);
        GeoPoint puntoDiFine = new GeoPoint(puntoDiFineLatitudine, puntoDiFineLongitudine);

        listaGeopoint.add(puntoDiInizio);
        listaGeopoint.add(puntoDiFine);
        mappa.getController().setCenter(puntoDiInizio);

        new Thread(() -> {

            for (GeoPoint p : listaGeopoint) {
                Marker marker = new Marker(mappa);
                marker.setPosition(p);
                marker.setTitle(getResources().getString(R.string.Latitudine) + p.getLatitude() + "\n" +
                        getResources().getString(R.string.Longitudine) + p.getLongitude());
                mappa.getOverlays().add(marker);
            }

            roadManager = new OSRMRoadManager(context, null);
            Road percorso = roadManager.getRoad(listaGeopoint);
            if (percorso.mStatus == Road.STATUS_OK) {
                polyline = RoadManager.buildRoadOverlay(percorso, Color.RED, 3.0f);
                mappa.getOverlays().add(polyline);
            }

        }).start();

        TopToolBar.setNavigationOnClickListener(click -> requireActivity().getSupportFragmentManager()
                .popBackStackImmediate());
    }
}