package com.nutsuser.ridersdomain.web.api;


import com.google.gson.JsonObject;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Query;
import retrofit.mime.TypedFile;

/**
 * Created by admin on 22-02-2016.
 */
public interface FileUploadService {
    //test   https://ridersapi.herokuapp.com
    //live  http://ridersopininon.herokuapp.com
    public static final String BASE_URL = "https://ridersapi.herokuapp.com/index.php/";
    public static final String BASE_URL_NEARBY_FRIENDS = "https://ridersapi.herokuapp.com/";
    //http://ridersopininon.herokuapp.com/

    /**
     * Web service to Favroite Destination.
     * <p/>
     * Insert app user favroiteDestination.
     * [HttpPost]
     * Route:BASE_URL
     * [Child("/ridingDestination/favroiteDestination")]
     * <p/>
     */
    @GET("/ridingDestination/favoriteDestination")
    void favroiteDestination(@Query("userId") String userId, @Query("accessToken") String accessToken,
                             Callback<JsonObject> cb);

    /**
     * Web service to POST imageUpload.
     * <p/>
     * Insert app user imageUpload data.
     * [HttpPost]
     * Route:BASE_URL
     * [Child("/rideEvent/imageUpload")]
     * <p/>
     */
    @Multipart
    @POST("/rideEvent/imageUpload")
    void upload_(@Part("image") TypedFile image,
                 @Part("accessToken") String accessToken, @Part("eventId") String eventId,
                 Callback<JsonObject> cb);

    /**
     * Web service to POST trackUser.
     * <p/>
     * Insert app user trackUser data.
     * [HttpPost]
     * Route:BASE_URL
     * [Child("/track/trackUser")]
     * <p/>
     */
    @Multipart
    @POST("/track/trackUser")
    void tracking_info(@Part("userId") String userId, @Part("eventId") String eventId, @Part("lat") String lat, @Part("long") String longi, @Part("accessToken") String accessToken, Callback<JsonObject> callback);

    /**
     * Web service to POST eventDetail.
     * <p/>
     * Insert app user eventDetail data.
     * [HttpPost]
     * Route:BASE_URL
     * [Child("/rideEvent/eventDetail")]
     * <p/>
     */
    @Multipart
    @POST("/rideEvent/eventDetail")
    void eventDetail(@Part("userId") String userId, @Part("accessToken") String accessToken, @Part("eventId") String eventId, Callback<JsonObject> callback);

    /**
     * Web service to POST updateTrack.
     * <p/>
     * Insert app user updateTrack data.
     * [HttpPost]
     * Route:BASE_URL
     * [Child("/track/updateTrack")]
     * <p/>
     */
    @Multipart
    @POST("/track/updateTrack")
    void updateTrack(@Part("userId") String userId, @Part("eventId") String eventId, @Part("lat") String lat, @Part("long") String long1, @Part("accessToken") String accessToken, Callback<JsonObject> callback);

    /**
     * Web service to GET updateTrack.
     * <p/>
     * Insert app user myRides data.
     * Route:BASE_URL
     * [Child("/riderProfile/myRides")]
     * <p/>
     */
    @GET("/riderProfile/myRides")
    void MyRide(@Query("userId") String userId, @Query("accessToken") String accessToken,
                @Query("condition") String condition,
                Callback<JsonObject> cb);

    @GET("/rideEvent/joinEvent?")
    void joinEvent(@Query("userId") String userId, @Query("accessToken") String accessToken,
                @Query("eventId") String eventId,
                Callback<JsonObject> cb);




    /**
     * Web service to POST upcoming ride.
     * <p/>
     * Insert app user upcoming data.
     * [HttpPost]
     * Route:BASE_URL
     * [Child("/track/trackUser")]
     * <p/>
     */
    @Multipart
    @POST("/riderProfile/deleteRide")
    void remove_riding(@Part("joinEventId") String joinEventId, @Part("accessToken") String accessToken, Callback<JsonObject> callback);

    /**
     * Web service to POST upcoming ride.
     * <p/>
     * Insert app user upcoming data.
     * [HttpPost]
     * Route:BASE_URL
     * [Child("/ridingDestination/favroite")]
     * <p/>
     */
    @Multipart
    @POST("/ridingDestination/favroite")
    void remove_fav(@Part("userId") String userId, @Part("destId") String destId, @Part("accessToken") String accessToken, Callback<JsonObject> callback);

