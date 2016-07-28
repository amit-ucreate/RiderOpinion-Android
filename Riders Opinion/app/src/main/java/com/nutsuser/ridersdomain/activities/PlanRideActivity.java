package com.nutsuser.ridersdomain.activities;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.appeaser.sublimepickerlibrary.datepicker.SelectedDate;
import com.appeaser.sublimepickerlibrary.helpers.SublimeOptions;
import com.appeaser.sublimepickerlibrary.recurrencepicker.SublimeRecurrencePicker;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.adapter.CustomGridAdapter;
import com.nutsuser.ridersdomain.adapter.EndPlaceArrayAdapter;
import com.nutsuser.ridersdomain.adapter.NotificationAdapter;
import com.nutsuser.ridersdomain.adapter.PlaceArrayAdapter;
import com.nutsuser.ridersdomain.adapter.PlaceEndRouteHault;
import com.nutsuser.ridersdomain.adapter.PlaceStartRouteHault;
import com.nutsuser.ridersdomain.chat.SampleCometChatActivity;
import com.nutsuser.ridersdomain.datetime.SublimePickerFragment;
import com.nutsuser.ridersdomain.route.GMapV2GetRouteDirection;
import com.nutsuser.ridersdomain.services.GPSService;
import com.nutsuser.ridersdomain.utils.CustomizeDialog;
import com.nutsuser.ridersdomain.view.BetterPopupWindow;
import com.nutsuser.ridersdomain.view.CustomDialog;
import com.nutsuser.ridersdomain.web.api.FileUploadService;
import com.nutsuser.ridersdomain.web.api.ServiceGenerator;
import com.nutsuser.ridersdomain.web.pojos.GetCount;
import com.nutsuser.ridersdomain.web.pojos.GetCountData;
import com.nutsuser.ridersdomain.web.pojos.NotificationListResponse;
import com.nutsuser.ridersdomain.web.pojos.PlanARide;
import com.nutsuser.ridersdomain.web.pojos.PlanARideData;
import com.nutsuser.ridersdomain.web.pojos.SaveCounter;
import com.rollbar.android.Rollbar;

import org.w3c.dom.Document;

import java.io.File;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import java.util.StringTokenizer;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;

/**
 * Created by user on 9/2/2015.
 */
public class PlanRideActivity extends BaseActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {

    GetCountData getCountData;
    @Bind(R.id.tvEatables)
    TextView tvEatables;
    @Bind(R.id.tvPetrolPump)
    TextView tvPetrolPump;
    @Bind(R.id.tvServiceCenter)
    TextView tvServiceCenter;
    @Bind(R.id.tvFirstAid)
    TextView tvFirstAid;
    @Bind(R.id.fmClick)
    FrameLayout fmClick;
    // Chosen values
    SelectedDate mSelectedDate;
    //int mHour, mMinute;
    private final int INVALID_VAL = -1;
    int whichdate = 0;

