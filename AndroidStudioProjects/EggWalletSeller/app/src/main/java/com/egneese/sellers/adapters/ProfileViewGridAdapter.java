package com.egneese.sellers.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.egneese.sellers.R;


/**
 * Created by adityaagrawal on 05/01/16.
 */
public class ProfileViewGridAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater inflater;
    public Integer[] mThumbIds = {
            R.mipmap.ic_launcher, R.mipmap.ic_launcher,
            R.mipmap.ic_launcher, R.mipmap.ic_launcher
    };
    public String[] itemNames = {"Expenses", "Saved Wallets", "Share", "Settings"};

    public ProfileViewGridAdapter(Context c) {
        mContext = c;
        inflater = LayoutInflater.from(c);
    }

    @Override
    public int getCount() {
        return mThumbIds.length;
    }

    @Override
    public Object getItem(int position) {
        return mThumbIds[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.profile_view_grid_single_item, parent, false);
        ((ImageView)view.findViewById(R.id.grid_image)).setImageResource(mThumbIds[position]);
        ((com.neopixl.pixlui.components.textview.TextView)view.findViewById(R.id.grid_text)).setText(itemNames[position]);
        return view;
    }

}