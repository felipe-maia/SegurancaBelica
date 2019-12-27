package com.example.segurancabelica.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.segurancabelica.R;
import com.example.segurancabelica.config.ConfigFirebase;
import com.example.segurancabelica.helper.Base64Custom;
import com.example.segurancabelica.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class CadastroActivity extends AppCompatActivity {

    private EditText edNome, edEmail, edPosto, edSenha, edToken;
    private Button btCadastrar;
    private FirebaseAuth autenticacao;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        edNome = findViewById(R.id.editNome);
        edEmail = findViewById(R.id.editEmail);
        edPosto = findViewById(R.id.editPosto);
        edSenha = findViewById(R.id.editSenha);
        edToken = findViewById(R.id.editToken);
        btCadastrar = findViewById(R.id.btEntrar);

        btCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textNome = edNome.getText().toString();
                String textPosto = edPosto.getText().toString();
                String textEmail = edEmail.getText().toString();
                String textSenha = edSenha.getText().toString();
                String textToken = edToken.getText().toString();

                if (textNome.isEmpty()) {
                    Toast.makeText(CadastroActivity.this, "Preencha o campo nome!", Toast.LENGTH_SHORT).show();
                } else {
                    if (textEmail.isEmpty()) {
                        Toast.makeText(CadastroActivity.this, "Preencha o campo e-mail!", Toast.LENGTH_SHORT).show();
                    } else {
                        if (textSenha.isEmpty()) {
                            Toast.makeText(CadastroActivity.this, "Preencha o campo senha!", Toast.LENGTH_SHORT).show();
                        } else {
                            if (textPosto.isEmpty()) {
                                Toast.makeText(CadastroActivity.this, "Preencha o campo posto!", Toast.LENGTH_SHORT).show();
                            } else {
                                if (textToken.isEmpty()) {
                                    Toast.makeText(CadastroActivity.this, "Preencha o campo token!", Toast.LENGTH_SHORT).show();
                                } else {
                                    


                                    usuario = new Usuario();
                                    usuario.setNome(textNome);
                                    usuario.setEmail(textEmail);
                                    usuario.setSenha(textSenha);
                                    cadastrarUsuario();
                                }
                            }
                        }
                    }
                }
            }
        });

    }

    public void cadastrarUsuario() {
        autenticacao = ConfigFirebase.getAutenticacao();
        autenticacao.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String idUsuario = Base64Custom.codificarBase64(usuario.getEmail());
                    usuario.setIdUsuario(idUsuario);
                    usuario.salvar();
                    finish();
                } else {
                    String excecao = "";
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        excecao = "Digite uma senha mais forte!";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        excecao = "Digite um e-mail válido!";
                    } catch (FirebaseAuthUserCollisionException e) {
                        excecao = "Essa conta já foi cadastrada!";
                    } catch (Exception e) {
                        excecao = "Erro ao cadastrar usuário: " + e.getMessage();
                        e.printStackTrace();
                    }
                    Toast.makeText(CadastroActivity.this, excecao, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
