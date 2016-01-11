package com.nutsuser.ridersdomain.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.nutsuser.ridersdomain.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class RegisterActivity extends BaseActivity {

    private Activity activity;
    @Bind(R.id.tvTagRiders)
    TextView tvTagRiders;
    @Bind(R.id.tvTagOpinion)
    TextView tvTagOpinion;
    @Bind(R.id.edPhoneNo)
    EditText edTagPhoneNo;
  /*  @Bind(R.id.edEmailAddress)
    EditText edTagEmailAddress;
    @Bind(R.id.edBrandName)
    EditText edTagBrandName;
    @Bind(R.id.edModelName)
    EditText edTagModelName;*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        activity = RegisterActivity.this;
        ButterKnife.bind(activity);
        setFontsToViews();
    }

    private void setFontsToViews() {
        tvTagRiders.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT EXTRA LIGHT.TTF"));
        tvTagOpinion.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
    }

    @OnClick({R.id.tvExplore})
    void click(View view) {
        switch (view.getId()) {
            case R.id.tvExplore:
                startActivity(new Intent(RegisterActivity.this, AfterRegisterScreen.class));
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
