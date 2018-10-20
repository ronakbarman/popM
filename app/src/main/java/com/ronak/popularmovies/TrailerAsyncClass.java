package com.ronak.popularmovies;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public  class TrailerAsyncClass extends android.content.AsyncTaskLoader<ArrayList<Trailer>> {
    Bundle mBundle;
    public TrailerAsyncClass(@NonNull Context context, Bundle bundle) {
        super(context);
        mBundle = bundle;
    }

    @Nullable
    @Override
    public ArrayList<Trailer> loadInBackground() {
        Context context = this.getContext();
        String movieId = mBundle.getString("movie_id");
        String result = null;
        URL url = NetworkUtilities.UrlBuild(movieId,"videos");
        try {
            result = NetworkUtilities.queryResults(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return NetworkUtilities.trailerArrayList(result,context);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}