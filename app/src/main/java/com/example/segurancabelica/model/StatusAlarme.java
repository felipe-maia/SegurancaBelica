package com.example.segurancabelica.model;

import java.util.Date;

public class StatusAlarme {

    private String statusAlarme; //Ativo, Inativo, Disparado;
    private Date dataHora;

    public StatusAlarme() {
    }

    public String getStatusAlarme() {
        return statusAlarme;
    }

    public void setStatusAlarme(String statusAlarme) {
        statusAlarme = statusAlarme;
    }

    public Date getDataHora() {
        return dataHora;
    }

    public void setDataHora(Date dataHora) {
        this.dataHora = dataHora;
    }
}
