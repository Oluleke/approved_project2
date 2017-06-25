package com.andela.rainmekka.mymoviedb;


/**
 * Created by Oluleke on 5/14/2017.
 */

public class MovieRecord {

    //private variables
    int _id;
    public String _movie_id;
    public String _original_title;

    // Empty constructor
    public MovieRecord(){

    }
    // constructor
    public MovieRecord(int id, String _movie_id, String _original_title){
        this._id = id;
        this._movie_id = _movie_id;
        this._original_title = _original_title;
    }

    // getting ID
    public int getID(){
        return this._id;
    }

    // setting id
    public void setID(int id){
        this._id = id;
    }

    public String get_movie_id(){
        return this._movie_id;
    }

    public void set_movie_id(String _movie_id){
        this._movie_id = _movie_id;
    }

    public String get__original_title(){
        return this._original_title;
    }

    public void set_original_title(String _original_title){
        this._original_title = _original_title;
    }

   }
