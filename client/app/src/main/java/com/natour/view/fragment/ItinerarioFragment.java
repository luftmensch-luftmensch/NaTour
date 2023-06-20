/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.view.fragment;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;
import androidx.transition.TransitionValues;
import androidx.transition.Visibility;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.natour.R;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.natour.model.ChatRoom;
import com.natour.model.GalleriaFotoItinerario;
import com.natour.model.Itinerario;
import com.natour.model.SegnalazioneFotoItinerario;
import com.natour.model.TagRicerca;
import com.natour.presenter.amplify.AmplifyS3Request;
import com.natour.presenter.callbackInterface.CallBackChatRoom;
import com.natour.presenter.callbackInterface.CallBackGalleriaFotoItinerario;
import com.natour.presenter.callbackInterface.CallBackGeneric;
import com.natour.presenter.callbackInterface.CallBackItinerario;
import com.natour.presenter.callbackInterface.CallBackSegnalazioneFotoItinerario;
import com.natour.presenter.callbackInterface.CallBackTagRicerca;
import com.natour.presenter.request.ChatRoomRequest;
import com.natour.presenter.request.GalleriaFotoItinerarioRequest;
import com.natour.presenter.request.ItinerarioRequest;
import com.natour.presenter.request.SegnalazioneFotoItinerarioRequest;
import com.natour.presenter.request.TagRicercaRequest;
import com.natour.utils.constants.Constants;
import com.natour.utils.handler.ApiHandler;
import com.natour.utils.persistence.LocalUser;
import com.natour.utils.persistence.LocalUserDbManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import es.dmoral.toasty.Toasty;
import retrofit2.Response;

public class ItinerarioFragment extends Fragment {
    private static final String ItinerarioFragment_TAG = "ItinerarioFragment";
    private Context context;

    private ApiHandler apiHandler;

    private AppBarLayout TopAppBar;
    private MaterialToolbar TopToolBar;

    private FloatingActionButton NuovoMessaggioButton;

    private CardView        CardTitolo;
    private ImageView       FotoItinerarioImageView;
    private TextView        NomeItinerarioTextView;

    private CardView        CardVisualizzaFotoItinerario;
    private ImageView       FotoItinerarioZoomataImageView;
    private ImageButton     SegnalaFotoItinerarioButton;

    private CardView        CardAutore;
    private TextView        TitoloCardAutore;
    private LinearLayout    AutoreLinearLayout;
    private TextView        UsernameAutoreTextView; // EmailAutoreTextView;

    private CardView        CardMappa;
    private ImageView       DifficoltaImageView;
    private TextView        DifficoltaTextView, DisabilitaTextView;

    private CardView        CardDescrizione;
    private LinearLayout    DescrizioneLinearLayout;
    private TextView        TitoloCardDescrizione;
    private TextView        DescrizioneTextView;

    private CardView        CardCaratteristiche;
    private LinearLayout    CaratteristicheLinearLayout;
    private TextView        TitoloCardCaratteristiche;
    private TextView        CittaTextView, ZonaTextView, DislivelloTextView, DurataTextView, LunghezzaTextView;

    private CardView        CardGalleria;
    private ImageSlider     GalleriaImageSlider;

    private CardView        CardPuntoInizio;
    private LinearLayout    PuntoInizioLinearLayout;
    private TextView        TitoloCardPuntoInizio;
    private TextView        PuntoInizioLatitudineTextView, PuntoInizioLongitudineTextView;

    private CardView        CardPuntoFine;
    private LinearLayout    PuntoFineLinearLayout;
    private TextView        TitoloCardPuntoFine;
    private TextView        PuntoFineLatitudineTextView, PuntoFineLongitudineTextView;

    private CardView        CardTag;
    private FlexboxLayout   TagFlexBoxLayout;

    private CardView        SegnalaFotoItinerarioPopup;
    private MaterialButton  NoButton;
    private MaterialButton  SiButton;

    private Transition CardVisualizzaFotoTransition, SegnalaFotoItinerarioTransition;

    private String nomeItinerario, urlFotoItinerario;


    private Double puntoDiInizioLongitudine, puntoDiInizioLatitudine;
    private Double puntoDiFineLongitudine, puntoDiFineLatitudine;

    // Gestione della galleria di foto associate ad un itinerario
    private ArrayList<String> keyFotoGalleria;
    private ArrayList<SlideModel> imageList;

