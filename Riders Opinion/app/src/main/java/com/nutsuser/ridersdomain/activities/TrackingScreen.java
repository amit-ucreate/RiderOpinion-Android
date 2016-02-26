package com.nutsuser.ridersdomain.activities;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.location.LocationListener;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.FIFOLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.services.GPSService;
import com.nutsuser.ridersdomain.services.LocationService;
import com.nutsuser.ridersdomain.utils.ApplicationGlobal;
import com.nutsuser.ridersdomain.utils.CustomizeDialog;
import com.nutsuser.ridersdomain.utils.PrefsManager;
import com.nutsuser.ridersdomain.web.api.volley.RequestJsonObject;
import com.nutsuser.ridersdomain.web.pojos.TrackUserList;
import com.nutsuser.ridersdomain.web.pojos.TrackUserListData;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by admin on 24-02-2016.
 */
public class TrackingScreen extends AppCompatActivity {
    Bitmap bitmapCuurent;
    MarkerOptions markerCurrent;
    Marker markerCurrentChanged;
    Toolbar toolbar;
    PrefsManager prefsManager;
    CustomizeDialog mCustomizeDialog;
    String AccessToken, UserId, eventId;
    double start1, end1;
    String star_lat, star_long;
    // Google Map
    GPSService mGPSService;
    GPS_Service gps_service;
    private GoogleMap map;
    private final LatLng HAMBURG = new LatLng(53.558, 9.927);
    private final LatLng HAMBURG_ = new LatLng(53.500, 9.900);
    private final LatLng _HAMBURG_ = new LatLng(53.400, 9.800);

