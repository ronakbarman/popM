package com.ronak.popularmovies;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FavoritesHelper extends SQLiteOpenHelper {

   private static final String DATABASE_NAME = "favoritesTable.db";
   private static final  int DATABASE_VERSION = 1;

   FavoritesHelper(Context context) {
       super(context,DATABASE_NAME, null, DATABASE_VERSION);

   }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

       final String CREATE_TABLE = "CREATE TABLE " + FavoritesBaseColumnContract.FavoritesEntry.TABLE_NAME + " (" +
               FavoritesBaseColumnContract.FavoritesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
               FavoritesBaseColumnContract.FavoritesEntry.COLUMN_ID + " TEXT NOT NULL," +
               FavoritesBaseColumnContract.FavoritesEntry.COLUMN_TITLE + " TEXT," +
               FavoritesBaseColumnContract.FavoritesEntry.COLUMN_RELEASE_DATE + " TEXT, " +
               FavoritesBaseColumnContract.FavoritesEntry.COLUMN_AVERAGE_VOTE + " TEXT, " +
               FavoritesBaseColumnContract.FavoritesEntry.COLUMN_PLOT + " TEXT, " +
               FavoritesBaseColumnContract.FavoritesEntry.COLUMN_POSTER_PATH + " TEXT " +
               "); ";
       sqLiteDatabase.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
       sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavoritesBaseColumnContract.FavoritesEntry.TABLE_NAME);
       onCreate(sqLiteDatabase);
    }
}
