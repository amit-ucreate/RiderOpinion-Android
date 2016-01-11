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
import com.nutsuser.ridersdomain.adapter.AdapterDestination;
import com.nutsuser.ridersdomain.adapter.CustomGridAdapter;
import com.nutsuser.ridersdomain.utils.RecyclerItemClickListener;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by user on 8/28/2015.
 */
public class DestinationsListActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    private Activity activity;
    @Bind(R.id.tvTitleToolbar)
    TextView tvTitleToolbar;
    @Bind(R.id.tvName)
    TextView tvName;
   /* @Bind(R.id.tvAddress)
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
    @Bind(R.id.rvDestinations)
    RecyclerView rvDestinations;
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.lvSlidingMenu)
    LinearLayout lvSlidingMenu;
    @Bind(R.id.gridView1)
    GridView gridView1;
    //public static String [] prgmNameList={"Riding Destinations","Meet 'N' Plan A Ride","Riding Events \n    ","Modifly Your Bikes","Healthy Riding","Get Directions","Notifications","Settings"};
    //public static int [] prgmImages={R.drawable.icon_menu_destination,R.drawable.icon_menu_meetplan,R.drawable.icon_menu_events,R.drawable.icon_modifybike,R.drawable.icon_menu_healthy_riding,R.drawable.icon_menu_get_direction,R.drawable.icon_menu_notification,R.drawable.icon_menu_settings};
    //public static Class [] classList={DestinationsListActivity.class,PlanRideActivity.class,EventsListActivity.class,ModifyBikeActivity.class,HealthyRidingActivity.class,GetDirections.class,NotificationScreen.class,SettingsActivity.class};

    public static String [] prgmNameList={"My Rides","My Messages","My Friends","Chats","Favourite Destination","Notifications","Settings","    \n"};
    public static int [] prgmImages={R.drawable.ic_menu_fav_destinations,R.drawable.ic_menu_my_messages,R.drawable.ic_menu_my_friends,R.drawable.ic_menu_menu_chats,R.drawable.ic_menu_fav_destinations,R.drawable.ic_menu_menu_notifications,R.drawable.ic_menu_menu_settings,R.drawable.ic_menu_menu_blank_icon};
    public static Class [] classList={MyRidesRecyclerView.class,ChatListScreen.class,MyFriends.class,ChatListScreen.class,FavouriteDesination.class,Notification.class,SettingsActivity.class,SettingsActivity.class};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destinations_list);
        activity = DestinationsListActivity.this;

        ButterKnife.bind(activity);
        setupActionBar();
        setFontsToTextViews();
        mDrawerLayout.closeDrawer(lvSlidingMenu);
        rvDestinations.setLayoutManager(new LinearLayoutManager(activity));
        rvDestinations.setAdapter(new AdapterDestination(activity));
        rvDestinations.addOnItemTouchListener(new RecyclerItemClickListener(activity, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                startActivity(new Intent(activity, DestinationsDetailActivity.class));
            }
        }));
        gridView1.setAdapter(new CustomGridAdapter(this, prgmNameList, prgmImages));
        gridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==7){

                }
                else{
                    Log.e("positiuon:", "" + classList[position]);
                    intentCalling(classList[position]);
                }
            }
        });

    }
    public void intentCalling(Class name){
        Intent mIntent=new Intent(DestinationsListActivity.this,name);
        startActivity(mIntent);

    }


    private void setupActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_home);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void setFontsToTextViews() {
        tvTitleToolbar.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
        tvName.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
       // tvAddress.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
       // tvDestinations.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
       // tvEvents.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
       // tvModifyBike.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
       // tvMeetAndPlanRide.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
       // tvHealthyRiding.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
        //tvGetDirections.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
       // tvNotifications.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
        //tvSettings.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
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

    @OnClick({R.id.ivFilter, R.id.ivMenu, R.id.rlProfile})
    void onclick(View view) {
        switch (view.getId()) {
            case R.id.ivFilter:
                startActivity(new Intent(DestinationsListActivity.this, FilterActivity.class));
                break;

            case R.id.ivMenu:
                if (mDrawerLayout.isDrawerOpen(lvSlidingMenu))
                    mDrawerLayout.closeDrawer(lvSlidingMenu);
                else
                    mDrawerLayout.openDrawer(lvSlidingMenu);
                break;
            case R.id.tvNotifications:
                startActivity(new Intent(activity, ChatListScreen.class));
                break;
            case R.id.rlProfile:
                startActivity(new Intent(activity, ProfileActivity.class));
                break;

        }
    }


}
