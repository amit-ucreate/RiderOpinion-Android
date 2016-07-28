package com.nutsuser.ridersdomain.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.adapter.ImageAdapter;
import com.nutsuser.ridersdomain.fragments.FragmentStatePagerAdapterFragment;
import com.nutsuser.ridersdomain.utils.ApplicationGlobal;
import com.nutsuser.ridersdomain.utils.CustomizeDialog;
import com.nutsuser.ridersdomain.utils.PrefsManager;
import com.nutsuser.ridersdomain.web.api.FileUploadService;
import com.nutsuser.ridersdomain.web.api.ServiceGenerator;
import com.nutsuser.ridersdomain.web.pojos.SubcribeGridData;
import com.nutsuser.ridersdomain.web.pojos.SubcribesGridImages;
import com.nutsuser.ridersdomain.web.pojos.SucribeGridOjectdata;
import com.rollbar.android.Rollbar;

import java.lang.reflect.Type;
import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;

/**
 * Created by admin on 11-02-2016.
 */
public class Subcribe extends BaseActivity {

    public PrefsManager prefsManager;
    CustomizeDialog mCustomizeDialog;
    String AccessToken, UserId;
    SucribeGridOjectdata data;
    ArrayList<SubcribesGridImages> arrayList;
    FrameLayout container;
    ImageView ivcross, ivGridView, ivWeather;
    GridView gridview;
    FrameLayout fmlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subscribe);
        try {
            ivcross = (ImageView) findViewById(R.id.ivcross);
            ivGridView = (ImageView) findViewById(R.id.ivGridView);
            ivWeather = (ImageView) findViewById(R.id.ivWeather);
            gridview = (GridView) findViewById(R.id.gridview);
            container = (FrameLayout) findViewById(R.id.container);
            fmlayout = (FrameLayout) findViewById(R.id.fmlayout);
            arrayList = new ArrayList<>();
            SubcribeImagemodelinfo();
            container.setVisibility(View.GONE);
            gridview.setVisibility(View.VISIBLE);
            ivcross.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction().add(R.id.container, new FragmentStatePagerAdapterFragment()).commit();
            }
            ivGridView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    container.setVisibility(View.GONE);
                    gridview.setVisibility(View.VISIBLE);
                }
            });
            ivWeather.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gridview.setVisibility(View.GONE);
                    container.setVisibility(View.VISIBLE);
                }
            });
        }catch(Exception e){
                Rollbar.reportException(e, "minor", "StatusRiderOnMapActivity on create");
            }
    }

    /**
     * Destination Details info .
     */
    public void SubcribeImagemodelinfo() {
        try {
            prefsManager = new PrefsManager(Subcribe.this);
            FileUploadService service = ServiceGenerator.createService(FileUploadService.BASE_URL);
            service.ridingDestinationAfterSubscribe(prefsManager.getCaseId(), prefsManager.getToken(), ApplicationGlobal.DestID, new Callback<JsonObject>() {
                @Override
                public void success(JsonObject jsonObject, retrofit.client.Response response) {
                    arrayList.clear();
                    Log.e("jsonObject:", "" + jsonObject);
                    Type type = new TypeToken<SubcribeGridData>() {
                    }.getType();
                    SubcribeGridData subcribeData = new Gson().fromJson(jsonObject.toString(), type);
                    if (subcribeData.getSuccess().equals("1")) {
                        data = subcribeData.getData();
                        arrayList.addAll(data.getImages());
                        if (arrayList.size() == 0) {
                            fmlayout.setBackgroundResource(R.drawable.ic_no_image_routereview);
                        } else {

                            gridview.setAdapter(new ImageAdapter(getApplicationContext(), arrayList));
                        }
                    } else {

                    }

                }

                @Override
                public void failure(RetrofitError error) {
                    Log.d("Error", "");


                }
            });
        } catch(Exception e){
                Rollbar.reportException(e, "minor", "StatusRiderOnMapActivity ridingDestinationAfterSubscribe API ");
            }
    }


}
