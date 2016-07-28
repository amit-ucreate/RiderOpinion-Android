package com.nutsuser.ridersdomain.activities;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.inscripts.cometchat.sdk.CometChat;
import com.inscripts.interfaces.Callbacks;
import com.inscripts.utils.Logger;
import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.adapter.PlaceArrayAdapter;
import com.nutsuser.ridersdomain.services.GPSService;
import com.nutsuser.ridersdomain.utils.ApplicationGlobal;
import com.nutsuser.ridersdomain.utils.CustomizeDialog;
import com.nutsuser.ridersdomain.utils.PrefsManager;
import com.nutsuser.ridersdomain.view.CustomDialog;
import com.nutsuser.ridersdomain.web.api.volley.RequestJsonObject;
import com.nutsuser.ridersdomain.web.pojos.Register;
import com.nutsuser.ridersdomain.web.pojos.RegisterDetails;
import com.rollbar.android.Rollbar;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.fabric.sdk.android.Fabric;
import retrofit.http.HEAD;


public class RegisterActivity extends BaseActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {
    String deviceToken;
    String typeEmailPhone=null;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    //GCM Declear
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    public static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS_CORSE = 2;
    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "nQ3iUHEZ8Mh4syPepbJsFkmrr";
    private static final String TWITTER_SECRET = "D2j3TPEjVnXBvyOnGjoK21wbv4He79Nx0O0ye5SQb2fPnssXKE";
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));
    private static final String LOG_TAG = "RegisterActivity";
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private static final int TAG_LOGIN_FACEBOOK = 1;
    private static final int TAG_LOGIN_TWITTER = 2;
    PrefsManager prefsManager;
    CustomizeDialog mCustomizeDialog;
    @Bind(R.id.tv_TagRiders)
    TextView tv_TagRiders;
    @Bind(R.id.tv_TagOpinion)
    TextView tv_TagOpinion;
    @Bind(R.id.edPhoneNo)
    EditText edTagPhoneNo;
    @Bind(R.id.edUsername)
    EditText edUsername;
    /*@Bind(R.id.edPassword)
    EditText edTagPassword;*/
    @Bind(R.id.tvExplore)
    TextView tvExplore;
    // twitter
    //@Bind(R.id.login_button)
    TwitterLoginButton loginTwitter;
    Map<String, String> params;
    String mStringlong, mStringlat;
    RegisterDetails mRegisterDetails;
    // facebook
    @Bind(R.id.btldFacebook)
    LoginButton loginButton;
    Button btlTwitter;
    double start, end;
    JSONObject jsonGraphObject, jsonResponseObj;
    CallbackManager callbackManager;
    String strAccountId, strCity, strEmail, strUserName, strLastName, strContact, strTokenId;
    private GoogleApiClient mGoogleApiClient;
    private PlaceArrayAdapter mPlaceArrayAdapter;
    private AutoCompleteTextView mAutocompleteTextView;
    private Activity activity;
    private CometChat cometChat;

    private int iPositionSelected;//Use to describe which type of login user selected.
    /**
     * Implement facebook integration callback functionality.
     */
    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {

            //Show wait dialog.
            //showPleaseWait("Please Wait...");

            // App code
            GraphRequest request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(
                                JSONObject object,
                                GraphResponse response) {

                            //Log.e("FACEBOOK RESPONSE ", response.toString());
                            jsonGraphObject = response.getJSONObject();

                            //Get user detail and save it to loacal instances.
                            getFacebookInfo();

                            LoginManager.getInstance().logOut();

                            //Save info to params and pass Login Type [Twitch,Facebook,Twitter].
                            //saveInfoToParams(ApplicationGlobal.TAG_LOGIN_FACEBOOK);
                        }
                    });
            Bundle parameters = new Bundle();
            //   parameters.putString("fields", "id,city,name,email,gender, birthday,first_name,locale,country,latitude,longitude,state,zip");
            parameters.putString("fields", "email,first_name,last_name, locale, location");
            request.setParameters(parameters);
            request.executeAsync();
        }

        @Override
        public void onCancel() {
            // showSnackBarMessages(coordinatorLayout, "Attempt to Login failed");
            // hideProgress();
        }

        @Override
        public void onError(FacebookException e) {
            // showSnackBarMessages(coordinatorLayout, "Login unsuccessful!");
            Log.e("FACEBOOK ERROR ", e.toString());
            // Rollbar.reportException(e, "critical", "Login unsuccessful! " + e);
            // hideProgress();
        }
    };
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e(LOG_TAG, "Place query did not complete. Error: " +
                        places.getStatus().toString());
                return;
            }
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
            mStringlong = "" + Html.fromHtml(place.getLatLng().longitude + "");
            mStringlat = "" + Html.fromHtml(place.getLatLng().latitude + "");
            Log.e("Long:", "" + mStringlong);
            Log.e("Latitude:", "" + mStringlat);
            //  mPhoneTextView.setText(Html.fromHtml("Lat:" + place.getLatLng().latitude + "--long:" + latlong));
            //mWebTextView.setText(place.getWebsiteUri() + "");
            if(edTagPhoneNo.getText().toString().isEmpty()) {
                edTagPhoneNo.setFocusable(true);
                edTagPhoneNo.requestFocus();
            }
            if (attributions != null) {
                // mAttTextView.setText(Html.fromHtml(attributions.toString()));
            }
        }

    };
    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            Log.i(LOG_TAG, "Selected: " + item.description);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            Log.i(LOG_TAG, "Fetching details for ID: " + item.placeId);
        }
    };

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

    /**
     * Extract user detail from @jsonGraphObject and store each information
     * separately so that it can store it on server easily.
     */
    public void getFacebookInfo() {
        String strAccountId = jsonGraphObject.optString(ApplicationGlobal.TAG_ACCOUNT_ID);
        String strCity = jsonGraphObject.optString(ApplicationGlobal.TAG_CITY);
        String strEmail = jsonGraphObject.optString(ApplicationGlobal.TAG_EMAIL);
        String strUserName = jsonGraphObject.optString(ApplicationGlobal.TAG_FIRST_NAME);
        String strLastName = jsonGraphObject.optString(ApplicationGlobal.TAG_LAST_NAME);//TAG_LAST_NAME
        String strContact = "";//jsonGraphObject.optString(ApplicationGlobal.TAG_CONTACT);
        String strTokenId = "1823928";//Static now.
         if (strUserName.isEmpty()) {
            strUserName = jsonGraphObject.optString(ApplicationGlobal.TAG_USER_NAME);
        }
        registerInfo(strAccountId, mStringlat, mStringlong,deviceToken = prefsManager.getDevicetoken(), "facebook", "", "email",strUserName);

    }

    /**
     * Implement this method to allow and check GPS permission.
     */

    public void checkPermission() {
        Log.e("checkPermission", "checkPermission");

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(RegisterActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(RegisterActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(RegisterActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(RegisterActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }


    }

    /**
     * After Facebook/Twitter/Twitch credentials and get user-information,
     * control comes here.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (iPositionSelected) {

            case TAG_LOGIN_FACEBOOK:
                callbackManager.onActivityResult(requestCode, resultCode, data);
                break;
            case TAG_LOGIN_TWITTER:
                Log.e("Activity Result: ", "onActivityResult requestCode:" + requestCode + " resultCode:" + resultCode);

                loginTwitter.onActivityResult(requestCode, resultCode, data);

                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_register);


        cometChat = CometChat.getInstance(getApplicationContext(),
                "10415x77177883eedf5255554e825180e563c1");

        try {
            activity = RegisterActivity.this;
            ButterKnife.bind(activity);
            setFontsToViews();
            checkPermission();
            loginTwitter = (TwitterLoginButton) findViewById(R.id.login_button);
            Button flTwitter = (Button) findViewById(R.id.btlTwitter);
            flTwitter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iPositionSelected = TAG_LOGIN_TWITTER;
                    Log.e("Twitter:", "" + iPositionSelected);
                    loginTwitter.setCallback(new LoginHandler());
                    loginTwitter.performClick();
                }
            });
            prefsManager = new PrefsManager(this);
            mGoogleApiClient = new GoogleApiClient.Builder(RegisterActivity.this)
                    .addApi(Places.GEO_DATA_API)
                    .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
                    .addConnectionCallbacks(this)
                    .build();
            mAutocompleteTextView = (AutoCompleteTextView) findViewById(R.id
                    .autoCompleteTextView);
            mAutocompleteTextView.setThreshold(1);

            mAutocompleteTextView.setOnItemClickListener(mAutocompleteClickListener);
            mPlaceArrayAdapter = new PlaceArrayAdapter(this, android.R.layout.simple_list_item_1,
                    BOUNDS_MOUNTAIN_VIEW, null);
            mAutocompleteTextView.setAdapter(mPlaceArrayAdapter);

            callbackManager = CallbackManager.Factory.create();

            loginButton.setReadPermissions(Arrays.asList("public_profile, email, user_birthday, user_friends"));

            loginButton.registerCallback(callbackManager, callback);
//        GPSService mGPSService = new GPSService(this);
//        mGPSService.getLocation();
//
//        if (mGPSService.isLocationAvailable == false) {
//
//            // Here you can ask the user to try again, using return; for that
//            Toast.makeText(getApplicationContext(), "Your location is not available, please try again.", Toast.LENGTH_SHORT).show();
//            return;
//
//            // Or you can continue without getting the location, remove the return; above and uncomment the line given below
//            // address = "Location not available";
//        } else {
//
//            // Getting location co-ordinates
//            double latitude = mGPSService.getLatitude();
//            double longitude = mGPSService.getLongitude();
//            // Toast.makeText(getApplicationContext(), "Latitude:" + latitude + " | Longitude: " + longitude, Toast.LENGTH_LONG).show();
//
//            start = mGPSService.getLatitude();
//            end = mGPSService.getLongitude();
//            mStringlat = Double.toString(start);
//            mStringlong = Double.toString(end);
//            Log.e("getLocationAddress()",""+mGPSService.getLocationAddress());
//            mAutocompleteTextView.setText(mGPSService.getLocationAddress());
//        }
            mRegistrationBroadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    //  mRegistrationProgressBar.setVisibility(ProgressBar.GONE);
                    SharedPreferences sharedPreferences =
                            PreferenceManager.getDefaultSharedPreferences(context);
                    boolean sentToken = sharedPreferences
                            .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
                    if (sentToken) {
                        //mInformationTextView.setText(getString(R.string.gcm_send_message));
                    } else {
                        // mInformationTextView.setText(getString(R.string.token_error_message));
                    }
                }
            };
            Log.e("checkPlayServices():", "" + checkPlayServices());
            if (checkPlayServices()) {
                Log.e("", "CALLING");
                // Start IntentService to register this application with GCM.
                deviceToken = prefsManager.getDevicetoken();
                Log.e("deviceToken", "" + deviceToken);
                if (deviceToken != null) {
                    Log.e("if", "CALLING");
                } else {
                    Log.e("ELSE", "CALLING");
                    Intent intent = new Intent(this, RegistrationIntentService.class);
                    startService(intent);
                }
            }
        }catch(Exception e){
            Rollbar.reportException(e, "minor", "Register activity on create");
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        GPSService mGPSService = new GPSService(this);
        mGPSService.getLocation();

        if (mGPSService.isLocationAvailable == false) {

            // Here you can ask the user to try again, using return; for that
           // Toast.makeText(getApplicationContext(), "Your location is not available, please try again.", Toast.LENGTH_SHORT).show();
            return;

            // Or you can continue without getting the location, remove the return; above and uncomment the line given below
            // address = "Location not available";
        } else {

            Log.e("location call","Loaction call");
            // Getting location co-ordinates
            double latitude = mGPSService.getLatitude();
            double longitude = mGPSService.getLongitude();
            // Toast.makeText(getApplicationContext(), "Latitude:" + latitude + " | Longitude: " + longitude, Toast.LENGTH_LONG).show();

            start = mGPSService.getLatitude();
            end = mGPSService.getLongitude();
            mStringlat = Double.toString(start);
            mStringlong = Double.toString(end);
            Log.e("getLocationAddress()",""+mGPSService.getLocationAddress());
            mAutocompleteTextView.setText(mGPSService.getLocationAddress());
        }
        Log.e("OnREsume ","Loaction ");
        try {
            deviceToken = prefsManager.getDevicetoken();
            if (deviceToken != null) {

            } else {
                LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                        new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
            }
        } catch (Exception e) {
            Rollbar.reportException(e, "critical", "login on resume");
        }
    }

    @Override
    protected void onPause() {
        Log.e("onPause ","Loaction ");
        try {
            deviceToken = prefsManager.getDevicetoken();
            if (deviceToken != null) {

            } else {
                LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
            }
        } catch (Exception e) {
            Rollbar.reportException(e, "critical", "Login on pause");
        }
        super.onPause();
    }

    private void setFontsToViews() {
        tvExplore.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/Lato-Heavy.ttf"));
        String text = "<font color=#D1622A>Divided</font> <font color=#000000>By Boundaries</font>";
        String text_ = "<font color=#D1622A>United</font> <font color=#000000>By Throttles</font>";
        tv_TagRiders.setText(Html.fromHtml(text));
        tv_TagOpinion.setText(Html.fromHtml(text_));
        tv_TagRiders.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/LATO-REGULAR.TTF"));
        tv_TagOpinion.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/LATO-REGULAR.TTF"));
        //tvTagRiders.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT EXTRA LIGHT.TTF"));
        // tvTagOpinion.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
    }

    @OnClick({R.id.tvExplore, R.id.flFacebook})
    void click(View view) {
        switch (view.getId()) {
            case R.id.tvExplore:
                if (isNetworkConnected()) {
                    try {
                        if (edTagPhoneNo.getText().toString().trim().equals("")) {
                            showToast("Please Enter Email and Phone No");
                        } /*else if (edTagPassword.getText().toString().trim().equals("")) {
                            showToast("Please Enter Password");
                        }*/
                        else if(edUsername.getText().toString().isEmpty()){
                            showToast("Please Enter Username");
                        }
                        else if (mAutocompleteTextView.getText().toString().trim().equals("")) {
                            showToast("Please Enter Location");
                        } else {
                            Log.e("", "ESLE");
//                            StringTokenizer startlocation = new StringTokenizer(mAutocompleteTextView.getText().toString(), ",");
//                            String tvStartLocation = startlocation.nextToken();
//                            Log.e("tvStartLocation",tvStartLocation+"  ==="+mAutocompleteTextView.getText().toString() );
                            if (edTagPhoneNo.getText().toString().contains("@")) {
                                Log.e("IF", "MATCHED");
                                boolean check = validEmail(edTagPhoneNo.getText().toString());
                                Log.e("IF: :", "" + check);
                                if (check) {
                                   // showToast("EMAIL CORRECT");
                                    Log.e("IF", "MATCHED CALLING");
                                    registerInfo(edTagPhoneNo.getText().toString(), mStringlat, mStringlong,deviceToken = prefsManager.getDevicetoken(), "default", mAutocompleteTextView.getText().toString().trim(), "email",edUsername.getText().toString());
                                }else{
                                    showToast("Please enter valid email.");
                                }
                            } else {
                                Log.e("ELSE", "PHONE MATCHED");
                                boolean check = validPhone(edTagPhoneNo.getText().toString());
                                Log.e("ELSE:", "" + check);
                                if (check) {
                                  //  showToast("PHONE CORRECT");
                                    Log.e("ELSE", "PHONE MATCHED API");
                                    registerInfo(edTagPhoneNo.getText().toString(), mStringlat, mStringlong,deviceToken = prefsManager.getDevicetoken(), "default", mAutocompleteTextView.getText().toString().trim(), "phone",edUsername.getText().toString());
                                }else{
                                    showToast("Please enter valid Phone Number or Email.");
                                }
                            }

                        }
                      /* startActivity(new Intent(RegisterActivity.this, AfterRegisterScreen.class));
                        finish();*/

                    } catch (Exception e) {
                        // Rollbar.reportException(e, "critical", "Login Start");
                    }
                } else {
                    showToast("Internet Not Connected");
                }


                break;
            case R.id.flFacebook:
                iPositionSelected = TAG_LOGIN_FACEBOOK;
                loginButton.performClick();
                break;
         /*  case R.id.login_button:
               iPositionSelected = TAG_LOGIN_TWITTER;
               Log.e("Twitter:", "" + iPositionSelected);
               loginTwitter.setCallback(new LoginHandler());

               //loginTwitter.performClick();
                break;*/

        }
    }

