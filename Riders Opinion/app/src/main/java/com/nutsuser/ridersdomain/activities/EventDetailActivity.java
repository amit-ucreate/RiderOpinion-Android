package com.nutsuser.ridersdomain.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.inscripts.cometchat.sdk.CometChatroom;
import com.inscripts.interfaces.Callbacks;
import com.inscripts.interfaces.SubscribeChatroomCallbacks;
import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.adapter.CustomGridAdapter;
import com.nutsuser.ridersdomain.database.DatabaseHandler;
import com.nutsuser.ridersdomain.database.Keys;
import com.nutsuser.ridersdomain.database.SharedPreferenceHelper;
import com.nutsuser.ridersdomain.database.Utils;
import com.nutsuser.ridersdomain.utils.ApplicationGlobal;
import com.nutsuser.ridersdomain.utils.CustomizeDialog;
import com.nutsuser.ridersdomain.utils.PrefsManager;
import com.nutsuser.ridersdomain.view.CustomDialog;
import com.nutsuser.ridersdomain.web.api.FileUploadService;
import com.nutsuser.ridersdomain.web.api.ServiceGenerator;
import com.nutsuser.ridersdomain.web.api.volley.RequestJsonObject;
import com.nutsuser.ridersdomain.web.pojos.ChatroomChatMessage;
import com.nutsuser.ridersdomain.web.pojos.PlanRideDetails;
import com.nutsuser.ridersdomain.web.pojos.PlanRideDetailsData;
import com.nutsuser.ridersdomain.web.pojos.RiderJoined;
import com.rollbar.android.Rollbar;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by user on 9/25/2015.
 */
public class EventDetailActivity extends BaseActivity {

