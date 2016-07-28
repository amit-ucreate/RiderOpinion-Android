package com.nutsuser.ridersdomain.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.activities.EventDetailActivity;
import com.nutsuser.ridersdomain.activities.MapActivity;
import com.nutsuser.ridersdomain.activities.StatusRiderOnMapActivity;
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

    public AdapterEvent(Context context, ArrayList<RidingEventData> data) {
        mContext = context;
        this.data = data;
    }

    @Override
    public AdapterEvent.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout
                        .item_event, parent,
                false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (position % 2 == 0) {
            holder.rllayout.setBackgroundColor(Color.WHITE);
        } else {
            holder.rllayout.setBackgroundColor(Color.parseColor("#fefaf1"));
        }
        if (data.get(position).getEndDate().toString().matches("")) {
            Log.e("EndDate", "" + data.get(position).getEndDate());
            holder.tvdate.setText(data.get(position).getStartDate());
        } else {
            holder.tvdate.setText(data.get(position).getStartDate() + " - " + data.get(position).getEndDate());
        }
        if (data.get(position).getHalts().size() == 0 || data.get(position).getHalts() == null) {
            holder.tvTitle.setText(data.get(position).getDaysCount() + "-Day Ride From " + data.get(position).getBaseLocation() + " to " + data.get(position).getDestLocation());
        } else {

            holder.tvTitle.setText(data.get(position).getDaysCount() + "-Day Ride From " + data.get(position).getBaseLocation() + " to " + data.get(position).getDestLocation() + " (Stopover at " + data.get(position).getHalts().get(0).getHaltLocation() + ")");
        }

        holder.tvtime.setText(data.get(position).getStartTime());
        holder.tvdesloc.setText(data.get(position).getDestLocation());
        holder.tvNumberOfRiders.setText(data.get(position).getRiders() + " Riders");
        if (data.get(position).getMutual().toString().matches("")) {
            holder.tvLabelHaveJoined.setText("(" + data.get(position).getMutual() + ")");
        } else {
            //holder.tvLabelHaveJoined.setText("(" + "0" + ")");
            holder.tvLabelHaveJoined.setText("");
        }
        holder.lllayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(mContext, EventDetailActivity.class);
                mIntent.putExtra("eventId", data.get(position).getEventId());
                //  mIntent.putExtra("typeEmailPhone", getIntent().getStringExtra("typeEmailPhone"));
                mContext.startActivity(mIntent);
            }
        });
        holder.ivMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lat = data.get(position).getBaseLat();
                String lon = data.get(position).getBaseLong();
                String deslon =data.get(position).getDestLat();
                String deslat = data.get(position).getDestLong();
                Intent mIntent = new Intent(mContext, MapActivity.class);
                mIntent.putExtra("endLat", lat);
                mIntent.putExtra("endLon", lon);
                mIntent.putExtra("deslon", deslon);
                mIntent.putExtra("deslat", deslat);
                mIntent.putExtra("destName",data.get(position).getDestLocation());
                mContext.startActivity(mIntent);


            }
        });

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
        @Bind(R.id.rllayout)
        RelativeLayout rllayout;
        @Bind(R.id.lllayout)
        LinearLayout lllayout;
        @Bind(R.id.ivMap)
        ImageView ivMap;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
