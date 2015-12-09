package com.nutsuser.ridersdomain.activities;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nutsuser.ridersdomain.R;
import com.yahoo.mobile.client.android.util.rangeseekbar.RangeSeekBar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by user on 8/31/2015.
 */
public class FilterActivity extends BaseActivity {

    @Bind(R.id.tvCancel)
    TextView tvCancel;
    @Bind(R.id.tvTitleToolbar)
    TextView tvTitleToolbar;
    @Bind(R.id.tvClear)
    TextView tvClear;
    @Bind(R.id.tvLabelDistance)
    TextView tvLabelDistance;
    @Bind(R.id.tvLabelType)
    TextView tvLabelType;
    @Bind(R.id.tvBeach)
    TextView tvBeach;
    @Bind(R.id.tvHills)
    TextView tvHills;
    @Bind(R.id.tvWildLife)
    TextView tvWildLife;
    @Bind(R.id.tvAdventure)
    TextView tvAdventure;
    @Bind(R.id.tvLabelRating)
    TextView tvLabelRating;
    @Bind(R.id.tvApply)
    TextView tvApply;
    RangeSeekBar<Integer> rsbDistance;
    @Bind(R.id.llRangeSeekBar)
    LinearLayout llRangeSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        overridePendingTransition(R.anim.trans_top_in, R.anim.trans_top_out);
        ButterKnife.bind(this);
        setFontsToViews();
        setupSeekbar();
    }

    private void setupSeekbar() {
        rsbDistance = new RangeSeekBar<Integer>(this);
        rsbDistance.setRangeValues(0, 1500);
        rsbDistance.setSelectedMinValue(50);
        rsbDistance.setSelectedMaxValue(400);
        llRangeSeekBar.addView(rsbDistance);
    }

    private void setFontsToViews() {
        Typeface typefaceNormal = Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF");
        tvCancel.setTypeface(typefaceNormal);
        tvTitleToolbar.setTypeface(typefaceNormal);
        tvClear.setTypeface(typefaceNormal);
        tvLabelDistance.setTypeface(typefaceNormal);
        tvLabelType.setTypeface(typefaceNormal);
        tvBeach.setTypeface(typefaceNormal);
        tvHills.setTypeface(typefaceNormal);
        tvWildLife.setTypeface(typefaceNormal);
        tvAdventure.setTypeface(typefaceNormal);
        tvLabelRating.setTypeface(typefaceNormal);
        tvApply.setTypeface(typefaceNormal);
    }

    @OnClick({R.id.tvCancel})
    void click(View view) {
        switch (view.getId()) {
            case R.id.tvCancel:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_bottom_in, R.anim.trans_bottom_out);
    }
}
