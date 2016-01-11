package com.nutsuser.ridersdomain.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.web.pojos.Student;

import java.util.ArrayList;

/**
 * Created by user on 1/8/2016.
 */
public class FavouriteDestinationAdapter extends RecyclerSwipeAdapter<FavouriteDestinationAdapter.SimpleViewHolder> {


    private Context mContext;
    private ArrayList<Student> studentList;

    public FavouriteDestinationAdapter(Context context, ArrayList<Student> objects) {
        this.mContext = context;
        this.studentList = objects;
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favourite_swipe_row, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder viewHolder, final int position) {
        final Student item = studentList.get(position);

        //viewHolder.tvName.setText((item.getName()) + "  -  Row Position " + position);
        // viewHolder.tvEmailId.setText(item.getEmailId());


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

        /*viewHolder.swipeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if ((((SwipeLayout) v).getOpenStatus() == SwipeLayout.Status.Close)) {
                    //Start your activity

                    Toast.makeText(mContext, " onClick : " + item.getName() + " \n" + item.getEmailId(), Toast.LENGTH_SHORT).show();
                }

            }
        });*/

        viewHolder.swipeLayout.getSurfaceView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, " onClick : " + item.getName() + " \n" + item.getEmailId(), Toast.LENGTH_SHORT).show();
            }
        });


        viewHolder.btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Toast.makeText(v.getContext(), "Clicked on Map " + viewHolder.tvName.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });


        viewHolder.tvShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Toast.makeText(view.getContext(), "Clicked on Share " + viewHolder.tvName.getText().toString(), Toast.LENGTH_SHORT).show();
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


        // mItemManger is member in RecyclerSwipeAdapter Class
        mItemManger.bindView(viewHolder.itemView, position);

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
        //  TextView tvName;
        // TextView tvEmailId;
        // TextView tvDelete;
      //  TextView tvEdit;
        TextView tvShare;
        ImageButton btnLocation;
        ImageView ivimage;

        public SimpleViewHolder(View itemView) {
            super(itemView);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            //tvName = (TextView) itemView.findViewById(R.id.tvName);
            // tvEmailId = (TextView) itemView.findViewById(R.id.tvEmailId);
            //  tvDelete = (TextView) itemView.findViewById(R.id.tvDelete);
           // tvEdit = (TextView) itemView.findViewById(R.id.tvEdit);
            tvShare = (TextView) itemView.findViewById(R.id.tvShare);
            btnLocation = (ImageButton) itemView.findViewById(R.id.btnLocation);
            ivimage=(ImageView) itemView.findViewById(R.id.ivimage);


        }
    }
}

