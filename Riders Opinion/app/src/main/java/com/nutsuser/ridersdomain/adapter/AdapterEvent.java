package com.nutsuser.ridersdomain.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nutsuser.ridersdomain.R;

import butterknife.ButterKnife;

/**
 * Created by user on 9/23/2015.
 */
public class AdapterEvent extends RecyclerView.Adapter<AdapterEvent.ViewHolder> {

    private final Context mContext;

    public AdapterEvent(Context context) {
        mContext = context;
    }

    @Override
    public AdapterEvent.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout
                        .item_event, parent,
                false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        /*@Bind(R.id.tvContact)
        TextView tvContact;*/

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
