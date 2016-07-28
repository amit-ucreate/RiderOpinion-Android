package com.nutsuser.ridersdomain.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.utils.ApplicationGlobal;
import com.nutsuser.ridersdomain.utils.CustomizeDialog;
import com.nutsuser.ridersdomain.utils.PrefsManager;
import com.nutsuser.ridersdomain.view.BetterPopupWindow;
import com.nutsuser.ridersdomain.web.api.volley.RequestJsonObject;
import com.nutsuser.ridersdomain.web.pojos.VehicleDetails;
import com.nutsuser.ridersdomain.web.pojos.VehicleModel;
import com.nutsuser.ridersdomain.web.pojos.VehicleModelDetails;
import com.nutsuser.ridersdomain.web.pojos.VehicleName;
import com.rollbar.android.Rollbar;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by user on 12/28/2015.
 */
public class AfterRegisterScreen extends BaseActivity {

    CustomizeDialog mCustomizeDialog;

    @Bind(R.id.edPhoneNo)
    EditText edTagPhoneNo;
    @Bind(R.id.btVehiclesOwned)
    Button btVehiclesOwned;
    @Bind(R.id.btModel)
    Button btModel;
    @Bind(R.id.btYear)
    Button btYear;
    @Bind(R.id.tvwelcome)
    TextView tvwelcome;
    @Bind(R.id.tvSkip)
    TextView tvSkip;
    @Bind(R.id.tvSubmit)
    TextView tvSubmit;
    @Bind(R.id.edEmail)
    EditText edEmail;
    DemoPopupWindow dw;
    ModelPopupWindow mdw;
    String vehicleId = null;
    View view;
    CustomBaseAdapter adapter;
    ModelCustomBaseAdapter ModelCustomBaseAdapter;
    PrefsManager prefsManager;
    private ArrayList<VehicleDetails> mVehicleDetailses = new ArrayList<VehicleDetails>();
    private ArrayList<VehicleModelDetails> vehicleModelDetailses = new ArrayList<VehicleModelDetails>();
    private Activity activity;
    @Bind(R.id.tv_TagRiders)
    TextView tv_TagRiders;
    @Bind(R.id.tv_TagOpinion)
    TextView tv_TagOpinion;
    String type;
    TimePopupWindow tw;
    TimeBaseAdapter mAdapter;
    ArrayList<Integer> arrayYear = new ArrayList<Integer>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afterregister);
        try {
            activity = AfterRegisterScreen.this;
            modelYears();
            ButterKnife.bind(activity);
            setFontsToViews();
            view = new View(this);
            prefsManager = new PrefsManager(this);
            btVehiclesOwned = (Button) findViewById(R.id.btVehiclesOwned);
            btModel = (Button) findViewById(R.id.btModel);
            type = getIntent().getStringExtra("typeEmailPhone");
            try {
                if (type.equals("phone")) {
                    edTagPhoneNo.setVisibility(View.GONE);
                    edEmail.setVisibility(View.VISIBLE);
                } else {
                    edTagPhoneNo.setVisibility(View.VISIBLE);
                    edEmail.setVisibility(View.GONE);
                }
            } catch (NullPointerException e) {

            }
            btModel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (vehicleId != null) {
                        if (isNetworkConnected()) {
                            vechiclemodelinfo();
                        } else {
                            showToast("Internet is not connected.");
                        }
                    } else {
                        Log.e("", "FALSE");
                    }
                }
            });

            btYear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tw = new TimePopupWindow(view, AfterRegisterScreen.this);
                    tw.showLikeQuickAction(0, 0);
                }
            });

            btVehiclesOwned.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // hideKeyboard();
                    if (isNetworkConnected()) {
                        vechicleinfo();
                    } else {
                        showToast("Internet is not connected.");
                    }
                }
            });
        }catch(Exception e){
            Rollbar.reportException(e, "minor", "AfterRegisterScreen on create");
        }

    }



    private void hideKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void setFontsToViews() {
        // tvTagRiders.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT EXTRA LIGHT.TTF"));
        // tvTagOpinion.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
        tvwelcome.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/LATO-REGULAR.TTF"));
        tvSkip.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/Lato-Heavy.ttf"));
        tvSkip.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/Lato-Heavy.ttf"));
        String text = "<font color=#D1622A>Divided</font> <font color=#000000>By Boundaries</font>";
        String text_ = "<font color=#D1622A>United</font> <font color=#000000>By Throttles</font>";
        tv_TagRiders.setText(Html.fromHtml(text));
        tv_TagOpinion.setText(Html.fromHtml(text_));
        tv_TagRiders.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/LATO-REGULAR.TTF"));
        tv_TagOpinion.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/LATO-REGULAR.TTF"));
    }

    @OnClick({R.id.tvSubmit, R.id.tvSkip})
    void click(View view) {
        switch (view.getId()) {
            case R.id.tvSubmit:
                if (vehicleId != null) {
                    try {
                        if (type.equals("phone")) {
                            edEmail.setVisibility(View.VISIBLE);
                            edTagPhoneNo.setVisibility(View.GONE);
                            if (edEmail.getText().toString().length() == 0) {
                            } else {
                                if (validEmail(edEmail.getText().toString())) {
                                    submitinfo(edEmail.getText().toString(), prefsManager.getCaseId(), vehicleId, prefsManager.getToken(), btYear.getText().toString());
                                }else{
                                    showToast("Please enter valid email.");
                                }
                            }
                        } else {
                            edTagPhoneNo.setVisibility(View.VISIBLE);
                            edEmail.setVisibility(View.GONE);
                            if (edTagPhoneNo.getText().toString().length() == 0) {
                            } else {
                                if (validPhone(edTagPhoneNo.getText().toString())) {
                                    submitinfo(edTagPhoneNo.getText().toString(), prefsManager.getCaseId(), vehicleId, prefsManager.getToken(), btYear.getText().toString());
                                }else{
                                    showToast("Please enter valid Phone Number.");
                                }
                            }
                        }
                    }catch(NullPointerException e){
                        Rollbar.reportException(e, "minor", "AfterRegisterScreen on submit ");
                    }

                }

                break;
            case R.id.tvSkip:
                prefsManager.setRadius("2000");
                if(ApplicationGlobal.FIRST_TAKE_TOUR){
                    ApplicationGlobal.TAKE_TOUR=true;
                    startActivity(new Intent(activity, HomeScreenTakeATour.class));
                    finish();
                }
                else{
                    ApplicationGlobal.TAKE_TOUR=false;
                    finish();
                }

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

                Type type = new TypeToken<VehicleName>() {
                }.getType();
                VehicleName mVehicleName = new Gson().fromJson(response.toString(), type);
                mVehicleDetailses.clear();

                Log.e("response: ", "" + response);
                if (mVehicleName.getSuccess().equals("1")) {

                    mVehicleDetailses.addAll(mVehicleName.getData());


                    dw = new DemoPopupWindow(view, AfterRegisterScreen.this);
                    dw.showLikeQuickAction(0, 0);

                }

                //adapter.notifyDataSetChanged();
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
    //http://ridersopininon.herokuapp.com/index.php/riders/vehicle

    /**
     * Register info .
     */
    public void vechicleinfo() {
        showProgressDialog();
        Log.e("See", "ENTER");
        try {
            //  Log.e("URL: ",""+ ApplicationGlobal.ROOT+ApplicationGlobal.baseurl_sigup+"utypeid="+utypeid+"&latitude="+latitude+"&longitude="+longitude+"&password="+password+"&deviceToken="+devicetoken+"&OS=Android");
            RequestQueue requestQueue = Volley.newRequestQueue(AfterRegisterScreen.this);
            RequestJsonObject loginTaskRequest = new RequestJsonObject(Request.Method.POST,
                    ApplicationGlobal.ROOT + ApplicationGlobal.baseurl_vehicle, null,
                    volleyErrorListener(), volleySuccessListener()
            );

            requestQueue.add(loginTaskRequest);

        } catch (Exception e) {
            e.printStackTrace();
            Rollbar.reportException(e, "minor", "AfterRegisterScreen getVechicleinfo API");

        }
    }

//

    /**
     * Register info .
     */
    public void  submitinfo(String id, String userid, String vehicle_id, String accesstoken,String vehicle_year) {
        showProgressDialog();
        Log.e("vechiclemodelinfo", "vechiclemodelinfo");
        try {

            Log.e("URL: ", "" + ApplicationGlobal.ROOT + ApplicationGlobal.baseurl_updateUserInfo + "userId=" + userid + "&utypeid=" + id + "&vehicleTypeId=" + vehicle_id +"&vehicle_year=" + vehicle_year + "&accessToken=" + accesstoken);
            RequestQueue requestQueue = Volley.newRequestQueue(AfterRegisterScreen.this);
            RequestJsonObject loginTaskRequest = new RequestJsonObject(Request.Method.GET,
                    ApplicationGlobal.ROOT + ApplicationGlobal.baseurl_updateUserInfo + "userId=" + userid + "&utypeid=" + id + "&vehicleTypeId=" + vehicle_id +"&vehicle_year=" + vehicle_year + "&accessToken=" + accesstoken, null,
                    volleySubmitErrorListener(), volleSubmitSuccessListener()
            );

            requestQueue.add(loginTaskRequest);

        } catch (Exception e) {
            e.printStackTrace();
            Rollbar.reportException(e, "minor", "AfterRegisterScreen submitinfo API");

        }
    }

    /**
     * Implement success listener on execute api url.
     */
    public Response.Listener<JSONObject> volleSubmitSuccessListener() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dismissProgressDialog();
                if(ApplicationGlobal.FIRST_TAKE_TOUR){
                    ApplicationGlobal.TAKE_TOUR=true;
                    ApplicationGlobal.FIRST_TAKE_TOUR=false;
                    startActivity(new Intent(activity, HomeScreenTakeATour.class));
                    finish();
                }
                else{
                    ApplicationGlobal.TAKE_TOUR=false;
                    finish();
                }
                Log.e("Model response:", "" + response);
                prefsManager.setVehicleId(vehicleId);
                prefsManager.setRadius("2000");
                finish();

            }
        };
    }

    /**
     * Implement Volley error listener here.
     */
    public Response.ErrorListener volleySubmitErrorListener() {
        dismissProgressDialog();
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error: ", "" + error);
            }
        };
    }


    //


    //http://ridersopininon.herokuapp.com/index.php/riders/vehicle

    /**
     * Register info .
     */
    public void vechiclemodelinfo() {
        showProgressDialog();
        Log.e("vechiclemodelinfo", "vechiclemodelinfo");
        try {
            //  Log.e("URL: ",""+ ApplicationGlobal.ROOT+ApplicationGlobal.baseurl_sigup+"utypeid="+utypeid+"&latitude="+latitude+"&longitude="+longitude+"&password="+password+"&deviceToken="+devicetoken+"&OS=Android");
            RequestQueue requestQueue = Volley.newRequestQueue(AfterRegisterScreen.this);
            RequestJsonObject loginTaskRequest = new RequestJsonObject(Request.Method.POST,
                    ApplicationGlobal.ROOT + ApplicationGlobal.baseurl_model + vehicleId, null,
                    volleyModelErrorListener(), volleyModelSuccessListener()
            );

            requestQueue.add(loginTaskRequest);

        } catch (Exception e) {
            e.printStackTrace();
            Rollbar.reportException(e, "minor", "AfterRegisterScreen vechiclemodelinfo API");


        }
    }

    /**
     * Implement success listener on execute api url.
     */
    public Response.Listener<JSONObject> volleyModelSuccessListener() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dismissProgressDialog();
                Log.e("Model response:", "" + response);

                Type type = new TypeToken<VehicleModel>() {
                }.getType();
                VehicleModel vehicleModel = new Gson().fromJson(response.toString(), type);

                vehicleModelDetailses.clear();


                if (vehicleModel.getSuccess().equals("1")) {
                    vehicleModelDetailses.addAll(vehicleModel.getData());
                    Log.e("vehicleModelDetailses: ", "" + vehicleModelDetailses.size());
                    mdw = new ModelPopupWindow(view, AfterRegisterScreen.this);
                    mdw.showLikeQuickAction(0, 0);

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
    // ************** Class for pop-up window **********************

    /**
     * The Class DemoPopupWindow.
     */
    private class DemoPopupWindow extends BetterPopupWindow {

        /**
         * Instantiates a new demo popup window.
         *
         * @param anchor the anchor
         * @param cnt    the cnt
         */
        public DemoPopupWindow(View anchor, Context cnt) {
            super(anchor);
        }

        /*
         * (non-Javadoc)
         *
         * @see com.cellalert24.Views.BetterPopupWindow#onCreate()
         */
        @Override
        protected void onCreate() {
            // inflate layout
            LayoutInflater inflater = (LayoutInflater) this.anchor.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            ViewGroup root = (ViewGroup) inflater.inflate(
                    R.layout.share_choose_popup, null);

            ListView listview = (ListView) root.findViewById(R.id.listview);
            adapter = new CustomBaseAdapter(AfterRegisterScreen.this, mVehicleDetailses);
            listview.setAdapter(adapter);
            Button mButton = (Button) root.findViewById(R.id.cancelBtn);


            mButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    // mFrameLayout.setVisibility(View.GONE);
                    // layout.setBackgroundColor(Color.WHITE);
                    dismiss();

                }
            });

            this.setContentView(root);
        }

    }

    public class CustomBaseAdapter extends BaseAdapter {
        Context context;
        private ArrayList<VehicleDetails> mVehicleDetailses;

        public CustomBaseAdapter(Context context, ArrayList<VehicleDetails> mVehicleDetailses) {
            this.mVehicleDetailses = mVehicleDetailses;
            this.context = context;

        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.list_item, null);
                holder = new ViewHolder();

                holder.txtTitle = (TextView) convertView.findViewById(R.id.title);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }


            holder.txtTitle.setText(mVehicleDetailses.get(position).getVehicle_name());

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vehicleId = mVehicleDetailses.get(position).getVehicle_id();

                    btVehiclesOwned.setText(mVehicleDetailses.get(position).getVehicle_name());
                    dw.dismiss();
                }
            });
            return convertView;
        }

        @Override
        public int getCount() {
            return mVehicleDetailses.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        private class ViewHolder {

            TextView txtTitle;

        }
    }


    //////
    // ************** Class for pop-up window **********************

    /**
     * The Class DemoPopupWindow.
     */
    private class ModelPopupWindow extends BetterPopupWindow {

        /**
         * Instantiates a new demo popup window.
         *
         * @param anchor the anchor
         * @param cnt    the cnt
         */
        public ModelPopupWindow(View anchor, Context cnt) {
            super(anchor);
        }

        /*
         * (non-Javadoc)
         *
         * @see com.cellalert24.Views.BetterPopupWindow#onCreate()
         */
        @Override
        protected void onCreate() {
            // inflate layout
            LayoutInflater inflater = (LayoutInflater) this.anchor.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            ViewGroup root = (ViewGroup) inflater.inflate(
                    R.layout.share_choose_popup, null);

            ListView listview = (ListView) root.findViewById(R.id.listview);
            ModelCustomBaseAdapter = new ModelCustomBaseAdapter(AfterRegisterScreen.this, vehicleModelDetailses);
            listview.setAdapter(ModelCustomBaseAdapter);
            Button mButton = (Button) root.findViewById(R.id.cancelBtn);


            mButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    // mFrameLayout.setVisibility(View.GONE);
                    // layout.setBackgroundColor(Color.WHITE);
                    dismiss();

                }
            });

            this.setContentView(root);
        }

    }

    public class ModelCustomBaseAdapter extends BaseAdapter {
        Context context;
        private ArrayList<VehicleModelDetails> vehicleModelDetailses;

        public ModelCustomBaseAdapter(Context context, ArrayList<VehicleModelDetails> vehicleModelDetailses) {
            this.vehicleModelDetailses = vehicleModelDetailses;
            this.context = context;

        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.list_item, null);
                holder = new ViewHolder();

                holder.txtTitle = (TextView) convertView.findViewById(R.id.title);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }


            holder.txtTitle.setText(vehicleModelDetailses.get(position).getVehicleType());

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vehicleId = vehicleModelDetailses.get(position).getVehicleTypeId();
                    btModel.setText(vehicleModelDetailses.get(position).getVehicleType());
                    mdw.dismiss();
                }
            });
            return convertView;
        }

        @Override
        public int getCount() {
            return vehicleModelDetailses.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        private class ViewHolder {

            TextView txtTitle;

        }
    }


    //============== model year adapter================//
    public  class TimeBaseAdapter extends BaseAdapter {

        Context context;
        private ArrayList<Integer> mVehicleDetailses;

        public TimeBaseAdapter(Context context, ArrayList<Integer> mVehicleDetailses) {
            this.mVehicleDetailses = mVehicleDetailses;
            this.context = context;

        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.list_item, null);
                holder = new ViewHolder();

                holder.txtTitle = (TextView) convertView.findViewById(R.id.title);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }


            holder.txtTitle.setText("" + mVehicleDetailses.get(position));

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    btYear.setText(""+mVehicleDetailses.get(position));

                    tw.dismiss();
