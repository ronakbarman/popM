package com.ronak.popularmovies;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;

public class FavoritesProvider extends ContentProvider {
    FavoritesHelper databaseHelper;

    public static final int FAVORITES = 100;
    public static final int FAVORITES_ID = 101;
    private static final UriMatcher uriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher1 = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher1.addURI(FavoritesBaseColumnContract.AUTHORITY,FavoritesBaseColumnContract.FavoritesEntry.TABLE_NAME, FAVORITES);
        uriMatcher1.addURI(FavoritesBaseColumnContract.AUTHORITY, FavoritesBaseColumnContract.FavoritesEntry.TABLE_NAME + "/#", FAVORITES_ID);
        return uriMatcher1;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
       int rowDeleted;
       if (selection == null)
           selection = "1";
       switch (uriMatcher.match(uri)) {
           case FAVORITES:
               rowDeleted = databaseHelper.getWritableDatabase().delete(FavoritesBaseColumnContract.FavoritesEntry.TABLE_NAME, selection, selectionArgs);
               break;
               default:
                   throw new UnsupportedOperationException("Unknown Uri" + uri);
       }
       if (rowDeleted != 0) {
           getContext().getContentResolver().notifyChange(uri,null);
       }
       return rowDeleted;
    }

    @Override
    public String getType(Uri uri) {

        return null;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
       final SQLiteDatabase db = databaseHelper.getWritableDatabase();
       int match = uriMatcher.match(uri);
       Uri uri1;
       switch (match) {
           case  FAVORITES:
               long id = db.insert(FavoritesBaseColumnContract.FavoritesEntry.TABLE_NAME,null, values);
               if (id > 0) {
                   uri1 = ContentUris.withAppendedId(FavoritesBaseColumnContract.CONTENT_URI, id);
               } else {
                   throw new android.database.SQLException("Failed to insert:" + uri);
               }
               break;
            default:
                throw new UnsupportedOperationException("Unknown URI:" + uri);
       }
       getContext().getContentResolver().notifyChange(uri, null);
       return uri1;

    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        databaseHelper = new FavoritesHelper(context);
        return  true;
    }

    @Override
    public Cursor query(@NonNull Uri uri,
                        String[] strings,
                        String selection,
                        String[] selectionArgs,
                        String sortOrder) {
       final SQLiteDatabase database = databaseHelper.getReadableDatabase();
       int match = uriMatcher.match(uri);
       Cursor cursor;
       switch (match) {
           case FAVORITES:
               cursor = database.query(FavoritesBaseColumnContract.FavoritesEntry.TABLE_NAME,
                       strings,
                       selection,
                       selectionArgs,
                       null,
                       null,
                       sortOrder);
               break;
               default:
                   throw new UnsupportedOperationException("Unknown Uri" + uri);

       }
       cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public int update(@NonNull Uri uri,
                      ContentValues values,
                      String selection,
                      String[] selectionArgs) {
       return 0;

    }
}
