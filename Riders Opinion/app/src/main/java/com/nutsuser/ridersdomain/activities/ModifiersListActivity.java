package com.nutsuser.ridersdomain.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.adapter.AdapterModifier;
import com.nutsuser.ridersdomain.adapter.CustomGridAdapter;
import com.nutsuser.ridersdomain.chat.SampleCometChatActivity;
import com.nutsuser.ridersdomain.utils.CustomizeDialog;
import com.nutsuser.ridersdomain.utils.PrefsManager;
import com.nutsuser.ridersdomain.view.BetterPopupWindow;
import com.nutsuser.ridersdomain.view.CustomDialog;
import com.nutsuser.ridersdomain.web.api.FileUploadService;
import com.nutsuser.ridersdomain.web.api.ServiceGenerator;
import com.nutsuser.ridersdomain.web.pojos.ModifyBikeListData;
import com.nutsuser.ridersdomain.web.pojos.ModilyBikeList;
import com.nutsuser.ridersdomain.web.pojos.VehicleDetails;
import com.nutsuser.ridersdomain.web.pojos.VehicleName;
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
 * Created by user on 9/30/2015.
 */
public class ModifiersListActivity extends BaseActivity {
    CustomizeDialog mCustomizeDialog;
    public PrefsManager prefsManager;
    ArrayList<ModifyBikeListData> modifyBikeListDatas=new ArrayList<>();
    String VenderId,BikeName;
    public static String[] prgmNameList = {"My Rides", "My Messages", "My Friends", "Chats", "Favorites", "Notifications", "Settings", "Riders Near By"};
    public static int[] prgmImages = {R.drawable.ic_menu_my_rides, R.drawable.ic_menu_my_messages, R.drawable.menu_my_friends, R.drawable.ic_menu_menu_chats, R.drawable.menu_fav_destinations, R.drawable.ic_menu_menu_notifications, R.drawable.menu_settings, R.drawable.icon_nearby};
    public static Class[] classList = {MyRidesRecyclerView.class, RecentChatListActivity.class,MyFriendsActivity.class, ComingSoon.class, FavouriteDesination.class, NotificationListActivity.class, SettingsActivity.class, NearByFriendsAcitivity.class};
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.lvDealers)
    ListView lvDealers;
    @Bind(R.id.tvTitleToolbar)
    TextView tvTitleToolbar;
    @Bind(R.id.tvBikeName)
    TextView tvBikeName;
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
    @Bind(R.id.tvName)
    TextView tvName;
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.lvSlidingMenu)
    LinearLayout lvSlidingMenu;
    // public static String [] prgmNameList={"Riding Destinations","Meet 'N' Plan A Ride","Riding Events \n    ","Modifly Your Bikes","Healthy Riding","Get Directions","Notifications","Settings"};
    //public static int [] prgmImages={R.drawable.icon_menu_destination,R.drawable.icon_menu_meetplan,R.drawable.icon_menu_events,R.drawable.icon_modifybike,R.drawable.icon_menu_healthy_riding,R.drawable.icon_menu_get_direction,R.drawable.icon_menu_notification,R.drawable.icon_menu_settings};
    //  public static Class [] classList={DestinationsListActivity.class,PlanRideActivity.class,EventsListActivity.class,ModifyBikeActivity.class,HealthyRidingActivity.class,GetDirections.class,NotificationScreen.class,SettingsActivity.class};
    @Bind(R.id.gridView1)
    GridView gridView1;
    /*    @Bind(R.id.tvAddress)
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
    private AdapterModifier adapterModifier;
    private Activity activity;
    @Bind(R.id.sdvDp)
    SimpleDraweeView sdvDp;
    // drop down code
    View view;
    private ArrayList<VehicleDetails> mVehicleDetailses = new ArrayList<VehicleDetails>();
    CustomBaseAdapter adapter;
    DemoPopupWindow dw;
    String vehicleId,BikeId;
    @Bind(R.id.btUpdateProfile)
    Button btUpdateProfile;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifiers_list);
        try {
            activity = this;
            ButterKnife.bind(this);
            setupActionBar();
            setFontsToViews();
            view = new View(this);
            prefsManager = new PrefsManager(this);
            VenderId = getIntent().getStringExtra("VenderId");
            BikeName = getIntent().getStringExtra("BikeName");
            BikeId = getIntent().getStringExtra("BikeId");

            lvDealers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Intent intent = new Intent(ModifiersListActivity.this, ModifierDetailActivity.class);
                    intent.putExtra("VenderId", modifyBikeListDatas.get(position).getVenderId());
                    intent.putExtra("CatId", VenderId);
                    intent.putExtra("BikeId", BikeId);
                    intent.putExtra("BikeName", tvBikeName.getText().toString());
                    startActivity(intent);
                }
            });
            gridView1.setAdapter(new CustomGridAdapter(this, prgmNameList, prgmImages));
            gridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (position == 7) {
//
//                } else {
//                    if(position==1){
//                        Log.e("positiuon:", "" + classList[position]);
//                        intent_Calling(classList[position], "My Messages");
//                    }


                    //   else{
                    Log.e("positiuon:", "" + classList[position]);
                    intentCalling(classList[position]);
                    //      }
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
            if(!BikeId.isEmpty()){
                if(isNetworkConnected()) {
                    modifyVender(BikeId);
                }
            }
            if(!BikeName.isEmpty()) {
                tvBikeName.setText(BikeName);
            }
        }catch(Exception e){
            Rollbar.reportException(e, "minor", "ModifierListActivity on create");
        }

    }
    /** '
     *Show Profile Image
     ***/
    private void showProfileImage(){
        if(prefsManager.getUserName()==null){
            tvName.setText("No Name");
        }
        else{
            tvName.setText(prefsManager.getUserName());
        }
        if (prefsManager.getImageUrl() == null) {
            Toast.makeText(ModifiersListActivity.this, "Image Address is Null", Toast.LENGTH_SHORT).show();
        }
        else {
            String imageUrl = prefsManager.getImageUrl();
            sdvDp.setImageURI(Uri.parse(imageUrl));
//            File file = new File(imageUrl);
//            sdvDp.setImageURI(Uri.fromFile(file));
        }

    }
    public void intentCalling(Class name) {
        Intent mIntent = new Intent(ModifiersListActivity.this, name);
        startActivity(mIntent);

    }

    public void intent_Calling(Class name,String na) {
        Intent mIntent = new Intent(ModifiersListActivity.this, name);
        mIntent.putExtra(SCREEN_OPEN,na);
        startActivity(mIntent);

    }
    private void setFontsToViews() {
        Typeface typefaceNormal = Typeface.createFromAsset(getResources().getAssets(), "fonts/LATO-REGULAR.TTF");
        Typeface typefaceTitle = Typeface.createFromAsset(getResources().getAssets(), "fonts/MACHINEN.TTF");
        tvBikeName.setTypeface(typefaceNormal);
        tvTitleToolbar.setTypeface(typefaceTitle);
        tvSponsoredAdTitle1.setTypeface(typefaceNormal);
        tvSponsoredAdTitle2.setTypeface(typefaceNormal);
        tvSponsoredAdLocation1.setTypeface(typefaceNormal);
        tvSponsoredAdLocation2.setTypeface(typefaceNormal);
        tvSponsoredAdReviews1.setTypeface(typefaceNormal);
        tvSponsoredAdReviews2.setTypeface(typefaceNormal);
        tvName.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/MACHINEN.TTF"));
        // tvAddress.setTypeface(typefaceNormal);
        // tvDestinations.setTypeface(typefaceNormal);
        // tvEvents.setTypeface(typefaceNormal);
        //tvModifyBike.setTypeface(typefaceNormal);
        // tvMeetAndPlanRide.setTypeface(typefaceNormal);
        // //tvHealthyRiding.setTypeface(typefaceNormal);
        //tvGetDirections.setTypeface(typefaceNormal);
        // tvNotifications.setTypeface(typefaceNormal);
        // tvSettings.setTypeface(typefaceNormal);
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
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ModifiersListActivity.this,ModifyBikeActivity.class).putExtra("BikeId",BikeId));
        finish();
    }

    @OnClick({R.id.ivMenu, R.id.rlProfile,R.id.btFullProfile,R.id.btUpdateProfile,R.id.tvBikeName})
    void click(View view) {
        switch (view.getId()) {
            case R.id.tvBikeName:
                vechicleinfo();
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
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (mDrawerLayout.isDrawerOpen(lvSlidingMenu))
        {
            showProfileImage();
            mDrawerLayout.closeDrawer(lvSlidingMenu);
        }
    }
    /** modifyVender Data (Only Strings) to Server  **/
    private void  modifyVender(String bikid){

        showProgressDialog();

        String userId = prefsManager.getCaseId();

        String accessToken = prefsManager.getToken();

        FileUploadService service = ServiceGenerator.createService(FileUploadService.BASE_URL);

        service.venders(bikid,VenderId, userId, accessToken, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, retrofit.client.Response response) {

                Type type = new TypeToken<ModilyBikeList>() {
                }.getType();
                ModilyBikeList
                        modiflyBikeDatas = new Gson().fromJson(jsonObject.toString(), type);
                Log.e("jsonObject:", "" + jsonObject);
                modifyBikeListDatas.clear();
                if (modiflyBikeDatas.getSuccess() == 1) {
                    dismissProgressDialog();
                    modifyBikeListDatas.addAll(modiflyBikeDatas.getData());

                    // gvModifiers.setAdapter(new AdapterModify(activity,modiflyBikeDatas));
                    adapterModifier = new AdapterModifier(ModifiersListActivity.this,modifyBikeListDatas);
                    lvDealers.setAdapter(adapterModifier);
                } else {
                    dismissProgressDialog();
                    adapterModifier = new AdapterModifier(ModifiersListActivity.this,modifyBikeListDatas);
                    lvDealers.setAdapter(adapterModifier);
                    CustomDialog.showProgressDialog(ModifiersListActivity.this, modiflyBikeDatas.getMessage().toString());
                }


            }

            @Override
            public void failure(RetrofitError error) {
                dismissProgressDialog();
                Log.d("DataUploading------", "Data Uploading Failure......" + error);
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

                    Log.e("response: ", "" + response);
                    if (mVehicleName.getSuccess().equals("1")) {
                        dismissProgressDialog();

                        mVehicleDetailses.addAll(mVehicleName.getData());


                        dw = new DemoPopupWindow(view, ModifiersListActivity.this);
                        dw.showLikeQuickAction(0, 0);

                    }
                    else{
                        dismissProgressDialog();

                    }


                }

                @Override
                public void failure(RetrofitError error) {
                    dismissProgressDialog();
                    Log.d("DataUploading------", "Data Uploading Failure......" + error);
                    CustomDialog.showProgressDialog(ModifiersListActivity.this, error.toString());
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
            Rollbar.reportException(e, "minor", "ModifierListActivity getVehicleInfo API");

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
            adapter = new CustomBaseAdapter(ModifiersListActivity.this, mVehicleDetailses);
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
                    tvBikeName.setText(mVehicleDetailses.get(position).getVehicle_name());
                    dw.dismiss();
                    BikeId=vehicleId;
                    modifyVender(BikeId);
                    //modifyYourBike(vehicleId);
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
