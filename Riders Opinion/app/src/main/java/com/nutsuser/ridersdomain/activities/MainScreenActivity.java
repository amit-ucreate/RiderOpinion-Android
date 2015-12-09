package com.nutsuser.ridersdomain.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.nutsuser.ridersdomain.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by user on 8/28/2015.
 */
public class MainScreenActivity extends BaseActivity {

    private Activity activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        activity = this;
        ButterKnife.bind(activity);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               if (!prefsManager.isLoginDone())
                    startActivityForResult(new Intent(activity, RegisterActivity.class), 1);

            }
        }, 300);
    }

    @OnClick({R.id.ivRidingDestination, R.id.ivPlanRide, R.id.ivRidingEvents, R.id.ivModifyBike, R.id.ivHealthyRides, R.id.ivSettings})
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