//    @Override
//    public void onBackPressed() {
//        //super.onBackPressed();
//    }

    /**
     * Implement success listener on execute api url.
     */
    public Response.Listener<JSONObject> volleySuccessListener( final String typeEntered) {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dismissProgressDialog();
                Log.e("response: ", "" + response);
                Type type = new TypeToken<Register>() {
                }.getType();
                Register register = new Gson().fromJson(response.toString(), type);
                if (register.getSuccess().equals("1")) {
                    mRegisterDetails = register.getData();
                    prefsManager.setLoginDone(true);
                    prefsManager.setToken(mRegisterDetails.getAccessToken());
                    prefsManager.setCaseId(mRegisterDetails.getUserId());
                    prefsManager.setRadius("2000");
                    prefsManager.setUserName(mRegisterDetails.getUserName());
                    prefsManager.setImageUrl(mRegisterDetails.getUserProfileImage());
                    prefsManager.setmKeyBannerImageUrl(mRegisterDetails.getUserBannerImage());

                    Log.e("mRegisterDetails",""+mRegisterDetails);
                    if (mRegisterDetails.getIsNewUser().equals("1")) {
                        maincongProgressDialog(RegisterActivity.this, register.getMessage(), typeEntered);
                    } else {
                        congProgressDialog(RegisterActivity.this, register.getMessage(), typeEntered);
                    }
                  cometChatLogin(mRegisterDetails.getUserId());
                } else {
                    CustomDialog.showProgressDialog(RegisterActivity.this, register.getMessage());
                }
            }
        };
    }


    private void cometChatLogin(String userId){
        cometChat.login(SITE_URL_COMETCHAT, userId, "cometchat", new Callbacks() {

            @Override
            public void successCallback(JSONObject response) {

              //  SharedPreferenceHelper.save(SharedPreferenceKeys.USER_NAME, username);
                prefsManager.setPasswordCometChat("cometchat");
               // SharedPreferenceHelper.save(SharedPreferenceKeys.PASSWORD, password);
               // Logger.debug("sresponse->" + response);
               // LogsActivity.addToLog("Login successCallback");
               // startCometchat();
            }

            @Override
            public void failCallback(JSONObject response) {
//                usernameField.setError("Incorrect username");
//                passwordField.setError("Incorrect password");
                Logger.debug("fresponse->" + response);
               // LogsActivity.addToLog("Login failCallback");
            }
        });

//            cometChat.login(SITE_URL_COMETCHAT, userId,"cometchat", new Callbacks() {
//                @Override
//                public void successCallback(JSONObject jsonObject) {
//                  Log.e("jsonObject",""+jsonObject);
//                }
//
//                @Override
//                public void failCallback(JSONObject jsonObject) {
//                    Log.e("failCallback",""+jsonObject);
//                }
//            });
    }
    public void congProgressDialog(Context context, String message, final String type) {

        new AlertDialog.Builder(context).setTitle("Congratulations!!")
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ApplicationGlobal.FIRST_TAKE_TOUR=true;
                        Intent intent = new Intent(RegisterActivity.this, AfterRegisterScreen.class);
                        intent.putExtra("typeEmailPhone", type);
                        startActivity(intent);
                        finish();


                    }
                }).show();

                        // dialog.dismiss();

                    }




    public void maincongProgressDialog(Context context, String message,final String type) {

       //ew AlertDialog.Builder(context).setTitle("Message");

        new AlertDialog.Builder(context).setTitle("Welcome!!")

                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                        startActivity(new Intent(RegisterActivity.this, MainScreenActivity.class).putExtra("typeEmailPhone", type));
                        finish();


                    }
                }).show();

    }

    /**
     * Implement Volley error listener here.
     */
    public Response.ErrorListener volleyErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error: ", "" + error);
                dismissProgressDialog();
               // showToast("Error:" + error);
            }
        };
    }

    private boolean validEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();

    }

    private boolean validPhone(String phone) {
        if(phone.length()>=10&&phone.length()<=12) {
            Pattern pattern = Patterns.PHONE;
            return pattern.matcher(phone).matches();
        }else{
            return false;
        }

    }




