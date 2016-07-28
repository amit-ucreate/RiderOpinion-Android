package com.nutsuser.ridersdomain.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.activities.EventDetailActivity;
import com.nutsuser.ridersdomain.activities.MapActivity;
import com.nutsuser.ridersdomain.utils.PrefsManager;
import com.nutsuser.ridersdomain.web.api.FileUploadService;
import com.nutsuser.ridersdomain.web.api.ServiceGenerator;
import com.nutsuser.ridersdomain.web.pojos.Datum;
import com.nutsuser.ridersdomain.web.pojos.RemoveMyRideUpcoming;
import com.nutsuser.ridersdomain.web.pojos.Student;

import java.lang.reflect.Type;
import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;

/**
 * Created by user on 1/4/2016.
 */
public class UpcomingSwipeViewAdapter extends RecyclerSwipeAdapter<UpcomingSwipeViewAdapter.SimpleViewHolder> {


    private Context mContext;
    private ArrayList<Student> studentList;
    ArrayList<Datum> datum;
    String AccessToken, joinEventId;
    PrefsManager prefsManager;

    public UpcomingSwipeViewAdapter(Context context, ArrayList<Student> objects, ArrayList<Datum> datum) {
        this.mContext = context;
        this.studentList = objects;
        this.datum = datum;
        prefsManager = new PrefsManager(context);
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.upcome_swipeview, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder viewHolder, final int position) {
        if (position % 2 == 0){
            viewHolder.rllayout.setBackgroundColor(Color.WHITE);
        }

        else{
            viewHolder.rllayout.setBackgroundColor(Color.parseColor("#fefaf1"));
        }
        final Student item = studentList.get(position);

        if (datum.get(position).getHalts().size() == 0||datum.get(position).getHalts()==null) {
            viewHolder.tvTitle.setText(datum.get(position).getDaysCount()+"-Day Ride From "+datum.get(position).getBaseLocation() + " to " + datum.get(position).getDestLocation());
        }
        else{

            viewHolder.tvTitle.setText(datum.get(position).getDaysCount()+"-Day Ride From "+datum.get(position).getBaseLocation() + " to " + datum.get(position).getDestLocation()+" (Stopover at "+datum.get(position).getHalts().get(0).getHaltLocation()+")");
        }
       // viewHolder.tvTitle.setText(datum.get(position).getBaseLocation() + " To " + datum.get(position).getDestLocation());
        viewHolder.tvdate.setText(datum.get(position).getStartDate());
        viewHolder.tvtime.setText(datum.get(position).getStartTime());
        viewHolder.tvDesplace.setText(datum.get(position).getDestLocation());
        viewHolder.tvNumberOfRiders.setText(datum.get(position).getRiders() + " Riders");
//        if (datum.get(position).getMutual() == 0) {
//            viewHolder.tvLabelHaveJoined.setText("Not Joined");
//        } else {
//            viewHolder.tvLabelHaveJoined.setText("Have Joined");
//        }

        viewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);

        // Drag From Left
        //  viewHolder.swipeLayout.addDrag(SwipeLayout.DragEdge.Left, viewHolder.swipeLayout.findViewById(R.id.bottom_wrapper1));

