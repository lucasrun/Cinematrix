package com.example.android.cinematrix;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.android.cinematrix.model.Movie;
import com.example.android.cinematrix.utils.ImageAdapter;
import com.example.android.cinematrix.utils.MovieAsyncTask;
import com.example.android.cinematrix.utils.OnTaskCompleted;

public class MainActivity extends AppCompatActivity implements OnTaskCompleted {

    public static final String PARCEL_MOVIE = "PARCEL_MOVIE";
    private static final String SORT_BY_POPULARITY = "popular";
    private static final String SORT_BY_RATING = "top_rated";
    private static final String NO_INTERNET = "No internet connection found";
    GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridView = findViewById(R.id.grid_view);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = (Movie) parent.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                intent.putExtra(PARCEL_MOVIE, movie);
                startActivity(intent);
            }
        });

        if (savedInstanceState == null) {
            getData(SORT_BY_POPULARITY);
        } else {
            Parcelable[] parcelable = savedInstanceState.getParcelableArray(PARCEL_MOVIE);

            if (parcelable != null) {
                int movieCount = parcelable.length;
                Movie[] movie = new Movie[movieCount];
                for (int i = 0; i < movieCount; i++) {
                    movie[i] = (Movie) parcelable[i];
                }
                gridView.setAdapter(new ImageAdapter(this, movie));
            }
        }
    }

    // setting action bar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.most_popular:
                getData(SORT_BY_POPULARITY);
                return true;
            case R.id.top_rated:
                getData(SORT_BY_RATING);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        int movieCount = gridView.getCount();
        if (movieCount > 0) {
            Movie[] movie = new Movie[movieCount];
            for (int i = 0; i < movieCount; i++) {
                movie[i] = (Movie) gridView.getItemAtPosition(i);
            }
            outState.putParcelableArray(PARCEL_MOVIE, movie);
        }

        super.onSaveInstanceState(outState);
    }

    private void getData(String sortType)  {
        if (isNetworkAvailable()) {
            OnTaskCompleted taskCompleted = new OnTaskCompleted() {
                @Override
                public void onTaskCompleted(Movie[] movies) {
                    gridView.setAdapter(new ImageAdapter(getApplicationContext(), movies));
                }
            };

            MovieAsyncTask movieAsyncTask = new MovieAsyncTask(taskCompleted);
            movieAsyncTask.execute(sortType);
        } else {
            Toast.makeText(this, NO_INTERNET, Toast.LENGTH_LONG).show();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onTaskCompleted(Movie[] movie) {

    }
}