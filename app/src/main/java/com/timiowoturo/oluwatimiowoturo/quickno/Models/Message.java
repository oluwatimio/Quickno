package com.timiowoturo.oluwatimiowoturo.quickno.Models;

public class Message {
    public String userName;
    public String uid;
    public String message;

    public Message(){

    }

    public Message(String userName, String uid, String message) {
        this.userName = userName;
        this.uid = uid;
        this.message = message;
    }
}
