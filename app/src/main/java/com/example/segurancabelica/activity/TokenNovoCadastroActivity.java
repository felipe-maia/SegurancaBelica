package com.example.segurancabelica.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.segurancabelica.R;
import com.example.segurancabelica.config.ConfigFirebase;
import com.example.segurancabelica.model.TokenUsuarioNovo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class TokenNovoCadastroActivity extends AppCompatActivity {

    private DatabaseReference reference = ConfigFirebase.getDataBase();
    private DatabaseReference tokenDB = reference.child("tokenUser");
    private ValueEventListener valueEventListenerTokenDB;
    private EditText codigoCartao;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private TextView tokenGerado, ultimoToken, ultimoTokenStatus, ultimoTokenNivel;
    private TokenUsuarioNovo token, buscaUltimoToken;
    private Button btGerarToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token_novo_cadastro);

        token = new TokenUsuarioNovo();
        buscaUltimoToken = new TokenUsuarioNovo();

        tokenGerado = findViewById(R.id.textTokenGerado);
        radioGroup = findViewById(R.id.radioGroup);
        codigoCartao = findViewById(R.id.editCodigoCartao);

        ultimoToken = findViewById(R.id.textUltimoToken);
        ultimoTokenNivel = findViewById(R.id.textUltimoNivel);
        ultimoTokenStatus = findViewById(R.id.textUltimoStatus);

        btGerarToken = findViewById(R.id.btGerarToken);
        btGerarToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String textCodigoCartao = codigoCartao.getText().toString();

                if (textCodigoCartao.isEmpty()) {
                    Toast.makeText(TokenNovoCadastroActivity.this, "Preencha o campo código do cartão!", Toast.LENGTH_SHORT).show();
                }else {
                    token.setCodigoCartao(textCodigoCartao);
                    int radioId = radioGroup.getCheckedRadioButtonId();
                    radioButton = findViewById(radioId);
                    if (radioButton.getText().toString().equals("Nível Padrão")) {
                        token.setNivelPermissao("Nível Padrão");
                    } else {
                        token.setNivelPermissao("Nível ADM");
                    }
                    gerarToken();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        verificaUltimoToken();
    }

    public void gerarToken() {
        int x = new Random().nextInt(9999);
        token.setToken(String.valueOf(x));
        token.setStatus(false);
        tokenGerado.setText(token.getToken());

        tokenDB.push().setValue(token);
    }

    public void verificaUltimoToken() {

        Query queryUltimoToken = tokenDB.orderByKey().limitToLast(1);

        valueEventListenerTokenDB = queryUltimoToken.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    buscaUltimoToken = postSnapshot.getValue(TokenUsuarioNovo.class);
                    buscaUltimoToken.setKey(postSnapshot.getKey());
                    ultimoToken.setText(buscaUltimoToken.getToken());
                    ultimoTokenNivel.setText(buscaUltimoToken.getNivelPermissao());
                }
                if (buscaUltimoToken.isStatus()) {
                    ultimoTokenStatus.setText("Token já utilizado");
                    btGerarToken.setEnabled(true);
                }else {
                    btGerarToken.setEnabled(false);
                    ultimoTokenStatus.setText("Token não utilizado, não poderá gerar novo token");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        tokenDB.removeEventListener(valueEventListenerTokenDB);
    }
}
