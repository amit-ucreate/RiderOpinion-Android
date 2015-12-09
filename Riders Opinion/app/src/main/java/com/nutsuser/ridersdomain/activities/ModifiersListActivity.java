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
import android.widget.ListView;
import android.widget.TextView;

import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.adapter.AdapterModifier;
import com.nutsuser.ridersdomain.adapter.CustomGridAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by user on 9/30/2015.
 */
public class ModifiersListActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.lvDealers)
    ListView lvDealers;
    @Bind(R.id.tvTitleToolbar)
    TextView tvTitleToolbar;
    @Bind(R.id.tvBikeName)
    TextView tvBikeName;
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
    private AdapterModifier adapterModifier;
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    private Activity activity;
    @Bind(R.id.lvSlidingMenu)
    LinearLayout lvSlidingMenu;

    @Bind(R.id.gridView1)
    GridView gridView1;
    public static String [] prgmNameList={"Riding Destinations","Meet 'N' Plan A Ride","Riding Events \n    ","Modifly Your Bikes","Healthy Riding","Get Directions","Notifications","Settings"};
    public static int [] prgmImages={R.drawable.icon_menu_destination,R.drawable.icon_menu_meetplan,R.drawable.icon_menu_events,R.drawable.icon_modifybike,R.drawable.icon_menu_healthy_riding,R.drawable.icon_menu_get_direction,R.drawable.icon_menu_notification,R.drawable.icon_menu_settings};
    public static Class [] classList={DestinationsListActivity.class,PlanRideActivity.class,EventsListActivity.class,ModifyBikeActivity.class,HealthyRidingActivity.class,GetDirections.class,NotificationScreen.class,SettingsActivity.class};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifiers_list);
        activity = this;
        ButterKnife.bind(this);
        setupActionBar();
        setFontsToViews();
        adapterModifier = new AdapterModifier(this);
        lvDealers.setAdapter(adapterModifier);
        lvDealers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(ModifiersListActivity.this, ModifierDetailActivity.class));
            }
        });
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
        Intent mIntent=new Intent(ModifiersListActivity.this,name);
        startActivity(mIntent);

    }
    private void setFontsToViews() {
        Typeface typefaceNormal = Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF");
        tvBikeName.setTypeface(typefaceNormal);
        tvTitleToolbar.setTypeface(typefaceNormal);
        tvSponsoredAdTitle1.setTypeface(typefaceNormal);
        tvSponsoredAdTitle2.setTypeface(typefaceNormal);
        tvSponsoredAdLocation1.setTypeface(typefaceNormal);
        tvSponsoredAdLocation2.setTypeface(typefaceNormal);
        tvSponsoredAdReviews1.setTypeface(typefaceNormal);
        tvSponsoredAdReviews2.setTypeface(typefaceNormal);
        tvName.setTypeface(typefaceNormal);
       // tvAddress.setTypeface(typefaceNormal);
       // tvDestinations.setTypeface(typefaceNormal);
       // tvEvents.setTypeface(typefaceNormal);
        //tvModifyBike.setTypeface(typefaceNormal);
       // tvMeetAndPlanRide.setTypeface(typefaceNormal);
       // //tvHealthyRiding.setTypeface(typefaceNormal);
        //tvGetDirections.setTypeface(typefaceNormal);
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
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.ivMenu, R.id.ivFilter, R.id.rlProfile})
    void click(View view){
        switch (view.getId()) {
            case R.id.ivFilter:
                startActivity(new Intent(activity, FilterActivity.class));
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
        /*    case R.id.tvNotifications:
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
