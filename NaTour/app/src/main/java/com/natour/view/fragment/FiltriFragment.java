package com.natour.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.natour.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.natour.model.Itinerario;
import com.natour.presenter.callbackInterface.CallBackItinerario;
import com.natour.presenter.request.ItinerarioRequest;
import com.natour.utils.handler.ApiHandler;
import com.natour.utils.persistence.LocalUser;
import com.natour.utils.persistence.LocalUserDbManager;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Response;

public class FiltriFragment extends Fragment {
    private static final String FiltriFragment_TAG = "FiltriFragment";
    private Context context;

    private AppBarLayout    TopAppBar;
    private MaterialToolbar TopToolBar;
    private BottomNavigationView MenuInferiore;

    private MenuItem ConfermaButton;

    private RadioGroup FiltriRadioGroup;
    private MaterialRadioButton FiltroNomeRadioButton;
    private MaterialRadioButton FiltroDurataRadioButton;
    private MaterialRadioButton FiltroZonaRadioButton;
    private MaterialRadioButton FiltroUtenteProprietarioButton;
    private MaterialRadioButton FiltroRecentiButton;

    private TextInputLayout     FiltroTextInput;
    private TextInputEditText   FiltroEditText;

    private LinearLayout        FiltroDurataLayout;
    private TextInputLayout     OreTextInput, MinutiTextInput, SecondiTextInput;
    private EditText            OreEditText, MinutiEditText, SecondiEditText;
    private TextView            FiltroDurataErrorTextView;

    private TextInputLayout     FiltroZonaTextInput;
    private MaterialAutoCompleteTextView FiltroZonaAutoCompleteTextView;

    private ApiHandler apiHandler;
    private List<Itinerario> listaItinerari;

