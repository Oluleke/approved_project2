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
import java.util.List;
import java.util.Scanner;

/**
 * Created by yemi on 17/04/2017.
 */

public class FetchMovieTask extends AsyncTask<URL, Integer, List<MovieClass>> {
    private static final String TAG = "FetchMyDataTask";
    private String movieDBSearchResults = null;
    private URL curURL;

    private Context context;
    private AsyncHelper.AsyncTaskCompleteListener<MovieClass> listener;

    private ProgressBar pbLoading;

    public FetchMovieTask(Context ctx,
                          AsyncHelper.AsyncTaskCompleteListener<MovieClass> listener) {
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
    protected  List<MovieClass> doInBackground(URL... urls) {
        List<MovieClass> mFetchDataArrayList;
        mFetchDataArrayList = new ArrayList<>();

        try {
            movieDBSearchResults = getResponseFromHttpURL(urls[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {

            JSONObject jObj = new JSONObject(movieDBSearchResults);
            JSONArray mMovieJsonArray = jObj.getJSONArray("results");
            // Extract data from json and store into ArrayList as class objects
            for (int i = 0; i < mMovieJsonArray.length(); i++) {
                JSONObject json_movie_data = mMovieJsonArray.getJSONObject(i);
                MovieClass movieData = new MovieClass();
                movieData.adultGenre = json_movie_data.getString("adult");
                movieData.poster_path = json_movie_data.getString("poster_path");
                movieData.original_title = json_movie_data.getString("original_title");
                movieData.overview = json_movie_data.getString("overview");
                movieData.release_date = json_movie_data.getString("release_date");
                movieData.vote_average = json_movie_data.getString("vote_average");
                movieData.movie_id = json_movie_data.get("id").toString();

                //Add MovieData to Moviedata List
                mFetchDataArrayList.add(movieData);
            }


        } catch (JSONException e) {
            //Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        return mFetchDataArrayList;
    }

    @Override
    protected void onPostExecute(List <MovieClass> MovieClasses) {
        super.onPostExecute(MovieClasses);
        listener.onTaskComplete(MovieClasses);
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