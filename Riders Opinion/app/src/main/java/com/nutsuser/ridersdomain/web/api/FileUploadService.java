package com.nutsuser.ridersdomain.web.api;



import com.google.gson.JsonObject;

import retrofit.Callback;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.mime.TypedFile;

/**
 * Created by admin on 22-02-2016.
 */
public interface FileUploadService {
    public static final String BASE_URL = "http://ridersopininon.herokuapp.com/index.php";
    @Multipart
    @POST("/rideEvent/imageUpload")
    void upload_(@Part("image") TypedFile image,
                 @Part("accessToken") String accessToken, @Part("eventId") String eventId,
                 Callback<JsonObject> cb);
    @Multipart
    @POST("/rideEvent/imageUpload")
    void upload(TypedFile typedFile, String description, Callback<JsonObject> callback);
}
