package com.nutsuser.ridersdomain.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.adapter.AdapterRide;
import com.nutsuser.ridersdomain.adapter.CustomGridAdapter;
import com.nutsuser.ridersdomain.utils.DividerItemDecoration;
import com.nutsuser.ridersdomain.utils.RecyclerItemClickListener;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by user on 10/1/2015.
 */
public class PlannedRidingsActivity extends BaseActivity {

    //  public static String [] prgmNameList={"My Rides","My Messages","My Friends","Chats","Favourite Desination","Notifications","Settings","NEW"};
    // public static int [] prgmImages={R.drawable.icon_menu_destination,R.drawable.icon_menu_meetplan,R.drawable.icon_menu_events,R.drawable.icon_modifybike,R.drawable.icon_menu_healthy_riding,R.drawable.icon_menu_notification,R.drawable.icon_modifybike,R.drawable.icon_menu_destination};
    // public static Class [] classList={DestinationsListActivity.class,PlanRideActivity.class,EventsListActivity.class,ModifyBikeActivity.class,HealthyRidingActivity.class,GetDirections.class,NotificationScreen.class,SettingsActivity.class};
    public static String[] prgmNameList = {"My Rides", "My Messages", "My Friends", "Chats", "Favourite Destination", "Notifications", "Settings", "    \n"};
    public static int[] prgmImages = {R.drawable.ic_menu_fav_destinations, R.drawable.ic_menu_my_messages, R.drawable.ic_menu_my_friends, R.drawable.ic_menu_menu_chats, R.drawable.ic_menu_fav_destinations, R.drawable.ic_menu_menu_notifications, R.drawable.ic_menu_menu_settings, R.drawable.ic_menu_menu_blank_icon};
    public static Class[] classList = {MyRidesRecyclerView.class, ChatListScreen.class, MyFriends.class, ChatListScreen.class, FavouriteDesination.class, Notification.class, SettingsActivity.class, SettingsActivity.class};
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.rvRides)
    RecyclerView rvRides;
    @Bind(R.id.tvPlaces)
    TextView tvPlaces;
    @Bind(R.id.tvDateAndTime)
    TextView tvDateAndTime;
    @Bind(R.id.tvRidesAlreadyPlanned)
    TextView tvRidesAlreadyPlanned;
    @Bind(R.id.tvSubmit)
    TextView tvSubmit;
    @Bind(R.id.tvName)
    TextView tvName;
    /*   @Bind(R.id.tvAddress)
       TextView tvAddress;
       @Bind(R.id.tvDestinations)
       TextView tvDestinations;
       @Bind(R.id.tvEvents)
       TextView tvEvents;
       @Bind(R.id.tvModifyBike)
       TextView tvModifyBike;
       @Bind(R.id.tvMeetAndPlanRide)
       TextView tvMeetAndPlanRide;
       @Bind(R.id.tvHealthyRiding)
       TextView tvHealthyRiding;
       @Bind(R.id.tvGetDirections)
       TextView tvGetDirections;
       @Bind(R.id.tvNotifications)
       TextView tvNotifications;
       @Bind(R.id.tvSettings)
       TextView tvSettings;*/
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.lvSlidingMenu)
    LinearLayout lvSlidingMenu;
    @Bind(R.id.gridView1)
    GridView gridView1;
    private AdapterRide adapterRide;
    private Activity activity;

    String ridetype,startdate,enddate,starttime,fromlatitude,fromlongitude,fromlocation,tolatitude,tolongitude,tolocation,hlatitude,hlongitude,htype,hlocation,hlatitude1,hlongitude1,htype1,hlocation1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planned_ridings);
        activity = this;
        ButterKnife.bind(this);
        setupActionBar();
        setFonts();
        ridetype=getIntent().getStringExtra("rideType");
        if(ridetype.matches("Breakfast Ride")){
            startdate=getIntent().getStringExtra("startdate");
            starttime=getIntent().getStringExtra("starttime");
            fromlatitude=getIntent().getStringExtra("fromlatitude");
            fromlongitude=getIntent().getStringExtra("fromlatitude");
            fromlocation=getIntent().getStringExtra("fromlocation");
            tolatitude=getIntent().getStringExtra("tolatitude");
            tolongitude=getIntent().getStringExtra("tolongitude");
            tolocation=getIntent().getStringExtra("tolocation");
            hlatitude=getIntent().getStringExtra("hlatitude");
            hlongitude=getIntent().getStringExtra("hlongitude");
            hlocation=getIntent().getStringExtra("hlocation");
            htype=getIntent().getStringExtra("breakfast");
        }
        else{
            startdate=getIntent().getStringExtra("startdate");
            starttime=getIntent().getStringExtra("starttime");
            fromlatitude=getIntent().getStringExtra("fromlatitude");
            fromlongitude=getIntent().getStringExtra("fromlatitude");
            fromlocation=getIntent().getStringExtra("fromlocation");
            enddate=getIntent().getStringExtra("enddate");
            tolatitude=getIntent().getStringExtra("tolatitude");
            tolongitude=getIntent().getStringExtra("tolongitude");
            tolocation=getIntent().getStringExtra("tolocation");
            hlatitude=getIntent().getStringExtra("hlatitude");
            hlongitude=getIntent().getStringExtra("hlongitude");
            hlocation=getIntent().getStringExtra("hlocation");
            htype=getIntent().getStringExtra("htype");
            hlatitude1=getIntent().getStringExtra("hlatitude1");
            hlongitude1=getIntent().getStringExtra("hlongitude1");
            hlocation1=getIntent().getStringExtra("hlocation1");
            htype1=getIntent().getStringExtra("htype1");

        }
        tvPlaces.setText(fromlocation+"  -  "+tolocation);
        tvDateAndTime.setText(startdate+"/"+starttime);
        rvRides.setLayoutManager(new LinearLayoutManager(this));
        adapterRide = new AdapterRide(this);

        rvRides.addItemDecoration(new DividerItemDecoration(this, R.drawable.divider));
        rvRides.setAdapter(adapterRide);
        rvRides.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                startActivity(new Intent(PlannedRidingsActivity.this, PlanRideDetailActivity.class));
            }
        }));

        gridView1.setAdapter(new CustomGridAdapter(this, prgmNameList, prgmImages));
        gridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 7) {

                } else {
                    Log.e("positiuon:", "" + classList[position]);
                    intentCalling(classList[position]);
                }
            }
        });
    }

    public void intentCalling(Class name) {
        Intent mIntent = new Intent(PlannedRidingsActivity.this, name);
        startActivity(mIntent);

    }

    private void setFonts() {
        Typeface typefaceNormal = Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF");
        tvPlaces.setTypeface(typefaceNormal);
        tvPlaces.setTypeface(typefaceNormal);
        tvDateAndTime.setTypeface(typefaceNormal);
        tvRidesAlreadyPlanned.setTypeface(typefaceNormal);
        tvSubmit.setTypeface(typefaceNormal);
        tvName.setTypeface(typefaceNormal);
        //  tvAddress.setTypeface(typefaceNormal);
        //tvDestinations.setTypeface(typefaceNormal);
        // tvEvents.setTypeface(typefaceNormal);
        // tvModifyBike.setTypeface(typefaceNormal);
        ///tvMeetAndPlanRide.setTypeface(typefaceNormal);
        //tvHealthyRiding.setTypeface(typefaceNormal);
        // tvGetDirections.setTypeface(typefaceNormal);
        // tvNotifications.setTypeface(typefaceNormal);
        // tvSettings.setTypeface(typefaceNormal);
    }

    private void setupActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_back);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.ivMenu, R.id.rlProfile, R.id.tvSubmit})
    void click(View view) {
        switch (view.getId()) {

            case R.id.ivMenu:
                if (mDrawerLayout.isDrawerOpen(lvSlidingMenu))
                    mDrawerLayout.closeDrawer(lvSlidingMenu);
                else
                    mDrawerLayout.openDrawer(lvSlidingMenu);
                break;
            case R.id.rlProfile:
                startActivity(new Intent(activity, ProfileActivity.class));
                break;
            case R.id.tvSubmit:
                startActivity(new Intent(activity, PlanARidePostRide.class));
                break;
            /*case R.id.tvNotifications:
                startActivity(new Intent(activity, ChatListScreen.class));
                break;
            case R.id.tvDestinations:
                startActivity(new Intent(activity, DestinationsListActivity.class));
                break;
            case R.id.tvEvents:
                startActivity(new Intent(activity, EventsListActivity.class));
                break;
            case R.id.tvModifyBike:
                startActivity(new Intent(activity, ModifyBikeActivity.class));
                break;
            case R.id.tvMeetAndPlanRide:
                startActivity(new Intent(activity, PlanRideActivity.class));
                break;
            case R.id.tvHealthyRiding:
                startActivity(new Intent(activity, HealthyRidingActivity.class));
                break;

            case R.id.tvSettings:
                startActivity(new Intent(activity, SettingsActivity.class));
                break;*/
        }
    }

}
