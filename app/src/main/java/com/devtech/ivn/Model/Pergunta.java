package com.devtech.ivn.Model;

public class Pergunta {

    private String id;
    private String msg;
    private String tipo;
    private boolean respondida;

    public Pergunta() {
    }

    public Pergunta(String id, String msg, String tipo, boolean respondida) {
        this.id = id;
        this.msg = msg;
        this.tipo = tipo;
        this.respondida = respondida;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public boolean isRespondida() {
        return respondida;
    }

    public void setRespondida(boolean respondida) {
        this.respondida = respondida;
    }
}
