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
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.activities.DestinationsDetailActivity;
import com.nutsuser.ridersdomain.activities.FavouriteDesination;
import com.nutsuser.ridersdomain.activities.Subcribe;
import com.nutsuser.ridersdomain.activities.YouTubeVideoPlay;
import com.nutsuser.ridersdomain.utils.ApplicationGlobal;
import com.nutsuser.ridersdomain.utils.PrefsManager;
import com.nutsuser.ridersdomain.view.CustomDialog;
import com.nutsuser.ridersdomain.web.api.FileUploadService;
import com.nutsuser.ridersdomain.web.api.ServiceGenerator;
import com.nutsuser.ridersdomain.web.pojos.FavoriteDestination;
import com.nutsuser.ridersdomain.web.pojos.FavouriteDestinationData;

import java.lang.reflect.Type;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;

/**
 * Created by user on 1/8/2016.
 */
public class FavouriteDestinationAdapter extends RecyclerSwipeAdapter<FavouriteDestinationAdapter.SimpleViewHolder> {

    String AccessToken, joinEventId, userid;
    PrefsManager prefsManager;
    private Activity mContext;
    private ArrayList<FavouriteDestinationData> studentList;
    private Typeface typeHeading, typeiconfamily, typeregular;

    public FavouriteDestinationAdapter(Activity context, ArrayList<FavouriteDestinationData> objects) {
        this.mContext = context;
        this.studentList = objects;
        prefsManager = new PrefsManager(context);
        typeHeading = Typeface.createFromAsset(mContext.getAssets(), "fonts/Lato-Heavy.ttf");
        typeiconfamily = Typeface.createFromAsset(mContext.getAssets(), "fonts/Lato-Bold.ttf");
        typeregular = Typeface.createFromAsset(mContext.getAssets(), "fonts/LATO-REGULAR.TTF");
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favourite_swipe_row, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder holder, final int position) {
        if (position % 2 == 0) {
            holder.rllayout.setBackgroundColor(Color.WHITE);
        } else {
            holder.rllayout.setBackgroundColor(Color.parseColor("#fefaf1"));
        }
        final FavouriteDestinationData item = studentList.get(position);
        holder.tvDesc.setTypeface(typeregular);
        holder.tvRestaurant.setTypeface(typeregular);
        holder.tvPetrolPump.setTypeface(typeregular);
        holder.tvServiceStation.setTypeface(typeregular);
        holder.tvRiders.setTypeface(typeregular);
        holder.tvHospitals.setTypeface(typeregular);
        holder.tvTitle.setTypeface(typeHeading);
        if(item.getDestName().isEmpty()){
            holder.tvTitle.setText(item.getDestName());
        }else {
            holder.tvTitle.setText(item.getDestName().trim());
        }
        holder.tvDesc.setText(item.getDescription());
        holder.tvRestaurant.setText("" + item.getRestaurant());
        holder.tvPetrolPump.setText("" + item.getPetrolpumps());
        holder.tvServiceStation.setText("" + item.getServiceStation());
        holder.tvHospitals.setText("" + item.getHospitals());
        holder.tvRiders.setText("" + item.getRiders());
        holder.tvOffers.setText("" + item.getOffers());
        holder.sdvPostImage.setImageURI(Uri.parse(item.getImages()));

        holder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);

        // Drag From Left
        //  viewHolder.swipeLayout.addDrag(SwipeLayout.DragEdge.Left, viewHolder.swipeLayout.findViewById(R.id.bottom_wrapper1));

        // Drag From Right
        holder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right, holder.swipeLayout.findViewById(R.id.bottom_wrapper));


        // Handling different events when swiping
        holder.swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onClose(SwipeLayout layout) {
                //when the SurfaceView totally cover the BottomView.
            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
                //you are swiping.
            }

            @Override
            public void onStartOpen(SwipeLayout layout) {

            }

            @Override
            public void onOpen(SwipeLayout layout) {
                //when the BottomView totally show.
            }

            @Override
            public void onStartClose(SwipeLayout layout) {

            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                //when user's hand released.
            }
        });

        holder.fmremove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                joinEventId = item.getDestId();
                mItemManger.removeShownLayouts(holder.swipeLayout);
                if (isNetworkConnected()) {
                    RemoveFav(joinEventId, position);
                }else{
                    Toast.makeText(mContext,"Internet is not connected.",Toast.LENGTH_SHORT).show();;
                }

            }
        });

        holder.swipeLayout.getSurfaceView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(mContext, " onClick : " + item.getDestName(), Toast.LENGTH_SHORT).show();
            }
        });


        holder.ivVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Intent = new Intent(mContext, YouTubeVideoPlay.class);
                Intent.putExtra("VIDEOURL", studentList.get(position).getVideoUrl());
                mContext.startActivity(Intent);
                // Toast.makeText(v.getContext(), "Clicked on Map " + viewHolder.tvName.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });


        holder.ivMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApplicationGlobal.DestID=studentList.get(position).getDestId();
                Intent mIntent = new Intent(mContext, Subcribe.class);
                mContext.startActivity(mIntent);
                // Toast.makeText(view.getContext(), "Clicked on Share " + viewHolder.tvName.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        holder.fmDestiList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String string = studentList.get(position).getDestId();
