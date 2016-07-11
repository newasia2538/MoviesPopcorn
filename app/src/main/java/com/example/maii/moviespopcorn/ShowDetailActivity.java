package com.example.maii.moviespopcorn;

import android.content.Intent;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ShowDetailActivity extends AppCompatActivity {
    List<MoviesList> itemList;
    int position;
    ImageView imageView;
    TextView movieName, movieYearInTheater, movieRated, movieDescription;
    String date;
    Button buttonMarkAsFavourite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_detail);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



        buttonMarkAsFavourite = (Button) findViewById(R.id.markAsFavoriteButton);
        imageView = (ImageView) findViewById(R.id.moviePoster);
        movieName = (TextView) findViewById(R.id.movieName);
        movieYearInTheater = (TextView) findViewById(R.id.movieYearInTheater);
        movieRated = (TextView) findViewById(R.id.movieRated);
        movieDescription = (TextView) findViewById(R.id.movieDescription);
        buttonMarkAsFavourite.setOnClickListener(onMarkAsFavouriteMovie());
        if(itemList == null) {
            getDataToShow();
        }


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("listOfItem", (ArrayList<? extends Parcelable>) itemList);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        itemList = savedInstanceState.getParcelableArrayList("listOfItem");
    }

    @Override
    protected void onResume() {
        super.onResume();
        showDetail();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    private View.OnClickListener onMarkAsFavouriteMovie(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ShowDetailActivity.this,itemList.get(position).getOriginalTitle()+" has mark as your favourite.",Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void getDataToShow(){
        Intent intent = getIntent();
        Bundle b = getIntent().getExtras();
        itemList = b.getParcelableArrayList("par");
        position = intent.getExtras().getInt("position");

        Date movieReleaseDate = itemList.get(position).getReleaseDate();
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy");
        date = formatDate.format(movieReleaseDate);

        showDetail();
        }
    private void showDetail(){
        Picasso.with(getApplicationContext()).load("http://image.tmdb.org/t/p/w342/"+itemList.get(position).getPosterPath()).into(imageView);
        movieName.setText(itemList.get(position).getOriginalTitle());
        movieYearInTheater.setText(itemList.get(position).getReleaseDate().toString());
        movieDescription.setText(itemList.get(position).getOverview());
        movieRated.setText(String.valueOf(itemList.get(position).getVoteAverage())+"/10");
        movieYearInTheater.setText(date);


    }


}
