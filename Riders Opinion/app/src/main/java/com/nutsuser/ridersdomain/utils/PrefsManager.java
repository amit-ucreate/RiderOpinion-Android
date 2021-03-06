package com.nutsuser.ridersdomain.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by jass on 6/12/2015.
 */
public class PrefsManager {
    public static final String DEVICE_TOKEN = "devicetoken";
    public static final String SERVICE_RUNNING = "service_running";
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private Context mContext;
    private String mPrefsName = "riders_opinion_prefs";
    private String mKeyToken = "token";
    private String mKeyName = "myName";
    private String mKeyPassword = "password";
    private String mKeyEmail = "email";
    private String mKeyAddress = "addmKeyFollowingListingress";
    private String mKeyCity = "city";
    private String mKeyImageUrl = "image_url";
    private String mKeyUserId = "my_user_id";
    private String mKeyUserName = "user_name";
    private String mKeyPasswordCometChat = "password_comet_chat";

    private String mKeyBannerImageUrl = "banner_image_url";
    private String mDistanceParameter= "M";
    private String mVehicleId="vehicleId";

    public String getDistanceParameter() {
        return mSharedPreferences.getString(mDistanceParameter, "M");

    }

    public void setDistanceParameter(String distanceParameter) {
        mEditor.putString(this.mDistanceParameter, distanceParameter);
        mEditor.apply();

    }

    public String getUserName() {
        return mSharedPreferences.getString(mKeyUserName, null);
    }
    public void setUserName(String token) {
        mEditor.putString(mKeyUserName, token);
        mEditor.apply();

    }
    public String getPasswordCometChat() {
        return mSharedPreferences.getString(mKeyPasswordCometChat, null);
    }
    public void setPasswordCometChat(String pass) {
        mEditor.putString(mKeyPasswordCometChat, pass);
        mEditor.apply();

    }


