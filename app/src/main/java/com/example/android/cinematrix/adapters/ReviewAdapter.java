package com.example.android.cinematrix.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.android.cinematrix.R;
import com.example.android.cinematrix.models.Review;

/**
 * Created by mhesah on 2018-04-14.
 */

public class ReviewAdapter extends BaseAdapter {
    private final Context mContext;
    private final Review[] mReview;

    public ReviewAdapter(Context context, Review[] review) {
        mContext = context;
        mReview = review;
    }

    @Override
    public int getCount() {
        if (mReview == null || mReview.length == 0) {
            return -1;
        }
        return mReview.length;
    }

    @Override
    public Review getItem(int position) {
        if (mReview == null || mReview.length == 0) {
            return null;
        }
        return mReview[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView reviewer;
        TextView review;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.review_item, null);
        }

        reviewer = convertView.findViewById(R.id.text_view_reviewer);
        reviewer.setText(mReview[position].getAuthor());
        review = convertView.findViewById(R.id.text_view_review);
        review.setText(mReview[position].getContent());

        return convertView;
    }
}