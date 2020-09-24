package com.genworks.oppm.Adapter;




import android.content.Context;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.genworks.oppm.R;

import java.util.ArrayList;

/**
 * Created by Belal on 6/6/2017.
 */

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private ArrayList<String> account_name;
    private Context mContext;
    public CustomAdapter(ArrayList<String> names) {
        this.account_name = names;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textViewName.setText(account_name.get(position));


    }


    @Override
    public int getItemCount() {
        return account_name.size();
    }

    public void filterList(ArrayList<String> filterdNames) {
        this.account_name = filterdNames;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;

        ViewHolder(View itemView) {
            super(itemView);

            textViewName = (TextView) itemView.findViewById(R.id.textViewName);

        }
    }


}
