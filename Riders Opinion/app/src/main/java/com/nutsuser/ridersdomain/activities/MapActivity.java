package com.nutsuser.ridersdomain.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.location.LocationManager;


import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.location.LocationRequest;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.route.GMapV2GetRouteDirection;
import com.nutsuser.ridersdomain.services.GPSService;

import org.w3c.dom.Document;


import java.util.ArrayList;


import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by user on 9/11/2015.
 */
public class MapActivity extends BaseActivity {

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
    LatLng mString_start;
    double start, end;
    double start1, end1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        v2GetRouteDirection = new GMapV2GetRouteDirection();
        ButterKnife.bind(this);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
        googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                R.id.map)).getMap();

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
        start = Double.valueOf(endlat.trim()).doubleValue();
        end = Double.valueOf(endlon.trim()).doubleValue();
        mString_end = new LatLng(start, end);
        String address = "";
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
            mString_start = new LatLng(start1, end1);
            Log.e("start:", "" + mString_start);
            GetRouteTask getRoute = new GetRouteTask();
            getRoute.execute();
        }


        // make sure you close the gps after using it. Save user's battery power
        mGPSService.closeGPS();


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }


    @OnClick({R.id.ivBack})
    void click(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                onBackPressed();
                break;
        }
    }


    /**
     * @author Amit Agnihotri
     *         This class Get Route on the map
     */
    public class GetRouteTask extends AsyncTask<String, Void, String> {

        private ProgressDialog Dialog = new ProgressDialog(MapActivity.this);
        String response = "";

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
                PolylineOptions rectLine = new PolylineOptions().width(10).color(
                        Color.parseColor("#D1622A"));

                for (int i = 0; i < directionPoint.size(); i++) {
                    rectLine.add(directionPoint.get(i));
                }
                LatLng latLng = new LatLng(start, end);

                // Adding route on the map
                googleMap.addPolyline(rectLine);
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 9);
                googleMap.animateCamera(cameraUpdate);


            }

            Dialog.dismiss();
        }
    }
}







