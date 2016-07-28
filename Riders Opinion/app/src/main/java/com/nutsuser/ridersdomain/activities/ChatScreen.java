package com.nutsuser.ridersdomain.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.inscripts.cometchat.sdk.CometChat;
import com.inscripts.interfaces.SubscribeCallbacks;
import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.adapter.BuddylistAdapter;
import com.nutsuser.ridersdomain.adapter.CustomGridAdapter;
import com.nutsuser.ridersdomain.view.CircleButtonText;
import com.nutsuser.ridersdomain.web.pojos.DividerItemDecoration;
import com.nutsuser.ridersdomain.web.pojos.SingleUser;
import com.nutsuser.ridersdomain.web.pojos.Student;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by user on 12/10/2015.
 */
public class ChatScreen extends BaseActivity {
    //=========== slider items====================//
    public static String[] prgmNameList = {"My Rides", "My Messages", "My Friends", "Chats", "Favourite Destination", "Notifications", "Settings", "Near By Friends"};
    public static int[] prgmImages = {R.drawable.ic_menu_my_rides, R.drawable.ic_menu_my_messages, R.drawable.menu_my_friends, R.drawable.ic_menu_menu_chats, R.drawable.menu_fav_destinations, R.drawable.ic_menu_menu_notifications, R.drawable.menu_settings, R.drawable.icon_nearby};
    public static Class[] classList = {MyRidesRecyclerView.class, RecentChatListActivity.class,MyFriendsActivity.class, ChatScreen.class, FavouriteDesination.class, NotificationListActivity.class, SettingsActivity.class, NearByFriendsAcitivity.class};

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
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tvTitleToolbar)
    TextView tvTitleToolbar;
    //=====================================//

    private Activity activty;
    private TextView tvEmptyView;
    private RecyclerView mRecyclerView;
    @Bind(R.id.bt_current)
    TextView bt_chats;
    @Bind(R.id.bt_request)
    TextView bt_message;
    @Bind(R.id.fmCurrent)
    FrameLayout fmChats;
    @Bind(R.id.fmRequest)
    FrameLayout fmMessage;
    CircleButtonText tvMessageCount, tvChatCount;
    private ArrayList<Student> data;
    private CometChat cometChat;
    private static ArrayList<String> list;
    static String response;
    /* For mapping userId and name */
    private static ArrayList<SingleUser> usersList = new ArrayList<SingleUser>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        tvEmptyView = (TextView) findViewById(R.id.empty_view);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        tvMessageCount =(CircleButtonText)findViewById(R.id.tvPastCount);
        tvChatCount =(CircleButtonText)findViewById(R.id.tvUpcomingCount);
        activty=this;
        ButterKnife.bind(this);
        setupActionBar(toolbar);
        setFonts();
        setSliderMenu();
        setDrawerSlider();
        setRecyclerView();
        cometChat= CometChat.getInstance(ChatScreen.this,API_KEY);
        list = new ArrayList<String>();
        //CometChat.setDevelopmentMode(false);

    }
    //========= set Online list data======================//
    @Override
    protected void onResume() {
        super.onResume();
        if (list.size() <= 0) {
            //getOnlineUserList();
        }
//        if(usersList.size()==0){
//            showToast("No User is online.");
//        }else {
//        adapter = new BuddylistAdapter(this, usersList);
//        usersListView.setAdapter(adapter);
        // }
        //getUsers();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (list.size() <= 0) {
           // getOnlineUserList();
            getUsers();
        }
    }
    private void getUsers(){
        final SubscribeCallbacks subCallbacks= new SubscribeCallbacks() {
            @Override
            public void gotOnlineList(JSONObject jsonObject) {
                Log.e("UserList",""+jsonObject);
             //   populateList(jsonObject.toString());
            }

            @Override
            public void onError(JSONObject jsonObject) {
                Log.e("onError",""+jsonObject);
            }

            @Override
            public void onMessageReceived(JSONObject jsonObject) {

            }

            @Override
            public void gotProfileInfo(JSONObject jsonObject) {
                Log.e("gotProfileInfo",""+jsonObject);
            }

            @Override
            public void gotAnnouncement(JSONObject jsonObject) {

            }

            @Override
            public void onActionMessageReceived(JSONObject jsonObject) {

            }
        };
    }

