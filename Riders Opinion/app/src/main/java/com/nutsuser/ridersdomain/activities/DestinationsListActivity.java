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
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import com.google.gson.reflect.TypeToken;
import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.adapter.AdapterDestination;
import com.nutsuser.ridersdomain.adapter.CustomGridAdapter;
import com.nutsuser.ridersdomain.services.GPSService;
import com.nutsuser.ridersdomain.utils.ApplicationGlobal;
import com.nutsuser.ridersdomain.utils.CustomizeDialog;
import com.nutsuser.ridersdomain.utils.PrefsManager;
import com.nutsuser.ridersdomain.utils.RecyclerItemClickListener;
import com.nutsuser.ridersdomain.web.api.RestClient;
import com.nutsuser.ridersdomain.web.api.volley.RequestJsonObject;
import com.nutsuser.ridersdomain.web.pojos.RidingDestination;
import com.nutsuser.ridersdomain.web.pojos.RidingDestinationDetails;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;

/**
 * Created by user on 8/28/2015.
 */
public class DestinationsListActivity extends BaseActivity {

    public static String[] prgmNameList = {"My Rides", "My Messages", "My Friends", "Chats", "Favourite Destination", "Notifications", "Settings", "    \n"};
    public static int[] prgmImages = {R.drawable.ic_menu_fav_destinations, R.drawable.ic_menu_my_messages, R.drawable.ic_menu_my_friends, R.drawable.ic_menu_menu_chats, R.drawable.ic_menu_fav_destinations, R.drawable.ic_menu_menu_notifications, R.drawable.ic_menu_menu_settings, R.drawable.ic_menu_menu_blank_icon};
    public static Class[] classList = {MyRidesRecyclerView.class, ChatListScreen.class, MyFriends.class, ChatListScreen.class, FavouriteDesination.class, Notification.class, SettingsActivity.class, SettingsActivity.class};
    PrefsManager prefsManager;
    CustomizeDialog mCustomizeDialog;
    String AccessToken, UserId;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
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
    @Bind(R.id.edSearch)
    EditText edSearch;
    //public static String [] prgmNameList={"Riding Destinations","Meet 'N' Plan A Ride","Riding Events \n    ","Modifly Your Bikes","Healthy Riding","Get Directions","Notifications","Settings"};
    //public static int [] prgmImages={R.drawable.icon_menu_destination,R.drawable.icon_menu_meetplan,R.drawable.icon_menu_events,R.drawable.icon_modifybike,R.drawable.icon_menu_healthy_riding,R.drawable.icon_menu_get_direction,R.drawable.icon_menu_notification,R.drawable.icon_menu_settings};
    //public static Class [] classList={DestinationsListActivity.class,PlanRideActivity.class,EventsListActivity.class,ModifyBikeActivity.class,HealthyRidingActivity.class,GetDirections.class,NotificationScreen.class,SettingsActivity.class};
    double start1, end1;
    String star_lat, star_long;
    public static  ArrayList<RidingDestinationDetails> mRidingDestinationDetailses = new ArrayList<RidingDestinationDetails>();
    private Activity activity;

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

