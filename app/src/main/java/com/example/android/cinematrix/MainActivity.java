package com.example.android.cinematrix;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.android.cinematrix.adapters.MovieAdapter;
import com.example.android.cinematrix.interfaces.MovieTaskCompleted;
import com.example.android.cinematrix.models.Movie;
import com.example.android.cinematrix.utilities.FavouriteAsyncTask;
import com.example.android.cinematrix.utilities.MovieAsyncTask;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MovieTaskCompleted {

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
            ArrayList<Movie> movie = savedInstanceState.getParcelableArrayList(PARCEL_MOVIE);

            if (movie != null) {
                gridView.setAdapter(new MovieAdapter(this, movie));
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
            case R.id.library:
                getLocalData(getBaseContext());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        int movieCount = gridView.getCount();
        if (movieCount > 0) {
            ArrayList<Movie> movie = new ArrayList<>();
            for (int i = 0; i < movieCount; i++) {
                movie.add((Movie) gridView.getItemAtPosition(i));
            }
            outState.putParcelableArrayList(PARCEL_MOVIE, movie);
        }

        super.onSaveInstanceState(outState);
    }

    private void getData(String sortType)  {
        if (isNetworkAvailable()) {
            MovieTaskCompleted taskCompleted = new MovieTaskCompleted() {
                @Override
                public void movieTaskCompleted(ArrayList<Movie> movie) {
                    gridView.setAdapter(new MovieAdapter(getApplicationContext(), movie));
                }
            };

            MovieAsyncTask movieAsyncTask = new MovieAsyncTask(taskCompleted);
            movieAsyncTask.execute(sortType);
        } else {
            Toast.makeText(this, NO_INTERNET, Toast.LENGTH_LONG).show();
        }
    }

    private void getLocalData(Context context)  {
        MovieTaskCompleted taskCompleted = new MovieTaskCompleted() {
            @Override
            public void movieTaskCompleted(ArrayList<Movie> movie) {
                gridView.setAdapter(new MovieAdapter(getApplicationContext(), movie));
            }
        };

        FavouriteAsyncTask favouriteAsyncTask = new FavouriteAsyncTask(taskCompleted, context);
        favouriteAsyncTask.execute();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void movieTaskCompleted(ArrayList<Movie> movie) {

    }
}