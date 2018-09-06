package com.timiowoturo.oluwatimiowoturo.quickno.Models;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class User implements Parcelable {
    private String name;
    private String uid;
    private ArrayList<Quickno> quicknos;
    public UserRating rating;
    public String photoUrl;

    public User(){

    }
    public User(String name, String uid, ArrayList<Quickno> quicknos, String url, UserRating rating) {
        this.name = name;
        this.uid = uid;
        this.quicknos = quicknos;
        this.rating = rating;
        this.photoUrl = url;
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.uid);
        dest.writeList(this.quicknos);
    }

    protected User(Parcel in) {
        this.name = in.readString();
        this.uid = in.readString();
        this.quicknos = new ArrayList<Quickno>();
        in.readList(this.quicknos, Quickno.class.getClassLoader());
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
