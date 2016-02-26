package com.nutsuser.ridersdomain.activities;

import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.adapter.AdapterDestination;
import com.nutsuser.ridersdomain.services.GPSService;
import com.nutsuser.ridersdomain.utils.ApplicationGlobal;
import com.nutsuser.ridersdomain.utils.CustomizeDialog;
import com.nutsuser.ridersdomain.utils.PrefsManager;
import com.nutsuser.ridersdomain.web.api.volley.RequestJsonObject;
import com.nutsuser.ridersdomain.web.pojos.RidingDestination;
import com.nutsuser.ridersdomain.web.pojos.RidingDestinationDetails;
import com.nutsuser.ridersdomain.web.pojos.RidingDestinationDetailsClick;
import com.yahoo.mobile.client.android.util.rangeseekbar.RangeSeekBar;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by user on 8/31/2015.
 */
public class FilterActivity extends BaseActivity {
    CustomizeDialog mCustomizeDialog;
    @Bind(R.id.tvCancel)
    TextView tvCancel;
    @Bind(R.id.tvTitleToolbar)
    TextView tvTitleToolbar;
    @Bind(R.id.tvClear)
    TextView tvClear;
    @Bind(R.id.tvLabelDistance)
    TextView tvLabelDistance;
    @Bind(R.id.tvLabelType)
    TextView tvLabelType;
    @Bind(R.id.tvBeach)
    TextView tvBeach;
    @Bind(R.id.tvHills)
    TextView tvHills;
    @Bind(R.id.tvWildLife)
    TextView tvWildLife;
    @Bind(R.id.tvAdventure)
    TextView tvAdventure;
    @Bind(R.id.tvLabelRating)
    TextView tvLabelRating;
    @Bind(R.id.tvApply)
    TextView tvApply;
   @Bind(R.id.seekBar1)
    SeekBar seekBar1;
    @Bind(R.id.ivRatting1)
    ImageView ivRatting1;

    @Bind(R.id.ivRatting2)
    ImageView ivRatting2;

    @Bind(R.id.ivRatting3)
    ImageView ivRatting3;

    @Bind(R.id.ivRatting4)
    ImageView ivRatting4;

    @Bind(R.id.ivRatting5)
    ImageView ivRatting5;
    @Bind(R.id.tvDistance)
    TextView tvDistance;

    String AccessToken, UserId;

    double start1, end1;
    String star_lat, star_long,ratting,type;

    public static boolean filter=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        overridePendingTransition(R.anim.trans_top_in, R.anim.trans_top_out);
        ButterKnife.bind(this);
        setFontsToViews();
        setupSeekbar();
        tvDistance=(TextView)findViewById(R.id.tvDistance);
        seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            int progress = 0;


