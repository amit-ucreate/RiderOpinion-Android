package com.nutsuser.ridersdomain.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.adapter.CustomGridAdapter;
import com.nutsuser.ridersdomain.utils.ApplicationGlobal;
import com.nutsuser.ridersdomain.utils.CustomizeDialog;
import com.nutsuser.ridersdomain.utils.PrefsManager;
import com.nutsuser.ridersdomain.web.api.volley.RequestJsonObject;
import com.nutsuser.ridersdomain.web.pojos.PlanRideDetails;
import com.nutsuser.ridersdomain.web.pojos.PlanRideDetailsData;

import org.json.JSONObject;

import java.lang.reflect.Type;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by user on 9/25/2015.
 */
public class EventDetailActivity extends BaseActivity {
    PlanRideDetailsData planRideDetailsData;
    String AccessToken, UserId, eventId, LOCATION;
    CustomizeDialog mCustomizeDialog;
    public static String[] prgmNameList = {"My Rides", "My Messages", "My Friends", "Chats", "Favourite Destination", "Notifications", "Settings", "    \n"};
    public static int[] prgmImages = {R.drawable.ic_menu_fav_destinations, R.drawable.ic_menu_my_messages, R.drawable.ic_menu_my_friends, R.drawable.ic_menu_menu_chats, R.drawable.ic_menu_fav_destinations, R.drawable.ic_menu_menu_notifications, R.drawable.ic_menu_menu_settings, R.drawable.ic_menu_menu_blank_icon};
    public static Class[] classList = {MyRidesRecyclerView.class, ChatListScreen.class, MyFriends.class, ChatListScreen.class, FavouriteDesination.class, Notification.class, SettingsActivity.class, SettingsActivity.class};
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tvTitleToolbar)
    TextView tvTitleToolbar;
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.tvEventName)
    TextView tvEventName;
    @Bind(R.id.tvDate)
    TextView tvDate;
    @Bind(R.id.tvTime)
    TextView tvTime;
    @Bind(R.id.tvLabelHaveJoined)
    TextView tvLabelHaveJoined;
    @Bind(R.id.tvNumberOfRiders)
    TextView tvNumberOfRiders;
    @Bind(R.id.sdvEventImage)
    SimpleDraweeView sdvEventImage;
    @Bind(R.id.tvEatables)
    TextView tvEatables;
    @Bind(R.id.tvPetrolPump)
    TextView tvPetrolPump;
    @Bind(R.id.tvFirstAid)
    TextView tvFirstAid;
    @Bind(R.id.tvServiceCenter)
    TextView tvServiceCenter;
    @Bind(R.id.lvSlidingMenu)
    LinearLayout lvSlidingMenu;
    // public static String [] prgmNameList={"Riding Destinations","Meet 'N' Plan A Ride","Riding Events \n    ","Modifly Your Bikes","Healthy Riding","Get Directions","Notifications","Settings"};
    // public static int [] prgmImages={R.drawable.icon_menu_destination,R.drawable.icon_menu_meetplan,R.drawable.icon_menu_events,R.drawable.icon_modifybike,R.drawable.icon_menu_healthy_riding,R.drawable.icon_menu_get_direction,R.drawable.icon_menu_notification,R.drawable.icon_menu_settings};
    // public static Class [] classList={DestinationsListActivity.class,PlanRideActivity.class,EventsListActivity.class,ModifyBikeActivity.class,HealthyRidingActivity.class,GetDirections.class,NotificationScreen.class,SettingsActivity.class};
    @Bind(R.id.tvName)
    TextView tvName;
    @Bind(R.id.gridView1)
    GridView gridView1;
    private Activity activity;
    @Bind(R.id.fmtrackrider)
    FrameLayout fmtrackrider;
    @Bind(R.id.tvJoin)
    TextView tvJoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        activity = this;
        ButterKnife.bind(this);
        setupActionBar();
        setFonts();
        mDrawerLayout.closeDrawer(lvSlidingMenu);
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
        eventId = getIntent().getStringExtra("eventId");
        if(isNetworkConnected()){
            RidingDetails();
        }
        else{
            showToast("Internet Not Connected");
        }

    }

    public void intentCalling(Class name) {
        Intent mIntent = new Intent(EventDetailActivity.this, name);
        startActivity(mIntent);

    }

    private void setFonts() {
        tvTitleToolbar.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
        tvName.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
        // tvAddress.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
        // tvDestinations.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
        // tvEvents.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
        // tvModifyBike.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
        // tvMeetAndPlanRide.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
        //tvHealthyRiding.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
        //tvGetDirections.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
        // tvNotifications.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
        // tvSettings.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
    }

    private void setupActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @OnClick({R.id.ivBack, R.id.ivFilter, R.id.ivMenu, R.id.rlProfile, R.id.fmtrackrider,R.id.tvJoin})
    void click(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                onBackPressed();
                break;
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
            case R.id.fmtrackrider:

                Intent intent = new Intent(activity, TrackingScreen.class);
                intent.putExtra("eventId", eventId);
                startActivity(intent);
                break;
             case R.id.tvJoin:
                 if(planRideDetailsData.getIsVehId()==1){
                     if(isNetworkConnected()){
                         JOINEVENT();
                     }
                     else{
                         showToast("Internet Not Connected");
                     }
                 }
                 else{
                     startActivity(new Intent(EventDetailActivity.this, AfterRegisterScreen.class));
                 }
                break;
            /*case R.id.tvEvents:
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
     * Match Event Details info .
     */
    public void RidingDetails() {
        showProgressDialog();
        Log.e("riding destination", "riding destination");
        try {
            prefsManager = new PrefsManager(EventDetailActivity.this);
            AccessToken = prefsManager.getToken();
            UserId = prefsManager.getCaseId();
            String radius = prefsManager.getRadius();


            //http://ridersopininon.herokuapp.com/index.php/ridingDestination?userId=75&longitude=0.000000&latitude=0.000000&accessToken=eddfbf2bf4046e90fc768d8e319a4355
            Log.e("URL: ", "" + ApplicationGlobal.ROOT + ApplicationGlobal.baseurl_eventdetails + "userId=" + UserId + "&accessToken=" + AccessToken + "&eventId=" + eventId);
            RequestQueue requestQueue = Volley.newRequestQueue(EventDetailActivity.this);
            RequestJsonObject loginTaskRequest = new RequestJsonObject(Request.Method.GET,
                    ApplicationGlobal.ROOT + ApplicationGlobal.baseurl_eventdetails + "userId=" + UserId + "&accessToken=" + AccessToken + "&eventId=" + eventId, null,
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

                Type type = new TypeToken<PlanRideDetails>() {
                }.getType();
                PlanRideDetails planARide = new Gson().fromJson(response.toString(), type);


                if (planARide.getSuccess().equals("1")) {
                    dismissProgressDialog();
                    planRideDetailsData = planARide.getData();
                    if(planRideDetailsData.getIsJoin()==1){
                        tvJoin.setText("JOINED");
                    }
                    else{
                        tvJoin.setText("JOIN");
                    }
                    tvEventName.setText(planRideDetailsData.getBaseLocation() + " to " + planRideDetailsData.getDestLocation());
                    tvTime.setText(planRideDetailsData.getStartTime());

                    tvDate.setText(planRideDetailsData.getStartDate());
                    tvNumberOfRiders.setText(planRideDetailsData.getRiders());
                    tvLabelHaveJoined.setText(planRideDetailsData.getMutual());
                    tvServiceCenter.setText(planRideDetailsData.getServiceStation());
                    tvFirstAid.setText(planRideDetailsData.getHospitals());
                    tvPetrolPump.setText(planRideDetailsData.getPetrolpumps());
                    tvEatables.setText(planRideDetailsData.getRestaurant());
                    if (planRideDetailsData.getImage() != null) {
                        String milestonesJsonInString = planRideDetailsData.getImage().toString();
                        milestonesJsonInString = milestonesJsonInString.replace("\\\"", "\"");
                        milestonesJsonInString = milestonesJsonInString.replace("\"{", "{");
                        milestonesJsonInString = milestonesJsonInString.replace("}\"", "}");
                        sdvEventImage.setImageURI(Uri.parse(milestonesJsonInString));
                    }
                } else if (planARide.getMessage().equals("Data Not Found.")) {
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

        mCustomizeDialog = new CustomizeDialog(EventDetailActivity.this);
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

    /**
     * Match Event Details info .
     */
    public void JOINEVENT() {
        showProgressDialog();
        Log.e("JOIN EVENT", "JOIN EVENT");
        try {
            prefsManager = new PrefsManager(EventDetailActivity.this);
            AccessToken = prefsManager.getToken();
            UserId = prefsManager.getCaseId();
            String radius = prefsManager.getRadius();
            //http://ridersopininon.herokuapp.com/index.php/ridingDestination?userId=75&longitude=0.000000&latitude=0.000000&accessToken=eddfbf2bf4046e90fc768d8e319a4355
            Log.e("URL: ", "" + ApplicationGlobal.ROOT + ApplicationGlobal.baseurl_joinEvent + "userId=" + UserId + "&accessToken=" + AccessToken + "&eventId=" + eventId);
            RequestQueue requestQueue = Volley.newRequestQueue(EventDetailActivity.this);
            RequestJsonObject loginTaskRequest = new RequestJsonObject(Request.Method.GET,
                    ApplicationGlobal.ROOT + ApplicationGlobal.baseurl_joinEvent + "userId=" + UserId + "&accessToken=" + AccessToken + "&eventId=" + eventId, null,
                    volleyJoinErrorListener(), volleyJoinSuccessListener()
            );

            requestQueue.add(loginTaskRequest);

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    /**
     * Implement success listener on execute api url.
     */
    public Response.Listener<JSONObject> volleyJoinSuccessListener() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("Model response:", "" + response);



            }
        };
    }

    /**
     * Implement Volley error listener here.
     */
    public Response.ErrorListener volleyJoinErrorListener() {
        dismissProgressDialog();
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error: ", "" + error);
            }
        };
    }

}
