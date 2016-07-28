
package com.nutsuser.ridersdomain.web.pojos;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class MainScreenData {

    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("CountEvent")
    @Expose
    private String countEvent;
    @SerializedName("CountProfile")
    @Expose
    private Integer countProfile;
    @SerializedName("type")
    @Expose
    private String type;

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
     *     The countEvent
     */
    public String getCountEvent() {
        return countEvent;
    }

    /**
     * 
     * @param countEvent
     *     The CountEvent
     */
    public void setCountEvent(String countEvent) {
        this.countEvent = countEvent;
    }

    /**
     * 
     * @return
     *     The countProfile
     */
    public Integer getCountProfile() {
        return countProfile;
    }

    /**
     * 
     * @param countProfile
     *     The CountProfile
     */
    public void setCountProfile(Integer countProfile) {
        this.countProfile = countProfile;
    }

    /**
     * 
     * @return
     *     The type
     */
    public String getType() {
        return type;
    }

    /**
     * 
     * @param type
     *     The type
     */
    public void setType(String type) {
        this.type = type;
    }

}
