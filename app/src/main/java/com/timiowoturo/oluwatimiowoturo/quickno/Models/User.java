package com.timiowoturo.oluwatimiowoturo.quickno.Models;

import java.util.ArrayList;

public class User {
    private String name;
    private String uid;
    private ArrayList<Quickno> quicknos;
    public User(String name, String uid) {
        this.name = name;
        this.uid = uid;
        this.quicknos = new ArrayList<>();
    }
    public String getName() {
        return name;
    }
    public String getUid() {
        return uid;
    }
    public ArrayList<Quickno> getQuicknos() {
        return quicknos;
    }
}
