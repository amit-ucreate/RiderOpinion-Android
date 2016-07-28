package com.nutsuser.ridersdomain.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
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
import android.widget.FrameLayout;
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
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.adapter.AdapterEvent;
import com.nutsuser.ridersdomain.adapter.CustomGridAdapter;
import com.nutsuser.ridersdomain.fragments.CaldroidCustomFragment;
import com.nutsuser.ridersdomain.services.GPSService;
import com.nutsuser.ridersdomain.utils.ApplicationGlobal;
import com.nutsuser.ridersdomain.utils.CustomizeDialog;
import com.nutsuser.ridersdomain.utils.PrefsManager;
import com.nutsuser.ridersdomain.view.BetterPopupWindow;
import com.nutsuser.ridersdomain.view.CustomDialog;
import com.nutsuser.ridersdomain.web.api.FileUploadService;
import com.nutsuser.ridersdomain.web.api.ServiceGenerator;
import com.nutsuser.ridersdomain.web.api.volley.RequestJsonObject;
import com.nutsuser.ridersdomain.web.pojos.RidingEvent;
import com.nutsuser.ridersdomain.web.pojos.RidingEventData;
import com.rollbar.android.Rollbar;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;

/**
 * Created by user on 9/2/2015.
 */
public class EventsListActivity extends BaseActivity {
    public static String[] prgmNameList = {"My Rides", "My Messages", "My Friends", "Chats", "Favorites", "Notifications", "Settings", "Riders Near By"};
    public static int[] prgmImages = {R.drawable.ic_menu_my_rides, R.drawable.ic_menu_my_messages, R.drawable.menu_my_friends, R.drawable.ic_menu_menu_chats, R.drawable.menu_fav_destinations, R.drawable.ic_menu_menu_notifications, R.drawable.menu_settings, R.drawable.icon_nearby};
    public static Class[] classList = {MyRidesRecyclerView.class, RecentChatListActivity.class, MyFriendsActivity.class, ComingSoon.class, FavouriteDesination.class, NotificationListActivity.class, SettingsActivity.class, NearByFriendsAcitivity.class};
    double start1, end1;
    String star_lat, star_long;
    PrefsManager prefsManager;
    CustomizeDialog mCustomizeDialog;
    String AccessToken, UserId;
    String dat;
    public static ArrayList<RidingEventData> data = new ArrayList<>();
    // public static  ArrayList<CalendarEventData> calendarResponseData = new ArrayList<>();
    public static ArrayList<String> dateGroup = new ArrayList<String>();
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tvTitleToolbar)
    TextView tvTitleToolbar;
    @Bind(R.id.tvListView)
    TextView tvListView;
    @Bind(R.id.tvCalendarView)
    TextView tvCalendarView;
    @Bind(R.id.tvDestName)
    TextView tvDestName;
    @Bind(R.id.rvEvents)
    RecyclerView rvEvents;
    @Bind(R.id.llCalender)
    LinearLayout llCalender;
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.lvSlidingMenu)
    LinearLayout lvSlidingMenu;
    @Bind(R.id.tvName)
    TextView tvName;
    @Bind(R.id.edsearch)
    EditText edsearch;
    int i = 0, j = 0;

    boolean calenderlist = false;

    String ridetype, baselat, baselong;
    /*  @Bind(R.id.tvAddress)
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
      */
    @Bind(R.id.fmdateTag)
    FrameLayout fmdateTag;
    @Bind(R.id.tvdateTag)
    TextView tvdateTag;
    @Bind(R.id.gridView1)
    GridView gridView1;
    @Bind(R.id.tvGetDirection)
    TextView tvGetDirection;
    //public static String [] prgmNameList={"Riding Destinations","Meet 'N' Plan A Ride","Riding Events \n    ","Modifly Your Bikes","Healthy Riding","Get Directions","Notifications","Settings"};
    // public static int [] prgmImages={R.drawable.icon_menu_destination,R.drawable.icon_menu_meetplan,R.drawable.icon_menu_events,R.drawable.icon_modifybike,R.drawable.icon_menu_healthy_riding,R.drawable.icon_menu_get_direction,R.drawable.icon_menu_notification,R.drawable.icon_menu_settings};
    //public static Class [] classList={DestinationsListActivity.class,PlanRideActivity.class,EventsListActivity.class,ModifyBikeActivity.class,HealthyRidingActivity.class,GetDirections.class,NotificationScreen.class,SettingsActivity.class};
    private AdapterEvent adapterEvent;
    private CaldroidCustomFragment caldroidFragment;

    ArrayList<RidingEventData> calendarResponseData = new ArrayList<>();

    private Activity activity;
    @Bind(R.id.sdvDp)
    SimpleDraweeView sdvDp;
    @Bind(R.id.ivSearch)
    ImageView ivSearch;
    @Bind(R.id.btUpdateProfile)
    Button btUpdateProfile;
    private ActionBarDrawerToggle mDrawerToggle;
    @Bind(R.id.fab_evnt)
    FloatingActionButton fab_evnt;
    DemoPopupWindow dw;
    CustomBaseAdapter adapter;
    private ArrayList<String> ridetypeArray = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_events);
        try {
            activity = this;
            prefsManager = new PrefsManager(EventsListActivity.this);
            ButterKnife.bind(this);
            setupActionBar();
            mDrawerLayout.closeDrawer(lvSlidingMenu);
            rvEvents.setLayoutManager(new LinearLayoutManager(activity));
            setFonts();
            setDestNameTitle();
            ridetypeArray.clear();
            ridetypeArray.add("Breakfast Ride");
            ridetypeArray.add("Overnight Ride");

           /* rvEvents.addOnItemTouchListener(new RecyclerItemClickListener(activity, new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {

                    Intent mIntent = new Intent(activity, EventDetailActivity.class);
                    mIntent.putExtra("eventId", data.get(position).getEventId());
                   // mIntent.putExtra("typeEmailPhone", getIntent().getStringExtra("typeEmailPhone"));
                    startActivity(mIntent);
                }
            }));*/
            tvTitleToolbar.setText("RIDING EVENTS");

            gridView1.setAdapter(new CustomGridAdapter(this, prgmNameList, prgmImages));
            gridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                if (position == 1) {
//                    Log.e("positiuon:", "" + classList[position]);
//                    intent_Calling(classList[position], "My Messages");
//                } else {
                    Log.e("positiuon:", "" + classList[position]);
                    intentCalling(classList[position]);
                    // }

                }
            });
            ridetype = getIntent().getStringExtra("rides");
            if (ridetype.equals("yes")) {
                star_lat = getIntent().getStringExtra("baslat");
                star_long = getIntent().getStringExtra("baslon");
                RidingEventinfo(ridetype);
            } else {
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
                        RidingEventinfo(ridetype);
                    } else {
                        showToast("Internet Not Connected");
                    }
                }
                // make sure you close the gps after using it. Save user's battery power
                mGPSService.closeGPS();
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
            edsearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                        //do something
                        edsearch.clearFocus();

                        if (edsearch.getText().toString().length() > 1) {

                            hideKeyboard();
                        }
                        serachevent(edsearch.getText().toString());
                    }

                    return false;
                }
            });
            edsearch.addTextChangedListener(new TextWatcher() {

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
                    if (!edsearch.getText().toString().isEmpty()) {
                        edsearch.setText("");
                        hideKeyboard();
                    }
                }
            });

            if (new PrefsManager(this).isServicesRunning()) {
                fab_evnt.setVisibility(View.VISIBLE);
            } else {
                fab_evnt.setVisibility(View.GONE);
            }
            fab_evnt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String eventid = new PrefsManager(EventsListActivity.this).getEventId();
                    Intent intent = new Intent(activity, TrackingScreen.class);
                    intent.putExtra("eventId", eventid);
                    startActivity(intent);
                }
            });
        } catch (Exception e) {
            Rollbar.reportException(e, "minor", "EventListActivity on create");
        }

    }

    private void hideKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
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
            //Toast.makeText(EventsListActivity.this, "Image Address is Null", Toast.LENGTH_SHORT).show();
        } else {
            String imageUrl = prefsManager.getImageUrl();
            sdvDp.setImageURI(Uri.parse(imageUrl));
//            File file = new File(imageUrl);
//            sdvDp.setImageURI(Uri.fromFile(file));
        }

    }

    public void intentCalling(Class name) {
        Intent mIntent = new Intent(EventsListActivity.this, name);
        startActivity(mIntent);

    }

    private void setUpCalendar() {
        caldroidFragment = new CaldroidCustomFragment();
        final SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");

        Bundle args = new Bundle();
        final Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));


        caldroidFragment.setArguments(args);
        caldroidFragment.setBackgroundResourceForDate(R.color.light_brown, cal.getTime());
        caldroidFragment.setTextColorForDate(R.color.white, cal.getTime());
        Log.e("", "dateGroup" + dateGroup.size());

        dateGroup.clear();
        for (int i = 0; i < data.size(); i++) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            dateGroup.add(data.get(i).getStartDate());

            Date dates = new Date(dateGroup.get(i));
            String date_format = sdf.format(dates);
            Date datess = null;
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            try {
                datess = format.parse(date_format);
                Log.e("dates", "" + datess);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                Log.e("ParseException", "" + e);
                Rollbar.reportException(e, "minor", "EventListActivity on setUpCalendar method");
            }
            caldroidFragment.setSelectedDate(datess);

        }


        // caldroidFragment.setDisableDatesFromString(date);


        caldroidFragment.setThemeResource(com.caldroid.R.style.CaldroidDefaultDarkCalendarViewLayout);
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.llCalender, caldroidFragment);
        t.commit();

        caldroidFragment.setCaldroidListener(mListener);

    }

    CaldroidListener mListener = new CaldroidListener() {
        @Override
        public void onSelectDate(Date date, View view) {
            final SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
            Log.e("formatter.format(date)", "" + formatter.format(date));
            Log.e("dateGroup", "" + dateGroup);
            if (dateGroup.contains(formatter.format(date))) {
                Log.e("date", formatter.format(date));
                calenderlist = true;
                if (data.size() == 0) {
                    dismissProgressDialog();
                    rvEvents.setVisibility(View.GONE);
                    fmdateTag.setVisibility(View.GONE);
                } else {
                    fmdateTag.setVisibility(View.VISIBLE);
                    dat = formatter.format(date);
                    tvdateTag.setText(dat);
                    calendarResponseData.clear();
                    for (int i = 0; i < data.size(); i++) {
                        if (dat.matches(data.get(i).getStartDate()) || dat.equalsIgnoreCase(EventsListActivity.data.get(i).getStartDate())) {
                            calendarResponseData.add(EventsListActivity.data.get(i));
                            adapterEvent = new AdapterEvent(activity, calendarResponseData);
                            rvEvents.setAdapter(adapterEvent);
                            Log.e("yes", "match");
                        } else {
                            Log.e("no", "match");
                        }
                    }
                }
                tvListView.setBackgroundColor(ContextCompat.getColor(activity, R.color.light_brown));
                tvCalendarView.setBackgroundColor(ContextCompat.getColor(activity, R.color.fourty_of_black));
                llCalender.setVisibility(View.GONE);
                rvEvents.setVisibility(View.VISIBLE);
                // startActivity(new Intent(EventsListActivity.this, CalendarEventListActivity.class).putExtra("date", formatter.format(date)));

            }
        }

        @Override
        public void onChangeMonth(int month, int year) {
            String text = "month: " + month + " year: " + year;
            Log.e("text", text);
//            if(i==1){
//            Log.e("text",""+1);
////        Calendar c = Calendar.getInstance();
////        int year2 = c.get(Calendar.YEAR);
////        int month2 = c.get(Calendar.MONTH);
////        String dat= month2+"/"+year2;
//             if(j!=0){
//                 getCalendarEvents(month + "/" + year);
//                 j=0;
//                }
//             i++;
//            }else{
//                Log.e("text",""+2);
            //getCalendarEvents(month + "/" + year);
//             j++;
//        }
            //  if(!(getCurrentMoth().equalsIgnoreCase(month + "/" + year))) {


            //  }
        }
    };

    public void intent_Calling(Class name, String na) {
        Intent mIntent = new Intent(EventsListActivity.this, name);
        mIntent.putExtra(SCREEN_OPEN, na);
        startActivity(mIntent);

    }

    private void setupActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_home);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void setFonts() {
        tvListView.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/MACHINEN.TTF"));
        tvCalendarView.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/MACHINEN.TTF"));
        tvTitleToolbar.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/MACHINEN.TTF"));
        tvName.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/MACHINEN.TTF"));
        tvGetDirection.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/Lato-Heavy.ttf"));
        //  tvAddress.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
        //  tvDestinations.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
        // tvEvents.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
        // tvModifyBike.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
        // tvMeetAndPlanRide.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
        ///tvHealthyRiding.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
        // tvGetDirections.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
        // tvNotifications.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));
        //tvSettings.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF"));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(EventsListActivity.this, MainScreenActivity.class);
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
        Intent intent = new Intent(EventsListActivity.this, MainScreenActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("MainScreenResponse", "OPEN");
        startActivity(intent);
        finish();
    }


    @OnClick({R.id.tvCalendarView, R.id.tvListView, R.id.ivMenu, R.id.btFullProfile, R.id.btUpdateProfile, R.id.tvGetDirection, R.id.ivFilter, R.id.ivClose})
    void click(View view) {
        switch (view.getId()) {
            case R.id.tvGetDirection:
                startActivity(new Intent(activity, GetDirections.class));
                break;
            case R.id.tvCalendarView:
                //  getCalendarEvents(getCurrentMoth());

                //caldroidFragment.setCaldroidListener(mListener);

                tvCalendarView.setBackgroundColor(ContextCompat.getColor(activity, R.color.light_brown));
                tvListView.setBackgroundColor(ContextCompat.getColor(activity, R.color.fourty_of_black));
                llCalender.setVisibility(View.VISIBLE);
                rvEvents.setVisibility(View.GONE);
                setUpCalendar();
                break;

            case R.id.ivClose:
                fmdateTag.setVisibility(View.GONE);
                calenderlist = false;
                adapterEvent = new AdapterEvent(activity, data);
                rvEvents.setAdapter(adapterEvent);
                break;
            case R.id.tvListView:
                tvListView.setBackgroundColor(ContextCompat.getColor(activity, R.color.light_brown));
                tvCalendarView.setBackgroundColor(ContextCompat.getColor(activity, R.color.fourty_of_black));
                llCalender.setVisibility(View.GONE);
                rvEvents.setVisibility(View.VISIBLE);
                break;

            case R.id.ivMenu:
                if (mDrawerLayout.isDrawerOpen(lvSlidingMenu))
                    mDrawerLayout.closeDrawer(lvSlidingMenu);
                else
                    mDrawerLayout.openDrawer(lvSlidingMenu);
                break;
            case R.id.ivFilter:
                dw = new DemoPopupWindow(view, EventsListActivity.this);
                dw.showLikeQuickAction(0, 0);
                break;
           /* case R.id.tvNotifications:
            startActivity(new Intent(activity, ChatListScreen.class));
            break;
            case R.id.tvDestinations:
                startActivity(new Intent(activity, DestinationsListActivity.class));
                break;
            case R.id.tvModifyBike:
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
            case R.id.btUpdateProfile:
                startActivity(new Intent(activity, ProfileActivity.class));
                break;
            case R.id.btFullProfile:
                startActivity(new Intent(activity, PublicProfileScreen.class));
                break;
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

                Type type = new TypeToken<RidingEvent>() {
                }.getType();
                RidingEvent ridingEvent = new Gson().fromJson(response.toString(), type);
                data.clear();

                if (ridingEvent.getSuccess() == 1) {
                    dismissProgressDialog();
                    data.addAll(ridingEvent.getData());
                    adapterEvent = new AdapterEvent(activity, data);
                    rvEvents.setAdapter(adapterEvent);
                } else {
                    dismissProgressDialog();
                    adapterEvent = new AdapterEvent(activity, data);
                    rvEvents.setAdapter(adapterEvent);
                    CustomDialog.showProgressDialog(EventsListActivity.this, ridingEvent.getMessage().toString());
                }

                //  getCalendarEvents(getCurrentMoth());

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
     * Riding Event info .
     */
    public void RidingEventinfo(String ridestype) {
        showProgressDialog();
        try {

            AccessToken = prefsManager.getToken();
            UserId = prefsManager.getCaseId();
            String radius = prefsManager.getRadius();
            if (star_long == null) {
                star_long = null;
            }
            if (star_lat == null) {
                star_lat = null;
            }

            Log.e("EVEnt LIST URL: ", "" + ApplicationGlobal.ROOT + ApplicationGlobal.baseurl_ridingevent + "userId=" + UserId + "&baseLon=" + star_long + "&baseLat=" + star_lat + "&accessToken=" + AccessToken + "&rides=" + ridestype);
            RequestQueue requestQueue = Volley.newRequestQueue(EventsListActivity.this);
            RequestJsonObject loginTaskRequest = new RequestJsonObject(Request.Method.POST,
                    ApplicationGlobal.ROOT + ApplicationGlobal.baseurl_ridingevent + "userId=" + UserId + "&baseLon=" + star_long + "&baseLat=" + star_lat + "&accessToken=" + AccessToken + "&rides=" + ridestype, null,
                    volleyModelErrorListener(), volleyModelSuccessListener()
            );

            requestQueue.add(loginTaskRequest);

        } catch (Exception e) {
            e.printStackTrace();
            Rollbar.reportException(e, "minor", "EventListActivity on RidingEventinfo API");

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mDrawerLayout.isDrawerOpen(lvSlidingMenu)) {
            showProfileImage();
            mDrawerLayout.closeDrawer(lvSlidingMenu);
        }
        ridetype = getIntent().getStringExtra("rides");
        if (ridetype.equals("yes")) {
            star_lat = getIntent().getStringExtra("baslat");
            star_long = getIntent().getStringExtra("baslon");
            RidingEventinfo(ridetype);
        } else {
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
                    RidingEventinfo(ridetype);
                } else {
                    showToast("Internet Not Connected");
                }
            }
            // make sure you close the gps after using it. Save user's battery power
            mGPSService.closeGPS();
        }
    }

    /**
     * modifyVender Data (Only Strings) to Server
     **/
    private void serachevent(String search) {

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
                showProgressDialog();

                String userId = prefsManager.getCaseId();

                String accessToken = prefsManager.getToken();
                // star_long + "&latitude=" + star_lat + "&accessToken=" + AccessToken
                FileUploadService service = ServiceGenerator.createService(FileUploadService.BASE_URL);

                service.ridingeventsearch(userId, star_lat, star_long, search, accessToken, new Callback<JsonObject>() {
                    @Override
                    public void success(JsonObject jsonObject, retrofit.client.Response response) {

                        Type type = new TypeToken<RidingEvent>() {
                        }.getType();
                        RidingEvent ridingEvent = new Gson().fromJson(jsonObject.toString(), type);
                        data.clear();
                        Log.e("jsonObject:", "" + jsonObject);
                        if (ridingEvent.getSuccess() == 1) {
                            dismissProgressDialog();
                            data.addAll(ridingEvent.getData());
                            if (calenderlist) {
                                calenderlist = true;
                                if (data.size() == 0) {
                                    dismissProgressDialog();
                                    rvEvents.setVisibility(View.GONE);
                                    tvdateTag.setVisibility(View.GONE);
                                } else {
                                    tvdateTag.setVisibility(View.VISIBLE);
                                    tvdateTag.setText("RESET DATE");
                                    calendarResponseData.clear();
                                    for (int i = 0; i < data.size(); i++) {
                                        if (dat.matches(data.get(i).getStartDate()) || dat.equalsIgnoreCase(EventsListActivity.data.get(i).getStartDate())) {
                                            calendarResponseData.add(data.get(i));
                                            adapterEvent = new AdapterEvent(activity, calendarResponseData);
                                            rvEvents.setAdapter(adapterEvent);
                                            Log.e("yes", "match");
                                        } else {
                                            Log.e("no", "match");
                                        }
                                    }
                                }
                                tvListView.setBackgroundColor(ContextCompat.getColor(activity, R.color.light_brown));
                                tvCalendarView.setBackgroundColor(ContextCompat.getColor(activity, R.color.fourty_of_black));
                                llCalender.setVisibility(View.GONE);
                                rvEvents.setVisibility(View.VISIBLE);
                            } else {
                                adapterEvent = new AdapterEvent(activity, data);
                                rvEvents.setAdapter(adapterEvent);
                            }

                            //edsearch.setText("");
                        } else {
                            dismissProgressDialog();
                            if (calenderlist) {
                                calendarResponseData.clear();
                                adapterEvent = new AdapterEvent(activity, calendarResponseData);
                                rvEvents.setAdapter(adapterEvent);
                            } else {
                                adapterEvent = new AdapterEvent(activity, data);
                                rvEvents.setAdapter(adapterEvent);
                            }
                            CustomDialog.showProgressDialog(EventsListActivity.this, ridingEvent.getMessage().toString());
                            //edsearch.setText("");
                        }
                        tvListView.setBackgroundColor(ContextCompat.getColor(activity, R.color.light_brown));
                        tvCalendarView.setBackgroundColor(ContextCompat.getColor(activity, R.color.fourty_of_black));
                        llCalender.setVisibility(View.GONE);
                        rvEvents.setVisibility(View.VISIBLE);

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        dismissProgressDialog();
                        edsearch.setText("");
                        Log.d("DataUploading------", "Data Uploading Failure......" + error);
                        CustomDialog.showProgressDialog(EventsListActivity.this, error.toString());
                    }
                });
            } else {
                showToast("Internet Not Connected");
            }


        }


        // make sure you close the gps after using it. Save user's battery power
        mGPSService.closeGPS();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // CustomDialog.dismisDialog();
    }


