package com.andela.rainmekka.mymoviedb;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.andela.rainmekka.mymoviedb.rest.NetworkOkhttpUtil;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.andela.rainmekka.mymoviedb.MovieProviderContract.BASE_CONTENT_URI;
import static com.andela.rainmekka.mymoviedb.MovieProviderContract.FavoriteMoviesTable.PATH_TASKS;



/**
 * Created by Oluleke on 5/22/2017.
 */

public class MovieCursorAdapter extends SimpleCursorAdapter {

    private Context mContext;
    private Context appContext;
    private int layout;
    private Cursor cr;
    private final LayoutInflater inflater;
    private int flgs;
    private int x;
    private MovieClass mclass;
    private int fav_count;
    private SharedPreferences sharedpref;

    final static String MOVIEDB_BASE_URL = "https://api.themoviedb.org/3/movie/";

    final static String PARAM_QUERY = "api_key";

    public MovieCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to,int flags)
    {
        super(context, layout, c, from, to);
        this.layout = layout;
        this.mContext = context;
        this.inflater = LayoutInflater.from(context);
        this.cr = c;
        this.flgs = flags;

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent)
    {
        return inflater.inflate(layout, null);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor)
    {
        super.bindView(view, context, cursor);

        LinearLayout parentView = (LinearLayout) view.findViewById(R.id.listview_item_container);
        TextView textview = (TextView) view.findViewById(R.id.movie_entry);


        textview.setText((cr.getString(2)));

        //textview.setId(Integer.parseInt(mclass.movie_id));

        textview.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // Use interface to send a callback to the

                //Toast.makeText(context, "TextView clicked", Toast.LENGTH_SHORT).show();

                Class destActivity = MovieDetails.class;

                Intent intent = new Intent(context, destActivity);

                TextView mtctv = (TextView) view;

                String original_title = mtctv.getText().toString();
                String movie_id="popular";
                cr.moveToFirst();
                while(!cr.isAfterLast()){
                    String x5 = cr.getString(2);

                    if (cr.getString(2).equalsIgnoreCase(original_title)) {
                        movie_id = cr.getString(1);//ToDO..Save movieID in invisble TextBox and retrive with position
                        cr.moveToLast();
                        cr.moveToNext();
                    }else{
                        cr.moveToNext();
                    }
                }

                Uri mobie_item_uri = Uri.parse(MOVIEDB_BASE_URL).buildUpon()
                        .appendPath(movie_id)
                        .appendQueryParameter(PARAM_QUERY, context.getString(R.string.api_key))
                        .build();


                NetworkOkhttpUtil getMovieData = new NetworkOkhttpUtil();

                try {
                    JSONObject response = getMovieData.execute(mobie_item_uri.toString()).get();
                    //JSONArray movieItemArray = response.getJSONArray("results");


                    // Extract data from json and store into ArrayList as class objects
                    //for (int i = 0; i < response.length(); i++) {
                        //JSONObject json_movie_item_data = response.getJSONObject(i);
                        mclass = new MovieClass();
                        //Add MovieData to Moviedata List
                        mclass.adultGenre = response.getString("adult");
                        mclass.poster_path = response.getString("poster_path");
                        mclass.original_title = response.getString("original_title");
                        mclass.overview = response.getString("overview");
                        mclass.release_date = response.getString("release_date");
                        mclass.vote_average = response.getString("vote_average");
                        mclass.movie_id = movie_id;
                     //}

                } catch(@NonNull JSONException | InterruptedException | ExecutionException e){
                    e.printStackTrace();
                }

                intent.putExtra("MovieClass", mclass);
               // intent.putExtra("layout_view", context);

                mContext.startActivity(intent);
            }
        });

        parentView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // Use interface to send a callback to the activity
                Toast.makeText(context, "List item clicked", Toast.LENGTH_SHORT).show();
            }
        });

    }
//    public View getView(int position, View convertView, ViewGroup parent) {
//        TextView textView;
//        if (convertView == null) {
//            // if it's not recycled, initialize some attributes
//            textView = new TextView(mContext);
//        } else {
//            textView = (TextView) convertView;
//        }
//
//        //textView.setText(madapter.get(position).original_title);
//
//        String original_title = textView.getText().toString();
//        String movie_id;
//        cr.moveToFirst();
//        while(!cr.isLast()){
//            if (cr.getString(2).equalsIgnoreCase(original_title)){
//                movie_id = cr.getString(1);//ToDO..Save movieID in invisble TextBox and retrive with position
//
//                for (int number = 0; number < madapter.size(); number++) {
//
//                    if (madapter.get(number).movie_id.equalsIgnoreCase(movie_id)){
//                        mclass = madapter.get(number);
//                        break;
//                    }
//                }
//
//                break;
//            }else{
//                cr.moveToNext();
//            }
//        }
//
//        //Include switch for backup path here
//
//
//        textView.setTextSize(25);
//
//        textView.getText();
//
//        final int pst = position;
//
//        textView.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//
//                Class destActivity = MovieDetails.class;
//
//                Intent intent = new Intent(mContext, destActivity);
//
//                intent.putExtra("MovieClass", mclass);
//
//                mContext.startActivity(intent);
//            }
//        });
//
//        return textView;
//    }
}
