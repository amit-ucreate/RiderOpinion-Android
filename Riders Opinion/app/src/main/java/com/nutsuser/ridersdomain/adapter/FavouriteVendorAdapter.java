package com.nutsuser.ridersdomain.adapter;

import android.app.Activity;
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
import com.nutsuser.ridersdomain.activities.FavouriteDesination;
import com.nutsuser.ridersdomain.activities.Subcribe;
import com.nutsuser.ridersdomain.activities.YouTubeVideoPlay;
import com.nutsuser.ridersdomain.utils.ApplicationGlobal;
import com.nutsuser.ridersdomain.utils.CustomizeDialog;
import com.nutsuser.ridersdomain.utils.PrefsManager;
import com.nutsuser.ridersdomain.view.CustomDialog;
import com.nutsuser.ridersdomain.web.api.FileUploadService;
import com.nutsuser.ridersdomain.web.api.ServiceGenerator;
import com.nutsuser.ridersdomain.web.pojos.FavVendor;
import com.nutsuser.ridersdomain.web.pojos.FavoriteDestination;
import com.nutsuser.ridersdomain.web.pojos.FavouriteDestinationData;
import com.nutsuser.ridersdomain.web.pojos.ModiflyBikeFav;

import java.lang.reflect.Type;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;

/**
 * Created by user on 1/8/2016.
 */
public class FavouriteVendorAdapter extends RecyclerSwipeAdapter<FavouriteVendorAdapter.SimpleViewHolder> {

    String AccessToken, vendorId, userid;
    PrefsManager prefsManager;
    private Activity mContext;
    private ArrayList<FavVendor> studentList;
    private Typeface typeHeading, typeiconfamily, typeregular;
    public CustomizeDialog mCustomizeDialog;

    public FavouriteVendorAdapter(Activity context, ArrayList<FavVendor> objects) {
        this.mContext = context;
        this.studentList = objects;
        prefsManager = new PrefsManager(context);
        typeHeading = Typeface.createFromAsset(mContext.getAssets(), "fonts/Lato-Heavy.ttf");
        typeiconfamily = Typeface.createFromAsset(mContext.getAssets(), "fonts/Lato-Bold.ttf");
        typeregular = Typeface.createFromAsset(mContext.getAssets(), "fonts/LATO-REGULAR.TTF");
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_fav_vendor, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder holder, final int position) {
        if (position % 2 == 0) {
            holder.rllayout.setBackgroundColor(Color.WHITE);
        } else {
            holder.rllayout.setBackgroundColor(Color.parseColor("#fefaf1"));
        }
        final FavVendor item = studentList.get(position);


        Log.e("item",item.getVenderId());

        holder.sdv.setImageURI(Uri.parse(item.getImage()));
        if (item.getCompany() != null) {
            holder.tvDealerName.setText(item.getCompany());
        } else {
            holder.tvDealerName.setText(item.getCompany());
        }
        if (item.getAddress() != null) {
            holder.tvLocation.setText(item.getAddress());
        } else {
            holder.tvLocation.setText("No Address");
        }
        holder.tvOffers.setText(""+item.getProductCount());
        holder.tvdes.setText(item.getDescription());


        holder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);

        // Drag From Left
        //  viewHolder.swipeLayout.addDrag(SwipeLayout.DragEdge.Left, viewHolder.swipeLayout.findViewById(R.id.bottom_wrapper1));

