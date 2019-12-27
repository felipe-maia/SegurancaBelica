package com.example.segurancabelica.model;

public class TokenUsuarioNovo {

    private int token;
    private boolean status;
    private int nivelPermissao;

    public TokenUsuarioNovo() {
    }

    public int getToken() {
        return token;
    }

    public void setToken(int token) {
        this.token = token;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getNivelPermissao() {
        return nivelPermissao;
    }

    public void setNivelPermissao(int nivelPermissao) {
        this.nivelPermissao = nivelPermissao;
    }
}
