package com.nutsuser.ridersdomain.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.adapter.CustomGridAdapter;
import com.nutsuser.ridersdomain.chat.SampleCometChatActivity;
import com.nutsuser.ridersdomain.utils.CustomizeDialog;
import com.nutsuser.ridersdomain.utils.PrefsManager;
import com.nutsuser.ridersdomain.view.BetterPopupWindow;
import com.nutsuser.ridersdomain.view.CustomDialog;
import com.nutsuser.ridersdomain.web.api.FileUploadService;
import com.nutsuser.ridersdomain.web.api.ServiceGenerator;
import com.nutsuser.ridersdomain.web.pojos.GetSetting;
import com.nutsuser.ridersdomain.web.pojos.GetSettingData;
import com.nutsuser.ridersdomain.web.pojos.VehicleModelDetails;
import com.rollbar.android.Rollbar;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;

/**
 * Created by user on 9/29/2015.
 */
public class SettingsActivity extends BaseActivity {
    @Bind(R.id.tvTitleToolbar)
    TextView tvTitleToolbar;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tvLabelDestinations)
    TextView tvLabelDestinations;
    @Bind(R.id.tvLabelEvents)
    TextView tvLabelEvents;
    @Bind(R.id.tvLabelCustomisations)
    TextView tvLabelCustomisations;
    @Bind(R.id.tvLabelDistance)
    TextView tvLabelDistance;
    @Bind(R.id.tvLabelSelectDistance)
    TextView tvLabelSelectDistance;
    @Bind(R.id.tvDeleteOldNotification)
    TextView tvDeleteOldNotification;
    @Bind(R.id.tvLabelNotifications)
    TextView tvLabelNotifications;
    @Bind(R.id.tvLabelOptions)
    TextView tvLabelOptions;
    @Bind(R.id.tv_Distance_final)
    TextView tv_Distance_final;
    @Bind(R.id.tvLabelFriends)
    TextView tvLabelFriends;
    @Bind(R.id.tvLabelChat)
    TextView tvLabelChat;
    @Bind(R.id.seekBar1)
    SeekBar seekBar1;
    @Bind(R.id.tvUpdate)
    TextView tvUpdate;
    PrefsManager prefsManager;
    @Bind(R.id.toggleFBAutoShare)
    ToggleButton toggleFBAutoShare;
    @Bind(R.id.toggleLabelEvents)
    ToggleButton toggleLabelEvents;
    @Bind(R.id.toggleCustomisations)
    ToggleButton toggleCustomisations;
    @Bind(R.id.toggleDistance)
    ToggleButton toggleDistance;
    @Bind(R.id.toggleMain)
    ToggleButton toggleMain;
    @Bind(R.id.toggleFriends)
    ToggleButton toggleFriends;
    @Bind(R.id.toggleChat)
    ToggleButton toggleChat;

    String destination, customization, event,distanceParameter="1", chat,friends,main;
    CustomizeDialog mCustomizeDialog;
    GetSettingData getSettingData;
    @Bind(R.id.tvdays)
    TextView tvdays;
    ModelPopupWindow mdw;
    private ArrayList<String> numberOfDays;

    //=========== slider items====================//
    public static String[] prgmNameList = {"My Rides", "My Messages", "My Friends", "Chats", "Favorites", "Notifications", "Settings", "Riders Near By"};
    public static int[] prgmImages = {R.drawable.ic_menu_my_rides, R.drawable.ic_menu_my_messages, R.drawable.menu_my_friends, R.drawable.ic_menu_menu_chats, R.drawable.menu_fav_destinations, R.drawable.ic_menu_menu_notifications, R.drawable.menu_settings, R.drawable.icon_nearby};
    public static Class[] classList = {MyRidesRecyclerView.class, RecentChatListActivity.class,MyFriendsActivity.class, ComingSoon.class, FavouriteDesination.class, NotificationListActivity.class, SettingsActivity.class, NearByFriendsAcitivity.class};

    @Bind(R.id.gridView1)
    GridView gridView1;
    private ActionBarDrawerToggle mDrawerToggle;
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.lvSlidingMenu)
    LinearLayout lvSlidingMenu;
    @Bind(R.id.sdvDp)
    SimpleDraweeView sdvDp;
    @Bind(R.id.btUpdateProfile)
    Button btUpdateProfile;
    @Bind(R.id.btFullProfile)
    Button btFullProfile;
    @Bind(R.id.tvName)
    TextView tvName;
