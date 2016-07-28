
package com.nutsuser.ridersdomain.web.pojos;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class FriendList {

    @SerializedName("friendId")
    @Expose
    private String friendId;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("userImage")
    @Expose
    private String userImage;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("profileName")
    @Expose
    private String profileName;
    @SerializedName("vehicleType")
    @Expose
    private String vehicleType;

/*

    public  FriendList(String fID, String uId,String uImg,String uNm, String pNm, String vTpe){
        friendId = fID;
                userId = uId;
        userImage = uImg;
                username= uNm;
        profileName = pNm;
                vehicleType = vTpe;
    }
*/

    /**
     * 
     * @return
     *     The friendId
     */
    public String getFriendId() {
        return friendId;
    }

    /**
     * 
     * @param friendId
     *     The friendId
     */
    public void setFriendId(String friendId) {
        this.friendId = friendId;
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
     *     The vehicleType
     */
    public String getVehicleType() {
        return vehicleType;
    }

    /**
     * 
     * @param vehicleType
     *     The vehicleType
     */
    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

}
