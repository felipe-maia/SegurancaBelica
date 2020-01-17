package com.example.segurancabelica.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.segurancabelica.R;
import com.example.segurancabelica.model.DisparoAlarme;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RelatorioDisparoAdapter extends RecyclerView.Adapter<RelatorioDisparoAdapter.DisparoViewHolder> {

    private List<DisparoAlarme> listaDisparos;
    private SimpleDateFormat dfDataFirebase = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);
    private SimpleDateFormat dfMostrarData = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);


    public RelatorioDisparoAdapter(List<DisparoAlarme> lista) {
        this.listaDisparos = lista;
    }

    @NonNull
    @Override
    public DisparoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.relatorio_disparo_alarme, parent, false);

        return new DisparoViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull DisparoViewHolder holder, int position) {

        DisparoAlarme disparoAlarme = listaDisparos.get(position);
        if (disparoAlarme.isAlarme()) {
            holder.statusDisparo.setText("Alarme Disparado");
        } else
            holder.statusDisparo.setText("Alarme Não Disparado");

        try {
            Date dataFormatada = dfDataFirebase.parse(String.valueOf(disparoAlarme.getData()));
            holder.data.setText(dfMostrarData.format(dataFormatada));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.hora.setText(disparoAlarme.getHora());


    }

    @Override
    public int getItemCount() {
        return listaDisparos.size();
    }

    public class DisparoViewHolder extends RecyclerView.ViewHolder {

        private TextView statusDisparo;
        private TextView data;
        private TextView hora;

        public DisparoViewHolder(@NonNull View itemView) {
            super(itemView);
            statusDisparo = itemView.findViewById(R.id.textStatus);
            data = itemView.findViewById(R.id.textData);
            hora = itemView.findViewById(R.id.textHora);
        }
    }
}
