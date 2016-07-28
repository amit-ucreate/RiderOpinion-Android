package com.nutsuser.ridersdomain.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Handler;
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
import com.facebook.common.util.UriUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.JsonObject;
import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.activities.MyFriendsActivity;
import com.nutsuser.ridersdomain.activities.PublicProfileScreen;
import com.nutsuser.ridersdomain.activities.RegisterActivity;
import com.nutsuser.ridersdomain.utils.ApplicationGlobal;
import com.nutsuser.ridersdomain.utils.CONSTANTS;
import com.nutsuser.ridersdomain.utils.CustomizeDialog;
import com.nutsuser.ridersdomain.utils.PrefsManager;
import com.nutsuser.ridersdomain.view.CircleButtonText;
import com.nutsuser.ridersdomain.web.api.FileUploadService;
import com.nutsuser.ridersdomain.web.api.ServiceGenerator;
import com.nutsuser.ridersdomain.web.pojos.DataMyFriendsList;
import com.nutsuser.ridersdomain.web.pojos.FriendCombineData;
import com.nutsuser.ridersdomain.web.pojos.FriendList;
import com.nutsuser.ridersdomain.web.pojos.FriendRequest;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by user on 1/8/2016.
 */
public class FreindsRequestListSwipeAdapter extends RecyclerSwipeAdapter<FreindsRequestListSwipeAdapter.SimpleViewHolder> implements CONSTANTS{


    private Context mContext;
    private List<FriendRequest> studentList;
    private ArrayList<FriendList> friendList;
    private Typeface typeHeading, typeiconfamily, typeregular;
    PrefsManager prefsManager;
    public CustomizeDialog mCustomizeDialog;
    CircleButtonText btnReqCount;
    FreindsCurrentListSwipeAdapter currentFriendList;
    MyFriendsActivity myFriendsActivity;


    public FreindsRequestListSwipeAdapter(Context context, FriendCombineData objects, CircleButtonText btnRequestCount, FreindsCurrentListSwipeAdapter friendList) {
        this.mContext = context;
        this.studentList = objects.getFriendRequest();
        this. friendList=(ArrayList<FriendList>) objects.getFriendList();
        prefsManager = new PrefsManager(context);
        typeHeading = Typeface.createFromAsset(mContext.getAssets(), "fonts/Lato-Heavy.ttf");
        typeiconfamily = Typeface.createFromAsset(mContext.getAssets(), "fonts/Lato-Bold.ttf");
        typeregular = Typeface.createFromAsset(mContext.getAssets(), "fonts/LATO-REGULAR.TTF");
        btnReqCount=btnRequestCount;
        currentFriendList = friendList;
        myFriendsActivity=new MyFriendsActivity();
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
//        View view=null;
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.swipe_row_current, parent, false);
//            }
//        }, 100);


        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder viewHolder, final int position) {
        final FriendRequest item = studentList.get(position);

        if (position % 2 == 0) {
            viewHolder.rllayout.setBackgroundColor(Color.WHITE);
        } else {
            viewHolder.rllayout.setBackgroundColor(Color.parseColor("#fefaf1"));
        }

        viewHolder.tvName.setTypeface(typeHeading);
        viewHolder.tvAddress.setTypeface(typeregular);
        viewHolder.tvDesc.setTypeface(typeregular);

        Log.e("VehicleType() current",""+item.getVehicleType());
        Log.e("getProfileName() ","==current"+item.getProfileName());
        viewHolder.tvName.setText(item.getUsername());
        viewHolder.tvDesc.setText(""+item.getProfileName());
        viewHolder.tvAddress.setText(""+item.getVehicleType());
        if(item.getUserImage()==null){
            Uri uri = new Uri.Builder()
                    .scheme(UriUtil.LOCAL_RESOURCE_SCHEME) // "res"
                    .path(String.valueOf(R.drawable.logo_small))
                    .build();
            viewHolder.sdvPostImage.setImageURI(uri);
        }else {

            viewHolder.sdvPostImage.setImageURI(Uri.parse(item.getUserImage()));
        }
       // viewHolder.sdvPostImage.setImageURI(Uri.parse(item.getImages()));

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
              //  Toast.makeText(mContext, " onClick : " + item.getName() + " \n" + item.getEmailId(), Toast.LENGTH_SHORT).show();
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

        viewHolder.tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //  Toast.makeText(view.getContext(), "Clicked on Edit  " + viewHolder.tvName.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        viewHolder.fmDeclineRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mItemManger.removeShownLayouts(viewHolder.swipeLayout);
                if (isNetworkConnected()) {
                    handleFriendRequestAPI(item.getFriendId(),position,"2", studentList);
                } else {
                    showToast("Internet is not connected.");
                }

                //  Toast.makeText(view.getContext(), "Clicked on Edit  " + viewHolder.tvName.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
        viewHolder.fmAcceptRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemManger.removeShownLayouts(viewHolder.swipeLayout);
                if (isNetworkConnected()) {
                    handleFriendRequestAPI(item.getFriendId(),position,"1",studentList);
//                    //currentFriendList.addView(studentList.get(position));
                   // friendList.add(new FriendList(studentList.get(position).getFriendId(),studentList.get(position).getUserId(),studentList.get(position).getUserImage(),studentList.get(position).getUsername(),studentList.get(position).getProfileName(),studentList.get(position).getVehicleType()));

                    //currentFriendList = new FreindsCurrentListSwipeAdapter(mContext, friendList, MyFriendsActivity.tvCurrentCount);
                   // MyFriendsActivity.mRecyclerViewFriends.setAdapter(currentFriendList);
                   // currentFriendList.notifyData(friendList);

                   // currentFriendList.notifyDataSetChanged();;
                } else {
                    showToast("Internet is not connected.");
                }

                //  Toast.makeText(view.getContext(), "Clicked on Edit  " + viewHolder.tvName.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });


        viewHolder.fmRequestlistItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemManger.closeAllItems();
                Intent intent = new Intent(mContext, PublicProfileScreen.class);
                intent.putExtra(PROFILE, true);
                intent.putExtra(PROFILE_IMAGE, item.getUserImage());
                intent.putExtra(USER_ID, item.getFriendId());
                mContext.startActivity(intent);
            }
        });


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
        @Bind(R.id.swipe)
        SwipeLayout swipeLayout;
        @Bind(R.id.txtName)
        TextView tvName;
        @Bind(R.id.txtDesc)
        TextView tvDesc;
        @Bind(R.id.txtAddress)
        TextView tvAddress;
        @Bind(R.id.tvEdit)
        TextView tvEdit;
        @Bind(R.id.tvShare)
        TextView tvShare;
        @Bind(R.id.btnLocation)
        ImageButton btnLocation;
        @Bind(R.id.ivimage)
        ImageView ivimage;
        @Bind(R.id.sdvPostImage)
        SimpleDraweeView sdvPostImage;
        @Bind(R.id.rllayout)
        RelativeLayout rllayout;
        @Bind(R.id.fmAccept)
        FrameLayout fmAcceptRequest;
        @Bind(R.id.fmDecline)
        FrameLayout fmDeclineRequest;
        @Bind(R.id.fmRequestlistItem)
        FrameLayout fmRequestlistItem;



        public SimpleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
