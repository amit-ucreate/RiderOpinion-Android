package com.nutsuser.ridersdomain.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.nutsuser.ridersdomain.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by user on 8/27/2015.
 */
public class FirstPageActivity extends BaseActivity {

    private Activity activity;
    @Bind(R.id.tvTagRiders)
    TextView tvTagRiders;
    @Bind(R.id.tvTagOpinion)
    TextView tvTagOpinion;

    @Bind(R.id.tv_TagRiders)
    TextView tv_TagRiders;
    @Bind(R.id.tv_TagOpinion)
    TextView tv_TagOpinion;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);
        //getSupportActionBar().hide();
        activity = FirstPageActivity.this;
        ButterKnife.bind(activity);
        setFontsToViews();
        String text = "<font color=#D1622A>Divided</font> <font color=#000000>By Boundaries</font>";
        String text_ = "<font color=#D1622A>United</font> <font color=#000000>By Throttles</font>";
        tv_TagRiders.setText(Html.fromHtml(text));
        tv_TagOpinion.setText(Html.fromHtml(text_));

    }

    private void setFontsToViews() {
        tvTagRiders.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT EXTRA LIGHT.TTF"));
        tvTagOpinion.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
    }


    @OnClick(R.id.ivStart)
    void click(View view) {
        switch (view.getId()) {
            case R.id.ivStart:
                Intent intent=new Intent(activity,MainScreenActivity.class);
                intent.putExtra("MainScreen", "CLOSED");
                startActivity(intent);

                break;
        }
    }
}
