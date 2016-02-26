package com.nutsuser.ridersdomain.web.pojos;

/**
 * Created by admin on 24-02-2016.
 */
public class TrackUserListData {
String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    String latitude;
    String longitude;
    String rideDistance;
    String username;

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getRideDistance() {
        return rideDistance;
    }

    public void setRideDistance(String rideDistance) {
        this.rideDistance = rideDistance;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    String image;

}
