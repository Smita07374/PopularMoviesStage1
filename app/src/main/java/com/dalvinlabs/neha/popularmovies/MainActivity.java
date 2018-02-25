package com.dalvinlabs.neha.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.graphics.Movie;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    GridView mGridView;
    URL url;
    TextView mErrorTextView;
    ProgressBar mLoadingIndicator;
    String[] posters;
    String[] titles;
    String[] overview;
    String[] releasedate;
    String[] votes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGridView = findViewById(R.id.grid_view);
        mErrorTextView = (TextView) findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        makeMovieSearchQuery(NetworkUtils.sortBy_Base);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intentSendMovieDetails = new Intent(MainActivity.this,MovieDetials.class);
                Bundle MOVIES_EXTRA = new Bundle();
                MOVIES_EXTRA.putString("Title", titles[i]);
                MOVIES_EXTRA.putString("Overview", overview[i]);
                MOVIES_EXTRA.putString("ReleaseDate", releasedate[i]);
                MOVIES_EXTRA.putString("Votes", votes[i]);
                MOVIES_EXTRA.putString("Poster",posters[i]);
                intentSendMovieDetails.putExtras(MOVIES_EXTRA);
                startActivity(intentSendMovieDetails);
            }
        });

    }


    void makeMovieSearchQuery(String x)
    {
        url = NetworkUtils.buildUrl(x);
        //Toast.makeText(this,""+url,Toast.LENGTH_LONG).show();
        if (isNetworkAvailable()) {
            new MovieQueryTask().execute(url);
        }
        else
        {
            showErrorMessage();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        switch (itemThatWasClickedId)
        {
            case R.id.action_search:
                makeMovieSearchQuery(NetworkUtils.sortBy_Base);
                break;
            case R.id.action_toprated:
                makeMovieSearchQuery(NetworkUtils.sortBy_TopRated);
                break;
            case R.id.action_popular:
                makeMovieSearchQuery(NetworkUtils.sortBy_Popular);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

   private void showJsonResponse()
    {
        mErrorTextView.setVisibility(View.INVISIBLE);
    }
    
   private void showErrorMessage()
    {
        mErrorTextView.setVisibility(View.VISIBLE);
        mErrorTextView.setText(R.string.error);
    }

    public class MovieQueryTask extends AsyncTask<URL, Void, String[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected String[] doInBackground(URL... urls) {
            URL searchedUrl = urls[0];
            String searchedUrlResults = null;
                try {
                    searchedUrlResults = NetworkUtils.getResponseFromHttpUrl(searchedUrl);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    return getMovieDataFromJson(searchedUrlResults);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            return null;
        }

        private String[] getMovieDataFromJson(String searchedUrlResults) throws JSONException{
            final String TAG_RESULTS = "results";
            final String TAG_MOVIE_POSTER = "poster_path";

            final String TAG_MOVIE_TITLE = "title";
            final String TAG_OVERVIEW = "overview";
            final String TAG_VOTE_AVG = "vote_average";
            final String TAG_RELEASE_DATE = "release_date";

            JSONObject moviesJson = new JSONObject(searchedUrlResults);
            JSONArray jsonResultsArray = moviesJson.getJSONArray(TAG_RESULTS);

            posters = new String[jsonResultsArray.length()];
            titles = new String[jsonResultsArray.length()];
            overview = new String[jsonResultsArray.length()];
            releasedate = new String[jsonResultsArray.length()];
            votes = new String[jsonResultsArray.length()];

            for (int i = 0;i<jsonResultsArray.length();i++)
            {
                JSONObject movieInfo = jsonResultsArray.getJSONObject(i);
                posters[i] = movieInfo.getString(TAG_MOVIE_POSTER);
                titles[i] =  movieInfo.getString(TAG_MOVIE_TITLE);
                overview[i] = movieInfo.getString(TAG_OVERVIEW);
                votes[i] = movieInfo.getString(TAG_VOTE_AVG);
                releasedate[i] = movieInfo.getString(TAG_RELEASE_DATE);
            }
            return posters;

        }

        @Override
        protected void onPostExecute(String[] s) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            String[] posters = s;
            if(s != null && !s.equals("")) {
                showJsonResponse();
                mGridView.setAdapter(new GridAdapter(MainActivity.this, s));
            }
            else
            {
                showErrorMessage();
            }
            return ;
        }
    }
    
}
