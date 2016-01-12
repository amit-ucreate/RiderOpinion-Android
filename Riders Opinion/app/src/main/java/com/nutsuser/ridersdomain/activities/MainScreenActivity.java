package com.nutsuser.ridersdomain.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.nutsuser.ridersdomain.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by user on 8/28/2015.
 */
public class MainScreenActivity extends BaseActivity {

    private Activity activity;
    @Bind(R.id.tv_TagRiders)
    TextView tv_TagRiders;
    @Bind(R.id.tv_TagOpinion)
    TextView tv_TagOpinion;
    String mStringOpenClosed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        activity = this;
        ButterKnife.bind(activity);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               if (!prefsManager.isLoginDone()){
                   Intent intent = getIntent();
                   mStringOpenClosed= intent.getStringExtra("MainScreen");
                   if(mStringOpenClosed.matches("CLOSED")){
                       startActivityForResult(new Intent(activity, RegisterActivity.class), 1);
                   }

               }


            }
        }, 300);
        String text = "<font color=#D1622A>Divided</font> <font color=#000000>By Boundaries</font>";
        String text_ = "<font color=#D1622A>United</font> <font color=#000000>By Throttles</font>";
        tv_TagRiders.setText(Html.fromHtml(text));
        tv_TagOpinion.setText(Html.fromHtml(text_));
    }

    @OnClick({R.id.ivRidingDestination, R.id.ivPlanRide, R.id.ivRidingEvents, R.id.ivModifyBike, R.id.ivHealthyRides, R.id.ivSettings,R.id.ivGetDirections})
    void click(View view) {
        switch (view.getId()) {
            case R.id.ivRidingDestination:
                //if (!prefsManager.isLoginDone())
                  //  startActivityForResult(new Intent(activity, RegisterActivity.class), 1);
                startActivity(new Intent(MainScreenActivity.this, DestinationsListActivity.class));
                break;

            case R.id.ivPlanRide:
                //if (!prefsManager.isLoginDone())
                 //   startActivityForResult(new Intent(activity, RegisterActivity.class), 1);
                startActivity(new Intent(MainScreenActivity.this, PlanRideActivity.class));
                break;
            case R.id.ivGetDirections:
                //if (!prefsManager.isLoginDone())
                //   startActivityForResult(new Intent(activity, RegisterActivity.class), 1);
                startActivity(new Intent(MainScreenActivity.this, GetDirections.class));
                break;

            case R.id.ivRidingEvents:
                //if (!prefsManager.isLoginDone())
                  //  startActivityForResult(new Intent(activity, RegisterActivity.class), 1);
                startActivity(new Intent(MainScreenActivity.this, EventsListActivity.class));
                break;

            case R.id.ivModifyBike:
                //if (!prefsManager.isLoginDone())
                 //   startActivityForResult(new Intent(activity, RegisterActivity.class), 1);
                startActivity(new Intent(MainScreenActivity.this, ModifyBikeActivity.class));
                break;

            case R.id.ivHealthyRides:
               // if (!prefsManager.isLoginDone())
                //    startActivityForResult(new Intent(activity, RegisterActivity.class), 1);
                startActivity(new Intent(MainScreenActivity.this, HealthyRidingActivity.class));
                break;

            case R.id.ivSettings:
                //if (!prefsManager.isLoginDone())
                 //   startActivityForResult(new Intent(activity, RegisterActivity.class), 1);
                startActivity(new Intent(MainScreenActivity.this, SettingsActivity.class));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
            }
        }
    }
}
