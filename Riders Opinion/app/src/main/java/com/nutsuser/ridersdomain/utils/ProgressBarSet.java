package com.nutsuser.ridersdomain.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import com.nutsuser.ridersdomain.R;

public class ProgressBarSet {
    static ProgressDialog pDialog;

    public static void showProgressDialog(Context context) {
        if (pDialog == null) {
            pDialog = new ProgressDialog(context);
            pDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            pDialog.setContentView(R.layout.progress_dialog);
            pDialog.setMessage("Please Wait...");
            // pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            pDialog.setCancelable(false);
            pDialog.show();

        }
    }

    public static void hideProgressDialog() {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

    // Class util funcitons
    public static boolean hideKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        return imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }

}
