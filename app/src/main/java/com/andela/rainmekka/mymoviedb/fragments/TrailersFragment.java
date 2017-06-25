package com.andela.rainmekka.mymoviedb.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andela.rainmekka.mymoviedb.MovieClass;
import com.andela.rainmekka.mymoviedb.R;
import com.andela.rainmekka.mymoviedb.TrailerClass;

import java.util.ArrayList;

/**
 * Created by Oluleke on 5/12/2017.
 */

public class TrailersFragment extends Fragment {
    //TextView _list;
    ArrayList<TrailerClass> trailerClasses = new ArrayList<>();
    public TrailersFragment() {
        //
    }
    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
      }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.trailers_fragment, container, false);

        TextView tv = (TextView)rootView.findViewById(R.id.tvTrailerFragText);

        ArrayList<TrailerClass> trailerClasses = getArguments().getParcelableArrayList("trailerObject");

        tv.setText(String.valueOf(trailerClasses.size()));

        return rootView;
    }

    public static TrailersFragment newInstance(ArrayList<TrailerClass> trailerClasses){

        TrailersFragment f = new TrailersFragment();
        Bundle b = new Bundle();

        b.putParcelableArrayList("trailerObject",trailerClasses);

        f.setArguments(b);

        return f;
    }
}
