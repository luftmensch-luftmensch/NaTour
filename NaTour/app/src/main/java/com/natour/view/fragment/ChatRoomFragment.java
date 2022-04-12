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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.natour.R;
import com.natour.model.ChatRoom;
import com.natour.presenter.adapter.ChatRoomRecyclerViewAdapter;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.natour.presenter.callbackInterface.CallBackChatRoom;
import com.natour.presenter.request.ChatRoomRequest;
import com.natour.utils.handler.ApiHandler;
import com.natour.utils.handler.RecyclerItemClickListener;
import com.natour.utils.persistence.LocalUser;
import com.natour.utils.persistence.LocalUserDbManager;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Response;

public class ChatRoomFragment extends Fragment {
    private static final String ChatRoomFragment_TAG = "ChatRoomFragment";
    private Context context;

    private AppBarLayout TopAppBar;
    private MaterialToolbar TopToolBar;
    private BottomNavigationView MenuInferiore;

    // Recycler view
    private RecyclerView ChatRoomRecyclerView;
    private ChatRoomRecyclerViewAdapter chatRoomRecyclerViewAdapter;

    // Api Handler
    private ApiHandler apiHandler;

    // Db Manager
    private LocalUserDbManager localUserDbManager;
    private LocalUser localUser;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_messaggi, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUI(view);
        localUser = getDatiutente();
        if (isAdded()) {
            retrievalChatRooms();
        }
    }

    private void setUI(View view) {
        TopAppBar = requireActivity().findViewById(R.id.AppBarLayout);
        TopToolBar = (MaterialToolbar) TopAppBar.getChildAt(0);
        MenuInferiore = requireActivity().findViewById(R.id.MenuInferiore);

        TopToolBar.setTitle(R.string.MessaggiButton);
        TopToolBar.getMenu().clear();
        TopToolBar.setNavigationIcon(null);

        requireActivity().findViewById(R.id.RicercaTextInput_appBar).setVisibility(View.GONE);
        requireActivity().findViewById(R.id.FloatingActionButton).setVisibility(View.GONE);
        requireActivity().findViewById(R.id.NuovoItinerarioPopup).setVisibility(View.GONE);
        requireActivity().findViewById(R.id.NuovoItinerarioPopupOutside).setVisibility(View.GONE);

        ChatRoomRecyclerView = view.findViewById(R.id.MessaggiRecyclerView);
        ChatRoomRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        MenuInferiore.getMenu().findItem(R.id.MessaggiButton).setChecked(true);
    }

    private LocalUser getDatiutente(){
        localUserDbManager = new LocalUserDbManager(this.getContext());
        localUserDbManager.openR();
        LocalUser localUser = localUserDbManager.fetchDataUser();
        localUserDbManager.closeDB();
        return localUser;
    }

    private void retrievalChatRooms(){
        ChatRoomRequest chatRoomRequest = new ChatRoomRequest();
        chatRoomRequest.getChatRoomsByUtente(localUser.getUsername(), new CallBackChatRoom() {
            @Override
            public void onSuccessList(List<ChatRoom> chatRooms) {
                Log.d(ChatRoomFragment_TAG, "onSuccessList: Get delle Chat room dell'utente loggato");
                chatRoomRecyclerViewAdapter = new ChatRoomRecyclerViewAdapter(chatRooms, getActivity(), localUser.getUsername());
                ChatRoomRecyclerView.setAdapter(chatRoomRecyclerViewAdapter);
                getSelectedItem(chatRooms);
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
    private void getSelectedItem(List<ChatRoom> chatRooms){
        ChatRoomRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(context, ChatRoomRecyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        String utenteNonLocale = null;
                        Log.d(ChatRoomFragment_TAG, String.valueOf(chatRooms.get(position).getIdChatRoom()));
                        if(chatRooms.get(position).getUtenteA().equals(localUser.getUsername())){
                            utenteNonLocale =  chatRooms.get(position).getUtenteB();
                        }

                        if(chatRooms.get(position).getUtenteB().equals(localUser.getUsername())){
                            utenteNonLocale =  chatRooms.get(position).getUtenteA();
                        }
                        passaggioASingolaChat(chatRooms.get(position).getIdChatRoom(), utenteNonLocale);
                    }
                    @Override public void onLongItemClick(View view, int position) {
                        Toasty.info(context, "Per accedere alla chat clicca senza tenere premuto",
                                Toast.LENGTH_SHORT, true).show();
                    }
                })
        );

    }
    private void passaggioASingolaChat(String idChatRoom, String utenteNonLocale){
        ChatSingolaFragment chatSingolaFragment = new ChatSingolaFragment(idChatRoom, utenteNonLocale);
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.FragmentContainer, chatSingolaFragment, "ChatSingolaFragment")
                .addToBackStack(ChatRoomFragment_TAG)
                .commit();
    }
}