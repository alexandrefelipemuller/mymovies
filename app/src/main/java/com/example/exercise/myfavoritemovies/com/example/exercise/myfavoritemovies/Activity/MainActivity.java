package com.example.exercise.myfavoritemovies.com.example.exercise.myfavoritemovies.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.exercise.myfavoritemovies.MoviesAdapter;
import com.example.exercise.myfavoritemovies.NetworkUtils;
import com.example.exercise.myfavoritemovies.R;
import com.example.exercise.myfavoritemovies.com.example.exercise.myfavoritemovies.Model.Movie;
import com.example.exercise.myfavoritemovies.com.example.exercise.myfavoritemovies.Model.MovieList;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private ProgressBar mLoadingIndicator;
    private GridView gridView;
    private Movie[] movies;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridView = findViewById(R.id.gridView1);
        mLoadingIndicator = findViewById(R.id.loadingIndicator);

        AsyncTask<Void, Void, String> task =  new getMoviesList();
        task.execute();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("movieObject", movies[position]);
                startActivity(intent);
            }
        });
    }

    private class getMoviesList extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... p){
            return NetworkUtils.getPopularListResponse();
        }
        @Override
        protected void onPostExecute(String data) {
            // When we finish loading, we want to hide the loading indicator from the user.
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (data == null) {
                NetworkUtils.defaultError(getApplicationContext());
            } else {
                Log.d("onPostExecute", "Received JSON: " + data);
                try{
                    JSONObject main = new JSONObject(data);
                    JSONArray arr = main.getJSONArray("results");
                    MovieList response = new Gson().fromJson(data, MovieList.class);
                    movies = response.getResults();
                    MoviesAdapter moviesAdapter = new MoviesAdapter(getBaseContext(), movies);
                    gridView.setAdapter(moviesAdapter);
                }
                catch(JSONException e){
                    e.printStackTrace();
                    NetworkUtils.defaultError(getApplicationContext());
                }
            }
            Log.i("getMoviesList","Task Completed!");
        }
    }
}