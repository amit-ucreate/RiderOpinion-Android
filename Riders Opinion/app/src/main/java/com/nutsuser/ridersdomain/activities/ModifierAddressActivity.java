package com.nutsuser.ridersdomain.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.vision.barcode.Barcode;
import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.async.AsyncImageView;
import com.nutsuser.ridersdomain.route.GMapV2GetRouteDirection;
import com.nutsuser.ridersdomain.services.GPSService;
import com.nutsuser.ridersdomain.utils.PrefsManager;
import com.rollbar.android.Rollbar;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by user on 10/5/2015.
 */
public class ModifierAddressActivity extends BaseActivity {

    @Bind(R.id.ivcross)
    ImageView ivcross;
    @Bind(R.id.tvLabelAddress)
    TextView tvLabelAddress;
    @Bind(R.id.tvModifierAddress)
    TextView tvModifierAddress;
    @Bind(R.id.tvLabelPhone)
    TextView tvLabelPhone;
    @Bind(R.id.tvPhone)
    TextView tvPhone;
    @Bind(R.id.tvLabelTiming)
    TextView tvLabelTiming;
    @Bind(R.id.tvTiming)
    TextView tvTiming;
    @Bind(R.id.tvModifierName)
    TextView tvModifierName;
    @Bind(R.id.tvReviews)
    TextView tvReviews;
    private Activity activity;
    @Bind(R.id.ivVideo)
    ImageView ivVideo;
    @Bind(R.id.ivFavorite)
    ImageView ivFavorite;
    /* @Bind(R.id.toolbar)
     Toolbar toolbar;
     @Bind(R.id.tvTitleToolbar)
     TextView tvTitleToolbar;*/
    // Google Map
    private GoogleMap googleMap;
    private String company,address,phone,startTime,endTime,videoUrl;
    private int isFav;
    LatLng mString_start;
    LatLng mString_end;
    private ProgressDialog Dialog;
    Document document;
    GMapV2GetRouteDirection v2GetRouteDirection;
    double start, end;
    PrefsManager prefsManager;
    String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //settheme();
        setContentView(R.layout.activity_modifier_address);
        prefsManager= new PrefsManager(ModifierAddressActivity.this);
        // getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.black_transparent)));
        try {
            activity = this;
            ButterKnife.bind(this);
            v2GetRouteDirection = new GMapV2GetRouteDirection();
            imageUrl = prefsManager.getImageUrl();
            company= getIntent().getExtras().getString(KEY_COMPANY);
            address= getIntent().getExtras().getString(KEY_ADDRESS);
            phone= getIntent().getExtras().getString(KEY_PHONE);
            startTime= getIntent().getExtras().getString(KEY_START_DATE);
            endTime= getIntent().getExtras().getString(KEY_END_DATE);
            videoUrl = getIntent().getExtras().getString(KEY_VIDEO_URL);
            isFav = getIntent().getExtras().getInt(KEY_IS_FAV);
            if(isFav==1){
                ivFavorite.setImageResource(R.drawable.icon_star_full);
            }else{
                ivFavorite.setImageResource(R.drawable.icon_star_empty);
            }

            tvModifierName.setText(company);
            tvModifierAddress.setText(address);
            tvPhone.setText(phone);
            tvTiming.setText(startTime+" - "+endTime);
            //overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
            // setupActionBar();
            setFonts();
            tvReviews.setText("");

            //===================================//
            GPSService mGPSService = new GPSService(this);
            mGPSService.getLocation();

            if (mGPSService.isLocationAvailable == false) {

                // Here you can ask the user to try again, using return; for that
                // Toast.makeText(getApplicationContext(), "Your location is not available, please try again.", Toast.LENGTH_SHORT).show();
                return;

                // Or you can continue without getting the location, remove the return; above and uncomment the line given below
                // address = "Location not available";
            } else {

                Log.e("location call", "Loaction call");
                // Getting location co-ordinates
                double latitude = mGPSService.getLatitude();
                double longitude = mGPSService.getLongitude();
                mString_start = new LatLng(latitude,longitude);

            }

           Address location = getLocationFromAddress(address);

            if(location==null){
                showRideTypeDialog();
            }else {
                Log.e("lat", "" + location.getLatitude());
                Log.e("lng", "" + location.getLongitude());
                mString_end = new LatLng(location.getLatitude(), location.getLongitude());

                GetRouteTask getRoute = new GetRouteTask();
                getRoute.execute();
            }
        }catch(Exception e){
            Log.e("exception"+e.getClass(),e.getMessage());
          Rollbar.reportException(e, "minor", "ModifierAddressActivity on create");
        }
    }

    private void showRideTypeDialog(){

        new AlertDialog.Builder(ModifierAddressActivity.this).setTitle("Alert!!").setCancelable(false)
                .setMessage("No Route found for address.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();


                    }
                }).show();
    }
    @Override
    protected void onResume() {
        super.onResume();
        try {
            // Loading map
            initializeMap();

        } catch (Exception e) {
            e.printStackTrace();
            Rollbar.reportException(e, "minor", "ModifierAddressActivity on Resume");
        }
    }

    private void setFonts() {
        Typeface typefaceNormal = Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF");
        //   tvTitleToolbar.setTypeface(typefaceNormal);
        tvReviews.setTypeface(typefaceNormal);
        tvLabelAddress.setTypeface(typefaceNormal);
        tvModifierName.setTypeface(typefaceNormal);
        tvModifierAddress.setTypeface(typefaceNormal);
        tvLabelPhone.setTypeface(typefaceNormal);
        tvPhone.setTypeface(typefaceNormal);
        tvLabelTiming.setTypeface(typefaceNormal);
        tvTiming.setTypeface(typefaceNormal);
    }

    private void initializeMap() {
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getMap();

            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            } else
            try {
                googleMap.setMyLocationEnabled(true);
            }catch(SecurityException s){
                Rollbar.reportException(s, "minor", "ModifierAddressActivity initialize map");
            }
        }
    }

    private void setupActionBar() {
        //  setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_back);
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
                Intent intent = new Intent(ModifierAddressActivity.this, MainScreenActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("MainScreenResponse", "OPEN");
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.ivcross,R.id.ivVideo})
    void click(View view) {
        switch (view.getId()) {
            case R.id.ivcross:
                finish();
                //startActivity(new Intent(activity, ModifierAddressActivity.class));
                break;

            case R.id.ivVideo:
                Log.e("VIDEO URL: ", "" + videoUrl);
                if(videoUrl.isEmpty()||videoUrl==null){
                    ivVideo.setClickable(false);
                    ivVideo.setClickable(false);
                    ivVideo.setFocusable(false);
                    ivVideo.setEnabled(false);
                    Toast.makeText(context,"Video not available",Toast.LENGTH_SHORT).show();
                }else {
                    Intent Intent = new Intent(activity, YouTubeVideoPlay.class);
                    Intent.putExtra("VIDEOURL", videoUrl);
                    startActivity(Intent);
                }
                break;

        }
    }


    //================ getAddress================//
    public Address getLocationFromAddress(String strAddress){

        Geocoder coder = new Geocoder(this);
        List<Address> address;
//       GeoPoint p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

//            p1 = new GeoPoint((int) (location.getLatitude() * 1E6),
//                    (int) (location.getLongitude() * 1E6));

            return location;
        }catch (Exception e){
            return null;
    }
    }


    private class GetRouteTask extends AsyncTask<String, Void, String> {

        String response = "";

        @Override
        protected void onPreExecute() {
            Dialog = new ProgressDialog(ModifierAddressActivity.this);
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
                PolylineOptions rectLine = new PolylineOptions().width(12).color(
                        Color.parseColor("#D1622A"));

                for (int i = 0; i < directionPoint.size(); i++) {
                    rectLine.add(directionPoint.get(i));
                }
                LatLng latLng = new LatLng(start, end);

                // Adding route on the map
                googleMap.addPolyline(rectLine);
                CameraUpdate cameraUpdate=null;
                if( CalculationByDistance(mString_start,mString_end)<=50) {
                    cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 9);
                }else if(CalculationByDistance(mString_start,mString_end)<=100&&CalculationByDistance(mString_start,mString_end)>50){
                    cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 7);
                }else{
                    cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 6);
                }

                googleMap.animateCamera(cameraUpdate);
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
        googleMap.addMarker(marker);
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
                        latLng).zoom(6).build();
            }

            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            googleMap.getUiSettings().setZoomControlsEnabled(true);
        } else {

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


}
