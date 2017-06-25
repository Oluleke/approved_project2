package com.andela.rainmekka.mymoviedb;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import com.andela.rainmekka.mymoviedb.MovieProviderContract.*;

/**
 * Created by Oluleke on 5/16/2017.
 */

public class MovieDataProvider extends ContentProvider
{
    private MovieDBHelper mHelper ;
    // helper constants for use with the UriMatcher
    private static final int MOVIE_ID = 101;
    private static final int MOVIE_LIST = 100;
    private static final UriMatcher URI_MATCHER= new UriMatcher(UriMatcher.NO_MATCH);
   // URI_MATCHER
    static {

        URI_MATCHER.addURI(MovieProviderContract.CONTENT_URI_STRING,
                MovieProviderContract.FavoriteMoviesTable.TABLE_NAME,
                MOVIE_LIST);
        URI_MATCHER.addURI(MovieProviderContract.CONTENT_URI_STRING,
                MovieProviderContract.FavoriteMoviesTable.TABLE_NAME+"/movieitem",
                MOVIE_ID);
    }

    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {
        return 0;
    }

    @Override
    public String getType(Uri uri) {

        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        final SQLiteDatabase db = mHelper.getWritableDatabase();

        int match = URI_MATCHER.match(uri);
        Uri returnUri=null;

        switch (match) {
            case MOVIE_LIST:
                try {
                    long id = db.insert(FavoriteMoviesTable.TABLE_NAME, null, values);
                    if ( id > 0 ) {
                        returnUri = ContentUris.withAppendedId(
                                FavoriteMoviesTable.CONTENT_URI,id);
                        try{
                            getContext().getContentResolver().notifyChange(returnUri,null);
                        }catch(NullPointerException e){
                            e.printStackTrace();
                        }

                    } else {
                        throw new android.database.SQLException("Failed to insert row into "  + uri);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: "  + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        // Return constructed uri (this points to the newly inserted row of data)
        return returnUri;
   }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mHelper = new MovieDBHelper(context);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        final SQLiteDatabase db = mHelper.getReadableDatabase();

       //SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String selection_query =  selection + "=?";


        int match = URI_MATCHER.match(uri);
        Cursor retCursor;

        switch (match) {
            // Query for the tasks directory
            case MOVIE_LIST:
                retCursor =  db.query(FavoriteMoviesTable.TABLE_NAME,
                        projection,
                        null,
                        null,
                        null,
                        null,
                        sortOrder);
                break;
            case MOVIE_ID:
                retCursor =  db.query(FavoriteMoviesTable.TABLE_NAME,
                        projection,
                        selection_query,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            // Default exception
            default:
                throw new UnsupportedOperationException("Unknown uri: "+  uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        // Return the desired Cursor
        return retCursor;

    }

    @Override
    public int update(Uri uri, ContentValues values, String where,
                      String[] whereArgs) {
        return 0;
    }
}
