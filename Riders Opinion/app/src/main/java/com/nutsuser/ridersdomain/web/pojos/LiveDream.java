
package com.nutsuser.ridersdomain.web.pojos;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class LiveDream {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("success")
    @Expose
    private Integer success;

    @SerializedName("dreamId")
    @Expose
    private Integer dreamId;

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
     *     The dreamId
     */
    public Integer getdreamId() {
        return dreamId;
    }

    /**
     *
     * @param dreamId
     *     The dreamId
     */
    public void setdreamId(Integer dreamId) {
        this.dreamId = dreamId;
    }

}
