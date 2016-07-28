package com.nutsuser.ridersdomain.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.utils.ApplicationGlobal;
import com.rollbar.android.Rollbar;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by user on 8/28/2015.
 */
public class HomeScreenTakeATour extends BaseActivity {
    public static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;

    private Activity activity;
    @Bind(R.id.ivLiveDream)
    ImageView ivLiveDream;
    @Bind(R.id.fmlayout)
    FrameLayout fmlayout;
    @Bind(R.id.tvNo)
    Button tvNo;
    @Bind(R.id.tvYes)
    Button tvYes;

    Intent intent;


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    finish();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.take_a_tour_activity_main_screen);
        activity = this;
        ButterKnife.bind(activity);
        Rollbar.init(this, ApplicationGlobal.rollBarId, "production");
        try {

            if (ApplicationGlobal.TAKE_TOUR) {
                fmlayout.setVisibility(View.VISIBLE);
                ApplicationGlobal.TAKE_TOUR = false;
            } else {
                fmlayout.setVisibility(View.GONE);
                ApplicationGlobal.TAKE_TOUR = false;
            }
            tvYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fmlayout.setVisibility(View.GONE);
                }
            });
            tvNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            checkPermission();

        } catch (Exception e) {
            Rollbar.reportException(e, "minor", "HomeScreenTakeATour on create");
        }

    }

    @Override
    protected void onResume() {
        super.onResume();


    }


    /**
     * Implement this method to allow and check GPS permission.
     */

    public void checkPermission() {
        Log.e("checkPermission", "checkPermission");

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(HomeScreenTakeATour.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(HomeScreenTakeATour.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(HomeScreenTakeATour.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(HomeScreenTakeATour.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }


    }


    /**
     * Generate Hash key pragmatically.
     */
    public void printHashKey() {
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));

            }
        } catch (PackageManager.NameNotFoundException e) {
            Rollbar.reportException(e, "minor", "MainScreenActivity on getHashKey");
        } catch (NoSuchAlgorithmException e) {
            Rollbar.reportException(e, "minor", "MainScreenActivity on getHashKey");
        }
    }

    @OnClick({R.id.ivRidingDestination, R.id.ivPlanRide, R.id.ivRidingEvents, R.id.ivModifyBike, R.id.ivHealthyRides, R.id.ivGetDirections, R.id.ivLiveDream, R.id.ivProfile, R.id.ivMenu})
    void click(View view) {
        switch (view.getId()) {
            case R.id.ivRidingDestination:

                intent = new Intent(HomeScreenTakeATour.this, TakeATour.class);
                intent.putExtra("TAKEATOUR", "DESTINATION");
                startActivity(intent);
                break;

            case R.id.ivPlanRide:
                intent = new Intent(HomeScreenTakeATour.this, TakeATour.class);
                intent.putExtra("TAKEATOUR", "PLANARIDE");
                startActivity(intent);

                break;
            case R.id.ivGetDirections:
                intent = new Intent(HomeScreenTakeATour.this, TakeATour.class);
                intent.putExtra("TAKEATOUR", "DIRECTION");
                startActivity(intent);

                break;

            case R.id.ivRidingEvents:
                intent = new Intent(HomeScreenTakeATour.this, TakeATour.class);
                intent.putExtra("TAKEATOUR", "RIDINGEVENT");
                startActivity(intent);

                break;

            case R.id.ivModifyBike:
                intent = new Intent(HomeScreenTakeATour.this, TakeATour.class);
                intent.putExtra("TAKEATOUR", "MODIFY");
                startActivity(intent);

                break;

            case R.id.ivHealthyRides:
                intent = new Intent(HomeScreenTakeATour.this, TakeATour.class);
                intent.putExtra("TAKEATOUR", "HELATHY");
                startActivity(intent);

                break;

            case R.id.ivLiveDream:
                intent = new Intent(HomeScreenTakeATour.this, TakeATour.class);
                intent.putExtra("TAKEATOUR", "LIVE");
                startActivity(intent);

                break;
            case R.id.ivProfile:
                intent = new Intent(HomeScreenTakeATour.this, TakeATour.class);
                intent.putExtra("TAKEATOUR", "PROFILE");
                startActivity(intent);

                break;
            case R.id.ivMenu:
                finish();

                break;


        }
    }

}

