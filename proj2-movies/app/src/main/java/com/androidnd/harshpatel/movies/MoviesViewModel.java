package com.androidnd.harshpatel.movies;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import java.util.ArrayList;

public class MoviesViewModel extends ViewModel implements MovieDBAPIResultGetter {

    private MutableLiveData<ArrayList<Movie>> movieList;
    private MovieApiCallTask apiCallTask;
    private ArrayList<String> API_URLS;

    public MoviesViewModel() {
        super();
    }

    public LiveData<ArrayList<Movie>> getMovies(ArrayList<String> API_URLS, String type) {

        this.API_URLS = API_URLS;
        if(movieList == null) {
            movieList = new MutableLiveData<ArrayList<Movie>>();
        }

        loadMovies(type);


        return movieList;
    }

    @Override
    public void getMovieApiResult(ArrayList<Movie> movieList) {
        this.movieList.setValue(movieList);
    }

    private void loadMovies(String type) {
        API_URLS.add(type);
        apiCallTask = new MovieApiCallTask();
        apiCallTask.resultGetter = this;
        apiCallTask.execute(API_URLS.toArray(new String [API_URLS.size()]));
    }
}