        // Drag From Right
        viewHolder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right, viewHolder.swipeLayout.findViewById(R.id.bottom_wrapper));


        // Handling different events when swiping
        viewHolder.swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
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


        viewHolder.lllinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(mContext, EventDetailActivity.class);
                mIntent.putExtra("eventId", datum.get(position).getEventId());
                mContext.startActivity(mIntent);
                //Toast.makeText(mContext, " onClick : " + item.getName() + " \n" + item.getEmailId(), Toast.LENGTH_SHORT).show();
            }
        });

        viewHolder.btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Toast.makeText(v.getContext(), "Clicked on Map " + viewHolder.tvName.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });


        viewHolder.frameremove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinEventId = datum.get(position).getJoinEventId();
                mItemManger.removeShownLayouts(viewHolder.swipeLayout);
                RemoveUpcoming(joinEventId, position);

            }
        });


        viewHolder.fmchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //  Toast.makeText(view.getContext(), "Clicked on Edit  " + viewHolder.tvName.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });


       viewHolder.ivMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String lat = datum.get(position).getBaseLat();
                String lon = datum.get(position).getBaseLong();
                String deslat= datum.get(position).getdestLat();
                String deslon= datum.get(position).getdestLong();
                Intent mIntent = new Intent(mContext, MapActivity.class);
                mIntent.putExtra("endLat", lat);
                mIntent.putExtra("endLon", lon);
                mIntent.putExtra("deslon", deslon);
                mIntent.putExtra("deslat", deslat);
                mContext.startActivity(mIntent);
            }
        });


        // mItemManger is member in RecyclerSwipeAdapter Class
        mItemManger.bindView(viewHolder.itemView, position);

    }

    @Override
    public int getItemCount() {
        return datum.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }
    //  ViewHolder Class
    public static class SimpleViewHolder extends RecyclerView.ViewHolder {
        SwipeLayout swipeLayout;
        FrameLayout frameremove;
        FrameLayout fmchat;
        TextView tvRemove;
        TextView tvEdit, tvTitle, tvdate, tvtime, tvDesplace, tvNumberOfRiders, tvLabelHaveJoined;
        ImageButton btnLocation;
        ImageView ivimage;
        RelativeLayout rllayout;
        LinearLayout lllinear;
        ImageView ivMap;

        public SimpleViewHolder(View itemView) {
            super(itemView);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            //tvName = (TextView) itemView.findViewById(R.id.tvName);
            // tvEmailId = (TextView) itemView.findViewById(R.id.tvEmailId);
            //  tvDelete = (TextView) itemView.findViewById(R.id.tvDelete);
            tvRemove = (TextView) itemView.findViewById(R.id.tvRemove);
            frameremove = (FrameLayout) itemView.findViewById(R.id.frameremove);
            btnLocation = (ImageButton) itemView.findViewById(R.id.btnLocation);
            ivimage = (ImageView) itemView.findViewById(R.id.ivimage);
            fmchat=(FrameLayout)itemView.findViewById(R.id.fmchat);
          //  fmRoutereview=(FrameLayout)itemView.findViewById(R.id.fmRoutereview);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvdate = (TextView) itemView.findViewById(R.id.tvdate);
            tvtime = (TextView) itemView.findViewById(R.id.tvtime);
            tvDesplace = (TextView) itemView.findViewById(R.id.tvDesplace);
            tvNumberOfRiders = (TextView) itemView.findViewById(R.id.tvNumberOfRiders);
            tvLabelHaveJoined = (TextView) itemView.findViewById(R.id.tvLabelHaveJoined);
            rllayout=(RelativeLayout)itemView.findViewById(R.id.rllayout);
            lllinear=(LinearLayout)itemView.findViewById(R.id.lllinear);
            ivMap=(ImageView)itemView.findViewById(R.id.ivMap);


        }
    }

    /**
     * Match RemoveUpcoming .
     */
    public void RemoveUpcoming(final String joinid, final int pos) {
        // showProgressDialog();
        Log.e(" RemoveUpcoming", " RemoveUpcoming");
        try {

            AccessToken = prefsManager.getToken();
            FileUploadService service = ServiceGenerator.createService(FileUploadService.BASE_URL);
            service.remove_riding(joinid, AccessToken, new Callback<JsonObject>() {
                @Override
                public void success(JsonObject jsonObject, retrofit.client.Response response) {
                    Log.e("Upload", "success");
                    Log.e("jsonObject:", "" + jsonObject.toString());

                    Type type = new TypeToken<RemoveMyRideUpcoming>() {
                    }.getType();
                    RemoveMyRideUpcoming removeMyRideUpcoming = new Gson().fromJson(jsonObject.toString(), type);
                    if (removeMyRideUpcoming.getSuccess().equals("1")) {
                        //dismissProgressDialog();
                        datum.remove(pos);
                        notifyItemRemoved(pos);
                        notifyItemRangeChanged(pos, datum.size());
                        mItemManger.closeAllItems();
                    } else if (removeMyRideUpcoming.getMessage().equals("Data Not Found.")) {
                        // dismissProgressDialog();
                        // showToast("Data Not Found.");
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    //dismissProgressDialog();
                    //showToast("Error:" + error);
                    Log.e("Upload", "error");
                }
            });


        } catch (Exception e) {
            e.printStackTrace();

        }
    }

}

