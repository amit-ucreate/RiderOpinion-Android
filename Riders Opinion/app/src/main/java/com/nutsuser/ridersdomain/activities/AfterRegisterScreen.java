package com.nutsuser.ridersdomain.activities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.utils.ApplicationGlobal;
import com.nutsuser.ridersdomain.view.BetterPopupWindow;
import com.nutsuser.ridersdomain.web.api.volley.RequestJsonObject;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by user on 12/28/2015.
 */
public class AfterRegisterScreen extends BaseActivity {

    private Activity activity;
    @Bind(R.id.tvTagRiders)
    TextView tvTagRiders;
    @Bind(R.id.tvTagOpinion)
    TextView tvTagOpinion;
    @Bind(R.id.edPhoneNo)
    EditText edTagPhoneNo;
    @Bind(R.id.edVehiclesOwned)
    EditText edVehiclesOwned;
    DemoPopupWindow dw;
    ArrayList<String> options=new ArrayList<>();
View v;
    CustomBaseAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afterregister);
        activity = AfterRegisterScreen.this;
        ButterKnife.bind(activity);
        setFontsToViews();

        edVehiclesOwned=(EditText)findViewById(R.id.edVehiclesOwned);
        options=new ArrayList<String>();
        options.add("VEHICLE OWNED");


        edVehiclesOwned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                vechicleinfo();

            }
        });




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
                Log.e("response: ", "" + response);
                options.clear();
                dw = new DemoPopupWindow(v, AfterRegisterScreen.this);
                dw.showLikeQuickAction(0, 0);
                options.add("TEST");
                options.add("TEST1");
                options.add("TEST2");
                //adapter.notifyDataSetChanged();
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
    //http://ridersopininon.herokuapp.com/index.php/riders/vehicle
    /**
     * Register info .
     */
    public void vechicleinfo() {

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
            adapter=new CustomBaseAdapter(AfterRegisterScreen.this);
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

        public CustomBaseAdapter(Context context) {
            this.context = context;

        }


        private class ViewHolder {

            TextView txtTitle;

        }

        public View getView(int position, View convertView, ViewGroup parent) {
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




            holder.txtTitle.setText(options.get(position));


            return convertView;
        }
        @Override
        public int getCount() {
            return options.size();
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

