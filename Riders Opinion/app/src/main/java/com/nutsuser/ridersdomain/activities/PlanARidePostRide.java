package com.nutsuser.ridersdomain.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.inscripts.cometchat.sdk.CometChatroom;
import com.inscripts.enums.ChatroomType;
import com.inscripts.interfaces.Callbacks;
import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.adapter.PlanRidePostAdapter;
import com.nutsuser.ridersdomain.adapter.PlanRidePostAdapterAssistance;
import com.nutsuser.ridersdomain.utils.ApplicationGlobal;
import com.nutsuser.ridersdomain.utils.CustomizeDialog;
import com.nutsuser.ridersdomain.utils.PrefsManager;
import com.nutsuser.ridersdomain.view.CustomDialog;
import com.nutsuser.ridersdomain.web.api.FileUploadService;
import com.nutsuser.ridersdomain.web.api.ServiceGenerator;
import com.nutsuser.ridersdomain.web.api.volley.RequestJsonObject;
import com.nutsuser.ridersdomain.web.api.volley.RequestJsonObjectClient;
import com.nutsuser.ridersdomain.web.pojos.PostPlanARide;
import com.nutsuser.ridersdomain.web.pojos.PostPlanARideData;
import com.nutsuser.ridersdomain.web.pojos.ProductMultipleSelect;
import com.nutsuser.ridersdomain.web.pojos.ProductMultipleSelectAssistance;
import com.nutsuser.ridersdomain.web.pojos.ProfileName;
import com.nutsuser.ridersdomain.web.pojos.VehicleDetails;
import com.nutsuser.ridersdomain.web.pojos.VehicleName;
import com.rollbar.android.Rollbar;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;

/**
 * Created by user on 1/6/2016.
 */
public class PlanARidePostRide extends BaseActivity {
    public ArrayList<String> arrayList;
    public ArrayList<String> arrayList1;
    public ArrayList<String> arrayListCheckAssistance;
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
    @Bind(R.id.tvSubmit)
    TextView tvSubmit;
    @Bind(R.id.seekBar1)
    SeekBar seekBar1;
    ArrayList<ProductMultipleSelect> objects;
    PlanRidePostAdapter mTaskDetailsAdapter;
    PlanRidePostAdapterAssistance planRidePostAdapterAssistance;
    String ridetype, startdate, enddate, endtime, starttime, fromlatitude, fromlongitude, fromlocation, tolatitude, tolongitude, tolocation, hlatitude, hlongitude, htype, hlocation, hlatitude1, hlongitude1, htype1, hlocation1;
    Map<String, String> params;
    private ArrayList<VehicleDetails> mVehicleDetailses = new ArrayList<VehicleDetails>();
    private ArrayList<ProductMultipleSelectAssistance> productMultipleSelectAssistances = new ArrayList<ProductMultipleSelectAssistance>();
    private Activity activity;
   // private Dialog dialog;

