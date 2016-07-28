package com.nutsuser.ridersdomain.activities;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.swipe.util.Attributes;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.adapter.CustomGridAdapter;
import com.nutsuser.ridersdomain.adapter.FavouriteDestinationAdapter;
import com.nutsuser.ridersdomain.adapter.RecentChatListSwipeAdapter;
import com.nutsuser.ridersdomain.chat.SampleCometChatActivity;
import com.nutsuser.ridersdomain.view.CustomDialog;
import com.nutsuser.ridersdomain.web.api.FileUploadService;
import com.nutsuser.ridersdomain.web.api.ServiceGenerator;
import com.nutsuser.ridersdomain.web.pojos.DividerItemDecoration;
import com.nutsuser.ridersdomain.web.pojos.FavouriteDestinationData;
import com.nutsuser.ridersdomain.web.pojos.FavouriteDestinationItem;
import com.nutsuser.ridersdomain.web.pojos.RecentChatListData;
import com.nutsuser.ridersdomain.web.pojos.RecentChatListReponse;
import com.rollbar.android.Rollbar;

import java.lang.reflect.Type;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;

/**
 * Created by ucreateuser on 7/7/2016.
 */
public class RecentChatListActivity extends BaseActivity {

    private Activity activity;
    @Bind(R.id.tvTitleToolbar)
    TextView tvTitleToolbar;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    //=========== slider items====================//
    public static String[] prgmNameList = {"My Rides", "My Messages", "My Friends", "Chats", "Favorites", "Notifications", "Settings", "Riders Near By"};
    public static int[] prgmImages = {R.drawable.ic_menu_my_rides, R.drawable.ic_menu_my_messages, R.drawable.menu_my_friends, R.drawable.ic_menu_menu_chats, R.drawable.menu_fav_destinations, R.drawable.ic_menu_menu_notifications, R.drawable.menu_settings, R.drawable.icon_nearby};
    public static Class[] classList = {MyRidesRecyclerView.class, RecentChatListActivity.class, MyFriendsActivity.class, ComingSoon.class, FavouriteDesination.class, NotificationListActivity.class, SettingsActivity.class, NearByFriendsAcitivity.class};

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

    private ArrayList<RecentChatListData> recentChatListData;
    private RecentChatListSwipeAdapter recentChatAdapter;
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_recent_chat_users_list);
        try {
            activity = this;
            ButterKnife.bind(activity);
            setupActionBar(toolbar);
            setFonts();
            setSliderMenu();
            setDrawerSlider();
            recentChatListData = new ArrayList<RecentChatListData>();
            mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
            // Layout Managers:
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

            // Item Decorator:
            mRecyclerView.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.divider)));

