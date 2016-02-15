package com.nutsuser.ridersdomain.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.nutsuser.ridersdomain.R;
import com.yahoo.mobile.client.android.util.rangeseekbar.RangeSeekBar;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by user on 9/29/2015.
 */
public class SettingsActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.llSeekBar)
    LinearLayout llSeekBar;
    RangeSeekBar<Integer> rsbDistance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        setupActionBar();
        setupSeekbar();
    }

    private void setupSeekbar() {
        rsbDistance = new RangeSeekBar<Integer>(this);
        rsbDistance.setRangeValues(0, 1500);
        rsbDistance.setSelectedMinValue(50);
        rsbDistance.setSelectedMaxValue(400);
        llSeekBar.addView(rsbDistance);
    }

    private void setupActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_home);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
