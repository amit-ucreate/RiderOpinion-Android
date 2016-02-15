package com.nutsuser.ridersdomain.web.api;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by user on 10/27/2015.
 */
public class RestClient {
    //test  http://wymservice.ucreate.co.in/APIServices.svc
    //live  http://services.whenyoumove.com/APIServices.svc

    static String ROOT = "http://ridersopininon.herokuapp.com/index.php/";
    private static API REST_CLIENT;

    static {
        setupRestClient();
    }

    private RestClient() {
    }

    public static API get() {
        return REST_CLIENT;
    }
    RequestInterceptor requestInterceptor = new RequestInterceptor() {
        @Override
        public void intercept(RequestFacade request) {
           // request.addHeader("User-Agent", "Retrofit-Sample-App");
        }
    };
    private static void setupRestClient() {
        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setEndpoint(ROOT)
                .setClient(new OkClient());
        builder.setLogLevel(RestAdapter.LogLevel.FULL);
        RestAdapter restAdapter = builder.build();

        REST_CLIENT = restAdapter.create(API.class);
    }
}