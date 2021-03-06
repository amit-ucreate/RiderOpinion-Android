
package com.nutsuser.ridersdomain.web.pojos;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class FriendsCombineListResponse {

    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("data")
    @Expose
    private FriendCombineData data;
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
    public FriendCombineData getData() {
        return data;
    }

    /**
     * 
     * @param data
     *     The data
     */
    public void setData(FriendCombineData data) {
        this.data = data;
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
