package com.nutsuser.ridersdomain.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.adapter.AdapterNotification;
import com.nutsuser.ridersdomain.adapter.PlanRidePostAdapter;
import com.nutsuser.ridersdomain.web.pojos.PostRide;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by user on 1/6/2016.
 */
public class Notification extends BaseActivity {

    private Activity activity;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.listView)
    ListView listView;
    ArrayList<Object> taskadapterlist = new ArrayList<>();
    AdapterNotification mTaskDetailsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificationscreen);
        activity = this;
        ButterKnife.bind(this);
        setupActionBar();

        taskadapterlist=new ArrayList<>();
        // PostRide mPostRide = new PostRide("ASISTANCE REQUIRED", "", "");
        PostRide mPostRide1 = new PostRide("Riding Destination", "", "");
        PostRide mPostRide2= new PostRide("Riding Events", "", "");

        PostRide mPostRide4 = new PostRide("Message", "", "");
        PostRide mPostRide5 = new PostRide("Chat", "", "");
        PostRide mPostRide6 = new PostRide("Bike Modification", "", "");

        taskadapterlist.clear();

        taskadapterlist.add("Today");
        taskadapterlist.add(mPostRide1);
        taskadapterlist.add(mPostRide2);
        taskadapterlist.add(mPostRide4);
        taskadapterlist.add(mPostRide5);
        taskadapterlist.add(mPostRide6);
        taskadapterlist.add("Yesterday");
        taskadapterlist.add(mPostRide1);
        taskadapterlist.add(mPostRide2);
        taskadapterlist.add(mPostRide4);
        taskadapterlist.add(mPostRide5);
        taskadapterlist.add(mPostRide6);

        mTaskDetailsAdapter = new AdapterNotification(Notification.this, taskadapterlist);
        listView.setAdapter(mTaskDetailsAdapter);
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
                // finish();
                Intent intent = new Intent(Notification.this, MainScreenActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("MainScreen","OPEN");
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
