package com.example.android.cinematrix.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.android.cinematrix.R;
import com.example.android.cinematrix.models.Trailer;
import com.squareup.picasso.Picasso;

/**
 * Created by mhesah on 2018-04-14.
 */

public class TrailerAdapter  extends BaseAdapter {
    private final Context mContext;
    private final Trailer[] mTrailer;
    public final static int TRAILER_POSTER_HEIGHT = 90;
    public final static int TRAILER_POSTER_WIDTH = 160;

    public TrailerAdapter(Context context, Trailer[] trailer) {
        mContext = context;
        mTrailer = trailer;
    }

    @Override
    public int getCount() {
        if (mTrailer == null || mTrailer.length == 0) {
            return -1;
        }
        return mTrailer.length;
    }

    @Override
    public Trailer getItem(int position) {
        if (mTrailer == null || mTrailer.length == 0) {
            return null;
        }
        return mTrailer[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
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

        Picasso.with(mContext)
                .load(mTrailer[position].getPoster())
                .resize(TRAILER_POSTER_WIDTH, TRAILER_POSTER_HEIGHT)
                .error(R.drawable.error_retrieving_movies)
                .placeholder(R.drawable.retrieving_movies)
                .into(imageView);

        return imageView;
    }
}