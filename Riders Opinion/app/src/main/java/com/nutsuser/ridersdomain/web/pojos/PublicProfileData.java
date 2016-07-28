
package com.nutsuser.ridersdomain.web.pojos;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class PublicProfileData {

    @SerializedName("requestStatus")
    @Expose
    private String requestStatus;
    @SerializedName("MutualFriends")
    @Expose
    private List<MutualFriend> mutualFriends = new ArrayList<MutualFriend>();
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("profileName")
    @Expose
    private String profileName;
    @SerializedName("userName")
    @Expose
    private String userName;
    @SerializedName("baseLocation")
    @Expose
    private String baseLocation;
    @SerializedName("vehName")
    @Expose
    private String vehName;
    @SerializedName("vehType")
    @Expose
    private String vehType;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("totalMiles")
    @Expose
    private Float totalMiles;
    @SerializedName("countOfRides")
    @Expose
    private Integer countOfRides;
    @SerializedName("myFriends")
    @Expose
    private MyFriends myFriends;
    @SerializedName("coverImage")
    @Expose
    private String coverImage;
    @SerializedName("userImage")
    @Expose
    private String userImage;
    @SerializedName("friendStatus")
    @Expose
    private Integer friendStatus;
    @SerializedName("status_message")
    @Expose
    private String statusMessage;
    @SerializedName("RecentFriend")
    @Expose
    private List<RecentFriend> recentFriend = new ArrayList<RecentFriend>();

    /**
     * 
     * @return
     *     The requestStatus
     */
    public String getRequestStatus() {
        return requestStatus;
    }

    /**
     * 
     * @param requestStatus
     *     The requestStatus
     */
    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    /**
     * 
     * @return
     *     The mutualFriends
     */
    public List<MutualFriend> getMutualFriends() {
        return mutualFriends;
    }

    /**
     * 
     * @param mutualFriends
     *     The MutualFriends
     */
    public void setMutualFriends(List<MutualFriend> mutualFriends) {
        this.mutualFriends = mutualFriends;
    }

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
     *     The profileName
     */
    public String getProfileName() {
        return profileName;
    }

    /**
     * 
     * @param profileName
     *     The profileName
     */
    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    /**
     * 
     * @return
     *     The userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 
     * @param userName
     *     The userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * 
     * @return
     *     The baseLocation
     */
    public String getBaseLocation() {
        return baseLocation;
    }

    /**
     * 
     * @param baseLocation
     *     The baseLocation
     */
    public void setBaseLocation(String baseLocation) {
        this.baseLocation = baseLocation;
    }

    /**
     * 
     * @return
     *     The vehName
     */
    public String getVehName() {
        return vehName;
    }

    /**
     * 
     * @param vehName
     *     The vehName
     */
    public void setVehName(String vehName) {
        this.vehName = vehName;
    }

    /**
     * 
     * @return
     *     The vehType
     */
    public String getVehType() {
        return vehType;
    }

    /**
     * 
     * @param vehType
     *     The vehType
     */
    public void setVehType(String vehType) {
        this.vehType = vehType;
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
     *     The description
     */
    public String getDescription() {
        return description;
    }

    /**
     * 
     * @param description
     *     The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 
     * @return
     *     The totalMiles
     */
    public Float getTotalMiles() {
        return totalMiles;
    }

    /**
     * 
     * @param totalMiles
     *     The totalMiles
     */
    public void setTotalMiles(Float totalMiles) {
        this.totalMiles = totalMiles;
    }

    /**
     * 
     * @return
     *     The countOfRides
     */
    public Integer getCountOfRides() {
        return countOfRides;
    }

    /**
     * 
     * @param countOfRides
     *     The countOfRides
     */
    public void setCountOfRides(Integer countOfRides) {
        this.countOfRides = countOfRides;
    }

    /**
     * 
     * @return
     *     The myFriends
     */
    public MyFriends getMyFriends() {
        return myFriends;
    }

    /**
     * 
     * @param myFriends
     *     The myFriends
     */
    public void setMyFriends(MyFriends myFriends) {
        this.myFriends = myFriends;
    }

    /**
     * 
     * @return
     *     The coverImage
     */
    public String getCoverImage() {
        return coverImage;
    }

    /**
     * 
     * @param coverImage
     *     The coverImage
     */
    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    /**
     * 
     * @return
     *     The userImage
     */
    public String getUserImage() {
        return userImage;
    }

    /**
     * 
     * @param userImage
     *     The userImage
     */
    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    /**
     * 
     * @return
     *     The friendStatus
     */
    public Integer getFriendStatus() {
        return friendStatus;
    }

    /**
     * 
     * @param friendStatus
     *     The friendStatus
     */
    public void setFriendStatus(Integer friendStatus) {
        this.friendStatus = friendStatus;
    }

    /**
     * 
     * @return
     *     The statusMessage
     */
    public String getStatusMessage() {
        return statusMessage;
    }

    /**
     * 
     * @param statusMessage
     *     The status_message
     */
    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    /**
     * 
     * @return
     *     The recentFriend
     */
    public List<RecentFriend> getRecentFriend() {
        return recentFriend;
    }

    /**
     * 
     * @param recentFriend
     *     The RecentFriend
     */
    public void setRecentFriend(List<RecentFriend> recentFriend) {
        this.recentFriend = recentFriend;
    }

}
