package com.nutsuser.ridersdomain.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.utils.PrefsManager;

import java.util.Calendar;

/**
 * Created by ucreateuser on 7/7/2016.
 */
public class SplashActivity extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 1000;
    private PrefsManager prefsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        prefsManager= new PrefsManager(SplashActivity.this); Calendar cal = Calendar.getInstance();


        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                if(prefsManager.isLoginDone()){
                        Intent intent = new Intent(SplashActivity.this, MainScreenActivity.class);
                        intent.putExtra("MainScreenResponse", "CLOSED");
                        startActivity(intent);
                        finish();
                }else{
                    Intent intent = new Intent(SplashActivity.this, FirstPageActivity.class);
                    startActivity(intent);
                    finish();

                }
            }
        }, SPLASH_TIME_OUT);
    }
}
