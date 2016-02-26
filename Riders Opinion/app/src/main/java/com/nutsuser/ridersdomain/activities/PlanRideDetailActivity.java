package com.nutsuser.ridersdomain.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.widget.DrawerLayout;
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
import com.nutsuser.ridersdomain.adapter.AdapterRide;
import com.nutsuser.ridersdomain.adapter.CustomGridAdapter;
import com.nutsuser.ridersdomain.utils.ApplicationGlobal;
import com.nutsuser.ridersdomain.utils.CustomizeDialog;
import com.nutsuser.ridersdomain.utils.PrefsManager;
import com.nutsuser.ridersdomain.web.api.FileUploadService;
import com.nutsuser.ridersdomain.web.api.ServiceGenerator;
import com.nutsuser.ridersdomain.web.api.volley.RequestJsonObject;
import com.nutsuser.ridersdomain.web.pojos.PlanARide;
import com.nutsuser.ridersdomain.web.pojos.PlanRideDetails;
import com.nutsuser.ridersdomain.web.pojos.PlanRideDetailsData;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.mime.TypedFile;

/**
 * Created by user on 10/1/2015.
 */
public class PlanRideDetailActivity extends BaseActivity {
    PlanRideDetailsData planRideDetailsData;
    String AccessToken, UserId,eventId,LOCATION;
    CustomizeDialog mCustomizeDialog;
    public static String[] prgmNameList = {"My Rides", "My Messages", "My Friends", "Chats", "Favourite Destination", "Notifications", "Settings", "    \n"};
    public static int[] prgmImages = {R.drawable.ic_menu_fav_destinations, R.drawable.ic_menu_my_messages, R.drawable.ic_menu_my_friends, R.drawable.ic_menu_menu_chats, R.drawable.ic_menu_fav_destinations, R.drawable.ic_menu_menu_notifications, R.drawable.ic_menu_menu_settings, R.drawable.ic_menu_menu_blank_icon};
    public static Class[] classList = {MyRidesRecyclerView.class, ChatListScreen.class, MyFriends.class, ChatListScreen.class, FavouriteDesination.class, Notification.class, SettingsActivity.class, SettingsActivity.class};
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tvPlaces)
    TextView tvPlace;
    @Bind(R.id.tvDateAndTime)
    TextView tvDateAndTime;
    @Bind(R.id.tvTitle)
    TextView tvTitle;
    @Bind(R.id.tvLabelHostedBy)
    TextView tvLabelHostedBy;
    @Bind(R.id.tvHostedBy)
    TextView tvHostedBy;
    @Bind(R.id.tvNumberOfRiders)
    TextView tvNumberOfRiders;
    @Bind(R.id.tvLabelHaveJoined)
    TextView tvLabelHaveJoined;
    @Bind(R.id.tvDate)
    TextView tvDate;

    @Bind(R.id.tvTime)
    TextView tvTime;
    @Bind(R.id.tvEatables)
    TextView tvEatables;
    @Bind(R.id.tvPetrolPump)
    TextView tvPetrolPump;
    @Bind(R.id.tvServiceCenter)
    TextView tvServiceCenter;
    @Bind(R.id.tvFirstAid)
    TextView tvFirstAid;
    @Bind(R.id.tvJoin)
    TextView tvJoin;
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
    @Bind(R.id.tvDesc)
    TextView tvDesc;
    @Bind(R.id.tvSubmit)
    TextView tvSubmit;
    @Bind(R.id.tvName)
    TextView tvName;
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
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    // public static String [] prgmNameList={"Riding Destinations","Meet 'N' Plan A Ride","Riding Events \n    ","Modifly Your Bikes","Healthy Riding","Get Directions","Notifications","Settings"};
    // public static int [] prgmImages={R.drawable.icon_menu_destination,R.drawable.icon_menu_meetplan,R.drawable.icon_menu_events,R.drawable.icon_modifybike,R.drawable.icon_menu_healthy_riding,R.drawable.icon_menu_get_direction,R.drawable.icon_menu_notification,R.drawable.icon_menu_settings};
    // public static Class [] classList={DestinationsListActivity.class,PlanRideActivity.class,EventsListActivity.class,ModifyBikeActivity.class,HealthyRidingActivity.class,GetDirections.class,NotificationScreen.class,SettingsActivity.class};
    @Bind(R.id.lvSlidingMenu)
    LinearLayout lvSlidingMenu;
    @Bind(R.id.gridView1)
    GridView gridView1;
    @Bind(R.id.btEdit)
    Button btEdit;
    @Bind(R.id.sdvEventImage)
    SimpleDraweeView sdvEventImage;
    private Activity activity;

    int REQUEST_CAMERA = 0, SELECT_FILE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_ride_detail);
        activity = this;
        ButterKnife.bind(this);
        setupActionBar();
        setFonts();
        gridView1.setAdapter(new CustomGridAdapter(this, prgmNameList, prgmImages));
        gridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 7) {

                } else {
                    Log.e("positiuon:", "" + classList[position]);
                    intentCalling(classList[position]);
                }
            }
        });

        eventId=getIntent().getStringExtra("eventId");
        LOCATION=getIntent().getStringExtra("LOCATION");
        if(LOCATION.matches("details")){
            btEdit.setVisibility(View.GONE);

        }
        else{
            btEdit.setVisibility(View.VISIBLE);
            tvJoin.setText("JOINED");
        }
        RidingDetails();
    }

    public void intentCalling(Class name) {
        Intent mIntent = new Intent(PlanRideDetailActivity.this, name);
        startActivity(mIntent);

    }

    private void setFonts() {
        Typeface typefaceNormal = Typeface.createFromAsset(getResources().getAssets(), "fonts/ITC AVANT GARDE GOTHIC LT CONDENSED BOOK.TTF");
        tvTitle.setTypeface(typefaceNormal);
        tvPlace.setTypeface(typefaceNormal);
        tvDateAndTime.setTypeface(typefaceNormal);
        tvHostedBy.setTypeface(typefaceNormal);
        tvLabelHostedBy.setTypeface(typefaceNormal);
        tvNumberOfRiders.setTypeface(typefaceNormal);
        tvSubmit.setTypeface(typefaceNormal);
        tvLabelHaveJoined.setTypeface(typefaceNormal);
        tvDate.setTypeface(typefaceNormal);
        ;

        tvTime.setTypeface(typefaceNormal);
        ;
        tvEatables.setTypeface(typefaceNormal);
        ;
        tvPetrolPump.setTypeface(typefaceNormal);
        ;
        tvServiceCenter.setTypeface(typefaceNormal);
        ;
        tvFirstAid.setTypeface(typefaceNormal);
        ;
        tvJoin.setTypeface(typefaceNormal);
        ;
        tvSponsoredAdTitle1.setTypeface(typefaceNormal);
        ;
        tvSponsoredAdReviews1.setTypeface(typefaceNormal);
        ;
        tvSponsoredAdLocation1.setTypeface(typefaceNormal);
        ;
        tvSponsoredAdTitle2.setTypeface(typefaceNormal);
        ;
        tvSponsoredAdReviews2.setTypeface(typefaceNormal);
        ;
        tvSponsoredAdLocation2.setTypeface(typefaceNormal);
        ;
        tvDesc.setTypeface(typefaceNormal);
        ;
        tvName.setTypeface(typefaceNormal);
        //tvAddress.setTypeface(typefaceNormal);
        // tvDestinations.setTypeface(typefaceNormal);
        // tvEvents.setTypeface(typefaceNormal);
        // tvModifyBike.setTypeface(typefaceNormal);
        //  tvMeetAndPlanRide.setTypeface(typefaceNormal);
        // tvHealthyRiding.setTypeface(typefaceNormal);
        // tvGetDirections.setTypeface(typefaceNormal);
        //tvNotifications.setTypeface(typefaceNormal);
        //tvSettings.setTypeface(typefaceNormal);
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
                if(LOCATION.matches("details")){
                    finish();
                }
                else{
                    Intent intent = new Intent(PlanRideDetailActivity.this, MainScreenActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.ivMenu, R.id.rlProfile,R.id.btEdit,R.id.tvJoin})
    void onclick(View view) {
        switch (view.getId()) {
            case R.id.ivMenu:
                if (mDrawerLayout.isDrawerOpen(lvSlidingMenu))
                    mDrawerLayout.closeDrawer(lvSlidingMenu);
                else
                    mDrawerLayout.openDrawer(lvSlidingMenu);
                break;
            case R.id.rlProfile:
                startActivity(new Intent(activity, ProfileActivity.class));
                break;
            case R.id.btEdit:
              selectImage();
                break;
            case R.id.tvJoin:
                if(tvJoin.getText().toString().matches("JOIN")){

                }

                break;
           /* case R.id.tvEvents:
                startActivity(new Intent(activity, EventsListActivity.class));
                break;
            case R.id.tvModifyBike:
                startActivity(new Intent(activity, ModifyBikeActivity.class));
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
     * Match Riding List info .
     */
    public void RidingDetails() {
        showProgressDialog();
        Log.e("riding destination", "riding destination");
        try {
            prefsManager = new PrefsManager(PlanRideDetailActivity.this);
            AccessToken = prefsManager.getToken();
            UserId = prefsManager.getCaseId();
            String radius=prefsManager.getRadius();



            //http://ridersopininon.herokuapp.com/index.php/ridingDestination?userId=75&longitude=0.000000&latitude=0.000000&accessToken=eddfbf2bf4046e90fc768d8e319a4355
            Log.e("URL: ", "" + ApplicationGlobal.ROOT + ApplicationGlobal.baseurl_eventdetails + "userId=" + UserId +"&accessToken="+AccessToken+"&eventId="+eventId);
            RequestQueue requestQueue = Volley.newRequestQueue(PlanRideDetailActivity.this);
            RequestJsonObject loginTaskRequest = new RequestJsonObject(Request.Method.GET,
                    ApplicationGlobal.ROOT + ApplicationGlobal.baseurl_eventdetails + "userId=" + UserId +"&accessToken="+AccessToken+"&eventId="+eventId, null,
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

                Type type = new TypeToken<PlanRideDetails>() {
                }.getType();
                PlanRideDetails planARide = new Gson().fromJson(response.toString(), type);


                if (planARide.getSuccess().equals("1")) {
                    dismissProgressDialog();
                    planRideDetailsData=planARide.getData();

                    tvTitle.setText(planRideDetailsData.getBaseLocation()+" to "+planRideDetailsData.getDestLocation());
                    tvTime.setText(planRideDetailsData.getStartTime());

                    tvDate.setText(planRideDetailsData.getStartDate());
                    tvEatables.setText(planRideDetailsData.getRestaurant());
                    tvPetrolPump.setText(planRideDetailsData.getPetrolpumps());
                    tvServiceCenter.setText(planRideDetailsData.getServiceStation());
                    tvFirstAid.setText(planRideDetailsData.getHospitals());
                    if(planRideDetailsData.getImage()!=null){
                        String milestonesJsonInString = planRideDetailsData.getImage().toString();
                        milestonesJsonInString = milestonesJsonInString.replace("\\\"", "\"");
                        milestonesJsonInString = milestonesJsonInString.replace("\"{", "{");
                        milestonesJsonInString = milestonesJsonInString.replace("}\"", "}");
                        sdvEventImage.setImageURI(Uri.parse(milestonesJsonInString));
                    }
                }
                else if(planARide.getMessage().equals("Data Not Found.")){
                    dismissProgressDialog();
                    showToast("Data Not Found.");
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

        mCustomizeDialog = new CustomizeDialog(PlanRideDetailActivity.this);
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

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(PlanRideDetailActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);
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
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        showProgressDialog();
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        final File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        prefsManager = new PrefsManager(PlanRideDetailActivity.this);
        AccessToken = prefsManager.getToken();
        FileUploadService service = ServiceGenerator.createService(PlanRideDetailActivity.this, FileUploadService.BASE_URL);
        TypedFile typedFile = new TypedFile("multipart/form-data", destination);

        service.upload_(typedFile, AccessToken, eventId, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, retrofit.client.Response response) {
                Log.e("Upload", "success");
                Log.e("jsonObject:", "" + jsonObject.toString());
                Log.e("response:", "" + response.toString());
                Log.e("Status:", "" + response.getStatus());
                Log.e("Body:", "" + response.getBody());
                Log.e("Reason:", "" + response.getReason());
                dismissProgressDialog();
                sdvEventImage.setImageURI(Uri.fromFile(destination));

            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("Upload", "error");
            }
        });

        //ivImage.setImageBitmap(thumbnail);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        showProgressDialog();
        Uri selectedImageUri = data.getData();
        String[] projection = { MediaStore.MediaColumns.DATA };
        Cursor cursor = managedQuery(selectedImageUri, projection, null, null,
                null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();

        String selectedImagePath = cursor.getString(column_index);

        Bitmap bm;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(selectedImagePath, options);
        final int REQUIRED_SIZE = 200;
        int scale = 1;
        while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                && options.outHeight / scale / 2 >= REQUIRED_SIZE)
            scale *= 2;
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(selectedImagePath, options);
        final File file=savebitmap(bm);
        prefsManager = new PrefsManager(PlanRideDetailActivity.this);
        AccessToken = prefsManager.getToken();
        FileUploadService service = ServiceGenerator.createService(PlanRideDetailActivity.this, FileUploadService.BASE_URL);
        TypedFile typedFile = new TypedFile("multipart/form-data", file);
        service.upload_(typedFile, AccessToken, eventId, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, retrofit.client.Response response) {
                Log.e("Upload", "success");
                Log.e("jsonObject:", "" + jsonObject.toString());
                Log.e("response:", "" + response.toString());
                Log.e("Status:", "" + response.getStatus());
                Log.e("Body:", "" + response.getBody());
                Log.e("Reason:", "" + response.getReason());
                dismissProgressDialog();
                sdvEventImage.setImageURI(Uri.fromFile(file));

            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("Upload", "error");
            }
        });
        // ivImage.setImageBitmap(bm);
    }

    private File savebitmap(Bitmap filename) {
        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        OutputStream outStream = null;
        File file = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        // File file = new File(filename + ".png");
        if (file.exists()) {
            file.delete();
            file = new File(Environment.getExternalStorageDirectory(),
                    System.currentTimeMillis() + ".jpg");
            Log.e("file exist", "" + file + ",Bitmap= " + filename);
        }
        try {
            // make a new bitmap from your file
            Bitmap bitmap = filename;

            outStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            outStream.flush();
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("file", "" + file);
        return file;

    }

}
