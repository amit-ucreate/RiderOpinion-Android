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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.swipe.util.Attributes;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.inscripts.cometchat.sdk.CometChat;
import com.inscripts.cometchat.sdk.MessageSDK;
import com.inscripts.interfaces.Callbacks;
import com.inscripts.interfaces.SubscribeCallbacks;
import com.inscripts.utils.Logger;
import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.adapter.CustomGridAdapter;
import com.nutsuser.ridersdomain.adapter.FreindsCurrentListSwipeAdapter;
import com.nutsuser.ridersdomain.adapter.FreindsRequestListSwipeAdapter;
import com.nutsuser.ridersdomain.chat.SampleCometChatActivity;
import com.nutsuser.ridersdomain.database.DatabaseHandler;
import com.nutsuser.ridersdomain.database.Keys;
import com.nutsuser.ridersdomain.database.PushNotificationsManager;
import com.nutsuser.ridersdomain.database.SharedPreferenceHelper;
import com.nutsuser.ridersdomain.database.Utils;
import com.nutsuser.ridersdomain.view.CircleButtonText;
import com.nutsuser.ridersdomain.view.CustomDialog;
import com.nutsuser.ridersdomain.web.api.FileUploadService;
import com.nutsuser.ridersdomain.web.api.ServiceGenerator;
import com.nutsuser.ridersdomain.web.pojos.DataMyFriendsList;
import com.nutsuser.ridersdomain.web.pojos.DividerItemDecoration;
import com.nutsuser.ridersdomain.web.pojos.FriendCombineData;
import com.nutsuser.ridersdomain.web.pojos.FriendsCombineListResponse;
import com.nutsuser.ridersdomain.web.pojos.MyFriendsList;
import com.nutsuser.ridersdomain.web.pojos.SingleChatMessage;
import com.nutsuser.ridersdomain.web.pojos.Student;
import com.rollbar.android.Rollbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by user on 1/6/2016.
 */
public class MyFriendsActivity extends BaseActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.bt_current)
    TextView bt_current;
    @Bind(R.id.bt_request)
    TextView bt_request;
    @Bind(R.id.fmCurrent)
    FrameLayout fmCurrent;
    @Bind(R.id.fmRequest)
    FrameLayout fmRequest;
    @Bind(R.id.tvTitleToolbar)
    TextView tvTitleToolbar;
//    @Bind(R.id.LvRequest)
   static LinearLayout LvRequest;
    static LinearLayout LvFriends;
//    @Bind(R.id.LvFriends)
//    @Bind(R.id.empty_friends)
  static TextView empty_friends;

    public static CircleButtonText tvRequestCount, tvCurrentCount;
    // FreindsCurrentListSwipeAdapter mUpcomingSwipeViewAdapter;
    public static FreindsCurrentListSwipeAdapter mCurrentListSwipeViewAdapter;
    public static FreindsRequestListSwipeAdapter mRequestListSwipeViewAdapter;
    public static FriendCombineData friendCombineData;

    private Activity activity;
    //public static TextView tvEmptyViewRequest,tvemptyViewFriends;
    public static RecyclerView mRecyclerViewRequest,mRecyclerViewFriends;
//    private ArrayList<DataMyFriendsList> mDataSet = new ArrayList<>();
      private ArrayList<Student> data;
    //=========== slider items====================//
    public static String[] prgmNameList = {"My Rides", "My Messages", "My Friends", "Chats", "Favorites", "Notifications", "Settings", "Riders Near By"};
    public static int[] prgmImages = {R.drawable.ic_menu_my_rides, R.drawable.ic_menu_my_messages, R.drawable.menu_my_friends, R.drawable.ic_menu_menu_chats, R.drawable.menu_fav_destinations, R.drawable.ic_menu_menu_notifications, R.drawable.menu_settings, R.drawable.icon_nearby};
    public static Class[] classList = {MyRidesRecyclerView.class, RecentChatListActivity.class,MyFriendsActivity.class, ComingSoon.class, FavouriteDesination.class, NotificationListActivity.class, SettingsActivity.class, NearByFriendsAcitivity.class};

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

    int selectedTab = 0;
