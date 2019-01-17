package com.example.exercise.myfavoritemovies;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.example.exercise.myfavoritemovies.com.example.exercise.myfavoritemovies.Converters;
import com.example.exercise.myfavoritemovies.com.example.exercise.myfavoritemovies.Model.Movie;

@Database(entities = {Movie.class}, version = 2, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class MovieDatabase extends RoomDatabase {
    public abstract DaoAccess daoAccess() ;
}