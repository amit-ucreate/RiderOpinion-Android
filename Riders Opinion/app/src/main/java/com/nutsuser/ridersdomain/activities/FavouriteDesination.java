package com.nutsuser.ridersdomain.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.swipe.util.Attributes;
import com.facebook.FacebookSdk;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.adapter.CustomGridAdapter;
import com.nutsuser.ridersdomain.adapter.FavouriteDestinationAdapter;
import com.nutsuser.ridersdomain.adapter.FavouriteVendorAdapter;
import com.nutsuser.ridersdomain.chat.SampleCometChatActivity;
import com.nutsuser.ridersdomain.utils.CustomizeDialog;
import com.nutsuser.ridersdomain.utils.PrefsManager;
import com.nutsuser.ridersdomain.view.CircleButtonText;
import com.nutsuser.ridersdomain.view.CustomDialog;
import com.nutsuser.ridersdomain.web.api.FileUploadService;
import com.nutsuser.ridersdomain.web.api.ServiceGenerator;
import com.nutsuser.ridersdomain.web.pojos.DividerItemDecoration;
import com.nutsuser.ridersdomain.web.pojos.FavVendor;
import com.nutsuser.ridersdomain.web.pojos.FavouriteDestinationData;
import com.nutsuser.ridersdomain.web.pojos.FavouriteDestinationItem;
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
 * Created by user on 1/6/2016.
 */
