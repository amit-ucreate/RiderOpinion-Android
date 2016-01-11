package com.nutsuser.ridersdomain.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
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
import com.nutsuser.ridersdomain.adapter.AdapterEvent;
import com.nutsuser.ridersdomain.adapter.CustomGridAdapter;
import com.nutsuser.ridersdomain.fragments.CaldroidCustomFragment;
import com.nutsuser.ridersdomain.utils.RecyclerItemClickListener;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by user on 9/2/2015.
 */
public class EventsListActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tvTitleToolbar)
    TextView tvTitleToolbar;
    @Bind(R.id.tvListView)
    TextView tvListView;
    @Bind(R.id.tvCalendarView)
    TextView tvCalendarView;
    @Bind(R.id.rvEvents)
    RecyclerView rvEvents;
    @Bind(R.id.llCalender)
    LinearLayout llCalender;
    private AdapterEvent adapterEvent;
    private CaldroidCustomFragment caldroidFragment;
    private Activity activity;
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.lvSlidingMenu)
    LinearLayout lvSlidingMenu;
    @Bind(R.id.tvName)
    TextView tvName;
  /*  @Bind(R.id.tvAddress)
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
  @Bind(R.id.gridView1)
  GridView gridView1;
    //public static String [] prgmNameList={"Riding Destinations","Meet 'N' Plan A Ride","Riding Events \n    ","Modifly Your Bikes","Healthy Riding","Get Directions","Notifications","Settings"};
   // public static int [] prgmImages={R.drawable.icon_menu_destination,R.drawable.icon_menu_meetplan,R.drawable.icon_menu_events,R.drawable.icon_modifybike,R.drawable.icon_menu_healthy_riding,R.drawable.icon_menu_get_direction,R.drawable.icon_menu_notification,R.drawable.icon_menu_settings};
   //public static Class [] classList={DestinationsListActivity.class,PlanRideActivity.class,EventsListActivity.class,ModifyBikeActivity.class,HealthyRidingActivity.class,GetDirections.class,NotificationScreen.class,SettingsActivity.class};

    public static String [] prgmNameList={"My Rides","My Messages","My Friends","Chats","Favourite Destination","Notifications","Settings","    \n"};
    public static int [] prgmImages={R.drawable.ic_menu_fav_destinations,R.drawable.ic_menu_my_messages,R.drawable.ic_menu_my_friends,R.drawable.ic_menu_menu_chats,R.drawable.ic_menu_fav_destinations,R.drawable.ic_menu_menu_notifications,R.drawable.ic_menu_menu_settings,R.drawable.ic_menu_menu_blank_icon};
    public static Class [] classList={MyRidesRecyclerView.class,ChatListScreen.class,MyFriends.class,ChatListScreen.class,FavouriteDesination.class,Notification.class,SettingsActivity.class,SettingsActivity.class};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_events);
        activity = this;
        ButterKnife.bind(this);
        setupActionBar();
        mDrawerLayout.closeDrawer(lvSlidingMenu);
        rvEvents.setLayoutManager(new LinearLayoutManager(activity));
        setFonts();
        adapterEvent = new AdapterEvent(activity);
        rvEvents.setAdapter(adapterEvent);
        rvEvents.addOnItemTouchListener(new RecyclerItemClickListener(activity, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                startActivity(new Intent(activity, EventDetailActivity.class));
            }
        }));
        tvTitleToolbar.setText("RIDING EVENTS");
        setUpCalendar();
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
        Intent mIntent=new Intent(EventsListActivity.this,name);
        startActivity(mIntent);

    }
    private void setUpCalendar() {
        caldroidFragment = new CaldroidCustomFragment();
        Bundle args = new Bundle();
        final Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));

        caldroidFragment.setArguments(args);

        caldroidFragment.setBackgroundResourceForDate(R.color.dark_brown, cal.getTime());
        caldroidFragment.setTextColorForDate(R.color.white, cal.getTime());
        caldroidFragment.setThemeResource(com.caldroid.R.style.CaldroidDefaultDarkCalendarViewLayout);
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.llCalender, caldroidFragment);
        t.commit();
        caldroidFragment.setCaldroidListener(new CaldroidListener() {
            @Override
            public void onSelectDate(Date date, View view) {
                caldroidFragment.setBackgroundResourceForDate(R.color.light_brown, date);
                caldroidFragment.setTextColorForDate(R.color.white, date);
                caldroidFragment.refreshView();
            }
        });
    }

    private void setupActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_home);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void setFonts() {
        tvListView.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
        tvCalendarView.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
        tvTitleToolbar.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
        tvName.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
      //  tvAddress.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
      //  tvDestinations.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
       // tvEvents.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
       // tvModifyBike.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
       // tvMeetAndPlanRide.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
        ///tvHealthyRiding.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
       // tvGetDirections.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
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

    @OnClick({R.id.ivFilter, R.id.tvCalendarView, R.id.tvListView, R.id.ivMenu, R.id.rlProfile})
    void click(View view) {
        switch (view.getId()) {
            case R.id.ivFilter:
                startActivity(new Intent(activity, FilterActivity.class));
                break;
            case R.id.tvCalendarView:
                tvCalendarView.setBackgroundColor(ContextCompat.getColor(activity, R.color.light_brown));
                tvListView.setBackgroundColor(ContextCompat.getColor(activity, R.color.fourty_of_black));
                llCalender.setVisibility(View.VISIBLE);
                rvEvents.setVisibility(View.GONE);
                break;

            case R.id.tvListView:
                tvListView.setBackgroundColor(ContextCompat.getColor(activity, R.color.light_brown));
                tvCalendarView.setBackgroundColor(ContextCompat.getColor(activity, R.color.fourty_of_black));
                llCalender.setVisibility(View.GONE);
                rvEvents.setVisibility(View.VISIBLE);
                break;

            case R.id.ivMenu:
                if (mDrawerLayout.isDrawerOpen(lvSlidingMenu))
                    mDrawerLayout.closeDrawer(lvSlidingMenu);
                else
                    mDrawerLayout.openDrawer(lvSlidingMenu);
                break;
            case R.id.rlProfile:
                startActivity(new Intent(activity, ProfileActivity.class));
                break;
           /* case R.id.tvNotifications:
            startActivity(new Intent(activity, ChatListScreen.class));
            break;
            case R.id.tvDestinations:
                startActivity(new Intent(activity, DestinationsListActivity.class));
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
