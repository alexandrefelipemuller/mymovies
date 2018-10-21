package com.example.exercise.myfavoritemovies;

import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        //    URL githubSearchUrl = NetworkUtils.buildUrl(githubQuery);
    }

    //@Override
    public Loader<String> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<String>(this) {

            @Nullable
            @Override
            public String loadInBackground() {
                return null;
            }

            // COMPLETED (1) Create a String member variable called mGithubJson that will store the raw JSON
            /* This String will contain the raw JSON from the results of our Github search */
            String mGithubJson;

            @Override
            protected void onStartLoading() {

                /* If no arguments were passed, we don't have a query to perform. Simply return. */
                if (args == null) {
                    return;
                }

                /*
                 * When we initially begin loading in the background, we want to display the
                 * loading indicator to the user
                 */
                mLoadingIndicator.setVisibility(View.VISIBLE);

                // COMPLETED (2) If mGithubJson is not null, deliver that result. Otherwise, force a load
                /*
                 * If we already have cached results, just deliver them now. If we don't have any
                 * cached results, force a load.
                 */
                if (mGithubJson != null) {
                    deliverResult(mGithubJson);
                } else {
                    forceLoad();
                }

            }
        };
    }
}
/*
            @Override
            public String loadInBackground() {

                // Extract the search query from the args using our constant
                String searchQueryUrlString = args.getString(SEARCH_QUERY_URL_EXTRA);

                // If the user didn't enter anything, there's nothing to search for
                if (searchQueryUrlString == null || TextUtils.isEmpty(searchQueryUrlString)) {
                    return null;
                }

                // Parse the URL from the passed in String and perform the search
                try {
                    URL githubUrl = new URL(searchQueryUrlString);
                    String githubSearchResults = NetworkUtils.getResponseFromHttpUrl(githubUrl);
                    return githubSearchResults;
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            // COMPLETED (3) Override deliverResult and store the data in mGithubJson
            // COMPLETED (4) Call super.deliverResult after storing the data
            @Override
            public void deliverResult(String githubJson) {
                mGithubJson = githubJson;
                super.deliverResult(githubJson);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {

        // When we finish loading, we want to hide the loading indicator from the user.
        mLoadingIndicator.setVisibility(View.INVISIBLE);

        if (null == data) {
            showErrorMessage();
        } else {
            mSearchResultsTextView.setText(data);
            showJsonDataView();
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.action_search) {
            makeGithubSearchQuery();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        String queryUrl = mUrlDisplayTextView.getText().toString();
        outState.putString(SEARCH_QUERY_URL_EXTRA, queryUrl);
    }*/