package com.example.exercise.myfavoritemovies.com.example.exercise.myfavoritemovies;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.exercise.myfavoritemovies.MovieDatabase;
import com.example.exercise.myfavoritemovies.com.example.exercise.myfavoritemovies.Activity.MainActivity;
import com.example.exercise.myfavoritemovies.com.example.exercise.myfavoritemovies.Model.Movie;

import java.util.List;

public class movieDBHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Movies.db";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE favMovies (ID INTEGER PRIMARY KEY )";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS favMovies";
    private SQLiteDatabase db;
    private MovieDatabase movieDatabase;
    private Context context;

    public movieDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
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
        Toast.makeText(context, "Movie removed from favorites", Toast.LENGTH_SHORT).show();
    }

    public void getFavorites(final Boolean refreshScreen) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                new getFavoriteView().execute(refreshScreen);
            }
        }) .start();
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    private class getFavoriteView extends AsyncTask<Boolean, Void, Void> {
        @Override
        protected void onPostExecute(Void result) {
            MainActivity.updateView();
        }
        @Override
        protected Void doInBackground(Boolean... params) {
            List<Movie> favMovies = movieDatabase.daoAccess().fetchAllMovies();
            MainActivity.updateMoviesList(favMovies);
            MainActivity.updateFavMoviesList();
            return null;
        }
    }
}
