package com.timiowoturo.oluwatimiowoturo.quickno.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Locator implements Parcelable {
    String uid;
    Double lat;
    Double lng;

    public Locator(){

    }
    public Locator(String uid, Double lat, Double lng) {
        this.uid = uid;
        this.lat = lat;
        this.lng = lng;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.uid);
        dest.writeValue(this.lat);
        dest.writeValue(this.lng);
    }

    protected Locator(Parcel in) {
        this.uid = in.readString();
        this.lat = (Double) in.readValue(Double.class.getClassLoader());
        this.lng = (Double) in.readValue(Double.class.getClassLoader());
    }

    public static final Parcelable.Creator<Locator> CREATOR = new Parcelable.Creator<Locator>() {
        @Override
        public Locator createFromParcel(Parcel source) {
            return new Locator(source);
        }

        @Override
        public Locator[] newArray(int size) {
            return new Locator[size];
        }
    };
}
