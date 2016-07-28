package com.nutsuser.ridersdomain.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.adapter.AdapterRecentFriends;
import com.nutsuser.ridersdomain.adapter.CustomGridAdapter;
import com.nutsuser.ridersdomain.chat.ChatroomListActivity;
import com.nutsuser.ridersdomain.chat.SampleCometChatActivity;
import com.nutsuser.ridersdomain.chat.SampleSingleChatActivity;
import com.nutsuser.ridersdomain.utils.CustomizeDialog;
import com.nutsuser.ridersdomain.utils.HorizontalListView;
import com.nutsuser.ridersdomain.utils.PrefsManager;
import com.nutsuser.ridersdomain.web.api.FileUploadService;
import com.nutsuser.ridersdomain.web.api.ServiceGenerator;

import com.nutsuser.ridersdomain.web.pojos.PublicProfileData;
import com.nutsuser.ridersdomain.web.pojos.PublicProfileModel;
import com.nutsuser.ridersdomain.web.pojos.RecentFriend;
import com.rollbar.android.Rollbar;

import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by user on 1/5/2016.
 */
public class PublicProfileScreen extends BaseActivity {
    CustomizeDialog mCustomizeDialog;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    private Activity activity;
    @Bind(R.id.tvUsername)
    TextView tvUsername;
    @Bind(R.id.tvNickname)
    TextView tvNickname;
    @Bind(R.id.sdvDisplayPicture)
    SimpleDraweeView sdvDisplayPicture;
    @Bind(R.id.sdv_DisplayPicture)
    SimpleDraweeView sdv_DisplayPicture;
    private ActionBarDrawerToggle mDrawerToggle;
    private PublicProfileData publicProfileData;
    //======== slider items============//
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.sdvDp)
    SimpleDraweeView sdvDp;
    @Bind(R.id.lvSlidingMenu)
    LinearLayout lvSlidingMenu;
    @Bind(R.id.tvTitleToolbar)
    TextView tvTitleToolbar;
    @Bind(R.id.btUpdateProfile)
    Button btUpdateProfile;
    @Bind(R.id.btFullProfile)
    Button btFullProfile;
    @Bind(R.id.gridView1)
    GridView gridView1;
    @Bind(R.id.tvName)
    TextView tvName;
    @Bind(R.id.txtRecentFriends)
    TextView txtRecentFriends;
    public static String[] prgmNameList = {"My Rides", "My Messages", "My Friends", "Chats", "Favorites", "Notifications", "Settings", "Riders Near By"};
    public static int[] prgmImages = {R.drawable.ic_menu_my_rides, R.drawable.ic_menu_my_messages, R.drawable.menu_my_friends, R.drawable.ic_menu_menu_chats, R.drawable.menu_fav_destinations, R.drawable.ic_menu_menu_notifications, R.drawable.menu_settings, R.drawable.icon_nearby};
    public static Class[] classList = {MyRidesRecyclerView.class, RecentChatListActivity.class,MyFriendsActivity.class, ComingSoon.class, FavouriteDesination.class, NotificationListActivity.class, SettingsActivity.class, NearByFriendsAcitivity.class};

    //================================//
    @Bind(R.id.tvmiles)
    TextView tvmiles;
    @Bind(R.id.lvFriends)
    HorizontalListView lvFriends;
    @Bind(R.id.tvcity)
    TextView tvcity;
    @Bind(R.id.tvrides)
    TextView tvrides;
    @Bind(R.id.tvAddress)
    TextView tvAddress;
    @Bind(R.id.tvIntersest)
    TextView tvIntersest;
    @Bind(R.id.fmAddFriend)
    FrameLayout fmAddFriend;
    @Bind(R.id.fmRides)
    FrameLayout fmRides;
    @Bind(R.id.txtAddFriends)
    TextView txtAddFriends;
    @Bind(R.id.imgAddFriends)
    ImageView imgAddFriends;

    boolean check=false;
    String user_id=null;
    private int friendStatus;

    @Bind(R.id.IvMessage)
    ImageView IvMessage;
    boolean home=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.public_profile_screen);
        try {
            activity = this;
            ButterKnife.bind(this);
            setupActionBar(toolbar);
            prefsManager = new PrefsManager(PublicProfileScreen.this);
            setFonts();

            viewData();
            gridView1.setAdapter(new CustomGridAdapter(this, prgmNameList, prgmImages));
            gridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (position == 1) {
//                        Log.e("positiuon:", "" + classList[position]);
//                        intent_Calling(classList[position], "My Messages");
//                    }   else {
                    startActivity(new Intent(PublicProfileScreen.this, classList[position]));
                    // finish();
                    //  }

                }
            });
            lvFriends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.e("horizontal click", "Horizontal list click = " + position);
                    Intent intent = new Intent(context, PublicProfileScreen.class);
                    intent.putExtra(PROFILE, true);
                    intent.putExtra(PROFILE_IMAGE, publicProfileData.getRecentFriend().get(position).getRecentFriendImage());
                    intent.putExtra(USER_ID, publicProfileData.getRecentFriend().get(position).getFriendId());
                    context.startActivity(intent);
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
        }catch(Exception e){
            Rollbar.reportException(e, "minor", "Public Profile activity on create");
        }

    }


    private void setFonts() {
        tvTitleToolbar.setTypeface(typeFaceMACHINEN);
        tvName.setTypeface(typeFaceMACHINEN);
        tvUsername.setTypeface(typeFaceLatoHeavy);
        tvNickname.setTypeface(typeFaceLatoHeavy);
        tvAddress.setTypeface(typeFaceLatoBold);
        // tvHostedBy.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/Lato-Bold.ttf"));
        // tvTime.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/Lato-Bold.ttf"));
        // tvDate.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/Lato-Bold.ttf"));
    }


    public void intent_Calling(Class name, String na) {
        Intent mIntent = new Intent(PublicProfileScreen.this, name);
        mIntent.putExtra(SCREEN_OPEN, na);
        startActivity(mIntent);
        finish();

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
        Log.e("Profile image Public",prefsManager.getImageUrl());
        if (prefsManager.getImageUrl() == null) {
            //Toast.makeText(PublicProfileScreen.this, "Image Address is Null", Toast.LENGTH_SHORT).show();
        } else {
            String imageUrl = prefsManager.getImageUrl();
            sdvDp.setImageURI(Uri.parse(imageUrl));
//            File file = new File(imageUrl);
//            sdvDp.setImageURI(Uri.fromFile(file));
        }

    }




    private void viewData() {
        showProgressDialog();
        String Id = null;
        try {
            check = getIntent().getExtras().getBoolean(PROFILE);
            user_id = getIntent().getExtras().getString(USER_ID);
        }catch(NullPointerException e){
            check=false;
            user_id=prefsManager.getCaseId();
        }

        if (!check) {
            //Id = prefsManager.getCaseId();
            Id = null;
            sdvDisplayPicture.setImageURI(Uri.fromFile(new File(prefsManager.getmKeyBannerImageUrl())));

//            Uri image_Uri = Uri.parse(prefsManager.getImageUrl().trim());
//         //   sdvDisplayPicture.setImageURI(imageUri);
//            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(image_Uri)
//                    .setProgressiveRenderingEnabled(true) //Progressive loading
//                    .build();
//            DraweeController controller = Fresco.newDraweeControllerBuilder()
//                    .setImageRequest(request)
//                    .setOldController(sdv_DisplayPicture.getController())
//                    .build();
//            sdv_DisplayPicture.setController(controller);
            sdv_DisplayPicture.setImageURI(Uri.fromFile(new File(prefsManager.getImageUrl())));
            imgAddFriends.setBackgroundResource(R.drawable.user);
            txtAddFriends.setText("Friends");

        }
//        else {
//            Id = user_id;
//            imgAddFriends.setBackgroundResource(R.drawable.ic_public_profile_add_friend);
//            txtAddFriends.setText("Add Friends");
//
//        }

        Log.e("getToken():", "" + prefsManager.getToken());



        if (user_id.equalsIgnoreCase(prefsManager.getCaseId())) {
            getViewProfileAPI("0");
            home = false;
            IvMessage.setVisibility(View.GONE);
            fmRides.setClickable(true);
            tvTitleToolbar.setText("MY PROFILE");
        } else {
            home = true;
            setupActionBarBack(toolbar);
            getViewProfileAPI(user_id);
            IvMessage.setVisibility(View.VISIBLE);
            fmRides.setClickable(false);
            tvTitleToolbar.setText("PROFILE");
        }
    }

    //============ get profile data=============//
    private void getViewProfileAPI(String friend_id){
        Log.e("friend_id",friend_id);
        FileUploadService service = ServiceGenerator.createService(FileUploadService.BASE_URL);
        service.viewPublicProfile(prefsManager.getCaseId(),friend_id ,prefsManager.getToken(), new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, Response response) {
                dismissProgressDialog();
                Log.e("jsonObject:", "" + jsonObject);
                //try {
                Type type = new TypeToken<PublicProfileModel>() {
                }.getType();
                PublicProfileModel publicProfileModel = new Gson().fromJson(jsonObject.toString(), type);
                if (publicProfileModel.getSuccess() == 1) {


                    publicProfileData = publicProfileModel.getData();

                    AdapterRecentFriends mAdapter=new AdapterRecentFriends(PublicProfileScreen.this, publicProfileData.getRecentFriend());
                    lvFriends.setAdapter(mAdapter);



                    if (!(publicProfileData.getProfileName() == null)) {
                        tvUsername.setText(publicProfileData.getProfileName().trim());
                    } else {
                        tvUsername.setText("");
                    }
                    if (!(publicProfileData.getUserName() == null)) {
                        tvNickname.setText(publicProfileData.getUserName().trim());
                    } else {
                        tvNickname.setText("");
                    }
                    tvrides.setText("" + publicProfileData.getCountOfRides());

                    try {
                        if (prefsManager.getDistanceParameter().equalsIgnoreCase("M")) {
                            tvmiles.setText("" + publicProfileData.getTotalMiles() + " M");
                        } else {
                            tvmiles.setText("" + getDistanceKilometer(Double.parseDouble(publicProfileData.getTotalMiles().toString())) + " KM");
                        }
                    }catch(NullPointerException e){
                        tvmiles.setText("" + publicProfileData.getTotalMiles() + " M");
                    }
                    tvcity.setText(publicProfileData.getBaseLocation());
                    tvAddress.setText(publicProfileData.getVehType());
                    tvIntersest.setText(publicProfileData.getDescription());
                    if (!check) {
                        txtRecentFriends.setText("RECENT FREINDS "+ lvFriends.getAdapter().getCount());
                    }else if(publicProfileData.getMutualFriends().size()==0){
                        txtRecentFriends.setText("RECENT FREINDS "+ lvFriends.getAdapter().getCount() +"(0 MUTUAL)");
                    }else{
                        txtRecentFriends.setText("RECENT FREINDS "+ lvFriends.getAdapter().getCount() +"("+publicProfileData.getMutualFriends().size() +" MUTUAL)");
                    }


                    friendStatus = publicProfileData.getFriendStatus();
                    if (publicProfileData.getFriendStatus() == 1) {
                        imgAddFriends.setBackgroundResource(R.drawable.remove);
                        txtAddFriends.setText("Remove");
                    } else if (!check) {
                        imgAddFriends.setBackgroundResource(R.drawable.user);
                        txtAddFriends.setText("View Friends");
                    } else {
                        imgAddFriends.setBackgroundResource(R.drawable.ic_public_profile_add_friend);
                        txtAddFriends.setText(publicProfileData.getStatusMessage());
                    }
                    //  if (check) {
                    Uri imageUri = Uri.parse(publicProfileData.getCoverImage());
                    Uri image_Uri = Uri.parse(publicProfileData.getUserImage());
                    sdvDisplayPicture.setImageURI(imageUri);
//                ImageRequest request = ImageRequestBuilder.newBuilderWithSource(image_Uri)
//                        .setProgressiveRenderingEnabled(true) //Progressive loading
//                        .build();
//                DraweeController controller = Fresco.newDraweeControllerBuilder()
//                        .setImageRequest(request)
//                        .setOldController(sdv_DisplayPicture.getController())
//                        .build();
//                sdv_DisplayPicture.setController(controller);
                    sdv_DisplayPicture.setImageURI(image_Uri);
                    //}
                } else {
                    showToast(publicProfileModel.getMessage());
                }
//                }catch(Exception e){
//            Log.e("pblic Prfle Exception",e.getMessage());
//                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("Error", "");
                dismissProgressDialog();

            }
        });
    }

    @OnClick({R.id.ivMenu, R.id.btUpdateProfile,R.id.fmAddFriend,R.id.btFullProfile,R.id.fmRides,R.id.IvMessage})
    void click(View v) {
        switch (v.getId()) {
            case R.id.ivMenu:
                if (mDrawerLayout.isDrawerOpen(lvSlidingMenu))
                    mDrawerLayout.closeDrawer(lvSlidingMenu);
                else
                    mDrawerLayout.openDrawer(lvSlidingMenu);
                break;
            case R.id.btUpdateProfile:
                startActivity(new Intent(activity, ProfileActivity.class));
                break;
            case R.id.btFullProfile:
                mDrawerLayout.closeDrawer(lvSlidingMenu);
                break;
            case R.id.fmAddFriend:
                if (check) {
                    if(user_id.equalsIgnoreCase(prefsManager.getCaseId())){
                        startActivity(new Intent(PublicProfileScreen.this,MyFriendsActivity.class));
                        finish();
                    }else {
                        if (isNetworkConnected()) {
                            if(friendStatus==0){
                                handleFriendRequestAPI(user_id,"1");
                            }else if(friendStatus==1){
                                unFriendAPI(user_id);
                            }else if(friendStatus==2){
                                //No Action will be performed..wait for the response.
                            }else if(friendStatus==3){
                                addFriendAPI();
                            }else if(friendStatus==4){
                                startActivity(new Intent(PublicProfileScreen.this,MyFriendsActivity.class));
                                finish();
                            }

                        } else {
                            showToast("Internet is not connected.");
                        }
                    }
                }else{
                    startActivity(new Intent(PublicProfileScreen.this,MyFriendsActivity.class));
                    finish();
                }

                //  showToast("click");
                break;

            case R.id.fmRides:
                startActivity(new Intent(activity, MyRidesRecyclerView.class).putExtra("past",true).putExtra("userId",publicProfileData.getUserId()));
                finish();
                break;
            case R.id.IvMessage:
                String channel = "";
                String username="";
                String id="";
                try {
                    Iterator<String> keys = MyFriendsActivity.jsonObjectUsers.keys();

                    while (keys.hasNext()) {
                        JSONObject user = MyFriendsActivity.jsonObjectUsers.getJSONObject(keys.next().toString());
                        id=user.getString("id");
                        if(id.equalsIgnoreCase(publicProfileData.getUserId().trim())) {
                            if (user.has("ch")) {
                                channel = user.getString("ch");                            }

                            Log.e("friend_id",publicProfileData.getUserId()+" = "+id+" name "+ user.getString("n")+" channel "+channel);
                            Intent intent = new Intent(PublicProfileScreen.this, SampleSingleChatActivity.class);
                            intent.putExtra("user_id", Long.parseLong(id));
                            intent.putExtra("user_name", user.getString("n"));
                            intent.putExtra("channel", channel);
                            intent.putExtra("userImage",publicProfileData.getUserImage());
                            intent.putExtra("name",publicProfileData.getUserId());
                            startActivity(intent);
                        }
                    }
                }catch(Exception e){

                    Log.e("exception",": "+e) ;

                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mDrawerLayout.isDrawerOpen(lvSlidingMenu)) {
            showProfileImage();
            mDrawerLayout.closeDrawer(lvSlidingMenu);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (!home) {
                    Intent intent = new Intent(PublicProfileScreen.this, MainScreenActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("MainScreenResponse", "OPEN");
                    startActivity(intent);
                    finish();
                } else {
                    finish();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.e("home", "" + home);
        if (!home) {
            Intent intent = new Intent(PublicProfileScreen.this, MainScreenActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("MainScreenResponse", "OPEN");
            startActivity(intent);
            finish();
        } else {
            finish();
        }
    }
    //=========== function to add friend======================//
    private void addFriendAPI(){

        Log.e("user_id",""+prefsManager.getCaseId()+"  =="+prefsManager.getToken());
        Log.e("friend_id",""+user_id);
        showProgressDialog();

        FileUploadService service = ServiceGenerator.createService(FileUploadService.BASE_URL_NEARBY_FRIENDS);

        service.handleFriends(prefsManager.getCaseId(), user_id, prefsManager.getToken(), "0", new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, Response response) {
                Log.e("jsonObject",""+jsonObject);
                showToast(jsonObject.get("message").toString());
                txtAddFriends.setText("Request Sent");
                dismissProgressDialog();
                viewData();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("failure",""+error.getMessage());
                dismissProgressDialog();
            }
        });
    }

    //=========== function to remove friend======================//
    private void unFriendAPI(String friend_id){

        Log.e("user_id",""+prefsManager.getCaseId()+"  =="+prefsManager.getToken());
        Log.e("friend_id",""+friend_id);
        showProgressDialog();

        FileUploadService service = ServiceGenerator.createService(FileUploadService.BASE_URL_NEARBY_FRIENDS);

        service.handleFriends(prefsManager.getCaseId(), friend_id, prefsManager.getToken(), "3", new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, Response response) {
                Log.e("jsonObject",""+jsonObject);
                showToast("Friend deleted Successfully.");
                txtAddFriends.setText("Add");
                imgAddFriends.setBackgroundResource(R.drawable.ic_public_profile_add_friend);
                dismissProgressDialog();
                viewData();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("failure",""+error.getMessage());
                dismissProgressDialog();
            }
        });
    }

    //=========== function to handle friend request======================//
    private void handleFriendRequestAPI(String friendId,final String requestType){

        Log.e("user_id",""+prefsManager.getCaseId()+"  =="+prefsManager.getToken());
        Log.e("friend_id",""+friendId);
        showProgressDialog();

        FileUploadService service = ServiceGenerator.createService(FileUploadService.BASE_URL_NEARBY_FRIENDS);
        Log.e("url",""+FileUploadService.BASE_URL_NEARBY_FRIENDS+friendId+prefsManager.getCaseId()+prefsManager.getToken()+requestType);
        service.handleFriends(prefsManager.getCaseId(), friendId,prefsManager.getToken(), requestType, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, Response response) {
                Log.e("jsonObject",""+jsonObject);
                txtAddFriends.setText("Friend");
                imgAddFriends.setBackgroundResource(R.drawable.remove);
                showToast(jsonObject.get("message").toString());
                dismissProgressDialog();
                viewData();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("failure",""+error.getMessage());
                dismissProgressDialog();
            }
        });
    }
}
