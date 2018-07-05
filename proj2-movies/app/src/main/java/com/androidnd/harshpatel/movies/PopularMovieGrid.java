package com.androidnd.harshpatel.movies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

public class PopularMovieGrid extends Fragment implements MovieDBAPIResultGetter, FavMovieDBResultGetter {

    private String API_URL;
    private MovieApiCallTask apiCallTask;
    private ArrayList<Movie> movieList = new ArrayList<>();
    private View root;
    private GridView movie_grid;
    private FavMovieDataOpsTask favMovieDataOpsTask;
    private ArrayList<FavoriteMovie> favoriteMovies = new ArrayList<>();
    private ArrayList<String> favMovieIds = new ArrayList<>();


    public PopularMovieGrid() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_popular_movie_grid, container, false);
        API_URL =  this.getResources().getString(R.string.api_url_popular_movie);
        apiCallTask = new MovieApiCallTask(root);
        apiCallTask.resultGetter = this;
        apiCallTask.execute(API_URL, String.valueOf(R.string.type_popular));

        return root;
    }

    @Override
    public void getMovieApiResult(final ArrayList<Movie> movieList) {
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

}
   