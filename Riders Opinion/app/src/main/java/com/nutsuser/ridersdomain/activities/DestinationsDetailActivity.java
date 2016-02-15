package com.nutsuser.ridersdomain.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.adapter.CustomGridAdapter;
import com.nutsuser.ridersdomain.adapter.EndPlaceArrayAdapter;
import com.nutsuser.ridersdomain.adapter.PlaceArrayAdapter;
import com.nutsuser.ridersdomain.route.GMapV2GetRouteDirection;
import com.nutsuser.ridersdomain.utils.ApplicationGlobal;
import com.nutsuser.ridersdomain.utils.CustomizeDialog;
import com.nutsuser.ridersdomain.utils.PrefsManager;
import com.nutsuser.ridersdomain.web.api.volley.RequestJsonObject;
import com.nutsuser.ridersdomain.web.pojos.FavoriteDestination;
import com.nutsuser.ridersdomain.web.pojos.Like;
import com.nutsuser.ridersdomain.web.pojos.LikeDetails;
import com.nutsuser.ridersdomain.web.pojos.RidingDestinationDetailsClick;
import com.nutsuser.ridersdomain.web.pojos.RidingDestinationDetailsClickInfo;

import org.json.JSONObject;
import org.w3c.dom.Document;

import java.lang.reflect.Type;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Amit Agnihotri on 9/1/2015.
 */
