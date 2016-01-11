package com.nutsuser.ridersdomain.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.nutsuser.ridersdomain.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by user on 1/5/2016.
 */
public class PublicProfileScreen extends BaseActivity {
    private Activity activity;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.public_profile_screen);
        activity = this;
        ButterKnife.bind(this);
        setupActionBar();
    }
    private void setupActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_back);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
}
