package com.androidnd.harshpatel.movies;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;


public class TopRatedMovieGrid extends Fragment implements MovieDBAPIResultGetter {

    private String API_URL = "https://api.themoviedb.org/3/movie/top_rated?api_key=f3ef1a64d5d3b1d4605bf13f2a17ac88\n";
    private MovieApiCallTask apiCallTask;
    private ArrayList<Movie> movieList = null;


    public TopRatedMovieGrid() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_top_rated_movie_grid, container, false);

        apiCallTask = new MovieApiCallTask(root);
        apiCallTask.resultGetter = this;
        apiCallTask.execute(API_URL, String.valueOf(R.string.type_top_rated));

        return root;
    }

    @Override
    public void getMovieApiResult(ArrayList<Movie> movieList) {
        this.movieList = movieList;
    }

}