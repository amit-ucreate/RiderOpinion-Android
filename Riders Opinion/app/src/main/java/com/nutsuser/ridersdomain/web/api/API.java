package com.nutsuser.ridersdomain.web.api;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.Map;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.FieldMap;
import retrofit.http.Headers;
import retrofit.http.POST;

/**
 * Created by user on 10/27/2015.
 */
public interface API {

    @Headers("Content-type: application/json")
    @POST("/UserRegistration")
    void doRegisteration(@Body JsonObject jsonObject, Callback<JsonPrimitive> callback);

    @Headers("Content-type: application/json")
    @POST("/ridingDestination")
    void index(Map<String, String> params, Callback<JsonObject> callback);

    @Headers("Content-type: application/json")
    @POST("/ridingDestination")
    void ridingDestination(@FieldMap Map<String, String> params, Callback<JsonObject> callback);

    //http://ridersopininon.herokuapp.com/index.php/ridingDestination?userId=138&longitude=76.70724116&latitude=30.71017463&radius=2820&accessToken=7a2ca049f3179ad6b3bbb2864eda6a63
    //  @Headers("Content-type: application/json")
    //  @GET("/ridingDestination?")
//    void riding_estination(@Query ,Callback<JsonObject> callback);


}
