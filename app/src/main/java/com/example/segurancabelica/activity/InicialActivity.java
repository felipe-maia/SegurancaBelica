package com.example.segurancabelica.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.segurancabelica.R;
import com.example.segurancabelica.config.ConfigFirebase;
import com.google.firebase.auth.FirebaseAuth;

public class InicialActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicial);
    }

    @Override
    protected void onStart() {
        super.onStart();
        verificaLogin();
    }

    public void cadastrar(View v){
        startActivity(new Intent(this, CadastroActivity.class));
    }

    public void logar(View v){
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void verificaLogin() {
        autenticacao = ConfigFirebase.getAutenticacao();
        if (autenticacao.getCurrentUser()!=null){
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }
}
