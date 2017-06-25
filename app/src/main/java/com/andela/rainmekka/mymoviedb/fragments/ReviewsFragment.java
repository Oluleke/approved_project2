package com.andela.rainmekka.mymoviedb.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andela.rainmekka.mymoviedb.MovieClass;
import com.andela.rainmekka.mymoviedb.MyAdapter;
import com.andela.rainmekka.mymoviedb.R;
import com.andela.rainmekka.mymoviedb.ReviewClass;
import java.util.ArrayList;

/**
 * Created by Oluleke on 5/12/2017.
 */

public class ReviewsFragment extends Fragment {
    //TextView _list;
    private MovieClass mMovieObj;
    ArrayList<ReviewClass>  reviewClasses = new ArrayList<>();
    public ReviewsFragment() {
        //
    }

    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.review_fragment, container, false);

        //this.setContentView(R.layout.review_fragment);

        TextView tv = (TextView)rootView.findViewById(R.id.tvReviewFragText);

        Bundle args = getArguments();

        if(args != null ){
            reviewClasses = args.getParcelableArrayList("revObject");
        }else{
            reviewClasses = new ArrayList<ReviewClass>();
        }

        //reviewClasses = savedInstanceState.getParcelableArrayList("revObject");

        tv.setText(String.valueOf(reviewClasses.size()));

        return rootView;
    }

    public static ReviewsFragment newInstance(ArrayList<ReviewClass> reviewclasses2){

        ReviewsFragment f = new ReviewsFragment();
        Bundle b = new Bundle();

        b.putParcelableArrayList("revObject",reviewclasses2);

        f.setArguments(b);

        return f;
    }

}


