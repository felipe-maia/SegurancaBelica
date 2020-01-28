package com.example.segurancabelica.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.segurancabelica.R;
import com.example.segurancabelica.adapter.ListaUsuariosAdapter;
import com.example.segurancabelica.config.ConfigFirebase;
import com.example.segurancabelica.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UsuariosActivity extends AppCompatActivity {

    private DatabaseReference reference = ConfigFirebase.getDataBase();
    private DatabaseReference refUser = reference.child("usuarios");
    private List<Usuario> listaUsuarios = new ArrayList<>();
    private RecyclerView recyclerUsuarios;
    private ListaUsuariosAdapter usuariosAdapter;
    private Button btNovoUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuarios);
        recyclerUsuarios = findViewById(R.id.recyclerUsuarios);
        btNovoUsuario = findViewById(R.id.btNovoUsuario);
        btNovoUsuario.setOnClickListener(v -> abrirNovoCadastro());
    }


    @Override
    protected void onResume() {
        super.onResume();
        buscaUsuarios();
    }

    public void buscaUsuarios() {
        listaUsuarios.clear();

        refUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Usuario usuarios = postSnapshot.getValue(Usuario.class);
                    listaUsuarios.add(usuarios);
                    configAdapterRecycler();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void configAdapterRecycler() {
        //config recycler e adapter usuarios
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerUsuarios.setLayoutManager(layoutManager);
        recyclerUsuarios.setHasFixedSize(true);
        usuariosAdapter = new ListaUsuariosAdapter(listaUsuarios);
        recyclerUsuarios.setAdapter(usuariosAdapter);
    }

    public void abrirNovoCadastro() {
        startActivity(new Intent(this, TokenNovoCadastroActivity.class));
    }
}
