package com.androidnd.harshpatel.movies;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.HashMap;


public class FavoriteMovieGrid extends Fragment {

    private View root;
    private String SINGLE_URL;
    private HashMap<String, FavoriteMovie> favoriteMovieHashMap = new HashMap<>();
    private HashMap<String, Movie> movieHashMap = new HashMap<>();
    private GridView favMovieGrid;
    private ArrayList<Movie> movieList = new ArrayList<>();
    private String FIRST_MOVIE_POS = "FIRST_MOVIE_POS";

    public FavoriteMovieGrid() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        SINGLE_URL = getString(R.string.api_url_single_movie);

        if(savedInstanceState != null) {
            int index = savedInstanceState.getInt(FIRST_MOVIE_POS);
            favMovieGrid.smoothScrollToPosition(index);
        }

        Log.i("TAG_C", "ON CREATE");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("TAG_E", "ON CREATE VIEW");

        root =  inflater.inflate(R.layout.fragment_favorite_movie_grid, container, false);


        return root;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.i("TAG_E", "IN SET USER VISIBLE HINT");

        if(isVisibleToUser) {
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
    }

    public void getMovies() {


        ArrayList<String> currUrls = new ArrayList<>();

        for(final String movieId : favoriteMovieHashMap.keySet()) {

            Log.i("TAG_C", "GOT MOVIE ID: " + movieId);
            String curr_url = String.format(SINGLE_URL, movieId);
            currUrls.add(curr_url);
        }

        final MoviesViewModel moviesViewModel = ViewModelProviders.of(this).get(MoviesViewModel.class);

        moviesViewModel.getMovies(currUrls, getString(R.string.type_single_url)).observe(this, new Observer<ArrayList<Movie>>() {
            @Override
            public void onChanged(@Nullable ArrayList<Movie> movieArrayList) {

                Log.i("TAG_E", "MOVIE NAME: " + movieArrayList.toString());
                Log.i("TAG_E", "MOVIE LIST  SIZE: " + movieArrayList.size());

                if(movieArrayList != null ) {
                    movieHashMap = new HashMap<>();
                    for (Movie movie : movieArrayList) {
                        movieHashMap.put(movie.getId(), movie);
                    }

                    populateFavMovieGrid();
                }
            }
        });
    }

    public void populateFavMovieGrid() {

        movieList = new ArrayList<>();
        for(String key: movieHashMap.keySet()) {
            movieList.add(movieHashMap.get(key));
        }

        favMovieGrid = root.findViewById(R.id.favorite_movie_grid);
        favMovieGrid.setAdapter(new MovieImageAdapter(root.getContext(), movieList));
        favMovieGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Movie currMovie = movieList.get(i);
                if(favoriteMovieHashMap.containsKey(currMovie.getId())) {
                    currMovie.setIsFavorite(1);
                } else {
                    currMovie.setIsFavorite(0);
                }

                Intent movieShowCase = new Intent(view.getContext(), MovieShowCase.class);
                movieShowCase.putExtra(String.valueOf(R.string.movie_data_extra), currMovie);
                view.getContext().startActivity(movieShowCase);
            }
        });
        movieHashMap = new HashMap<>();
        favMovieGrid.refreshDrawableState();

    }

    @Override
    public void onResume() {
        Log.i("TAG_E", "ON RESUME");

        final FavMoviesViewModel viewModel = ViewModelProviders.of(this.getActivity()).get(FavMoviesViewModel.class);

        viewModel.getFavMovies(root.getContext()).observe(this, new Observer<FavoriteMovie []>() {

            @Override
            public void onChanged(@Nullable FavoriteMovie[] liveData) {
                favoriteMovieHashMap = new HashMap<>();

                if(liveData != null) {

                    Log.i("TAG_D", "FOUND FAV MOVIES: " + liveData);

                    for(FavoriteMovie movie : liveData) {
                        favoriteMovieHashMap.put(movie.getId(), movie);
                    }

                }

                getMovies();
            }

        });

        super.onResume();
    }

    @Override
    public void onStart() {
        Log.i("TAG_E", "ON START");
        super.onStart();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        int first_movie_position = favMovieGrid.getFirstVisiblePosition();

        outState.putInt(FIRST_MOVIE_POS, first_movie_position);

        super.onSaveInstanceState(outState);

        Log.i("TAG_C", "ON SAVE INSTANCE STATE");
    }
}
