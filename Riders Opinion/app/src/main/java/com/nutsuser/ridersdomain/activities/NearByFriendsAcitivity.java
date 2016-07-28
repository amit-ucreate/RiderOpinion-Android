package com.nutsuser.ridersdomain.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.adapter.AdapterDestination;
import com.nutsuser.ridersdomain.adapter.CustomGridAdapter;
import com.nutsuser.ridersdomain.adapter.PlaceArrayAdapter;
import com.nutsuser.ridersdomain.async.AsyncImageView;
import com.nutsuser.ridersdomain.chat.SampleCometChatActivity;
import com.nutsuser.ridersdomain.services.GPSService;
import com.nutsuser.ridersdomain.services.GPS_Services_Setting;
import com.nutsuser.ridersdomain.utils.CustomizeDialog;
import com.nutsuser.ridersdomain.utils.PrefsManager;
import com.nutsuser.ridersdomain.view.CustomDialog;
import com.nutsuser.ridersdomain.web.api.FileUploadService;
import com.nutsuser.ridersdomain.web.api.ServiceGenerator;
import com.nutsuser.ridersdomain.web.pojos.DataNearByFreind;
import com.nutsuser.ridersdomain.web.pojos.NearByFreinds;
import com.nutsuser.ridersdomain.web.pojos.TrackUserListData;
import com.rollbar.android.Rollbar;


import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;

/**
 * Created by ucreateuser on 5/17/2016.
 */
public class NearByFriendsAcitivity extends BaseActivity implements  GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks{

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.liSearchBar)
    LinearLayout liSearchBar;
    private Activity activity;
    @Bind(R.id.tvTitleToolbar)
    TextView tvTitleToolbar;
    PrefsManager prefsManager;
    String AccessToken, UserId;
    String star_lat, star_long;
    double start_lat1, start_long1;
    GPSService mGPSService;
    private GoogleMap map;
    private Marker marker;
    ArrayList<DataNearByFreind> nearByFreindsData;

    ArrayList<String> lat;
    ArrayList<String> log;
    ArrayList<String> url;
    ArrayList<String> ids;
    ArrayList<String> ridername;

    //=========== slider items====================//
    public static String[] prgmNameList = {"My Rides", "My Messages", "My Friends", "Chats", "Favorites", "Notifications", "Settings", "Riders Near By"};
    public static int[] prgmImages = {R.drawable.ic_menu_my_rides, R.drawable.ic_menu_my_messages, R.drawable.menu_my_friends, R.drawable.ic_menu_menu_chats, R.drawable.menu_fav_destinations, R.drawable.ic_menu_menu_notifications, R.drawable.menu_settings, R.drawable.icon_nearby};
    public static Class[] classList = {MyRidesRecyclerView.class, RecentChatListActivity.class, MyFriendsActivity.class, ComingSoon.class, FavouriteDesination.class, NotificationListActivity.class, SettingsActivity.class, NearByFriendsAcitivity.class};
    @Bind(R.id.autoLocation)
    AutoCompleteTextView autoLocation;
    @Bind(R.id.flSearch)
    FrameLayout flSearch;
    @Bind(R.id.gridView1)
    GridView gridView1;
    private ActionBarDrawerToggle mDrawerToggle;
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.lvSlidingMenu)
    LinearLayout lvSlidingMenu;
    @Bind(R.id.sdvDp)
    SimpleDraweeView sdvDp;
    @Bind(R.id.btUpdateProfile)
    Button btUpdateProfile;
    @Bind(R.id.btFullProfile)
    Button btFullProfile;
    @Bind(R.id.tvName)
    TextView tvName;

    String LOG_TAG="NearByFriendsActivity";
    private GoogleApiClient mGoogleApiClient;
    private PlaceArrayAdapter mPlaceArrayAdapter;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));
    private static final int GOOGLE_API_CLIENT_ID = 0;
    String mStringlong, mStringlat;
