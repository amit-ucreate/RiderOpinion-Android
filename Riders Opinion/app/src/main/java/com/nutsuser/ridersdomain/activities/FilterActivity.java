package com.nutsuser.ridersdomain.activities;

import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.adapter.FilterMultipleCheck;
import com.nutsuser.ridersdomain.database.DatabaseHelper;
import com.nutsuser.ridersdomain.database.DatasourceHandler;
import com.nutsuser.ridersdomain.services.GPSService;
import com.nutsuser.ridersdomain.utils.CustomizeDialog;
import com.nutsuser.ridersdomain.utils.PrefsManager;
import com.nutsuser.ridersdomain.view.CustomDialog;
import com.nutsuser.ridersdomain.web.api.FileUploadService;
import com.nutsuser.ridersdomain.web.api.ServiceGenerator;
import com.nutsuser.ridersdomain.web.pojos.FilterMultiple;
import com.nutsuser.ridersdomain.web.pojos.RidingDestination;
import com.rollbar.android.Rollbar;

import java.lang.reflect.Type;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;

/**
 * Created by user on 8/31/2015.
 */
public class FilterActivity extends BaseActivity {
    public static boolean filter = false;
    CustomizeDialog mCustomizeDialog;
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


    @Bind(R.id.tvApply)
    TextView tvApply;
    @Bind(R.id.seekBar1)
    SeekBar seekBar1;

    @Bind(R.id.tvDistance)
    TextView tvDistance;
    String AccessToken, UserId;
    double start1, end1;
    String star_lat, star_long, type;

    @Bind(R.id.listView)
    ListView listView;
    FilterMultipleCheck filterMultipleCheck;
    ArrayList<FilterMultiple> filterMultiples = new ArrayList<>();
    ArrayList<FilterMultiple> filterMultiplesDisable = new ArrayList<>();
    public ArrayList<String> arrayList;
   // public ArrayList<String> check;

