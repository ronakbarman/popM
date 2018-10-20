package com.ronak.popularmovies;

import android.content.Context;
import android.os.Bundle;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public  class ReviewAsyncTask extends android.content.AsyncTaskLoader<ArrayList<Review>> {
    private Bundle mBundle;
    public  ReviewAsyncTask(Context context, Bundle bundle) {
        super(context);
        mBundle = bundle;
    }
//todo check result equals nll here and in the other classes

    @Override
    public ArrayList<Review> loadInBackground() {
        Context context = this.getContext();
        String movieId = mBundle.getString("movie_id");
        String result = null;
        URL url = NetworkUtilities.UrlBuild(movieId,"reviews");

        try {
            result = NetworkUtilities.queryResults(url);
        } catch (IOException e){
            e.printStackTrace();
        }
       return NetworkUtilities.reviewArrayList(result,context);
    }



    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
