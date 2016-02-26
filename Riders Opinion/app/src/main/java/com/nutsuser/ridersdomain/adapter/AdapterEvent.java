package com.nutsuser.ridersdomain.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.web.pojos.RidingEventData;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by user on 9/23/2015.
 */
public class AdapterEvent extends RecyclerView.Adapter<AdapterEvent.ViewHolder> {

    private final Context mContext;
    ArrayList<RidingEventData> data;

    public AdapterEvent(Context context,ArrayList<RidingEventData> data) {
        mContext = context;
        this.data=data;
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
        if(data.get(position).getEndDate().toString().matches("")){
            Log.e("EndDate",""+data.get(position).getEndDate());
            holder.tvdate.setText(data.get(position).getStartDate()+" - "+data.get(position).getEndDate());
        }
        else{
            holder.tvdate.setText(data.get(position).getStartDate());
        }
        holder.tvTitle.setText(data.get(position).getBaseLocation()+" to "+data.get(position).getDestLocation());
        holder.tvtime.setText(data.get(position).getStartTime());
        holder.tvdesloc.setText(data.get(position).getDestLocation());
        holder.tvNumberOfRiders.setText(data.get(position).getRiders()+" Riders");
        if(data.get(position).getMutual().toString().matches("")){
            holder.tvLabelHaveJoined.setText("("+data.get(position).getMutual()+")");
        }
        else{
            holder.tvLabelHaveJoined.setText("("+"0"+")");
        }



    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

       @Bind(R.id.tvTitle)
       TextView tvTitle;
        @Bind(R.id.tvdate)
        TextView tvdate;
        @Bind(R.id.tvtime)
        TextView tvtime;
        @Bind(R.id.tvdesloc)
        TextView tvdesloc;
        @Bind(R.id.tvNumberOfRiders)
        TextView tvNumberOfRiders;
        @Bind(R.id.tvLabelHaveJoined)
        TextView tvLabelHaveJoined;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
