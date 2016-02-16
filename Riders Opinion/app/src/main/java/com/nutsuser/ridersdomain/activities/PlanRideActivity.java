package com.nutsuser.ridersdomain.activities;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolylineOptions;
import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.adapter.CustomGridAdapter;
import com.nutsuser.ridersdomain.adapter.EndPlaceArrayAdapter;
import com.nutsuser.ridersdomain.adapter.PlaceArrayAdapter;
import com.nutsuser.ridersdomain.adapter.PlaceEndRouteHault;
import com.nutsuser.ridersdomain.adapter.PlaceStartRouteHault;
import com.nutsuser.ridersdomain.route.GMapV2GetRouteDirection;
import com.nutsuser.ridersdomain.view.BetterPopupWindow;
import org.w3c.dom.Document;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.StringTokenizer;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by user on 9/2/2015.
 */
public class PlanRideActivity extends BaseActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {

    DemoPopupWindow dw;
    private ArrayList<String> ridetype = new ArrayList<String>();
    CustomBaseAdapter adapter;

    boolean rideTypeChoose=false;

    // Variable for storing current date and time
    private int mYear, mMonth, mDay, mHour, mMinute,AMPM;

    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));
    private static final String LOG_TAG = "PlanRideActivity";
    private static final int GOOGLE_API_CLIENT_ID = 0;
     @Bind(R.id.tv_StartTagDate)
    TextView tv_StartTagDate;
    @Bind(R.id.tv_EndTagDate)
    TextView tv_EndTagDate;
    @Bind(R.id.fmStartDate)
    FrameLayout fmStartDate;
    @Bind(R.id.fmEndDate)
    FrameLayout fmEndDate;
    @Bind(R.id.edPlace4)
    AutoCompleteTextView edPlace4;
    @Bind(R.id.tv_rideTag)
    TextView tv_rideTag;
    @Bind(R.id.edPlace3)
    AutoCompleteTextView edPlace3;
    String amPM="";
    /*@Bind(R.id.tvMeetAndPlanRide)
    TextView tvMeetAndPlanRide;
    @Bind(R.id.tvHealthyRiding)
    TextView tvHealthyRiding;
    @Bind(R.id.tvGetDirections)
    TextView tvGetDirections;
    @Bind(R.id.tvNotifications)
    TextView tvNotifications;
    @Bind(R.id.tvSettings)
    TextView tvSettings;*/
    //  public static String [] prgmNameList={"Riding Destinations","Meet 'N' Plan A Ride","Riding Events \n    ","Modifly Your Bikes","Healthy Riding","Get Directions","Notifications","Settings"};
    //public static int [] prgmImages={R.drawable.icon_menu_destination,R.drawable.icon_menu_meetplan,R.drawable.icon_menu_events,R.drawable.icon_modifybike,R.drawable.icon_menu_healthy_riding,R.drawable.icon_menu_get_direction,R.drawable.icon_menu_notification,R.drawable.icon_menu_settings};
    //  public static Class [] classList={DestinationsListActivity.class,PlanRideActivity.class,EventsListActivity.class,ModifyBikeActivity.class,HealthyRidingActivity.class,GetDirections.class,NotificationScreen.class,SettingsActivity.class};
    public static String[] prgmNameList = {"My Rides", "My Messages", "My Friends", "Chats", "Favourite Destination", "Notifications", "Settings", "    \n"};
    public static int[] prgmImages = {R.drawable.ic_menu_fav_destinations, R.drawable.ic_menu_my_messages, R.drawable.ic_menu_my_friends, R.drawable.ic_menu_menu_chats, R.drawable.ic_menu_fav_destinations, R.drawable.ic_menu_menu_notifications, R.drawable.ic_menu_menu_settings, R.drawable.ic_menu_menu_blank_icon};
    public static Class[] classList = {MyRidesRecyclerView.class, ChatListScreen.class, MyFriends.class, ChatListScreen.class, FavouriteDesination.class, Notification.class, SettingsActivity.class, SettingsActivity.class};
    GoogleMap mGoogleMap;
    GMapV2GetRouteDirection v2GetRouteDirection;
    LocationManager locManager;
    Drawable drawable;
    Document document;
    @Bind(R.id.ivMap)
    ImageView ivMap;
    @Bind(R.id.gridView1)
    GridView gridView1;
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
    EndPlaceArrayAdapter _ArrayAdapter;
    LatLng mString_end;
    LatLng mString_start;
    double start, end;
    private Activity activity;
    private AutoCompleteTextView tvPlace1, tvPlace2;
    private GoogleApiClient mGoogleApiClient;
    private PlaceArrayAdapter mPlaceArrayAdapter;
    private PlaceStartRouteHault placeStartRouteHault;
    private PlaceEndRouteHault placeEndRouteHault;
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback_start;
    LatLng mString_endroutehault;
    LatLng mString_startroutehault;


    String fromstartlat,fromendlong,fromloactionnanem,tostartlat,toendlong,tolocationnane,fromroutehaultlat,fromroutehaultlong,fromroutehaultlocation,toroutehaultlat,toroutehaultlong,toroutehaultlocationnanme;


    private AdapterView.OnItemClickListener mAutocompleteClickListener_routehaultSTART
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            Log.i(LOG_TAG, "Selected: " + item.description);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback_routehaultSTART);
            Log.i(LOG_TAG, "Fetching details for ID: " + item.placeId);
        }
    };
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback_routehaultSTART
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
fromroutehaultlat=s;
            fromroutehaultlong=s1;

            mString_startroutehault = new LatLng(start, end);


            Log.e("startroutehault:", "" + mString_startroutehault);

            if (attributions != null) {
                // mAttTextView.setText(Html.fromHtml(attributions.toString()));
            }
        }

    };



    private AdapterView.OnItemClickListener mAutocompleteClickListener_routehaultEND
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            Log.i(LOG_TAG, "Selected: " + item.description);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback_routehaultEND);
            Log.i(LOG_TAG, "Fetching details for ID: " + item.placeId);
        }
    };
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback_routehaultEND
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

