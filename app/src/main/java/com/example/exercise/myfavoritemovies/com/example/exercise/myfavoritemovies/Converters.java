package com.example.exercise.myfavoritemovies.com.example.exercise.myfavoritemovies;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;

import java.util.List;

public class Converters {

    @TypeConverter
    public static String listToJson(List<Integer> list) {
        if(list == null)
            return null;

        return list.isEmpty() ? null : new Gson().toJson(list);
    }
}
