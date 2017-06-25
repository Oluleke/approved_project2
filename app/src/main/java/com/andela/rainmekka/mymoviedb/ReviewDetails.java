package com.andela.rainmekka.mymoviedb;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.andela.rainmekka.mymoviedb.fragments.ReviewsFragment;
import com.andela.rainmekka.mymoviedb.fragments.TrailersFragment;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;

public class ReviewDetails extends AppCompatActivity {

    private String mMovie;
    private TextView mMovieDisplay;
    private TextView mOverviewDisplay;
    private ImageView mMoviePosterView;
    private TextView mRelease_Date;
    private TextView mVote_Average;
    private TextView mReview_list;
    private TextView mTrailer_List;

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

    String reviewUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);



        mMovieDisplay = (TextView) findViewById(R.id.tv_movie_title);
        mOverviewDisplay = (TextView)findViewById(R.id.tv_overview);
        mRelease_Date = (TextView)findViewById(R.id.tv_release_date);
        mVote_Average = (TextView)findViewById(R.id.tv_vote_average);



        mMoviePosterView = new ImageView(ReviewDetails.this);
        mMoviePosterView = (ImageView) findViewById(R.id.iv_movie);

        mMoviePosterView.setScaleType(ImageView.ScaleType.CENTER_CROP);

//        viewPager = (ViewPager) findViewById(R.id.viewpager);
//        setupViewPager(viewPager);
//
//        tabLayout = (TabLayout) findViewById(R.id.tabs);
//        tabLayout.setupWithViewPager(viewPager);




        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra("MovieClass")) {
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



//                NetworkOkhttpUtil getTrailerData = new NetworkOkhttpUtil();
//                NetworkOkhttpUtil getReviewData = new NetworkOkhttpUtil();
//
//                try {
//                    JSONObject response = getTrailerData.execute(trailerUrl).get();
//                    JSONArray trailerArray = response.getJSONArray("results");
//
//                    JSONObject response2 = getReviewData.execute(reviewUrl).get();
//                    JSONArray reviewArray = response2.getJSONArray("results");
//
//                    // Extract data from json and store into ArrayList as class objects
//                    for (int i = 0; i < trailerArray.length(); i++) {
//                        JSONObject json_trailer_data = trailerArray.getJSONObject(i);
//                        TrailerClass trailerClass = new TrailerClass();
//                        //Add MovieData to Moviedata List
//                        trailerClass.name = json_trailer_data.getString("name");
//                        trailerClass.id = json_trailer_data.getString("id");
//                        trailerClass.iso_639_1 = json_trailer_data.getString("iso_639_1");
//                        trailerClass.iso_3166_1 = json_trailer_data.getString("iso_3166_1");
//                        trailerClass.site = json_trailer_data.getString("site");
//                        trailerClass.size = json_trailer_data.getString("size");
//                        trailerClass.type = json_trailer_data.getString("type");
//                        trailerClass.movie_id = mymovieobject.movie_id;
//
//                        trailerClassList.add(trailerClass);
//                    }
//                    for (int i = 0; i < reviewArray.length(); i++) {
//                        JSONObject json_review_data = reviewArray.getJSONObject(i);
//                        ReviewClass reviewClass = new ReviewClass();
//
//                        reviewClass.author = json_review_data.getString("author");
//                        reviewClass.content = json_review_data.getString("content");
//                        reviewClass.id = json_review_data.getString("id");
//                        reviewClass.url = json_review_data.getString("url");
//                        reviewClass.movie_id = mymovieobject.movie_id;
//
//
//                        reviewClassList.add(reviewClass);
//                    }
//
//
//
//                } catch(@NonNull  JSONException | InterruptedException | ExecutionException e){
//                    e.printStackTrace();
//                }


            }
        }
    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        Bundle bundle = new Bundle();
        //bundle.putParcelableArrayList();
        bundle.putParcelableArrayList("trailerObject",trailerClassList);
        TrailersFragment trailerfrag = new TrailersFragment();
        trailerfrag.setArguments(bundle);

        Bundle revbundle = new Bundle();
        //revbundle.putParcelableArrayList("revObject",reviewClassList);
        revbundle.putString("revQuery",reviewUrl);
        ReviewsFragment reviewfrag = new ReviewsFragment();
        reviewfrag.setArguments(revbundle);


        adapter.addFrag(trailerfrag, "Trailers");
        adapter.addFrag(reviewfrag, "Reviews");

        viewPager.setAdapter(adapter);
    }
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();


        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);

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
}

