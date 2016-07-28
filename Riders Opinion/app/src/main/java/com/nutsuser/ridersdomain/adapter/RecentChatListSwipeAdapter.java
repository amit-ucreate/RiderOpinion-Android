package com.nutsuser.ridersdomain.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
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
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.JsonObject;
import com.inscripts.cometchat.sdk.CometChat;
import com.inscripts.custom.EmojiTextView;
import com.inscripts.interfaces.Callbacks;
import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.activities.MyFriendsActivity;
import com.nutsuser.ridersdomain.activities.PublicProfileScreen;
import com.nutsuser.ridersdomain.chat.SampleSingleChatActivity;
import com.nutsuser.ridersdomain.database.DatabaseHandler;
import com.nutsuser.ridersdomain.database.Utils;
import com.nutsuser.ridersdomain.utils.ApplicationGlobal;
import com.nutsuser.ridersdomain.utils.CONSTANTS;
import com.nutsuser.ridersdomain.utils.CustomizeDialog;
import com.nutsuser.ridersdomain.utils.PrefsManager;
import com.nutsuser.ridersdomain.view.CircleButtonText;
import com.nutsuser.ridersdomain.web.api.FileUploadService;
import com.nutsuser.ridersdomain.web.api.ServiceGenerator;
import com.nutsuser.ridersdomain.web.pojos.FriendList;
import com.nutsuser.ridersdomain.web.pojos.RecentChatListData;
import com.nutsuser.ridersdomain.web.pojos.SingleChatMessage;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by user on 1/8/2016.
 */
public class RecentChatListSwipeAdapter extends RecyclerSwipeAdapter<RecentChatListSwipeAdapter.SimpleViewHolder> implements CONSTANTS{


    private Activity mContext;
    private List<RecentChatListData> studentList;
    private Typeface typeHeading, typeiconfamily, typeregular;
    PrefsManager prefsManager;
    public CustomizeDialog mCustomizeDialog;
    private CometChat cometChat;
    private String msg_id;
    private String friendId;
    private DatabaseHandler dbhelper;


    public RecentChatListSwipeAdapter(Activity context, List<RecentChatListData> objects) {
        this.mContext = context;
        this.studentList = objects;
        prefsManager = new PrefsManager(context);
        typeHeading = Typeface.createFromAsset(mContext.getAssets(), "fonts/Lato-Heavy.ttf");
        typeiconfamily = Typeface.createFromAsset(mContext.getAssets(), "fonts/Lato-Bold.ttf");
        typeregular = Typeface.createFromAsset(mContext.getAssets(), "fonts/LATO-REGULAR.TTF");
        cometChat= CometChat.getInstance(context,API_KEY);
        dbhelper = new DatabaseHandler(context);
       // sortByDate();
         sortByTime();

    }
    public void notifyData(ArrayList<RecentChatListData> myList) {
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
        final RecentChatListData item = studentList.get(position);
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

        viewHolder.txtNickName.setText(item.getProfileName());

        if(item.getMessage().contains("src=\"")){
          String msg =  item.getMessage().replaceAll("src=\\\"","src=\"").replaceAll(".png\\\"",".png\"");
            viewHolder.txtMessage.setText(Html.fromHtml(msg));
            Log.e("msg",msg);
        }else{
            viewHolder.txtMessage.setEmojiText(item.getMessage());
        }

        //viewHolder.txtMessage.setEmojiText(":blush:");
        viewHolder.txtUsername.setText(item.getUsername());

        if(prefsManager.getHistoryCount().isEmpty()) {
            Log.e("histortcount","count");
            if (item.getFrom().equals(prefsManager.getCaseId())) {

                getChatHistory(Long.parseLong(item.getTo()), Long.parseLong(item.getId()), item.getUsername());
            } else {
                getChatHistory(Long.parseLong(item.getFrom()), Long.parseLong(item.getId()), item.getUsername());
            }
        }


        String[] sentDate = item.getSent().split(" ");
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
        }
        if (item.getMsgcount().equals("0")) {
            viewHolder.tvchatCount.setVisibility(View.GONE);
        } else {
            viewHolder.tvchatCount.setText(item.getMsgcount());
        }


//        if(item.getFrom().equals(prefsManager.getCaseId())){
//            friendId= item.getTo();
//            viewHolder.sdvPostImage.setImageURI(Uri.parse("https://s3.amazonaws.com/rideropinion/users/"+item.getTo() +"/profileImage/"+item.getProfileImage()));
//            Log.e("friend Image url","https://s3.amazonaws.com/rideropinion/users/"+item.getTo() +"/profileImage/"+item.getProfileImage());
//        }else{
//            friendId= item.getFrom();
//            Log.e("friend Image url","https://s3.amazonaws.com/rideropinion/users/"+item.getFrom() +"/profileImage/"+item.getProfileImage());
        viewHolder.sdvPostImage.setImageURI(Uri.parse(item.getProfileImage()));
        //  }


