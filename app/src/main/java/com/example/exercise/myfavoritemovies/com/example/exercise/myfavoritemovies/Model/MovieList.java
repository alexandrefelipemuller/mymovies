package com.example.exercise.myfavoritemovies.com.example.exercise.myfavoritemovies.Model;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovieList {

    private Integer page;
    private Integer totalResults;
    private Integer totalPages;
    private List<Movie> results = null;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public List<Movie> getResults() {
        return results;
    }

    public void setResults(List<Movie> results) {
        this.results = results;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Insert
    void insertOnlySingleMovie (Movie movie) {
    }

    @Query("SELECT * FROM favMovies WHERE ID = :movieId")
    Movie fetchOneMoviesbyMovieId(int movieId) {
        return null;
    }

    @Update
    void updateMovie (Movie movie) {
    }
    @Delete
    void deleteMovie (Movie movie) {
    }
}
