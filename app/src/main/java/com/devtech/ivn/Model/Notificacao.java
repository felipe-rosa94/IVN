package com.devtech.ivn.Model;

public class Notificacao {
    private String titulo;
    private String descricao;
    private boolean ativo;

    public Notificacao() {
    }

    public Notificacao(String titulo, String descricao, boolean ativo) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.ativo = ativo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}
