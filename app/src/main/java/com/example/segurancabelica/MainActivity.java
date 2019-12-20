package com.example.segurancabelica;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth userAuthentication = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (userAuthentication.getCurrentUser()!=null){
            Log.i("Authentication", "Usuario logado");
        }else{
            Log.i("Authentication", "Usuario logado");

        }



        /*   //*cria usuario autenticado email e senha
        userAuthentication.createUserWithEmailAndPassword(
                "fe_switch@hotmail.com", "123456").addOnCompleteListener(
                        MainActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.i("Cadastro", "Usuario cadastrado com sucesso!");

                }else{
                    Log.i("Cadastro", "Erro ao cadastrar usuario!");

                }
            }
        });
        /*




        /*
        DatabaseReference usuarios = reference.child("usuarios"); // referencia da tabela
        usuarios.addValueEventListener(new ValueEventListener() { // listener da tabela referenciada
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) { // capta qualquer alteração no DB e atualiza app

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Usuario user = new Usuario();
        user.setNome("Felipe");
        user.setPosto("SGT");
        user.setPermissao("ADM");
        usuarios.child("001").setValue(user); //insere objeto na tabela
        */

    }
}