public class DestinationsDetailActivity extends BaseActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {

    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));
    private static final String LOG_TAG = "GetDirection";
    private static final int GOOGLE_API_CLIENT_ID = 0;
    public static String[] prgmNameList = {"My Rides", "My Messages", "My Friends", "Chats", "Favourite Destination", "Notifications", "Settings", "    \n"};
    public static int[] prgmImages = {R.drawable.ic_menu_fav_destinations, R.drawable.ic_menu_my_messages, R.drawable.ic_menu_my_friends, R.drawable.ic_menu_menu_chats, R.drawable.ic_menu_fav_destinations, R.drawable.ic_menu_menu_notifications, R.drawable.ic_menu_menu_settings, R.drawable.ic_menu_menu_blank_icon};
    public static Class[] classList = {MyRidesRecyclerView.class, ChatListScreen.class, MyFriends.class, ChatListScreen.class, FavouriteDesination.class, Notification.class, SettingsActivity.class, SettingsActivity.class};
    GMapV2GetRouteDirection v2GetRouteDirection;
    LocationManager locManager;
    Drawable drawable;
    Document document;
    EndPlaceArrayAdapter _ArrayAdapter;
    LatLng mString_end;
    LatLng mString_start;
    double start, end;
    CustomizeDialog mCustomizeDialog;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tvTitleToolbar)
    TextView tvTitleToolbar;
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
    String jsonInString;
    // public static String [] prgmNameList={"Riding Destinations","Meet 'N' Plan A Ride","Riding Events \n    ","Modifly Your Bikes","Healthy Riding","Get Directions","Notifications","Settings"};
    // public static int [] prgmImages={R.drawable.icon_menu_destination,R.drawable.icon_menu_meetplan,R.drawable.icon_menu_events,R.drawable.icon_modifybike,R.drawable.icon_menu_healthy_riding,R.drawable.icon_menu_get_direction,R.drawable.icon_menu_notification,R.drawable.icon_menu_settings};
    // public static Class [] classList={DestinationsListActivity.class,PlanRideActivity.class,EventsListActivity.class,ModifyBikeActivity.class,HealthyRidingActivity.class,GetDirections.class,NotificationScreen.class,SettingsActivity.class};
    RidingDestinationDetailsClickInfo mRidingDestinationDetailsClickInfo;
    String AccessToken, UserId;
    public static String DestID;
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
    // Google Map
    private GoogleMap map;
    private AutoCompleteTextView tvPlace3, tvPlace4;
    private GoogleApiClient mGoogleApiClient;
    private PlaceArrayAdapter mPlaceArrayAdapter;
    private Activity activity;
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback_start;
    private AdapterView.OnItemClickListener mAutocompleteClickListener_start
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            Log.i(LOG_TAG, "Selected: " + item.description);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback_start);
            Log.i(LOG_TAG, "Fetching details for ID: " + item.placeId);
        }
    };
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback_end
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e(LOG_TAG, "Place query did not complete. Error: " +
                        places.getStatus().toString());
                return;
            }
            // Selecting the first object buffer.
            final Place place = places.get(0);
            CharSequence attributions = places.getAttributions();

            // mNameTextView.setText("NAME:"+ Html.fromHtml(place.getName() + ""));
            // mAddressTextView.setText("ADDRESS: "+Html.fromHtml(place.getAddress() + ""));
            //  mIdTextView.setText(Html.fromHtml("PLACEID:" + place.getId() + ""));
            String s = "" + Html.fromHtml("" + place.getLatLng().latitude);
            String s1 = "" + Html.fromHtml("" + place.getLatLng().longitude);
            double start = Double.valueOf(s.trim()).doubleValue();
            double end = Double.valueOf(s1.trim()).doubleValue();


            mString_end = new LatLng(start, end);
            hideKeyboard();
            //mString_end =Html.fromHtml(place.getLatLng() + "");

            Log.e("end:", "" + mString_end);
            //mPhoneTextView.setText(Html.fromHtml("Lat:" + place.getLatLng().latitude + "--long:" + latlong));
            //mWebTextView.setText(place.getWebsiteUri() + "");
            if (attributions != null) {
                // mAttTextView.setText(Html.fromHtml(attributions.toString()));
            }
        }

    };
    private AdapterView.OnItemClickListener mAutocompleteClickListener_end
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final EndPlaceArrayAdapter.PlaceAutocomplete item = _ArrayAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            Log.i(LOG_TAG, "Selected: " + item.description);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback_end);
            Log.i(LOG_TAG, "Fetching details for ID: " + item.placeId);
        }
    };

    {
        mUpdatePlaceDetailsCallback_start = new ResultCallback<PlaceBuffer>() {
            @Override
            public void onResult(PlaceBuffer places) {
                if (!places.getStatus().isSuccess()) {
                    Log.e(LOG_TAG, "Place query did not complete. Error: " +
                            places.getStatus().toString());
                    return;
                }
                // Selecting the first object buffer.
                final Place place = places.get(0);
                CharSequence attributions = places.getAttributions();

                // mNameTextView.setText("NAME:"+ Html.fromHtml(place.getName() + ""));
                // mAddressTextView.setText("ADDRESS: "+Html.fromHtml(place.getAddress() + ""));
                //  mIdTextView.setText(Html.fromHtml("PLACEID:" + place.getId() + ""));
                String s = "" + Html.fromHtml("" + place.getLatLng().latitude);
                String s1 = "" + Html.fromHtml("" + place.getLatLng().longitude);
                start = Double.valueOf(s.trim()).doubleValue();
                end = Double.valueOf(s1.trim()).doubleValue();
                mString_start = new LatLng(start, end);

                Log.e("start:", "" + mString_start);

                //  mPhoneTextView.setText(Html.fromHtml("Lat:" + place.getLatLng().latitude + "--long:" + latlong));
                //mWebTextView.setText(place.getWebsiteUri() + "");
                if (attributions != null) {
                    // mAttTextView.setText(Html.fromHtml(attributions.toString()));
                }
            }

        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination_detail);
        v2GetRouteDirection = new GMapV2GetRouteDirection();
        activity = this;
        ButterKnife.bind(activity);
        setupActionBar();
        setFontsToTextViews();
        Intent intent = getIntent();
        DestID = intent.getStringExtra("DestID");
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

        mGoogleApiClient = new GoogleApiClient.Builder(DestinationsDetailActivity.this)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, GOOGLE_API_CLIENT_ID, DestinationsDetailActivity.this)
                .addConnectionCallbacks(this)
                .addApi(AppIndex.API).build();
        tvPlace3 = (AutoCompleteTextView) findViewById(R.id
                .tvPlace3);
        tvPlace4 = (AutoCompleteTextView) findViewById(R.id
                .tvPlace4);
        tvPlace3.setThreshold(3);
        tvPlace4.setThreshold(3);
        tvPlace3.setOnItemClickListener(mAutocompleteClickListener_start);
        mPlaceArrayAdapter = new PlaceArrayAdapter(this, android.R.layout.simple_list_item_1,
                BOUNDS_MOUNTAIN_VIEW, null);
        tvPlace3.setAdapter(mPlaceArrayAdapter);
        tvPlace4.setOnItemClickListener(mAutocompleteClickListener_end);
        _ArrayAdapter = new EndPlaceArrayAdapter(this, android.R.layout.simple_list_item_1,
                BOUNDS_MOUNTAIN_VIEW, null);
        tvPlace4.setAdapter(_ArrayAdapter);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                hideKeyboard();
            }
        }, 300);


        RidingListDetailsmodelinfo();


    }

    private void hideKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public void intentCalling(Class name) {
        Intent mIntent = new Intent(DestinationsDetailActivity.this, name);
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

    @OnClick({R.id.ivBack, R.id.ivFilter, R.id.ivMap, R.id.ivMenu, R.id.rlProfile, R.id.ivFavorites, R.id.tvLike, R.id.ivVideo, R.id.btMap,R.id.tvShare})
    void onclick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                onBackPressed();
                break;
            case R.id.ivFilter:
                startActivity(new Intent(activity, FilterActivity.class));
                break;

            case R.id.ivVideo:
                Log.e("VIDEO URL: ", "" + mRidingDestinationDetailsClickInfo.getVideoUrl());
                Intent Intent = new Intent(activity, YouTubeVideoPlay.class);
                Intent.putExtra("VIDEOURL", mRidingDestinationDetailsClickInfo.getVideoUrl());
                startActivity(Intent);
                break;

            case R.id.ivMap:
                String lat = mRidingDestinationDetailsClickInfo.getDestLatitude();
                String lon = mRidingDestinationDetailsClickInfo.getDestLongitude();
                Intent mIntent = new Intent(activity, Subcribe.class);
               // mIntent.putExtra("endLat", lat);
               // mIntent.putExtra("endLon", lon);
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
            case R.id.btMap:
                GetRouteTask getRoute = new GetRouteTask();
                getRoute.execute();
                break;
            case R.id.tvShare:
                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(android.content.Intent.EXTRA_TEXT, jsonInString);
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Destiantion Picture");
                startActivity(android.content.Intent.createChooser(intent, "Share"));
                break;
            /*case R.id.tvHealthyRiding:
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
            prefsManager = new PrefsManager(DestinationsDetailActivity.this);
            AccessToken = prefsManager.getToken();
            UserId = prefsManager.getCaseId();
            Log.e("AccessToken:", "" + AccessToken + "----UserId----" + UserId);

            RequestQueue requestQueue = Volley.newRequestQueue(DestinationsDetailActivity.this);
            RequestJsonObject loginTaskRequest = new RequestJsonObject(Request.Method.POST,
                    ApplicationGlobal.ROOT + ApplicationGlobal.baseurl_ridingdetailDestination + "userId=" + UserId + "&destId=" + DestID + "&longitude=0.000000&latitude=0.000000&accessToken=" + AccessToken, null,
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
                Log.e("Model response:", "" + response);

                Type type = new TypeToken<RidingDestinationDetailsClick>() {
                }.getType();
                RidingDestinationDetailsClick mRidingDestination = new Gson().fromJson(response.toString(), type);


                // mRidingDestinationDetailses.clear();


                if (mRidingDestination.getSuccess().equals("1")) {
                    mRidingDestinationDetailsClickInfo = mRidingDestination.getData();
                    tvPlace.setText(mRidingDestinationDetailsClickInfo.getDestName());
                    jsonInString = mRidingDestinationDetailsClickInfo.getImages().toString();
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
                    if (mRidingDestinationDetailsClickInfo.getFavroite().matches("1")) {
                        ivFavorites.setImageResource(R.drawable.icon_remove_favorite);
                    } else {
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

    public void showProgressDialog() {

        mCustomizeDialog = new CustomizeDialog(DestinationsDetailActivity.this);
        mCustomizeDialog.setCancelable(false);
        mCustomizeDialog.show();
        Log.e("HERE", "HERE");
    }

    public void dismissProgressDialog() {
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
            prefsManager = new PrefsManager(DestinationsDetailActivity.this);
            AccessToken = prefsManager.getToken();
            UserId = prefsManager.getCaseId();
            Log.e("AccessToken:", "" + AccessToken + "----UserId----" + UserId);

            RequestQueue requestQueue = Volley.newRequestQueue(DestinationsDetailActivity.this);
            RequestJsonObject loginTaskRequest = new RequestJsonObject(Request.Method.POST,
                    ApplicationGlobal.ROOT + ApplicationGlobal.baseurl_like + "userId=" + UserId + "&destId=" + DestID + "&accessToken=" + AccessToken, null,
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
                Log.e("Model response:", "" + response);

                Type type = new TypeToken<Like>() {
                }.getType();
                Like like = new Gson().fromJson(response.toString(), type);
                LikeDetails mLikeDetails;

                // mRidingDestinationDetailses.clear();


                if (like.getSuccess().equals("1")) {
                    mLikeDetails = like.getData();

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
            prefsManager = new PrefsManager(DestinationsDetailActivity.this);
            AccessToken = prefsManager.getToken();
            UserId = prefsManager.getCaseId();
            Log.e("AccessToken:", "" + AccessToken + "----UserId----" + UserId);

            RequestQueue requestQueue = Volley.newRequestQueue(DestinationsDetailActivity.this);
            RequestJsonObject loginTaskRequest = new RequestJsonObject(Request.Method.POST,
                    ApplicationGlobal.ROOT + ApplicationGlobal.baseurl_favroite + "userId=" + UserId + "&destId=" + DestID + "&accessToken=" + AccessToken, null,
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
                Log.e("Model response:", "" + response);

                Type type = new TypeToken<FavoriteDestination>() {
                }.getType();
                FavoriteDestination mFavoriteDestination = new Gson().fromJson(response.toString(), type);


                if (mFavoriteDestination.getSuccess().equals("1")) {
                    if (mFavoriteDestination.getFav().matches("1")) {
                        ivFavorites.setImageResource(R.drawable.icon_remove_favorite);
                    } else {
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

    @Override
    public void onConnected(Bundle bundle) {
        _ArrayAdapter.setGoogleApiClient(mGoogleApiClient);
        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
        Log.i(LOG_TAG, "Google Places API connected.");
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

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mGoogleApiClient.connect();

    }

    @Override
    protected void onStop() {
        super.onStop();

        mGoogleApiClient.disconnect();
    }

    private void initializeMap() {
        if (map == null) {
            map = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getMap();

            // check if map is created successfully or not
            if (map == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            } else {

                // map.setMyLocationEnabled(true);
                map.getUiSettings().setZoomControlsEnabled(true);
                map.getUiSettings().setCompassEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(true);
                map.getUiSettings().setAllGesturesEnabled(true);
                map.setTrafficEnabled(true);

            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initializeMap();
    }

    /**
     * @author Amit Agnihotri
     *         This class Get Route on the map
     */
    private class GetRouteTask extends AsyncTask<String, Void, String> {

        String response = "";
        private ProgressDialog Dialog;

        @Override
        protected void onPreExecute() {
            Dialog = new ProgressDialog(DestinationsDetailActivity.this);
            Dialog.setMessage("Loading route...");
            Dialog.show();
        }

        @Override
        protected String doInBackground(String... urls) {
            //Get All Route values
            document = v2GetRouteDirection.getDocument(mString_start, mString_end, GMapV2GetRouteDirection.MODE_DRIVING);
            response = "Success";
            return response;

        }

        @Override
        protected void onPostExecute(String result) {
            map.clear();
            if (response.equalsIgnoreCase("Success")) {
                ArrayList<LatLng> directionPoint = v2GetRouteDirection.getDirection(document);
                PolylineOptions rectLine = new PolylineOptions().width(10).color(
                        Color.parseColor("#D1622A"));

                for (int i = 0; i < directionPoint.size(); i++) {
                    rectLine.add(directionPoint.get(i));
                }
                LatLng latLng = new LatLng(start, end);

                // Adding route on the map
                map.addPolyline(rectLine);
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 9);
                map.animateCamera(cameraUpdate);
                //  mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(12));
                //mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(6),1000, null);
                // markerOptions.position(toPosition);
                // markerOptions.draggable(true);
                // mGoogleMap.addMarker(markerOptions);

            }

            Dialog.dismiss();
        }
    }
}
