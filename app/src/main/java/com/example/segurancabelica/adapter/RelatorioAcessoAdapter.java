package com.example.segurancabelica.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.segurancabelica.R;
import com.example.segurancabelica.model.Acessos;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class RelatorioAcessoAdapter extends RecyclerView.Adapter<RelatorioAcessoAdapter.MinhaViewHolder> {

    private List<Acessos> listaAcessos;
    private SimpleDateFormat dfDataFirebase = new SimpleDateFormat("yyyyMMdd",Locale.ENGLISH);
    private SimpleDateFormat dfMostrarData = new SimpleDateFormat("dd/MM/yyyy",Locale.ENGLISH);


    public RelatorioAcessoAdapter(List<Acessos> lista) {
        this.listaAcessos = lista;
    }

    @NonNull
    @Override
    public MinhaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.relatorio_acessos, parent, false);

        return new MinhaViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MinhaViewHolder holder, int position) {

        Acessos acessos =  listaAcessos.get(position);
        if (acessos.isStatusAlarme()) {
            holder.statusAlarme.setText("Alarme Ativado");
        }else
            holder.statusAlarme.setText("Alarme Desativado");

        try {
            Date dataFormatada = dfDataFirebase.parse(String.valueOf(acessos.getData()));
            holder.data.setText(dfMostrarData.format(dataFormatada));

        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.codigoCartao.setText(acessos.getCodigoCartao());
        holder.hora.setText(String.valueOf(acessos.getHora()));

    }

    @Override
    public int getItemCount() {
        return this.listaAcessos.size();
    }

    public class MinhaViewHolder extends RecyclerView.ViewHolder{

        private TextView statusAlarme;
        private TextView codigoCartao;
        private TextView data;
        private TextView hora;


        public MinhaViewHolder(@NonNull View itemView) {
            super(itemView);

            statusAlarme = itemView.findViewById(R.id.textStatus);
            codigoCartao = itemView.findViewById(R.id.textCodigo);
            data = itemView.findViewById(R.id.textDataInicio);
            hora = itemView.findViewById(R.id.textHora);

        }
    }

}
