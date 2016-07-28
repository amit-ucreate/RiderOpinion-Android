package com.nutsuser.ridersdomain.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.nutsuser.ridersdomain.R;
import com.rollbar.android.Rollbar;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by user on 9/2/2015.
 */
public class HealthyRidingActivity extends BaseActivity {

    @Bind(R.id.wvHealthTips)
    WebView wvHealthTips;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tvTitleToolbar)
    TextView tvTitleToolbar;
    @Bind(R.id.ivBack)
    ImageView ivBack;
    private Context mContext;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_healthy_riding);
        try {
            mContext = this;
            ButterKnife.bind(this);
            ivBack.setVisibility(View.GONE);
            setupActionBar(toolbar);
            setFonts();
            WebSettings webSettings = wvHealthTips.getSettings();
            webSettings.setJavaScriptEnabled(true);
            startWebView("http://ridersopinion.com/tips/");
        } catch (Exception e) {
            Rollbar.reportException(e, "minor", "HealthyRidingActivity  on create");
        }
        //wvHealthTips.loadUrl("http://ridersopinion.com/tips/");
    }

    private void startWebView(String url) {
        if (progressDialog == null) {
            // in standard case YourActivity.this
            progressDialog = new ProgressDialog(HealthyRidingActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }
        //Create new webview Client to show progress dialog
        //When opening a url or click on link

        wvHealthTips.setWebViewClient(new WebViewClient() {


            //If you will not use this method url links are opeen in new brower not in webview
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

//            @Override
//            public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                super.onPageStarted(view, url, favicon);
////                if (progressDialog == null) {
////                    // in standard case YourActivity.this
////                    progressDialog = new ProgressDialog(HealthyRidingActivity.this);
////                    progressDialog.setMessage("Loading...");
////                    progressDialog.show();
////                }
//            }


            //Show loader on url load
            public void onLoadResource(WebView view, String url) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (progressDialog!=null) {
                                progressDialog.dismiss();
                                progressDialog = null;
                            }
                        } catch (Exception exception) {
                            exception.printStackTrace();
                            Rollbar.reportException(exception, "minor", "HealthyRidingActivity  webview");
                        }
                    }
                }, 5000);

            }

            public void onPageFinished(WebView view, String url) {
                try {
                    if (progressDialog!=null) {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }

        });

        wvHealthTips.loadUrl(url);
    }

    private void setFonts() {
        tvTitleToolbar.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/MACHINEN.TTF"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(HealthyRidingActivity.this, MainScreenActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("MainScreenResponse", "OPEN");
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(HealthyRidingActivity.this, MainScreenActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("MainScreenResponse", "OPEN");
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (wvHealthTips.canGoBack()) {
                        wvHealthTips.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }
}
