package com.nutsuser.ridersdomain.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
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
import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.activities.EventDetailActivity;
import com.nutsuser.ridersdomain.activities.MapActivity;
import com.nutsuser.ridersdomain.activities.Subcribe;
import com.nutsuser.ridersdomain.web.pojos.Datum;
import com.nutsuser.ridersdomain.web.pojos.Student;

import java.util.ArrayList;

import butterknife.Bind;

public class SwipeRecyclerViewAdapter extends RecyclerSwipeAdapter<SwipeRecyclerViewAdapter.SimpleViewHolder> {


    private Context mContext;
    private ArrayList<Student> studentList;
    ArrayList<Datum> datum;

    public SwipeRecyclerViewAdapter(Context context, ArrayList<Student> objects, ArrayList<Datum> datum) {
        this.mContext = context;
        this.studentList = objects;
        this.datum = datum;
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.swipe_row_item, parent, false);
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
        //Log.e("Location:", "" + datum.get(position).getBaseLocation());
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

//        if(datum.get(position).getMutual()==0){
//            viewHolder.tvLabelHaveJoined.setText("Not Joined");
//        }
//        else{
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


      /*  viewHolder.ivMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // mItemManger.removeShownLayouts(viewHolder.swipeLayout);
                //mItemManger.closeAllItems();
                String lat = datum.get(position).getBaseLat();
                String lon = datum.get(position).getBaseLong();
                String deslon = datum.get(position).getdestLong();
                String deslat= datum.get(position).getdestLat();
                Intent mIntent = new Intent(mContext, MapActivity.class);
                 mIntent.putExtra("endLat", lat);
                 mIntent.putExtra("endLon", lon);
                mIntent.putExtra("deslon", deslon);
                mIntent.putExtra("deslat", deslat);
                mContext.startActivity(mIntent);

                // Toast.makeText(view.getContext(), "Clicked on Share " + viewHolder.tvName.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });*/

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
                String deslon = datum.get(position).getdestLong();
                String deslat= datum.get(position).getdestLat();
                Intent mIntent = new Intent(mContext, MapActivity.class);
                mIntent.putExtra("endLat", lat);
                mIntent.putExtra("endLon", lon);
                mIntent.putExtra("deslon", deslon);
                mIntent.putExtra("deslat", deslat);
                mIntent.putExtra("destName",datum.get(position).getDestLocation());
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
        //  TextView tvName;
        // TextView tvEmailId;
        // TextView tvDelete;
        FrameLayout fmchat;
        TextView tvEdit, tvTitle, tvdate, tvtime, tvDesplace, tvNumberOfRiders,tvLabelHaveJoined;
        TextView tvShare;
        ImageButton btnLocation;
        ImageView ivimage;
        LinearLayout lllinear;
        ImageView ivMap;

        RelativeLayout rllayout;

        public SimpleViewHolder(View itemView) {
            super(itemView);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            fmchat=(FrameLayout)itemView.findViewById(R.id.fmchat);
            //fmRoutereview=(FrameLayout)itemView.findViewById(R.id.fmRoutereview);
            tvEdit = (TextView) itemView.findViewById(R.id.tvEdit);
            tvShare = (TextView) itemView.findViewById(R.id.tvShare);
            btnLocation = (ImageButton) itemView.findViewById(R.id.btnLocation);
            ivimage = (ImageView) itemView.findViewById(R.id.ivimage);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvdate = (TextView) itemView.findViewById(R.id.tvdate);
            tvtime = (TextView) itemView.findViewById(R.id.tvtime);
            tvDesplace = (TextView) itemView.findViewById(R.id.tvDesplace);
            tvNumberOfRiders = (TextView) itemView.findViewById(R.id.tvNumberOfRiders);
            tvLabelHaveJoined=(TextView)itemView.findViewById(R.id.tvLabelHaveJoined);
            rllayout=(RelativeLayout)itemView.findViewById(R.id.rllayout);
            lllinear=(LinearLayout)itemView.findViewById(R.id.lllinear);
            ivMap=(ImageView)itemView.findViewById(R.id.ivMap);


        }
    }
}
