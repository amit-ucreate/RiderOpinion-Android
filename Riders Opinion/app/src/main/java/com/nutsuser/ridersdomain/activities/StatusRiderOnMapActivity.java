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
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.route.GMapV2GetRouteDirection;
import com.nutsuser.ridersdomain.utils.CustomizeDialog;
import com.nutsuser.ridersdomain.utils.PrefsManager;
import com.nutsuser.ridersdomain.view.FlushedInputStream;
import com.nutsuser.ridersdomain.web.api.FileUploadService;
import com.nutsuser.ridersdomain.web.api.ServiceGenerator;
import com.nutsuser.ridersdomain.web.pojos.StatusRider;
import com.nutsuser.ridersdomain.web.pojos.StatusRiderDatum;
import com.rollbar.android.Rollbar;

import org.w3c.dom.Document;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;

/**
 * Created by user on 9/11/2015.
 */
public class StatusRiderOnMapActivity extends BaseActivity {
    ArrayList<StatusRiderDatum>statusRiderDatums=new ArrayList<>();
    String UserId;
    PrefsManager prefsManager;
    String AccessToken, eventId;
    CustomizeDialog mCustomizeDialog;
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


    ArrayList<String> ids;
    ArrayList<LatLng> latLngArrayList;
    String image;
    Bitmap bitmap;
    ArrayList<Bitmap> url1;
    int valueMarker = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        try {
            v2GetRouteDirection = new GMapV2GetRouteDirection();
            ButterKnife.bind(this);
            ids = new ArrayList<>();
            latLngArrayList = new ArrayList<>();
            url1 = new ArrayList<>();
            prefsManager = new PrefsManager(this);
            UserId = prefsManager.getCaseId();
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
            eventId = intent.getStringExtra("event_Id");
            String endlat = intent.getStringExtra("latstatus");
            String endlon = intent.getStringExtra("lonstatus");
            String deslon = intent.getStringExtra("deslonstatus");
            String deslat = intent.getStringExtra("deslatstatus");

            start = Double.valueOf(endlat.trim()).doubleValue();
            end = Double.valueOf(endlon.trim()).doubleValue();
            mString_start = new LatLng(start, end);
            start1 = Double.valueOf(deslat.trim()).doubleValue();
            end1 = Double.valueOf(deslon.trim()).doubleValue();
            mString_end = new LatLng(start1, end1);

            GetRouteTask getRoute = new GetRouteTask();
            getRoute.execute();
        }catch(Exception e){
            Rollbar.reportException(e, "minor", "StatusRiderOnMapActivity on create ");
        }


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

        String response = "";
        private ProgressDialog Dialog = new ProgressDialog(StatusRiderOnMapActivity.this);

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
                Log.e("distance:",""+ CalculationByDistance(mString_start,mString_end));

