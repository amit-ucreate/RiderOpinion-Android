package com.nutsuser.ridersdomain.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nutsuser.ridersdomain.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by user on 8/28/2015.
 */
public class AdapterDestination extends RecyclerView.Adapter<AdapterDestination.ViewHolder> {

    private final Context mContext;

    public AdapterDestination(Context context) {
        mContext = context;
    }

    @Override
    public AdapterDestination.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout
                        .item_destination, parent,
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
