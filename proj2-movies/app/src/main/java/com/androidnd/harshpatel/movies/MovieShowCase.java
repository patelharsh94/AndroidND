package com.androidnd.harshpatel.movies;

import android.arch.lifecycle.ViewModelProviders;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.HashMap;

public class MovieShowCase extends AppCompatActivity implements ReviewTrailerResultGetter{

    private Movie movie;
    private FavoriteMovie favoriteMovie;
    private CheckBox isFavorite;
    private Context root;
    private RecyclerView trailerRecyclerView;
    private RecyclerView reviewRecyclerView;
    private TrailerAdapter trailerAdapter;
    private ReviewsAdapter reviewsAdapter;
    private HashMap<String, String> trailerTitleMap = new HashMap<>();
    private HashMap<String, String> reviewMap = new HashMap<>();


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ReviewTrailerApiCallTask reviewTrailerApiCallTask;
        String reviewApiURL, trailerApiURL;

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

        favoriteMovie = new FavoriteMovie();
        favoriteMovie.setTitle(movie.getTitle());
        favoriteMovie.setId(movie.getId());

        trailerRecyclerView = findViewById(R.id.trailer_recycler_view);
        reviewRecyclerView = findViewById(R.id.review_recycler_view);


        reviewApiURL = String.format(root.getString(R.string.api_url_review), movie.getId(),
                                     BuildConfig.MOVIE_DB_API_KEY);
        reviewTrailerApiCallTask = new ReviewTrailerApiCallTask(root);
        reviewTrailerApiCallTask.resultGetter = this;
        reviewTrailerApiCallTask.execute(reviewApiURL, root.getString(R.string.type_review));

        trailerApiURL = String.format(root.getString(R.string.api_url_trailer),
                                      movie.getId(), BuildConfig.MOVIE_DB_API_KEY);
        reviewTrailerApiCallTask = new ReviewTrailerApiCallTask(root);
        reviewTrailerApiCallTask.resultGetter = this;
        reviewTrailerApiCallTask.execute(trailerApiURL, root.getString(R.string.type_trailer));


        Log.i("SHOW_CASE", movie.getTitle() + ":" + movie.getIsFavorite());

        if(movie.getIsFavorite() == 1) {
            isFavorite.setChecked(true);
        }
        else {
            isFavorite.setChecked(false);
        }

        isFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {

                    insertMovieInFav();

                    Log.i("TAG", movie.getTitle() + " CHECKED");
                }
                // if not checked
                else {
                    removeMovieFromFav();

                    Log.i("TAG", movie.getTitle() +" UNCHECKED");

                }
            }
        });

    }

    public void insertMovieInFav() {
        final FavMoviesViewModel viewModel = ViewModelProviders.of(this).get(FavMoviesViewModel.class);

        viewModel.insertFavMovie(favoriteMovie, root);
    }

    public void removeMovieFromFav() {
        final FavMoviesViewModel viewModel = ViewModelProviders.of(this).get(FavMoviesViewModel.class);

        viewModel.deleteFavMovie(favoriteMovie, root);
    }

    @Override
    protected void onStop() {
        super.onStop();
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

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

}
