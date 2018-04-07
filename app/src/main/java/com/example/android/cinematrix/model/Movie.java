package com.example.android.cinematrix.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mhesah on 2018-04-01.
 */

public class Movie implements Parcelable {

    private String mPoster;
    private String mTitle;
    private Double mVote;
    private String mRelease;
    private String mPlot;
    private static final String HTTP_POSTER_ENTRY_URL = "https://image.tmdb.org/t/p/w185";

    // purpose empty constructor
    public Movie() {
    }

    // setters
    public void setPoster(String poster) {
        mPoster = poster;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setVote(Double vote) {
        mVote = vote;
    }

    public void setRelease(String release) {
        mRelease = release;
    }

    public void setPlot(String plot) {
        mPlot = plot;
    }

    // getters
    public String getPoster() {
        return HTTP_POSTER_ENTRY_URL + mPoster;
    }

    public String getTitle() {
        return mTitle;
    }

    public Double getVote() {
        return mVote;
    }

    public String getRelease() {
        return mRelease;
    }

    public String getPlot() {
        return mPlot;
    }

    // parcelable implementations
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mPoster);
        dest.writeString(mTitle);
        dest.writeValue(mVote);
        dest.writeString(mRelease);
        dest.writeString(mPlot);
    }

    private Movie(Parcel in) {
        mPoster = in.readString();
        mTitle = in.readString();
        mVote = (Double) in.readValue(Double.class.getClassLoader());
        mRelease = in.readString();
        mPlot = in.readString();
    }

    // deserializing object
    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}