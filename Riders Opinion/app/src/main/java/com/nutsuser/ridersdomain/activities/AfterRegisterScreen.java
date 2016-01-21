package com.nutsuser.ridersdomain.activities;

import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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

import com.nutsuser.ridersdomain.utils.CustomizeDialog;
import com.nutsuser.ridersdomain.view.BetterPopupWindow;
import com.nutsuser.ridersdomain.web.api.volley.RequestJsonObject;
import com.nutsuser.ridersdomain.web.pojos.VehicleDetails;
import com.nutsuser.ridersdomain.web.pojos.VehicleModel;
import com.nutsuser.ridersdomain.web.pojos.VehicleModelDetails;
import com.nutsuser.ridersdomain.web.pojos.VehicleName;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by user on 12/28/2015.
 */
public class AfterRegisterScreen extends BaseActivity {

    private ArrayList<VehicleDetails> mVehicleDetailses = new ArrayList<VehicleDetails>();
    private ArrayList<VehicleModelDetails> vehicleModelDetailses = new ArrayList<VehicleModelDetails>();
    CustomizeDialog mCustomizeDialog;
    private Activity activity;
    @Bind(R.id.tvTagRiders)
    TextView tvTagRiders;
    @Bind(R.id.tvTagOpinion)
    TextView tvTagOpinion;
    @Bind(R.id.edPhoneNo)
    EditText edTagPhoneNo;
    @Bind(R.id.btVehiclesOwned)
    Button btVehiclesOwned;
    @Bind(R.id.btModel)
    Button btModel;
    DemoPopupWindow dw;
    ModelPopupWindow mdw;
String vehicleId=null;

View view;
    CustomBaseAdapter adapter;
    ModelCustomBaseAdapter ModelCustomBaseAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afterregister);
        activity = AfterRegisterScreen.this;
        ButterKnife.bind(activity);
        setFontsToViews();
        view=new View(this);

        btVehiclesOwned=(Button)findViewById(R.id.btVehiclesOwned);
        btModel=(Button)findViewById(R.id.btModel);

        btModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
if(vehicleId!=null){
    vechiclemodelinfo();
}
                else{
    Log.e("","FALSE");
}
            }
        });

        btVehiclesOwned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // hideKeyboard();
                vechicleinfo();

            }
        });




    }

    public  void showProgressDialog() {

        mCustomizeDialog = new CustomizeDialog(AfterRegisterScreen.this);
        mCustomizeDialog.setCancelable(false);
        mCustomizeDialog.show();
        Log.e("HERE", "HERE");
    }

    public  void dismissProgressDialog() {
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

    private void setFontsToViews() {
        tvTagRiders.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT EXTRA LIGHT.TTF"));
        tvTagOpinion.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
    }

    @OnClick({R.id.tvSubmit,R.id.tvSkip})
    void click(View view) {
        switch (view.getId()) {
            case R.id.tvSubmit:
                finish();
                break;
            case R.id.tvSkip:
                finish();
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
                if (mVehicleName.getSuccess().equals("1")){

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
        Log.e("See","ENTER");
        try {
          //  Log.e("URL: ",""+ ApplicationGlobal.ROOT+ApplicationGlobal.baseurl_sigup+"utypeid="+utypeid+"&latitude="+latitude+"&longitude="+longitude+"&password="+password+"&deviceToken="+devicetoken+"&OS=Android");
            RequestQueue requestQueue = Volley.newRequestQueue(AfterRegisterScreen.this);
            RequestJsonObject loginTaskRequest = new RequestJsonObject(Request.Method.POST,
                    "http://ridersopininon.herokuapp.com/index.php/riders/vehicle", null,
                    volleyErrorListener(), volleySuccessListener()
            );

            requestQueue.add(loginTaskRequest);

        } catch (Exception e) {
            e.printStackTrace();

        }
    }


    //http://ridersopininon.herokuapp.com/index.php/riders/vehicle
    /**
     * Register info .
     */
    public void vechiclemodelinfo() {
        showProgressDialog();
        Log.e("vechiclemodelinfo","vechiclemodelinfo");
        try {
            //  Log.e("URL: ",""+ ApplicationGlobal.ROOT+ApplicationGlobal.baseurl_sigup+"utypeid="+utypeid+"&latitude="+latitude+"&longitude="+longitude+"&password="+password+"&deviceToken="+devicetoken+"&OS=Android");
            RequestQueue requestQueue = Volley.newRequestQueue(AfterRegisterScreen.this);
            RequestJsonObject loginTaskRequest = new RequestJsonObject(Request.Method.POST,
                    "http://ridersopininon.herokuapp.com/index.php/riders/vehicleId?vehicleId="+vehicleId, null,
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
                dismissProgressDialog();
                Log.e("Model response:",""+response);

                Type type = new TypeToken<VehicleModel>() {
                }.getType();
                VehicleModel vehicleModel = new Gson().fromJson(response.toString(), type);

                vehicleModelDetailses.clear();


                if (vehicleModel.getSuccess().equals("1")){
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
         * @param anchor
         *            the anchor
         * @param cnt
         *            the cnt
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

            ListView listview=(ListView)root.findViewById(R.id.listview);
            adapter=new CustomBaseAdapter(AfterRegisterScreen.this,mVehicleDetailses);
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
        public CustomBaseAdapter(Context context,ArrayList<VehicleDetails> mVehicleDetailses) {
            this.mVehicleDetailses = mVehicleDetailses;
            this.context = context;

        }


        private class ViewHolder {

            TextView txtTitle;

        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            LayoutInflater mInflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.list_item, null);
                holder = new ViewHolder();

                holder.txtTitle = (TextView) convertView.findViewById(R.id.title);

                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }




            holder.txtTitle.setText(mVehicleDetailses.get(position).getVehicle_name());

convertView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        vehicleId=mVehicleDetailses.get(position).getVehicle_id();
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
         * @param anchor
         *            the anchor
         * @param cnt
         *            the cnt
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

            ListView listview=(ListView)root.findViewById(R.id.listview);
            ModelCustomBaseAdapter=new ModelCustomBaseAdapter(AfterRegisterScreen.this,vehicleModelDetailses);
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
        public ModelCustomBaseAdapter(Context context,ArrayList<VehicleModelDetails> vehicleModelDetailses) {
            this.vehicleModelDetailses = vehicleModelDetailses;
            this.context = context;

        }


        private class ViewHolder {

            TextView txtTitle;

        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            LayoutInflater mInflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.list_item, null);
                holder = new ViewHolder();

                holder.txtTitle = (TextView) convertView.findViewById(R.id.title);

                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }




            holder.txtTitle.setText(vehicleModelDetailses.get(position).getVehicleType());

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                 //   vehicleId=mVehicleDetailses.get(position).getVehicle_id();
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
    }
}

