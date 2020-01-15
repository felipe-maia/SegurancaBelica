package com.example.segurancabelica.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.segurancabelica.R;
import com.example.segurancabelica.config.ConfigFirebase;
import com.example.segurancabelica.model.Acessos;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class RelatorioActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, DialogInterface.OnCancelListener {

    private DatabaseReference reference = ConfigFirebase.getDataBase();
    private DatabaseReference refAcessos = reference.child("Acessos");
    private DatabaseReference refDisparoAlarme = reference.child("DisparoAlarme");

    private Button dtInicial, dtFinal, btRelatorio;
    private RecyclerView recyclerRelatorio;
    private List<Acessos> relatorioAcessos = new ArrayList<>();
    private int year, month, day;
    private GregorianCalendar dataInicio, dataFim;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorio);
        dataInicio = new GregorianCalendar();
        dataFim = new GregorianCalendar();
        recyclerRelatorio = findViewById(R.id.recyclerRelatorio);
        //configurando recycler
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerRelatorio.setLayoutManager(layoutManager);
        recyclerRelatorio.setHasFixedSize(true);
        //recyclerRelatorio.setAdapter();

        dtInicial = findViewById(R.id.btDataInicial);
        dtInicial.setOnClickListener(view -> {
            inicializarData();
            Calendar dateDefault = Calendar.getInstance();
            dateDefault.set(year, month, day);
            DatePickerDialog dataI = DatePickerDialog.newInstance(RelatorioActivity.this::onDateSet, dateDefault.get(Calendar.YEAR), dateDefault.get(Calendar.MONTH), dateDefault.get(Calendar.DAY_OF_MONTH));
            dataI.setTitle("Data inicial do relatório");
            dataI.setOnDateSetListener((view12, year, monthOfYear, dayOfMonth) -> {
                dataInicio.set(year, monthOfYear, dayOfMonth);
                Log.i("msg", "DATA IN  " + dataInicio.getTime());
            });
            dataI.setOnCancelListener(this::onCancel);
            dataI.show(getSupportFragmentManager(), "DATAINICIAL");
        });
        dtFinal = findViewById(R.id.btDataFinal);
        dtFinal.setOnClickListener(view -> {
            inicializarData();
            Calendar dateDefault = Calendar.getInstance();
            dateDefault.set(year, month, day);
            DatePickerDialog dataF = DatePickerDialog.newInstance(RelatorioActivity.this::onDateSet, dateDefault.get(Calendar.YEAR), dateDefault.get(Calendar.MONTH), dateDefault.get(Calendar.DAY_OF_MONTH));
            dataF.setTitle("Data final do relatório");
            dataF.setOnDateSetListener((view1, year, monthOfYear, dayOfMonth) -> {
                dataFim.set(year, monthOfYear, dayOfMonth);
                Log.i("msg", "DATA FIM  " + dataFim.getTime());

            });
            dataF.setOnCancelListener(this::onCancel);
            dataF.show(getSupportFragmentManager(), "DATAFINAL");
        });

        btRelatorio = findViewById(R.id.btRelatorio);
        btRelatorio.setOnClickListener(view -> gerarRelatorioAcessos());


        //selecionarData();


        /*//buscando dados do firebase
        Date date = new Date();
        GregorianCalendar gc = new GregorianCalendar();
        gc.setGregorianChange(date);
        gc.add(Calendar.HOUR,-3);
        GregorianCalendar dataInicio = new GregorianCalendar();
        dataInicio.setTime(gc.getTime());
        GregorianCalendar dataFim = new GregorianCalendar();
        dataFim.setTime(gc.getTime());
        */

        //Query queryRelatorios = relatorios.orderByChild("dataHora").startAt(dataInicio.getTimeInMillis()).endAt(dataFim.getTimeInMillis());





    }

    public void inicializarData() {
        if (year == 0) {
            Calendar calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
        }
    }

    @Override
    public void onCancel(DialogInterface dialogInterface) {
        year = month = day = 0;
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        //dataInicio.set(year, monthOfYear, dayOfMonth);
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {

    }

    public void gerarRelatorioAcessos() {
        relatorioAcessos.clear();

        Query queryRelatorioAcessos = refAcessos.orderByChild("data").startAt(20200101).endAt(20201031);
        queryRelatorioAcessos.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Acessos acesso = postSnapshot.getValue(Acessos.class);
                    relatorioAcessos.add(acesso);
                    Log.v("Relatorio Acessos", "Dado: "+ acesso.getCodigoCartao() + acesso.getData() + acesso.getHora());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void gerarRelatorioDisparoAlarme(){
        Query queryRelatorioDisparo = refDisparoAlarme.orderByChild("data").startAt(20200101).endAt(20201031);

        queryRelatorioDisparo.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Log.v("Relatorio Alarme", "Dado: " + postSnapshot.getValue());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}