    /**
     * Web service to Update Profile only Text.
     * <p/>
     * Insert app user favourite stream data.
     * [HttpPost]
     * Route:BASE_URL
     * [Child("/riderProfile/updateProfile")]
     * <p/>
     */
    @GET("/riderProfile/updateProfile")
    void profile_upload_data(@Query("userId") String userId, @Query("profileName") String profileName,
                             @Query("userName") String userName, @Query("email") String email, @Query("phone") String phone,
                             @Query("baseLocation") String baseLocation, @Query("latitude") String latitude,
                             @Query("longitude") String longitude, @Query("vehTypeId") String vehTypeId,
                             @Query("description") String description, @Query("vehicle_year") String vehicle_year,  @Query("accessToken") String accessToken,
                             Callback<JsonObject> cb);


    /**
     * Web service to Update Profile Image.
     * <p/>
     * Insert app user favourite stream data.
     * [HttpPost]
     * Route:BASE_URL
     * [Child("/riderProfile/uploadImage")]
     * <p/>
     */
    @Multipart
    @POST("/riderProfile/uploadImage")
    void profileImageUpload(@Part("userId") String userId, @Part("image") TypedFile image,
                            @Part("accessToken") String accessToken, Callback<JsonObject> cb);

    /**
     * Web service to Update Banner Image.
     * <p/>
     * Insert app user favourite stream data.
     * [HttpPost]
     * Route:BASE_URL
     * [Child("/riderProfile/uploadProfileImage")]
     * <p/>
     */
    @Multipart
    @POST("/riderProfile/uploadProfileImage")
    void bannerImageUpload(@Part("userId") String userId, @Part("image") TypedFile image,
                           @Part("accessToken") String accessToken, Callback<JsonObject> cb);


    /**
     * Web service to Update Profile only Text.
     * <p/>
     * Insert app user favourite stream data.
     * [HttpPost]
     * Route:BASE_URL
     * [Child("/riderProfile/viewProfile")]
     * <p/>
     */
    //http://ridersopininon.herokuapp.com/index.php/riderProfile/viewProfile?userId=340&friendId=357&accessToken=62b4cb77c3a806f25995b0abd718b7f8
    @GET("/riderProfile/viewProfile")
    void viewPublicProfile(@Query("userId") String userId, @Query("friendId") String friendId,@Query("accessToken") String accessToken,
                           Callback<JsonObject> cb);
    //http://ridersopininon.herokuapp.com/riderProfile/viewProfile?userId=339&accessToken=9361e1486ddf46fa3a463136c3c35313&friendId=345

    /**
     * Web service to Update Profile only Text.
     * <p/>
     * Insert app user favourite stream data.
     * [HttpPost]
     * Route:BASE_URL
     * [Child("/riderProfile/viewProfile")]
     * <p/>
     */
    @GET("/riderProfile/editProfile")
    void editProfile(@Query("userId") String userId, @Query("accessToken") String accessToken,
                     Callback<JsonObject> cb);

    /**
     * Web service to Update setting only Text.
     * <p/>
     * [HttpPost]
     * Route:BASE_URL
     * [Child("/riderProfile/setting")]
     * <p/>
     */

    @Multipart
    @POST("/riderProfile/updateSetting")
    void updateSetting(@Part("userId") String userId, @Part("accessToken") String accessToken,
                       @Part("destination") String destination, @Part("event") String event,
                       @Part("customization") String customization, @Part("distance_to") String distanc_to,
                       @Part("distance_from") String distanc_from,@Part("chatMessage") String chatMessage,
                       @Part("deleteDays") String deleteDays,@Part("friendsAlert") String friendsAlert,
                       @Part("main_Notification") String main_Notification,
                       Callback<JsonObject> cb);

    //

    /**
     * Web service to ridingDestination ridingDestinationAfterSubscribe.
     * <p/>
     * [HttpPost]
     * Route:BASE_URL
     * [Child("/ridingDestination/ridingDestinationAfterSubscribe")]
     * <p/>
     */
    @GET("/ridingDestination/ridingDestinationAfterSubscribe")
    void ridingDestinationAfterSubscribe(@Query("userId") String userId, @Query("accessToken") String accessToken,
                                         @Query("destId") String destId,
                                         Callback<JsonObject> cb);

    /**
     * Web service to Update setting only Text.
     * <p/>
     * [HttpPost]
     * Route:BASE_URL
     * [Child("/riderProfile/setting")]
     * <p/>
     */
    @GET("/riderProfile/getSetting")
    void getSetting(@Query("userId") String userId, @Query("accessToken") String accessToken,
                    Callback<JsonObject> cb);