    String ditnaceInMiles="500";
    private static CometChatroom cometChatroom;
    int submitCount=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planaridepostride);
        try {
            activity = this;
            ButterKnife.bind(this);
            setupActionBar(toolbar);
            setFonts();
            cometChatroom = CometChatroom.getInstance(getApplicationContext());
            objects = new ArrayList<>();
            arrayList = new ArrayList<>();
            arrayList1 = new ArrayList<>();
            arrayListCheckAssistance = new ArrayList<>();
            arrayList1.add("PLAN THIS RIDE");
            arrayList1.add("HOTEL/RESTAURANT BOOKING");
            for (int i = 0; i < arrayList1.size(); i++) {
                Log.e("NAME:", "" + arrayList1.get(i));
                productMultipleSelectAssistances.add(new ProductMultipleSelectAssistance(false, arrayList1.get(i)));
            }
            ridetype = getIntent().getStringExtra("rideType");
            if (ridetype.matches("breakfast")) {
                ridetype = "breakfast";
                startdate = getIntent().getStringExtra("startdate");
                starttime = getIntent().getStringExtra("starttime");
                fromlatitude = getIntent().getStringExtra("fromlatitude");
                fromlongitude = getIntent().getStringExtra("fromlongitude");
                fromlocation = getIntent().getStringExtra("fromlocation");
                tolatitude = getIntent().getStringExtra("tolatitude");
                tolongitude = getIntent().getStringExtra("tolongitude");
                tolocation = getIntent().getStringExtra("tolocation");
                hlatitude = getIntent().getStringExtra("hlatitude");
                hlongitude = getIntent().getStringExtra("hlongitude");
                hlocation = getIntent().getStringExtra("hlocation");
                htype = getIntent().getStringExtra("htype");
            } else {
                ridetype = "overnight";
                startdate = getIntent().getStringExtra("startdate");
                starttime = getIntent().getStringExtra("starttime");
                fromlatitude = getIntent().getStringExtra("fromlatitude");
                fromlongitude = getIntent().getStringExtra("fromlongitude");
                fromlocation = getIntent().getStringExtra("fromlocation");
                enddate = getIntent().getStringExtra("enddate");
                endtime = getIntent().getStringExtra("endtime");
                tolatitude = getIntent().getStringExtra("tolatitude");
                tolongitude = getIntent().getStringExtra("tolongitude");
                tolocation = getIntent().getStringExtra("tolocation");
                hlatitude = getIntent().getStringExtra("hlatitude");
                hlongitude = getIntent().getStringExtra("hlongitude");
                hlocation = getIntent().getStringExtra("hlocation");
                htype = getIntent().getStringExtra("htype");
                hlatitude1 = getIntent().getStringExtra("hlatitude1");
                hlongitude1 = getIntent().getStringExtra("hlongitude1");
                hlocation1 = getIntent().getStringExtra("hlocation1");
                htype1 = getIntent().getStringExtra("htype1");
                Log.e("endtime:", "" + endtime);
            }
            tvPlaces.setText(fromlocation + "  -  " + tolocation);
            String timein12Format = "";
            try {
                final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
                final Date dateObj = sdf.parse(starttime);
                timein12Format = new SimpleDateFormat("K:mm a").format(dateObj);
            } catch (final ParseException e) {
                e.printStackTrace();
            }
            tvDateAndTime.setText(startdate + "/" + timein12Format);


            if (prefsManager.getDistanceSettings() == null) {
                seekBar1.setProgress(500);
                if (prefsManager.getDistanceParameter().equalsIgnoreCase("M")) {
                    tvDistance.setText("500 M");
                    ditnaceInMiles = "500";
                } else {
                    tvDistance.setText(getDistanceKilometer(500) + " KM");
                }


            } else {
                try {
                    seekBar1.setProgress(Integer.parseInt(prefsManager.getDistanceSettings()));
                    if (prefsManager.getDistanceParameter().equalsIgnoreCase("M")) {
                        tvDistance.setText(prefsManager.getDistanceSettings() + " M");
                        ditnaceInMiles = prefsManager.getDistanceSettings();
                    } else {
                        tvDistance.setText(getDistanceKilometer(Double.parseDouble(prefsManager.getDistanceSettings())) + " KM");
                    }

                } catch (Exception e) {
                    seekBar1.setProgress(500);
                    if (prefsManager.getDistanceParameter().equalsIgnoreCase("M")) {
                        tvDistance.setText("500 M");
                        ditnaceInMiles = "500";
                    } else {
                        tvDistance.setText(getDistanceKilometer(500) + " KM");
                    }
                }
            }

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
                    Log.e("seekBar.getProgress()", "" + seekBar.getProgress());

                    if (prefsManager.getDistanceParameter().equalsIgnoreCase("M")) {
                        tvDistance.setText("" + seekBar.getProgress() + " M");
                        ditnaceInMiles = "" + seekBar.getProgress();
                    } else {
                        tvDistance.setText(getDistanceKilometer(seekBar.getProgress()) + " KM");
                    }

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
        }catch(Exception e){
            Rollbar.reportException(e, "minor", "PostRideActivity on create");
        }
    }

    @OnClick({R.id.tvSubmit})
    void click(View view) {
        switch (view.getId()) {

            case R.id.tvSubmit:
                if(submitCount==0){
                tvSubmit.setClickable(false);
                tvSubmit.setFocusable(false);
                tvSubmit.setEnabled(false);
                tvSubmit.setBackgroundColor(getResources().getColor(R.color.disable_light_yellow));
                arrayList.clear();
                for (ProductMultipleSelect p : mTaskDetailsAdapter.getBox()) {
                    if (p.box) {
                        arrayList.add(p.bikeid);
                    }
                }
                arrayListCheckAssistance.clear();
                for (ProductMultipleSelectAssistance p : planRidePostAdapterAssistance.getBox()) {
                    if (p.box) {
                        arrayListCheckAssistance.add(p._name);
                    }
                }
                // showToast("" + arrayList + "--------" + arrayListCheckAssistance);
                //  Post();
                boolean post = postfields(eddescrption.getText().toString(), arrayListCheckAssistance.size(), arrayList.size(), ditnaceInMiles);
                if (post) {
                    showProgressDialog();
                    submitCount++;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Log.e("click", " click submit");
                            saveInfoToParams();
                        }
                    }, 100);
                }

                }
                break;
        }
    }

    //======== progress dialog===========//
    public void showProgressDialog() {

        mCustomizeDialog = new CustomizeDialog(this);
        mCustomizeDialog.setCancelable(false);
        mCustomizeDialog.show();
        Log.e("HERE", "HERE");
    }

    public void dismissProgressDialog() {
        if (mCustomizeDialog != null && mCustomizeDialog.isShowing()) {
            mCustomizeDialog.dismiss();
            mCustomizeDialog = null;
        }
    }
    public boolean postfields(String desc, int assistance, int whocanjoin, String distance) {

       /* if (desc.equals("")) {
            showToast("Please fill description");
            return false;
        } else*/ /*if (assistance == 0) {
            showToast("Please select assistance");
            return false;
        } else*/
        if (whocanjoin == 0) {
            showToast("Please select whocanjoin");
            return false;
        } else if (distance.equals("")) {
            showToast("Please select distance");
            return false;
        }
        return true;
    }


    private void setFonts() {
        tvTitleToolbar.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/MACHINEN.TTF"));
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
                new android.support.v7.app.AlertDialog.Builder(PlanARidePostRide.this).setTitle("Message")
                        .setMessage("Are you sure you want to discard Ride Plan?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // mGoogleApiClient.disconnect();
                                finish();
                                dialog.dismiss();

                            }
                        }).show();
               // finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        new android.support.v7.app.AlertDialog.Builder(PlanARidePostRide.this).setTitle("Message")
                .setMessage("Are you sure you want to discard Ride Plan?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       // mGoogleApiClient.disconnect();
                        finish();
                        dialog.dismiss();

                    }
                }).show();
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
            Rollbar.reportException(e, "minor", "PostRideActivity vechicleinfo API");

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
                    for (int i = 0; i < mVehicleDetailses.size(); i++) {
                        objects.add(new ProductMultipleSelect(true, mVehicleDetailses.get(i).getVehicle_name(), mVehicleDetailses.get(i).getVehicle_id()));
                    }
                    Log.e("Size:", "" + objects.size());
                    mTaskDetailsAdapter = new PlanRidePostAdapter(PlanARidePostRide.this, mVehicleDetailses, objects);
                    listView.setAdapter(mTaskDetailsAdapter);
                    Log.e("productMultiple:", "" + productMultipleSelectAssistances.size());
                    planRidePostAdapterAssistance = new PlanRidePostAdapterAssistance(PlanARidePostRide.this, productMultipleSelectAssistances);
                    list_View.setAdapter(planRidePostAdapterAssistance);
                  //  prefsManager = new PrefsManager(PlanARidePostRide.this);

                    Log.e("UserName===============","============================"+prefsManager.getUserName());
                    if (TextUtils.isEmpty(prefsManager.getUserName())) {
                        showInputDialog();
                    }

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
                showToast("Server error.Please try again after some time.");
            }
        };
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

        //prefsManager = new PrefsManager(PlanARidePostRide.this);
        AccessToken = prefsManager.getToken();
        UserId = prefsManager.getCaseId();
        Log.e("AccessToken:", "" + AccessToken);
        params = new HashMap<String, String>();
        String arraystring, stringarrayListCheckAssistance;
        try {
            if (ridetype.matches("breakfast")) {
                if (TextUtils.isEmpty(hlocation)) {
                    hlocation = "";
                }
                if (TextUtils.isEmpty(hlatitude)) {
                    hlatitude = "";
                }
                if (TextUtils.isEmpty(hlongitude)) {
                    hlongitude = "";
                }
                if (eddescrption.getText().toString().equals("")) {
                    eddescrption.setText(" ");
                }
                arraystring = arrayList.toString().replace("[", "").replace("]", "");
                stringarrayListCheckAssistance = arrayListCheckAssistance.toString().replace("[", "").replace("]", "");
                Log.e("arraystring:", "" + arraystring);
                if (TextUtils.isEmpty(arraystring)) {
                    arraystring = "";
                }
                if (TextUtils.isEmpty(stringarrayListCheckAssistance)) {
                    stringarrayListCheckAssistance = "";
                }
                params.put("userId", UserId);
                params.put("rideType", ridetype);
                params.put("startDate", startdate);
                params.put("startTime", starttime);
                params.put("frmLatitude", fromlatitude);
                params.put("frmLongitude", fromlongitude);
                params.put("frmLocation", fromlocation);
                params.put("toLatitude", tolatitude);
                params.put("toLongitude", tolongitude);
                params.put("toLocation", tolocation);
                params.put("hLatitude", hlatitude);
                params.put("hLongitue", hlongitude);
                params.put("hLocation", hlocation);
                params.put("hType", htype);
                params.put("description", eddescrption.getText().toString());
                params.put("assistance", stringarrayListCheckAssistance);
                params.put("whoCanJoin", arraystring);
                params.put("distance", ditnaceInMiles);
                params.put("deviceType", "android");
                params.put("accessToken", AccessToken);
            } else {
                if (TextUtils.isEmpty(hlocation)) {
                    hlocation = "";
                }
                if (TextUtils.isEmpty(hlatitude)) {
                    hlatitude = "";
                }

                if (TextUtils.isEmpty(hlongitude)) {
                    hlongitude = "";
                }

                if (eddescrption.getText().toString().equals("")) {
                    eddescrption.setText(" ");
                }

                if (TextUtils.isEmpty(hlocation1)) {
                    hlocation1 = "";
                }

                if (TextUtils.isEmpty(hlatitude1)) {
                    hlatitude1 = "";
                }

                if (TextUtils.isEmpty(hlongitude1)) {
                    hlongitude1 = "";
                }

                arraystring = arrayList.toString().replace("[", "").replace("]", "");
                stringarrayListCheckAssistance = arrayListCheckAssistance.toString().replace("[", "").replace("]", "");
                Log.e("arraystring:", "" + arraystring);
                if (TextUtils.isEmpty(arraystring)) {
                    arraystring = "";
                }
                if (TextUtils.isEmpty(stringarrayListCheckAssistance)) {
                    stringarrayListCheckAssistance = "";
                }
                if (TextUtils.isEmpty(endtime)) {
                    endtime = "";
                }
                params.put("userId", UserId);
                params.put("rideType", ridetype);
                params.put("startDate", startdate);
                params.put("endDate", enddate);
                params.put("endTime", endtime);
                params.put("startTime", starttime);
                params.put("frmLatitude", fromlatitude);
                params.put("frmLongitude", fromlongitude);
                params.put("frmLocation", fromlocation);
                params.put("toLatitude", tolatitude);
                params.put("toLongitude", tolongitude);
                params.put("toLocation", tolocation);
                params.put("hLatitude", hlatitude);
                params.put("hLongitue", hlongitude);
                params.put("hLocation", hlocation);
                params.put("hType", htype);
                params.put("hLatitude1", hlatitude1);
                params.put("hLongitue1", hlongitude1);
                params.put("hLocation1", hlocation1);
                params.put("hType1", htype1);
                params.put("description", eddescrption.getText().toString());
                params.put("assistance", stringarrayListCheckAssistance);
                params.put("whoCanJoin", arraystring);
                params.put("distance", ditnaceInMiles);
                params.put("deviceType", "android");
                params.put("accessToken", AccessToken);
            }
            Log.e("map:", "" + params);


        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Exception:", "" + e);
            Rollbar.reportException(e, "minor", "PostRideActivity PostRide API");

        }

        //createChatRoom(tolocation);
        try {
            Log.e("URL:", "" + ApplicationGlobal.ROOT + ApplicationGlobal.baseurl_postride);
            RequestQueue requestQueue = Volley.newRequestQueue(PlanARidePostRide.this);
            RequestJsonObjectClient loginTaskRequest = new RequestJsonObjectClient(Request.Method.POST,
                    ApplicationGlobal.ROOT + ApplicationGlobal.baseurl_postride, params,
                    volleyModelErrorListener(), volleyModelSuccessListener()
            );

            requestQueue.add(loginTaskRequest);


        } catch (Exception e) {
            e.printStackTrace();
            Rollbar.reportException(e, "minor", "PostRideActivity PostRide API");

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
                    postPlanARideData = planARide.getData();
                    congsuccess(PlanARidePostRide.this,"Hurray ! You have successfully post your ride, Enjoy");


                } else if (planARide.getMessage().equals("Data Not Found.")) {
                    dismissProgressDialog();
                    showToast("Data Not Found.");
                }

            }

        };
    }
    public void congsuccess(Activity context,String message) {

        new android.support.v7.app.AlertDialog.Builder(context).setTitle("Congratulations!!")
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent mIntent = new Intent(PlanARidePostRide.this, PlanRideDetailActivity.class);
                        mIntent.putExtra("eventId", postPlanARideData.getEventId());
                        mIntent.putExtra("LOCATION", "post");
                        mIntent.putExtra("LocationSet", "NOTSEE");
                        startActivity(mIntent);
                        prefsManager.setEventId(postPlanARideData.getEventId());

                    }
                }).show();
        // custom dialog
