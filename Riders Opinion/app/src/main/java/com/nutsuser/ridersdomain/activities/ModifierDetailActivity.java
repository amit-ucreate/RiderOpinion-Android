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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.adapter.AdapterPrdouct;
import com.nutsuser.ridersdomain.adapter.CustomGridAdapter;
import com.nutsuser.ridersdomain.utils.CustomizeDialog;
import com.nutsuser.ridersdomain.view.CustomDialog;
import com.nutsuser.ridersdomain.web.api.FileUploadService;
import com.nutsuser.ridersdomain.web.api.ServiceGenerator;
import com.nutsuser.ridersdomain.web.pojos.LastModifierDetail;
import com.nutsuser.ridersdomain.web.pojos.LastModifierDetailsData;
import com.nutsuser.ridersdomain.web.pojos.ModiflyBikeFav;
import com.nutsuser.ridersdomain.web.pojos.Product;
import com.rollbar.android.Rollbar;

import java.lang.reflect.Type;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;

/**
 * Created by user on 9/30/2015.
 */
public class ModifierDetailActivity extends BaseActivity {

    String VenderId, BikeId, CatId, BikeName;
    LastModifierDetailsData lastModifierDetailsData;
    ArrayList<Product> stringArrayList;
    @Bind(R.id.sdv)
    SimpleDraweeView sdv;
    @Bind(R.id.ivFav)
    ImageView ivFav;

