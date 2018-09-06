package com.timiowoturo.oluwatimiowoturo.quickno.Models;

public class UserRating {
    private String uid;
    private double rating;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public UserRating(){

    }

    public UserRating(String uid, double rating){
        this.uid = uid;
        this.rating = rating;
    }
}