public class FavouriteDesination extends BaseActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tvTitleToolbar)
    TextView tvTitleToolbar;
    RecyclerView mRecyclerView,mRecyclerViewVendor;
    FavouriteDestinationAdapter mAdapter;
    FavouriteVendorAdapter favVendoradapter;
    private Activity activity;
    String AccessToken,UserId;
    CustomizeDialog mCustomizeDialog;
    public static LinearLayout LvDestination;
    public static LinearLayout LvVendors;
    public static TextView empty_fields;
    public static CircleButtonText tvFavCount, tvVendorCount;

    private ArrayList<FavouriteDestinationData> mDataSet;
    private ArrayList<FavVendor> mVendorList;
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
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_favouritedestination);
        activity = this;
        try {
            ButterKnife.bind(this);
            setupActionBar();
            setFontsToTextViews();
            mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
            mRecyclerViewVendor= (RecyclerView) findViewById(R.id.my_recycler_view_vendor);
            tvFavCount = (CircleButtonText) findViewById(R.id.tvFavCount);
            tvVendorCount = (CircleButtonText) findViewById(R.id.tvVendorCount);
            LvDestination = (LinearLayout) findViewById(R.id.LvDestination);
            LvVendors = (LinearLayout) findViewById(R.id.LvVendor);
            empty_fields=(TextView)findViewById(R.id.empty_view_fav);
            // Layout Managers:
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            // Item Decorator:
            mRecyclerView.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.divider)));
            // mRecyclerView.setItemAnimator(new FadeInLeftAnimator());


            mRecyclerViewVendor.setLayoutManager(new LinearLayoutManager(this));
            // Item Decorator:
            mRecyclerViewVendor.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.divider)));
            // mRecyclerView.setItemAnimator(new FadeInLeftAnimator());


            mDataSet = new ArrayList<FavouriteDestinationData>();
            mVendorList = new ArrayList<FavVendor>();
            loadData();

            // Creating Adapter object
            mAdapter = new FavouriteDestinationAdapter(this, mDataSet);


            // Setting Mode to Single to reveal bottom View for one item in List
            // Setting Mode to Mutliple to reveal bottom Views for multile items in List
            ((FavouriteDestinationAdapter) mAdapter).setMode(Attributes.Mode.Single);

            mRecyclerView.setAdapter(mAdapter);

        /* Scroll Listeners */
            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    Log.e("RecyclerView", "onScrollStateChanged");
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                }
            });

            favVendoradapter = new FavouriteVendorAdapter(this,mVendorList);

            // Setting Mode to Single to reveal bottom View for one item in List
            // Setting Mode to Mutliple to reveal bottom Views for multile items in List
            ((FavouriteVendorAdapter) favVendoradapter).setMode(Attributes.Mode.Single);

            mRecyclerViewVendor.setAdapter(favVendoradapter);

        /* Scroll Listeners */
            mRecyclerViewVendor.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    Log.e("RecyclerView", "onScrollStateChanged");
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                }
            });

            setSliderMenu();
            setDrawerSlider();
        }catch(Exception e){
            Rollbar.reportException(e, "minor", "Favourite Destination on create");
        }
    }
    private void setFontsToTextViews() {
        tvTitleToolbar.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/MACHINEN.TTF"));


    }

    private void setupActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_home);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
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
                if (position == 4) {
                    mDrawerLayout.closeDrawer(lvSlidingMenu);
                }else {
//                if (position == 1) {
//                    Log.e("positiuon:", "" + classList[position]);
//                    intent_Calling(classList[position], "My Messages");
//                }  else {
                    Log.e("positiuon:", "" + classList[position]);
                    intentCalling(classList[position]);
                    //  }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        if(mDrawerLayout.isDrawerOpen(lvSlidingMenu)){
            showProfileImage();
            mDrawerLayout.closeDrawer(lvSlidingMenu);
        }
        super.onResume();

    }

    //======== function to move on next activty=========//
    public void intentCalling(Class name) {
        Intent mIntent = new Intent(FavouriteDesination.this, name);
        startActivity(mIntent);
       // finish();
    }

    public void intent_Calling(Class name, String na) {
        Intent mIntent = new Intent(FavouriteDesination.this, name);
        mIntent.putExtra(SCREEN_OPEN, na);
        startActivity(mIntent);
        finish();
    }
    // load initial data
    public void loadData() {

//        for (int i = 0; i <= 20; i++) {
//            mDataSet.add(new Student("Student " + i, "androidstudent" + i + "@gmail.com"));
//        }
        showProgressDialog();
        Log.e(" MyRidingDetails", " MyRidingDetails");
        try {
            prefsManager = new PrefsManager(FavouriteDesination.this);
            AccessToken = prefsManager.getToken();
            UserId = prefsManager.getCaseId();
            FileUploadService service = ServiceGenerator.createService(FileUploadService.BASE_URL);

            Log.e(" UserId", " "+UserId);
            Log.e(" AccessToken", " "+AccessToken);
            service.favroiteDestination(UserId, AccessToken, new Callback<JsonObject>() {
                @Override
                public void success(JsonObject jsonObject, retrofit.client.Response response) {
                    Log.e("Upload", "success");
                    Log.e("jsonObject:", "" + jsonObject.toString());

                    Type type = new TypeToken<FavouriteDestinationItem>() {
                    }.getType();
                    FavouriteDestinationItem favouriteDestinationItem = new Gson().fromJson(jsonObject.toString(), type);

                    if (favouriteDestinationItem.getSuccess() == 1) {
                        dismissProgressDialog();
                        mDataSet.clear();
                        mVendorList.clear();
                        mDataSet.addAll(favouriteDestinationItem.getData());
                        mVendorList.addAll(favouriteDestinationItem.getFavVendor());

                        if(mDataSet.size()==0&&mVendorList.size()==0){
                            LvDestination.setVisibility(View.GONE);
                            mRecyclerView.setVisibility(View.GONE);
                            LvVendors.setVisibility(View.GONE);
                            mRecyclerViewVendor.setVisibility(View.GONE);
                            empty_fields.setVisibility(View.VISIBLE);
                        }

                        if(mDataSet.size()==0){
                            LvDestination.setVisibility(View.GONE);
                            mRecyclerView.setVisibility(View.GONE);
                        }
                        if(mVendorList.size()==0){
                            LvVendors.setVisibility(View.GONE);
                            mRecyclerViewVendor.setVisibility(View.GONE);
                        }


                            tvFavCount.setText(""+mDataSet.size());
                            mAdapter = new FavouriteDestinationAdapter(FavouriteDesination.this, mDataSet);

                        // Setting Mode to Single to reveal bottom View for one item in List
                        // Setting Mode to Mutliple to reveal bottom Views for multile items in List
                        ((FavouriteDestinationAdapter) mAdapter).setMode(Attributes.Mode.Single);

                        mRecyclerView.setAdapter(mAdapter);

                        tvVendorCount.setText(""+mVendorList.size());

                        favVendoradapter= new FavouriteVendorAdapter(FavouriteDesination.this, mVendorList);

                        // Setting Mode to Single to reveal bottom View for one item in List
                        // Setting Mode to Mutliple to reveal bottom Views for multile items in List
                        ((FavouriteVendorAdapter) favVendoradapter).setMode(Attributes.Mode.Single);

                        mRecyclerViewVendor.setAdapter(favVendoradapter);



                        }  else {
                        LvDestination.setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.GONE);
                        LvVendors.setVisibility(View.GONE);
                        mRecyclerViewVendor.setVisibility(View.GONE);
                        empty_fields.setVisibility(View.VISIBLE);
                        dismissProgressDialog();
                        //CustomDialog.showProgressDialog(FavouriteDesination.this, favouriteDestinationItem.getMessage().toString());
                    }
                }
                @Override
                public void failure(RetrofitError error) {
                    dismissProgressDialog();
                    //showToast("Error:" + error);
                    Log.e("Upload", "error : "+error);
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
            Rollbar.reportException(e, "minor", "Favourite Destination get favroiteDestination API");

        }
    }

    //=========== menus===============//
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // finish();
                Intent intent = new Intent(FavouriteDesination.this, MainScreenActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("MainScreenResponse", "OPEN");
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //======== OnClick Listeners=============//
    @OnClick({ R.id.btFullProfile, R.id.btUpdateProfile, R.id.ivMenu, R.id.rlProfile})
    void click(View view) {
        switch (view.getId()) {

            case R.id.btUpdateProfile:
                startActivity(new Intent(FavouriteDesination.this, ProfileActivity.class));
                break;
            case R.id.btFullProfile:
                startActivity(new Intent(FavouriteDesination.this, PublicProfileScreen.class));
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
        }
    }

}
