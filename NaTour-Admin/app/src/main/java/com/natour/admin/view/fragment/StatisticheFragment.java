/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.admin.view.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.natour.admin.R;
import com.natour.admin.presenter.callbackInterface.CallBackStatistiche;
import com.natour.admin.presenter.request.AdminRequest;

import java.util.ArrayList;
import java.util.List;

// Per la gestione dei dati (mediante diagrammi) facciamo uso della libreria MPAndroidChart (https://github.com/PhilJay/MPAndroidChart)
public class StatisticheFragment extends Fragment {
    private static final String StatisticheFragment_TAG = "StatisticheFragment";
    Context context;

    private AppBarLayout TopAppBar;
    private MaterialToolbar TopToolBar;
    private BottomNavigationView MenuInferiore;
    private MenuItem RicaricaButton;
    private BarChart barChart;

    // Definiamo un array di colori custom da utilizzare per la definizione automatica delle barre nel diagramma
    private static final int[] CUSTOM_COLORS = {
            Color.rgb(193, 37, 82), Color.rgb(255, 102, 0), Color.rgb(245, 199, 0),
            Color.rgb(106, 150, 31), Color.rgb(179, 100, 53)
    };

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_statistiche, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUI(view);
    }

    private void setUI(View view) {
        TopAppBar = requireActivity().findViewById(R.id.AppBarLayout);
        TopToolBar = (MaterialToolbar) TopAppBar.getChildAt(0);

        TopToolBar.setTitle(R.string.StatisticheButton);
        TopToolBar.getMenu().clear();
        TopToolBar.setNavigationIcon(null);
        TopToolBar.inflateMenu(R.menu.statistiche_topappbar_menu);
        RicaricaButton = TopToolBar.getMenu().findItem(R.id.RicaricaButton);

        MenuInferiore = requireActivity().findViewById(R.id.MenuInferiore);
        MenuInferiore.getMenu().findItem(R.id.StatisticheButton).setChecked(true);

        // Retrieval delle statistiche di utilizzo presenti sul db
        getStatistiche();

        RicaricaButton.setOnMenuItemClickListener(click -> {
            getStatistiche();
            return false;
        });
    }

    // Retrieval delle statistiche dal db
    private void getStatistiche(){
        AdminRequest adminRequest = new AdminRequest();
        adminRequest.getStatistiche(new CallBackStatistiche() {
            @Override
            public void onSuccess(List<Integer> listaValoriStatistiche) {
                Log.d(StatisticheFragment_TAG, "onSuccess: Retrieval della lista dei dati per la gestione delle statistiche");
                setChartDataValues(listaValoriStatistiche);
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.d(StatisticheFragment_TAG, "onFailure: Richiesta dati fallita " + throwable.getLocalizedMessage());
            }
        });

    }

    // Una volta fatto il retrieval dei dati necessari carichiamo il backend con i dati
    private void setChartDataValues(List<Integer> listaValoriStatistiche){
        // Definizione di un arraylist necessario a inserire le entry per la visualizzazione dei dati all'interno di un bar chart
        ArrayList<BarEntry> barEntries = new ArrayList<>();

        for(int i = 0; i < listaValoriStatistiche.size(); i++){ barEntries.add(new BarEntry(i, listaValoriStatistiche.get(i))); }

        // Definizione di un dataset
        BarDataSet statisticheDataSet = new BarDataSet(barEntries, "");
        // Settiamo automaticamente i colori da utilizzare
        statisticheDataSet.setColors(CUSTOM_COLORS);

        // Settiamo il testo che indica il valore di ogni singola barra a bianco in modo da poter essere più leggibile
        statisticheDataSet.setValueTextColor(Color.WHITE);

        BarData data = new BarData(statisticheDataSet);
        data.setBarWidth(0.9f);

        barChart = requireActivity().findViewById(R.id.BarChart);

        // Opzioni per la barChart
        // Lo scaling può essere effettuato una coordinata per volta
        barChart.setPinchZoom(false);
        barChart.setDrawValueAboveBar(true);
        barChart.setMaxVisibleValueCount(60);
        barChart.setFitBars(true);
        barChart.getDescription().setEnabled(false);
        barChart.getXAxis().setEnabled(false);

        // Opzioni sulla legenda
        Legend legenda = barChart.getLegend();
        legenda.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legenda.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legenda.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legenda.setDrawInside(false);
        legenda.setForm(Legend.LegendForm.SQUARE);
        legenda.setFormSize(9f);
        legenda.setTextSize(10f);
        legenda.setXEntrySpace(4f);
        legenda.setTextColor(Color.WHITE);

        // Definiamo la lista di entry per far corrispondere ad ogni singola barra un nome nella legenda con lo stesso colore
        LegendEntry itinerariTotali = new LegendEntry("Itinerari", Legend.LegendForm.SQUARE, 10f, 2f, null, CUSTOM_COLORS[0]);
        LegendEntry chatRoomTotali = new LegendEntry("Chat rooms", Legend.LegendForm.SQUARE, 10f, 2f, null, CUSTOM_COLORS[1]);
        LegendEntry messaggiTotali = new LegendEntry("Messaggi", Legend.LegendForm.SQUARE, 10f, 2f, null, CUSTOM_COLORS[2]);
        LegendEntry utentiTotali = new LegendEntry("Utenti", Legend.LegendForm.SQUARE, 10f, 2f, null, CUSTOM_COLORS[3]);

        // Inseriamo ogni entry all'interno della legenda
        legenda.setCustom(new LegendEntry[]{itinerariTotali, chatRoomTotali, messaggiTotali, utentiTotali });

        // Visto che usiamo la dark mode di default settiamo le coordinate di colore biano in modo da essere più leggibili
        XAxis labelAsseX = barChart.getXAxis();
        YAxis labelAsseY = barChart.getAxisRight();
        labelAsseX.setTextColor(Color.WHITE);
        labelAsseY.setTextColor(Color.WHITE);

        barChart.setData(data);
        barChart.invalidate();
    }
}