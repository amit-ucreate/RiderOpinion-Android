package com.nutsuser.ridersdomain.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.adapter.CustomGridAdapter;
import com.nutsuser.ridersdomain.adapter.EndPlaceArrayAdapter;
import com.nutsuser.ridersdomain.adapter.PlaceArrayAdapter;
import com.nutsuser.ridersdomain.chat.SampleCometChatActivity;
import com.nutsuser.ridersdomain.route.GMapV2GetRouteDirection;
import com.nutsuser.ridersdomain.services.GPSService;
import com.nutsuser.ridersdomain.utils.ApplicationGlobal;
import com.nutsuser.ridersdomain.utils.CustomizeDialog;
import com.nutsuser.ridersdomain.utils.PrefsManager;
import com.nutsuser.ridersdomain.view.CircleButtonText;
import com.nutsuser.ridersdomain.web.api.FileUploadService;
import com.nutsuser.ridersdomain.web.api.ServiceGenerator;
import com.nutsuser.ridersdomain.web.api.volley.RequestJsonObject;
import com.nutsuser.ridersdomain.web.pojos.FavoriteDestination;
import com.nutsuser.ridersdomain.web.pojos.GetCount;
import com.nutsuser.ridersdomain.web.pojos.GetCountData;
import com.nutsuser.ridersdomain.web.pojos.Like;
import com.nutsuser.ridersdomain.web.pojos.LikeDetails;
import com.nutsuser.ridersdomain.web.pojos.RidingDestinationDetailsClick;
import com.nutsuser.ridersdomain.web.pojos.RidingDestinationDetailsClickInfo;
import com.rollbar.android.Rollbar;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;
import org.w3c.dom.Document;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;

/**
 * Created by Amit Agnihotri on 9/1/2015.
 */
public class DestinationsDetailActivity extends BaseActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {

