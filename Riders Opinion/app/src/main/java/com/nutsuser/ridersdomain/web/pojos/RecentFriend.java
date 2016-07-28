
package com.nutsuser.ridersdomain.web.pojos;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class RecentFriend {

    @SerializedName("RecentFriendImage")
    @Expose
    private String recentFriendImage;
    @SerializedName("FriendId")
    @Expose
    private String friendId;

    /**
     * 
     * @return
     *     The recentFriendImage
     */
    public String getRecentFriendImage() {
        return recentFriendImage;
    }

    /**
     * 
     * @param recentFriendImage
     *     The RecentFriendImage
     */
    public void setRecentFriendImage(String recentFriendImage) {
        this.recentFriendImage = recentFriendImage;
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
     *     The FriendId
     */
    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

}
