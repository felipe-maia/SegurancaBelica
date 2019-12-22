package com.example.segurancabelica.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.segurancabelica.R;

public class InicialActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicial);
    }

    public void cadastrar(View v){
        startActivity(new Intent(this, CadastroActivity.class));
    }

    public void logar(View v){
        startActivity(new Intent(this, LoginActivity.class));
    }

}
