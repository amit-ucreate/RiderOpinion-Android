
package com.nutsuser.ridersdomain.web.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class MyFriends {

    @SerializedName("user_friend_id")
    @Expose
    private String userFriendId;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("friend_id")
    @Expose
    private String friendId;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("join_date")
    @Expose
    private String joinDate;

    /**
     * 
     * @return
     *     The userFriendId
     */
    public String getUserFriendId() {
        return userFriendId;
    }

    /**
     * 
     * @param userFriendId
     *     The user_friend_id
     */
    public void setUserFriendId(String userFriendId) {
        this.userFriendId = userFriendId;
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
     *     The user_id
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

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
     *     The friend_id
     */
    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    /**
     * 
     * @return
     *     The status
     */
    public String getStatus() {
        return status;
    }

    /**
     * 
     * @param status
     *     The status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 
     * @return
     *     The joinDate
     */
    public String getJoinDate() {
        return joinDate;
    }

    /**
     * 
     * @param joinDate
     *     The join_date
     */
    public void setJoinDate(String joinDate) {
        this.joinDate = joinDate;
    }

}
