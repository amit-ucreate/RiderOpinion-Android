package com.nutsuser.ridersdomain.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.adapter.CustomGridAdapter;
import com.nutsuser.ridersdomain.adapter.PlaceArrayAdapter;
import com.nutsuser.ridersdomain.chat.SampleCometChatActivity;
import com.nutsuser.ridersdomain.services.GPSService;
import com.nutsuser.ridersdomain.utils.ApplicationGlobal;
import com.nutsuser.ridersdomain.utils.CustomizeDialog;
import com.nutsuser.ridersdomain.utils.PrefsManager;
import com.nutsuser.ridersdomain.view.BetterPopupWindow;
import com.nutsuser.ridersdomain.web.api.FileUploadService;
import com.nutsuser.ridersdomain.web.api.ServiceGenerator;
import com.nutsuser.ridersdomain.web.api.volley.RequestJsonObject;
import com.nutsuser.ridersdomain.web.pojos.Data;
import com.nutsuser.ridersdomain.web.pojos.ProfileJsonData;
import com.nutsuser.ridersdomain.web.pojos.ProfileUpdateData;
import com.nutsuser.ridersdomain.web.pojos.VehicleDetails;
import com.nutsuser.ridersdomain.web.pojos.VehicleModel;
import com.nutsuser.ridersdomain.web.pojos.VehicleModelDetails;
import com.nutsuser.ridersdomain.web.pojos.VehicleName;
import com.rollbar.android.Rollbar;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.mime.TypedFile;

/**
 * Created by user on 9/29/2015.
 */
public class ProfileActivity extends BaseActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {
    private static final int WRITE_EXTERNAL_PERMISSIONS_REQUEST = 1;
    private static final String LOG_TAG = "ProfileActivity";
    String vehicleId = null,vehicle_type_id;
    String mStringlong, mStringlat;
    private GoogleApiClient mGoogleApiClient;
    private ArrayList<VehicleModelDetails> vehicleModelDetailses = new ArrayList<VehicleModelDetails>();

    Data data;
    private String mImagePath = "";
    CustomizeDialog mCustomizeDialog;
    private ArrayList<VehicleDetails> mVehicleDetailses = new ArrayList<VehicleDetails>();
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    ModelCustomBaseAdapter ModelCustomBaseAdapter;

    @Bind(R.id.etProfileName)
    EditText etProfileName;

