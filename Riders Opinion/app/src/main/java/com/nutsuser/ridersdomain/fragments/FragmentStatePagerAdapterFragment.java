package com.nutsuser.ridersdomain.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nutsuser.ridersdomain.R;
import com.nutsuser.ridersdomain.activities.DestinationsDetailActivity;
import com.nutsuser.ridersdomain.adapter.MyFragmentStatePagerAdapter;
import com.nutsuser.ridersdomain.utils.ApplicationGlobal;
import com.nutsuser.ridersdomain.utils.CustomizeDialog;
import com.nutsuser.ridersdomain.utils.PrefsManager;
import com.nutsuser.ridersdomain.utils.UtilsSubcribe;
import com.nutsuser.ridersdomain.web.api.volley.RequestJsonObject;
import com.nutsuser.ridersdomain.web.pojos.RidingDestinationDetailsClick;
import com.nutsuser.ridersdomain.web.pojos.SubcribeData;
import com.nutsuser.ridersdomain.web.pojos.SubcribesImages;
import com.nutsuser.ridersdomain.web.pojos.SucribeOjectdata;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;



/**
 * Created by noor on 10/04/15.
 */
public class FragmentStatePagerAdapterFragment extends Fragment {
    private static final String TAG = "FragmentStatPgrAdapFrag";
    private MyFragmentStatePagerAdapter mPagerAdapter;
    private ArrayList<UtilsSubcribe.DummyItem> mImageItemList;
    private ViewPager mViewPager;
    CustomizeDialog mCustomizeDialog;
    public PrefsManager prefsManager;
    String AccessToken, UserId;
    SucribeOjectdata data;
    ArrayList<SubcribesImages>subcribesImages=new ArrayList<>();
    ArrayList<UtilsSubcribe.DummyItem> fullImageList = new ArrayList<>();
    /* Avoid non-default constructors in fragments: use a default constructor plus Fragment.setArguments(Bundle) instead and use Type value = getArguments().getType("key") to retrieve back the values in the bundle in onCreateView()*/
    public FragmentStatePagerAdapterFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_subribe, container, false);
        rootView.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));

        mViewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        SubcribeImagemodelinfo();
        return rootView;

    }
    /**
     * Destination Details info .
     */
    public void SubcribeImagemodelinfo() {
        showProgressDialog();
        Log.e("riding destination", "riding destination");
        try {
            prefsManager = new PrefsManager(getActivity());
            AccessToken = prefsManager.getToken();
            UserId = prefsManager.getCaseId();
            Log.e("AccessToken:", "" + AccessToken + "----UserId----" + UserId);

            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            RequestJsonObject loginTaskRequest = new RequestJsonObject(Request.Method.POST,
                    ApplicationGlobal.ROOT + ApplicationGlobal.baseurl_ridingSubcribedetails + "userId=" + UserId + "&destId=" + DestinationsDetailActivity.DestID + "&&accessToken=" + AccessToken, null,
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

                Type type = new TypeToken<SubcribeData>() {
                }.getType();
                SubcribeData subcribeData = new Gson().fromJson(response.toString(), type);
                 fullImageList.clear();
                subcribesImages.clear();
                if (subcribeData.getSuccess().equals("1")) {
                    data=subcribeData.getData();
                    subcribesImages.addAll(data.getImages());
                    for (int i = 0; i < subcribesImages.size(); i++) {
                        fullImageList.add(new UtilsSubcribe.DummyItem(subcribesImages.get(i).getDestImage(), "Full Image:"+subcribesImages.get(i).getDestImageId()));
                    }
                    dismissProgressDialog();
                    mPagerAdapter = new MyFragmentStatePagerAdapter(getChildFragmentManager(), fullImageList);
                    mViewPager.setAdapter(mPagerAdapter);
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

        mCustomizeDialog = new CustomizeDialog(getActivity());
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


}
