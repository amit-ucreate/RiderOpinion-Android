package com.nutsuser.ridersdomain.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.adapter.AdapterDestination;
import com.nutsuser.ridersdomain.adapter.CustomGridAdapter;
import com.nutsuser.ridersdomain.utils.ApplicationGlobal;
import com.nutsuser.ridersdomain.utils.CustomizeDialog;
import com.nutsuser.ridersdomain.utils.PrefsManager;
import com.nutsuser.ridersdomain.web.api.volley.RequestJsonObject;
import com.nutsuser.ridersdomain.web.pojos.FavoriteDestination;
import com.nutsuser.ridersdomain.web.pojos.Like;
import com.nutsuser.ridersdomain.web.pojos.LikeDetails;
import com.nutsuser.ridersdomain.web.pojos.RidingDestination;
import com.nutsuser.ridersdomain.web.pojos.RidingDestinationDetailsClick;
import com.nutsuser.ridersdomain.web.pojos.RidingDestinationDetailsClickInfo;

import org.json.JSONObject;

import java.lang.reflect.Type;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by user on 9/1/2015.
 */
public class DestinationsDetailActivity extends BaseActivity {
    CustomizeDialog mCustomizeDialog;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tvTitleToolbar)
    TextView tvTitleToolbar;
    private Activity activity;
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.lvSlidingMenu)
    LinearLayout lvSlidingMenu;
    @Bind(R.id.tvName)
    TextView tvName;
    @Bind(R.id.tvEatables)
    TextView tvEatables;
    @Bind(R.id.tvPetrolPump)
    TextView tvPetrolPump;
    @Bind(R.id.tvServiceCenter)
    TextView tvServiceCenter;
    @Bind(R.id.tvFirstAid)
    TextView tvFirstAid;
    @Bind(R.id.tvRides)
    TextView tvRides;
    @Bind(R.id.tvOffers)
    TextView tvOffers;

   @Bind(R.id.gridView1)
   GridView gridView1;
   // public static String [] prgmNameList={"Riding Destinations","Meet 'N' Plan A Ride","Riding Events \n    ","Modifly Your Bikes","Healthy Riding","Get Directions","Notifications","Settings"};
   // public static int [] prgmImages={R.drawable.icon_menu_destination,R.drawable.icon_menu_meetplan,R.drawable.icon_menu_events,R.drawable.icon_modifybike,R.drawable.icon_menu_healthy_riding,R.drawable.icon_menu_get_direction,R.drawable.icon_menu_notification,R.drawable.icon_menu_settings};
   // public static Class [] classList={DestinationsListActivity.class,PlanRideActivity.class,EventsListActivity.class,ModifyBikeActivity.class,HealthyRidingActivity.class,GetDirections.class,NotificationScreen.class,SettingsActivity.class};
   RidingDestinationDetailsClickInfo mRidingDestinationDetailsClickInfo;
    public static String [] prgmNameList={"My Rides","My Messages","My Friends","Chats","Favourite Destination","Notifications","Settings","    \n"};
    public static int [] prgmImages={R.drawable.ic_menu_fav_destinations,R.drawable.ic_menu_my_messages,R.drawable.ic_menu_my_friends,R.drawable.ic_menu_menu_chats,R.drawable.ic_menu_fav_destinations,R.drawable.ic_menu_menu_notifications,R.drawable.ic_menu_menu_settings,R.drawable.ic_menu_menu_blank_icon};
    public static Class [] classList={MyRidesRecyclerView.class,ChatListScreen.class,MyFriends.class,ChatListScreen.class,FavouriteDesination.class,Notification.class,SettingsActivity.class,SettingsActivity.class};
    String AccessToken,UserId,DestID;
    @Bind(R.id.sdvPostImage)
    SimpleDraweeView sdvPostImage;
    @Bind(R.id.tvPlace)
    TextView tvPlace;
    @Bind(R.id.tvLike)
    TextView tvLike;
    @Bind(R.id.tvDesc)
    TextView tvDesc;
    @Bind(R.id.ivFavorites)
    ImageView ivFavorites;
    @Bind(R.id.ivMap)
    ImageView ivMap;
    @Bind(R.id.ivVideo)
    ImageView ivVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination_detail);
        activity = this;
        ButterKnife.bind(activity);
        setupActionBar();
        setFontsToTextViews();
        Intent intent = getIntent();
        DestID= intent.getStringExtra("DestID");
        mDrawerLayout.closeDrawer(lvSlidingMenu);
        gridView1.setAdapter(new CustomGridAdapter(this, prgmNameList, prgmImages));
        gridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 7) {

                } else {
                    Log.e("position:", "" + classList[position]);
                    intentCalling(classList[position]);
                }
            }
        });
        RidingListDetailsmodelinfo();
    }
    public void intentCalling(Class name){
        Intent mIntent=new Intent(DestinationsDetailActivity.this,name);
        startActivity(mIntent);

    }
    private void setupActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void setFontsToTextViews() {
        tvTitleToolbar.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
        tvName.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
       // tvAddress.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
       // tvDestinations.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
       // tvEvents.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
        //tvModifyBike.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
       // tvMeetAndPlanRide.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
       // tvHealthyRiding.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
        //tvGetDirections.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
        //tvNotifications.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
        //tvSettings.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
    }

    @OnClick({R.id.ivBack, R.id.ivFilter, R.id.ivMap, R.id.ivMenu, R.id.rlProfile,R.id.ivFavorites,R.id.tvLike})
    void onclick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                onBackPressed();
                break;
            case R.id.ivFilter:
                startActivity(new Intent(activity, FilterActivity.class));
                break;

            case R.id.ivMap:
                String lat=mRidingDestinationDetailsClickInfo.getDestLatitude();
                String lon=mRidingDestinationDetailsClickInfo.getDestLongitude();
                Intent mIntent=new Intent(activity, MapActivity.class);
                mIntent.putExtra("endLat",lat);
                mIntent.putExtra("endLon",lon);
                startActivity(mIntent);
                //startActivity(new Intent(activity, MapActivity.class));
                break;
            case R.id.ivMenu:
                if (mDrawerLayout.isDrawerOpen(lvSlidingMenu))
                    mDrawerLayout.closeDrawer(lvSlidingMenu);
                else
                    mDrawerLayout.openDrawer(lvSlidingMenu);
                break;
            case R.id.rlProfile:
                startActivity(new Intent(activity, ProfileActivity.class));
                break;

            case R.id.ivFavorites:
                Favorite();
                break;
            case R.id.tvLike:
                Like();
                break;
            /*case R.id.ivMap:
                startActivity(new Intent(activity, ModifyBikeActivity.class));
                break;
            case R.id.tvMeetAndPlanRide:
                startActivity(new Intent(activity, PlanRideActivity.class));
                break;
            case R.id.tvHealthyRiding:
                startActivity(new Intent(activity, HealthyRidingActivity.class));
                break;

            case R.id.tvSettings:
                startActivity(new Intent(activity, SettingsActivity.class));
                break;*/
        }
    }

    /**
     * Destination Details info .
     */
    public void RidingListDetailsmodelinfo() {
        showProgressDialog();
        Log.e("riding destination", "riding destination");
        try {
            prefsManager=new PrefsManager(DestinationsDetailActivity.this);
            AccessToken=prefsManager.getToken();
            UserId=prefsManager.getCaseId();
            Log.e("AccessToken:",""+AccessToken+"----UserId----"+UserId);

            RequestQueue requestQueue = Volley.newRequestQueue(DestinationsDetailActivity.this);
            RequestJsonObject loginTaskRequest = new RequestJsonObject(Request.Method.POST,
                    ApplicationGlobal.ROOT+ApplicationGlobal.baseurl_ridingdetailDestination+"userId="+UserId+"&destId="+DestID+"&longitude=0.000000&latitude=0.000000&accessToken="+AccessToken, null,
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
                Log.e("Model response:",""+response);

                Type type = new TypeToken<RidingDestinationDetailsClick>() {
                }.getType();
                RidingDestinationDetailsClick mRidingDestination = new Gson().fromJson(response.toString(), type);


               // mRidingDestinationDetailses.clear();


                if (mRidingDestination.getSuccess().equals("1")){
                    mRidingDestinationDetailsClickInfo=mRidingDestination.getData();
                    tvPlace.setText(mRidingDestinationDetailsClickInfo.getDestName());
                    String jsonInString = mRidingDestinationDetailsClickInfo.getImages().toString();
                    jsonInString = jsonInString.replace("\\\"", "\"");
                    jsonInString = jsonInString.replace("\"{", "{");
                    jsonInString = jsonInString.replace("}\"", "}");
                    Log.e("jsonInString: ", "" + jsonInString);
                    Uri imageUri = Uri.parse(jsonInString);
                    sdvPostImage.setImageURI(imageUri);
                    tvLike.setText(mRidingDestinationDetailsClickInfo.getLikes());
                    tvDesc.setText(mRidingDestinationDetailsClickInfo.getDescription());
                    tvEatables.setText(mRidingDestinationDetailsClickInfo.getRestaurant());
                    tvPetrolPump.setText(mRidingDestinationDetailsClickInfo.getPetrolpumps());
                    tvServiceCenter.setText(mRidingDestinationDetailsClickInfo.getServiceStation());
                    tvFirstAid.setText(mRidingDestinationDetailsClickInfo.getHospitals());
                    tvRides.setText(mRidingDestinationDetailsClickInfo.getRiders());
                    tvOffers.setText(mRidingDestinationDetailsClickInfo.getOffers());
                    if(mRidingDestinationDetailsClickInfo.getFavroite().matches("1")){
                        ivFavorites.setImageResource(R.drawable.icon_remove_favorite);
                    }
                    else{
                        ivFavorites.setImageResource(R.drawable.icon_add_favorites);
                    }
                    dismissProgressDialog();
                   // mRidingDestinationDetailses.addAll(mRidingDestination.getData());
                   // rvDestinations.setAdapter(new AdapterDestination(DestinationsDetailActivity.this,mRidingDestinationDetailses));
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

    public  void showProgressDialog() {

        mCustomizeDialog = new CustomizeDialog(DestinationsDetailActivity.this);
        mCustomizeDialog.setCancelable(false);
        mCustomizeDialog.show();
        Log.e("HERE", "HERE");
    }

    public  void dismissProgressDialog() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mCustomizeDialog != null && mCustomizeDialog.isShowing()) {
                    mCustomizeDialog.dismiss();
                    mCustomizeDialog = null;
                }
            }
        }, 1000);

    }


    /**
     * Like Details info .
     */
    public void Like() {
        showProgressDialog();
        Log.e("riding destination", "riding destination");
        try {
            prefsManager=new PrefsManager(DestinationsDetailActivity.this);
            AccessToken=prefsManager.getToken();
            UserId=prefsManager.getCaseId();
            Log.e("AccessToken:",""+AccessToken+"----UserId----"+UserId);

            RequestQueue requestQueue = Volley.newRequestQueue(DestinationsDetailActivity.this);
            RequestJsonObject loginTaskRequest = new RequestJsonObject(Request.Method.POST,
                    ApplicationGlobal.ROOT+ApplicationGlobal.baseurl_like+"userId="+UserId+"&destId="+DestID+"&accessToken="+AccessToken, null,
                    volleyModelLikeErrorListener(), volleyModelLikeSuccessListener()
            );

            requestQueue.add(loginTaskRequest);

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    /**
     * Implement success listener on execute api url.
     */
    public Response.Listener<JSONObject> volleyModelLikeSuccessListener() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("Model response:",""+response);

                Type type = new TypeToken<Like>() {
                }.getType();
                Like like = new Gson().fromJson(response.toString(), type);
                LikeDetails mLikeDetails;

                // mRidingDestinationDetailses.clear();


                if (like.getSuccess().equals("1")){
                    mLikeDetails=like.getData();

                    tvLike.setText(mLikeDetails.getLikes());

                    dismissProgressDialog();
                    // mRidingDestinationDetailses.addAll(mRidingDestination.getData());
                    // rvDestinations.setAdapter(new AdapterDestination(DestinationsDetailActivity.this,mRidingDestinationDetailses));
                }

            }
        };
    }

    /**
     * Implement Volley error listener here.
     */
    public Response.ErrorListener volleyModelLikeErrorListener() {
        dismissProgressDialog();
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error: ", "" + error);
            }
        };
    }
    /**
     * Favorite Details info .
     */
    public void Favorite() {
        showProgressDialog();
        Log.e("riding destination", "riding destination");
        try {
            prefsManager=new PrefsManager(DestinationsDetailActivity.this);
            AccessToken=prefsManager.getToken();
            UserId=prefsManager.getCaseId();
            Log.e("AccessToken:",""+AccessToken+"----UserId----"+UserId);

            RequestQueue requestQueue = Volley.newRequestQueue(DestinationsDetailActivity.this);
            RequestJsonObject loginTaskRequest = new RequestJsonObject(Request.Method.POST,
                    ApplicationGlobal.ROOT+ApplicationGlobal.baseurl_favroite+"userId="+UserId+"&destId="+DestID+"&accessToken="+AccessToken, null,
                    volleyModelFavoriteErrorListener(), volleyModelFavoriteSuccessListener()
            );

            requestQueue.add(loginTaskRequest);

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    /**
     * Implement success listener on execute api url.
     */
    public Response.Listener<JSONObject> volleyModelFavoriteSuccessListener() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("Model response:",""+response);

                Type type = new TypeToken<FavoriteDestination>() {
                }.getType();
                FavoriteDestination mFavoriteDestination = new Gson().fromJson(response.toString(), type);


                if (mFavoriteDestination.getSuccess().equals("1")){
                    if(mFavoriteDestination.getFav().matches("1")){
                        ivFavorites.setImageResource(R.drawable.icon_remove_favorite);
                    }
                    else{
                        ivFavorites.setImageResource(R.drawable.icon_add_favorites);
                    }


                   // tvLike.setText(mLikeDetails.getLikes());

                    dismissProgressDialog();
                    // mRidingDestinationDetailses.addAll(mRidingDestination.getData());
                    // rvDestinations.setAdapter(new AdapterDestination(DestinationsDetailActivity.this,mRidingDestinationDetailses));
                }

            }
        };
    }

    /**
     * Implement Volley error listener here.
     */
    public Response.ErrorListener volleyModelFavoriteErrorListener() {
        dismissProgressDialog();
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error: ", "" + error);
            }
        };
    }

}
