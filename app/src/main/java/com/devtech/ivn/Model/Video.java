package com.devtech.ivn.Model;

public class Video {

    private String link;
    private String titulo;
    private String imagem;
    private String copyright;

    public Video() {
    }

    public Video(String link, String titulo, String imagem, String copyright) {
        this.link = link;
        this.titulo = titulo;
        this.imagem = imagem;
        this.copyright = copyright;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }
}
