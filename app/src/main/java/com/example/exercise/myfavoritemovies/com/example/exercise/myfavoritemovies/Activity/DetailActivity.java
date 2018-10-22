package com.example.exercise.myfavoritemovies.com.example.exercise.myfavoritemovies.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.exercise.myfavoritemovies.R;
import com.example.exercise.myfavoritemovies.com.example.exercise.myfavoritemovies.Model.Movie;

public class DetailActivity extends AppCompatActivity {
    TextView TVtitle;
    TextView TVrelease;
    TextView TVSynopsis;
    TextView TVUserRating;
    ImageView IVPoster;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);
        TVtitle = findViewById(R.id.mTitle);
        TVrelease = findViewById(R.id.mReleaseDate);
        TVSynopsis = findViewById(R.id.mSynopsis);
        TVUserRating = findViewById(R.id.mUserRating);
        IVPoster = findViewById(R.id.mPosterImage);
        Movie movie = (Movie) getIntent().getSerializableExtra("movieObject");
        TVtitle.setText(movie.getTitle());
        TVrelease.setText(movie.getReleaseDate());
        TVSynopsis.setText(movie.getOverview());
        if (movie.getVoteCount() != null)
            TVUserRating.setText(movie.getVoteCount().toString());
        Glide.with(getApplicationContext()).load("http://image.tmdb.org/t/p/w500"+movie.getPosterPath()).into(IVPoster);


    }
}
