package com.ronak.popularmovies;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.ronak.popularmovies.RecyclerViewAdapters.MovieRecyclerViewAdapter;

import java.util.ArrayList;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public  class MainActivity extends AppCompatActivity implements MovieRecyclerViewAdapter.ItemClickListener, SharedPreferences.OnSharedPreferenceChangeListener, android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor> {

    private static final int MOVIE_LOADER  = 10;
    private ArrayList<Movie> movieArrayList;
    private MovieRecyclerViewAdapter movieRecyclerViewAdapter;


    @BindView(R2.id.movie_recyclerView)
    RecyclerView movieRecyclerView;
    @BindString(R2.string.movie_detail_key)
    String movieDetailkey;
    @BindString(R.string.recycler_position_key)
    String recyclerPosK;
    GridLayoutManager gridLayoutManager;
    Parcelable listState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        movieArrayList = new ArrayList<>();
        gridLayoutManager = new GridLayoutManager(this,3);
        movieRecyclerView.setLayoutManager(gridLayoutManager);
        movieRecyclerViewAdapter = new MovieRecyclerViewAdapter(movieArrayList,this,this);
        movieRecyclerView.setAdapter(movieRecyclerViewAdapter);
        String fetchMovieStr = sharedPreferences.getString("sortKey","popular");
        String favorites = ("favorites");
        if (fetchMovieStr.equals(favorites)){
            movieArrayList = new ArrayList<>();
            getSupportLoaderManager().initLoader(1,null, this);
        }else {
            fetchMovies();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.aSetting){
            Intent startSettingActivity = new Intent(this,SettingsActivity.class);
            startActivity(startSettingActivity);
            return  true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void fetchMovies(){
        LoaderManager loaderManager = getLoaderManager();
        Loader<String> loader = loaderManager.getLoader(MOVIE_LOADER);
        if (loader == null){
            loaderManager.initLoader(MOVIE_LOADER,null, movieLoaderListener);
        }else {
            loaderManager.restartLoader(MOVIE_LOADER, null, movieLoaderListener);
        }
    }

    public LoaderManager.LoaderCallbacks<ArrayList<Movie>> movieLoaderListener = new LoaderManager.LoaderCallbacks<ArrayList<Movie>>() {
        @Override
        public Loader<ArrayList<Movie>> onCreateLoader(int id, Bundle args) {

            return new MovieAsyncClass(MainActivity.this);
        }

        @Override
        public void onLoadFinished(Loader<ArrayList<Movie>> loader, ArrayList<Movie> data) {
         if (data != null) {
             movieArrayList = data;
             movieRecyclerViewAdapter.setDataSource(movieArrayList);
             if (listState != null) gridLayoutManager.onRestoreInstanceState(listState);
         }
        }

        @Override
        public void onLoaderReset(Loader<ArrayList<Movie>> loader) {
 movieArrayList = null;
        }
    };

    @NonNull
    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri NAME_URI = FavoritesBaseColumnContract.CONTENT_URI;
        return  new android.support.v4.content.CursorLoader(this,NAME_URI, null, null, null,null);
    }

    @Override
    public void onLoadFinished(@NonNull android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        if (data != null){
            data.moveToPosition(-1);
            try{
                while (data.moveToNext()){
                    String title = data.getString(data.getColumnIndex(FavoritesBaseColumnContract.FavoritesEntry.COLUMN_TITLE));
                    String movieId = data.getString(data.getColumnIndex(FavoritesBaseColumnContract.FavoritesEntry.COLUMN_ID));
                    String plot = data.getString(data.getColumnIndex(FavoritesBaseColumnContract.FavoritesEntry.COLUMN_PLOT));
                    String date = data.getString(data.getColumnIndex(FavoritesBaseColumnContract.FavoritesEntry.COLUMN_RELEASE_DATE));
                    String vote = data.getString(data.getColumnIndex(FavoritesBaseColumnContract.FavoritesEntry.COLUMN_AVERAGE_VOTE));
                    String path = data.getString(data.getColumnIndex(FavoritesBaseColumnContract.FavoritesEntry.COLUMN_POSTER_PATH));
                    Movie movie = new Movie(movieId,title,date,vote,plot,path);
                    movieArrayList.add(movie);
                }
            }finally {
                movieRecyclerViewAdapter.setDataSource(movieArrayList);
                gridLayoutManager.onRestoreInstanceState(listState);
            }
        }

    }

    @Override
    public void onLoaderReset(@NonNull android.support.v4.content.Loader<Cursor> loader) {

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    if (key.equals("sortKey")){
        if (key.equals("favorites")){
            movieArrayList = new ArrayList<>();
            movieRecyclerViewAdapter.setDataSource(movieArrayList);
        } else {
            fetchMovies();
        }
    }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(recyclerPosK,movieRecyclerView.getLayoutManager().onSaveInstanceState());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        listState = savedInstanceState.getParcelable(recyclerPosK);
        System.out.println(listState);
        movieRecyclerView.getLayoutManager().onRestoreInstanceState(listState);
    }

    @Override
    public void onItemClick(Movie movie) {
 Intent intent = new Intent(this,DetailActivity.class);
 intent.putExtra(movieDetailkey,movie);
 startActivity(intent);
    }
}
