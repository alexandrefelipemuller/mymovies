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
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ProgressBar mLoadingIndicator;
    private GridView gridView;
    private List<Movie> movies;
    private boolean filtered = false;
    private boolean viewTopRated = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridView = findViewById(R.id.gridView1);
        mLoadingIndicator = findViewById(R.id.loadingIndicator);

        AsyncTask<String, Void, String> task =  new getMoviesList();
        task.execute("popular");

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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favorites:
                if (!filtered) {
                    movieDBHelper mDbHelper = new movieDBHelper(getApplicationContext());
                    List<Long> favoriteList = mDbHelper.getFavorites();
                    //List<Integer> favoriteList = PrefSingleton.getInstance().getFavorites();
                    List<Movie> moviesFiltered = new ArrayList<>();
                    if (favoriteList != null) {
                        //TODO filter it in rightway
                        for (int i = 0; i < favoriteList.size(); i++) {
                            for (int j = 0; j < movies.size(); j++)
                                if (movies.get(j).getId().toString().equals(favoriteList.get(i).toString()))
                                    moviesFiltered.add(movies.get(j));
                        }
                        if (moviesFiltered.size() > 0) {
                            MoviesAdapter moviesAdapter = new MoviesAdapter(getBaseContext(), moviesFiltered);
                            gridView.setAdapter(moviesAdapter);
                            filtered = true;
                        }
                    }
                } else {
                    MoviesAdapter moviesAdapter = new MoviesAdapter(getBaseContext(), movies);
                    gridView.setAdapter(moviesAdapter);
                    filtered = false;
                }
                return true;
            case R.id.action_filter:
                if (viewTopRated){
                    AsyncTask<String, Void, String> task =  new getMoviesList();
                    task.execute("popular");
                    viewTopRated=false;
                }else {
                    AsyncTask<String, Void, String> task =  new getMoviesList();
                    task.execute("top_rated");
                    viewTopRated=true;
                }
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