package com.example.maii.moviespopcorn;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class CustomAdapter extends BaseAdapter {
    private Context mContext;
    private List<MoviesList> item;
    private LayoutInflater inflater;
    Realm myRealm;


    private OnPosterClickListener lis;

    public CustomAdapter(Context c, List<MoviesList> item) {
        this.mContext = c;

        if (item != null) {
            this.item = item;
        } else {
            this.item = new ArrayList<>();
        }
        inflater = LayoutInflater.from(mContext);
    }

    public void setOnPosterClickListener(OnPosterClickListener lis) {
        this.lis = lis;
    }

    @Override
    public int getCount() {
        return item.size();
    }

    @Override
    public MoviesList getItem(int i) {
        return item.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final Holder holder;
        inflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);


        myRealm = Realm.getDefaultInstance();
        final RealmResults<DataBaseMovie> resultsQueries = myRealm.where(DataBaseMovie.class).findAll();


        if (view == null) {
            view = inflater.inflate(R.layout.activity_custom_adapter, viewGroup, false);
            view.setClickable(true);
            view.setFocusable(true);
            holder = new Holder();
            holder.isFavourite = false;
            holder.imageView = (ImageView) view.findViewById(R.id.image_Thumbnail);
            holder.starButton = (ImageView) view.findViewById(R.id.favouriteButton);

            if (resultsQueries.isLoaded()) {
                for (DataBaseMovie c : resultsQueries) {
                    if (c.getFavourite() == false) {
                        holder.starButton.setImageResource(R.drawable.ic_star_none);
                    } else {
                        holder.starButton.setImageResource(R.drawable.ic_star);
                    }
                }
            }

            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }


        setPosterSize(i, holder);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lis != null) {
                    lis.onPosterClick(i);
                }
            }
        });
        holder.starButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lis != null) {
                    lis.onFavClick(i);
                    holder.onFavouriteClick();
                }
            }
        });

        return view;
    }

    public class Holder {
        ImageView imageView;
        ImageView starButton;
        boolean isFavourite;

        public void onFavouriteClick() {
            Log.d("number of pos", isFavourite + "");
            if (!isFavourite) {
                isFavourite = true;
                starButton.setImageResource(R.drawable.ic_star);
                Log.d("isFavourite ", String.valueOf(isFavourite));
            } else {
                isFavourite = false;
                starButton.setImageResource(R.drawable.ic_star_none);
                Log.d("isFavourite ", String.valueOf(isFavourite));
            }
        }


    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void setPosterSize(int i, Holder holder) {

        RealmResults<DataBaseMovie> resultsQueries = myRealm.where(DataBaseMovie.class).findAll();
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        if (width <= height) {
            if (isOnline()) {
                Picasso.with(mContext).load("http://image.tmdb.org/t/p/w342/" + item.get(i).getPosterPath())
                        .resize(width / 2, height / 2)
                        .into(holder.imageView);
            } else {
                byte[] decodedString = Base64.decode(resultsQueries.get(i).getImageBase64(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                holder.imageView.setImageBitmap(Bitmap.createScaledBitmap(decodedByte, width / 2, height / 2, false));
            }
        } else if (width > height) {
            if (isOnline()) {
                Picasso.with(mContext).load("http://image.tmdb.org/t/p/w342/" + item.get(i).getPosterPath())
                        .resize(width / 2, height)
                        .into(holder.imageView);
            } else {
                byte[] decodedString = Base64.decode(resultsQueries.get(i).getImageBase64(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                holder.imageView.setImageBitmap(Bitmap.createScaledBitmap(decodedByte, width / 2, height, false));
            }
        }

    }


}
