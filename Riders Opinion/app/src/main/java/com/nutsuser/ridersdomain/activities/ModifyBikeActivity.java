package com.nutsuser.ridersdomain.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.adapter.AdapterModify;
import com.nutsuser.ridersdomain.adapter.CustomGridAdapter;
import com.nutsuser.ridersdomain.chat.SampleCometChatActivity;
import com.nutsuser.ridersdomain.utils.ApplicationGlobal;
import com.nutsuser.ridersdomain.utils.CustomizeDialog;
import com.nutsuser.ridersdomain.utils.PrefsManager;
import com.nutsuser.ridersdomain.view.BetterPopupWindow;
import com.nutsuser.ridersdomain.view.CustomDialog;
import com.nutsuser.ridersdomain.web.api.FileUploadService;
import com.nutsuser.ridersdomain.web.api.ServiceGenerator;
import com.nutsuser.ridersdomain.web.api.volley.RequestJsonObject;
import com.nutsuser.ridersdomain.web.pojos.ModiflyBikeData;
import com.nutsuser.ridersdomain.web.pojos.ModilyBike;
import com.nutsuser.ridersdomain.web.pojos.VehicleDetails;
import com.nutsuser.ridersdomain.web.pojos.VehicleModel;
import com.nutsuser.ridersdomain.web.pojos.VehicleModelDetails;
import com.nutsuser.ridersdomain.web.pojos.VehicleName;
import com.rollbar.android.Rollbar;

import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;

/**
 * Created by user on 9/2/2015.
 */
