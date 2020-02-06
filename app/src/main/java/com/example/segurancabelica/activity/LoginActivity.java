package com.example.segurancabelica.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.segurancabelica.R;
import com.example.segurancabelica.config.ConfigFirebase;
import com.example.segurancabelica.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class LoginActivity extends AppCompatActivity {

    private EditText edEmail, edSenha;
    private Button btEntrar;
    private Usuario usuario;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edEmail = findViewById(R.id.editEmail);
        edSenha = findViewById(R.id.editSenha);
        btEntrar = findViewById(R.id.btEntrar);
        btEntrar.setOnClickListener(view -> {
            String textEmail = edEmail.getText().toString();
            String textSenha = edSenha.getText().toString();
            if (textEmail.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Preencha o campo e-mail!", Toast.LENGTH_SHORT).show();
            } else {
                if (textSenha.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Preencha o campo senha!", Toast.LENGTH_SHORT).show();
                } else {
                    usuario = new Usuario();
                    usuario.setEmail(textEmail);
                    usuario.setSenha(textSenha);
                    validarLogin();
                }
            }
        });
    }

    public void validarLogin(){
        autenticacao = ConfigFirebase.getAutenticacao();
        autenticacao.signInWithEmailAndPassword(usuario.getEmail(),usuario.getSenha()).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                abrirMenuPincipal();
            }else{
                String excecao;
                try {
                    throw task.getException();
                }catch (FirebaseAuthInvalidCredentialsException e){
                    excecao = "E-mail e senha não correspondem a um usuário!";
                }catch (FirebaseAuthInvalidUserException e){
                    excecao = "Usuário não cadastrado!";
                }catch (Exception e){
                    excecao = "Erro ao efetuar login: " + e.getMessage();
                    e.printStackTrace();
                }
                Toast.makeText(LoginActivity.this, excecao, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void abrirMenuPincipal(){
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
