package com.nutsuser.ridersdomain.activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.Toast;

import com.daimajia.swipe.util.Attributes;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.adapter.CustomGridAdapter;
import com.nutsuser.ridersdomain.adapter.SwipeRecyclerViewAdapter;
import com.nutsuser.ridersdomain.adapter.UpcomingSwipeViewAdapter;
import com.nutsuser.ridersdomain.chat.SampleCometChatActivity;
import com.nutsuser.ridersdomain.services.GPSService;
import com.nutsuser.ridersdomain.utils.CustomizeDialog;
import com.nutsuser.ridersdomain.utils.PrefsManager;
import com.nutsuser.ridersdomain.view.CircleButtonText;
import com.nutsuser.ridersdomain.view.CustomDialog;
import com.nutsuser.ridersdomain.web.api.FileUploadService;
import com.nutsuser.ridersdomain.web.api.ServiceGenerator;
import com.nutsuser.ridersdomain.web.pojos.Datum;
import com.nutsuser.ridersdomain.web.pojos.DividerItemDecoration;
import com.nutsuser.ridersdomain.web.pojos.MyRideUpcomingPast;
import com.nutsuser.ridersdomain.web.pojos.Student;
import com.rollbar.android.Rollbar;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;


public class MyRidesRecyclerView extends BaseActivity {
    PrefsManager prefsManager;
    String AccessToken, UserId;
    CustomizeDialog mCustomizeDialog;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.bt_upcoming)
    TextView bt_upcoming;
    @Bind(R.id.bt_past)
    TextView bt_past;
    UpcomingSwipeViewAdapter mUpcomingSwipeViewAdapter;
    SwipeRecyclerViewAdapter mAdapter;
    ArrayList<Datum> datum = new ArrayList<>();
//    @Bind(R.id.fmUpcoming)
//    FrameLayout fmUpcoming;
    CircleButtonText tvUpcomingCount;
    @Bind(R.id.tvTitleToolbar)
    TextView tvTitleToolbar;


    /**
     * RecyclerView: The new recycler view replaces the list view. Its more modular and therefore we
     * must implement some of the functionality ourselves and attach it to our recyclerview.
     * <p/>
     * 1) Position items on the screen: This is done with LayoutManagers
     * 2) Animate & Decorate views: This is done with ItemAnimators & ItemDecorators
     * 3) Handle any touch events apart from scrolling: This is now done in our adapter's ViewHolder
     */

    private ArrayList<Student> mDataSet;
    private TextView tvEmptyView;
    private RecyclerView mRecyclerView;

//=========== slider items====================//
    public static String[] prgmNameList = {"My Rides", "My Messages", "My Friends", "Chats", "Favorites", "Notifications", "Settings", "Near By Friends"};
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
    int selectedTab =0 ;
