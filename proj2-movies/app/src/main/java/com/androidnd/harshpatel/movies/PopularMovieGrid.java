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
import android.widget.TextView;

import java.util.ArrayList;

public class PopularMovieGrid extends Fragment {

    private String API_URL;
    private ArrayList<Movie> movieList = new ArrayList<>();
    private View root;
    private GridView movie_grid;
    private ArrayList<FavoriteMovie> favoriteMovies = new ArrayList<>();
    private ArrayList<String> favMovieIds = new ArrayList<>();
    private String POP_MOVIE_KEY = "POPULAR_MOVIES_LIST";

    public PopularMovieGrid() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        if(savedInstanceState != null) {
            this.movieList = savedInstanceState.getParcelableArrayList(POP_MOVIE_KEY);
        }

        super.onCreate(savedInstanceState);
    }

    public void populateMovieGrid(final ArrayList<Movie> movieList) {
        this.movieList = movieList;
        movie_grid =  root.findViewById(R.id.most_popular_grid);

        movie_grid.setAdapter(new MovieImageAdapter(root.getContext(), movieList));
        movie_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Movie currMovie = movieList.get(i);

                Intent movieShowCase = new Intent(view.getContext(), MovieShowCase.class);
                movieShowCase.putExtra(String.valueOf(R.string.movie_data_extra), currMovie);
                view.getContext().startActivity(movieShowCase);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        API_URL =  this.getResources().getString(R.string.api_url_popular_movie);

        final MoviesViewModel viewModel = ViewModelProviders.of(this).get(MoviesViewModel.class);
        ArrayList<String> pop_movie_urls = new ArrayList<>();
        pop_movie_urls.add(API_URL);

        viewModel.getMovies(pop_movie_urls, getString(R.string.type_popular)).observe(this, new Observer<ArrayList<Movie>>() {
            @Override
            public void onChanged(@Nullable ArrayList<Movie> movieArrayList) {
                Log.i("TAG","FOUND MOVIE LIST: " + movieArrayList);
                populateMovieGrid(movieArrayList);
            }
        });

        root = inflater.inflate(R.layout.fragment_popular_movie_grid, container, false);

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

        final FavMoviesViewModel viewModel = ViewModelProviders.of(this.getActivity()).get(FavMoviesViewModel.class);

        viewModel.getFavMovies(root.getContext()).observe(this, new Observer<ArrayList<FavoriteMovie>>() {
            @Override
            public void onChanged(@Nullable ArrayList<FavoriteMovie> favoriteMovieArrayList) {
                if(favoriteMovieArrayList != null)
                {
                    favoriteMovies = favoriteMovieArrayList;

                    for(FavoriteMovie favoriteMovie : favoriteMovies) {
                        favMovieIds.add(favoriteMovie.getId());
                    }

                }

                Log.i("TAG_C", "GOT FAV MOVIES IN POP: " + favoriteMovieArrayList.toString());
            }
        });


        Log.i("TAG", "IN ON START");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putParcelableArrayList(POP_MOVIE_KEY, this.movieList);
        super.onSaveInstanceState(outState);
    }
}
   