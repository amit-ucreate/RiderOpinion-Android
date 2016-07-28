
package com.nutsuser.ridersdomain.web.pojos;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class MyFriendsList {

    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("data")
    @Expose
    private List<DataMyFriendsList> data = new ArrayList<DataMyFriendsList>();
    @SerializedName("friendCount")
    @Expose
    private String friendCount;
    @SerializedName("requests")
    @Expose
    private String requests;
    @SerializedName("message")
    @Expose
    private String message;

    /**
     * 
     * @return
     *     The success
     */
    public Integer getSuccess() {
        return success;
    }

    /**
     * 
     * @param success
     *     The success
     */
    public void setSuccess(Integer success) {
        this.success = success;
    }

    /**
     * 
     * @return
     *     The data
     */
    public List<DataMyFriendsList> getData() {
        return data;
    }

    /**
     * 
     * @param data
     *     The data
     */
    public void setData(List<DataMyFriendsList> data) {
        this.data = data;
    }

    /**
     * 
     * @return
     *     The friendCount
     */
    public String getFriendCount() {
        return friendCount;
    }

    /**
     * 
     * @param friendCount
     *     The friendCount
     */
    public void setFriendCount(String friendCount) {
        this.friendCount = friendCount;
    }

    /**
     * 
     * @return
     *     The requests
     */
    public String getRequests() {
        return requests;
    }

    /**
     * 
     * @param requests
     *     The requests
     */
    public void setRequests(String requests) {
        this.requests = requests;
    }

    /**
     * 
     * @return
     *     The message
     */
    public String getMessage() {
        return message;
    }

    /**
     * 
     * @param message
     *     The message
     */
    public void setMessage(String message) {
        this.message = message;
    }

}
