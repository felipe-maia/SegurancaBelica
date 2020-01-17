package com.example.segurancabelica.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.segurancabelica.R;
import com.example.segurancabelica.adapter.RelatorioAcessoAdapter;
import com.example.segurancabelica.adapter.RelatorioDisparoAdapter;
import com.example.segurancabelica.config.ConfigFirebase;
import com.example.segurancabelica.model.Acessos;
import com.example.segurancabelica.model.DisparoAlarme;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class RelatorioActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, DialogInterface.OnCancelListener {

    private DatabaseReference reference = ConfigFirebase.getDataBase();
    private DatabaseReference refAcessos = reference.child("Acessos");
    private DatabaseReference refDisparoAlarme = reference.child("DisparoAlarme");

    private Button btDataInicial, btDataFinal, btRelatorioAcessos, btRelatorioDisparos;
    private TextView textDataInicio, textDataFim;
    private RecyclerView recyclerRelatorio;
    private RelatorioAcessoAdapter relatorioAcessoAdapter;
    private RelatorioDisparoAdapter relatorioDisparoAdapter;
    private List<Acessos> relatorioAcessos;
    private List<DisparoAlarme> relatorioDisparo;
    private Calendar dataISelecionada, dataFSelecionada;
    private SimpleDateFormat dfBuscaFireBase, dfMostrarData;
    private int dataInicio, dataFim;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorio);
        inicializarVariaveis();


        btDataInicial.setOnClickListener(view -> {

            DatePickerDialog dataI = DatePickerDialog.newInstance(RelatorioActivity.this::onDateSet, dataISelecionada.get(Calendar.YEAR), dataISelecionada.get(Calendar.MONTH), dataISelecionada.get(Calendar.DAY_OF_MONTH));
            dataI.setTitle("Data inicial do relatório");
            dataI.setOnDateSetListener((view12, year, monthOfYear, dayOfMonth) -> {
                String dataSelecionadaString;

                dataISelecionada.set(year, monthOfYear, dayOfMonth);
                textDataInicio.setText(dfMostrarData.format(dataISelecionada.getTime()));

                dataSelecionadaString = dfBuscaFireBase.format(dataISelecionada.getTime());
                dataInicio = Integer.valueOf(dataSelecionadaString);

            });
            dataI.setOnCancelListener(this::onCancel);
            dataI.show(getSupportFragmentManager(), "DATAINICIAL");
        });


        btDataFinal.setOnClickListener(view -> {

            DatePickerDialog dataF = DatePickerDialog.newInstance(RelatorioActivity.this::onDateSet, dataFSelecionada.get(Calendar.YEAR), dataFSelecionada.get(Calendar.MONTH), dataFSelecionada.get(Calendar.DAY_OF_MONTH));
            dataF.setTitle("Data final do relatório");
            dataF.setOnDateSetListener((view1, year, monthOfYear, dayOfMonth) -> {
                String dataSelecionadaString;

                dataFSelecionada.set(year, monthOfYear, dayOfMonth);
                textDataFim.setText(dfMostrarData.format(dataFSelecionada.getTime()));


                dataSelecionadaString = dfBuscaFireBase.format(dataFSelecionada.getTime());
                dataFim = Integer.valueOf(dataSelecionadaString);

            });
            dataF.setOnCancelListener(this::onCancel);
            dataF.show(getSupportFragmentManager(), "DATAFINAL");
        });

        btRelatorioAcessos.setOnClickListener(view -> gerarRelatorioAcessos());

        btRelatorioDisparos.setOnClickListener(view -> gerarRelatorioDisparoAlarme());


    }
    public void inicializarVariaveis(){
        btDataInicial = findViewById(R.id.btDataInicial);
        btDataFinal = findViewById(R.id.btDataFinal);
        btRelatorioAcessos = findViewById(R.id.btRelatorio);
        btRelatorioDisparos = findViewById(R.id.btRelatorioDisparos);

        textDataInicio = findViewById(R.id.textData);
        textDataFim = findViewById(R.id.textDataFim);

        dfBuscaFireBase = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);
        dfMostrarData = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        relatorioAcessos = new ArrayList<>();
        relatorioDisparo = new ArrayList<>();

        dataISelecionada = dataFSelecionada = Calendar.getInstance();

        textDataInicio.setText(dfMostrarData.format(dataISelecionada.getTime()));
        textDataFim.setText(dfMostrarData.format(dataFSelecionada.getTime()));
    }

    public void gerarRelatorioAcessos() {
        relatorioAcessos.clear();

        Query queryRelatorioAcessos = refAcessos.orderByChild("data").startAt(dataInicio).endAt(dataFim);

        queryRelatorioAcessos.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Acessos acesso = postSnapshot.getValue(Acessos.class);
                    relatorioAcessos.add(acesso);
                }
                if (relatorioAcessos.size() == 0)
                    Toast.makeText(RelatorioActivity.this, "Não existe dados entre as datas selecionadas", Toast.LENGTH_LONG).show();
                else
                    configurarAdapter(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void gerarRelatorioDisparoAlarme() {
        relatorioDisparo.clear();

        Query queryRelatorioDisparo = refDisparoAlarme.orderByChild("data").startAt(dataInicio).endAt(dataFim);

        queryRelatorioDisparo.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    DisparoAlarme disparoAlarme =  postSnapshot.getValue(DisparoAlarme.class);
                    relatorioDisparo.add(disparoAlarme);
                }
                if (relatorioDisparo.size() == 0)
                    Toast.makeText(RelatorioActivity.this, "Não existe dados entre as datas selecionadas", Toast.LENGTH_LONG).show();
                else
                    configurarAdapter(false);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void configurarAdapter(boolean relatorio) {
        recyclerRelatorio = findViewById(R.id.recyclerRelatorio);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerRelatorio.setLayoutManager(layoutManager);
        recyclerRelatorio.setHasFixedSize(true);
        //configurando adapter
        if(relatorio) {
            relatorioAcessoAdapter = new RelatorioAcessoAdapter(relatorioAcessos);
            //configurando recycler
            recyclerRelatorio.setAdapter(relatorioAcessoAdapter);
        }else{
            relatorioDisparoAdapter = new RelatorioDisparoAdapter(relatorioDisparo);
            //configurando recycler
            recyclerRelatorio.setAdapter(relatorioDisparoAdapter);
        }
    }


    @Override
    public void onCancel(DialogInterface dialogInterface) {
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {

    }
}