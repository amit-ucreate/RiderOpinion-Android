package com.nutsuser.ridersdomain.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.inscripts.cometchat.sdk.CometChatroom;
import com.inscripts.interfaces.Callbacks;
import com.inscripts.interfaces.SubscribeChatroomCallbacks;
import com.inscripts.utils.Logger;
import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.adapter.CustomGridAdapter;
import com.nutsuser.ridersdomain.adapter.GroupChatListSwipeAdapter;
import com.nutsuser.ridersdomain.adapter.RecentChatListSwipeAdapter;
import com.nutsuser.ridersdomain.database.DatabaseHandler;
import com.nutsuser.ridersdomain.database.Keys;
import com.nutsuser.ridersdomain.database.SharedPreferenceHelper;
import com.nutsuser.ridersdomain.database.Utils;
import com.nutsuser.ridersdomain.view.CustomDialog;
import com.nutsuser.ridersdomain.web.api.FileUploadService;
import com.nutsuser.ridersdomain.web.api.ServiceGenerator;
import com.nutsuser.ridersdomain.web.pojos.ChatroomChatMessage;
import com.nutsuser.ridersdomain.web.pojos.DividerItemDecoration;
import com.nutsuser.ridersdomain.web.pojos.GroupChatListData;
import com.nutsuser.ridersdomain.web.pojos.GroupChatListResponse;
import com.nutsuser.ridersdomain.web.pojos.RecentChatListData;
import com.nutsuser.ridersdomain.web.pojos.RecentChatListReponse;
import com.rollbar.android.Rollbar;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;

/**
 * Created by ucreateuser on 7/7/2016.
 */
public class GroupChatListActivity extends BaseActivity {

    private Activity activity;
    @Bind(R.id.tvTitleToolbar)
    TextView tvTitleToolbar;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    //=========== slider items====================//
    public static String[] prgmNameList = {"My Rides", "My Messages", "My Friends", "Chats", "Favorites", "Notifications", "Settings", "Near By Friends"};
    public static int[] prgmImages = {R.drawable.ic_menu_my_rides, R.drawable.ic_menu_my_messages, R.drawable.menu_my_friends, R.drawable.ic_menu_menu_chats, R.drawable.menu_fav_destinations, R.drawable.ic_menu_menu_notifications, R.drawable.menu_settings, R.drawable.icon_nearby};
    public static Class[] classList = {MyRidesRecyclerView.class, GroupChatListActivity.class, MyFriendsActivity.class, ComingSoon.class, FavouriteDesination.class, NotificationListActivity.class, SettingsActivity.class, NearByFriendsAcitivity.class};

