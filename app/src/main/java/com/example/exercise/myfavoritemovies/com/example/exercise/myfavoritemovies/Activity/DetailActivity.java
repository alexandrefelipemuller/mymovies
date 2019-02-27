package com.example.exercise.myfavoritemovies.com.example.exercise.myfavoritemovies.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.exercise.myfavoritemovies.NetworkUtils;
import com.example.exercise.myfavoritemovies.R;
import com.example.exercise.myfavoritemovies.com.example.exercise.myfavoritemovies.Model.Movie;
import com.example.exercise.myfavoritemovies.com.example.exercise.myfavoritemovies.Model.MovieVideoDetail;
import com.example.exercise.myfavoritemovies.com.example.exercise.myfavoritemovies.Model.Review;
import com.example.exercise.myfavoritemovies.com.example.exercise.myfavoritemovies.Model.Reviews;
import com.example.exercise.myfavoritemovies.com.example.exercise.myfavoritemovies.Model.VideoDetail;
import com.example.exercise.myfavoritemovies.com.example.exercise.myfavoritemovies.movieDBHelper;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {
    TextView TVtitle;
    TextView TVrelease;
    TextView TVSynopsis;
    TextView TVUserRating;
    TextView TVTotalRating;
    ImageView IVPoster;
    ImageView IVfavorite;
    LinearLayout LLplay;
    ListView LVReviews;
    boolean favoriteMovie =false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);
        TVtitle = findViewById(R.id.mTitle);
        TVrelease = findViewById(R.id.mReleaseDate);
        TVSynopsis = findViewById(R.id.mSynopsis);
        TVUserRating = findViewById(R.id.mUserRating);
        TVTotalRating = findViewById(R.id.mTotalRating);
        IVPoster = findViewById(R.id.mPosterImage);
        IVfavorite = findViewById(R.id.imageview_favorite);
        LLplay = findViewById(R.id.play_pause_button);
        LVReviews = findViewById(R.id.reviewsId);

        final Movie movie = (Movie) getIntent().getSerializableExtra("movieObject");
        TVtitle.setText(movie.getTitle());
        TVrelease.setText(getApplicationContext().getString(R.string.lblDate) +movie.getReleaseDate());
        TVSynopsis.setText(movie.getOverview());
        if (movie.getVoteCount() != null)
            TVUserRating.setText(getApplicationContext().getString(R.string.lblVotes)+movie.getVoteCount().toString());
        if (movie.getVoteAverage() != null)
            TVTotalRating.setText(getApplicationContext().getString(R.string.lblRating)+movie.getVoteAverage().toString());
        Glide.with(getApplicationContext()).load("http://image.tmdb.org/t/p/w500"+movie.getPosterPath()).into(IVPoster);

        IVfavorite.setClickable(true);
        IVfavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movieDBHelper mDbHelper = new movieDBHelper(getApplicationContext());
                if (favoriteMovie){
                    mDbHelper.removeFavorite(movie.getId());
                    IVfavorite.setImageResource(R.drawable.star);
                    favoriteMovie = false;
                }
                else
                {
                    mDbHelper.addFavorite(movie);
                    IVfavorite.setImageResource(R.drawable.star_filled);
                    favoriteMovie = true;
                }
            }
        });
        AsyncTask<String, Void, String> task =  new DetailActivity.getMovieDetail();
        task.execute(movie.getId().toString());

        AsyncTask<String, Void, String> task2 = new DetailActivity.getMovieReviews();
        task2.execute(movie.getId().toString());
        favoriteMovie = MainActivity.getFavorite(movie.getId());
        if (favoriteMovie)
            IVfavorite.setImageResource(R.drawable.star_filled);
        else
            IVfavorite.setImageResource(R.drawable.star);
    }
    private class getMovieDetail extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... p){
            return NetworkUtils.getDetailsResponse(p[0]);
        }
        @Override
        protected void onPostExecute(String data) {

            LLplay.setVisibility(View.VISIBLE);
            if (data == null) {
                NetworkUtils.defaultError(getApplicationContext());
                return;
            } else {
                Log.d("onPostExecute", "Received JSON: " + data);
                try{
                    MovieVideoDetail response = new Gson().fromJson(data, MovieVideoDetail.class);
                    final VideoDetail[] list = response.getVideoDetails();
                    if (list != null && list.length > 1) {
                        for (int i = 0; i < list.length; i++) {
                            Button btnAddARoom = new Button(getApplicationContext());
                            btnAddARoom.setText("WATCH TRAILLER #"+i);
                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                            btnAddARoom.setLayoutParams(params);
                            final int finalI = i;
                            btnAddARoom.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String url = "http://www.youtube.com/watch?v=" + list[finalI].getKey();
                                    Intent inte = new Intent(Intent.ACTION_VIEW);
                                    inte.setData(Uri.parse(url));
                                    startActivity(inte);

                                }
                            });
                            LLplay.addView(btnAddARoom);
                        }
            }
            else
                Toast.makeText(getApplicationContext(), "Sorry, no trailler available", Toast.LENGTH_SHORT).show();

                }
                catch(Exception e){
                    e.printStackTrace();
                    NetworkUtils.defaultError(getApplicationContext());
                }
            }
            Log.i("getMovieDetail","Task Completed!");
        }
    }

    private class getMovieReviews extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... p) {
            return NetworkUtils.getReviewsResponse(p[0]);
        }

        @Override
        protected void onPostExecute(String data) {

            if (data == null) {
                NetworkUtils.defaultError(getApplicationContext());
                return;
            } else {
                Log.d("onPostExecute", "Received JSON: " + data);
                try {
                    Reviews response = new Gson().fromJson(data, Reviews.class);
                    final Review[] reviewsList = response.getVideoReviews();
                    List<String> values = new ArrayList<>();
                    for (Review val : reviewsList) {
                        values.add(val.content);
                    }
                    //TODO make custom adapter
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                            android.R.layout.simple_list_item_2, android.R.id.text1, values);
                    LVReviews.setAdapter(adapter);
                    ViewGroup.LayoutParams params = LVReviews.getLayoutParams();

                    params.height = values.size()*1000;
                    LVReviews.setLayoutParams(params);
                } catch (Exception e) {
                    e.printStackTrace();
                    NetworkUtils.defaultError(getApplicationContext());
                }
            }
            Log.i("getMovieReviews", "Task Completed!");
        }
    }

}
