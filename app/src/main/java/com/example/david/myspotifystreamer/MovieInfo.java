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

    public MovieInfo(String... params) {
        this.posterPath = params[0];
        this.titleName = params[1];
        this.overview = params[2];
        this.voteAverage = params[3];
    }

    public MovieInfo(Parcel parcel) {
        posterPath = parcel.readString();
        titleName = parcel.readString();
        overview = parcel.readString();
        voteAverage = parcel.readString();
    }

    @Override
    public String toString() {
        return posterPath + "\n" + titleName + "\n" + overview + "\n" + voteAverage;
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
    }

    public String getPosterPath() {return posterPath;}

    public String getTitleName() {return titleName;}

    public String getOverview() {return overview;}

    public String getVoteAverage() {return voteAverage;}

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