//=====================================//



    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e(LOG_TAG, "Place query did not complete. Error: " +
                        places.getStatus().toString());
                return;
            }
            // Selecting the first object buffer.

            // Selecting the first object buffer.
            Place place;

            try {
                place = places.get(0);
            } catch (IllegalStateException e) {
                places.release();
                return;
            }
           // final Place place = places.get(0);
            CharSequence attributions = places.getAttributions();

            // mNameTextView.setText("NAME:"+ Html.fromHtml(place.getName() + ""));
            // mAddressTextView.setText("ADDRESS: "+Html.fromHtml(place.getAddress() + ""));
            //  mIdTextView.setText(Html.fromHtml("PLACEID:" + place.getId() + ""));
            mStringlong = "" + Html.fromHtml(place.getLatLng().longitude + "");
              mStringlat = "" + Html.fromHtml(place.getLatLng().latitude + "");
            Log.e("Long:", "" + mStringlong);
            Log.e("Latitude:", "" + mStringlat);

            searchByLocation(mStringlat , mStringlong);
            //  mPhoneTextView.setText(Html.fromHtml("Lat:" + place.getLatLng().latitude + "--long:" + latlong));
            //mWebTextView.setText(place.getWebsiteUri() + "");
            if (attributions != null) {
                // mAttTextView.setText(Html.fromHtml(attributions.toString()));
            }
        }

    };

    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            Log.i(LOG_TAG, "Selected: " + item.description);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            Log.i(LOG_TAG, "Fetching details for ID: " + item.placeId);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_friends);
        try {
            activity = this;
            prefsManager = new PrefsManager(activity);
            nearByFreindsData = new ArrayList<>();
            mGoogleApiClient = new GoogleApiClient.Builder(NearByFriendsAcitivity.this)
                    .addApi(Places.GEO_DATA_API)
                    .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
                    .addConnectionCallbacks(this)
                    .build();
            ButterKnife.bind(this);
            autoLocation.setThreshold(1);

            autoLocation.setOnItemClickListener(mAutocompleteClickListener);
            mPlaceArrayAdapter = new PlaceArrayAdapter(this, android.R.layout.simple_list_item_1,
                    BOUNDS_MOUNTAIN_VIEW, null);
            autoLocation.setAdapter(mPlaceArrayAdapter);

            setupActionBar(toolbar);
            setFonts();
            initializeList();
            setSliderMenu();
            setDrawerSlider();
            flSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                searchByLocation(mStringlat,mStringlong);
                }
            });
            autoLocation.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    boolean handled = false;
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        searchByLocation(mStringlat,mStringlong);
                        handled = true;
                    }
                    return handled;
                }
            });
        }catch(Exception e){
            Rollbar.reportException(e, "minor", "NearByFriendsActivity on create");
        }

    }

    private void searchByLocation(String latitute , String lng) {

        lat.clear();
        log.clear();
        url.clear();
        // url1.clear();
        ids.clear();

        showProgressDialog();

        try {

            AccessToken = prefsManager.getToken();
            UserId = prefsManager.getCaseId();
            String radius = prefsManager.getRadius();
            FileUploadService service = ServiceGenerator.createService(FileUploadService.BASE_URL_NEARBY_FRIENDS);
            Log.e("URL:", "" + FileUploadService.BASE_URL_NEARBY_FRIENDS);
            Log.e("UserId:", "" + UserId);
            Log.e("mStringlong:", "" + lng);
            Log.e("mStringlat:", "" + latitute);

            Log.e("AccessToken:", "" + AccessToken);


            service.FriendsSearchByLocation(UserId, latitute, lng, AccessToken,"50", new Callback<JsonObject>() {
                @Override
                public void success(JsonObject jsonObject, retrofit.client.Response response) {

                    Type type = new TypeToken<NearByFreinds>() {
                    }.getType();
                    NearByFreinds mNearByFriends = new Gson().fromJson(jsonObject.toString(), type);
                    Log.e(LOG_TAG, "" + jsonObject);
                    nearByFreindsData.clear();
                    dismissProgressDialog();

                    // if (mNearByFriends.getSuccess()==1) {
                    // if (mNearByFriends.getSuccess()==1) {
                    // Log.e("mNearByFriends Data:", "" + mNearByFriends.getData());

                    Log.e("nearBy search data",""+nearByFreindsData.size());
                    nearByFreindsData.addAll(mNearByFriends.getData());
                   Log.e("nearByFreindsData",""+nearByFreindsData.size());
                    if(nearByFreindsData.size()==0){
                        CustomDialog.showProgressDialog(NearByFriendsAcitivity.this, mNearByFriends.getMessage().toString());
                    }
                    else {
                        for (int i = 0; i < nearByFreindsData.size(); i++) {
                            lat.add(nearByFreindsData.get(i).getLatitude());
                            log.add(nearByFreindsData.get(i).getLongitude());
                            Log.e("mNearByFriends Image:", "" + nearByFreindsData.get(i).getImage());

                            url.add(nearByFreindsData.get(i).getImage());
                            ids.add(nearByFreindsData.get(i).getUserId());
                            ridername.add(nearByFreindsData.get(i).getUsername());
                        }
                    }
                    Log.e("nameLIst Size: ", " " + ridername.size());
                    Log.e("id Size: ", " " + ids.size());

                    if (isNetworkConnected()) {
                        map.clear();


                        for (int i = 0; i < url.size(); i++) {
                            double mLatitude;
                            double mLongitude;
                            mLatitude = Double.valueOf(lat.get(i));
                            mLongitude = Double.valueOf(log.get(i));
                            LatLng lat = new LatLng(mLatitude, mLongitude);
                            asynshowAllLocation(i, lat);
                        }

                    } else {
                        showToast("Internet Not Connected");
                    }


//                    } else {
//                        CustomDialog.showProgressDialog(NearByFriendsAcitivity.this, mNearByFriends.getMessage().toString());
//                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    dismissProgressDialog();
                    Log.d("DataFeteching------", "Data Feteching Failure......" + error);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            Rollbar.reportException(e, "minor", "NearByFriendsActivity Search by location API.");

        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        View v =getCurrentFocus();
        boolean ret = super.dispatchTouchEvent(event);

        if (v instanceof EditText) {
            View w = getCurrentFocus();
            int scrcoords[] = new int[2];
            w.getLocationOnScreen(scrcoords);
            float x = event.getRawX() + w.getLeft() - scrcoords[0];
            float y = event.getRawY() + w.getTop() - scrcoords[1];

            Log.d("Activity", "Touch event "+event.getRawX()+","+event.getRawY()+" "+x+","+y+" rect "+w.getLeft()+","+w.getTop()+","+w.getRight()+","+w.getBottom()+" coords "+scrcoords[0]+","+scrcoords[1]);
            if (event.getAction() == MotionEvent.ACTION_UP && (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w.getBottom()) ) {

                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
            }
        }
        return ret;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(mDrawerLayout.isDrawerOpen(lvSlidingMenu)){
            showProfileImage();
            mDrawerLayout.closeDrawer(lvSlidingMenu);
        }

        GPSService mGPSService = new GPSService(this);
        mGPSService.getLocation();

        if (mGPSService.isLocationAvailable == false) {

            // Here you can ask the user to try again, using return; for that
            // Toast.makeText(getApplicationContext(), "Your location is not available, please try again.", Toast.LENGTH_SHORT).show();
            return;

            // Or you can continue without getting the location, remove the return; above and uncomment the line given below
            // address = "Location not available";
        } else {

            // Getting location co-ordinates
            double latitude = mGPSService.getLatitude();
            double longitude = mGPSService.getLongitude();
            // Toast.makeText(getApplicationContext(), "Latitude:" + latitude + " | Longitude: " + longitude, Toast.LENGTH_LONG).show();

            double start = mGPSService.getLatitude();
            double end = mGPSService.getLongitude();
            String mStringlat = Double.toString(start);
            String mStringlong = Double.toString(end);
            Log.e("getLocationAddress()", "" + mGPSService.getLocationAddress());
         //   autoLocation.setText(mGPSService.getLocationAddress());
        }


        initializeMap();

    }

    private void initializeList() {

        lat = new ArrayList<String>();
        log = new ArrayList<String>();
        url = new ArrayList<String>();
        ids = new ArrayList<>();
        ridername = new ArrayList<>();
    }

    private void setFonts() {
        tvTitleToolbar.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/MACHINEN.TTF"));
        tvTitleToolbar.setText("RIDERS NEAR BY");
        tvName.setTypeface(typeFaceMACHINEN);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(NearByFriendsAcitivity.this, MainScreenActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("MainScreenResponse", "OPEN");
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * NearByFriendsListInfo info : json parsing.
     */
    public void NearByFriendsListInfo() {
        lat.clear();
        log.clear();
        url.clear();
        // url1.clear();
        ids.clear();

        showProgressDialog();

        try {
            prefsManager = new PrefsManager(NearByFriendsAcitivity.this);
            AccessToken = prefsManager.getToken();
            UserId = prefsManager.getCaseId();
            String radius = prefsManager.getRadius();
            FileUploadService service = ServiceGenerator.createService(FileUploadService.BASE_URL_NEARBY_FRIENDS);
            Log.e("URL:", "" + FileUploadService.BASE_URL_NEARBY_FRIENDS);
            Log.e("UserId:", "" + UserId);
            Log.e("star_long:", "" + star_long);
            Log.e("star_lat:", "" + star_lat);

            Log.e("AccessToken:", "" + AccessToken);

            service.nearByFriends(UserId, star_lat, star_long, AccessToken, new Callback<JsonObject>() {
                @Override
                public void success(JsonObject jsonObject, retrofit.client.Response response) {

                    Type type = new TypeToken<NearByFreinds>() {
                    }.getType();
                    NearByFreinds mNearByFriends = new Gson().fromJson(jsonObject.toString(), type);
                    Log.e("mNearByFriends :", "" + jsonObject);
                    nearByFreindsData.clear();
                    dismissProgressDialog();

                    // if (mNearByFriends.getSuccess()==1) {
                    // if (mNearByFriends.getSuccess()==1) {
                    // Log.e("mNearByFriends Data:", "" + mNearByFriends.getData());
                    nearByFreindsData.addAll(mNearByFriends.getData());
                    for (int i = 0; i < nearByFreindsData.size(); i++) {
                        lat.add(nearByFreindsData.get(i).getLatitude());
                        log.add(nearByFreindsData.get(i).getLongitude());
                        Log.e("mNearByFriends Image:", "" + nearByFreindsData.get(i).getImage());

                        url.add(nearByFreindsData.get(i).getImage());
                        ids.add(nearByFreindsData.get(i).getUserId());
                        ridername.add(nearByFreindsData.get(i).getUsername());
                    }

                    Log.e("nameLIst Size: ", " " + ridername.size());
                    Log.e("id Size: ", " " + ids.size());

                    if (isNetworkConnected()) {
                        map.clear();


                        for (int i = 0; i < url.size(); i++) {
                            double mLatitude;
                            double mLongitude;
                            mLatitude = Double.valueOf(lat.get(i));
                            mLongitude = Double.valueOf(log.get(i));
                            LatLng lat = new LatLng(mLatitude, mLongitude);
                            asynshowAllLocation(i, lat);
                        }

                    } else {
                        showToast("Internet Not Connected");
                    }


//                    } else {
//                        CustomDialog.showProgressDialog(NearByFriendsAcitivity.this, mNearByFriends.getMessage().toString());
//                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    dismissProgressDialog();
                    Log.d("DataFeteching------", "Data Feteching Failure......" + error);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            Rollbar.reportException(e, "minor", "NearByFriendsActivity NearBy friends list API.");

        }
    }

    //======== initiliaze the map===========//
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
                mGPSService = new GPSService(this);
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

                    start_lat1 = mGPSService.getLatitude();
                    start_long1 = mGPSService.getLongitude();
                    star_lat = String.valueOf(start_lat1);
                    star_long = String.valueOf(start_long1);
                    Log.e("TEST1:", "" + star_lat);
                    Log.e("TEST2:", "" + star_long);
                    if (isNetworkConnected()) {
                        NearByFriendsListInfo();
                    } else {
                        showToast("Internet Not Connected");
                    }

                }
                // make sure you close the gps after using it. Save user's battery power
                mGPSService.closeGPS();
                //new TheTask("https://s3.amazonaws.com/rideropinion/users/139/userImage/1455631200.jpeg",HAMBURG).execute();
                //setUpMap();
            }

        }
    }

    public void asynshowAllLocation(final int position, LatLng latlng) {
        Log.e("latlng:", "" + latlng + " position  " + position);
        if (position == 0) {
            map.clear();
        }
        MarkerOptions marker = null;

        View view = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.asycustom_marker_layout, null);
        AsyncImageView ivMapImage = (AsyncImageView) view.findViewById(R.id.ivMapImage);
        FrameLayout fmlayout = (FrameLayout) view.findViewById(R.id.fmlayout);
        if (UserId.equals(ids.get(position))) {
//            Log.e("asynshowAllLocation url:", "" + url.get(position));
//            Log.e("asynshowAllLocation ids:", "" + ids.get(position));
//            Log.e("asynshowAllLocation position:", "" + position);
            fmlayout.setBackgroundResource(R.drawable.ic_your_locationstart);
            // create marker
            ivMapImage.downloadImage(url.get(position));
        } else {
//            Log.e("asynshowAllLocation else url:", "" + url.get(position));
//            Log.e("asynshowAllLocation else ids:", "" + ids.get(position));
//            Log.e("asynshowAllLocation else position:", "" + position);
            ivMapImage.downloadImage(url.get(position));
        }

        marker = new MarkerOptions().position(latlng).title(nearByFreindsData.get(position).getUsername()+","+position).snippet(nearByFreindsData.get(position).getBaseLocation()).snippet(nearByFreindsData.get(position).getVehicleName()).snippet(nearByFreindsData.get(position).getVehicleType());
        // Changing marker icon
        // pos.add(marker.getPosition());
        marker.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, view)));
        // adding marker
        //  marker.snippet(lat.get(position));
        map.addMarker(marker);
        if (UserId.equals(ids.get(position))) {
            CameraPosition cameraPosition = new CameraPosition.Builder().target(
                    latlng).zoom(10).build();
            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            map.getUiSettings().setZoomControlsEnabled(true);
        }
        else{
            CameraPosition cameraPosition = new CameraPosition.Builder().target(
                    latlng).zoom(10).build();
            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            map.getUiSettings().setZoomControlsEnabled(true);
        }
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        // check if map is created successfully or not
        if (map == null) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                    .show();
        }


        //=======InfoWindow click listener==========//
        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                String[] separated = marker.getTitle().split(",");

                int position=Integer.parseInt(separated[1]); // this will contain Marker Position
                Log.e("ID:", "IDDDDDD===   " + ids.get(position));

                Intent intent = new Intent(activity, PublicProfileScreen.class);
                intent.putExtra(PROFILE, true);
                intent.putExtra(PROFILE_IMAGE, url.get(position));
                intent.putExtra(USER_ID, ids.get(position));
                startActivity(intent);
            }
        });

        //=======custom InfoWindow adapter of marker==========//

        map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            private final View window = getLayoutInflater().inflate(
                    R.layout.custom_info_window_nearby_friends, null);

            @Override
            public View getInfoWindow(Marker marker) {
                String title = marker.getTitle();
                String[] separated = marker.getTitle().split(",");
                title= separated[0]; // this will contain RiderNAme
                int position=Integer.parseInt(separated[1]); // this will contain Marker Position
                TextView txtTitle = ((TextView) window
                        .findViewById(R.id.title));
                TextView txtPlace = ((TextView) window
                        .findViewById(R.id.snippetPlace));
                TextView txtVehicle = ((TextView) window
                        .findViewById(R.id.snippetVehicle));
                TextView txtVehicleType = ((TextView) window
                        .findViewById(R.id.snippetVehicleType));
                if (TextUtils.isEmpty(title)) {
                    txtTitle.setText("");
                } else {
                    txtTitle.setText(title);
                }

//                txtPlace.setText(nearByFreindsData.get(Integer.parseInt(marker.getId().replace("m", ""))).getBaseLocation());
//                txtVehicle.setText(nearByFreindsData.get(Integer.parseInt(marker.getId().replace("m", ""))).getVehicleName());
//                txtVehicleType.setText(nearByFreindsData.get(Integer.parseInt(marker.getId().replace("m", ""))).getVehicleType());

                txtPlace.setText(nearByFreindsData.get(position).getBaseLocation());
                txtVehicle.setText(nearByFreindsData.get(position).getVehicleName());
                txtVehicleType.setText(nearByFreindsData.get(position).getVehicleType());
                Log.e("snippet 1", "" + marker.getSnippet());
                return window;
            }

            @Override
            public View getInfoContents(Marker marker) {
                return null;
            }
        });


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

    //============ function to ser Drawer=================//
    private void setDrawerSlider() {
        mDrawerLayout.closeDrawer(lvSlidingMenu);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.icon_image_view, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ) {
            public void onDrawerClosed(View view) {

            }

            public void onDrawerOpened(View drawerView) {
                showProfileImage();

            }
        };
        mDrawerLayout.closeDrawer(lvSlidingMenu);
        showProfileImage();
    }

    /**
     * '
     * Show Profile Image
     ***/
    private void showProfileImage() {
        try {
            if (prefsManager.getUserName() == null) {
                tvName.setText("No Name");
            } else {
                tvName.setText(prefsManager.getUserName());            }

            if (prefsManager.getImageUrl() == null) {
                // Toast.makeText(MyRidesRecyclerView.this, "Image Address is Null", Toast.LENGTH_SHORT).show();
            } else {
                String imageUrl = prefsManager.getImageUrl();
                sdvDp.setImageURI(Uri.parse(imageUrl));
//                File file = new File(imageUrl);
//                sdvDp.setImageURI(Uri.fromFile(file));
            }
        } catch (NullPointerException e) {
            tvName.setText("No Name");

            if (prefsManager.getImageUrl() == null) {
                // Toast.makeText(MyRidesRecyclerView.this, "Image Address is Null", Toast.LENGTH_SHORT).show();
            } else {
                String imageUrl = prefsManager.getImageUrl();
                sdvDp.setImageURI(Uri.parse(imageUrl));
//                File file = new File(imageUrl);
//                sdvDp.setImageURI(Uri.fromFile(file));
            }
            Rollbar.reportException(e, "minor", "NearByFriendsActivity Show Profile Image");
        }
    }

    //============= function to set slider menu============//
    private void setSliderMenu() {
        gridView1.setAdapter(new CustomGridAdapter(this, prgmNameList, prgmImages));
        gridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 7) {
                    mDrawerLayout.closeDrawer(lvSlidingMenu);
                }