    @Bind(R.id.sdvDp)
    SimpleDraweeView sdvDp;
    @Bind(R.id.btUpdateProfile)
    Button btUpdateProfile;
    private ActionBarDrawerToggle mDrawerToggle;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));
    private static final String LOG_TAG = "PlanRideActivity";
    private static final int GOOGLE_API_CLIENT_ID = 0;
    public static String[] prgmNameList = {"My Rides", "My Messages", "My Friends", "Chats", "Favourite Destination", "Notifications", "Settings", "Riders Near By"};
    public static int[] prgmImages = {R.drawable.ic_menu_my_rides, R.drawable.ic_menu_my_messages, R.drawable.menu_my_friends, R.drawable.ic_menu_menu_chats, R.drawable.menu_fav_destinations, R.drawable.ic_menu_menu_notifications, R.drawable.menu_settings, R.drawable.icon_nearby};
    public static Class[] classList = {MyRidesRecyclerView.class, RecentChatListActivity.class,MyFriendsActivity.class, SampleCometChatActivity.class, FavouriteDesination.class, NotificationListActivity.class, SettingsActivity.class, NearByFriendsAcitivity.class};
    DemoPopupWindow dw;
    CustomBaseAdapter adapter;
    String AccessToken, UserId;
    boolean rideTypeChoose = false;
    Map<String, String> params;
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
    String amPM = "";
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
    LatLng mString_endroutehault;
    LatLng mString_startroutehault;
    CustomizeDialog mCustomizeDialog;
    String fromstartlat, fromendlong, fromloactionnanem, tostartlat, toendlong, tolocationnane, fromroutehaultlat, fromroutehaultlong, fromroutehaultlocation, toroutehaultlat, toroutehaultlong, toroutehaultlocationnanme;
    private ArrayList<String> ridetype = new ArrayList<String>();
    // Variable for storing current date and time
    private int mYear, mMonth, mDay, mHour, mMinute, AMPM;
    private Activity activity;
    private AutoCompleteTextView tvPlace1, tvPlace2;
    private GoogleApiClient mGoogleApiClient;
    private PlaceArrayAdapter mPlaceArrayAdapter;
    private PlaceStartRouteHault placeStartRouteHault;
    private PlaceEndRouteHault placeEndRouteHault;
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback_start;

    @Bind(R.id.mainSV)
    ScrollView mainSV;
    @Bind(R.id.transparent_image)
    ImageView transparent_image;

    Marker marker = null;
    Marker markerTea = null;
    private ProgressDialog Dialog;
    private boolean showDialog = false;
    public static ArrayList<PlanARideData> planARideDatas = new ArrayList<>();

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback_routehaultSTART
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e(LOG_TAG, "Place query did not complete. Error: " +
                        places.getStatus().toString());
                return;
            }
            try {
                // Selecting the first object buffer.
                Place place;

                try {
                    place = places.get(0);
                } catch (IllegalStateException e) {
                    places.release();
                    return;
                }
                CharSequence attributions = places.getAttributions();
                //mString_startroutehault="";
                // mNameTextView.setText("NAME:"+ Html.fromHtml(place.getName() + ""));
                // mAddressTextView.setText("ADDRESS: "+Html.fromHtml(place.getAddress() + ""));
                //  mIdTextView.setText(Html.fromHtml("PLACEID:" + place.getId() + ""));
                String s = "" + Html.fromHtml("" + place.getLatLng().latitude);
                String s1 = "" + Html.fromHtml("" + place.getLatLng().longitude);
                double start = Double.valueOf(s.trim()).doubleValue();
                double end = Double.valueOf(s1.trim()).doubleValue();
                fromroutehaultlat = s;
                fromroutehaultlong = s1;

                mString_startroutehault = new LatLng(start, end);

                addBreakFastMarker(mString_startroutehault);
                hideKeyboard();

                Log.e("startroutehault:", "" + mString_startroutehault);
                edPlace4.setFocusable(true);
                edPlace4.requestFocus();
                if (attributions != null) {
                    // mAttTextView.setText(Html.fromHtml(attributions.toString()));
                }
            }
            catch (Exception e){

            }

        }

    };
    private AdapterView.OnItemClickListener mAutocompleteClickListener_routehaultSTART
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final PlaceStartRouteHault.PlaceAutocomplete item = placeStartRouteHault.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            Log.i(LOG_TAG, "Selected: " + item.description);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback_routehaultSTART);
            Log.i(LOG_TAG, "Fetching details for ID: " + item.placeId);
            hideKeyboard();
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
            try {
                // Selecting the first object buffer.
                Place place;

                try {
                    place = places.get(0);
                } catch (IllegalStateException e) {
                    places.release();
                    return;
                }
                CharSequence attributions = places.getAttributions();
                // mString_endroutehault="";
                // mNameTextView.setText("NAME:"+ Html.fromHtml(place.getName() + ""));
                // mAddressTextView.setText("ADDRESS: "+Html.fromHtml(place.getAddress() + ""));
                //  mIdTextView.setText(Html.fromHtml("PLACEID:" + place.getId() + ""));
                String s = "" + Html.fromHtml("" + place.getLatLng().latitude);
                String s1 = "" + Html.fromHtml("" + place.getLatLng().longitude);
                double start = Double.valueOf(s.trim()).doubleValue();
                double end = Double.valueOf(s1.trim()).doubleValue();

                toroutehaultlat = s;
                toroutehaultlong = s1;
                mString_endroutehault = new LatLng(start, end);
                addmarkerTeaMarker(mString_endroutehault);
                hideKeyboard();
                Log.e("endroutehault:", "" + mString_endroutehault);

                if (attributions != null) {
                    // mAttTextView.setText(Html.fromHtml(attributions.toString()));
                }
            }
            catch (Exception e){

            }

        }

    };
    private AdapterView.OnItemClickListener mAutocompleteClickListener_routehaultEND
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final PlaceEndRouteHault.PlaceAutocomplete item = placeEndRouteHault.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            Log.i(LOG_TAG, "Selected: " + item.description);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback_routehaultEND);
            Log.i(LOG_TAG, "Fetching details for ID: " + item.placeId);
            hideKeyboard();
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
            hideKeyboard();
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
            try {
                // Selecting the first object buffer.
                // Selecting the first object buffer.
                Place place;

                try {
                    place = places.get(0);
                } catch (IllegalStateException e) {
                    places.release();
                    return;
                }
                //final Place place = places.get(0);
                CharSequence attributions = places.getAttributions();

                // mNameTextView.setText("NAME:"+ Html.fromHtml(place.getName() + ""));
                // mAddressTextView.setText("ADDRESS: "+Html.fromHtml(place.getAddress() + ""));
                //  mIdTextView.setText(Html.fromHtml("PLACEID:" + place.getId() + ""));
                String s = "" + Html.fromHtml("" + place.getLatLng().latitude);
                String s1 = "" + Html.fromHtml("" + place.getLatLng().longitude);
                tostartlat = s;
                toendlong = s1;
                double start = Double.valueOf(s.trim()).doubleValue();
                double end = Double.valueOf(s1.trim()).doubleValue();


                mString_end = new LatLng(start, end);
                tvPlace2.clearFocus();
                hideKeyboard();
                if (tvPlace2.getText().toString().trim().equals(""))

                    showToast("Please enter end destination");
                else if (tvPlace1.getText().toString().trim().equals(""))

                    showToast("Please enter start destination");
                else {
                    if (isNetworkConnected()) {
                        try {
                            //  fewdetailssendbackend();
                            GetRouteTask getRoute = new GetRouteTask();
                            getRoute.execute();

                        } catch (Exception e) {
                            Rollbar.reportException(e, "minor", "Plan ride activity Route issue method");
                        }
                    } else {
                        showToast("Internet Not Connected");
                    }
                }


                //hideKeyboard();
                //mString_end =Html.fromHtml(place.getLatLng() + "");

                Log.e("end:", "" + mString_end);
                //mPhoneTextView.setText(Html.fromHtml("Lat:" + place.getLatLng().latitude + "--long:" + latlong));
                //mWebTextView.setText(place.getWebsiteUri() + "");
                if (attributions != null) {
                    // mAttTextView.setText(Html.fromHtml(attributions.toString()));
                }
            }
            catch (Exception e){

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
            hideKeyboard();
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
                try {
                    // Selecting the first object buffer.
                     Place place;

                    try {
                        place = places.get(0);
                    } catch (IllegalStateException e) {
                        places.release();
                        return;
                    }
                    CharSequence attributions = places.getAttributions();

                    // mNameTextView.setText("NAME:"+ Html.fromHtml(place.getName() + ""));
                    // mAddressTextView.setText("ADDRESS: "+Html.fromHtml(place.getAddress() + ""));
                    //  mIdTextView.setText(Html.fromHtml("PLACEID:" + place.getId() + ""));
                    String s = "" + Html.fromHtml("" + place.getLatLng().latitude);
                    String s1 = "" + Html.fromHtml("" + place.getLatLng().longitude);
                    fromstartlat = s;
                    fromendlong = s1;
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
                catch (Exception e){

                }
            }

        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_ride);

        try {
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
            setupActionBar(toolbar);
            setFonts();
            mainSV = (ScrollView) findViewById(R.id.mainSV);
            transparent_image = (ImageView) findViewById(R.id.transparent_image);


            // ATTENTION: This "addApi(AppIndex.API)"was auto-generated to implement the App Indexing API.
            // See https://g.co/AppIndexing/AndroidStudio for more information.
            mGoogleApiClient = new GoogleApiClient.Builder(PlanRideActivity.this)
                    .addApi(Places.GEO_DATA_API).addApi(Places.PLACE_DETECTION_API)
                    .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
                    .addConnectionCallbacks(this)
                    .addApi(AppIndex.API).build();
            tvPlace1 = (AutoCompleteTextView) findViewById(R.id
                    .tvPlace1);
            tvPlace2 = (AutoCompleteTextView) findViewById(R.id
                    .tvPlace2);
            tvPlace2.setThreshold(1);
            tvPlace1.setThreshold(1);

            Log.e("ImageURL", prefsManager.getImageUrl());
            hideKeyboard();
            setfocusOnViews();
            try {
                if (getIntent().getExtras().getBoolean(DESTINATION_NAVIGATE) == true) {
                    tvPlace2.setText(getIntent().getExtras().getString(DESTINATION_NAME));
                    Log.e("dest name:", getIntent().getExtras().getString(DESTINATION_NAME));
                    // if(getIntent().getExtras().getString(DESTINATION_NAME).isEmpty())
                    String s = getIntent().getExtras().getString(DESTINATION_LAT);
                    String s1 = getIntent().getExtras().getString(DESTINATION_LONG);
                    tostartlat = s;
                    toendlong = s1;
                    double start = Double.valueOf(s.trim()).doubleValue();
                    double end = Double.valueOf(s1.trim()).doubleValue();
                    mString_end = new LatLng(start, end);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
//                    hideKeyboard();
                            if (isNetworkConnected()) {
                                try {
                                    //  fewdetailssendbackend();
                                    GetRouteTask getRoute = new GetRouteTask();
                                    getRoute.execute();
                                } catch (Exception e) {
                                    Rollbar.reportException(e, "minor", "Plan ride activity Route issue");
                                }
                            }
                        }
                    }, 500);

                }
            } catch (NullPointerException e) {
                Rollbar.reportException(e, "minor", "Plan ride activity on create");
            }

            tvTitleToolbar.setText("MEET & PLAN A RIDE");
            gridView1.setAdapter(new CustomGridAdapter(this, prgmNameList, prgmImages));
            gridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                    if (position == 1) {
//                        Log.e("positiuon:", "" + classList[position]);
//                        intent_Calling(classList[position], "My Messages");
//                    }  else {
                    Log.e("positiuon:", "" + classList[position]);
                    intentCalling(classList[position]);
                    //   }

                }
            });


            //======= set start and end location in textview===========//

            tvPlace1.setOnItemClickListener(mAutocompleteClickListener_start);
            mPlaceArrayAdapter = new PlaceArrayAdapter(this, android.R.layout.simple_list_item_1,
                    BOUNDS_MOUNTAIN_VIEW, null);
            tvPlace1.setAdapter(mPlaceArrayAdapter);

//            tvPlace2.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(tv_rideTag.getText().toString()==null||tv_rideTag.getText().toString().isEmpty()){
//                        tv_rideTag.setClickable(false);
//                        tv_rideTag.setClickable(false);
//                        tv_rideTag.setFocusable(false);
//                        tv_rideTag.setEnabled(false);
//                        showRideTypeDialog();
//                        tvPlace2.setText("");
//                    }else{
//                        tv_rideTag.setClickable(true);
//                        tv_rideTag.setClickable(true);
//                        tv_rideTag.setFocusable(true);
//                        tv_rideTag.setEnabled(true);
//                    }
//                }
//            });


            tvPlace2.setOnItemClickListener(mAutocompleteClickListener_end);
            _ArrayAdapter = new EndPlaceArrayAdapter(this, android.R.layout.simple_list_item_1,
                    BOUNDS_MOUNTAIN_VIEW, null);
            tvPlace2.setAdapter(_ArrayAdapter);
            //============================set halt location======================//
            edPlace3.setThreshold(1);
            edPlace3.setOnItemClickListener(mAutocompleteClickListener_routehaultSTART);
            placeStartRouteHault = new PlaceStartRouteHault(this, android.R.layout.simple_list_item_1,
                    BOUNDS_MOUNTAIN_VIEW, null);
            edPlace3.setAdapter(placeStartRouteHault);
            edPlace4.setThreshold(1);
            edPlace4.setOnItemClickListener(mAutocompleteClickListener_routehaultEND);
            placeEndRouteHault = new PlaceEndRouteHault(this, android.R.layout.simple_list_item_1,
                    BOUNDS_MOUNTAIN_VIEW, null);
            edPlace4.setAdapter(placeEndRouteHault);

            //new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                hideKeyboard();