    /**
     * Web service to POST modifyYourBike.
     * <p/>
     * Insert app user upcoming data.
     * [HttpPost]
     * Route:BASE_URL
     * [Child("/modifyYourBike")]
     * <p/>
     */
    @Multipart
    @POST("/modifyYourBike")
    void modifyYourBike(@Part("bikeId") String bikeId, @Part("userId") String userId, @Part("accessToken") String accessToken, Callback<JsonObject> callback);


    //http://ridersopininon.herokuapp.com/modifyYourBike?bikeId=2&userId=374&search_keyword=Indian&accessToken=d2e5d33edf484140c8e676c507405a5e
    @Multipart
    @POST("/modifyYourBike")
    void modifyYourBikeSearch(@Part("bikeId") String bikeId, @Part("userId") String userId, @Part("search_keyword") String search_keyword, @Part("accessToken") String accessToken, Callback<JsonObject> callback);



    /**
     * Web service to POST modifyYourBike venders.
     * <p/>
     * Insert app user upcoming data.
     * [HttpPost]
     * Route:BASE_URL
     * [Child("/modifyYourBike/venders")]
     * <p/>
     */
    @Multipart
    @POST("/modifyYourBike/venders")
    void venders(@Part("bikeId") String bikeId, @Part("catId") String catId, @Part("userId") String userId, @Part("accessToken") String accessToken, Callback<JsonObject> callback);
//
//

    /**
     * Web service to POST rideEvent search.
     * <p/>
     * Insert app user upcoming data.
     * [HttpPost]
     * Route:BASE_URL
     * [Child("/rideEvent/search")]
     * <p/>
     */
    @Multipart
    @POST("/rideEvent/search")
    void ridingeventsearch(@Part("userId") String userId, @Part("baseLat") String baseLat, @Part("baseLon") String baseLon, @Part("search") String search, @Part("accessToken") String accessToken, Callback<JsonObject> callback);

    /**
     * Web service to GET  map saveCounter.
     * <p/>
     * Insert app user upcoming data.
     * [HttpPost]
     * Route:BASE_URL
     * [Child("/ map/saveCounter")]
     * <p/>
     */
    @Multipart
    @POST("/map/saveCounter")
    void saveCounter(@Part("baseLocation") String baseLocation, @Part("destLocation") String destLocation, @Part("accessToken") String accessToken, Callback<JsonObject> callback);

    /**
     * Web service to GET  map saveCounter.
     * <p/>
     * Insert app user upcoming data.
     * [HttpPost]
     * Route:BASE_URL
     * [Child("/map/getCounter")]
     * <p/>
     */
    @Multipart
    @POST("/map/getCounter")
    void getCounter(@Part("baseLocation") String baseLocation, @Part("destLocation") String destLocation, @Part("accessToken") String accessToken, Callback<JsonObject> callback);

    /**
     * Web service to GET  map saveCounter.
     * <p/>
     * Insert app user upcoming data.
     * [HttpPost]
     * Route:BASE_URL
     * [Child("/map/getCounter")]
     * <p/>
     */
    @Multipart
    @POST("/ridingDestination")
    void ridingDestinationfilter(@Part("userId") String userId, @Part("longitude") String longitude, @Part("latitude") String latitude, @Part("radius") String radius, @Part("destType") String destType, @Part("accessToken") String accessToken, Callback<JsonObject> callback);

    /**
     * Web service to riders vehicle info only Text.
     * <p/>
     * [HttpPost]
     * Route:BASE_URL
     * [Child("/riders/vehicle")]
     * <p/>
     */
    @GET("/riders/vehicle")
    void getVehicleInfo(Callback<JsonObject> cb);
    //

    /**
     * Web service to GET  map saveCounter.
     * <p/>
     * Insert app user upcoming data.
     * [HttpPost]
     * Route:BASE_URL
     * [Child("/map/getCounter")]
     * <p/>
     */
    @Multipart
    @POST("/modifyYourBike/venderDetail")
    void modifyYourBikevenderDetail(@Part("venderId") String venderId, @Part("bikeId") String bikeId, @Part("catId") String catId, @Part("userId") String userId, @Part("offer") String offer, @Part("accessToken") String accessToken, Callback<JsonObject> callback);
    //rideEvent/getUserOfEvents

