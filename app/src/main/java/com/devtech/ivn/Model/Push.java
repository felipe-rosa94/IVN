package com.devtech.ivn.Model;

public class Push {
    private String to;
    private notification notification;

    public Push() {
    }

    public Push(String to, notification notification) {
        this.to = to;
        this.notification = notification;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public notification getNotification() {
        return notification;
    }

    public void setNotification(notification notification) {
        this.notification = notification;
    }
}
