package com.example.segurancabelica.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.segurancabelica.R;
import com.example.segurancabelica.config.ConfigFirebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, DialogInterface.OnCancelListener {

    private DatabaseReference reference = ConfigFirebase.getDataBase();
    private DatabaseReference relatorios = reference.child("Acessos");
    private FirebaseAuth autenticacao;
    private Button btToken, btRelatorioAcesso, btRelatorioAlarme;
    private int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btToken = findViewById(R.id.btActivityToken);
        btToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirAtivityToken();
            }
        });

        btRelatorioAcesso = findViewById(R.id.btRelatorioAcesso);
        btRelatorioAlarme = findViewById(R.id.btRealatorioAlarme);
        btRelatorioAcesso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDate();
            }
        });




        /* buscando dados do firebase
        Date date = new Date();
        GregorianCalendar gc = new GregorianCalendar();
        gc.setGregorianChange(date);
        gc.add(Calendar.HOUR,-3);
        GregorianCalendar dataInicio = new GregorianCalendar();
        dataInicio.setTime(gc.getTime());
        GregorianCalendar dataFim = new GregorianCalendar();
        dataFim.setTime(gc.getTime());

        Query queryRelatorios = relatorios.orderByChild("dataHora").startAt(dataInicio.getTimeInMillis()).endAt(dataFim.getTimeInMillis());
        Log.v("relatorio", "Antes relatorio");
        queryRelatorios.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Log.v("relatorio", "Dados: "+ postSnapshot.getValue());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        */

        //deslogar usuario
        //user.signOut();
    }

    private void abrirAtivityToken() {
        startActivity(new Intent(this, TokenNovoCadastroActivity.class));
        finish();
    }

    public void selectDate(){
        initDate();
        Calendar dateDefault = Calendar.getInstance();
        dateDefault.set(year,month,day);

        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                this, dateDefault.get(Calendar.YEAR), dateDefault.get(Calendar.MONTH),dateDefault.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.setOnCancelListener(this);
        datePickerDialog.show(getSupportFragmentManager(),"DatePickerDialog");
    }

    public void initDate(){
        if(year == 0){
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

    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {

    }
}
