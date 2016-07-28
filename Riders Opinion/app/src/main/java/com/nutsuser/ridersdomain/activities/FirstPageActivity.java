package com.nutsuser.ridersdomain.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.newrelic.agent.android.NewRelic;
import com.nutsuser.ridersdomain.R;
import com.rollbar.android.Rollbar;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.fabric.sdk.android.Fabric;

/**
 * Created by Amit Agnihotri on 8/27/2015.
 */
public class FirstPageActivity extends BaseActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "nQ3iUHEZ8Mh4syPepbJsFkmrr";
    private static final String TWITTER_SECRET = "D2j3TPEjVnXBvyOnGjoK21wbv4He79Nx0O0ye5SQb2fPnssXKE";

    private static final int READ_CONTACTS_PERMISSIONS_REQUEST = 1;
    String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};

    @Bind(R.id.tvTagRiders)
    TextView tvTagRiders;
    @Bind(R.id.tvTagOpinion)
    TextView tvTagOpinion;
    @Bind(R.id.tv_TagRiders)
    TextView tv_TagRiders;
    @Bind(R.id.tv_TagOpinion)
    TextView tv_TagOpinion;
    Map<String, String> params;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if(prefsManager.isLoginDone()){
//            Intent intent = new Intent(FirstPageActivity.this, MainScreenActivity.class);
//            intent.putExtra("MainScreenResponse", "CLOSED");
//            startActivity(intent);
//            finish();
//        }


        try {
            TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
            Fabric.with(this, new Twitter(authConfig));
            NewRelic.withApplicationToken(
                    "AA8eb7c97891052af5975c5066a8433edad995e960"
            ).start(this.getApplication());

            setContentView(R.layout.activity_first_page);
            //getSupportActionBar().hide();
            activity = FirstPageActivity.this;
            ButterKnife.bind(activity);
            setFontsToViews();
            String text = "<font color=#D1622A>Divided</font> <font color=#000000>By Boundaries</font>";
            String text_ = "<font color=#D1622A>United</font> <font color=#000000>By Throttles</font>";
            tv_TagRiders.setText(Html.fromHtml(text));
            tv_TagOpinion.setText(Html.fromHtml(text_));
            // callApplyCompetitionService();
            getPermissionToCamera();
        } catch (Exception e) {
            Rollbar.reportException(e, "minor", "First Page activity Activity on create");
        }
    }

    private void setFontsToViews() {
        tvTagRiders.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT EXTRA LIGHT.TTF"));
        tvTagOpinion.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
    }


    // Called when the user is performing an action which requires the app to read the
    // user's Camera
    @TargetApi(Build.VERSION_CODES.M)
    public void getPermissionToCamera() {
        // 1) Use the support library version ContextCompat.checkSelfPermission(...) to avoid
        // checking the build version since Context.checkSelfPermission(...) is only available
        // in Marshmallow
        // 2) Always check for permission (even if permission has already been granted)
        // since the user can revoke permissions at any time through Settings
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // The permission is NOT already granted.
            // Check if the user has been asked about this permission already and denied
            // it. If so, we want to give more explanation about why the permission is needed.
            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.CAMERA)) {
                // Show our own UI to explain to the user why we need to read the contacts
                // before actually requesting the permission and showing the default UI

            }

            // Fire off an async request to actually get the permission
            // This will show the standard permission request dialog UI

            String[] permissions = new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION};
            requestPermissions(permissions,
                    READ_CONTACTS_PERMISSIONS_REQUEST);
        } else {
            //openCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case READ_CONTACTS_PERMISSIONS_REQUEST: {
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


    @OnClick(R.id.ivStart)
    void click(View view) {
        switch (view.getId()) {
            case R.id.ivStart:
                Intent intent = new Intent(activity, MainScreenActivity.class);
                intent.putExtra("MainScreenResponse", "CLOSED");
                startActivity(intent);
                finish();
                break;
        }
    }


}
