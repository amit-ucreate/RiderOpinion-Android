package com.nutsuser.ridersdomain.fragments;

import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.utils.ApplicationGlobal;
import com.nutsuser.ridersdomain.utils.CONSTANTS;
import com.nutsuser.ridersdomain.utils.CustomizeDialog;
import com.nutsuser.ridersdomain.utils.PrefsManager;

/**
 * Created by ucreateuser on 5/20/2016.
 */
public class BaseFragment extends Fragment implements CONSTANTS{
    public PrefsManager prefsManager;
    public CustomizeDialog mCustomizeDialog;
    public Typeface typeFaceMACHINEN,typeFaceLatoHeavy;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefsManager = new PrefsManager(getActivity());
        Fresco.initialize(getActivity());
        typeFaceMACHINEN =Typeface.createFromAsset(getResources().getAssets(), "fonts/MACHINEN.TTF");
        typeFaceLatoHeavy= Typeface.createFromAsset(getResources().getAssets(), "fonts/Lato-Heavy.ttf");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        prefsManager = new PrefsManager(getActivity());
        Fresco.initialize(getActivity());
        typeFaceMACHINEN =Typeface.createFromAsset(getResources().getAssets(), "fonts/MACHINEN.TTF");
        typeFaceLatoHeavy= Typeface.createFromAsset(getResources().getAssets(), "fonts/Lato-Heavy.ttf");

        return super.onCreateView(inflater, container, savedInstanceState);
    }


    public void showProgressDialog() {
        mCustomizeDialog = new CustomizeDialog(getActivity());
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
    protected void showToast(String message) {

        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
    /**
     * @return screen size int[width, height]
     */
    public int[] getScreenSize() {
        Point size = new Point();
        WindowManager w = getActivity().getWindowManager();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            w.getDefaultDisplay().getSize(size);
            return new int[]{size.x, size.y};
        } else {
            Display d = w.getDefaultDisplay();
            //noinspection deprecation
            return new int[]{d.getWidth(), d.getHeight()};
        }
    }

    public void settheme() {
        getActivity().setTheme(R.style.Theme_trans);
        Log.e("BASE ", "THEME CALL");
    }

    public boolean isNetworkConnected() {
        if (ApplicationGlobal.isNetworkConnected(getActivity()))
            return true;
        else
            return false;
    }
}
