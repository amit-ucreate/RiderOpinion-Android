package com.nutsuser.ridersdomain.activities;

import android.app.Activity;
import android.content.Intent;
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
 * Created by user on 12/9/2015.
 */
public class NotificationScreen extends BaseActivity {


    @Bind(R.id.toolbar)
    Toolbar toolbar;
    private Activity activity;
    @Bind(R.id.tvTitleToolbar)
    TextView tvTitleToolbar;
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.lvSlidingMenu)
    LinearLayout lvSlidingMenu;
    @Bind(R.id.gridView1)
    GridView gridView1;
    @Bind(R.id.grid_View)
    GridView grid_View;

    //public static String [] prgmNameList={"Riding Destinations","Meet 'N' Plan A Ride","Riding Events \n    ","Modifly Your Bikes","Healthy Riding","Get Directions","Notifications","Settings"};
    //public static int [] prgmImages={R.drawable.icon_menu_destination,R.drawable.icon_menu_meetplan,R.drawable.icon_menu_events,R.drawable.icon_modifybike,R.drawable.icon_menu_healthy_riding,R.drawable.icon_menu_get_direction,R.drawable.icon_menu_notification,R.drawable.icon_menu_settings};
   // public static Class [] classList={DestinationsListActivity.class,PlanRideActivity.class,EventsListActivity.class,ModifyBikeActivity.class,HealthyRidingActivity.class,GetDirections.class,NotificationScreen.class,SettingsActivity.class};
    public static String [] prgmNameList={"My Rides","My Messages","My Friends","Chats","Favourite Desination","Notifications","Settings","    \n"};
    public static int [] prgmImages={R.drawable.icon_menu_destination,R.drawable.icon_menu_meetplan,R.drawable.icon_menu_events,R.drawable.icon_modifybike,R.drawable.icon_menu_healthy_riding,R.drawable.icon_menu_notification,R.drawable.icon_modifybike,R.drawable.icon_menu_destination};
    public static Class [] classList={DestinationsListActivity.class,PlanRideActivity.class,EventsListActivity.class,ModifyBikeActivity.class,HealthyRidingActivity.class,GetDirections.class,NotificationScreen.class,SettingsActivity.class};


    public static String [] prgm_Name={"Chat","Riders Alert"};
    public static int [] prgm_Images={R.drawable.icon_menu_destination,R.drawable.icon_menu_meetplan};
    public static Class [] class_List={ChatListScreen.class,ChatListScreen.class};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        activity = NotificationScreen.this;
        ButterKnife.bind(activity);
        setupActionBar();
        mDrawerLayout.closeDrawer(lvSlidingMenu);
        gridView1.setAdapter(new CustomGridAdapter(this, prgmNameList, prgmImages));
        gridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               // Log.e("positiuon:", "" + classList[position]);
               // intentCalling(classList[position]);
            }
        });

        grid_View.setAdapter(new CustomGridAdapter(this, prgm_Name, prgm_Images));
        grid_View.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("position:", "" + classList[position]);
                intentCalling(class_List[position]);
            }
        });
    }
    public void intentCalling(Class name){
        Intent mIntent=new Intent(NotificationScreen.this,name);
        startActivity(mIntent);

    }
    private void setupActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_home);
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
    @OnClick({R.id.ivFilter, R.id.ivMenu, R.id.rlProfile})
    void onclick(View view) {
        switch (view.getId()) {
            case R.id.ivFilter:
                startActivity(new Intent(NotificationScreen.this, FilterActivity.class));
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
