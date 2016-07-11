package com.example.maii.moviespopcorn;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by MAII on 1/7/2559.
 */
public class AllMovieList {
    @SerializedName("results")
    List<MoviesList> results;

    public List<MoviesList> getResults() {
        return results;
    }

    public void setResults(List<MoviesList> results) {
        this.results = results;
    }
}
