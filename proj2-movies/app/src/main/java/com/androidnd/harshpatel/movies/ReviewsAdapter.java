package com.androidnd.harshpatel.movies;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsViewHolder>  {

    public ArrayList<String> reviewsList;

    public class ReviewsViewHolder extends RecyclerView.ViewHolder {

        TextView review;

        public ReviewsViewHolder(View view) {
            super(view);

            review = view.findViewById(R.id.movie_review);
        }

    }

    public ReviewsAdapter(HashMap<String, String> reviewsMap) {

        reviewsList = new ArrayList<>();
        for(String key : reviewsMap.keySet()) {
            this.reviewsList.add(reviewsMap.get(key));
        }

    }

    @Override
    public ReviewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent, false);

        return new ReviewsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ReviewsViewHolder holder, int position) {
        holder.review.setText(reviewsList.get(position));

    }

    @Override
    public int getItemCount() {
        return reviewsList.size();
    }



}
