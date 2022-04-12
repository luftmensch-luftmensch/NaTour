/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.admin.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.natour.admin.R;
import com.natour.admin.utils.handler.ApiHandler;

public class SegnalazioniFragment extends Fragment {

    private static final String SegnalazioniFragment_TAG = "SegnalazioniFragment";
    private Context context;

    private ApiHandler apiHandler;

    private AppBarLayout TopAppBar;
    private MaterialToolbar TopToolBar;
    private BottomNavigationView MenuInferiore;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_segnalazioni, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUI(view);
    }

    private void setUI(View view) {
        TopAppBar = requireActivity().findViewById(R.id.AppBarLayout);
        TopToolBar = (MaterialToolbar) TopAppBar.getChildAt(0);

        TopToolBar.setTitle(R.string.SegnalazioniButton);
        TopToolBar.getMenu().clear();
        TopToolBar.setNavigationIcon(null);

        MenuInferiore = requireActivity().findViewById(R.id.MenuInferiore);
        MenuInferiore.getMenu().findItem(R.id.SegnalazioniButton).setChecked(true);
    }
}