    @Bind(R.id.gridView1)
    GridView gridView1;
    private ActionBarDrawerToggle mDrawerToggle;
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.lvSlidingMenu)
    LinearLayout lvSlidingMenu;
    @Bind(R.id.sdvDp)
    SimpleDraweeView sdvDp;
    @Bind(R.id.btUpdateProfile)
    Button btUpdateProfile;
    @Bind(R.id.btFullProfile)
    Button btFullProfile;
    @Bind(R.id.tvName)
    TextView tvName;

    private ArrayList<GroupChatListData> groupChatListData;
    private GroupChatListSwipeAdapter groupChatAdapter;
    RecyclerView mRecyclerView;
    private CometChatroom cometChatroom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_recent_chat_users_list);
        try {
            activity = this;
            ButterKnife.bind(activity);
            setupActionBar(toolbar);
            setFonts();
            setSliderMenu();
            setDrawerSlider();
            cometChatroom = CometChatroom.getInstance(activity);
            subscribChatRoom();

            groupChatListData = new ArrayList<GroupChatListData>();
            mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
            // Layout Managers:
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

            // Item Decorator:
            mRecyclerView.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.divider)));


        }catch(Exception e){
            Rollbar.reportException(e, "minor", "Recent Chat activity on create");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isNetworkConnected()) {
            getGroupChatList();
        }else{
            showToast("Internet is not connected.");
        }
    }

    private void setFonts() {
        tvTitleToolbar.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/MACHINEN.TTF"));
        tvTitleToolbar.setText("CHATS");
        tvName.setTypeface(typeFaceMACHINEN);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(GroupChatListActivity.this, MainScreenActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("MainScreenResponse", "OPEN");
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //============ function to ser Drawer=================//
    private void setDrawerSlider() {
        mDrawerLayout.closeDrawer(lvSlidingMenu);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.icon_image_view, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ) {
            public void onDrawerClosed(View view) {

            }

            public void onDrawerOpened(View drawerView) {
                showProfileImage();

            }
        };
        mDrawerLayout.closeDrawer(lvSlidingMenu);
        showProfileImage();
    }

    /**
     * '
     * Show Profile Image
     ***/
    private void showProfileImage() {
        try {
            if (prefsManager.getUserName() == null) {
                tvName.setText("No Name");
            } else {
                tvName.setText(prefsManager.getUserName());            }

            if (prefsManager.getImageUrl() == null) {
                // Toast.makeText(MyRidesRecyclerView.this, "Image Address is Null", Toast.LENGTH_SHORT).show();
            } else {
                String imageUrl = prefsManager.getImageUrl();
                sdvDp.setImageURI(Uri.parse(imageUrl));
//                File file = new File(imageUrl);
//                sdvDp.setImageURI(Uri.fromFile(file));
            }
        } catch (NullPointerException e) {
            tvName.setText("No Name");

            if (prefsManager.getImageUrl() == null) {
                // Toast.makeText(MyRidesRecyclerView.this, "Image Address is Null", Toast.LENGTH_SHORT).show();
            } else {
                String imageUrl = prefsManager.getImageUrl();
                sdvDp.setImageURI(Uri.parse(imageUrl));
//                File file = new File(imageUrl);
//                sdvDp.setImageURI(Uri.fromFile(file));
            }
        }
    }

    //============= function to set slider menu============//
    private void setSliderMenu() {
        gridView1.setAdapter(new CustomGridAdapter(this, prgmNameList, prgmImages));
        gridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) {
                    mDrawerLayout.closeDrawer(lvSlidingMenu);
                }  else {
                    Log.e("positiuon:", "" + classList[position]);
                    intentCalling(classList[position]);
                }
            }
        });
    }

    //======== function to move on next activty=========//
    public void intentCalling(Class name) {
        Intent mIntent = new Intent(GroupChatListActivity.this, name);
        startActivity(mIntent);
        //finish();
    }

    public void intent_Calling(Class name, String na) {
        Intent mIntent = new Intent(GroupChatListActivity.this, name);
        mIntent.putExtra(SCREEN_OPEN, na);
        startActivity(mIntent);
        finish();
    }

    //======== OnClick Listeners=============//
    @OnClick({R.id.btFullProfile, R.id.btUpdateProfile, R.id.ivMenu, R.id.rlProfile})
    void click(View view) {
        switch (view.getId()) {

            case R.id.btUpdateProfile:
                startActivity(new Intent(GroupChatListActivity.this, ProfileActivity.class));
                break;
            case R.id.btFullProfile:
                startActivity(new Intent(GroupChatListActivity.this, PublicProfileScreen.class));
                break;
            case R.id.ivMenu:
                if (mDrawerLayout.isDrawerOpen(lvSlidingMenu)) {
                    mDrawerLayout.closeDrawer(lvSlidingMenu);
                }
                else {
                    mDrawerLayout.openDrawer(lvSlidingMenu);
                }
                break;
            case R.id.rlProfile:
                // startActivity(new Intent(MyFriendsActivity.this, ProfileActivity.class));
                break;

        }
    }

    //========== get recent chat list data ================//

    private void getGroupChatList(){
        try {
            showProgressDialog();
            FileUploadService service = ServiceGenerator.createService(FileUploadService.BASE_URL);

            service.getGroupListing(prefsManager.getCaseId(), prefsManager.getToken(), new Callback<JsonObject>() {
                @Override
                public void success(JsonObject jsonObject, retrofit.client.Response response) {
                    Log.e("Upload", "success");
                    Log.e("jsonObject:", "" + jsonObject.toString());

                    Type type = new TypeToken<GroupChatListResponse>() {
                    }.getType();
                    GroupChatListResponse groupChatListReponse = new Gson().fromJson(jsonObject.toString(), type);

                    if (groupChatListReponse.getSuccess() == 1) {
                        dismissProgressDialog();
                        groupChatListData.clear();
                        groupChatListData.addAll(groupChatListReponse.getData());
                        groupChatAdapter = new GroupChatListSwipeAdapter(GroupChatListActivity.this, groupChatListData);
                        //((RecentChatListSwipeAdapter) recentChatAdapter).setMode(Attributes.Mode.Single);
                        mRecyclerView.setAdapter(groupChatAdapter);
                    } else {
                        dismissProgressDialog();
                        CustomDialog.showProgressDialog(GroupChatListActivity.this, groupChatListReponse.getMessage().toString());
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    dismissProgressDialog();
                    //showToast("Error:" + error);
                    Log.e("Upload", "error");
                }
            });
        }catch(Exception e){
            Rollbar.reportException(e, "minor", "Recent Chat activity API");
        }
    }

    //================== chat room subscribe==================//

    private void subscribChatRoom() {
        Log.e("subscribChatRoom"," subscribChatRoom");

        cometChatroom.getAllChatrooms(new Callbacks() {
            @Override
            public void successCallback(JSONObject jsonObject) {
                Log.e("subscribChatRoom"," jsonObject");
            }

            @Override
            public void failCallback(JSONObject jsonObject) {
                Log.e("failCallback", ""+jsonObject);

            }
        });

        cometChatroom.subscribe(true, new SubscribeChatroomCallbacks() {

            @Override
            public void onMessageReceived(JSONObject receivedMessage) {

                //  LogsActivity.addToLog("Chatrooms onMessageReceived");
                Log.d("abc", "On charoom message received = " + receivedMessage);
                try {
                    if (receivedMessage.has("message")) {
                        String mess = receivedMessage.getString("message");
                        String name = receivedMessage.getString("from");
                        String fromId = receivedMessage.getString("fromid");
                        String time = receivedMessage.getString("sent");
                        String messagetype = receivedMessage.getString("message_type");
                        boolean isMymessage = false, videoMessage = false, imageMessage = false;
                        Intent intent = new Intent("Chatroom_message");
                        intent.putExtra("fromid", fromId);
                        intent.putExtra("message_type", messagetype);
                        intent.putExtra("message_id", receivedMessage.getString("id"));
                        intent.putExtra("Message", mess);
                        intent.putExtra("from", name);
                        intent.putExtra("time", time);
                        if (!fromId.equals(prefsManager.getCaseId())) {
                            intent.putExtra("Newmessage", 1);
                            if (messagetype.equals("12")) {
                                intent.putExtra("imageMessage", "1");
                                imageMessage = true;
                            } else if (messagetype.equals("14")) {
                                intent.putExtra("videoMessage", "1");
                                videoMessage = true;
                            }
                        } else if (fromId.equals(prefsManager.getCaseId())) {
                            isMymessage = true;
                            intent.putExtra("Newmessage", 1);

                            if (messagetype.equals("12")) {
                                intent.putExtra("imageMessage", "1");
                                imageMessage = true;
                                intent.putExtra("myphoto", "1");
                            } else if (messagetype.equals("14")) {
                                intent.putExtra("videoMessage", "1");
                                videoMessage = true;
                                intent.putExtra("myvideo", "1");

                            } else if (messagetype.equals("10")
                                    && fromId.equals(prefsManager.getCaseId())) {
								/*
								 * This else if condition added to avoid self duplicate message to be appended in list,
								 *  and it will also show when self message is obtained from last 10 message.
								 *  Please change "Me" to other thing as per the language you are using for CometChat */
                                intent.putExtra("Newmessage", 1);
                                intent.putExtra("selfmessage", true);
                            }
                        }
                        ChatroomChatMessage newmessage = new ChatroomChatMessage(receivedMessage.getString("id"), mess,
                                Utils.convertTimestampToDate(Long.parseLong(time)), name + " :", isMymessage,
                                messagetype, fromId, cometChatroom.getCurrentChatroom());

                        DatabaseHandler helper = new DatabaseHandler(getApplicationContext());
                        helper.insertChatroomMessage(newmessage);
                        getApplicationContext().sendBroadcast(intent);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onLeaveChatroom(JSONObject leaveResponse) {
                // LogsActivity.addToLog("Chatrooms onLeaveChatroom");
            }

            @Override
            public void onError(JSONObject errorResponse) {
                // LogsActivity.addToLog("Chatrooms onError");
            }

            @Override
            public void gotChatroomMembers(JSONObject chatroomMembers) {
                Log.e("chatroomMembers",""+chatroomMembers);
                //   LogsActivity.addToLog("Chatrooms gotChatroomMembers");
            }

            @Override
            public void gotChatroomList(JSONObject chatroomList) {
                Log.e("gotChatroomList",""+chatroomList);
                try {
                    SharedPreferenceHelper.save(Keys.SharedPreferenceKeys.CHATROOMS_LIST, chatroomList.toString());
//                    populateChatroomList();
//                    adapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onActionMessageReceived(JSONObject response) {
                Logger.debug("chatroom actions =" + response);

            }
        });
    }
}
