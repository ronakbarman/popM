package com.ronak.popularmovies;

import android.net.Uri;
import android.provider.BaseColumns;

public class FavoritesBaseColumnContract {

 public static final class FavoritesEntry implements BaseColumns

    {

        public static final String TABLE_NAME = "favoritesTable";
        public static final String COLUMN_ID = "movieId";
        public static final String COLUMN_TITLE = "movieTitle";
        public static final String COLUMN_RELEASE_DATE = "movieReleaseDate";
        public static final String COLUMN_AVERAGE_VOTE = "movieAverageVote";
        public static final String COLUMN_PLOT = "moviePlot";
        public static final String COLUMN_POSTER_PATH = "moviePosterPath";




    }

    static final String AUTHORITY = "com.ronak.popularmovies";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static  final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(FavoritesEntry.TABLE_NAME).build();

}