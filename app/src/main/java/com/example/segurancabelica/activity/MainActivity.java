package com.example.segurancabelica.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.segurancabelica.R;
import com.example.segurancabelica.config.ConfigFirebase;
import com.example.segurancabelica.helper.Base64Custom;
import com.example.segurancabelica.model.Acessos;
import com.example.segurancabelica.model.DisparoAlarme;
import com.example.segurancabelica.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;


public class MainActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao = ConfigFirebase.getAutenticacao();
    private DatabaseReference reference = ConfigFirebase.getDataBase();
    private DatabaseReference refAcessos = reference.child("Acessos");
    private DatabaseReference refDisparoAlarme = reference.child("DisparoAlarme");
    private DatabaseReference refUser = reference.child("usuarios");

    private ValueEventListener valueEventListenerAcesso, valueEventListenerDisparo;
    private Usuario user = new Usuario();

    private Button btUsuarios, btRelatorios, btLogout;
    private TextView textStatusAlarme, textNome, textPosto;
    private boolean statusAcesso, statusDisparo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        verificaUsuario();
        recuperaToken();

        setContentView(R.layout.activity_main);
        textStatusAlarme = findViewById(R.id.textStatusAlarme);
        textNome = findViewById(R.id.textNome);
        textPosto = findViewById(R.id.textPosto);

        btUsuarios = findViewById(R.id.btActivityUsuarios);
        btUsuarios.setEnabled(false);
        btUsuarios.setOnClickListener(view -> abrirUsuarios());

        btRelatorios = findViewById(R.id.btRelatorio);
        btRelatorios.setOnClickListener(view -> abrirRelatorio());

        btLogout = findViewById(R.id.btLogout);
        btLogout.setOnClickListener(v -> {
            autenticacao.signOut();
            sair();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        atualizaStatusAlarme();
    }

    public void recuperaToken() { //token para envio de notificações
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w("token", "getInstanceId failed", task.getException());
                        return;
                    }
                    // Get new Instance ID token
                    String token = task.getResult().getToken();

                    // Log and toast
                    Log.d("token", token);
                    //Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
                });
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
    }

    public void verificaUsuario() {

        if (autenticacao.getCurrentUser() != null) {
            String id = Base64Custom.codificarBase64(autenticacao.getCurrentUser().getEmail());

            Query queryUser = refUser.orderByKey().equalTo(id);
            queryUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            user = postSnapshot.getValue(Usuario.class);
                            textNome.setText(user.getNome());
                            textPosto.setText(user.getPosto());

                            if (user.getPermissao().equals("Nível ADM")) {
                                btUsuarios.setEnabled(true);
                            }
                        }
                        Toast.makeText(getApplicationContext(), "Bem vindo " + user.getNome() + "!", Toast.LENGTH_LONG).show();

                    } else {
                        excluirUser();
                        Toast.makeText(getApplicationContext(), "Usuário descadastrado", Toast.LENGTH_LONG).show();
                        redirecionaActivityInicial();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        } else {
            redirecionaActivityInicial();
        }

    }

    public void excluirUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.delete()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("EXCLUIUSER", "User account deleted.");
                    }
                });
    }

    public void redirecionaActivityInicial() {
        startActivity(new Intent(this, InicialActivity.class));
        finish();
    }

    public void atualizaStatusAlarme() {
        Query queryUltimoDisparo = refDisparoAlarme.orderByKey().limitToLast(1);
        valueEventListenerDisparo = queryUltimoDisparo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    DisparoAlarme disparoAlarme = postSnapshot.getValue(DisparoAlarme.class);
                    statusDisparo = disparoAlarme.isAlarme();
                    defineStatus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Query queryUltimoAcesso = refAcessos.orderByKey().limitToLast(1);
        valueEventListenerAcesso = queryUltimoAcesso.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Acessos acesso = postSnapshot.getValue(Acessos.class);
                    statusAcesso = acesso.isStatusAlarme();
                    defineStatus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void defineStatus() {
        String statusAlarme;
        if (statusAcesso) {
            if (statusDisparo) {
                statusAlarme = "Status Alarme: Atenção Alarme Disparado!";
            } else {
                statusAlarme = "Status Alarme: Ativado";
            }
        } else {
            statusAlarme = "Status Alarme: Desativado";
        }
        textStatusAlarme.setText(statusAlarme);
    }

    public void sair() {
        autenticacao.signOut();
        startActivity(new Intent(this, InicialActivity.class));
        finish();
    }

    public void abrirRelatorio() {
        startActivity(new Intent(this, RelatorioActivity.class));
    }

    public void abrirUsuarios() {
        startActivity(new Intent(this, UsuariosActivity.class));
    }

    @Override
    protected void onPause() {
        super.onPause();
        refAcessos.removeEventListener(valueEventListenerAcesso);
        refDisparoAlarme.removeEventListener(valueEventListenerDisparo);
    }
}