//    public  void populateList(String onlineUserList) {
//        try {
//            if (null != list && null != usersList && null != adapter) {
//                JSONObject onlineUsers;
//
//                if (!onlineUserList.isEmpty()) {
//                    onlineUsers = new JSONObject(onlineUserList);
//                } else {
//                    onlineUsers = new JSONObject();
//                }
//
//                Iterator<String> keys = onlineUsers.keys();
//                list.clear();
//                usersList.clear();
//                while (keys.hasNext()) {
//                    JSONObject user = onlineUsers.getJSONObject(keys.next().toString());
//                    String username = user.getString("n");
//                    list.add(username);
//                    String channel = "";
//                    if (user.has("ch")) {
//                        channel = user.getString("ch");
//                    }
//
//                    //Log.e("usersList",""+usersList);
//                    usersList.add(new SingleUser(username, user.getInt("id"), user.getString("m"), user.getString("s"),
//                            channel));
//                }
//
//                adapter.notifyDataSetChanged();
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        Log.e("usersList",""+usersList.size());
//    }
    //================ function to set recyclerview============//
    private void setRecyclerView(){
        // Layout Managers:
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Item Decorator:
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.divider)));
        data= new ArrayList<Student>();
        if (data.isEmpty()) {
            mRecyclerView.setVisibility(View.GONE);
            tvEmptyView.setVisibility(View.VISIBLE);

        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            tvEmptyView.setVisibility(View.GONE);
        }

          /* Scroll Listeners */
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.e("RecyclerView", "onScrollStateChanged");
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

    }
    //============ FUNCTION TO SET FONTS==============//
    private void setFonts() {
        //tvTitleToolbar.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/MACHINEN.TTF"));
        bt_message.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/Lato-Heavy.ttf"));
        bt_chats.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/Lato-Heavy.ttf"));
        tvTitleToolbar.setText("CHATS");
        bt_message.setText("Messages");
        bt_chats.setText("Chats");
    }

    //============ function to ser Drawer=================//
    private void setDrawerSlider(){
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
        if (prefsManager.getUserName() == null) {
            tvName.setText("No Name");
        } else {
            tvName.setText(prefsManager.getUserName());
        }
        if (prefsManager.getImageUrl() == null) {
            // Toast.makeText(MyRidesRecyclerView.this, "Image Address is Null", Toast.LENGTH_SHORT).show();
        } else {
            String imageUrl = prefsManager.getImageUrl();
            sdvDp.setImageURI(Uri.parse(imageUrl));

//            File file = new File(imageUrl);
//            sdvDp.setImageURI(Uri.fromFile(file));
        }
    }

    //============= function to set slider menu============//
    private void setSliderMenu(){
        gridView1.setAdapter(new CustomGridAdapter(this, prgmNameList, prgmImages));
        gridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 2) {
                    mDrawerLayout.closeDrawer(lvSlidingMenu);
                }else
//                if (position == 1) {
//                    Log.e("positiuon:", "" + classList[position]);
//                    intent_Calling(classList[position], "My Messages");
//                }  else {
                    Log.e("positiuon:", "" + classList[position]);
                    intentCalling(classList[position]);
               // }
            }
        });
    }

    //======== function to move on next activty=========//
    public void intentCalling(Class name) {
        Intent mIntent = new Intent(ChatScreen.this, name);
        startActivity(mIntent);
        finish();
    }

    public void intent_Calling(Class name, String na) {
        Intent mIntent = new Intent(ChatScreen.this, name);
        mIntent.putExtra(SCREEN_OPEN, na);
        startActivity(mIntent);
        finish();
    }
    // load initial data
    public void loadData() {

        for (int i = 0; i <= 20; i++) {
            data.add(new Student("Student " + i, "androidstudent" + i + "@gmail.com"));

        }
    }

    //=========================================//
    @TargetApi(16)
    @OnClick({R.id.bt_request, R.id.bt_current, R.id.btFullProfile, R.id.btUpdateProfile, R.id.ivMenu, R.id.rlProfile})
    void click(View view) {
        switch (view.getId()) {
            case R.id.bt_request:
                tvMessageCount.setText("0");
                fmMessage.setBackground(getResources().getDrawable(R.drawable.button_corner_right));
                fmChats.setBackground(getResources().getDrawable(R.drawable.button_corner_left));
                bt_chats.setBackgroundColor(Color.parseColor("#ffffff"));
                bt_message.setBackgroundColor(Color.parseColor("#C6B9B3"));
                bt_message.setTextColor(Color.parseColor("#ffffff"));
                bt_chats.setTextColor(Color.parseColor("#CD411E"));
//                mDataSet.clear();
//                getFriendsList("0");

                break;
            case R.id.bt_current:
                tvChatCount.setText("0");
                fmMessage.setBackground(getResources().getDrawable(R.drawable.button_corner_right_white));
                fmChats.setBackground(getResources().getDrawable(R.drawable.button_corner_left_gray));
                bt_chats.setBackgroundColor(Color.parseColor("#C6B9B3"));
                bt_message.setBackgroundColor(Color.parseColor("#ffffff"));
                bt_message.setTextColor(Color.parseColor("#CD411E"));
                bt_chats.setTextColor(Color.parseColor("#ffffff"));
                // Creating Adapter object
//                mDataSet.clear();
//                getFriendsList("1");
                break;
            case R.id.btUpdateProfile:
                startActivity(new Intent(ChatScreen.this, ProfileActivity.class));
                break;
            case R.id.btFullProfile:
                startActivity(new Intent(ChatScreen.this, PublicProfileScreen.class));
                break;
            case R.id.ivMenu:
                if (mDrawerLayout.isDrawerOpen(lvSlidingMenu))
                    mDrawerLayout.closeDrawer(lvSlidingMenu);
                else
                    mDrawerLayout.openDrawer(lvSlidingMenu);
                break;
            case R.id.rlProfile:
                // startActivity(new Intent(MyFriendsActivity.this, ProfileActivity.class));
                break;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // finish();
                Intent intent = new Intent(ChatScreen.this, MainScreenActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("MainScreenResponse", "OPEN");
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    }
