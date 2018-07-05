package com.androidnd.harshpatel.movies;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class MovieShowCase extends AppCompatActivity implements FavMovieDBResultGetter, ReviewTrailerResultGetter{

    private ArrayList<FavoriteMovie> favoriteMoviesList = new ArrayList<>();
    private Movie movie;
    private FavoriteMovie favoriteMovie;
    private CheckBox isFavorite;
    private Context root;
    private FavMovieDBManipulator favMovieDBManipulator;
    private boolean isFavoriteChecked = false;
    private FavMovieDataOpsTask favMovieDataOpsTask;
    private ArrayList<FavoriteMovie> favoriteMovies = new ArrayList<>();
    private ArrayList<String> favMovieIds = new ArrayList<>();
    private RecyclerView trailerRecyclerView;
    private RecyclerView reviewRecyclerView;
    private TrailerAdapter trailerAdapter;
    private ReviewsAdapter reviewsAdapter;
    private HashMap<String, String> trailerTitleMap = new HashMap<>();
    private HashMap<String, String> reviewMap = new HashMap<>();
    private ReviewTrailerApiCallTask reviewTrailerApiCallTask;
    private String reviewApiURL, trailerApiURL;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        root = getBaseContext();
        String ratingString = "%s/10";
        String totalVotesText = "Total Votes: %s";
        setContentView(R.layout.activity_movie_show_case);
        final Intent intent = getIntent();
        String IMAGE_API_BASE_URL = "http://image.tmdb.org/t/p/w342";
        Toolbar movieTitleToolbar = findViewById(R.id.title_toolbar);
        ImageView moviePoster = findViewById(R.id.movie_poster);
        ImageView movieThumbnail = findViewById(R.id.movie_thumbnail);
        TextView movieRatings = findViewById(R.id.ratings);
        TextView movieReleaseDate = findViewById(R.id.release_date);
        TextView movieSummary = findViewById(R.id.movie_summary);
        TextView totalVotes = findViewById(R.id.total_votes);
        movie = intent.getParcelableExtra(String.valueOf(R.string.movie_data_extra));
        isFavorite = findViewById(R.id.fav_choice_button);
        movieTitleToolbar.setTitle(movie.getTitle());
        Picasso.with(moviePoster.getContext()).load(IMAGE_API_BASE_URL+movie.getPosterUrl()).into(moviePoster);
        Picasso.with(movieThumbnail.getContext()).load(IMAGE_API_BASE_URL+movie.getThumbnailUrl()).into(movieThumbnail);
        movieRatings.setText(String.format(ratingString, movie.getVote_average()));
        movieReleaseDate.setText(movie.getReleaseData());
        movieSummary.setText(movie.getSummary());
        totalVotes.setText(String.format(totalVotesText, movie.getVote_count()));

        trailerRecyclerView = findViewById(R.id.trailer_recycler_view);
        trailerAdapter = new TrailerAdapter(trailerTitleMap);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        trailerRecyclerView.setLayoutManager(layoutManager);
        trailerRecyclerView.setItemAnimator(new DefaultItemAnimator());
        trailerRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        trailerRecyclerView.setAdapter(trailerAdapter);
        trailerAdapter.notifyDataSetChanged();

        reviewRecyclerView = findViewById(R.id.review_recycler_view);
        reviewsAdapter = new ReviewsAdapter(reviewMap);
        RecyclerView.LayoutManager reviewLayoutManager = new LinearLayoutManager(getApplicationContext());

        reviewRecyclerView.setLayoutManager(reviewLayoutManager);
        reviewRecyclerView.setItemAnimator(new DefaultItemAnimator());
        reviewRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        reviewRecyclerView.setAdapter(reviewsAdapter);
        reviewsAdapter.notifyDataSetChanged();


        favMovieDataOpsTask = new FavMovieDataOpsTask(root);
        favMovieDataOpsTask.resultGetter = this;
        favMovieDataOpsTask.execute(root.getString(R.string.get_all_fav_movie), "", "");

        favoriteMovie = new FavoriteMovie();
        favoriteMovie.setTitle(movie.getTitle());
        favoriteMovie.setId(movie.getId());

        trailerRecyclerView = findViewById(R.id.trailer_recycler_view);
        reviewRecyclerView = findViewById(R.id.review_recycler_view);


        reviewApiURL = String.format(root.getString(R.string.api_url_review), movie.getId());
        reviewTrailerApiCallTask = new ReviewTrailerApiCallTask(root);
        reviewTrailerApiCallTask.resultGetter = this;
        reviewTrailerApiCallTask.execute(reviewApiURL, root.getString(R.string.type_review));

        trailerApiURL = String.format(root.getString(R.string.api_url_trailer), movie.getId());
        reviewTrailerApiCallTask = new ReviewTrailerApiCallTask(root);
        reviewTrailerApiCallTask.resultGetter = this;
        reviewTrailerApiCallTask.execute(trailerApiURL, root.getString(R.string.type_trailer));


        Log.i("SHOW_CASE", movie.getTitle() + ":" + movie.getIsFavorite());


        isFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {

                    isFavoriteChecked = true;

                    Log.i("TAG", movie.getTitle() + " CHECKED");
                }
                // if not checked
                else {
                    isFavoriteChecked = false;

                    Log.i("TAG", movie.getTitle() +" UNCHECKED");

                }
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        favMovieDataOpsTask = new FavMovieDataOpsTask(root);
        favMovieDataOpsTask.resultGetter = this;

        if(isFavoriteChecked)
        {
            favMovieDataOpsTask.execute(root.getString(R.string.insert_fav_movie), favoriteMovie.getId(), favoriteMovie.getTitle());
        }
        else {

            favMovieDataOpsTask.execute(root.getString(R.string.delete_fav_movie), favoriteMovie.getId(), favoriteMovie.getTitle());
        }
    }

    @Override
    public void getFavMovieData(ArrayList<FavoriteMovie> favoriteMovies) {
        this.favoriteMovies = favoriteMovies;

        for(FavoriteMovie favoriteMovie : favoriteMovies) {
            favMovieIds.add(favoriteMovie.getId());
        }


        if(favMovieIds.contains(favoriteMovie.getId()))
        {
            isFavoriteChecked = true;
        }
        else {
            isFavoriteChecked = false;
        }

        isFavorite.setChecked(isFavoriteChecked);

        Log.i("TAG_A", favoriteMovies.toString());
    }

    @Override
    public void getReviewData(HashMap<String, String> reviewMap) {
        this.reviewMap = reviewMap;
        Log.i("TAG", this.reviewMap.toString());

        reviewsAdapter = new ReviewsAdapter(reviewMap);
        RecyclerView.LayoutManager reviewLayoutManager = new LinearLayoutManager(getApplicationContext());

        reviewRecyclerView.setLayoutManager(reviewLayoutManager);
        reviewRecyclerView.setItemAnimator(new DefaultItemAnimator());
        reviewRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        reviewRecyclerView.setAdapter(reviewsAdapter);
        reviewsAdapter.notifyDataSetChanged();

    }

    @Override
    public void getTrailerData(HashMap<String, String> trailerMap) {
        this.trailerTitleMap = trailerMap;
        Log.i("TAG", this.trailerTitleMap.toString());

        trailerAdapter = new TrailerAdapter(trailerTitleMap);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        trailerRecyclerView.setLayoutManager(layoutManager);
        trailerRecyclerView.setItemAnimator(new DefaultItemAnimator());
        trailerRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        trailerRecyclerView.setAdapter(trailerAdapter);
        trailerAdapter.notifyDataSetChanged();


    }
}
