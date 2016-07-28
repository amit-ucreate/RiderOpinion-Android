package com.nutsuser.ridersdomain.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.google.gson.JsonObject;
import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.activities.DestinationsDetailActivity;
import com.nutsuser.ridersdomain.activities.EventDetailActivity;
import com.nutsuser.ridersdomain.activities.PublicProfileScreen;
import com.nutsuser.ridersdomain.utils.ApplicationGlobal;
import com.nutsuser.ridersdomain.utils.CONSTANTS;
import com.nutsuser.ridersdomain.utils.PrefsManager;
import com.nutsuser.ridersdomain.view.CustomDialog;
import com.nutsuser.ridersdomain.web.api.FileUploadService;
import com.nutsuser.ridersdomain.web.api.ServiceGenerator;
import com.nutsuser.ridersdomain.web.pojos.DataNotificationListResponse;
import com.nutsuser.ridersdomain.web.pojos.RidingEventData;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by ucreateuser on 5/20/2016.
 */
public class NotificationAdapter extends RecyclerSwipeAdapter<NotificationAdapter.ViewHolder> implements CONSTANTS{
    private final Activity mContext;
    ArrayList<DataNotificationListResponse> data;
    private Typeface typeHeading, typeiconfamily, typeregular;
    PrefsManager prefsManager;


    public NotificationAdapter(Activity context, ArrayList<DataNotificationListResponse> data) {
        mContext = context;
        this.data = data;
        Log.e("data",""+data.size());
        prefsManager = new PrefsManager(context);
        typeHeading = Typeface.createFromAsset(mContext.getAssets(), "fonts/Lato-Heavy.ttf");
        typeiconfamily = Typeface.createFromAsset(mContext.getAssets(), "fonts/Lato-Bold.ttf");
        typeregular = Typeface.createFromAsset(mContext.getAssets(), "fonts/LATO-REGULAR.TTF");
    }

    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout
                        .layout_notification_adapter, parent,
                false);

        return new ViewHolder(v);
    }
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (position % 2 == 0) {
            holder.rllayout.setBackgroundColor(Color.WHITE);
        } else {
            holder.rllayout.setBackgroundColor(Color.parseColor("#fefaf1"));
        }
        if(data.get(position).getType().equalsIgnoreCase("Event")){
            holder.imgNotification.setBackgroundResource(R.drawable.noti_events);
            holder.rllayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(new Intent(mContext, EventDetailActivity.class).putExtra("eventId",data.get(position).getId().trim()).putExtra("typeEmailPhone",prefsManager.getEventType()));
                }
            });
        }else if(data.get(position).getType().equalsIgnoreCase("friendRequest")||data.get(position).getType().equalsIgnoreCase("friendRequestAccepted")||data.get(position).getType().equalsIgnoreCase("Friend")){
            holder.imgNotification.setBackgroundResource(R.drawable.ic_menu_my_friends);
            holder.rllayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(new Intent(mContext, PublicProfileScreen.class).putExtra(USER_ID,data.get(position).getId().trim()).putExtra(PROFILE,true));
                }
            });
        }else if(data.get(position).getType().equalsIgnoreCase("Destination")){
            holder.imgNotification.setBackgroundResource(R.drawable.menu_fav_destinations);
            holder.rllayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // mContext.startActivity(new Intent(mContext, PublicProfileScreen.class).putExtra(USER_ID,data.get(position).getId()).putExtra(PROFILE,true));
                    try {
                        String string = data.get(position).getId().trim();
                        Intent mIntent = new Intent(mContext, DestinationsDetailActivity.class);
                        ApplicationGlobal.DestID = string;
                        mContext.startActivity(mIntent);
                    }catch(Exception e){

                    }
                }
            });
        }


        holder.txtNotifHeading.setTypeface(typeHeading);
        holder.txtNotifDetails.setTypeface(typeiconfamily);
        holder.txtNotifDate.setTypeface(typeregular);
        holder.txtNotifDate.setText(data.get(position).getDate());
        holder.txtNotifHeading.setText(data.get(position).getTitle());
        holder.txtNotifDetails.setText(data.get(position).getMessage());

        holder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
        holder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right, holder.swipeLayout.findViewById(R.id.bottom_wrapper));

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

                mItemManger.removeShownLayouts(holder.swipeLayout);
                if (isNetworkConnected()) {
                    removeNotification(data.get(position).getNotificationId(), position);
                }else{
                    Toast.makeText(mContext,"Internet is not connected.",Toast.LENGTH_SHORT).show();;
                }

            }
        });


        mItemManger.bindView(holder.itemView, position);
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    public boolean isNetworkConnected() {
        if (ApplicationGlobal.isNetworkConnected(mContext))
            return true;
        else
            return false;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.txtNotifHeading)
        TextView txtNotifHeading;
        @Bind(R.id.txtNotifDate)
        TextView txtNotifDate;
        @Bind(R.id.txtNotifDetails)
        TextView txtNotifDetails;
        @Bind(R.id.imgNotification)
        ImageView imgNotification;
        @Bind(R.id.swipe)
        SwipeLayout swipeLayout;
        @Bind(R.id.fmremove)
        FrameLayout fmremove;
        @Bind(R.id.rllayout)
        LinearLayout rllayout;

        public ViewHolder(View itemView) {
            super(itemView);
          ButterKnife.bind(this, itemView);
        }
    }

    //============ function to remove notification=============//

    private void removeNotification(String id, final int pos){
        Log.e(" notifid:", " " + id);
        Log.e(" pos:", " " + pos);
        Log.e(" accessToken:", " " + prefsManager.getToken());
        FileUploadService service = ServiceGenerator.createService(FileUploadService.BASE_URL_NEARBY_FRIENDS);

        service.remove_notification(id, prefsManager.getToken(), new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, Response response) {
                Log.e("jsonObject",""+jsonObject);
                Log.e("json==",""+jsonObject.get("success").toString());

                if(jsonObject.get("success").toString().equalsIgnoreCase("1")){
                    data.remove(pos);
                    notifyItemRemoved(pos);
                    notifyItemRangeChanged(pos, data.size());
                    mItemManger.closeAllItems();
                    if (data.size() == 0) {
                        CustomDialog.showProgressDialog(mContext, "Oop's  didn't found what you are looking ! Try some other keyword.");
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

    }


}
