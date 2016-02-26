package com.nutsuser.ridersdomain.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.adapter.AdapterDestination;
import com.nutsuser.ridersdomain.adapter.AdapterRide;
import com.nutsuser.ridersdomain.adapter.CustomGridAdapter;
import com.nutsuser.ridersdomain.utils.ApplicationGlobal;
import com.nutsuser.ridersdomain.utils.CustomizeDialog;
import com.nutsuser.ridersdomain.utils.DividerItemDecoration;
import com.nutsuser.ridersdomain.utils.PrefsManager;
import com.nutsuser.ridersdomain.utils.RecyclerItemClickListener;
import com.nutsuser.ridersdomain.web.api.volley.RequestJsonObject;
import com.nutsuser.ridersdomain.web.pojos.PlanARide;
import com.nutsuser.ridersdomain.web.pojos.PlanARideData;
import com.nutsuser.ridersdomain.web.pojos.RidingDestination;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by user on 10/1/2015.
 */
public class PlannedRidingsActivity extends BaseActivity {

    PrefsManager prefsManager;
    CustomizeDialog mCustomizeDialog;
    String AccessToken, UserId;
    ArrayList<PlanARideData> planARideDatas=new ArrayList<>();

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
            ridetype="breakfast";
            startdate=getIntent().getStringExtra("startdate");
            starttime=getIntent().getStringExtra("starttime");
            fromlatitude=getIntent().getStringExtra("fromlatitude");
            fromlongitude=getIntent().getStringExtra("fromlongitude");
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
            ridetype="overnight";
            startdate=getIntent().getStringExtra("startdate");
            starttime=getIntent().getStringExtra("starttime");
            fromlatitude=getIntent().getStringExtra("fromlatitude");
            fromlongitude=getIntent().getStringExtra("fromlongitude");
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
        tvPlaces.setText(fromlocation + "  -  " + tolocation);
        tvDateAndTime.setText(startdate + "/" + starttime);
        Log.e("fromlatitude:", "" + fromlatitude);
        Log.e("fromlongitude:", "" + fromlongitude);
        Log.e("tolatitude:", "" + tolatitude);
        Log.e("tolongitude:", "" + tolongitude);


        rvRides.setLayoutManager(new LinearLayoutManager(this));
        rvRides.addItemDecoration(new DividerItemDecoration(this, R.drawable.divider));

        rvRides.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent mIntent=new Intent(PlannedRidingsActivity.this,PlanRideDetailActivity.class);
                mIntent.putExtra("LOCATION","details");
                mIntent.putExtra("eventId",planARideDatas.get(position).getEventId());
                startActivity(mIntent);
                //startActivity(new Intent(PlannedRidingsActivity.this, PlanRideDetailActivity.class));
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
        MatchRidinginfo();
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
                if(ridetype.matches("breakfast")){
                    Intent mIntent=new Intent(PlannedRidingsActivity.this, PlanARidePostRide.class);
                    mIntent.putExtra("rideType",ridetype);
                    mIntent.putExtra("startdate",startdate);
                    mIntent.putExtra("starttime",starttime);
                    mIntent.putExtra("fromlatitude",fromlatitude);
                    mIntent.putExtra("fromlongitude",fromlongitude);
                    mIntent.putExtra("fromlocation",fromlocation);
                    mIntent.putExtra("tolatitude",tolatitude);
                    mIntent.putExtra("tolongitude",tolongitude);
                    mIntent.putExtra("tolocation",tolocation);
                    mIntent.putExtra("hlatitude",hlatitude);
                    mIntent.putExtra("hlongitude",hlongitude);
                    mIntent.putExtra("hlocation",hlocation);
                    mIntent.putExtra("htype","breakfast");
                    startActivity(mIntent);
                }
                else{
                    Intent mIntent=new Intent(PlannedRidingsActivity.this, PlanARidePostRide.class);
                    mIntent.putExtra("rideType",ridetype);
                    mIntent.putExtra("startdate",startdate);
                    mIntent.putExtra("starttime",starttime);
                    mIntent.putExtra("enddate",enddate);
                    mIntent.putExtra("fromlatitude",fromlatitude);
                    mIntent.putExtra("fromlongitude",fromlongitude);
                    mIntent.putExtra("fromlocation",fromlocation);
                    mIntent.putExtra("tolatitude",tolatitude);
                    mIntent.putExtra("tolongitude",tolongitude);
                    mIntent.putExtra("tolocation",tolocation);
                    mIntent.putExtra("hlatitude",hlatitude);
                    mIntent.putExtra("hlongitude",hlongitude);
                    mIntent.putExtra("hlocation",hlocation);
                    mIntent.putExtra("htype","breakfast");
                    mIntent.putExtra("hlatitude1",hlatitude1);
                    mIntent.putExtra("hlongitude1",hlongitude1);
                    mIntent.putExtra("hlocation1",hlocation1);
                    mIntent.putExtra("htype1","Overnight");
                    startActivity(mIntent);
                }