    public PrefsManager(Context context) {

        this.mContext = context;
        mSharedPreferences = context.getSharedPreferences(mPrefsName, context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();

    }
    public String getEventId() {
        return mSharedPreferences.getString("eventId", null);

    }

    public void setEventId(String eventId) {
        mEditor.putString("eventId", eventId);
        mEditor.apply();

    }

    public String getHistoryCount() {
        return mSharedPreferences.getString("historyCount", "");

    }

    public void setHistoryCount(String historyCount) {
        mEditor.putString("historyCount", historyCount);
        mEditor.apply();

    }

    public String getDreamId() {
        return mSharedPreferences.getString("dreamId", null);

    }

    public void setDreamId(String dreamId) {
        mEditor.putString("dreamId", dreamId);
        mEditor.apply();

    }
    public String getDevicetoken() {
        return mSharedPreferences.getString(PrefsManager.DEVICE_TOKEN, null);

    }

    public void setDevicetoken(String token) {
        mEditor.putString(PrefsManager.DEVICE_TOKEN, token);
        mEditor.apply();

    }
    public void setmKeyBannerImageUrl(String mKeyBannerImage) {

        mEditor.putString(mKeyBannerImageUrl, mKeyBannerImage);
        mEditor.apply();
    }
    public String getmKeyBannerImageUrl() {

        return mSharedPreferences.getString(mKeyBannerImageUrl, "");
    }
    public boolean isLoginDone() {
        return mSharedPreferences.getBoolean("loginDone", false);

    }


    public void setLoginDone(boolean loginDone) {

        mEditor.putBoolean("loginDone", loginDone);
        mEditor.apply();

    }


    // services
    public boolean isServicesRunning() {
        return mSharedPreferences.getBoolean(SERVICE_RUNNING, false);

    }


    public void setServicesRunning(boolean ServicesRunning) {
        mEditor.putBoolean(SERVICE_RUNNING, ServicesRunning);
        mEditor.apply();

    }


    public String getMyId() {
        return mSharedPreferences.getString(mKeyUserId, "");

    }

    public void setMyId(String id) {
        mEditor.putString(mKeyUserId, id);
        mEditor.apply();

    }

    public String getCaseId() {
        return mSharedPreferences.getString("caseId", "");

    }

    public void setCaseId(String id) {
        mEditor.putString("caseId", id);
        mEditor.apply();
    }

    public String getVehicleId() {
        return mSharedPreferences.getString("mVehicleId", "");

    }

    public void setVehicleId(String id) {
        mEditor.putString("mVehicleId", id);
        mEditor.apply();

    }
    public String getEventType() {
        return mSharedPreferences.getString("eventType", "");

    }

    public void setEventType(String id) {
        mEditor.putString("eventType", id);
        mEditor.apply();
    }

    public String getRadius() {
        return mSharedPreferences.getString("radius", "");

    }

    public void setRadius(String id) {
        mEditor.putString("radius", id);
        mEditor.apply();

    }

    public String getToken() {
        return mSharedPreferences.getString(mKeyToken, "");

    }

    public void setToken(String token) {
        mEditor.putString(mKeyToken, token);
        mEditor.apply();

    }
    public String getDistanceSettings() {
        return mSharedPreferences.getString("mDistance", "");

    }

    public void setDistanceSettings(String token) {
        mEditor.putString("mDistance", token);
        mEditor.apply();

    }


    public String getName() {
        return mSharedPreferences.getString(mKeyName, "");

    }

    public void setName(String name) {
        mEditor.putString(mKeyName, name);
        mEditor.apply();
    }

    public String getPassword() {
        return mSharedPreferences.getString(mKeyPassword, "");

    }

    public void setPassword(String password) {
        mEditor.putString(mKeyPassword, password);
        mEditor.apply();
    }

    public String getEmail() {
        return mSharedPreferences.getString(mKeyEmail, "");

    }

    public void setEmail(String email) {
        mEditor.putString(mKeyEmail, email);
        mEditor.apply();
    }

    public String getAddress() {
        return mSharedPreferences.getString(mKeyAddress, "");

    }

    public void setAddress(String address) {
        mEditor.putString(mKeyAddress, address);
        mEditor.apply();
    }

    public String getCity() {
        return mSharedPreferences.getString(mKeyCity, "");

    }

    public void setCity(String city) {
        mEditor.putString(mKeyCity, city);
        mEditor.apply();
    }

    public String getImageUrl() {
        return mSharedPreferences.getString(mKeyImageUrl, "");

    }

    public void setImageUrl(String imageUrl) {
        mEditor.putString(mKeyImageUrl, imageUrl);
        mEditor.apply();
    }

    public void onLogout() {
        try {
            setLoginDone(false);
            setToken("");
            setCaseId("");
            setRadius("");
            setName("");
            setPassword("");
            setEmail("");
            setAddress("");
            setCity("");
            setImageUrl("");
            setMyId("");

        } catch (Exception e) {
            Log.i("exception on logout", e.toString());
        }
    }


    /*public UserDetails getUserDetails() {

        if (mSharedPreferences.contains(mKeyUserDetails)) {
            String UserListInJson = mSharedPreferences.getString(mKeyUserDetails, "");

            Type type = new TypeToken<UserDetails>() {
            }.getType();
            UserDetails userDetails = new Gson().fromJson(UserListInJson, type);
            return userDetails;
        }
        return new UserDetails();


    }

    public void setUserDetails(UserDetails userDetails) {
        String userDetailJSONString = new Gson().toJson(userDetails);
        mEditor.putString(mKeyUserDetails, userDetailJSONString);
        mEditor.apply();

    }

    public ArrayList<String> getTasks() {

            if (mSharedPreferences.contains(mKeyTask)) {
                String taskListInJson = mSharedPreferences.getString(mKeyTask, "");

                Type type = new TypeToken<ArrayList<String>>() {
                }.getType();
                ArrayList<String> taskList = new Gson().fromJson(taskListInJson, type);
                return taskList;
            } else {
                return new ArrayList<String>();
            }

    }

    public void setTasks(ArrayList<String> taskList) {

            String alertJSONString = new Gson().toJson(taskList);
            mEditor.putString(mKeyTask, alertJSONString);
            mEditor.apply();

    }*/


}
