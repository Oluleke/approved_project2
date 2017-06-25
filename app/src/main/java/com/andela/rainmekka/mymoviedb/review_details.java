package com.andela.rainmekka.mymoviedb;

/**
 * Created by Oluleke on 5/29/2017.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubePlayerView;

public class review_details extends Activity {

    private static final int RECOVERY_REQUEST = 1;
    private YouTubePlayerView youTubeView;
    private TextView authorTv;
    private TextView contentTv;
    private String strAuthor;
    private String strContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_details);

        authorTv = (TextView)findViewById(R.id.author);
        contentTv = (TextView)findViewById(R.id.content);

//        authorTv.setVisibility(View.INVISIBLE);
//        contentTv.setVisibility(View.INVISIBLE);

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra("ReviewClass")) {
                ReviewClass rvclass = getIntent().getParcelableExtra("ReviewClass");
                strAuthor = rvclass.author;
                strContent = rvclass.content;

                authorTv.setText(strAuthor);
                contentTv.setText(strContent);
            }
        }

    }
}