package com.nutsuser.ridersdomain.activities;

import android.app.Activity;
import android.content.DialogInterface;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.adapter.AdapterRide;
import com.nutsuser.ridersdomain.adapter.CustomGridAdapter;
import com.nutsuser.ridersdomain.chat.SampleCometChatActivity;
import com.nutsuser.ridersdomain.utils.ApplicationGlobal;
import com.nutsuser.ridersdomain.utils.CustomizeDialog;
import com.nutsuser.ridersdomain.utils.DividerItemDecoration;
import com.nutsuser.ridersdomain.utils.PrefsManager;
import com.nutsuser.ridersdomain.utils.RecyclerItemClickListener;
import com.nutsuser.ridersdomain.web.api.volley.RequestJsonObject;
import com.nutsuser.ridersdomain.web.pojos.Data;
import com.nutsuser.ridersdomain.web.pojos.PlanARide;
import com.nutsuser.ridersdomain.web.pojos.PlanARideData;
import com.rollbar.android.Rollbar;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
    public static String[] prgmNameList = {"My Rides", "My Messages", "My Friends", "Chats", "Favorites", "Notifications", "Settings", "Riders Near By"};
    public static int[] prgmImages = {R.drawable.ic_menu_my_rides, R.drawable.ic_menu_my_messages, R.drawable.menu_my_friends, R.drawable.ic_menu_menu_chats, R.drawable.menu_fav_destinations, R.drawable.ic_menu_menu_notifications, R.drawable.menu_settings, R.drawable.icon_nearby};
    public static Class[] classList = {MyRidesRecyclerView.class, RecentChatListActivity.class,MyFriendsActivity.class, ComingSoon.class, FavouriteDesination.class, NotificationListActivity.class, SettingsActivity.class, NearByFriendsAcitivity.class};
    PrefsManager prefsManager;
    CustomizeDialog mCustomizeDialog;
    String AccessToken, UserId;
    ArrayList<PlanARideData> planARideDatas = new ArrayList<>();
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
    String ridetype, startdate, enddate,endtime, starttime, fromlatitude, fromlongitude, fromlocation, tolatitude, tolongitude, tolocation, hlatitude, hlongitude, htype, hlocation, hlatitude1, hlongitude1, htype1, hlocation1;
    private AdapterRide adapterRide;
    private Activity activity;
    @Bind(R.id.tvTitleToolbar)
    TextView tvTitleToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planned_ridings);
        try {
            activity = this;
            ButterKnife.bind(this);
            setupActionBar(toolbar);
            setFonts();
            ridetype = getIntent().getStringExtra("rideType");
            if (ridetype.matches("Breakfast Ride")) {
                ridetype = "breakfast";
                startdate = getIntent().getStringExtra("startdate");
                Log.e("starttime", startdate);
                starttime = getIntent().getStringExtra("starttime");
                fromlatitude = getIntent().getStringExtra("fromlatitude");
                fromlongitude = getIntent().getStringExtra("fromlongitude");
                fromlocation = getIntent().getStringExtra("fromlocation");
                tolatitude = getIntent().getStringExtra("tolatitude");
                tolongitude = getIntent().getStringExtra("tolongitude");
                tolocation = getIntent().getStringExtra("tolocation");
                hlatitude = getIntent().getStringExtra("hlatitude");
                hlongitude = getIntent().getStringExtra("hlongitude");
                hlocation = getIntent().getStringExtra("hlocation");
                htype = getIntent().getStringExtra("breakfast");
                planARideDatas = PlanRideActivity.planARideDatas;
                Log.e("planARideDatas", ":::" + planARideDatas.size());
            } else {
                ridetype = "overnight";
                startdate = getIntent().getStringExtra("startdate");
                starttime = getIntent().getStringExtra("starttime");
                fromlatitude = getIntent().getStringExtra("fromlatitude");
                fromlongitude = getIntent().getStringExtra("fromlongitude");
                fromlocation = getIntent().getStringExtra("fromlocation");
                enddate = getIntent().getStringExtra("enddate");
                endtime = getIntent().getStringExtra("endtime");
                tolatitude = getIntent().getStringExtra("tolatitude");
                tolongitude = getIntent().getStringExtra("tolongitude");
                tolocation = getIntent().getStringExtra("tolocation");
                hlatitude = getIntent().getStringExtra("hlatitude");
                hlongitude = getIntent().getStringExtra("hlongitude");
                hlocation = getIntent().getStringExtra("hlocation");
                htype = getIntent().getStringExtra("htype");
                hlatitude1 = getIntent().getStringExtra("hlatitude1");
                hlongitude1 = getIntent().getStringExtra("hlongitude1");
                hlocation1 = getIntent().getStringExtra("hlocation1");
                htype1 = getIntent().getStringExtra("htype1");
                Log.e("endtime:", "" + endtime);
            }
            tvPlaces.setText(fromlocation + "  -  " + tolocation);
            String timein12Format = "";
            try {
                final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
                final Date dateObj = sdf.parse(starttime);
                timein12Format = new SimpleDateFormat("K:mm a").format(dateObj);
            } catch (final ParseException e) {
                e.printStackTrace();
            }

            tvDateAndTime.setText(startdate + "/" + timein12Format);
            Log.e("fromlatitude:", "" + fromlatitude);
            Log.e("fromlongitude:", "" + fromlongitude);
            Log.e("tolatitude:", "" + tolatitude);
            Log.e("tolongitude:", "" + tolongitude);
            Log.e("starttime:", "" + timein12Format);


            rvRides.setLayoutManager(new LinearLayoutManager(this));
            rvRides.addItemDecoration(new DividerItemDecoration(this, R.drawable.divider));

            rvRides.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    // Intent mIntent = new Intent(PlannedRidingsActivity.this, PlanRideDetailActivity.class);

                    if (ridetype.matches("breakfast")) {
                        Intent mIntent = new Intent(PlannedRidingsActivity.this, PlanRideDetailActivity.class);
                        mIntent.putExtra("rideType", ridetype);
                        mIntent.putExtra("LocationSet", "SEE");
                        mIntent.putExtra("startdate", startdate);
                        mIntent.putExtra("starttime", starttime);
                        mIntent.putExtra("fromlatitude", fromlatitude);
                        mIntent.putExtra("fromlongitude", fromlongitude);
                        mIntent.putExtra("fromlocation", fromlocation);
                        mIntent.putExtra("tolatitude", tolatitude);
                        mIntent.putExtra("tolongitude", tolongitude);
                        mIntent.putExtra("tolocation", tolocation);
                        mIntent.putExtra("hlatitude", hlatitude);
                        mIntent.putExtra("hlongitude", hlongitude);
                        mIntent.putExtra("hlocation", hlocation);
                        mIntent.putExtra("htype", "breakfast");
                        mIntent.putExtra("LOCATION", "details");
                        mIntent.putExtra("eventId", planARideDatas.get(position).getEventId());
                        startActivity(mIntent);
                    } else {
                        Intent mIntent = new Intent(PlannedRidingsActivity.this, PlanRideDetailActivity.class);
                        mIntent.putExtra("rideType", ridetype);
                        mIntent.putExtra("startdate", startdate);
                        mIntent.putExtra("starttime", starttime);
                        mIntent.putExtra("enddate", enddate);
                        mIntent.putExtra("endtime", endtime);
                        mIntent.putExtra("fromlatitude", fromlatitude);
                        mIntent.putExtra("fromlongitude", fromlongitude);
                        mIntent.putExtra("fromlocation", fromlocation);
                        mIntent.putExtra("tolatitude", tolatitude);
                        mIntent.putExtra("tolongitude", tolongitude);
                        mIntent.putExtra("tolocation", tolocation);
                        mIntent.putExtra("hlatitude", hlatitude);
                        mIntent.putExtra("hlongitude", hlongitude);
                        mIntent.putExtra("hlocation", hlocation);
                        mIntent.putExtra("htype", "breakfast");
                        mIntent.putExtra("hlatitude1", hlatitude1);
                        mIntent.putExtra("hlongitude1", hlongitude1);
                        mIntent.putExtra("hlocation1", hlocation1);
                        mIntent.putExtra("htype1", "Overnight");
                        mIntent.putExtra("LOCATION", "details");
                        mIntent.putExtra("eventId", planARideDatas.get(position).getEventId());
                        startActivity(mIntent);
                    }
                    //startActivity(mIntent);
                    //startActivity(new Intent(PlannedRidingsActivity.this, PlanRideDetailActivity.class));
                }
            }));

            gridView1.setAdapter(new CustomGridAdapter(this, prgmNameList, prgmImages));
            gridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (position == 1) {
//
//                    intent_Calling(classList[position], "My Messages");
//                }
//                else {
//                    intentCalling(classList[position]);
//
//                }
                    Log.e("positiuon:", "" + classList[position]);
                    intentCalling(classList[position]);

                }
            });
            //MatchRidinginfo();
            setMatchRideEventList();
        }catch(Exception e){
            Rollbar.reportException(e, "minor", "PlannedRidingActivity on create");
        }
    }
    //===================== function to move activity to activity===========//
    public void intent_Calling(Class name, String na) {
        Intent mIntent = new Intent(PlannedRidingsActivity.this, name);
        mIntent.putExtra(SCREEN_OPEN, na);
        startActivity(mIntent);

    }
    public void intentCalling(Class name) {
        Intent mIntent = new Intent(PlannedRidingsActivity.this, name);
        startActivity(mIntent);

    }

    private void setFonts() {
        Typeface typefaceNormal = Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF");
        Typeface typefacehead = Typeface.createFromAsset(getResources().getAssets(), "fonts/Lato-Bold.ttf");
        tvTitleToolbar.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/MACHINEN.TTF"));
        tvPlaces.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/Lato-Bold.ttf"));
        tvDateAndTime.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/LATO-REGULAR.TTF"));
        tvRidesAlreadyPlanned.setTypeface(typefacehead);
        tvSubmit.setTypeface(typefaceNormal);
        tvName.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/MACHINEN.TTF"));
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



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                new android.support.v7.app.AlertDialog.Builder(PlannedRidingsActivity.this).setTitle("Message")
                        .setMessage("Are you sure you want to discard Ride Plan?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // mGoogleApiClient.disconnect();
                                finish();
                                dialog.dismiss();

                            }
                        }).show();
               // finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick({ R.id.rlProfile, R.id.tvSubmit})
    void click(View view) {
        switch (view.getId()) {

//            case R.id.ivMenu:
//                if (mDrawerLayout.isDrawerOpen(lvSlidingMenu))
//                    mDrawerLayout.closeDrawer(lvSlidingMenu);
//                else
//                    mDrawerLayout.openDrawer(lvSlidingMenu);
//                break;
            case R.id.rlProfile:
               // startActivity(new Intent(activity, ProfileActivity.class));
                break;
            case R.id.tvSubmit:
                if (ridetype.matches("breakfast")) {
                    Intent mIntent = new Intent(PlannedRidingsActivity.this, PlanARidePostRide.class);
                    mIntent.putExtra("rideType", ridetype);
                    mIntent.putExtra("startdate", startdate);
                    mIntent.putExtra("starttime", starttime);
                    mIntent.putExtra("fromlatitude", fromlatitude);
                    mIntent.putExtra("fromlongitude", fromlongitude);
                    mIntent.putExtra("fromlocation", fromlocation);
                    mIntent.putExtra("tolatitude", tolatitude);
                    mIntent.putExtra("tolongitude", tolongitude);
                    mIntent.putExtra("tolocation", tolocation);
                    mIntent.putExtra("hlatitude", hlatitude);
                    mIntent.putExtra("hlongitude", hlongitude);
                    mIntent.putExtra("hlocation", hlocation);
                    mIntent.putExtra("htype", "breakfast");
                    startActivity(mIntent);
                } else {
                    Intent mIntent = new Intent(PlannedRidingsActivity.this, PlanARidePostRide.class);
                    mIntent.putExtra("rideType", ridetype);
                    mIntent.putExtra("startdate", startdate);
                    mIntent.putExtra("starttime", starttime);
                    mIntent.putExtra("enddate", enddate);
                    mIntent.putExtra("endtime", endtime);
                    mIntent.putExtra("fromlatitude", fromlatitude);
                    mIntent.putExtra("fromlongitude", fromlongitude);
                    mIntent.putExtra("fromlocation", fromlocation);
                    mIntent.putExtra("tolatitude", tolatitude);
                    mIntent.putExtra("tolongitude", tolongitude);
                    mIntent.putExtra("tolocation", tolocation);
                    mIntent.putExtra("hlatitude", hlatitude);
                    mIntent.putExtra("hlongitude", hlongitude);
                    mIntent.putExtra("hlocation", hlocation);
                    mIntent.putExtra("htype", "breakfast");
                    mIntent.putExtra("hlatitude1", hlatitude1);
                    mIntent.putExtra("hlongitude1", hlongitude1);
                    mIntent.putExtra("hlocation1", hlocation1);
                    mIntent.putExtra("htype1", "Overnight");
                    startActivity(mIntent);
                }
                break;
        }
    }


    //================= matchRidingEventListSet=============//
    private void setMatchRideEventList(){
        adapterRide = new AdapterRide(activity, planARideDatas);
        rvRides.setAdapter(adapterRide);
    }

