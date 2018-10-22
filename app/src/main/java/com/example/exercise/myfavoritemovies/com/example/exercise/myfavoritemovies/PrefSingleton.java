package com.example.exercise.myfavoritemovies.com.example.exercise.myfavoritemovies;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

public class PrefSingleton{
    private static PrefSingleton mInstance;
    private Context mContext;
    public List<Integer> favorites = null;
    private SharedPreferences mMyPreferences;

    private PrefSingleton(){ favorites = new ArrayList<>(); }

    public static PrefSingleton getInstance(){
        if (mInstance == null) mInstance = new PrefSingleton();
        return mInstance;
    }

    public void Initialize(Context ctxt){
        mContext = ctxt;
        mMyPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    public void addFavorite(Integer value){
        favorites.add(value);
    }

    public List<Integer> getFavorites() {
        return favorites;
    }
}