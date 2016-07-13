package com.example.maii.moviespopcorn;

import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends BaseAdapter {
    private Context mContext;
    private List<MoviesList> item;
    private LayoutInflater inflater;
    Holder holder;


    private OnPosterClickListener lis;

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

    public void setOnPosterClickListener(OnPosterClickListener lis){
        this.lis = lis;
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
            holder.isFavourite = false;
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
        else if(width>height) {
            Picasso.with(mContext).load("http://image.tmdb.org/t/p/w342/" + item.get(i).getPosterPath())
                    .resize(width/2, height)
                    .into(holder.imageView);

        }

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lis != null){
                lis.onPosterClick(i);}
            }
        });
        holder.starButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(lis != null){
                    lis.onFavClick(i);
                    Log.d("number of pos", String.valueOf(i));
                    holder.onFavouriteClick();
                    }
            }
        });

        return view;
    }

    public class Holder{
        ImageView imageView;
        ImageView starButton;
        boolean isFavourite;
        public void onFavouriteClick(){
            Log.d("number of pos", isFavourite+"");
            if(!isFavourite) {
                isFavourite = true;
                Picasso.with(mContext).load(R.drawable.ic_star).into(starButton);
                Toast.makeText(mContext, "Add to favourite.", Toast.LENGTH_SHORT).show();

            }
            else{
                isFavourite = false;
                Picasso.with(mContext).load(R.drawable.ic_star_none).into(starButton);
                Toast.makeText(mContext, "Remove from favourite.", Toast.LENGTH_SHORT).show();
            }
        }
    }



}
