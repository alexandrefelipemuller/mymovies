package com.example.exercise.myfavoritemovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.exercise.myfavoritemovies.com.example.exercise.myfavoritemovies.Model.Movie;

import java.util.List;

public class MoviesAdapter extends BaseAdapter {

    private final Context mContext;
    private final List<Movie> movies;

    public MoviesAdapter(Context context, List<Movie> movies) {
        this.mContext = context;
        this.movies = movies;
    }

    @Override
    public int getCount() {
        if (movies != null)
            return movies.size();
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Movie movies;
        movies = this.movies.get(position);

        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.layout_movie, null);
        }

        final ImageView imageView = convertView.findViewById(R.id.imageview_cover_art);
        final TextView nameTextView = convertView.findViewById(R.id.textview_name);
        final ImageView imageViewFavorite = convertView.findViewById(R.id.imageview_favorite);

        Glide.with(mContext).load("http://image.tmdb.org/t/p/w92"+movies.getPosterPath()).into(imageView);

        nameTextView.setText(movies.getTitle());

        return convertView;
    }

}

