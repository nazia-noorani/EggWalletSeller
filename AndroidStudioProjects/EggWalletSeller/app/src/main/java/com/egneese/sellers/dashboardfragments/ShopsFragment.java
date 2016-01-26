package com.egneese.sellers.dashboardfragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.egneese.sellers.R;


/**
 * Created by adityaagrawal on 17/01/16.
 */
public class ShopsFragment extends Fragment {
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_transaction, container, false);
        return rootView;
    }
}
