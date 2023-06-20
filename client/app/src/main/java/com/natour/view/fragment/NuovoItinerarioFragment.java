/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.transition.Slide;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;
import androidx.transition.TransitionSet;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;
import com.natour.R;
import com.natour.model.Itinerario;
import com.natour.model.TagRicerca;
import com.natour.presenter.amplify.AmplifyS3Request;
import com.natour.presenter.callbackInterface.CallBackGeneric;
import com.natour.presenter.callbackInterface.CallBackItinerario;
import com.natour.presenter.callbackInterface.CallBackTagRicerca;
import com.natour.presenter.request.ItinerarioRequest;
import com.natour.presenter.request.TagRicercaRequest;
import com.natour.utils.constants.Constants;
import com.natour.utils.persistence.LocalUser;
import com.natour.utils.persistence.LocalUserDbManager;
import com.natour.view.CustomTokenCompleteTextView;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Response;

public class NuovoItinerarioFragment extends Fragment {
    private static final String NuovoItinerarioFragment_TAG = "NuovoItinerarioFragment";
    private Context context;

    private NestedScrollView NuovoItinerarioScrollView;
    private LinearLayout    NuovoItinerarioLayout;
    private AppBarLayout    TopAppBar;
    private MaterialToolbar TopToolBar;

    private CardView        InformazioniCardView;
	private TextInputLayout NomeTextInput;
	private TextInputLayout DescrizioneTextInput;

	private CardView        CaratteristicheCardView;
	private TextInputLayout CittaTextInput, LunghezzaTextInput, DislivelloTextInput;
	private TextInputLayout OreTextInput, MinutiTextInput, SecondiTextInput;
	private TextView        DurataErrorTextView;
    private CheckBox        DisabilitaCheckBox;
	private MaterialAutoCompleteTextView ZonaAutoCompleteTextView;
	private MaterialAutoCompleteTextView DifficoltaAutoCompleteTextView;

	private CardView            ConfermaCardView;
	private MaterialButton      AnnullaButton;
	private MaterialButton      ConfermaButton;

	private CustomTokenCompleteTextView TagRicercaTextView;
	private String[] tags;
    ArrayAdapter<String> tagAdapter;

    private CardView ConfermaSelezioneImmagineCardView;
    private MaterialButton SiButton, NoButton;

    // Transizioni
    private Transition InformazioniTransition;
    private Transition CaratteristicheTransition;
    private Transition ConfermaTransition;
    private Transition ConfermaSelezioneImmagineTransition;
    private TransitionSet transitionSet;

    // Db Manager
    private LocalUserDbManager localUserDbManager;
    private LocalUser localUser;

    // Request per aggiungere un nuovo itinerario
    private String nome;
    private String descrizione;
    private String citta;
    private String lunghezza;
    private String dislivello;
    private String durata;

    private Double puntoDiInizioLongitudine, puntoDiInizioLatitudine;
    private Double puntoDiFineLongitudine, puntoDiFineLatitudine;

    private Itinerario nuovoItinerario;
    private ItinerarioRequest requestItinerario;
    private TagRicercaRequest requestTagRicerca;

    // Unchecked call to 'launch(I)' as a member of raw type 'androidx.activity.result.ActivityResultLauncher' (Di default era
    private ActivityResultLauncher<Intent> activityResultLauncherImmagineItinerario;

    public NuovoItinerarioFragment() {

    }

