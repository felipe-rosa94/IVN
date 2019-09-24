package com.devtech.ivn.Model;

public class Album {
    private String id;
    private String nome;
    private String capa;

    public Album() {
    }

    public Album(String id, String nome, String capa) {
        this.id = id;
        this.nome = nome;
        this.capa = capa;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        if (nome == null){
            nome = "";
        }
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCapa() {
        if (capa == null){
            capa = "";
        }
        return capa;
    }

    public void setCapa(String capa) {
        this.capa = capa;
    }
}