            @Override

            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {

                progress = progresValue;
               // tvDistance.setText("Please Wait..");
                //Toast.makeText(getApplicationContext(), "Changing seekbar's progress: "+progress, Toast.LENGTH_SHORT).show();

            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
               // Toast.makeText(getApplicationContext(), "Started tracking seekbar:"+seekBar.getProgress(), Toast.LENGTH_SHORT).show();

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                tvDistance.setText(""+seekBar.getProgress());
                //textView.setText("Covered: " + progress + "/" + seekBar.getMax());
                //Toast.makeText(getApplicationContext(), "Stopped tracking seekbar+_"+seekBar.getProgress(), Toast.LENGTH_SHORT).show();

            }

        });

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
            star_lat = String.valueOf(start1);
            star_long = String.valueOf(end1);
           // Filter();
        }


        // make sure you close the gps after using it. Save user's battery power
        mGPSService.closeGPS();

    }

    private void setupSeekbar() {
     /*   rsbDistance = new RangeSeekBar<Integer>(this);
        rsbDistance.setRangeValues(0, 1500);
        rsbDistance.setSelectedMinValue(50);
        rsbDistance.setSelectedMaxValue(400);
        llRangeSeekBar.addView(rsbDistance);*/
    }

    private void setFontsToViews() {
        Typeface typefaceNormal = Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF");
        tvCancel.setTypeface(typefaceNormal);
        tvTitleToolbar.setTypeface(typefaceNormal);
        tvClear.setTypeface(typefaceNormal);
        tvLabelDistance.setTypeface(typefaceNormal);
        tvLabelType.setTypeface(typefaceNormal);
        tvBeach.setTypeface(typefaceNormal);
        tvHills.setTypeface(typefaceNormal);
        tvWildLife.setTypeface(typefaceNormal);
        tvAdventure.setTypeface(typefaceNormal);
        tvLabelRating.setTypeface(typefaceNormal);
        tvApply.setTypeface(typefaceNormal);
    }

    @OnClick({R.id.tvCancel,R.id.ivRatting1,R.id.ivRatting2,R.id.ivRatting3,R.id.ivRatting4,R.id.ivRatting5,R.id.tvBeach,R.id.tvHills,R.id.tvWildLife,R.id.tvAdventure,R.id.tvClear,R.id.tvApply})
    void click(View view) {
        switch (view.getId()) {
            case R.id.tvCancel:
                onBackPressed();
                break;
            case R.id.tvBeach:
                type="beach";
                tvBeach.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_tick, 0);
                tvHills.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                tvWildLife.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                tvAdventure.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                break;
            case R.id.tvHills:
                type="hills";
                tvBeach.setCompoundDrawablesWithIntrinsicBounds(0, 0,0, 0);
                tvHills.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_tick, 0);
                tvWildLife.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                tvAdventure.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                break;
            case R.id.tvWildLife:
                type="wildlife";
                tvBeach.setCompoundDrawablesWithIntrinsicBounds(0, 0,0, 0);
                tvHills.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                tvWildLife.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_tick, 0);
                tvAdventure.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                break;
            case R.id.tvAdventure:
                type="adventure";
                tvBeach.setCompoundDrawablesWithIntrinsicBounds(0, 0,0, 0);
                tvHills.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                tvWildLife.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                tvAdventure.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.icon_tick, 0);
                break;

            case R.id.ivRatting1:
                ratting="1";
                ivRatting1.setImageResource(R.drawable.icon_radio_selected);
                ivRatting2.setImageResource(R.drawable.icon_radio);
                ivRatting3.setImageResource(R.drawable.icon_radio);
                ivRatting4.setImageResource(R.drawable.icon_radio);
                ivRatting5.setImageResource(R.drawable.icon_radio);

                break;
            case R.id.ivRatting2:
                ratting="2";
                ivRatting1.setImageResource(R.drawable.icon_radio);
                ivRatting2.setImageResource(R.drawable.icon_radio_selected);
                ivRatting3.setImageResource(R.drawable.icon_radio);
                ivRatting4.setImageResource(R.drawable.icon_radio);
                ivRatting5.setImageResource(R.drawable.icon_radio);
                break;
            case R.id.ivRatting3:
                ratting="3";
                ivRatting1.setImageResource(R.drawable.icon_radio);
                ivRatting2.setImageResource(R.drawable.icon_radio);
                ivRatting3.setImageResource(R.drawable.icon_radio_selected);
                ivRatting4.setImageResource(R.drawable.icon_radio);
                ivRatting5.setImageResource(R.drawable.icon_radio);
                break;
            case R.id.ivRatting4:
                ratting="4";
                ivRatting1.setImageResource(R.drawable.icon_radio);
                ivRatting2.setImageResource(R.drawable.icon_radio);
                ivRatting3.setImageResource(R.drawable.icon_radio);
                ivRatting4.setImageResource(R.drawable.icon_radio_selected);
                ivRatting5.setImageResource(R.drawable.icon_radio);
                break;
            case R.id.ivRatting5:
                ratting="5";
                ivRatting1.setImageResource(R.drawable.icon_radio);
                ivRatting2.setImageResource(R.drawable.icon_radio);
                ivRatting3.setImageResource(R.drawable.icon_radio);
                ivRatting4.setImageResource(R.drawable.icon_radio);
                ivRatting5.setImageResource(R.drawable.icon_radio_selected);
                break;
            case R.id.tvClear:
                ratting="0";
                type="";
                ivRatting1.setImageResource(R.drawable.icon_radio);
                ivRatting2.setImageResource(R.drawable.icon_radio);
                ivRatting3.setImageResource(R.drawable.icon_radio);
                ivRatting4.setImageResource(R.drawable.icon_radio);
                ivRatting5.setImageResource(R.drawable.icon_radio);
                seekBar1.setProgress(0);
                tvBeach.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                tvHills.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                tvWildLife.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                tvAdventure.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                tvDistance.setText("");
                break;
            case R.id.tvApply:
                Filter(tvDistance.getText().toString(),ratting,type);

                break;


        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_bottom_in, R.anim.trans_bottom_out);
    }


    /**
     * Destination Details info .
     */
    public void Filter(String radius,String ratting,String Type) {
        showProgressDialog();
        Log.e("Filter", "Filter");
        try {
            prefsManager = new PrefsManager(FilterActivity.this);
            AccessToken = prefsManager.getToken();
            UserId = prefsManager.getCaseId();
            Log.e("AccessToken:", "" + AccessToken + "----UserId----" + UserId);

            RequestQueue requestQueue = Volley.newRequestQueue(FilterActivity.this);
            RequestJsonObject loginTaskRequest = new RequestJsonObject(Request.Method.POST,
                    ApplicationGlobal.ROOT + ApplicationGlobal.baseurl_ridingFilter + "userId=" + UserId +"&longitude=" + star_long + "&latitude=" + star_lat +"&radius="+radius+"&rating"+ratting+"&destType="+Type+"&accessToken=" + AccessToken, null,
                    volleyModelErrorListener(), volleyModelSuccessListener()
            );

            requestQueue.add(loginTaskRequest);

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    /**
     * Implement success listener on execute api url.
     */
    public Response.Listener<JSONObject> volleyModelSuccessListener() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("Model response:", "" + response);

                Type type = new TypeToken<RidingDestination>() {
                }.getType();
                RidingDestination mRidingDestination = new Gson().fromJson(response.toString(), type);

                DestinationsListActivity.mRidingDestinationDetailses.clear();

                dismissProgressDialog();
                if (mRidingDestination.getSuccess().equals("1")) {
                    filter=true;
                    prefsManager=new PrefsManager(FilterActivity.this);
                    prefsManager.setRadius(tvDistance.getText().toString().trim());
                    DestinationsListActivity.mRidingDestinationDetailses.addAll(mRidingDestination.getData());
                    finish();
                    //rvDestinations.setAdapter(new AdapterDestination(DestinationsListActivity.this, mRidingDestinationDetailses));
                }

            }
        };
    }

    /**
     * Implement Volley error listener here.
     */
    public Response.ErrorListener volleyModelErrorListener() {
        dismissProgressDialog();
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error: ", "" + error);
            }
        };
    }

    public void showProgressDialog() {

        mCustomizeDialog = new CustomizeDialog(FilterActivity.this);
        mCustomizeDialog.setCancelable(false);
        mCustomizeDialog.show();
        Log.e("HERE", "HERE");
    }

    public void dismissProgressDialog() {
        if (mCustomizeDialog != null && mCustomizeDialog.isShowing()) {
            mCustomizeDialog.dismiss();
            mCustomizeDialog = null;
        }
        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mCustomizeDialog != null && mCustomizeDialog.isShowing()) {
                    mCustomizeDialog.dismiss();
                    mCustomizeDialog = null;
                }
            }
        }, 1000);
*/
    }

}
