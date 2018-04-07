package com.example.android.cinematrix;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.cinematrix.model.Movie;
import com.squareup.picasso.Picasso;

import static com.example.android.cinematrix.MainActivity.PARCEL_MOVIE;
import static com.example.android.cinematrix.utils.ImageAdapter.POSTER_HEIGHT;
import static com.example.android.cinematrix.utils.ImageAdapter.POSTER_WIDTH;

/**
 * Created by mhesah on 2018-03-30.
 */

public class DetailActivity extends AppCompatActivity {

    private static final String MISSING_INFO = "missing info";

    ImageView poster;
    TextView title, vote, release, plot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        poster = findViewById(R.id.image_view_poster);
        title = findViewById(R.id.text_view_title);
        vote = findViewById(R.id.text_view_vote);
        release = findViewById(R.id.text_view_release);
        plot = findViewById(R.id.text_view_plot);

        Intent intent = getIntent();
        Movie movie = intent.getParcelableExtra(PARCEL_MOVIE);

        /*
         setting views
          */
        Picasso.with(this)
                .load(movie.getPoster())
                .resize(POSTER_WIDTH, POSTER_HEIGHT)
                .error(R.drawable.error_retrieving_movies)
                .placeholder(R.drawable.retrieving_movies)
                .into(poster);

        title.setText(movie.getTitle());

        if (movie.getVote() != null) {
            vote.setText("" + movie.getVote());
        }else vote.setText(MISSING_INFO);

        if (movie.getRelease() != null) {
            release.setText("" + movie.getRelease());
        }else release.setText(MISSING_INFO);

        if (movie.getPlot() != null) {
            plot.setText("" + movie.getPlot());
        }else plot.setText(MISSING_INFO);
    }
}