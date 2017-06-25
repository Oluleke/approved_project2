package com.andela.rainmekka.mymoviedb.rest;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NetworkOkhttpUtil extends AsyncTask<String, Void, JSONObject> {
    OkHttpClient client = new OkHttpClient();




//    public JSONObject run(String url) throws IOException {
//        try {
//            Request request = new Request.Builder()
//                .url(url)
//                .build();
//
//
//            Response response = client.newCall(request).execute();
//            return new JSONObject(response.body().string());
//        }
//        catch(@NonNull IOException | JSONException e){
//            e.printStackTrace();
//        }
//        return null;
//    }

    @Override
    protected JSONObject doInBackground(String... params) {
        try {
            Request request = new Request.Builder()
                    .url(params[0])
                    .build();


            Response response = client.newCall(request).execute();
            return new JSONObject(response.body().string());
        }
        catch(@NonNull IOException | JSONException e){
            e.printStackTrace();
        }
        return null;

    }

//    public String run(String url) throws IOException {
//        Request request = new Request.Builder()
//                .url(url)
//                .build();
//
//        try {
//            Response response = client.newCall(request).execute();
//            return response.body().string();
//        }
//        catch(IOException ex){
//            ex.printStackTrace();
//        }
//        return url;
//    }
}