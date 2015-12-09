package com.nutsuser.ridersdomain.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nutsuser.ridersdomain.R;

import butterknife.ButterKnife;

/**
 * Created by user on 10/1/2015.
 */
public class AdapterRide extends RecyclerView.Adapter<AdapterRide.ViewHolder> {

    private Activity activity;

    public AdapterRide(Activity activity) {
        this.activity = activity;
    }

    @Override
    public AdapterRide.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ride, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterRide.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
