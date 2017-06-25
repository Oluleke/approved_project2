package com.andela.rainmekka.mymoviedb;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by yemi on 17/04/2017.
 */

public class FetchReviewsTask extends AsyncTask<URL, Integer, ArrayList<ReviewClass>> {
    private static final String TAG = "FetchMyDataTask";
    private String reviewsSearchResults = null;
    private URL curURL;

    private Context context;
    private AsyncHelper.AsyncTaskCompleteListener<ReviewClass> listener;

    private ProgressBar pbLoading;

    public FetchReviewsTask(Context ctx,
                            AsyncHelper.AsyncTaskCompleteListener<ReviewClass> listener) {
        this.context = ctx;
        this.listener = listener;
        //this.curURL = srcUrl;
    }

    protected void onPreExecute() {
        //check for network connection

        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (isConnected != true){
            Toast.makeText(context, R.string.connection_errmsg, Toast.LENGTH_LONG).show();
            cancel(true);
        }else {
            pbLoading = new ProgressBar(context);
            pbLoading.setVisibility(View.VISIBLE);
        }


        super.onPreExecute();
    }

    @Override
    protected  ArrayList<ReviewClass> doInBackground(URL... urls) {
        ArrayList<ReviewClass> reviewClassList;
        reviewClassList = new ArrayList<>();

        try {
            reviewsSearchResults = getResponseFromHttpURL(urls[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {

            JSONObject jObj = new JSONObject(reviewsSearchResults);
            JSONArray reviewArray = jObj.getJSONArray("results");
            // Extract data from json and store into ArrayList as class objects
            for (int i = 0; i < reviewArray.length(); i++) {
                        JSONObject json_review_data = reviewArray.getJSONObject(i);
                        ReviewClass reviewClass = new ReviewClass();

                        reviewClass.author = json_review_data.getString("author");
                        reviewClass.content = json_review_data.getString("content");
                        reviewClass.id = json_review_data.getString("id");
                        reviewClass.url = json_review_data.getString("url");
                        //reviewClass.movie_id = mymovieobject.movie_id;


                        reviewClassList.add(reviewClass);
                    }


        } catch (JSONException e) {
            //Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        return reviewClassList;
    }

    @Override
    protected void onPostExecute(ArrayList <ReviewClass> reviewClasses) {
        super.onPostExecute(reviewClasses);
        listener.onTaskComplete(reviewClasses);
    }

    /**
     *
     * ToDO: Use OkHttps/Volley Instead of HTTPUrlConnection
     * @return
     * @throws IOException
     */
    public String getResponseFromHttpURL(URL url) throws IOException {
        HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();

        urlConn.setConnectTimeout(5000);
        urlConn.setReadTimeout(10000);

        try {
            InputStream in = urlConn.getInputStream();

            Scanner scanner = new Scanner(in);

            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();

            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConn.disconnect();
        }
    }
}