        // Drag From Right
        holder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right, holder.swipeLayout.findViewById(R.id.bottom_wrapper));
        holder.ivVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Intent = new Intent(mContext, YouTubeVideoPlay.class);
                Intent.putExtra("VIDEOURL", studentList.get(position).getVideoCode());
                mContext.startActivity(Intent);
                // Toast.makeText(v.getContext(), "Clicked on Map " + viewHolder.tvName.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });

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

                vendorId = item.getVenderId();
                mItemManger.removeShownLayouts(holder.swipeLayout);
                if (isNetworkConnected()) {
                    modifyYourBikefavroite(vendorId,position);
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


//        holder.ivVideo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Intent Intent = new Intent(mContext, YouTubeVideoPlay.class);
////                Intent.putExtra("VIDEOURL", studentList.get(position).getVideoUrl());
////                mContext.startActivity(Intent);
//                // Toast.makeText(v.getContext(), "Clicked on Map " + viewHolder.tvName.getText().toString(), Toast.LENGTH_SHORT).show();
//            }
//        });
//
//
//        holder.ivMap.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                ApplicationGlobal.DestID=studentList.get(position).getDestId();
////                Intent mIntent = new Intent(mContext, Subcribe.class);
////                mContext.startActivity(mIntent);
//                // Toast.makeText(view.getContext(), "Clicked on Share " + viewHolder.tvName.getText().toString(), Toast.LENGTH_SHORT).show();
//            }
//        });

        holder.rllayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemManger.closeAllItems();
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
//        if(studentList.get(position).getRating()==0.5){
//            holder.ivStar1.setVisibility(View.VISIBLE);
//            holder.ivStar2.setVisibility(View.VISIBLE);
//            holder.ivStar3.setVisibility(View.VISIBLE);
//            holder.ivStar4.setVisibility(View.VISIBLE);
//            holder.ivStar5.setVisibility(View.VISIBLE);
//            holder.ivStar1.setImageResource(R.drawable.ic_rating_orange_half);
//            holder.ivStar2.setImageResource(R.drawable.ic_ratting_grey);
//            holder.ivStar3.setImageResource(R.drawable.ic_ratting_grey);
//            holder.ivStar4.setImageResource(R.drawable.ic_ratting_grey);
//            holder.ivStar5.setImageResource(R.drawable.ic_ratting_grey);
//        }
//        else if (studentList.get(position).getRating()==1.0||studentList.get(position).getRating()==1) {
//            holder.ivStar1.setVisibility(View.VISIBLE);
//            holder.ivStar2.setVisibility(View.VISIBLE);
//            holder.ivStar3.setVisibility(View.VISIBLE);
//            holder.ivStar4.setVisibility(View.VISIBLE);
//            holder.ivStar5.setVisibility(View.VISIBLE);
//            holder.ivStar1.setImageResource(R.drawable.ic_rating_orange);
//            holder.ivStar2.setImageResource(R.drawable.ic_ratting_grey);
//            holder.ivStar3.setImageResource(R.drawable.ic_ratting_grey);
//            holder.ivStar4.setImageResource(R.drawable.ic_ratting_grey);
//            holder.ivStar5.setImageResource(R.drawable.ic_ratting_grey);
//        } else if (studentList.get(position).getRating()==1.5) {
//            holder.ivStar1.setVisibility(View.VISIBLE);
//            holder.ivStar2.setVisibility(View.VISIBLE);
//            holder.ivStar3.setVisibility(View.VISIBLE);
//            holder.ivStar4.setVisibility(View.VISIBLE);
//            holder.ivStar5.setVisibility(View.VISIBLE);
//            holder.ivStar1.setImageResource(R.drawable.ic_rating_orange);
//            holder.ivStar2.setImageResource(R.drawable.ic_rating_orange_half);
//            holder.ivStar3.setImageResource(R.drawable.ic_ratting_grey);
//            holder.ivStar4.setImageResource(R.drawable.ic_ratting_grey);
//            holder.ivStar5.setImageResource(R.drawable.ic_ratting_grey);
//        } else if (studentList.get(position).getRating()==2.0||studentList.get(position).getRating()==2) {
//            holder.ivStar1.setVisibility(View.VISIBLE);
//            holder.ivStar2.setVisibility(View.VISIBLE);
//            holder.ivStar3.setVisibility(View.VISIBLE);
//            holder.ivStar4.setVisibility(View.VISIBLE);
//            holder.ivStar5.setVisibility(View.VISIBLE);
//            holder.ivStar1.setImageResource(R.drawable.ic_rating_orange);
//            holder.ivStar2.setImageResource(R.drawable.ic_rating_orange);
//            holder.ivStar3.setImageResource(R.drawable.ic_ratting_grey);
//            holder.ivStar4.setImageResource(R.drawable.ic_ratting_grey);
//            holder.ivStar5.setImageResource(R.drawable.ic_ratting_grey);
//        } else if (studentList.get(position).getRating()==2.5) {
//            holder.ivStar1.setVisibility(View.VISIBLE);
//            holder.ivStar2.setVisibility(View.VISIBLE);
//            holder.ivStar3.setVisibility(View.VISIBLE);
//            holder.ivStar4.setVisibility(View.VISIBLE);
//            holder.ivStar5.setVisibility(View.VISIBLE);
//            holder.ivStar1.setImageResource(R.drawable.ic_rating_orange);
//            holder.ivStar2.setImageResource(R.drawable.ic_rating_orange);
//            holder.ivStar3.setImageResource(R.drawable.ic_rating_orange_half);
//            holder.ivStar4.setImageResource(R.drawable.ic_ratting_grey);
//            holder.ivStar5.setImageResource(R.drawable.ic_ratting_grey);
//        } else if (studentList.get(position).getRating()==3.0||studentList.get(position).getRating()==3) {
//            holder.ivStar1.setVisibility(View.VISIBLE);
//            holder.ivStar2.setVisibility(View.VISIBLE);
//            holder.ivStar3.setVisibility(View.VISIBLE);
//            holder.ivStar4.setVisibility(View.VISIBLE);
//            holder.ivStar5.setVisibility(View.VISIBLE);
//            holder.ivStar1.setImageResource(R.drawable.ic_rating_orange);
//            holder.ivStar2.setImageResource(R.drawable.ic_rating_orange);
//            holder.ivStar3.setImageResource(R.drawable.ic_rating_orange);
//
//            holder.ivStar4.setImageResource(R.drawable.ic_ratting_grey);
//            holder.ivStar5.setImageResource(R.drawable.ic_ratting_grey);
//        } else if (studentList.get(position).getRating()==3.5) {
//            holder.ivStar1.setVisibility(View.VISIBLE);
//            holder.ivStar2.setVisibility(View.VISIBLE);
//            holder.ivStar3.setVisibility(View.VISIBLE);
//            holder.ivStar4.setVisibility(View.VISIBLE);
//            holder.ivStar5.setVisibility(View.VISIBLE);
//            holder.ivStar1.setImageResource(R.drawable.ic_rating_orange);
//            holder.ivStar2.setImageResource(R.drawable.ic_rating_orange);
//            holder.ivStar3.setImageResource(R.drawable.ic_rating_orange);
//            holder.ivStar4.setImageResource(R.drawable.ic_rating_orange_half);
//            holder.ivStar5.setImageResource(R.drawable.ic_ratting_grey);
//        } else if (studentList.get(position).getRating()==4.0||studentList.get(position).getRating()==4) {
//            holder.ivStar1.setVisibility(View.VISIBLE);
//            holder.ivStar2.setVisibility(View.VISIBLE);
//            holder.ivStar3.setVisibility(View.VISIBLE);
//            holder.ivStar4.setVisibility(View.VISIBLE);
//            holder.ivStar5.setVisibility(View.VISIBLE);
//            holder.ivStar1.setImageResource(R.drawable.ic_rating_orange);
//            holder.ivStar2.setImageResource(R.drawable.ic_rating_orange);
//            holder.ivStar3.setImageResource(R.drawable.ic_rating_orange);
//            holder.ivStar4.setImageResource(R.drawable.ic_rating_orange);
//            holder.ivStar5.setImageResource(R.drawable.ic_ratting_grey);
//        } else if (studentList.get(position).getRating()==4.5) {
//            holder.ivStar1.setVisibility(View.VISIBLE);
//            holder.ivStar2.setVisibility(View.VISIBLE);
//            holder.ivStar3.setVisibility(View.VISIBLE);
//            holder.ivStar4.setVisibility(View.VISIBLE);
//            holder.ivStar5.setVisibility(View.VISIBLE);
//            holder.ivStar1.setImageResource(R.drawable.ic_rating_orange);
//            holder.ivStar2.setImageResource(R.drawable.ic_rating_orange);
//            holder.ivStar3.setImageResource(R.drawable.ic_rating_orange);
//            holder.ivStar4.setImageResource(R.drawable.ic_rating_orange);
//            holder.ivStar5.setImageResource(R.drawable.ic_rating_orange_half);
//        } else if (studentList.get(position).getRating()==5.0||studentList.get(position).getRating()==5) {
////            holder.ivStar1.setVisibility(View.VISIBLE);
////            holder.ivStar2.setVisibility(View.VISIBLE);
////            holder.ivStar3.setVisibility(View.VISIBLE);
////            holder.ivStar4.setVisibility(View.VISIBLE);
////            holder.ivStar5.setVisibility(View.VISIBLE);
//            holder.ivStar1.setImageResource(R.drawable.ic_rating_orange);
//            holder.ivStar2.setImageResource(R.drawable.ic_rating_orange);
//            holder.ivStar3.setImageResource(R.drawable.ic_rating_orange);
//            holder.ivStar4.setImageResource(R.drawable.ic_rating_orange);
//            holder.ivStar5.setImageResource(R.drawable.ic_rating_orange);
//        } else {
////            holder.ivStar1.setImageResource(R.drawable.ic_ratting_grey);
////            holder.ivStar2.setImageResource(R.drawable.ic_ratting_grey);
////            holder.ivStar3.setImageResource(R.drawable.ic_ratting_grey);
////            holder.ivStar4.setImageResource(R.drawable.ic_ratting_grey);
////            holder.ivStar5.setImageResource(R.drawable.ic_ratting_grey);
//            holder.ivStar1.setVisibility(View.GONE);
//            holder.ivStar2.setVisibility(View.GONE);
//            holder.ivStar3.setVisibility(View.GONE);
//            holder.ivStar4.setVisibility(View.GONE);
//            holder.ivStar5.setVisibility(View.GONE);
//        }

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


    //  ViewHolder Class

    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        SwipeLayout swipeLayout;
        FrameLayout fmremove,fmVendoriList;
       // @Bind(R.id.ivVideo)
        ImageView  ivVideo;
        ///@Bind(R.id.ivMap)
        ImageView ivMap;
      /*  @Bind(R.id.ivStar1)
        ImageView ivStar1;
        @Bind(R.id.ivStar2)
        ImageView ivStar2;
        @Bind(R.id.ivStar3)
        ImageView ivStar3;
        @Bind(R.id.ivStar4)
        ImageView ivStar4;
        @Bind(R.id.ivStar5)
        ImageView ivStar5;*/
        RelativeLayout rllayout;
        TextView tvDealerName,tvLocation,tvOffers,tvdes;
        SimpleDraweeView sdv;

        public SimpleViewHolder(View itemView) {
            super(itemView);
           // ButterKnife.bind(this, itemView);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            fmremove = (FrameLayout) itemView.findViewById(R.id.fmremove);
            rllayout= (RelativeLayout) itemView.findViewById(R.id.rllayout);
            rllayout=(RelativeLayout)itemView.findViewById(R.id.rllayout);
            tvDealerName=(TextView)itemView.findViewById(R.id.tvDealerName);
            tvLocation=(TextView)itemView.findViewById(R.id.tvLocation);
            tvOffers=(TextView)itemView.findViewById(R.id.tvOffers);
            tvdes=(TextView)itemView.findViewById(R.id.tvdes);
            sdv=(SimpleDraweeView)itemView.findViewById(R.id.sdv);
            ivVideo=(ImageView)itemView.findViewById(R.id.ivVideo);

        }
    }

    //============== remove fav vendor===================//

    private void modifyYourBikefavroite(String VenderId, final int pos) {

        showProgressDialog();

        String userId = prefsManager.getCaseId();

        String accessToken = prefsManager.getToken();

        FileUploadService service = ServiceGenerator.createService(FileUploadService.BASE_URL);

        service.modifyYourBikefavroite(VenderId, userId,accessToken, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, retrofit.client.Response response) {

                Type type = new TypeToken<ModiflyBikeFav>() {
                }.getType();
                ModiflyBikeFav mFavoriteDestination = new Gson().fromJson(jsonObject.toString(), type);


                if (mFavoriteDestination.getSuccess()==1) {
                    dismissProgressDialog();
                    if (mFavoriteDestination.getFav()==0) {
                        studentList.remove(pos);
                        notifyItemRemoved(pos);
                        notifyItemRangeChanged(pos, studentList.size());
                        FavouriteDesination.tvVendorCount.setText(""+studentList.size());
                        mItemManger.closeAllItems();
                        if (studentList.size() == 0) {
                            FavouriteDesination.tvVendorCount.setVisibility(View.GONE);
                            FavouriteDesination.LvVendors.setVisibility(View.GONE);
                          //  CustomDialog.showProgressDialog(mContext, "Oop's  didn't found what you are looking ! Try some other keyword.");
                        }
                      // CustomDialog.FavshowProgressDialog(mContext, mFavoriteDestination.getMessage().toString());
                    } else {

                        //ivFav.setImageResource(R.drawable.icon_add_favorites);
                       // CustomDialog.FavshowProgressDialog(mContext, mFavoriteDestination.getMessage().toString());
                    }


                }
                else{
                    dismissProgressDialog();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                dismissProgressDialog();
                Log.e("DataUploading------", "Data Uploading Failure......" + error);
            }
        });
    }

    //======== progress dialog===========//
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

