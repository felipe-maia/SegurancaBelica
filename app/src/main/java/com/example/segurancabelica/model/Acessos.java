package com.example.segurancabelica.model;


import java.util.Calendar;

public class Acessos {

    private String codigoCartao; //codigo do cartao do usuario
    private int data;
    private int hora;
    private boolean statusAlarme; //Ativo ou Inativo
    private Calendar dataHora = Calendar.getInstance();

    public String getCodigoCartao() {
        return codigoCartao;
    }

    public void setCodigoCartao(String codigoCartao) {
        this.codigoCartao = codigoCartao;
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

    public boolean isStatusAlarme() {
        return statusAlarme;
    }

    public void setStatusAlarme(boolean statusAlarme) {
        this.statusAlarme = statusAlarme;
    }

    public Calendar getDataHora() {
        return dataHora;
    }

    public void setDataHora(Calendar dataHora) {
        this.dataHora = dataHora;
    }
}