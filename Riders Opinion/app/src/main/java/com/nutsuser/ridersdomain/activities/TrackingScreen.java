package com.nutsuser.ridersdomain.activities;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Path;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.FIFOLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.async.AsyncImageView;
import com.nutsuser.ridersdomain.services.BroadcastService;
import com.nutsuser.ridersdomain.services.GPSService;
import com.nutsuser.ridersdomain.utils.ApplicationGlobal;
import com.nutsuser.ridersdomain.utils.CustomizeDialog;
import com.nutsuser.ridersdomain.utils.PrefsManager;
import com.nutsuser.ridersdomain.web.api.FileUploadService;
import com.nutsuser.ridersdomain.web.api.ServiceGenerator;
import com.nutsuser.ridersdomain.web.pojos.TrackUserList;
import com.nutsuser.ridersdomain.web.pojos.TrackUserListData;
import com.rollbar.android.Rollbar;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;

/**
 * Created by admin on 24-02-2016.
 */
public class TrackingScreen extends AppCompatActivity implements
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnInfoWindowClickListener {
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    ArrayList<String> lat;
    ArrayList<String> log;
    ArrayList<String> url;
    ArrayList<Bitmap> url1;
    ArrayList<String> ids;
    ArrayList<String> ridername;
    ArrayList<String> riderdist;

    ArrayList<LatLng> pos;

    Bitmap bitmap;
    Marker marker1;
    //
    public static String eventId;
    private final LatLng HAMBURG = new LatLng(53.558, 9.927);
    private final LatLng HAMBURG_ = new LatLng(53.500, 9.900);
    private final LatLng _HAMBURG_ = new LatLng(53.400, 9.800);
    public DisplayImageOptions options;
    Bitmap bitmapCuurent;
    String user, distance;
    int cuurentPosition;
    MarkerOptions markerCurrent;
    Marker markerCurrentChanged;
    ArrayList<Marker> allmarkers;
    ArrayList<MarkerOptions> allmarkerOptionsArrayListCurrent;
    ArrayList<String> arrayListPostion;
    Toolbar toolbar;
    PrefsManager prefsManager;
    CustomizeDialog mCustomizeDialog;
    String AccessToken, UserId;
    double start1, end1;
    String star_lat, star_long;
    // Google Map
    GPSService mGPSService;
    ArrayList<Bitmap> bitmapArrayList;
    ArrayList<LatLng> latLngArrayList;
    ArrayList<String> UsernamearrayList;
    ArrayList<String> DistancearrayList;
    ArrayList<TrackUserListData> data = new ArrayList<>();
    TextView tvStartStop;
    ImageLoader imageLoader;
    GPS_Service gps_service;
    Map<String, String> params = new HashMap<>();
    private GoogleMap map;
    private Marker marker;


    // broadcast receiver
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (isNetworkConnected()){
                map.clear();
                String jsonobject = intent.getExtras().getString("UpdateUI");
               // Log.e("jsonObject:", "" + jsonobject.toString());
                Type type = new TypeToken<TrackUserList>() {
                }.getType();
                TrackUserList trackUserList = new Gson().fromJson(jsonobject.toString(), type);
                data.clear();
                if (trackUserList.getSuccess().equals("1")) {
                    dismissProgressDialog();
                    data.addAll(trackUserList.getData());
                    url.clear();

                    for (int i = 0; i < data.size(); i++) {
                        lat.add(data.get(i).getLatitude());
                        log.add(data.get(i).getLongitude());
                        url.add(data.get(i).getImage());
                        ids.add(data.get(i).getUserId());
                        ridername.add(data.get(i).getUsername());
                        riderdist.add(data.get(i).getRideDistance());
                    }
                    if (isNetworkConnected()) {
                        map.clear();

                        for (int i = 0; i < url.size(); i++) {
                            double mLatitude;
                            double mLongitude;
                            mLatitude = Double.valueOf(data.get(i).getLatitude());
                            mLongitude = Double.valueOf(data.get(i).getLongitude());
                            LatLng lat = new LatLng(mLatitude, mLongitude);
                            asynshowAllLocation(i, lat);

                        }

                    } else {
                        showToast("Internet Not Connected");
                    }


                } else {
                    dismissProgressDialog();
                }
            }
            else{
                showToast("Internet Not Connected");
            }


            //updateUI(intent);

        }
    };

    public static Bitmap GetBitmapClippedCircle(Bitmap bitmap) {

        final int width = bitmap.getWidth();
        final int height = bitmap.getHeight();
        final Bitmap outputBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        final Path path = new Path();
        path.addCircle(
                (float) (width / 2)
                , (float) (height / 2)
                , (float) Math.min(width, (height / 2))
                , Path.Direction.CCW);

        final Canvas canvas = new Canvas(outputBitmap);
        canvas.clipPath(path);
        canvas.drawBitmap(bitmap, 0, 0, null);
        return outputBitmap;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trackinglocation);
        try {
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            tvStartStop = (TextView) findViewById(R.id.tvStartStop);
            setupActionBar();
            initImageLoader();
            gps_service = new GPS_Service(this);

            bitmapArrayList = new ArrayList<>();
            latLngArrayList = new ArrayList<>();
            UsernamearrayList = new ArrayList<>();
            DistancearrayList = new ArrayList<>();
            // delclear
            allmarkers = new ArrayList<>();
            arrayListPostion = new ArrayList<>();
            allmarkerOptionsArrayListCurrent = new ArrayList<>();
            latLngArrayList.clear();
            bitmapArrayList.clear();
            DistancearrayList.clear();
            UsernamearrayList.clear();
            lat = new ArrayList<String>();
            log = new ArrayList<String>();
            url = new ArrayList<String>();
            url1 = new ArrayList<Bitmap>();
            ids = new ArrayList<>();
            ridername = new ArrayList<>();
            riderdist = new ArrayList<>();
            pos = new ArrayList<>();

            if (!isGooglePlayServicesAvailable()) {
                finish();
            }


            imageLoader = ImageLoader.getInstance();

            options = new DisplayImageOptions.Builder()
                    .showStubImage(R.drawable.ic_launcher)        //	Display Stub Image
                    .showImageForEmptyUri(R.drawable.ic_launcher)    //	If Empty image found
                    .cacheInMemory()
                    .cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565).build();
            imageLoader = ImageLoader.getInstance();
            eventId = getIntent().getStringExtra("eventId");
            prefsManager = new PrefsManager(TrackingScreen.this);
            UserId = prefsManager.getCaseId();
            prefsManager = new PrefsManager(TrackingScreen.this);
            if (prefsManager.isServicesRunning()) {
                tvStartStop.setText("STOP TRACKING");
            } else {
                tvStartStop.setText("START TRACKING");
            }
            tvStartStop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (prefsManager.isServicesRunning()) {
                        tvStartStop.setText("START TRACKING");
                        stopService(new Intent(TrackingScreen.this, BroadcastService.class));
                        Log.e("UI Update", "Font Update UI STOP");
                        prefsManager.setServicesRunning(false);
                    } else {
                        prefsManager.setServicesRunning(true);
                        startService(new Intent(TrackingScreen.this, BroadcastService.class));
                        tvStartStop.setText("STOP TRACKING");
                        Log.e("UI Update", "Font Update UI");
                    }

                    //startService(new Intent(TrackingScreen.this, LocationService.class));
                    //stopService(new Intent(TrackingScreen.this, LocationService.class));
                }
            });
            gps_service.getLocation();
            if (gps_service.isLocationAvailable == false) {

                // Here you can ask the user to try again, using return; for that
                Toast.makeText(getApplicationContext(), "Your location is not available, please try again.", Toast.LENGTH_SHORT).show();
                return;

                // Or you can continue without getting the location, remove the return; above and uncomment the line given below
                // address = "Location not available";
            } else {

                Log.e("RUNNUNG", "RUNNING");

            }
        }catch(Exception e){
            Rollbar.reportException(e, "minor", "Tracking screen on create");
        }


    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initializeMap();

        registerReceiver(broadcastReceiver, new IntentFilter(BroadcastService.BROADCAST_ACTION));

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(TrackingScreen.this, MainScreenActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("MainScreenResponse", "OPEN");
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_home);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
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
                    Toast.makeText(getApplicationContext(), "Latitude:" + latitude + " | Longitude: " + longitude, Toast.LENGTH_LONG).show();

                    start1 = mGPSService.getLatitude();
                    end1 = mGPSService.getLongitude();
                    star_lat = String.valueOf(start1);
                    star_long = String.valueOf(end1);
                    Log.e("TEST1:", "" + star_lat);
                    Log.e("TEST2:", "" + star_long);
                    if (isNetworkConnected()) {
                        RidingListmodelinfo();
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

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.e("Position:", "" + marker.getPosition());

        return false;
    }


    /**
     * Rider info .
     */
    public void RidingListmodelinfo() {
        showProgressDialog();
        lat.clear();
        log.clear();
        url.clear();
        url1.clear();
        ids.clear();
        pos.clear();
        Log.e("riding destination", "riding destination");
        try {
            prefsManager = new PrefsManager(TrackingScreen.this);
            AccessToken = prefsManager.getToken();
            UserId = prefsManager.getCaseId();
            String radius = prefsManager.getRadius();
            params.put("userId", UserId);
            params.put("eventId", eventId);
            params.put("lat", star_lat);
            params.put("long", star_long);
            params.put("accessToken", AccessToken);
            Log.e("UserId:", "" + UserId);
            Log.e("eventId:", "" + eventId);
            Log.e("star_lat:", "" + star_lat);
            Log.e("star_long:", "" + star_long);
            Log.e("AccessToken:", "" + AccessToken);


            FileUploadService service = ServiceGenerator.createService(FileUploadService.BASE_URL);
            //TypedFile typedFile = new TypedFile("multipart/form-data", destination);

            service.tracking_info(UserId, eventId, star_lat, star_long, AccessToken, new Callback<JsonObject>() {
                @Override
                public void success(JsonObject jsonObject, retrofit.client.Response response) {

                    Log.e("jsonObject:", "" + jsonObject.toString());
                    Type type = new TypeToken<TrackUserList>() {
                    }.getType();
                    TrackUserList trackUserList = new Gson().fromJson(jsonObject.toString(), type);
                    data.clear();
                    if (trackUserList.getSuccess().equals("1")) {
                        dismissProgressDialog();
                        data.addAll(trackUserList.getData());

                        for (int i = 0; i < data.size(); i++) {
                            lat.add(data.get(i).getLatitude());
                            log.add(data.get(i).getLongitude());
                            url.add(data.get(i).getImage());
                            ids.add(data.get(i).getUserId());
                            ridername.add(data.get(i).getUsername());
                            riderdist.add(data.get(i).getRideDistance());
                        }
                        if (isNetworkConnected()) {
                            map.clear();
                            //Log.e("URL::", "" + url.size());
                            // Log.e("url::", "" + url);

                            for (int i = 0; i < url.size(); i++) {
                                double mLatitude;
                                double mLongitude;
                                mLatitude = Double.valueOf(data.get(i).getLatitude());
                                mLongitude = Double.valueOf(data.get(i).getLongitude());
                                LatLng lat = new LatLng(mLatitude, mLongitude);
                                asynshowAllLocation(i, lat);
                            }


                        } else {
                            showToast("Internet Not Connected");
                        }


                    } else {
                        dismissProgressDialog();
                    }


                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e("Upload:", "" + error);
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
            Rollbar.reportException(e, "minor", "Tracking screen tracking_info API");

        }
    }


    public void showProgressDialog() {

        mCustomizeDialog = new CustomizeDialog(TrackingScreen.this);
        mCustomizeDialog.setCancelable(false);
        mCustomizeDialog.show();
        Log.e("HERE", "HERE");
    }

    public void dismissProgressDialog() {
        if (mCustomizeDialog != null && mCustomizeDialog.isShowing()) {
            mCustomizeDialog.dismiss();
            mCustomizeDialog = null;
        }
      /*  new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mCustomizeDialog != null && mCustomizeDialog.isShowing()) {
                    mCustomizeDialog.dismiss();
                    mCustomizeDialog = null;
                }
            }
        }, 1000);*/

    }

    public boolean isNetworkConnected() {
        if (ApplicationGlobal.isNetworkConnected(this))
            return true;
        else
            return false;
    }

    protected void showToast(String message) {

        Toast.makeText(TrackingScreen.this, message, Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onStop() {
        super.onStop();
        gps_service.closeGPS();

        unregisterReceiver(broadcastReceiver);
    }


    private void handleNewLocation(Location location) {
        Log.e("handleNewLocation", "handleNewLocation");
        double mLatitude;
        double mLongitude;
        mLatitude = location.getLatitude();
        mLongitude = location.getLongitude();
        LatLng latLng = new LatLng(mLatitude, mLongitude);

        if (url.size() == 0) {

        } else {
            showToast("LatLng: " + lat);
            for (int i = 0; i < url.size(); i++) {
                if (UserId.equals(ids.get(i))) {
                    asynshowAllLocation(i, latLng);
                } else {
                    mLatitude = Double.valueOf(lat.get(i));
                    mLongitude = Double.valueOf(log.get(i));
                    LatLng lat1 = new LatLng(mLatitude, mLongitude);
                    asynshowAllLocation(i, lat1);
                }

            }
        }

    }


    public void asynshowAllLocation(int position, LatLng latlng) {
        Log.e("latlng:", "" + latlng);
        if (position == 0) {
            map.clear();
        }
        MarkerOptions marker = null;
        View view = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.asycustom_marker_layout, null);
        AsyncImageView ivMapImage = (AsyncImageView) view.findViewById(R.id.ivMapImage);
        FrameLayout fmlayout = (FrameLayout) view.findViewById(R.id.fmlayout);
        if (UserId.equals(ids.get(position))) {
            Log.e("asynshowAllLocation url:", "" + url.get(position));
            Log.e("asynshowAllLocation ids:", "" + ids.get(position));
            Log.e("asynshowAllLocation position:", "" + position);
            fmlayout.setBackgroundResource(R.drawable.ic_your_locationstart);
            // create marker
            ivMapImage.downloadImage(url.get(position));
        } else {
            Log.e("asynshowAllLocation else url:", "" + url.get(position));
            Log.e("asynshowAllLocation else ids:", "" + ids.get(position));
            Log.e("asynshowAllLocation else position:", "" + position);
            ivMapImage.downloadImage(url.get(position));
        }

        marker = new MarkerOptions().position(latlng).title(ridername.get(position)).snippet(riderdist.get(position));
        // Changing marker icon
        pos.add(marker.getPosition());
        marker.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, view)));
        // adding marker
        //  marker.snippet(lat.get(position));
        map.addMarker(marker);
        if (UserId.equals(ids.get(position))) {
            CameraPosition cameraPosition = new CameraPosition.Builder().target(
                    latlng).zoom(20).build();
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
    }


    public class GPS_Service extends Service implements LocationListener {

        // Minimum time fluctuation for next update (in milliseconds)
        private static final long TIME = 1000;
        // Minimum distance fluctuation for next update (in meters)
        private static final long DISTANCE = 10;
        // saving the context for later use
        private final Context mContext;
        // if Location co-ordinates are available using GPS or Network
        public boolean isLocationAvailable = false;
        // Declaring a Location Manager
        protected LocationManager mLocationManager;
        // if GPS is enabled
        boolean isGPSEnabled = false;
        // if Network is enabled
        boolean isNetworkEnabled = false;
        // Location and co-ordinates coordinates
        Location mLocation;
        double mLatitude;
        double mLongitude;

        public GPS_Service(Context context) {
            this.mContext = context;
            mLocationManager = (LocationManager) mContext
                    .getSystemService(LOCATION_SERVICE);

        }

        /**
         * Returs the Location
         *
         * @return Location or null if no location is found
         */
        public Location getLocation() {
            try {

                // Getting GPS status
                isGPSEnabled = mLocationManager
                        .isProviderEnabled(LocationManager.GPS_PROVIDER);

                // If GPS enabled, get latitude/longitude using GPS Services
                if (isGPSEnabled) {
                    mLocationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER, TIME, DISTANCE, this);
                    if (mLocationManager != null) {
                        mLocation = mLocationManager
                                .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (mLocation != null) {
                            mLatitude = mLocation.getLatitude();
                            mLongitude = mLocation.getLongitude();
                            isLocationAvailable = true; // setting a flag that
                            // location is available
                            return mLocation;
                        }
                    }
                }

                // If we are reaching this part, it means GPS was not able to fetch
                // any location
                // Getting network status
                isNetworkEnabled = mLocationManager
                        .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                if (isNetworkEnabled) {
                    mLocationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER, TIME, DISTANCE, this);
                    if (mLocationManager != null) {
                        mLocation = mLocationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (mLocation != null) {
                            mLatitude = mLocation.getLatitude();
                            mLongitude = mLocation.getLongitude();
                            isLocationAvailable = true; // setting a flag that
                            // location is available
                            return mLocation;
                        }
                    }
                }
                // If reaching here means, we were not able to get location neither
                // from GPS not Network,
                if (!isGPSEnabled) {
                    // so asking user to open GPS
                    askUserToOpenGPS();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            // if reaching here means, location was not available, so setting the
            // flag as false
            isLocationAvailable = false;
            return null;
        }

        /**
         * Gives you complete address of the location
         *
         * @return complete address in String
         */
        public String getLocationAddress() {

            if (isLocationAvailable) {

                Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
                // Get the current location from the input parameter list
                // Create a list to contain the result address
                List<Address> addresses = null;
                try {
                /*
                 * Return 1 address.
				 */
                    addresses = geocoder.getFromLocation(mLatitude, mLongitude, 1);
                } catch (IOException e1) {
                    e1.printStackTrace();
                    return ("No address");
                } catch (IllegalArgumentException e2) {
                    // Error message to post in the log
                    String errorString = "Illegal arguments "
                            + Double.toString(mLatitude) + " , "
                            + Double.toString(mLongitude)
                            + " passed to address service";
                    e2.printStackTrace();
                    return errorString;
                }
                // If the reverse geocode returned an address
                if (addresses != null && addresses.size() > 0) {
                    // Get the first address
                    Address address = addresses.get(0);
                /*
                 * Format the first line of address (if available), city, and
				 * country name.
				 */
                    String addressText = String.format(
                            "%s", address.getLocality());
// address.getCountryName()
                    //// If there's a street address, add it
                    // address.getMaxAddressLineIndex() > 0 ? address
                    //        .getAddressLine(0) : "",
                    // Locality is usually a city
                    // Return the text
                    return addressText;
                } else {
                    return "No address";
                }
            } else {
                return "Location Not available";
            }

        }


        /**
         * get latitude
         *
         * @return latitude in double
         */
        public double getLatitude() {
            if (mLocation != null) {
                mLatitude = mLocation.getLatitude();
            }
            return mLatitude;
        }

        /**
         * get longitude
         *
         * @return longitude in double
         */
        public double getLongitude() {
            if (mLocation != null) {
                mLongitude = mLocation.getLongitude();
            }
            return mLongitude;
        }

        /**
         * close GPS to save battery
         */
        public void closeGPS() {
            if (mLocationManager != null) {
                mLocationManager.removeUpdates(GPS_Service.this);
            }
        }

        /**
         * show settings to open GPS
         */
        public void askUserToOpenGPS() {
            AlertDialog.Builder mAlertDialog = new AlertDialog.Builder(mContext);

            // Setting Dialog Title
            mAlertDialog.setTitle("Location not available, Open GPS?")
                    .setMessage("Activate GPS to use use location services?")
                    .setPositiveButton("Open Settings", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            mContext.startActivity(intent);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            askUserToOpenGPS();
                        }
                    }).show();
        }

        /**
         * Updating the location when location changes
         */
        @Override
        public void onLocationChanged(Location location) {
            Log.e("onLocationChanged", "onLocationChanged");
            handleNewLocation(location);
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public IBinder onBind(Intent arg0) {
            return null;
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
        }

        @Override
        public void onCreate() {
            super.onCreate();
        }
    }

}