//                else if (position == 1) {
//                    Log.e("positiuon:", "" + classList[position]);
//                    intent_Calling(classList[position], "My Messages");
//                }
else {
                    Log.e("positiuon:", "" + classList[position]);
                    intentCalling(classList[position]);
                }
            }
        });
    }

    //======== function to move on next activty=========//
    public void intentCalling(Class name) {
        Intent mIntent = new Intent(NearByFriendsAcitivity.this, name);
        startActivity(mIntent);
        //finish();
    }

    public void intent_Calling(Class name, String na) {
        Intent mIntent = new Intent(NearByFriendsAcitivity.this, name);
        mIntent.putExtra(SCREEN_OPEN, na);
        startActivity(mIntent);
        finish();
    }

    //======== OnClick Listeners=============//
    @OnClick({R.id.btFullProfile, R.id.btUpdateProfile, R.id.ivMenu, R.id.rlProfile})
    void click(View view) {
        switch (view.getId()) {

            case R.id.btUpdateProfile:
                startActivity(new Intent(NearByFriendsAcitivity.this, ProfileActivity.class));
                break;
            case R.id.btFullProfile:
                startActivity(new Intent(NearByFriendsAcitivity.this, PublicProfileScreen.class));
                break;
            case R.id.ivMenu:
                if (mDrawerLayout.isDrawerOpen(lvSlidingMenu)) {
                    mDrawerLayout.closeDrawer(lvSlidingMenu);
                    liSearchBar.setVisibility(View.VISIBLE);
                }
                else {
                    mDrawerLayout.openDrawer(lvSlidingMenu);
                    liSearchBar.setVisibility(View.GONE);
                }
                break;
            case R.id.rlProfile:
                // startActivity(new Intent(MyFriendsActivity.this, ProfileActivity.class));
                break;

        }
    }
    @Override
    public void onConnected(Bundle bundle) {
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

//        Toast.makeText(this,
//                "Google Places API connection failed with error code:" +
//                        connectionResult.getErrorCode(),
//                Toast.LENGTH_LONG).show();
    }

}
