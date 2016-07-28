package com.nutsuser.ridersdomain.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

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
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.FIFOLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.adapter.CustomGridAdapter;
import com.nutsuser.ridersdomain.adapter.EndPlaceArrayAdapter;
import com.nutsuser.ridersdomain.adapter.PlaceArrayAdapter;
import com.nutsuser.ridersdomain.async.AsyncImageView;
import com.nutsuser.ridersdomain.chat.SampleCometChatActivity;
import com.nutsuser.ridersdomain.route.GMapV2GetRouteDirection;
import com.nutsuser.ridersdomain.services.GPSService;
import com.nutsuser.ridersdomain.utils.PrefsManager;
import com.rollbar.android.Rollbar;

import org.w3c.dom.Document;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by user on 12/7/2015.
 */
public class GetDirections extends BaseActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));

    String imageUrl;
    File file;
    private ProgressDialog Dialog;

    private static final String LOG_TAG = "GetDirection";
    private static final int GOOGLE_API_CLIENT_ID = 0;
    public static String[] prgmNameList = {"My Rides", "My Messages", "My Friends", "Chats", "Favorites", "Notifications", "Settings", "Riders Near By"};
    public static int[] prgmImages = {R.drawable.ic_menu_my_rides, R.drawable.ic_menu_my_friends, R.drawable.ic_menu_my_messages, R.drawable.ic_menu_menu_chats, R.drawable.ic_menu_fav_destinations, R.drawable.ic_menu_menu_notifications, R.drawable.ic_menu_menu_settings, R.drawable.icon_nearby};
    public static Class[] classList = {MyRidesRecyclerView.class, RecentChatListActivity.class, MyFriendsActivity.class, ComingSoon.class, FavouriteDesination.class, NotificationListActivity.class, SettingsActivity.class, NearByFriendsAcitivity.class};
    // public static String [] prgmNameList={"Riding Destinations","Meet 'N' Plan A Ride","Riding Events \n    ","Modifly Your Bikes","Healthy Riding","Get Directions","Notifications","Settings"};
    // public static int [] prgmImages={R.drawable.icon_menu_destination,R.drawable.icon_menu_meetplan,R.drawable.icon_menu_events,R.drawable.icon_modifybike,R.drawable.icon_menu_healthy_riding,R.drawable.icon_menu_get_direction,R.drawable.icon_menu_notification,R.drawable.icon_menu_settings};
    // public static Class [] classList={DestinationsListActivity.class,PlanRideActivity.class,EventsListActivity.class,ModifyBikeActivity.class,HealthyRidingActivity.class,GetDirections.class,NotificationScreen.class,SettingsActivity.class};
    private final LatLng HAMBURG = new LatLng(53.558, 9.927);
    private final LatLng HAMBURG_ = new LatLng(53.500, 9.900);
    public ImageLoader imageLoader;
    public DisplayImageOptions options;
    GMapV2GetRouteDirection v2GetRouteDirection;
    LocationManager locManager;
    Drawable drawable;
    Document document;
    EndPlaceArrayAdapter _ArrayAdapter;
    LatLng mString_end;

    private String destLat;
    private String destLong;
    LatLng mString_start;
    double start, end;
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
    @Bind(R.id.btMap)
    Button btMap;
    @Bind(R.id.btMapNAv)
    Button btMapNAv;



    // Google Map
    private GoogleMap map;
    private AutoCompleteTextView tvPlace3, tvPlace4;
    private GoogleApiClient mGoogleApiClient;
    private PlaceArrayAdapter mPlaceArrayAdapter;
    private Activity activity;
    private Marker customMarker;
    private LatLng markerLatLng,mString_current;
    private Marker marker;
    private Hashtable<String, String> markers;
    @Bind(R.id.sdvDp)
    SimpleDraweeView sdvDp;
    private ActionBarDrawerToggle mDrawerToggle;
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
            destLat= "" + Html.fromHtml("" + place.getLatLng().latitude);
            destLong = "" + Html.fromHtml("" + place.getLatLng().longitude);
            double start = Double.valueOf(destLat.trim()).doubleValue();
            double end = Double.valueOf(destLong.trim()).doubleValue();


            mString_end = new LatLng(start, end);
            hideKeyboard();
            //mString_end =Html.fromHtml(place.getLatLng() + "");
            GetRouteTask getRoute = new GetRouteTask();
            getRoute.execute();
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
                Place place;

                try {
                    place = places.get(0);
                } catch (IllegalStateException e) {
                    places.release();
                    return;
                }
                // Selecting the first object buffer.
               // final Place place = places.get(0);
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

                //  mPhoneTextView.setText(Html.fromHtml("Lat:" + place.getLatLng().latitude + "--long:" + latlong));
                //mWebTextView.setText(place.getWebsiteUri() + "");
                if (attributions != null) {
                    // mAttTextView.setText(Html.fromHtml(attributions.toString()));
                }
            }

        };
    }

    // Convert a view to bitmap
    public static Bitmap createDrawableFromView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getdirections);
        try {
            v2GetRouteDirection = new GMapV2GetRouteDirection();
            activity = this;
            ButterKnife.bind(this);
            setupActionBar();
            setFontsToTextViews();
            markerLatLng = new LatLng(48.8567, 2.3508);
            tvTitleToolbar.setText("Get Directions");
            prefsManager = new PrefsManager(this);
            imageUrl = prefsManager.getImageUrl();
            Log.e("imageUrl:", "" + imageUrl);
            file = new File(imageUrl);

            Log.e("file:", "" + file.getAbsolutePath());
            gridView1.setAdapter(new CustomGridAdapter(this, prgmNameList, prgmImages));
            gridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (position == 7) {
//
//                } else {
//                    if(position==1){
//                        Log.e("positiuon:", "" + classList[position]);
//                        intent_Calling(classList[position], "My Messages");
//                    }
//
//                    else{
                    Log.e("positiuon:", "" + classList[position]);
                    intentCalling(classList[position]);
                    // }
                    //  }

                }
            });
            initImageLoader();
            markers = new Hashtable<String, String>();
            imageLoader = ImageLoader.getInstance();

            options = new DisplayImageOptions.Builder()
                    .showStubImage(R.drawable.ic_launcher)        //	Display Stub Image
                    .showImageForEmptyUri(R.drawable.ic_launcher)    //	If Empty image found
                    .cacheInMemory()
                    .cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565).build();
            // map.setMyLocationEnabled(true);

            // mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(2));
            // initializeMap();

            // ATTENTION: This "addApi(AppIndex.API)"was auto-generated to implement the App Indexing API.
            // See https://g.co/AppIndexing/AndroidStudio for more information.
            mGoogleApiClient = new GoogleApiClient.Builder(GetDirections.this)
                    .addApi(Places.GEO_DATA_API)
                    .enableAutoManage(this, GOOGLE_API_CLIENT_ID, GetDirections.this)
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
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    hideKeyboard();
                }
            }, 300);
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
                // Toast.makeText(getApplicationContext(), "Latitude:" + latitude + " | Longitude: " + longitude, Toast.LENGTH_LONG).show();

                double start_ = mGPSService.getLatitude();
                double end_ = mGPSService.getLongitude();
                mString_start = new LatLng(start_, end_);
                Log.e("mString_start:", "" + mString_start);
                Log.e("getLocationAddress()", "" + mGPSService.getLocationAddress());


                tvPlace3.setText(mGPSService.getLocationAddress());

            }


            // make sure you close the gps after using it. Save user's battery power
            mGPSService.closeGPS();
        }catch(Exception e){
            Rollbar.reportException(e, "minor", "First Page activity Activity on create");
        }
    }

    private void setFontsToTextViews() {
        tvTitleToolbar.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/MACHINEN.TTF"));
        tvName.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/MACHINEN.TTF"));

    }
    /** '
     *Show Profile Image
     ***/
    private void showProfileImage(){
        if(prefsManager.getUserName()==null){
            tvName.setText("No Name");
        }
        else{
            tvName.setText(prefsManager.getUserName());
        }
        if (prefsManager.getImageUrl() == null) {
            //Toast.makeText(GetDirections.this, "Image Address is Null", Toast.LENGTH_SHORT).show();
        }
        else {
            String imageUrl = prefsManager.getImageUrl();
            sdvDp.setImageURI(Uri.parse(imageUrl));
//            File file = new File(imageUrl);
//            sdvDp.setImageURI(Uri.fromFile(file));
        }

    }
    @Override
    protected void onResume() {
        super.onResume();
        initializeMap();
        if (mDrawerLayout.isDrawerOpen(lvSlidingMenu))
        {
            showProfileImage();
            mDrawerLayout.closeDrawer(lvSlidingMenu);
        }
    }

    public void intentCalling(Class name) {
        Intent mIntent = new Intent(GetDirections.this, name);
        startActivity(mIntent);

    }

    public void intent_Calling(Class name,String na) {
        Intent mIntent = new Intent(GetDirections.this, name);
        mIntent.putExtra(SCREEN_OPEN,na);
        startActivity(mIntent);

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
                //setUpMap();
            }

        }
    }

    private void setupActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_home);
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
                mGoogleApiClient.disconnect();
                Intent intent = new Intent(GetDirections.this, MainScreenActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("MainScreenResponse", "OPEN");
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(GetDirections.this, MainScreenActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("MainScreenResponse", "OPEN");
        startActivity(intent);
        finish();
    }

    @OnClick({R.id.ivMenu, R.id.rlProfile, R.id.btMap,R.id.btFullProfile,R.id.btUpdateProfile,R.id.btMapNAv})
    void onclick(View view) {
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
            case R.id.btMap:
//                GetRouteTask getRoute = new GetRouteTask();
//                getRoute.execute();
                navigateToGoogleMap(tvPlace4.getText().toString());
                break;
            case R.id.btUpdateProfile:
                startActivity(new Intent(activity, ProfileActivity.class));
                break;
            case R.id.btFullProfile:
                startActivity(new Intent(activity, PublicProfileScreen.class));
                break;
            case R.id.btMapNAv:
                //navigateToGoogleMap(String.valueOf(mString_end.latitude),String.valueOf(mString_end.longitude));
                break;
        }
    }

    private void initImageLoader() {
        int memoryCacheSize;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            int memClass = ((ActivityManager)
                    getSystemService(Context.ACTIVITY_SERVICE))
                    .getMemoryClass();
            memoryCacheSize = (memClass / 8) * 1024 * 1024;
        } else {
            memoryCacheSize = 2 * 1024 * 1024;
        }

        final ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                this).threadPoolSize(5)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .memoryCacheSize(memoryCacheSize)
                .memoryCache(new FIFOLimitedMemoryCache(memoryCacheSize - 1000000))
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO).enableLogging()
                .build();

        ImageLoader.getInstance().init(config);
    }

    private void setUpMap() {
        map.setInfoWindowAdapter(new CustomInfoWindowAdapter());
        View marker = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker_layout, null);
        final Marker hamburg = map.addMarker(new MarkerOptions()
                .position(HAMBURG)
                .title("Hamburg")
                .icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, marker))));
        markers.put(hamburg.getId(), "http://img.india-forums.com/images/100x100/37525-a-still-image-of-akshay-kumar.jpg");


        map.moveCamera(CameraUpdateFactory.newLatLngZoom(HAMBURG, 15));
        map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);


        customMarker = map.addMarker(new MarkerOptions()
                .position(markerLatLng)
                .title("TEST")
                .snippet("Description")
                .icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, marker))));
        markers.put(customMarker.getId(), "http://img.india-forums.com/images/100x100/37525-a-still-image-of-akshay-kumar.jpg");
        // map.moveCamera(CameraUpdateFactory.newLatLngZoom(HAMBURG, 15));

        final Marker custom_Marker = map.addMarker(new MarkerOptions()
                .position(HAMBURG_)
                .title("AMIT")
                .snippet("Description")
                .icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, marker))));
        markers.put(custom_Marker.getId(), "http://img.india-forums.com/images/100x100/37525-a-still-image-of-akshay-kumar.jpg");


        map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(com.google.android.gms.maps.model.Marker marker) {

                marker.showInfoWindow();
                return true;
            }
        });
        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {

                Log.e("ID:", "" + marker.getId());


                startActivity(new Intent(activity, PublicProfileScreen.class));
            }
        });

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

    private class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private View view;

        public CustomInfoWindowAdapter() {
            view = getLayoutInflater().inflate(R.layout.custom_info_window,
                    null);
        }

        @Override
        public View getInfoContents(Marker marker) {

            if (GetDirections.this.marker != null
                    && GetDirections.this.marker.isInfoWindowShown()) {
                GetDirections.this.marker.hideInfoWindow();
                GetDirections.this.marker.showInfoWindow();
            }
            return null;
        }


        @Override
        public View getInfoWindow(final Marker marker) {
            GetDirections.this.marker = marker;


            String url = null;


            if (marker.getId() != null && markers != null && markers.size() > 0) {
                if (markers.get(marker.getId()) != null &&
                        markers.get(marker.getId()) != null) {
                    url = markers.get(marker.getId());
                }
            }
            final ImageView image = ((ImageView) view.findViewById(R.id.badge));

            if (url != null && !url.equalsIgnoreCase("null")
                    && !url.equalsIgnoreCase("")) {
                imageLoader.displayImage(url, image, options,
                        new SimpleImageLoadingListener() {
                            @Override
                            public void onLoadingComplete(String imageUri,
                                                          View view, Bitmap loadedImage) {
                                super.onLoadingComplete(imageUri, view,
                                        loadedImage);
                                getInfoContents(marker);
                            }
                        });
            } else {
                image.setImageResource(R.drawable.ic_launcher);
            }

            final String title = marker.getTitle();
            final TextView titleUi = ((TextView) view.findViewById(R.id.title));
            if (title != null) {
                titleUi.setText(title);
            } else {
                titleUi.setText("");
            }

            final String snippet = marker.getSnippet();
            final TextView snippetUi = ((TextView) view
                    .findViewById(R.id.snippet));
            if (snippet != null) {
                snippetUi.setText(snippet);
            } else {
                snippetUi.setText("");
            }

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("", "CLICKED");
                    //   startActivity(new Intent(activity, PublicProfileScreen.class));
                }
            });

            return view;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(Dialog != null){
            Dialog.dismiss();
        }
    }
    /**
     * @author Amit Agnihotri
     *         This class Get Route on the map
     */

    private class GetRouteTask extends AsyncTask<String, Void, String> {

        String response = "";

        @Override
        protected void onPreExecute() {
            Dialog = new ProgressDialog(GetDirections.this);
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
                PolylineOptions rectLine = new PolylineOptions().width(12).color(
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

                map.animateCamera(cameraUpdate);
                for (int i = 1; i < 4; i++) {
                    if (i == 1) {
                        addMarker(mString_start, 1);
                    } else if (i == 2) {
                        addMarker(mString_end, 2);
                    }
                  /*  else if(i==3){
                        addMarker(mString_current, 3);
                    }*/
                }

            }

            Dialog.dismiss();
        }
    }
    public void addMarker(LatLng latLng, int pos) {
        MarkerOptions marker = null;
        View view = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.asycustom_marker_layout, null);
        FrameLayout fmlayout = (FrameLayout) view.findViewById(R.id.fmlayout);
        AsyncImageView ivMapImage = (AsyncImageView) view.findViewById(R.id.ivMapImage);

        if (pos == 1) { //fmlayout.setBackgroundResource(R.drawable.ic_your_locationstart);
            if (TextUtils.isEmpty(imageUrl)) {
                ivMapImage.setDefaultImage();
            }
            else {
   // try {
//    URL url = new URL(prefsManager.getImageUrl());
//    Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//     Bitmap bmp = BitmapFactory.decodeFile(file.getAbsolutePath());
//    Log.e("bmp:", "" + bmp);
//    Bitmap bitmap = getRoundedShape(bmp);
//    Log.e("bitmap:", "" + bitmap);
    ivMapImage.downloadImage(prefsManager.getImageUrl());
//    }catch(Exception e){
//
//    }
            }
            fmlayout.setBackgroundResource(R.drawable.ic_your_locationstart);
        }
        else if(pos==2){
            fmlayout.setBackgroundResource(R.drawable.ic_endlocation);
        }
        else {
          /*  fmlayout.setBackgroundResource(R.drawable.ic_your_locationstart);
            if (TextUtils.isEmpty(imageUrl)) {
                ivMapImage.setImageResource(R.drawable.app_icon);
            }
            else {
                Bitmap bmp = BitmapFactory.decodeFile(file.getAbsolutePath());
                Log.e("bmp:",""+bmp);
                Bitmap bitmap=getRoundedShape(bmp);
                Log.e("bitmap:",""+bitmap);
                ivMapImage.setImageBitmap(bitmap);
            }*/

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

            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            map.getUiSettings().setZoomControlsEnabled(true);
        } else {

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


    private void navigateToGoogleMap(String destination){
    Uri gmmIntentUri = Uri.parse("google.navigation:q="+destination);
     //   Uri gmmIntentUri = Uri.parse("google.navigation:q=Taronga+Zoo,+Sydney+Australia");
    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
    mapIntent.setPackage("com.google.android.apps.maps");
    startActivity(mapIntent);
    }
}
