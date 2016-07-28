package com.nutsuser.ridersdomain.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.activities.DestinationsDetailActivity;
import com.nutsuser.ridersdomain.activities.DestinationsListActivity;
import com.nutsuser.ridersdomain.activities.Subcribe;
import com.nutsuser.ridersdomain.activities.YouTubeVideoPlay;
import com.nutsuser.ridersdomain.services.GPSService;
import com.nutsuser.ridersdomain.utils.ApplicationGlobal;
import com.nutsuser.ridersdomain.utils.CustomizeDialog;
import com.nutsuser.ridersdomain.utils.PrefsManager;
import com.nutsuser.ridersdomain.view.CustomDialog;
import com.nutsuser.ridersdomain.web.api.FileUploadService;
import com.nutsuser.ridersdomain.web.api.ServiceGenerator;
import com.nutsuser.ridersdomain.web.pojos.FavoriteDestination;
import com.nutsuser.ridersdomain.web.pojos.RidingDestination;
import com.nutsuser.ridersdomain.web.pojos.RidingDestinationDetails;

import java.lang.reflect.Type;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;

/**
 * Created by user on 8/28/2015.
 */
public class AdapterDestination extends RecyclerView.Adapter<AdapterDestination.ViewHolder> {
    PrefsManager prefsManager;
    String star_lat, star_long;
    String AccessToken, UserId;
    CustomizeDialog mCustomizeDialog;
    double start1, end1;
    private final Activity mContext;
    private ArrayList<RidingDestinationDetails> mRidingDestinationDetailses;
    private Typeface typeHeading, typeiconfamily;

    public AdapterDestination(Activity context, ArrayList<RidingDestinationDetails> mRidingDestinationDetailses) {
        this.mContext = context;
        prefsManager = new PrefsManager(mContext);
        this.mRidingDestinationDetailses = mRidingDestinationDetailses;
        typeHeading = Typeface.createFromAsset(mContext.getAssets(), "fonts/Lato-Heavy.ttf");
        typeiconfamily = Typeface.createFromAsset(mContext.getAssets(), "fonts/Lato-Bold.ttf");
    }

    @Override
    public AdapterDestination.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout
                        .item_destination, parent,
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
        holder.tvTitle.setTypeface(typeHeading);
        holder.tvDesc.setTypeface(typeiconfamily);
        holder.tvEatables.setTypeface(typeiconfamily);
        holder.tvPetrolPump.setTypeface(typeiconfamily);
        holder.tvServiceCenter.setTypeface(typeiconfamily);
        holder.tvFirstAid.setTypeface(typeiconfamily);
        holder.tvBikes.setTypeface(typeiconfamily);
        holder.tvTitle.setText(mRidingDestinationDetailses.get(position).getDestName().toUpperCase());
        holder.tvDesc.setText(mRidingDestinationDetailses.get(position).getDescription());
        holder.tvEatables.setText(mRidingDestinationDetailses.get(position).getRestaurant());
        holder.tvPetrolPump.setText(mRidingDestinationDetailses.get(position).getPetrolpumps());
        holder.tvServiceCenter.setText(mRidingDestinationDetailses.get(position).getServiceStation());
        holder.tvFirstAid.setText(mRidingDestinationDetailses.get(position).getHospitals());
        holder.tvBikes.setText(mRidingDestinationDetailses.get(position).getRiders());
        holder.txtStateName.setText(mRidingDestinationDetailses.get(position).getState());

        Log.e("dest Name:", mRidingDestinationDetailses.get(position).getDestName());
        Log.e("dest Rating:", mRidingDestinationDetailses.get(position).getRating()+"  : "+mRidingDestinationDetailses.get(position).getRating().matches("4.5"));


        // holder.tvOffers.setText(mRidingDestinationDetailses.get(position).getOffers());

        if (mRidingDestinationDetailses.get(position).getFavroite().matches("1")) {
            holder.ivFavorites.setImageResource(R.drawable.icon_remove_favorite);
        } else {
            holder.ivFavorites.setImageResource(R.drawable.icon_add_favorites);
        }
        String jsonInString = mRidingDestinationDetailses.get(position).getImages().toString();
        jsonInString = jsonInString.replace("\\\"", "\"");
        jsonInString = jsonInString.replace("\"{", "{");
        jsonInString = jsonInString.replace("}\"", "}");
        Log.e("jsonInString: ", "" + jsonInString);
        Uri imageUri = Uri.parse(jsonInString);
        holder.sdv.setImageURI(imageUri);

