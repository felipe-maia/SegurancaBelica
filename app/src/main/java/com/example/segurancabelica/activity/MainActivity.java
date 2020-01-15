package com.example.segurancabelica.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.segurancabelica.R;


public class MainActivity extends AppCompatActivity  {

    private Button btToken, btRelatorioAcesso, btRelatorioAlarme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btToken = findViewById(R.id.btActivityToken);
        btToken.setOnClickListener(view -> abrirAtivityToken());

        btRelatorioAcesso = findViewById(R.id.btRelatorioAcesso);
        btRelatorioAcesso.setOnClickListener(view -> abrirRelatorio());

        btRelatorioAlarme = findViewById(R.id.btRealatorioAlarme);

        //deslogar usuario
        //user.signOut();
    }


    public void abrirRelatorio() {
        startActivity(new Intent(this, RelatorioActivity.class));
        finish();
    }

    public void abrirAtivityToken() {
        startActivity(new Intent(this, TokenNovoCadastroActivity.class));
        finish();
    }
}
