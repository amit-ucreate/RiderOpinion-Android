
package com.nutsuser.ridersdomain.web.pojos;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class FriendCombineData {

    @SerializedName("friendRequest")
    @Expose
    private List<FriendRequest> friendRequest = new ArrayList<FriendRequest>();
    @SerializedName("friendList")
    @Expose
    private List<FriendList> friendList = new ArrayList<FriendList>();

    /**
     * 
     * @return
     *     The friendRequest
     */
    public List<FriendRequest> getFriendRequest() {
        return friendRequest;
    }

    /**
     * 
     * @param friendRequest
     *     The friendRequest
     */
    public void setFriendRequest(List<FriendRequest> friendRequest) {
        this.friendRequest = friendRequest;
    }

    /**
     * 
     * @return
     *     The friendList
     */
    public List<FriendList> getFriendList() {
        return friendList;
    }

    /**
     * 
     * @param friendList
     *     The friendList
     */
    public void setFriendList(List<FriendList> friendList) {
        this.friendList = friendList;
    }

}
