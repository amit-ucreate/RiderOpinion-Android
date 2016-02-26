package com.nutsuser.ridersdomain.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.web.pojos.PlanARideData;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * Created by user on 10/1/2015.
 */
public class AdapterRide extends RecyclerView.Adapter<AdapterRide.ViewHolder> {

    private Activity activity;
    ArrayList<PlanARideData> planARideDatas;

    public AdapterRide(Activity activity,ArrayList<PlanARideData> planARideDatas) {
        this.activity = activity;
        this.planARideDatas=planARideDatas;
    }

    @Override
    public AdapterRide.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ride, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterRide.ViewHolder holder, int position) {
        holder.tvTitle.setText(planARideDatas.get(position).getBaseLocation()+" to "+planARideDatas.get(position).getDestLocation());
        holder.tvTime.setText(planARideDatas.get(position).getStartTime());
        holder.tvDate.setText(planARideDatas.get(position).getStartDate());
    }

    @Override
    public int getItemCount() {
        return planARideDatas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivMap,ivStar;
        TextView tvPlace,tvDate,tvTitle,tvTime;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            tvPlace=(TextView)itemView.findViewById(R.id.tvPlace);
            tvDate=(TextView)itemView.findViewById(R.id.tvDate);
            tvTitle=(TextView)itemView.findViewById(R.id.tvTitle);
            tvTime=(TextView)itemView.findViewById(R.id.tvTime);
            ivMap=(ImageView)itemView.findViewById(R.id.ivMap);
            ivStar=(ImageView)itemView.findViewById(R.id.ivStar);


        }
    }
}