//                    BikeId = vehicleId;
//                    strBikeYear = mVehicleDetailses.get(position).toString();
//                    showFilterDialog(strBikeName, strBikeYear, strBikeModel);
//                    pDialog.show();

                }
            });
            return convertView;
        }

        @Override
        public int getCount() {
            return mVehicleDetailses.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        private class ViewHolder {

            TextView txtTitle;

        }
    }



    // ************** Class for pop-up window **********************

    /**
     * The Class DemoPopupWindow.
     */
    private class TimePopupWindow extends BetterPopupWindow {

        /**
         * Instantiates a new demo popup window.
         *
         * @param anchor the anchor
         * @param cnt    the cnt
         */
        public TimePopupWindow(View anchor, Context cnt) {
            super(anchor);
        }

        /*
         * (non-Javadoc)
         *
         * @see com.cellalert24.Views.BetterPopupWindow#onCreate()
         */
        @Override
        protected void onCreate() {
            // inflate layout
            LayoutInflater inflater = (LayoutInflater) this.anchor.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            ViewGroup root = (ViewGroup) inflater.inflate(
                    R.layout.share_choose_popup, null);

            ListView listview = (ListView) root.findViewById(R.id.listview);
            mAdapter = new TimeBaseAdapter(AfterRegisterScreen.this, arrayYear);
            listview.setAdapter(mAdapter);
            Button mButton = (Button) root.findViewById(R.id.cancelBtn);


            mButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    // mFrameLayout.setVisibility(View.GONE);
                    // layout.setBackgroundColor(Color.WHITE);
                    dismiss();

                }
            });

            this.setContentView(root);
        }

    }
    //=========== year entry==========={
    private void modelYears(){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int lastYear=  year-1990;
        Log.e("last year",""+( year-1990));
        for(int i=lastYear;i>=0;i--){
            arrayYear.add((year-i));
        }
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
}

