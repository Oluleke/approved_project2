package com.andela.rainmekka.mymoviedb;


/**
 * Created by Oluleke on 5/15/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import com.andela.rainmekka.mymoviedb.MovieProviderContract.*;

import java.util.ArrayList;

public class MovieDBHelper extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "MovieRecordsManager";


    // MovieRecords Table Columns names

    private static final String KEY_ID = "_id";

    public MovieDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIES);

        String CREATE_MOVIES_TABLE = "CREATE TABLE  " + FavoriteMoviesTable.TABLE_NAME + "("
                + KEY_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, " + FavoriteMoviesTable.COLUMN_MOVIE_ID
                + " TEXT NOT NULL," + FavoriteMoviesTable.COLUMN_ORIGINAL_TITLE  + " TEXT NOT NULL" + ");";
        db.execSQL(CREATE_MOVIES_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + FavoriteMoviesTable.TABLE_NAME);

        // Create tables again
        onCreate(db);

    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new movierecord
    void addMovieRecord(MovieRecord movierecord) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FavoriteMoviesTable.COLUMN_MOVIE_ID, movierecord.get_movie_id());
        values.put(FavoriteMoviesTable.COLUMN_ORIGINAL_TITLE, movierecord.get__original_title());

        // Inserting Row
        db.insert(FavoriteMoviesTable.TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }

    // Getting single movierecord
    public MovieRecord getMovieRecord(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(FavoriteMoviesTable.TABLE_NAME, new String[]{FavoriteMoviesTable._ID,
                        FavoriteMoviesTable.COLUMN_MOVIE_ID, FavoriteMoviesTable.
                        COLUMN_ORIGINAL_TITLE}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        MovieRecord movierecord = new MovieRecord(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));
        // return movierecord
        return movierecord;
    }
    public int getMovieDataExists(String movieid) {
        String countQuery = "SELECT  * FROM " + FavoriteMoviesTable.TABLE_NAME + " WHERE "
                + FavoriteMoviesTable.COLUMN_MOVIE_ID + "=" +
                movieid;
        SQLiteDatabase db = this.getReadableDatabase();

        try {

            Cursor cursor = db.rawQuery(countQuery, null);
            if (cursor == null){
                return -1;
            }
            else{
                return cursor.getCount();
            }
        }catch (SQLiteException ex){
            return -1;
        }
    }

    // Getting All MovieRecords
    public ArrayList<MovieRecord> getAllMovieRecords() {
        ArrayList<MovieRecord> movierecordList = new ArrayList<MovieRecord>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + FavoriteMoviesTable.TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                MovieRecord movierecord = new MovieRecord();
                movierecord.setID(Integer.parseInt(cursor.getString(0)));
                movierecord.set_movie_id(cursor.getString(1));
                movierecord.set_original_title(cursor.getString(2));

                movierecordList.add(movierecord);
            } while (cursor.moveToNext());
        }

        // return movierecord list
        return movierecordList;
    }

    // Updating single movierecord
    public int updateMovieRecord(MovieRecord movierecord) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FavoriteMoviesTable.COLUMN_MOVIE_ID, movierecord.get_movie_id());
        values.put(FavoriteMoviesTable.COLUMN_ORIGINAL_TITLE, movierecord.get__original_title());

        // updating row
        return db.update(FavoriteMoviesTable.TABLE_NAME, values, KEY_ID + " = ?",
                new String[]{String.valueOf(movierecord.getID())});
    }

    // Deleting single movierecord
    public void deleteMovieRecord(MovieRecord movierecord) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(FavoriteMoviesTable.TABLE_NAME, KEY_ID + " = ?",
                new String[]{String.valueOf(movierecord.getID())});
        db.close();
    }


    // Getting movierecords Count
    public int getMovieRecordsCount() {
        String countQuery = "SELECT  * FROM " + FavoriteMoviesTable.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }
}