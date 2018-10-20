package com.ronak.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {
    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
      parcel.writeString(Id);
      parcel.writeString(MovieTitle);
      parcel.writeString(ReleaseDate);
      parcel.writeString(AverageVote);
      parcel.writeString(Plot);
      parcel.writeString(PosterPath);
    }

    private String Id, MovieTitle, ReleaseDate,AverageVote,Plot,PosterPath;

    Movie(String id, String movieTitle, String releaseDate, String averageVote,String plot, String posterPath) {
        this.Id = id;
        this.MovieTitle = movieTitle;
        this.AverageVote = averageVote;
        this.ReleaseDate = releaseDate;
        this.Plot = plot;
        this.PosterPath = posterPath;

    }

    Movie(Parcel parcel) {
        Id = parcel.readString();
        MovieTitle = parcel.readString();
        ReleaseDate =  parcel.readString();
        AverageVote = parcel.readString();
        Plot = parcel.readString();
        PosterPath = parcel.readString();

    }

    public static final Creator<Movie> creator = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getmovieId(){ return  Id; }
    public String getMovieTitle() { return MovieTitle; }
    public String getMovieReleaseDate(){return ReleaseDate;}
    public String getAverageVotes(){return AverageVote;}
    public String getMoviePlot(){return  Plot;}
    public String getMovieposterpath() {return PosterPath;}

    public void setId(String id) { this.Id = id; }





}
