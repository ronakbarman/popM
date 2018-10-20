package com.ronak.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class NetworkUtilities {

    // todo API
    private static final String scheme = "https";
    private static final String uri_path1 = "3";
    private static final String uri_path2 = "movie";
    private static final String authority = "api.themoviedb.org";
    private static final String query = "api_key";
    private static final String api = "YOUR API HERE";
    private static final String poster = "https://image.tmdb.org/t/p/w185%s";
    private static final String uri_vendor = "vnd.youtube:";
    private static final String uri_web = "https://www.youtube.com/watch?v=";

    public static String queryResults(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream inputStream = urlConnection.getInputStream();
            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");
            boolean inputPresent = scanner.hasNext();
            if (inputPresent){
                return scanner.next();
            }else {
                return null;
            }
        }finally {
            urlConnection.disconnect();
        }
    }

    public static URL UrlBuild(String id, String searchType) {
        Uri.Builder urlBuild = new  Uri.Builder();
       urlBuild.scheme(scheme);
       urlBuild.authority(authority);
       urlBuild.appendPath(uri_path1);
        urlBuild.appendPath(uri_path2);
       if (id != null) {
           urlBuild.appendPath(id);
       }
       urlBuild.appendPath(searchType);
       urlBuild.appendQueryParameter(query,api);
        URL url = null;
       try {
           url = new URL(urlBuild.build().toString());
       }catch (MalformedURLException e) {
           e.printStackTrace();
       }
       return url;
    }

    public static ArrayList<Movie> movieArrayList(String jsonStr, Context context) {
        if (jsonStr != null) {
            try {
                JSONObject jsonObject = new JSONObject(jsonStr);
                JSONArray results = jsonObject.getJSONArray("results");
                ArrayList<Movie> movieArrayList = new ArrayList<>();
                for (int i = 0; i < results.length(); i++ ) {
                    JSONObject object = results.getJSONObject(i);
                    String id = object.getString("id");
                    String releaseDate = object.getString("release_date");
                    String title = object.getString("title");
                    String averageVote = object.getString("vote_average");
                    String plot = object.getString("overview");
                    String posterPath = object.getString("poster_path");
                    Movie movie = new Movie(id, title, releaseDate, averageVote,plot, posterPath);
                    movieArrayList.add(movie);
                }
                return  movieArrayList;
            } catch (final JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

   public static ArrayList<Review> reviewArrayList(String jsonStr, Context context) {
        if (jsonStr != null) {
            try {
                JSONObject object = new JSONObject(jsonStr);
                JSONArray results = object.getJSONArray("results");
                ArrayList<Review> reviews = new ArrayList<>();
                for (int i = 0; i < results.length(); i++) {
                    JSONObject obj = results.getJSONObject(i);
                    String author = obj.getString("author");
                    String content = obj.getString("content");
                    Review review = new Review(author, content);
                    reviews.add(review);
                    }
                    return reviews;
            }catch (final JSONException e){
                e.printStackTrace();
            }
        }
        return  null;
   }


    public static ArrayList<Trailer>  trailerArrayList(String jsonStr, Context context) {
        if (jsonStr != null) {
            try {
                JSONObject object = new JSONObject(jsonStr);
                JSONArray results = object.getJSONArray("results");
                ArrayList<Trailer> trailers = new ArrayList<>();
                for (int i = 0; i < results.length(); i++) {
                    JSONObject obj = results.getJSONObject(i);
                    String name = obj.getString("name");
                    String key = obj.getString("key");
                    Trailer trailer = new Trailer(name, key);
                    trailers.add(trailer);
                }
                return trailers;
            }catch (final JSONException e){
                e.printStackTrace();
            }
        }
        return  null;
    }


    public static  void setPoster(Context context, String path, ImageView view) {
        Glide.with(context).load(String.format(poster,path)).into(view);
    }

    public  static void trailer_intent(Context context, String key) {
        Intent youtube = new Intent(Intent.ACTION_VIEW, Uri.parse(uri_vendor+key));
        Intent web = new Intent(Intent.ACTION_VIEW,Uri.parse(uri_web+key));
        try {
            context.startActivity(youtube);
        } catch (ActivityNotFoundException e){
            context.startActivity(web);
        }
    }



    public static String trailerlink(String key){
        return uri_web + key;
    }

}
