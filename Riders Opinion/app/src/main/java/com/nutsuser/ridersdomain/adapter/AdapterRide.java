package com.nutsuser.ridersdomain.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.web.pojos.PlanARideData;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * Created by user on 10/1/2015.
 */
public class AdapterRide extends RecyclerView.Adapter<AdapterRide.ViewHolder> {

    ArrayList<PlanARideData> planARideDatas;
    private Activity activity;

    public AdapterRide(Activity activity, ArrayList<PlanARideData> planARideDatas) {
        this.activity = activity;
        this.planARideDatas = planARideDatas;
    }

    @Override
    public AdapterRide.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ride, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterRide.ViewHolder holder, int position) {

        if (position % 2 == 0){
            holder.rlbackground.setBackgroundColor(Color.WHITE);
        }

        else{
            holder.rlbackground.setBackgroundColor(Color.parseColor("#fefaf1"));
        }
        if (planARideDatas.get(position).getHalts().size() == 0) {
            holder.tvTitle.setText(planARideDatas.get(position).getDaysCount()+" -Day Ride From "+planARideDatas.get(position).getBaseLocation() + " to " + planARideDatas.get(position).getDestLocation());
        }
        else{

            holder.tvTitle.setText(planARideDatas.get(position).getDaysCount() + "-Day Ride From " + planARideDatas.get(position).getBaseLocation() + " to " + planARideDatas.get(position).getDestLocation() + "(Stopover at " + planARideDatas.get(position).getHalts().get(0).getHaltLocation());
        }

        holder.tvTime.setText(planARideDatas.get(position).getStartTime());
        holder.tvDate.setText(planARideDatas.get(position).getStartDate());
        holder.tvPlace.setText(planARideDatas.get(position).getBaseLocation());
    }

    @Override
    public int getItemCount() {
        return planARideDatas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivMap, ivStar;
        TextView tvPlace, tvDate, tvTitle, tvTime;
        RelativeLayout rlbackground;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            tvPlace = (TextView) itemView.findViewById(R.id.tvPlace);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
            ivMap = (ImageView) itemView.findViewById(R.id.ivMap);
            ivStar = (ImageView) itemView.findViewById(R.id.ivStar);
            rlbackground=(RelativeLayout)itemView.findViewById(R.id.rlbackground);


        }
    }
}
