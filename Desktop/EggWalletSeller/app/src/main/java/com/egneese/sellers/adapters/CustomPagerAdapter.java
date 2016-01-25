package com.egneese.sellers.adapters;

/**
 * Created by nazianoorani on 21/01/16.
 */

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.egneese.sellers.R;

public class CustomPagerAdapter extends PagerAdapter {
    int[] mResources = {
            R.mipmap.logo,
            R.mipmap.app_info_1,
            R.mipmap.app_info_2,
            R.mipmap.app_info_3,
            R.mipmap.app_info_4,
            R.mipmap.app_info_5,
            R.mipmap.app_info_6};
    Context mContext;
    LayoutInflater mLayoutInflater;

    public CustomPagerAdapter(Context context) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mResources.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.imgAppInfoImageView);
        imageView.setImageResource(mResources[position]);
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}

