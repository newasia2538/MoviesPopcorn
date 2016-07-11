package com.example.maii.moviespopcorn;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by MAII on 30/6/2559.
 */
public interface TMDbAPI {


    @GET("top_rated")
    Call<AllMovieList> listTopRated(@Query("api_key") String id);

    @GET("popular")
    Call<AllMovieList> listHighPopular(@Query("api_key") String id);

}

