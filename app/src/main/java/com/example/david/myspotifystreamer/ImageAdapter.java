package com.example.david.myspotifystreamer;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by David on 10/20/15.
 */
public class ImageAdapter extends BaseAdapter {

    private Context mContext;

    private String[] urlArray = {
            "http://image.tmdb.org/t/p/w185/vlTPQANjLYTebzFJM1G4KeON0cb.jpg",
            "http://image.tmdb.org/t/p/w185/kqjL17yufvn9OVLyXYpvtyrFfak.jpg"
    };

    public ImageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return urlArray.length;
    }

    @Override
    public String getItem(int position) {
        return urlArray[position];
    }

    public long getItemId(int position) {
        return position;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            //imageView.setLayoutParams(new GridView.LayoutParams(120,120));
            //imageView.setLayoutParams(mImageViewLayoutParams);
            //imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            //imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        //imageView.setImageResource(mThumbIds[position]);

        Picasso.with(imageView.getContext())
                .load(getItem(position))
                .noFade()//.resize(250,250)
                .into(imageView);
        /*Picasso.with(mContext)
                .load("http://imgur.com/M3ny4P3")
                .noFade().resize(250,250)
                .into(imageView);
        */
        return imageView;
    }

    public void clear() {
        urlArray = new String[0];
    }

    public void addAll(ArrayList<MovieInfo> result) {
        urlArray = new String[result.size()];
        for (int i = 0; i < urlArray.length; i++) {
            urlArray[i] = result.get(i).getPosterPath();
        }
        notifyDataSetChanged();
    }

    public void add(String src) {
        return;
    }

}
