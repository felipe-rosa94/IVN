package com.devtech.ivn.Model;

public class Membros {

    private String id;
    private String descricao;
    private int dia;
    private String mes;

    public Membros() {
    }

    public Membros(String id, String descricao, int dia, String mes) {
        this.id = id;
        this.descricao = descricao;
        this.dia = dia;
        this.mes = mes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }
}