//            }
//        }, 300);

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
                            try {
                                Log.e("dest name 2:", getIntent().getExtras().getString(DESTINATION_NAME));
                                if (isNetworkConnected()) {
                                    try {
                                        //  fewdetailssendbackend();
                                        GetRouteTask getRoute = new GetRouteTask();
                                        getRoute.execute();
                                    } catch (Exception e) {

                                    }
                                } else {
                                    showToast("Internet Not Connected");
                                }
                            } catch (NullPointerException e) {

                            }
                        }
                    }

                    return false;
                }
            });
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
                //Toast.makeText(getApplicationContext(), "Latitude:" + latitude + " | Longitude: " + longitude, Toast.LENGTH_LONG).show();

                String s = "" + Html.fromHtml("" + latitude);
                String s1 = "" + Html.fromHtml("" + longitude);
                fromstartlat = s;
                fromendlong = s1;
                start = Double.valueOf(s.trim()).doubleValue();
                end = Double.valueOf(s1.trim()).doubleValue();
                mString_start = new LatLng(start, end);

                Log.e("start:", "" + mString_start);
                tvPlace1.setText(mGPSService.getLocationAddress());

            }


            // make sure you close the gps after using it. Save user's battery power
            mGPSService.closeGPS();
            mainSV.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //replace this line to scroll up or down
                    mainSV.fullScroll(ScrollView.FOCUS_UP);
                }
            }, 1000);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    hideKeyboard();
                }
            }, 300);

            transparent_image.setOnTouchListener(new View.OnTouchListener() {
                                                     @Override
                                                     public boolean onTouch(View v, MotionEvent event) {
                                                         int action = event.getAction();
                                                         switch (action) {
                                                             case MotionEvent.ACTION_DOWN:
                                                                 // Disallow ScrollView to intercept touch events.
                                                                 mainSV.requestDisallowInterceptTouchEvent(true);
                                                                 // Disable touch on transparent view
                                                                 return false;

                                                             case MotionEvent.ACTION_UP:
                                                                 // Allow ScrollView to intercept touch events.
                                                                 mainSV.requestDisallowInterceptTouchEvent(false);
                                                                 return true;

                                                             case MotionEvent.ACTION_MOVE:
                                                                 mainSV.requestDisallowInterceptTouchEvent(true);
                                                                 return false;

                                                             default:
                                                                 return true;
                                                         }
                                                     }
                                                 }

            );

            String comindate = getComingDateString().replace("/", " ");
            String todaydate = getCurrentDateString().replace("/", " ");
            Log.e("comindate:", "" + comindate);
            Log.e("todaydate:", "" + todaydate);
            tv_StartTagDate.setHint(todaydate);
            tv_EndTagDate.setHint(comindate);
        }catch(Exception e){
            Rollbar.reportException(e, "minor", "Plan ride activity on create");
        }
    }
    private String getComingDateString() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, +1);
        return dateFormat.format(cal.getTime());
    }
    private String getCurrentDateString() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        //cal.add(Calendar.DATE,1);
        return dateFormat.format(cal.getTime());
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
           // Toast.makeText(PlanRideActivity.this, "Image Address is Null", Toast.LENGTH_SHORT).show();
        } else {
            String imageUrl = prefsManager.getImageUrl();
            sdvDp.setImageURI(Uri.parse(imageUrl));

//            File file = new File(imageUrl);
//            sdvDp.setImageURI(Uri.fromFile(file));
        }

    }

    public void intentCalling(Class name) {
        Intent mIntent = new Intent(PlanRideActivity.this, name);
        startActivity(mIntent);

    }

    public void intent_Calling(Class name, String na) {
        Intent mIntent = new Intent(PlanRideActivity.this, name);
        mIntent.putExtra(SCREEN_OPEN, na);
        startActivity(mIntent);

    }



    private void setFonts() {
        Typeface typefaceNormal = Typeface.createFromAsset(getResources().getAssets(), "fonts/LATO-REGULAR.TTF");
        tvTitleToolbar.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/MACHINEN.TTF"));
        tvName.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/MACHINEN.TTF"));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                new AlertDialog.Builder(PlanRideActivity.this).setTitle("Message")
                        .setMessage("Are you sure you want to Discard Plan A Ride?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mGoogleApiClient.disconnect();
                                Intent intent = new Intent(PlanRideActivity.this, MainScreenActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra("MainScreenResponse", "OPEN");
                                startActivity(intent);
                                finish();
                                dialog.dismiss();

                            }
                        }).show();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(PlanRideActivity.this).setTitle("Message")
                .setMessage("Are you sure you want to Discard Plan A Ride?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mGoogleApiClient.disconnect();
                        Intent intent = new Intent(PlanRideActivity.this, MainScreenActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("MainScreenResponse", "OPEN");
                        startActivity(intent);
                        finish();
                        dialog.dismiss();

                    }
                }).show();
    }

    @OnClick({R.id.tvNext, R.id.ivMenu, R.id.rlProfile, R.id.ivMap, R.id.tv_rideTag, R.id.fmStartDate, R.id.fmEndDate, R.id.btFullProfile, R.id.btUpdateProfile, R.id.fmClick,R.id.tvPlace1,R.id.tvPlace2,R.id.edPlace3,R.id.edPlace4})
    void onclick(View view) {
        switch (view.getId()) {
            case R.id.tvNext:
                if (isNetworkConnected()) {
                    validations();
                   // getMatchDestinationList();

//                    try {
//                        if (rideTypeChoose) {
//                            boolean approved = Next(tv_rideTag.getText().toString(), tv_StartTagDate.getText().toString(), "", tvPlace1.getText().toString(), tvPlace2.getText().toString(), edPlace3.getText().toString(), "");
//                            if (approved) {
//                                StringTokenizer startlocation = new StringTokenizer(tvPlace1.getText().toString(), ",");
//                                StringTokenizer endlocation = new StringTokenizer(tvPlace2.getText().toString(), ",");
//                                String tvStartLocation = startlocation.nextToken();
//                                String tvEndLocation = endlocation.nextToken();
//                                Log.e("tvEndLocation",tvEndLocation);
//                                StringTokenizer tokens = new StringTokenizer(tv_StartTagDate.getText().toString(), "/");
//                                String first = tokens.nextToken();// this will contain "Fruit"
//                                String second = tokens.nextToken();// this will contain " they taste good"
//                                Intent mIntent = new Intent(PlanRideActivity.this, PlannedRidingsActivity.class);
//                                mIntent.putExtra("rideType", tv_rideTag.getText().toString());
//                                mIntent.putExtra("startdate", first);
//                                mIntent.putExtra("starttime", second);
//                                mIntent.putExtra("fromlatitude", fromstartlat);
//                                mIntent.putExtra("fromlongitude", fromendlong);
//                                mIntent.putExtra("fromlocation", tvStartLocation);
//                                mIntent.putExtra("tolatitude", tostartlat);
//                                mIntent.putExtra("tolongitude", toendlong);
//                                mIntent.putExtra("tolocation", tvEndLocation);
//                                mIntent.putExtra("hlatitude", fromroutehaultlat);
//                                mIntent.putExtra("hlongitude", fromroutehaultlong);
//                                mIntent.putExtra("hlocation", edPlace3.getText().toString());
//                                mIntent.putExtra("htype", "breakfast");
//                                startActivity(mIntent);
//                            } else {
//                                //showToast("Please fill all Value");
//                            }
//                        } else {
//                            boolean approved = Next(tv_rideTag.getText().toString(), tv_StartTagDate.getText().toString(), tv_EndTagDate.getText().toString(), tvPlace1.getText().toString(), tvPlace2.getText().toString(), edPlace3.getText().toString(), edPlace4.getText().toString());
//                            if (approved) {
//                                StringTokenizer startlocation = new StringTokenizer(tvPlace1.getText().toString(), ",");
//                                StringTokenizer endlocation = new StringTokenizer(tvPlace2.getText().toString(), ",");
//                                String tvStartLocation = startlocation.nextToken();
//                                String tvEndLocation = endlocation.nextToken();
//                                StringTokenizer tokens = new StringTokenizer(tv_StartTagDate.getText().toString(), "/");
//                                String first = tokens.nextToken();// this will contain "Fruit"
//                                String second = tokens.nextToken();// this will contain " they taste good"
//
//                                StringTokenizer tokens_ = new StringTokenizer(tv_EndTagDate.getText().toString(), "/");
//                                String first_ = tokens_.nextToken();// this will contain "Fruit"
//                                String second_ = tokens_.nextToken();// this will contain " they taste good"
//                                Log.e("second_:", "" + second_);
//                                Intent mIntent = new Intent(PlanRideActivity.this, PlannedRidingsActivity.class);
//                                mIntent.putExtra("rideType", tv_rideTag.getText().toString());
//                                mIntent.putExtra("startdate", first);
//                                mIntent.putExtra("starttime", second);
//                                mIntent.putExtra("enddate", first_);
//                                mIntent.putExtra("endtime", second_);
//                                mIntent.putExtra("fromlatitude", fromstartlat);
//                                mIntent.putExtra("fromlongitude", fromendlong);
//                                mIntent.putExtra("fromlocation", tvStartLocation);
//                                mIntent.putExtra("tolatitude", tostartlat);
//                                mIntent.putExtra("tolongitude", toendlong);
//                                mIntent.putExtra("tolocation", tvEndLocation);
//                                mIntent.putExtra("hlatitude", fromroutehaultlat);
//                                mIntent.putExtra("hlongitude", fromroutehaultlong);
//                                mIntent.putExtra("hlocation", edPlace3.getText().toString());
//                                mIntent.putExtra("htype", "breakfast");
//                                mIntent.putExtra("hlatitude1", toroutehaultlat);
//                                mIntent.putExtra("hlongitude1", toroutehaultlong);
//                                mIntent.putExtra("hlocation1", edPlace4.getText().toString());
//                                mIntent.putExtra("htype1", "Overnight");
//                                startActivity(mIntent);
//                            } else {
//                                showToast("Please fill all Value");
//                            }
//                        }
//                    } catch (Exception e) {
//
//                    }
                } else {
                    showToast("Internet Not Connected");
                }


                break;
            case R.id.btUpdateProfile:
                startActivity(new Intent(activity, ProfileActivity.class));
                break;
            case R.id.btFullProfile:
                startActivity(new Intent(activity, PublicProfileScreen.class));
                break;
            case R.id.fmClick:
                getCount();
                break;
            case R.id.ivMenu:
                if (mDrawerLayout.isDrawerOpen(lvSlidingMenu))
                    mDrawerLayout.closeDrawer(lvSlidingMenu);
                else
                    mDrawerLayout.openDrawer(lvSlidingMenu);
                break;
            case R.id.rlProfile:
               // startActivity(new Intent(activity, ProfileActivity.class));
                break;
            case R.id.fmStartDate:
                Log.e("Ride Type",tv_rideTag.getText().toString());
                if(tv_rideTag.getText().toString()==null||tv_rideTag.getText().toString().isEmpty()){
                    if(showDialog==false) {
                        showRideTypeDialog();
                    }
                }else {
                    whichdate = 1;
                    SublimePickerFragment pickerFrag = new SublimePickerFragment();
                    pickerFrag.setCallback(mFragmentCallback);

                    // Options
                    Pair<Boolean, SublimeOptions> optionsPair = getOptions();

                    if (!optionsPair.first) { // If options are not valid
                        Toast.makeText(PlanRideActivity.this, "No pickers activated",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Valid options
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("SUBLIME_OPTIONS", optionsPair.second);
                    pickerFrag.setArguments(bundle);

                    pickerFrag.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
                    pickerFrag.show(getSupportFragmentManager(), "SUBLIME_PICKER");
                /*final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                mMinute = c.get(Calendar.MINUTE);
                mHour = c.get(Calendar.HOUR);
                AMPM = c.get(Calendar.AM_PM);
                if (AMPM == 0) {
                    amPM = "AM";
                } else {
                    amPM = "PM";
                }

                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                String monthname=new DateFormatSymbols().getMonths()[monthOfYear];
                                // Display Selected date in textbox
                                tv_StartTagDate.setText(" " + dayOfMonth + "-"
                                        + monthname + "-" + year + "/" + mHour + ":" + mMinute + " ");

                            }
                        }, mYear, mMonth, mDay);
                dpd.show();*/
                }
                break;
            case R.id.tv_rideTag:
                dw = new DemoPopupWindow(view, PlanRideActivity.this);
                dw.showLikeQuickAction(0, 0);
                break;

            case R.id.fmEndDate:
                if(tv_rideTag.getText().toString()==null||tv_rideTag.getText().toString().isEmpty()){
                    if(showDialog==false) {
                        showRideTypeDialog();
                    }
                }else {
                    whichdate = 2;
                    SublimePickerFragment pickerFrag1 = new SublimePickerFragment();
                    pickerFrag1.setCallback(mFragmentCallback);

                    // Options
                    Pair<Boolean, SublimeOptions> optionsPair1 = getOptions();

                    if (!optionsPair1.first) { // If options are not valid
                        Toast.makeText(PlanRideActivity.this, "No pickers activated",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Valid options
                    Bundle bundle1 = new Bundle();
                    bundle1.putParcelable("SUBLIME_OPTIONS", optionsPair1.second);
                    pickerFrag1.setArguments(bundle1);

                    pickerFrag1.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
                    pickerFrag1.show(getSupportFragmentManager(), "SUBLIME_PICKER");
               /* final Calendar c1 = Calendar.getInstance();
                mYear = c1.get(Calendar.YEAR);
                mMonth = c1.get(Calendar.MONTH);
                mDay = c1.get(Calendar.DAY_OF_MONTH);

                // Launch Date Picker Dialog
                DatePickerDialog dpd1 = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                String monthname=new DateFormatSymbols().getMonths()[monthOfYear];
                                // Display Selected date in textbox
                                tv_EndTagDate.setText(" " + dayOfMonth + "-"
                                        + monthname + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                dpd1.show();*/
                }
                break;
            case R.id.tvPlace1:
            if(tv_rideTag.getText().toString()==null||tv_rideTag.getText().toString().isEmpty()){
                if(showDialog==false) {
                    showRideTypeDialog();
                }
            }
            case R.id.tvPlace2:
                if(tv_rideTag.getText().toString()==null||tv_rideTag.getText().toString().isEmpty()){
                    if(showDialog==false) {
                        showRideTypeDialog();
                    }
                }
            case R.id.edPlace3:
                if(tv_rideTag.getText().toString()==null||tv_rideTag.getText().toString().isEmpty()){

                    if(showDialog==false) {
                        showRideTypeDialog();
                    }
                }
            case R.id.edPlace4:
                if(tv_rideTag.getText().toString()==null||tv_rideTag.getText().toString().isEmpty()){
                    if(showDialog==false) {
                        showRideTypeDialog();
                    }
                }

        }
    }

    public boolean Next(String RideType, String startDate, String endDate, String startLocation, String endLocation, String breakfast, String tea) {

        if (RideType.matches("")) {
            return false;
        } else if (RideType.matches("Overnight Ride")) {
            if (endDate.matches("")) {
                showToast("Please enter your End Date");
                return false;
            } else if (startDate.matches("")) {
                showToast("Please enter your Start Date");
                return false;
            }
            if (breakfast.matches("")) {
                showToast("Please enter your breakfast halt");
                return false;
            } else if (tea.matches("")) {
                showToast("Please enter your Tea Halt");
                return false;
            }

        } else if (startDate.matches("")) {
            showToast("Please enter your Start Date");
            return false;
        } else if (startLocation.matches("")) {
            showToast("Please enter your Start Location");
            return false;
        } else if (endLocation.matches("")) {
            showToast("Please enter your End Location");
            return false;
        } /*else if (breakfast.matches("")) {
            return false;
        }*/


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


    // ************** Class for pop-up window **********************


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


    public void addMarker(LatLng latLng) {
        MarkerOptions marker = null;
        View view = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker_layout, null);
        FrameLayout fmlayout = (FrameLayout) view.findViewById(R.id.fmlayout);
       // ImageView ivMapImage = (ImageView) view.findViewById(R.id.ivMapImage);
        fmlayout.setBackgroundResource(R.drawable.ic_endlocation);
        // create marker
        marker = new MarkerOptions().position(latLng).title("");
        // Changing marker icon
        marker.getPosition();
        marker.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, view)));
        // adding marker
        //marker.snippet(lat.get(position));
        mGoogleMap.addMarker(marker);
        CameraPosition cameraPosition=null;
        if( CalculationByDistance(mString_start,mString_end)<=50) {
            cameraPosition = new CameraPosition.Builder().target(
                    latLng).zoom(9).build();
        }else if(CalculationByDistance(mString_start,mString_end)<=100&&CalculationByDistance(mString_start,mString_end)>50){
            cameraPosition = new CameraPosition.Builder().target(
                    latLng).zoom(8).build();
        }else{
            cameraPosition = new CameraPosition.Builder().target(
                    latLng).zoom(7).build();
        }

//        CameraPosition cameraPosition = new CameraPosition.Builder().target(
//                    latLng).zoom(9).build();
        mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // check if map is created successfully or not
        if (mGoogleMap == null) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                    .show();
        }
    }


    public void addmarkerTeaMarker(LatLng latLng) {
        Log.e("latLng:", "" + latLng);
        if(markerTea!=null){
            Log.e("if:", "if");
            markerTea.remove();
            // marker.position(latLng);
            // Changing marker icon
            View view = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_new_marker_layout, null);
            FrameLayout fmlayout = (FrameLayout) view.findViewById(R.id.fmlayout);
            // ImageView ivMapImage = (ImageView) view.findViewById(R.id.ivMapImage);
            fmlayout.setBackgroundResource(R.drawable.ic_tea_location_tip_only);


            // mGoogleMap.addMarker(marker);
            markerTea = mGoogleMap.addMarker(new MarkerOptions()
                    .position(latLng).snippet("")
                    .icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, view))));
            markerTea.getPosition();
            CameraPosition cameraPosition = new CameraPosition.Builder().target(
                    latLng).zoom(8).build();
            mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
            mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

            // check if map is created successfully or not
            if (mGoogleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
        }
        else{
            Log.e("else:","else");
            View view = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_new_marker_layout, null);
            FrameLayout fmlayout = (FrameLayout) view.findViewById(R.id.fmlayout);
            // ImageView ivMapImage = (ImageView) view.findViewById(R.id.ivMapImage);
            fmlayout.setBackgroundResource(R.drawable.ic_tea_location_tip_only);
            markerTea = mGoogleMap.addMarker(new MarkerOptions()
                    .position(latLng).snippet("")
                    .icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, view))));
            markerTea.getPosition();
            CameraPosition cameraPosition = new CameraPosition.Builder().target(
                    latLng).zoom(8).build();
            mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
            mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            // check if map is created successfully or not
            if (mGoogleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
        }

    }
    public void addBreakFastMarker(LatLng latLng) {
        Log.e("latLng:", "" + latLng);
        if(marker!=null){
            Log.e("if:", "if");
            marker.remove();
           // marker.position(latLng);
            // Changing marker icon
            View view = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_new_marker_layout, null);
            FrameLayout fmlayout = (FrameLayout) view.findViewById(R.id.fmlayout);
            // ImageView ivMapImage = (ImageView) view.findViewById(R.id.ivMapImage);
            fmlayout.setBackgroundResource(R.drawable.ic_breakfast_location_tip_only);


            // mGoogleMap.addMarker(marker);
            marker = mGoogleMap.addMarker(new MarkerOptions()
                    .position(latLng).snippet("")
                    .icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, view))));
            marker.getPosition();
            CameraPosition cameraPosition = new CameraPosition.Builder().target(
                    latLng).zoom(8).build();
            mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
            mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

            // check if map is created successfully or not
            if (mGoogleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
        }
        else{
            Log.e("else:","else");
            View view = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_new_marker_layout, null);
            FrameLayout fmlayout = (FrameLayout) view.findViewById(R.id.fmlayout);
            // ImageView ivMapImage = (ImageView) view.findViewById(R.id.ivMapImage);
            fmlayout.setBackgroundResource(R.drawable.ic_breakfast_location_tip_only);
            marker = mGoogleMap.addMarker(new MarkerOptions()
                    .position(latLng).snippet("")
                    .icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, view))));
            marker.getPosition();
            CameraPosition cameraPosition = new CameraPosition.Builder().target(
                    latLng).zoom(8).build();
            mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
            mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            // check if map is created successfully or not
            if (mGoogleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
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
    /**
     * @author AMIT AGNIHOTRI
     *         This class Get Route on the map
     */
    private class GetRouteTask extends AsyncTask<String, Void, String> {

        String response = "";

        @Override
        protected void onPreExecute() {
            Dialog = new ProgressDialog(PlanRideActivity.this);
            Dialog.setMessage("Loading route...");
            Dialog.show();
        }

        @Override
        protected String doInBackground(String... urls) {
            //Get All Route values
            Log.e("mString_start"," : "+mString_start);
            Log.e("mString_end"," : "+mString_end);
            if(mString_end.equals(null)){
                showToast("Please enter valid destination.");
            }else {
                document = v2GetRouteDirection.getDocument(mString_start, mString_end, GMapV2GetRouteDirection.MODE_DRIVING);
                response = "Success";
            }
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
                CameraUpdate cameraUpdate=null;
                if( CalculationByDistance(mString_start,mString_end)<=50) {
                    cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 10);
                }else if(CalculationByDistance(mString_start,mString_end)<=100&&CalculationByDistance(mString_start,mString_end)>50){
                    cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 9);
                }else{
                    cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 7);
                }
                //CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 9);
                mGoogleMap.animateCamera(cameraUpdate);

                for (int i = 1; i < 3; i++){
                    if (i == 1) {
                        addMarker(mString_start);
                    } else if (i == 2) {
                        addMarker(mString_end);
                    }
                }
            }

            if(Dialog.isShowing()) {
                Dialog.dismiss();
            }
            SaveCount();
            edPlace3.setFocusable(true);
            edPlace3.requestFocus();
        }

    }

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
                    if (ridetype.get(position).matches("Breakfast Ride")) {
                        tv_rideTag.setText(ridetype.get(position));
                        fmEndDate.setVisibility(View.GONE);
                        edPlace4.setVisibility(View.GONE);
                        rideTypeChoose = true;
                        dw.dismiss();
                    } else if (ridetype.get(position).matches("Overnight Ride")) {
                        tv_rideTag.setText(ridetype.get(position));
                        fmEndDate.setVisibility(View.VISIBLE);
                        fmStartDate.setVisibility(View.VISIBLE);
                        edPlace4.setVisibility(View.VISIBLE);
                        rideTypeChoose = false;
                        dw.dismiss();
                    }

                }
            });
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


    @Override
    protected void onResume() {
        super.onResume();
        if (mDrawerLayout.isDrawerOpen(lvSlidingMenu)) {
            showProfileImage();
            mDrawerLayout.closeDrawer(lvSlidingMenu);
        }
    }


    Pair<Boolean, SublimeOptions> getOptions() {
        SublimeOptions options = new SublimeOptions();
        int displayOptions = 0;

        // if (cbDatePicker.isChecked()) {

        // }

        // if (cbTimePicker.isChecked()) {
        displayOptions |= SublimeOptions.ACTIVATE_TIME_PICKER;
        //}
        displayOptions |= SublimeOptions.ACTIVATE_DATE_PICKER;


        // if (rbDatePicker.getVisibility() == View.VISIBLE && rbDatePicker.isChecked()) {

        // } else if (rbTimePicker.getVisibility() == View.VISIBLE && rbTimePicker.isChecked()) {
        options.setPickerToShow(SublimeOptions.Picker.TIME_PICKER);
        // }
        options.setPickerToShow(SublimeOptions.Picker.DATE_PICKER);
   /* else if (rbRecurrencePicker.getVisibility() == View.VISIBLE && rbRecurrencePicker.isChecked()) {
            options.setPickerToShow(SublimeOptions.Picker.REPEAT_OPTION_PICKER);
        }*/

        options.setDisplayOptions(displayOptions);


        // Example for setting date range:
        // Note that you can pass a date range as the initial date params
        // even if you have date-range selection disabled. In this case,
        // the user WILL be able to change date-range using the header
        // TextViews, but not using long-press.

        /*Calendar startCal = Calendar.getInstance();
        startCal.set(2016, 2, 4);
        Calendar endCal = Calendar.getInstance();
        endCal.set(2016, 2, 17);

        options.setDateParams(startCal, endCal);*/

        // If 'displayOptions' is zero, the chosen options are not valid
        return new Pair<>(displayOptions != 0 ? Boolean.TRUE : Boolean.FALSE, options);
    }

    SublimePickerFragment.Callback mFragmentCallback = new SublimePickerFragment.Callback() {
        @Override
        public void onCancelled() {
            //rlDateTimeRecurrenceInfo.setVisibility(View.GONE);
        }

        @Override
        public void onDateTimeRecurrenceSet(SelectedDate selectedDate,
                                            int hourOfDay, int minute,
                                            SublimeRecurrencePicker.RecurrenceOption recurrenceOption,
                                            String recurrenceRule) {

            Log.e("selectedDate:", "" + selectedDate.toString());
            Log.e("mHour:", "" + hourOfDay);
            Log.e("mMinute:", "" + minute);
            if (whichdate == 1) {
                tv_StartTagDate.setText(selectedDate.toString() + "/" + hourOfDay + ":" + minute);
            } else if (whichdate == 2) {
                tv_EndTagDate.setText(selectedDate.toString() + "/" + hourOfDay + ":" + minute);
            }
        }
    };
    // Keys for saving state
    final String SS_DATE_PICKER_CHECKED = "saved.state.date.picker.checked";
    final String SS_TIME_PICKER_CHECKED = "saved.state.time.picker.checked";
    final String SS_RECURRENCE_PICKER_CHECKED = "saved.state.recurrence.picker.checked";
    final String SS_ALLOW_DATE_RANGE_SELECTION = "saved.state.allow.date.range.selection";
    final String SS_START_YEAR = "saved.state.start.year";
    final String SS_START_MONTH = "saved.state.start.month";
    final String SS_START_DAY = "saved.state.start.day";
    final String SS_END_YEAR = "saved.state.end.year";
    final String SS_END_MONTH = "saved.state.end.month";
    final String SS_END_DAY = "saved.state.end.day";
    final String SS_HOUR = "saved.state.hour";
    final String SS_MINUTE = "saved.state.minute";
    final String SS_RECURRENCE_OPTION = "saved.state.recurrence.option";
    final String SS_RECURRENCE_RULE = "saved.state.recurrence.rule";
    final String SS_INFO_VIEW_VISIBILITY = "saved.state.info.view.visibility";
    final String SS_SCROLL_Y = "saved.state.scroll.y";

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Save state of CheckBoxes
        // State of RadioButtons can be evaluated
        outState.putBoolean(SS_DATE_PICKER_CHECKED, true);
        outState.putBoolean(SS_TIME_PICKER_CHECKED, true);
        //outState.putBoolean(SS_RECURRENCE_PICKER_CHECKED, cbRecurrencePicker.isChecked());
        //outState.putBoolean(SS_ALLOW_DATE_RANGE_SELECTION, cbAllowDateRangeSelection.isChecked());

        int startYear = mSelectedDate != null ? mSelectedDate.getStartDate().get(Calendar.YEAR) : INVALID_VAL;
        int startMonth = mSelectedDate != null ? mSelectedDate.getStartDate().get(Calendar.MONTH) : INVALID_VAL;
        int startDayOfMonth = mSelectedDate != null ? mSelectedDate.getStartDate().get(Calendar.DAY_OF_MONTH) : INVALID_VAL;

        int endYear = mSelectedDate != null ? mSelectedDate.getEndDate().get(Calendar.YEAR) : INVALID_VAL;
        int endMonth = mSelectedDate != null ? mSelectedDate.getEndDate().get(Calendar.MONTH) : INVALID_VAL;
        int endDayOfMonth = mSelectedDate != null ? mSelectedDate.getEndDate().get(Calendar.DAY_OF_MONTH) : INVALID_VAL;

        // Save data
        outState.putInt(SS_START_YEAR, startYear);
        outState.putInt(SS_START_MONTH, startMonth);
        outState.putInt(SS_START_DAY, startDayOfMonth);
        outState.putInt(SS_END_YEAR, endYear);
        outState.putInt(SS_END_MONTH, endMonth);
        outState.putInt(SS_END_DAY, endDayOfMonth);
        outState.putInt(SS_HOUR, mHour);
        outState.putInt(SS_MINUTE, mMinute);
        /*outState.putString(SS_RECURRENCE_OPTION, mRecurrenceOption);
        outState.putString(SS_RECURRENCE_RULE, mRecurrenceRule);*/
        /*outState.putBoolean(SS_INFO_VIEW_VISIBILITY,
                rlDateTimeRecurrenceInfo.getVisibility() == View.VISIBLE);*/
        //outState.putInt(SS_SCROLL_Y, svMainContainer.getScrollY());

        super.onSaveInstanceState(outState);
    }




    /**
     * saveCounter to Server
     **/
    private void SaveCount() {

        String accessToken = prefsManager.getToken();

        FileUploadService service = ServiceGenerator.createService(FileUploadService.BASE_URL);
        Log.e("BasLat:", "" + tvPlace1.getText().toString());
        Log.e("BasLon:", "" + tvPlace2.getText().toString());
        Log.e("accessToken:", "" + accessToken);

        service.saveCounter(tvPlace1.getText().toString(), tvPlace2.getText().toString(), accessToken, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, retrofit.client.Response response) {

                Type type = new TypeToken<SaveCounter>() {
                }.getType();
                SaveCounter
                        saveCounter = new Gson().fromJson(jsonObject.toString(), type);
                Log.e("SaveCounter:", "" + jsonObject);

                if (saveCounter.getSuccess() == 1) {


                } else {
                    dismissProgressDialog();
                    //CustomDialog.showProgressDialog(PlanRideActivity.this, saveCounter.getMessage().toString());
                }


            }

            @Override
            public void failure(RetrofitError error) {

                Log.e("DataUploading------", "Data Uploading Failure......" + error);

            }
        });
    }

    /**
     * getCount (Only Strings) to Server
     **/
    private void getCount() {

        String accessToken = prefsManager.getToken();

        FileUploadService service = ServiceGenerator.createService(FileUploadService.BASE_URL);
        Log.e("BasLat:", "" + tvPlace1.getText().toString());
        Log.e("BasLon:", "" + tvPlace2.getText().toString());
        Log.e("accessToken:", "" + accessToken);

        service.getCounter(tvPlace1.getText().toString(), tvPlace2.getText().toString(), accessToken, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, retrofit.client.Response response) {

                Type type = new TypeToken<GetCount>() {
                }.getType();
                GetCount
                        modilyBike = new Gson().fromJson(jsonObject.toString(), type);
                Log.e("getCounter:", "" + jsonObject);

                if (modilyBike.getSuccess() == 1) {
                    dismissProgressDialog();
                    getCountData = modilyBike.getData();
                    tvEatables.setText(getCountData.getResturants());
                    tvPetrolPump.setText(getCountData.getGasStation());
                    tvServiceCenter.setText(getCountData.getRepair());
                    tvFirstAid.setText(getCountData.getDoctor());
                    fmClick.setVisibility(View.GONE);
                } else {
                    fmClick.setVisibility(View.VISIBLE);
                    dismissProgressDialog();
                    CustomDialog.showProgressDialog(PlanRideActivity.this, modilyBike.getMessage().toString());
                }


            }

            @Override
            public void failure(RetrofitError error) {
                fmClick.setVisibility(View.VISIBLE);
                dismissProgressDialog();
                Log.e("DataUploading------", "Data Uploading Failure......" + error);

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

               if (Dialog != null) {
                   Dialog.dismiss();
                   Dialog = null;
            }
    }

    private void showRideTypeDialog(){
        showDialog= true;
        new AlertDialog.Builder(PlanRideActivity.this).setTitle("Alert!!").setCancelable(false)
                .setMessage("Please select Ride Type First.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        showDialog= false;

                    }
                }).show();
    }


    //============== set focus on textview===============//
    private void setfocusOnViews(){

//        tvPlace1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if(hasFocus){
//                    if(tv_rideTag.getText().toString()==null||tv_rideTag.getText().toString().isEmpty()){
//                        showRideTypeDialog();
//                        tvPlace1.setClickable(false);
//                    }else{
//                        tvPlace1.setCursorVisible(true);
//                        tvPlace1.setClickable(true);
//                    }
//                }
//            }
//        });
        tvPlace2.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() != 0)
