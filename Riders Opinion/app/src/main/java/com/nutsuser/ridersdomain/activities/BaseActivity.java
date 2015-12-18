package com.nutsuser.ridersdomain.activities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.utils.PrefsManager;

/**
 * Created by nutsuser on 8/6/2015.
 */
public class BaseActivity extends AppCompatActivity {

    public PrefsManager prefsManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefsManager = new PrefsManager(this);
        Fresco.initialize(this);
    }

    protected void showToast(String message) {

        Toast.makeText(BaseActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * @return screen size int[width, height]
     */
    public int[] getScreenSize() {
        Point size = new Point();
        WindowManager w = getWindowManager();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            w.getDefaultDisplay().getSize(size);
            return new int[]{size.x, size.y};
        } else {
            Display d = w.getDefaultDisplay();
            //noinspection deprecation
            return new int[]{d.getWidth(), d.getHeight()};
        }
    }
    public void settheme(){
        setTheme(R.style.Theme_trans);
        Log.e("BASE ","THEME CALL");
    }
}
