package com.nutsuser.ridersdomain.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.adapter.AdapterRide;
import com.nutsuser.ridersdomain.adapter.PlanRidePostAdapter;
import com.nutsuser.ridersdomain.adapter.PlanRidePostAdapterAssistance;
import com.nutsuser.ridersdomain.utils.ApplicationGlobal;
import com.nutsuser.ridersdomain.utils.CustomizeDialog;
import com.nutsuser.ridersdomain.utils.PrefsManager;
import com.nutsuser.ridersdomain.web.api.volley.RequestJsonObject;
import com.nutsuser.ridersdomain.web.api.volley.RequestJsonObjectClient;
import com.nutsuser.ridersdomain.web.pojos.PlanARide;
import com.nutsuser.ridersdomain.web.pojos.PostPlanARide;
import com.nutsuser.ridersdomain.web.pojos.PostPlanARideData;
import com.nutsuser.ridersdomain.web.pojos.PostRide;
import com.nutsuser.ridersdomain.web.pojos.ProductMultipleSelect;
import com.nutsuser.ridersdomain.web.pojos.ProductMultipleSelectAssistance;
import com.nutsuser.ridersdomain.web.pojos.VehicleDetails;
import com.nutsuser.ridersdomain.web.pojos.VehicleName;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by user on 1/6/2016.
 */
public class PlanARidePostRide extends BaseActivity {
    PostPlanARideData postPlanARideData;
    CustomizeDialog mCustomizeDialog;
    String AccessToken, UserId;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tvTitleToolbar)
    TextView tvTitleToolbar;
    @Bind(R.id.listView)
    ListView listView;
    @Bind(R.id.list_View)
    ListView list_View;
    @Bind(R.id.tvPlaces)
    TextView tvPlaces;
    @Bind(R.id.tvDateAndTime)
    TextView tvDateAndTime;
    @Bind(R.id.eddescrption)
    EditText eddescrption;
