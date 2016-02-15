package com.nutsuser.ridersdomain.activities;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.nutsuser.ridersdomain.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by user on 10/5/2015.
 */
public class ModifierAddressActivity extends BaseActivity {

    @Bind(R.id.ivcross)
    ImageView ivcross;
    @Bind(R.id.tvLabelAddress)
    TextView tvLabelAddress;
    @Bind(R.id.tvModifierAddress)
    TextView tvModifierAddress;
    @Bind(R.id.tvLabelPhone)
    TextView tvLabelPhone;
    @Bind(R.id.tvPhone)
    TextView tvPhone;
    @Bind(R.id.tvLabelTiming)
    TextView tvLabelTiming;
    @Bind(R.id.tvTiming)
    TextView tvTiming;
    @Bind(R.id.tvModifierName)
    TextView tvModifierName;
    @Bind(R.id.tvReviews)
    TextView tvReviews;
    private Activity activity;
    /* @Bind(R.id.toolbar)
     Toolbar toolbar;
     @Bind(R.id.tvTitleToolbar)
     TextView tvTitleToolbar;*/
    // Google Map
    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //settheme();
        setContentView(R.layout.activity_modifier_address);
        // getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.black_transparent)));
        activity = this;
        ButterKnife.bind(this);
        //overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
        // setupActionBar();
        setFonts();
        tvReviews.setText("102,678 ewviews & opinions");
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            // Loading map
            initializeMap();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setFonts() {
        Typeface typefaceNormal = Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF");
        //   tvTitleToolbar.setTypeface(typefaceNormal);
        tvReviews.setTypeface(typefaceNormal);
        tvLabelAddress.setTypeface(typefaceNormal);
        tvModifierName.setTypeface(typefaceNormal);
        tvModifierAddress.setTypeface(typefaceNormal);
        tvLabelPhone.setTypeface(typefaceNormal);
        tvPhone.setTypeface(typefaceNormal);
        tvLabelTiming.setTypeface(typefaceNormal);
        tvTiming.setTypeface(typefaceNormal);
    }

    private void initializeMap() {
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getMap();

            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            } else
                googleMap.setMyLocationEnabled(true);
        }
    }

    private void setupActionBar() {
        //  setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_back);
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
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.ivcross})
    void click(View view) {
        switch (view.getId()) {
            case R.id.ivcross:
                finish();
                //startActivity(new Intent(activity, ModifierAddressActivity.class));
                break;

        }
    }
}