    // Db Manager
    private LocalUserDbManager localUserDbManager;
    private LocalUser localUser;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_filtri, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUI(view);
        localUser = getDatiutente();
    }

    // Informazioni dell'utente locale
    private LocalUser getDatiutente(){
        LocalUserDbManager localUserDbManager = new LocalUserDbManager(this.getContext());
        localUserDbManager.openR();
        LocalUser localUser = localUserDbManager.fetchDataUser();
        localUserDbManager.closeDB();
        return localUser;
    }

    private void setUI(View view) {
        TopAppBar = requireActivity().findViewById(R.id.AppBarLayout);
        TopToolBar = (MaterialToolbar) TopAppBar.getChildAt(0);
        MenuInferiore = requireActivity().findViewById(R.id.MenuInferiore);

        TopToolBar.setTitle(R.string.FiltriFragment);
        TopToolBar.getMenu().clear();
        TopToolBar.setNavigationIcon(R.drawable.arrow_back_24dp);
        TopToolBar.inflateMenu(R.menu.check_topappbar_menu);

        ConfermaButton = TopToolBar.getMenu().findItem(R.id.CheckButton);

        requireActivity().findViewById(R.id.RicercaTextInput_appBar).setVisibility(View.GONE);
        requireActivity().findViewById(R.id.FloatingActionButton).setVisibility(View.GONE);
        requireActivity().findViewById(R.id.NuovoItinerarioPopup).setVisibility(View.GONE);
        requireActivity().findViewById(R.id.NuovoItinerarioPopupOutside).setVisibility(View.GONE);

        FiltriRadioGroup = view.findViewById(R.id.FiltriRadioGroup);
        FiltroNomeRadioButton = view.findViewById(R.id.FiltroNomeItinerario_radio);
        FiltroDurataRadioButton = view.findViewById(R.id.FiltroDurataItinerario_radio);
        FiltroZonaRadioButton = view.findViewById(R.id.FiltroZonaItinerario_radio);
        FiltroUtenteProprietarioButton = view.findViewById(R.id.FiltroUtenteProprietario_radio);
        FiltroRecentiButton = view.findViewById(R.id.FiltroRecenti_radio);

        FiltroTextInput = view.findViewById(R.id.FiltriTextInputLayout);
        FiltroEditText = view.findViewById(R.id.FiltriEditText);

        FiltroDurataLayout = view.findViewById(R.id.FiltroDurataLayout);
        OreTextInput = view.findViewById(R.id.OreTextInput_filtro);
        MinutiTextInput = view.findViewById(R.id.MinutiTextInput_filtro);
        SecondiTextInput = view.findViewById(R.id.SecondiTextInput_filtro);
        OreEditText = view.findViewById(R.id.OreEditText_filtro);
        MinutiEditText = view.findViewById(R.id.MinutiEditText_filtro);
        SecondiEditText = view.findViewById(R.id.SecondiEditText_filtro);
        FiltroDurataErrorTextView = view.findViewById(R.id.FiltroDurataErrorText);

        FiltroZonaTextInput = view.findViewById(R.id.FiltroZonaTextInputLayout);
        FiltroZonaAutoCompleteTextView = view.findViewById(R.id.FiltroZonaAutoCompleteTextView);
        FiltroZonaAutoCompleteTextView.setAdapter(new ArrayAdapter<>(
                context,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.ZoneItinerario)
        ));
        FiltroZonaAutoCompleteTextView.setText(
                FiltroZonaAutoCompleteTextView.getAdapter().getItem(0).toString(), false);

        TopToolBar.setNavigationOnClickListener(click -> requireActivity().getSupportFragmentManager()
                .popBackStackImmediate());

        FiltroNomeRadioButton.setOnCheckedChangeListener((radioButton, isChecked) -> {
            if (isChecked) {
                FiltroTextInput.setVisibility(View.VISIBLE);
                FiltroZonaTextInput.setVisibility(View.GONE);
                FiltroDurataLayout.setVisibility(View.GONE);
                FiltroDurataErrorTextView.setVisibility(View.GONE);
                FiltroEditText.setText("");
            }
        });

        FiltroDurataRadioButton.setOnCheckedChangeListener((radioButton, isChecked) -> {
            if (isChecked) {
                FiltroTextInput.setVisibility(View.GONE);
                FiltroZonaTextInput.setVisibility(View.GONE);
                FiltroDurataLayout.setVisibility(View.VISIBLE);
                FiltroDurataErrorTextView.setVisibility(View.GONE);
                OreEditText.setText("");
                MinutiEditText.setText("");
                SecondiEditText.setText("");
            }
        });

        FiltroZonaRadioButton.setOnCheckedChangeListener((radioButton, isChecked) -> {
            if (isChecked) {
                FiltroTextInput.setVisibility(View.GONE);
                FiltroZonaTextInput.setVisibility(View.VISIBLE);
                FiltroDurataLayout.setVisibility(View.GONE);
                FiltroDurataErrorTextView.setVisibility(View.GONE);
            }
        });

        FiltroUtenteProprietarioButton.setOnCheckedChangeListener((radioButton, isChecked) -> {
            if (isChecked) {
                FiltroTextInput.setVisibility(View.VISIBLE);
                FiltroZonaTextInput.setVisibility(View.GONE);
                FiltroDurataLayout.setVisibility(View.GONE);
                FiltroDurataErrorTextView.setVisibility(View.GONE);
                FiltroEditText.setText("");
                FiltroTextInput.setEnabled(true);
            }
        });

        FiltroRecentiButton.setOnCheckedChangeListener((radioButton, isChecked) -> {
            if (isChecked) {
                FiltroTextInput.setVisibility(View.GONE);
                FiltroZonaTextInput.setVisibility(View.GONE);
                FiltroDurataLayout.setVisibility(View.GONE);
                FiltroDurataErrorTextView.setVisibility(View.GONE);
            }
        });

        ConfermaButton.setOnMenuItemClickListener(click -> {
            retrievalItinerari();
            return false;
        });
    }

    // Retrieval degli itinerari in base ai filtri settati
    private void retrievalItinerari() {
        String testoFiltro = FiltroEditText.getText().toString();

        if(!testoFiltro.isEmpty() && FiltroTextInput.isEnabled()){
            if (FiltroNomeRadioButton.isChecked()){
                Log.d(FiltriFragment_TAG, "Filtro " + FiltroNomeRadioButton.getText());
                retrievalByNomeItinerario(testoFiltro);
            } else if (FiltroUtenteProprietarioButton.isChecked()){
                Log.d(FiltriFragment_TAG, "Filtro " + FiltroUtenteProprietarioButton.getText());
                retrievalByUtenteProprietario(testoFiltro);
            }
        } else if (FiltroRecentiButton.isChecked()){
            Log.d(FiltriFragment_TAG, "Filtro " + FiltroRecentiButton.getText());
            retrievalByRecenti();

        } else if (FiltroZonaAutoCompleteTextView.getVisibility() == View.VISIBLE && FiltroZonaRadioButton.isChecked()){
            Log.d(FiltriFragment_TAG, "Filtro " + FiltroZonaRadioButton.getText());
            retrievalByZonaGeografica(FiltroZonaTextInput.getEditText().getText().toString());

        } else if (FiltroDurataRadioButton.isChecked()) {

            // Recupero della durata in ore, minuti e secondi
            String oreText = OreEditText.getText().toString();
            int ore = (oreText.isEmpty() ? 0 : Integer.parseInt(oreText));

            String minutiText = MinutiEditText.getText().toString();
            int minuti = (minutiText.isEmpty() ? 0 : Integer.parseInt(minutiText));

            String secondiText = SecondiEditText.getText().toString();
            int secondi = (secondiText.isEmpty() ? 0 : Integer.parseInt(secondiText));

            // Controlli sulla durata
            FiltroDurataErrorTextView.setVisibility(View.GONE);

            if (ore == 0 && minuti == 0 && secondi == 0) {
                FiltroDurataErrorTextView.setVisibility(View.VISIBLE);
                FiltroDurataErrorTextView.setText("Scegli un valore per i secondi maggiori di 0");
            } else if (ore > 99) {
                FiltroDurataErrorTextView.setVisibility(View.VISIBLE);
                FiltroDurataErrorTextView.setText("Scegli un valore per le ore minore di 100");
            } else if (minuti > 59) {
                FiltroDurataErrorTextView.setVisibility(View.VISIBLE);
                FiltroDurataErrorTextView.setText("Scegli un valore per i minuti minore di 60");
            } else if (secondi > 59) {
                FiltroDurataErrorTextView.setVisibility(View.VISIBLE);
                FiltroDurataErrorTextView.setText("Scegli un valore per i secondi minore di 60");
            } else {
                // Formattazione della stringa "testoFiltro" con il pattern HH:MM:SS
                testoFiltro = ((ore >= 0 && ore < 10) ? "0" + ore : String.valueOf(ore));
                testoFiltro = testoFiltro + ":";
                testoFiltro = ((minuti >= 0 && minuti < 10) ? testoFiltro + "0" + minuti : testoFiltro + minuti);
                testoFiltro = testoFiltro + ":";
                testoFiltro = ((secondi >= 0 && secondi < 10) ? testoFiltro + "0" + secondi : testoFiltro + secondi);

                Log.d(FiltriFragment_TAG, testoFiltro);
                Log.d(FiltriFragment_TAG, "Filtro " + FiltroDurataRadioButton.getText());
                retrievalByDurata(testoFiltro);
            }
        } else {
            Log.d(FiltriFragment_TAG, "Nessun filtro selezionato");
        }
    }

    // Retrieval dei dati in base al nome (Essendo il nome una chiave primaria, e quindi, ipoteticamente un match esclusivo, facciamo una query che sia case insentive)
    private void retrievalByNomeItinerario(String nomeItinerario){
        ItinerarioRequest request = new ItinerarioRequest();
        request.getItinerariByNomeItinerario(nomeItinerario, new CallBackItinerario() {
            @Override
            public void onSuccess(Itinerario itinerario) {
                // Nulla da fare qui
            }

            @Override
            public void onSuccessList(List<Itinerario> itinerari) {
                Log.d(FiltriFragment_TAG, "onSuccessList (retrieval Itinerari by Nome itinerario): Richiesta completata");
                listaItinerari = itinerari;
                passaggioAScopriFragment();
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.d(FiltriFragment_TAG, "onFailure (retrieval Itinerari by Nome Itinerario): Richiesta fallita " + throwable.getLocalizedMessage());
                Toasty.info(context, "Non è stato possibile completare la richiesta. Riprova più tardi", Toasty.LENGTH_SHORT, true).show();
            }

            @Override
            public void onSuccessResponse(Response<Void> response) {
                // Nulla da fare qui
            }
        });
    }

    // Retrieval dei dati in base alla durata
    private void retrievalByDurata(String durata){
        ItinerarioRequest request = new ItinerarioRequest();
        request.getItinerariByDurata(durata, new CallBackItinerario() {
            @Override
            public void onSuccess(Itinerario itinerario) {
                // Nulla da fare qui
            }

            @Override
            public void onSuccessList(List<Itinerario> itinerari) {
                Log.d(FiltriFragment_TAG, "onSuccessList (retrieval Itinerari by Durata): Richiesta completata. Risultati trovati: " + itinerari.size());
                listaItinerari = itinerari;
                passaggioAScopriFragment();
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.d(FiltriFragment_TAG, "onFailure (retrieval Itinerari by Durata): Richiesta fallita " + throwable.getLocalizedMessage());
                Toasty.info(context, "Non è stato possibile completare la richiesta. Riprova più tardi", Toasty.LENGTH_SHORT, true).show();
            }

            @Override
            public void onSuccessResponse(Response<Void> response) {
                // Nulla da fare qui
            }
        });
    }

    // Retrieval dei dati in base all'utente proprietario
    private void retrievalByUtenteProprietario(String utenteProprietario){
        ItinerarioRequest request = new ItinerarioRequest();
        request.getItinerariByUtente(utenteProprietario, new CallBackItinerario() {
            @Override
            public void onSuccess(Itinerario itinerario) {
                // Nulla da fare qui
            }

            @Override
            public void onSuccessList(List<Itinerario> itinerari) {
                Log.d(FiltriFragment_TAG, "onSuccessList (retrieval Itinerari by Utente Proprietario): Richiesta completata. Risultati trovati: " + itinerari.size());
                listaItinerari = itinerari;
                passaggioAScopriFragment();
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.d(FiltriFragment_TAG, "onFailure (retrieval Itinerari by Utente Proprietario): Richiesta fallita " + throwable.getLocalizedMessage());
                Toasty.info(context, "Non è stato possibile completare la richiesta. Riprova più tardi", Toasty.LENGTH_SHORT, true).show();
            }

            @Override
            public void onSuccessResponse(Response<Void> response) {
                // Nulla da fare qui
            }
        });
    }

    // Retrieval dei dati per Recenti
    private void retrievalByRecenti(){
        ItinerarioRequest request = new ItinerarioRequest();
        request.getItinerariDescOrder(new CallBackItinerario() {
            @Override
            public void onSuccess(Itinerario itinerario) {
               // Nulla da fare qui
            }

            @Override
            public void onSuccessList(List<Itinerario> itinerari) {
                Log.d(FiltriFragment_TAG, "onSuccessList (retrieval Itinerari by Zona geografica): Richiesta completata. Risultati trovati: " + itinerari.size());
                listaItinerari = itinerari;
                passaggioAScopriFragment();
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.d(FiltriFragment_TAG, "onFailure (retrieval Itinerari by Zona geografica): Richiesta fallita " + throwable.getLocalizedMessage());
                Toasty.info(context, "Non è stato possibile completare la richiesta. Riprova più tardi", Toasty.LENGTH_SHORT, true).show();
            }

            @Override
            public void onSuccessResponse(Response<Void> response) {
                // Nulla da fare qui
            }
        });

    }

    // Retrieval dei dati in base alla zona geografica
    private void retrievalByZonaGeografica(String zonaGeografica){
        ItinerarioRequest request = new ItinerarioRequest();
        request.getItinerariByZonaGeografica(zonaGeografica, new CallBackItinerario() {
            @Override
            public void onSuccess(Itinerario itinerario) {
                // Nulla da fare qui
            }

            @Override
            public void onSuccessList(List<Itinerario> itinerari) {
                Log.d(FiltriFragment_TAG, "onSuccessList (retrieval Itinerari by Zona geografica): Richiesta completata. Risultati trovati: " + itinerari.size());
                listaItinerari = itinerari;
                passaggioAScopriFragment();
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.d(FiltriFragment_TAG, "onFailure (retrieval Itinerari by Zona geografica): Richiesta fallita " + throwable.getLocalizedMessage());
                Toasty.info(context, "Non è stato possibile completare la richiesta. Riprova più tardi", Toasty.LENGTH_SHORT, true).show();
            }

            @Override
            public void onSuccessResponse(Response<Void> response) {
                // Nulla da fare qui
            }
        });
    }

    // Passaggio a Scropri Fragment
    private void passaggioAScopriFragment(){
        ScopriFragment scopriFragment = new ScopriFragment(listaItinerari);
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.FragmentContainer, scopriFragment);
        fragmentTransaction.commit();
    }
}
