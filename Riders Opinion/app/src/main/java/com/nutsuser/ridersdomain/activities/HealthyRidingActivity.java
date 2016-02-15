package com.nutsuser.ridersdomain.activities;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.nutsuser.ridersdomain.R;

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
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_healthy_riding);
        mContext = this;
        ButterKnife.bind(this);
        setupActionBar();
        setFonts();
        WebSettings webSettings = wvHealthTips.getSettings();
        webSettings.setJavaScriptEnabled(true);
        wvHealthTips.loadUrl("https://in.yahoo.com/");
    }

    private void setupActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_back);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void setFonts() {
        tvTitleToolbar.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
