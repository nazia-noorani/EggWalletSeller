package com.egneese.sellers.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.egneese.sellers.R;
import com.egneese.sellers.dto.DashboardListItemDTO;
import com.neopixl.pixlui.components.textview.TextView;

import java.util.List;

/**
 * Created by adityaagrawal on 10/01/16.
 */
public class DashboardListAdapter extends BaseAdapter{
    private Context context;
    private List<DashboardListItemDTO> items;
    private LayoutInflater inflater;

    public DashboardListAdapter(Context context, List<DashboardListItemDTO> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int location) {
        return items.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.fragment_home_list_single_row, null);
            ((TextView)convertView.findViewById(R.id.txtDashboardSingleRowTitle)).setText(items.get(position).getTitle());
            ((TextView)convertView.findViewById(R.id.txtDashboardSingleRowTDisc)).setText(items.get(position).getDisc());
            ((ImageView)convertView.findViewById(R.id.imgDashboardSingleRow)).setImageResource(items.get(position).getImage());
            return convertView;
    }
}