//    private CometChat cometchat;
//    public static String msg_id;
//    private DatabaseHandler dbhelper;
//    public  static JSONObject jsonObjectUsers = new JSONObject();
//=====================================//


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        try {
            activity = this;
            ButterKnife.bind(this);
            setupActionBar(toolbar);
            //cometChatInilizers();


//        tvEmptyViewRequest = (TextView) findViewById(R.id.empty_view);
//        tvemptyViewFriends = (TextView) findViewById(R.id.empty_view_friends);
            mRecyclerViewRequest = (RecyclerView) findViewById(R.id.my_recycler_view);
            mRecyclerViewFriends = (RecyclerView) findViewById(R.id.my_recycler_view_friends);
            tvRequestCount = (CircleButtonText) findViewById(R.id.tvPastCount);
            tvCurrentCount = (CircleButtonText) findViewById(R.id.tvUpcomingCount);
            LvRequest = (LinearLayout) findViewById(R.id.LvRequest);
            LvFriends = (LinearLayout) findViewById(R.id.LvFriends);
            empty_friends=(TextView)findViewById(R.id.empty_view_friends);
//        bt_chats = (TextView) findViewById(R.id.bt_chats);
//        bt_message = (TextView) findViewById(R.id.bt_message);

            // Layout Managers:
            mRecyclerViewRequest.setLayoutManager(new LinearLayoutManager(this));

            // Item Decorator:
            mRecyclerViewRequest.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.divider)));
            // mRecyclerView.setItemAnimator(new FadeInLeftAnimator());

            // Layout Managers:
            mRecyclerViewFriends.setLayoutManager(new LinearLayoutManager(this));

            // Item Decorator:
            mRecyclerViewFriends.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.divider)));
            // mRecyclerView.setItemAnimator(new FadeInLeftAnimator());


            data = new ArrayList<Student>();

            loadData();

            if (data.isEmpty()) {
                mRecyclerViewRequest.setVisibility(View.GONE);
                //tvEmptyViewRequest.setVisibility(View.VISIBLE);
                mRecyclerViewFriends.setVisibility(View.GONE);
                //tvemptyViewFriends.setVisibility(View.VISIBLE);

            } else {
                mRecyclerViewRequest.setVisibility(View.VISIBLE);
                //tvEmptyViewRequest.setVisibility(View.GONE);
                mRecyclerViewFriends.setVisibility(View.VISIBLE);
                // tvemptyViewFriends.setVisibility(View.GONE);
            }
            setFonts();



        /* Scroll Listeners */
            mRecyclerViewRequest.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    Log.e("RecyclerViewReuest", "onScrollStateChanged");
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                }
            });

            mRecyclerViewFriends.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    Log.e("RecyclerViewFriends", "onScrollStateChanged");
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                }
            });
            // getFriendsList("0");
            getFriendcombineListAgain();
            setSliderMenu();
            setDrawerSlider();
        }catch(Exception e){
            Rollbar.reportException(e, "minor", "MyFriendActivity on Create");
        }

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

    @Override
    protected void onResume() {
        super.onResume();
    if(mDrawerLayout.isDrawerOpen(lvSlidingMenu)){
    showProfileImage();
    mDrawerLayout.closeDrawer(lvSlidingMenu);
    }
    }

    //======== function to move on next activty=========//
    public void intentCalling(Class name) {
        Intent mIntent = new Intent(MyFriendsActivity.this, name);
        startActivity(mIntent);
        //finish();
    }

    public void intent_Calling(Class name, String na) {
        Intent mIntent = new Intent(MyFriendsActivity.this, name);
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
    private void setFonts() {
        tvTitleToolbar.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/MACHINEN.TTF"));
        bt_request.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/Lato-Heavy.ttf"));
        bt_current.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/Lato-Heavy.ttf"));

    }
    //=========================================//
    @TargetApi(16)
    @OnClick({R.id.bt_request, R.id.bt_current, R.id.btFullProfile, R.id.btUpdateProfile, R.id.ivMenu, R.id.rlProfile})
    void click(View view) {
        switch (view.getId()) {
            case R.id.btUpdateProfile:
                startActivity(new Intent(MyFriendsActivity.this, ProfileActivity.class));
                finish();
                break;
            case R.id.btFullProfile:
                startActivity(new Intent(MyFriendsActivity.this, PublicProfileScreen.class));
                finish();
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

                Intent intent = new Intent(MyFriendsActivity.this, MainScreenActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("MainScreenResponse", "OPEN");
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

//
//    //========= function to get friendsLIst===============//
//
//    private void getFriendsList(final String requestType){
//            showProgressDialog();
//        Log.e("userId",""+prefsManager.getCaseId());
//        Log.e("requestType",""+requestType);
//        Log.e("acessToken",""+prefsManager.getToken());
//        FileUploadService service = ServiceGenerator.createService(FileUploadService.BASE_URL_NEARBY_FRIENDS);
//        service.get_friendsList(prefsManager.getCaseId(), requestType, prefsManager.getToken(), new Callback<JsonObject>() {
//            @Override
//            public void success(JsonObject jsonObject, Response response) {
//
//                Type type = new TypeToken<MyFriendsList>() {
//                }.getType();
//                Log.e("jsonObject:", "" + jsonObject.toString());
//                dismissProgressDialog();
//                MyFriendsList myFriendsListResponse = new Gson().fromJson(jsonObject.toString(), type);
//
//                if(myFriendsListResponse.getSuccess()==1){
//                    mDataSet.clear();
//                    mDataSet.addAll(myFriendsListResponse.getData());
//                    if(requestType.equalsIgnoreCase("all")){
//                        tvCurrentCount.setText(""+mDataSet.size());
//
//                        Log.e("mDataSet:", "" + mDataSet.size());
//                        mCurrentListSwipeViewAdapter = new FreindsCurrentListSwipeAdapter(MyFriendsActivity.this,mDataSet,tvCurrentCount);
//
//                        // Setting Mode to Single to reveal bottom View for one item in List
//                        // Setting Mode to Mutliple to reveal bottom Views for multile items in List
//                        ((FreindsCurrentListSwipeAdapter) mCurrentListSwipeViewAdapter).setMode(Attributes.Mode.Single);
//
//                        mRecyclerView.setAdapter(mCurrentListSwipeViewAdapter);
//                    }else{
//                        tvRequestCount.setText(""+mDataSet.size());
//                        mRequestListSwipeViewAdapter = new FreindsRequestListSwipeAdapter(MyFriendsActivity.this, mDataSet,tvRequestCount);
//
//
//                        // Setting Mode to Single to reveal bottom View for one item in List
//                        // Setting Mode to Mutliple to reveal bottom Views for multile items in List
//                        ((FreindsRequestListSwipeAdapter) mRequestListSwipeViewAdapter).setMode(Attributes.Mode.Single);
//
//                        mRecyclerView.setAdapter(mRequestListSwipeViewAdapter);
//                    }
//                }else{
//                    if(requestType.equalsIgnoreCase("all")){
//                        mCurrentListSwipeViewAdapter = new FreindsCurrentListSwipeAdapter(MyFriendsActivity.this,mDataSet,tvCurrentCount);
//                        tvCurrentCount.setText(""+mDataSet.size());
//                        ((FreindsCurrentListSwipeAdapter) mCurrentListSwipeViewAdapter).setMode(Attributes.Mode.Single);
//
//                         mRecyclerView.setAdapter(mCurrentListSwipeViewAdapter);
//                        if(mDataSet.size()==0){
//                            tvCurrentCount.setText("0");
//                            CustomDialog.showProgressDialog(MyFriendsActivity.this, myFriendsListResponse.getMessage().toString());
//                        }
//
//                    }else{
//                        mRequestListSwipeViewAdapter = new FreindsRequestListSwipeAdapter(MyFriendsActivity.this, mDataSet,tvRequestCount);
//                        tvRequestCount.setText(""+mDataSet.size());
//                        ((FreindsRequestListSwipeAdapter) mRequestListSwipeViewAdapter).setMode(Attributes.Mode.Single);
//
//                        mRecyclerView.setAdapter(mRequestListSwipeViewAdapter);
//                        if(mDataSet.size()==0){
//                            tvRequestCount.setText("0");
//                            CustomDialog.showProgressDialog(MyFriendsActivity.this, myFriendsListResponse.getMessage().toString());
//                        }
//                    }
//                }
//
//
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//                dismissProgressDialog();
//            }
//        });
//    }


//    //========== get friendCombineList===================//
//
//    public void getFriendcombineList(){
//        showProgressDialog();
//        FileUploadService service = ServiceGenerator.createService(FileUploadService.BASE_URL_NEARBY_FRIENDS);
//
//        service.get_friendsCombineList(prefsManager.getCaseId(), prefsManager.getToken(), new Callback<JsonObject>() {
//            @Override
//            public void success(JsonObject jsonObject, Response response) {
//                Type type = new TypeToken<FriendsCombineListResponse>() {
//                }.getType();
//                Log.e("jsonObject:", "" + jsonObject.toString());
//                dismissProgressDialog();
//                FriendsCombineListResponse myFriendsListResponse = new Gson().fromJson(jsonObject.toString(), type);
//
//                if(myFriendsListResponse.getSuccess()==1) {
//                    friendCombineData = myFriendsListResponse.getData();
//                    if(friendCombineData.getFriendRequest().size()==0){
//                        mRecyclerViewRequest.setVisibility(View.GONE);
//                        tvEmptyViewRequest.setVisibility(View.VISIBLE);
//                        LvRequest.setVisibility(View.GONE);
//
//                    }
//                    else{
//                        LvRequest.setVisibility(View.VISIBLE);
//                    }
//                    if(friendCombineData.getFriendList().size()==0){
//                        mRecyclerViewFriends.setVisibility(View.GONE);
//                        tvemptyViewFriends.setVisibility(View.VISIBLE);
//                        LvFriends.setVisibility(View.GONE);
//                    }else{
//                        LvFriends.setVisibility(View.VISIBLE);
//                    }
//
//                    tvCurrentCount.setText("" + friendCombineData.getFriendList().size());
//                    tvRequestCount.setText("" + friendCombineData.getFriendRequest().size());
////                    if (selectedTab == 1) {
//                        mCurrentListSwipeViewAdapter = new FreindsCurrentListSwipeAdapter(MyFriendsActivity.this, friendCombineData.getFriendList(), tvCurrentCount);
//
//                        // Setting Mode to Single to reveal bottom View for one item in List
//                        // Setting Mode to Mutliple to reveal bottom Views for multile items in List
//                        ((FreindsCurrentListSwipeAdapter) mCurrentListSwipeViewAdapter).setMode(Attributes.Mode.Single);
//
//                        mRecyclerViewFriends.setAdapter(mCurrentListSwipeViewAdapter);
//
////                    } else {
//
//                       // tvRequestCount.setText("" + friendCombineData.getFriendRequest().size());
//                        mRequestListSwipeViewAdapter = new FreindsRequestListSwipeAdapter(MyFriendsActivity.this, friendCombineData, tvRequestCount,mCurrentListSwipeViewAdapter);
//
//
//                        // Setting Mode to Single to reveal bottom View for one item in List
//                        // Setting Mode to Mutliple to reveal bottom Views for multile items in List
//                        ((FreindsRequestListSwipeAdapter) mRequestListSwipeViewAdapter).setMode(Attributes.Mode.Single);
//
//                        mRecyclerViewRequest.setAdapter(mRequestListSwipeViewAdapter);
//
//
//                   // }
//                }
//
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//
//            }
//        });
//
//    }

    public static void getFriendcombineListAgain(){
        Log.e("static","getFriendcombineListAgain");

        FileUploadService service = ServiceGenerator.createService(FileUploadService.BASE_URL_NEARBY_FRIENDS);

        service.get_friendsCombineList(prefsManager.getCaseId(), prefsManager.getToken(), new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, Response response) {
                Type type = new TypeToken<FriendsCombineListResponse>() {
                }.getType();
                Log.e("jsonObject:", "" + jsonObject.toString());

                FriendsCombineListResponse myFriendsListResponse = new Gson().fromJson(jsonObject.toString(), type);

                if(myFriendsListResponse.getSuccess()==1) {
                    friendCombineData = myFriendsListResponse.getData();
                    if(friendCombineData.getFriendRequest().size()==0){
                        mRecyclerViewRequest.setVisibility(View.GONE);
                        //tvEmptyViewRequest.setVisibility(View.VISIBLE);
                        LvRequest.setVisibility(View.GONE);

                    }
                    else{
                        LvRequest.setVisibility(View.VISIBLE);
                        mRecyclerViewRequest.setVisibility(View.VISIBLE);
                    }
                    if(friendCombineData.getFriendList().size()==0){
                        mRecyclerViewFriends.setVisibility(View.GONE);
                       // empty_friends.setVisibility(View.VISIBLE);
                        LvFriends.setVisibility(View.GONE);
                }else{
                        LvFriends.setVisibility(View.VISIBLE);
                        mRecyclerViewFriends.setVisibility(View.VISIBLE);
                    }

                    if(friendCombineData.getFriendList().size()==0&&friendCombineData.getFriendRequest().size()==0){
                     empty_friends.setVisibility(View.VISIBLE);
                    }


                    tvCurrentCount.setText("" + friendCombineData.getFriendList().size());
                    tvRequestCount.setText("" + friendCombineData.getFriendRequest().size());
//                    if (selectedTab == 1) {
                    mCurrentListSwipeViewAdapter = new FreindsCurrentListSwipeAdapter(BaseActivity.context, friendCombineData.getFriendList(), tvCurrentCount);

                    // Setting Mode to Single to reveal bottom View for one item in List
                    // Setting Mode to Mutliple to reveal bottom Views for multile items in List
                    ((FreindsCurrentListSwipeAdapter) mCurrentListSwipeViewAdapter).setMode(Attributes.Mode.Single);

                    mRecyclerViewFriends.setAdapter(mCurrentListSwipeViewAdapter);

//                    } else {

                    // tvRequestCount.setText("" + friendCombineData.getFriendRequest().size());
                    mRequestListSwipeViewAdapter = new FreindsRequestListSwipeAdapter(BaseActivity.context, friendCombineData, tvRequestCount,mCurrentListSwipeViewAdapter);


                    // Setting Mode to Single to reveal bottom View for one item in List
                    // Setting Mode to Mutliple to reveal bottom Views for multile items in List
                    ((FreindsRequestListSwipeAdapter) mRequestListSwipeViewAdapter).setMode(Attributes.Mode.Single);

                    mRecyclerViewRequest.setAdapter(mRequestListSwipeViewAdapter);

                    // }
                }

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

    }



//    //================== comet chat==============//
//
//    private void cometChatInilizers(){
//        SharedPreferenceHelper.initialize(MyFriendsActivity.this);
//        cometchat = CometChat.getInstance(MyFriendsActivity.this,API_KEY);
//        DatabaseHandler handler = new DatabaseHandler(this);
//        dbhelper = new DatabaseHandler(this);
//        chatSubscribe();
//    }
//
//    private void chatSubscribe(){
//        final SubscribeCallbacks subCallbacks = new SubscribeCallbacks() {
//            @Override
//            public void gotOnlineList(JSONObject jsonObject) {
//
//                //Log.e("online users",""+jsonObject);
//                jsonObjectUsers=jsonObject;
//            }
//
//            @Override
//            public void onError(JSONObject jsonObject) {
//
//            }
//
//            @Override
//            public void onMessageReceived(JSONObject receivedMessage) {
//                try {
//
//                    Log.e("receivedMessage",""+receivedMessage);
//                    msg_id = receivedMessage.getString("id");
//                    String messagetype = receivedMessage.getString("message_type");
//                    Intent intent = new Intent();
//                    intent.setAction("NEW_SINGLE_MESSAGE");
//                    boolean imageMessage = false, videomessage = false, ismyMessage = false;
//                    if (messagetype.equals("12")) {
//                        intent.putExtra("imageMessage", "1");
//                        imageMessage = true;
//                        if (receivedMessage.getString("self").equals("1")) {
//                            intent.putExtra("myphoto", "1");
//                            ismyMessage = true;
//                        }
//                    } else if (messagetype.equals("14")) {
//                        intent.putExtra("videoMessage", "1");
//                        videomessage = true;
//                        if (receivedMessage.getString("self").equals("1")) {
//                            intent.putExtra("myVideo", "1");
//                            ismyMessage = true;
//                        }
//                    }
//                    intent.putExtra("message_type", messagetype);
//                    intent.putExtra("user_id", receivedMessage.getInt("from"));
//                    intent.putExtra("message", receivedMessage.getString("message").trim());
//                    intent.putExtra("time", receivedMessage.getString("sent"));
//                    intent.putExtra("message_id", receivedMessage.getString("id"));
//                    intent.putExtra("from", receivedMessage.getString("from"));
//                    String to = null;
//                    if (receivedMessage.has("to")) {
//                        to = receivedMessage.getString("to");
//                    } else {
//                        to = SharedPreferenceHelper.get(Keys.SharedPreferenceKeys.myId);
//                    }
//                    intent.putExtra("to", to);
//                    String time = Utils.convertTimestampToDate(Utils.correctTimestamp(Long.parseLong(receivedMessage
//                            .getString("sent"))));
//                    SingleChatMessage newMessage = new SingleChatMessage(receivedMessage.getString("id"),
//                            receivedMessage.getString("message").trim(), time, ismyMessage,
//                            receivedMessage.getString("from"), to, messagetype, 0);
//                    dbhelper.insertOneOnOneMessage(newMessage);
//                   sendBroadcast(intent);
//                }catch(JSONException e){
//
//                }
//            }
//
//            @Override
//            public void gotProfileInfo(JSONObject profileInfo) {
//                Logger.error("profile infor " + profileInfo);
//                cometchat.getPluginInfo(new Callbacks() {
//
//                    @Override
//                    public void successCallback(JSONObject response) {
//                        Log.d("abc", "PLugin infor =" + response);
//                    }
//
//                    @Override
//                    public void failCallback(JSONObject response) {
//
//                    }
//                });
//                JSONObject j = profileInfo;
//                try {
//                    msg_id = j.getString("id");
//                    SharedPreferenceHelper.save(Keys.SharedPreferenceKeys.myId, msg_id);
//                    if (j.has("push_channel")) {
//                        PushNotificationsManager.subscribe(j.getString("push_channel"));
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//
//            @Override
//            public void gotAnnouncement(JSONObject jsonObject) {
//
//            }
//
//            @Override
//            public void onActionMessageReceived(JSONObject response) {
//
//                Log.e("Action Receive Msg",""+response);
//                try {
//                    String action = response.getString("action");
//                    Intent i = new Intent("NEW_SINGLE_MESSAGE");
//                    if (action.equals("typing_start")) {
//                        i.putExtra("action", "typing_start");
//                    } else if (action.equals("typing_stop")) {
//                        i.putExtra("action", "typing_stop");
//                    } else if (action.equals("message_read")) {
//                        i.putExtra("action", "message_read");
//                        i.putExtra("from", response.getString("from"));
//                        i.putExtra("message_id", response.getString("message_id"));
//                        Utils.msgtoTickList.put(response.getString("message_id"), Keys.MessageTicks.read);
//                    } else if (action.equals("message_deliverd")) {
//                        i.putExtra("action", "message_deliverd");
//                        i.putExtra("from", response.getString("from"));
//                        i.putExtra("message_id", response.getString("message_id"));
//                        Utils.msgtoTickList.put(response.getString("message_id"), Keys.MessageTicks.deliverd);
//                    }
//                   sendBroadcast(i);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//
//        if (CometChat.isLoggedIn()) {
//            cometchat.subscribe(true, subCallbacks);
//        }
//    }
}
