package com.ronak.popularmovies;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MovieAsyncClass extends AsyncTaskLoader<ArrayList<Movie>> {
    public MovieAsyncClass(@NonNull Context context) {
        super(context);
    }

    @Nullable
    @Override
    public ArrayList<Movie> loadInBackground() {
        Context context = this.getContext();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String fetchMovieStr = preferences.getString("sortKey", "popular");
        URL url = NetworkUtilities.UrlBuild(null, fetchMovieStr);
        String result = "";
        try {
            result = NetworkUtilities.queryResults(url);
        }catch(IOException e) {
            e.printStackTrace();
        }
        return  NetworkUtilities.movieArrayList(result, context);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}