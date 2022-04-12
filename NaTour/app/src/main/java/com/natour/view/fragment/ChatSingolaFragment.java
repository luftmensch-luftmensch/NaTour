package com.natour.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.natour.R;
import com.natour.model.Messaggio;
import com.natour.presenter.adapter.ChatSingolaAdapter;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.natour.presenter.callbackInterface.CallBackMessaggi;
import com.natour.presenter.request.MessaggiRequest;
import com.natour.utils.handler.ApiHandler;
import com.natour.utils.persistence.LocalUser;
import com.natour.utils.persistence.LocalUserDbManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;
import retrofit2.Response;

public class ChatSingolaFragment extends Fragment {
    private static final String ChatSingolaFragment_TAG = "ChatSingolaFragment";
    private Context context;

    private AppBarLayout    TopAppBar;
    private MaterialToolbar TopToolBar;

    private RecyclerView chatSingolaRecyclerView;
    private ChatSingolaAdapter chatSingolaAdapter;

    /*
     Così come fatto nelle altre classi per evitare di gestire il caso di un possibile NullPointerException,
     utilizziamo l'Edit Text contenuto nell'Input Layout (invece di prenderlo direttamente dall'Input Layout)
    */

    //private TextInputLayout InviaMessaggioTextInput;
    private EditText InviaMessaggioEditText;

    private ImageButton InviaMessaggioImageButton;

    // Api Handler
    private ApiHandler apiHandler;

    // Db Manager
    private LocalUserDbManager localUserDbManager;
    private LocalUser localUser;

    private String idChatRoom;
    private String utenteNonLocale;

    public ChatSingolaFragment(String idChatRoom, String utenteNonLocale){
        this.idChatRoom = idChatRoom;
        this.utenteNonLocale = utenteNonLocale;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat_singola, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUI(view);
        localUser = getDatiutente();
        if (isAdded()) {
            retrievialMessaggi();
        }
    }

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

        TopToolBar.setTitle("Chat con " + utenteNonLocale);
        TopToolBar.getMenu().clear();
        TopToolBar.setNavigationIcon(R.drawable.arrow_back_24dp);

        TopToolBar.setNavigationOnClickListener(click -> requireActivity().getSupportFragmentManager().popBackStackImmediate());

        requireActivity().findViewById(R.id.FloatingActionButton).setVisibility(View.GONE);
        requireActivity().findViewById(R.id.RicercaTextInput_appBar).setVisibility(View.GONE);
        requireActivity().findViewById(R.id.NuovoItinerarioPopup).setVisibility(View.GONE);
        requireActivity().findViewById(R.id.NuovoItinerarioPopupOutside).setVisibility(View.GONE);

        InviaMessaggioEditText = view.findViewById(R.id.InviaMessaggioEditText);

        InviaMessaggioImageButton = view.findViewById(R.id.InviaMessaggioImageButton);
        chatSingolaRecyclerView = view.findViewById(R.id.ChatSingolaRecyclerView);
        chatSingolaRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        InviaMessaggioImageButton.setOnClickListener(click -> {
            String testoMessaggio = InviaMessaggioEditText.getText().toString();
            String data = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
            String ora = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

            if(testoMessaggio.equals("")){
                Toasty.error(context,
                        "Attenzione!\n Impossibile inviare un messaggio senza corpo",
                        Toasty.LENGTH_SHORT,
                        true).show();
            } else {
                // Creiamo un nuovo oggetto messaggio solo nel caso in cui il testoMessaggio non sia vuoto
                Messaggio messaggio = new Messaggio();

                // Passiamo l'id del messaggio a -1 in modo tale che sia autogestito dal Server
                messaggio.setIdMessaggio(-1L);
                messaggio.setMittente(localUser.getUsername());
                messaggio.setDestinatario(utenteNonLocale);
                messaggio.setTestoMessaggio(testoMessaggio);

                messaggio.setDataInvioMessaggio(data);
                messaggio.setOraInvioMessaggio(ora);
                messaggio.setChatRoomProprietaria(idChatRoom);

                // Svuoto i dati della edit text una volta costruito il messaggio
                InviaMessaggioEditText.setText("");

                // Infine inviamo il messaggio al backend
                inviaMessaggio(messaggio);
            }
        });
    }

    private void retrievialMessaggi(){
        MessaggiRequest messaggiRequest = new MessaggiRequest();
        messaggiRequest.getMessaggiByChatRoom(idChatRoom, new CallBackMessaggi() {
            @Override
            public void onSuccessList(List<Messaggio> messaggi) {
                Log.d(ChatSingolaFragment_TAG, "onSuccessList: Get dei messaggi della chat room");
                chatSingolaAdapter = new ChatSingolaAdapter(messaggi, getActivity(), localUser.getUsername());
                chatSingolaRecyclerView.setAdapter(chatSingolaAdapter);
                chatSingolaRecyclerView.scrollToPosition(messaggi.size() - 1);
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

    private void inviaMessaggio(Messaggio messaggio){
        MessaggiRequest messaggiRequest = new MessaggiRequest();
        messaggiRequest.inviaMessaggio(messaggio, new CallBackMessaggi() {
            @Override
            public void onSuccessList(List<Messaggio> messaggi) {
                Log.d(ChatSingolaFragment_TAG, "onSuccessList");
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.d(ChatSingolaFragment_TAG, "onFailure");
            }

            @Override
            public void onSuccessResponse(Response<Void> response) {
                Log.d(ChatSingolaFragment_TAG, "onSuccessResponse" + response.code());
                retrievialMessaggi();
            }
        });
    }
}
