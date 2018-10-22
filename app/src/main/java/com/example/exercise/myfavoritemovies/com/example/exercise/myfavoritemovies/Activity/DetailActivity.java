package com.example.exercise.myfavoritemovies.com.example.exercise.myfavoritemovies.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ImageViewTargetFactory;
import com.example.exercise.myfavoritemovies.MoviesAdapter;
import com.example.exercise.myfavoritemovies.NetworkUtils;
import com.example.exercise.myfavoritemovies.R;
import com.example.exercise.myfavoritemovies.com.example.exercise.myfavoritemovies.Model.Movie;
import com.example.exercise.myfavoritemovies.com.example.exercise.myfavoritemovies.Model.MovieList;
import com.example.exercise.myfavoritemovies.com.example.exercise.myfavoritemovies.Model.MovieVideoDetail;
import com.example.exercise.myfavoritemovies.com.example.exercise.myfavoritemovies.Model.VideoDetail;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

public class DetailActivity extends AppCompatActivity {
    TextView TVtitle;
    TextView TVrelease;
    TextView TVSynopsis;
    TextView TVUserRating;
    ImageView IVPoster;
    ImageView IVfavorite;
    Button YTplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);
        TVtitle = findViewById(R.id.mTitle);
        TVrelease = findViewById(R.id.mReleaseDate);
        TVSynopsis = findViewById(R.id.mSynopsis);
        TVUserRating = findViewById(R.id.mUserRating);
        IVPoster = findViewById(R.id.mPosterImage);
        IVfavorite = findViewById(R.id.imageview_favorite);
        YTplay = (Button) findViewById(R.id.play_pause_button);

        Movie movie = (Movie) getIntent().getSerializableExtra("movieObject");
        TVtitle.setText(movie.getTitle());
        TVrelease.setText(getApplicationContext().getString(R.string.lblDate) +movie.getReleaseDate());
        TVSynopsis.setText(movie.getOverview());
        if (movie.getVoteCount() != null)
            TVUserRating.setText(getApplicationContext().getString(R.string.lblVotes)+movie.getVoteCount().toString());
        Glide.with(getApplicationContext()).load("http://image.tmdb.org/t/p/w500"+movie.getPosterPath()).into(IVPoster);
        IVfavorite.setClickable(true);
        IVfavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),
                        "Would add on favorite" +
                                " clicking this icon",
                        Toast.LENGTH_LONG).show();
            }
        });
        AsyncTask<String, Void, String> task =  new DetailActivity.getMovieDetail();
        task.execute(movie.getId().toString());
    }
    private class getMovieDetail extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... p){
            return NetworkUtils.getDetailsResponse(p[0]);
        }
        @Override
        protected void onPostExecute(String data) {

            YTplay.setVisibility(View.VISIBLE);
            if (data == null) {
                NetworkUtils.defaultError(getApplicationContext());
                return;
            } else {
                Log.d("onPostExecute", "Received JSON: " + data);
                try{
                    MovieVideoDetail response = new Gson().fromJson(data, MovieVideoDetail.class);
                    final VideoDetail[] list = response.getVideoDetails();
                    YTplay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (list != null && list.length > 1){
                                String url = "http://www.youtube.com/watch?v="+list[0].getKey();
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(url));
                                startActivity(i);
                            }
                            else
                                Toast.makeText(getApplicationContext(), "Sorry, no trailler available", Toast.LENGTH_SHORT).show();

                        }
                    });

                }
                catch(Exception e){
                    e.printStackTrace();
                    NetworkUtils.defaultError(getApplicationContext());
                }
            }
            Log.i("getMovieDetail","Task Completed!");
        }
    }
}
