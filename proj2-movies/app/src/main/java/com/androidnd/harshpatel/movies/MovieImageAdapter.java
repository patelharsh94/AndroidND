package com.androidnd.harshpatel.movies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

/**
 * Created by harsh.patel on 5/18/18.
 */

public class MovieImageAdapter extends BaseAdapter{

    private Context mContext;
    private ArrayList<Movie> movieList = null;
    private String IMAGE_API_BASE_URL = "http://image.tmdb.org/t/p/w185";


    public MovieImageAdapter(Context context, ArrayList<Movie> movies) {
        this.mContext = context;
        movieList = new ArrayList<>();
        for(Movie movie : movies) {
            if(!movieList.contains(movie)) {
                movieList.add(movie);
            }
        }
    }

    @Override
    public int getCount() {

        if (movieList != null)
        {
            return movieList.size();
        }

        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView imageView = new ImageView(mContext);
        TextView textView;

        View movie_grid;
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        if(view == null) {

            movie_grid = layoutInflater.inflate(R.layout.movie_with_title, null);
            imageView = movie_grid.findViewById(R.id.movie_image);
            textView = movie_grid.findViewById(R.id.movie_title);
            textView.setText(movieList.get(i).getTitle());
        }
        else {
            movie_grid = view;
        }

        Picasso.with(imageView.getContext()).load(IMAGE_API_BASE_URL+movieList.get(i).getThumbnailUrl()).into(imageView);

        return movie_grid;
    }

    public void setData(ArrayList<Movie> movies) {
        this.movieList = movies;
        notifyDataSetChanged();
    }
}
