package com.andela.rainmekka.mymoviedb;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;



import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

/***
 *
 * ToDO: Remove padding from each item
 */

/**
 * Created by yemi on 17/04/2017.
 */

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private List<MovieClass> mDataArray = Collections.emptyList();
//    final static String THUMBNAIL_BASE_URL = "http://image.tmdb.org/t/p/";
//    final static String THUMBNAIL_SIZE = "w185";
    String imgUrlString;


    public ImageAdapter(Context c, List<MovieClass> mAdapterdata) {
        mContext = c;
        mDataArray = mAdapterdata;
    }


    public int getCount() {
        return mDataArray.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            //imageView.setLayoutParams(new GridView.LayoutParams(500,700));
            imageView.setAdjustViewBounds(true);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(10, 2,2, 2);
        } else {
            imageView = (ImageView) convertView;
        }
        //Include switch for backup path here
        imgUrlString = getImageUrlSting() + mDataArray.get(position).poster_path;
        Picasso.with(mContext) //Context
                .load(imgUrlString) //URL/FILE
                .into(imageView);//an ImageView Object to show the loaded image;
        imageView.setContentDescription(mDataArray.get(position).original_title);

        final int pst = position;

        imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                    Class destActivity = MovieDetails.class;

                    Intent intent = new Intent(mContext, destActivity);

                    intent.putExtra("MovieClass", mDataArray.get(pst));

                    mContext.startActivity(intent);
            }
        });

        return imageView;
    }


    public void swapData(List<MovieClass> movieClasses) {
        mDataArray = movieClasses; // assign the passed-in movie list to our `mDataForAdapterArrayList`
        notifyDataSetChanged(); // this is called so that the list can refresh itself
    }
    public final String getImageUrlSting(){
        return  mContext.getString(R.string.image_base_url) +
                mContext.getString(R.string.image_base_dimen);
    }

}