        rvDestinations.addOnItemTouchListener(new RecyclerItemClickListener(activity, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String string = mRidingDestinationDetailses.get(position).getDestId();
                Intent mIntent = new Intent(activity, DestinationsDetailActivity.class);
                mIntent.putExtra("DestID", string);
                startActivity(mIntent);
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
        GPSService mGPSService = new GPSService(this);
        mGPSService.getLocation();

        if (mGPSService.isLocationAvailable == false) {

            // Here you can ask the user to try again, using return; for that
            Toast.makeText(getApplicationContext(), "Your location is not available, please try again.", Toast.LENGTH_SHORT).show();
            return;

            // Or you can continue without getting the location, remove the return; above and uncomment the line given below
            // address = "Location not available";
        } else {

            // Getting location co-ordinates
            double latitude = mGPSService.getLatitude();
            double longitude = mGPSService.getLongitude();
            Toast.makeText(getApplicationContext(), "Latitude:" + latitude + " | Longitude: " + longitude, Toast.LENGTH_LONG).show();

            start1 = mGPSService.getLatitude();
            end1 = mGPSService.getLongitude();
            star_lat = String.valueOf(start1);
            star_long = String.valueOf(end1);
            RidingListmodelinfo();
        }


        // make sure you close the gps after using it. Save user's battery power
        mGPSService.closeGPS();
        edSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                    //do something
                    edSearch.clearFocus();

                    RidingListmodelinfosearch(edSearch.getText().toString().trim());
                }

                return false;
            }
        });

    }

    public void intentCalling(Class name) {
        Intent mIntent = new Intent(DestinationsListActivity.this, name);
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

//http://ridersopininon.herokuapp.com/index.php/riders/vehicle

    /**
     * Register info .
     */
    public void RidingListmodelinfo() {
        showProgressDialog();
        Log.e("riding destination", "riding destination");
        try {
            prefsManager = new PrefsManager(DestinationsListActivity.this);
            AccessToken = prefsManager.getToken();
            UserId = prefsManager.getCaseId();



            //http://ridersopininon.herokuapp.com/index.php/ridingDestination?userId=75&longitude=0.000000&latitude=0.000000&accessToken=eddfbf2bf4046e90fc768d8e319a4355
            Log.e("URL: ", "" + ApplicationGlobal.ROOT + ApplicationGlobal.baseurl_ridingdestination + "userId=" + UserId + "&longitude=" + star_long + "&latitude=" + star_lat + "&radius=2000&accessToken=" + AccessToken);
            RequestQueue requestQueue = Volley.newRequestQueue(DestinationsListActivity.this);
            RequestJsonObject loginTaskRequest = new RequestJsonObject(Request.Method.POST,
                    ApplicationGlobal.ROOT + ApplicationGlobal.baseurl_ridingdestination + "userId=" + UserId + "&longitude=" + star_long + "&latitude=" + star_lat + "&radius=2000&accessToken=" + AccessToken, null,
                    volleyModelErrorListener(), volleyModelSuccessListener()
            );

            requestQueue.add(loginTaskRequest);

        } catch (Exception e) {
            e.printStackTrace();

        }
    }


    /**
     * Search info .
     */
    public void RidingListmodelinfosearch(String search) {

        //http://ridersopininon.herokuapp.com/index.php/ridingDestination/search?search=shimla&userId=105&longitude=76.70740061&latitude=30.7104346&radius=2000&accessToken=764bb308d8e4967b8183969ca709a483
        showProgressDialog();
        Log.e("riding destination", "riding destination");
        try {
            prefsManager = new PrefsManager(DestinationsListActivity.this);
            AccessToken = prefsManager.getToken();
            UserId = prefsManager.getCaseId();
            Log.e("AccessToken:", "" + AccessToken + "----UserId----" + UserId);
            edSearch.setText("");

            //http://ridersopininon.herokuapp.com/index.php/ridingDestination?userId=75&longitude=0.000000&latitude=0.000000&accessToken=eddfbf2bf4046e90fc768d8e319a4355
            Log.e("URL: ", "" + ApplicationGlobal.ROOT + ApplicationGlobal.baseurl_search + "search=" + search + "userId=" + UserId + "&longitude=" + star_long + "&latitude=" + star_lat + "&accessToken=" + AccessToken);
            RequestQueue requestQueue = Volley.newRequestQueue(DestinationsListActivity.this);
            RequestJsonObject loginTaskRequest = new RequestJsonObject(Request.Method.POST,
                    ApplicationGlobal.ROOT + ApplicationGlobal.baseurl_search + "search=" + search + "&userId=" + UserId + "&longitude=" + star_long + "&latitude=" + star_lat + "&accessToken=" + AccessToken, null,
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

                Type type = new TypeToken<RidingDestination>() {
                }.getType();
                RidingDestination mRidingDestination = new Gson().fromJson(response.toString(), type);

                mRidingDestinationDetailses.clear();

                dismissProgressDialog();
                if (mRidingDestination.getSuccess().equals("1")) {
                    mRidingDestinationDetailses.addAll(mRidingDestination.getData());
                    rvDestinations.setAdapter(new AdapterDestination(DestinationsListActivity.this, mRidingDestinationDetailses));
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

        mCustomizeDialog = new CustomizeDialog(DestinationsListActivity.this);
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

    @Override
    protected void onResume() {
        super.onResume();
        if(FilterActivity.filter){
            FilterActivity.filter=false;
            rvDestinations.setAdapter(new AdapterDestination(DestinationsListActivity.this, mRidingDestinationDetailses));

        }
    }
}