//        dialog = new Dialog(context);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.custom_dialog_layout);
//        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//        lp.copyFrom(dialog.getWindow().getAttributes());
//        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        dialog.getWindow().setAttributes(lp);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        //  dialog.getWindow().
//
//        // set the custom dialog components - text, image and button
//        TextView textTitle = (TextView) dialog.findViewById(R.id.txtDialogHeader);
//        textTitle.setText("Congratulations");
//        TextView textMessage = (TextView) dialog.findViewById(R.id.txtDialogMessage);
//        textMessage.setText(message);
//        TextView txtDialogOk = (TextView) dialog.findViewById(R.id.txtDialogOk);
//
//        // if button is clicked, close the custom dialog
//        txtDialogOk.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//                Intent mIntent = new Intent(PlanARidePostRide.this, PlanRideDetailActivity.class);
//                mIntent.putExtra("eventId", postPlanARideData.getEventId());
//                mIntent.putExtra("LOCATION", "post");
//                mIntent.putExtra("LocationSet", "NOTSEE");
//                startActivity(mIntent);
//                prefsManager.setEventId(postPlanARideData.getEventId());
//            }
//        });
//        dialog.show();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (dialog != null) {
//            dialog.dismiss();
//            dialog = null;
//        }
    }

    /**
     * Implement Volley error listener here.
     */
    public Response.ErrorListener volleyModelErrorListener() {
        dismissProgressDialog();
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showToast("Server error.Please try again after some time.");
                Log.e("error: ", "" + error);
            }
        };
    }

    protected void showInputDialog() {

        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(PlanARidePostRide.this);
        View promptView = layoutInflater.inflate(R.layout.input_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PlanARidePostRide.this);
        alertDialogBuilder.setView(promptView);

        final EditText editText = (EditText) promptView.findViewById(R.id.edittext);
        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        if (TextUtils.isEmpty(editText.getText().toString())) {
                            showInputDialog();
                        } else {
                            profileNameUpdate(editText.getText().toString());
                        }

                    }
                });


        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    protected void profileNameUpdate(final String profilename) {
//        prefsManager = new PrefsManager(PlanARidePostRide.this);
        AccessToken = prefsManager.getToken();
        UserId = prefsManager.getCaseId();
        FileUploadService service = ServiceGenerator.createService(FileUploadService.BASE_URL);
        service.updateProfileName(UserId, profilename, AccessToken, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, retrofit.client.Response response) {
                Log.e("Upload", "success");
                Log.e("jsonObject:", "" + jsonObject.toString());

                Type type = new TypeToken<ProfileName>() {
                }.getType();
                ProfileName profileName = new Gson().fromJson(jsonObject.toString(), type);
                if (profileName.getSuccess() == 1) {
                    dismissProgressDialog();
                    prefsManager.setUserName(profilename);
                } else {
                    dismissProgressDialog();
                }


            }

            @Override
            public void failure(RetrofitError error) {
                dismissProgressDialog();
                //showToast("Error:" + error);
                Log.e("Upload", "error");
            }
        });

    }

//    private void createChatRoom(String destName){
//        cometChatroom.createChatroom(destName, "rider", ChatroomType.PUBLIC_CHATROOM, new Callbacks() {
//            @Override
//            public void successCallback(JSONObject response) {
//                Log.e("chat room",""+response);
//            }
//            @Override
//            public void failCallback(JSONObject response) {}
//        });
//    }
}
