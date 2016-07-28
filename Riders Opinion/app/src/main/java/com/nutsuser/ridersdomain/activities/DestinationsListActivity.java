package com.nutsuser.ridersdomain.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.adapter.AdapterDestination;
import com.nutsuser.ridersdomain.adapter.CustomGridAdapter;
import com.nutsuser.ridersdomain.chat.SampleCometChatActivity;
import com.nutsuser.ridersdomain.services.GPSService;
import com.nutsuser.ridersdomain.utils.CustomizeDialog;
import com.nutsuser.ridersdomain.utils.PrefsManager;
import com.nutsuser.ridersdomain.view.BetterPopupWindow;
import com.nutsuser.ridersdomain.view.CustomDialog;
import com.nutsuser.ridersdomain.web.api.FileUploadService;
import com.nutsuser.ridersdomain.web.api.ServiceGenerator;
import com.nutsuser.ridersdomain.web.pojos.RidingDestination;
import com.nutsuser.ridersdomain.web.pojos.RidingDestinationDetails;
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
 * Created by user on 8/28/2015.
 */
public class DestinationsListActivity extends BaseActivity {
    // drop down code
    View view;
    private ArrayList<String> mVehicleDetailses = new ArrayList<String>();
    CustomBaseAdapter adapter;
    DemoPopupWindow dw;
    @Bind(R.id.tvBikeName)
    TextView tvBikeName;
    public static String type;


    public static String[] prgmNameList = {"My Rides", "My Messages", "My Friends", "Chats", "Favorites", "Notifications", "Settings", "Riders Near By"};
    public static int[] prgmImages = {R.drawable.ic_menu_my_rides, R.drawable.ic_menu_my_messages, R.drawable.menu_my_friends, R.drawable.ic_menu_menu_chats, R.drawable.menu_fav_destinations, R.drawable.ic_menu_menu_notifications, R.drawable.menu_settings, R.drawable.icon_nearby};
    public static Class[] classList = {MyRidesRecyclerView.class, RecentChatListActivity.class,MyFriendsActivity.class, ComingSoon.class, FavouriteDesination.class, NotificationListActivity.class, SettingsActivity.class, NearByFriendsAcitivity.class};
    public static ArrayList<RidingDestinationDetails> mRidingDestinationDetailses = new ArrayList<RidingDestinationDetails>();
    PrefsManager prefsManager;
    CustomizeDialog mCustomizeDialog;
    String AccessToken, UserId;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tvTitleToolbar)
    TextView tvTitleToolbar;
    @Bind(R.id.tvName)
    TextView tvName;
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
    @Bind(R.id.rvDestinations)
    RecyclerView rvDestinations;
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.lvSlidingMenu)
    LinearLayout lvSlidingMenu;
    @Bind(R.id.gridView1)
    GridView gridView1;
    @Bind(R.id.edSearch)
    EditText edSearch;
    @Bind(R.id.btFullProfile)
    Button btFullProfile;
    @Bind(R.id.tvGetDirection)
    TextView tvGetDirection;
    //public static String [] prgmNameList={"Riding Destinations","Meet 'N' Plan A Ride","Riding Events \n    ","Modifly Your Bikes","Healthy Riding","Get Directions","Notifications","Settings"};
    //public static int [] prgmImages={R.drawable.icon_menu_destination,R.drawable.icon_menu_meetplan,R.drawable.icon_menu_events,R.drawable.icon_modifybike,R.drawable.icon_menu_healthy_riding,R.drawable.icon_menu_get_direction,R.drawable.icon_menu_notification,R.drawable.icon_menu_settings};
    //public static Class [] classList={DestinationsListActivity.class,PlanRideActivity.class,EventsListActivity.class,ModifyBikeActivity.class,HealthyRidingActivity.class,GetDirections.class,NotificationScreen.class,SettingsActivity.class};
    double start1, end1;
    String star_lat, star_long;
    private Activity activity;
    @Bind(R.id.sdvDp)
    SimpleDraweeView sdvDp;
    @Bind(R.id.ivSearch)
    ImageView ivSearch;
    @Bind(R.id.btUpdateProfile)
    Button btUpdateProfile;
    private ActionBarDrawerToggle mDrawerToggle;
    @Bind(R.id.lllayoutSort)
    LinearLayout lllayoutSort;
    @Bind(R.id.fab_destination)
    FloatingActionButton fab_destination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destinations_list);
        try {
            activity = DestinationsListActivity.this;
            view = new View(this);
            ButterKnife.bind(activity);
            setupActionBar(toolbar);
            setFontsToTextViews();
            mDrawerLayout.closeDrawer(lvSlidingMenu);
            rvDestinations.setLayoutManager(new LinearLayoutManager(activity));
            fab_destination.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String eventid = new PrefsManager(DestinationsListActivity.this).getEventId();
                    Intent intent = new Intent(activity, TrackingScreen.class);
                    intent.putExtra("eventId", eventid);
                    startActivity(intent);
                }
            });


            //=============set Navigation drawer list items click listener=========//
            gridView1.setAdapter(new CustomGridAdapter(this, prgmNameList, prgmImages));
            gridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                    if (position == 1) {
//                    intent_Calling(classList[position], "My Messages");
//                }
//                    else {
                    startActivity(new Intent(DestinationsListActivity.this, classList[position]));

                    //  }
                }

            });

            //=============== Get Location using GPS=============//
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
                if (isNetworkConnected()) {
                    RidingListmodelinfo();
                } else {
                    showToast("Internet Not Connected");
                }

            }


            // make sure you close the gps after using it. Save user's battery power
            mGPSService.closeGPS();
            edSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                        //do something
                        edSearch.clearFocus();
                        if (edSearch.getText().toString().length() > 1) {
                            //edSearch.setText("");
                            hideKeyboard();
                        }

                        RidingListmodelinfosearch(edSearch.getText().toString().trim());
                    }

                    return false;
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
                    String imageUrl = prefsManager.getImageUrl();
                    sdvDp.setImageURI(Uri.parse(imageUrl));
