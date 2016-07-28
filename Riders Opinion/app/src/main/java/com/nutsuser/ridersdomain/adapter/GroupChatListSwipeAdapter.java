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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.facebook.drawee.view.SimpleDraweeView;
import com.inscripts.cometchat.sdk.CometChat;
import com.inscripts.custom.EmojiTextView;
import com.inscripts.interfaces.Callbacks;
import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.activities.MyFriendsActivity;
import com.nutsuser.ridersdomain.chat.ChatroomChatActivity;
import com.nutsuser.ridersdomain.chat.SampleSingleChatActivity;
import com.nutsuser.ridersdomain.database.DatabaseHandler;
import com.nutsuser.ridersdomain.database.Utils;
import com.nutsuser.ridersdomain.utils.ApplicationGlobal;
import com.nutsuser.ridersdomain.utils.CONSTANTS;
import com.nutsuser.ridersdomain.utils.CustomizeDialog;
import com.nutsuser.ridersdomain.utils.PrefsManager;
import com.nutsuser.ridersdomain.view.CircleButtonText;
import com.nutsuser.ridersdomain.web.pojos.GroupChatListData;
import com.nutsuser.ridersdomain.web.pojos.RecentChatListData;
import com.nutsuser.ridersdomain.web.pojos.SingleChatMessage;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by user on 1/8/2016.
 */
public class GroupChatListSwipeAdapter extends RecyclerSwipeAdapter<GroupChatListSwipeAdapter.SimpleViewHolder> implements CONSTANTS{


    private Activity mContext;
    private List<GroupChatListData> studentList;
    private Typeface typeHeading, typeiconfamily, typeregular;
    PrefsManager prefsManager;
    public CustomizeDialog mCustomizeDialog;

    private String msg_id;
    private String friendId;
    private DatabaseHandler dbhelper;


    public GroupChatListSwipeAdapter(Activity context, List<GroupChatListData> objects) {
        this.mContext = context;
        this.studentList = objects;
        prefsManager = new PrefsManager(context);
        typeHeading = Typeface.createFromAsset(mContext.getAssets(), "fonts/Lato-Heavy.ttf");
        typeiconfamily = Typeface.createFromAsset(mContext.getAssets(), "fonts/Lato-Bold.ttf");
        typeregular = Typeface.createFromAsset(mContext.getAssets(), "fonts/LATO-REGULAR.TTF");

        dbhelper = new DatabaseHandler(context);
        //sortByDate();
        // sortByTime();

    }
    public void notifyData(ArrayList<GroupChatListData> myList) {
        Log.d("notifyData ", myList.size() + "");
        this.studentList = myList;
        notifyDataSetChanged();
    }
    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recent_chat_list_adapter, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder viewHolder, final int position) {
        final GroupChatListData item = studentList.get(position);
        if (position % 2 == 0) {
            viewHolder.rllayout.setBackgroundColor(Color.WHITE);
            // viewHolder.bottom_wrapper1.setBackgroundColor(Color.WHITE);
        } else {
            viewHolder.rllayout.setBackgroundColor(Color.parseColor("#fefaf1"));
            //  viewHolder.bottom_wrapper1.setBackgroundColor(Color.parseColor("#fefaf1"));
        }
        viewHolder.txtNickName.setTypeface(typeHeading);
        //viewHolder.txtMessage.setTypeface(typeregular);
        viewHolder.txtUsername.setTypeface(typeregular);
        viewHolder.txtTime.setTypeface(typeregular);

        viewHolder.txtNickName.setText(item.getDaysCount()+"-Day Ride From "+item.getTitle());
        viewHolder.txtMessage.setEmojiText(item.getMessage());
        //viewHolder.txtMessage.setEmojiText(":blush:");
        viewHolder.txtUsername.setText(item.getSenderName());

        viewHolder.tvchatCount.setVisibility(View.GONE);

//        if(prefsManager.getHistoryCount().isEmpty()) {
//            Log.e("histortcount","count");
//            if (item.getFrom().equals(prefsManager.getCaseId())) {
//
//            //    getChatHistory(Long.parseLong(item.getTo()), Long.parseLong(item.getId()), item.getUsername());
//            } else {
//              //  getChatHistory(Long.parseLong(item.getFrom()), Long.parseLong(item.getId()), item.getUsername());
//            }
//        }


       /* String[] sentDate = item.getSent().split(" ");
        Date date = null;
        try {
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            date = (Date) formatter.parse(sentDate[0]);
        } catch (Exception e) {

        }
        SimpleDateFormat newFormat = new SimpleDateFormat("dd/MM/yyyy");
        String finalString = newFormat.format(date);

        Log.e("finalString", "" + finalString + " = " + getCurrentMoth());
        if (finalString.matches(getCurrentMoth())) {
            viewHolder.txtTime.setText("Today");
        } else {
            // SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            viewHolder.txtTime.setText(finalString);
        }*/
//        if (item.getMsgcount().equals("0")) {
//            viewHolder.tvchatCount.setVisibility(View.GONE);
//        } else {
//            viewHolder.tvchatCount.setText(item.getMsgcount());
//        }

        if(item.getImage()==null){

        }else {
            viewHolder.sdvPostImage.setImageURI(Uri.parse(item.getImage()));
        }

        viewHolder.fmFrienflistItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ChatroomChatActivity.class);
                intent.putExtra(
                        "cName", item.getName());
                intent.putExtra("chatroomid",
                        item.getId());
                mContext.startActivity(intent);

