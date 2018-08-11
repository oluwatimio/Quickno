package com.timiowoturo.oluwatimiowoturo.quickno.Models;

import android.os.Parcel;
import android.os.Parcelable;

// Subjects are called Quickno's
public class Quickno implements Parcelable {
    private String tag;

    public Quickno(){

    }
    public Quickno(String tag){
        this.tag = tag;
    }
    public String getTag() {
        return tag;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.tag);
    }

    protected Quickno(Parcel in) {
        this.tag = in.readString();
    }

    public static final Parcelable.Creator<Quickno> CREATOR = new Parcelable.Creator<Quickno>() {
        @Override
        public Quickno createFromParcel(Parcel source) {
            return new Quickno(source);
        }

        @Override
        public Quickno[] newArray(int size) {
            return new Quickno[size];
        }
    };
}
