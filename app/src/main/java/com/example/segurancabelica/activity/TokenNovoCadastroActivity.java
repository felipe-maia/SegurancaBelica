package com.example.segurancabelica.activity;

import android.os.Bundle;
import android.util.Log;
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
        ultimoToken = findViewById(R.id.textUltimoToken);
        ultimoTokenNivel = findViewById(R.id.textUltimoNivel);
        ultimoTokenStatus = findViewById(R.id.textUltimoStatus);
        codigoCartao = findViewById(R.id.editCodigoCartao);

        btGerarToken = findViewById(R.id.btGerarToken);
        verificaToken();

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


    public void gerarToken() {
        int x = new Random().nextInt(9999);
        tokenGerado.setText(String.valueOf(x));
        token.setToken(x);
        token.setStatus(false);

        DatabaseReference tokenDB = reference.child("tokenUser");
        tokenDB.push().setValue(token);
    }

    public void verificaToken() {

        DatabaseReference tokenDB = reference.child("tokenUser"); // referencia da tabela
        Query queryUltimoToken = tokenDB.orderByKey().limitToLast(1);   //.orderByChild("status").equalTo(true);

        queryUltimoToken.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    buscaUltimoToken = postSnapshot.getValue(TokenUsuarioNovo.class);
                    buscaUltimoToken.setKey(postSnapshot.getKey());
                    ultimoToken.setText(String.valueOf(buscaUltimoToken.getToken()));
                    ultimoTokenNivel.setText(buscaUltimoToken.getNivelPermissao());
                }
                if (buscaUltimoToken.isStatus()) {
                    ultimoTokenStatus.setText("Token já utilizado");
                    btGerarToken.setEnabled(true);
                    Log.i("teste token", "token: " + buscaUltimoToken.getKey());
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
}