public class ModifyBikeActivity extends BaseActivity implements View.OnClickListener {
    CustomizeDialog mCustomizeDialog;
    public PrefsManager prefsManager;
    Dialog pDialog;
    ArrayList<ModiflyBikeData> modiflyBikeDatas = new ArrayList<>();
    private ArrayList<VehicleModelDetails> vehicleModelDetailses = new ArrayList<VehicleModelDetails>();
    // public static String [] prgmNameList={"Riding Destinations","Meet 'N' Plan A Ride","Riding Events \n    ","Modifly Your Bikes","Healthy Riding","Get Directions","Notifications","Settings"};
    //  public static int [] prgmImages={R.drawable.icon_menu_destination,R.drawable.icon_menu_meetplan,R.drawable.icon_menu_events,R.drawable.icon_modifybike,R.drawable.icon_menu_healthy_riding,R.drawable.icon_menu_get_direction,R.drawable.icon_menu_notification,R.drawable.icon_menu_settings};
    // public static Class [] classList={DestinationsListActivity.class,PlanRideActivity.class,EventsListActivity.class,ModifyBikeActivity.class,HealthyRidingActivity.class,GetDirections.class,NotificationScreen.class,SettingsActivity.class};
    public static String[] prgmNameList = {"My Rides", "My Messages", "My Friends", "Chats", "Favorites", "Notifications", "Settings", "Riders Near By"};
    public static int[] prgmImages = {R.drawable.ic_menu_my_rides, R.drawable.ic_menu_my_messages, R.drawable.menu_my_friends, R.drawable.ic_menu_menu_chats, R.drawable.menu_fav_destinations, R.drawable.ic_menu_menu_notifications, R.drawable.menu_settings, R.drawable.icon_nearby};
    public static Class[] classList = {MyRidesRecyclerView.class, RecentChatListActivity.class, MyFriendsActivity.class, ComingSoon.class, FavouriteDesination.class, NotificationListActivity.class, SettingsActivity.class, NearByFriendsAcitivity.class};
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tvTitleToolbar)
    TextView tvTitleToolbar;
    @Bind(R.id.gvModifiers)
    GridView gvModifiers;
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.lvSlidingMenu)
    LinearLayout lvSlidingMenu;
    @Bind(R.id.tvName)
    TextView tvName;
    @Bind(R.id.ivEdit)
    ImageView ivEdit;
    ArrayList<Integer> arrayYear = new ArrayList<Integer>();
    /* @Bind(R.id.tvAddress)
     TextView tvAddress;
     @Bind(R.id.tvDestinations)
     TextView tvDestinations;
     @Bind(R.id.tvEvents)
     TextView tvEvents;
     @Bind(R.id.tvModifyBike)
     TextView tvModifyBike;
     @Bind(R.id.tvMeetAndPlanRide)
     TextView tvMeetAndPlanRide;
     @Bind(R.id.tvHealthyRiding)
     TextView tvHealthyRiding;
     @Bind(R.id.tvGetDirections)
     TextView tvGetDirections;
     @Bind(R.id.tvNotifications)
     TextView tvNotifications;
     @Bind(R.id.tvSettings)
     TextView tvSettings;*/
    @Bind(R.id.tvBikeName)
    TextView tvBikeName;
    @Bind(R.id.tvBikeYear)
    TextView tvBikeYear;
    @Bind(R.id.tvBikeModel)
    TextView tvBikeModel;
    @Bind(R.id.tvSponsoredAdTitle1)
    TextView tvSponsoredAdTitle1;
    @Bind(R.id.tvSponsoredAdReviews1)
    TextView tvSponsoredAdReviews1;
    @Bind(R.id.tvSponsoredAdLocation1)
    TextView tvSponsoredAdLocation1;
    @Bind(R.id.tvSponsoredAdTitle2)
    TextView tvSponsoredAdTitle2;
    @Bind(R.id.tvSponsoredAdReviews2)
    TextView tvSponsoredAdReviews2;
    @Bind(R.id.tvSponsoredAdLocation2)
    TextView tvSponsoredAdLocation2;
    @Bind(R.id.EtServiceCenter)
    EditText EtServiceCenter;
    @Bind(R.id.ivSearch)
    ImageView ivSearch;
    @Bind(R.id.gridView1)
    GridView gridView1;
    private Activity activity;
    @Bind(R.id.sdvDp)
    SimpleDraweeView sdvDp;
    ModelPopupWindow mdw;
    ModelCustomBaseAdapter ModelCustomBaseAdapter;
    @Bind(R.id.btUpdateProfile)
    Button btUpdateProfile;
    private ActionBarDrawerToggle mDrawerToggle;
    // drop down code
    View view;

    private ArrayList<VehicleDetails> mVehicleDetailses = new ArrayList<VehicleDetails>();
    CustomBaseAdapter adapter;
    TimeBaseAdapter mAdapter;
    DemoPopupWindow dw;
    TimePopupWindow tw;
    String vehicleId, BikeId;
    FrameLayout flBikeName, flBikeYear, flBikeModel;
    String strBikeName, strBikeYear, strBikeModel;
    TextView tvDone, tvCustomBikeName, tvCustomBikeYear, tvCustomBikeModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_bike);
        try {
            activity = this;
            ButterKnife.bind(this);
            setupActionBar(toolbar);
            prefsManager = new PrefsManager(this);
            try {
                if(getIntent().hasExtra("BikeId")) {
                    BikeId= getIntent().getExtras().getString("BikeId");
                }else{
                    BikeId = prefsManager.getVehicleId();
                }
            }catch(NullPointerException e1){
                Rollbar.reportException(e1, "minor", "ModifierBikeActivity intent data is null");
            }
            Log.e("Vechicle Id", prefsManager.getVehicleId());
            modelYears();
            //showFilterDialog(strBikeName, strBikeYear, strBikeModel);
            setFonts();
            view = new View(this);

            tvTitleToolbar.setText("MODIFY YOUR BIKE");

            setItemClickListener();
            gridView1.setAdapter(new CustomGridAdapter(this, prgmNameList, prgmImages));
            gridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (position == 7) {
//
//                } else {
//                if (position == 1) {
//                    Log.e("positiuon:", "" + classList[position]);
//                    intent_Calling(classList[position], "My Messages");
//                } else {
                    Log.e("positiuon:", "" + classList[position]);
                    intentCalling(classList[position]);
                    // }
                    // }
                }
            });
            /*****
             *
             * On Drawer Open and Close
             *
             * **/
            mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                    R.drawable.icon_image_view, //nav menu toggle icon
                    R.string.app_name, // nav drawer open - description for accessibility
                    R.string.app_name // nav drawer close - description for accessibility
            ) {
                public void onDrawerClosed(View view) {
                    //getActionBar().setTitle(mTitle);
                    // calling onPrepareOptionsMenu() to show action bar icons
                    //invalidateOptionsMenu();
                    // Toast.makeText(DestinationsListActivity.this, "Drawer Closed....", Toast.LENGTH_SHORT).show();
                }

                public void onDrawerOpened(View drawerView) {
//                    String imageUrl = prefsManager.getImageUrl();
//                    File file = new File(imageUrl);
//                    sdvDp.setImageURI(Uri.fromFile(file));
                    String imageUrl = prefsManager.getImageUrl();
                    sdvDp.setImageURI(Uri.parse(imageUrl));
                    // Toast.makeText(DestinationsListActivity.this, "Drawer Opened....", Toast.LENGTH_SHORT).show();
                    //getActionBar().setTitle(mDrawerTitle);
                    // calling onPrepareOptionsMenu() to hide action bar icons
                    //invalidateOptionsMenu();
                }
            };