//                Intent mIntent = new Intent(mContext, DestinationsDetailActivity.class);
//
//                ApplicationGlobal.DestID = string;
//                mContext.startActivity(mIntent);
            }
        });

      /*  viewHolder.tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //  Toast.makeText(view.getContext(), "Clicked on Edit  " + viewHolder.tvName.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });*/


      /*  viewHolder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemManger.removeShownLayouts(viewHolder.swipeLayout);
                studentList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, studentList.size());
                mItemManger.closeAllItems();
                Toast.makeText(view.getContext(), "Deleted " + viewHolder.tvName.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });*/
        if(studentList.get(position).getRating()==0.5){
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
        else if (studentList.get(position).getRating()==1.0||studentList.get(position).getRating()==1) {
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
        } else if (studentList.get(position).getRating()==1.5) {
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
        } else if (studentList.get(position).getRating()==2.0||studentList.get(position).getRating()==2) {
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
        } else if (studentList.get(position).getRating()==2.5) {
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
        } else if (studentList.get(position).getRating()==3.0||studentList.get(position).getRating()==3) {
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
        } else if (studentList.get(position).getRating()==3.5) {
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
        } else if (studentList.get(position).getRating()==4.0||studentList.get(position).getRating()==4) {
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
        } else if (studentList.get(position).getRating()==4.5) {
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
        } else if (studentList.get(position).getRating()==5.0||studentList.get(position).getRating()==5) {
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

        // mItemManger is member in RecyclerSwipeAdapter Class
        mItemManger.bindView(holder.itemView, position);

    }
    public boolean isNetworkConnected() {
        if (ApplicationGlobal.isNetworkConnected(mContext))
            return true;
        else
            return false;
    }
    @Override
    public int getItemCount() {
        return studentList.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    /**
     * Match RemoveUpcoming .
     */
    public void RemoveFav(final String joinid, final int pos) {
        // showProgressDialog();
        Log.e(" UnFavouriteDestination", " UnFavouriteDestination");
        Log.e(" joinid:", " " + joinid);
        Log.e(" pos:", " " + pos);
        try {
            userid = prefsManager.getCaseId();
            Log.e(" userid:", " " + userid);

            AccessToken = prefsManager.getToken();
            Log.e(" AccessToken:", " " + AccessToken);
            FileUploadService service = ServiceGenerator.createService(FileUploadService.BASE_URL);
            service.remove_fav(userid, joinid, AccessToken, new Callback<JsonObject>() {
                @Override
                public void success(JsonObject jsonObject, retrofit.client.Response response) {
                    Log.e("Upload", "success");
                    Log.e("jsonObject:", "" + jsonObject.toString());

                    Type type = new TypeToken<FavoriteDestination>() {
                    }.getType();
                    FavoriteDestination mFavoriteDestination = new Gson().fromJson(jsonObject.toString(), type);
                    if (mFavoriteDestination.getSuccess().equals("1")) {
                        Log.e("Success:", "" + mFavoriteDestination.getSuccess());
                        studentList.remove(pos);
                        notifyItemRemoved(pos);
                        notifyItemRangeChanged(pos, studentList.size());
                        FavouriteDesination.tvFavCount.setText(""+studentList.size());
                        mItemManger.closeAllItems();
                        if (studentList.size() == 0) {
                            FavouriteDesination.tvFavCount.setVisibility(View.GONE);
                            FavouriteDesination.LvDestination.setVisibility(View.GONE);
                           // CustomDialog.showProgressDialog(mContext, "Oop's  didn't found what you are looking ! Try some other keyword.");
                        }
                    }
                }

                @Override
                public void failure(RetrofitError error) {

                    Log.e("Upload", "error:" + error);
                }
            });


        } catch (Exception e) {
            e.printStackTrace();

        }
    }
    //  ViewHolder Class

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        SwipeLayout swipeLayout;
        //  TextView tvName;
        // TextView tvEmailId;
        // TextView tvDelete;
        //  TextView tvEdit;
        TextView tvShare, tvTitle, tvDesc, tvRestaurant, tvPetrolPump, tvServiceStation, tvHospitals, tvRiders, tvOffers;
        ImageButton btnLocation;
        ImageView ivimage, ivVideo, ivMap;
        FrameLayout fmremove,fmDestiList;
        RelativeLayout rllayout;
        SimpleDraweeView sdvPostImage;
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

        public SimpleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            fmremove = (FrameLayout) itemView.findViewById(R.id.fmremove);
            fmDestiList = (FrameLayout) itemView.findViewById(R.id.fmDestiList);
            // tvEmailId = (TextView) itemView.findViewById(R.id.tvEmailId);
            //  tvDelete = (TextView) itemView.findViewById(R.id.tvDelete);
            // tvEdit = (TextView) itemView.findViewById(R.id.tvEdit);
            tvShare = (TextView) itemView.findViewById(R.id.tvShare);
            btnLocation = (ImageButton) itemView.findViewById(R.id.btnLocation);
            ivimage = (ImageView) itemView.findViewById(R.id.ivimage);
            rllayout = (RelativeLayout) itemView.findViewById(R.id.rllayout);
            sdvPostImage = (SimpleDraweeView) itemView.findViewById(R.id.sdvPostImage);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvDesc = (TextView) itemView.findViewById(R.id.tvDesc);

            tvRestaurant = (TextView) itemView.findViewById(R.id.tvRestaurant);
            tvPetrolPump = (TextView) itemView.findViewById(R.id.tvPetrolPump);
            tvServiceStation = (TextView) itemView.findViewById(R.id.tvServiceStation);
            tvHospitals = (TextView) itemView.findViewById(R.id.tvHospitals);
            tvRiders = (TextView) itemView.findViewById(R.id.tvRiders);
            tvOffers = (TextView) itemView.findViewById(R.id.tvOffers);
            ivVideo = (ImageView) itemView.findViewById(R.id.ivVideo);
            ivMap= (ImageView) itemView.findViewById(R.id.ivMap);
        }
    }
}

