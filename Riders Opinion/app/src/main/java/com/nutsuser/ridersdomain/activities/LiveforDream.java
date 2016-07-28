package com.nutsuser.ridersdomain.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.common.util.UriUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.adapter.CustomGridAdapter;
import com.nutsuser.ridersdomain.utils.ApplicationGlobal;
import com.nutsuser.ridersdomain.utils.CustomizeDialog;
import com.nutsuser.ridersdomain.utils.PrefsManager;
import com.nutsuser.ridersdomain.view.BetterPopupWindow;
import com.nutsuser.ridersdomain.view.CustomDialog;
import com.nutsuser.ridersdomain.web.api.FileUploadService;
import com.nutsuser.ridersdomain.web.api.ServiceGenerator;
import com.nutsuser.ridersdomain.web.pojos.LiveDream;
import com.nutsuser.ridersdomain.web.pojos.VehicleDetails;
import com.nutsuser.ridersdomain.web.pojos.VehicleName;
import com.rollbar.android.Rollbar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

/**
 * Created by admin on 01-04-2016.
 */
public class LiveforDream extends BaseActivity {

    //
    private String mImagePath = "";
    File destination = null;
    boolean imageFlag = false;
    boolean imageAddressAvail = false;
    boolean imageLicenseAvail = false;

    PrefsManager prefsManager;
    CustomizeDialog mCustomizeDialog;
    String AccessToken, UserId;
    private Activity activity;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tvTitleToolbar)
    TextView tvTitleToolbar;
    @Bind(R.id.textView2)
    TextView textView2;
    @Bind(R.id.textView3)
    TextView textView3;
    @Bind(R.id.etProfileName)
    EditText etProfileName;
    @Bind(R.id.etPhone)
    EditText etPhone;
    @Bind(R.id.etName)
    EditText etName;
    String vehicleType;
    @Bind(R.id.etDescription)
    EditText etDescription;
    @Bind(R.id.sdvaddressproof)
    SimpleDraweeView sdvaddressproof;
    @Bind(R.id.sdvlicence)
    SimpleDraweeView sdvlicence;

    View view;
    CustomBaseAdapter adapter;
    DemoPopupWindow dw;
    private ArrayList<VehicleDetails> mVehicleDetailses = new ArrayList<VehicleDetails>();

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
    String dreamid;
    //===========================================//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_livefordream);
        try {
            activity = this;
            view = new View(this);
            ButterKnife.bind(activity);
            prefsManager = new PrefsManager(LiveforDream.this);
            prefsManager.setDreamId("");
            setupActionBar();
            setFontsToTextViews();
            setSliderMenu();
            setDrawerSlider();
        } catch (Exception e) {
            Rollbar.reportException(e, "minor", "LiveforDream  on create");
        }
    }

    private void setFontsToTextViews() {
        tvTitleToolbar.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/MACHINEN.TTF"));
        textView2.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/Lato-Heavy.ttf"));
        textView3.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/Lato-Bold.ttf"));
        tvName.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/MACHINEN.TTF"));

    }

    private void setupActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_home);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }


