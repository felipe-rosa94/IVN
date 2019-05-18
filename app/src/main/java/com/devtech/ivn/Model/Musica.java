package com.devtech.ivn.Model;

public class Musica {

    private String id;
    private String nome;
    private String descricao;
    private String urlSom;
    private String urlImagem;

    public Musica() {
    }

    public Musica(String id, String nome, String descricao, String urlSom, String urlImagem) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.urlSom = urlSom;
        this.urlImagem = urlImagem;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getUrlSom() {
        return urlSom;
    }

    public void setUrlSom(String urlSom) {
        this.urlSom = urlSom;
    }

    public String getUrlImagem() {
        return urlImagem;
    }

    public void setUrlImagem(String urlImagem) {
        this.urlImagem = urlImagem;
    }
}