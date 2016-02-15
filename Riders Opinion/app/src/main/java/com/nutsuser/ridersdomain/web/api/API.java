package com.nutsuser.ridersdomain.web.api;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.Map;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FieldMap;
import retrofit.http.GET;
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
    void ridingDestination(@FieldMap Map<String, String> params ,Callback<JsonObject> callback);



}
