package com.example.exercise.myfavoritemovies.com.example.exercise.myfavoritemovies.Activity;

import android.content.Context;
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

import java.util.List;

public class MainActivity extends AppCompatActivity {
    static  private ProgressBar mLoadingIndicator;
    private static GridView gridView;
    private static List<Movie> movies;
    private static List<Movie> favMovies;
    private static Context context;
    private movieDBHelper mDbHelper;

    public static String getCurrentView() {
        return currentView;
    }

    private static String currentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this.getApplicationContext();
        setContentView(R.layout.activity_main);
        gridView = findViewById(R.id.gridView1);
        mLoadingIndicator = findViewById(R.id.loadingIndicator);
        onUpdate();
        mDbHelper = new movieDBHelper(getApplicationContext());
        mDbHelper.getFavorites(false);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                if (movies != null) {
                    Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                    intent.putExtra("movieObject", movies.get(position));
                    startActivity(intent);
                }
            }
        });
    }

    private void onUpdate() {
        if (currentView == null) {
            currentView = "popular";
        }
        if (currentView == "favorites")
            updateView();
        else
        {
            final AsyncTask<String, Void, String> task = new getMoviesList();
            task.execute(currentView);
        }
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
    public static void updateMoviesList(List<Movie> List) {
        movies = List;
    }
    public static void updateFavMoviesList() {
        favMovies = movies;
    }

    static public void updateView()
    {
        if (movies != null && movies.size() > 0) {
            MoviesAdapter moviesAdapter = new MoviesAdapter(context, movies);
            gridView.setAdapter(moviesAdapter);
        }
        // When we finish loading, we want to hide the loading indicator from the user.
        mLoadingIndicator.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favorites:
                if (!currentView.equals("favorites")) {
                    mDbHelper.getFavorites(true);
                    currentView = "favorites";
                } else {
                    switchView();
                    onUpdate();
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
        int size = favMovies.size();
        for (int i = 0; i < size; i++) {
            if (favMovies.get(i).getId().intValue() == id.intValue())
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
            if (data == null) {
                NetworkUtils.defaultError(getApplicationContext());
            } else {
                Log.d("onPostExecute", "Received JSON: " + data);
                try{
                    JSONObject main = new JSONObject(data);
                    JSONArray arr = main.getJSONArray("results");
                    MovieList response = new Gson().fromJson(data, MovieList.class);
                    updateMoviesList(response.getResults());
                    updateView();
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