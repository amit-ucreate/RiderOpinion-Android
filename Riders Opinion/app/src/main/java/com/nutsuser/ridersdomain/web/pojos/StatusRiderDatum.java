
package com.nutsuser.ridersdomain.web.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class StatusRiderDatum {

    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("image")
    @Expose
    private String image;

    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("nick_name")
    @Expose
    private String nick_name;

    @SerializedName("vehicle_type")
    @Expose
    private String vehicle_type;

    @SerializedName("base_location")
    @Expose
    private String base_location;

    @SerializedName("vehicle_name")
    @Expose
    private String vehicle_name;


    /**
     * 
     * @return
     *     The userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * 
     * @param userId
     *     The userId
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * 
     * @return
     *     The latitude
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     * 
     * @param latitude
     *     The latitude
     */
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    /**
     * 
     * @return
     *     The longitude
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     * 
     * @param longitude
     *     The longitude
     */
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    /**
     * 
     * @return
     *     The image
     */
    public String getImage() {
        return image;
    }

    /**
     * 
     * @param image
     *     The image
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     *
     * @return
     *     The username
     */
    public String getUsername() {
        return username;
    }

    /**
     *
     * @param username
     *     The username
     */
    public void setUsername(String username) {
        this.username = username;
    }



    /**
     *
     * @return
     *     The nick_name
     */
    public String getNickName() {
        return nick_name;
    }

    /**
     *
     * @param nick_name
     *     The nick_name
     */
    public void setNickName(String nick_name) {
        this.nick_name = nick_name;
    }



    /**
     *
     * @return
     *     The vehicle_type
     */
    public String getVehicleType() {
        return vehicle_type;
    }

    /**
     *
     * @param vehicle_type
     *     The vehicle_type
     */
    public void setVehicleType(String vehicle_type) {
        this.vehicle_type = vehicle_type;
    }


    /**
     *
     * @return
     *     The base_location
     */
    public String getBaseLocation() {
        return base_location;
    }

    /**
     *
     * @param base_location
     *     The base_location
     */
    public void setBaseLocation(String base_location) {
        this.base_location = base_location;
    }


    /**
     *
     * @return
     *     The vehicle_name
     */
    public String getVehicleName() {
        return vehicle_name;
    }

    /**
     *
     * @param vehicle_name
     *     The vehicle_name
     */
    public void setVehicleName(String vehicle_name) {
        this.vehicle_name = vehicle_name;
    }
}
