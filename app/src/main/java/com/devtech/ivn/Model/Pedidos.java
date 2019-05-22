package com.devtech.ivn.Model;

public class Pedidos {
    private String key;
    private String nome;
    private String descricao;
    private long data;

    public Pedidos() {
    }

    public Pedidos(String key, String nome, String descricao, long data) {
        this.key = key;
        this.nome = nome;
        this.descricao = descricao;
        this.data = data;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public long getData() {
        return data;
    }

    public void setData(long data) {
        this.data = data;
    }
}
