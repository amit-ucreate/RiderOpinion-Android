package com.nutsuser.ridersdomain.activities;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.TextView;

import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.adapter.PlanRidePostAdapter;
import com.nutsuser.ridersdomain.web.pojos.PostRide;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by user on 1/6/2016.
 */
public class PlanARidePostRide extends BaseActivity{
    private Activity activity;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tvTitleToolbar)
    TextView tvTitleToolbar;
    @Bind(R.id.listView)
    ListView listView;
    ArrayList<Object> taskadapterlist = new ArrayList<>();
    //ArrayList<PostRide> mTaskDocumentses=new ArrayList<>();

    PlanRidePostAdapter mTaskDetailsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planaridepostride);
        activity = this;
        ButterKnife.bind(this);
        setupActionBar();
        setFonts();
        taskadapterlist=new ArrayList<>();
       // PostRide mPostRide = new PostRide("ASISTANCE REQUIRED", "", "");
        PostRide mPostRide1 = new PostRide("PLAN THIS RIDE", "", "");
        PostRide mPostRide2= new PostRide("HOTEL/RESTAURANT BOOKING", "", "");
        //PostRide mPostRide3 = new PostRide("WHO CAN JOIN", "", "");
        PostRide mPostRide4 = new PostRide("ANY BIKER", "", "");
        PostRide mPostRide5 = new PostRide("HARLEY", "", "");
        PostRide mPostRide6 = new PostRide("DUCATI", "", "");
        PostRide mPostRide7 = new PostRide("TRIUMPH", "", "");
        taskadapterlist.clear();

        taskadapterlist.add("ASISTANCE REQUIRED");
        taskadapterlist.add(mPostRide1);
        taskadapterlist.add(mPostRide2);
        taskadapterlist.add("WHO CAN JOIN");
        taskadapterlist.add(mPostRide4);
        taskadapterlist.add(mPostRide5);
        taskadapterlist.add(mPostRide6);
        taskadapterlist.add(mPostRide7);
        mTaskDetailsAdapter = new PlanRidePostAdapter(PlanARidePostRide.this, taskadapterlist);
        listView.setAdapter(mTaskDetailsAdapter);


    }
    private void setupActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_back);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
    private void setFonts() {
        tvTitleToolbar.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
        //tvName.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
        //tvAddress.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
        //tvDestinations.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
        // tvEvents.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
        //tvModifyBike.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
        // tvMeetAndPlanRide.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
        // tvHealthyRiding.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
        // tvGetDirections.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
        // tvNotifications.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
        // tvSettings.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
    }
}
