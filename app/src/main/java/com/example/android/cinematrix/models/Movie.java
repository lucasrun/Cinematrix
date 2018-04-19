package com.example.android.cinematrix.models;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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

    private String mId;
    private String mPoster;
    private String mTitle;
    private String mVote;
    private String mRelease;
    private String mPlot;

    private ArrayList<Movie> mMovie = new ArrayList<>();

    private static final String ID = "id";
    private static final String POSTER = "poster_path";
    private static final String TITLE = "original_title";
    private static final String VOTE = "vote_average";
    private static final String RELEASE = "release_date";
    private static final String PLOT = "overview";

    private static final int COLUMN_CURSOR_ID = 1;
    private static final int COLUMN_CURSOR_POSTER = 2;
    private static final int COLUMN_CURSOR_TITLE = 3;
    private static final int COLUMN_CURSOR_VOTE = 4;
    private static final int COLUMN_CURSOR_RELEASE = 5;
    private static final int COLUMN_CURSOR_PLOT = 6;

    // purpose empty constructor
    public Movie() {
    }

    public Movie(String id, String poster, String title, String vote, String release, String plot) {
        setId(id);
        setPoster(poster);
        setTitle(title);
        setVote(vote);
        setRelease(release);
        setPlot(plot);
    }

    public Movie(JSONObject movie) throws JSONException {
        mId = movie.getString(ID);
        mPoster = movie.getString(POSTER);
        mTitle = movie.getString(TITLE);
        mVote = movie.getString(VOTE);
        mRelease = movie.getString(RELEASE);
        mPlot = movie.getString(PLOT);
    }

    public Movie(Cursor cursor) {
        mId = cursor.getString(COLUMN_CURSOR_ID);
        mPoster = cursor.getString(COLUMN_CURSOR_POSTER);
        mTitle = cursor.getString(COLUMN_CURSOR_TITLE);
        mVote = cursor.getString(COLUMN_CURSOR_VOTE);
        mRelease = cursor.getString(COLUMN_CURSOR_RELEASE);
        mPlot = cursor.getString(COLUMN_CURSOR_PLOT);
    }

    public Movie(Parcel in) {
        mId = in.readString();
        mPoster = in.readString();
        mTitle = in.readString();
        mVote = in.readString();
        mRelease = in.readString();
        mPlot = in.readString();
        in.readTypedList (mMovie, Movie.CREATOR);
        if (mMovie == null) mMovie = new ArrayList<>();
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

    public ArrayList<Movie> getListMovie() {
        return mMovie;
    }

    public void setListMovie(ArrayList<Movie> movie) {
        mMovie = movie;
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
        dest.writeTypedList(mMovie);
    }
}