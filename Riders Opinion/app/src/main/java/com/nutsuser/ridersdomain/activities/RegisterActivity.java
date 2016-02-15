package com.nutsuser.ridersdomain.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.adapter.PlaceArrayAdapter;
import com.nutsuser.ridersdomain.services.GPSService;
import com.nutsuser.ridersdomain.utils.ApplicationGlobal;
import com.nutsuser.ridersdomain.utils.CustomizeDialog;
import com.nutsuser.ridersdomain.utils.PrefsManager;
import com.nutsuser.ridersdomain.web.api.volley.RequestJsonObject;
import com.nutsuser.ridersdomain.web.pojos.Register;
import com.nutsuser.ridersdomain.web.pojos.RegisterDetails;
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
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.fabric.sdk.android.Fabric;


public class RegisterActivity extends BaseActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {


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
    @Bind(R.id.tvTagRiders)
    TextView tvTagRiders;
    @Bind(R.id.tvTagOpinion)
    TextView tvTagOpinion;
    @Bind(R.id.edPhoneNo)
    EditText edTagPhoneNo;
    @Bind(R.id.edPassword)
    EditText edTagPassword;
    // twitter
    @Bind(R.id.loginTwitter)
    TwitterLoginButton loginTwitter;
    Map<String, String> params;
    String mStringlong, mStringlat;
    RegisterDetails mRegisterDetails;
    // facebook
    @Bind(R.id.btldFacebook)
    LoginButton loginButton;
    double start, end;
    JSONObject jsonGraphObject, jsonResponseObj;
    CallbackManager callbackManager;
    String strAccountId, strCity, strEmail, strUserName, strLastName, strContact, strTokenId;
    private GoogleApiClient mGoogleApiClient;
    private PlaceArrayAdapter mPlaceArrayAdapter;
    private AutoCompleteTextView mAutocompleteTextView;
    private Activity activity;
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
            // Selecting the first object buffer.
            final Place place = places.get(0);
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
        registerInfo(strAccountId, mStringlat, mStringlong, "123456", "fgejfgryj4376535yg", "facebook");
        if (strUserName.isEmpty()) {
            strUserName = jsonGraphObject.optString(ApplicationGlobal.TAG_USER_NAME);
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

        activity = RegisterActivity.this;
        ButterKnife.bind(activity);
        setFontsToViews();
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

            start = mGPSService.getLatitude();
            end = mGPSService.getLongitude();
            mStringlat = Double.toString(start);
            mStringlong = Double.toString(end);


        }
    }

    private void setFontsToViews() {
        tvTagRiders.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT EXTRA LIGHT.TTF"));
        tvTagOpinion.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
    }

    @OnClick({R.id.tvExplore, R.id.flFacebook, R.id.flTwitter})
    void click(View view) {
        switch (view.getId()) {
            case R.id.tvExplore:
                if (isNetworkConnected()) {
                    try {
                        if (edTagPhoneNo.getText().toString().trim().equals("")) {
                            showToast("Email and Phone No blank");
                        } else if (edTagPassword.getText().toString().trim().equals("")) {
                            showToast("Password blank");
                        } else if (mAutocompleteTextView.getText().toString().trim().equals("")) {
                            showToast("location blank");
                        } else {
                            Log.e("", "ESLE");
                            if (edTagPhoneNo.getText().toString().contains("@")) {
                                Log.e("IF", "MATCHED");
                                boolean check = validEmail(edTagPhoneNo.getText().toString());
                                Log.e("IF: :", "" + check);
                                if (check) {
                                    showToast("EMAIL CORRECT");
                                    Log.e("IF", "MATCHED CALLING");
                                    registerInfo(edTagPhoneNo.getText().toString(), mStringlat, mStringlong, edTagPassword.getText().toString(), "fgejfgryj4376535yg", "default");
                                }
                            } else {
                                Log.e("ELSE", "PHONE MATCHED");
                                boolean check = validPhone(edTagPhoneNo.getText().toString());
                                Log.e("ELSE:", "" + check);
                                if (check) {
                                    showToast("PHONE CORRECT");
                                    Log.e("ELSE", "PHONE MATCHED API");
                                    registerInfo(edTagPhoneNo.getText().toString(), mStringlat, mStringlong, edTagPassword.getText().toString(), "fgejfgryj4376535yg", "default");
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
            case R.id.flTwitter:
                iPositionSelected = TAG_LOGIN_TWITTER;
                Log.e("Twitter:", "" + iPositionSelected);
                loginTwitter.setCallback(new LoginHandler());

                loginTwitter.performClick();
                break;

        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    /**
     * Implement success listener on execute api url.
     */
    public Response.Listener<JSONObject> volleySuccessListener() {
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
                    startActivity(new Intent(RegisterActivity.this, AfterRegisterScreen.class));
                    finish();

                }
            }
        };
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
                showToast("Error:" + error);
            }
        };
    }

    private boolean validEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();

    }

    private boolean validPhone(String phone) {
        Pattern pattern = Patterns.PHONE;
        return pattern.matcher(phone).matches();

    }

    public void showProgressDialog() {

        mCustomizeDialog = new CustomizeDialog(RegisterActivity.this);
        mCustomizeDialog.setCancelable(false);
        mCustomizeDialog.show();

    }

    public void dismissProgressDialog() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mCustomizeDialog != null && mCustomizeDialog.isShowing()) {
                    mCustomizeDialog.dismiss();
                    mCustomizeDialog = null;
                }
            }
        }, 300);


    }

    /**
     * Register info .
     */
    public void registerInfo(String utypeid, String latitude, String longitude, String password, String devicetoken, String type) {
//http://ridersopininon.herokuapp.com/index.php/riders/signup?utypeid=jaswant123@gmail.com&latitude=768.0000&longitude=126.0000&password=111111&deviceToken=ioel3343&OS=android&loginType=default
        showProgressDialog();
        try {
            Log.e("URL: ", "" + ApplicationGlobal.ROOT + ApplicationGlobal.baseurl_sigup + "utypeid=" + utypeid + "&latitude=" + latitude + "&longitude=" + longitude + "&password=" + password + "&deviceToken=" + devicetoken + "&OS=Android&loginType=" + type);
            RequestQueue requestQueue = Volley.newRequestQueue(RegisterActivity.this);
            RequestJsonObject loginTaskRequest = new RequestJsonObject(Request.Method.POST,
                    ApplicationGlobal.ROOT + ApplicationGlobal.baseurl_sigup + "utypeid=" + utypeid + "&latitude=" + latitude + "&longitude=" + longitude + "&password=" + password + "&deviceToken=" + devicetoken + "&OS=Android&loginType=" + type, null,
                    volleyErrorListener(), volleySuccessListener()
            );

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

        Toast.makeText(this,
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();
    }


    /**
     * Twitter call back and user details handle here.
     */
    private class LoginHandler extends Callback<TwitterSession> {
        @Override
        public void success(Result<TwitterSession> twitterSessionResult) {

            // strCity = "en-us";//Now static.
            strAccountId = "" + twitterSessionResult.data.getUserId();
            registerInfo(strAccountId, mStringlat, mStringlong, "123456", "fgejfgryj4376535yg", "twitter");
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
            Log.e("Result: ", "" + exception.toString());
            //showSnackBarMessages(coordinatorLayout, "Attempt to Login failed");
        }
    }
}