    /**
     * Web service to rideEvent getUserOfEvents.
     * <p/>
     * [HttpPost]
     * Route:BASE_URL
     * [Child("/rideEvent/getUserOfEvents")]
     * <p/>
     */
    @GET("/rideEvent/getUserOfEvents")
    void getUserOfEvents(@Query("eventId") String eventId, @Query("accessToken") String accessToken,
                         Callback<JsonObject> cb);

    /**
     * Web service to modifyYourBike favroite.
     * <p/>
     * Insert app user upcoming data.
     * [HttpPost]
     * Route:BASE_URL
     * [Child("modifyYourBike/favroite")]
     * <p/>
     */
    @Multipart
    @POST("/modifyYourBike/favorite?")
    void modifyYourBikefavroite(@Part("venderId") String venderId, @Part("userId") String userId, @Part("accessToken") String accessToken, Callback<JsonObject> callback);

    /**
     * Web service to  riderProfile updateProfileName.
     * <p/>
     * Insert app user riderProfile updateProfileName.
     * [HttpPost]
     * Route:BASE_URL
     * [Child("/riderProfile/updateProfileName")]
     * <p/>
     */
    @GET("/riderProfile/updateProfileName")
    void updateProfileName(@Query("userId") String userId, @Query("profileName") String profileName, @Query("accessToken") String accessToken,
                           Callback<JsonObject> cb);
//

    /**
     * Web service to  riders vehicle
     * <p/>
     * Insert app user riders vehicle.
     * [HttpPost]
     * Route:BASE_URL
     * [Child("/riders/vehicle")]
     * <p/>
     */
    @GET("/riders/vehicle")
    void vehicle(Callback<JsonObject> cb);

    @Multipart
    @POST("/ridingDestination/search")
    void ridingDestinationsearch(@Part("search") String search, @Part("userId") String userId, @Part("longitude") String longitude, @Part("latitude") String latitude, @Part("accessToken") String accessToken, Callback<JsonObject> callback);

    @Multipart
    @POST("/ridingDestination")
    void ridingDestination(@Part("userId") String userId, @Part("longitude") String longitude, @Part("latitude") String latitude, @Part("radius") String radius, @Part("accessToken") String accessToken,@Part("sort") String sorttype, Callback<JsonObject> callback);

    @Multipart
    @POST("/riders/liveTheDream")
    void liveTheDreamText(@Part("userId") String userId, @Part("dreamId") String dreamId, @Part("vehicleId") String vehicleId, @Part("email") String email, @Part("phoneNo") String phoneNo, @Part("about") String about, @Part("accessToken") String accessToken, Callback<JsonObject> callback);

    //UPLOAD ADDRESS PROOF
   // http://ridersopininon.herokuapp.com/riders/uploadAddressProof?userId=&dreamId=&image=&accessToken=
    @Multipart
    @POST("/riders/uploadAddressProof?")
    void uploadAddressProof(@Part("userId") String userId, @Part("dreamId") String dreamId, @Part("image") TypedFile image, @Part("accessToken") String accessToken, Callback<JsonObject> callback);


    // UPLOAD Licence PROOF
    //http://ridersopininon.herokuapp.com/riders/uploadLicence?userId=&dreamId=&image=&accessToken=
    @Multipart
    @POST("/riders/uploadLicence?")
    void uploadLicence(@Part("userId") String userId, @Part("dreamId") String dreamId, @Part("image") TypedFile image, @Part("accessToken") String accessToken, Callback<JsonObject> callback);


    @Multipart
    @POST("/ridingDestination/favroite")
    void ridingDestination_favroite(@Part("userId") String userId, @Part("destId") String destId, @Part("accessToken") String accessToken, Callback<JsonObject> callback);
    @Multipart
    @POST("/ridingDestination")
    void ridingDestinationSort(@Part("userId") String userId, @Part("longitude") String longitude, @Part("latitude") String latitude, @Part("radius") String radius, @Part("accessToken") String accessToken,@Part("sort") String sorttype, Callback<JsonObject> callback);

    @Multipart
    @POST("/riderProfile/nearByFriends")
    void nearByFriends(@Part("userId") String userId, @Part("lat") String latitude, @Part("long") String longitude, @Part("accessToken") String accessToken, Callback<JsonObject> callback);

    @Multipart
    @POST("/riderProfile/notification?")
    void notificationList(@Part("userId") String userId, @Part("accessToken") String accessToken, Callback<JsonObject> callback);


    @Multipart
    @POST("/riderProfile/clearNotification")
    void remove_notification(@Part("notificationId") String notificationId, @Part("accessToken") String accessToken, Callback<JsonObject> callback);