toroutehaultlat=s;
            toroutehaultlong=s1;
            mString_endroutehault = new LatLng(start, end);


            Log.e("endroutehault:", "" + mString_endroutehault);

            if (attributions != null) {
                // mAttTextView.setText(Html.fromHtml(attributions.toString()));
            }
        }

    };





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
            tostartlat=s;
            toendlong=s1;
            double start = Double.valueOf(s.trim()).doubleValue();
            double end = Double.valueOf(s1.trim()).doubleValue();


            mString_end = new LatLng(start, end);
            //hideKeyboard();
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
                fromstartlat=s;
                fromendlong=s1;
                start = Double.valueOf(s.trim()).doubleValue();
                end = Double.valueOf(s1.trim()).doubleValue();
                mString_start = new LatLng(start, end);

                Log.e("start:", "" + mString_start);

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
        setContentView(R.layout.activity_plan_ride);
        v2GetRouteDirection = new GMapV2GetRouteDirection();
        mGoogleMap = ((MapFragment) getFragmentManager().findFragmentById(
                R.id.map)).getMap();

        // Enabling MyLocation in Google Map
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        mGoogleMap.getUiSettings().setCompassEnabled(true);
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        mGoogleMap.getUiSettings().setAllGesturesEnabled(true);
        mGoogleMap.setTrafficEnabled(true);
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(2));
        activity = this;
        ButterKnife.bind(this);
        setupActionBar();
        setFonts();
        // ATTENTION: This "addApi(AppIndex.API)"was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mGoogleApiClient = new GoogleApiClient.Builder(PlanRideActivity.this)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
                .addConnectionCallbacks(this)
                .addApi(AppIndex.API).build();
        tvPlace1 = (AutoCompleteTextView) findViewById(R.id
                .tvPlace1);
        tvPlace2 = (AutoCompleteTextView) findViewById(R.id
                .tvPlace2);
        tvPlace2.setThreshold(1);
        tvPlace1.setThreshold(1);
        tvTitleToolbar.setText("MEET & PLAN A RIDE");
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

        tvPlace1.setOnItemClickListener(mAutocompleteClickListener_start);
        mPlaceArrayAdapter = new PlaceArrayAdapter(this, android.R.layout.simple_list_item_1,
                BOUNDS_MOUNTAIN_VIEW, null);
        tvPlace1.setAdapter(mPlaceArrayAdapter);
        tvPlace2.setOnItemClickListener(mAutocompleteClickListener_end);
        _ArrayAdapter = new EndPlaceArrayAdapter(this, android.R.layout.simple_list_item_1,
                BOUNDS_MOUNTAIN_VIEW, null);
        tvPlace2.setAdapter(_ArrayAdapter);
        edPlace3.setThreshold(1);
        edPlace3.setOnItemClickListener(mAutocompleteClickListener_routehaultSTART);
        placeStartRouteHault = new PlaceStartRouteHault(this, android.R.layout.simple_list_item_1,
                BOUNDS_MOUNTAIN_VIEW, null);
        edPlace3.setAdapter(placeStartRouteHault);
        edPlace4.setThreshold(1);
        edPlace4.setOnItemClickListener(mAutocompleteClickListener_routehaultEND);
        placeEndRouteHault=new PlaceEndRouteHault(this, android.R.layout.simple_list_item_1,
                BOUNDS_MOUNTAIN_VIEW, null);
        edPlace4.setAdapter(placeEndRouteHault);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                hideKeyboard();
            }
        }, 300);
        ridetype.clear();
        ridetype.add("Breakfast Ride");
        ridetype.add("Overnight Ride");
        tvPlace2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                    //do something
                    tvPlace2.clearFocus();
                    hideKeyboard();
                    if (tvPlace2.getText().toString().trim().equals(""))

                        showToast("Please enter end destination");
                    else if (tvPlace1.getText().toString().trim().equals(""))

                        showToast("Please enter start destination");
                    else {
                        if (isNetworkConnected()) {
                            try {
                                GetRouteTask getRoute = new GetRouteTask();
                                getRoute.execute();
                            } catch (Exception e) {

                            }
                        } else {
                            showToast("Internet Not Connected");
                        }
                    }
                }

                return false;
            }
        });

    }

    public void intentCalling(Class name) {
        Intent mIntent = new Intent(PlanRideActivity.this, name);
        startActivity(mIntent);

    }

    private void setupActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_back);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void setFonts() {
        tvTitleToolbar.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
        tvName.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mGoogleApiClient.disconnect();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.tvNext, R.id.ivMenu, R.id.rlProfile, R.id.ivMap,R.id.tv_rideTag,R.id.fmStartDate,R.id.fmEndDate})
    void onclick(View view) {
        switch (view.getId()) {
            case R.id.tvNext:
                if (isNetworkConnected()) {
                    try {
                      if(rideTypeChoose){
                          boolean approved=Next(tv_rideTag.getText().toString(),tv_StartTagDate.getText().toString(),"",tvPlace1.getText().toString(),tvPlace2.getText().toString(),edPlace3.getText().toString(),"");
                                  if(approved){
                                      StringTokenizer tokens = new StringTokenizer(tv_StartTagDate.getText().toString(), "/");
                                      String first = tokens.nextToken();// this will contain "Fruit"
                                      String second = tokens.nextToken();// this will contain " they taste good"
                                      Intent mIntent=new Intent(PlanRideActivity.this, PlannedRidingsActivity.class);
                                          mIntent.putExtra("rideType",tv_rideTag.getText().toString());
                                          mIntent.putExtra("startdate",first);
                                          mIntent.putExtra("starttime",second);
                                          mIntent.putExtra("fromlatitude",fromstartlat);
                                          mIntent.putExtra("fromlongitude",fromendlong);
                                          mIntent.putExtra("fromlocation",tvPlace1.getText().toString());
                                          mIntent.putExtra("tolatitude",tostartlat);
                                          mIntent.putExtra("tolongitude",toendlong);
                                          mIntent.putExtra("tolocation",tvPlace2.getText().toString());
                                          mIntent.putExtra("hlatitude",fromroutehaultlat);
                                          mIntent.putExtra("hlongitude",fromroutehaultlong);
                                          mIntent.putExtra("hlocation",edPlace3.getText().toString());
                                          mIntent.putExtra("htype","breakfast");
                                          startActivity(mIntent);
                                  }
                          else{
                                      showToast("Please fill all Value");
                                  }
                      }
                        else{
                          boolean approved=Next(tv_rideTag.getText().toString(),tv_StartTagDate.getText().toString(),tv_EndTagDate.getText().toString(),tvPlace1.getText().toString(),tvPlace2.getText().toString(),edPlace3.getText().toString(),edPlace4.getText().toString());
                          if(approved){
                              StringTokenizer tokens = new StringTokenizer(tv_StartTagDate.getText().toString(), "/");
                              String first = tokens.nextToken();// this will contain "Fruit"
                              String second = tokens.nextToken();// this will contain " they taste good"
                              Intent mIntent=new Intent(PlanRideActivity.this, PlannedRidingsActivity.class);
                              mIntent.putExtra("rideType",tv_rideTag.getText().toString());
                              mIntent.putExtra("startdate",first);
                              mIntent.putExtra("starttime",second);
                              mIntent.putExtra("enddate",tv_EndTagDate.getText().toString());
                              mIntent.putExtra("fromlatitude",fromstartlat);
                              mIntent.putExtra("fromlongitude",fromendlong);
                              mIntent.putExtra("fromlocation",tvPlace1.getText().toString());
                              mIntent.putExtra("tolatitude",tostartlat);
                              mIntent.putExtra("tolongitude",toendlong);
                              mIntent.putExtra("tolocation",tvPlace2.getText().toString());
                              mIntent.putExtra("hlatitude",fromroutehaultlat);
                              mIntent.putExtra("hlongitude",fromroutehaultlong);
                              mIntent.putExtra("hlocation",edPlace3.getText().toString());
                              mIntent.putExtra("htype","breakfast");
                              mIntent.putExtra("hlatitude1",toroutehaultlat);
                              mIntent.putExtra("hlongitude1",toroutehaultlong);
                              mIntent.putExtra("hlocation1",edPlace4.getText().toString());
                              mIntent.putExtra("htype1","Overnight Ride");
                                  startActivity(mIntent);
                          }
                          else{
                              showToast("Please fill all Value");
                          }
                      }
                    } catch (Exception e) {

                    }
                } else {
                    showToast("Internet Not Connected");
                }


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
            case R.id.fmStartDate:

                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                mMinute=c.get(Calendar.MINUTE);
                mHour=c.get(Calendar.HOUR);
                AMPM=c.get(Calendar.AM_PM);
                if(AMPM==0){
                    amPM="AM";
                }
                else{
                    amPM="PM";
                }

                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox
                                tv_StartTagDate.setText(" "+dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year+"/"+mHour+":"+mMinute+" "+amPM);

                            }
                        }, mYear, mMonth, mDay);
                dpd.show();
                break;
            case R.id.tv_rideTag:
                dw = new DemoPopupWindow(view, PlanRideActivity.this);
                dw.showLikeQuickAction(0, 0);
                break;
            case R.id.fmEndDate:
                final Calendar c1 = Calendar.getInstance();
                mYear = c1.get(Calendar.YEAR);
                mMonth = c1.get(Calendar.MONTH);
                mDay = c1.get(Calendar.DAY_OF_MONTH);

                // Launch Date Picker Dialog
                DatePickerDialog dpd1 = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox
                                tv_EndTagDate.setText(" "+dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                dpd1.show();
                break;


        }
    }
