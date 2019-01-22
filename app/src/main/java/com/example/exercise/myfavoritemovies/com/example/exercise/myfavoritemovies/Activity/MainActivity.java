package com.example.exercise.myfavoritemovies.com.example.exercise.myfavoritemovies.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.example.exercise.myfavoritemovies.MoviesAdapter;
import com.example.exercise.myfavoritemovies.NetworkUtils;
import com.example.exercise.myfavoritemovies.R;
import com.example.exercise.myfavoritemovies.com.example.exercise.myfavoritemovies.Model.Movie;
import com.example.exercise.myfavoritemovies.com.example.exercise.myfavoritemovies.Model.MovieList;
import com.example.exercise.myfavoritemovies.com.example.exercise.myfavoritemovies.movieDBHelper;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ProgressBar mLoadingIndicator;
    private static GridView gridView;
    private static List<Movie> movies;
    private static List<Movie> moviesFiltered;
    private static boolean filtered = false;
    private boolean viewTopRated = false;
    private static String currentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridView = findViewById(R.id.gridView1);
        mLoadingIndicator = findViewById(R.id.loadingIndicator);
        onUpdate();
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("movieObject", movies.get(position));
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        movieDBHelper mDbHelper = new movieDBHelper(getApplicationContext());
        mDbHelper.getFavorites();
    }

    private void onUpdate() {
        if (currentView == null) {
            currentView = "popular";
        }
        final AsyncTask<String, Void, String> task =  new getMoviesList();
        task.execute(currentView);
    }
    private void switchView(){
        if (currentView != null && currentView.equals("top_rated")){
            currentView = "popular";
        }else {
            currentView = "top_rated";
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        return true;
    }
    public static void updateFavoriteList(List<Movie> favoriteList) {
        moviesFiltered = favoriteList;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favorites:
                if (!filtered) {
                    if (moviesFiltered != null && moviesFiltered.size() > 0) {
                        MoviesAdapter moviesAdapter = new MoviesAdapter(this.getApplicationContext(), moviesFiltered);
                        gridView.setAdapter(moviesAdapter);
                        filtered = true;
                    }
                } else {
                    MoviesAdapter moviesAdapter = new MoviesAdapter(getBaseContext(), movies);
                    gridView.setAdapter(moviesAdapter);
                    filtered = false;
                }
                return true;
            case R.id.action_filter:
                switchView();
                onUpdate();
                return true;
        }
        return false;
    }

    public static boolean getFavorite(Integer id) {
        int size = moviesFiltered.size();
        for (int i = 0; i < size; i++) {
            if (moviesFiltered.get(i).getId().intValue() == id.intValue())
                return true;
        }
        return false;
    }

    private class getMoviesList extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... p){
            return NetworkUtils.getListResponse(p[0]);
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