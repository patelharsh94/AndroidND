package com.androidnd.harshpatel.movies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class MovieShowCase extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_show_case);
        Intent intent = getIntent();
        String IMAGE_API_BASE_URL = "http://image.tmdb.org/t/p/w342";
        Toolbar movieTitleToolbar = findViewById(R.id.title_toolbar);
        ImageView movieposter = findViewById(R.id.movie_poster);
        Movie movie = intent.getParcelableExtra(String.valueOf(R.string.movie_data_extra));

        Log.i("TAG", "MOVIE POSTER: " + IMAGE_API_BASE_URL+movie.getPosterUrl() );
        movieTitleToolbar.setTitle(movie.getTitle());
        Picasso.with(movieposter.getContext()).load(IMAGE_API_BASE_URL+movie.getPosterUrl()).into(movieposter);

    }
}
