package com.example.maii.moviespopcorn;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by MAII on 1/7/2559.
 */
public class RetrofitBuilder {
    private static RetrofitBuilder instance;

    public static RetrofitBuilder getInstance(){
        if (instance == null)
            instance = new RetrofitBuilder();
            return instance;
    }

    private Context mContext;
    private TMDbAPI tmDbAPI;

    private RetrofitBuilder() {

        mContext = Contextor.getInstance().getContext();

        String BASE_URL = "https://api.themoviedb.org/3/movie/";

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        tmDbAPI = retrofit.create(TMDbAPI.class);
    }
    public TMDbAPI gettmDbAPI(){
            return tmDbAPI;
    }
}

