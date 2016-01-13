package com.nutsuser.ridersdomain.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.adapter.PlaceArrayAdapter;
import com.nutsuser.ridersdomain.utils.ApplicationGlobal;
import com.nutsuser.ridersdomain.web.api.RestClient;
import com.nutsuser.ridersdomain.web.api.volley.RequestJsonObject;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class RegisterActivity extends BaseActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks  {

    private GoogleApiClient mGoogleApiClient;
    private PlaceArrayAdapter mPlaceArrayAdapter;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));
    private static final String LOG_TAG = "RegisterActivity";
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private AutoCompleteTextView mAutocompleteTextView;
    private Activity activity;
    @Bind(R.id.tvTagRiders)
    TextView tvTagRiders;
    @Bind(R.id.tvTagOpinion)
    TextView tvTagOpinion;
    @Bind(R.id.edPhoneNo)
    EditText edTagPhoneNo;
    @Bind(R.id.edPassword)
    EditText edTagPassword;
    /*@Bind(R.id.edBrandName)
    EditText edTagBrandName;
    @Bind(R.id.edModelName)
    EditText edTagModelName;*/
  Map<String, String> params;
    String mStringlong,mStringlat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        activity = RegisterActivity.this;
        ButterKnife.bind(activity);
        setFontsToViews();
        mGoogleApiClient = new GoogleApiClient.Builder(RegisterActivity.this)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
                .addConnectionCallbacks(this)
                .build();
        mAutocompleteTextView = (AutoCompleteTextView) findViewById(R.id
                .autoCompleteTextView);
        mAutocompleteTextView.setThreshold(3);

        mAutocompleteTextView.setOnItemClickListener(mAutocompleteClickListener);
        mPlaceArrayAdapter = new PlaceArrayAdapter(this, android.R.layout.simple_list_item_1,
                BOUNDS_MOUNTAIN_VIEW, null);
        mAutocompleteTextView.setAdapter(mPlaceArrayAdapter);

    }
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
             mStringlong =""+Html.fromHtml(place.getLatLng().longitude + "");
             mStringlat =""+Html.fromHtml(place.getLatLng().latitude + "");
            Log.e("Long:",""+mStringlong);
            Log.e("Latitude:",""+mStringlat);
          //  mPhoneTextView.setText(Html.fromHtml("Lat:" + place.getLatLng().latitude + "--long:" + latlong));
            //mWebTextView.setText(place.getWebsiteUri() + "");
            if (attributions != null) {
               // mAttTextView.setText(Html.fromHtml(attributions.toString()));
            }
        }

    };
    private void setFontsToViews() {
        tvTagRiders.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT EXTRA LIGHT.TTF"));
        tvTagOpinion.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
    }

    @OnClick({R.id.tvExplore})
    void click(View view) {
        switch (view.getId()) {
            case R.id.tvExplore:
                if(isNetworkConnected()){
                    try {
                        if(edTagPhoneNo.getText().toString().trim().equals("")){
                            showToast("Email and Phone No blank");
                        }
                        else if(edTagPassword.getText().toString().trim().equals("")){
                            showToast("Password blank");
                        }
                        else if(mAutocompleteTextView.getText().toString().trim().equals("")){
                            showToast("location blank");
                        }
                        else{
                            Log.e("","ESLE");
                            if(edTagPhoneNo.getText().toString().contains("@")){
                                Log.e("IF","MATCHED");
                                boolean check=validEmail(edTagPhoneNo.getText().toString());
                                Log.e("IF: :",""+check);
                                if(check){
                                    showToast("EMAIL CORRECT");
                                    Log.e("IF","MATCHED CALLING");
                                    registerInfo(edTagPhoneNo.getText().toString(),mStringlat, mStringlong, edTagPassword.getText().toString(), "fgejfgryj4376535yg");
                                }
                            }
                            else{
                                Log.e("ELSE","PHONE MATCHED");
                                boolean check=validPhone(edTagPhoneNo.getText().toString());
                                Log.e("ELSE:",""+check);
                                if(check){
                                    showToast("PHONE CORRECT");
                                    Log.e("ELSE","PHONE MATCHED API");
                                    registerInfo(edTagPhoneNo.getText().toString(),mStringlat, mStringlong, edTagPassword.getText().toString(), "fgejfgryj4376535yg");
                                }
                            }

                        }

                    }
                    catch (Exception e){
                       // Rollbar.reportException(e, "critical", "Login Start");
                    }
                }

                else{
                    showToast("Internet Not Connected");
                }

               // startActivity(new Intent(RegisterActivity.this, AfterRegisterScreen.class));
               // finish();
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
Log.e("response: ",""+response);
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
                Log.e("error: ",""+error);
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
    /**
     * Register info .
     */
    public void registerInfo(String utypeid, String latitude, String longitude, String password, String devicetoken) {


        try {
            Log.e("URL: ",""+ApplicationGlobal.ROOT+ApplicationGlobal.baseurl_sigup+"utypeid="+utypeid+"&latitude="+latitude+"&longitude="+longitude+"&password="+password+"&deviceToken="+devicetoken+"&OS=Android");
            RequestQueue requestQueue = Volley.newRequestQueue(RegisterActivity.this);
            RequestJsonObject loginTaskRequest = new RequestJsonObject(Request.Method.POST,
                    ApplicationGlobal.ROOT+ApplicationGlobal.baseurl_sigup+"utypeid="+utypeid+"&latitude="+latitude+"&longitude="+longitude+"&password="+password+"&deviceToken="+devicetoken+"&OS=Android", null,
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
}
