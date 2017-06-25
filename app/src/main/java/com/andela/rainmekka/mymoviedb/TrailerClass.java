package com.andela.rainmekka.mymoviedb;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Oluleke on 5/12/2017.
 */

public class TrailerClass implements Parcelable {


    public String id;
    public String iso_639_1;
    public String iso_3166_1;
    public String name;
    public String site;
    public String size;
    public String type;
    public String movie_id;

    TrailerClass(){

    }

    protected TrailerClass(Parcel in) {
        id = in.readString();
        iso_639_1 = in.readString();
        iso_3166_1 = in.readString();
        name = in.readString();
        site = in.readString();
        size = in.readString();
        type = in.readString();
        movie_id = in.readString();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(iso_639_1);
        dest.writeString(iso_3166_1);
        dest.writeString(name);
        dest.writeString(site);
        dest.writeString(size);
        dest.writeString(type);
        dest.writeString(movie_id);

    }




    @SuppressWarnings("unused")
    public static final Parcelable.Creator<TrailerClass> CREATOR;

    static {
        CREATOR = new Creator<TrailerClass>() {
            @Override
            public TrailerClass createFromParcel(Parcel in) {
                return new TrailerClass(in);
            }

            @Override
            public TrailerClass[] newArray(int size) {
                return new TrailerClass[size];
            }
        };
    }

}

