package com.example.exercise.myfavoritemovies;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.exercise.myfavoritemovies.com.example.exercise.myfavoritemovies.Model.Movie;

import java.util.List;

@Dao
public interface DaoAccess {
    @Insert
    void insertOnlySingleMovie (Movie movie);
    @Query("SELECT * FROM Movie WHERE ID = :movieId")
    Movie fetchOneMoviesbyMovieId(int movieId);
    @Query("SELECT * FROM Movie")
    List<Movie> fetchAllMovies();
    @Update
    void updateMovie (Movie movie);
    @Delete
    void deleteMovie (Movie movie);
}
