package com.natour.presenter.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.natour.R;
import com.natour.model.Messaggio;
import com.natour.utils.constants.Constants;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

// NB: Di default non avevamo parametri sulla RecyclerView (Raw use of parameterized class 'RecyclerView.Adapter')
public class ChatSingolaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Messaggio> listaMessaggi;

    private String username;

    // Costruttore
    public ChatSingolaAdapter(List<Messaggio> listaMessaggi, Context context, String username){
        this.context = context;
        this.listaMessaggi = listaMessaggi;
        this.username = username;
    }

    // Gestione dei messaggi lato mittente
    private class MessaggiMittenteHolder extends RecyclerView.ViewHolder {

        private MaterialTextView MessaggioTextView;
        private MaterialTextView DataDiInvio;
        private MaterialTextView OraDiInvio;

        MessaggiMittenteHolder(@NonNull View view) {
            super(view);
            MessaggioTextView = view.findViewById(R.id.MessaggioTextView_mittente);
            DataDiInvio = view.findViewById(R.id.DataInvioTextView_mittente);
            OraDiInvio = view.findViewById(R.id.OraInvioTextView_mittente);
        }

        void bind(Messaggio messaggio) {
            MessaggioTextView.setText(messaggio.getTestoMessaggio());
            DataDiInvio.setText(messaggio.getDataInvioMessaggio());
            OraDiInvio.setText(messaggio.getOraInvioMessaggio());
        }

    }

    // Gestione dei messaggi lato destinatario
    private class MessaggiDestinatarioHolder extends RecyclerView.ViewHolder {

        private MaterialTextView MessaggioTextView;
        private MaterialTextView DataDiInvio;
        private MaterialTextView OraDiInvio;

        MessaggiDestinatarioHolder(@NonNull View view) {
            super(view);
            MessaggioTextView = view.findViewById(R.id.MessaggioTextView_destinatario);
            DataDiInvio = view.findViewById(R.id.DataInvioTextView_destinatario);
            OraDiInvio = view.findViewById(R.id.OraInvioTextView_destinatario);
        }

        void bind(Messaggio messaggio) {
            MessaggioTextView.setText(messaggio.getTestoMessaggio());
            DataDiInvio.setText(messaggio.getDataInvioMessaggio());
            OraDiInvio.setText(messaggio.getOraInvioMessaggio());
        }

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if (viewType == Constants.VIEW_MESSAGGIO_INVIATO) {
            view = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.card_messaggio_mittente, parent, false);
            return new MessaggiMittenteHolder(view);
        } else if (viewType == Constants.VIEW_MESSAGGIO_RICEVUTO) {
            view = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.card_messaggio_destinatario, parent, false);
            return new MessaggiDestinatarioHolder(view);
        }

        throw new IllegalArgumentException("Impossibile creare il componente richiesto");
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Messaggio messaggio = listaMessaggi.get(position);

        switch (holder.getItemViewType()) {
            case Constants.VIEW_MESSAGGIO_INVIATO:
            {
                ((MessaggiMittenteHolder) holder).bind(messaggio);
                break;
            }
            case Constants.VIEW_MESSAGGIO_RICEVUTO:
            {
                ((MessaggiDestinatarioHolder) holder).bind(messaggio);
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return listaMessaggi.size();
    }

    @Override
    public int getItemViewType(int position) {
        Messaggio messaggio = listaMessaggi.get(position);

        if(username.equals(messaggio.getMittente())){
            return Constants.VIEW_MESSAGGIO_INVIATO;
        }
        return Constants.VIEW_MESSAGGIO_RICEVUTO;
    }
}