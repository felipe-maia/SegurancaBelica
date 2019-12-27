package com.example.segurancabelica.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.segurancabelica.R;
import com.example.segurancabelica.config.ConfigFirebase;
import com.example.segurancabelica.model.TokenUsuarioNovo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class TokenNovoCadastroActivity extends AppCompatActivity {

    private DatabaseReference reference = ConfigFirebase.getDataBase();
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private TextView tokenGerado;
    private TokenUsuarioNovo token, ultimoToken;
    private Button btGerarToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token_novo_cadastro);


        token = new TokenUsuarioNovo();
        ultimoToken = new TokenUsuarioNovo();

        tokenGerado = findViewById(R.id.textTokenGerado);
        radioGroup = findViewById(R.id.radioGroup);

        btGerarToken = findViewById(R.id.btGerarToken);

        btGerarToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int radioId = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(radioId);
                if (radioButton.getText().toString().equals("Nível Padrão")) {
                    token.setNivelPermissao(1);
                } else {
                    token.setNivelPermissao(2);
                }
                gerarToken();
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
        Query queryUltimoToken = tokenDB.orderByKey().limitToLast(1);

        queryUltimoToken.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ultimoToken = (TokenUsuarioNovo) dataSnapshot.getValue();
                if (ultimoToken.isStatus()){
                    btGerarToken.setEnabled(true);
                }else {
                    btGerarToken.setEnabled(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