public boolean Next(String RideType,String startDate,String endDate,String startLocation,String endLocation,String breakfast,String tea){
    if(RideType.matches("")){
        return false;
    }
    else if(RideType.matches("Overnight Ride")){
        if(endDate.matches("")){
            return false;
        }
        else if(startDate.matches("")){
            return false;
        }
        else if(tea.matches("")){
            return false;
        }

    }
    else if(startDate.matches("")){
        return false;
    }
    else if(startLocation.matches("")){
        return false;
    }
    else if(endLocation.matches("")){
        return false;
    }
    else if(breakfast.matches("")){
        return false;
    }
    return true;
}
    @Override
    public void onConnected(Bundle bundle) {
        _ArrayAdapter.setGoogleApiClient(mGoogleApiClient);
        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
        placeStartRouteHault.setGoogleApiClient(mGoogleApiClient);
        placeEndRouteHault.setGoogleApiClient(mGoogleApiClient);
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

    private void hideKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        mGoogleApiClient.disconnect();
    }

    /**
     * @author VIJAYAKUMAR M
     *         This class Get Route on the map
     */
    private class GetRouteTask extends AsyncTask<String, Void, String> {

        String response = "";
        private ProgressDialog Dialog;

        @Override
        protected void onPreExecute() {
            Dialog = new ProgressDialog(PlanRideActivity.this);
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
            mGoogleMap.clear();
            if (response.equalsIgnoreCase("Success")) {
                ArrayList<LatLng> directionPoint = v2GetRouteDirection.getDirection(document);
                PolylineOptions rectLine = new PolylineOptions().width(10).color(
                        Color.parseColor("#D1622A"));

                for (int i = 0; i < directionPoint.size(); i++) {
                    rectLine.add(directionPoint.get(i));
                }
                LatLng latLng = new LatLng(start, end);

                // Adding route on the map
                mGoogleMap.addPolyline(rectLine);
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 9);
                mGoogleMap.animateCamera(cameraUpdate);
                //  mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(12));
                //mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(6),1000, null);
                // markerOptions.position(toPosition);
                // markerOptions.draggable(true);
                // mGoogleMap.addMarker(markerOptions);

            }

            Dialog.dismiss();
        }
    }


    // ************** Class for pop-up window **********************

    /**
     * The Class DemoPopupWindow.
     */
    private class DemoPopupWindow extends BetterPopupWindow {

        /**
         * Instantiates a new demo popup window.
         *
         * @param anchor the anchor
         * @param cnt    the cnt
         */
        public DemoPopupWindow(View anchor, Context cnt) {
            super(anchor);
        }

        /*
         * (non-Javadoc)
         *
         * @see com.cellalert24.Views.BetterPopupWindow#onCreate()
         */
        @Override
        protected void onCreate() {
            // inflate layout
            LayoutInflater inflater = (LayoutInflater) this.anchor.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            ViewGroup root = (ViewGroup) inflater.inflate(
                    R.layout.share_choose_popup, null);

            ListView listview = (ListView) root.findViewById(R.id.listview);
            adapter = new CustomBaseAdapter(PlanRideActivity.this, ridetype);
            listview.setAdapter(adapter);
            Button mButton = (Button) root.findViewById(R.id.cancelBtn);


            mButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub

                    dismiss();

                }
            });

            this.setContentView(root);
        }

    }

    public class CustomBaseAdapter extends BaseAdapter {
        Context context;
        private ArrayList<String> ridetype;

        public CustomBaseAdapter(Context context, ArrayList<String> ridetype) {
            this.ridetype = ridetype;
            this.context = context;

        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.list_item, null);
                holder = new ViewHolder();

                holder.txtTitle = (TextView) convertView.findViewById(R.id.title);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }


            holder.txtTitle.setText(ridetype.get(position));

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(ridetype.get(position).matches("Breakfast Ride")){
                        tv_rideTag.setText(ridetype.get(position));
                        fmEndDate.setVisibility(View.GONE);
                        edPlace4.setVisibility(View.GONE);
                        rideTypeChoose=true;
                        dw.dismiss();
                    }
                    else if(ridetype.get(position).matches("Overnight Ride")){
                        tv_rideTag.setText(ridetype.get(position));
                        fmEndDate.setVisibility(View.VISIBLE);
                        fmStartDate.setVisibility(View.VISIBLE);
                        edPlace4.setVisibility(View.VISIBLE);
                        rideTypeChoose=false;
                        dw.dismiss();
                    }

                }});
            return convertView;
        }

        @Override
        public int getCount() {
            return ridetype.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        private class ViewHolder {

            TextView txtTitle;

        }
    }



}
