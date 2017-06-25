package com.andela.rainmekka.mymoviedb;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.andela.rainmekka.mymoviedb.rest.NetworkOkhttpUtil;
import com.bumptech.glide.Glide;
import com.andela.rainmekka.mymoviedb.MovieProviderContract.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import okhttp3.OkHttpClient;

import android.widget.AdapterView.OnItemSelectedListener;

import static com.andela.rainmekka.mymoviedb.MovieProviderContract.BASE_CONTENT_URI;
import static com.andela.rainmekka.mymoviedb.MovieProviderContract.FavoriteMoviesTable.PATH_TASKS;


public class MovieDetails extends Activity implements OnItemSelectedListener {

    private String mMovie;
    private TextView mMovieDisplay;
    private TextView mOverviewDisplay;
    private ImageView mMoviePosterView;
    private TextView mRelease_Date;
    private TextView mVote_Average;
    private TextView mReview_listTv;
    private TextView mTrailer_ListTv;

    private String imgUrl;
    private String mOverview;
    private String release_Date;
    private String vote_average;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    MovieClass mymovieobject;
    private ArrayList<TrailerClass> trailerClassList = new ArrayList<>();
    private ArrayList<ReviewClass> reviewClassList = new ArrayList<>();

    OkHttpClient client = new OkHttpClient();
    public String url = "https://reqres.in/api/users/2";

    Context mContext;

    private boolean userIsInteracting ;

    int record_count;

    String reviewUrl;

    Button btn_fave;

