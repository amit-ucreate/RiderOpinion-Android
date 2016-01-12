package com.nutsuser.ridersdomain.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.web.api.volley.RequestJsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class RegisterActivity extends BaseActivity {

    private Activity activity;
    @Bind(R.id.tvTagRiders)
    TextView tvTagRiders;
    @Bind(R.id.tvTagOpinion)
    TextView tvTagOpinion;
    @Bind(R.id.edPhoneNo)
    EditText edTagPhoneNo;
  /*  @Bind(R.id.edEmailAddress)
    EditText edTagEmailAddress;
    @Bind(R.id.edBrandName)
    EditText edTagBrandName;
    @Bind(R.id.edModelName)
    EditText edTagModelName;*/
  Map<String, String> params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        activity = RegisterActivity.this;
        ButterKnife.bind(activity);
        setFontsToViews();
    }

    private void setFontsToViews() {
        tvTagRiders.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT EXTRA LIGHT.TTF"));
        tvTagOpinion.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
    }

    @OnClick({R.id.tvExplore})
    void click(View view) {
        switch (view.getId()) {
            case R.id.tvExplore:
                startActivity(new Intent(RegisterActivity.this, AfterRegisterScreen.class));
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

            }
        };
    }

    /**
     * Store user info to params.
     */
    public void saveInfoToParams(String strlogintype) {
        params = new HashMap<String, String>();
       /* params.put("City", "");
        params.put("AccountId", "");
        params.put("DeviceOS", "");
        params.put("DeviceUDID", "9E0806EB-ECDC-472A-976F-40FF22ED128");
        params.put("Email", "");
        params.put("FirstName", "");
        params.put("LastName", "");
        params.put("LoginType", "");
        params.put("MobileNumber", "");
        params.put("TeamId", "1");
        params.put("TokenID", "");*/

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(RegisterActivity.this);
            RequestJsonObject loginTaskRequest = new RequestJsonObject(Request.Method.POST,
                   "URL", params,
                    volleyErrorListener(), volleySuccessListener()
            );

            requestQueue.add(loginTaskRequest);

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

}
