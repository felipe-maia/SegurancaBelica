package com.example.segurancabelica.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.segurancabelica.R;
import com.example.segurancabelica.config.ConfigFirebase;
import com.google.firebase.auth.FirebaseAuth;

public class InicialActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao = ConfigFirebase.getAutenticacao();
    private Button btLogin, btCadastrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicial);
        btLogin = findViewById(R.id.btLogin);
        btLogin.setOnClickListener(view -> logar());
        btCadastrar = findViewById(R.id.btEntrar);
        btCadastrar.setOnClickListener(view -> cadastrar());
    }

    @Override
    protected void onStart() {
        super.onStart();
        verificaLogin();
    }

    public void cadastrar() {
        startActivity(new Intent(this, CadastroActivity.class));
    }

    public void logar() {
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void verificaLogin() {
        if (autenticacao.getCurrentUser() != null) {
            Toast.makeText(getApplicationContext(), "Bem vindo de volta " + autenticacao.getCurrentUser().getEmail() + "!", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "Usuario não está logado ", Toast.LENGTH_LONG).show();
        }
    }
}
