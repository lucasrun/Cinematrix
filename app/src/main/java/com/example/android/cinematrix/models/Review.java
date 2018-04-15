package com.example.android.cinematrix.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mhesah on 2018-04-08.
 */

public class Review implements Parcelable {

    // deserializing object
    public static final Parcelable.Creator<Review> CREATOR = new Parcelable.Creator<Review>() {
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    private static final String ID = "id";
    private static final String AUTHOR = "author";
    private static final String CONTENT = "content";
    private String mId;
    private String mAuthor;
    private String mContent;

    // purpose empty constructor
    public Review() {
    }

    public Review(JSONObject review) throws JSONException {
        mId = review.getString(ID);
        mAuthor = review.getString(AUTHOR);
        mContent = review.getString(CONTENT);
    }

    protected Review(Parcel in) {
        mId = in.readString();
        mAuthor = in.readString();
        mContent = in.readString();
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String author) {
        mAuthor = author;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mAuthor);
        dest.writeString(mContent);
    }
}