        if(mRidingDestinationDetailses.get(position).getRating().matches("0.5")){
            holder.ivStar1.setVisibility(View.VISIBLE);
            holder.ivStar2.setVisibility(View.VISIBLE);
            holder.ivStar3.setVisibility(View.VISIBLE);
            holder.ivStar4.setVisibility(View.VISIBLE);
            holder.ivStar5.setVisibility(View.VISIBLE);
            holder.ivStar1.setImageResource(R.drawable.ic_rating_orange_half);
            holder.ivStar2.setImageResource(R.drawable.ic_ratting_grey);
            holder.ivStar3.setImageResource(R.drawable.ic_ratting_grey);
            holder.ivStar4.setImageResource(R.drawable.ic_ratting_grey);
            holder.ivStar5.setImageResource(R.drawable.ic_ratting_grey);
        }
       else if (mRidingDestinationDetailses.get(position).getRating().matches("1.0")||mRidingDestinationDetailses.get(position).getRating().matches("1")) {
            holder.ivStar1.setVisibility(View.VISIBLE);
            holder.ivStar2.setVisibility(View.VISIBLE);
            holder.ivStar3.setVisibility(View.VISIBLE);
            holder.ivStar4.setVisibility(View.VISIBLE);
            holder.ivStar5.setVisibility(View.VISIBLE);
            holder.ivStar1.setImageResource(R.drawable.ic_rating_orange);
            holder.ivStar2.setImageResource(R.drawable.ic_ratting_grey);
            holder.ivStar3.setImageResource(R.drawable.ic_ratting_grey);
            holder.ivStar4.setImageResource(R.drawable.ic_ratting_grey);
            holder.ivStar5.setImageResource(R.drawable.ic_ratting_grey);
        } else if (mRidingDestinationDetailses.get(position).getRating().matches("1.5")) {
            holder.ivStar1.setVisibility(View.VISIBLE);
            holder.ivStar2.setVisibility(View.VISIBLE);
            holder.ivStar3.setVisibility(View.VISIBLE);
            holder.ivStar4.setVisibility(View.VISIBLE);
            holder.ivStar5.setVisibility(View.VISIBLE);
            holder.ivStar1.setImageResource(R.drawable.ic_rating_orange);
            holder.ivStar2.setImageResource(R.drawable.ic_rating_orange_half);
            holder.ivStar3.setImageResource(R.drawable.ic_ratting_grey);
            holder.ivStar4.setImageResource(R.drawable.ic_ratting_grey);
            holder.ivStar5.setImageResource(R.drawable.ic_ratting_grey);
        } else if (mRidingDestinationDetailses.get(position).getRating().matches("2.0")||mRidingDestinationDetailses.get(position).getRating().matches("2")) {
            holder.ivStar1.setVisibility(View.VISIBLE);
            holder.ivStar2.setVisibility(View.VISIBLE);
            holder.ivStar3.setVisibility(View.VISIBLE);
            holder.ivStar4.setVisibility(View.VISIBLE);
            holder.ivStar5.setVisibility(View.VISIBLE);
            holder.ivStar1.setImageResource(R.drawable.ic_rating_orange);
            holder.ivStar2.setImageResource(R.drawable.ic_rating_orange);
            holder.ivStar3.setImageResource(R.drawable.ic_ratting_grey);
            holder.ivStar4.setImageResource(R.drawable.ic_ratting_grey);
            holder.ivStar5.setImageResource(R.drawable.ic_ratting_grey);
        } else if (mRidingDestinationDetailses.get(position).getRating().matches("2.5")) {
            holder.ivStar1.setVisibility(View.VISIBLE);
            holder.ivStar2.setVisibility(View.VISIBLE);
            holder.ivStar3.setVisibility(View.VISIBLE);
            holder.ivStar4.setVisibility(View.VISIBLE);
            holder.ivStar5.setVisibility(View.VISIBLE);
            holder.ivStar1.setImageResource(R.drawable.ic_rating_orange);
            holder.ivStar2.setImageResource(R.drawable.ic_rating_orange);
            holder.ivStar3.setImageResource(R.drawable.ic_rating_orange_half);
            holder.ivStar4.setImageResource(R.drawable.ic_ratting_grey);
            holder.ivStar5.setImageResource(R.drawable.ic_ratting_grey);
        } else if (mRidingDestinationDetailses.get(position).getRating().matches("3.0")||mRidingDestinationDetailses.get(position).getRating().matches("3")) {
            holder.ivStar1.setVisibility(View.VISIBLE);
            holder.ivStar2.setVisibility(View.VISIBLE);
            holder.ivStar3.setVisibility(View.VISIBLE);
            holder.ivStar4.setVisibility(View.VISIBLE);
            holder.ivStar5.setVisibility(View.VISIBLE);
            holder.ivStar1.setImageResource(R.drawable.ic_rating_orange);
            holder.ivStar2.setImageResource(R.drawable.ic_rating_orange);
            holder.ivStar3.setImageResource(R.drawable.ic_rating_orange);

            holder.ivStar4.setImageResource(R.drawable.ic_ratting_grey);
            holder.ivStar5.setImageResource(R.drawable.ic_ratting_grey);
        } else if (mRidingDestinationDetailses.get(position).getRating().matches("3.5")) {
            holder.ivStar1.setVisibility(View.VISIBLE);
            holder.ivStar2.setVisibility(View.VISIBLE);
            holder.ivStar3.setVisibility(View.VISIBLE);
            holder.ivStar4.setVisibility(View.VISIBLE);
            holder.ivStar5.setVisibility(View.VISIBLE);
            holder.ivStar1.setImageResource(R.drawable.ic_rating_orange);
            holder.ivStar2.setImageResource(R.drawable.ic_rating_orange);
            holder.ivStar3.setImageResource(R.drawable.ic_rating_orange);
            holder.ivStar4.setImageResource(R.drawable.ic_rating_orange_half);
            holder.ivStar5.setImageResource(R.drawable.ic_ratting_grey);
        } else if (mRidingDestinationDetailses.get(position).getRating().matches("4.0")||mRidingDestinationDetailses.get(position).getRating().matches("4")) {
            holder.ivStar1.setVisibility(View.VISIBLE);
            holder.ivStar2.setVisibility(View.VISIBLE);
            holder.ivStar3.setVisibility(View.VISIBLE);
            holder.ivStar4.setVisibility(View.VISIBLE);
            holder.ivStar5.setVisibility(View.VISIBLE);
            holder.ivStar1.setImageResource(R.drawable.ic_rating_orange);
            holder.ivStar2.setImageResource(R.drawable.ic_rating_orange);
            holder.ivStar3.setImageResource(R.drawable.ic_rating_orange);
            holder.ivStar4.setImageResource(R.drawable.ic_rating_orange);
            holder.ivStar5.setImageResource(R.drawable.ic_ratting_grey);
        } else if (mRidingDestinationDetailses.get(position).getRating().matches("4.5")) {
            holder.ivStar1.setVisibility(View.VISIBLE);
            holder.ivStar2.setVisibility(View.VISIBLE);
            holder.ivStar3.setVisibility(View.VISIBLE);
            holder.ivStar4.setVisibility(View.VISIBLE);
            holder.ivStar5.setVisibility(View.VISIBLE);
            holder.ivStar1.setImageResource(R.drawable.ic_rating_orange);
            holder.ivStar2.setImageResource(R.drawable.ic_rating_orange);
            holder.ivStar3.setImageResource(R.drawable.ic_rating_orange);
            holder.ivStar4.setImageResource(R.drawable.ic_rating_orange);
            holder.ivStar5.setImageResource(R.drawable.ic_rating_orange_half);
        } else if (mRidingDestinationDetailses.get(position).getRating().matches("5.0")||mRidingDestinationDetailses.get(position).getRating().matches("5")) {
//            holder.ivStar1.setVisibility(View.VISIBLE);
//            holder.ivStar2.setVisibility(View.VISIBLE);
//            holder.ivStar3.setVisibility(View.VISIBLE);
//            holder.ivStar4.setVisibility(View.VISIBLE);
//            holder.ivStar5.setVisibility(View.VISIBLE);
            holder.ivStar1.setImageResource(R.drawable.ic_rating_orange);
            holder.ivStar2.setImageResource(R.drawable.ic_rating_orange);
            holder.ivStar3.setImageResource(R.drawable.ic_rating_orange);
            holder.ivStar4.setImageResource(R.drawable.ic_rating_orange);
            holder.ivStar5.setImageResource(R.drawable.ic_rating_orange);
        } else {
//            holder.ivStar1.setImageResource(R.drawable.ic_ratting_grey);
//            holder.ivStar2.setImageResource(R.drawable.ic_ratting_grey);
//            holder.ivStar3.setImageResource(R.drawable.ic_ratting_grey);
//            holder.ivStar4.setImageResource(R.drawable.ic_ratting_grey);
//            holder.ivStar5.setImageResource(R.drawable.ic_ratting_grey);
            holder.ivStar1.setVisibility(View.GONE);
            holder.ivStar2.setVisibility(View.GONE);
            holder.ivStar3.setVisibility(View.GONE);
            holder.ivStar4.setVisibility(View.GONE);
            holder.ivStar5.setVisibility(View.GONE);
        }
        holder.rl_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string = mRidingDestinationDetailses.get(position).getDestId();
                Intent mIntent = new Intent(mContext, DestinationsDetailActivity.class);

