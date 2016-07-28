package com.nutsuser.ridersdomain.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.web.pojos.CalendarEventData;
import com.nutsuser.ridersdomain.web.pojos.RidingEventData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by user on 9/23/2015.
 */
public class AdapterCalendarEvent extends RecyclerView.Adapter<AdapterCalendarEvent.ViewHolder> {

    private final Context mContext;
    ArrayList<RidingEventData> data;

    public AdapterCalendarEvent(Context context, ArrayList<RidingEventData> data) {
        mContext = context;
        this.data = data;
    }

    @Override
    public AdapterCalendarEvent.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout
                        .item_event, parent,
                false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position % 2 == 0) {
            holder.rllayout.setBackgroundColor(Color.WHITE);
        } else {
            holder.rllayout.setBackgroundColor(Color.parseColor("#fefaf1"));
        }
        if (data.get(position).getEndDate().toString().matches("")) {
            Log.e("EndDate", "" + data.get(position).getEndDate());
            holder.tvdate.setText(data.get(position).getStartDate() + " - " + data.get(position).getEndDate());
        } else {
            holder.tvdate.setText(data.get(position).getStartDate());
        }
        if (data.get(position).getHalts().size() == 0) {
            holder.tvTitle.setText(data.get(position).getDaysCount()+"-Day Ride From "+data.get(position).getBaseLocation() + " to " + data.get(position).getDestLocation());
        }
        else{
            holder.tvTitle.setText(data.get(position).getDaysCount()+"-Day Ride From "+data.get(position).getBaseLocation() + " to " + data.get(position).getDestLocation()+"(Stopover at "+data.get(position).getHalts().get(0).getHaltLocation());
        }


        String timein12Format="";
        try {
            final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
            final Date dateObj = sdf.parse(data.get(position).getStartTime());
            timein12Format=new SimpleDateFormat("K:mm a").format(dateObj);
        } catch (final ParseException e) {
            e.printStackTrace();
        }
        holder.tvtime.setText(timein12Format);
        holder.tvdesloc.setText(data.get(position).getDestLocation());
        holder.tvNumberOfRiders.setText(data.get(position).getRiders() + " Riders");
        if (data.get(position).getMutual().toString().matches("")) {
            holder.tvLabelHaveJoined.setText("(" + data.get(position).getMutual() + ")");
        } else {
            //holder.tvLabelHaveJoined.setText("(" + "0" + ")");
            holder.tvLabelHaveJoined.setText("");
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
        @Bind(R.id.rllayout)
        RelativeLayout rllayout;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
