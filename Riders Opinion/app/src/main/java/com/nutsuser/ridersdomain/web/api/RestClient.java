package com.nutsuser.ridersdomain.web.api;

import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by user on 10/27/2015.
 */
public class RestClient {
    //test  http://wymservice.ucreate.co.in/APIServices.svc
    //live  http://services.whenyoumove.com/APIServices.svc

    private static API REST_CLIENT;
     static String ROOT = "http://ridersopininon.herokuapp.com/index.php/";



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
                .setEndpoint(ROOT)
                .setClient(new OkClient());
        builder.setLogLevel(RestAdapter.LogLevel.FULL);
        RestAdapter restAdapter = builder.build();

        REST_CLIENT = restAdapter.create(API.class);
    }
}