//                File file = new File(imageUrl);
//                sdvDp.setImageURI(Uri.fromFile(file));

                    // Toast.makeText(DestinationsListActivity.this, "Drawer Opened....", Toast.LENGTH_SHORT).show();
                    //getActionBar().setTitle(mDrawerTitle);
                    // calling onPrepareOptionsMenu() to hide action bar icons
                    //invalidateOptionsMenu();
                }
            };
//         mDrawerLayout.setDrawerListener(mDrawerToggle);

            mDrawerLayout.closeDrawer(lvSlidingMenu);
            showProfileImage();

            edSearch.addTextChangedListener(new TextWatcher() {

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
                    if (edSearch.getText().toString().length() > 1) {
                        edSearch.setText("");
                        hideKeyboard();
                    }
                }
            });
            tvBikeName.setText("Nearest");
            type = "Nearest";
            if (new PrefsManager(this).isServicesRunning()) {
                fab_destination.setVisibility(View.VISIBLE);
            } else {
                fab_destination.setVisibility(View.GONE);
            }
        }catch(Exception e){
            Rollbar.reportException(e, "minor", "DestinationListActivity OnCreate()");
        }

    }

    //=========== function to hide keyborad=============//
    private void hideKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    //===================== function to move activity to activity===========//
    public void intent_Calling(Class name, String na) {
        Intent mIntent = new Intent(DestinationsListActivity.this, name);
        mIntent.putExtra(SCREEN_OPEN, na);
        startActivity(mIntent);

    }


    /**
     * '
     * Show Profile Image
     ***/
    private void showProfileImage() {
        try {
            if (prefsManager.getUserName() == null) {
                tvName.setText("No Name");
            } else {
                tvName.setText(prefsManager.getUserName());
            }
            if (prefsManager.getImageUrl() == null) {
               // Toast.makeText(DestinationsListActivity.this, "Image Address is Null", Toast.LENGTH_SHORT).show();
            } else {
                String imageUrl = prefsManager.getImageUrl();
                sdvDp.setImageURI(Uri.parse(imageUrl));
//                File file = new File(imageUrl);
//                sdvDp.setImageURI(Uri.fromFile(file));
            }
        }catch(NullPointerException e){
            Rollbar.reportException(e, "minor", "DestinationListActivity showProfileImage");

        }

    }


    //================ function to set fonts on textView=============//
    private void setFontsToTextViews() {
        tvTitleToolbar.setTypeface(typeFaceMACHINEN);
        tvName.setTypeface(typeFaceMACHINEN);
        tvGetDirection.setTypeface(typeFaceLatoHeavy);
        tvBikeName.setTypeface(typeFaceLatoHeavy);
    }

    //=========== set menus on actionBar====================//
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(DestinationsListActivity.this, MainScreenActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("MainScreenResponse", "OPEN");
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(DestinationsListActivity.this, MainScreenActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("MainScreenResponse", "OPEN");
        startActivity(intent);
        finish();
    }

    //================== set click listners on Acion Bar items=============//
    @OnClick({R.id.ivFilter, R.id.ivMenu, R.id.tvGetDirection, R.id.btFullProfile, R.id.btUpdateProfile, R.id.lllayoutSort})
    void onclick(View view) {
        switch (view.getId()) {
            case R.id.ivFilter:
                startActivity(new Intent(DestinationsListActivity.this, FilterActivity.class));
                break;
            case R.id.lllayoutSort:
                dw = new DemoPopupWindow(view, DestinationsListActivity.this);
                dw.showLikeQuickAction(0, 0);
                break;
            case R.id.ivMenu:
                if (mDrawerLayout.isDrawerOpen(lvSlidingMenu))
                    mDrawerLayout.closeDrawer(lvSlidingMenu);
                else
                    mDrawerLayout.openDrawer(lvSlidingMenu);
                break;
            case R.id.tvGetDirection:
                startActivity(new Intent(activity, GetDirections.class));
                break;
           /* case R.id.rlProfile:
                startActivity(new Intent(activity, ProfileActivity.class));
                break;*/
            case R.id.btUpdateProfile:
                startActivity(new Intent(activity, ProfileActivity.class));
                break;
            case R.id.btFullProfile:
                startActivity(new Intent(activity, PublicProfileScreen.class));
                break;

        }
    }

    /**
     * RidingListmodelinfo info .
     */
    public void RidingListmodelinfo() {
        showProgressDialog();

        try {
            prefsManager = new PrefsManager(DestinationsListActivity.this);
            AccessToken = prefsManager.getToken();
            UserId = prefsManager.getCaseId();
            String radius = prefsManager.getRadius();
            FileUploadService service = ServiceGenerator.createService(FileUploadService.BASE_URL);
            Log.e("URL:", "" + FileUploadService.BASE_URL);
            Log.e("UserId:", "" + UserId);
            Log.e("star_long:", "" + star_long);
            Log.e("star_lat:", "" + star_lat);
            Log.e("radius:", "" + radius);
            Log.e("AccessToken:", "" + AccessToken);
            String sorttype=tvBikeName.getText().toString();
            if (sorttype.toString().toLowerCase().matches("top rated")) {
                sorttype = "toprated";
            }
           /* else{
                sorttype=sorttype.toLowerCase();
            }*/

            service.ridingDestination(UserId, star_long, star_lat, radius, AccessToken, sorttype.toLowerCase(), new Callback<JsonObject>() {
                @Override
                public void success(JsonObject jsonObject, retrofit.client.Response response) {

                    Type type = new TypeToken<RidingDestination>() {
                    }.getType();
                    RidingDestination mRidingDestination = new Gson().fromJson(jsonObject.toString(), type);
                    Log.e("riding destination:", "" + jsonObject);
                    mRidingDestinationDetailses.clear();
                    dismissProgressDialog();
                    if (mRidingDestination.getSuccess().equals("1")) {

                        Log.e("Dest list",mRidingDestination.getData()+"");
                        mRidingDestinationDetailses.addAll(mRidingDestination.getData());
                        rvDestinations.setAdapter(new AdapterDestination(DestinationsListActivity.this, mRidingDestinationDetailses));
                    } else {
                        CustomDialog.showProgressDialog(DestinationsListActivity.this, mRidingDestination.getMessage().toString());
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    dismissProgressDialog();
                    Log.d("DataUploading------", "Data Uploading Failure......" + error);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            Rollbar.reportException(e, "minor", "DestinationListActivity RidingListmodelinfo API");

        }
    }


    /**
     * RidingListmodelinfo info .
     */
    public void RidingListsortinfo(String sorttype) {
        showProgressDialog();

        try {
            Log.e("Lower:", "" + sorttype.toString().toLowerCase());
            if (sorttype.toString().toLowerCase().matches("top rated")) {
                sorttype = "toprated";
            }
            prefsManager = new PrefsManager(DestinationsListActivity.this);
            AccessToken = prefsManager.getToken();
            UserId = prefsManager.getCaseId();
            String radius = prefsManager.getRadius();
            FileUploadService service = ServiceGenerator.createService(FileUploadService.BASE_URL);
          /*  Log.e("URL:",""+FileUploadService.BASE_URL);
            Log.e("UserId:",""+UserId);
            Log.e("star_long:",""+star_long);
            Log.e("star_lat:",""+star_lat);
            Log.e("radius:",""+radius);
            Log.e("AccessToken:",""+AccessToken);*/

            service.ridingDestinationSort(UserId, star_long, star_lat, radius, AccessToken, sorttype.toLowerCase(), new Callback<JsonObject>() {
                @Override
                public void success(JsonObject jsonObject, retrofit.client.Response response) {

                    Type type = new TypeToken<RidingDestination>() {
                    }.getType();
                    RidingDestination mRidingDestination = new Gson().fromJson(jsonObject.toString(), type);
                    Log.e("sort destination:", "" + jsonObject);
                    mRidingDestinationDetailses.clear();
                    dismissProgressDialog();
                    if (mRidingDestination.getSuccess().equals("1")) {
                        mRidingDestinationDetailses.addAll(mRidingDestination.getData());
                        rvDestinations.setAdapter(new AdapterDestination(DestinationsListActivity.this, mRidingDestinationDetailses));
                    } else {
                        CustomDialog.showProgressDialog(DestinationsListActivity.this, mRidingDestination.getMessage().toString());
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    dismissProgressDialog();
                    Log.d("DataUploading------", "Data Uploading Failure......" + error);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            Rollbar.reportException(e, "minor", "DestinationListActivity RidingListsortinfo API");

        }
    }


    /**
     * Search info .
     */
    public void RidingListmodelinfosearch(String search) {

        //http://ridersopininon.herokuapp.com/index.php/ridingDestination/search?search=shimla&userId=105&longitude=76.70740061&latitude=30.7104346&radius=2000&accessToken=764bb308d8e4967b8183969ca709a483
        showProgressDialog();
        Log.e("riding destination", "riding destination");
        try {
            prefsManager = new PrefsManager(DestinationsListActivity.this);
            AccessToken = prefsManager.getToken();
            UserId = prefsManager.getCaseId();
            Log.e("AccessToken:", "" + AccessToken + "----UserId----" + UserId+"=----"+star_long+"mnbxdvhnhb"+star_lat);
            //edSearch.setText("");
            FileUploadService service = ServiceGenerator.createService(FileUploadService.BASE_URL);

            service.ridingDestinationsearch(search, UserId, star_long, star_lat, AccessToken, new Callback<JsonObject>() {
                @Override
                public void success(JsonObject jsonObject, retrofit.client.Response response) {

                    Type type = new TypeToken<RidingDestination>() {
                    }.getType();
                    RidingDestination mRidingDestination = new Gson().fromJson(jsonObject.toString(), type);
                    mRidingDestinationDetailses.clear();
                    dismissProgressDialog();
                    if (mRidingDestination.getSuccess().equals("1")) {
                        mRidingDestinationDetailses.addAll(mRidingDestination.getData());
                        rvDestinations.setAdapter(new AdapterDestination(DestinationsListActivity.this, mRidingDestinationDetailses));
                    } else {
                        CustomDialog.showProgressDialog(DestinationsListActivity.this, mRidingDestination.getMessage().toString());
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    dismissProgressDialog();
                    Log.d("DataUploading------", "Data Uploading Failure......" + error);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            Rollbar.reportException(e, "minor", "DestinationListActivity RidingListmodelinfosearch API");
        }
    }




    @Override
    protected void onResume() {
        super.onResume();
        if (FilterActivity.filter) {
            FilterActivity.filter = false;
            rvDestinations.setAdapter(new AdapterDestination(DestinationsListActivity.this, mRidingDestinationDetailses));
            if (mRidingDestinationDetailses.size() == 0) {
                CustomDialog.showProgressDialog(DestinationsListActivity.this, "Oops didn't found the destination, as no ride is planned till now.");
            }

        }
        if (mDrawerLayout.isDrawerOpen(lvSlidingMenu)) {
            showProfileImage();
            mDrawerLayout.closeDrawer(lvSlidingMenu);
        }
        if (DestinationsDetailActivity.update) {
            DestinationsDetailActivity.update = false;
            if (isNetworkConnected()) {
                RidingListmodelinfo();
            } else {
                showToast("Internet Not Connected");
            }
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
            mVehicleDetailses.clear();
            mVehicleDetailses.add("Nearest");
            mVehicleDetailses.add("Furthest");
            mVehicleDetailses.add("Top Rated");

            // inflate layout
            LayoutInflater inflater = (LayoutInflater) this.anchor.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            ViewGroup root = (ViewGroup) inflater.inflate(
                    R.layout.share_choose_popup, null);

            ListView listview = (ListView) root.findViewById(R.id.listview);
            adapter = new CustomBaseAdapter(DestinationsListActivity.this, mVehicleDetailses);
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

    //============ Adapter to set Vehicles deatilList============//
    public class CustomBaseAdapter extends BaseAdapter {
        Context context;
        private ArrayList<String> mVehicleDetailses;

        public CustomBaseAdapter(Context context, ArrayList<String> mVehicleDetailses) {
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


            holder.txtTitle.setText(mVehicleDetailses.get(position));

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    type = mVehicleDetailses.get(position);
                    tvBikeName.setText(mVehicleDetailses.get(position));
                    GPSService mGPSService = new GPSService(DestinationsListActivity.this);
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
                        if (isNetworkConnected()) {
                            RidingListsortinfo(tvBikeName.getText().toString());
                        } else {
                            showToast("Internet Not Connected");
                        }

                    }


                    // make sure you close the gps after using it. Save user's battery power
                    mGPSService.closeGPS();
                    dw.dismiss();

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

}
