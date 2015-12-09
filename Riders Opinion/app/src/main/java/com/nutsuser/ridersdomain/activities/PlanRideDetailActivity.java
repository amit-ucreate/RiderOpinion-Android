package com.nutsuser.ridersdomain.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
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
import com.nutsuser.ridersdomain.adapter.CustomGridAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by user on 10/1/2015.
 */
public class PlanRideDetailActivity extends BaseActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tvPlace)
    TextView tvPlace;
    @Bind(R.id.tvDateAndTime)
    TextView tvDateAndTime;
    @Bind(R.id.tvTitle)
    TextView tvTitle;
    @Bind(R.id.tvLabelHostedBy)
    TextView tvLabelHostedBy;
    @Bind(R.id.tvHostedBy)
    TextView tvHostedBy;
    @Bind(R.id.tvNumberOfRiders)
    TextView tvNumberOfRiders;
    @Bind(R.id.tvLabelHaveJoined)
    TextView tvLabelHaveJoined;
    @Bind(R.id.tvDate)
    TextView tvDate;
    @Bind(R.id.tvCity)
    TextView tvCity;
    @Bind(R.id.tvTime)
    TextView tvTime;
    @Bind(R.id.tvEatables)
    TextView tvEatables;
    @Bind(R.id.tvPetrolPump)
    TextView tvPetrolPump;
    @Bind(R.id.tvServiceCenter)
    TextView tvServiceCenter;
    @Bind(R.id.tvFirstAid)
    TextView tvFirstAid;
    @Bind(R.id.tvJoin)
    TextView tvJoin;
    @Bind(R.id.tvSponsoredAdTitle1)
    TextView tvSponsoredAdTitle1;
    @Bind(R.id.tvSponsoredAdReviews1)
    TextView tvSponsoredAdReviews1;
    @Bind(R.id.tvSponsoredAdLocation1)
    TextView tvSponsoredAdLocation1;
    @Bind(R.id.tvSponsoredAdTitle2)
    TextView tvSponsoredAdTitle2;
    @Bind(R.id.tvSponsoredAdReviews2)
    TextView tvSponsoredAdReviews2;
    @Bind(R.id.tvSponsoredAdLocation2)
    TextView tvSponsoredAdLocation2;
    @Bind(R.id.tvDesc)
    TextView tvDesc;
    @Bind(R.id.tvSubmit)
    TextView tvSubmit;
    @Bind(R.id.tvName)
    TextView tvName;
/*    @Bind(R.id.tvAddress)
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
    private Activity activity;
    @Bind(R.id.gridView1)
    GridView gridView1;
    public static String [] prgmNameList={"Riding Destinations","Meet 'N' Plan A Ride","Riding Events \n    ","Modifly Your Bikes","Healthy Riding","Get Directions","Notifications","Settings"};
    public static int [] prgmImages={R.drawable.icon_menu_destination,R.drawable.icon_menu_meetplan,R.drawable.icon_menu_events,R.drawable.icon_modifybike,R.drawable.icon_menu_healthy_riding,R.drawable.icon_menu_get_direction,R.drawable.icon_menu_notification,R.drawable.icon_menu_settings};
    public static Class [] classList={DestinationsListActivity.class,PlanRideActivity.class,EventsListActivity.class,ModifyBikeActivity.class,HealthyRidingActivity.class,GetDirections.class,ChatListScreen.class,SettingsActivity.class};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_ride_detail);
        activity = this;
        ButterKnife.bind(this);
        setupActionBar();
        setFonts();
        gridView1.setAdapter(new CustomGridAdapter(this, prgmNameList, prgmImages));
        gridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("positiuon:", "" + classList[position]);
                intentCalling(classList[position]);
            }
        });
    }
    public void intentCalling(Class name){
        Intent mIntent=new Intent(PlanRideDetailActivity.this,name);
        startActivity(mIntent);

    }
    private void setFonts() {
        Typeface typefaceNormal = Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF");
        tvTitle.setTypeface(typefaceNormal);
        tvPlace.setTypeface(typefaceNormal);
        tvDateAndTime.setTypeface(typefaceNormal);
        tvHostedBy.setTypeface(typefaceNormal);
        tvLabelHostedBy.setTypeface(typefaceNormal);
        tvNumberOfRiders.setTypeface(typefaceNormal);
        tvSubmit.setTypeface(typefaceNormal);
        tvLabelHaveJoined.setTypeface(typefaceNormal);
        tvDate.setTypeface(typefaceNormal);;
        tvCity.setTypeface(typefaceNormal);;
        tvTime.setTypeface(typefaceNormal);;
        tvEatables.setTypeface(typefaceNormal);;
        tvPetrolPump.setTypeface(typefaceNormal);;
        tvServiceCenter.setTypeface(typefaceNormal);;
        tvFirstAid.setTypeface(typefaceNormal);;
        tvJoin.setTypeface(typefaceNormal);;
        tvSponsoredAdTitle1.setTypeface(typefaceNormal);;
        tvSponsoredAdReviews1.setTypeface(typefaceNormal);;
        tvSponsoredAdLocation1.setTypeface(typefaceNormal);;
        tvSponsoredAdTitle2.setTypeface(typefaceNormal);;
        tvSponsoredAdReviews2.setTypeface(typefaceNormal);;
        tvSponsoredAdLocation2.setTypeface(typefaceNormal);;
        tvDesc.setTypeface(typefaceNormal);;
        tvName.setTypeface(typefaceNormal);
        //tvAddress.setTypeface(typefaceNormal);
       // tvDestinations.setTypeface(typefaceNormal);
       // tvEvents.setTypeface(typefaceNormal);
       // tvModifyBike.setTypeface(typefaceNormal);
      //  tvMeetAndPlanRide.setTypeface(typefaceNormal);
       // tvHealthyRiding.setTypeface(typefaceNormal);
       // tvGetDirections.setTypeface(typefaceNormal);
        //tvNotifications.setTypeface(typefaceNormal);
        //tvSettings.setTypeface(typefaceNormal);
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

    @OnClick({R.id.ivMenu, R.id.rlProfile})
    void onclick(View view) {
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
           /* case R.id.tvNotifications:
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
            case R.id.tvHealthyRiding:
                startActivity(new Intent(activity, HealthyRidingActivity.class));
                break;

            case R.id.tvSettings:
                startActivity(new Intent(activity, SettingsActivity.class));
                break;*/
        }
    }

}
