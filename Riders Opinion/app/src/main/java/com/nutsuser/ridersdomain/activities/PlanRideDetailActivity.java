package com.nutsuser.ridersdomain.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.adapter.CustomGridAdapter;
import com.nutsuser.ridersdomain.chat.SampleCometChatActivity;
import com.nutsuser.ridersdomain.utils.ApplicationGlobal;
import com.nutsuser.ridersdomain.utils.CustomizeDialog;
import com.nutsuser.ridersdomain.utils.PrefsManager;
import com.nutsuser.ridersdomain.view.CustomDialog;
import com.nutsuser.ridersdomain.web.api.FileUploadService;
import com.nutsuser.ridersdomain.web.api.ServiceGenerator;
import com.nutsuser.ridersdomain.web.api.volley.RequestJsonObject;
import com.nutsuser.ridersdomain.web.pojos.PlanRideDetails;
import com.nutsuser.ridersdomain.web.pojos.PlanRideDetailsData;
import com.nutsuser.ridersdomain.web.pojos.RiderJoined;
import com.rollbar.android.Rollbar;

import org.json.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.mime.TypedFile;

/**
 * Created by user on 10/1/2015.
 */
public class PlanRideDetailActivity extends BaseActivity {
    public static String[] prgmNameList = {"My Rides", "My Messages", "My Friends", "Chats", "Favorites", "Notifications", "Settings", "Riders Near By"};
    public static int[] prgmImages = {R.drawable.ic_menu_my_rides, R.drawable.ic_menu_my_messages, R.drawable.menu_my_friends, R.drawable.ic_menu_menu_chats, R.drawable.menu_fav_destinations, R.drawable.ic_menu_menu_notifications, R.drawable.menu_settings, R.drawable.icon_nearby};
    public static Class[] classList = {MyRidesRecyclerView.class, RecentChatListActivity.class,MyFriendsActivity.class, ComingSoon.class, FavouriteDesination.class, NotificationListActivity.class, SettingsActivity.class, NearByFriendsAcitivity.class};
    PlanRideDetailsData planRideDetailsData;
    String AccessToken, UserId, eventId, LOCATION;
    CustomizeDialog mCustomizeDialog;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    private static final int WRITE_EXTERNAL_PERMISSIONS_REQUEST = 1;
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
    @Bind(R.id.tvTitleToolbar)
    TextView tvTitleToolbar;
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
    // public static String [] prgmNameList={"Riding Destinations","Meet 'N' Plan A Ride","Riding Events \n    ","Modifly Your Bikes","Healthy Riding","Get Directions","Notifications","Settings"};
    // public static int [] prgmImages={R.drawable.icon_menu_destination,R.drawable.icon_menu_meetplan,R.drawable.icon_menu_events,R.drawable.icon_modifybike,R.drawable.icon_menu_healthy_riding,R.drawable.icon_menu_get_direction,R.drawable.icon_menu_notification,R.drawable.icon_menu_settings};
    // public static Class [] classList={DestinationsListActivity.class,PlanRideActivity.class,EventsListActivity.class,ModifyBikeActivity.class,HealthyRidingActivity.class,GetDirections.class,NotificationScreen.class,SettingsActivity.class};
    @Bind(R.id.lvSlidingMenu)
    LinearLayout lvSlidingMenu;
    @Bind(R.id.gridView1)
    GridView gridView1;
    @Bind(R.id.btEdit)
    Button btEdit;
    @Bind(R.id.sdvEventImage)
    SimpleDraweeView sdvEventImage;
    int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private Activity activity;
    @Bind(R.id.sdvDp)
    SimpleDraweeView sdvDp;
    @Bind(R.id.tvPlace)
    TextView tvPlace;
    @Bind(R.id.btUpdateProfile)
    Button btUpdateProfile;
    private ActionBarDrawerToggle mDrawerToggle;
    String ridetype, startdate, enddate, endtime, starttime, fromlatitude, fromlongitude, fromlocation, tolatitude, tolongitude, tolocation, hlatitude, hlongitude, htype, hlocation, hlatitude1, hlongitude1, htype1, hlocation1;
    String see;
    private String mImagePath = "";
    File destination = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_ride_detail);
        try {
            activity = this;
            ButterKnife.bind(this);
            setupActionBar(toolbar);
            setFonts();
            gridView1.setAdapter(new CustomGridAdapter(this, prgmNameList, prgmImages));
            gridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                    if (position == 1) {
//                        Log.e("positiuon:", "" + classList[position]);
//                        intent_Calling(classList[position], "My Messages");
//                    }  else {
                    Log.e("positiuon:", "" + classList[position]);
                    startActivity(new Intent(PlanRideDetailActivity.this, classList[position]));
                    // }

                }
            });

            eventId = getIntent().getStringExtra("eventId");
            LOCATION = getIntent().getStringExtra("LOCATION");
            if (LOCATION.matches("details")) {
                btEdit.setVisibility(View.GONE);

            } else {
                btEdit.setVisibility(View.VISIBLE);
                tvJoin.setText("JOINED");
            }
            see = getIntent().getStringExtra("LocationSet");
            if (see.equals("NOTSEE")) {
                tvSubmit.setVisibility(View.GONE);
            } else {
                ridetype = getIntent().getStringExtra("rideType");
                if (ridetype.matches("breakfast")) {
                    ridetype = "breakfast";
                    startdate = getIntent().getStringExtra("startdate");
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
                    htype = getIntent().getStringExtra("htype");
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

                }
                tvPlace.setText(fromlocation + "  -  " + tolocation);

                String timein12Format = "";
                try {
                    final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
                    final Date dateObj = sdf.parse(starttime);
                    timein12Format = new SimpleDateFormat("K:mm a").format(dateObj);
                } catch (final ParseException e) {
                    e.printStackTrace();
                }
                tvDateAndTime.setText(startdate + "/" + timein12Format);
            }


            RidingDetails();
            /*****
             *
             * On Drawer Open and Close
             *
             * **/
            mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                    R.drawable.icon_image_view, //nav menu toggle icon
                    R.string.app_name, // nav drawer open - description for accessibility
                    R.string.app_name // nav drawer close - description for accessibility
            ) {
                public void onDrawerClosed(View view) {
                    //getActionBar().setTitle(mTitle);
                    // calling onPrepareOptionsMenu() to show action bar icons
                    //invalidateOptionsMenu();
                    // Toast.makeText(DestinationsListActivity.this, "Drawer Closed....", Toast.LENGTH_SHORT).show();
                }

                public void onDrawerOpened(View drawerView) {
                    String imageUrl = prefsManager.getImageUrl();
                    sdvDp.setImageURI(Uri.parse(imageUrl));
//                File file = new File(imageUrl);
//                sdvDp.setImageURI(Uri.fromFile(file));

                    // Toast.makeText(DestinationsListActivity.this, "Drawer Opened....", Toast.LENGTH_SHORT).show();
                    //getActionBar().setTitle(mDrawerTitle);
                    // calling onPrepareOptionsMenu() to hide action bar icons
                    //invalidateOptionsMenu();
                }
            };