// ALL GCM functionality declear

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i("LoginActivity", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    /**
     * Register info .
     */
    public void registerInfo(String utypeid, String latitude, String longitude,String devicetoken, String type, String baselocation, String secType,String username) {
//http://ridersopininon.herokuapp.com/index.php/riders/signup?utypeid=jaswant123@gmail.com&latitude=768.0000&longitude=126.0000&password=111111&deviceToken=ioel3343&OS=android&loginType=default
        showProgressDialog();
        try {

            typeEmailPhone = secType;
            String tvStartLocation=null;
            if (baselocation.equals("")) {
                tvStartLocation = "";
                Log.e("URL: ", "" + ApplicationGlobal.ROOT + ApplicationGlobal.baseurl_sigup + "utypeid=" + utypeid + "&latitude=" + latitude + "&longitude=" + longitude + "&baseLocation=" + tvStartLocation.replaceAll(" ","") +"&userName="+username.replaceAll(" ","%20")+ "&deviceToken=" + devicetoken + "&OS=Android&loginType=" + type);
            } else {
                StringTokenizer endlocation = new StringTokenizer(baselocation, ",");
                tvStartLocation = endlocation.nextToken().replaceAll(" ","%20");

                Log.e("tvStartLocation","======="+tvStartLocation.replaceAll(" ","%20"));
                Log.e("URL: ", "" + ApplicationGlobal.ROOT + ApplicationGlobal.baseurl_sigup + "utypeid=" + utypeid + "&latitude=" + latitude + "&longitude=" + longitude + "&baseLocation=" + tvStartLocation+"&userName="+username.replaceAll(" ","%20") + "&deviceToken=" + devicetoken + "&OS=Android&loginType=" + type);
            }


            RequestQueue requestQueue = Volley.newRequestQueue(RegisterActivity.this);
            RequestJsonObject loginTaskRequest = new RequestJsonObject(Request.Method.POST,
                    ApplicationGlobal.ROOT + ApplicationGlobal.baseurl_sigup + "utypeid=" + utypeid + "&latitude=" + latitude + "&longitude=" + longitude + "&baseLocation=" + tvStartLocation.replaceAll(" ","%20")+"&userName="+username.replaceAll(" ","%20") + "&deviceToken=" + devicetoken + "&OS=Android&loginType=" + type, null,
                    volleyErrorListener(), volleySuccessListener(secType)
            );
            int socketTimeout = 200000;//2 min - change to what you want
            loginTaskRequest.setRetryPolicy(new DefaultRetryPolicy(
                    socketTimeout,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            requestQueue.add(loginTaskRequest);

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    @Override
    public void onConnected(Bundle bundle) {
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

//        Toast.makeText(this,
//                "Google Places API connection failed with error code:" +
//                        connectionResult.getErrorCode(),
//                Toast.LENGTH_LONG).show();
    }


    /**
     * Twitter call back and user details handle here.
     */
    private class LoginHandler extends Callback<TwitterSession> {
        @Override
        public void success(Result<TwitterSession> twitterSessionResult) {

            // strCity = "en-us";//Now static.
            strAccountId = "" + twitterSessionResult.data.getUserId();
            strUserName =  "" + twitterSessionResult.data.getUserName();
            registerInfo(strAccountId, mStringlat, mStringlong,deviceToken = prefsManager.getDevicetoken(), "twitter", "", "email",strUserName);
            // strEmail = "abc@gmail.com"; //Now static.
            // strUserName = twitterSessionResult.data.getUserName();
            // strLastName = twitterSessionResult.data.getUserName();
            //// strContact = "";//Now static.
            //TypeCast Long to String to get user ID from Twitter.
            //strTokenId = Long.toString(twitterSessionResult.data.getUserId()); //now static

            //Save info to params and pass Login Type [Twitch,Facebook,Twitter].
            //saveInfoToParams(WebUrl.TAG_LOGIN_TYPE_TT);
        }

        @Override
        public void failure(TwitterException exception) {
//            Twitter.getSessionManager().clearActiveSession();
//            Twitter.logOut();
            Log.e("Result: ", "" + exception);
            //showSnackBarMessages(coordinatorLayout, "Attempt to Login failed");
        }
    }


}
