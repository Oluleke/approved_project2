package com.andela.rainmekka.mymoviedb;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andela.rainmekka.mymoviedb.rest.NetworkOkhttpUtil;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.andela.rainmekka.mymoviedb.MainActivity.MOVIEDB_BASE_URL;
import static com.andela.rainmekka.mymoviedb.MainActivity.PARAM_QUERY;



/**
 * Created by yemi on 17/04/2017.
 */

public class MyListAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<MovieClass> mDataArray = new ArrayList<>();
    MovieClass mclass;
;


    public MyListAdapter(Context c, ArrayList<MovieClass> mAdapterdata) {
        mContext = c;
        mDataArray = mAdapterdata;
    }


    public int getCount() {
        return mDataArray.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }



    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textview;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            textview = new TextView(mContext);
            //textview.setPadding(10, 2,2, 2);
        } else {
            textview = (TextView) convertView;
        }

        textview.setText(mDataArray.get(position).original_title);

        textview.setTextSize(40);

        final int pst = position;

        textview.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Uri mobie_item_uri = Uri.parse(MOVIEDB_BASE_URL).buildUpon()
                        .appendPath(mDataArray.get(pst).movie_id)
                        .appendQueryParameter(PARAM_QUERY, mContext.getString(R.string.api_key))
                        .build();


                NetworkOkhttpUtil getMovieData = new NetworkOkhttpUtil();

                try {
                    JSONObject response = getMovieData.execute(mobie_item_uri.toString()).get();
                    mclass = new MovieClass();


                    mclass.adultGenre = response.getString("adult");
                    mclass.poster_path = response.getString("poster_path");
                    mclass.original_title = response.getString("original_title");
                    mclass.overview = response.getString("overview");
                    mclass.release_date = response.getString("release_date");
                    mclass.vote_average = response.getString("vote_average");
                    mclass.movie_id = mDataArray.get(pst).movie_id;
                    //}

                } catch(@NonNull JSONException | InterruptedException | ExecutionException e){
                    e.printStackTrace();
                }
                    Class destActivity = MovieDetails.class;

                    Intent intent = new Intent(mContext, destActivity);

                    intent.putExtra("MovieClass", mclass);

                    mContext.startActivity(intent);
            }
        });

        return textview;
    }
    public void swapData(ArrayList<MovieClass> movieClasses) {
        mDataArray = movieClasses; // assign the passed-in movie list to our `mDataForAdapterArrayList`
        notifyDataSetChanged(); // this is called so that the list can refresh itself
    }
    public ArrayList<MovieClass> getAllObjects(){
        return mDataArray;
    }
}
