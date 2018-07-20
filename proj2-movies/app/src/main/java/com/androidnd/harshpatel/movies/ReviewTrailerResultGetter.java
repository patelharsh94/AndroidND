package com.androidnd.harshpatel.movies;

import java.util.HashMap;

public interface ReviewTrailerResultGetter {

    void getReviewData(HashMap<String, String> reviewList);
    void getTrailerData(HashMap<String, String> trailerList);
}
