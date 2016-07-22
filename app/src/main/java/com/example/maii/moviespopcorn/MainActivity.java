package com.example.maii.moviespopcorn;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
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
    Realm myRealm;
    CustomAdapter adapter;
    String ba1;
    RealmResults<DataBaseMovie> resultsQueries;

    @Override
    protected void onResume() {
        super.onResume();
        if (onRestoreParcelable == null) {
            int position;
            Intent getIntent = getIntent();
            Bundle bundle = getIntent().getExtras();


            if (itemList == null && bundle == null) {
                if (checkMenusPopular) {
                    getPopularMoviesFromServer();
                    getSupportActionBar().setTitle("Popular Movies");

                    checkMenusPopular = true;
                } else {
                    getTopRatedMoviesFromServer();
                    getSupportActionBar().setTitle("Top Rated Movies");

                    checkMenusPopular = false;
                }
            } else if (bundle != null) {
                itemList = bundle.getParcelableArrayList("par");
                position = getIntent.getExtras().getInt("position");
                setPosterGridView(itemList);
                gridView.setSelection(position);

            }
        } else {
            gridView.setAdapter(adapter);

            if (onRestoreParcelable != null) {
                if (checkMenusPopular) {
                    Log.d("onRestore poppalar", onRestoreParcelable.toString());
                    gridView.onRestoreInstanceState(onRestoreParcelable);
                    onScrollManage();
                    getSupportActionBar().setTitle("Popular Movies");
                } else {
                    Log.d("onRestore top rate", onRestoreParcelable.toString());
                    gridView.onRestoreInstanceState(onRestoreParcelable);
                    onScrollManage();
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
        myRealm = Realm.getInstance(realmConfig);


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
        adapter = new CustomAdapter(this, itemList);
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
                intent = new Intent(this, FavouriteMovie.class);
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
        outState.putBoolean("checkMenu", checkMenusPopular);
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
                    if (isOnline()) {
                        getPopularMoviesFromServer();
                    } else {

                    }
                } else {
                    if (isOnline()) {
                        getTopRatedMoviesFromServer();
                    } else {

                    }
                }

                swipeRefreshLayout.setRefreshing(false);
            }
        }, 3000);
    }


    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
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
                    setDataFromServerToDataBase();
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
                Toast.makeText(MainActivity.this, "NO INTERNET CONNECTION", Toast.LENGTH_SHORT).show();
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
                    setDataFromServerToDataBase();
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
                Toast.makeText(MainActivity.this, "NO INTERNET CONNECTION", Toast.LENGTH_SHORT).show();
                Log.e("OnResponse", t.getMessage());
            }
        });

    }

    private void setPosterGridView(List<MoviesList> list) {
        if (itemList != null) {
            adapter = new CustomAdapter(this, itemList);
            gridView.setAdapter(adapter);

            adapter.setOnPosterClickListener(new OnPosterClickListener() {

                @Override
                public void onPosterClick(int pos) {
                    Intent intent = new Intent(MainActivity.this, ShowDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("par", new ArrayList<MoviesList>(itemList));
                    intent.putExtra("position", pos);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }

                @Override
                public void onFavClick(int pos) {
                    setOrRemoveFavouriteMovieToRealm(pos);
                }
            });


            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Toast.makeText(MainActivity.this, gridView.getAdapter().getItem(i).toString(), Toast.LENGTH_SHORT).show();
                }
            });
            onScrollManage();

        } else {
            Toast.makeText(this, "List is Null", Toast.LENGTH_SHORT).show();
        }
    }


    private void onScrollManage() {
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

    public void setOrRemoveFavouriteMovieToRealm(final int position) {

        final RealmConfiguration configuration = new RealmConfiguration.Builder(this).build();
        myRealm = Realm.getInstance(configuration);
        RealmResults<DataBaseMovie> results = myRealm.where(DataBaseMovie.class).findAll();

        myRealm.beginTransaction();
        if (results.get(position).getFavourite() == false) {
            results.get(position).setFavourite(true);
            Toast.makeText(MainActivity.this, "Add to favourite.", Toast.LENGTH_SHORT).show();
        } else {
            results.get(position).setFavourite(false);
            Toast.makeText(MainActivity.this, "Remove from favourite.", Toast.LENGTH_SHORT).show();
        }
        myRealm.commitTransaction();

        Log.d("database", results.get(position).getFavourite().toString());

    }


    public void setDataFromServerToDataBase() {

        myRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (realm == null) {
                    for (int i = 0; i < itemList.size(); i++) {
                        compressImageToBase64(i);
                        DataBaseMovie dataBase = realm.createObject(DataBaseMovie.class, itemList.get(i).getId());
                        Date movieReleaseDate = itemList.get(i).getReleaseDate();
                        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy");
                        String date = formatDate.format(movieReleaseDate);


                        dataBase.setId(itemList.get(i).getId());
                        dataBase.setReleaseDate(date);
                        dataBase.setPosterpath("http://image.tmdb.org/t/p/w342/" + itemList.get(i).getPosterPath());
                        dataBase.setTitleName(itemList.get(i).getOriginalTitle());
                        dataBase.setOverview(itemList.get(i).getOverview());
                        dataBase.setFavourite(false);
                        dataBase.setImageBase64(ba1);
                        Log.d("ImageBase64 : ", resultsQueries.get(i).getImageBase64());
                    }
                    Toast.makeText(getApplicationContext(), "all Data was added to database", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public void compressImageToBase64(int position) {
        Bitmap bitmapOrg = BitmapFactory.decodeFile("http://image.tmdb.org/t/p/w342/" + itemList.get(position).getPosterPath());
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bitmapOrg.compress(Bitmap.CompressFormat.JPEG, 100, bao);
        byte[] ba = bao.toByteArray();
        ba1 = Base64.encodeToString(ba, Base64.DEFAULT);
    }

}

