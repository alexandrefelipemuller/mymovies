package com.example.exercise.myfavoritemovies.com.example.exercise.myfavoritemovies;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
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
    private Context context;

    public movieDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        this.db = this.getWritableDatabase();
    }

    public void addFavorite(int id) {
        ContentValues values = new ContentValues();
        values.put("ID", id);
        long newRowId = db.insert("favMovies", null, values);
        if (newRowId > 0)
            Toast.makeText(context, "Movie added to favorites", Toast.LENGTH_SHORT).show();
        else
            removeFavorite(id);
        //Toast.makeText(context, "Could not add your movie to favorite list, try again later...", Toast.LENGTH_SHORT).show();
    }

    private void removeFavorite(int id) {
        ContentValues values = new ContentValues();
        values.put("ID", id);
        long deleted = db.delete("favMovies", "id =" + id, null);
        if (deleted > 0)
            Toast.makeText(context, "Movie removed from favorites", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context, "Could not remove your movie from favorite list, try again later...", Toast.LENGTH_SHORT).show();
    }

    public List getFavorites() {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
                "ID"
        };
        String sortOrder = "ID DESC";

        try {
            Cursor cursor = db.query(
                    "favMovies",
                    projection,             // The array of columns to return (pass null to get all)
                    null,              // The columns for the WHERE clause
                    null,          // The values for the WHERE clause
                    null,
                    null,
                    sortOrder
            );
            List itemIds = new ArrayList<>();
            while (cursor.moveToNext()) {
                long itemId = cursor.getLong(
                        cursor.getColumnIndexOrThrow("ID"));
                itemIds.add(itemId);
            }
            cursor.close();
            return itemIds;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
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
}
