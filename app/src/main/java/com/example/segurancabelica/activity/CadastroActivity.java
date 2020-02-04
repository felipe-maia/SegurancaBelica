package com.example.segurancabelica.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.segurancabelica.R;
import com.example.segurancabelica.config.ConfigFirebase;
import com.example.segurancabelica.helper.Base64Custom;
import com.example.segurancabelica.model.TokenUsuarioNovo;
import com.example.segurancabelica.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class CadastroActivity extends AppCompatActivity {

    private DatabaseReference reference = ConfigFirebase.getDataBase();
    private DatabaseReference tokenDB = reference.child("tokenUser");
    private Spinner spinnerPosto;
    private EditText edNome, edEmail, edSenha, edToken;
    private Button btCadastrar;
    private FirebaseAuth autenticacao;
    private TokenUsuarioNovo buscaUltimoToken;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        edNome = findViewById(R.id.editNome);
        edEmail = findViewById(R.id.editEmail);
        spinnerPosto = findViewById(R.id.spinner);
        edSenha = findViewById(R.id.editSenha);
        edToken = findViewById(R.id.editToken);
        btCadastrar = findViewById(R.id.btEntrar);

        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(this, R.array.postos, android.R.layout.simple_spinner_item);
        spinnerPosto.setAdapter(arrayAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        btCadastrar.setOnClickListener(view -> {
            final String textNome = edNome.getText().toString();
            final String textPosto = spinnerPosto.getSelectedItem().toString();
            final String textEmail = edEmail.getText().toString();
            final String textSenha = edSenha.getText().toString();
            final String textToken = edToken.getText().toString();

            if (textNome.isEmpty()) {
                Toast.makeText(CadastroActivity.this, "Preencha o campo nome!", Toast.LENGTH_SHORT).show();
            } else {
                if (textEmail.isEmpty()) {
                    Toast.makeText(CadastroActivity.this, "Preencha o campo e-mail!", Toast.LENGTH_SHORT).show();
                } else {
                    if (textSenha.isEmpty()) {
                        Toast.makeText(CadastroActivity.this, "Preencha o campo senha!", Toast.LENGTH_SHORT).show();
                    } else {
                        if (textPosto.equals("Posto/Graduação")) {
                            Toast.makeText(CadastroActivity.this, "Selecione o posto/graduação!", Toast.LENGTH_SHORT).show();
                        } else {
                            if (textToken.isEmpty()) {
                                Toast.makeText(CadastroActivity.this, "Preencha o campo token!", Toast.LENGTH_SHORT).show();
                            } else {
                                usuario = new Usuario();
                                usuario.setNome(textNome);
                                usuario.setEmail(textEmail);
                                usuario.setSenha(textSenha);
                                usuario.setPosto(textPosto);
                                buscaUltimoToken(textToken);
                            }
                        }
                    }
                }
            }
        });

    }

    public void buscaUltimoToken(final String textToken) {
        buscaUltimoToken = new TokenUsuarioNovo();

        Query queryUltimoToken = tokenDB.orderByKey().limitToLast(1);

        queryUltimoToken.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    buscaUltimoToken = postSnapshot.getValue(TokenUsuarioNovo.class);
                    buscaUltimoToken.setKey(postSnapshot.getKey());
                }
                if (buscaUltimoToken.isStatus()) {
                    Toast.makeText(CadastroActivity.this, "Token já utilizado!", Toast.LENGTH_SHORT).show();
                } else {
                    if (buscaUltimoToken.getToken().equals(textToken)) {
                        usuario.setPermissao(buscaUltimoToken.getNivelPermissao());
                        usuario.setCodigoCartao(buscaUltimoToken.getCodigoCartao());
                        cadastrarUsuario();
                    } else {
                        Toast.makeText(CadastroActivity.this, "Token incorreto!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
                    usuario.setId(idUsuario);
                    usuario.salvar();
                    buscaUltimoToken.setStatus(true);
                    tokenDB.child(buscaUltimoToken.getKey()).setValue(buscaUltimoToken);
                    abrirMainActivity();
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

    public void abrirMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
