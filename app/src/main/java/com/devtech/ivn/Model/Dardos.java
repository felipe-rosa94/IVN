package com.devtech.ivn.Model;

public class Dardos {

    private String id;
    private String url;
    private String titulo;
    private String descricao;
    private String nomeArquivo;
    private int width;
    private int height;

    public Dardos() {
    }

    public Dardos(String id, String url, String titulo, String descricao, String nomeArquivo, int height, int width) {
        this.id = id;
        this.url = url;
        this.titulo = titulo;
        this.descricao = descricao;
        this.nomeArquivo = nomeArquivo;
        this.height = height;
        this.width = width;
    }

    public String getId() {
        if (id == null){
            id = "";
        }
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        if (url == null){
            url = "";
        }
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitulo() {
        if (titulo == null){
            titulo = "";
        }
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        if (descricao == null){

        }
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getNomeArquivo() {
        if (nomeArquivo == null){
            nomeArquivo = "";
        }
        return nomeArquivo;
    }

    public void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
