package com.nutsuser.ridersdomain.activities;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.nutsuser.ridersdomain.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by user on 12/28/2015.
 */
public class AfterRegisterScreen extends BaseActivity {

    private Activity activity;
    @Bind(R.id.tvTagRiders)
    TextView tvTagRiders;
    @Bind(R.id.tvTagOpinion)
    TextView tvTagOpinion;
    @Bind(R.id.edPhoneNo)
    EditText edTagPhoneNo;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afterregister);
        activity = AfterRegisterScreen.this;
        ButterKnife.bind(activity);
        setFontsToViews();
    }

    private void setFontsToViews() {
        tvTagRiders.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT EXTRA LIGHT.TTF"));
        tvTagOpinion.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
    }

    @OnClick({R.id.tvSubmit,R.id.tvSkip})
    void click(View view) {
        switch (view.getId()) {
            case R.id.tvSubmit:
                finish();
                break;
            case R.id.tvSkip:
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}