    CustomizeDialog mCustomizeDialog;
    public static String[] prgmNameList = {"My Rides", "My Messages", "My Friends", "Chats", "Favorites", "Notifications", "Settings", "Riders Near By"};
    public static int[] prgmImages = {R.drawable.ic_menu_my_rides, R.drawable.ic_menu_my_messages, R.drawable.menu_my_friends, R.drawable.ic_menu_menu_chats, R.drawable.menu_fav_destinations, R.drawable.ic_menu_menu_notifications, R.drawable.menu_settings, R.drawable.icon_nearby};
    public static Class[] classList = {MyRidesRecyclerView.class, RecentChatListActivity.class, MyFriendsActivity.class, ComingSoon.class, FavouriteDesination.class, NotificationListActivity.class, SettingsActivity.class, NearByFriendsAcitivity.class};
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.rvItems)
    RecyclerView rvItems;
    @Bind(R.id.tvTitleToolbar)
    TextView tvTitleToolbar;
    @Bind(R.id.tvBikeName)
    TextView tvBikeName;
    @Bind(R.id.tvDealerName)
    TextView tvDealerName;
    @Bind(R.id.tvSpeciality)
    TextView tvSpeciality;
    @Bind(R.id.tvOffers)
    TextView tvOffers;
    @Bind(R.id.tvProducts)
    TextView tvProducts;
    @Bind(R.id.tvViewAll)
    TextView tvViewAll;
    @Bind(R.id.tvName)
    TextView tvName;
    @Bind(R.id.tvAddress)
    TextView tvAddress;
    @Bind(R.id.ivVideo)
    ImageView ivVideo;
    @Bind(R.id.llofferandProduct)
    LinearLayout llofferandProduct;
    /*  @Bind(R.id.tvDestinations)
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
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.lvSlidingMenu)
    LinearLayout lvSlidingMenu;
    //public static String [] prgmNameList={"Riding Destinations","Meet 'N' Plan A Ride","Riding Events \n    ","Modifly Your Bikes","Healthy Riding","Get Directions","Notifications","Settings"};
    // public static int [] prgmImages={R.drawable.icon_menu_destination,R.drawable.icon_menu_meetplan,R.drawable.icon_menu_events,R.drawable.icon_modifybike,R.drawable.icon_menu_healthy_riding,R.drawable.icon_menu_get_direction,R.drawable.icon_menu_notification,R.drawable.icon_menu_settings};
    //  public static Class [] classList={DestinationsListActivity.class,PlanRideActivity.class,EventsListActivity.class,ModifyBikeActivity.class,HealthyRidingActivity.class,GetDirections.class,NotificationScreen.class,SettingsActivity.class};
    @Bind(R.id.gridView1)
    GridView gridView1;
    private AdapterPrdouct adapterPrdouct;
    private Activity activity;
    @Bind(R.id.sdvDp)
    SimpleDraweeView sdvDp;

    @Bind(R.id.btUpdateProfile)
    Button btUpdateProfile;
    private ActionBarDrawerToggle mDrawerToggle;
    private int fav = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_detail);
        try {
            activity = this;
            ButterKnife.bind(this);
            stringArrayList = new ArrayList<>();
            overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
            setupActionBar();

            VenderId = getIntent().getStringExtra("VenderId");
            BikeId = getIntent().getStringExtra("BikeId");
            CatId = getIntent().getStringExtra("CatId");
            BikeName = getIntent().getStringExtra("BikeName");
            rvItems.setFocusable(false);
            rvItems.setEnabled(false);
            rvItems.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));

            setFontsToViews();
            gridView1.setAdapter(new CustomGridAdapter(this, prgmNameList, prgmImages));
            gridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (position == 7) {
//
//                } else {
//                    if (position == 1) {
//                        Log.e("positiuon:", "" + classList[position]);
//                        intent_Calling(classList[position], "My Messages");
//                    } else {
                    Log.e("positiuon:", "" + classList[position]);
                    intentCalling(classList[position]);
                    // }
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
            llofferandProduct.setVisibility(View.GONE);
            mDrawerLayout.closeDrawer(lvSlidingMenu);
            showProfileImage();
            tvBikeName.setText(BikeName);
            modifyYourBikevenderDetail("0");
        } catch (Exception e) {
            Rollbar.reportException(e, "minor", "ModifierDetailActivity on create");
        }

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
            Toast.makeText(ModifierDetailActivity.this, "Image Address is Null", Toast.LENGTH_SHORT).show();
        } else {
            String imageUrl = prefsManager.getImageUrl();
            sdvDp.setImageURI(Uri.parse(imageUrl));
//            File file = new File(imageUrl);
//            sdvDp.setImageURI(Uri.fromFile(file));
        }

    }

    public void intentCalling(Class name) {
        Intent mIntent = new Intent(ModifierDetailActivity.this, name);
        startActivity(mIntent);

    }

    public void intent_Calling(Class name, String na) {
        Intent mIntent = new Intent(ModifierDetailActivity.this, name);
        mIntent.putExtra(SCREEN_OPEN, na);
        startActivity(mIntent);

    }

    private void setFontsToViews() {
        Typeface typefaceNormal = Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF");
        tvBikeName.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/Lato-Bold.ttf"));
        tvTitleToolbar.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/MACHINEN.TTF"));
        tvDealerName.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/Lato-Heavy.ttf"));
        tvSpeciality.setTypeface(typefaceNormal);
        tvOffers.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/Lato-Bold.ttf"));
        tvProducts.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/Lato-Bold.ttf"));
        tvViewAll.setTypeface(typefaceNormal);
        tvName.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/MACHINEN.TTF"));

    }

    private void setupActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_back);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
//                Intent intent = new Intent(ModifierDetailActivity.this, MainScreenActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.putExtra("MainScreenResponse", "OPEN");
//                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }

    @OnClick({R.id.ivGetLocation, R.id.ivMenu, R.id.rlProfile, R.id.btFullProfile, R.id.btUpdateProfile, R.id.tvOffers, R.id.tvProducts, R.id.ivVideo, R.id.ivFav})
    void click(View view) {
        switch (view.getId()) {
            case R.id.ivGetLocation:
                Intent mIntent = new Intent(activity, ModifierAddressActivity.class);
                mIntent.putExtra(KEY_ADDRESS, lastModifierDetailsData.getAddress());
                mIntent.putExtra(KEY_PHONE, lastModifierDetailsData.getPhone());
                mIntent.putExtra(KEY_START_DATE, lastModifierDetailsData.getStartTime());
                mIntent.putExtra(KEY_END_DATE, lastModifierDetailsData.getEndTime());
                mIntent.putExtra(KEY_COMPANY, lastModifierDetailsData.getCompany());
                mIntent.putExtra(KEY_VIDEO_URL, lastModifierDetailsData.getVideoCode());
                mIntent.putExtra(KEY_IS_FAV, fav);
                startActivity(mIntent);
                //startActivity(new Intent(activity, ModifierAddressActivity.class));
                break;
            case R.id.tvOffers:
                tvOffers.setBackgroundColor(Color.parseColor("#D1622A"));
                tvProducts.setBackgroundColor(Color.parseColor("#C6B9B3"));
                modifyYourBikevenderDetail("0");
                break;
            case R.id.tvProducts:
                tvOffers.setBackgroundColor(Color.parseColor("#C6B9B3"));
                tvProducts.setBackgroundColor(Color.parseColor("#D1622A"));
                modifyYourBikevenderDetail("1");
                break;
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
            case R.id.ivFav:
                modifyYourBikefavroite();
                break;
            case R.id.ivVideo:
                Log.e("VIDEO URL: ", "" + lastModifierDetailsData.getVideoCode());
                if (lastModifierDetailsData.getVideoCode().isEmpty() || lastModifierDetailsData.getVideoCode() == null) {
                    ivVideo.setClickable(false);
                    ivVideo.setClickable(false);
                    ivVideo.setFocusable(false);
                    ivVideo.setEnabled(false);
                    Toast.makeText(context, "Video not available", Toast.LENGTH_SHORT).show();
                } else {
                    Intent Intent = new Intent(activity, YouTubeVideoPlay.class);
                    Intent.putExtra("VIDEOURL", lastModifierDetailsData.getVideoCode());
                    startActivity(Intent);
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

    private void modifyYourBikevenderDetail(final String offres) {


        showProgressDialog();

        String userId = prefsManager.getCaseId();

        String accessToken = prefsManager.getToken();

        FileUploadService service = ServiceGenerator.createService(FileUploadService.BASE_URL);

        service.modifyYourBikevenderDetail(VenderId, BikeId, CatId, userId, offres, accessToken, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, retrofit.client.Response response) {

                Type type = new TypeToken<LastModifierDetail>() {
                }.getType();
                LastModifierDetail
                        lastModifierDetail = new Gson().fromJson(jsonObject.toString(), type);
                Log.e("jsonObject:", "" + jsonObject);
                stringArrayList.clear();
                if (lastModifierDetail.getSuccess() == 1) {
                    dismissProgressDialog();

                    lastModifierDetailsData = lastModifierDetail.getData();
                    stringArrayList.addAll(lastModifierDetailsData.getProducts());
                    if (lastModifierDetailsData.getOfferCount() == 0 && lastModifierDetailsData.getAllProductsCount() == 0) {
                        llofferandProduct.setVisibility(View.GONE);
                    } else {
                        llofferandProduct.setVisibility(View.VISIBLE);
                    }
                    Log.e("Size:", "" + stringArrayList.size());
                    if (stringArrayList.size() == 0) {
                        if (offres.equals("0")) {
                            tvOffers.setText("OFFERS(0)");
                            adapterPrdouct = new AdapterPrdouct(ModifierDetailActivity.this, stringArrayList);
                            rvItems.setAdapter(adapterPrdouct);
                        } else {
                            tvProducts.setText("PRODUCTS(0)");
                            adapterPrdouct = new AdapterPrdouct(ModifierDetailActivity.this, stringArrayList);
                            rvItems.setAdapter(adapterPrdouct);
                        }

                    } else {
//                        if (offres.equals("0")) {
                        tvOffers.setText("OFFERS(" + lastModifierDetailsData.getOfferCount() + ")");
                        // } else {
                        tvProducts.setText("PRODUCTS(" + lastModifierDetailsData.getAllProductsCount() + ")");
                        //  }

                        adapterPrdouct = new AdapterPrdouct(ModifierDetailActivity.this, stringArrayList);
                        rvItems.setAdapter(adapterPrdouct);
                    }

                    if (lastModifierDetailsData.getIsFavroite() == 1) {
                        ivFav.setImageResource(R.drawable.icon_remove_favorite);
                        // CustomDialog.FavshowProgressDialog(ModifierDetailActivity.this, lastModifierDetailsData.getMessage().toString());
                    } else {
                        ivFav.setImageResource(R.drawable.icon_add_favorites);
                        //CustomDialog.FavshowProgressDialog(ModifierDetailActivity.this, mFavoriteDestination.getMessage().toString());
                    }

                    fav = lastModifierDetailsData.getIsFavroite();
                    tvDealerName.setText(lastModifierDetailsData.getCompany());
                    tvTitleToolbar.setText(lastModifierDetailsData.getCompany());
                    tvAddress.setText(lastModifierDetailsData.getAddress());
                    String jsonInString = lastModifierDetailsData.getImage().toString();
                    jsonInString = jsonInString.replace("\\\"", "\"");
                    jsonInString = jsonInString.replace("\"{", "{");
                    jsonInString = jsonInString.replace("}\"", "}");
                    Log.e("jsonInString: ", "" + jsonInString);
                    Uri imageUri = Uri.parse(jsonInString);
                    sdv.setImageURI(imageUri);
                    tvSpeciality.setText(lastModifierDetailsData.getDescription());

                } else {
                    dismissProgressDialog();
                    CustomDialog.showProgressDialog(ModifierDetailActivity.this, lastModifierDetail.getMessage().toString());
                }


            }

            @Override
            public void failure(RetrofitError error) {
                dismissProgressDialog();
                Log.d("DataUploading------", "Data Uploading Failure......" + error);
            }
        });
    }


    private void modifyYourBikefavroite() {

        showProgressDialog();

        String userId = prefsManager.getCaseId();

        String accessToken = prefsManager.getToken();

        FileUploadService service = ServiceGenerator.createService(FileUploadService.BASE_URL);

        service.modifyYourBikefavroite(VenderId, userId, accessToken, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, retrofit.client.Response response) {

                Type type = new TypeToken<ModiflyBikeFav>() {
                }.getType();
                ModiflyBikeFav mFavoriteDestination = new Gson().fromJson(jsonObject.toString(), type);


                if (mFavoriteDestination.getSuccess() == 1) {
                    dismissProgressDialog();
                    fav = mFavoriteDestination.getFav();
                    if (mFavoriteDestination.getFav() == 1) {

                        ivFav.setImageResource(R.drawable.icon_remove_favorite);
                        CustomDialog.FavshowProgressDialog(ModifierDetailActivity.this, mFavoriteDestination.getMessage().toString());
                    } else {
                        ivFav.setImageResource(R.drawable.icon_add_favorites);
                        CustomDialog.FavshowProgressDialog(ModifierDetailActivity.this, mFavoriteDestination.getMessage().toString());
                    }


                } else {
                    dismissProgressDialog();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                dismissProgressDialog();
                Log.e("DataUploading------", "Data Uploading Failure......" + error);
            }
        });
    }


}