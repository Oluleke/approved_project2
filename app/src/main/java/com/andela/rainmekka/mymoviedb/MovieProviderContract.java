package com.andela.rainmekka.mymoviedb;

/**
 * Created by Oluleke on 5/16/2017.
 */

import android.content.ContentResolver;
import android.content.UriMatcher;
import android.net.Uri;
import android.provider.BaseColumns;

public class MovieProviderContract {

    public static final String AUTHORITY = "com.andela.rainmekka.mymoviedb";
    public static final String PROVIDER_NAME = "MovieDataProvider";
    public static final String CONTENT_URI_STRING = AUTHORITY +"."+PROVIDER_NAME;

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY+"."+PROVIDER_NAME);





    private MovieProviderContract() {
    }
   public static final class FavoriteMoviesTable implements BaseColumns {

       public static final String COLUMN_ORIGINAL_TITLE = "originalTitle";
       public static final String COLUMN_MOVIE_ID = "movieID";
       public static final String TABLE_NAME = "FavoriteMovies";
       public static final String PATH_TASKS = "FavoriteMovies";



       public static final Uri CONTENT_URI =
               BASE_CONTENT_URI.buildUpon().appendPath(PATH_TASKS).build();

       public static final Uri CONTENT_TYPE = Uri.parse(BASE_CONTENT_URI+ "/"+TABLE_NAME+"/");

       public static final Uri CONTENT_ITEM_TYPE = Uri.parse(BASE_CONTENT_URI+ "/"+TABLE_NAME +"/movieitem");

       public static final String[] PROJECTION_ALL =
               {_ID, COLUMN_ORIGINAL_TITLE, COLUMN_MOVIE_ID};

       public static final String SORT_ORDER_DEFAULT =
               COLUMN_ORIGINAL_TITLE + " ASC";
    }

    }
