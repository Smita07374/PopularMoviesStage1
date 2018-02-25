package com.dalvinlabs.neha.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetials extends AppCompatActivity {

    TextView mMovieDetails;
    ImageView mPoster;
    Intent intentMovieDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detials);

        mMovieDetails = findViewById(R.id.tv_movie_details);
        mPoster = findViewById(R.id.movieposter);
        intentMovieDetails = getIntent();
        Bundle MOVIES_EXTRA = intentMovieDetails.getExtras();
        String title = MOVIES_EXTRA.getString("Title");
        String overview =MOVIES_EXTRA.getString("Overview");
        String releaseDate=MOVIES_EXTRA.getString("ReleaseDate");
        String votes=MOVIES_EXTRA.getString("Votes");
        String posters = MOVIES_EXTRA.getString("Poster");

       if (MOVIES_EXTRA != null)
       {
           Picasso.with(this).load(NetworkUtils.POSTER_BASE_URL + posters).into(mPoster);
           mMovieDetails.setText("Movie Name : " +title + "\n\nOverview : "+overview + "\n\nRelease Date : "+releaseDate + "\n\nVotes : "
           +votes);
       }
    }
}