//                if(item.getFrom().equals(prefsManager.getCaseId())){
//                    friendId = item.getTo();
//                }else{
//                    friendId = item.getFrom();
//                }
//                mItemManger.closeAllItems();
//                String channel = "";
//                String username="";
//                String id="";
//                try {
//                    Iterator<String> keys = MyFriendsActivity.jsonObjectUsers.keys();
//
//                    while (keys.hasNext()) {
//                        JSONObject user = MyFriendsActivity.jsonObjectUsers.getJSONObject(keys.next().toString());
//                        id=user.getString("id");
//                        if(id.equalsIgnoreCase(friendId)) {
//                            if (user.has("ch")) {
//                                channel = user.getString("ch");                            }
//
//                            Log.e("friend_id",friendId+" = "+id+" name "+ user.getString("n")+" channel "+channel);
//                            Intent intent = new Intent(mContext, SampleSingleChatActivity.class);
//                            intent.putExtra("user_id", Long.parseLong(id));
//                            intent.putExtra("user_name", user.getString("n"));
//                            intent.putExtra("channel", channel);
//                            intent.putExtra("userImage",item.getProfileImage());
//                            intent.putExtra("name",item.getUsername());
//                            intent.putExtra("msg",item.getId());
//                            mContext.startActivity(intent);
//                        }
//                    }
//                }catch(Exception e){
//
//                    Log.e("exception",": "+e) ;
//
//                }


            }
        });


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
        @Bind(R.id.txtNickName)
        TextView txtNickName;
        @Bind(R.id.txtUsername)
        TextView txtUsername;
        @Bind(R.id.txtMessage)
        EmojiTextView txtMessage;
        @Bind(R.id.txtTime)
        TextView txtTime;
        @Bind(R.id.tvchatCount)
        CircleButtonText tvchatCount;
        @Bind(R.id.fmFrienflistItem)
        FrameLayout fmFrienflistItem;
        @Bind(R.id.rllayout)
        RelativeLayout rllayout;
        @Bind(R.id.sdvPostImage)
        SimpleDraweeView sdvPostImage;
//        @Bind((R.id.bottom_wrapper1))
//        LinearLayout bottom_wrapper1;

        public SimpleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
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

    private String getCurrentMoth(){
        Date date = Calendar.getInstance().getTime();
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String today = formatter.format(date);
        return today;
    }



//    public void sortByDate() {
//
//        Comparator<GroupChatListData> comparator = new Comparator<RecentChatListData>() {
//
//            @Override
//            public int compare(RecentChatListData object1, RecentChatListData object2) {
//                String [] date2 =object2.getSent().split(" ");
//                String[] date1 = object1.getSent().split(" ");
//                return date2[0].compareToIgnoreCase(date1[0]);
//            }
//        };
//        Collections.sort(studentList, comparator);
//        notifyDataSetChanged();
//    }
//
//    public void sortByTime() {
//
//        Comparator<GroupChatListData> comparator = new Comparator<RecentChatListData>() {
//
//            @Override
//            public int compare(RecentChatListData object1, RecentChatListData object2) {
//                String [] date2 =object2.getSent().split(" ");
//                String[] date1 = object1.getSent().split(" ");
//                return date1[1].compareToIgnoreCase(date2[1]);
//            }
//        };
//        Collections.sort(studentList, comparator);
//        notifyDataSetChanged();
//    }



}

