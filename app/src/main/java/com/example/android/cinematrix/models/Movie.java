package com.example.android.cinematrix.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mhesah on 2018-04-01.
 */

public class Movie implements Parcelable {

    // deserializing object
    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    private static final String HTTP_POSTER_ENTRY_URL = "https://image.tmdb.org/t/p/w185";
    private static final String ID = "id";
    private static final String POSTER = "poster_path";
    private static final String TITLE = "original_title";
    private static final String VOTE = "vote_average";
    private static final String RELEASE = "release_date";
    private static final String PLOT = "overview";

    private String mId;
    private String mPoster;
    private String mTitle;
    private String mVote;
    private String mRelease;
    private String mPlot;

    // purpose empty constructor
    public Movie() {
    }

    public Movie(JSONObject movie) throws JSONException {
        mId = movie.getString(ID);
        mPoster = movie.getString(POSTER);
        mTitle = movie.getString(TITLE);
        mVote = movie.getString(VOTE);
        mRelease = movie.getString(RELEASE);
        mPlot = movie.getString(PLOT);
    }

    private Movie(Parcel in) {
        mId = in.readString();
        mPoster = in.readString();
        mTitle = in.readString();
        mVote = in.readString();
        mRelease = in.readString();
        mPlot = in.readString();
    }

    public String getPoster() {
        return HTTP_POSTER_ENTRY_URL + mPoster;
    }

    public void setPoster(String poster) {
        mPoster = poster;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getVote() {
        return mVote;
    }

    public void setVote(String vote) {
        mVote = vote;
    }

    public String getRelease() {
        return mRelease;
    }

    public void setRelease(String release) {
        mRelease = release;
    }

    public String getPlot() {
        return mPlot;
    }

    public void setPlot(String plot) {
        mPlot = plot;
    }

    // parcelable implementations
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mPoster);
        dest.writeString(mTitle);
        dest.writeString(mVote);
        dest.writeString(mRelease);
        dest.writeString(mPlot);
    }
}