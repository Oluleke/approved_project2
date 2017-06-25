package com.andela.rainmekka.mymoviedb;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Movie;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.andela.rainmekka.mymoviedb.MovieProviderContract.BASE_CONTENT_URI;
import static com.andela.rainmekka.mymoviedb.MovieProviderContract.FavoriteMoviesTable.PATH_TASKS;


public class MainActivity extends AppCompatActivity
        implements MovieAdapter.ListItemClickListener{

    final static String MOVIEDB_BASE_URL = "https://api.themoviedb.org/3/movie/";

    final static String PARAM_QUERY = "api_key";

    public static ArrayList<MovieClass> mMovieDataArrayList = new ArrayList<>();
    public static ArrayList<MovieClass> mCursorDataArrayList = new ArrayList<>();
    public static ArrayList<MovieClass> mInstanceDataArrayList = new ArrayList<>();

    private GridView gridview;

    private SharedPreferences sharedpref;
    ListView listView;
    MovieCursorAdapter adapter;

    MyListAdapter listadapter;

    private static String layout_state_popular = "popular";
    private static String layout_state_toprated = "toprated";
    private static String layout_state_favorites = "favorites";

    private Cursor c;
    String STATE_ITEMS = "movies";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ListView listView2 = (ListView) findViewById(R.id.list_movies);

        if (savedInstanceState != null){

            sharedpref = getSharedPreferences(getString(R.string.preference_file_key),MODE_PRIVATE);

            mInstanceDataArrayList = savedInstanceState.getParcelableArrayList(STATE_ITEMS);

            int curselX = savedInstanceState.getInt("scrollx");
            int curselY = savedInstanceState.getInt("scrolly");

            String layoutstate = savedInstanceState.getString("layout_state");
            String cur_layout = sharedpref.getString(getString(R.string.preference_file_key),
                    layout_state_popular);

            SharedPreferences.Editor editor = sharedpref.edit();

            if (layoutstate.equalsIgnoreCase(layout_state_favorites)){
                //get
                listadapter = new MyListAdapter (getBaseContext(),mInstanceDataArrayList);

                setContentView(R.layout.list_activity);

                //ListView listView2 = (ListView) findViewById(R.id.list_movies);

                listView2.setAdapter(listadapter);

                listView2.scrollTo(curselX,curselY);


            }else{
                ImageAdapter newImgAdapt2 = new ImageAdapter(MainActivity.this, mInstanceDataArrayList);

                setContentView(R.layout.activity_main);

                GridView gridview2 = (GridView) findViewById(R.id.gridview);

                newImgAdapt2.swapData(mInstanceDataArrayList);

                gridview2.setAdapter(newImgAdapt2);

                gridview2.scrollTo(curselX,curselY);
            }

            editor.putString(getString(R.string.preference_file_key),layoutstate);

            editor.commit();

            return;


        }

      //Check for on app focus and return void to avoid any changes.

//        if (gridview !=null  && listView2 != null){
//            return;
//        }

        setContentView(R.layout.activity_main);

        String movieurl = getResources().getString(R.string.preference_file_key);

        URL movieDbQueryURL = makeMovieDBUrl(movieurl);

        new FetchMovieTask(this, new MovieDBQueryTaskCompleteListener()).execute(movieDbQueryURL);

        gridview = (GridView) findViewById(R.id.gridview);


    }


    public URL makeMovieDBUrl(String sortType) {
        Uri top_rated_uri = Uri.parse(MOVIEDB_BASE_URL).buildUpon()
                .appendPath(sortType)
                .appendQueryParameter(PARAM_QUERY, getString(R.string.api_key))
                .build();

        URL top_rated_url = null;

        try {
            top_rated_url = new URL(top_rated_uri.toString());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return top_rated_url;
    }

    public class MovieDBQueryTaskCompleteListener implements AsyncHelper
            .AsyncTaskCompleteListener<MovieClass> {

        @Override
        public void onTaskComplete(List<MovieClass> result) {
            // do something with the result
            mMovieDataArrayList = new ArrayList<>(result);
           // mMovieDataArrayList = result;
            gridview.setAdapter(new ImageAdapter(MainActivity.this, mMovieDataArrayList));


            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {
                }
            });
        }

        @Override
        public void onTaskComplete(ArrayList<ReviewClass> result) {

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private Cursor refreshValuesFromContentProvider() {

        Cursor mCursor = null;
        Uri movies_list = BASE_CONTENT_URI.buildUpon().appendPath(PATH_TASKS).build();

        try {

            mCursor = getContentResolver().query(movies_list, null,
                    MovieProviderContract.FavoriteMoviesTable.COLUMN_MOVIE_ID,
                    null, null);
            return mCursor;

        } catch(Exception e){
        android.util.Log.getStackTraceString(e);
        //addMovieToDB();
    }
    return mCursor;
    }
    //
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int clickedOptionItem = item.getItemId();

        String optUrl;

        ImageAdapter newImgAdapt=null;

        sharedpref = getSharedPreferences(getString(R.string.preference_file_key)
                ,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpref.edit();

        if (clickedOptionItem == R.id.pop_popularity) {
            //Refresh by popularity
            optUrl = getString(R.string.sort_popular);
        }else if (clickedOptionItem == R.id.pop_ratings){
            //Refresh by Ratings
            optUrl = getString(R.string.sort_top_rated);
        }else if (clickedOptionItem == R.id.mnu_favorites){
            try {

                c = refreshValuesFromContentProvider();
                if (c.getCount()==0){
                    Toast.makeText(getBaseContext(),"No favorites Specified yet",Toast.LENGTH_LONG)
                            .show();
                    return true;
                }
                adapter = new MovieCursorAdapter(getBaseContext(),
                        R.layout.listview_item,
                        c,
                        new String[] { MovieProviderContract.FavoriteMoviesTable.COLUMN_ORIGINAL_TITLE},
                        new int[] { R.id.movie_entry}, 0);

                setContentView(R.layout.list_activity);
                listView = (ListView) findViewById(R.id.list_movies);

                listView.setAdapter(adapter);

                optUrl = "favorites";

                editor.putString(getString(R.string.preference_file_key),optUrl);

                editor.commit();

                return true;
            }catch (Exception e) {
                //Log.e(TAG, "Failed to asynchronously load data.");
                e.printStackTrace();
                optUrl = getString(R.string.sort_top_rated);
            }
        }else{
            optUrl = getString(R.string.sort_top_rated);
        }
        editor.putString(getString(R.string.preference_file_key),optUrl);

        editor.commit();

        URL movieDbQueryURL = makeMovieDBUrl(optUrl);

        new FetchMovieTask(this, new MovieDBQueryTaskCompleteListener()).execute(movieDbQueryURL);

        newImgAdapt = new ImageAdapter(MainActivity.this, mMovieDataArrayList);

        setContentView(R.layout.activity_main);

        gridview = (GridView) findViewById(R.id.gridview);

        newImgAdapt.swapData(mMovieDataArrayList);

        gridview.setAdapter(newImgAdapt);

        return true;
        }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState){

        super.onSaveInstanceState(savedInstanceState);
        int curSelX =0;
        int curSelY=0;

        //MovieClass m = new MovieClass();
        ArrayList<MovieClass> mlist = new ArrayList<>();

        String value= layout_state_popular;

        SharedPreferences sharedpref2 = getBaseContext().getSharedPreferences(getString(R.string.preference_file_key),
                MODE_PRIVATE);

        SharedPreferences s3 = sharedpref2;

        if (sharedpref!=null){
            value = getResources().getString(R.string.preference_file_key);
        }
        ListView listView2 = (ListView) findViewById(R.id.list_movies);

        if (listView2 == null){
            //either popular/toprated
            mlist = mMovieDataArrayList;
            GridView gridview3 = (GridView) findViewById(R.id.gridview);
            curSelX = gridview3.getScrollX();
            curSelY = gridview3.getScrollY();
        }else {
            if (c != null) {
                if (c.getCount() > -1) {
                    c.moveToFirst();
                    while (!c.isAfterLast()) {
                        MovieClass m = new MovieClass();
                        m.movie_id = c.getString(1);
                        m.original_title = c.getString(2);
                        mlist.add(m);
                        c.moveToNext();
                    }
                    curSelX = listView2.getScrollX();
                    curSelY = listView2.getScrollY();
                    value = layout_state_favorites;
                } else {
                    //load from adapter
                    MyListAdapter ladapter = (MyListAdapter) listView2.getAdapter();
                    mlist = ladapter.getAllObjects();
                    curSelX = listView2.getScrollX();
                    curSelY = listView2.getScrollY();
                    value = layout_state_favorites;
                }
            }else{
                MyListAdapter ladapter = (MyListAdapter) listView2.getAdapter();
                mlist = ladapter.getAllObjects();
                curSelX = listView2.getScrollX();
                curSelY = listView2.getScrollY();
                value = layout_state_favorites;
            }
        }

        savedInstanceState.putParcelableArrayList(STATE_ITEMS, mlist);
        savedInstanceState.putInt("scrollx", curSelX);
        savedInstanceState.putInt("scrolly", curSelY);

        savedInstanceState.putString("layout_state", value);
    }
    @Override
    public void onResume() {
        super.onResume();
    }
//    @Override
////    public void onStart() {
////        super.onStart();
////    }
}





