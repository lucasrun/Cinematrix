package com.example.android.cinematrix.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mhesah on 2018-04-08.
 */

public class Trailer implements Parcelable {

    // deserializing object
    public static final Parcelable.Creator<Trailer> CREATOR = new Parcelable.Creator<Trailer>() {
        public Trailer createFromParcel(Parcel in) {
            return new Trailer(in);
        }

        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };

    private static final String HTTP_POSTER_ENTRY_URL = "http://img.youtube.com/vi/";
    private static final String HTTP_POSTER_END_URL = "/0.jpg";
    private String mId;
    private String mTrailerKey;

    // purpose empty constructor
    public Trailer() {
    }

    private Trailer(Parcel in) {
        mId = in.readString();
        mTrailerKey = in.readString();
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getTrailerKey() {
        return mTrailerKey;
    }

    public void setTrailerKey(String key) {
        mTrailerKey = key;
    }

    public String getPoster() {
        return HTTP_POSTER_ENTRY_URL + mTrailerKey + HTTP_POSTER_END_URL;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mTrailerKey);
    }
}