//         mDrawerLayout.setDrawerListener(mDrawerToggle);

            mDrawerLayout.closeDrawer(lvSlidingMenu);
            showProfileImage();


            EtServiceCenter.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                        //do something
                        EtServiceCenter.clearFocus();

                        if (EtServiceCenter.getText().toString().length() > 1) {

                            hideKeyboard();
                        }
                        modifybikeSearch(BikeId, EtServiceCenter.getText().toString());
                    }

                    return false;
                }
            });
            EtServiceCenter.addTextChangedListener(new TextWatcher() {

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() > 0) {
                        ivSearch.setImageResource(R.drawable.icon_close);
                    } else {
                        ivSearch.setImageResource(R.drawable.icon_search);
                    }
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void afterTextChanged(Editable s) {
                }

            });

            ivSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!EtServiceCenter.getText().toString().isEmpty()) {
                        EtServiceCenter.setText("");
                        hideKeyboard();
                    }
                }
            });
        }catch(Exception e){
            Rollbar.reportException(e, "minor", "ModifierBikeActivity on create");
        }

    }

    private void hideKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    /**
     * '
     * Show Profile Image
     ***/
//    private void showProfileImage() {
//        if (prefsManager.getUserName() == null) {
//            tvName.setText("No Name");
//        } else {
//            tvName.setText(prefsManager.getUserName());
//        }
//        if (prefsManager.getImageUrl() == null) {
//           // Toast.makeText(ModifyBikeActivity.this, "Image Address is Null", Toast.LENGTH_SHORT).show();
//        } else {
//            String imageUrl = prefsManager.getImageUrl();
//            File file = new File(imageUrl);
//            sdvDp.setImageURI(Uri.fromFile(file));
//        }
//
//    }
    private void showProfileImage() {
        if (prefsManager.getUserName() == null) {
            tvName.setText("No Name");
        } else {
            tvName.setText(prefsManager.getUserName());
        }
        if (prefsManager.getImageUrl() == null) {
            Toast.makeText(ModifyBikeActivity.this, "Image Address is Null", Toast.LENGTH_SHORT).show();
        } else {
            String imageUrl = prefsManager.getImageUrl();
            sdvDp.setImageURI(Uri.parse(imageUrl));
//            File file = new File(imageUrl);
//            sdvDp.setImageURI(Uri.fromFile(file));
        }

    }
    public void intentCalling(Class name) {
        Intent mIntent = new Intent(ModifyBikeActivity.this, name);
        startActivity(mIntent);

    }

    public void intent_Calling(Class name, String na) {
        Intent mIntent = new Intent(ModifyBikeActivity.this, name);
        mIntent.putExtra(SCREEN_OPEN, na);
        startActivity(mIntent);

    }

    private void setItemClickListener() {
        gvModifiers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(activity, ModifiersListActivity.class);
                intent.putExtra("VenderId", modiflyBikeDatas.get(position).getCatId());
                intent.putExtra("BikeName", tvBikeName.getText().toString().replace("/",""));
                Log.e("BikeId: ",""+BikeId+" strBikeYear: "+ strBikeYear+" strBikeModel: "+strBikeModel);
                intent.putExtra("BikeId", BikeId);
                intent.putExtra("BikeYear",strBikeYear );
                intent.putExtra("BikeModel",strBikeModel );
                startActivity(intent);
                // startActivity(new Intent(activity, ModifiersListActivity.class));
            }
        });
    }


    private void setFonts() {
        Typeface typefaceTitle = Typeface.createFromAsset(getResources().getAssets(), "fonts/MACHINEN.TTF");
        Typeface typefaceNormal = Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF");
        //  tvBikeName.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/Lato-Heavy.ttf"));
        tvTitleToolbar.setTypeface(typefaceTitle);
        tvSponsoredAdTitle1.setTypeface(typefaceNormal);
        tvSponsoredAdTitle2.setTypeface(typefaceNormal);
        tvSponsoredAdLocation1.setTypeface(typefaceNormal);
        tvSponsoredAdLocation2.setTypeface(typefaceNormal);
        tvSponsoredAdReviews1.setTypeface(typefaceNormal);
        tvSponsoredAdReviews2.setTypeface(typefaceNormal);
        tvName.setTypeface(typefaceTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                Intent intent = new Intent(ModifyBikeActivity.this, MainScreenActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("MainScreenResponse", "OPEN");
                startActivity(intent);
                finish();

        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ModifyBikeActivity.this, MainScreenActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("MainScreenResponse", "OPEN");
        startActivity(intent);
        finish();
    }
    @OnClick({R.id.ivMenu, R.id.rlProfile, R.id.btFullProfile, R.id.btUpdateProfile, R.id.tvBikeName})
    void click(View v) {
        switch (v.getId()) {
//            case R.id.ivFilter:
//                startActivity(new Intent(activity, FilterActivity.class));
//                break;

            case R.id.ivMenu:
                if (mDrawerLayout.isDrawerOpen(lvSlidingMenu))
                    mDrawerLayout.closeDrawer(lvSlidingMenu);
                else
                    mDrawerLayout.openDrawer(lvSlidingMenu);
                break;
            case R.id.rlProfile:
                // startActivity(new Intent(activity, ProfileActivity.class));
                break;
            case R.id.btUpdateProfile:
                startActivity(new Intent(activity, ProfileActivity.class));
                break;
            case R.id.btFullProfile:
                startActivity(new Intent(activity, PublicProfileScreen.class));
                break;
            case R.id.tvBikeName:

                break;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isNetworkConnected()) {
            modifyYourBike(BikeId);
        } else {
            showToast("Internet is not connected.");
        }

        if (mDrawerLayout.isDrawerOpen(lvSlidingMenu)) {
            showProfileImage();
            mDrawerLayout.closeDrawer(lvSlidingMenu);
        }    }

    /**
     * modifyYourBike Data (Only Strings) to Server
     **/
    private void modifyYourBike(String bikeid) {

        showProgressDialog();

        String userId = prefsManager.getCaseId();

        String accessToken = prefsManager.getToken();
        Log.e("userId : ", "" + userId + " bikeid = " + bikeid + " accessToken = " + accessToken);
        FileUploadService service = ServiceGenerator.createService(FileUploadService.BASE_URL);

        service.modifyYourBike(bikeid, userId, accessToken, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, retrofit.client.Response response) {

                Type type = new TypeToken<ModilyBike>() {
                }.getType();
                ModilyBike
                        modilyBike = new Gson().fromJson(jsonObject.toString(), type);
                Log.e("modifyYourBike:", "" + jsonObject);
                modiflyBikeDatas.clear();

                if (modilyBike.getSuccess() == 1) {
                    dismissProgressDialog();
//                    tvBikeYear.setText(modilyBike.getVehicle_year()+" /");
//                    tvBikeName.setText(" "+modilyBike.getvehicle() + " /");
                    tvBikeName.setText(""+modilyBike.getvehicle());
                    tvBikeModel.setText(modilyBike.getVehicle_type());
                    strBikeName = modilyBike.getvehicle();
                    strBikeYear= modilyBike.getVehicle_year();
                    strBikeModel= modilyBike.getVehicle_type();
                    BikeId = modilyBike.getbikeId();
                    modiflyBikeDatas.addAll(modilyBike.getData());
                    gvModifiers.setAdapter(new AdapterModify(activity, modiflyBikeDatas));
                    showFilterDialog(strBikeName, strBikeModel, strBikeYear);
                } else {
                    dismissProgressDialog();
                    gvModifiers.setAdapter(new AdapterModify(activity, modiflyBikeDatas));
                    CustomDialog.showProgressDialog(ModifyBikeActivity.this, modilyBike.getMessage().toString());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                dismissProgressDialog();
                Log.d("DataUploading------", "Data Uploading Failure......" + error);
                CustomDialog.showProgressDialog(ModifyBikeActivity.this, error.getMessage());
            }
        });
    }


    //=========== modify bike search============//
    private void modifybikeSearch(String bikeid,String search_keyword){
       // modifyYourBikeSearch

        showProgressDialog();

        String userId = prefsManager.getCaseId();

        String accessToken = prefsManager.getToken();
        Log.e("userId:", "" + userId + " bikeid" + bikeid + " accessToken" + accessToken);
        FileUploadService service = ServiceGenerator.createService(FileUploadService.BASE_URL);

        service.modifyYourBikeSearch(bikeid, userId,search_keyword.replace(" ","%20") ,accessToken, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, retrofit.client.Response response) {

                Type type = new TypeToken<ModilyBike>() {
                }.getType();
                ModilyBike
                        modilyBike = new Gson().fromJson(jsonObject.toString(), type);
                Log.e("jsonObject:", "" + jsonObject);
                modiflyBikeDatas.clear();

                if (modilyBike.getSuccess() == 1) {
                    dismissProgressDialog();
                   // tvBikeYear.setText(modilyBike.getVehicle_year()+" /");
                   // tvBikeName.setText(" "+modilyBike.getvehicle() + " /");
                    tvBikeName.setText(""+modilyBike.getvehicle());
                    tvBikeModel.setText(modilyBike.getVehicle_type());
                    strBikeName = modilyBike.getvehicle();
                    strBikeYear= modilyBike.getVehicle_year();
                    strBikeModel= modilyBike.getVehicle_type();
                    BikeId = modilyBike.getbikeId();
                    modiflyBikeDatas.addAll(modilyBike.getData());
                    gvModifiers.setAdapter(new AdapterModify(activity, modiflyBikeDatas));
                    showFilterDialog(strBikeName, strBikeModel, strBikeYear);
                } else {
                    dismissProgressDialog();
                    gvModifiers.setAdapter(new AdapterModify(activity, modiflyBikeDatas));
                    CustomDialog.showProgressDialog(ModifyBikeActivity.this, modilyBike.getMessage().toString());
                }


            }

            @Override
            public void failure(RetrofitError error) {
                dismissProgressDialog();
                Log.d("DataUploading------", "Data Uploading Failure......" + error);
                CustomDialog.showProgressDialog(ModifyBikeActivity.this, error.getMessage());
            }
        });
    }


    public void vechicleinfo() {
        showProgressDialog();

        try {
            FileUploadService service = ServiceGenerator.createService(FileUploadService.BASE_URL);

            service.getVehicleInfo(new Callback<JsonObject>() {
                @Override
                public void success(JsonObject jsonObject, retrofit.client.Response response) {

                    Type type = new TypeToken<VehicleName>() {
                    }.getType();
                    VehicleName mVehicleName = new Gson().fromJson(jsonObject.toString(), type);
                    mVehicleDetailses.clear();

                    Log.e("Vehicle response: ", "" + jsonObject);
                    if (mVehicleName.getSuccess().equals("1")) {
                        dismissProgressDialog();
                        mVehicleDetailses.addAll(mVehicleName.getData());
                        dw = new DemoPopupWindow(view, ModifyBikeActivity.this);
                        dw.showLikeQuickAction(0, 0);

                    } else {
                        dismissProgressDialog();
                    }


                }

                @Override
                public void failure(RetrofitError error) {
                    dismissProgressDialog();
                    Log.d("DataUploading------", "Data Uploading Failure......" + error);
                    CustomDialog.showProgressDialog(ModifyBikeActivity.this, error.toString());
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
            Rollbar.reportException(e, "minor", "ModifierBikeActivity getVehicleInfo API");

        }
    }




    public void Timeinfo() {
        showProgressDialog();

        try {
            FileUploadService service = ServiceGenerator.createService(FileUploadService.BASE_URL);

            service.getVehicleInfo(new Callback<JsonObject>() {
                @Override
                public void success(JsonObject jsonObject, retrofit.client.Response response) {

                    Type type = new TypeToken<VehicleName>() {
                    }.getType();
                    VehicleName mVehicleName = new Gson().fromJson(jsonObject.toString(), type);
                    mVehicleDetailses.clear();

                    Log.e("time response: ", "" + jsonObject);
                    if (mVehicleName.getSuccess().equals("1")) {
                        dismissProgressDialog();

                        mVehicleDetailses.addAll(mVehicleName.getData());


                        tw = new TimePopupWindow(view, ModifyBikeActivity.this);
                        tw.showLikeQuickAction(0, 0);


                    } else {
                        dismissProgressDialog();
                    }


                }

                @Override
                public void failure(RetrofitError error) {
                    dismissProgressDialog();
                    Log.d("DataUploading------", "Data Uploading Failure......" + error);
                    CustomDialog.showProgressDialog(ModifyBikeActivity.this, error.toString());
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
            Rollbar.reportException(e, "minor", "ModifierBikeActivity Time popup");

        }
    }

    @OnClick(R.id.ivEdit)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivEdit:
                pDialog.show();
                break;
        }
    }
    /*
    * Popup for Bike Filter
    * */

    public void showFilterDialog(final String strBikeName,  String strBikeModel,String strBikeYear) {

        try {

            pDialog = new Dialog(ModifyBikeActivity.this);
            pDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            pDialog.setContentView(R.layout.custom_dialog_filter_bike);
            pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            pDialog.setCancelable(false);
            flBikeName = (FrameLayout) pDialog.findViewById(R.id.flBikeName);
            flBikeYear = (FrameLayout) pDialog.findViewById(R.id.flBikeYear);
            flBikeModel = (FrameLayout) pDialog.findViewById(R.id.flBikeModel);
            tvDone = (TextView) pDialog.findViewById(R.id.tvDone);
            TextView tvHeader= (TextView) pDialog.findViewById(R.id.tvHeader);
            tvCustomBikeName = (TextView) pDialog.findViewById(R.id.tvCustomBikeName);
            tvCustomBikeYear = (TextView) pDialog.findViewById(R.id.tvCustomBikeYear);
            tvCustomBikeModel = (TextView) pDialog.findViewById(R.id.tvCustomBikeModel);
            Typeface typefaceTitle = Typeface.createFromAsset(getResources().getAssets(), "fonts/MACHINEN.TTF");
            Typeface typefaceNormal = Typeface.createFromAsset(getResources().getAssets(), "fonts/LATO-REGULAR.TTF");
            tvHeader.setTypeface(typefaceTitle);
            tvCustomBikeName.setTypeface(typefaceNormal);
            tvCustomBikeYear.setTypeface(typefaceNormal);
            tvCustomBikeModel.setTypeface(typefaceNormal);
            tvCustomBikeName.setText(strBikeName);
            tvCustomBikeYear.setText(strBikeYear);
            tvCustomBikeModel.setText(strBikeModel);

            flBikeYear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tw = new TimePopupWindow(view, ModifyBikeActivity.this);
                    tw.showLikeQuickAction(0, 0);
                    pDialog.dismiss();
                }
            });

            flBikeName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isNetworkConnected()) {
                        vechicleinfo();
                    }else{
                        showToast("Internet is not connected.");
                    }
                    pDialog.dismiss();
                }
            });
            flBikeModel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isNetworkConnected()) {
                        vechiclemodelinfo();
                    }else{
                        showToast("Internet is not connected.");
                    }

                    pDialog.dismiss();
                }
            });
            tvDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    modifyYourBike(BikeId);
                    pDialog.dismiss();
                }
            });


        } catch (Exception e) {
            Log.e("show", "" + e);
            Rollbar.reportException(e, "minor", "ModifierBikeActivity showFilterDialog");
        }


    }

    //http://ridersopininon.herokuapp.com/index.php/riders/vehicle

    /**
     * Register info .
     */
    public void vechiclemodelinfo() {
        showProgressDialog();
        Log.e("vechiclemodelinfo", "vechiclemodelinfo");
        try {
            //  Log.e("URL: ",""+ ApplicationGlobal.ROOT+ApplicationGlobal.baseurl_sigup+"utypeid="+utypeid+"&latitude="+latitude+"&longitude="+longitude+"&password="+password+"&deviceToken="+devicetoken+"&OS=Android");
            RequestQueue requestQueue = Volley.newRequestQueue(ModifyBikeActivity.this);
            RequestJsonObject loginTaskRequest = new RequestJsonObject(Request.Method.POST,
                    ApplicationGlobal.ROOT + ApplicationGlobal.baseurl_model + vehicleId, null,
                    volleyModelErrorListener(), volleyModelSuccessListener()
            );

            requestQueue.add(loginTaskRequest);

        } catch (Exception e) {
            e.printStackTrace();
            Rollbar.reportException(e, "minor", "ModifierBikeActivity vechiclemodelinfo API");

        }
    }

    /**
     * Implement success listener on execute api url.
     */
    public Response.Listener<JSONObject> volleyModelSuccessListener() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dismissProgressDialog();
                Log.e("Model response:", "" + response);

                Type type = new TypeToken<VehicleModel>() {
                }.getType();
                VehicleModel vehicleModel = new Gson().fromJson(response.toString(), type);

                vehicleModelDetailses.clear();


                if (vehicleModel.getSuccess().equals("1")) {
                    vehicleModelDetailses.addAll(vehicleModel.getData());
                    Log.e("vehicleModelDetailses: ", "" + vehicleModelDetailses.size());
                    mdw = new ModelPopupWindow(view, ModifyBikeActivity.this);
                    mdw.showLikeQuickAction(0, 0);

                }

            }
        };
    }

    /**
     * Implement Volley error listener here.
     */
    public Response.ErrorListener volleyModelErrorListener() {
        dismissProgressDialog();
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error: ", "" + error);
            }
        };
    }


    //============================ Adapter to set Vehicles Model Details===================//
    public class ModelCustomBaseAdapter extends BaseAdapter {
        Context context;
        private ArrayList<VehicleModelDetails> vehicleModelDetailses;

        public ModelCustomBaseAdapter(Context context, ArrayList<VehicleModelDetails> vehicleModelDetailses) {
            this.vehicleModelDetailses = vehicleModelDetailses;
            this.context = context;

        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.list_item, null);
                holder = new ViewHolder();

                holder.txtTitle = (TextView) convertView.findViewById(R.id.title);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }


            holder.txtTitle.setText(vehicleModelDetailses.get(position).getVehicleType());

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vehicleId = vehicleModelDetailses.get(position).getVehicleTypeId();
                    tvCustomBikeModel.setText(vehicleModelDetailses.get(position).getVehicleType());
                    strBikeModel = vehicleModelDetailses.get(position).getVehicleType();
                    tvBikeModel.setText(strBikeModel);
                    mdw.dismiss();
                    showFilterDialog(strBikeName, strBikeModel, strBikeYear);
                    pDialog.show();
                }
            });
            return convertView;
        }

        @Override
        public int getCount() {
            return vehicleModelDetailses.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        private class ViewHolder {

            TextView txtTitle;

        }
    }


    // ************** Class for pop-up window **********************

    /**
     * The Class DemoPopupWindow.
     */
    private class DemoPopupWindow extends BetterPopupWindow {

        /**
         * Instantiates a new demo popup window.
         *
         * @param anchor the anchor
         * @param cnt    the cnt
         */
        public DemoPopupWindow(View anchor, Context cnt) {
            super(anchor);
        }

        /*
         * (non-Javadoc)
         *
         * @see com.cellalert24.Views.BetterPopupWindow#onCreate()
         */
        @Override
        protected void onCreate() {
            // inflate layout
            LayoutInflater inflater = (LayoutInflater) this.anchor.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            ViewGroup root = (ViewGroup) inflater.inflate(
                    R.layout.share_choose_popup, null);

            ListView listview = (ListView) root.findViewById(R.id.listview);
            adapter = new CustomBaseAdapter(ModifyBikeActivity.this, mVehicleDetailses);
            listview.setAdapter(adapter);
            Button mButton = (Button) root.findViewById(R.id.cancelBtn);


            mButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    // mFrameLayout.setVisibility(View.GONE);
                    // layout.setBackgroundColor(Color.WHITE);
                    dismiss();

                }
            });

            this.setContentView(root);
        }

    }


    // ************** Class for pop-up window **********************

    /**
     * The Class DemoPopupWindow.
     */
    private class TimePopupWindow extends BetterPopupWindow {

        /**
         * Instantiates a new demo popup window.
         *
         * @param anchor the anchor
         * @param cnt    the cnt
         */
        public TimePopupWindow(View anchor, Context cnt) {
            super(anchor);
        }

        /*
         * (non-Javadoc)
         *
         * @see com.cellalert24.Views.BetterPopupWindow#onCreate()
         */
        @Override
        protected void onCreate() {
            // inflate layout
            LayoutInflater inflater = (LayoutInflater) this.anchor.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            ViewGroup root = (ViewGroup) inflater.inflate(
                    R.layout.share_choose_popup, null);

            ListView listview = (ListView) root.findViewById(R.id.listview);
            mAdapter = new TimeBaseAdapter(ModifyBikeActivity.this, arrayYear);
            listview.setAdapter(mAdapter);
            Button mButton = (Button) root.findViewById(R.id.cancelBtn);


            mButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    // mFrameLayout.setVisibility(View.GONE);
                    // layout.setBackgroundColor(Color.WHITE);
                    dismiss();

                }
            });

            this.setContentView(root);
        }

    }


    /**
     * The Class DemoPopupWindow.
     */
    private class ModelPopupWindow extends BetterPopupWindow {

        /**
         * Instantiates a new demo popup window.
         *
         * @param anchor the anchor
         * @param cnt    the cnt
         */
        public ModelPopupWindow(View anchor, Context cnt) {
            super(anchor);
        }

        /*
         * (non-Javadoc)
         *
         * @see com.cellalert24.Views.BetterPopupWindow#onCreate()
         */
        @Override
        protected void onCreate() {
            // inflate layout
            LayoutInflater inflater = (LayoutInflater) this.anchor.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            ViewGroup root = (ViewGroup) inflater.inflate(
                    R.layout.share_choose_popup, null);

            ListView listview = (ListView) root.findViewById(R.id.listview);
            ModelCustomBaseAdapter = new ModelCustomBaseAdapter(ModifyBikeActivity.this, vehicleModelDetailses);
            listview.setAdapter(ModelCustomBaseAdapter);
            Button mButton = (Button) root.findViewById(R.id.cancelBtn);


            mButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    // mFrameLayout.setVisibility(View.GONE);
                    // layout.setBackgroundColor(Color.WHITE);
                    dismiss();

                }
            });

            this.setContentView(root);
        }

    }

    public class CustomBaseAdapter extends BaseAdapter {
        Context context;
        private ArrayList<VehicleDetails> mVehicleDetailses;

        public CustomBaseAdapter(Context context, ArrayList<VehicleDetails> mVehicleDetailses) {
            this.mVehicleDetailses = mVehicleDetailses;
            this.context = context;

        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.list_item, null);
                holder = new ViewHolder();

                holder.txtTitle = (TextView) convertView.findViewById(R.id.title);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }


            holder.txtTitle.setText(mVehicleDetailses.get(position).getVehicle_name());

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    vehicleId = mVehicleDetailses.get(position).getVehicle_id();
                    Log.e("bike name",mVehicleDetailses.get(position).getVehicle_name() );
                    //tvBikeName.setText(mVehicleDetailses.get(position).getVehicle_name() + " / ");
                    tvBikeName.setText(mVehicleDetailses.get(position).getVehicle_name());
                    dw.dismiss();
                    BikeId = vehicleId;
                    strBikeName = mVehicleDetailses.get(position).getVehicle_name();
                    showFilterDialog(strBikeName, strBikeModel, strBikeYear);
                    pDialog.show();

                }
            });
            return convertView;
        }

        @Override
        public int getCount() {
            return mVehicleDetailses.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        private class ViewHolder {

            TextView txtTitle;

        }
    }


public  class TimeBaseAdapter extends BaseAdapter {

    Context context;
    private ArrayList<Integer> mVehicleDetailses;

    public TimeBaseAdapter(Context context, ArrayList<Integer> mVehicleDetailses) {
        this.mVehicleDetailses = mVehicleDetailses;
        this.context = context;

    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item, null);
            holder = new ViewHolder();

            holder.txtTitle = (TextView) convertView.findViewById(R.id.title);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.txtTitle.setText("" + mVehicleDetailses.get(position));

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tvBikeYear.setText(""+mVehicleDetailses.get(position)+" / ");

                tw.dismiss();
                BikeId = vehicleId;
                strBikeYear = mVehicleDetailses.get(position).toString();
                showFilterDialog(strBikeName, strBikeModel, strBikeYear);
                pDialog.show();

            }
        });
        return convertView;
    }

    @Override
    public int getCount() {
        return mVehicleDetailses.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {

        TextView txtTitle;

    }
}

    //=========== year entry==========={
    private void modelYears(){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int lastYear=  year-1990;
        Log.e("last year",""+( year-1990));
        for(int i=lastYear;i>=0;i--){
            arrayYear.add((year-i));
        }
    }
}
