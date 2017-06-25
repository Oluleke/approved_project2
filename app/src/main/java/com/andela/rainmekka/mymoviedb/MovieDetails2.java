package com.andela.rainmekka.mymoviedb;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.andela.rainmekka.mymoviedb.MovieProviderContract.FavoriteMoviesTable;
import com.andela.rainmekka.mymoviedb.fragments.ReviewsFragment;
import com.andela.rainmekka.mymoviedb.fragments.TrailersFragment;
import com.andela.rainmekka.mymoviedb.rest.NetworkOkhttpUtil;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import okhttp3.OkHttpClient;

import static com.andela.rainmekka.mymoviedb.MovieProviderContract.BASE_CONTENT_URI;
import static com.andela.rainmekka.mymoviedb.MovieProviderContract.FavoriteMoviesTable.PATH_TASKS;


public class MovieDetails2 extends FragmentActivity {

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

    int record_count;

    String reviewUrl;

    Button btn_fave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_movie_details);



        mMovieDisplay = (TextView) findViewById(R.id.tv_movie_title);
        mOverviewDisplay = (TextView)findViewById(R.id.tv_overview);
        mRelease_Date = (TextView)findViewById(R.id.tv_release_date);
        mVote_Average = (TextView)findViewById(R.id.tv_vote_average);

//        mTrailer_ListTv = (TextView)findViewById(R.id.tv_trailer_dummies);
//        mReview_listTv = (TextView)findViewById(R.id.tv_review_dummies);

        btn_fave =  (Button)  findViewById(R.id.btn_favorite);


        mMoviePosterView = new ImageView(MovieDetails2.this);
        mMoviePosterView = (ImageView) findViewById(R.id.iv_movie);

        mMoviePosterView.setScaleType(ImageView.ScaleType.CENTER_CROP);

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


                        reviewClassList.add(reviewClass);
                    }
                    //setup tabs
//
//                    viewPager = (ViewPager) findViewById(R.id.viewpager);
//                    setupViewPager(viewPager);
//
//                    tabLayout = (TabLayout) findViewById(R.id.tabs);
//                    tabLayout.setupWithViewPager(viewPager);

//                    ViewPager pager = (ViewPager) findViewById(R.id.viewpager);
//                    ReviewsFragment reviewfrag = ReviewsFragment.newInstance(reviewClassList);
//                    TrailersFragment trailerfrag = TrailersFragment.newInstance(trailerClassList);
//
//                    ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
//
//                    adapter.addFrag(reviewfrag, "Reviews");
//                    adapter.addFrag(trailerfrag, "Trailers");
//                    pager.setAdapter(adapter);

                    //Check if fav db is non-empty

//                    mReview_listTv.setText(String.valueOf(reviewClassList.size()));
//                    mTrailer_ListTv.setText(String.valueOf(trailerClassList.size()));

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

                //setup Fragments
//                ViewPager pager = (ViewPager) findViewById(R.id.viewpager);
//                pager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));


            }
        }
    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

//
      //  Bundle bundle = new Bundle();
//        //bundle.putParcelableArrayList();
//        bundle.putParcelableArrayList("trailerObject",trailerClassList);
//        TrailersFragment trailerfrag = new TrailersFragment();
//        trailerfrag.setArguments(bundle);
//
//        Bundle revbundle = new Bundle();
//        revbundle.putParcelableArrayList("revObject",reviewClassList);
//        ReviewsFragment reviewfrag = new ReviewsFragment();
//        reviewfrag.setArguments(revbundle);

        ReviewsFragment reviewfrag = ReviewsFragment.newInstance(reviewClassList);
        TrailersFragment trailerfrag = TrailersFragment.newInstance(trailerClassList);

        adapter.addFrag(reviewfrag, "Reviews");
        adapter.addFrag(trailerfrag, "Trailers");


        viewPager.setAdapter(adapter);
    }
    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();


        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            //1 - Review,2-Trailers

            switch(position){
                case 0:
                    return ReviewsFragment.newInstance(reviewClassList);
                case 1:
                    return TrailersFragment.newInstance(trailerClassList);

                default: return ReviewsFragment.newInstance(reviewClassList);
            }
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public void addToFavorites(View view){
        //add moviedbobject to favorites
        addMovieToDB();
    }
    public void addMovieToDB(){

        ContentValues contentValues = new ContentValues();
        contentValues.put(FavoriteMoviesTable.COLUMN_MOVIE_ID,
                mymovieobject.movie_id);
        contentValues.put(FavoriteMoviesTable.COLUMN_ORIGINAL_TITLE,
                mymovieobject.original_title);
// place try catch here
        Uri uri = getContentResolver().insert(FavoriteMoviesTable.CONTENT_URI, contentValues);
        if(uri != null) {

            btn_fave.setEnabled(false);
            btn_fave.setText("Fav'ed!" );
            Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_LONG).show();
        }else{
            btn_fave.setEnabled(true);
            btn_fave.setText("Favorite?" );
        }
    }

}