    // database work
    DatabaseHelper mDatabaseHelper;
    DatasourceHandler mDatasourceHandler;
    Cursor mCursor;
    ArrayList<String> fetchTypeData = new ArrayList<>();
    ArrayList<String> fetch_TypeData = new ArrayList<>();
    String seekValue;
    String userId;
    PrefsManager prefsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        try {
            overridePendingTransition(R.anim.trans_top_in, R.anim.trans_top_out);
            ButterKnife.bind(this);
            setFontsToViews();
            setupSeekbar();
            fetchTypeData = new ArrayList<>();
            prefsManager = new PrefsManager(this);
            userId = prefsManager.getCaseId();
            mDatabaseHelper = new DatabaseHelper(this);
            mDatasourceHandler = new DatasourceHandler(this);
            tvDistance = (TextView) findViewById(R.id.tvDistance);
            seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                int progress = 0;

                @Override
                public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {

                    progress = progresValue;
                    // tvDistance.setText("Please Wait..");
                    //Toast.makeText(getApplicationContext(), "Changing seekbar's progress: "+progress, Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    // Toast.makeText(getApplicationContext(), "Started tracking seekbar:"+seekBar.getProgress(), Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    tvDistance.setText("" + seekBar.getProgress());
                    boolean check = mDatasourceHandler.updateDistance(prefsManager.getCaseId(), tvDistance.getText().toString());
                    Log.e("check:", "" + check);
                    //textView.setText("Covered: " + progress + "/" + seekBar.getMax());
                    //Toast.makeText(getApplicationContext(), "Stopped tracking seekbar+_"+seekBar.getProgress(), Toast.LENGTH_SHORT).show();

                }

            });

            GPSService mGPSService = new GPSService(this);
            mGPSService.getLocation();

            if (mGPSService.isLocationAvailable == false) {

                // Here you can ask the user to try again, using return; for that
                Toast.makeText(getApplicationContext(), "Your location is not available, please try again.", Toast.LENGTH_SHORT).show();
                return;

                // Or you can continue without getting the location, remove the return; above and uncomment the line given below
                // address = "Location not available";
            } else {

                // Getting location co-ordinates
                double latitude = mGPSService.getLatitude();
                double longitude = mGPSService.getLongitude();
                //Toast.makeText(getApplicationContext(), "Latitude:" + latitude + " | Longitude: " + longitude, Toast.LENGTH_LONG).show();

                start1 = mGPSService.getLatitude();
                end1 = mGPSService.getLongitude();
                star_lat = String.valueOf(start1);
                star_long = String.valueOf(end1);
                // Filter();
            }


            // make sure you close the gps after using it. Save user's battery power
            mGPSService.closeGPS();
            try {
                mCursor = mDatasourceHandler.FilterInfo();
                Log.e("cursor at starting", mCursor.getCount() + "");
            } catch (Exception e) {
                Rollbar.reportException(e, "minor", "Filter Activity on create");
                e.printStackTrace();
            }
            if (mCursor.getCount() != 0) {
                fetchTypeData.clear();
                fetch_TypeData.clear();
                do {
                    seekValue = mCursor.getString(0).trim();
                    fetchTypeData.add(mCursor.getString(1).trim());
                    fetch_TypeData.add(mCursor.getString(2).trim());

                } while (mCursor.moveToNext());

                mCursor.close();
                seekBar1.setProgress(Integer.parseInt(seekValue));
                tvDistance.setText("" + Integer.parseInt(seekValue));
                Log.e("fetchTypeData: ", "" + fetchTypeData.size());
            } else {
                fetchTypeData.clear();
                fetch_TypeData.clear();
                for (int i = 0; i < 4; i++) {
                    if (i == 0) {
                        boolean check = mDatasourceHandler.updateFilter("2000", "Beach", userId, "1");
                        Log.e("check:", "" + check);
                    } else if (i == 1) {
                        boolean check = mDatasourceHandler.updateFilter("2000", "Hills", userId, "1");
                        Log.e("check:", "" + check);
                    } else if (i == 2) {
                        boolean check = mDatasourceHandler.updateFilter("2000", "Wildlife", userId, "1");
                        Log.e("check:", "" + check);
                    } else if (i == 3) {
                        boolean check = mDatasourceHandler.updateFilter("2000", "Adventure", userId, "1");
                        Log.e("check:", "" + check);
                    }
                }
                try {
                    mCursor = mDatasourceHandler.FilterInfo();
                    Log.e("cursor at starting", mCursor.getCount() + "");
                } catch (Exception e) {
                    Rollbar.reportException(e, "minor", "Filter Activity on create");
                    e.printStackTrace();
                }
                if (mCursor.getCount() != 0) {
                    fetchTypeData.clear();
                    do {
                        seekValue = mCursor.getString(0).trim();

                        fetchTypeData.add(mCursor.getString(1).trim());
                        fetch_TypeData.add(mCursor.getString(2).trim());

                    } while (mCursor.moveToNext());

                    mCursor.close();
                    seekBar1.setProgress(Integer.parseInt(seekValue));
                    tvDistance.setText("" + Integer.parseInt(seekValue));
                }
            }


            for (int i = 0; i < fetchTypeData.size(); i++) {
                Log.e("fetchTypeData:", "" + fetchTypeData);
                Log.e("fetch_TypeData:", "" + fetch_TypeData);
                if (i == 0) {
                    if (fetch_TypeData.get(i).equals("1")) {
                        FilterMultiple filterMultiple = new FilterMultiple(true, "Beach");
                        filterMultiples.add(filterMultiple);
                    } else {
                        FilterMultiple filterMultiple = new FilterMultiple(false, "Beach");
                        filterMultiples.add(filterMultiple);
                    }

                } else if (i == 1) {
                    if (fetch_TypeData.get(i).equals("1")) {
                        FilterMultiple filterMultiple = new FilterMultiple(true, "Hills");
                        filterMultiples.add(filterMultiple);
                    } else {
                        FilterMultiple filterMultiple = new FilterMultiple(false, "Hills");
                        filterMultiples.add(filterMultiple);
                    }

                } else if (i == 2) {
                    if (fetch_TypeData.get(i).equals("1")) {
                        FilterMultiple filterMultiple = new FilterMultiple(true, "Wildlife");
                        filterMultiples.add(filterMultiple);
                    } else {
                        FilterMultiple filterMultiple = new FilterMultiple(false, "Wildlife");
                        filterMultiples.add(filterMultiple);
                    }

                } else if (i == 3) {
                    if (fetch_TypeData.get(i).equals("1")) {
                        FilterMultiple filterMultiple = new FilterMultiple(true, "Adventure");
                        filterMultiples.add(filterMultiple);
                    } else {
                        FilterMultiple filterMultiple = new FilterMultiple(false, "Adventure");
                        filterMultiples.add(filterMultiple);
                    }

                }

            }

            filterMultipleCheck = new FilterMultipleCheck(FilterActivity.this, filterMultiples, tvDistance.getText().toString());
            listView.setAdapter(filterMultipleCheck);
        }catch(Exception e){
            Rollbar.reportException(e, "minor", "Filter Activity on create");
        }

    }

    private void setupSeekbar() {
     /*   rsbDistance = new RangeSeekBar<Integer>(this);
        rsbDistance.setRangeValues(0, 1500);
        rsbDistance.setSelectedMinValue(50);
        rsbDistance.setSelectedMaxValue(400);
        llRangeSeekBar.addView(rsbDistance);*/
    }

    private void setFontsToViews() {
        tvCancel.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/Lato-Heavy.ttf"));
        tvClear.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/Lato-Heavy.ttf"));
        tvLabelDistance.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/Lato-Bold.ttf"));
        tvLabelType.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/Lato-Bold.ttf"));
        tvTitleToolbar.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/MACHINEN.TTF"));

        tvApply.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/Lato-Bold.ttf"));
    }

    @OnClick({R.id.tvCancel, R.id.tvClear, R.id.tvApply})
    void click(View view) {
        switch (view.getId()) {
            case R.id.tvCancel:
                onBackPressed();
                break;

         /*   case R.id.ivRatting1:
                ratting = "1";
                ivRatting1.setImageResource(R.drawable.icon_radio_selected);
                ivRatting2.setImageResource(R.drawable.icon_radio);
                ivRatting3.setImageResource(R.drawable.icon_radio);
                ivRatting4.setImageResource(R.drawable.icon_radio);
                ivRatting5.setImageResource(R.drawable.icon_radio);

                break;
            case R.id.ivRatting2:
                ratting = "2";
                ivRatting1.setImageResource(R.drawable.icon_radio);
                ivRatting2.setImageResource(R.drawable.icon_radio_selected);
                ivRatting3.setImageResource(R.drawable.icon_radio);
                ivRatting4.setImageResource(R.drawable.icon_radio);
                ivRatting5.setImageResource(R.drawable.icon_radio);
                break;
            case R.id.ivRatting3:
                ratting = "3";
                ivRatting1.setImageResource(R.drawable.icon_radio);
                ivRatting2.setImageResource(R.drawable.icon_radio);
                ivRatting3.setImageResource(R.drawable.icon_radio_selected);
                ivRatting4.setImageResource(R.drawable.icon_radio);
                ivRatting5.setImageResource(R.drawable.icon_radio);
                break;
            case R.id.ivRatting4:
                ratting = "4";
                ivRatting1.setImageResource(R.drawable.icon_radio);
                ivRatting2.setImageResource(R.drawable.icon_radio);
                ivRatting3.setImageResource(R.drawable.icon_radio);
                ivRatting4.setImageResource(R.drawable.icon_radio_selected);
                ivRatting5.setImageResource(R.drawable.icon_radio);
                break;
            case R.id.ivRatting5:
                ratting = "5";
                ivRatting1.setImageResource(R.drawable.icon_radio);
                ivRatting2.setImageResource(R.drawable.icon_radio);
                ivRatting3.setImageResource(R.drawable.icon_radio);
                ivRatting4.setImageResource(R.drawable.icon_radio);
                ivRatting5.setImageResource(R.drawable.icon_radio_selected);
                break;*/
            case R.id.tvClear:
                //  ratting = "0";
                type = "";
            /*    ivRatting1.setImageResource(R.drawable.icon_radio);
                ivRatting2.setImageResource(R.drawable.icon_radio);
                ivRatting3.setImageResource(R.drawable.icon_radio);
                ivRatting4.setImageResource(R.drawable.icon_radio);
                ivRatting5.setImageResource(R.drawable.icon_radio);*/
                seekBar1.setProgress(0);
                tvDistance.setText("");
                for (int i = 0; i < 4; i++) {
                   // listView.getAdapter().getItem(i).
                    if(i==0){
                        boolean check = mDatasourceHandler.updateFilter("0", "Beach", userId,"0");
                        FilterMultiple filterMultiple = new FilterMultiple(false, "Beach");
                        filterMultiplesDisable.add(filterMultiple);
                        Log.e("check:", "" + check);
                    }
                    else if (i == 1){
                        boolean check = mDatasourceHandler.updateFilter("0", "Hills", userId,"0");
                        Log.e("check:", "" + check);
                        FilterMultiple filterMultiple = new FilterMultiple(false, "Hills");
                        filterMultiplesDisable.add(filterMultiple);
                    }
                    else if (i == 2){
                        boolean check = mDatasourceHandler.updateFilter("0", "Wildlife", userId,"0");
                        Log.e("check:", "" + check);
                        FilterMultiple filterMultiple = new FilterMultiple(false, "Wildlife");
                        filterMultiplesDisable.add(filterMultiple);
                    }
                    else if (i == 3){
                        boolean check = mDatasourceHandler.updateFilter("0", "Adventure", userId,"0");
                        Log.e("check:", "" + check);
                        FilterMultiple filterMultiple = new FilterMultiple(false, "Adventure");
                        filterMultiplesDisable.add(filterMultiple);
                    }

                    filterMultipleCheck = new FilterMultipleCheck(FilterActivity.this, filterMultiplesDisable,tvDistance.getText().toString());
                    listView.setAdapter(filterMultipleCheck);
                    filterMultipleCheck.notifyDataSetChanged();
                }
                break;
            case R.id.tvApply:
                arrayList = new ArrayList<>();
                //check= new ArrayList<>();
                arrayList.clear();
                //check.clear();
                for (FilterMultiple p : filterMultipleCheck.getBox()) {
                    if (p.box) {
                        arrayList.add(p._name.toString().trim());
                    }
                }
                if (arrayList.size() == 0) {

                } else {
                    // ArrayList<String> list=new ArrayList<>();

                    //arrayList.toString().replace("[", "").replace("]", "");
                    //check=arrayList;
                    type = arrayList.toString().replace("[", "").replace("]", "").replace(" ","").toLowerCase().trim();
                    filter(tvDistance.getText().toString(), type);
                }

                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_bottom_in, R.anim.trans_bottom_out);
    }


    /**
     * filter Data (Only Strings) to Server
     **/
    private void filter(final String radius, String Type) {
        showProgressDialog();
        prefsManager = new PrefsManager(FilterActivity.this);
        AccessToken = prefsManager.getToken();
        UserId = prefsManager.getCaseId();
       // Log.e("AccessToken:", "" + AccessToken + "----UserId----" + UserId);
       Log.e("radius:", "" + radius + "--Type--" + Type);
       // Log.e("star_long:", "" + star_long + "----star_lat----" + star_lat);
        FileUploadService service = ServiceGenerator.createService(FileUploadService.BASE_URL);
        service.ridingDestinationfilter(UserId, star_long, star_lat, radius, Type, AccessToken, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, retrofit.client.Response response) {
                Log.e("Filter:", "" + jsonObject);
                Type type = new TypeToken<RidingDestination>() {
                }.getType();
                RidingDestination mRidingDestination = new Gson().fromJson(jsonObject.toString(), type);
                DestinationsListActivity.mRidingDestinationDetailses.clear();
                dismissProgressDialog();
                if (mRidingDestination.getSuccess().equals("1")) {
                    filter = true;
                    prefsManager = new PrefsManager(FilterActivity.this);
                    prefsManager.setRadius(tvDistance.getText().toString().trim());
                    DestinationsListActivity.mRidingDestinationDetailses.addAll(mRidingDestination.getData());
                    finish();

                } else {
                    filter = true;
                    prefsManager = new PrefsManager(FilterActivity.this);
                    prefsManager.setRadius(tvDistance.getText().toString().trim());
                    finish();
                    //CustomDialog.showProgressDialog(FilterActivity.this, mRidingDestination.getMessage());
                }

            }

            @Override
            public void failure(RetrofitError error) {
                dismissProgressDialog();
                Log.d("DataUploading------", "Data Uploading Failure......" + error);
            }
        });
    }


    public void showProgressDialog() {
        mCustomizeDialog = new CustomizeDialog(FilterActivity.this);
        mCustomizeDialog.setCancelable(false);
        mCustomizeDialog.show();
    }

    public void dismissProgressDialog() {
        if (mCustomizeDialog != null && mCustomizeDialog.isShowing()) {
            mCustomizeDialog.dismiss();
            mCustomizeDialog = null;
        }
    }

}