    @Multipart
    @POST("/Friends/userFriendList")
    void get_friendsList(@Part("userId") String userId,@Part("requestType") String requestType ,@Part("accessToken") String accessToken, Callback<JsonObject> callback);


    //http://ridersopininon.herokuapp.com/Friends/userFriendListCombine?userId=374&accessToken=d2e5d33edf484140c8e676c507405a5e
    @Multipart
    @POST("/Friends/userFriendListCombine")
    void get_friendsCombineList(@Part("userId") String userId ,@Part("accessToken") String accessToken, Callback<JsonObject> callback);


    @Multipart
    @POST("/Friends/handelFriends")
    void handleFriends(@Part("userId") String userId,@Part("friendId") String friendId ,@Part("accessToken") String accessToken,@Part("requestStatus") String requestStatus ,Callback<JsonObject> callback);

    //    @GET("/riderProfile/nearByFriends")
    //    void searchNearByFriendsByLocation(@Query("userId") String userId, @Query("latitude") String latitude, @Query("longitude") String longitude, @Query("accessToken") String accessToken,@Query("location") String location, Callback<JsonObject> callback);

    @GET("/riderProfile/nearByFriends")
    void searchNearByFriendsByLocation(@Query("userId") String userId, @Query("latitude") String latitude, @Query("longitude") String longitude, @Query("accessToken") String accessToken,@Query("radius") String location, Callback<JsonObject> callback);

    //

    @GET("/riderProfile/FriendsSearchByLocation")
    void FriendsSearchByLocation(@Query("userId") String userId, @Query("latitude") String latitude, @Query("longitude") String longitude, @Query("accessToken") String accessToken,@Query("radius") String location, Callback<JsonObject> callback);

    // http://ridersopininon.herokuapp.com/Dashboard?userId=371&accessToken=2363e134d7c894c1933c65371ab11add  @Multipart
    @Multipart
    @POST("/Dashboard")
    void badgeCountAPI(@Part("userId") String userId ,@Part("accessToken") String accessToken,Callback<JsonObject> callback);

   // http://localhost/ridersopinion-web/index.php/rideEvent/delNotificationEventData?userId=371&accessToken=2363e134d7c894c1933c65371ab11add
   @Multipart
   @POST("/rideEvent/delNotificationEventData")
   void badgeEventCountDiableAPI(@Part("userId") String userId ,@Part("accessToken") String accessToken,Callback<JsonObject> callback);

//http://ridersopininon.herokuapp.com/index.php/rideEvent/calendarEventListing?userId=386&calendar_date=06/2016&accessToken=8d001557fd52f1d0544541b3ac60a704
    @Multipart
    @POST("/rideEvent/calendarEventListing")
    void calendarEventAPI(@Part("userId") String userId,@Part("calendar_date") String calendar_date ,@Part("accessToken") String accessToken,Callback<JsonObject> callback);


    //http://ridersopininon.herokuapp.com/rideEvent/matchEvent?userId=373&destiLocation=delhi&accessToken=7c538aae9a2717f91c33ad74a9e3be99
    @Multipart
    @POST("/rideEvent/matchEvent")
    void matchDestinationtAPI(@Part("userId") String userId,@Part("destiLocation") String destiLocation ,@Part("accessToken") String accessToken,Callback<JsonObject> callback);


    //http://ridersapi.herokuapp.com/chat/getRecentChatUser?userId=386&accessToken=8d001557fd52f1d0544541b3ac60a704

    @Multipart
    @POST("/chat/getRecentChatUser")
    void getRecentChatFriends(@Part("userId") String userId,@Part("accessToken") String accessToken,Callback<JsonObject> callback);

    //http://ridersapi.herokuapp.com/chat/getParticularUserChat?userId=386&friendId=374&accessToken=8d001557fd52f1d0544541b3ac60a704


    @Multipart
    @POST("/chat/getParticularUserChat")
    void getParticularUserChat(@Part("userId") String userId,@Part("friendId") String friendId,@Part("accessToken") String accessToken,Callback<JsonObject> callback);


    //http://ridersapi.herokuapp.com/chat/getGroupListing?userId=491&accessToken=9fbfc049e28392b0a5f6ec5367903752
    @Multipart
    @POST("/chat/getGroupListing")
    void getGroupListing(@Part("userId") String userId,@Part("accessToken") String accessToken,Callback<JsonObject> callback);
}