//        if(isNetworkConnected()) {
//            getRecentChatList();
//        }else{
//            showToast("Internet is not connected.");
//        }
        }catch(Exception e){
            Rollbar.reportException(e, "minor", "Recent Chat activity on create");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isNetworkConnected()) {
            getRecentChatList();
        }else{
            showToast("Internet is not connected.");
        }
    }

    private void setFonts() {
        tvTitleToolbar.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/MACHINEN.TTF"));
        tvTitleToolbar.setText("MY MESSAGES");
        tvName.setTypeface(typeFaceMACHINEN);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(RecentChatListActivity.this, MainScreenActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("MainScreenResponse", "OPEN");
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //============ function to ser Drawer=================//
    private void setDrawerSlider() {
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
        try {
            if (prefsManager.getUserName() == null) {
                tvName.setText("No Name");
            } else {
                tvName.setText(prefsManager.getUserName());            }

            if (prefsManager.getImageUrl() == null) {
                // Toast.makeText(MyRidesRecyclerView.this, "Image Address is Null", Toast.LENGTH_SHORT).show();
            } else {
                String imageUrl = prefsManager.getImageUrl();
                sdvDp.setImageURI(Uri.parse(imageUrl));
//                File file = new File(imageUrl);
//                sdvDp.setImageURI(Uri.fromFile(file));
            }
        } catch (NullPointerException e) {
            tvName.setText("No Name");

            if (prefsManager.getImageUrl() == null) {
                // Toast.makeText(MyRidesRecyclerView.this, "Image Address is Null", Toast.LENGTH_SHORT).show();
            } else {
                String imageUrl = prefsManager.getImageUrl();
                sdvDp.setImageURI(Uri.parse(imageUrl));
//                File file = new File(imageUrl);
//                sdvDp.setImageURI(Uri.fromFile(file));
            }
        }
    }

    //============= function to set slider menu============//
    private void setSliderMenu() {
        gridView1.setAdapter(new CustomGridAdapter(this, prgmNameList, prgmImages));
        gridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) {
                    mDrawerLayout.closeDrawer(lvSlidingMenu);
                }  else {
                    Log.e("positiuon:", "" + classList[position]);
                    intentCalling(classList[position]);
                }
            }
        });
    }

    //======== function to move on next activty=========//
    public void intentCalling(Class name) {
        Intent mIntent = new Intent(RecentChatListActivity.this, name);
        startActivity(mIntent);
        //finish();
    }

    public void intent_Calling(Class name, String na) {
        Intent mIntent = new Intent(RecentChatListActivity.this, name);
        mIntent.putExtra(SCREEN_OPEN, na);
        startActivity(mIntent);
        finish();
    }

    //======== OnClick Listeners=============//
    @OnClick({R.id.btFullProfile, R.id.btUpdateProfile, R.id.ivMenu, R.id.rlProfile})
    void click(View view) {
        switch (view.getId()) {

            case R.id.btUpdateProfile:
                startActivity(new Intent(RecentChatListActivity.this, ProfileActivity.class));
                break;
            case R.id.btFullProfile:
                startActivity(new Intent(RecentChatListActivity.this, PublicProfileScreen.class));
                break;
            case R.id.ivMenu:
                if (mDrawerLayout.isDrawerOpen(lvSlidingMenu)) {
                    mDrawerLayout.closeDrawer(lvSlidingMenu);
                }
                else {
                    mDrawerLayout.openDrawer(lvSlidingMenu);
                }
                break;
            case R.id.rlProfile:
                // startActivity(new Intent(MyFriendsActivity.this, ProfileActivity.class));
                break;

        }
    }

    //========== get recent chat list data ================//

    private void getRecentChatList(){
        try {
            showProgressDialog();
            FileUploadService service = ServiceGenerator.createService(FileUploadService.BASE_URL);

            service.getRecentChatFriends(prefsManager.getCaseId(), prefsManager.getToken(), new Callback<JsonObject>() {
                @Override
                public void success(JsonObject jsonObject, retrofit.client.Response response) {
                    Log.e("Upload", "success");
                    Log.e("jsonObject:", "" + jsonObject.toString());

                    Type type = new TypeToken<RecentChatListReponse>() {
                    }.getType();
                    RecentChatListReponse recentChatListReponse = new Gson().fromJson(jsonObject.toString(), type);

                    if (recentChatListReponse.getSuccess() == 1) {
                        dismissProgressDialog();
                        recentChatListData.clear();
                        recentChatListData.addAll(recentChatListReponse.getData());
                        recentChatAdapter = new RecentChatListSwipeAdapter(RecentChatListActivity.this, recentChatListData);
                        //((RecentChatListSwipeAdapter) recentChatAdapter).setMode(Attributes.Mode.Single);
                        mRecyclerView.setAdapter(recentChatAdapter);
                    } else {
                        dismissProgressDialog();
                        CustomDialog.showProgressDialog(RecentChatListActivity.this, recentChatListReponse.getMessage().toString());
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    dismissProgressDialog();
                    //showToast("Error:" + error);
                    Log.e("Upload", "error");
                }
            });
        }catch(Exception e){
            Rollbar.reportException(e, "minor", "Recent Chat activity API");
        }
    }
}
