package com.natour.presenter.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.textview.MaterialTextView;
import com.natour.model.ChatRoom;
import com.natour.R;
import com.natour.model.Utente;
import com.natour.presenter.amplify.AmplifyS3Request;
import com.natour.presenter.callbackInterface.CallBackGeneric;
import com.natour.presenter.callbackInterface.CallBackUtente;
import com.natour.presenter.request.UtenteRequest;

import java.util.List;

// NB: Di default non avevamo parametri sulla RecyclerView (Raw use of parameterized class 'RecyclerView.Adapter')
public class ChatRoomRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String ChatRoomViewAdapter_TAG = "ChatRoomViewAdapter";
    List<ChatRoom> listaChatRoom;
    Context chatRoomContext;
    String utenteLocale;

    // Costruttore
    public ChatRoomRecyclerViewAdapter(List<ChatRoom> listaChatRoom, Context chatRoomContext, String utenteLocale) {
        this.listaChatRoom = listaChatRoom;
        this.chatRoomContext = chatRoomContext;
        this.utenteLocale = utenteLocale;
    }

    private class ChatRoomRecyclerViewHolder extends RecyclerView.ViewHolder{
        MaterialTextView destinatarioTextView;
        ImageView fotoProfiloDestinatario;

        public ChatRoomRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            destinatarioTextView = itemView.findViewById(R.id.DestinatarioTextView_messaggio);
            fotoProfiloDestinatario = itemView.findViewById(R.id.FotoProfiloImageView_messaggio);
        }

        void bind(ChatRoom chatRoom){
            if(chatRoom.getUtenteB().equals(utenteLocale)){
                destinatarioTextView.setText(chatRoom.getUtenteA());
                getInformazioniUtente(chatRoom.getUtenteA());
            }else{
                destinatarioTextView.setText(chatRoom.getUtenteB());
                getInformazioniUtente(chatRoom.getUtenteB());
            }
        }
        // Metodo per il retrieval dei dati dell'utente dal db
        void getInformazioniUtente(String username){
            UtenteRequest utenteRequest = new UtenteRequest();
            utenteRequest.getSingoloUtente(username, new CallBackUtente() {
                @Override
                public void onSuccess(Utente utente) {
                    Log.d(ChatRoomViewAdapter_TAG, "onSuccesS: Retrieval dell'utente per il caricamento dell'immagine di profilo");
                    getUrlFotoProfilo(utente.getUrlFotoProfilo());

                }

                @Override
                public void onSuccessList(List<Utente> listaUtenti) {
                    // Nulla da fare qui
                }

                @Override
                public void onFailure(Throwable throwable) {
                    Log.d(ChatRoomViewAdapter_TAG, "onFailure: Impossibile recuperare i dati dell'utente" + throwable.getLocalizedMessage());
                }
            });

        }

        // Metodo per il retrieval dell'url della foto profilo dell'utente dal bucket S3
        void getUrlFotoProfilo(String urlFotoProfilo){
            AmplifyS3Request amplifyS3Request = new AmplifyS3Request();
            amplifyS3Request.getUrlImmagine(urlFotoProfilo, new CallBackGeneric() {
                @Override
                public void onSuccess(String urlFotoUtente) {
                    Log.d(ChatRoomViewAdapter_TAG, "onSuccess: Caricamento dell'Immagine con s3 e Glide nella chatroom");
                    Glide.with(fotoProfiloDestinatario.getContext())
                            .load(urlFotoUtente).circleCrop()
                            .into(fotoProfiloDestinatario);
                }
                @Override
                public void onFailure(Throwable throwable) {
                    Log.d(ChatRoomViewAdapter_TAG, "onFailure: Impossibile recuperare l'url della foto richiesta " + throwable.getLocalizedMessage());
                }
            });

        }

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View chatRoomView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.card_chat_singola_layout, parent, false);
        return  new ChatRoomRecyclerViewHolder(chatRoomView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatRoom chatRoom = listaChatRoom.get(position);
        ((ChatRoomRecyclerViewHolder) holder).bind(chatRoom);
    }

    @Override
    public int getItemCount() { return listaChatRoom.size(); }
}