//    private void getCalendarEvents(String date) {
//
//
//        showProgressDialog();
//
//        Log.e("AccessToken", "" + prefsManager.getToken());
//        Log.e("UserId", "" + prefsManager.getCaseId());
//
//        FileUploadService service = ServiceGenerator.createService(FileUploadService.BASE_URL_NEARBY_FRIENDS);
//
//        service.calendarEventAPI(prefsManager.getCaseId(), date, prefsManager.getToken(), new Callback<JsonObject>() {
//            @Override
//            public void success(JsonObject jsonObject, retrofit.client.Response response) {
//                Log.e("type", "==");
//                Type type = new TypeToken<CalendarEventsResponse>() {
//                }.getType();
//                Log.e("jsonObject", "==" + jsonObject);
//                CalendarEventsResponse calendarListResponse = new Gson().fromJson(jsonObject.toString(), type);
//                calendarResponseData.clear();
//
//                dismissProgressDialog();
//                if (calendarListResponse.getSuccess() == 1) {
//                    i=1;
//                    calendarResponseData.addAll(calendarListResponse.getData());
//                 //   setUpCalendar();
////                    dateGroup.clear();
////               //     final SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
//////                    caldroidFragment = new CaldroidCustomFragment();
//////
//////                    Bundle args = new Bundle();
//////                    final Calendar cal = Calendar.getInstance();
//////                    args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
//////                    args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
//////
//////                    caldroidFragment.setArguments(args);
//////                    caldroidFragment.setBackgroundResourceForDate(R.color.light_brown, cal.getTime());
//////                    caldroidFragment.setTextColorForDate(R.color.white, cal.getTime());
////                    Log.e("calendarResponseData",""+calendarResponseData+" dateGroup "+dateGroup);
////                    for (int i = 0; i < calendarResponseData.size(); i++) {
////                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
////                        dateGroup.add(calendarResponseData.get(i).getStartDate());
////
//////                        Date dates = new Date(calendarResponseData.get(i).getStartDate());
//////                        String date_format = sdf.format(dates);
//////                        Date datess = null;
//////                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//////                        try {
//////                            datess = format.parse(date_format);
//////                            Log.e("dates", "" + datess);
//////                        } catch (ParseException e) {
//////                            // TODO Auto-generated catch block
//////                            Log.e("ParseException", "" + e);
//////                        }
//////                        caldroidFragment.setSelectedDate(datess);
////
////                    }
//
//                  //  Log.e("calendarResponseData", "" + calendarResponseData.size());
//                    //setUpCalendar();
//
//
//                } else {
//                    CustomDialog.showProgressDialog(EventsListActivity.this, calendarListResponse.getMessage().toString());
//                }
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//                Log.e("error", "" + error.getMessage());
//                dismissProgressDialog();
//            }
//        });
//    }


