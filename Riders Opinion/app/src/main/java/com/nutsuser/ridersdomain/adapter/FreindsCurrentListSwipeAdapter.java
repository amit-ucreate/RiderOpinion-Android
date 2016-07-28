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
import com.google.gson.JsonObject;
import com.inscripts.cometchat.sdk.CometChat;
import com.inscripts.interfaces.Callbacks;
import com.inscripts.interfaces.SubscribeCallbacks;
import com.inscripts.utils.Logger;
import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.activities.MyFriendsActivity;
import com.nutsuser.ridersdomain.activities.PublicProfileScreen;
import com.nutsuser.ridersdomain.chat.SampleSingleChatActivity;
import com.nutsuser.ridersdomain.database.Keys;
import com.nutsuser.ridersdomain.database.PushNotificationsManager;
import com.nutsuser.ridersdomain.database.SharedPreferenceHelper;
import com.nutsuser.ridersdomain.database.Utils;
import com.nutsuser.ridersdomain.utils.ApplicationGlobal;
import com.nutsuser.ridersdomain.utils.CONSTANTS;
import com.nutsuser.ridersdomain.utils.CustomizeDialog;
import com.nutsuser.ridersdomain.utils.PrefsManager;
import com.nutsuser.ridersdomain.view.CircleButtonText;
import com.nutsuser.ridersdomain.web.api.FileUploadService;
import com.nutsuser.ridersdomain.web.api.ServiceGenerator;
import com.nutsuser.ridersdomain.web.pojos.FriendList;
import com.nutsuser.ridersdomain.web.pojos.SingleChatMessage;
import com.nutsuser.ridersdomain.web.pojos.SingleUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by user on 1/8/2016.
 */
public class FreindsCurrentListSwipeAdapter extends RecyclerSwipeAdapter<FreindsCurrentListSwipeAdapter.SimpleViewHolder> implements CONSTANTS{


    private Activity mContext;
    private List<FriendList> studentList;
    private Typeface typeHeading, typeiconfamily, typeregular;
    PrefsManager prefsManager;
    public CustomizeDialog mCustomizeDialog;
    CircleButtonText btnCurrentFriendCount;
    private CometChat cometChat;
    private String msg_id;

    public FreindsCurrentListSwipeAdapter(Activity context, List<FriendList> objects, CircleButtonText btnCurrentCount) {
        this.mContext = context;
        this.studentList = objects;
        prefsManager = new PrefsManager(context);
        typeHeading = Typeface.createFromAsset(mContext.getAssets(), "fonts/Lato-Heavy.ttf");
        typeiconfamily = Typeface.createFromAsset(mContext.getAssets(), "fonts/Lato-Bold.ttf");
        typeregular = Typeface.createFromAsset(mContext.getAssets(), "fonts/LATO-REGULAR.TTF");
        btnCurrentFriendCount=btnCurrentCount;
        cometChat= CometChat.getInstance(context,API_KEY);

    }
    public void notifyData(ArrayList<FriendList> myList) {
        Log.d("notifyData ", myList.size() + "");
        this.studentList = myList;
        notifyDataSetChanged();
    }
    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.swipe_row_requests, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder viewHolder, final int position) {
        final FriendList item = studentList.get(position);

        //viewHolder.tvName.setText((item.getName()) + "  -  Row Position " + position);
        // viewHolder.tvEmailId.setText(item.getEmailId());

        if (position % 2 == 0) {
            viewHolder.rllayout.setBackgroundColor(Color.WHITE);
        } else {
            viewHolder.rllayout.setBackgroundColor(Color.parseColor("#fefaf1"));
        }
        viewHolder.tvName.setTypeface(typeHeading);
        viewHolder.tvAddress.setTypeface(typeregular);
        viewHolder.tvDesc.setTypeface(typeregular);


        Log.e("VehicleType() Request",""+item.getVehicleType());
        Log.e("getProfileName() ","==Request"+item.getProfileName());
        viewHolder.tvName.setText(item.getUsername());
        viewHolder.tvAddress.setText(item.getVehicleType());
        viewHolder.tvDesc.setText(item.getProfileName());
        viewHolder.sdvPostImage.setImageURI(Uri.parse(item.getUserImage()));

        viewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);

        // Drag From Left
        // viewHolder.swipeLayout.addDrag(SwipeLayout.DragEdge.Left, viewHolder.swipeLayout.findViewById(R.id.bottom_wrapper1));

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
               // Toast.makeText(mContext, " onClick : " + item.getName() + " \n" + item.getEmailId(), Toast.LENGTH_SHORT).show();
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

        viewHolder.fmUnfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemManger.removeShownLayouts(viewHolder.swipeLayout);
                if (isNetworkConnected()) {
                    unFriendAPI(item.getFriendId(),position);
                } else {
                    showToast("Internet is not connected.");
                }
                //  Toast.makeText(view.getContext(), "Clicked on Edit  " + viewHolder.tvName.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        viewHolder.fmMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("click",": click") ;
                String channel = "";
                String username="";
                String id="";
                try {
                    Iterator<String> keys = MyFriendsActivity.jsonObjectUsers.keys();

                    while (keys.hasNext()) {
                        JSONObject user = MyFriendsActivity.jsonObjectUsers.getJSONObject(keys.next().toString());
                        id=user.getString("id");
                        if(id.equalsIgnoreCase(item.getFriendId().trim())) {
                            if (user.has("ch")) {
                                channel = user.getString("ch");                            }

                            Log.e("friend_id",item.getFriendId()+" = "+id+" name "+ user.getString("n")+" channel "+channel);
                            Intent intent = new Intent(mContext, SampleSingleChatActivity.class);
                            intent.putExtra("user_id", Long.parseLong(id));
                            intent.putExtra("user_name", user.getString("n"));
                            intent.putExtra("channel", channel);
                            intent.putExtra("userImage",item.getUserImage());
                            intent.putExtra("name",item.getUsername());
                            mContext.startActivity(intent);
                        }
                    }
                }catch(Exception e){

                    Log.e("exception",": "+e) ;

                }
                mItemManger.closeAllItems();
            }
        });


        viewHolder.fmFrienflistItem.setOnClickListener(new View.OnClickListener() {
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

        viewHolder.rllayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // mContext.startActivity(new Intent(mContext,PublicProfileScreen.class));
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
        @Bind(R.id.fmUnfriend)
        FrameLayout fmUnfriend;
        @Bind(R.id.fmMessage)
        FrameLayout fmMessage;
        @Bind(R.id.fmFrienflistItem)
        FrameLayout fmFrienflistItem;
        @Bind(R.id.rllayout)
        RelativeLayout rllayout;
        @Bind(R.id.sdvPostImage)
        SimpleDraweeView sdvPostImage;

        public SimpleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    //=========== function to remove friend======================//
    private void unFriendAPI(String friend_id, final int pos){

        Log.e("user_id",""+prefsManager.getCaseId()+"  =="+prefsManager.getToken());
        Log.e("friend_id",""+friend_id);
        showProgressDialog();

        FileUploadService service = ServiceGenerator.createService(FileUploadService.BASE_URL_NEARBY_FRIENDS);

        service.handleFriends(prefsManager.getCaseId(), friend_id, prefsManager.getToken(), "3", new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, Response response) {
                Log.e("jsonObject",""+jsonObject);
                showToast("Friend deleted Successfully.");
                studentList.remove(pos);
                btnCurrentFriendCount.setText(""+studentList.size());
                notifyDataSetChanged();
                notifyItemRemoved(pos);
                notifyItemRangeChanged(pos, studentList.size());
                mItemManger.closeAllItems();
                dismissProgressDialog();
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

