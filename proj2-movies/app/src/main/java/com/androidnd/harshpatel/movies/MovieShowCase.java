package com.androidnd.harshpatel.movies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieShowCase extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String ratingString = "%s/10";
        String totalVotesText = "Total Votes: %s";
        setContentView(R.layout.activity_movie_show_case);
        Intent intent = getIntent();
        String IMAGE_API_BASE_URL = "http://image.tmdb.org/t/p/w342";
        Toolbar movieTitleToolbar = findViewById(R.id.title_toolbar);
        ImageView moviePoster = findViewById(R.id.movie_poster);
        ImageView movieThumbnail = findViewById(R.id.movie_thumbnail);
        TextView movieRatings = findViewById(R.id.ratings);
        TextView movieReleaseDate = findViewById(R.id.release_date);
        TextView movieSummary = findViewById(R.id.movie_summary);
        TextView totalVotes = findViewById(R.id.total_votes);
        Movie movie = intent.getParcelableExtra(String.valueOf(R.string.movie_data_extra));

        Log.i("TAG", "MOVIE POSTER: " + IMAGE_API_BASE_URL+movie.getPosterUrl() );
        movieTitleToolbar.setTitle(movie.getTitle());
        Picasso.with(moviePoster.getContext()).load(IMAGE_API_BASE_URL+movie.getPosterUrl()).into(moviePoster);
        Picasso.with(movieThumbnail.getContext()).load(IMAGE_API_BASE_URL+movie.getThumbnailUrl()).into(movieThumbnail);
        movieRatings.setText(String.format(ratingString, movie.getVote_average()));
        movieReleaseDate.setText(movie.getReleaseData());
        movieSummary.setText(movie.getSummary());
        totalVotes.setText(String.format(totalVotesText, movie.getVote_count()));
    }
}
