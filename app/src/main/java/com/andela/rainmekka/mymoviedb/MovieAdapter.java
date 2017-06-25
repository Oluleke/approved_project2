package com.andela.rainmekka.mymoviedb;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;



import java.util.Collections;
import java.util.List;

/**
 * Created by yemi on 15/04/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder>
        implements View.OnClickListener{

    private Context context;
    private LayoutInflater inflater;
    private List<MovieClass> mDataForAdapterArrayList = Collections.emptyList();

    private static String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";

    private static String IMAGE_SIZE = "w185/";

    ImageView personPhoto;
    MovieClass current;

    ImageView vPoster;

    int currentPos=0;

    public MovieAdapter (List<MovieClass> mAdapterdata, Context context){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.mDataForAdapterArrayList = mAdapterdata;

        //mMoviePoster = (ImageView) view.findViewById(R.id.iv_moviePoster);
    }

    /*
     * An on-click handler that we've defined to make it easy for an Activity to interface with
     * our RecyclerView
     */
    private MovieAdapterOnClickHandler mClickHandler;

    @Override
    public void onClick(View v) {

    }

    /**
     * The interface that receives onClick messages.
     */
    public interface MovieAdapterOnClickHandler {
        void onClick(MovieClass MovieItemData);
    }

    /**
     * Creates a MovieAdapter.
     *
     * @param clickHandler The on-click handler for this adapter. This single handler is called
     *                     when an item is clicked.
     */
    public MovieAdapter(MovieAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    public interface ListItemClickListener {
    }

    /**
     * Cache of the children views for a forecast list item.
     */
    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        public final ImageView mMoviePoster;

        // create constructor to get widget reference

        public MovieAdapterViewHolder(View view) {


           super(view);
            mMoviePoster = (ImageView) view.findViewById(R.id.person_photo);
            view.setOnClickListener(this);
        }



        /**
         * This gets called by the child views during a click.
         *
         * @param v The View that was clicked
         */
        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            MovieClass movieDetails = mDataForAdapterArrayList.get(adapterPosition);
            mClickHandler.onClick(movieDetails);
        }
    }

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutformovieListItem = R.layout.movie_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldattacheToParentImmediately = false;

        View view = inflater.inflate(layoutformovieListItem, viewGroup, shouldattacheToParentImmediately);
        MovieAdapterViewHolder movieContainer = new MovieAdapterViewHolder(view);
        return movieContainer;
    }

    // Bind data
    @Override
    public void onBindViewHolder(MovieAdapterViewHolder movieadapterviewHolder, int position) {

        // Get current position of item in recyclerview to bind data and assign values from list
        MovieAdapterViewHolder movieContainer =  movieadapterviewHolder;

        MovieClass current = mDataForAdapterArrayList.get(position);
        // load image into imageview using glide
        String imgurl = IMAGE_BASE_URL + IMAGE_SIZE +current.poster_path;
        Glide.with(context).load(imgurl)
                .placeholder(R.drawable.liked)
                .error(R.drawable.share)
                .into(movieContainer.mMoviePoster);
    }

    @Override
    public int getItemCount() {
        if (null == mDataForAdapterArrayList) return 0;
        return mDataForAdapterArrayList.size();
    }
}
