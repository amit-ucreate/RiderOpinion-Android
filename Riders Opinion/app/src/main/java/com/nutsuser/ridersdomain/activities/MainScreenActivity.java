package com.nutsuser.ridersdomain.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.inscripts.cometchat.sdk.CometChat;
import com.inscripts.interfaces.Callbacks;
import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.services.GPS_Services_Setting;
import com.nutsuser.ridersdomain.utils.ApplicationGlobal;
import com.nutsuser.ridersdomain.view.CustomDialog;
import com.nutsuser.ridersdomain.web.api.FileUploadService;
import com.nutsuser.ridersdomain.web.api.ServiceGenerator;
import com.nutsuser.ridersdomain.web.pojos.EventCountData;
import com.nutsuser.ridersdomain.web.pojos.MainScreenData;
import com.nutsuser.ridersdomain.web.pojos.MainScreenResponse;
import com.rollbar.android.Rollbar;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.RetrofitError;

/**
 * Created by user on 8/28/2015.
 */
public class MainScreenActivity extends BaseActivity {
    public static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    @Bind(R.id.tv_TagRiders)
    TextView tv_TagRiders;
    @Bind(R.id.tv_TagOpinion)
    TextView tv_TagOpinion;
    String mStringOpenClosed;
    private Activity activity;
    @Bind(R.id.ivLiveDream)
    ImageView ivLiveDream;
    @Bind(R.id.ivProfile)
    ImageView ivProfile;
    @Bind(R.id.tvEventCount)
    com.nutsuser.ridersdomain.view.CircleButtonText tvEventCount;
    @Bind(R.id.tvProfileCount)
    com.nutsuser.ridersdomain.view.CircleButtonText tvProfileCount;
    private MainScreenData mainScreenData;
    private EventCountData eventCountData;
    @Bind(R.id.ivTakeaTour)
    ImageView ivTakeaTour;

    // R.id.ivRidingDestination, R.id.ivPlanRide, R.id.ivRidingEvents, R.id.ivModifyBike, R.id.ivHealthyRides, R.id.ivGetDirections, R.id.ivLiveDream,R.id.ivProfile

    // TextView OpinionSee;
    //String test;
    private CometChat cometChat;


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
        setContentView(R.layout.activity_main_screen);
        activity = this;
        ButterKnife.bind(activity);
        Rollbar.init(this, ApplicationGlobal.rollBarId, "production");
        try {

//        try {
//            OpinionSee.setText(test);
//        } catch (Exception e) {
//            Rollbar.reportException(e, "minor", "MainScreenActivity on create");
//        }


            Log.e("API_KEY", API_KEY);
            Log.e("SITE_URL_COMETCHAT", SITE_URL_COMETCHAT);

            cometChat = CometChat.getInstance(MainScreenActivity.this, API_KEY);


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!prefsManager.isLoginDone()) {
                        Intent intent = getIntent();
                        mStringOpenClosed = intent.getStringExtra("MainScreenResponse");
                        if (mStringOpenClosed.matches("CLOSED")) {
                            startActivityForResult(new Intent(activity, RegisterActivity.class), 1);
                        }

                    } else {
                        cometChatLogin();
                    }


                }
            }, 50);

            String text = "<font color=#D1622A>Divided</font> <font color=#000000>By Boundaries</font>";
            String text_ = "<font color=#D1622A>United</font> <font color=#000000>By Throttles</font>";
            tv_TagRiders.setText(Html.fromHtml(text));
            tv_TagOpinion.setText(Html.fromHtml(text_));
            tv_TagRiders.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/LATO-REGULAR.TTF"));
            tv_TagOpinion.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/LATO-REGULAR.TTF"));
            tvEventCount.setVisibility(View.GONE);
            // printHashKey();
            checkPermission();
