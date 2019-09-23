package com.devtech.ivn.Model;

import java.io.Serializable;

public class Musica implements Serializable {

    private String id;
    private String nome;
    private String descricao;
    private String urlSom;
    private String urlImagem;
    private long duration;

    public Musica() {
    }

    public Musica(String id, String nome, String descricao, String urlSom, String urlImagem, long duration) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.urlSom = urlSom;
        this.urlImagem = urlImagem;
        this.duration = duration;
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

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
