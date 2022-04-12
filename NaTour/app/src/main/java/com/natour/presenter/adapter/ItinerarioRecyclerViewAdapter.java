/*

 Scritto da Valentino Bocchetti e Mario Gabriele Carofano
 Copyright (c) 2022. All rights reserved.

*/
package com.natour.presenter.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.natour.model.Itinerario;
import com.natour.R;
import com.natour.presenter.amplify.AmplifyS3Request;
import com.bumptech.glide.Glide;
import com.natour.presenter.callbackInterface.CallBackGeneric;

import java.util.List;
// NB: Di default non avevamo parametri sulla RecyclerView (Raw use of parameterized class 'RecyclerView.Adapter')
public class ItinerarioRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String ItinerarioRecyclerViewAdapter_TAG = "ItinerarioRecyclerViewAdapter";
    List<Itinerario> listaItinerari;
    Context itinerarioContext;

    // Construttore
    public ItinerarioRecyclerViewAdapter(List<Itinerario> listaItinerari, Context itinerarioContext) {
        this.listaItinerari = listaItinerari;
        this.itinerarioContext = itinerarioContext;
    }

    private class ItinerarioRecyclerViewHolder extends RecyclerView.ViewHolder{
        TextView nomeItinerario, cittaItinerario, durataItinerario;
        ImageView fotoItinerario;

        ItinerarioRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            nomeItinerario = itemView.findViewById(R.id.NomeItinerarioTextView);
            cittaItinerario = itemView.findViewById(R.id.CittaItinerarioTextView);
            durataItinerario = itemView.findViewById(R.id.DurataItinerarioTextView);
            fotoItinerario = itemView.findViewById(R.id.FotoItinerarioImageView);
        }

        void bind(Itinerario itinerario) {
            AmplifyS3Request amplifyS3Request = new AmplifyS3Request();

            nomeItinerario.setText(itinerario.getNomeItinerario());
            cittaItinerario.setText(itinerario.getZonaGeografica());
            durataItinerario.setText(itinerario.getDurata());

            if(itinerario.getStatoFotoSegnalazione().equals("Si")){
                Log.d(ItinerarioRecyclerViewAdapter_TAG, "La foto è stata segnalata, pertanto non verrà caricata sulla homepage");
            }else{
                Log.d(ItinerarioRecyclerViewAdapter_TAG, "La foto non è stata segnalata, pertanto il valore dell'url dell'itinerario verrà convertito per ottenere l'immagine corrispondente");

                // Conversione da key (url_foto_itinerario) a url per l'utilizzo con glide
                amplifyS3Request.getUrlImmagine(itinerario.getUrlFotoItinerario(), new CallBackGeneric() {
                    @Override
                    public void onSuccess(String urlFotoItinerario) {
                        Log.d(ItinerarioRecyclerViewAdapter_TAG, "Conversione url : " + urlFotoItinerario);
                        Glide.with(fotoItinerario.getContext())
                                .load(urlFotoItinerario)
                                .into(fotoItinerario);
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.d(ItinerarioRecyclerViewAdapter_TAG, "Impossibile recuperare l'url della foto");
                    }
                });
            }
        }
    }

    @NonNull
    @Override
    public ItinerarioRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itinerarioView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.card_itinerario_layout, parent, false);
        return new ItinerarioRecyclerViewHolder(itinerarioView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Itinerario itinerario = listaItinerari.get(position);
        ((ItinerarioRecyclerViewHolder) holder).bind(itinerario);
    }

    @Override
    public int getItemCount() {
        return listaItinerari.size();
    }
}