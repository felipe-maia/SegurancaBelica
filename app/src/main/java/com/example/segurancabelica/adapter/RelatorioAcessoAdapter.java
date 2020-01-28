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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class RelatorioAcessoAdapter extends RecyclerView.Adapter<RelatorioAcessoAdapter.AcessoViewHolder> {

    private List<Acessos> listaAcessos;
    private SimpleDateFormat dfDataFirebase = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);
    private SimpleDateFormat dfMostrarData = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
    private SimpleDateFormat dfMostraHora = new SimpleDateFormat( "HH:mm:ss");


    public RelatorioAcessoAdapter(List<Acessos> lista) {
        this.listaAcessos = lista;
    }

    @NonNull
    @Override
    public AcessoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.relatorio_acessos, parent, false);

        return new AcessoViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull AcessoViewHolder holder, int position) {

        Acessos acessos = listaAcessos.get(position);
        if (acessos.isStatusAlarme()) {
            holder.statusAlarme.setText("Alarme Ativado");
        } else
            holder.statusAlarme.setText("Alarme Desativado");

        try {
            Date dataFormatada = dfDataFirebase.parse(String.valueOf(acessos.getData()));
            holder.data.setText(dfMostrarData.format(dataFormatada));

        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.codigoCartao.setText("Codigo Cartão: "+acessos.getCodigoCartao());

        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR,acessos.getHora());
        c.set(Calendar.MINUTE, acessos.getMin());
        c.set(Calendar.SECOND, acessos.getSeg());

        holder.hora.setText(dfMostraHora.format(c.getTime()));
    }

    @Override
    public int getItemCount() {
        return this.listaAcessos.size();
    }

    public class AcessoViewHolder extends RecyclerView.ViewHolder {

        private TextView statusAlarme;
        private TextView codigoCartao;
        private TextView data;
        private TextView hora;


        public AcessoViewHolder(@NonNull View itemView) {
            super(itemView);

            statusAlarme = itemView.findViewById(R.id.textNome);
            codigoCartao = itemView.findViewById(R.id.textCodigo);
            data = itemView.findViewById(R.id.textPosto);
            hora = itemView.findViewById(R.id.textEmail);

        }
    }

}
