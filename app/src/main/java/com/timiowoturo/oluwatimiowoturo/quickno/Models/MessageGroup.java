package com.timiowoturo.oluwatimiowoturo.quickno.Models;

import java.util.ArrayList;

public class MessageGroup {

    public String requesting;
    public String receiving;
    public ArrayList<Message> messages;

    public MessageGroup(){

    }

    public MessageGroup(String requesting, String receiving, ArrayList<Message> messages) {
        this.requesting = requesting;
        this.receiving = receiving;
        this.messages = messages;
    }
}
