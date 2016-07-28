package com.nutsuser.ridersdomain.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.adapter.AdapterCalendarEvent;
import com.nutsuser.ridersdomain.adapter.AdapterEvent;
import com.nutsuser.ridersdomain.adapter.CustomGridAdapter;
import com.nutsuser.ridersdomain.chat.SampleCometChatActivity;
import com.nutsuser.ridersdomain.utils.CONSTANTS;
import com.nutsuser.ridersdomain.utils.CustomizeDialog;
import com.nutsuser.ridersdomain.utils.PrefsManager;
import com.nutsuser.ridersdomain.utils.RecyclerItemClickListener;
import com.nutsuser.ridersdomain.web.pojos.CalendarEventData;
import com.nutsuser.ridersdomain.web.pojos.RidingEventData;
import com.rollbar.android.Rollbar;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ucreateuser on 6/30/2016.
 */
public class CalendarEventListActivity extends BaseActivity implements CONSTANTS {
    private Activity activity;
    @Bind(R.id.fab_evnt)
    FloatingActionButton fab_evnt;

    @Bind(R.id.ivBack)
    ImageView ivBack;

    PrefsManager prefsManager;
    CustomizeDialog mCustomizeDialog;
    String AccessToken, UserId;
//    @Bind(R.id.tvListView)
//    TextView tvListView;
    @Bind(R.id.rvEvents)
    RecyclerView rvEvents;
    ArrayList<RidingEventData> calendarResponseData = new ArrayList<>();
    private AdapterCalendarEvent adapterEvent;


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
    @Bind(R.id.tvTitleToolbar)
    TextView tvTitleToolbar;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
//=====================================//
    String date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_events);
        try {
            date = getIntent().getExtras().getString("date");
            activity = CalendarEventListActivity.this;
            prefsManager = new PrefsManager(CalendarEventListActivity.this);
            ButterKnife.bind(CalendarEventListActivity.this);
            setupActionBarBack(toolbar);
            setFonts();
            setSliderMenu();
            setDrawerSlider();

            if (isNetworkConnected()) {
                setUpCalendarEVentList(date);
            } else {
                showToast("Internet is not connected");
            }
            rvEvents.setLayoutManager(new LinearLayoutManager(activity));
            rvEvents.addOnItemTouchListener(new RecyclerItemClickListener(activity, new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {

                    Intent mIntent = new Intent(CalendarEventListActivity.this, EventDetailActivity.class);
                    mIntent.putExtra("eventId", calendarResponseData.get(position).getEventId());
                    mIntent.putExtra("typeEmailPhone", getIntent().getStringExtra("typeEmailPhone"));
                    startActivity(mIntent);
                }
            }));

            if (new PrefsManager(this).isServicesRunning()) {
                fab_evnt.setVisibility(View.VISIBLE);
            } else {
                fab_evnt.setVisibility(View.GONE);
            }
            fab_evnt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String eventid = new PrefsManager(CalendarEventListActivity.this).getEventId();
                    Intent intent = new Intent(activity, TrackingScreen.class);
                    intent.putExtra("eventId", eventid);
                    startActivity(intent);
                }
            });
        }catch(Exception e){
            Rollbar.reportException(e, "minor", "CalendarEventListActivity OnCreate()");
        }

    }

    private void setUpCalendarEVentList(String date){
        showProgressDialog();
        if(EventsListActivity.data.size()==0){
            dismissProgressDialog();
            rvEvents.setVisibility(View.GONE);
            //tvListView.setVisibility(View.VISIBLE);
        }else{
            for(int i=0;i<EventsListActivity.data.size();i++){
                if(date.matches(EventsListActivity.data.get(i).getStartDate())||date.equalsIgnoreCase(EventsListActivity.data.get(i).getStartDate())){
                    calendarResponseData.add(EventsListActivity.data.get(i));
                    adapterEvent = new AdapterCalendarEvent(activity,calendarResponseData);
                    rvEvents.setAdapter(adapterEvent);
                    Log.e("yes","match");
                }else{
                    Log.e("no","match");
                }
            }

            dismissProgressDialog();
        }
    }



    private void setFonts() {
        tvTitleToolbar.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/MACHINEN.TTF"));

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
                if (position == 5) {
                    mDrawerLayout.closeDrawer(lvSlidingMenu);
                }else
//                if (position == 1) {
//                    Log.e("positiuon:", "" + classList[position]);
//                    intent_Calling(classList[position], "My Messages");
//                }  else {
                    Log.e("positiuon:", "" + classList[position]);
                    intentCalling(classList[position]);
                //}
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
        Intent mIntent = new Intent(CalendarEventListActivity.this, name);
        startActivity(mIntent);
        //finish();
    }

    public void intent_Calling(Class name, String na) {
        Intent mIntent = new Intent(CalendarEventListActivity.this, name);
        mIntent.putExtra(SCREEN_OPEN, na);
        startActivity(mIntent);
        finish();
    }
    //=============== menus===============//
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
//                Intent intent = new Intent(CalendarEventListActivity.this, MainScreenActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.putExtra("MainScreenResponse", "OPEN");
//                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.ivBack,R.id.ivMenu, R.id.btFullProfile, R.id.btUpdateProfile,})
    void click(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                onBackPressed();
                break;
            case R.id.ivMenu:
                if (mDrawerLayout.isDrawerOpen(lvSlidingMenu))
                    mDrawerLayout.closeDrawer(lvSlidingMenu);
                else
                    mDrawerLayout.openDrawer(lvSlidingMenu);
                break;
            case R.id.btUpdateProfile:
                startActivity(new Intent(activity, ProfileActivity.class));
                break;
            case R.id.btFullProfile:
                startActivity(new Intent(activity, PublicProfileScreen.class));
                break;
        }
    }
}