    public NuovoItinerarioFragment(Double puntoDiInizioLongitudine, Double puntoDiInizioLatitudine, Double puntoDiFineLongitudine, Double puntoDiFineLatitudine){
        this.puntoDiInizioLongitudine = puntoDiInizioLongitudine;
        this.puntoDiInizioLatitudine = puntoDiInizioLatitudine;
        this.puntoDiFineLongitudine = puntoDiFineLongitudine;
        this.puntoDiFineLatitudine = puntoDiFineLatitudine;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }
	
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_nuovo_itinerario, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        localUser = getDatiutente();
        setUI(view);
    }

    private void setUI(View view) {
        NuovoItinerarioScrollView = view.findViewById(R.id.NuovoItinerarioScrollView);
        NuovoItinerarioLayout = view.findViewById(R.id.NuovoItinerarioLayout);

        TopAppBar = requireActivity().findViewById(R.id.AppBarLayout);
        TopToolBar = (MaterialToolbar) TopAppBar.getChildAt(0);

        TopToolBar.setTitle(R.string.NuovoItinerarioFragment);
        TopToolBar.getMenu().clear();
        TopToolBar.setNavigationIcon(R.drawable.arrow_back_24dp);

        requireActivity().findViewById(R.id.FloatingActionButton).setVisibility(View.GONE);
        requireActivity().findViewById(R.id.NuovoItinerarioPopup).setVisibility(View.GONE);
        requireActivity().findViewById(R.id.NuovoItinerarioPopupOutside).setVisibility(View.GONE);
        requireActivity().findViewById(R.id.RicercaTextInput_appBar).setVisibility(View.GONE);

        InformazioniCardView = view.findViewById(R.id.InformazioniPercorsoCardView_nuovo_it);
        NomeTextInput = view.findViewById(R.id.NomeItinerarioTextInput);
        DescrizioneTextInput = view.findViewById(R.id.DescrizioneItinerarioTextInput);

        CaratteristicheCardView = view.findViewById(R.id.CaratteristicheCardView_nuovo_it);
        CittaTextInput = view.findViewById(R.id.CittaTextInput);
        LunghezzaTextInput = view.findViewById(R.id.LunghezzaTextInput);
        DislivelloTextInput = view.findViewById(R.id.DislivelloTextInput);
        OreTextInput = view.findViewById(R.id.OreTextInput_nuovo_it);
        MinutiTextInput = view.findViewById(R.id.MinutiTextInput_nuovo_it);
        SecondiTextInput = view.findViewById(R.id.SecondiTextInput_nuovo_it);
        DurataErrorTextView = view.findViewById(R.id.DurataErrorText_nuovo_it);
        DisabilitaCheckBox = view.findViewById(R.id.DisabilitaCheckBox);

        ZonaAutoCompleteTextView = view.findViewById(R.id.ZonaAutoCompleteTextView);
        ZonaAutoCompleteTextView.setAdapter(new ArrayAdapter<>(
                context,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.ZoneItinerario)
        ));
        ZonaAutoCompleteTextView.setText(ZonaAutoCompleteTextView.getAdapter().getItem(0).toString(), false);

        DifficoltaAutoCompleteTextView = view.findViewById(R.id.DifficoltaAutoCompleteTextView);
        DifficoltaAutoCompleteTextView.setAdapter(new ArrayAdapter<>(
                context,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.DifficoltaItinerario)
        ));
        DifficoltaAutoCompleteTextView.setText(
                DifficoltaAutoCompleteTextView.getAdapter().getItem(0).toString(), false);

        tags = new String[]{};
        tagAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, tags);
        TagRicercaTextView = view.findViewById(R.id.TagRicercaTextView);
        TagRicercaTextView.setAdapter(tagAdapter);
        TagRicercaTextView.setActivated(false);

        ConfermaCardView = view.findViewById(R.id.ConfermaCardView_nuovo_it);
        AnnullaButton = view.findViewById(R.id.AnnullaButton_nuovo_it);
        ConfermaButton = view.findViewById(R.id.ConfermaButton_nuovo_it);

        ConfermaSelezioneImmagineCardView = view.findViewById(R.id.ConfermaSelezionaImmagineCardView_nuovo_it);
        SiButton = view.findViewById(R.id.SiButton_nuovo_it);
        NoButton = view.findViewById(R.id.NoButton_nuovo_it);

        // Animazione per InformazioniCardView
        InformazioniTransition = new Slide(Gravity.END)
                .setDuration(500)
                .addTarget(InformazioniCardView);

        // Animazione per CaratteristicheCardView
        CaratteristicheTransition = new Slide(Gravity.END)
                .setDuration(500)
                .addTarget(CaratteristicheCardView);

        // Animazione per ConfermaCardView
        ConfermaTransition = new Slide(Gravity.END)
                .setDuration(500)
                .addTarget(ConfermaCardView);

        // Animazione per ConfermaSelezioneImmagineCardView
        ConfermaSelezioneImmagineTransition = new Slide(Gravity.TOP)
                .setDuration(500)
                .addTarget(ConfermaSelezioneImmagineCardView);

        transitionSet = new TransitionSet()
                .addTransition(InformazioniTransition)
                .addTransition(CaratteristicheTransition)
                .addTransition(ConfermaTransition)
                .addTransition(ConfermaSelezioneImmagineTransition);

        TopToolBar.setNavigationOnClickListener(click -> requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.FragmentContainer, new ProfiloFragment())
                .commit()
        );

        ConfermaButton.setOnClickListener(click -> {
            if (controlloCampi(view)) {
                NuovoItinerarioScrollView.scrollTo(0, 0);
                TransitionManager.beginDelayedTransition(NuovoItinerarioLayout, transitionSet);
                InformazioniCardView.setVisibility(View.INVISIBLE);
                CaratteristicheCardView.setVisibility(View.INVISIBLE);
                ConfermaCardView.setVisibility(View.INVISIBLE);
                ConfermaSelezioneImmagineCardView.setVisibility(View.VISIBLE);
            }
        });

        AnnullaButton.setOnClickListener(click -> requireActivity().getSupportFragmentManager()
                .popBackStackImmediate());

        // Inserimento di una foto per l'itinerario (con s3)
        SiButton.setOnClickListener(click -> {
            // Soluzione al problema `Calling setType after setting URI in Intent constructor will clear the data`
            Intent selezionaFotoDaGalleria = new Intent();
            selezionaFotoDaGalleria.setType("image/*");
            selezionaFotoDaGalleria.setAction(Intent.ACTION_GET_CONTENT);
            activityResultLauncherImmagineItinerario.launch(Intent.createChooser(selezionaFotoDaGalleria, "Seleziona immagini"));
        });

        activityResultLauncherImmagineItinerario = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if((result.getResultCode() == Constants.RESULT_CODE_IMAGE_CHOSEN) && (result.getData() != null)){
                Intent data = result.getData();
                Log.d(NuovoItinerarioFragment_TAG, data.getData().toString());
                caricaImmagine(data.getData(), nome + "_url");
            }
        });

        // Inserimento di un itinerario senza un'immagine
        NoButton.setOnClickListener(click -> insertData());
    }

    // Gestione dell'utente locale
    private LocalUser getDatiutente(){
        localUserDbManager = new LocalUserDbManager(this.getContext());
        localUserDbManager.openR();
        LocalUser localUser = localUserDbManager.fetchDataUser();
        localUserDbManager.closeDB();
        return localUser;
    }

    // Controllo della validità sui campi
    private boolean controlloCampi(View view) {
        String campoObbligatorio = getResources().getString(R.string.CampoObbligatorio);
        descrizione = ((EditText) view.requireViewById(R.id.DescrizioneItinerarioEditText)).getText().toString();

        // Recupero del nome + controlli
        nome = ((EditText) view.requireViewById(R.id.NomeItinerarioEditText)).getText().toString();
        boolean checkNome = !nome.isEmpty();
        NomeTextInput.setErrorEnabled(nome.isEmpty());
        NomeTextInput.setError(nome.isEmpty() ? campoObbligatorio : null);

        // Recupero della citta + controlli
        citta = ((EditText) view.requireViewById(R.id.CittaEditText)).getText().toString();
        boolean checkCitta = !citta.isEmpty();
        CittaTextInput.setErrorEnabled(citta.isEmpty());
        CittaTextInput.setError(citta.isEmpty() ? campoObbligatorio : null);

        // Recupero della lunghezza + controlli
        lunghezza = ((EditText) view.requireViewById(R.id.LunghezzaEditText)).getText().toString();
        boolean checkLunghezza = !lunghezza.isEmpty();
        LunghezzaTextInput.setErrorEnabled(lunghezza.isEmpty());
        LunghezzaTextInput.setError(lunghezza.isEmpty() ? campoObbligatorio : null);

        // Recupero del dislivello + controlli
        dislivello = ((EditText) view.requireViewById(R.id.DislivelloEditText)).getText().toString();
        boolean checkDislivello = !dislivello.isEmpty();
        DislivelloTextInput.setErrorEnabled(dislivello.isEmpty());
        DislivelloTextInput.setError(dislivello.isEmpty() ? campoObbligatorio : null);

        // Recupero della durata in ore, minuti e secondi
        String oreText = ((EditText) view.requireViewById(R.id.OreEditText_nuovo_it)).getText().toString();
        int ore = (oreText.isEmpty() ? 0 : Integer.parseInt(oreText));

        String minutiText = ((EditText) view.requireViewById(R.id.MinutiEditText_nuovo_it)).getText().toString();
        int minuti = (minutiText.isEmpty() ? 0 : Integer.parseInt(minutiText));

        String secondiText = ((EditText) view.requireViewById(R.id.SecondiEditText_nuovo_it)).getText().toString();
        int secondi = (secondiText.isEmpty() ? 0 : Integer.parseInt(secondiText));

        // Controlli sulla durata
        boolean checkDurata = true;
        DurataErrorTextView.setVisibility(View.GONE);

        if (ore == 0 && minuti == 0 && secondi == 0) {
            checkDurata = false;
            DurataErrorTextView.setVisibility(View.VISIBLE);
            DurataErrorTextView.setText("Scegli un valore per i secondi maggiori di 0");
        }

        if (ore > 99) {
            checkDurata = false;
            DurataErrorTextView.setVisibility(View.VISIBLE);
            DurataErrorTextView.setText("Scegli un valore per le ore minore di 100");
        }

        if (minuti > 59) {
            checkDurata = false;
            DurataErrorTextView.setVisibility(View.VISIBLE);
            DurataErrorTextView.setText("Scegli un valore per i minuti minore di 60");
        }

        if (secondi > 59) {
            checkDurata = false;
            DurataErrorTextView.setVisibility(View.VISIBLE);
            DurataErrorTextView.setText("Scegli un valore per i secondi minore di 60");
        }

        // Formattazione della stringa "durata" con il pattern HH:MM:SS
        durata = ((ore >= 0 && ore < 10) ? "0" + ore : String.valueOf(ore));
        durata = durata + ":";
        durata = ((minuti >= 0 && minuti < 10) ? durata + "0" + minuti : durata + minuti);
        durata = durata + ":";
        durata = ((secondi >= 0 && secondi < 10) ? durata + "0" + secondi : durata + secondi);

        Log.d(NuovoItinerarioFragment_TAG, durata);

        return checkNome & checkCitta & checkLunghezza & checkDislivello & checkDurata;
    }

    // Caricamento
    private void insertData() {
        nuovoItinerario = new Itinerario();
        nuovoItinerario.setUtenteProprietario(localUser.getUsername());
        nuovoItinerario.setNomeItinerario(nome);
        nuovoItinerario.setDescrizione(descrizione);

        nuovoItinerario.setCitta(citta);
        nuovoItinerario.setLunghezza(Double.valueOf(lunghezza));
        nuovoItinerario.setDislivello(Integer.valueOf(dislivello));
        nuovoItinerario.setDurata(durata);
        nuovoItinerario.setZonaGeografica(ZonaAutoCompleteTextView.getText().toString());
        nuovoItinerario.setDifficolta(DifficoltaAutoCompleteTextView.getText().toString());
        nuovoItinerario.setAccessoDisabili(DisabilitaCheckBox.isChecked() ? "Si" : "No");

        nuovoItinerario.setPuntoDiInizioLatitudine(puntoDiInizioLatitudine);
        nuovoItinerario.setPuntoDiInizioLongitudine(puntoDiInizioLongitudine);
        nuovoItinerario.setPuntoDiFineLatitudine(puntoDiFineLatitudine);
        nuovoItinerario.setPuntoDiFineLongitudine(puntoDiFineLongitudine);
        nuovoItinerario.setUrlFotoItinerario("");

        // Di default settiamo lo stato della segnalazione della foto a No (non è stata segnalata)
        nuovoItinerario.setStatoFotoSegnalazione("No");


        requestItinerario = new ItinerarioRequest();

        requestItinerario.aggiungiItinerario(nuovoItinerario, new CallBackItinerario() {
            @Override
            public void onSuccess(Itinerario itinerario) {
                // Nulla da fare qui
            }
            @Override
            public void onSuccessList(List<Itinerario> itinerari) {
                // Nulla da fare qui

            }
            @Override
            public void onSuccessResponse(Response<Void> response) {
                if (response.isSuccessful()){
                    Log.d(NuovoItinerarioFragment_TAG,"onSuccessResponse: Inserimento itinerario Completato");
                    caricaTag();
                }else{
                    Toasty.info(requireContext(), "Attenzione! Non è stato possibile creare il nuovo itinerario\nRiprova più tardi",
                            Toasty.LENGTH_SHORT, true).show();
                }
            }
            @Override
            public void onFailure(Throwable throwable) {
                Toasty.error(context,
                        "Non è stato possibile caricare l'itinerario sulla piattaforma",
                        Toasty.LENGTH_SHORT,
                        true).show();
            }
        });
    }

    private void insertData(String urlFotoItinerario){
        nuovoItinerario = new Itinerario();

        nuovoItinerario.setNomeItinerario(nome);
        nuovoItinerario.setDescrizione(descrizione);
        nuovoItinerario.setAccessoDisabili(DisabilitaCheckBox.isChecked() ? "Si" : "No");
        nuovoItinerario.setZonaGeografica(ZonaAutoCompleteTextView.getText().toString());
        nuovoItinerario.setDifficolta(DifficoltaAutoCompleteTextView.getText().toString());

        nuovoItinerario.setPuntoDiInizioLatitudine(puntoDiInizioLatitudine);
        nuovoItinerario.setPuntoDiInizioLongitudine(puntoDiInizioLongitudine);
        nuovoItinerario.setPuntoDiFineLatitudine(puntoDiFineLatitudine);
        nuovoItinerario.setPuntoDiFineLongitudine(puntoDiFineLongitudine);

        nuovoItinerario.setDurata(durata);
        nuovoItinerario.setCitta(citta);
        nuovoItinerario.setDislivello(Integer.valueOf(dislivello));
        nuovoItinerario.setLunghezza(Double.valueOf(lunghezza));

        nuovoItinerario.setUtenteProprietario(localUser.getUsername());
        nuovoItinerario.setUrlFotoItinerario(urlFotoItinerario);

        // Di default settiamo lo stato della segnalazione della foto a No (non è stata segnalata)
        nuovoItinerario.setStatoFotoSegnalazione("No");

        requestItinerario = new ItinerarioRequest();
        requestItinerario.aggiungiItinerario(nuovoItinerario, new CallBackItinerario() {
            @Override
            public void onSuccess(Itinerario itinerario) {
                // Nulla da fare qui
            }
            @Override
            public void onSuccessList(List<Itinerario> itinerari) {
                // Nulla da fare qui

            }
            @Override
            public void onSuccessResponse(Response<Void> response) {
               if (response.isSuccessful()){
                   Log.d(NuovoItinerarioFragment_TAG,"onSuccessResponse: Inserimento itinerario Completato");
                   caricaTag();
               }else{
                   Toasty.info(requireContext(), "Attenzione! Non è stato possibile creare il nuovo itinerario\nRiprova più tardi",
                           Toasty.LENGTH_SHORT, true).show();
               }

            }
            @Override
            public void onFailure(Throwable throwable) {
                Toasty.error(context,
                        "Non è stato possibile caricare l'itinerario sulla piattaforma",
                        Toasty.LENGTH_SHORT,
                        true).show();
            }
        });
    }
    // Caricamento dell'immagine dell'itinerario scelto dall'utente su s3
    private void caricaImmagine(Uri uri, String key){
        AmplifyS3Request amplifyS3Request = new AmplifyS3Request();
        amplifyS3Request.uploadImage(uri, key, context, new CallBackGeneric() {
            @Override
            public void onSuccess(String s) {
                Log.d(NuovoItinerarioFragment_TAG, "onSuccess: Caricamento dell'immagine su s3 completato " + s);
                insertData(key);
            }
            @Override
            public void onFailure(Throwable throwable) {
                Log.d(NuovoItinerarioFragment_TAG, "onFailure: Caricamento dell'immagine su s3 fallita: " + throwable.getLocalizedMessage());
                Toasty.error(context,
                        "Non è stato possibile caricare l'immagine selezionata",
                        Toasty.LENGTH_SHORT,
                        true).show();
            }
        });
    }

    private void caricaTag(){
        requestTagRicerca = new TagRicercaRequest();

        for (String s : TagRicercaTextView.getObjects()) {
            requestTagRicerca.aggiungiTag(new TagRicerca(-1L, s, nuovoItinerario.getNomeItinerario()), new CallBackTagRicerca() {
                @Override
                public void onSuccessList(List<TagRicerca> tags) {
                    // Nulla da fare qui
                }

                @Override
                public void onFailure(Throwable throwable) {
                    Log.d(NuovoItinerarioFragment_TAG, throwable.getLocalizedMessage());
                    Toasty.error(context,
                            "Impossibile caricare il tag selezionato",
                            Toasty.LENGTH_SHORT,
                            true).show();

                }

                @Override
                public void onSuccessResponse(Response<Void> response) {
                    if (response.isSuccessful()){
                        Log.d(NuovoItinerarioFragment_TAG, "Tag caricato correttamente sul db");
                    }else{
                        Toasty.info(requireContext(), "Attenzione! Non è stato possibile caricare il tag inerente all'itinerario\nRiprova più tardi",
                                Toasty.LENGTH_SHORT, true).show();
                    }
                }
            });
        }
        passaggioAHomePage();
    }

    // Passaggio alla home page in caso di successo
    private void passaggioAHomePage(){
        HomeFragment homeFragment = new HomeFragment();
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.FragmentContainer, homeFragment);
        fragmentTransaction.commit();
    }
}