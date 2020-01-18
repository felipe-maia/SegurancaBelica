package com.example.segurancabelica.model;

public class DisparoAlarme {

    private boolean Alarme; //true = disparado, false = nao disparado;
    private int data;
    private int hora;
    private int min;
    private int seg;

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

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getSeg() {
        return seg;
    }

    public void setSeg(int seg) {
        this.seg = seg;
    }
}
