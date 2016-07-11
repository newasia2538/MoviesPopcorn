package com.example.maii.moviespopcorn;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmResults;

public class CustomAdapter extends BaseAdapter {
    private Context mContext;
    private List<MoviesList> item;
    private LayoutInflater inflater;
    Holder holder;
    Boolean isFavourite = false;
    Realm realm;

    public CustomAdapter(Context c,List<MoviesList> item) {
        this.mContext = c;
        if(item != null){
        this.item = item;
        }
        else{
            this.item = new ArrayList<>();
        }
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return  item.size();
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
        inflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);



        if(view == null) {
            view = inflater.inflate(R.layout.activity_custom_adapter, viewGroup,false);
            view.setClickable(true);
            view.setFocusable(true);
            holder = new Holder();
            holder.imageView = (ImageView) view.findViewById(R.id.image_Thumbnail);
            holder.starButton = (ImageView) view.findViewById(R.id.favouriteButton);
            view.setTag(holder);
        }
        else{
            holder = (Holder) view.getTag();
        }
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        if(width<=height) {
            Picasso.with(mContext).load("http://image.tmdb.org/t/p/w342/" + item.get(i).getPosterPath())
                    .resize(width/2, height/2)
                    .into(holder.imageView);


        }
        else {
            Picasso.with(mContext).load("http://image.tmdb.org/t/p/w342/" + item.get(i).getPosterPath())
                    .resize(height/ 2, width / 2)
                    .into(holder.imageView);


        }

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ShowDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("par", new ArrayList<MoviesList>(item));
                intent.putExtra("position", i);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
        holder.starButton.setOnClickListener(onClickFavouriteButton());

        return view;
    }

    public class Holder{
        ImageView imageView;
        ImageView starButton;
    }
    private View.OnClickListener onClickFavouriteButton(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                int pos = Integer.parseInt(view.getTag().toString());
                if(!isFavourite) {

                    isFavourite = true;
                    Picasso.with(mContext).load(R.drawable.ic_star).into((ImageView) view);
                    Toast.makeText(mContext, "Add to favourite.", Toast.LENGTH_SHORT).show();

//                    realm.beginTransaction();
//                    DataBaseMovie db = realm.createObject(DataBaseMovie.class);
//                    db.setId(UUID.randomUUID().timestamp());
//                    db.setTitleName(item.get(pos).getOriginalTitle());
//                    db.setFavourite(true);
//                    db.setPosterpath(item.get(pos).getPosterPath());
//                    db.setReleaseDate(item.get(pos).getReleaseDate().toString());
//                    db.setOverview(item.get(pos).getOverview());
//                    realm.commitTransaction();
//
//                    if(db != null){
//                        Log.d("database", db.toString());
//                    }
                }
                else{
                    isFavourite = false;
                    Picasso.with(mContext).load(R.drawable.ic_star_none).into((ImageView) view);
                    Toast.makeText(mContext, "Remove from favourite.", Toast.LENGTH_SHORT).show();
                    realm.delete(DataBaseMovie.class);
                }
            }
        };
    }



}
