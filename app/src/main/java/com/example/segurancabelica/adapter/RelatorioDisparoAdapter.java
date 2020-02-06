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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RelatorioDisparoAdapter extends RecyclerView.Adapter<RelatorioDisparoAdapter.DisparoViewHolder> {

    private List<DisparoAlarme> listaDisparos;
    private SimpleDateFormat dfDataFirebase = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);
    private SimpleDateFormat dfMostrarData = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
    private SimpleDateFormat dfMostraHora = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);


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
        String status;
        if (disparoAlarme.isAlarme())
            status = "Alarme Disparado";
        else
            status = "Alarme Reativado";

        holder.statusDisparo.setText(status);

        try {
            Date dataFormatada = dfDataFirebase.parse(String.valueOf(disparoAlarme.getData()));

            Calendar c = Calendar.getInstance();
            c.setTime(dataFormatada);
            c.set(Calendar.HOUR, disparoAlarme.getHora());
            c.set(Calendar.MINUTE, disparoAlarme.getMin());
            c.set(Calendar.SECOND, disparoAlarme.getSeg());

            holder.data.setText(dfMostrarData.format(c.getTime()));
            holder.hora.setText(dfMostraHora.format(c.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
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
            statusDisparo = itemView.findViewById(R.id.textNome);
            data = itemView.findViewById(R.id.textPosto);
            hora = itemView.findViewById(R.id.textEmail);
        }
    }
}
