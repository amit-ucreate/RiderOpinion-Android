package com.nutsuser.ridersdomain.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.gms.maps.model.LatLng;
import com.inscripts.cometchat.sdk.CometChat;
import com.inscripts.interfaces.Callbacks;
import com.inscripts.interfaces.SubscribeCallbacks;
import com.inscripts.utils.Logger;
import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.database.DatabaseHandler;
import com.nutsuser.ridersdomain.database.Keys;
import com.nutsuser.ridersdomain.database.PushNotificationsManager;
import com.nutsuser.ridersdomain.database.SharedPreferenceHelper;
import com.nutsuser.ridersdomain.database.Utils;
import com.nutsuser.ridersdomain.utils.ApplicationGlobal;
import com.nutsuser.ridersdomain.utils.CONSTANTS;
import com.nutsuser.ridersdomain.utils.CustomizeDialog;
import com.nutsuser.ridersdomain.utils.PrefsManager;
import com.nutsuser.ridersdomain.web.pojos.SingleChatMessage;
import com.rollbar.android.Rollbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

/**
 * Created by nutsuser on 8/6/2015.
 */
public class BaseActivity extends AppCompatActivity implements CONSTANTS {

    public static PrefsManager prefsManager;
    public static Activity context;
    public CustomizeDialog mCustomizeDialog;
    public Typeface typeFaceMACHINEN,typeFaceLatoHeavy,typeFaceLatoBold;
    private CometChat cometchat;
    public static String msg_id;
    private DatabaseHandler dbhelper;
    public  static JSONObject jsonObjectUsers = new JSONObject();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Rollbar.init(this, ApplicationGlobal.rollBarId, "production");
        prefsManager = new PrefsManager(this);
        Fresco.initialize(this);
        typeFaceMACHINEN =Typeface.createFromAsset(getResources().getAssets(), "fonts/MACHINEN.TTF");
        typeFaceLatoHeavy= Typeface.createFromAsset(getResources().getAssets(), "fonts/Lato-Heavy.ttf");
        typeFaceLatoBold=Typeface.createFromAsset(getResources().getAssets(), "fonts/Lato-Bold.ttf");
        context=BaseActivity.this;
        cometChatInilizers();
    }

    protected void showToast(String message) {
        Toast.makeText(BaseActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    //============ function to set actionbar===============//
    public void setupActionBar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_home);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
    //============ function to set actionbar===============//
    public void setupActionBarBack(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.icon_back);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
    //======== progress dialog===========//
    public void showProgressDialog() {

        mCustomizeDialog = new CustomizeDialog(this);
        mCustomizeDialog.setCancelable(false);
        mCustomizeDialog.show();
        Log.e("HERE", "HERE");
    }

    public void dismissProgressDialog() {
        if (mCustomizeDialog != null && mCustomizeDialog.isShowing()) {
            mCustomizeDialog.dismiss();
            mCustomizeDialog = null;
        }
    }
    /**
     * @return screen size int[width, height]
     */
    public int[] getScreenSize() {
        Point size = new Point();
        WindowManager w = getWindowManager();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            w.getDefaultDisplay().getSize(size);
            return new int[]{size.x, size.y};
        } else {
            Display d = w.getDefaultDisplay();
            //noinspection deprecation
            return new int[]{d.getWidth(), d.getHeight()};
        }
    }

    //========= function to set theme===============//
    public void settheme() {
        setTheme(R.style.Theme_trans);
        Log.e("BASE ", "THEME CALL");
    }

    //========= function to check internet connectivity======//
    public boolean isNetworkConnected() {
        if (ApplicationGlobal.isNetworkConnected(this))
            return true;
        else
            return false;
    }
    public int CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.e("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        //return Radius * c;
        return kmInDec;
    }


    public double getDistanceKilometer(double miles){
//        miles_to_km = 1 * 1.60934
//        km_to_miles = 1 * 0.621371

                double dist= miles*(1.60934);
                double roundOff = (double) Math.round(dist * 100) / 100;
                return roundOff;
    }



    public double getDistanceMiles(double km){
//        miles_to_km = 1 * 1.60934
//        km_to_miles = 1 * 0.621371
        double dist= km*(0.621371);
        double roundOff = (double) Math.round(dist * 100) / 100;
        return roundOff;
    }

    //================== comet chat==============//

    private void cometChatInilizers(){
        SharedPreferenceHelper.initialize(context);
        cometchat = CometChat.getInstance(context,API_KEY);
        DatabaseHandler handler = new DatabaseHandler(this);
        dbhelper = new DatabaseHandler(this);
        chatSubscribe();
    }

    private void chatSubscribe(){
        final SubscribeCallbacks subCallbacks = new SubscribeCallbacks() {
            @Override
            public void gotOnlineList(JSONObject jsonObject) {

                //Log.e("online users",""+jsonObject);
                jsonObjectUsers=jsonObject;
            }

            @Override
            public void onError(JSONObject jsonObject) {

            }

            @Override
            public void onMessageReceived(JSONObject receivedMessage) {
                try {

                    Log.e("receivedMessage",""+receivedMessage);
                    msg_id = receivedMessage.getString("id");
                    String messagetype = receivedMessage.getString("message_type");
                    Intent intent = new Intent();
                    intent.setAction("NEW_SINGLE_MESSAGE");
                    boolean imageMessage = false, videomessage = false, ismyMessage = false;
                    if (messagetype.equals("12")) {
                        intent.putExtra("imageMessage", "1");
                        imageMessage = true;
                        if (receivedMessage.getString("self").equals("1")) {
                            intent.putExtra("myphoto", "1");
                            ismyMessage = true;
                        }
                    } else if (messagetype.equals("14")) {
                        intent.putExtra("videoMessage", "1");
                        videomessage = true;
                        if (receivedMessage.getString("self").equals("1")) {
                            intent.putExtra("myVideo", "1");
                            ismyMessage = true;
                        }
                    }else if(messagetype.equals("10")){
                        if (receivedMessage.getString("self").equals("1")) {
                            ismyMessage = true;
                        }
                    }
                    intent.putExtra("message_type", messagetype);
                    intent.putExtra("user_id", receivedMessage.getInt("from"));
                    intent.putExtra("message", receivedMessage.getString("message").trim());
                    intent.putExtra("time", receivedMessage.getString("sent"));
                    intent.putExtra("message_id", receivedMessage.getString("id"));
                    intent.putExtra("from", receivedMessage.getString("from"));
                    String to = null;
                    if (receivedMessage.has("to")) {
                        to = receivedMessage.getString("to");
                    } else {
                        to = SharedPreferenceHelper.get(Keys.SharedPreferenceKeys.myId);
                    }
                    intent.putExtra("to", to);
                    String time = Utils.convertTimestampToDate(Utils.correctTimestamp(Long.parseLong(receivedMessage
                            .getString("sent"))));
                    SingleChatMessage newMessage = new SingleChatMessage(receivedMessage.getString("id"),
                            receivedMessage.getString("message").trim(), time, ismyMessage,
                            receivedMessage.getString("from"), to, messagetype, 0);
                    dbhelper.insertOneOnOneMessage(newMessage);
                    sendBroadcast(intent);
                }catch(JSONException e){
                    Rollbar.reportException(e, "minor", "BaseActivity CometChatSubscribe method");
                }
            }

            @Override
            public void gotProfileInfo(JSONObject profileInfo) {
                Logger.error("profile infor " + profileInfo);
                cometchat.getPluginInfo(new Callbacks() {

                    @Override
                    public void successCallback(JSONObject response) {
                        Log.d("abc", "PLugin infor =" + response);
                    }

                    @Override
                    public void failCallback(JSONObject response) {

                    }
                });
                JSONObject j = profileInfo;
                try {
                    msg_id = j.getString("id");
                    SharedPreferenceHelper.save(Keys.SharedPreferenceKeys.myId, msg_id);
                    if (j.has("push_channel")) {
                        PushNotificationsManager.subscribe(j.getString("push_channel"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Rollbar.reportException(e, "minor", "BaseActivity CometChat gotProfileInfo method");
                }

            }

            @Override
            public void gotAnnouncement(JSONObject jsonObject) {

            }

            @Override
            public void onActionMessageReceived(JSONObject response) {

                Log.e("Action Receive Msg",""+response);
                try {
                    String action = response.getString("action");
                    Intent i = new Intent("NEW_SINGLE_MESSAGE");
                    if (action.equals("typing_start")) {
                        i.putExtra("action", "typing_start");
                    } else if (action.equals("typing_stop")) {
                        i.putExtra("action", "typing_stop");
                    } else if (action.equals("message_read")) {
                        i.putExtra("action", "message_read");
                        i.putExtra("from", response.getString("from"));
                        i.putExtra("message_id", response.getString("message_id"));
                        Utils.msgtoTickList.put(response.getString("message_id"), Keys.MessageTicks.read);
                    } else if (action.equals("message_deliverd")) {
                        i.putExtra("action", "message_deliverd");
                        i.putExtra("from", response.getString("from"));
                        i.putExtra("message_id", response.getString("message_id"));
                        Utils.msgtoTickList.put(response.getString("message_id"), Keys.MessageTicks.deliverd);
                    }
                    sendBroadcast(i);
                } catch (Exception e) {
                    e.printStackTrace();
                    Rollbar.reportException(e, "minor", "BaseActivity CometChat onActionMessageReceived method");
                }
            }
        };

        if (CometChat.isLoggedIn()) {
            cometchat.subscribe(true, subCallbacks);
        }
    }

}