@Bind(R.id.tvDistance)
TextView tvDistance;
    @Bind(R.id.seekBar1)
    SeekBar seekBar1;

    ArrayList<ProductMultipleSelect> objects;
    PlanRidePostAdapter mTaskDetailsAdapter;
    PlanRidePostAdapterAssistance planRidePostAdapterAssistance;
    private ArrayList<VehicleDetails> mVehicleDetailses = new ArrayList<VehicleDetails>();
    private ArrayList<ProductMultipleSelectAssistance> productMultipleSelectAssistances = new ArrayList<ProductMultipleSelectAssistance>();
    public ArrayList<String> arrayList;
    public ArrayList<String> arrayList1;
    public ArrayList<String> arrayListCheckAssistance;

    private Activity activity;
    String ridetype,startdate,enddate,starttime,fromlatitude,fromlongitude,fromlocation,tolatitude,tolongitude,tolocation,hlatitude,hlongitude,htype,hlocation,hlatitude1,hlongitude1,htype1,hlocation1;

    Map<String, String> params;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planaridepostride);
        activity = this;
        ButterKnife.bind(this);
        setupActionBar();
        setFonts();
        objects=new ArrayList<>();
        arrayList=new ArrayList<>();
        arrayList1=new ArrayList<>();
        arrayListCheckAssistance=new ArrayList<>();
        arrayList1.add("PLAN THIS RIDE");
        arrayList1.add("HOTEL/RESTAURANT BOOKING");
        for (int i = 0; i <arrayList1.size(); i++){
            Log.e("NAME:",""+arrayList1.get(i));
            productMultipleSelectAssistances.add(new ProductMultipleSelectAssistance(false,arrayList1.get(i)));
        }
        ridetype=getIntent().getStringExtra("rideType");
        if(ridetype.matches("breakfast")){
            ridetype="breakfast";
            startdate=getIntent().getStringExtra("startdate");
            starttime=getIntent().getStringExtra("starttime");
            fromlatitude=getIntent().getStringExtra("fromlatitude");
            fromlongitude=getIntent().getStringExtra("fromlongitude");
            fromlocation=getIntent().getStringExtra("fromlocation");
            tolatitude=getIntent().getStringExtra("tolatitude");
            tolongitude=getIntent().getStringExtra("tolongitude");
            tolocation=getIntent().getStringExtra("tolocation");
            hlatitude=getIntent().getStringExtra("hlatitude");
            hlongitude=getIntent().getStringExtra("hlongitude");
            hlocation=getIntent().getStringExtra("hlocation");
            htype=getIntent().getStringExtra("htype");
        }
        else{
            ridetype="overnight";
            startdate=getIntent().getStringExtra("startdate");
            starttime=getIntent().getStringExtra("starttime");
            fromlatitude=getIntent().getStringExtra("fromlatitude");
            fromlongitude=getIntent().getStringExtra("fromlongitude");
            fromlocation=getIntent().getStringExtra("fromlocation");
            enddate=getIntent().getStringExtra("enddate");
            tolatitude=getIntent().getStringExtra("tolatitude");
            tolongitude=getIntent().getStringExtra("tolongitude");
            tolocation=getIntent().getStringExtra("tolocation");
            hlatitude=getIntent().getStringExtra("hlatitude");
            hlongitude=getIntent().getStringExtra("hlongitude");
            hlocation=getIntent().getStringExtra("hlocation");
            htype=getIntent().getStringExtra("htype");
            hlatitude1=getIntent().getStringExtra("hlatitude1");
            hlongitude1=getIntent().getStringExtra("hlongitude1");
            hlocation1=getIntent().getStringExtra("hlocation1");
            htype1=getIntent().getStringExtra("htype1");

        }
        tvPlaces.setText(fromlocation + "  -  " + tolocation);
        tvDateAndTime.setText(startdate + "/" + starttime);

        seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            int progress = 0;


            @Override

            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {

                progress = progresValue;
                //tvDistance.setText("Please Wait..");
                //Toast.makeText(getApplicationContext(), "Changing seekbar's progress: "+progress, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Toast.makeText(getApplicationContext(), "Started tracking seekbar:"+seekBar.getProgress(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                tvDistance.setText("" + seekBar.getProgress());
                //textView.setText("Covered: " + progress + "/" + seekBar.getMax());
                //Toast.makeText(getApplicationContext(), "Stopped tracking seekbar+_"+seekBar.getProgress(), Toast.LENGTH_SHORT).show();

            }

        });
        vechicleinfo();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
              hideKeyboard();

            }
        }, 1000);
    }
    @OnClick({R.id.tvSubmit})
    void click(View view) {
        switch (view.getId()) {

            case R.id.tvSubmit:
                arrayList.clear();
                for (ProductMultipleSelect p : mTaskDetailsAdapter.getBox()) {
                    if (p.box){
                        arrayList.add(p.bikename);
                    }
                }
                arrayListCheckAssistance.clear();
                for (ProductMultipleSelectAssistance p : planRidePostAdapterAssistance.getBox()) {
                    if (p.box){
                        arrayListCheckAssistance.add(p._name);
                    }
                }
               // showToast("" + arrayList + "--------" + arrayListCheckAssistance);
              //  Post();
                saveInfoToParams();
                break;

        }
    }


    private void setupActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_back);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void setFonts() {
        tvTitleToolbar.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
        //tvName.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
        //tvAddress.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
        //tvDestinations.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
        // tvEvents.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
        //tvModifyBike.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
        // tvMeetAndPlanRide.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
        // tvHealthyRiding.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
        // tvGetDirections.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
        // tvNotifications.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
        // tvSettings.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * Register info .
     */
    public void vechicleinfo() {
        showProgressDialog();
        Log.e("See", "ENTER");
        try {
            //  Log.e("URL: ",""+ ApplicationGlobal.ROOT+ApplicationGlobal.baseurl_sigup+"utypeid="+utypeid+"&latitude="+latitude+"&longitude="+longitude+"&password="+password+"&deviceToken="+devicetoken+"&OS=Android");
            RequestQueue requestQueue = Volley.newRequestQueue(PlanARidePostRide.this);
            RequestJsonObject loginTaskRequest = new RequestJsonObject(Request.Method.POST,
                    ApplicationGlobal.ROOT + ApplicationGlobal.baseurl_vehicle, null,
                    volleyErrorListener(), volleySuccessListener()
            );

            requestQueue.add(loginTaskRequest);

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    /**
     * Implement success listener on execute api url.
     */
    public Response.Listener<JSONObject> volleySuccessListener() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dismissProgressDialog();

                Type type = new TypeToken<VehicleName>() {
                }.getType();
                VehicleName mVehicleName = new Gson().fromJson(response.toString(), type);
                mVehicleDetailses.clear();

                Log.e("response: ", "" + response);
                if (mVehicleName.getSuccess().equals("1")) {
                    mVehicleDetailses.addAll(mVehicleName.getData());
                    for (int i = 0; i <mVehicleDetailses.size(); i++){
                        objects.add(new ProductMultipleSelect(false,mVehicleDetailses.get(i).getVehicle_name()));
                    }
                    Log.e("Size:",""+objects.size());
                    mTaskDetailsAdapter = new PlanRidePostAdapter(PlanARidePostRide.this, mVehicleDetailses,objects);
                    listView.setAdapter(mTaskDetailsAdapter);
                    Log.e("productMultiple:", "" + productMultipleSelectAssistances.size());
                    planRidePostAdapterAssistance = new PlanRidePostAdapterAssistance(PlanARidePostRide.this, productMultipleSelectAssistances);
                    list_View.setAdapter(planRidePostAdapterAssistance);

                }


            }
        };
    }

    /**
     * Implement Volley error listener here.
     */
    public Response.ErrorListener volleyErrorListener() {
        // dismissProgressDialog();
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error: ", "" + error);
            }
        };
    }

    public void showProgressDialog() {

        mCustomizeDialog = new CustomizeDialog(PlanARidePostRide.this);
        mCustomizeDialog.setCancelable(false);
        mCustomizeDialog.show();
        Log.e("HERE", "HERE");
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
    private void hideKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    /**
     * Post.
     */
    public void saveInfoToParams() {
        showProgressDialog();
        prefsManager = new PrefsManager(PlanARidePostRide.this);
        AccessToken = prefsManager.getToken();
        UserId = prefsManager.getCaseId();
       Log.e("AccessToken:",""+AccessToken);
        params = new HashMap<String, String>();
        try {
            if(ridetype.matches("breakfast")){
                params.put("userId",UserId);
                params.put("rideType",ridetype);
                params.put("startDate",startdate);

                params.put("startTime",starttime);
                params.put("frmLatitude",fromlatitude);
                params.put("frmLongitude",fromlongitude);
                params.put("frmLocation",fromlocation);
                params.put("toLatitude",tolatitude);
                params.put("toLongitude",tolongitude);
                params.put("toLocation",tolocation);
                params.put("hLatitude",hlatitude);
                params.put("hLongitue",hlongitude);
                params.put("hLocation",hlocation);
                params.put("hType",htype);
                params.put("description",eddescrption.getText().toString());
                params.put("assistance",arrayListCheckAssistance.toString());
                params.put("whoCanJoin",arrayList.toString());
                params.put("distance",tvDistance.getText().toString());
                params.put("deviceType","android");
                params.put("accessToken",AccessToken);
            }
            else{
                 params.put("userId",UserId);
              params.put("rideType",ridetype);
                params.put("startDate",startdate);
                params.put("endDate",enddate);
                params.put("startTime",starttime);
                params.put("frmLatitude",fromlatitude);
                params.put("frmLongitude",fromlongitude);
                params.put("frmLocation",fromlocation);
                params.put("toLatitude",tolatitude);
                params.put("toLongitude",tolongitude);
                params.put("toLocation",tolocation);
                params.put("hLatitude",hlatitude);
                params.put("hLongitue",hlongitude);
                params.put("hLocation",hlocation);
                params.put("hType",htype);
                params.put("hLatitude1",hlatitude1);
                params.put("hLongitue1",hlongitude1);
                params.put("hLocation1",hlocation1);
                params.put("hType1",htype1);
                params.put("description",eddescrption.getText().toString());
                params.put("assistance",arrayListCheckAssistance.toString());
                params.put("whoCanJoin",arrayList.toString());
                params.put("distance",tvDistance.getText().toString());
                params.put("deviceType","android");
                params.put("accessToken",AccessToken);
            }
            Log.e("map:", "" + params);


        } catch (Exception e) {
            e.printStackTrace();
        }



        try {
            Log.e("URL:",""+ApplicationGlobal.ROOT + ApplicationGlobal.baseurl_postride);
            RequestQueue requestQueue = Volley.newRequestQueue(PlanARidePostRide.this);
            RequestJsonObjectClient loginTaskRequest = new RequestJsonObjectClient(Request.Method.POST,
                    ApplicationGlobal.ROOT + ApplicationGlobal.baseurl_postride, params,
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

                Type type = new TypeToken<PostPlanARide>() {
                }.getType();
                PostPlanARide planARide = new Gson().fromJson(response.toString(), type);
                if (planARide.getSuccess().equals("1")) {
                    dismissProgressDialog();
                    postPlanARideData=planARide.getData();
                    Intent mIntent=new Intent(PlanARidePostRide.this,PlanRideDetailActivity.class);
                    mIntent.putExtra("eventId",postPlanARideData.getEventId());
                    mIntent.putExtra("LOCATION","post");
                    startActivity(mIntent);
                }
                else if(planARide.getMessage().equals("Data Not Found.")){
                    dismissProgressDialog();
                    showToast("Data Not Found.");
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


}
