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


public class TopRatedMovieGrid extends Fragment implements FavMovieDBResultGetter {

    private String API_URL;
    private ArrayList<Movie> movieList = new ArrayList<>();
    private View root;
    private GridView movie_grid;
    private FavMovieDataOpsTask favMovieDataOpsTask;
    private ArrayList<FavoriteMovie> favoriteMovies = new ArrayList<>();
    private ArrayList<String> favMovieIds = new ArrayList<>();
    private String TOP_RATED_KEY = "TOP_RATED_MOVIES_LIST";

    public TopRatedMovieGrid() {
        // Required empty public constructor
    }

    public void populateMovieGrid(final ArrayList<Movie> movieList) {
        this.movieList = movieList;
        movie_grid =  root.findViewById(R.id.top_rated_grid);

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_top_rated_movie_grid, container, false);
        API_URL =  this.getResources().getString(R.string.api_url_top_rated_movie);

        final MoviesViewModel viewModel = ViewModelProviders.of(this).get(MoviesViewModel.class);
        ArrayList<String> top_movie_urls = new ArrayList<>();
        top_movie_urls.add(API_URL);
        viewModel.getMovies(top_movie_urls, getString(R.string.type_top_rated)).observe(this, new Observer<ArrayList<Movie>>() {
            @Override
            public void onChanged(@Nullable ArrayList<Movie> movieArrayList) {
                populateMovieGrid(movieArrayList);
            }
        });


        return root;
    }



    @Override
    public void onStart() {
        super.onStart();
        favMovieDataOpsTask = new FavMovieDataOpsTask(root.getContext());
        favMovieDataOpsTask.resultGetter = this;

        favMovieDataOpsTask.execute(root.getContext().getString(R.string.get_all_fav_movie), "", "");
        Log.i("TAG", "IN ON START");
    }

    @Override
    public void getFavMovieData(ArrayList<FavoriteMovie> favoriteMovies) {
        this.favoriteMovies = favoriteMovies;

        for(FavoriteMovie favoriteMovie : favoriteMovies) {
            favMovieIds.add(favoriteMovie.getId());
        }

        Log.i("TAG", favoriteMovies.toString());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if(savedInstanceState != null) {
            this.movieList = savedInstanceState.getParcelableArrayList(TOP_RATED_KEY);
        }

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(TOP_RATED_KEY, this.movieList);
        super.onSaveInstanceState(outState);
    }
}
