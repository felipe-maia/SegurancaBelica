package com.example.segurancabelica.model;

public class DisparoAlarme {

    private boolean Alarme; //true = disparado, false = nao disparado;
    private int data;
    private String hora;

    public DisparoAlarme() {
    }

    public boolean isAlarme() {
        return Alarme;
    }

    public void setAlarme(boolean alarme) {
        Alarme = alarme;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }
}
