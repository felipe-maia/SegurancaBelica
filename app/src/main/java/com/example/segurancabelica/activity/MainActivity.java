package com.example.segurancabelica.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.segurancabelica.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth user = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseReference usuarios = reference.child("usuarios"); // referencia da tabela

        //pesquisas
        //Query usuarioPesquisa = usuarios.orderByChild("nome").equalTo("Felipe");
        //limitando primeiros 3 usuarios
        //Query usuarioPesquisa = usuarios.orderByKey().limitToFirst(3);

        Query usuarioPesquisa = usuarios.orderByChild ("nome").startAt("F").endAt("G" + "/utf8ff");
        //Query usuarioPesquisa = usuarios.orderByChild ("nome").startAt("L");




        usuarioPesquisa.addValueEventListener(new ValueEventListener() { // listener da tabela referenciada
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) { // capta qualquer alteração no DB e atualiza app

                Log.i("pesquisa","usuario "+ dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /*//identificador unico
        Usuario usuario = new Usuario();
        usuario.setNome("Fernanda");
        usuario.setPosto("1Ten");
        usuario.setPermissao("Default");
        usuarios.push().setValue(usuario);
         */


        /*
        //deslogar usuario
        user.signOut();

        //login de usuario
        user.signInWithEmailAndPassword("fe_switch@hotmail.com", "123456").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.i("Login", "Login realizado com sucesso!");

                }else{
                    Log.i("Login", "Erro ao efetuar login!");

                }
            }
        });


        if (user.getCurrentUser()!=null){ // verifica se esta autenticado
            Toast.makeText(getApplicationContext(), "Bem vindo de volta " + user.getCurrentUser().getEmail() + "!", Toast.LENGTH_LONG).show();
       }else{
            Toast.makeText(getApplicationContext(), "Usuario não está logado " , Toast.LENGTH_LONG).show();

        }
        /*
         */



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
