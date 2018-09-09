package com.timiowoturo.oluwatimiowoturo.quickno.Utils;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DirectionsDownloader {
    String url;
    String data;
    OkHttpClient client = new OkHttpClient();
    public DirectionsDownloader(String url){
        this.url = url;
        this.data = download();
    }

    public String download() {


        try{
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = client.newCall(request).execute();


            return response.body().string();
        }catch (Exception e){

        }

        return null;
    }

    public String getData(){
        return this.data;
    }
}