//            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
//            tvName = (TextView) itemView.findViewById(R.id.txtName);
//            tvDesc = (TextView) itemView.findViewById(R.id.txtDesc);
//            tvAddress = (TextView) itemView.findViewById(R.id.txtAddress);
//            tvEdit = (TextView) itemView.findViewById(R.id.tvEdit);
//            tvShare = (TextView) itemView.findViewById(R.id.tvShare);
//            btnLocation = (ImageButton) itemView.findViewById(R.id.btnLocation);



        }
    }

    //=========== function to handle friend request======================//
    private void handleFriendRequestAPI(String friendId, final int pos,final String requestType,final List<FriendRequest> list){

        Log.e("user_id",""+prefsManager.getCaseId()+"  =="+prefsManager.getToken());
        Log.e("friend_id",""+friendId);
        showProgressDialog();

        FileUploadService service = ServiceGenerator.createService(FileUploadService.BASE_URL_NEARBY_FRIENDS);
        Log.e("url",""+FileUploadService.BASE_URL_NEARBY_FRIENDS+friendId+prefsManager.getCaseId()+prefsManager.getToken()+requestType);
        service.handleFriends(prefsManager.getCaseId(), friendId,prefsManager.getToken(), requestType, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, Response response) {
                Log.e("jsonObject",""+jsonObject);
               showToast(jsonObject.get("message").toString());
//                if(requestType.equalsIgnoreCase("1")){
//
//                }if(requestType.equalsIgnoreCase("2")) {
                    studentList.remove(pos);
                btnReqCount.setText(""+list.size());
               // }
                notifyDataSetChanged();
                notifyItemRemoved(pos);
                notifyItemRangeChanged(pos, studentList.size());
                mItemManger.closeAllItems();
                //currentFriendList.notifyDataSetChanged();;
                dismissProgressDialog();
                //myFriendsActivity.getFriendcombineList();
                MyFriendsActivity.getFriendcombineListAgain();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("failure",""+error.getMessage());
                dismissProgressDialog();
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
    protected void showToast(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }
    //========= function to check internet connectivity======//
    public boolean isNetworkConnected() {
        if (ApplicationGlobal.isNetworkConnected(mContext))
            return true;
        else
            return false;
    }

}