        viewHolder.fmFrienflistItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(item.getFrom().equals(prefsManager.getCaseId())){
                    friendId = item.getTo();
                }else{
                    friendId = item.getFrom();
                }
                mItemManger.closeAllItems();
                String channel = "";
                String username="";
                String id="";
                try {
                    Iterator<String> keys = MyFriendsActivity.jsonObjectUsers.keys();

                    while (keys.hasNext()) {
                        JSONObject user = MyFriendsActivity.jsonObjectUsers.getJSONObject(keys.next().toString());
                        id=user.getString("id");
                        if(id.equalsIgnoreCase(friendId)) {
                            if (user.has("ch")) {
                                channel = user.getString("ch");                            }

                            Log.e("friend_id",friendId+" = "+id+" name "+ user.getString("n")+" channel "+channel);
                            Intent intent = new Intent(mContext, SampleSingleChatActivity.class);
                            intent.putExtra("user_id", Long.parseLong(id));
                            intent.putExtra("user_name", user.getString("n"));
                            intent.putExtra("channel", channel);
                            intent.putExtra("userImage",item.getProfileImage());
                            intent.putExtra("name",item.getUsername());
                            intent.putExtra("msg",item.getId());
                            mContext.startActivity(intent);
                        }
                    }
                }catch(Exception e){

                    Log.e("exception",": "+e) ;

                }


            }
        });

//        viewHolder.rllayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//               // mContext.startActivity(new Intent(mContext,PublicProfileScreen.class));
//            }
//        });
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


    private void getChatHistory(final long friendId, long msgId, final String username){

        cometChat.getChatHistory(friendId, msgId, new Callbacks() {
            @Override
            public void successCallback(JSONObject jsonObject) {
//                Log.e("friendId ",""+friendId +" username : "+username);
                Log.e("chat Hostory",""+jsonObject);
                try {
                    JSONArray history= jsonObject.getJSONArray("history");
                    // Log.e("history Array",""+history);
                    for(int i=0;i<history.length();i++) {
                        JSONObject messageObject = history.getJSONObject(i);
                        //  Log.e("messageObject",""+messageObject);
                        prefsManager.setHistoryCount("1");
                        if (messageObject.getString("self").equals("1")) {
                            String time = Utils.convertTimestampToDate(Utils.correctTimestamp(Long.parseLong(messageObject
                                    .getString("sent"))));

                            SingleChatMessage newMessage = new SingleChatMessage(messageObject.getString("id"),
                                    messageObject.getString("message").trim(), time, true,
                                    messageObject.getString("from"), messageObject.getString("to"), messageObject.getString("message_type"), 0);
                            dbhelper.insertOneOnOneMessage(newMessage);
                        }else{
                            String time = Utils.convertTimestampToDate(Utils.correctTimestamp(Long.parseLong(messageObject
                                    .getString("sent"))));
                            SingleChatMessage newMessage = new SingleChatMessage(messageObject.getString("id"),
                                    messageObject.getString("message").trim(), time, false,
                                    messageObject.getString("from"), messageObject.getString("to"), messageObject.getString("message_type"), 0);
                            dbhelper.insertOneOnOneMessage(newMessage);
                        }
                    }

                }catch(Exception e){

                }
            }

            @Override
            public void failCallback(JSONObject jsonObject) {

            }
        });
    }

    public void sortByDate() {

        Comparator<RecentChatListData> comparator = new Comparator<RecentChatListData>() {

            @Override
            public int compare(RecentChatListData object1, RecentChatListData object2) {
                String [] date2 =object2.getSent().split(" ");
                String[] date1 = object1.getSent().split(" ");
                return date2[0].compareToIgnoreCase(date1[0]);
            }
        };
        Collections.sort(studentList, comparator);
        notifyDataSetChanged();
    }

    private long getTime(String time) {
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateFormat2 = null;
        long newTime=0;
        try {

            dateFormat2 = dateTimeFormat.parse(time);
            newTime= dateFormat2.getTime();
            Log.e("dateFormat2.getTime()", "" + dateFormat2.getTime());
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return newTime;
    }

    public void sortByTime() {

        Comparator<RecentChatListData> comparator = new Comparator<RecentChatListData>() {

            @Override
            public int compare(RecentChatListData object1, RecentChatListData object2) {
                return object2.getTime().compareToIgnoreCase(object1.getTime());
            }
        };
        Collections.sort(studentList, comparator);
        notifyDataSetChanged();
    }



//private long getTime(String sentTime){
//    DateFormat format = new SimpleDateFormat("dd-MMM-yyyy hh:mm", Locale.ENGLISH);
//    long futureTime = 0;
//    try {
//        Date date = format.parse(sentTime);
//        futureTime = date.getTime();
//    } catch (ParseException e) {
//        Log.e("log", e.getMessage(), e);
//    }
//
//    long curTime = System.currentTimeMillis();
//    long diff = futureTime - curTime;
//
//    return diff;
//}

}

