package com.nutsuser.ridersdomain.web.api;

import com.google.gson.FieldNamingPolicy;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by user on 10/27/2015.
 */
public class RestClient {
    //test   https://ridersapi.herokuapp.com
    //live  http://ridersopininon.herokuapp.com
    static String ROOT = "https://ridersapi.herokuapp.com/index.php/";
    private static API REST_CLIENT;

    static {
        setupRestClient();
    }
    private RestClient() {
    }

    public static API get() {
        return REST_CLIENT;
    }

    private static void setupRestClient() {
        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setEndpoint(ROOT).setLogLevel(RestAdapter.LogLevel.FULL)
                .setClient(new OkClient());
        builder.setLogLevel(RestAdapter.LogLevel.FULL);
        RestAdapter restAdapter = builder.build();

        REST_CLIENT = restAdapter.create(API.class);
    }
}