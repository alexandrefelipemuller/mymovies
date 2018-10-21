package com.example.exercise.myfavoritemovies;

import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private ProgressBar mLoadingIndicator;
    private TextView test;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        test = (TextView) findViewById(R.id.TVtest);
        AsyncTask<Void, Void, String> task =  new InitTask();
        task.execute();

    }
    private class InitTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... p){
            try {
                URL servUrl = new URL(NetworkUtils.buildUrl());
                String searchResults = NetworkUtils.getResponseFromHttpUrl(servUrl);
                return searchResults;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            catch (Exception e){
                e.printStackTrace();
               //Toast.makeText(getApplicationContext(), "Erro de conexão tente novamente mais tarde", Toast.LENGTH_LONG).show();
                return null;
            }
        }
        @Override
        protected void onPostExecute(String data) {

            // When we finish loading, we want to hide the loading indicator from the user.
            //mLoadingIndicator.setVisibility(View.INVISIBLE);

            if (null == data) {
                Toast.makeText(MainActivity.this, "Network error...", Toast.LENGTH_SHORT).show();
            } else {
                test.setText(data);
                //showJsonDataView();
            }
            Toast.makeText(getApplicationContext(),
                    "Task Completed!", Toast.LENGTH_LONG).show();
        }
    }
}