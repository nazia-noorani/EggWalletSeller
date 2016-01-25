package com.egneese.sellers.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.egneese.sellers.R;
import com.egneese.sellers.dto.TransactionDTO;

import java.util.List;


/**
 * Created by adityaagrawal on 17/01/16.
 */
public class TransactionListAdapter extends BaseAdapter{
    private Context context;
    private LayoutInflater inflater;
    private List<TransactionDTO> transactionDTOs;

    public TransactionListAdapter(Context context, List<TransactionDTO> transactionDTOs){
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.transactionDTOs = transactionDTOs;
    }

    @Override
    public int getCount() {
        return transactionDTOs.size();
    }

    @Override
    public Object getItem(int position) {
        return transactionDTOs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.fragment_transaction_single_row, null);
        ((TextView)view.findViewById(R.id.txtTrans)).setText(transactionDTOs.get(position).toString());
        return view;
    }
}
