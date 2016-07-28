package com.nutsuser.ridersdomain.activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Rect;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.route.GMapV2GetRouteDirection;
import com.nutsuser.ridersdomain.services.GPSService;
import com.nutsuser.ridersdomain.utils.PrefsManager;
import com.rollbar.android.Rollbar;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Document;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by user on 9/11/2015.
 */
public class MapActivity extends BaseActivity {
    Bitmap bitmap;
    String image;
    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;
    public LocationManager locationManager;
    public Criteria criteria;
    public String bestProvider;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation;
    // Google Map
    GoogleMap googleMap;
    GMapV2GetRouteDirection v2GetRouteDirection;
    Document document;
    LatLng mString_end;
    LatLng mString_start,mString_current;
    double start, end;
    double start1, end1;
    public PrefsManager prefsManager;
    String imageUrl;
    File file;
    private String destName;

    @Bind(R.id.btMapNAv)
    Button btMapNAv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        try {
            prefsManager = new PrefsManager(this);
            v2GetRouteDirection = new GMapV2GetRouteDirection();
            ButterKnife.bind(this);
            overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getMap();
            prefsManager = new PrefsManager(this);
            imageUrl = prefsManager.getImageUrl();
            Log.e("imageUrl:", "" + imageUrl);
            file = new File(imageUrl);
            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            } else {
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
            }
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            googleMap.getUiSettings().setCompassEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            googleMap.getUiSettings().setAllGesturesEnabled(true);
            googleMap.setTrafficEnabled(true);


            Intent intent = getIntent();
            String endlat = intent.getStringExtra("endLat");
            String endlon = intent.getStringExtra("endLon");
            String deslon = intent.getStringExtra("deslon");
            String deslat = intent.getStringExtra("deslat");
            destName = intent.getExtras().getString("destName");
            try {
                Log.e("destName", destName);
            } catch (NullPointerException e) {
                Log.e("destName", "no name");
            }

            start = Double.valueOf(endlat.trim()).doubleValue();
            end = Double.valueOf(endlon.trim()).doubleValue();
            mString_start = new LatLng(start, end);
            start1 = Double.valueOf(deslat.trim()).doubleValue();
            end1 = Double.valueOf(deslon.trim()).doubleValue();
            mString_end = new LatLng(start1, end1);


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
                mString_current = new LatLng(start_, end_);
                Log.e("mString_current:", "" + mString_current);
                GetRouteTask getRoute = new GetRouteTask();
                getRoute.execute();

            }
            // make sure you close the gps after using it. Save user's battery power
            mGPSService.closeGPS();
        }catch(Exception e){
            Rollbar.reportException(e, "minor", "MapActivity on create");
        }


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }


    @OnClick({R.id.ivBack,R.id.btMapNAv})
    void click(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                onBackPressed();
                break;
            case R.id.btMapNAv:
                navigateToGoogleMap(destName);
                break;
        }
    }


    /**
     * @author Amit Agnihotri
     *         This class Get Route on the map
     */
    public class GetRouteTask extends AsyncTask<String, Void, String> {

        String response = "";
        private ProgressDialog Dialog = new ProgressDialog(MapActivity.this);

        @Override
        protected void onPreExecute() {
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
            googleMap.clear();
            if (response.equalsIgnoreCase("Success")) {
                ArrayList<LatLng> directionPoint = v2GetRouteDirection.getDirection(document);
                PolylineOptions rectLine = new PolylineOptions().width(16).color(
                        Color.parseColor("#D1622A"));

                for (int i = 0; i < directionPoint.size(); i++) {
                    rectLine.add(directionPoint.get(i));
                }
                LatLng latLng = new LatLng(start, end);

                // Adding route on the map
                googleMap.addPolyline(rectLine);
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 8);
                googleMap.animateCamera(cameraUpdate);
                for (int i = 1; i < 4; i++) {
                    if (i == 1) {
                        addMarker(mString_start, 1);
                    } else if (i == 2) {
                        addMarker(mString_end, 2);
                    }
                    else if(i==3){
                        addMarker(mString_current, 3);
                    }
                }

            }

            Dialog.dismiss();
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

    public void addMarker(LatLng latLng, int pos) {
        MarkerOptions marker = null;
        View view = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker_layout, null);
        FrameLayout fmlayout = (FrameLayout) view.findViewById(R.id.fmlayout);
        ImageView ivMapImage = (ImageView) view.findViewById(R.id.ivMapImage);
        if (pos == 1) {
            fmlayout.setBackgroundResource(R.drawable.ic_endlocation);
        }
        else if(pos==2){
            fmlayout.setBackgroundResource(R.drawable.ic_endlocation);
        }
        else {
            fmlayout.setBackgroundResource(R.drawable.ic_your_locationstart);
            if (TextUtils.isEmpty(imageUrl)) {
                ivMapImage.setImageResource(R.drawable.app_icon);
            } else {
//                Bitmap bmp = BitmapFactory.decodeFile(file.getAbsolutePath());
//                Log.e("bmp:",""+bmp);
//                Bitmap bitmap=getRoundedShape(bmp);
//                Log.e("bitmap:",""+bitmap);
                //  ivMapImage.setImageBitmap(getBitmapFromURL(prefsManager.getImageUrl()));
                Picasso.with(MapActivity.this).load(prefsManager.getImageUrl()).transform(new CircleTransform()).into(ivMapImage);
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
        googleMap.addMarker(marker);
        if (pos == 2) {

        } else {
            CameraPosition cameraPosition = new CameraPosition.Builder().target(
                    latLng).zoom(7).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            googleMap.getUiSettings().setZoomControlsEnabled(true);
        }


        // Toast.makeText(MapActivity.this, "Matched: "+ridername.get(position) +"---Id--" +UserId, Toast.LENGTH_LONG).show();

        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


        // check if map is created successfully or not
        if (googleMap == null) {
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
            Rollbar.reportException(e, "minor", "MapActivity getBitmapFromURL method");
            return null;
        }
    }

    /* private class MyTask extends AsyncTask<String, Void, Bitmap> {

         @Override
         protected void onPreExecute() {
         }

         @Override
         protected Bitmap doInBackground(String... params) {


             InputStream in = null;
             try {
                 in = new FlushedInputStream((InputStream) new URL(image).getContent());
                 //here we put input stream in the arrylist

             } catch (IOException e) {
                 e.printStackTrace();
             }
             bitmap = BitmapFactory.decodeStream(in);
             bitmap = getRoundedShape(bitmap);

             return bitmap;

         }


         @Override
         protected void onPostExecute(Bitmap result) {

             super.onPostExecute(result);


         }

     }*/
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