//        GPS_Services_Setting gps_services_setting=new GPS_Services_Setting(this);
//        gps_services_setting.getLocation();
//        gps_services_setting.closeGPS();

            prefsManager.setEventType(getIntent().getStringExtra("typeEmailPhone"));
        } catch (Exception e) {
            Rollbar.reportException(e, "minor", "MainScreenActivity on create");
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (prefsManager.isLoginDone()) {
            GPS_Services_Setting gps_services_setting = new GPS_Services_Setting(this);
            gps_services_setting.getLocation();
            gps_services_setting.closeGPS();
            //badgeCountInfo(prefsManager.getCaseId(), prefsManager.getToken());
        }
    }

    //========= function to Login in cometchat==========//
    private void cometChatLogin() {

        Log.e("userId", prefsManager.getCaseId());
        cometChat.login(SITE_URL_COMETCHAT, prefsManager.getCaseId(), "cometchat", new Callbacks() {
            @Override
            public void successCallback(JSONObject jsonObject) {
                Log.e("jsonObject", "" + jsonObject);
            }

            @Override
            public void failCallback(JSONObject jsonObject) {
                Log.e("failCallback", "" + jsonObject);
            }
        });
    }

    /**
     * Implement this method to allow and check GPS permission.
     */

    public void checkPermission() {
        Log.e("checkPermission", "checkPermission");

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(MainScreenActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainScreenActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(MainScreenActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(MainScreenActivity.this,
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

    @OnClick({R.id.ivRidingDestination, R.id.ivPlanRide, R.id.ivRidingEvents, R.id.ivModifyBike, R.id.ivHealthyRides, R.id.ivGetDirections, R.id.ivLiveDream, R.id.ivProfile,R.id.ivTakeaTour})
    void click(View view) {
        switch (view.getId()) {
            case R.id.ivRidingDestination:
                if (!prefsManager.isLoginDone()) {
                    startActivityForResult(new Intent(activity, RegisterActivity.class), 1);
                } else {
                    startActivity(new Intent(MainScreenActivity.this, DestinationsListActivity.class));
                    finish();
                }
                break;

            case R.id.ivPlanRide:
                if (!prefsManager.isLoginDone()) {
                    startActivityForResult(new Intent(activity, RegisterActivity.class), 1);
                } else {
                    startActivity(new Intent(MainScreenActivity.this, PlanRideActivity.class).putExtra(DESTINATION_NAVIGATE, false));
                    finish();
                }
                break;
            case R.id.ivGetDirections:
                if (!prefsManager.isLoginDone()) {
                    startActivityForResult(new Intent(activity, RegisterActivity.class), 1);
                } else {
                    startActivity(new Intent(MainScreenActivity.this, GetDirections.class));
                    finish();
                }
                break;

            case R.id.ivRidingEvents:
                if (prefsManager.isLoginDone()) {
                    //badgeEventCountDisableInfo(prefsManager.getCaseId(),prefsManager.getToken());
                    Intent intentevnt = new Intent(MainScreenActivity.this, EventsListActivity.class);
                    intentevnt.putExtra("rides", "no");
                    intentevnt.putExtra("typeEmailPhone", getIntent().getStringExtra("typeEmailPhone"));
                    startActivity(intentevnt);
                    finish();
                } else {
                    //if (!prefsManager.isLoginDone())
                    startActivityForResult(new Intent(activity, RegisterActivity.class), 1);
                }
                break;

            case R.id.ivModifyBike:
                if (!prefsManager.isLoginDone()) {
                    startActivityForResult(new Intent(activity, RegisterActivity.class), 1);
                } else {
                    // startActivity(new Intent(MainScreenActivity.this, ComingSoon.class).putExtra(SCREEN_OPEN, "MODIFY YOUR BIKE"));
                    startActivity(new Intent(MainScreenActivity.this, ModifyBikeActivity.class));
                    finish();
                }
                break;

            case R.id.ivHealthyRides:
                if (!prefsManager.isLoginDone()) {
                    startActivityForResult(new Intent(activity, RegisterActivity.class), 1);
                } else {
                    startActivity(new Intent(MainScreenActivity.this, HealthyRidingActivity.class));
                    finish();
                }
                break;

            case R.id.ivLiveDream:
                if (!prefsManager.isLoginDone()) {
                    startActivityForResult(new Intent(activity, RegisterActivity.class), 1);
                } else {
                    startActivity(new Intent(MainScreenActivity.this, LiveforDream.class));
                }
                break;
            case R.id.ivProfile:
                if (!prefsManager.isLoginDone()) {
                    startActivityForResult(new Intent(activity, RegisterActivity.class), 1);
                } else {
                    startActivity(new Intent(MainScreenActivity.this, PublicProfileScreen.class));
                    finish();
                }
                break;
            case R.id.ivTakeaTour:
                startActivity(new Intent(MainScreenActivity.this, HomeScreenTakeATour.class));
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
            }
        }
    }

    //=========== API for badge count=====================//
    private void badgeCountInfo(String userId, String acessToken) {
        showProgressDialog();

        Log.e("AccessToken", "" + acessToken);
        Log.e("UserId", "" + userId);

        FileUploadService service = ServiceGenerator.createService(FileUploadService.BASE_URL_NEARBY_FRIENDS);

        service.badgeCountAPI(userId, acessToken, new retrofit.Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, retrofit.client.Response response) {
                Log.e("type", "==");
                Type type = new TypeToken<MainScreenResponse>() {
                }.getType();

                Log.e("jsonObject", "==" + jsonObject);
                MainScreenResponse mainScreenResponse = new Gson().fromJson(jsonObject.toString(), type);
                dismissProgressDialog();
                if (mainScreenResponse.getSuccess() == 1) {
                    mainScreenData = mainScreenResponse.getData();
                    if (mainScreenData.getCountEvent().equalsIgnoreCase("0")) {
                        tvEventCount.setVisibility(View.GONE);
                    } else {
                        tvEventCount.setVisibility(View.VISIBLE);
                        tvEventCount.setText(mainScreenData.getCountEvent());
                    }
                    if (mainScreenData.getCountProfile() == 0) {
                        tvProfileCount.setVisibility(View.GONE);
                    } else {
                        tvProfileCount.setVisibility(View.VISIBLE);
                        tvProfileCount.setText("" + mainScreenData.getCountProfile());
                        // tvProfileCount.setText(mainScreenData.getCountProfile());
                    }
                } else {

                    CustomDialog.showProgressDialog(MainScreenActivity.this, mainScreenResponse.getMessage().toString());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("error", "" + error.getMessage());
                dismissProgressDialog();
            }
        });
    }

//    //=========== API for badge count=====================//
//    private void badgeEventCountDisableInfo(String userId,String acessToken){
//        showProgressDialog();
//
//        Log.e("AccessToken",""+acessToken);
//        Log.e("UserId",""+userId);
//
//        FileUploadService service = ServiceGenerator.createService(FileUploadService.BASE_URL);
//
//        service.badgeEventCountDiableAPI(userId, acessToken,   new retrofit.Callback<JsonObject>() {
//            @Override
//            public void success(JsonObject jsonObject, retrofit.client.Response response) {
//                Log.e("type","==");
//                Type type = new TypeToken<EventCountDisableResponse>() {
//                }.getType();
//
//                Log.e("jsonObject","=="+jsonObject);
//                EventCountDisableResponse eventCountDisableResponse = new Gson().fromJson(jsonObject.toString(), type);
//                dismissProgressDialog();
//                if (eventCountDisableResponse.getSuccess()==1) {
//                    eventCountData = eventCountDisableResponse.getData();
////                    if(mainScreenData.getCountEvent().equalsIgnoreCase("0")){
////                        tvEventCount.setVisibility(View.GONE);
////                    }else {
////                        //tvEventCount.setVisibility(View.VISIBLE);
////                        tvEventCount.setText(mainScreenData.getCountEvent());
////                    }
////                    if(mainScreenData.getCountProfile()==0){
////                        tvProfileCount.setVisibility(View.GONE);
////                    }else {
////                        //tvProfileCount.setVisibility(View.VISIBLE);
////                        tvProfileCount.setText(""+mainScreenData.getCountProfile());
////                        // tvProfileCount.setText(mainScreenData.getCountProfile());
////                    }
//                }else{
//
//                    CustomDialog.showProgressDialog(MainScreenActivity.this, eventCountDisableResponse.getMessage().toString());
//                }
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//                Log.e("error",""+error.getMessage());
//                dismissProgressDialog();
//            }
//        });
//    }
}