    ArrayList<Bitmap> bitmapArrayList;
    ArrayList<LatLng> latLngArrayList;
    ArrayList<TrackUserListData> data = new ArrayList<>();
    TextView tvStartStop;
    int i = 3;
    int aa = 0;
    ImageLoader imageLoader;
    public DisplayImageOptions options;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trackinglocation);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvStartStop = (TextView) findViewById(R.id.tvStartStop);
        setupActionBar();
        initImageLoader();
        bitmapArrayList = new ArrayList<>();
        latLngArrayList = new ArrayList<>();
        latLngArrayList.clear();
        bitmapArrayList.clear();
        if (!isGooglePlayServicesAvailable()) {
            finish();
        }
        gps_service=new GPS_Service(this);

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

        tvStartStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefsManager = new PrefsManager(TrackingScreen.this);
                prefsManager.setServicesRunning(true);
                startService(new Intent(TrackingScreen.this, LocationService.class));
            }
        });

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
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_back);
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


    class TheTask extends AsyncTask<String, Void, Void> {
        Bitmap bmp;
        String url;
        LatLng lat;
        String mainUrl;

        public TheTask(String mainUrl, LatLng lat) {
            this.mainUrl = mainUrl;
            this.lat = lat;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(String... params) {
            URL url;
            try {
                //imageUri=Uri.parse("https://s3.amazonaws.com/rideropinion/users/139/userImage/1455631200.jpeg");
                // url = new URL("https://s3.amazonaws.com/rideropinion/users/139/userImage/1455631200.jpeg");
                url = new URL("" + mainUrl);
                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);
            Log.e("Before add aa:", "" + aa);
            aa++;
            if (aa < data.size()) {
                if (bmp != null) {
                    Log.e("Pbmp:", "" + bmp + ": aa:" + aa + "data:" + data.size());
                    Bitmap bitmap = GetBitmapClippedCircle(bmp);
                    if (UserId.matches(data.get(aa).getUserId())) {
                        currentMap(bitmap, lat);
                    } else {
                        setUpMap(bitmap, lat);
                    }

                } else {

                    if (data.size() == aa) {
                        Log.e("LAST", "LAST");
                        Log.e("Nbmp:", "" + bmp + ": aa:" + aa + "data:" + data.size());
                        dismissProgressDialog();
                    } else {
                        if (UserId.matches(data.get(aa).getUserId())) {
                            currentMap(null, lat);
                        } else {
                            setUpMap(null, lat);
                        }

                  /*  Log.e("ELSE:", "" + bmp + ": aa:" + aa+"data:"+data.size());
                    String jsonInString = data.get(aa).getImage().toString();
                    jsonInString = jsonInString.replace("\\\"", "\"");
                    jsonInString = jsonInString.replace("\"{", "{");
                    jsonInString = jsonInString.replace("}\"", "}");
                    Log.e("jsonInString: ", "" + jsonInString);
                    new TheTask(jsonInString,latLngArrayList.get(aa)).execute();*/
                        // new TheTask("https://s3.amazonaws.com/rideropinion/users/139/userImage/1455631200.jpeg",HAMBURG_).execute();
                    }
                }
            } else {
                dismissProgressDialog();
                gps_service.getLocation();
            }

        }

    }

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

    public Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        //return _bmp;
        return output;
    }

    private void setUpMap(Bitmap imageUri, LatLng lng) {
        //map.setInfoWindowAdapter(new CustomInfoWindowAdapter());
        View marker = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker_layout, null);
        SimpleDraweeView ivMapImage = (SimpleDraweeView) marker.findViewById(R.id.ivMapImage);
        // imageLoader.displayImage("https://s3.amazonaws.com/rideropinion/users/139/userImage/1455631200.jpeg",ivMapImage);
        if (imageUri == null) {

        } else {
            ivMapImage.setImageBitmap(imageUri);
        }

        // Load image, decode it to Bitmap and return Bitmap to callback
        map.addMarker(new MarkerOptions()
                .position(lng)
                .icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, marker))));
        //markers.put(hamburg.getId(), "http://img.india-forums.com/images/100x100/37525-a-still-image-of-akshay-kumar.jpg");


        // map.moveCamera(CameraUpdateFactory.newLatLngZoom(lng, 5));
        /// map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
        Log.e("aa:", "" + aa);
        if (data.size() == aa) {
            dismissProgressDialog();
        } else {
            if (isNetworkConnected()) {
                String jsonInString = data.get(aa).getImage().toString();
                jsonInString = jsonInString.replace("\\\"", "\"");
                jsonInString = jsonInString.replace("\"{", "{");
                jsonInString = jsonInString.replace("}\"", "}");
                Log.e("setUpMap jsonInString: ", "" + jsonInString);

                new TheTask(jsonInString, latLngArrayList.get(aa)).execute();

            } else {
                dismissProgressDialog();
                showToast("Internet Not Connected");
            }

            // new TheTask("https://s3.amazonaws.com/rideropinion/users/139/userImage/1455631200.jpeg",HAMBURG_).execute();
        }


    }

    private void currentMap(Bitmap imageUri, LatLng lng) {
        bitmapCuurent=imageUri;
        //map.setInfoWindowAdapter(new CustomInfoWindowAdapter());
        View marker = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker_layout, null);
        SimpleDraweeView ivMapImage = (SimpleDraweeView) marker.findViewById(R.id.ivMapImage);
        // imageLoader.displayImage("https://s3.amazonaws.com/rideropinion/users/139/userImage/1455631200.jpeg",ivMapImage);
        if (imageUri == null) {

        } else {
            ivMapImage.setImageBitmap(imageUri);
        }

        markerCurrent = new MarkerOptions()
                .position(lng)
                .icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, marker)));
       // map.addMarker(markerCurrent);
        markerCurrentChanged =  map.addMarker(markerCurrent);
        markerCurrentChanged.setPosition(lng);
       //markerCurrentChanged = map.addMarker(markerCurrent);

        //markers.put(hamburg.getId(), "http://img.india-forums.com/images/100x100/37525-a-still-image-of-akshay-kumar.jpg");


        map.moveCamera(CameraUpdateFactory.newLatLngZoom(lng, 5));
        map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
        Log.e("aa:", "" + aa);
        if (data.size() == aa) {
            dismissProgressDialog();
        } else {
            if (isNetworkConnected()) {
                String jsonInString = data.get(aa).getImage().toString();
                jsonInString = jsonInString.replace("\\\"", "\"");
                jsonInString = jsonInString.replace("\"{", "{");
                jsonInString = jsonInString.replace("}\"", "}");
                Log.e("setUpMap jsonInString: ", "" + jsonInString);

                new TheTask(jsonInString, latLngArrayList.get(aa)).execute();

            } else {
                dismissProgressDialog();
                showToast("Internet Not Connected");
            }

            // new TheTask("https://s3.amazonaws.com/rideropinion/users/139/userImage/1455631200.jpeg",HAMBURG_).execute();
        }


    }
    private void currentUpdateMap(Bitmap imageUri, LatLng lng) {

        //map.setInfoWindowAdapter(new CustomInfoWindowAdapter());
        View marker = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker_layout, null);
        SimpleDraweeView ivMapImage = (SimpleDraweeView) marker.findViewById(R.id.ivMapImage);
        // imageLoader.displayImage("https://s3.amazonaws.com/rideropinion/users/139/userImage/1455631200.jpeg",ivMapImage);
        if (imageUri == null) {

        } else {
            ivMapImage.setImageBitmap(imageUri);
        }

        markerCurrent = new MarkerOptions()
                .position(lng)
                .icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, marker)));

        markerCurrentChanged =  map.addMarker(markerCurrent);
        markerCurrentChanged.setPosition(lng);


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
     * Register info .
     */
    public void RidingListmodelinfo() {
        showProgressDialog();
        Log.e("riding destination", "riding destination");
        try {
            prefsManager = new PrefsManager(TrackingScreen.this);
            AccessToken = prefsManager.getToken();
            UserId = prefsManager.getCaseId();
            String radius = prefsManager.getRadius();
            //http://ridersopininon.herokuapp.com/index.php/ridingDestination?userId=75&longitude=0.000000&latitude=0.000000&accessToken=eddfbf2bf4046e90fc768d8e319a4355
            Log.e("URL: ", "" + ApplicationGlobal.ROOT + ApplicationGlobal.baseurl_trackuserlist + "userId=" + UserId + "&eventId=" + eventId + "&lat=" + star_lat + "&long=" + star_long + "&accessToken=" + AccessToken);
            RequestQueue requestQueue = Volley.newRequestQueue(TrackingScreen.this);
            RequestJsonObject loginTaskRequest = new RequestJsonObject(Request.Method.POST,
                    ApplicationGlobal.ROOT + ApplicationGlobal.baseurl_trackuserlist + "userId=" + UserId + "&eventId=" + eventId + "&lat=" + star_lat + "&long=" + star_long + "&accessToken=" + AccessToken, null,
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

                Type type = new TypeToken<TrackUserList>() {
                }.getType();
                TrackUserList trackUserList = new Gson().fromJson(response.toString(), type);

                data.clear();


                if (trackUserList.getSuccess().equals("1")) {
                    dismissProgressDialog();
                    data.addAll(trackUserList.getData());

                    for (int i = 0; i < data.size(); i++) {
                        String lat, lon;
                        lat = data.get(i).getLatitude();
                        lon = data.get(i).getLongitude();
                        double lat_d = Double.parseDouble(lat);
                        double lon_d = Double.parseDouble(lon);
                        LatLng latLng = new LatLng(lat_d, lon_d);
                        latLngArrayList.add(latLng);
                      /*  String jsonInString = data.get(i).getImage().toString();
                        jsonInString = jsonInString.replace("\\\"", "\"");
                        jsonInString = jsonInString.replace("\"{", "{");
                        jsonInString = jsonInString.replace("}\"", "}");
                        Log.e("jsonInString: ", "" + jsonInString);*/

                    }
                    if (latLngArrayList.size() != 0) {
                        if (isNetworkConnected()) {
                            showProgressDialog();
                            String jsonInString = data.get(aa).getImage().toString();
                            jsonInString = jsonInString.replace("\\\"", "\"");
                            jsonInString = jsonInString.replace("\"{", "{");
                            jsonInString = jsonInString.replace("}\"", "}");
                            Log.e("jsonInString: ", "" + jsonInString);
                            new TheTask(jsonInString, latLngArrayList.get(aa)).execute();
                        } else {
                            showToast("Internet Not Connected");
                        }


                    }

                } else {
                    dismissProgressDialog();
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

    private void updateCurrentMarker(LatLng mLatLng) {
//		if (!isFirstMessage) {
//			isFirstMessage = false;
//			mMarker.remove();
//		}
       // markerCurrent.position(mLatLng);
        if(markerCurrentChanged != null){

            markerCurrentChanged.remove();
            currentUpdateMap(bitmapCuurent,mLatLng);
            showToast("mLatLng: " + mLatLng);
}
       // markerCurrentChanged.setPosition(mLatLng);

        //animateMarker(markerCurrentChanged,mLatLng,true);

        //map.addMarker(markerCurrent.position(mLatLng));
    }
    public void animateMarker(final Marker marker, final LatLng toPosition,
                              final boolean hideMarker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = map.getProjection();
        Point startPoint = proj.toScreenLocation(marker.getPosition());
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 500;

        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * toPosition.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * toPosition.latitude + (1 - t)
                        * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));

                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else {
                    if (hideMarker) {
                        marker.setVisible(false);
                    } else {
                        marker.setVisible(true);
                    }
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        gps_service.closeGPS();
    }

    // track loacation
    public class GPS_Service implements android.location.LocationListener {

        // Minimum time fluctuation for next update (in milliseconds)
        private static final long TIME = 1000;
        // Minimum distance fluctuation for next update (in meters)
        private static final long DISTANCE = 5;
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
                    return ("IO Exception trying to get address:" + e1);
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
                            "%s, %s, %s",
                            // If there's a street address, add it
                            address.getMaxAddressLineIndex() > 0 ? address
                                    .getAddressLine(0) : "",
                            // Locality is usually a city
                            address.getLocality(),
                            // The country of the address
                            address.getCountryName());
                    // Return the text
                    return addressText;
                } else {
                    return "No address found by the service: Note to the developers, If no address is found by google itself, there is nothing you can do about it.";
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
                if (ActivityCompat.checkSelfPermission(TrackingScreen.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(TrackingScreen.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
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
                    }
                }).show();
    }

    /**
     * Updating the location when location changes
     */
    @Override
    public void onLocationChanged(Location location) {

        mLatitude = location.getLatitude();
        mLongitude = location.getLongitude();
        LatLng lat=new LatLng(mLatitude,mLongitude);
        updateCurrentMarker(lat);
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


}

}