    private ActivityResultLauncher<Intent> activityResultLauncherFotoGalleria;


    // Db Manager
    private LocalUserDbManager localUserDbManager;
    private LocalUser localUser;

    public ItinerarioFragment(String nomeItinerario){
        keyFotoGalleria = new ArrayList<>();
        imageList = new ArrayList<>();
        this.nomeItinerario = nomeItinerario;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_itinerario, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        localUser = getDatiutente();
        setUI(view);
        if (isAdded()) {
            retrieveDataItinerario();
            retrieveDataTagItinerario();
        }
    }

    private void setUI(View view) {
        // TopAppBar Customization
        TopAppBar = requireActivity().findViewById(R.id.AppBarLayout);
        TopToolBar = (MaterialToolbar) TopAppBar.getChildAt(0);

        TopToolBar.setTitle(R.string.ItinerarioFragment);
        TopToolBar.getMenu().clear();
        TopToolBar.setNavigationIcon(R.drawable.arrow_back_24dp);

        requireActivity().findViewById(R.id.NuovoItinerarioPopup).setVisibility(View.GONE);
        requireActivity().findViewById(R.id.NuovoItinerarioPopupOutside).setVisibility(View.GONE);
        requireActivity().findViewById(R.id.RicercaTextInput_appBar).setVisibility(View.GONE);

        // NuovoMessaggioButton (Invio di un messaggio all'utente che ha creato il sinbgolo itinerario) Customization
        NuovoMessaggioButton = requireActivity().findViewById(R.id.FloatingActionButton);
        NuovoMessaggioButton.setVisibility(View.VISIBLE);
        NuovoMessaggioButton.setImageResource(R.drawable.new_message_36dp);
        DrawableCompat.setTint(
                DrawableCompat.wrap(NuovoMessaggioButton.getDrawable()),
                ContextCompat.getColor(context, R.color.tema_scuro_testi)
        );

        // Immagine dell'itinario + nome
        CardTitolo = view.findViewById(R.id.TitoloLayout_itinerario);
        FotoItinerarioImageView = CardTitolo.findViewById(R.id.TitoloImageView_card);
        NomeItinerarioTextView = CardTitolo.findViewById(R.id.TitoloTextView_card);

        // Zoom della foto + segnalazione
        CardVisualizzaFotoItinerario = view.findViewById(R.id.VisualizzaFotoItinerarioCardView);
        FotoItinerarioZoomataImageView = CardVisualizzaFotoItinerario.findViewById(R.id.FotoItinerarioImageView_visualizzaCard);
        SegnalaFotoItinerarioButton = CardVisualizzaFotoItinerario.findViewById(R.id.SegnalaFotoItinerarioButton_visualizzaCard);

        // Informazioni sull'autore
        CardAutore = view.findViewById(R.id.AutoreLayout_itinerario);
        AutoreLinearLayout = CardAutore.findViewById(R.id.LinearLayout_infoCard);
        TitoloCardAutore = CardAutore.findViewById(R.id.TitoloTextView_infoCard);
        TitoloCardAutore.setText(R.string.TitoloCardAutore);
        UsernameAutoreTextView = createTextView(AutoreLinearLayout, R.string.UsernameAutore);
        //EmailAutoreTextView = createTextView(AutoreLinearLayout, R.string.EmailAutore);

        // Mappa e relativa customizzazione
        CardMappa = view.findViewById(R.id.MappaLayout_itinerario);
        DifficoltaTextView = CardMappa.findViewById(R.id.DifficoltaTextView_card);
        DifficoltaImageView = CardMappa.findViewById(R.id.DifficoltaImageView_card);
        DisabilitaTextView = CardMappa.findViewById(R.id.DisabilitaTextView_card);

        // Descrizione dell'itinerario
        CardDescrizione = view.findViewById(R.id.DescrizioneLayout_itinerario);
        DescrizioneLinearLayout = CardDescrizione.findViewById(R.id.LinearLayout_infoCard);
        TitoloCardDescrizione = CardDescrizione.findViewById(R.id.TitoloTextView_infoCard);
        TitoloCardDescrizione.setText(R.string.TitoloCardDescrizione);
        DescrizioneTextView = createTextView(DescrizioneLinearLayout, R.string.Descrizione);

        // Caratteristiche dell'itinerario (Città, zona geografica, dislivello, durata, lunghezza)
        CardCaratteristiche = view.findViewById(R.id.CaratteristicheLayout_itinerario);
        CaratteristicheLinearLayout = CardCaratteristiche.findViewById(R.id.LinearLayout_infoCard);
        TitoloCardCaratteristiche = CardCaratteristiche.findViewById(R.id.TitoloTextView_infoCard);
        TitoloCardCaratteristiche.setText(R.string.TitoloCardCaratteristiche);
        CittaTextView = createTextView(CaratteristicheLinearLayout, R.string.CittaItinerario);
        ZonaTextView = createTextView(CaratteristicheLinearLayout, R.string.ZonaGeografica);
        DislivelloTextView = createTextView(CaratteristicheLinearLayout, R.string.Dislivello);
        DurataTextView = createTextView(CaratteristicheLinearLayout, R.string.DurataItinerario);
        LunghezzaTextView = createTextView(CaratteristicheLinearLayout, R.string.Lunghezza);

        // Galleria Customization
        CardGalleria = view.findViewById(R.id.GalleriaLayout_itinerario);
        GalleriaImageSlider = CardGalleria.findViewById(R.id.GalleriaImageSlider);
        setGalleriaImageSlider();

        // Coordinate dell'itinerario
        CardPuntoInizio = view.findViewById(R.id.PuntoInizioLayout_itinerario);
        PuntoInizioLinearLayout = CardPuntoInizio.findViewById(R.id.LinearLayout_infoCard);
        TitoloCardPuntoInizio = CardPuntoInizio.findViewById(R.id.TitoloTextView_infoCard);
        TitoloCardPuntoInizio.setText(R.string.TitoloCardPuntoInizio);
        PuntoInizioLatitudineTextView = createTextView(PuntoInizioLinearLayout, R.string.Latitudine);
        PuntoInizioLongitudineTextView = createTextView(PuntoInizioLinearLayout, R.string.Longitudine);

        CardPuntoFine = view.findViewById(R.id.PuntoFineLayout_itinerario);
        PuntoFineLinearLayout = CardPuntoFine.findViewById(R.id.LinearLayout_infoCard);
        TitoloCardPuntoFine = CardPuntoFine.findViewById(R.id.TitoloTextView_infoCard);
        TitoloCardPuntoFine.setText(R.string.TitoloCardPuntoFine);
        PuntoFineLatitudineTextView = createTextView(PuntoFineLinearLayout, R.string.Latitudine);
        PuntoFineLongitudineTextView = createTextView(PuntoFineLinearLayout, R.string.Longitudine);

        // Lista dei tag relativi all'itinerario
        CardTag = view.findViewById(R.id.TagRicercaLayout_itinerario);
        TagFlexBoxLayout = CardTag.findViewById(R.id.FlexBoxLayout_tag);

        // Popup per la segnalazione delle foto
        SegnalaFotoItinerarioPopup = view.findViewById(R.id.CardView_popupSegnalazione);
        NoButton = SegnalaFotoItinerarioPopup.findViewById(R.id.NoButton_popupSegnalazione);
        SiButton = SegnalaFotoItinerarioPopup.findViewById(R.id.SiButton_popupSegnalazione);

        CardVisualizzaFotoTransition = new Visibility() {
            @Nullable
            @Override
            public Animator onAppear(ViewGroup sceneRoot, View view, TransitionValues startValues, TransitionValues endValues) {
                // Coordinate del punto centrale della view
                int cx = CardVisualizzaFotoItinerario.getWidth() / 2;
                int cy = CardVisualizzaFotoItinerario.getHeight() / 2;

                // Specifica il tipo di animazione
                return ViewAnimationUtils.createCircularReveal(CardVisualizzaFotoItinerario, cx, cy, 0f, (float) Math.hypot(cx, cy));
            }

            @Nullable
            @Override
            public Animator onDisappear(ViewGroup sceneRoot, View view, TransitionValues startValues, TransitionValues endValues) {
                // Coordinate del punto centrale della view
                int cx = CardVisualizzaFotoItinerario.getWidth() / 2;
                int cy = CardVisualizzaFotoItinerario.getHeight() / 2;

                // Specifica il tipo di animazione
                return ViewAnimationUtils.createCircularReveal(CardVisualizzaFotoItinerario, cx, cy, (float) Math.hypot(cx, cy), 0f);
            }
        }
                .setDuration(300)
                .addTarget(CardVisualizzaFotoItinerario);

        SegnalaFotoItinerarioTransition = new Visibility() {
            @Nullable
            @Override
            public Animator onAppear(ViewGroup sceneRoot, View view, TransitionValues startValues, TransitionValues endValues) {
                // Coordinate del punto centrale della view
                int cx = SegnalaFotoItinerarioPopup.getWidth() / 2;
                int cy = SegnalaFotoItinerarioPopup.getHeight() / 2;

                // Specifica il tipo di animazione
                return ViewAnimationUtils.createCircularReveal(SegnalaFotoItinerarioPopup, cx, cy, 0f, (float) Math.hypot(cx, cy));
            }

            @Nullable
            @Override
            public Animator onDisappear(ViewGroup sceneRoot, View view, TransitionValues startValues, TransitionValues endValues) {
                // Coordinate del punto centrale della view
                int cx = SegnalaFotoItinerarioPopup.getWidth() / 2;
                int cy = SegnalaFotoItinerarioPopup.getHeight() / 2;

                // Specifica il tipo di animazione
                return ViewAnimationUtils.createCircularReveal(SegnalaFotoItinerarioPopup, cx, cy, (float) Math.hypot(cx, cy), 0f);
            }
        }
                .setDuration(300)
                .addTarget(SegnalaFotoItinerarioPopup);

        TopToolBar.setNavigationOnClickListener(click -> requireActivity().getSupportFragmentManager()
                .popBackStackImmediate());

        NuovoMessaggioButton.setOnClickListener(click -> {
            if (!UsernameAutoreTextView.getText().toString().isEmpty()) {
                // Controlliamo che i due utenti non siano la stessa persona (Non ha senso creare una chat room con se stessi)
                if(UsernameAutoreTextView.getText().toString().equals(localUser.getUsername())){
                    Toasty.info(requireContext(), "Ti senti così solo da voler parlare da solo?",
                            Toasty.LENGTH_SHORT, true).show();

                }else{
                    // In caso contrario creiamo la chat room e in caso di di successo spostiamo la vista dell'utente sul tab chatroom
                    ChatRoomRequest chatRoomRequest = new ChatRoomRequest();
                    ChatRoom chatRoom = new ChatRoom();
                    chatRoom.setUtenteA(localUser.getUsername());
                    chatRoom.setUtenteB(UsernameAutoreTextView.getText().toString());
                    chatRoom.setIdChatRoom(localUser.getUsername()+UsernameAutoreTextView.getText().toString());
                    chatRoomRequest.aggiungiChatRoom(chatRoom, new CallBackChatRoom() {
                        @Override
                        public void onSuccessList(List<ChatRoom> chatRooms) {
                            // Nulla da fare qui
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            Log.d(ItinerarioFragment_TAG, "onFailure: Creazione di una nuova chat room fallita " + throwable.getLocalizedMessage());
                        }

                        @Override
                        public void onSuccessResponse(Response<Void> response) {
                            // Se la response e 201 (successFul) significa che la chat room che si vuole creare non esisteva già
                            if(response.isSuccessful()){
                                // Nel caso in cui la response sia succesful cambiamo la vista sulla singola chat
                                passaggioASingolaChat(chatRoom.getIdChatRoom(), UsernameAutoreTextView.getText().toString());
                            }else{
                                // In caso contrario avvisiamo l'utente che la chat room esiste già
                                Toasty.info(requireContext(), "Attenzione! La chat room che si vuole creare esiste già\nControlla tra i tuoi messaggi privati",
                                        Toasty.LENGTH_SHORT, true).show();
                            }
                        }
                    });
                }
            }
        });

        // Selezione / Modifica immagine di profilo
        FotoItinerarioImageView.setOnClickListener(click -> {
            TransitionManager.beginDelayedTransition(
                    (ViewGroup) CardVisualizzaFotoItinerario.getParent(), CardVisualizzaFotoTransition);
            CardVisualizzaFotoItinerario.setVisibility(View.VISIBLE);
            SegnalaFotoItinerarioButton.setVisibility(View.VISIBLE);
        });

        FotoItinerarioZoomataImageView.setOnClickListener(click -> {
            TransitionManager.beginDelayedTransition(
                    (ViewGroup) CardVisualizzaFotoItinerario.getParent(), CardVisualizzaFotoTransition);
            CardVisualizzaFotoItinerario.setVisibility(View.GONE);
            SegnalaFotoItinerarioButton.setVisibility(View.GONE);
        });

        SegnalaFotoItinerarioButton.setOnClickListener(click -> {
            TransitionManager.beginDelayedTransition(
                    (ViewGroup) SegnalaFotoItinerarioPopup.getParent(), SegnalaFotoItinerarioTransition);
            SegnalaFotoItinerarioPopup.setVisibility(View.VISIBLE);
            //segnalaFotoItinerario(urlFotoItinerario);
            Log.d(ItinerarioFragment_TAG, "SEGNALAZIONE");
        });

        // Informazioni sull'autore del itinerario
        CardAutore.setOnClickListener(click -> {
            if (!UsernameAutoreTextView.getText().toString().isEmpty()) {
                VisualizzaInfoProfiloFragment visualizzaInfoProfiloFragment
                        = new VisualizzaInfoProfiloFragment(UsernameAutoreTextView.getText().toString());
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.FragmentContainer, visualizzaInfoProfiloFragment, "VisualizzaInfoProfiloFragment")
                        .addToBackStack(ItinerarioFragment_TAG)
                        .commit();
            }
        });