//=====================================//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        try {
            ButterKnife.bind(this);
            setupActionBar();
            setFontsToTextViews();
            prefsManager = new PrefsManager(this);
            if (isNetworkConnected()) {
                getupdatesetting();
            } else {
                showToast("Internet Not Connected");
            }


                toggleFBAutoShare.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                            if (isChecked) {
                                if (main.equals("0")) {
                                    destination = "1";
                                    main = "1";
                                    toggleMain.setChecked(true);
                                }else{
                                    destination = "1";
                                }
                            } else {
                                destination = "0";
                            }
                        }

                });
                toggleLabelEvents.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        if (isChecked) {
                            if (main.equals("0")) {
                                event = "1";
                                main = "1";
                                toggleMain.setChecked(true);
                            }else{
                                event = "1";
                            }

                        } else {
                            event = "0";
                        }

                    }
                });
                toggleCustomisations.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        if (isChecked) {
                            if (main.equals("0")) {
                                customization = "1";
                                main = "1";
                                toggleMain.setChecked(true);
                            }else{
                                customization = "1";
                            }

                        } else {
                            customization = "0";
                        }

                    }
                });
                toggleFriends.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        if (isChecked) {
                            if (main.equals("0")) {
                                friends = "1";
                                main = "1";
                                toggleMain.setChecked(true);
                            }else{
                                friends = "1";
                            }

                        } else {
                            friends = "0";
                        }

                    }
                });
                toggleChat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        if (isChecked) {
                            if (main.equals("0")) {
                                chat = "1";
                                main = "1";
                                toggleMain.setChecked(true);
                            }else{
                                chat = "1";
                            }

                        } else {
                            chat = "0";
                        }

                    }
                });



            toggleMain.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        main = "1";
                        Log.e("main destination", "destination");
                        if(friends.equals("0")&&chat.equals("0")&&event.equals("0")&&customization.equals("0")&&destination.equals("0")){
                            Log.e("main", "if");
                            toggleFriends.setChecked(true);
                            toggleChat.setChecked(true);
                            toggleCustomisations.setChecked(true);
                            toggleLabelEvents.setChecked(true);
                            toggleFBAutoShare.setChecked(true);
//                            friends = "1";
//                            chat = "1";
//                            event = "1";
//                            customization = "1";
//                            destination = "1";
                        }else{
                            Log.e("main", "else");
                            if(friends.equals("0")){
                                toggleFriends.setChecked(false);
                               // friends = "0";
                            }else{
                                toggleFriends.setChecked(true);
                               // friends = "1";
                            }
                            if(chat.equals("0")){
                                toggleChat.setChecked(false);
                               // chat = "0";
                            }else{
                                toggleChat.setChecked(true);
                              //  chat = "1";
                            }
                            if(event.equals("0")){
                                toggleLabelEvents.setChecked(false);
                              //  event = "0";
                            }else{
                                toggleLabelEvents.setChecked(true);
                              //  event = "1";
                            }
                            if(customization.equals("0")){
                                toggleCustomisations.setChecked(false);
                               // customization = "0";
                            }else{
                                toggleCustomisations.setChecked(true);
                              //  customization = "1";
                            }
                            if(destination.equals("0")){
                                toggleFBAutoShare.setChecked(false);
                               // destination = "0";
                            }else{
                                toggleFBAutoShare.setChecked(true);
                               // destination = "1";
                            }
                        }
                    } else {
                        main = "0";
                        Log.e("main toggle2", main);
                        toggleMain.setChecked(false);
                        toggleFriends.setChecked(false);
                        toggleChat.setChecked(false);
                        toggleCustomisations.setChecked(false);
                        toggleLabelEvents.setChecked(false);
                        toggleFBAutoShare.setChecked(false);
                        friends = "0";
                        chat = "0";
                        event = "0";
                        customization = "0";
                        destination = "0";
                    }
                }
            });


            Log.e("dist parameter",prefsManager.getDistanceParameter());
            if (prefsManager.getDistanceParameter().trim().equalsIgnoreCase("M")) {
                toggleDistance.setChecked(true);
            } else if(prefsManager.getDistanceParameter().trim().equalsIgnoreCase("KM")){
                toggleDistance.setChecked(false);
            }

            toggleDistance.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    try {
                        if (isChecked) {
                            distanceParameter = "1";
                            prefsManager.setDistanceParameter("M");
                            tv_Distance_final.setText(seekBar1.getProgress() + " M");
                        } else {
                            distanceParameter = "0";
                            prefsManager.setDistanceParameter("KM");
                            tv_Distance_final.setText(getDistanceKilometer(seekBar1.getProgress()) + " KM");
                        }
                    } catch (NullPointerException e) {
                        distanceParameter = "1";
                        prefsManager.setDistanceParameter("M");
                    }
                }
            });
            seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                int progress = 0;


                @Override

                public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {

                    progress = progresValue;

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                    if (distanceParameter.equals("1")) {
                        tv_Distance_final.setText("" + seekBar.getProgress() + " M");
                    } else {
//                    distanceParameter = "0";
//                    prefsManager.setDistanceParameter("KM");
                        tv_Distance_final.setText(getDistanceKilometer(seekBar.getProgress()) + " KM");
                    }

                    prefsManager.setDistanceSettings("" + seekBar.getProgress());
                    Log.e("getProgress() settings", "" + seekBar.getProgress());

                }

            });
            // seekBar1.setProgress(Integer.parseInt(getSettingData.getDistanceTo()));
            tvUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                  if (main.equals("1")) {
                      updatesetting();
                    } else {
                      if(destination.equals("0")&&event.equals("0")&&friends.equals("0")&&customization.equals("0")&&chat.equals("0")){
                          updatesetting();
                      }else{
                          showToast("Please Turn ON the Main Notification button.");
                      }
                    }
                }
            });
            setSliderMenu();
            setDrawerSlider();
        }catch(Exception e){
            Rollbar.reportException(e, "minor", "Setting activity on create");
        }
    }


    private void setupActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_home);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void setFontsToTextViews() {
        tvTitleToolbar.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/MACHINEN.TTF"));
        tvDeleteOldNotification.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/Lato-Heavy.ttf"));
        tvLabelDistance.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/Lato-Bold.ttf"));
        tvLabelDestinations.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/Lato-Heavy.ttf"));
        tvLabelEvents.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/Lato-Heavy.ttf"));
        tvLabelCustomisations.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/Lato-Heavy.ttf"));
        tvLabelDistance.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/Lato-Heavy.ttf"));
        tvLabelNotifications.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/Lato-Heavy.ttf"));
        tvLabelOptions.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/Lato-Heavy.ttf"));
        tv_Distance_final.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/Lato-Bold.ttf"));
        tvLabelChat.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/Lato-Bold.ttf"));
        tvLabelFriends.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/Lato-Bold.ttf"));
        tvdays.setPaintFlags(tvdays.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvdays.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/Lato-Bold.ttf"));
       // tvDistance.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/Lato-Bold.ttf"));
        tvUpdate.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/Lato-Bold.ttf"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(SettingsActivity.this, MainScreenActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("MainScreenResponse", "OPEN");
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * Update Setting Data (Only Strings) to Server
     **/
    private void updatesetting() {
        try {
            Log.e("main toggle3", main);
            String[] days = tvdays.getText().toString().split(" ");
            showProgressDialog();
            String userId = prefsManager.getCaseId();
            String distanc_to = "" + seekBar1.getProgress();
            String distanc_from = "1";
            String accessToken = prefsManager.getToken();
            Log.e("userId:", "" + userId);
            Log.e("accessToken:", "" + accessToken);
            Log.e("destination:", "" + destination);
            Log.e("event:", "" + event);
            Log.e("customization:", "" + customization);
            Log.e("distanc_to:", "" + distanc_to);
            Log.e("distanc_from:", "" + distanc_from);
            Log.e("chat:", "" + chat);
            Log.e("friendsAlert:", "" + friends);
            Log.e("deleteDays:", "" + days[0]);
            Log.e("mainNotification:", "" + main);

            FileUploadService service = ServiceGenerator.createService(FileUploadService.BASE_URL);

            service.updateSetting(userId, accessToken, destination, event, customization, distanc_to, distanc_from, chat, days[0], friends, main, new Callback<JsonObject>() {
                @Override
                public void success(JsonObject jsonObject, retrofit.client.Response response) {
                    Log.e("jsonObject:", "" + jsonObject);
                    dismissProgressDialog();
                      /*  Type type = new TypeToken<ProfileUpdateData>() {
                        }.getType();
                        ProfileUpdateData
                                profileJsonData = new Gson().fromJson(jsonObject.toString(), type);
                        Log.e("jsonObject:", "" + jsonObject);
                        Log.e("profileJsonData:", "" + profileJsonData);
                        if (profileJsonData.getSuccess() == 1) {
                            prefsManager.setUserName(etProfileName.getText().toString() + " " + etName.getText().toString());
                            Log.d("DataUploading------", "Data Uploading Done..." + jsonObject.toString());
                            Log.d("DataUploadingResponse", "Data Uploading Done..." + response.toString());
                            dismissProgressDialog();
                            showMesaage("Profile Updated");
                        } else {
                            dismissProgressDialog();
                            showMesaage("" + profileJsonData.getMessage());
                        }*/

                }

                @Override
                public void failure(RetrofitError error) {
                    dismissProgressDialog();
                    Log.e("DataUploading------", "Data Uploading Failure......" + error);
                }
            });
        }catch(Exception e){
            Rollbar.reportException(e, "minor", "Setting activity update API ");
        }
    }

    /**
     * Get Setting Data(Only Strings) to Server
     **/
    private void getupdatesetting() {
        try {
    showProgressDialog();

    String userId = prefsManager.getCaseId();
    String accessToken = prefsManager.getToken();

    FileUploadService service = ServiceGenerator.createService(FileUploadService.BASE_URL);

    service.getSetting(userId, accessToken, new Callback<JsonObject>() {
        @Override
        public void success(JsonObject jsonObject, retrofit.client.Response response) {


            Type type = new TypeToken<GetSetting>() {
            }.getType();
            GetSetting
                    getSetting = new Gson().fromJson(jsonObject.toString(), type);
            Log.e("jsonObject:", "" + jsonObject);

            if (getSetting.getSuccess() == 1) {
                getSettingData = getSetting.getData();
                destination = getSettingData.getDestination();
                event = "" + getSettingData.getEvent();
                customization = getSettingData.getCustomization();
                try {
                    main = getSettingData.getMainNotification().trim();
                } catch (NullPointerException e) {

                }
                chat = getSettingData.getChatMessage();
                friends = getSettingData.getFriendsAlert();
                if (!getSettingData.getDeleteDays().isEmpty()) {
                    tvdays.setText(getSettingData.getDeleteDays().trim() + " Days");
                }

                Log.e("destination:", "" + destination);
                Log.e("event:", "" + event);
                Log.e("customization:", "" + customization);
                Log.e("main_notification:", "" + main);
                Log.e("friendsAlert:", "" + friends);
                Log.e("chat:", "" + chat);

                if (main.equalsIgnoreCase("1")) {
                    toggleMain.setChecked(true);
                } else {
                    toggleMain.setChecked(false);
                }
                if (destination.equals("1")) {

                    toggleFBAutoShare.setChecked(true);
                } else {
                    toggleFBAutoShare.setChecked(false);
                }
                if (event.equals("1")) {
                    toggleLabelEvents.setChecked(true);
                } else {
                    toggleLabelEvents.setChecked(false);
                }
                if (customization.equals("1")) {
                    toggleCustomisations.setChecked(true);
                } else {
                    toggleCustomisations.setChecked(false);
                }

                if (chat.equals("1")) {
                    toggleChat.setChecked(true);
                } else {
                    toggleChat.setChecked(false);
                }

                if (friends.equals("1")) {
                    toggleFriends.setChecked(true);
                } else {
                    toggleFriends.setChecked(false);
                }

                if (prefsManager.getDistanceParameter().trim().equalsIgnoreCase("M")) {
                    toggleDistance.setChecked(true);
                    tv_Distance_final.setText(getSettingData.getDistanceTo() + " M");
                } else {
                    toggleDistance.setChecked(false);
                    tv_Distance_final.setText(getDistanceKilometer(Double.parseDouble(getSettingData.getDistanceTo())) + " KM");
                }
                seekBar1.setProgress(Integer.parseInt(getSettingData.getDistanceTo()));
                //tv_Distance_final.setText(getSettingData.getDistanceTo() + " M");
                prefsManager.setDistanceSettings(getSettingData.getDistanceTo());
                Log.d("DataUploading------", "Data Uploading Done..." + jsonObject.toString());
                Log.d("DataUploadingResponse", "Data Uploading Done..." + response.toString());
                dismissProgressDialog();
                // CustomDialog.showProgressDialog(SettingsActivity.this, getSetting.getMessage().toString());
            } else {
                dismissProgressDialog();
                CustomDialog.showProgressDialog(SettingsActivity.this, getSetting.getMessage().toString());
            }

        }

        @Override
        public void failure(RetrofitError error) {
            dismissProgressDialog();
            Log.d("Data UpDate------", "Data UpDate Failure......" + error);
        }
    });
}catch(Exception e){
    Rollbar.reportException(e, "minor", "Setting activity getupdatesettings API ");
}
    }

    //============ function to ser Drawer=================//
    private void setDrawerSlider(){
        mDrawerLayout.closeDrawer(lvSlidingMenu);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.icon_image_view, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ) {
            public void onDrawerClosed(View view) {

            }

            public void onDrawerOpened(View drawerView) {
                showProfileImage();

            }
        };
        mDrawerLayout.closeDrawer(lvSlidingMenu);
        showProfileImage();
    }
    /**
     * '
     * Show Profile Image
     ***/
    private void showProfileImage() {
        if (prefsManager.getUserName() == null) {
            tvName.setText("No Name");
        } else {
            tvName.setText(prefsManager.getUserName());
        }
        if (prefsManager.getImageUrl() == null) {
            // Toast.makeText(MyRidesRecyclerView.this, "Image Address is Null", Toast.LENGTH_SHORT).show();
        } else {
            String imageUrl = prefsManager.getImageUrl();
            sdvDp.setImageURI(Uri.parse(imageUrl));
//            File file = new File(imageUrl);
//            sdvDp.setImageURI(Uri.fromFile(file));
        }
    }

    //============= function to set slider menu============//
    private void setSliderMenu(){
        gridView1.setAdapter(new CustomGridAdapter(this, prgmNameList, prgmImages));
        gridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 6) {
                    mDrawerLayout.closeDrawer(lvSlidingMenu);
                }else {
//                if (position == 1) {
//                    Log.e("positiuon:", "" + classList[position]);
//                    intent_Calling(classList[position], "My Messages");
//                }  else {
                    Log.e("positiuon:", "" + classList[position]);
                    intentCalling(classList[position]);
                    // }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mDrawerLayout.isDrawerOpen(lvSlidingMenu)){
            showProfileImage();
            mDrawerLayout.closeDrawer(lvSlidingMenu);
        }
    }

    //======== function to move on next activty=========//
    public void intentCalling(Class name) {
        Intent mIntent = new Intent(SettingsActivity.this, name);
        startActivity(mIntent);
       // finish();
    }

    public void intent_Calling(Class name, String na) {
        Intent mIntent = new Intent(SettingsActivity.this, name);
        mIntent.putExtra(SCREEN_OPEN, na);
        startActivity(mIntent);
        finish();
    }

    //======== OnClick Listeners=============//
    @OnClick({ R.id.btFullProfile, R.id.btUpdateProfile, R.id.ivMenu, R.id.rlProfile,R.id.tvdays})
    void click(View view) {
        switch (view.getId()) {

            case R.id.btUpdateProfile:
                startActivity(new Intent(SettingsActivity.this, ProfileActivity.class));
                break;
            case R.id.btFullProfile:
                startActivity(new Intent(SettingsActivity.this, PublicProfileScreen.class));
                break;
            case R.id.ivMenu:
                if (mDrawerLayout.isDrawerOpen(lvSlidingMenu))
                    mDrawerLayout.closeDrawer(lvSlidingMenu);
                else
                    mDrawerLayout.openDrawer(lvSlidingMenu);
                break;
            case R.id.rlProfile:
                // startActivity(new Intent(MyFriendsActivity.this, ProfileActivity.class));
                break;
            case R.id.tvdays:

                numberOfDays= new ArrayList<String>();
                numberOfDays.add("5 Days");
                numberOfDays.add("10 Days");
                numberOfDays.add("15 Days");
                numberOfDays.add("20 Days");
                numberOfDays.add("25 Days");
                mdw = new ModelPopupWindow(view, SettingsActivity.this);
                mdw.showLikeQuickAction(0, 0);
                break;


        }
    }


    //////
    // ************** Class for pop-up window **********************

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
            ModelCustomBaseAdapter modelCustomBaseAdapter= new ModelCustomBaseAdapter(SettingsActivity.this, numberOfDays);
            listview.setAdapter(modelCustomBaseAdapter);
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


    public class ModelCustomBaseAdapter extends BaseAdapter {
        Context context;
        private ArrayList<String> numberOfDays;

        public ModelCustomBaseAdapter(Context context, ArrayList<String> numberOfDays) {
            this.numberOfDays = numberOfDays;
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


            holder.txtTitle.setText(numberOfDays.get(position));

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tvdays.setText(numberOfDays.get(position));
                    mdw.dismiss();
                }
            });
            return convertView;
        }

        @Override
        public int getCount() {
            return numberOfDays.size();
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
}
