package com.example.maii.moviespopcorn;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    GridView gridView;
    String id = "f76f874a27c86a07070dc3d9bd82009a";
    List<MoviesList> itemList;
    SwipeRefreshLayout swipeRefreshLayout;
    ProgressBar relativeProgressBar;
    Boolean checkMenusPopular = true;
    Parcelable onRestoreParcelable;
    Intent intent;


    @Override
    protected void onResume() {
        super.onResume();
        if (itemList == null) {
            if (checkMenusPopular) {
                getPopularMoviesFromServer();
                getSupportActionBar().setTitle("Popular Movies");

                checkMenusPopular = true;
            } else {
                getTopRatedMoviesFromServer();
                getSupportActionBar().setTitle("Top Rated Movies");

                checkMenusPopular = false;
            }
        } else {
            gridView.setAdapter(new CustomAdapter(this, itemList));

            if (itemList != null && onRestoreParcelable != null) {
                if (checkMenusPopular) {
                    onScrollManage();
                    gridView.onRestoreInstanceState(onRestoreParcelable);
                    getSupportActionBar().setTitle("Popular Movies");
                } else {
                    onScrollManage();
                    gridView.onRestoreInstanceState(onRestoreParcelable);
                    getSupportActionBar().setTitle("Top Rated Movies");
                }
            }

        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initWidget();

        RealmConfiguration realmConfig = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(realmConfig);



        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_red_light));

    }





    private void initWidget() {
        gridView = (GridView) findViewById(R.id.gridView);
        relativeProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.topRated:
                checkMenusPopular = false;
                getTopRatedMoviesFromServer();
                return true;
            case R.id.popular:
                checkMenusPopular = true;
                getPopularMoviesFromServer();
                return true;
            case R.id.favourite:
                intent = new Intent(this,FavouriteMovie.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable("parcelableState", gridView.onSaveInstanceState());
        outState.putBoolean("checkMenu",checkMenusPopular);
        outState.putParcelableArrayList("listofItem", (ArrayList<? extends Parcelable>) itemList);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        onRestoreParcelable = savedInstanceState.getParcelable("parcelableState");
        checkMenusPopular = savedInstanceState.getBoolean("checkMenu");
        itemList = savedInstanceState.getParcelableArrayList("listofItem");

    }

    @Override
    public void onRefresh() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (checkMenusPopular) {
                    getPopularMoviesFromServer();
                } else {
                    getTopRatedMoviesFromServer();
                }

                swipeRefreshLayout.setRefreshing(false);
            }
        }, 3000);
    }



    private void getTopRatedMoviesFromServer() {

        Call<AllMovieList> call = RetrofitBuilder.getInstance().gettmDbAPI().listTopRated(id);
        call.enqueue(new Callback<AllMovieList>() {
            @Override
            public void onResponse(Call<AllMovieList> call, Response<AllMovieList> response) {
                relativeProgressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    getSupportActionBar().setTitle("Top Rated Movies");
                    itemList = response.body().getResults();
                    setPosterGridView(itemList);
                    if (onRestoreParcelable != null) {
                        gridView.onRestoreInstanceState(onRestoreParcelable);
                    }
                } else {
                    try {
                        Log.d("Error", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<AllMovieList> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                Log.e("OnResponse", t.getMessage());
            }
        });

    }

    private void getPopularMoviesFromServer() {
        Call<AllMovieList> call = RetrofitBuilder.getInstance().gettmDbAPI().listHighPopular(id);
        call.enqueue(new Callback<AllMovieList>() {
            @Override
            public void onResponse(Call<AllMovieList> call, Response<AllMovieList> response) {
                relativeProgressBar.setVisibility(View.GONE);
                if (response != null) {
                    getSupportActionBar().setTitle("Popular Movies");
                    itemList = response.body().getResults();
                    setPosterGridView(itemList);
                    if (onRestoreParcelable != null) {
                        gridView.onRestoreInstanceState(onRestoreParcelable);
                    }
                } else {
                    try {
                        Log.d("Error", response.errorBody().string());
                        Toast.makeText(MainActivity.this, response.errorBody().toString()
                                , Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<AllMovieList> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
                Log.e("OnResponse", t.getMessage());
            }
        });

    }

    private void setPosterGridView(List<MoviesList> list) {
        if (itemList != null) {

            gridView.setAdapter(new CustomAdapter(this, list));
            onScrollManage();

        } else {
            Toast.makeText(this, "itemList is Null", Toast.LENGTH_SHORT).show();
        }
    }


    private void onScrollManage(){
        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                boolean enable = false;
                if (gridView != null && gridView.getChildCount() > 0) {

                    boolean firstItemVisible = gridView.getFirstVisiblePosition() == 0;
                    boolean topOfFirstItemVisible = gridView.getChildAt(0).getTop() == 0;
                    enable = firstItemVisible && topOfFirstItemVisible;

                }
                swipeRefreshLayout.setEnabled(enable);

            }

        });
    }

}

