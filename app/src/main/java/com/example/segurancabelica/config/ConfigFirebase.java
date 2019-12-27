package com.example.segurancabelica.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConfigFirebase {

    private static FirebaseAuth autenticacao;
    private static DatabaseReference reference;

    //instancia firebase DB
    public static DatabaseReference getDataBase(){
        if (reference == null){
            reference = FirebaseDatabase.getInstance().getReference();
        }
        return reference;
    }

    //instancia firebase auth
    public static FirebaseAuth getAutenticacao() {

        if (autenticacao == null) {
            autenticacao = FirebaseAuth.getInstance();
        }
        return autenticacao;
    }
}