                // Adding route on the map
                googleMap.addPolyline(rectLine);
                CameraUpdate cameraUpdate=null;
                if( CalculationByDistance(mString_start,mString_end)<=50) {
                     cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 10);
                }else if(CalculationByDistance(mString_start,mString_end)<=100&&CalculationByDistance(mString_start,mString_end)>50){
                     cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 8);
                }else{
                    cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 7);
                }
                googleMap.animateCamera(cameraUpdate);
               /* for (int i = 1; i < 3; i++) {
                    if (i == 1) {
                        addMarker(mString_start, 1);
                    } else if (i == 2) {
                        addMarker(mString_end, 2);
                    }
                }*/

                if(isNetworkConnected()) {
                    MyRidingDetails(eventId);
                }else{
                    showToast("Internet is not connected.");
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
        ivMapImage.setImageBitmap(url1.get(pos));
        if (UserId.equals(ids.get(pos))) {
            fmlayout.setBackgroundResource(R.drawable.ic_your_locationstart);
           // ivMapImage.setImageBitmap(url1.get(pos));
           // ivMapImage.setImageResource(R.drawable.app_icon);
        } else {
           // ivMapImage.setImageBitmap(url1.get(pos));
            //fmlayout.setBackgroundResource(R.drawable.ic_endlocation);
        }


        //ivMapImage.setImageBitmap(url1.get(position));
        // create marker
        marker = new MarkerOptions().position(latLng).title(statusRiderDatums.get(pos).getUsername());
        // Changing marker icon
        marker.getPosition();
        marker.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, view)));
        // adding marker
        //marker.snippet(lat.get(position));
        googleMap.addMarker(marker);
        if (UserId.equals(ids.get(pos))) {
            CameraPosition cameraPosition = new CameraPosition.Builder().target(
                    latLng).zoom(7).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            googleMap.getUiSettings().setZoomControlsEnabled(true);
        }


        // Toast.makeText(MapActivity.this, "Matched: "+ridername.get(position) +"---Id--" +UserId, Toast.LENGTH_LONG).show();

        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        valueMarker++;
        pos++;
        Log.e("valueMarker:", "" + valueMarker);
        Log.e("pos:", "" + pos);


        if (latLngArrayList.size() == valueMarker) {

        } else {
            image = statusRiderDatums.get(valueMarker).getImage();
            new MyTask().execute();
        }

        // check if map is created successfully or not
        if (googleMap == null) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                    .show();

        }


        //=======InfoWindow click listener==========//
        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                String[] separated = marker.getTitle().split(",");
                Log.e("id",""+  marker.getId());

                int position=Integer.parseInt(marker.getId().replace("m","")); // this will contain Marker Position
                Log.e("ID:", "IDDDDDD===   " + ids.get(position));

                Intent intent = new Intent(StatusRiderOnMapActivity.this, PublicProfileScreen.class);
                intent.putExtra(PROFILE, true);
                intent.putExtra(PROFILE_IMAGE, url1.get(position));
                intent.putExtra(USER_ID, ids.get(position));
                startActivity(intent);
            }
        });

        //=======custom InfoWindow adapter of marker==========//

        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            private final View window = getLayoutInflater().inflate(
                    R.layout.custom_info_window_nearby_friends, null);

            @Override
            public View getInfoWindow(Marker marker) {
                String title = marker.getTitle();
                TextView txtTitle = ((TextView) window
                        .findViewById(R.id.title));
                TextView txtPlace = ((TextView) window
                        .findViewById(R.id.snippetPlace));
                TextView txtVehicle = ((TextView) window

                        .findViewById(R.id.snippetVehicle));
                TextView txtVehicleType = ((TextView) window
                        .findViewById(R.id.snippetVehicleType));
                if(TextUtils.isEmpty(title)){
                    txtTitle.setText("");
                }else{
                    txtTitle.setText(title);
                }

                txtPlace.setText(statusRiderDatums.get(Integer.parseInt(marker.getId().replace("m",""))).getBaseLocation());
                txtVehicle.setText( statusRiderDatums.get(Integer.parseInt(marker.getId().replace("m",""))).getVehicleName());
                txtVehicleType.setText( statusRiderDatums.get(Integer.parseInt(marker.getId().replace("m",""))).getVehicleType());
                Log.e("snippet 1",""+marker.getSnippet());
                return window;
            }

            @Override
            public View getInfoContents(Marker marker) {
                return null;
            }
        });



    }

    /**
     * Match MyRidingDetails .
     */
    public void MyRidingDetails(String eventId) {

        showProgressDialog();
        Log.e(" MyRidingDetails", " MyRidingDetails");
        try {
            prefsManager = new PrefsManager(StatusRiderOnMapActivity.this);
            AccessToken = prefsManager.getToken();


            FileUploadService service = ServiceGenerator.createService(FileUploadService.BASE_URL);
            service.getUserOfEvents(eventId, AccessToken, new Callback<JsonObject>() {
                @Override
                public void success(JsonObject jsonObject, retrofit.client.Response response) {
                    Log.e("Upload", "success");
                    Log.e("jsonObject:", "" + jsonObject.toString());

                    Type type = new TypeToken<StatusRider>() {
                    }.getType();
                    statusRiderDatums.clear();
                    StatusRider statusRider = new Gson().fromJson(jsonObject.toString(), type);
                    if (statusRider.getSuccess() == 1) {
                        dismissProgressDialog();

                        Log.e("riderlist.getData()",""+statusRider.getData());
                        statusRiderDatums.addAll(statusRider.getData());
                        for (int i=0;i<statusRiderDatums.size();i++){
                            ids.add(statusRiderDatums.get(i).getUserId());

                            double start = Double.valueOf(statusRiderDatums.get(i).getLatitude().toString().trim()).doubleValue();
                            double end = Double.valueOf(statusRiderDatums.get(i).getLongitude().toString().trim()).doubleValue();
                            LatLng latlngstart = new LatLng(start, end);
                            latLngArrayList.add(latlngstart);

                        }
                        if(statusRiderDatums.size()==0){

                        }
                        else {
                            image=statusRiderDatums.get(valueMarker).getImage();
                            new MyTask().execute();
                           // addMarker(latLngArrayList.get(0),0);
                        }

                    } else if (statusRider.getMessage().equals("Data Not Found.")) {
                        dismissProgressDialog();

                        // showToast("Data Not Found.");
                    } else {
                        dismissProgressDialog();

                    }


                }

                @Override
                public void failure(RetrofitError error) {
                    dismissProgressDialog();
                    //showToast("Error:" + error);
                    Log.e("Upload", "error" + error);
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
            Rollbar.reportException(e, "minor", "StatusRiderOnMapActivity Riding detail API ");

        }
    }



    private class MyTask extends AsyncTask<String, Void, Bitmap> {

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
            url1.add(bitmap);
            return bitmap;

        }


        @Override
        protected void onPostExecute(Bitmap result) {

            super.onPostExecute(result);
            addMarker(latLngArrayList.get(valueMarker), valueMarker);

        }

    }
    ///this function is for get rounded bitmap
    public Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
        int targetWidth = 120;
        int targetHeight = 120;
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



}
