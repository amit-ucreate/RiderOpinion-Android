package com.nutsuser.ridersdomain.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.web.pojos.RidingDestinationDetails;

import java.io.File;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by user on 8/28/2015.
 */
public class AdapterDestination extends RecyclerView.Adapter<AdapterDestination.ViewHolder> {

    private final Context mContext;
    private ArrayList<RidingDestinationDetails> mRidingDestinationDetailses;
    public AdapterDestination(Context context,ArrayList<RidingDestinationDetails> mRidingDestinationDetailses) {
        mContext = context;
        this.mRidingDestinationDetailses=mRidingDestinationDetailses;
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

        holder.tvTitle.setText(mRidingDestinationDetailses.get(position).getDestName());
        holder.tvDesc.setText(mRidingDestinationDetailses.get(position).getDescription());
        holder.tvEatables.setText(mRidingDestinationDetailses.get(position).getRestaurant());
        holder.tvPetrolPump.setText(mRidingDestinationDetailses.get(position).getPetrolpumps());
        holder.tvServiceCenter.setText(mRidingDestinationDetailses.get(position).getServiceStation());
        holder.tvFirstAid.setText(mRidingDestinationDetailses.get(position).getHospitals());
        holder.tvBikes.setText(mRidingDestinationDetailses.get(position).getRiders());
        holder.tvOffers.setText(mRidingDestinationDetailses.get(position).getOffers());
        String jsonInString = mRidingDestinationDetailses.get(position).getImages().toString();
        jsonInString = jsonInString.replace("\\\"", "\"");
        jsonInString = jsonInString.replace("\"{", "{");
        jsonInString = jsonInString.replace("}\"", "}");
        Log.e("jsonInString: ", ""+jsonInString);
        Uri imageUri = Uri.parse(jsonInString);
        holder.sdv.setImageURI(imageUri);

    }

    @Override
    public int getItemCount() {
        return mRidingDestinationDetailses.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.tvTitle)
        TextView tvTitle;
        @Bind(R.id.tvDesc)
        TextView tvDesc;
        @Bind(R.id.tvEatables)
        TextView tvEatables;
        @Bind(R.id.tvPetrolPump)
        TextView tvPetrolPump;
        @Bind(R.id.tvServiceCenter)
        TextView tvServiceCenter;
        @Bind(R.id.tvFirstAid)
        TextView tvFirstAid;
        @Bind(R.id.tvBikes)
        TextView tvBikes;
        @Bind(R.id.tvOffers)
        TextView tvOffers;
        @Bind(R.id.sdv)
        SimpleDraweeView sdv;
        @Bind(R.id.ivVideo)
        ImageView ivVideo;
        @Bind(R.id.ivMap)
        ImageView ivMap;
        @Bind(R.id.ivFavorites)
        ImageView ivFavorites;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