//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (mDrawerLayout.isDrawerOpen(lvSlidingMenu)) {
//            showProfileImage();
//            mDrawerLayout.closeDrawer(lvSlidingMenu);
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(LiveforDream.this, MainScreenActivity.class);
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
        Intent intent = new Intent(LiveforDream.this, MainScreenActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("MainScreenResponse", "OPEN");
        startActivity(intent);
        finish();
    }

    @OnClick({R.id.sdvaddressproof, R.id.sdvlicence,
            R.id.etProfileName, R.id.tvUpdate, R.id.btFullProfile, R.id.btUpdateProfile, R.id.ivMenu})
    void onclick(View view) {
        switch (view.getId()) {
            case R.id.etProfileName:
                Vehicle();
               /* if (etProfileName.getText().toString().trim().length() > 0) {
                    Vehicle();
                } else {
                    Toast.makeText(LiveforDream.this, "Please Enter Bike Brand First ", Toast.LENGTH_SHORT).show();
                }*/
                break;
            case R.id.sdvaddressproof:
                imageFlag = true;
                selectImage();
                break;

            case R.id.tvUpdate:
                boolean check = submitText(etPhone.getText().toString(), etName.getText().toString());
                if (check) {
                    String about;

                    if (TextUtils.isEmpty(vehicleType)) {
                        vehicleType = "";
                        showToast("Please select vehicle.");
                        return;
                    }
                    if (TextUtils.isEmpty(etDescription.getText().toString())) {
                        about = "";
                    } else {
                        about = etDescription.getText().toString();
                    }
                    if (imageAddressAvail == true) {
                        if (imageLicenseAvail == true) {
                            if (TextUtils.isEmpty(vehicleType)) {
                                showToast("Please select vehicle.");
                            } else {
                                SubmitTextLiveDream(etPhone.getText().toString(), etName.getText().toString(), vehicleType, about);
                            }
                        } else {
                            showToast("Please select License Proof.");
                        }
                    } else {
                        showToast("Please select Address Proof.");
                    }
                }

                break;
            case R.id.sdvlicence:
                imageFlag = false;
                // profileImageFlag = true;
                selectImage();
                break;
            case R.id.btUpdateProfile:
                startActivity(new Intent(LiveforDream.this, ProfileActivity.class));
                finish();
                break;
            case R.id.btFullProfile:
                startActivity(new Intent(LiveforDream.this, PublicProfileScreen.class));
                finish();
                break;
            case R.id.ivMenu:
                if (mDrawerLayout.isDrawerOpen(lvSlidingMenu))
                    mDrawerLayout.closeDrawer(lvSlidingMenu);
                else {
                    mDrawerLayout.openDrawer(lvSlidingMenu);
                    hideKeyboard();
                }
                break;
        }
    }

    public boolean submitText(String phoneno, String email) {
        if (TextUtils.isEmpty(phoneno)) {
            showToast("Please Enter Phone No");
            return false;
        } else if (TextUtils.isEmpty(email)) {
            showToast("Please Enter Email");
            return false;
        } else if (validEmail(email) == false) {
            showToast("Please Enter Valid Email");
            return false;
        }
        return true;
    }

    private boolean validEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();

    }

    /**
     * Image Selection from Gallery or Camera
     **/

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(LiveforDream.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {

                    Intent takePictureIntent = new Intent(
                            MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePictureIntent,
                            ApplicationGlobal.CAMERA_REQUEST);
                } else if (items[item].equals("Choose from Library")) {

                    Intent i = new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i,
                            ApplicationGlobal.RESULT_LOAD_IMAGE);
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

        if (resultCode == RESULT_OK) {
            if (requestCode == ApplicationGlobal.RESULT_LOAD_IMAGE && resultCode == Activity.RESULT_OK) {
                onSelectFromGalleryResult(data);
            } else if (requestCode == ApplicationGlobal.CAMERA_REQUEST
                    && resultCode == Activity.RESULT_OK) {
                // Bitmap bitmap = ApplicationGlobal.getFile(mImagePath, LiveforDream.this);
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                onCapture_ImageResult(bitmap);
            }
        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        //showProgressDialog();
        String picturePath;
        Uri selectedImageUri = data.getData();
        String[] projection = {MediaStore.MediaColumns.DATA};
        try {


            // String selectedImagePath = cursor.getString(column_index);

            try {
                String[] proj = {MediaStore.Images.Media.DATA};
                Cursor cursor = managedQuery(selectedImageUri, proj, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                picturePath = cursor.getString(column_index);
            } catch (Exception e) {
                picturePath = selectedImageUri.getPath();
            }
            final File file = new File(picturePath);

            ///  Bitmap bm = ApplicationGlobal.getFile(selectedImagePath, LiveforDream.this);
            /// final File file = savebitmap(bm);


            FileUploadService service = ServiceGenerator.createService(FileUploadService.BASE_URL);

            Log.e("file", "" + file);
            TypedFile typedFile = new TypedFile("multipart/form-data", file);


            if (imageFlag) {
                sdvaddressproof.setImageURI(Uri.fromFile(file));
                imageAddressAvail = true;
                uploadAddressProof(service, typedFile);
            } else {
                sdvlicence.setImageURI(Uri.fromFile(file));
                imageLicenseAvail = true;
                uploadLicenceProof(service, typedFile);
            }
        } catch (NullPointerException e) {
            Rollbar.reportException(e, "minor", "LiveforDream onSelectFromGalleryResult");

        }
    }

    private File savebitmap(Bitmap filename) {
        File imageF = null;
        try {
            if (Environment.MEDIA_MOUNTED.equals(Environment
                    .getExternalStorageState())) {

                File storageDir = new File(
                        ApplicationGlobal.LOCAL_STORAGE_BASE_PATH_FOR_POSTED_PHOTOS)
                        .getParentFile();

                if (storageDir != null) {
                    if (!storageDir.mkdirs()) {
                        if (!storageDir.exists()) {
                            Log.d("CameraSample", "failed to create directory");
                            return null;
                        }
                    }
                }
                imageF = File.createTempFile(ApplicationGlobal.JPEG_FILE_PREFIX
                                + System.currentTimeMillis() + "_",
                        ApplicationGlobal.JPEG_FILE_SUFFIX, storageDir);
            } else {
                Log.v("image loading status",
                        "External storage is not mounted READ/WRITE.");
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            Rollbar.reportException(e, "minor", "LiveforDream savebitmap");
        } catch (Exception e) {
            e.printStackTrace();
            Rollbar.reportException(e, "minor", "LiveforDream savebitmap");
        }
        OutputStream outStream = null;
        try {
            // make a new bitmap from your file
            Bitmap bitmap = filename;

            outStream = new FileOutputStream(imageF);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            outStream.flush();
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            Rollbar.reportException(e, "minor", "LiveforDream savebitmap");
        }
        Log.e("file", "" + imageF);
        return imageF;

    }

    private void onCapture_ImageResult(Bitmap bitmap) {


        Log.e("bitmap:", "" + bitmap);

        destination = ApplicationGlobal.bitmapToFile(bitmap);
        Log.e("destination:", "" + destination);
        prefsManager = new PrefsManager(LiveforDream.this);

       /* // showProgressDialog();
        Bitmap thumbnail = bitmap;
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        FileOutputStream fo;
        try {

            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            Rollbar.reportException(e, "minor", "LiveforDream onCapture_ImageResult");
        }
        prefsManager = new PrefsManager(LiveforDream.this);*/

        FileUploadService service = ServiceGenerator.createService(FileUploadService.BASE_URL);

        TypedFile typedFile = new TypedFile("multipart/form-data", destination);

        Log.e("CameraImage User Id", "" + prefsManager.getCaseId());
        Log.e("CameraImage Token", "" + prefsManager.getToken());
        Log.e("FileLocation", "" + destination);
        Log.e("CameraImage TypedFile", "" + typedFile);

        if (imageFlag) {
            imageAddressAvail = true;
            sdvaddressproof.setImageURI(Uri.fromFile(destination));
            uploadAddressProof(service, typedFile);
            // uploadBannerImage(service, typedFile, thumbnail, destination);
        } else {
            imageLicenseAvail = true;
            sdvlicence.setImageURI(Uri.fromFile(destination));
            uploadLicenceProof(service, typedFile);
            //uploadProfileImage(service, typedFile, thumbnail, destination);
        }

    }

    /**
     * SubmitTextLiveDream info .
     */
    @TargetApi(16)
    public void SubmitTextLiveDream(String phoneno, String email, String bikeid, String about) {
        showProgressDialog();
        Log.e("SubmitTextLiveDream", "SubmitTextLiveDream");

        try {
            prefsManager = new PrefsManager(LiveforDream.this);
            AccessToken = prefsManager.getToken();
            UserId = prefsManager.getCaseId();
            Log.e("AccessToken:", "" + AccessToken + "----UserId----" + UserId);
            if (TextUtils.isEmpty(prefsManager.getDreamId())) {
                dreamid = "";
            } else {
                dreamid = prefsManager.getDreamId();
            }
            Log.e("dreamid:", "" + prefsManager.getDreamId());
            Log.e("bikeid:", "" + bikeid);
            Log.e("email:", "" + email);
            Log.e("phoneno:", "" + phoneno);
            Log.e("about:", "" + about);

            FileUploadService service = ServiceGenerator.createService(FileUploadService.BASE_URL);
            service.liveTheDreamText(UserId, dreamid, bikeid, email, phoneno, about, AccessToken, new Callback<JsonObject>() {
                @Override
                public void success(JsonObject jsonObject, retrofit.client.Response response) {

                    Log.e("jsonObject", "" + jsonObject);
                    Type type = new TypeToken<LiveDream>() {
                    }.getType();
                    LiveDream mRidingDestination = new Gson().fromJson(jsonObject.toString(), type);

                    dismissProgressDialog();
                    Log.e("live th dream resp", "" + mRidingDestination.getSuccess());
                    if (mRidingDestination.getSuccess() == 1) {
//                        if(dreamid.isEmpty()) {
//                            prefsManager.setDreamId("" + mRidingDestination.getdreamId());
//                        }else{
                        prefsManager.setDreamId("");
                        // }
                        etPhone.setText("");
                        etName.setText("");
                        etDescription.setText("");
                        etProfileName.setText("Bike Intrested");
                        Uri uri = new Uri.Builder()
                                .scheme(UriUtil.LOCAL_RESOURCE_SCHEME) // "res"
                                .path(String.valueOf(R.color.twenty_of_black))
                                .build();

                        sdvaddressproof.setImageURI(uri);
                        sdvlicence.setImageURI(uri);
                        CustomDialog.showProgressDialog(LiveforDream.this, mRidingDestination.getMessage().toString());
                    } else {
                        CustomDialog.showProgressDialog(LiveforDream.this, mRidingDestination.getMessage().toString());
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    dismissProgressDialog();
                    Log.e("DataUploading------", "Data Uploading Failure......" + error);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Exception------", "" + e);
            Rollbar.reportException(e, "minor", "LiveforDream liveTheDreamText API");

        }
    }


    //vehicle

    /**
     * SubmitTextLiveDream info .
     */
    public void Vehicle() {
        showProgressDialog();

        try {
            FileUploadService service = ServiceGenerator.createService(FileUploadService.BASE_URL);

            service.vehicle(new Callback<JsonObject>() {
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

                        dw = new DemoPopupWindow(view, LiveforDream.this);
                        dw.showLikeQuickAction(0, 0);
                    } else {
                        dismissProgressDialog();
                        CustomDialog.showProgressDialog(LiveforDream.this, mVehicleName.getMessage().toString());
                    }

                }

                @Override
                public void failure(RetrofitError error) {
                    dismissProgressDialog();
                    Log.d("DataUploading------", "Data Uploading Failure......" + error);

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            Rollbar.reportException(e, "minor", "LiveforDream vehicle API");

        }
    }

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

            ViewGroup root = (ViewGroup) inflater.inflate(R.layout.share_choose_popup, null);

            ListView listview = (ListView) root.findViewById(R.id.listview);
            adapter = new CustomBaseAdapter(LiveforDream.this, mVehicleDetailses);
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
                    vehicleType = mVehicleDetailses.get(position).getVehicle_id();
                    //Toast.makeText(ProfileActivity.this, "Vehicle Id is... " +mVehicleDetailses.get(position).getVehicle_name(), Toast.LENGTH_SHORT).show();
                    etProfileName.setText(mVehicleDetailses.get(position).getVehicle_name());
                    Log.e("vehicleType:", "" + vehicleType);
                    dw.dismiss();
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
    //////
    // ************** Class for pop-up window **********************


    @Override
    protected void onDestroy() {
        super.onDestroy();


        //CustomDialog.dismisDialog();

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
                tvName.setText(prefsManager.getUserName());
            }
        } catch (NullPointerException e) {
            tvName.setText("No Name");
            Rollbar.reportException(e, "minor", "LiveforDream showProfileImage");
        }
        try {
            if (prefsManager.getImageUrl() == null) {
                // Toast.makeText(MyRidesRecyclerView.this, "Image Address is Null", Toast.LENGTH_SHORT).show();
            } else {
                String imageUrl = prefsManager.getImageUrl();
                sdvDp.setImageURI(Uri.parse(imageUrl));
//            File file = new File(imageUrl);
//            sdvDp.setImageURI(Uri.fromFile(file));
            }
        } catch (NullPointerException e) {
            Rollbar.reportException(e, "minor", "LiveforDream showProfileImage");

        }

    }

    //============= function to set slider menu============//
    private void setSliderMenu() {
        gridView1.setAdapter(new CustomGridAdapter(this, prgmNameList, prgmImages));
        gridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (position == 2) {
//                    mDrawerLayout.closeDrawer(lvSlidingMenu);
//                }else
//                if (position == 1) {
//                    Log.e("positiuon:", "" + classList[position]);
//                    intent_Calling(classList[position], "My Messages");
//                }  else {
                Log.e("positiuon:", "" + classList[position]);
                intentCalling(classList[position]);
                // }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mDrawerLayout.isDrawerOpen(lvSlidingMenu)) {
            showProfileImage();
            mDrawerLayout.closeDrawer(lvSlidingMenu);
        }

    }

    //======== function to move on next activty=========//
    public void intentCalling(Class name) {
        Intent mIntent = new Intent(LiveforDream.this, name);
        startActivity(mIntent);
        //finish();
    }

    public void intent_Calling(Class name, String na) {
        Intent mIntent = new Intent(LiveforDream.this, name);
        mIntent.putExtra(SCREEN_OPEN, na);
        startActivity(mIntent);
        finish();
    }

    private void hideKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    //============= upload address image==============//
    private void uploadAddressProof(FileUploadService service, TypedFile typedFile) {
        Log.e("typedFile", "" + typedFile);
        showProgressDialog();

        if (TextUtils.isEmpty(prefsManager.getDreamId())) {
            Log.e("dreamid", "no");
            dreamid = "";
        } else {
            dreamid = prefsManager.getDreamId();
            Log.e("dreamid", "" + dreamid);
        }
        service.uploadAddressProof(prefsManager.getCaseId(), dreamid, typedFile, prefsManager.getToken(), new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, Response response) {
                Log.e("upload address", "" + jsonObject.toString() + "  :" + response.getBody() + "  " + response.getReason());

                //JsonObject object = jsonObject.getAsJsonObject();
                if (dreamid.isEmpty()) {
                    prefsManager.setDreamId(jsonObject.get("dreamId").toString());
                }

                dismissProgressDialog();
            }

            @Override
            public void failure(RetrofitError error) {
                dismissProgressDialog();
            }
        });

    }


    //============= upload licence image ===========//
    private void uploadLicenceProof(FileUploadService service, TypedFile typedFile) {
        Log.e("typedFile L", "" + typedFile);
        showProgressDialog();

        if (TextUtils.isEmpty(prefsManager.getDreamId())) {
            dreamid = "";

        } else {
            dreamid = prefsManager.getDreamId();
        }
        service.uploadLicence(prefsManager.getCaseId(), dreamid, typedFile, prefsManager.getToken(), new Callback<JsonObject>() {
            @Override
            public void success(JsonObject jsonObject, Response response) {

                Log.e("upload address", "" + jsonObject.toString() + "  :" + response.getBody() + "  " + response.getReason());
                if (dreamid.isEmpty()) {
                    prefsManager.setDreamId(jsonObject.get("dreamId").toString());
                }
                dismissProgressDialog();
            }

            @Override
            public void failure(RetrofitError error) {
                dismissProgressDialog();
            }
        });

    }


}
