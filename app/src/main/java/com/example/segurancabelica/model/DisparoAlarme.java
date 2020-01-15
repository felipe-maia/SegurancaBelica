package com.example.segurancabelica.model;

public class DisparoAlarme {

    private boolean Alarme; //true = disparado, false = nao disparado;
    private int data;
    private int hora;

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

    public int getHora() {
        return hora;
    }

    public void setHora(int hora) {
        this.hora = hora;
    }
}
