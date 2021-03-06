/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.exercise.myfavoritemovies;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * These utilities will be used to communicate with the network.
 */
public class NetworkUtils {

    private final static String BASE_URL =
            "https://api.themoviedb.org/3";

    public static String buildUrl(String request) {
        Uri builtUri = Uri.parse(BASE_URL+request).buildUpon()
                .appendQueryParameter("api_key", BuildConfig.API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        if (url != null)
            return url.toString();
        else
            return null;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
    public static String getListResponse(String param){
        try {
            URL servUrl = new URL(NetworkUtils.buildUrl("/movie/"+param));
            Log.i("doInBackground", "Trying to connect to: " + servUrl.toString());
            return NetworkUtils.getResponseFromHttpUrl(servUrl);
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static String getDetailsResponse(String id){
        try {
            URL servUrl = new URL(NetworkUtils.buildUrl("/movie/"+id+"/videos"));
            Log.i("doInBackground", "Trying to connect to: " + servUrl.toString());
            return NetworkUtils.getResponseFromHttpUrl(servUrl);
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    public static String getReviewsResponse(String id) {
        try {
            URL servUrl = new URL(NetworkUtils.buildUrl("/movie/" + id + "/reviews"));
            Log.i("doInBackground", "Trying to connect to: " + servUrl.toString());
            return NetworkUtils.getResponseFromHttpUrl(servUrl);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void defaultError(Context context){
        Toast.makeText(context, "Network error, please try again later...", Toast.LENGTH_SHORT).show();
    }
}