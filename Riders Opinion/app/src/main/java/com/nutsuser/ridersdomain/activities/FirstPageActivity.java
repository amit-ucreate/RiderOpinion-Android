package com.nutsuser.ridersdomain.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.web.api.volley.RequestJsonObject;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by user on 8/27/2015.
 */
public class FirstPageActivity extends BaseActivity {

    private Activity activity;
    @Bind(R.id.tvTagRiders)
    TextView tvTagRiders;
    @Bind(R.id.tvTagOpinion)
    TextView tvTagOpinion;

    @Bind(R.id.tv_TagRiders)
    TextView tv_TagRiders;
    @Bind(R.id.tv_TagOpinion)
    TextView tv_TagOpinion;
    Map<String, String> params;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);
        //getSupportActionBar().hide();
        activity = FirstPageActivity.this;
        ButterKnife.bind(activity);
        setFontsToViews();
        String text = "<font color=#D1622A>Divided</font> <font color=#000000>By Boundaries</font>";
        String text_ = "<font color=#D1622A>United</font> <font color=#000000>By Throttles</font>";
        tv_TagRiders.setText(Html.fromHtml(text));
        tv_TagOpinion.setText(Html.fromHtml(text_));
       // callApplyCompetitionService();

    }

    private void setFontsToViews() {
        tvTagRiders.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT EXTRA LIGHT.TTF"));
        tvTagOpinion.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
    }


    @OnClick(R.id.ivStart)
    void click(View view) {
        switch (view.getId()) {
            case R.id.ivStart:
                Intent intent=new Intent(activity,MainScreenActivity.class);
                intent.putExtra("MainScreen", "CLOSED");
                startActivity(intent);

                break;
        }
    }


    private void callApplyCompetitionService() {


        // showPleaseWait("Sending...");
        params = new HashMap<String, String>();

        params.put("User_Id", "214");
        params.put("CompetitionId", "164");
        params.put("FirstName", "Karan");
        params.put("LastName", "NASSA");
        params.put("AppName", getResources().getString(R.string.app_name));
        params.put("ContactNumber", "23457376");
        params.put("EmailAddress", "aa@gmail.com");

        Log.e("TAG_COMPETITIONS", "APPLY COMPETITIONS CALL");

        Log.e("params:", "" + params);
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(FirstPageActivity.this);
            RequestJsonObject loginTaskRequest = new RequestJsonObject(Request.Method.POST,
                    "http://www.heeroservice.ucreate.co.in/api/applycompetition", params,
                    createErrorListener(), createSuccessListener()
            );



            requestQueue.add(loginTaskRequest);

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public Response.ErrorListener createErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                Log.e("ERROR LISTENER", "STArt: " + volleyError);
                if (volleyError.networkResponse == null) {
                    if (volleyError.getClass().equals(TimeoutError.class)) {
                        // Show timeout error message
                        //hideProgress();

                    }
                } else {


                    //If any Volley error occurs, then hide progress.




                    Log.e("ERROR LISTENER", "createErrorListener: " + volleyError);

                }
            }
        };
    }

    /**
     * Implement success listener on execute api url.
     */
    public Response.Listener<JSONObject> createSuccessListener() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                // hideProgress();

                Log.e("Response:", response.toString());

            }
        };
    }
}