    public static boolean update = false;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));
    private static final String LOG_TAG = "GetDirection";
    private static final int GOOGLE_API_CLIENT_ID = 0;
    public static String[] prgmNameList = {"My Rides", "My Messages", "My Friends", "Chats", "Favorites", "Notifications", "Settings", "Riders Near By"};
    public static int[] prgmImages = {R.drawable.ic_menu_my_rides, R.drawable.ic_menu_my_messages, R.drawable.menu_my_friends, R.drawable.ic_menu_menu_chats, R.drawable.menu_fav_destinations, R.drawable.ic_menu_menu_notifications, R.drawable.menu_settings, R.drawable.icon_nearby};
    public static Class[] classList = {MyRidesRecyclerView.class, RecentChatListActivity.class,MyFriendsActivity.class, ComingSoon.class, FavouriteDesination.class, NotificationListActivity.class, SettingsActivity.class, NearByFriendsAcitivity.class};

    GMapV2GetRouteDirection v2GetRouteDirection;
    LocationManager locManager;
    Drawable drawable;
    Document document;
    EndPlaceArrayAdapter _ArrayAdapter;
    LatLng mString_end, mStringCurrent;
    String destName;
    LatLng mString_start;
    double start, end;
    CustomizeDialog mCustomizeDialog;
    @Bind(R.id.rv_properties)
    RelativeLayout rv_properties;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tvTitleToolbar)
    TextView tvTitleToolbar;
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.lvSlidingMenu)
    LinearLayout lvSlidingMenu;
    @Bind(R.id.tvName)
    TextView tvName;
    @Bind(R.id.tvEatables)
    TextView tvEatables;
    @Bind(R.id.tvPetrolPump)
    TextView tvPetrolPump;
    @Bind(R.id.tvServiceCenter)
    TextView tvServiceCenter;
    @Bind(R.id.tvFirstAid)
    TextView tvFirstAid;
    @Bind(R.id.transparent_image)
    ImageView transparent_image;
    @Bind(R.id.sdvDisplayPicture)
    SimpleDraweeView sdvDisplayPicture;
    @Bind(R.id.tv_Eatables)
    TextView tv_Eatables;
    @Bind(R.id.tv_PetrolPump)
    TextView tv_PetrolPump;
    @Bind(R.id.tv_ServiceCenter)
    TextView tv_ServiceCenter;
    @Bind(R.id.tv_FirstAid)
    TextView tv_FirstAid;
    @Bind(R.id.tvReviews)
    TextView tvReviews;
    @Bind(R.id.tvUpcomingCount)
    CircleButtonText tvRides;
    @Bind(R.id.gridView1)
    GridView gridView1;
    String jsonInString;
    @Bind(R.id.ivStar1)
    ImageView ivStar1;
    @Bind(R.id.ivStar2)
    ImageView ivStar2;
    @Bind(R.id.ivStar3)
    ImageView ivStar3;
    @Bind(R.id.ivStar4)
    ImageView ivStar4;
    @Bind(R.id.ivStar5)
    ImageView ivStar5;
    @Bind(R.id.scView)
    ScrollView scView;
    @Bind(R.id.fmback)
            FrameLayout fmback;
    GetCountData getCountData;
    // public static String [] prgmNameList={"Riding Destinations","Meet 'N' Plan A Ride","Riding Events \n    ","Modifly Your Bikes","Healthy Riding","Get Directions","Notifications","Settings"};
    // public static int [] prgmImages={R.drawable.icon_menu_destination,R.drawable.icon_menu_meetplan,R.drawable.icon_menu_events,R.drawable.icon_modifybike,R.drawable.icon_menu_healthy_riding,R.drawable.icon_menu_get_direction,R.drawable.icon_menu_notification,R.drawable.icon_menu_settings};
    // public static Class [] classList={DestinationsListActivity.class,PlanRideActivity.class,EventsListActivity.class,ModifyBikeActivity.class,HealthyRidingActivity.class,GetDirections.class,NotificationScreen.class,SettingsActivity.class};
    RidingDestinationDetailsClickInfo mRidingDestinationDetailsClickInfo;
    String AccessToken, UserId;
    @Bind(R.id.sdvPostImage)
    SimpleDraweeView sdvPostImage;
    @Bind(R.id.tvPlace)
    TextView tvPlace;
    @Bind(R.id.tvLike)
    TextView tvLike;
    @Bind(R.id.tvDesc)
    TextView tvDesc;
    @Bind(R.id.ivFavorites)
    ImageView ivFavorites;
    @Bind(R.id.ivMap)
    ImageView ivMap;
    @Bind(R.id.ivVideo)
    ImageView ivVideo;
    @Bind(R.id.fmLayout)
    FrameLayout fmLayout;
    @Bind(R.id.tvNavMeetPlan)
    TextView tvNavMeetPlan;
    // Google Map
    private GoogleMap map;

    @Bind(R.id.sdvDp)
    SimpleDraweeView sdvDp;
    String fromstartlat, fromendlong;
    @Bind(R.id.btUpdateProfile)
    Button btUpdateProfile;
    private ActionBarDrawerToggle mDrawerToggle;
    private AutoCompleteTextView tvPlace3, tvPlace4;
    private GoogleApiClient mGoogleApiClient;
    private PlaceArrayAdapter mPlaceArrayAdapter;
    private Activity activity;


    public PrefsManager prefsManager;
    String imageUrl = null;
    File file;
    String s, s1;


    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback_start;
    private AdapterView.OnItemClickListener mAutocompleteClickListener_start
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            Log.i(LOG_TAG, "Selected: " + item.description);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback_start);
            Log.i(LOG_TAG, "Fetching details for ID: " + item.placeId);
        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback_end
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e(LOG_TAG, "Place query did not complete. Error: " +
                        places.getStatus().toString());
                return;
            }
            // Selecting the first object buffer.
            final Place place = places.get(0);
            CharSequence attributions = places.getAttributions();

            // mNameTextView.setText("NAME:"+ Html.fromHtml(place.getName() + ""));
            // mAddressTextView.setText("ADDRESS: "+Html.fromHtml(place.getAddress() + ""));
            //  mIdTextView.setText(Html.fromHtml("PLACEID:" + place.getId() + ""));
            String s = "" + Html.fromHtml("" + place.getLatLng().latitude);
            String s1 = "" + Html.fromHtml("" + place.getLatLng().longitude);
            double start = Double.valueOf(s.trim()).doubleValue();
            double end = Double.valueOf(s1.trim()).doubleValue();


            mString_end = new LatLng(start, end);

            //mString_end =Html.fromHtml(place.getLatLng() + "");

            Log.e("end:", "" + mString_end);
            //mPhoneTextView.setText(Html.fromHtml("Lat:" + place.getLatLng().latitude + "--long:" + latlong));
            //mWebTextView.setText(place.getWebsiteUri() + "");
            if (attributions != null) {
                // mAttTextView.setText(Html.fromHtml(attributions.toString()));
            }
        }

    };
    private AdapterView.OnItemClickListener mAutocompleteClickListener_end
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final EndPlaceArrayAdapter.PlaceAutocomplete item = _ArrayAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            Log.i(LOG_TAG, "Selected: " + item.description);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback_end);
            Log.i(LOG_TAG, "Fetching details for ID: " + item.placeId);
        }
    };

    {
        mUpdatePlaceDetailsCallback_start = new ResultCallback<PlaceBuffer>() {
            @Override
            public void onResult(PlaceBuffer places) {
                if (!places.getStatus().isSuccess()) {
                    Log.e(LOG_TAG, "Place query did not complete. Error: " +
                            places.getStatus().toString());
                    return;
                }
                // Selecting the first object buffer.
                final Place place = places.get(0);
                CharSequence attributions = places.getAttributions();

                // mNameTextView.setText("NAME:"+ Html.fromHtml(place.getName() + ""));
                // mAddressTextView.setText("ADDRESS: "+Html.fromHtml(place.getAddress() + ""));
                //  mIdTextView.setText(Html.fromHtml("PLACEID:" + place.getId() + ""));
                String s = "" + Html.fromHtml("" + place.getLatLng().latitude);
                String s1 = "" + Html.fromHtml("" + place.getLatLng().longitude);
                start = Double.valueOf(s.trim()).doubleValue();
                end = Double.valueOf(s1.trim()).doubleValue();
                mString_start = new LatLng(start, end);

                Log.e("start:", "" + mString_start);
                hideKeyboard();
                //  mPhoneTextView.setText(Html.fromHtml("Lat:" + place.getLatLng().latitude + "--long:" + latlong));
                //mWebTextView.setText(place.getWebsiteUri() + "");
                if (attributions != null) {
                    // mAttTextView.setText(Html.fromHtml(attributions.toString()));
                }
            }

        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination_detail);
        try {
            v2GetRouteDirection = new GMapV2GetRouteDirection();
            activity = this;
            ButterKnife.bind(activity);
            setupActionBar();
            setFontsToTextViews();
            tvRides = (CircleButtonText) findViewById(R.id.tvUpcomingCount);
            prefsManager = new PrefsManager(this);
            imageUrl = prefsManager.getImageUrl();
            Log.e("imageUrl:", "" + imageUrl);
            file = new File(imageUrl);
            mDrawerLayout.closeDrawer(lvSlidingMenu);
            gridView1.setAdapter(new CustomGridAdapter(this, prgmNameList, prgmImages));
            gridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (position == 7) {
//
//                } else {
//                    if (position == 1) {
//                        Log.e("positiuon:", "" + classList[position]);
//                        intent_Calling(classList[position], "My Messages");
//                    }   else {
                    Log.e("positiuon:", "" + classList[position]);
                    intentCalling(classList[position]);
                    //  }
                    // }
                }
            });


            mGoogleApiClient = new GoogleApiClient.Builder(DestinationsDetailActivity.this)
                    .addApi(Places.GEO_DATA_API)
                    .enableAutoManage(this, GOOGLE_API_CLIENT_ID, DestinationsDetailActivity.this)
                    .addConnectionCallbacks(this)
                    .addApi(AppIndex.API).build();
            tvPlace3 = (AutoCompleteTextView) findViewById(R.id
                    .tvPlace3);
            tvPlace4 = (AutoCompleteTextView) findViewById(R.id
                    .tvPlace4);
            tvPlace3.setThreshold(3);
            tvPlace4.setThreshold(3);
            tvPlace3.setOnItemClickListener(mAutocompleteClickListener_start);
            mPlaceArrayAdapter = new PlaceArrayAdapter(this, android.R.layout.simple_list_item_1,
                    BOUNDS_MOUNTAIN_VIEW, null);
            tvPlace3.setAdapter(mPlaceArrayAdapter);
            tvPlace4.setOnItemClickListener(mAutocompleteClickListener_end);
            _ArrayAdapter = new EndPlaceArrayAdapter(this, android.R.layout.simple_list_item_1,
                    BOUNDS_MOUNTAIN_VIEW, null);
            tvPlace4.setAdapter(_ArrayAdapter);
