package com.example.android.cinematrix.utilities;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.example.android.cinematrix.database.Contract;
import com.example.android.cinematrix.interfaces.MovieTaskCompleted;
import com.example.android.cinematrix.models.Movie;

import java.util.ArrayList;

/**
 * Created by mhesah on 2018-04-15.
 */

public class FavouriteAsyncTask extends AsyncTask<Void, Void, ArrayList<Movie>> {

    private static final String[] COLUMNS = {
            Contract.Entry._ID,
            Contract.Entry.COLUMN_MOVIE_ID,
            Contract.Entry.COLUMN_POSTER,
            Contract.Entry.COLUMN_TITLE,
            Contract.Entry.COLUMN_VOTE,
            Contract.Entry.COLUMN_RELEASE,
            Contract.Entry.COLUMN_PLOT
    };

    private Context mContext;
    private MovieTaskCompleted mListener;

//    public FavouriteAsyncTask(Context context) {
//        mContext = context;
//    }

    public FavouriteAsyncTask(MovieTaskCompleted listener, Context context) {
        mListener = listener;
        mContext = context;
    }

    @Override
    protected ArrayList<Movie> doInBackground(Void... params) {
        Cursor cursor = mContext.getContentResolver().query(
                Contract.Entry.CONTENT_URI,
                COLUMNS,
                null,
                null,
                null
        );

        if (cursor == null) {
            return null;
        }

        ArrayList<Movie> movieArrayList = new ArrayList<>();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Movie movie = new Movie(cursor);
                movieArrayList.add(movie);
            } while (cursor.moveToNext());
            cursor.close();
        }

        cursor.close();
        return movieArrayList;

    }

    @Override
    protected void onPostExecute(ArrayList<Movie> movie) {

        super.onPostExecute(movie);
        mListener.movieTaskCompleted(movie);

    }
}