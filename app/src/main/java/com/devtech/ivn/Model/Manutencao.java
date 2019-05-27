package com.devtech.ivn.Model;

public class Manutencao {
    private String key;
    private boolean ativo;

    public Manutencao() {
    }

    public Manutencao(String key, boolean ativo) {
        this.key = key;
        this.ativo = ativo;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}
