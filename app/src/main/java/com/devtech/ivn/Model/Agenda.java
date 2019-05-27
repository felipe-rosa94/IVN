package com.devtech.ivn.Model;

public class Agenda {

    private int id;
    private String key;
    private String titulo;
    private String descricao;
    private String tipo;
    private String anoMes;
    private String anoMesDia;
    private String data;
    private String urlImagem;
    private String color;

    public Agenda() {
    }

    public Agenda(int id, String key, String titulo, String descricao, String tipo, String anoMes, String anoMesDia, String data, String urlImagem, String color) {
        this.id = id;
        this.key = key;
        this.titulo = titulo;
        this.descricao = descricao;
        this.tipo = tipo;
        this.anoMes = anoMes;
        this.anoMesDia = anoMesDia;
        this.data = data;
        this.urlImagem = urlImagem;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKey() {
        if (key == null) {
            key = "";
        }
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitulo() {
        if (titulo == null) {
            titulo = "";
        }
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        if (descricao == null) {
            descricao = "";
        }
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipo() {
        if (tipo == null) {
            tipo = "";
        }
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getAnoMes() {
        if (anoMes == null) {
            anoMes = "";
        }
        return anoMes;
    }

    public void setAnoMes(String anoMes) {
        this.anoMes = anoMes;
    }

    public String getAnoMesDia() {
        if (anoMesDia == null) {
            anoMesDia = "";
        }
        return anoMesDia;
    }

    public void setAnoMesDia(String anoMesDia) {
        this.anoMesDia = anoMesDia;
    }

    public String getData() {
        if (data == null){
            data = "";
        }
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getUrlImagem() {
        return urlImagem;
    }

    public void setUrlImagem(String urlImagem) {
        this.urlImagem = urlImagem;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
