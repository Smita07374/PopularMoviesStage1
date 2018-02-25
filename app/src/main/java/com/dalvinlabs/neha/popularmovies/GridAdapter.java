package com.dalvinlabs.neha.popularmovies;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by neha on 03/02/18.
 */

public class GridAdapter extends BaseAdapter{
    Context mContext;
    ImageView imageView;
    String[] posters;

    public GridAdapter(Context mainActivity, String[] s)
    {
        mContext = mainActivity;
        posters = s;
    }
    @Override
    public int getCount() {
        return posters.length;
    }

    @Override
    public Object getItem(int i) {
        return posters[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null)
        {
            imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
        else
        {
            imageView = (ImageView) view;
        }
        Picasso.with(mContext).load(NetworkUtils.POSTER_BASE_URL + posters[i]).into(imageView);
        return imageView;
    }
}

