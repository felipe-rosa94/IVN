package com.devtech.ivn.Model;

public class Aviso {

    boolean ativo;
    private String titulo;
    private String msg;
    private String urlImagem;
    private boolean proporcional;

    public Aviso() {
    }

    public Aviso(boolean ativo, String titulo, String msg, String urlImagem, boolean proporcional) {
        this.ativo = ativo;
        this.titulo = titulo;
        this.msg = msg;
        this.urlImagem = urlImagem;
        this.proporcional = proporcional;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
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

    public String getMsg() {
        if (msg == null) {
            msg = "";
        }
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getUrlImagem() {
        if (urlImagem == null) {
            urlImagem = "";
        }
        return urlImagem;
    }

    public void setUrlImagem(String urlImagem) {
        this.urlImagem = urlImagem;
    }

    public boolean isProporcional() {
        return proporcional;
    }

    public void setProporcional(boolean proporcional) {
        this.proporcional = proporcional;
    }
}
