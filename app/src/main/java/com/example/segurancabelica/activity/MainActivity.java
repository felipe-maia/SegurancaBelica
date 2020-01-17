package com.example.segurancabelica.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.segurancabelica.R;
import com.example.segurancabelica.config.ConfigFirebase;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao = ConfigFirebase.getAutenticacao();
    private Button btToken, btRelatorios, btLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btToken = findViewById(R.id.btActivityToken);
        btToken.setOnClickListener(view -> abrirAtivityToken());

        btRelatorios = findViewById(R.id.btRelatorio);
        btRelatorios.setOnClickListener(view -> abrirRelatorio());

        btLogout = findViewById(R.id.btLogout);
        btLogout.setOnClickListener(v -> {
            autenticacao.signOut();
            sair();
        });
    }

    public void sair() {
        startActivity(new Intent(this, InicialActivity.class));
        finish();
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