/*
Riding Details fetch through this function
*/

            if (isNetworkConnected()) {
                RidingListDetailsmodelinfo();
            } else {
                showToast("Internet is not connected.");
            }
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


            scView = (ScrollView) findViewById(R.id.scView);
            transparent_image = (ImageView) findViewById(R.id.transparent_image);
            transparent_image.setOnTouchListener(new View.OnTouchListener() {
                                                     @Override
                                                     public boolean onTouch(View v, MotionEvent event) {
                                                         int action = event.getAction();
                                                         switch (action) {
                                                             case MotionEvent.ACTION_DOWN:
                                                                 // Disallow ScrollView to intercept touch events.
                                                                 scView.requestDisallowInterceptTouchEvent(true);
                                                                 // Disable touch on transparent view
                                                                 return false;

                                                             case MotionEvent.ACTION_UP:
                                                                 // Allow ScrollView to intercept touch events.
                                                                 scView.requestDisallowInterceptTouchEvent(false);
                                                                 return true;

                                                             case MotionEvent.ACTION_MOVE:
                                                                 scView.requestDisallowInterceptTouchEvent(true);
                                                                 return false;

                                                             default:
                                                                 return true;
                                                         }
                                                     }
                                                 }

            );
        }catch(Exception e){
            Rollbar.reportException(e, "minor", "DestinationDetailActivity OnCreate()");
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

        if (TextUtils.isEmpty(prefsManager.getImageUrl())) {
          //  Toast.makeText(DestinationsDetailActivity.this, "Image Address is Null", Toast.LENGTH_SHORT).show();
        } else {
            String imageUrl = prefsManager.getImageUrl();
            sdvDp.setImageURI(Uri.parse(imageUrl));
//            File file = new File(imageUrl);
//            sdvDp.setImageURI(Uri.fromFile(file));
        }

    }

    public void intent_Calling(Class name, String na) {
        Intent mIntent = new Intent(DestinationsDetailActivity.this, name);
        mIntent.putExtra(SCREEN_OPEN, na);
        startActivity(mIntent);

    }

    private void hideKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public void intentCalling(Class name) {
        Intent mIntent = new Intent(DestinationsDetailActivity.this, name);
        startActivity(mIntent);

    }

    private void setupActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void setFontsToTextViews() {
        tvTitleToolbar.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/MACHINEN.TTF"));
        tvName.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/MACHINEN.TTF"));
        tvDesc.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/Lato-Bold.ttf"));
        tvPlace.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/Lato-Heavy.ttf"));
        // tvAddress.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
        // tvDestinations.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
        // tvEvents.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
        //tvModifyBike.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
        // tvMeetAndPlanRide.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
        // tvHealthyRiding.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
        //tvGetDirections.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
        //tvNotifications.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
        //tvSettings.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
    }

    @OnClick({R.id.ivBack, R.id.ivMap, R.id.ivMenu, R.id.ivFavorites, R.id.tvLike, R.id.ivVideo, R.id.btMap, R.id.tvShare, R.id.btFullProfile, R.id.btUpdateProfile, R.id.fmLayout,R.id.tvNavMeetPlan,R.id.tvReviews})
    void onclick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                update = true;
                onBackPressed();
                break;

            case R.id.tvReviews:
                startActivity(new Intent(DestinationsDetailActivity.this,DestinationReviewsActivity.class).putExtra(KEY_REVIEW_URL,mRidingDestinationDetailsClickInfo.getBlogLinks()));
                break;
            case R.id.ivVideo:
                Log.e("VIDEO URL: ", "" + mRidingDestinationDetailsClickInfo.getVideoUrl());
                Intent Intent = new Intent(activity, YouTubeVideoPlay.class);
                Intent.putExtra("VIDEOURL", mRidingDestinationDetailsClickInfo.getVideoUrl());
                startActivity(Intent);
                break;

            case R.id.ivMap:
                String lat = mRidingDestinationDetailsClickInfo.getDestLatitude();
                String lon = mRidingDestinationDetailsClickInfo.getDestLongitude();
                Intent mIntent = new Intent(activity, Subcribe.class);
                // mIntent.putExtra("endLat", lat);
                // mIntent.putExtra("endLon", lon);
                startActivity(mIntent);
                //startActivity(new Intent(activity, MapActivity.class));
                break;
            case R.id.ivMenu:
                if (mDrawerLayout.isDrawerOpen(lvSlidingMenu))
                    mDrawerLayout.closeDrawer(lvSlidingMenu);
                else
                    mDrawerLayout.openDrawer(lvSlidingMenu);
                break;
            case R.id.fmLayout:
                if(tvRides.getText().toString().matches("0")){

                }
                else{
                    Log.e("destName",mRidingDestinationDetailsClickInfo.getDestName());
                    Intent intentevnt = new Intent(activity, EventsListActivity.class);
                    intentevnt.putExtra("rides", "yes");
                    intentevnt.putExtra("destName",mRidingDestinationDetailsClickInfo.getDestName() );
                    intentevnt.putExtra("baslat", mRidingDestinationDetailsClickInfo.getDestLatitude());
                    intentevnt.putExtra("baslon", mRidingDestinationDetailsClickInfo.getDestLongitude());
                    startActivity(intentevnt);
                }

                break;

            case R.id.ivFavorites:
                Favorite();
                break;
            case R.id.tvLike:
                Like();
                break;
            case R.id.btMap:
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
                    // Toast.makeText(getApplicationContext(), "Latitude:" + latitude + " | Longitude: " + longitude, Toast.LENGTH_LONG).show();

                    double start_ = mGPSService.getLatitude();
                    double end_ = mGPSService.getLongitude();
                    mStringCurrent = new LatLng(start_, end_);
                    Log.e("mStringCurrent:", "" + mStringCurrent);
                    try {
                        GetUpdateRouteTask getRoute = new GetUpdateRouteTask();
                        getRoute.execute();
                    }catch(Exception e){

                    }

                }


                // make sure you close the gps after using it. Save user's battery power
                mGPSService.closeGPS();

                break;
            case R.id.tvShare:
                Log.e("id",ApplicationGlobal.DestID);
                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(android.content.Intent.EXTRA_TEXT, DESTINATION_SHARE_URL+ApplicationGlobal.DestID);
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Destiantion Picture");
                startActivity(android.content.Intent.createChooser(intent, "Share"));
                break;
            case R.id.btUpdateProfile:
                startActivity(new Intent(activity, ProfileActivity.class));
                break;
            case R.id.btFullProfile:
                startActivity(new Intent(activity, PublicProfileScreen.class));
                break;
            case R.id.tvNavMeetPlan:
                //Log.e("destName",destName);
               startActivity(new Intent(activity, PlanRideActivity.class).putExtra(DESTINATION_LAT, s).putExtra(DESTINATION_LONG, s1).putExtra(DESTINATION_NAME, destName).putExtra(DESTINATION_NAVIGATE, true));

                break;
        }
    }

    /**
     * Destination Details info .
     */
    public void RidingListDetailsmodelinfo() {
        showProgressDialog();
        Log.e("riding destination", "riding destination");
        try {
            prefsManager = new PrefsManager(DestinationsDetailActivity.this);
            AccessToken = prefsManager.getToken();
            UserId = prefsManager.getCaseId();
            Log.e("AccessToken:", "" + AccessToken + "----UserId----" + UserId+"  destId: "+ApplicationGlobal.DestID );

            RequestQueue requestQueue = Volley.newRequestQueue(DestinationsDetailActivity.this);
            RequestJsonObject loginTaskRequest = new RequestJsonObject(Request.Method.POST,
                    ApplicationGlobal.ROOT + ApplicationGlobal.baseurl_ridingdetailDestination + "userId=" + UserId + "&destId=" + ApplicationGlobal.DestID + "&longitude=0.000000&latitude=0.000000&accessToken=" + AccessToken, null,
                    volleyModelErrorListener(), volleyModelSuccessListener()
            );

            requestQueue.add(loginTaskRequest);

        } catch (Exception e) {
            e.printStackTrace();
            Rollbar.reportException(e, "minor", "DestinationDetailActivity RidingListDetailsmodelinfo API");

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

                Type type = new TypeToken<RidingDestinationDetailsClick>() {
                }.getType();
                RidingDestinationDetailsClick mRidingDestination = new Gson().fromJson(response.toString(), type);

                // mRidingDestinationDetailses.clear();

                if (mRidingDestination.getSuccess().equals("1")) {

                    mRidingDestinationDetailsClickInfo = mRidingDestination.getData();
                    tvPlace.setText(mRidingDestinationDetailsClickInfo.getDestName());
                     s = mRidingDestinationDetailsClickInfo.getDestLatitude();
                     s1 = mRidingDestinationDetailsClickInfo.getDestLongitude();
                    fromstartlat = s;
                    fromendlong = s1;
                    double start = Double.valueOf(s.trim()).doubleValue();
                    double end = Double.valueOf(s1.trim()).doubleValue();
                    mString_end = new LatLng(start, end);
                    Log.e("mString_end:", "" + mString_end);
                    destName = mRidingDestinationDetailsClickInfo.getDestName();
                    tvPlace4.setText(mRidingDestinationDetailsClickInfo.getDestName());
                    jsonInString = mRidingDestinationDetailsClickInfo.getImages().toString();
                    jsonInString = jsonInString.replace("\\\"", "\"");
                    jsonInString = jsonInString.replace("\"{", "{");
                    jsonInString = jsonInString.replace("}\"", "}");
                    Log.e("jsonInString: ", "" + jsonInString);
                    Uri imageUri = Uri.parse(jsonInString);
                    sdvPostImage.setImageURI(imageUri);
                    tvLike.setText(mRidingDestinationDetailsClickInfo.getLikes());
                    tvDesc.setText(mRidingDestinationDetailsClickInfo.getDescription());
                    tvEatables.setText(mRidingDestinationDetailsClickInfo.getRestaurant());
                    tvPetrolPump.setText(mRidingDestinationDetailsClickInfo.getPetrolpumps());
                    tvServiceCenter.setText(mRidingDestinationDetailsClickInfo.getServiceStation());
                    tvFirstAid.setText(mRidingDestinationDetailsClickInfo.getHospitals());
                    tvRides.setText("" + mRidingDestinationDetailsClickInfo.getRiders());
                    if(tvRides.getText().toString().matches("0")){
                        GenericDraweeHierarchy hierarchy = sdvDisplayPicture.getHierarchy();
                        hierarchy.setPlaceholderImage(R.color.greycolor);
                        RoundingParams roundingParams = hierarchy.getRoundingParams();
                        //roundingParams.setOverlayColor(R.color.greycolor);
                        roundingParams.setRoundAsCircle(true);
                        hierarchy.setRoundingParams(roundingParams);
                       // sdvDisplayPicture.hierarchy.setRoundingParams(roundingParams);

                }else{
                        GenericDraweeHierarchy hierarchy = sdvDisplayPicture.getHierarchy();
                        hierarchy.setPlaceholderImage(R.color.light_brown);
                        RoundingParams roundingParams = hierarchy.getRoundingParams();
                        //roundingParams.setOverlayColor(R.color.greycolor);
                        roundingParams.setRoundAsCircle(true);
                        hierarchy.setRoundingParams(roundingParams);
                    }

                    tvReviews.setPaintFlags(tvReviews.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                    tvReviews.setText(mRidingDestinationDetailsClickInfo.getReviewsCount()+" reviews & opinions");

                    // tvOffers.setText(mRidingDestinationDetailsClickInfo.getOffers());
                    if (mRidingDestinationDetailsClickInfo.getFavroite().matches("1")) {
                        ivFavorites.setImageResource(R.drawable.icon_remove_favorite);
                    } else {
                        ivFavorites.setImageResource(R.drawable.icon_add_favorites);
                    }

                   // Log.e("is Like",mRidingDestinationDetailsClickInfo.getIsLike());
                    if (mRidingDestinationDetailsClickInfo.getIsLike().matches("1")) {
                        tvLike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_like, 0, 0, 0);
                    } else {
                        tvLike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_like_gray, 0, 0, 0);
                    }





                    if(mRidingDestinationDetailsClickInfo.getRating().matches("0.5")){
                        ivStar1.setVisibility(View.VISIBLE);
                        ivStar1.setImageResource(R.drawable.ic_rating_orange_half);
                        ivStar2.setImageResource(R.drawable.ic_ratting_grey);
                        ivStar3.setImageResource(R.drawable.ic_ratting_grey);
                        ivStar4.setImageResource(R.drawable.ic_ratting_grey);
                        ivStar5.setImageResource(R.drawable.ic_ratting_grey);
                    }
                   else if (mRidingDestinationDetailsClickInfo.getRating().matches("1.0")||mRidingDestinationDetailsClickInfo.getRating().matches("1")) {
                        ivStar1.setVisibility(View.VISIBLE);
                        ivStar1.setImageResource(R.drawable.ic_rating_orange);
                        ivStar2.setImageResource(R.drawable.ic_ratting_grey);
                        ivStar3.setImageResource(R.drawable.ic_ratting_grey);
                        ivStar4.setImageResource(R.drawable.ic_ratting_grey);
                        ivStar5.setImageResource(R.drawable.ic_ratting_grey);
                    }
                    else if (mRidingDestinationDetailsClickInfo.getRating().matches("1.5")) {
                        ivStar1.setVisibility(View.VISIBLE);
                        ivStar1.setImageResource(R.drawable.ic_rating_orange);
                        ivStar2.setImageResource(R.drawable.ic_rating_orange_half);
                        ivStar3.setImageResource(R.drawable.ic_ratting_grey);
                        ivStar4.setImageResource(R.drawable.ic_ratting_grey);
                        ivStar5.setImageResource(R.drawable.ic_ratting_grey);
                    }
                    else if (mRidingDestinationDetailsClickInfo.getRating().matches("2.0")||mRidingDestinationDetailsClickInfo.getRating().matches("2")) {
                        ivStar1.setVisibility(View.VISIBLE);
                        ivStar2.setVisibility(View.VISIBLE);
                        ivStar1.setImageResource(R.drawable.ic_rating_orange);
                        ivStar2.setImageResource(R.drawable.ic_rating_orange);
                        ivStar3.setImageResource(R.drawable.ic_ratting_grey);
                        ivStar4.setImageResource(R.drawable.ic_ratting_grey);
                        ivStar5.setImageResource(R.drawable.ic_ratting_grey);
                    }
                    else if (mRidingDestinationDetailsClickInfo.getRating().matches("2.5")) {
                        ivStar1.setVisibility(View.VISIBLE);
                        ivStar2.setVisibility(View.VISIBLE);
                        ivStar1.setImageResource(R.drawable.ic_rating_orange);
                        ivStar2.setImageResource(R.drawable.ic_rating_orange);
                        ivStar3.setImageResource(R.drawable.ic_rating_orange_half);
                        ivStar4.setImageResource(R.drawable.ic_ratting_grey);
                        ivStar5.setImageResource(R.drawable.ic_ratting_grey);
                    }

                    else if (mRidingDestinationDetailsClickInfo.getRating().matches("3.0")||mRidingDestinationDetailsClickInfo.getRating().matches("3")) {
                        ivStar1.setVisibility(View.VISIBLE);
                        ivStar2.setVisibility(View.VISIBLE);
                        ivStar3.setVisibility(View.VISIBLE);
                        ivStar1.setImageResource(R.drawable.ic_rating_orange);
                        ivStar2.setImageResource(R.drawable.ic_rating_orange);
                        ivStar3.setImageResource(R.drawable.ic_rating_orange);
                        ivStar4.setImageResource(R.drawable.ic_ratting_grey);
                        ivStar5.setImageResource(R.drawable.ic_ratting_grey);
                    }
                    else if (mRidingDestinationDetailsClickInfo.getRating().matches("3.5")) {
                        ivStar1.setVisibility(View.VISIBLE);
                        ivStar2.setVisibility(View.VISIBLE);
                        ivStar3.setVisibility(View.VISIBLE);
                        ivStar1.setImageResource(R.drawable.ic_rating_orange);
                        ivStar2.setImageResource(R.drawable.ic_rating_orange);
                        ivStar3.setImageResource(R.drawable.ic_rating_orange);
                        ivStar4.setImageResource(R.drawable.ic_rating_orange_half);
                        ivStar5.setImageResource(R.drawable.ic_ratting_grey);
                    }
                    else if (mRidingDestinationDetailsClickInfo.getRating().matches("4.0")||mRidingDestinationDetailsClickInfo.getRating().matches("4")) {
                        ivStar1.setVisibility(View.VISIBLE);
                        ivStar2.setVisibility(View.VISIBLE);
                        ivStar3.setVisibility(View.VISIBLE);
                        ivStar4.setVisibility(View.VISIBLE);
                        ivStar1.setImageResource(R.drawable.ic_rating_orange);
                        ivStar2.setImageResource(R.drawable.ic_rating_orange);
                        ivStar3.setImageResource(R.drawable.ic_rating_orange);
                        ivStar4.setImageResource(R.drawable.ic_rating_orange);
                        ivStar5.setImageResource(R.drawable.ic_ratting_grey);
                    }
                    else if (mRidingDestinationDetailsClickInfo.getRating().matches("4.5")) {
                        ivStar1.setVisibility(View.VISIBLE);
                        ivStar2.setVisibility(View.VISIBLE);
                        ivStar3.setVisibility(View.VISIBLE);
                        ivStar4.setVisibility(View.VISIBLE);
                        ivStar1.setImageResource(R.drawable.ic_rating_orange);
                        ivStar2.setImageResource(R.drawable.ic_rating_orange);
                        ivStar3.setImageResource(R.drawable.ic_rating_orange);
                        ivStar4.setImageResource(R.drawable.ic_rating_orange);
                        ivStar5.setImageResource(R.drawable.ic_rating_orange_half);
                    }
                    else if (mRidingDestinationDetailsClickInfo.getRating().matches("5.0")||mRidingDestinationDetailsClickInfo.getRating().matches("5")) {
                        ivStar1.setVisibility(View.VISIBLE);
                        ivStar2.setVisibility(View.VISIBLE);
                        ivStar3.setVisibility(View.VISIBLE);
                        ivStar4.setVisibility(View.VISIBLE);
                        ivStar5.setVisibility(View.VISIBLE);
                        ivStar1.setImageResource(R.drawable.ic_rating_orange);
                        ivStar2.setImageResource(R.drawable.ic_rating_orange);
                        ivStar3.setImageResource(R.drawable.ic_rating_orange);
                        ivStar4.setImageResource(R.drawable.ic_rating_orange);
                        ivStar5.setImageResource(R.drawable.ic_rating_orange);
                    } else {
                        ivStar1.setImageResource(R.drawable.ic_ratting_grey);
                        ivStar2.setImageResource(R.drawable.ic_ratting_grey);
                        ivStar3.setImageResource(R.drawable.ic_ratting_grey);
                        ivStar4.setImageResource(R.drawable.ic_ratting_grey);
                        ivStar5.setImageResource(R.drawable.ic_ratting_grey);
                    }
                    dismissProgressDialog();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            hideKeyboard();
                        }
                    }, 300);
                    GPSService mGPSService = new GPSService(DestinationsDetailActivity.this);
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
                        //Toast.makeText(getApplicationContext(), "Latitude:" + latitude + " | Longitude: " + longitude, Toast.LENGTH_LONG).show();

                        String s11 = "" + Html.fromHtml("" + latitude);
                        String s122 = "" + Html.fromHtml("" + longitude);
                        fromstartlat = s11;
                        fromendlong = s122;
                        start = Double.valueOf(s11.trim()).doubleValue();
                        end = Double.valueOf(s122.trim()).doubleValue();
                        mString_start = new LatLng(start, end);

                        Log.e("start:", "" + mString_start);
                        if(mGPSService.getLocationAddress().matches("No address")){
                            tvPlace3.setText(mGPSService.getLocationAddress());
                        }
                        else if(mGPSService.getLocationAddress().matches("Location Not available")){
                            tvPlace3.setText(mGPSService.getLocationAddress());
                        }
                        else{
                            tvPlace3.setText(mGPSService.getLocationAddress());
                        }


                    }


                    // make sure you close the gps after using it. Save user's battery power
                    mGPSService.closeGPS();
                    if(tvPlace3.getText().toString().matches("No address")){
                        tvPlace3.setText("");
                    }
                    else if(tvPlace3.getText().toString().matches("Location Not available")){
                        tvPlace3.setText("");
                    }
                    else{
                        try {
                            GetRouteTask getRoute = new GetRouteTask();
                            getRoute.execute();
                        }catch(Exception e){

                        }
                    }


                    // mRidingDestinationDetailses.addAll(mRidingDestination.getData());
                    // rvDestinations.setAdapter(new AdapterDestination(DestinationsDetailActivity.this,mRidingDestinationDetailses));
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




    /**
     * Like Details info .
     */
    public void Like() {
        showProgressDialog();
        Log.e("riding destination", "riding destination");
        try {
            prefsManager = new PrefsManager(DestinationsDetailActivity.this);
            AccessToken = prefsManager.getToken();
            UserId = prefsManager.getCaseId();
            Log.e("AccessToken:", "" + AccessToken + "----UserId----" + UserId);

            RequestQueue requestQueue = Volley.newRequestQueue(DestinationsDetailActivity.this);
            RequestJsonObject loginTaskRequest = new RequestJsonObject(Request.Method.POST,
                    ApplicationGlobal.ROOT + ApplicationGlobal.baseurl_like + "userId=" + UserId + "&destId=" + ApplicationGlobal.DestID + "&accessToken=" + AccessToken, null,
                    volleyModelLikeErrorListener(), volleyModelLikeSuccessListener()
            );

            requestQueue.add(loginTaskRequest);

        } catch (Exception e) {
            e.printStackTrace();
            Rollbar.reportException(e, "minor", "DestinationDetailActivity get Like detail API");

        }
    }

    /**
     * Implement success listener on execute api url.
     */
    public Response.Listener<JSONObject> volleyModelLikeSuccessListener() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("Model response:", "" + response);

                Type type = new TypeToken<Like>() {
                }.getType();
                Like like = new Gson().fromJson(response.toString(), type);
                LikeDetails mLikeDetails;

                // mRidingDestinationDetailses.clear();


                if (like.getSuccess().equals("1")) {
                    mLikeDetails = like.getData();

                    if(mLikeDetails.getResponse().equals("1")){
                        tvLike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_like, 0, 0, 0);
                    }else{
                        tvLike.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_like_gray, 0, 0, 0);
                    }
                    tvLike.setText(mLikeDetails.getLikes());


                    dismissProgressDialog();
                    // mRidingDestinationDetailses.addAll(mRidingDestination.getData());
                    // rvDestinations.setAdapter(new AdapterDestination(DestinationsDetailActivity.this,mRidingDestinationDetailses));
                }

            }
        };
    }

    /**
     * Implement Volley error listener here.
     */
    public Response.ErrorListener volleyModelLikeErrorListener() {
        dismissProgressDialog();
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error: ", "" + error);
            }
        };
    }

    /**
     * Favorite Details info .
     */
    public void Favorite() {
        showProgressDialog();
        Log.e("riding destination", "riding destination");
        try {
            prefsManager = new PrefsManager(DestinationsDetailActivity.this);
            AccessToken = prefsManager.getToken();
            UserId = prefsManager.getCaseId();
            Log.e("AccessToken:", "" + AccessToken + "----UserId----" + UserId);

            RequestQueue requestQueue = Volley.newRequestQueue(DestinationsDetailActivity.this);
            RequestJsonObject loginTaskRequest = new RequestJsonObject(Request.Method.POST,
                    ApplicationGlobal.ROOT + ApplicationGlobal.baseurl_favroite + "userId=" + UserId + "&destId=" + ApplicationGlobal.DestID + "&accessToken=" + AccessToken, null,
                    volleyModelFavoriteErrorListener(), volleyModelFavoriteSuccessListener()
            );

            requestQueue.add(loginTaskRequest);

        } catch (Exception e) {
            e.printStackTrace();
            Rollbar.reportException(e, "minor", "DestinationDetailActivity get Favorite detail API");

        }
    }

    /**
     * Implement success listener on execute api url.
     */
    public Response.Listener<JSONObject> volleyModelFavoriteSuccessListener() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("Model response:", "" + response);

                Type type = new TypeToken<FavoriteDestination>() {
                }.getType();
                FavoriteDestination mFavoriteDestination = new Gson().fromJson(response.toString(), type);


                if (mFavoriteDestination.getSuccess().equals("1")) {
                    if (mFavoriteDestination.getFav().matches("1")) {
                        ivFavorites.setImageResource(R.drawable.icon_remove_favorite);
                    } else {
                        ivFavorites.setImageResource(R.drawable.icon_add_favorites);
                    }


                    // tvLike.setText(mLikeDetails.getLikes());

                    dismissProgressDialog();
                    // mRidingDestinationDetailses.addAll(mRidingDestination.getData());
                    // rvDestinations.setAdapter(new AdapterDestination(DestinationsDetailActivity.this,mRidingDestinationDetailses));
                }

            }
        };
    }

    /**
     * Implement Volley error listener here.
     */
    public Response.ErrorListener volleyModelFavoriteErrorListener() {
        dismissProgressDialog();
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error: ", "" + error);
            }
        };
    }

    @Override
    public void onConnected(Bundle bundle) {
        _ArrayAdapter.setGoogleApiClient(mGoogleApiClient);
        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
        Log.i(LOG_TAG, "Google Places API connected.");
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(LOG_TAG, "Google Places API connection failed with error code: "
                + connectionResult.getErrorCode());

        Toast.makeText(this,
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mGoogleApiClient.connect();

    }

    @Override
    protected void onStop() {
        super.onStop();

        mGoogleApiClient.disconnect();
    }

    private void initializeMap() {
        if (map == null) {
            map = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getMap();

            // check if map is created successfully or not
            if (map == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            } else {

                // map.setMyLocationEnabled(true);
                map.getUiSettings().setZoomControlsEnabled(true);
                map.getUiSettings().setCompassEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(true);
                map.getUiSettings().setAllGesturesEnabled(true);
                map.setTrafficEnabled(true);

            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initializeMap();
        if (mDrawerLayout.isDrawerOpen(lvSlidingMenu)) {
            mDrawerLayout.closeDrawer(lvSlidingMenu);
            showProfileImage();
        }
    }

    /**
     * @author Amit Agnihotri
     *         This class Get Route on the map
     */
    private class GetRouteTask extends AsyncTask<String, Void, String> {

        String response = "";
        private ProgressDialog Dialog;

        @Override
        protected void onPreExecute() {
            Dialog = new ProgressDialog(DestinationsDetailActivity.this);
            Dialog.setMessage("Loading route...");
            Dialog.show();
        }

        @Override
        protected String doInBackground(String... urls) {
            //Get All Route values
            document = v2GetRouteDirection.getDocument(mString_start, mString_end, GMapV2GetRouteDirection.MODE_DRIVING);
            response = "Success";
            return response;

        }

        @Override
        protected void onPostExecute(String result) {
            map.clear();
            if (response.equalsIgnoreCase("Success")) {
                ArrayList<LatLng> directionPoint = v2GetRouteDirection.getDirection(document);
                PolylineOptions rectLine = new PolylineOptions().width(10).color(
                        Color.parseColor("#D1622A"));

                for (int i = 0; i < directionPoint.size(); i++) {
                    rectLine.add(directionPoint.get(i));
                }
                LatLng latLng = new LatLng(start, end);

                // Adding route on the map
                map.addPolyline(rectLine);
                CameraUpdate cameraUpdate=null;
                if( CalculationByDistance(mString_start,mString_end)<=50) {
                    cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 10);
                }else if(CalculationByDistance(mString_start,mString_end)<=100&&CalculationByDistance(mString_start,mString_end)>50){
                    cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 9);
                }else{
                    cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 7);
                }
              //  CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 9);

                map.animateCamera(cameraUpdate);
                for (int i = 1; i < 3; i++) {
                    if (i == 1) {
                        addMarker(mString_start, 1);
                    } else if (i == 2) {
                        addMarker(mString_end, 2);
                    }

                }
            }
            getCount();
            try {
                Dialog.dismiss();
            }catch (Exception e){

            }
                scView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //replace this line to scroll up or down
                    scView.fullScroll(ScrollView.FOCUS_UP);
                }
            }, 1000);
        }
    }

    /**
     * @author Amit Agnihotri
     *         This class Get Route on the map
     */
    private class GetUpdateRouteTask extends AsyncTask<String, Void, String> {

        String response = "";
        private ProgressDialog Dialog;

        @Override
        protected void onPreExecute() {
            Dialog = new ProgressDialog(DestinationsDetailActivity.this);
            Dialog.setMessage("Loading route...");
            Dialog.show();
        }

        @Override
        protected String doInBackground(String... urls) {
            //Get All Route values
            document = v2GetRouteDirection.getDocument(mString_start, mString_end, GMapV2GetRouteDirection.MODE_DRIVING);
            response = "Success";
            return response;

        }

        @Override
        protected void onPostExecute(String result) {
            map.clear();
            if (response.equalsIgnoreCase("Success")) {
                ArrayList<LatLng> directionPoint = v2GetRouteDirection.getDirection(document);
                PolylineOptions rectLine = new PolylineOptions().width(10).color(
                        Color.parseColor("#D1622A"));

                for (int i = 0; i < directionPoint.size(); i++) {
                    rectLine.add(directionPoint.get(i));
                }
                LatLng latLng = new LatLng(start, end);

                // Adding route on the map
                map.addPolyline(rectLine);
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 9);
                map.animateCamera(cameraUpdate);
                for (int i = 1; i < 4; i++) {
                    if (i == 1) {
                        addUpdateMarker(mString_start, 1);
                    } else if (i == 2) {
                        addUpdateMarker(mString_end, 2);
                    } else if (i == 3) {
                        addUpdateMarker(mStringCurrent, 3);
                    }
                }
            }
            rv_properties.setVisibility(View.VISIBLE);
            getCount();
            Dialog.dismiss();
        }
    }

    public void addMarker(LatLng latLng, int pos) {
        MarkerOptions marker = null;
        View view = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker_layout, null);
        FrameLayout fmlayout = (FrameLayout) view.findViewById(R.id.fmlayout);
        ImageView ivMapImage = (ImageView) view.findViewById(R.id.ivMapImage);
        if (pos == 1) {
            fmlayout.setBackgroundResource(R.drawable.ic_endlocation);
        } else {
            fmlayout.setBackgroundResource(R.drawable.ic_your_locationstart);
            if (TextUtils.isEmpty(imageUrl)) {
                ivMapImage.setImageResource(R.drawable.app_icon);
            } else {
//                Bitmap bmp = BitmapFactory.decodeFile(file.getAbsolutePath());
//                Log.e("bmp:", "" + bmp);
//                Bitmap bitmap = getRoundedShape(bmp);
//                Log.e("bitmap:", "" + bitmap);
                Log.e("url",""+prefsManager.getImageUrl());
                // ivMapImage.setImageBitmap(getBitmapFromURL(prefsManager.getImageUrl()));
                Picasso.with(DestinationsDetailActivity.this).load(prefsManager.getImageUrl()).transform(new CircleTransform()).into(ivMapImage);
            }

        }

        //ivMapImage.setImageBitmap(url1.get(position));
        // create marker
        marker = new MarkerOptions().position(latLng).title("");
        // Changing marker icon
        marker.getPosition();
        marker.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, view)));
        // adding marker
        //marker.snippet(lat.get(position));
        map.addMarker(marker);
        if (pos == 2) {

        } else {
            CameraPosition cameraPosition=null;
            if( CalculationByDistance(mString_start,mString_end)<=50) {
                cameraPosition = new CameraPosition.Builder().target(
                        latLng).zoom(9).build();
            }else if(CalculationByDistance(mString_start,mString_end)<=100&&CalculationByDistance(mString_start,mString_end)>50){
                cameraPosition = new CameraPosition.Builder().target(
                        latLng).zoom(8).build();
            }else{
                cameraPosition = new CameraPosition.Builder().target(
                        latLng).zoom(6).build();
            }
//            CameraPosition cameraPosition = new CameraPosition.Builder().target(
//                    latLng).zoom(5).build();
            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            map.getUiSettings().setZoomControlsEnabled(true);
        }


        // Toast.makeText(MapActivity.this, "Matched: "+ridername.get(position) +"---Id--" +UserId, Toast.LENGTH_LONG).show();

        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);


        // check if map is created successfully or not
        if (map == null) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                    .show();

        }


    }

    public void addUpdateMarker(LatLng latLng, int pos) {
        MarkerOptions marker = null;
        View view = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker_layout, null);
        FrameLayout fmlayout = (FrameLayout) view.findViewById(R.id.fmlayout);
        ImageView ivMapImage = (ImageView) view.findViewById(R.id.ivMapImage);
        if (pos == 1) {
            fmlayout.setBackgroundResource(R.drawable.ic_endlocation);
        } else if (pos == 2) {
            fmlayout.setBackgroundResource(R.drawable.ic_endlocation);
        } else {
            fmlayout.setBackgroundResource(R.drawable.ic_your_locationstart);
            if (TextUtils.isEmpty(imageUrl)) {
                ivMapImage.setImageResource(R.drawable.app_icon);
            }else {
//
//                Bitmap bmp = BitmapFactory.decodeFile(file.getAbsolutePath());
//                Log.e("bmp:", "" + bmp);
//                Bitmap bitmap = getRoundedShape(bmp);
//                Log.e("bitmap:", "" + bitmap);
                ivMapImage.setImageBitmap(getBitmapFromURL(prefsManager.getImageUrl()));
            }

        }

        //ivMapImage.setImageBitmap(url1.get(position));
        // create marker
        marker = new MarkerOptions().position(latLng).title("");
        // Changing marker icon
        marker.getPosition();
        marker.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, view)));
        // adding marker
        //marker.snippet(lat.get(position));
        map.addMarker(marker);
        if (pos == 2) {

        } else {
            CameraPosition cameraPosition = new CameraPosition.Builder().target(
                    latLng).zoom(5).build();
            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            map.getUiSettings().setZoomControlsEnabled(true);
        }


        // Toast.makeText(MapActivity.this, "Matched: "+ridername.get(position) +"---Id--" +UserId, Toast.LENGTH_LONG).show();

        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);


        // check if map is created successfully or not
        if (map == null) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                    .show();

        }


    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (Exception e) {
            e.printStackTrace();
            Rollbar.reportException(e, "minor", "DestinationDetailActivity getBitmapFromURL method");
            return null;
        }
    }
    // Convert a view to bitmap
    public static Bitmap createDrawableFromView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }

    ///this function is for get rounded bitmap
    public Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
        int targetWidth = 70;
        int targetHeight = 70;
        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth,
                targetHeight, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(targetBitmap);
        Path path = new Path();
        path.addCircle(((float) targetWidth - 1) / 2,
                ((float) targetHeight - 1) / 2,
                (Math.min(((float) targetWidth),
                        ((float) targetHeight)) / 2),
                Path.Direction.CCW);

        canvas.clipPath(path);
        Bitmap sourceBitmap = scaleBitmapImage;
        canvas.drawBitmap(sourceBitmap,
                new Rect(0, 0, sourceBitmap.getWidth(),
                        sourceBitmap.getHeight()),
                new Rect(0, 0, targetWidth, targetHeight), null);
        return targetBitmap;
    }


    /**
     * modifyYourBike Data (Only Strings) to Server
     **/
    private void getCount() {

        String accessToken = prefsManager.getToken();

        FileUploadService service = ServiceGenerator.createService(FileUploadService.BASE_URL);
        Log.e("BasLat:", "" + tvPlace3.getText().toString());
        Log.e("BasLon:", "" + tvPlace4.getText().toString());
        Log.e("accessToken:", "" + accessToken);

        service.getCounter(tvPlace3.getText().toString(), tvPlace4.getText().toString(), accessToken, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, retrofit.client.Response response) {
                Log.e("jsonObject:------", "" + jsonObject);
                Type type = new TypeToken<GetCount>() {
                }.getType();
                GetCount
                        modilyBike = new Gson().fromJson(jsonObject.toString(), type);
                Log.e("jsonObject:", "" + jsonObject);

                if (modilyBike.getSuccess() == 1) {
                    dismissProgressDialog();
                    getCountData = modilyBike.getData();
                    tv_Eatables.setText(getCountData.getResturants());
                    tv_PetrolPump.setText(getCountData.getGasStation());
                    tv_ServiceCenter.setText(getCountData.getRepair());
                    tv_FirstAid.setText(getCountData.getDoctor());
                   // fmClick.setVisibility(View.GONE);
                } else {
                    rv_properties.setVisibility(View.GONE);
                    dismissProgressDialog();
                    //CustomDialog.showProgressDialog(DestinationsDetailActivity.this, modilyBike.getMessage().toString());
                }


            }

            @Override
            public void failure(RetrofitError error) {
                dismissProgressDialog();
                rv_properties.setVisibility(View.GONE);
                Log.e("DataUploading------", "Data Uploading Failure......" + error);

            }
        });
    }
}
