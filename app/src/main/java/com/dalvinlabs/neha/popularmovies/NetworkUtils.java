package com.dalvinlabs.neha.popularmovies;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

/**
 * Created by neha on 01/02/18.
 */

public class NetworkUtils {
    //MOVIE API and Key
    final static String MOVIE_BASE_URL ="http://api.themoviedb.org/3";
    final static String POSTER_BASE_URL = "http://image.tmdb.org/t/p/w185/";
    final static String MY_API_KEY = "8d8eb23d2c5bfee6de3a49813d686eb5";//KEY Removed

    final static String PARAM_APIKEY = "api_key";
    final static String sortBy_TopRated = "movie/top_rated";
    final static String sortBy_Popular ="movie/popular";
    final static String sortBy_Base ="discover/movie";

    public static URL buildUrl(String movieSearchQuery) {
        Uri uribuilt = Uri.parse(MOVIE_BASE_URL).buildUpon()
                .appendEncodedPath(movieSearchQuery)
                .appendQueryParameter(PARAM_APIKEY,MY_API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(uribuilt.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException
    {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream inputStream = urlConnection.getInputStream();
            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();
            if(hasInput)
            { return scanner.next();}
            else
            {return null;}
        }
        finally {
            urlConnection.disconnect();
        }

    }
}
