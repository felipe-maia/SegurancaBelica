package com.example.segurancabelica.model;

import java.util.Date;

public class Acessos {

    private String codigoCartao; //codigo do cartao do usuario, quando não tiver foi disparo
    private Long dataHora;
    private String statusAlarme; //Ativo, Inativo, Disparado;


    public String getCodigoCartao() {
        return codigoCartao;
    }

    public void setCodigoCartao(String codigoCartao) {
        this.codigoCartao = codigoCartao;
    }

    public Long getDataHora() {
        return dataHora;
    }

    public void setDataHora(Long dataHora) {
        this.dataHora = dataHora;
    }

    public String getStatusAlarme() {
        return statusAlarme;
    }

    public void setStatusAlarme(String statusAlarme) {
        this.statusAlarme = statusAlarme;
    }
}