//=====================================//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swiprecyclerview_update);
        prefsManager = new PrefsManager(MyRidesRecyclerView.this);
        try {
            Fresco.initialize(this);
            ButterKnife.bind(this);

            toolbar = (Toolbar) findViewById(R.id.toolbar);
            tvEmptyView = (TextView) findViewById(R.id.empty_view);
            tvUpcomingCount = (CircleButtonText) findViewById(R.id.tvUpcomingCount);
            mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
            bt_upcoming = (TextView) findViewById(R.id.bt_upcoming);
            bt_past = (TextView) findViewById(R.id.bt_past);
            //fmUpcoming=(FrameLayout)findViewById(R.id.fmUpcoming);
            // Layout Managers:
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

            // Item Decorator:
            mRecyclerView.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.divider)));
            // mRecyclerView.setItemAnimator(new FadeInLeftAnimator());

            mDataSet = new ArrayList<Student>();
            loadData();

            setFonts();
            setupActionBar(toolbar);


            //=== set empty view is arraylist is 0=======//
            if (mDataSet.isEmpty()) {
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

            try {
             //   if(getIntent().hasExtra("past")) {
                UserId= getIntent().getExtras().getString("userId");
                    if (getIntent().getExtras().getBoolean("past") == true) {

                        MyRidingDetails(UserId,"past");
                        selectedTab++;
                    } else {
                        MyRidingDetails(UserId,"upcoming");
                        selectedTab = 0;
                  //  }
                }
            } catch (NullPointerException e) {
                UserId= prefsManager.getCaseId();
                MyRidingDetails(UserId,"upcoming");
                selectedTab = 0;
            }
            setSliderMenu();

            /*****
             On Drawer Open and Close
             * **/
            setDrawerSlider();

            //=============== Get Location using GPS=============//

            GPSService mGPSService = new GPSService(this);
            mGPSService.getLocation();
        }catch(Exception e){
            Rollbar.reportException(e, "minor", "MyRidesRecyclerView on create");
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
                if (position == 0) {
                        mDrawerLayout.closeDrawer(lvSlidingMenu);
                }else
//                if (position == 1) {
//                    Log.e("positiuon:", "" + classList[position]);
//                    intent_Calling(classList[position], "My Messages");
//                }  else {
                    Log.e("positiuon:", "" + classList[position]);
                    intentCalling(classList[position]);
              //  }
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
        Intent mIntent = new Intent(MyRidesRecyclerView.this, name);
        startActivity(mIntent);
        //finish();
    }

    public void intent_Calling(Class name, String na) {
        Intent mIntent = new Intent(MyRidesRecyclerView.this, name);
        mIntent.putExtra(SCREEN_OPEN, na);
        startActivity(mIntent);
        finish();
    }

    //======== function to set fonts==============//
    private void setFonts() {
        tvTitleToolbar.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/MACHINEN.TTF"));
        bt_past.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/Lato-Heavy.ttf"));
        bt_upcoming.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/Lato-Heavy.ttf"));

    }
    // load initial data
    public void loadData() {

        for (int i = 0; i <= 20; i++) {
            mDataSet.add(new Student("Student " + i, "androidstudent" + i + "@gmail.com"));

        }
    }

    //================ function to set click listeners===========//

    @TargetApi(16)
    @OnClick({R.id.bt_past, R.id.bt_upcoming, R.id.btFullProfile, R.id.btUpdateProfile, R.id.ivMenu, R.id.rlProfile})
    void click(View view) {
        switch (view.getId()) {
            case R.id.bt_past:
                Log.e("fmPast","fmPast");
//                tvUpcomingCount.setText("0");
               if(selectedTab==0){
                   bt_past.setText("PAST");
                   bt_upcoming.setText("UPCOMING RIDES");
                   datum.clear();
                   MyRidingDetails(UserId,"past");
                   selectedTab++;
               }else{
                   bt_past.setText("UPCOMING");
                   bt_upcoming.setText("PAST RIDES");
                   datum.clear();
                   MyRidingDetails(UserId,"upcoming");
                   selectedTab=0;
               }

//                fmUpcoming.setBackground(getResources().getDrawable(R.drawable.button_corner_left));
//                fmPast.setBackground(getResources().getDrawable(R.drawable.button_corner_right));
//                bt_past.setTextColor(Color.parseColor("#ffffff"));
//                bt_upcoming.setTextColor(Color.parseColor("#CD411E"));


                break;
//            case R.id.bt_upcoming:
//                Log.e("fmUpcoming","fmUpcoming");
//                tvUpcomingCount.setText("0");
//
////                fmUpcoming.setBackground(getResources().getDrawable(R.drawable.button_corner_left_gray));
////                fmPast.setBackground(getResources().getDrawable(R.drawable.button_corner_right_white));
////                bt_past.setTextColor(Color.parseColor("#CD411E"));
////                bt_upcoming.setTextColor(Color.parseColor("#ffffff"));
//                datum.clear();
//                MyRidingDetails("upcoming");
//                break;
            case R.id.btUpdateProfile:
                startActivity(new Intent(MyRidesRecyclerView.this, ProfileActivity.class));
                break;
            case R.id.btFullProfile:
                startActivity(new Intent(MyRidesRecyclerView.this, PublicProfileScreen.class));
                break;
            case R.id.ivMenu:
                if (mDrawerLayout.isDrawerOpen(lvSlidingMenu))
                    mDrawerLayout.closeDrawer(lvSlidingMenu);
                else
                    mDrawerLayout.openDrawer(lvSlidingMenu);
                break;
            case R.id.rlProfile:
               // startActivity(new Intent(MyRidesRecyclerView.this, ProfileActivity.class));
                break;

        }
    }

    //============== function for home icon click========//
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // finish();
                Intent intent = new Intent(MyRidesRecyclerView.this, MainScreenActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("MainScreenResponse", "OPEN");
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try {

            if(getIntent().hasExtra("past")) {

                if (getIntent().getExtras().getBoolean("past") == true) {
                    startActivity(new Intent(MyRidesRecyclerView.this, PublicProfileScreen.class));


                }
            }
        }catch(NullPointerException e){
            Rollbar.reportException(e, "minor", "MyRidesRecyclerView on back pressed");

        }
        finish();
    }

    /**
     * Match MyRidingDetails .
     */
    public void MyRidingDetails(final String userId, final String conditionnane) {
        showProgressDialog();
        Log.e(" MyRidingDetails", " MyRidingDetails");
        try {

            AccessToken = prefsManager.getToken();
            String radius = prefsManager.getRadius();

            FileUploadService service = ServiceGenerator.createService(FileUploadService.BASE_URL);
            service.MyRide(userId, AccessToken, conditionnane, new Callback<JsonObject>() {
                @Override
                public void success(JsonObject jsonObject, retrofit.client.Response response) {
                    Log.e("Upload", "success");
                    Log.e("jsonObject:", "" + jsonObject.toString());

                    Type type = new TypeToken<MyRideUpcomingPast>() {
                    }.getType();
                    MyRideUpcomingPast myRideUpcomingPast = new Gson().fromJson(jsonObject.toString(), type);
                    if (myRideUpcomingPast.getSuccess() == 1) {
                        dismissProgressDialog();
                        datum.clear();
                        datum.addAll(myRideUpcomingPast.getData());

                        Log.e("conditionnane",conditionnane);
                        if (conditionnane.equals("past")) {
                            // Creating Adapter object
                            if(datum.size()==0){
                                tvUpcomingCount.setVisibility(View.GONE);
                            }else {
                                tvUpcomingCount.setText("" + datum.size());
                            }
                            mAdapter = new SwipeRecyclerViewAdapter(MyRidesRecyclerView.this, mDataSet, datum);
                            bt_past.setText("UPCOMING");
                            bt_upcoming.setText("PAST RIDES");


                            // Setting Mode to Single to reveal bottom View for one item in List
                            // Setting Mode to Mutliple to reveal bottom Views for multile items in List
                            ((SwipeRecyclerViewAdapter) mAdapter).setMode(Attributes.Mode.Single);

                            mRecyclerView.setAdapter(mAdapter);
                        } else {
                            bt_past.setText("PAST");
                            bt_upcoming.setText("UPCOMING RIDES");
                            // Creating Adapter object
                            mUpcomingSwipeViewAdapter = new UpcomingSwipeViewAdapter(MyRidesRecyclerView.this, mDataSet, datum);


                            if(datum.size()==0){
                                tvUpcomingCount.setVisibility(View.GONE);
                            }else {
                                tvUpcomingCount.setText("" + datum.size());
                            }
                            // Setting Mode to Single to reveal bottom View for one item in List
                            // Setting Mode to Mutliple to reveal bottom Views for multile items in List
                            ((UpcomingSwipeViewAdapter) mUpcomingSwipeViewAdapter).setMode(Attributes.Mode.Single);

                            mRecyclerView.setAdapter(mUpcomingSwipeViewAdapter);
                        }


                    } else  {
                        dismissProgressDialog();
                        if (conditionnane.equals("past")) {
                            // Creating Adapter object
                            mAdapter = new SwipeRecyclerViewAdapter(MyRidesRecyclerView.this, mDataSet, datum);
                            tvUpcomingCount.setText("" + datum.size());
                            bt_past.setText("UPCOMING");
                            bt_upcoming.setText("PAST RIDES");
                            // Setting Mode to Single to reveal bottom View for one item in List
                            // Setting Mode to Mutliple to reveal bottom Views for multile items in List
                            ((SwipeRecyclerViewAdapter) mAdapter).setMode(Attributes.Mode.Single);

                            mRecyclerView.setAdapter(mAdapter);
                            if(datum.size()==0){
                                tvUpcomingCount.setVisibility(View.GONE);
                                CustomDialog.showProgressDialog(MyRidesRecyclerView.this, myRideUpcomingPast.getMessage().toString());
                            }
                        } else {
                            bt_past.setText("PAST");
                            bt_upcoming.setText("UPCOMING RIDES");
                            // Creating Adapter object
                            mUpcomingSwipeViewAdapter = new UpcomingSwipeViewAdapter(MyRidesRecyclerView.this, mDataSet, datum);

                            tvUpcomingCount.setText("" + datum.size());
                            // Setting Mode to Single to reveal bottom View for one item in List
                            // Setting Mode to Mutliple to reveal bottom Views for multile items in List
                            ((UpcomingSwipeViewAdapter) mUpcomingSwipeViewAdapter).setMode(Attributes.Mode.Single);

                            mRecyclerView.setAdapter(mUpcomingSwipeViewAdapter);
                            if(datum.size()==0){
                                tvUpcomingCount.setVisibility(View.GONE);
                                CustomDialog.showProgressDialog(MyRidesRecyclerView.this, myRideUpcomingPast.getMessage().toString());
                            }
                        }
                        // showToast("Data Not Found.");
                    }


                }

                @Override
                public void failure(RetrofitError error) {
                    dismissProgressDialog();
                    //showToast("Error:" + error);
                    Log.e("Upload", "error");
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
            Rollbar.reportException(e, "minor", "MyRidesRecyclerView My Rides detail API");

        }
    }


}