//                if(tv_rideTag.getText().toString()==null||tv_rideTag.getText().toString().isEmpty()){
//                    showRideTypeDialog();
//                    tvPlace2.setText("");
//                }
//                else{
                    tvPlace2.setCursorVisible(true);
               // }
            }
        });
        edPlace3.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() != 0)
                    if(tv_rideTag.getText().toString()==null||tv_rideTag.getText().toString().isEmpty()){
                        if(showDialog==false) {
                            showRideTypeDialog();
                        }
                        edPlace3.setText("");
                    }
                    else{
                        edPlace3.setCursorVisible(true);
                    }
            }
        });
        edPlace4.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() != 0)
                    if(tv_rideTag.getText().toString()==null||tv_rideTag.getText().toString().isEmpty()){
                        if(showDialog==false) {
                            showRideTypeDialog();
                        }
                        edPlace4.setText("");
                    }
                    else{
                        edPlace4.setCursorVisible(true);
                    }
            }
        });
//        tvPlace2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if(hasFocus){
//                    if(tv_rideTag.getText().toString()==null||tv_rideTag.getText().toString().isEmpty()){
//                        showRideTypeDialog();
//                    }
//                    else{
//                        tvPlace1.setCursorVisible(true);
//                    }
//
//                }
//            }
//        });
//        edPlace3.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if(hasFocus){
//                    tvPlace1.setCursorVisible(true);
//                    if(tv_rideTag.getText().toString()==null||tv_rideTag.getText().toString().isEmpty()){
//                        showRideTypeDialog();
//                    }
//
//                }
//            }
//        });
//        edPlace4.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if(hasFocus){
//                    tvPlace1.setCursorVisible(true);
//                    if(tv_rideTag.getText().toString()==null||tv_rideTag.getText().toString().isEmpty()){
//                        showRideTypeDialog();
//                    }
//
//                }
//            }
//        });
    }


    private void validations(){
        if(tv_rideTag.getText().toString().isEmpty()) {
            showToast("Please select Ride type");
        }else {
            if (tv_rideTag.getText().toString().equalsIgnoreCase("Overnight Ride")) {
                if (tv_StartTagDate.getText().toString().isEmpty()) {
                    showToast("Please select the Start Date");
                }else
                if (tv_EndTagDate.getText().toString().isEmpty()) {
                    showToast("Please enter your End Date");
                } else if (tvPlace1.getText().toString().isEmpty()) {
                    showToast("Please enter your Start Location");

                } else if (tvPlace2.getText().toString().isEmpty()) {
                    showToast("Please enter your End Location");
                } else if (edPlace3.getText().toString().isEmpty()) {
                    showToast("Please enter your breakfast halt");

                } else if (edPlace4.getText().toString().isEmpty()) {
                    showToast("Please enter your Tea Halt");

                } else {
                    getMatchDestinationList();
                }

            } else if (tv_rideTag.getText().toString().equalsIgnoreCase("Breakfast Ride")) {
                if (tv_StartTagDate.getText().toString().isEmpty()) {
                    showToast("Please select the Start Date");
                }
                else if (tvPlace1.getText().toString().isEmpty()) {
                    showToast("Please enter your Start Location");

                } else if (tvPlace2.getText().toString().isEmpty()) {
                    showToast("Please enter your End Location");
                } else if (edPlace3.getText().toString().isEmpty()) {
                    showToast("Please enter your breakfast halt");

                }  else {
                    getMatchDestinationList();
                }
            }
        }

            }

    //================ get matchDestination List================//
    private void getMatchDestinationList(){

//         boolean check = Next(tv_rideTag.getText().toString(), tv_StartTagDate.getText().toString(), "", tvPlace1.getText().toString(), tvPlace2.getText().toString(), edPlace3.getText().toString(), "");

        if(tv_rideTag.getText().toString().isEmpty()) {
            showToast("Please select Ride type");
        }else {
            showProgressDialog();
            StringTokenizer endlocation = new StringTokenizer(tvPlace2.getText().toString(), ",");
            String tvEndLocation = endlocation.nextToken();
            Log.e("tvEndLocation", tvEndLocation);
//
//        Log.e("AccessToken",""+acessToken);
//        Log.e("UserId",""+userId);

            FileUploadService service = ServiceGenerator.createService(FileUploadService.BASE_URL_NEARBY_FRIENDS);

            service.matchDestinationtAPI(prefsManager.getCaseId(), tvEndLocation, prefsManager.getToken(), new Callback<JsonObject>() {
                @Override
                public void success(JsonObject jsonObject, retrofit.client.Response response) {
                    Log.e("type", "==");
                    Type type = new TypeToken<PlanARide>() {
                    }.getType();

                    Log.e("jsonObject", "==" + jsonObject);
                    PlanARide planRideResponse = new Gson().fromJson(jsonObject.toString(), type);
                    planARideDatas.clear();
                    ;
                    dismissProgressDialog();
                    if (planRideResponse.getSuccess() == 1) {
                        planARideDatas.addAll(planRideResponse.getData());
                        Log.e("Match Ride size", "" + planARideDatas.size());
                        if (planARideDatas.size() == 0) {
                            postRideScreen();
                        } else {
                            navigateToNextScreen(planARideDatas);
                        }
                    } else {
                        postRideScreen();
                        //CustomDialog.showProgressDialog(PlanRideActivity.this, planRideResponse.getMessage().toString());
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e("error", "" + error.getMessage());
                    dismissProgressDialog();
                }
            });
        }

    }

    //============ check ride type and navigate to next screen===========//
    private void navigateToNextScreen(ArrayList<PlanARideData> planARideDatas){
        try {
            if (rideTypeChoose) {
                boolean approved = Next(tv_rideTag.getText().toString(), tv_StartTagDate.getText().toString(), "", tvPlace1.getText().toString(), tvPlace2.getText().toString(), edPlace3.getText().toString(), "");
                if (approved) {
                    StringTokenizer startlocation = new StringTokenizer(tvPlace1.getText().toString(), ",");
                    StringTokenizer endlocation = new StringTokenizer(tvPlace2.getText().toString(), ",");
                    String tvStartLocation = startlocation.nextToken();
                    String tvEndLocation = endlocation.nextToken();
                    Log.e("tvEndLocation ",tvEndLocation);
                    StringTokenizer tokens = new StringTokenizer(tv_StartTagDate.getText().toString(), "/");
                    String first = tokens.nextToken();// this will contain "Fruit"
                    String second = tokens.nextToken();// this will contain " they taste good"
                    Intent mIntent = new Intent(PlanRideActivity.this, PlannedRidingsActivity.class);
                    mIntent.putExtra("rideType", tv_rideTag.getText().toString());
                    mIntent.putExtra("startdate", first);
                    Log.e("starttime",second);
                    mIntent.putExtra("starttime", second);
                    mIntent.putExtra("fromlatitude", fromstartlat);
                    mIntent.putExtra("fromlongitude", fromendlong);
                    mIntent.putExtra("fromlocation", tvStartLocation);
                    mIntent.putExtra("tolatitude", tostartlat);
                    mIntent.putExtra("tolongitude", toendlong);
                    mIntent.putExtra("tolocation", tvEndLocation);
                    mIntent.putExtra("hlatitude", fromroutehaultlat);
                    mIntent.putExtra("hlongitude", fromroutehaultlong);
                    mIntent.putExtra("hlocation", edPlace3.getText().toString());
                    mIntent.putExtra("htype", "breakfast");
                    //mIntent.putExtra("planARideDatas",planARideDatas);
                    Log.e("planARideDatas ",":: "+planARideDatas.size());
                    startActivity(mIntent);
                } else {
                   showToast("Please fill all Value");
                }
            } else {
                boolean approved = Next(tv_rideTag.getText().toString(), tv_StartTagDate.getText().toString(), tv_EndTagDate.getText().toString(), tvPlace1.getText().toString(), tvPlace2.getText().toString(), edPlace3.getText().toString(), edPlace4.getText().toString());
                if (approved) {
                    StringTokenizer startlocation = new StringTokenizer(tvPlace1.getText().toString(), ",");
                    StringTokenizer endlocation = new StringTokenizer(tvPlace2.getText().toString(), ",");
                    String tvStartLocation = startlocation.nextToken();
                    String tvEndLocation = endlocation.nextToken();
                    StringTokenizer tokens = new StringTokenizer(tv_StartTagDate.getText().toString(), "/");
                    String first = tokens.nextToken();// this will contain "Fruit"
                    String second = tokens.nextToken();// this will contain " they taste good"

                    StringTokenizer tokens_ = new StringTokenizer(tv_EndTagDate.getText().toString(), "/");
                    String first_ = tokens_.nextToken();// this will contain "Fruit"
                    String second_ = tokens_.nextToken();// this will contain " they taste good"
                    Log.e("second_:", "" + second_);
                    Intent mIntent = new Intent(PlanRideActivity.this, PlannedRidingsActivity.class);
                    mIntent.putExtra("rideType", tv_rideTag.getText().toString());
                    mIntent.putExtra("startdate", first);
                    mIntent.putExtra("starttime", second);
                    mIntent.putExtra("enddate", first_);
                    mIntent.putExtra("endtime", second_);
                    mIntent.putExtra("fromlatitude", fromstartlat);
                    mIntent.putExtra("fromlongitude", fromendlong);
                    mIntent.putExtra("fromlocation", tvStartLocation);
                    mIntent.putExtra("tolatitude", tostartlat);
                    mIntent.putExtra("tolongitude", toendlong);
                    mIntent.putExtra("tolocation", tvEndLocation);
                    mIntent.putExtra("hlatitude", fromroutehaultlat);
                    mIntent.putExtra("hlongitude", fromroutehaultlong);
                    mIntent.putExtra("hlocation", edPlace3.getText().toString());
                    mIntent.putExtra("htype", "breakfast");
                    mIntent.putExtra("hlatitude1", toroutehaultlat);
                    mIntent.putExtra("hlongitude1", toroutehaultlong);
                    mIntent.putExtra("hlocation1", edPlace4.getText().toString());
                    mIntent.putExtra("htype1", "Overnight");
                    //mIntent.putExtra("planARideDatas",planARideDatas);
                    startActivity(mIntent);
                } else {
                    showToast("Please fill all Value");
                }
            }
        } catch (Exception e) {
            Rollbar.reportException(e, "minor", "Plan ride activity navigateToNextScreen method");
        }
    }


    //============ navigate to PostRide Screen===========//
    private void postRideScreen(){

        if (rideTypeChoose) {
            boolean approved = Next(tv_rideTag.getText().toString(), tv_StartTagDate.getText().toString(), "", tvPlace1.getText().toString(), tvPlace2.getText().toString(), edPlace3.getText().toString(), "");
            if (approved) {
                StringTokenizer startlocation = new StringTokenizer(tvPlace1.getText().toString(), ",");
                StringTokenizer endlocation = new StringTokenizer(tvPlace2.getText().toString(), ",");
                String tvStartLocation = startlocation.nextToken();
                String tvEndLocation = endlocation.nextToken();
                Log.e("tvEndLocation", tvEndLocation);
                StringTokenizer tokens = new StringTokenizer(tv_StartTagDate.getText().toString(), "/");
                String first = tokens.nextToken();// this will contain "Fruit"
                String second = tokens.nextToken();// this will contain " they taste good"

            Intent mIntent = new Intent(PlanRideActivity.this, PlanARidePostRide.class);
            mIntent.putExtra("rideType", "breakfast");
            mIntent.putExtra("startdate", first);
            mIntent.putExtra("starttime", second);
            mIntent.putExtra("fromlatitude", fromstartlat);
            mIntent.putExtra("fromlongitude", fromendlong);
            mIntent.putExtra("fromlocation", tvStartLocation);
            mIntent.putExtra("tolatitude", tostartlat);
            mIntent.putExtra("tolongitude", toendlong);
            mIntent.putExtra("tolocation", tvEndLocation);
            mIntent.putExtra("hlatitude", fromroutehaultlat);
            mIntent.putExtra("hlongitude", fromroutehaultlong);
            mIntent.putExtra("hlocation", edPlace3.getText().toString());
            mIntent.putExtra("htype", "breakfast");
                startActivity(mIntent);
            }
        } else {
            boolean approved = Next(tv_rideTag.getText().toString(), tv_StartTagDate.getText().toString(), tv_EndTagDate.getText().toString(), tvPlace1.getText().toString(), tvPlace2.getText().toString(), edPlace3.getText().toString(), edPlace4.getText().toString());
            if (approved) {
                StringTokenizer startlocation = new StringTokenizer(tvPlace1.getText().toString(), ",");
                StringTokenizer endlocation = new StringTokenizer(tvPlace2.getText().toString(), ",");
                String tvStartLocation = startlocation.nextToken();
                String tvEndLocation = endlocation.nextToken();
                StringTokenizer tokens = new StringTokenizer(tv_StartTagDate.getText().toString(), "/");
                String first = tokens.nextToken();// this will contain "Fruit"
                String second = tokens.nextToken();// this will contain " they taste good"

                StringTokenizer tokens_ = new StringTokenizer(tv_EndTagDate.getText().toString(), "/");
                String first_ = tokens_.nextToken();// this will contain "Fruit"
                String second_ = tokens_.nextToken();// this will contain " they taste good"
                Intent mIntent = new Intent(PlanRideActivity.this, PlanARidePostRide.class);
                mIntent.putExtra("rideType", "overnight");
                mIntent.putExtra("startdate", first);
                mIntent.putExtra("starttime", second);
                mIntent.putExtra("enddate", first_);
                mIntent.putExtra("endtime", second_);
                mIntent.putExtra("fromlatitude", fromstartlat);
                mIntent.putExtra("fromlongitude", fromendlong);
                mIntent.putExtra("fromlocation", tvStartLocation);
                mIntent.putExtra("tolatitude", tostartlat);
                mIntent.putExtra("tolongitude", toendlong);
                mIntent.putExtra("tolocation", tvEndLocation);
                mIntent.putExtra("hlatitude", fromroutehaultlat);
                mIntent.putExtra("hlongitude", fromroutehaultlong);
                mIntent.putExtra("hlocation", edPlace3.getText().toString());
                mIntent.putExtra("htype", "breakfast");
                mIntent.putExtra("hlatitude1", toroutehaultlat);
                mIntent.putExtra("hlongitude1", toroutehaultlong);
                mIntent.putExtra("hlocation1", edPlace4.getText().toString());
                mIntent.putExtra("htype1", "Overnight");
                startActivity(mIntent);
            }
        }
    }
}