    @Bind(R.id.etName)
    EditText etName;
    @Bind(R.id.etAddress)
    AutoCompleteTextView etAddress;
    @Bind(R.id.etPhone)
    EditText etPhone;
    @Bind(R.id.etEmail)
    EditText etEmail;
    @Bind(R.id.etBikeBrand)
    EditText etBikeBrand;
    @Bind(R.id.etBikeModel)
    EditText etBikeModel;
    @Bind(R.id.etBikeYear)
    EditText etBikeYear;
    @Bind(R.id.etDescription)
    EditText etDescription;
    @Bind(R.id.sdvDisplayPicture)
    SimpleDraweeView sdvDisplayPicture;
    @Bind(R.id.ivBannerImage)
    SimpleDraweeView ivBannerImage;
    @Bind(R.id.tvUpdate)
    TextView tvUpdate;
    double start1, end1;
    String star_lat, star_long;
    private Activity activity;
    View view;
    CustomBaseAdapter adapter;
    DemoPopupWindow dw;
    ModelPopupWindow mdw;
    PrefsManager prefsManager;
    int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    boolean imageFlag = false;
   //========================== navigation drawer list items========================//
    public static String[] prgmNameList = {"My Rides", "My Messages", "My Friends", "Chats", "Favorites", "Notifications", "Settings", "Riders Near By"};
    public static int[] prgmImages = {R.drawable.ic_menu_my_rides, R.drawable.ic_menu_my_messages, R.drawable.menu_my_friends, R.drawable.ic_menu_menu_chats, R.drawable.menu_fav_destinations, R.drawable.ic_menu_menu_notifications, R.drawable.menu_settings, R.drawable.icon_nearby};
    public static Class[] classList = {MyRidesRecyclerView.class, RecentChatListActivity.class,MyFriendsActivity.class, ComingSoon.class, FavouriteDesination.class, NotificationListActivity.class, SettingsActivity.class, NearByFriendsAcitivity.class};
    @Bind(R.id.gridView1)
    GridView gridView1;
    @Bind(R.id.btUpdateProfile)
    Button btUpdateProfile;
    @Bind(R.id.btFullProfile)
    Button btFullProfile;
    @Bind(R.id.tvName)
    TextView tvName;
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.sdvDp)
    SimpleDraweeView sdvDp;
    @Bind(R.id.lvSlidingMenu)
    LinearLayout lvSlidingMenu;
    @Bind(R.id.tvTitleToolbar)
    TextView tvTitleToolbar;
    private ActionBarDrawerToggle mDrawerToggle;
    private File profile_image=null;
    boolean imageUpdateFlag = false;

    //==============================================//
    private static final int GOOGLE_API_CLIENT_ID = 0;
    List<ProfileJsonData> jsonData;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));
    private PlaceArrayAdapter mPlaceArrayAdapter;
    File destination = null;

    TimePopupWindow tw;
    TimeBaseAdapter mAdapter;
    ArrayList<Integer> arrayYear = new ArrayList<Integer>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        try {
            activity = this;
            view = new View(this);
            ButterKnife.bind(activity);
            setupActionBar(toolbar);
            modelYears();

            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

            mDrawerLayout.closeDrawer(lvSlidingMenu);

            setFontsToTextViews();
            prefsManager = new PrefsManager(this);

            //==================== google initilization================//
            mGoogleApiClient = new GoogleApiClient.Builder(ProfileActivity.this)
                    .addApi(Places.GEO_DATA_API)
                    .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
                    .addConnectionCallbacks(this)
                    .build();

            //=============== Set Places data in adapter===============//
            etAddress = (AutoCompleteTextView) findViewById(R.id
                    .etAddress);
            etAddress.setThreshold(1);

            etAddress.setOnItemClickListener(mAutocompleteClickListener);
            mPlaceArrayAdapter = new PlaceArrayAdapter(this, android.R.layout.simple_list_item_1,
                    BOUNDS_MOUNTAIN_VIEW, null);
            etAddress.setAdapter(mPlaceArrayAdapter);

            etAddress.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(etAddress, InputMethodManager.SHOW_IMPLICIT);
                    } else {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                }
            });

            //======= set Profile before updation========//
            showProfileBeforeUpdate();

            //=============set Navigation drawer list items click listener=========//
            gridView1.setAdapter(new CustomGridAdapter(this, prgmNameList, prgmImages));
            gridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                    if (position == 1) {
//                        Log.e("positiuon:", "" + classList[position]);
//                        intent_Calling(classList[position], "My Messages");
//                    }  else {
                    Log.e("positiuon:", "" + classList[position]);
                    startActivity(new Intent(ProfileActivity.this, classList[position]));
                    //finish();
                    //  }

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

                }

                public void onDrawerOpened(View drawerView) {
                    showProfileImage();

                }
            };
            mDrawerLayout.closeDrawer(lvSlidingMenu);
            showProfileImage();

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

            }
            // make sure you close the gps after using it. Save user's battery power
            mGPSService.closeGPS();
            getPermissionToWriteExternal();
        }catch(Exception e){
            Rollbar.reportException(e, "minor", "Profile activity on create");
        }
        //===================================================//

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
           // Toast.makeText(ProfileActivity.this, "Image Address is Null", Toast.LENGTH_SHORT).show();
        } else {
            Log.e("Profile image",prefsManager.getImageUrl());
            if(imageUpdateFlag==false) {
                String imageUrl = prefsManager.getImageUrl();
                sdvDp.setImageURI(Uri.parse(imageUrl));
//                File file = new File(imageUrl);
//                sdvDp.setImageURI(Uri.fromFile(file));
            }else{
                sdvDp.setImageURI(Uri.fromFile(profile_image));
            }
        }
    }

    // Called when the user is performing an action which requires the app to read the
    // user's Camera
    @TargetApi(Build.VERSION_CODES.M)
    public void getPermissionToWriteExternal() {
        // 1) Use the support library version ContextCompat.checkSelfPermission(...) to avoid
        // checking the build version since Context.checkSelfPermission(...) is only available
        // in Marshmallow
        // 2) Always check for permission (even if permission has already been granted)
        // since the user can revoke permissions at any time through Settings
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // The permission is NOT already granted.
            // Check if the user has been asked about this permission already and denied
            // it. If so, we want to give more explanation about why the permission is needed.
            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show our own UI to explain to the user why we need to read the contacts
                // before actually requesting the permission and showing the default UI

            }

            // Fire off an async request to actually get the permission
            // This will show the standard permission request dialog UI
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_EXTERNAL_PERMISSIONS_REQUEST);
        } else {
            //openCamera();
        }
    }

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e("ProfileActivity:", "Place query did not complete. Error: " +
                        places.getStatus().toString());
                return;
            }
            // Selecting the first object buffer.
            Place place;

            try {
                place = places.get(0);
            } catch (IllegalStateException e) {
                places.release();
                return;
            }
            CharSequence attributions = places.getAttributions();

            // mNameTextView.setText("NAME:"+ Html.fromHtml(place.getName() + ""));
            // mAddressTextView.setText("ADDRESS: "+Html.fromHtml(place.getAddress() + ""));
            //  mIdTextView.setText(Html.fromHtml("PLACEID:" + place.getId() + ""));
            mStringlong = "" + Html.fromHtml(place.getLatLng().longitude + "");
            mStringlat = "" + Html.fromHtml(place.getLatLng().latitude + "");
            Log.e("Long:", "" + mStringlong);
            Log.e("Latitude:", "" + mStringlat);
            //  mPhoneTextView.setText(Html.fromHtml("Lat:" + place.getLatLng().latitude + "--long:" + latlong));
            //mWebTextView.setText(place.getWebsiteUri() + "");
            if (attributions != null) {
                // mAttTextView.setText(Html.fromHtml(attributions.toString()));
            }
        }

    };
    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            Log.i("ProfileActivity:", "Selected: " + item.description);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            Log.i("ProfileActivity:", "Fetching details for ID: " + item.placeId);
        }
    };

    //============== Intent to move from this activity to another activity=========//
    public void intent_Calling(Class name, String na) {
        Intent mIntent = new Intent(ProfileActivity.this, name);
        mIntent.putExtra(SCREEN_OPEN, na);
        startActivity(mIntent);
        finish();;
    }



    //=============== Set font on TextView===========================//
    private void setFontsToTextViews() {
        tvName.setTypeface(typeFaceMACHINEN);
        tvTitleToolbar.setTypeface(typeFaceMACHINEN);
       // tvName.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
        //   tvAddress.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));

    }


    //===================== Set Menus on ActionBar==========//
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(ProfileActivity.this, MainScreenActivity.class);
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
    protected void onResume() {
        super.onResume();
        if(mDrawerLayout.isDrawerOpen(lvSlidingMenu)){
            showProfileImage();;
            mDrawerLayout.closeDrawer(lvSlidingMenu);
        }
    }

    //============== Set click listener on menu items====================//
    @OnClick({R.id.ivMenu, R.id.rlProfile, R.id.tvUpdate,
            R.id.etBikeBrand, R.id.etBikeModel,R.id.etBikeYear, R.id.sdvDisplayPicture, R.id.ivBannerImage,R.id.btUpdateProfile,R.id.btFullProfile})
    void onclick(View view) {
        switch (view.getId()) {

            case R.id.ivMenu:
                if (mDrawerLayout.isDrawerOpen(lvSlidingMenu))
                    mDrawerLayout.closeDrawer(lvSlidingMenu);
                else
                    mDrawerLayout.openDrawer(lvSlidingMenu);
                break;
            case R.id.rlProfile:
//                if (mDrawerLayout.isDrawerOpen(lvSlidingMenu))
//                    mDrawerLayout.closeDrawer(lvSlidingMenu);
//                else
//                    mDrawerLayout.openDrawer(lvSlidingMenu);
                break;

            case R.id.tvUpdate:
                //Data Validations
                Boolean validateData = checkDataHasEntered();
                if (validateData) {
                    uploadProfileData(star_lat, star_long);
                }
                break;
            case R.id.btUpdateProfile:
                mDrawerLayout.closeDrawer(lvSlidingMenu);
                break;
            case R.id.etBikeBrand:
                if(isNetworkConnected()) {
                    vechicleinfo();
                }else{
                    showToast("Internet is not connected.");
                }
                break;

            case R.id.etBikeModel:
                if (etBikeBrand.getText().toString().trim().length() > 0) {
                    if(isNetworkConnected()) {
                        vechiclemodelinfo();
                    }else{
                        showToast("Internet is not connected.");
                    }
                } else {
                    Toast.makeText(ProfileActivity.this, "Please Enter Bike Brand First ", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.etBikeYear:
                Log.e("bike year","bike year");
//                if(etBikeYear.getText().toString().isEmpty()){
//                showToast("Please enter Bike Model Year.");
//                }else {
                    tw = new TimePopupWindow(view, ProfileActivity.this);
                    tw.showLikeQuickAction(0, 0);
               // }
                break;

            case R.id.ivBannerImage:
                imageFlag = true;
                selectImage();
                break;
            case R.id.sdvDisplayPicture:
                imageFlag = false;
                // profileImageFlag = true;
                selectImage();
                break;
            case R.id.btFullProfile:
                startActivity(new Intent(ProfileActivity.this, PublicProfileScreen.class));
                break;
        }
    }


    /**
     * Upload Data (Only Strings) to Server
     **/
    private void uploadProfileData(String lat, String lon) {

        showProgressDialog();

        String userId = prefsManager.getCaseId();
       final String profileName = etProfileName.getText().toString();

        String name = etName.getText().toString();

        String email = etEmail.getText().toString();

        String phone = etPhone.getText().toString();

        String baseLocation = etAddress.getText().toString();
        StringTokenizer endlocation = new StringTokenizer(baseLocation, ",");
        String tvStartLocation = endlocation.nextToken();

        String latitude = lat;
        String longitude = lon;
        String bikeBrand = etBikeBrand.getText().toString();
        String bikeModel = etBikeModel.getText().toString();
        String bikeYear= etBikeYear.getText().toString();
        String description = etDescription.getText().toString();
        String accessToken = prefsManager.getToken();

        Log.e("profilename",profileName);
        Log.e("name",name);
        Log.e("email",email);
        Log.e("phone",phone);
        Log.e("phone",phone);
        Log.e("phone",phone);

        FileUploadService service = ServiceGenerator.createService(FileUploadService.BASE_URL);

        service.profile_upload_data(userId, profileName, name, email, phone, tvStartLocation, latitude, longitude,
                vehicle_type_id, description,bikeYear, accessToken, new Callback<JsonObject>() {
                    @Override
                    public void success(JsonObject jsonObject, retrofit.client.Response response) {

                        Type type = new TypeToken<ProfileUpdateData>() {
                        }.getType();
                        ProfileUpdateData
                                profileJsonData = new Gson().fromJson(jsonObject.toString(), type);
                        Log.e("jsonObject:", "" + jsonObject);
                        Log.e("profileJsonData:", "" + profileJsonData);
                        if (profileJsonData.getSuccess() == 1) {
                            prefsManager.setUserName(etProfileName.getText().toString());
                            Log.d("DataUploading------", "Data Uploading Done..." + jsonObject.toString());
                            Log.d("DataUploadingResponse", "Data Uploading Done..." + response.toString());
                            dismissProgressDialog();
                            showToast("Profile Updated");
                            prefsManager.setUserName(profileName);
                            prefsManager.setVehicleId(vehicleId);

                            showProfileImage();
                        } else {
                            dismissProgressDialog();
                            showToast("" + profileJsonData.getMessage());
                        }

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        dismissProgressDialog();
                        Log.e("DataUploading------", "Data Uploading Failure......" + error);
                    }
                });
    }

    /**
     * Register info .
     */
    public void vechicleinfo() {
        showProgressDialog();
        Log.e("See", "ENTER");
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(ProfileActivity.this);
            RequestJsonObject loginTaskRequest = new RequestJsonObject(Request.Method.POST,
                    ApplicationGlobal.ROOT + ApplicationGlobal.baseurl_vehicle, null,
                    volleyErrorListener(), volleySuccessListener()
            );

            requestQueue.add(loginTaskRequest);

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    /**
     * Implement success listener on execute api url.
     */
    public Response.Listener<JSONObject> volleySuccessListener() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dismissProgressDialog();

                Type type = new TypeToken<VehicleName>() {
                }.getType();
                VehicleName mVehicleName = new Gson().fromJson(response.toString(), type);
                mVehicleDetailses.clear();

                Log.e("response: ", "" + response);
                if (mVehicleName.getSuccess().equals("1")) {

                    mVehicleDetailses.addAll(mVehicleName.getData());

                    dw = new DemoPopupWindow(view, ProfileActivity.this);
                    dw.showLikeQuickAction(0, 0);
                }

                //adapter.notifyDataSetChanged();
            }
        };
    }

    /**
     * Implement Volley error listener here.
     */
    public Response.ErrorListener volleyErrorListener() {
        // dismissProgressDialog();
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error-----: ", "" + error);
            }
        };
    }


    @Override
    public void onConnected(Bundle bundle) {
        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
        Log.i("", "Google Places API connected.");
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(LOG_TAG, "Google Places API connection failed with error code: "
                + connectionResult.getErrorCode());

        Toast.makeText(this,
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();
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

            ViewGroup root = (ViewGroup) inflater.inflate(R.layout.share_choose_popup, null);

            ListView listview = (ListView) root.findViewById(R.id.listview);
            adapter = new CustomBaseAdapter(ProfileActivity.this, mVehicleDetailses);
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


    //=============== Sey Vechicles List in Adapter===================//
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
                    //Toast.makeText(ProfileActivity.this, "Vehicle Id is... " +mVehicleDetailses.get(position).getVehicle_name(), Toast.LENGTH_SHORT).show();
                    etBikeBrand.setText(mVehicleDetailses.get(position).getVehicle_name());
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
            ModelCustomBaseAdapter = new ModelCustomBaseAdapter(ProfileActivity.this, vehicleModelDetailses);
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
                    vehicle_type_id = vehicleModelDetailses.get(position).getVehicleTypeId();
                    etBikeModel.setText(vehicleModelDetailses.get(position).getVehicleType());
                    mdw.dismiss();
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
    //http://ridersopininon.herokuapp.com/index.php/riders/vehicle

    /**
     * Register info .
     */
    public void vechiclemodelinfo() {
        showProgressDialog();
        Log.e("vechiclemodelinfo", "vechiclemodelinfo");
        try {
            //  Log.e("URL: ",""+ ApplicationGlobal.ROOT+ApplicationGlobal.baseurl_sigup+"utypeid="+utypeid+"&latitude="+latitude+"&longitude="+longitude+"&password="+password+"&deviceToken="+devicetoken+"&OS=Android");
            RequestQueue requestQueue = Volley.newRequestQueue(ProfileActivity.this);
            RequestJsonObject loginTaskRequest = new RequestJsonObject(Request.Method.POST,
                    ApplicationGlobal.ROOT + ApplicationGlobal.baseurl_model + vehicleId, null,
                    volleyModelErrorListener(), volleyModelSuccessListener()
            );

            requestQueue.add(loginTaskRequest);

        } catch (Exception e) {
            e.printStackTrace();

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
                    mdw = new ModelPopupWindow(view, ProfileActivity.this);
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

    /**
     * Validations
     **/
    private boolean checkDataHasEntered() {
        if (etProfileName.getText().toString().trim().length() <= 0) {
            showToast("Please Enter Profile Name");
            return false;
        } else if (etName.getText().toString().trim().length() <= 0) {
            showToast("Please Enter Name");
            return false;
        } else if (etAddress.getText().toString().trim().length() <= 0) {
            showToast("Please Enter Address");
            return false;
        } else if (etPhone.getText().toString().trim().length() <= 0) {
            showToast("Please Enter Phone Number");
            return false;
        } else if (etEmail.getText().toString().trim().length() <= 0) {
            showToast("Please Enter Email");

            return false;
        } else if(validEmail(etEmail.getText().toString().trim())==false){
            showToast("Please Enter Valid Email");
            return false;

        } else if(validEmail(etEmail.getText().toString().trim())==false){
            showToast("Please Enter Valid Email");
            return false;

        }else if (etBikeBrand.getText().toString().trim().length() <= 0) {
            showToast("Please Enter Brand Name ");
            return false;
        } else if (etBikeModel.getText().toString().trim().length() <= 0) {
            showToast("Please Enter Bike Model ");
            return false;
        } else if (etDescription.getText().toString().trim().length() <= 0) {
            showToast("Please Enter Description ");
            return false;
        }
        return true;
    }

    private boolean validEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();

    }


    /**
     * Image Selection from Gallery or Camera
     **/

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent takePictureIntent = new Intent(
                            MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePictureIntent,
                            ApplicationGlobal.CAMERA_REQUEST);

                } else if (items[item].equals("Choose from Library")) {
                    Intent i = new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i,
                            ApplicationGlobal.RESULT_LOAD_IMAGE);

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == ApplicationGlobal.RESULT_LOAD_IMAGE
                    && resultCode == Activity.RESULT_OK) {
                onSelectFromGalleryResult(data);
            }
            else if (requestCode == ApplicationGlobal.CAMERA_REQUEST
                    && resultCode == Activity.RESULT_OK) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                onCapture_ImageResult(bitmap,data.getData());
            }
        }
    }

    //================ function to get image after capturing using camera=============//
    private void onCapture_ImageResult(Bitmap bitmap,Uri path) {
        showProgressDialog();
        Log.e("bitmap:",""+bitmap);
        destination=ApplicationGlobal.bitmapToFile(bitmap);
        Log.e("destination:",""+destination);
        prefsManager = new PrefsManager(ProfileActivity.this);

        FileUploadService service = ServiceGenerator.createService(FileUploadService.BASE_URL);

        TypedFile typedFile = new TypedFile("multipart/form-data", destination);

        Log.e("CameraImage User Id", "" + prefsManager.getCaseId());
        Log.e("CameraImage Token", "" + prefsManager.getToken());
        Log.e("FileLocation", "" + destination);
        Log.e("CameraImage TypedFile", "" + typedFile);

        if (imageFlag) {
            uploadBannerImage(service, typedFile, destination);
            ivBannerImage.setImageBitmap(bitmap);
            //ivBannerImage.setImageURI(Uri.fromFile(destination));
        } else {
            uploadProfileImage(service, typedFile, destination);
        }

    }

    //============ function to select image from galary================//

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        showProgressDialog();
        String picturePath;
        Uri selectedImageUri = data.getData();



