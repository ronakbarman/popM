package com.ronak.popularmovies;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.CursorLoader;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ronak.popularmovies.RecyclerViewAdapters.ReviewRecyclerViewAdapter;
import com.ronak.popularmovies.RecyclerViewAdapters.TrailerRecyclerViewAdapter;

import java.util.ArrayList;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailActivity extends AppCompatActivity implements TrailerRecyclerViewAdapter.BTap, android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor> {


    @BindView(R2.id.scrollView)
    NestedScrollView scrollView;
    @BindView(R2.id.poster)
    ImageView imageView;
    @BindView(R2.id.title)
    TextView titleView;
    @BindView(R2.id.plot)
    TextView plotView;
    @BindView(R2.id.review_Head)
    TextView reviewHeadView;
    @BindView(R2.id.date)
    TextView releaseDateView;
    @BindView(R2.id.trailerHead)
    TextView trailerHeadView;
    @BindView(R2.id.vote)
    TextView voteView;
    @BindView(R2.id.rating)
    RatingBar ratingBar;
    @BindView(R2.id.review_recyclerView)
    RecyclerView reviewRecyclerView;
    @BindView(R2.id.trailer_recyclerView)
    RecyclerView trailerRecyclerView;
    @BindView(R2.id.fav_b)
    Button addToFavb;
    @BindString(R2.string.addFav)
    String addTOFav;
    @BindString(R2.string.remFav)
    String remFav;
    @BindString(R2.string.movie_detail_key)
    String movieDetailKey;
    @BindString(R2.string.movie_id_key)
    String movieIdKey;
    @BindString(R2.string.share_type)
    String shareType;
    @BindString(R2.string.share_intent_title)
    String shareTitle;
    @BindString(R2.string.trailers_label)
    String trailers;
    @BindString(R2.string.scroll_position_key)
    String scrollPositionK;

    private static final int REVIEW_LOADER = 100;
    private static final int TRAILER_LOADER = 1000;

    private ArrayList<Review> reviewArrayList;
    private ArrayList<Trailer> trailerArrayList;
    private ReviewRecyclerViewAdapter reviewRecyclerViewAdapter;
    private TrailerRecyclerViewAdapter trailerRecyclerViewAdapter;
    private String title, Id, Plot, ReleaseDate, AverageVote, PosterPath;
    private boolean favorites, trailors, loadedReviews, loadedTrailers;
    private int[] scrollPos;
    private Toast favToast;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        Movie movie =  getIntent().getParcelableExtra(movieDetailKey);
        if (movie != null){
            title = movie.getMovieTitle();
            Id = movie.getmovieId();
            Plot = movie.getMoviePlot();
            ReleaseDate = movie.getMovieReleaseDate();
            AverageVote = movie.getAverageVotes();
            PosterPath = movie.getMovieposterpath();
            getSupportLoaderManager().initLoader(1,null,this);
            setUpRecyclerView();
            uiUpdate();
            fetchReview(movie.getmovieId());
            fetchTrailer(movie.getmovieId());
        }
    }

    private  void setUpRecyclerView(){
        reviewArrayList = new ArrayList<>();
        LinearLayoutManager reviewLayoutManager = new LinearLayoutManager(this);
        reviewLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        reviewRecyclerView.setLayoutManager(reviewLayoutManager);
        reviewRecyclerView.setHasFixedSize(true);
        reviewRecyclerViewAdapter = new ReviewRecyclerViewAdapter(reviewArrayList);
        reviewRecyclerView.setAdapter(reviewRecyclerViewAdapter);
        reviewRecyclerView.setNestedScrollingEnabled(false);

        trailerArrayList = new ArrayList<>();
        LinearLayoutManager trailerLayoutManager = new LinearLayoutManager(this);
        trailerLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        trailerRecyclerView.setLayoutManager(trailerLayoutManager);
        trailerRecyclerView.setHasFixedSize(true);
        trailerRecyclerViewAdapter = new TrailerRecyclerViewAdapter(trailerArrayList,this);
        trailerRecyclerView.setAdapter(trailerRecyclerViewAdapter);
        trailerRecyclerView.setNestedScrollingEnabled(false);
    }

    private void uiUpdate(){
        NetworkUtilities.setPoster(this,PosterPath,imageView);
        titleView.setText(title);
        plotView.setText(Plot);
        releaseDateView.setText(ReleaseDate);
        voteView.setText(AverageVote);
        ratingBar.setRating(Float.parseFloat(AverageVote));
    }

    private void fetchReview(String movieID){
        Bundle bundle = new Bundle();
        bundle.putString(movieIdKey,movieID);
        LoaderManager loaderManager = getLoaderManager();
        Loader<String> loader = loaderManager.getLoader(REVIEW_LOADER);
        if (loader == null){
            loaderManager.initLoader(REVIEW_LOADER,bundle,reviewLoaderListener);
        } else {
            loaderManager.restartLoader(REVIEW_LOADER, bundle, reviewLoaderListener);
        }
    }
    public LoaderManager.LoaderCallbacks<ArrayList<Review>> reviewLoaderListener = new LoaderManager.LoaderCallbacks<ArrayList<Review>>() {
        @Override
        public Loader<ArrayList<Review>> onCreateLoader(int id, Bundle bundle) {
            return  new ReviewAsyncTask(DetailActivity.this, bundle);
        }

        @Override
        public void onLoadFinished(Loader<ArrayList<Review>> loader, ArrayList<Review> data) {

            if (data != null) {
                if (data.size() > 0){
                    reviewHeadView.setVisibility(View.VISIBLE);
                    reviewArrayList = data;
                    reviewRecyclerViewAdapter.setDataSource(reviewArrayList);
                }
            }
            loadedReviews = true;
            if (loadedReviews) {
                restoreScrollPos();
            }

        }

        @Override
        public void onLoaderReset(Loader<ArrayList<Review>> loader) {

        }
    };

    private void fetchTrailer(String movieID){
        Bundle bundle = new Bundle();
        bundle.putString(movieIdKey, movieID);
        LoaderManager loaderManager = getLoaderManager();
        Loader<String> loader = loaderManager.getLoader(TRAILER_LOADER);
        if (loader == null) {
            loaderManager.initLoader(TRAILER_LOADER,bundle,trailorLoaderListener);
        } else {
            loaderManager.restartLoader(TRAILER_LOADER,bundle,trailorLoaderListener);
        }
    }

    public LoaderManager.LoaderCallbacks<ArrayList<Trailer>> trailorLoaderListener = new LoaderManager.LoaderCallbacks<ArrayList<Trailer>>() {
        @Override
        public Loader<ArrayList<Trailer>> onCreateLoader(int id, Bundle bundle) {
         return new TrailerAsyncClass(DetailActivity.this,bundle);
        }

        @Override
        public void onLoadFinished(Loader<ArrayList<Trailer>> loader, ArrayList<Trailer> data) {
            if (data != null){
                if (data.size()>0){
                   trailerArrayList = data;
                   trailerRecyclerViewAdapter.setDataSource(trailerArrayList);
                   trailerHeadView.setVisibility(View.VISIBLE);
                   trailors = true;
                   invalidateOptionsMenu();
                }
            }
            loadedTrailers = true;
            if (loadedTrailers){
                restoreScrollPos();
            }
        }

        @Override
        public void onLoaderReset(Loader<ArrayList<Trailer>> loader) {

        }
    };

    @Override
    public void selectedTrailer(String key) {
        NetworkUtilities.trailer_intent(this,key);
    }
    @OnClick(R.id.fav_b)
    public void onClick(View view){
        if (favorites) {
            removeFav();
        } else {
            addFAV();
        }
        getSupportLoaderManager().initLoader(1,null,this);
    }

    private void removeFav(){
        getContentResolver().delete(FavoritesBaseColumnContract.CONTENT_URI,FavoritesBaseColumnContract.FavoritesEntry.COLUMN_ID + "=?", new String[]{Id});
        if (favToast != null){
            favToast.cancel();
        }
        favToast = Toast.makeText(this,remFav,Toast.LENGTH_SHORT);
        favToast.show();
    }

    private void addFAV(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(FavoritesBaseColumnContract.FavoritesEntry._ID,Id);
        contentValues.put(FavoritesBaseColumnContract.FavoritesEntry.COLUMN_TITLE,title);
        contentValues.put(FavoritesBaseColumnContract.FavoritesEntry.COLUMN_RELEASE_DATE,ReleaseDate);
        contentValues.put(FavoritesBaseColumnContract.FavoritesEntry.COLUMN_AVERAGE_VOTE,AverageVote);
        contentValues.put(FavoritesBaseColumnContract.FavoritesEntry.COLUMN_PLOT,Plot);
        contentValues.put(FavoritesBaseColumnContract.FavoritesEntry.COLUMN_POSTER_PATH,PosterPath);



        getContentResolver().insert(FavoritesBaseColumnContract.CONTENT_URI,contentValues);
        if (favToast != null){
            favToast.cancel();
        }
        favToast = Toast.makeText(this,"Added to Favorites",Toast.LENGTH_SHORT);
        favToast.show();
    }

    @NonNull
    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        Uri FAVORITES_URI = FavoritesBaseColumnContract.CONTENT_URI;
        return new CursorLoader(this,FAVORITES_URI, null,null,null,null);
    }

    @Override
    public void onLoadFinished(@NonNull android.support.v4.content.Loader<Cursor> loader, Cursor cursor) {
        ArrayList<String> movieIDs = new ArrayList<>();
        if (cursor != null){
            cursor.moveToPosition(-1);
            try {
                while(cursor.moveToNext()){
                    String movieId = cursor.getString(cursor.getColumnIndex(FavoritesBaseColumnContract.FavoritesEntry.COLUMN_ID));
                    movieIDs.add(movieId);
                }
            }finally {
                if (movieIDs.contains(Id)){
                    favorites = true;
                    addToFavb.setText(remFav);
                }else {
                    favorites = false;
                    addToFavb.setText(addTOFav);
                }
            }
        }
    }

    @Override
    public void onLoaderReset(@NonNull android.support.v4.content.Loader<Cursor> loader) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.shareoption, menu);
        MenuItem menuItem = menu.findItem(R.id.a_share);
        if (favorites){
            menuItem.setVisible(true);
        }
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.a_share){
            if (trailerArrayList.size() > 0){
                Trailer trailer = trailerArrayList.get(0);
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType(shareType);
                String body = NetworkUtilities.trailerlink(trailer.getKey());
                intent.putExtra(Intent.EXTRA_SUBJECT,trailers );
                intent.putExtra(Intent.EXTRA_TEXT,body);
                startActivity(Intent.createChooser(intent,shareTitle));
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntArray(scrollPositionK,new int[] {scrollView.getScrollX(), scrollView.getScrollY()});
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        scrollPos = savedInstanceState.getIntArray(scrollPositionK);
    }
    void restoreScrollPos(){
        if (scrollPos != null){
            scrollView.post(new Runnable() {
                @Override
                public void run() {
                    scrollView.scrollTo(scrollPos[0],scrollPos[1]);
                }
            });
        }
    }
}