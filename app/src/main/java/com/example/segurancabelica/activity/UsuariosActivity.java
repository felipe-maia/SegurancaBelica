package com.example.segurancabelica.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
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
    private Usuario user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Button btNovoUsuario;
        setContentView(R.layout.activity_usuarios);
        recyclerUsuarios = findViewById(R.id.recyclerUsuarios);
        btNovoUsuario = findViewById(R.id.btNovoUsuario);
        btNovoUsuario.setOnClickListener(v -> abrirNovoCadastro());
    }

    @Override
    protected void onResume() {
        super.onResume();
        buscaUsuarios();
        swipe();
    }

    public void buscaUsuarios() {
        listaUsuarios.clear();
        refUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Usuario usuarios = postSnapshot.getValue(Usuario.class);
                    usuarios.setId(postSnapshot.getKey());
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
        recyclerUsuarios.setHasFixedSize(false);
        usuariosAdapter = new ListaUsuariosAdapter(listaUsuarios);
        recyclerUsuarios.setAdapter(usuariosAdapter);
    }

    public void swipe() {

        ItemTouchHelper.Callback itemTouch = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {

                int dragFlags = ItemTouchHelper.ACTION_STATE_IDLE;
                int swipeFlags = ItemTouchHelper.END;
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                excluirUsuario(viewHolder);
            }
        };
        new ItemTouchHelper(itemTouch).attachToRecyclerView(recyclerUsuarios);

    }

    public void excluirUsuario(RecyclerView.ViewHolder viewHolder) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Excluir Usuário");
        alertDialog.setMessage("Tem certeza que deseja excluir o usuário selecionado?");
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton("Confirmar", (dialog, which) -> {
            int p = viewHolder.getAdapterPosition();
            user = listaUsuarios.get(p);
            refUser.child(user.getId()).removeValue();
            listaUsuarios.remove(p);
            usuariosAdapter.notifyItemRemoved(p);
        });

        alertDialog.setNegativeButton("Cancelar", (dialog, which) -> {
            Toast.makeText(getApplicationContext(), "Cancelado", Toast.LENGTH_SHORT).show();
            usuariosAdapter.notifyDataSetChanged();
        });

        AlertDialog alert = alertDialog.create();
        alert.show();
    }

    public void abrirNovoCadastro() {
        startActivity(new Intent(this, TokenNovoCadastroActivity.class));
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
