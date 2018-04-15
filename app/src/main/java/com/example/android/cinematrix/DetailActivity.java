package com.example.android.cinematrix;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.cinematrix.adapters.ReviewAdapter;
import com.example.android.cinematrix.adapters.TrailerAdapter;
import com.example.android.cinematrix.interfaces.ReviewTaskCompleted;
import com.example.android.cinematrix.interfaces.TrailerTaskCompleted;
import com.example.android.cinematrix.models.Movie;
import com.example.android.cinematrix.models.Review;
import com.example.android.cinematrix.models.Trailer;
import com.example.android.cinematrix.utilities.ReviewAsyncTask;
import com.example.android.cinematrix.utilities.TrailerAsyncTask;
import com.squareup.picasso.Picasso;

import static com.example.android.cinematrix.MainActivity.PARCEL_MOVIE;
import static com.example.android.cinematrix.adapters.MovieAdapter.POSTER_HEIGHT;
import static com.example.android.cinematrix.adapters.MovieAdapter.POSTER_WIDTH;

/**
 * Created by mhesah on 2018-03-30.
 */

public class DetailActivity extends AppCompatActivity implements TrailerTaskCompleted, ReviewTaskCompleted {

    public static final String PARCEL_TRAILER = "PARCEL_TRAILER";
    public static final String PARCEL_REVIEW = "PARCEL_REVIEW";
    private static final String MISSING_INFO = "missing info";
    private static final String NO_INTERNET = "No internet connection found";
    private static final String YOUTUBE = "http://www.youtube.com/watch?v=";
    ImageView poster;
    TextView title, vote, release, plot;

    ListView listViewTrailer, listViewReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        poster = findViewById(R.id.image_view_poster);
        title = findViewById(R.id.text_view_title);
        vote = findViewById(R.id.text_view_vote);
        release = findViewById(R.id.text_view_release);
        plot = findViewById(R.id.text_view_plot);
        listViewTrailer = findViewById(R.id.list_view_trailer);
        listViewReview = findViewById(R.id.list_view_review);

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
        } else vote.setText(MISSING_INFO);

        if (movie.getRelease() != null) {
            release.setText("" + movie.getRelease());
        } else release.setText(MISSING_INFO);

        if (movie.getPlot() != null) {
            plot.setText("" + movie.getPlot());
        } else plot.setText(MISSING_INFO);

        listViewTrailer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Trailer trailer = (Trailer) parent.getItemAtPosition(position);
                String key = trailer.getTrailerKey();
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE + key)));
            }
        });

        if (savedInstanceState == null) {
            getTrailer(movie.getId());
            getReview(movie.getId());
        } else {
            Parcelable[] parcelableTrailer = savedInstanceState.getParcelableArray(PARCEL_TRAILER);
            Parcelable[] parcelableReview = savedInstanceState.getParcelableArray(PARCEL_REVIEW);

            if (parcelableTrailer != null) {
                int trailerCount = parcelableTrailer.length;
                Trailer[] trailerArray = new Trailer[trailerCount];
                for (int i = 0; i < trailerCount; i++) {
                    trailerArray[i] = (Trailer) parcelableTrailer[i];
                }
                listViewTrailer.setAdapter(new TrailerAdapter(this, trailerArray));
            }

            if (parcelableReview != null) {
                int reviewCount = parcelableReview.length;
                Review[] reviewArray = new Review[reviewCount];
                for (int i = 0; i < reviewCount; i++) {
                    reviewArray[i] = (Review) parcelableReview[i];
                }
                listViewReview.setAdapter(new ReviewAdapter(this, reviewArray));
            }
        }
    }

    // setting action bar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.favourite_add:
                favourite_add();
                return true;
            case R.id.favourite_remove:
                favourite_remove();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        int trailerCount = listViewTrailer.getCount();
        if (trailerCount > 0) {
            Trailer[] trailer = new Trailer[trailerCount];
            for (int i = 0; i < trailerCount; i++) {
                trailer[i] = (Trailer) listViewTrailer.getItemAtPosition(i);
            }
            outState.putParcelableArray(PARCEL_TRAILER, trailer);
        }

        int reviewCount = listViewReview.getCount();
        if (reviewCount > 0) {
            Review[] review = new Review[reviewCount];
            for (int i = 0; i < reviewCount; i++) {
                review[i] = (Review) listViewReview.getItemAtPosition(i);
            }
            outState.putParcelableArray(PARCEL_REVIEW, review);
        }

        super.onSaveInstanceState(outState);
    }

    private void getTrailer(String key) {
        if (isNetworkAvailable()) {
            TrailerTaskCompleted taskCompleted = new TrailerTaskCompleted() {
                @Override
                public void trailerTaskCompleted(Trailer[] trailer) {

                }
            };

            TrailerAsyncTask trailerAsyncTask = new TrailerAsyncTask(taskCompleted);
            trailerAsyncTask.execute(key);
        } else {
            Toast.makeText(this, NO_INTERNET, Toast.LENGTH_LONG).show();
        }
    }

    private void getReview(String key) {
        if (isNetworkAvailable()) {
            ReviewTaskCompleted taskCompleted = new ReviewTaskCompleted() {
                @Override
                public void reviewTaskCompleted(Review[] review) {

                }
            };

            ReviewAsyncTask reviewAsyncTask = new ReviewAsyncTask(taskCompleted);
            reviewAsyncTask.execute(key);
        } else {
            Toast.makeText(this, NO_INTERNET, Toast.LENGTH_LONG).show();
        }
    }

    private void favourite_add() {
        // TODO: 2018-04-14 add to sql db if doesnt exist
    }

    private void favourite_remove() {
        // TODO: 2018-04-14 remove from sql db if exists
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void trailerTaskCompleted(Trailer[] trailer) {

    }

    @Override
    public void reviewTaskCompleted(Review[] review) {

    }
}