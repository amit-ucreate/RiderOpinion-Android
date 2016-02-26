package com.nutsuser.ridersdomain.web.api;

import android.app.Activity;

import com.squareup.okhttp.OkHttpClient;

import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by admin on 22-02-2016.
 */
public class ServiceGenerator {
    private static FileUploadService REST_CLIENT;

    public static final String API_BASE_URL = "http://ridersopininon.herokuapp.com/index.php";

    private static RestAdapter.Builder builder = new RestAdapter.Builder()
            .setEndpoint(API_BASE_URL)
            .setClient(new OkClient(new OkHttpClient()));

    public static FileUploadService createService(Activity mainActivity, String baseUrl) {
        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setEndpoint(baseUrl)
                .setClient(new OkClient());
        builder.setLogLevel(RestAdapter.LogLevel.FULL);
        RestAdapter restAdapter = builder.build();

        REST_CLIENT = restAdapter.create(FileUploadService.class);
        return REST_CLIENT;
    }
}
