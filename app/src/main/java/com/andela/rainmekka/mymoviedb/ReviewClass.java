package com.andela.rainmekka.mymoviedb;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Oluleke on 5/12/2017.
 */

public class ReviewClass implements Parcelable {


    public String id;
    public String author;
    public String content;
    public String url;
    public String movie_id;

    ReviewClass(){

    }

    protected ReviewClass(Parcel in) {
        id = in.readString();
        author = in.readString();
        content = in.readString();
        url = in.readString();
        movie_id = in.readString();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(author);
        dest.writeString(content);
        dest.writeString(url);
        dest.writeString(movie_id);

    }




    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ReviewClass> CREATOR;

    static {
        CREATOR = new Creator<ReviewClass>() {
            @Override
            public ReviewClass createFromParcel(Parcel in) {
                return new ReviewClass(in);
            }

            @Override
            public ReviewClass[] newArray(int size) {
                return new ReviewClass[size];
            }
        };
    }

}