    Spinner mTrailer_List_spinner,mReview_list_spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_movie_details);



        mMovieDisplay = (TextView) findViewById(R.id.tv_movie_title);
        mOverviewDisplay = (TextView)findViewById(R.id.tv_overview);
        mRelease_Date = (TextView)findViewById(R.id.tv_release_date);
        mVote_Average = (TextView)findViewById(R.id.tv_vote_average);


        mTrailer_List_spinner = (Spinner) findViewById(R.id.spinner_trailerlist);
        mReview_list_spinner = (Spinner)findViewById(R.id.spinner_review);

        btn_fave =  (Button)  findViewById(R.id.btn_favorite);


        mMoviePosterView = new ImageView(MovieDetails.this);
        mMoviePosterView = (ImageView) findViewById(R.id.iv_movie);

        mMoviePosterView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        //spinnerCount = this.m

       Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra("MovieClass")) {

                //get context

                if (intentThatStartedThisActivity.hasExtra("layout_view")){
                    mContext = getIntent().getParcelableExtra("layout_view");
                }

                mymovieobject = getIntent().getParcelableExtra("MovieClass");
                mMovie = mymovieobject.original_title;
                mOverview = mymovieobject.overview;
                release_Date = getString(R.string.release_date) + mymovieobject.release_date;
                vote_average = getString(R.string.vote_average) + mymovieobject.vote_average;
                mMovieDisplay.setText(mMovie);
                mOverviewDisplay.setText(mOverview);
                mRelease_Date.setText(release_Date);
                mVote_Average.setText(vote_average);
                mMovieDisplay.setTypeface(null, Typeface.BOLD);
                imgUrl = getString(R.string.image_base_url) +  getString(R.string.image_base_dimen) +
                        mymovieobject.poster_path;
                Glide.with(this)
                        .load(imgUrl)
                        .into(mMoviePosterView);

                //get reviews and trailer objects

                String baseUrl = getString(R.string.base_url);
                reviewUrl = baseUrl+mymovieobject.movie_id+"/reviews?api_key="+
                        getString(R.string.api_key);
                String trailerUrl = baseUrl+mymovieobject.movie_id+"/videos?api_key="+
                        getString(R.string.api_key);



                NetworkOkhttpUtil getTrailerData = new NetworkOkhttpUtil();
                NetworkOkhttpUtil getReviewData = new NetworkOkhttpUtil();

                try {
                    JSONObject response = getTrailerData.execute(trailerUrl).get();
                    JSONArray trailerArray = response.getJSONArray("results");

                    JSONObject response2 = getReviewData.execute(reviewUrl).get();
                    JSONArray reviewArray = response2.getJSONArray("results");

                    // Extract data from json and store into ArrayList as class objects

                    List<String> trailer_name_list = new ArrayList<String>();
                    List<String> review_OrigTitle_list = new ArrayList<String>();

                    for (int i = 0; i < trailerArray.length(); i++) {
                        JSONObject json_trailer_data = trailerArray.getJSONObject(i);
                        TrailerClass trailerClass = new TrailerClass();
                        //Add MovieData to Moviedata List
                        trailerClass.name = json_trailer_data.getString("name");
                        trailerClass.id = json_trailer_data.getString("id");
                        trailerClass.iso_639_1 = json_trailer_data.getString("iso_639_1");
                        trailerClass.iso_3166_1 = json_trailer_data.getString("iso_3166_1");
                        trailerClass.site = json_trailer_data.getString("site");
                        trailerClass.size = json_trailer_data.getString("size");
                        trailerClass.type = json_trailer_data.getString("type");
                        trailerClass.movie_id = mymovieobject.movie_id;

                        trailer_name_list.add(json_trailer_data.getString("name"));

                        trailerClassList.add(trailerClass);
                    }
                    for (int i = 0; i < reviewArray.length(); i++) {
                        JSONObject json_review_data = reviewArray.getJSONObject(i);
                        ReviewClass reviewClass = new ReviewClass();

                        reviewClass.author = json_review_data.getString("author");
                        reviewClass.content = json_review_data.getString("content");
                        reviewClass.id = json_review_data.getString("id");
                        reviewClass.url = json_review_data.getString("url");
                        reviewClass.movie_id = mymovieobject.movie_id;

                        String strBuffer = reviewClass.author + "'s Review : ";

                        review_OrigTitle_list.add(strBuffer);


                        reviewClassList.add(reviewClass);
                    }

                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                            android.R.layout.simple_spinner_item, review_OrigTitle_list);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    mReview_list_spinner.setAdapter(dataAdapter);

                    mReview_list_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parentView, View selectedItemView,
                                                   int position, long id) {

                            if (userIsInteracting) {

                                Class destActivity = review_details.class;

                                Intent intent = new Intent(getBaseContext(), destActivity);

                                intent.putExtra("ReviewClass", reviewClassList.get(position));

                                getBaseContext().startActivity(intent);
                            }

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parentView) {
                            // your code here
                        }

                    });

                    ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this,
                            android.R.layout.simple_spinner_item, trailer_name_list);
                    dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    mTrailer_List_spinner.setAdapter(dataAdapter2);

                    mTrailer_List_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parentView, View selectedItemView,
                                                   int position, long id) {

                            if (userIsInteracting) {

                                Class destActivity = trailer_details.class;

                                Intent intent = new Intent(getBaseContext(), destActivity);

                                intent.putExtra("TrailerClass", trailerClassList.get(position));

                                getBaseContext().startActivity(intent);
                            }

                        }
                        //

                        @Override
                        public void onNothingSelected(AdapterView<?> parentView) {
                            // your code here
                        }

                    });

                    //Handle favoritesn button

                    btn_fave =  (Button)  findViewById(R.id.btn_favorite);

                    Cursor mCursor= null;
                    String[] mSelectionArgs ={""} ;
                    mSelectionArgs[0] =mymovieobject.movie_id;

                    Uri movies = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TASKS).appendPath("movieitem")
                            .build();
                    try {

                        mCursor= getContentResolver().query(movies ,null,
                                FavoriteMoviesTable.COLUMN_MOVIE_ID,
                                mSelectionArgs, null);

                        if(mCursor.getCount()>0){
                            btn_fave.setEnabled(false);
                            btn_fave.setText("Fav'ed!");
                            Toast.makeText(getBaseContext(), mCursor.toString(), Toast.LENGTH_LONG).show();
                            mCursor.close();
                        }else{
                            //addMovieToDB();
                        }
                    }catch(Exception e){
                        android.util.Log.getStackTraceString(e);
                        //addMovieToDB();
                    }
                } catch(@NonNull  JSONException | InterruptedException | ExecutionException e){
                    e.printStackTrace();
                }

            }
        }
    }

    public void addToFavorites(View view){
        //add moviedbobject to favorites
        addMovieToDB();
    }
    public void addMovieToDB(){

        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieProviderContract.FavoriteMoviesTable.COLUMN_MOVIE_ID,
                mymovieobject.movie_id);
        contentValues.put(MovieProviderContract.FavoriteMoviesTable.COLUMN_ORIGINAL_TITLE,
                mymovieobject.original_title);
// place try catch here
        Uri uri = getContentResolver().insert(MovieProviderContract.FavoriteMoviesTable.CONTENT_URI, contentValues);
        if(uri != null) {

            btn_fave.setEnabled(false);
            btn_fave.setText("Fav'ed!" );
            Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_LONG).show();
        }else{
            btn_fave.setEnabled(true);
            btn_fave.setText("Favorite?" );
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

       String msg;
        switch (parent.getId()) {
            case R.id.spinner_review:
                msg = reviewClassList.get(position).url;
            case R.id.spinner_trailerlist:
                msg = trailerClassList.get(position).site;
            default:
                msg = " Invalid Selection";
        }

//        Class destActivity = MovieDetails.class;
//
//        Intent intent = new Intent(mContext, destActivity);
//
//        intent.putExtra("MovieClass", mDataArray.get(pst));
//
//        mContext.startActivity(intent);

        Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //do nothing
    }
    @Override
    public void onUserInteraction(){
        super.onUserInteraction();
        userIsInteracting = true;
    }
}