               // startActivity(new Intent(activity, PlanARidePostRide.class));
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
    /**
     * Match Riding List info .
     */
    public void MatchRidinginfo() {
        showProgressDialog();
        Log.e("riding destination", "riding destination");
        try {
            prefsManager = new PrefsManager(PlannedRidingsActivity.this);
            AccessToken = prefsManager.getToken();
            UserId = prefsManager.getCaseId();
            String radius=prefsManager.getRadius();



            //http://ridersopininon.herokuapp.com/index.php/ridingDestination?userId=75&longitude=0.000000&latitude=0.000000&accessToken=eddfbf2bf4046e90fc768d8e319a4355
            Log.e("URL: ", "" + ApplicationGlobal.ROOT + ApplicationGlobal.baseurl_matchevent + "userId=" + UserId + "&baseLat=" + fromlatitude + "&baseLon=" + fromlongitude + "&destLat="+tolatitude+"&destLon=" + tolongitude+"&accessToken="+AccessToken+"&eventType="+ridetype);
            RequestQueue requestQueue = Volley.newRequestQueue(PlannedRidingsActivity.this);
            RequestJsonObject loginTaskRequest = new RequestJsonObject(Request.Method.GET,
                    ApplicationGlobal.ROOT + ApplicationGlobal.baseurl_matchevent + "userId=" + UserId + "&baseLat=" + fromlatitude + "&baseLon=" + fromlongitude + "&destLat="+tolatitude+"&destLon=" + tolongitude+"&accessToken="+AccessToken+"&eventType="+ridetype, null,
                    volleyModelErrorListener(), volleyModelSuccessListener()
            );

            requestQueue.add(loginTaskRequest);

        } catch (Exception e) {
            e.printStackTrace();

        }
    }
    /**
     * Implement success listener on execute api url.
     */
    public Response.Listener<JSONObject> volleyModelSuccessListener() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("Model response:", "" + response);

               Type type = new TypeToken<PlanARide>() {
                }.getType();
                PlanARide planARide = new Gson().fromJson(response.toString(), type);

                planARideDatas.clear();
                if (planARide.getSuccess().equals("1")) {
                    dismissProgressDialog();
                    planARideDatas.addAll(planARide.getData());
                    adapterRide = new AdapterRide(activity,planARideDatas);
                    rvRides.setAdapter(adapterRide);
                }
                else if(planARide.getMessage().equals("Data Not Found.")){
                    dismissProgressDialog();
                    showToast("Data Not Found.");
                }

            }
        };
    }

    /**
     * Implement Volley error listener here.
     */
    public Response.ErrorListener volleyModelErrorListener() {
        dismissProgressDialog();
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error: ", "" + error);
            }
        };
    }

    public void showProgressDialog() {

        mCustomizeDialog = new CustomizeDialog(PlannedRidingsActivity.this);
        mCustomizeDialog.setCancelable(false);
        mCustomizeDialog.show();
        Log.e("HERE", "HERE");
    }

    public void dismissProgressDialog() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mCustomizeDialog != null && mCustomizeDialog.isShowing()) {
                    mCustomizeDialog.dismiss();
                    mCustomizeDialog = null;
                }
            }
        }, 1000);

    }

}