    public static String[] prgmNameList = {"My Rides", "My Messages", "My Friends", "Chats", "Favorites", "Notifications", "Settings", "Riders Near By"};
    public static int[] prgmImages = {R.drawable.ic_menu_my_rides, R.drawable.ic_menu_my_messages, R.drawable.menu_my_friends, R.drawable.ic_menu_menu_chats, R.drawable.menu_fav_destinations, R.drawable.ic_menu_menu_notifications, R.drawable.menu_settings, R.drawable.icon_nearby};
    public static Class[] classList = {MyRidesRecyclerView.class, RecentChatListActivity.class, MyFriendsActivity.class, ComingSoon.class, FavouriteDesination.class, NotificationListActivity.class, SettingsActivity.class, NearByFriendsAcitivity.class};
    PlanRideDetailsData planRideDetailsData;
    String AccessToken, UserId, eventId, LOCATION;
    CustomizeDialog mCustomizeDialog;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tvTitleToolbar)
    TextView tvTitleToolbar;
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.tvEventName)
    TextView tvEventName;
    @Bind(R.id.tvDate)
    TextView tvDate;
    @Bind(R.id.tvTime)
    TextView tvTime;
    @Bind(R.id.tvLabelHaveJoined)
    TextView tvLabelHaveJoined;
    @Bind(R.id.tvNumberOfRiders)
    TextView tvNumberOfRiders;
    @Bind(R.id.sdvEventImage)
    SimpleDraweeView sdvEventImage;
    @Bind(R.id.tvEatables)
    TextView tvEatables;
    @Bind(R.id.tvPetrolPump)
    TextView tvPetrolPump;
    @Bind(R.id.tvFirstAid)
    TextView tvFirstAid;
    @Bind(R.id.tvServiceCenter)
    TextView tvServiceCenter;
    @Bind(R.id.lvSlidingMenu)
    LinearLayout lvSlidingMenu;
    @Bind(R.id.tvHostedBy)
    TextView tvHostedBy;
    @Bind(R.id.ivMap)
    ImageView ivMap;
    // public static String [] prgmNameList={"Riding Destinations","Meet 'N' Plan A Ride","Riding Events \n    ","Modifly Your Bikes","Healthy Riding","Get Directions","Notifications","Settings"};
    // public static int [] prgmImages={R.drawable.icon_menu_destination,R.drawable.icon_menu_meetplan,R.drawable.icon_menu_events,R.drawable.icon_modifybike,R.drawable.icon_menu_healthy_riding,R.drawable.icon_menu_get_direction,R.drawable.icon_menu_notification,R.drawable.icon_menu_settings};
    // public static Class [] classList={DestinationsListActivity.class,PlanRideActivity.class,EventsListActivity.class,ModifyBikeActivity.class,HealthyRidingActivity.class,GetDirections.class,NotificationScreen.class,SettingsActivity.class};
    @Bind(R.id.tvName)
    TextView tvName;
    @Bind(R.id.gridView1)
    GridView gridView1;
    @Bind(R.id.fmtrackrider)
    FrameLayout fmtrackrider;
    @Bind(R.id.tvJoin)
    TextView tvJoin;
    private Activity activity;
    @Bind(R.id.sdvDp)
    SimpleDraweeView sdvDp;
    @Bind(R.id.btUpdateProfile)
    Button btUpdateProfile;
    private ActionBarDrawerToggle mDrawerToggle;
    @Bind(R.id.tvDesc)
    TextView tvDesc;
    @Bind(R.id.ivImage)
    ImageView ivImage;
    @Bind(R.id.tvShare)
    TextView tvShare;
    @Bind(R.id.rvRiders)
    RelativeLayout rvRiders;
    private CometChatroom cometChatroom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        try {
            activity = this;
            ButterKnife.bind(this);
            setupActionBar();
            setFonts();
            cometChatroom = CometChatroom.getInstance(activity);
            subscribChatRoom();

            mDrawerLayout.closeDrawer(lvSlidingMenu);
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
//                    }  else {
                    Log.e("positiuon:", "" + classList[position]);
                    intentCalling(classList[position]);
                    // }

                    // }
                }
            });
            eventId = getIntent().getStringExtra("eventId");

            if (isNetworkConnected()) {
                RidingDetails();
            } else {
                showToast("Internet Not Connected");
            }
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
        } catch (Exception e) {
            Rollbar.reportException(e, "minor", "EventDetailActivity OnCreate()");

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
            // Toast.makeText(EventDetailActivity.this, "Image Address is Null", Toast.LENGTH_SHORT).show();
        } else {
            String imageUrl = prefsManager.getImageUrl();
            sdvDp.setImageURI(Uri.parse(imageUrl));
//            File file = new File(imageUrl);
//            sdvDp.setImageURI(Uri.fromFile(file));
        }

    }

    public void intentCalling(Class name) {
        Intent mIntent = new Intent(EventDetailActivity.this, name);
        startActivity(mIntent);

    }

    public void intent_Calling(Class name, String na) {
        Intent mIntent = new Intent(EventDetailActivity.this, name);
        mIntent.putExtra(SCREEN_OPEN, na);
        startActivity(mIntent);

    }

    private void setFonts() {
        tvTitleToolbar.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/MACHINEN.TTF"));
        tvName.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/MACHINEN.TTF"));
        tvEventName.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/Lato-Heavy.ttf"));
        tvNumberOfRiders.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/Lato-Heavy.ttf"));
        tvLabelHaveJoined.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/Lato-Bold.ttf"));
        tvHostedBy.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/Lato-Bold.ttf"));
        tvTime.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/Lato-Bold.ttf"));
        tvDate.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/Lato-Bold.ttf"));

    }

    private void setupActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @OnClick({R.id.ivBack, R.id.ivMap, R.id.ivMenu, R.id.fmtrackrider, R.id.tvJoin, R.id.btFullProfile, R.id.btUpdateProfile, R.id.rvRiders, R.id.tvShare})
    void click(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                onBackPressed();
                break;
            case R.id.ivMap:
                String lat = planRideDetailsData.getBaseLat();
                String lon = planRideDetailsData.getBaseLong();
                String deslon = planRideDetailsData.getDestLong();
                String deslat = planRideDetailsData.getDestLat();
                Intent mIntent = new Intent(EventDetailActivity.this, MapActivity.class);
                mIntent.putExtra("endLat", lat);
                mIntent.putExtra("endLon", lon);
                mIntent.putExtra("deslon", deslon);
                mIntent.putExtra("deslat", deslat);
                mIntent.putExtra("destName", planRideDetailsData.getDestLocation());
                startActivity(mIntent);
                break;


            case R.id.ivMenu:
                if (mDrawerLayout.isDrawerOpen(lvSlidingMenu))
                    mDrawerLayout.closeDrawer(lvSlidingMenu);
                else
                    mDrawerLayout.openDrawer(lvSlidingMenu);
                break;
            case R.id.rvRiders:
                String latstatus = planRideDetailsData.getBaseLat();
                String lonstatus = planRideDetailsData.getBaseLong();
                String deslonstatus = planRideDetailsData.getDestLong();
                String deslatstatus = planRideDetailsData.getDestLat();
                Intent statusintent = new Intent(activity, StatusRiderOnMapActivity.class);
                statusintent.putExtra("event_Id", eventId);
                statusintent.putExtra("latstatus", latstatus);
                statusintent.putExtra("lonstatus", lonstatus);
                statusintent.putExtra("deslonstatus", deslonstatus);
                statusintent.putExtra("deslatstatus", deslatstatus);
                startActivity(statusintent);
                break;
            case R.id.fmtrackrider:

                if (planRideDetailsData.getCanTrack() == 1 && planRideDetailsData.getIsJoin() == 1) {
                    ivImage.setImageResource(R.drawable.ic_trackrider);
                    Intent intent = new Intent(activity, TrackingScreen.class);
                    intent.putExtra("eventId", eventId);
                    prefsManager = new PrefsManager(EventDetailActivity.this);
                    prefsManager.setEventId(eventId);
                    startActivity(intent);
                } else {

                }
                break;
            case R.id.tvJoin:
                try {
                    if (planRideDetailsData.getIsVehId() == 1) {
                        if (isNetworkConnected()) {
                            RidingJOIN();
                        } else {
                            showToast("Internet Not Connected");
                        }
                    } else {
                        startActivity(new Intent(EventDetailActivity.this, AfterRegisterScreen.class));
                    }
                } catch (Exception e) {

                }

                break;
            case R.id.btUpdateProfile:
                startActivity(new Intent(activity, ProfileActivity.class));
                break;
            case R.id.btFullProfile:
                startActivity(new Intent(activity, PublicProfileScreen.class));
                break;
            case R.id.tvShare:
                Log.e("id", eventId);
                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(android.content.Intent.EXTRA_TEXT, EVENt_SHARE_URL + eventId);
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Event Detail");
                startActivity(android.content.Intent.createChooser(intent, "Share"));
                break;
        }
    }

    /**
     * Match Event Details info .
     */
    public void RidingDetails() {
        showProgressDialog();
        Log.e("RidingDetails", "RidingDetails");
        try {
            prefsManager = new PrefsManager(EventDetailActivity.this);
            AccessToken = prefsManager.getToken();
            UserId = prefsManager.getCaseId();
            String radius = prefsManager.getRadius();
            Log.e("UserId:", "" + UserId);
            Log.e("AccessToken:", "" + AccessToken);
            Log.e("eventId:", "" + eventId);
            FileUploadService service = ServiceGenerator.createService(FileUploadService.BASE_URL);
            service.eventDetail(UserId, AccessToken, eventId, new Callback<JsonObject>() {
                @Override
                public void success(JsonObject jsonObject, Response response) {
                    Log.e("Upload", "success");
                    Log.e("jsonObject:", "" + jsonObject.toString());
                    Type type = new TypeToken<PlanRideDetails>() {
                    }.getType();
                    PlanRideDetails planARide = new Gson().fromJson(jsonObject.toString(), type);
                    if (planARide.getSuccess() == 1) {
                        dismissProgressDialog();

                        planRideDetailsData = planARide.getData();
                        Log.e("IsJoinId==", "========" + planRideDetailsData.getIsJoin());
                        if (planRideDetailsData.getIsJoin() == 1) {
                            // tvJoin.setText("JOIN");
                            tvJoin.setVisibility(View.GONE);
                            //  tvJoin.setBackgroundColor(Color.parseColor("#66000000"));
                        } else {
                            tvJoin.setText("JOIN");
                            tvJoin.setVisibility(View.VISIBLE);
                        }
                        try {
                            //tvEventName.setText(planRideDetailsData.getBaseLocation() + " to " + planRideDetailsData.getDestLocation());
                            tvEventName.setText(planRideDetailsData.getDaysCount() + "-Day Ride From " + planRideDetailsData.getBaseLocation() + " to " + planRideDetailsData.getDestLocation() + " (Stopover at " + planRideDetailsData.getHalts().get(0).getHaltLocation() + ")");

                        } catch (Exception e) {

                        }
                        String timein12Format = "";
                        try {
                            final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
                            final Date dateObj = sdf.parse(planRideDetailsData.getStartTime());
                            timein12Format = new SimpleDateFormat("K:mm a").format(dateObj);
                        } catch (final ParseException e) {
                            e.printStackTrace();
                        }
                        tvTime.setText(timein12Format);
                        if (planRideDetailsData.getHostedBy() != null) {
                            tvHostedBy.setText(planRideDetailsData.getHostedBy());
                        } else {
                            tvHostedBy.setText("N/A");
                        }

                        tvDate.setText(planRideDetailsData.getStartDate());
                        tvNumberOfRiders.setText(planRideDetailsData.getRiders() + " Riders");
                        tvLabelHaveJoined.setText(planRideDetailsData.getMutual() + " Mutual");
                        tvServiceCenter.setText(String.valueOf(planRideDetailsData.getServiceStation()));
                        tvFirstAid.setText(String.valueOf(planRideDetailsData.getHospitals()));
                        tvPetrolPump.setText(String.valueOf(planRideDetailsData.getPetrolpumps()));
                        tvEatables.setText(String.valueOf(planRideDetailsData.getRestaurant()));
                        if (planRideDetailsData.getImage() != null) {
                            String milestonesJsonInString = planRideDetailsData.getImage().toString();
                            milestonesJsonInString = milestonesJsonInString.replace("\\\"", "\"");
                            milestonesJsonInString = milestonesJsonInString.replace("\"{", "{");
                            milestonesJsonInString = milestonesJsonInString.replace("}\"", "}");
                            sdvEventImage.setImageURI(Uri.parse(milestonesJsonInString));
                        }
                        tvDesc.setText(planRideDetailsData.getDescription());
                        if (planRideDetailsData.getCanTrack() == 1 && planRideDetailsData.getIsJoin() == 1) {
                            ivImage.setImageResource(R.drawable.ic_trackrider);
                        } else {

                        }
                    } else if (planARide.getMessage().equals("Data Not Found.")) {
                        dismissProgressDialog();
                        showToast("Data Not Found.");
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e("Upload", "error");
                    showToast("Server error.");
                    Log.e("error:", ""+error);
                    dismissProgressDialog();
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
            Rollbar.reportException(e, "minor", "EventDetailActivity RidingDetails method API");

        }
    }

    /**
     * Match Event Details info .
     */
    public void RidingJOIN() {
        showProgressDialog();

        try {
            prefsManager = new PrefsManager(EventDetailActivity.this);
            AccessToken = prefsManager.getToken();
            UserId = prefsManager.getCaseId();
            Log.e("UserId:", "" + UserId);
            Log.e("AccessToken:", "" + AccessToken);
            Log.e("eventId:", "" + eventId);
            FileUploadService service = ServiceGenerator.createService(FileUploadService.BASE_URL);
            service.joinEvent(UserId, AccessToken, eventId, new Callback<JsonObject>() {
                @Override
                public void success(JsonObject jsonObject,Response response) {
                    Log.e("jsonObject:", "" + jsonObject.toString());
                    Type type = new TypeToken<RiderJoined>() {
                    }.getType();
                    RiderJoined riderJoined = new Gson().fromJson(jsonObject.toString(), type);
                    Log.e("riderJoined", riderJoined.toString());
                    if (riderJoined.getSuccess() == 1) {
                        tvJoin.setText("JOIN");
                        tvJoin.setBackgroundColor(Color.parseColor("#66000000"));
                        joinChatGroup(planRideDetailsData.getRoomId(), planRideDetailsData.getRoomName(), planRideDetailsData.getRoomPassword());
                        RidingDetails();
                        CustomDialog.JoinedshowProgressDialog(EventDetailActivity.this, riderJoined.getMessage().toString());
                    } else {
                        CustomDialog.showProgressDialog(EventDetailActivity.this, riderJoined.getMessage().toString());
                    }

                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e("Upload", "error");
                    showToast("Server error.");
                    Log.e("error:", ""+error);
                    dismissProgressDialog();
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
            Rollbar.reportException(e, "minor", "EventDetailActivity RidingDetails method API");

        }
    }


   /* *//**
     * Match Event Details info .
     *//*
    public void JOINEVENT() {
        showProgressDialog();
        Log.e("JOIN EVENT", "JOIN EVENT");
        try {
            prefsManager = new PrefsManager(EventDetailActivity.this);
            AccessToken = prefsManager.getToken();
            UserId = prefsManager.getCaseId();
            String radius = prefsManager.getRadius();
            //http://ridersopininon.herokuapp.com/index.php/ridingDestination?userId=75&longitude=0.000000&latitude=0.000000&accessToken=eddfbf2bf4046e90fc768d8e319a4355
            Log.e("URL: ", "" + ApplicationGlobal.ROOT + ApplicationGlobal.baseurl_joinEvent + "userId=" + UserId + "&accessToken=" + AccessToken + "&eventId=" + eventId);
            RequestQueue requestQueue = Volley.newRequestQueue(EventDetailActivity.this);
            RequestJsonObject loginTaskRequest = new RequestJsonObject(Request.Method.GET,
                    ApplicationGlobal.ROOT + ApplicationGlobal.baseurl_joinEvent + "userId=" + UserId + "&accessToken=" + AccessToken + "&eventId=" + eventId, null,
                    volleyJoinErrorListener(), volleyJoinSuccessListener()
            );

            requestQueue.add(loginTaskRequest);

        } catch (Exception e) {
            e.printStackTrace();
            Rollbar.reportException(e, "minor", "EventDetailActivity JOINEVENT API");

        }
    }

    *//**
     * Implement success listener on execute api url.
     *//*
    public Response.Listener<JSONObject> volleyJoinSuccessListener() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("Model response:", "" + response);
                Type type = new TypeToken<RiderJoined>() {
                }.getType();
                RiderJoined riderJoined = new Gson().fromJson(response.toString(), type);
                Log.e("riderJoined", riderJoined.toString());
                if (riderJoined.getSuccess() == 1) {
                    tvJoin.setText("JOIN");
                    tvJoin.setBackgroundColor(Color.parseColor("#66000000"));
                    RidingDetails();
                    CustomDialog.JoinedshowProgressDialog(EventDetailActivity.this, riderJoined.getMessage().toString());
                } else {
                    CustomDialog.showProgressDialog(EventDetailActivity.this, riderJoined.getMessage().toString());
                }

            }
        };
    }

    *//**
     * Implement Volley error listener here.
     *//*
    public Response.ErrorListener volleyJoinErrorListener() {
        dismissProgressDialog();
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error: ", "" + error);
            }
        };
    }
*/
    @Override
    protected void onResume() {
        super.onResume();
        if (mDrawerLayout.isDrawerOpen(lvSlidingMenu)) {
            showProfileImage();
            mDrawerLayout.closeDrawer(lvSlidingMenu);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // CustomDialog.dismisDialog();
    }



//=========== join Chat group==============================//

    private void joinChatGroup(String roomId, String roomName, String roomPassword) {
        Log.e("roomId", roomId);
        Log.e("roomName", roomName);
        Log.e("roomPassword", roomPassword);

        cometChatroom.joinChatroom(roomId, roomName.replace(" ","%20"), roomPassword, new Callbacks() {
            @Override
            public void successCallback(JSONObject jsonObject) {
                Log.e("join response", "" + jsonObject);
            }

            @Override
            public void failCallback(JSONObject jsonObject) {
                Log.e("join failCallback", "" + jsonObject);
            }
        });
        Log.e("isSubscribedToChatroom",""+ cometChatroom.isSubscribedToChatroom(roomName));
    }

    //================== chat room subscribe==================//

    private void subscribChatRoom() {
        cometChatroom.subscribe(true, new SubscribeChatroomCallbacks() {

            @Override
            public void onMessageReceived(JSONObject receivedMessage) {

                //  LogsActivity.addToLog("Chatrooms onMessageReceived");
                Log.d("abc", "On charoom message received = " + receivedMessage);
                try {
                    if (receivedMessage.has("message")) {
                        String mess = receivedMessage.getString("message");
                        String name = receivedMessage.getString("from");
                        String fromId = receivedMessage.getString("fromid");
                        String time = receivedMessage.getString("sent");
                        String messagetype = receivedMessage.getString("message_type");
                        boolean isMymessage = false, videoMessage = false, imageMessage = false;
                        Intent intent = new Intent("Chatroom_message");
                        intent.putExtra("fromid", fromId);
                        intent.putExtra("message_type", messagetype);
                        intent.putExtra("message_id", receivedMessage.getString("id"));
                        intent.putExtra("Message", mess);
                        intent.putExtra("from", name);
                        intent.putExtra("time", time);
                        if (!fromId.equals(prefsManager.getCaseId())) {
                            intent.putExtra("Newmessage", 1);
                            if (messagetype.equals("12")) {
                                intent.putExtra("imageMessage", "1");
                                imageMessage = true;
                            } else if (messagetype.equals("14")) {
                                intent.putExtra("videoMessage", "1");
                                videoMessage = true;
                            }
                        } else if (fromId.equals(prefsManager.getCaseId())) {
                            isMymessage = true;
                            intent.putExtra("Newmessage", 1);

                            if (messagetype.equals("12")) {
                                intent.putExtra("imageMessage", "1");
                                imageMessage = true;
                                intent.putExtra("myphoto", "1");
                            } else if (messagetype.equals("14")) {
                                intent.putExtra("videoMessage", "1");
                                videoMessage = true;
                                intent.putExtra("myvideo", "1");

                            } else if (messagetype.equals("10")
                                    && fromId.equals(SharedPreferenceHelper.get(Keys.SharedPreferenceKeys.myId))) {
        /*
         * This else if condition added to avoid self duplicate message to be appended in list,
         *  and it will also show when self message is obtained from last 10 message.
           Please change "Me" to other thing as per the language you are using for CometChat */
                                intent.putExtra("Newmessage", 1);
                                intent.putExtra("selfmessage", true);
                            }
                        }
                        ChatroomChatMessage newmessage = new ChatroomChatMessage(receivedMessage.getString("id"), mess,
                                Utils.convertTimestampToDate(Long.parseLong(time)), name + " :", isMymessage,
                                messagetype, fromId, cometChatroom.getCurrentChatroom());

                        DatabaseHandler helper = new DatabaseHandler (getApplicationContext());
                        helper.insertChatroomMessage(newmessage);
                        getApplicationContext().sendBroadcast(intent);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onLeaveChatroom(JSONObject leaveResponse) {
                // LogsActivity.addToLog("Chatrooms onLeaveChatroom");
            }

            @Override
            public void onError(JSONObject errorResponse) {
                // LogsActivity.addToLog("Chatrooms onError");
            }

            @Override
            public void gotChatroomMembers(JSONObject chatroomMembers) {
                Log.e("chatroomMembers",""+chatroomMembers);
                //   LogsActivity.addToLog("Chatrooms gotChatroomMembers");
            }

            @Override
            public void gotChatroomList(JSONObject chatroomList) {
               Log.e("gotChatroomList",""+chatroomList);
                try {
                    SharedPreferenceHelper.save(Keys.SharedPreferenceKeys.CHATROOMS_LIST, chatroomList.toString());
//                    populateChatroomList();
//                    adapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onActionMessageReceived(JSONObject response) {
                Log.e("chatroom actions =",""+ response);

            }
        });
    }

}
