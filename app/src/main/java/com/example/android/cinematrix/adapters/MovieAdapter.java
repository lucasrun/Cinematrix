package com.example.android.cinematrix.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.android.cinematrix.R;
import com.example.android.cinematrix.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by mhesah on 2018-04-01.
 */

public class MovieAdapter extends BaseAdapter {
    private final Context mContext;
    private final ArrayList<Movie> mMovie;
    private final String mChoice;
    private final static String mFavouriteMovies = "favourite";
    private static final String HTTP_POSTER_ENTRY_URL = "https://image.tmdb.org/t/p/w185";
    public final static int POSTER_HEIGHT = 278;
    public final static int POSTER_WIDTH = 185;

    public MovieAdapter(Context context, ArrayList<Movie> movie, String choice) {
        mContext = context;
        mMovie = movie;
        mChoice = choice;
    }

    public void add(Movie movie) {
        mMovie.add(movie);
        notifyDataSetChanged();
    }

    public void clear() {
        mMovie.clear();
        notifyDataSetChanged();
    }

    public void setData(ArrayList<Movie> data) {
        clear();
        for (Movie movie : data) {
            add(movie);
        }
    }

    @Override
    public int getCount() {
        return mMovie.size();
    }

    @Override
    public Movie getItem(int position) {
        return mMovie.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;

        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setAdjustViewBounds(true);
        } else {
            imageView = (ImageView) convertView;
        }

        if (!(mChoice == mFavouriteMovies)) {
            Picasso.with(mContext)
                    .load(HTTP_POSTER_ENTRY_URL + mMovie.get(position).getPoster())
                    .resize(POSTER_WIDTH, POSTER_HEIGHT)
                    .error(R.drawable.error_retrieving_movies)
                    .placeholder(R.drawable.retrieving_movies)
                    .into(imageView);
        } else {
            Picasso.with(mContext)
                    .load(HTTP_POSTER_ENTRY_URL + mMovie.get(position).getPoster())
                    .resize(POSTER_WIDTH, POSTER_HEIGHT)
                    .error(R.drawable.error_retrieving_movies)
                    .placeholder(R.drawable.retrieving_movies)
                    .into(imageView);
        }

        return imageView;
    }
}