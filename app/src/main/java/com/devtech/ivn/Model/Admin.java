package com.devtech.ivn.Model;

public class Admin {

    private String usuario;
    private String token;

    public Admin() {
    }

    public Admin(String usuario, String token) {
        this.usuario = usuario;
        this.token = token;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
