package com.example.exercise.myfavoritemovies.com.example.exercise.myfavoritemovies;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.exercise.myfavoritemovies.MovieDatabase;
import com.example.exercise.myfavoritemovies.com.example.exercise.myfavoritemovies.Activity.MainActivity;
import com.example.exercise.myfavoritemovies.com.example.exercise.myfavoritemovies.Model.Movie;

import java.util.List;

public class movieDBHelper {
    private static final String DATABASE_NAME = "Movies.db";
    private MovieDatabase movieDatabase;
    private Context context;

    public movieDBHelper(Context context) {
        this.context = context;
        movieDatabase = Room.databaseBuilder(this.context,
                MovieDatabase.class, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build();
    }

    public void addFavorite(final Movie movie) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    movieDatabase.daoAccess().insertOnlySingleMovie(movie);
                }
                catch(SQLiteConstraintException e){
                    if (e.getMessage().contains("UNIQUE"))
                        removeFavorite(movie.getId());
                }
            }
        }) .start();
        Toast.makeText(context, "Movie added to favorites", Toast.LENGTH_SHORT).show();
    }

    public void removeFavorite(final int id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Movie movie =new Movie();
                movie.setId(id);
                movieDatabase.daoAccess().deleteMovie(movie);
            }
        }) .start();
    }

    public void getFavorites(final Boolean refreshScreen) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                new getFavoriteView().execute(refreshScreen);
            }
        }) .start();
    }

    private class getFavoriteView extends AsyncTask<Boolean, Void, Void> {
        @Override
        protected void onPostExecute(Void result) {
            MainActivity.updateView();
        }
        @Override
        protected Void doInBackground(Boolean... params) {
            List<Movie> favMovies = movieDatabase.daoAccess().fetchAllMovies();
            if (params[0])
                MainActivity.updateMoviesList(favMovies);
            MainActivity.updateFavoritesMoviesList(favMovies);
            return null;
        }
    }
}