//    /**
//     * Match Riding List info .
//     */
//    public void MatchRidinginfo() {
//        showProgressDialog();
//        Log.e("riding destination", "riding destination");
//        try {
//            prefsManager = new PrefsManager(PlannedRidingsActivity.this);
//            AccessToken = prefsManager.getToken();
//            UserId = prefsManager.getCaseId();
//            String radius = prefsManager.getRadius();
//
//
//            //http://ridersopininon.herokuapp.com/index.php/ridingDestination?userId=75&longitude=0.000000&latitude=0.000000&accessToken=eddfbf2bf4046e90fc768d8e319a4355
//            Log.e("URL: ", "" + ApplicationGlobal.ROOT + ApplicationGlobal.baseurl_matchevent + "userId=" + UserId + "&baseLat=" + fromlatitude + "&baseLon=" + fromlongitude + "&destLat=" + tolatitude + "&destLon=" + tolongitude + "&accessToken=" + AccessToken + "&eventType=" + ridetype);
//            RequestQueue requestQueue = Volley.newRequestQueue(PlannedRidingsActivity.this);
//            RequestJsonObject loginTaskRequest = new RequestJsonObject(Request.Method.GET,
//                    ApplicationGlobal.ROOT + ApplicationGlobal.baseurl_matchevent + "userId=" + UserId + "&baseLat=" + fromlatitude + "&baseLon=" + fromlongitude + "&destLat=" + tolatitude + "&destLon=" + tolongitude + "&accessToken=" + AccessToken + "&eventType=" + ridetype, null,
//                    volleyModelErrorListener(), volleyModelSuccessListener()
//            );
//
//            requestQueue.add(loginTaskRequest);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//
//        }
//    }
//
//    /**
//     * Implement success listener on execute api url.
//     */
//    public Response.Listener<JSONObject> volleyModelSuccessListener() {
//        return new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                Log.e("Model response:", "" + response);
//
//                Type type = new TypeToken<PlanARide>() {
//                }.getType();
//                PlanARide planARide = new Gson().fromJson(response.toString(), type);
//
//                planARideDatas.clear();
//                if (planARide.getSuccess().equals("1")) {
//                    dismissProgressDialog();
//                    planARideDatas.addAll(planARide.getData());
//                    tvRidesAlreadyPlanned.setText(planARideDatas.size() + " OTHER RIDES ALSO PLANNED");
//                    adapterRide = new AdapterRide(activity, planARideDatas);
//                    rvRides.setAdapter(adapterRide);
//                } else {
//                    dismissProgressDialog();
//                    if (ridetype.matches("breakfast")) {
//                        Intent mIntent = new Intent(PlannedRidingsActivity.this, PlanARidePostRide.class);
//                        mIntent.putExtra("rideType", ridetype);
//                        mIntent.putExtra("startdate", startdate);
//                        mIntent.putExtra("starttime", starttime);
//                        mIntent.putExtra("fromlatitude", fromlatitude);
//                        mIntent.putExtra("fromlongitude", fromlongitude);
//                        mIntent.putExtra("fromlocation", fromlocation);
//                        mIntent.putExtra("tolatitude", tolatitude);
//                        mIntent.putExtra("tolongitude", tolongitude);
//                        mIntent.putExtra("tolocation", tolocation);
//                        mIntent.putExtra("hlatitude", hlatitude);
//                        mIntent.putExtra("hlongitude", hlongitude);
//                        mIntent.putExtra("hlocation", hlocation);
//                        mIntent.putExtra("htype", "breakfast");
//                        startActivity(mIntent);
//                        finish();
//                    } else {
//                        Intent mIntent = new Intent(PlannedRidingsActivity.this, PlanARidePostRide.class);
//                        mIntent.putExtra("rideType", ridetype);
//                        mIntent.putExtra("startdate", startdate);
//                        mIntent.putExtra("starttime", starttime);
//                        mIntent.putExtra("enddate", enddate);
//                        mIntent.putExtra("fromlatitude", fromlatitude);
//                        mIntent.putExtra("fromlongitude", fromlongitude);
//                        mIntent.putExtra("fromlocation", fromlocation);
//                        mIntent.putExtra("tolatitude", tolatitude);
//                        mIntent.putExtra("tolongitude", tolongitude);
//                        mIntent.putExtra("tolocation", tolocation);
//                        mIntent.putExtra("hlatitude", hlatitude);
//                        mIntent.putExtra("hlongitude", hlongitude);
//                        mIntent.putExtra("hlocation", hlocation);
//                        mIntent.putExtra("htype", "breakfast");
//                        mIntent.putExtra("hlatitude1", hlatitude1);
//                        mIntent.putExtra("hlongitude1", hlongitude1);
//                        mIntent.putExtra("hlocation1", hlocation1);
//                        mIntent.putExtra("htype1", "Overnight");
//                        startActivity(mIntent);
//                        finish();
//                    }
//                    //CustomDialog.showProgressDialog(PlannedRidingsActivity.this, planARide.getMessage().toString());
//                }
//
//            }
//        };
//    }
//
//    /**
//     * Implement Volley error listener here.
//     */
//    public Response.ErrorListener volleyModelErrorListener() {
//        dismissProgressDialog();
//        return new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e("error: ", "" + error);
//            }
//        };
//    }



}
