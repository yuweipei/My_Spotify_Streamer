package com.example.david.myspotifystreamer;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by David on 10/23/15.
 */
public class MovieInfo implements Parcelable {
    private String posterPath;
    private String titleName;
    private String overview;
    private String voteAverage;
    private String releaseDate;
    private String movieId;

    public MovieInfo(String... params) {
        this.posterPath = params[0];
        this.titleName = params[1];
        this.overview = params[2];
        this.voteAverage = params[3];
        this.releaseDate = params[4];
        this.movieId = params[5];
    }

    public MovieInfo(Parcel parcel) {
        posterPath = parcel.readString();
        titleName = parcel.readString();
        overview = parcel.readString();
        voteAverage = parcel.readString();
        releaseDate = parcel.readString();
        movieId = parcel.readString();
    }

    @Override
    public String toString() {
        return movieId + posterPath + "\n" + titleName + "\n" + overview + "\n" + voteAverage +
                "\n" + releaseDate ;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(posterPath);
        parcel.writeString(titleName);
        parcel.writeString(overview);
        parcel.writeString(voteAverage);
        parcel.writeString(releaseDate);
        parcel.writeString(movieId);
    }

    public String getPosterPath() {return posterPath;}

    public String getTitleName() {return titleName;}

    public String getOverview() {return overview;}

    public String getVoteAverage() {return voteAverage;}

    public String getReleaseDate() {return releaseDate;}

    public String getMovieId() {return movieId;}

    public static final Creator<MovieInfo> CREATOR = new Creator<MovieInfo>(){
        @Override
        public MovieInfo createFromParcel(Parcel source) {
            return new MovieInfo(source);
        }

        @Override
        public MovieInfo[] newArray(int size) {
            return new MovieInfo[size];
        }
    };
}