//         mDrawerLayout.setDrawerListener(mDrawerToggle);

            mDrawerLayout.closeDrawer(lvSlidingMenu);
            showProfileImage();
            getPermissionToWriteExternal();
        }catch(Exception e){
            Rollbar.reportException(e, "minor", "Plan ride Detail activity on create");
        }
    }
    // Called when the user is performing an action which requires the app to read the
    // user's Camera
    @TargetApi(Build.VERSION_CODES.M)
    public void getPermissionToWriteExternal() {
        // 1) Use the support library version ContextCompat.checkSelfPermission(...) to avoid
        // checking the build version since Context.checkSelfPermission(...) is only available
        // in Marshmallow
        // 2) Always check for permission (even if permission has already been granted)
        // since the user can revoke permissions at any time through Settings
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // The permission is NOT already granted.
            // Check if the user has been asked about this permission already and denied
            // it. If so, we want to give more explanation about why the permission is needed.
            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show our own UI to explain to the user why we need to read the contacts
                // before actually requesting the permission and showing the default UI

            }

            // Fire off an async request to actually get the permission
            // This will show the standard permission request dialog UI
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_EXTERNAL_PERMISSIONS_REQUEST);
        } else {
            //openCamera();
        }
    }
    /**
     * '
     * Show Profile Image
     ***/
    private void showProfileImage() {
        if (prefsManager.getUserName() == null) {
            tvName.setText("No Name");
        } else {
            tvName.setText(prefsManager.getUserName());
        }
        if (prefsManager.getImageUrl() == null) {
            Toast.makeText(PlanRideDetailActivity.this, "Image Address is Null", Toast.LENGTH_SHORT).show();
        } else {
            String imageUrl = prefsManager.getImageUrl();
            sdvDp.setImageURI(Uri.parse(imageUrl));
//            File file = new File(imageUrl);
//            sdvDp.setImageURI(Uri.fromFile(file));
        }

    }

    public void intent_Calling(Class name, String na) {
        Intent mIntent = new Intent(PlanRideDetailActivity.this, name);
        mIntent.putExtra(SCREEN_OPEN, na);
        startActivity(mIntent);

    }



    private void setFonts() {
        tvName.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/MACHINEN.TTF"));
        Typeface typefaceNormal = Typeface.createFromAsset(getResources().getAssets(), "fonts/LATO-REGULAR.TTF");
        tvTitle.setTypeface(typefaceNormal);
        tvPlace.setTypeface(typefaceNormal);
        tvDateAndTime.setTypeface(typefaceNormal);
        tvHostedBy.setTypeface(typefaceNormal);
        //  tvLabelHostedBy.setTypeface(typefaceNormal);
        // tvNumberOfRiders.setTypeface(typefaceNormal);
        tvSubmit.setTypeface(typefaceNormal);
        tvLabelHaveJoined.setTypeface(typefaceNormal);
        //tvDate.setTypeface(typefaceNormal);
        tvTitleToolbar.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/MACHINEN.TTF"));
        //tvTime.setTypeface(typefaceNormal);
        tvEatables.setTypeface(typefaceNormal);
        tvPetrolPump.setTypeface(typefaceNormal);
        tvServiceCenter.setTypeface(typefaceNormal);
        tvFirstAid.setTypeface(typefaceNormal);
        tvJoin.setTypeface(typefaceNormal);
        tvSponsoredAdTitle1.setTypeface(typefaceNormal);
        tvSponsoredAdReviews1.setTypeface(typefaceNormal);
        tvSponsoredAdLocation1.setTypeface(typefaceNormal);
        tvSponsoredAdTitle2.setTypeface(typefaceNormal);
        tvSponsoredAdReviews2.setTypeface(typefaceNormal);
        tvSponsoredAdLocation2.setTypeface(typefaceNormal);
        // tvDesc.setTypeface(typefaceNormal);
        tvTitleToolbar.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/MACHINEN.TTF"));
        tvName.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/MACHINEN.TTF"));
        // tvEventName.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/Lato-Heavy.ttf"));
        tvNumberOfRiders.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/Lato-Heavy.ttf"));
        tvLabelHaveJoined.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/Lato-Bold.ttf"));
        tvHostedBy.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/Lato-Bold.ttf"));
        tvTime.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/Lato-Bold.ttf"));
        tvDate.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/Lato-Bold.ttf"));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (LOCATION.matches("details")) {
                    finish();
                } else {
                    Intent intent = new Intent(PlanRideDetailActivity.this, MainScreenActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.ivMenu, R.id.rlProfile, R.id.btEdit, R.id.tvJoin, R.id.btFullProfile, R.id.btUpdateProfile, R.id.tvNumberOfRiders, R.id.ivMap, R.id.tvSubmit})
    void onclick(View view) {
        switch (view.getId()) {
            case R.id.ivMenu:
                if (mDrawerLayout.isDrawerOpen(lvSlidingMenu))
                    mDrawerLayout.closeDrawer(lvSlidingMenu);
                else
                    mDrawerLayout.openDrawer(lvSlidingMenu);
                break;
            case R.id.rlProfile:
               // startActivity(new Intent(activity, ProfileActivity.class));
                break;
            case R.id.tvSubmit:
                if (ridetype.matches("breakfast")) {
                    Intent mIntent = new Intent(PlanRideDetailActivity.this, PlanARidePostRide.class);
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
                    Intent mIntent = new Intent(PlanRideDetailActivity.this, PlanARidePostRide.class);
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
            case R.id.btEdit:
                selectImage();
                break;
            case R.id.tvJoin:
                if (planRideDetailsData.getIsVehId() == 1) {
                    if (isNetworkConnected()) {
                        JOINEVENT();
                    } else {
                        showToast("Internet Not Connected");
                    }
                } else {
                    startActivity(new Intent(PlanRideDetailActivity.this, AfterRegisterScreen.class));
                }
                break;

            case R.id.ivMap:
                String lat = planRideDetailsData.getBaseLat();
                String lon = planRideDetailsData.getBaseLong();
                String deslon = planRideDetailsData.getDestLong();
                String deslat = planRideDetailsData.getDestLat();
                Intent mIntent = new Intent(PlanRideDetailActivity.this, MapActivity.class);
                mIntent.putExtra("endLat", lat);
                mIntent.putExtra("endLon", lon);
                mIntent.putExtra("deslon", deslon);
                mIntent.putExtra("deslat", deslat);
                startActivity(mIntent);
                break;
            case R.id.tvNumberOfRiders:
                String latstatus = planRideDetailsData.getBaseLat();
                String lonstatus = planRideDetailsData.getBaseLong();
                String deslonstatus = planRideDetailsData.getDestLong();
                String deslatstatus = planRideDetailsData.getDestLat();
                Intent statusintent = new Intent(activity, StatusRiderOnMapActivity.class);
                statusintent.putExtra("event_Id", eventId);
                statusintent.putExtra("latstatus", latstatus);
                statusintent.putExtra("lonstatus", lonstatus);
                statusintent.putExtra("deslonstatus", deslonstatus);
                statusintent.putExtra("deslatstatus", deslatstatus);
                startActivity(statusintent);
                break;
            case R.id.btUpdateProfile:
                startActivity(new Intent(activity, ProfileActivity.class));
                break;
            case R.id.btFullProfile:
                startActivity(new Intent(activity, PublicProfileScreen.class));
                break;
        }
    }

    /**
     * Match Riding List info .
     */
    public void RidingDetails() {
        showProgressDialog();
        Log.e(" destination", " destination");
        try {
            prefsManager = new PrefsManager(PlanRideDetailActivity.this);
            AccessToken = prefsManager.getToken();
            UserId = prefsManager.getCaseId();
            String radius = prefsManager.getRadius();

            FileUploadService service = ServiceGenerator.createService(FileUploadService.BASE_URL);
            service.eventDetail(UserId, AccessToken, eventId, new Callback<JsonObject>() {
                @Override
                public void success(JsonObject jsonObject, retrofit.client.Response response) {
                    Log.e("Details :", "" + jsonObject.toString());
                    Type type = new TypeToken<PlanRideDetails>() {
                    }.getType();
                    PlanRideDetails planARide = new Gson().fromJson(jsonObject.toString(), type);
                    if (planARide.getSuccess() == 1) {
                        dismissProgressDialog();
                        planRideDetailsData = planARide.getData();
                        if (see.equals("NOTSEE")){
                            tvPlace.setText(planRideDetailsData.getBaseLocation() + " - " + planRideDetailsData.getDestLocation());
                            tvDateAndTime.setText(planRideDetailsData.getStartTime());
                        }
                        if(TextUtils.isEmpty(planRideDetailsData.getHostedBy())){
                            tvHostedBy.setText("No Name");
                        }
                        else{
                            tvHostedBy.setText(planRideDetailsData.getHostedBy());
                        }
                        Log.e("Base:", ""+planRideDetailsData.getBaseLocation());
                        tvTitle.setText(planRideDetailsData.getBaseLocation() + " to " + planRideDetailsData.getDestLocation());
                        tvTime.setText(planRideDetailsData.getStartTime());
                        tvDate.setText(planRideDetailsData.getStartDate());
                        tvEatables.setText("" + planRideDetailsData.getRestaurant());
                        tvPetrolPump.setText("" + planRideDetailsData.getPetrolpumps());
                        tvServiceCenter.setText("" + planRideDetailsData.getServiceStation());
                        tvFirstAid.setText("" + planRideDetailsData.getHospitals());
                        tvNumberOfRiders.setText(planRideDetailsData.getRiders() + " Riders");
                        planRideDetailsData = planARide.getData();
                        if (planRideDetailsData.getIsJoin() == 1) {
                            tvJoin.setText("JOIN");
                            tvJoin.setBackgroundColor(Color.parseColor("#66000000"));
                        } else {
                            tvJoin.setText("JOIN");
                        }
                        if (planRideDetailsData.getImage() != null) {
                            String milestonesJsonInString = planRideDetailsData.getImage().toString();
                            milestonesJsonInString = milestonesJsonInString.replace("\\\"", "\"");
                            milestonesJsonInString = milestonesJsonInString.replace("\"{", "{");
                            milestonesJsonInString = milestonesJsonInString.replace("}\"", "}");
                            sdvEventImage.setImageURI(Uri.parse(milestonesJsonInString));
                        }
                        tvDesc.setText(planRideDetailsData.getDescription());
                    } else if (planARide.getMessage().equals("Data Not Found.")) {
                        dismissProgressDialog();
                        showToast("Data Not Found.");
                    } else {
                        dismissProgressDialog();
                    }

                }

                @Override
                public void failure(RetrofitError error) {
                    dismissProgressDialog();
                    showToast("Error:" + error);
                    Log.e("Upload", "error");
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
            Rollbar.reportException(e, "minor", "Plan ride Detail activity event detail API");

        }
    }



    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(PlanRideDetailActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent takePictureIntent = new Intent(
                            MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePictureIntent,
                            ApplicationGlobal.CAMERA_REQUEST);
                  /*  Intent takePictureIntent = new Intent(
                            MediaStore.ACTION_IMAGE_CAPTURE);


                    try {
                        destination = ApplicationGlobal.setUpPhotoFile();
                        mImagePath = destination.getAbsolutePath();
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(destination));
                    } catch (IOException e) {
                        e.printStackTrace();
                        destination = null;
                        mImagePath = null;
                    }
                    startActivityForResult(takePictureIntent,
                            ApplicationGlobal.CAMERA_REQUEST);*/
                    //Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                   // startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Choose from Library")) {
                    Intent i = new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i,
                            ApplicationGlobal.RESULT_LOAD_IMAGE);
                   /* Intent i = new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i,
                            ApplicationGlobal.RESULT_LOAD_IMAGE);*/
                  /*  Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image*//*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);*/
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == ApplicationGlobal.RESULT_LOAD_IMAGE
                    && resultCode == Activity.RESULT_OK) {
                try {
                    onSelectFromGalleryResult(data);
                }
                catch(NullPointerException e){

                    }
            }
            else if (requestCode == ApplicationGlobal.CAMERA_REQUEST
                    && resultCode == Activity.RESULT_OK) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");

                onCapture_ImageResult(bitmap);
            }
            /*else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);*/
        }
    }
    private void onCapture_ImageResult(Bitmap bitmap) {
        showProgressDialog();
       /* Bitmap thumbnail = bitmap;
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        FileOutputStream fo;
        try {

            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        destination=ApplicationGlobal.bitmapToFile(bitmap);
        Log.e("destination:",""+destination);
        prefsManager = new PrefsManager(PlanRideDetailActivity.this);

        FileUploadService service = ServiceGenerator.createService(FileUploadService.BASE_URL);

        TypedFile typedFile = new TypedFile("multipart/form-data", destination);

        Log.e("CameraImage User Id", "" + prefsManager.getCaseId());
        Log.e("CameraImage Token", "" + prefsManager.getToken());
        Log.e("FileLocation", "" + destination);
        Log.e("CameraImage TypedFile", "" + typedFile);
        service.upload_(typedFile, AccessToken, eventId, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, retrofit.client.Response response) {
                Log.e("Upload", "success");
                Log.e("jsonObject:", "" + jsonObject.toString());
                Log.e("response:", "" + response.toString());
                Log.e("Status:", "" + response.getStatus());
                Log.e("Body:", "" + response.getBody());
                Log.e("Reason:", "" + response.getReason());
                dismissProgressDialog();
                sdvEventImage.setImageURI(Uri.fromFile(destination));
            }
            @Override
            public void failure(RetrofitError error) {
                Log.e("Upload", "error");
            }
        });


    }
    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        showProgressDialog();
        Uri selectedImageUri = data.getData();
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = managedQuery(selectedImageUri, projection, null, null,
                null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();

        String selectedImagePath = cursor.getString(column_index);

       // Bitmap bm = ApplicationGlobal.getFile(selectedImagePath, PlanRideDetailActivity.this);
        Bitmap bm = ApplicationGlobal.getFile(selectedImagePath, PlanRideDetailActivity.this);
        destination=ApplicationGlobal.bitmapToFile(bm);
        final File file = savebitmap(bm);
        //final File file = savebitmap(bm);
        prefsManager = new PrefsManager(PlanRideDetailActivity.this);

        FileUploadService service = ServiceGenerator.createService(FileUploadService.BASE_URL);
        TypedFile typedFile = new TypedFile("multipart/form-data", file);
        service.upload_(typedFile, AccessToken, eventId, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, retrofit.client.Response response) {
                Log.e("Upload", "success");
                Log.e("jsonObject:", "" + jsonObject.toString());
                Log.e("response:", "" + response.toString());
                Log.e("Status:", "" + response.getStatus());
                Log.e("Body:", "" + response.getBody());
                Log.e("Reason:", "" + response.getReason());
                dismissProgressDialog();
                sdvEventImage.setImageURI(Uri.fromFile(file));

            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("Upload", "error");
                Log.e("error:", ""+error);
            }
        });
    }
    private File savebitmap(Bitmap filename) {
        File imageF = null;
        try {
            if (Environment.MEDIA_MOUNTED.equals(Environment
                    .getExternalStorageState())) {

                File storageDir = new File(
                        ApplicationGlobal.LOCAL_STORAGE_BASE_PATH_FOR_POSTED_PHOTOS)
                        .getParentFile();

                if (storageDir != null) {
                    if (!storageDir.mkdirs()) {
                        if (!storageDir.exists()) {
                            Log.d("CameraSample", "failed to create directory");
                            return null;
                        }
                    }
                }
                imageF = File.createTempFile(ApplicationGlobal.JPEG_FILE_PREFIX
                                + System.currentTimeMillis() + "_",
                        ApplicationGlobal.JPEG_FILE_SUFFIX, storageDir);
            } else {
                Log.v("image loading status",
                        "External storage is not mounted READ/WRITE.");
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        OutputStream outStream = null;
        try {
            // make a new bitmap from your file
            Bitmap bitmap = filename;

            outStream = new FileOutputStream(imageF);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            outStream.flush();
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("file", "" + imageF);
        return imageF;

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mDrawerLayout.isDrawerOpen(lvSlidingMenu)) {
            showProfileImage();
            mDrawerLayout.closeDrawer(lvSlidingMenu);
        }
    }

    /**
     * Match Event Details info .
     */
    public void JOINEVENT() {
        showProgressDialog();
        Log.e("JOIN EVENT", "JOIN EVENT");
        try {
            prefsManager = new PrefsManager(PlanRideDetailActivity.this);
            AccessToken = prefsManager.getToken();
            UserId = prefsManager.getCaseId();
            String radius = prefsManager.getRadius();
            //http://ridersopininon.herokuapp.com/index.php/ridingDestination?userId=75&longitude=0.000000&latitude=0.000000&accessToken=eddfbf2bf4046e90fc768d8e319a4355
            Log.e("URL: ", "" + ApplicationGlobal.ROOT + ApplicationGlobal.baseurl_joinEvent + "userId=" + UserId + "&accessToken=" + AccessToken + "&eventId=" + eventId);
            RequestQueue requestQueue = Volley.newRequestQueue(PlanRideDetailActivity.this);
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
                Type type = new TypeToken<RiderJoined>() {
                }.getType();
                RiderJoined riderJoined = new Gson().fromJson(response.toString(), type);

                if (riderJoined.getSuccess() == 1) {
                    tvJoin.setText("JOIN");
                    tvJoin.setBackgroundColor(Color.parseColor("#66000000"));
                    CustomDialog.JoinedshowProgressDialog(PlanRideDetailActivity.this, riderJoined.getMessage().toString());
                } else {
                    CustomDialog.showProgressDialog(PlanRideDetailActivity.this, riderJoined.getMessage().toString());
                }

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