                ApplicationGlobal.DestID = string;
                mContext.startActivity(mIntent);
            }
        });

        holder.layoutDestName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string = mRidingDestinationDetailses.get(position).getDestId();
                Intent mIntent = new Intent(mContext, DestinationsDetailActivity.class);

                ApplicationGlobal.DestID = string;
                mContext.startActivity(mIntent);
            }
        });

        holder.sdv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string = mRidingDestinationDetailses.get(position).getDestId();
                Intent mIntent = new Intent(mContext, DestinationsDetailActivity.class);

                ApplicationGlobal.DestID = string;
                mContext.startActivity(mIntent);
            }
        });
        holder.ivVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Intent = new Intent(mContext, YouTubeVideoPlay.class);
                Intent.putExtra("VIDEOURL", mRidingDestinationDetailses.get(position).getVideoUrl());
                mContext.startActivity(Intent);
            }
        });

        holder.ivMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(mContext, Subcribe.class);
                String string = mRidingDestinationDetailses.get(position).getDestId();
                ApplicationGlobal.DestID = string;

                mContext.startActivity(mIntent);
              /*  String lat = mRidingDestinationDetailses.get(position).getDestLatitude();
                String lon = mRidingDestinationDetailses.get(position).getDestLatitude();
                Intent mIntent = new Intent(mContext, MapActivity.class);
                mIntent.putExtra("endLat", lat);
                mIntent.putExtra("endLon", lon);
                mContext.startActivity(mIntent);*/
            }
        });
        holder.ivFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GPSService mGPSService = new GPSService(mContext);
                mGPSService.getLocation();

                if (mGPSService.isLocationAvailable == false) {

                    // Here you can ask the user to try again, using return; for that
                    Toast.makeText(mContext, "Your location is not available, please try again.", Toast.LENGTH_SHORT).show();
                    return;

                    // Or you can continue without getting the location, remove the return; above and uncomment the line given below
                    // address = "Location not available";
                } else {

                    // Getting location co-ordinates
                    double latitude = mGPSService.getLatitude();
                    double longitude = mGPSService.getLongitude();
                    //Toast.makeText(getApplicationContext(), "Latitude:" + latitude + " | Longitude: " + longitude, Toast.LENGTH_LONG).show();

                    start1 = mGPSService.getLatitude();
                    end1 = mGPSService.getLongitude();
                    star_lat = String.valueOf(start1);
                    star_long = String.valueOf(end1);
                    if (isNetworkConnected()) {
                        Fav(mRidingDestinationDetailses.get(position).getDestId());
                    } else {
                        showToast("Internet Not Connected");
                    }

                }


                // make sure you close the gps after using it. Save user's battery power
                mGPSService.closeGPS();
            }
        });
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
        /* @Bind(R.id.tvOffers)
         TextView tvOffers;*/
        @Bind(R.id.sdv)
        SimpleDraweeView sdv;
        @Bind(R.id.ivVideo)
        ImageView ivVideo;
        @Bind(R.id.ivMap)
        ImageView ivMap;
        @Bind(R.id.ivFavorites)
        ImageView ivFavorites;
        @Bind(R.id.rllayout)
        RelativeLayout rllayout;
        @Bind(R.id.ivStar1)
        ImageView ivStar1;
        @Bind(R.id.ivStar2)
        ImageView ivStar2;
        @Bind(R.id.ivStar3)
        ImageView ivStar3;
        @Bind(R.id.ivStar4)
        ImageView ivStar4;
        @Bind(R.id.ivStar5)
        ImageView ivStar5;
        @Bind(R.id.rl_layout)
        RelativeLayout rl_layout;
        @Bind(R.id.txtStateName)
        TextView txtStateName;
        @Bind(R.id.layoutDestName)
        LinearLayout layoutDestName;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * updateOlderData .
     */
    public void updateOlderData() {
        String sorttype = null;
        showProgressDialog();
        Log.e("riding destination", "riding destination");
        try {
            prefsManager = new PrefsManager(mContext);
            AccessToken = prefsManager.getToken();
            UserId = prefsManager.getCaseId();
            String radius = prefsManager.getRadius();
            FileUploadService service = ServiceGenerator.createService(FileUploadService.BASE_URL);
            if(DestinationsListActivity.type.matches("Top Rated")){
                sorttype="toprated";
            }
            else if(DestinationsListActivity.type.matches("Nearest")){
                sorttype="nearest";
            }
            else if(DestinationsListActivity.type.matches("Furthest")){
                sorttype="furthest";
            }
            service.ridingDestination(UserId, star_long, star_lat, radius, AccessToken,sorttype, new Callback<JsonObject>() {
                @Override
                public void success(JsonObject jsonObject, retrofit.client.Response response) {

                    Type type = new TypeToken<RidingDestination>() {
                    }.getType();
                    RidingDestination mRidingDestination = new Gson().fromJson(jsonObject.toString(), type);

                    mRidingDestinationDetailses.clear();

                    if (mRidingDestination.getSuccess().equals("1")) {
                        dismissProgressDialog();
                        mRidingDestinationDetailses.addAll(mRidingDestination.getData());
                        notifyDataSetChanged();
                        //   rvDestinations.setAdapter(new AdapterDestination(DestinationsListActivity.this, mRidingDestinationDetailses));
                    } else {
                        dismissProgressDialog();
                        CustomDialog.showProgressDialog(mContext, mRidingDestination.getMessage().toString());
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    dismissProgressDialog();
                    Log.d("DataUploading------", "Data Uploading Failure......" + error);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();

        }
    }


    /**
     * updateOlderData .
     */
    public void Fav(String desid) {
        showProgressDialog();
        Log.e("riding destination", "riding destination");
        try {
            prefsManager = new PrefsManager(mContext);
            AccessToken = prefsManager.getToken();
            UserId = prefsManager.getCaseId();
            String radius = prefsManager.getRadius();
            FileUploadService service = ServiceGenerator.createService(FileUploadService.BASE_URL);

            service.ridingDestination_favroite(UserId, desid, AccessToken, new Callback<JsonObject>() {
                @Override
                public void success(JsonObject jsonObject, retrofit.client.Response response) {

                    Type type = new TypeToken<FavoriteDestination>() {
                    }.getType();
                    FavoriteDestination mFavoriteDestination = new Gson().fromJson(jsonObject.toString(), type);

                    if (mFavoriteDestination.getSuccess().equals("1")) {
                        dismissProgressDialog();
                        updateOlderData();
                    } else {
                        dismissProgressDialog();
                        CustomDialog.showProgressDialog(mContext, mFavoriteDestination.getMessage().toString());
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    dismissProgressDialog();
                    Log.d("DataUploading------", "Data Uploading Failure......" + error);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public boolean isNetworkConnected() {
        if (ApplicationGlobal.isNetworkConnected(mContext))
            return true;
        else
            return false;
    }

    protected void showToast(String message) {

        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    public void showProgressDialog() {

        mCustomizeDialog = new CustomizeDialog(mContext);
        mCustomizeDialog.setCancelable(false);
        mCustomizeDialog.show();
        Log.e("HERE", "HERE");
    }

    public void dismissProgressDialog() {

        if (mCustomizeDialog != null && mCustomizeDialog.isShowing()) {
            mCustomizeDialog.dismiss();
            mCustomizeDialog = null;
        }


    }
}