//    private String getCurrentMoth(){
//        Calendar c = Calendar.getInstance();
//        int year = c.get(Calendar.YEAR);
//        int month = c.get(Calendar.MONTH)+1;
//        String date= month+"/"+year;
//
//        Log.e("date",date);
//
//        return date;
//    }

    private void setDestNameTitle() {
        try {
            String destName = getIntent().getExtras().getString("destName");
            if (TextUtils.isEmpty(destName)) {
                tvDestName.setVisibility(View.GONE);
            } else {
                tvDestName.setText("Places Near By " + destName);
            }
        } catch (NullPointerException e) {
            tvDestName.setVisibility(View.GONE);
        }
    }


    //=============== popup window=================//

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
            adapter = new CustomBaseAdapter(EventsListActivity.this, ridetypeArray);
            listview.setAdapter(adapter);
            Button mButton = (Button) root.findViewById(R.id.cancelBtn);


            mButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub

                    dismiss();

                }
            });

            this.setContentView(root);
        }

    }

    public class CustomBaseAdapter extends BaseAdapter {
        Context context;
        private ArrayList<String> ridetype;

        public CustomBaseAdapter(Context context, ArrayList<String> ridetype) {
            this.ridetype = ridetype;
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


            holder.txtTitle.setText(ridetype.get(position));

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ridetype.get(position).matches("Breakfast Ride")) {
//                        tv_rideTag.setText(ridetype.get(position));
//                        fmEndDate.setVisibility(View.GONE);
//                        edPlace4.setVisibility(View.GONE);
//                        rideTypeChoose = true;
                        dw.dismiss();
                    } else if (ridetype.get(position).matches("Overnight Ride")) {
//                        tv_rideTag.setText(ridetype.get(position));
//                        fmEndDate.setVisibility(View.VISIBLE);
//                        fmStartDate.setVisibility(View.VISIBLE);
//                        edPlace4.setVisibility(View.VISIBLE);
//                        rideTypeChoose = false;
                        dw.dismiss();
                    }

                }
            });
            return convertView;
        }

        @Override
        public int getCount() {
            return ridetype.size();
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