//        String[] projection = {MediaStore.MediaColumns.DATA};
       // Cursor cursor = managedQuery(selectedImageUri, projection, null, null,
//                null);
//        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
//        cursor.moveToFirst();
//        String selectedImagePath = cursor.getString(column_index);

        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = managedQuery(selectedImageUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            picturePath=cursor.getString(column_index);
        } catch (Exception e) {
            picturePath=selectedImageUri.getPath();
        }
        destination=new File(picturePath);
//        Bitmap bm = ApplicationGlobal.getFile(picturePath, ProfileActivity.this);
//       destination=ApplicationGlobal.bitmapToFile(bm);
//        final File file = savebitmap(bm);
        prefsManager = new PrefsManager(ProfileActivity.this);

        FileUploadService service = ServiceGenerator.createService(FileUploadService.BASE_URL);

        Log.e("destination", "" + destination);
        TypedFile typedFile = new TypedFile("multipart/form-data", destination);


        if (imageFlag) {
            uploadBannerImage(service, typedFile,destination);
        } else {
            uploadProfileImage(service, typedFile,destination);
        }
    }

    //================== function to save Image Bitmap===============//
    private File savebitmap(Bitmap filename) {
        File imageF = null;
        try {
            if (Environment.MEDIA_MOUNTED.equals(Environment
                    .getExternalStorageState())) {

                File storageDir = new File(
                        ApplicationGlobal.LOCAL_STORAGE_BASE_PATH_FOR_POSTED_PHOTOS)
                        .getParentFile();

                if (storageDir != null) {
                    if (!storageDir.mkdirs()) {
                        if (!storageDir.exists()) {
                            Log.d("CameraSample", "failed to create directory");
                            return null;
                        }
                    }
                }
                imageF = File.createTempFile(ApplicationGlobal.JPEG_FILE_PREFIX
                                + System.currentTimeMillis() + "_",
                        ApplicationGlobal.JPEG_FILE_SUFFIX, storageDir);
            } else {
                Log.v("image loading status",
                        "External storage is not mounted READ/WRITE.");
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        OutputStream outStream = null;
        try {
            // make a new bitmap from your file
            Bitmap bitmap = filename;

            outStream = new FileOutputStream(imageF);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            outStream.flush();
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("file", "" + imageF);
        return imageF;

    }


    /**
     * Function to upload Banner Image
     **/
    @TargetApi(16)
    private void uploadBannerImage(FileUploadService service, TypedFile typedFile, final File file) {

        service.bannerImageUpload(prefsManager.getCaseId(), typedFile, prefsManager.getToken(), new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, retrofit.client.Response response) {
                //ivBannerImage.setImageBitmap(ob);
                Log.d("BannerImagePath_InSet", " " + file);
                ivBannerImage.setImageURI(Uri.fromFile(file));
                //ivBannerImage.setBackground(new BitmapDrawable(getResources(),ob));
                prefsManager.setmKeyBannerImageUrl("" + file);
                showToast("Banner Image is Updated ");
                Log.e("Upload", "success");
                Log.e("jsonObject:", "" + jsonObject.toString());
                Log.e("response:", "" + response.toString());
                Log.e("Status:", "" + response.getStatus());
                Log.e("Body:", "" + response.getBody());
                Log.e("Reason:", "" + response.getReason());
                dismissProgressDialog();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("EnteredUploadErroe", "error---------- " + error);
                showToast("Sorry Image not Updated Try Again");
                dismissProgressDialog();
            }
        });
    }

    /**
     * Function to Upload Profile Image
     **/
    private void uploadProfileImage(FileUploadService service, TypedFile typedFile, final File file) {


        Log.e("UserId:", "" + prefsManager.getCaseId());
        Log.e("Token:", "" + prefsManager.getToken());
        Log.e("typedFile:", "" + typedFile);

        /**
         *
         * **/
        service.profileImageUpload(prefsManager.getCaseId(), typedFile, prefsManager.getToken(), new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, retrofit.client.Response response) {
                //sdvDisplayPicture.setImageBitmap(bm);
                //sdvDisplayPicture.setImageURI(Uri.fromFile(file));
                showToast("Profile Image Updated");
//                BasgjDFJK
//                prefsManager.setImageUrl("" + file);

                Log.e("Upload", "success");
                Log.e("jsonObject:", "" + jsonObject.toString());
                Log.e("response:", "" + response.toString());
                Log.e("Status:", "" + response.getStatus());
                Log.e("Body:", "" + response.getBody());
                Log.e("Reason:", "" + response.getReason());
                profile_image=file;
                imageUpdateFlag=true;
                showProfileImage();
                dismissProgressDialog();
                showProfileBeforeUpdate();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("ProfileImageUpload", "error----" + error);
                dismissProgressDialog();
            }
        });
    }


    /**
     * View Profile Before Update
     **/
    @TargetApi(16)
    private void showProfileBeforeUpdate() {

        showProgressDialog();
        FileUploadService service = ServiceGenerator.createService(FileUploadService.BASE_URL);
        service.editProfile(prefsManager.getCaseId(), prefsManager.getToken(), new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, retrofit.client.Response response) {
                dismissProgressDialog();
                Type type = new TypeToken<ProfileJsonData>() {
                }.getType();
                ProfileJsonData
                        profileJsonData = new Gson().fromJson(jsonObject.toString(), type);
                Log.e("jsonObject:", "" + jsonObject);
                Log.e("profileJsonData:", "" + profileJsonData);
                if (profileJsonData.getSuccess() == 1) {
                    data = profileJsonData.getData();
                    Log.e("BaseLocation:", "" + data.getBaseLocation());

                    etProfileName.setText(data.getProfileName());
                    etName.setText(data.getUserName());
                    etAddress.setText(data.getBaseLocation());
                    etPhone.setText(data.getPhone());
                    etEmail.setText(data.getEmail());
                    etBikeBrand.setText(data.getVehName());
                    etBikeModel.setText(data.getVehType());
                    etBikeYear.setText(data.getVehicle_year());
                    etDescription.setText(data.getDescription());
                    vehicleId = data.getVehId();
                    vehicle_type_id=data.getVehTypeId();
                    prefsManager.setmKeyBannerImageUrl(data.getCoverImage());
                    Log.e("Set Profile",data.getUserImage());
                    prefsManager.setImageUrl(data.getUserImage());



                    if(data.getUserImage()==null) {
                        sdvDisplayPicture.setImageURI(Uri.parse(prefsManager.getImageUrl()));
                       // sdvDisplayPicture.setImageURI(Uri.fromFile(new File(prefsManager.getImageUrl())));
                    }else{
                       // sdvDisplayPicture.setImageURI(Uri.fromFile(new File(data.getUserImage())));
                        sdvDisplayPicture.setImageURI(Uri.parse(data.getUserImage()));
                    }
                    Log.d("user_image ", "----" + data.getUserImage());
                    Log.d("BannerImagePath ", "----" + prefsManager.getmKeyBannerImageUrl()+" :"+data.getCoverImage());
                    //ivBannerImage.setImageURI(Uri.parse(prefsManager.getmKeyBannerImageUrl()));
                    //ivBannerImage.setBackgroundBitmap(BitmapFactory.decodeFile(prefsManager.getmKeyBannerImageUrl()));
                    //ivBannerImage.setImageBitmap(BitmapFactory.decodeFile(prefsManager.getmKeyBannerImageUrl()));
                    if(data.getCoverImage()==null){
                        ivBannerImage.setImageURI(Uri.parse(prefsManager.getmKeyBannerImageUrl()));
                    }else{
                        ivBannerImage.setImageURI(Uri.parse(data.getCoverImage()));
                    }


//                    try {
//                    URL url = null;
//                    if(data.getCoverImage()==null){
//                         url = new URL(prefsManager.getmKeyBannerImageUrl());
//
//                    }else {
//                         url = new URL(data.getCoverImage());
//                    }
//
//                        Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//
//                        Log.e("image Bitmap",""+image);
//                        ivBannerImage.setBackground(new BitmapDrawable(getResources(), image));
//                    }catch(Exception e){
//
//                    }
                } else {

                }
            }

            @Override
            public void failure(RetrofitError error) {
                dismissProgressDialog();
                Log.d("DataUploading------", "Data Uploading Failure......" + error);
            }
        });


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
            mAdapter = new TimeBaseAdapter(ProfileActivity.this, arrayYear);
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

    //============== model year adapter================//
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

                    etBikeYear.setText(""+mVehicleDetailses.get(position));

                    tw.dismiss();
//                    BikeId = vehicleId;
//                    strBikeYear = mVehicleDetailses.get(position).toString();
//                    showFilterDialog(strBikeName, strBikeYear, strBikeModel);
//                    pDialog.show();

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