        // Card view per visualizzare la mappa
        CardMappa.setOnClickListener(click -> {
            VisualizzaInfoMappaFragment visualizzaInfoMappaFragment = new VisualizzaInfoMappaFragment(puntoDiInizioLongitudine, puntoDiInizioLatitudine, puntoDiFineLongitudine, puntoDiFineLatitudine);
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.FragmentContainer, visualizzaInfoMappaFragment, "VisualizzaInfoMappaFragment")
                    .addToBackStack(ItinerarioFragment_TAG)
                    .commit();
        });

        NoButton.setOnClickListener(click -> {
            TransitionManager.beginDelayedTransition(
                    (ViewGroup) SegnalaFotoItinerarioPopup.getParent(), SegnalaFotoItinerarioTransition);
            SegnalaFotoItinerarioPopup.setVisibility(View.GONE);
            GalleriaImageSlider.startSliding(5000);
        });

        activityResultLauncherFotoGalleria = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Constants.RESULT_CODE_IMAGE_CHOSEN && result.getData() != null){
                // Castiamo l'activity result a un intent in modo da ottere l'uri
                Intent data = result.getData();
                Log.d(ItinerarioFragment_TAG, data.getData().toString());
                String key = UUID.randomUUID().toString();
                caricaFotoAggiuntivaS3(data.getData(), key + "_url");
            }
        });
    }

    private TextView createTextView(LinearLayout layout, int testo) {
        LinearLayout ll = new LinearLayout(context);
        LinearLayout.LayoutParams llParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        llParams.setMargins(0, 5, 0, 0);
        ll.setLayoutParams(llParams);
        ll.setOrientation(LinearLayout.HORIZONTAL);

        TextView infoView = new TextView(context);
        LinearLayout.LayoutParams infoParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        infoParams.setMarginEnd(15);
        infoView.setGravity(Gravity.CENTER);
        infoView.setLayoutParams(infoParams);
        infoView.setText(testo);

        TextView textView = new TextView(context);
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        textView.setLayoutParams(textParams);

        layout.addView(ll);
        ll.addView(infoView);
        ll.addView(textView);

        return textView;
    }

    // Gestione dell'utente locale
    private LocalUser getDatiutente(){
        localUserDbManager = new LocalUserDbManager(this.getContext());
        localUserDbManager.openR();
        LocalUser localUser = localUserDbManager.fetchDataUser();
        localUserDbManager.closeDB();
        return localUser;
    }

    // Selezione della lista della galleria di foto appartenenti al singolo itinerario
    private void setGalleriaImageSlider() {
        GalleriaFotoItinerarioRequest galleriaRequest = new GalleriaFotoItinerarioRequest();
        galleriaRequest.getGalleriaFotoStessoItinerario(nomeItinerario, new CallBackGalleriaFotoItinerario() {
            @Override
            public void onSuccessList(List<GalleriaFotoItinerario> galleriaFotoItinerarioList) {

                for (GalleriaFotoItinerario fotoItinerario : galleriaFotoItinerarioList) {
                    Log.d(ItinerarioFragment_TAG, fotoItinerario.getUrlFotoItinerario());
                    Log.d(ItinerarioFragment_TAG, String.valueOf(fotoItinerario.getIdFotoItinerario()));
                    getUrlFotoAggiuntiva(fotoItinerario.getUrlFotoItinerario(), fotoItinerario.getIdFotoItinerario());
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.d(ItinerarioFragment_TAG, "onFailure");
                apiHandler = new ApiHandler(throwable, context);
            }

            @Override
            public void onSuccessResponse(Response<Void> response) {
                // Nulla da fare qui
            }
        });
    }

    // Conversione della lista delle key (url della singola foto aggiuntiva) a url vero e proprio (ottenuto da S3)
    private void getUrlFotoAggiuntiva(String url, String key){
        AmplifyS3Request amplifyS3Request = new AmplifyS3Request();
        amplifyS3Request.getUrlImmagine(url, new CallBackGeneric() {
            @Override
            public void onSuccess(String s) {
                imageList.add(new SlideModel(s, ScaleTypes.CENTER_CROP));
                keyFotoGalleria.add(key);
                GalleriaImageSlider.setImageList(imageList);

                // Selettore della foto, nel caso in cui si voglia segnalare l'immagine
                GalleriaImageSlider.setItemClickListener(position -> {
                    GalleriaImageSlider.stopSliding();
                    TransitionManager.beginDelayedTransition(
                            (ViewGroup) SegnalaFotoItinerarioPopup.getParent(), SegnalaFotoItinerarioTransition);
                    SegnalaFotoItinerarioPopup.setVisibility(View.VISIBLE);
                    segnalaFotoItinerario(keyFotoGalleria.get(position));
                });
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.d(ItinerarioFragment_TAG, "onFailure: " + throwable.getLocalizedMessage());
            }
        });

    }


    // Segnalazione di una foto appartente all'itinerario
    private void segnalaFotoItinerario (String key) {
        Log.d(ItinerarioFragment_TAG, "Immagine segnalata" + key);
        SiButton.setOnClickListener(click -> {
            TransitionManager.beginDelayedTransition(
                    (ViewGroup) SegnalaFotoItinerarioPopup.getParent(), SegnalaFotoItinerarioTransition);
            SegnalaFotoItinerarioPopup.setVisibility(View.GONE);
            GalleriaImageSlider.startSliding(5000);
            creaSegnalazione(key);
        });
    }

    // Creazione e invio della segnalazione effettuata
    private void creaSegnalazione(String fotoDaSegnalare){
        Log.d(ItinerarioFragment_TAG, "Creazione segnalazione");
        SegnalazioneFotoItinerarioRequest segnalazioneRequest = new SegnalazioneFotoItinerarioRequest();
        SegnalazioneFotoItinerario segnalazione = new SegnalazioneFotoItinerario();
        segnalazione.setIdSegnalazione(-1L);
        segnalazione.setUtenteSegnalatore(localUser.getUsername());
        segnalazione.setGalleriaFotoItinerarioCorrispondente(fotoDaSegnalare);

        segnalazioneRequest.aggiungiSegnalazione(segnalazione, new CallBackSegnalazioneFotoItinerario() {
            @Override
            public void onSuccessList(List<SegnalazioneFotoItinerario> segnalazioneFotoItinerarios) {
                // Nulla da fare qui
            }

            @Override
            public void onFailure(Throwable throwable) { apiHandler = new ApiHandler(throwable, getActivity()); }

            @Override
            public void onSuccessResponse(Response<Void> response) {
                Log.d(ItinerarioFragment_TAG, "Segnalazione effettuata correttamente");
                Toasty.success(requireContext(), "Segnalazione effettuata correttamente",
                        Toasty.LENGTH_SHORT, true).show();

            }
        });
    }

    // Retrieval dei dati di un itinerario
    private void retrieveDataItinerario() {
        ItinerarioRequest request = new ItinerarioRequest();
        request.getSingoloItinerario(nomeItinerario, new CallBackItinerario() {
            @Override
            public void onSuccess(Itinerario itinerario) {
                // Retrieval url foto itinerario (prima di eseguire una chiamata a S3 controlliamo che la foto non sia stata segnalata)
                if(itinerario.getStatoFotoSegnalazione().equals("No")){
                    retrievalFotoProfiloItinerario(itinerario.getUrlFotoItinerario());
                }

                // Riga di assegnazione dell'urlFotoItinerario (Scritta solo per il testing del metodo della segnalazione)

                NomeItinerarioTextView.setText(itinerario.getNomeItinerario());

                setDifficolta(itinerario.getDifficolta());
                setDisabilita(itinerario.getAccessoDisabili());

                DescrizioneTextView.setText(itinerario.getDescrizione());

                CittaTextView.setText(itinerario.getCitta());
                DislivelloTextView.setText(String.valueOf(itinerario.getDislivello()));
                LunghezzaTextView.setText(String.valueOf(itinerario.getLunghezza()));
                ZonaTextView.setText(itinerario.getZonaGeografica());

                DurataTextView.setText(itinerario.getDurata());

                UsernameAutoreTextView.setText(itinerario.getUtenteProprietario());

                PuntoInizioLongitudineTextView.setText(String.valueOf(itinerario.getPuntoDiInizioLongitudine()));
                PuntoInizioLatitudineTextView.setText(String.valueOf(itinerario.getPuntoDiInizioLatitudine()));

                PuntoFineLongitudineTextView.setText(String.valueOf(itinerario.getPuntoDiFineLongitudine()));
                PuntoFineLatitudineTextView.setText(String.valueOf(itinerario.getPuntoDiFineLatitudine()));

                puntoDiInizioLongitudine = itinerario.getPuntoDiInizioLongitudine();
                puntoDiInizioLatitudine = itinerario.getPuntoDiInizioLatitudine();

                puntoDiFineLongitudine = itinerario.getPuntoDiFineLongitudine();
                puntoDiFineLatitudine = itinerario.getPuntoDiFineLatitudine();

                if (localUser.getUsername().equals(itinerario.getUtenteProprietario())) {
                    TopToolBar.inflateMenu(R.menu.itinerario_topappbar_menu);
                    MenuItem nuovaFotoGalleriaButton = TopToolBar.getMenu().findItem(R.id.NuovaFotoGalleriaButton);
                    nuovaFotoGalleriaButton.setOnMenuItemClickListener(click -> {
                        Intent selezionaFotoDaGalleria = new Intent();
                        selezionaFotoDaGalleria.setType("image/*");
                        selezionaFotoDaGalleria.setAction(Intent.ACTION_GET_CONTENT);
                        activityResultLauncherFotoGalleria.launch(Intent.createChooser(selezionaFotoDaGalleria, "Seleziona immagini"));
                        return false;
                    });
                } else {
                    TopToolBar.getMenu().clear();
                }
            }

            @Override
            public void onSuccessList(List<Itinerario> itinerari) {
                Log.d(ItinerarioFragment_TAG, "onSuccessList: Nulla da fare qui");
            }

            @Override
            public void onFailure(Throwable throwable) {
                apiHandler = new ApiHandler(throwable, getActivity());
            }

            @Override
            public void onSuccessResponse(Response<Void> response) {
                // Nulla da fare qui
            }
        });
    }

    private void caricaFotoAggiuntivaS3(Uri uri, String key){
        AmplifyS3Request amplifyS3Request = new AmplifyS3Request();

        amplifyS3Request.uploadImage(uri, key, context, new CallBackGeneric() {
            @Override
            public void onSuccess(String s) {
                Log.d(ItinerarioFragment_TAG, "onSuccess: Caricamento dell'immagine su s3 completato " + s);
                aggiungiFotoAggiuntiva(key);
            }
            @Override
            public void onFailure(Throwable throwable) {
                Log.d(ItinerarioFragment_TAG, "onFailure: Caricamento dell'immagine su s3 fallita: " + throwable.getLocalizedMessage());
                Toasty.error(context, "Non è stato possibile caricare l'immagine selezionata", Toasty.LENGTH_SHORT, true).show();
            }
        });
    }

    private void aggiungiFotoAggiuntiva(String key){
        GalleriaFotoItinerario fotoDaAggiungere = new GalleriaFotoItinerario();
        fotoDaAggiungere.setIdFotoItinerario(key);
        fotoDaAggiungere.setUrlFotoItinerario(key);
        fotoDaAggiungere.setItinerarioProprietario(nomeItinerario);
        GalleriaFotoItinerarioRequest request = new GalleriaFotoItinerarioRequest();

        request.aggiungiFoto(fotoDaAggiungere, new CallBackGalleriaFotoItinerario() {
            @Override
            public void onSuccessList(List<GalleriaFotoItinerario> galleriaFotoItinerarioList) {
                // Nulla da fare qui
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.d(ItinerarioFragment_TAG, "onFailure: Caricamento delle informazioni dell'immagine sul db fallita : " + throwable.getLocalizedMessage());
                Toasty.error(context, "Non è stato possibile completare la richiesta", Toasty.LENGTH_SHORT, true).show();
            }

            @Override
            public void onSuccessResponse(Response<Void> response) {
                if(response.isSuccessful()){
                    Toasty.success(context, "L'aggiunta è andata a buon fine", Toasty.LENGTH_SHORT, true).show();
                }else{
                    Toasty.error(context, "Non è stato possibile completare la richiesta", Toasty.LENGTH_SHORT, true).show();
                }
            }
        });
    }

    // Conversione key -> url (SpringBoot/S3)
    private void retrievalFotoProfiloItinerario(String key){
        AmplifyS3Request amplifyS3Request = new AmplifyS3Request();
        amplifyS3Request.getUrlImmagine(key, new CallBackGeneric() {
            @Override
            public void onSuccess(String urlFoto) {
                urlFotoItinerario = urlFoto;
                Glide.with(context).load(urlFoto).circleCrop().into(FotoItinerarioImageView);
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.d(ItinerarioFragment_TAG, "Impossibile caricare l'immagine del profilo richiesta");
            }
        });

    }

    // Retrieval dei tag appartenenti ad un itinerario
    private void retrieveDataTagItinerario(){
        TagRicercaRequest tagRicercaRequest = new TagRicercaRequest();
        tagRicercaRequest.getTagsStessoItinerario(nomeItinerario, new CallBackTagRicerca() {
            @Override
            public void onSuccessList(List<TagRicerca> tags) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                LinearLayout tagLayout;

                for (TagRicerca tag : tags) {
                    tagLayout = (LinearLayout) inflater.inflate( R.layout.tag_ricerca_token, TagFlexBoxLayout, false);
                    ((MaterialTextView) tagLayout.getChildAt(1)).setText(tag.getValue());
                    TagFlexBoxLayout.addView(tagLayout);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                apiHandler = new ApiHandler(throwable, getActivity());
            }
            @Override
            public void onSuccessResponse(Response<Void> response) {
                // Nulla da fare qui
            }
        });
    }

    // Set della difficoltà di un percorso
    private void setDifficolta(String difficolta) {
        Log.d(ItinerarioFragment_TAG, "DIFFICOLTA" + difficolta);
        DifficoltaTextView.setText(difficolta);
        switch (difficolta) {
            case "Facile":
            {
                DifficoltaImageView.setImageResource(R.drawable.difficolta_facile);
                break;
            }
            case "Non molto facile":
            {
                DifficoltaImageView.setImageResource(R.drawable.difficolta_non_molto_facile);
                break;
            }
            case "Medio":
            {
                DifficoltaImageView.setImageResource(R.drawable.difficolta_medio);
                break;
            }
            case "Difficile":
            {
                DifficoltaImageView.setImageResource(R.drawable.difficolta_difficile);
                break;
            }
            case "Molto Difficile":
            {
                DifficoltaImageView.setImageResource(R.drawable.difficolta_molto_difficile);
                break;
            }
            default:
                Log.e(ItinerarioFragment_TAG, "Errore nel recupero del valore della difficoltà");
        }
    }

    // Informazione riguardante l'accesso ai disabili (Si/No)
    private void setDisabilita(String disabilita) {
        switch (disabilita) {
            case "Si":
            {
                DisabilitaTextView.setText(R.string.Disabilita_accessibile);
                break;
            }
            case "No":
            {
                DisabilitaTextView.setText(R.string.Disabilita_non_accessibile);
                break;
            }
            default:
                Log.e(ItinerarioFragment_TAG, "Errore nel recupero del valore di accesso disabili");
        }
    }


    // Una volta creata la chat con l'utente spostiamo l'utente alla chat corrispondente
    private void passaggioASingolaChat(String idChatRoom, String utenteLocale){
        ChatSingolaFragment chatSingolaFragment = new ChatSingolaFragment(idChatRoom, utenteLocale);
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.FragmentContainer, chatSingolaFragment, "ChatSingolaFragment")
                .addToBackStack(ItinerarioFragment_TAG)
                .commit();
    }
}