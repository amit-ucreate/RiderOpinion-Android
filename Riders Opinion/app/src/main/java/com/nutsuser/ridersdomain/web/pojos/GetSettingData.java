
package com.nutsuser.ridersdomain.web.pojos;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class GetSettingData {

    @SerializedName("settingId")
    @Expose
    private String settingId;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("event")
    @Expose
    private String event;
    @SerializedName("destination")
    @Expose
    private String destination;
    @SerializedName("customization")
    @Expose
    private String customization;
    @SerializedName("distance_to")
    @Expose
    private String distanceTo;
    @SerializedName("distance_from")
    @Expose
    private String distanceFrom;


    @SerializedName("friendsAlert")
    @Expose
    private String friendsAlert;


    @SerializedName("chatMessage")
    @Expose
    private String chatMessage;

    @SerializedName("main_Notification")
    @Expose
    private String main_Notification;

    @SerializedName("deleteDays")
    @Expose
    private String deleteDays;



    /**
     * 
     * @return
     *     The settingId
     */
    public String getSettingId() {
        return settingId;
    }

    /**
     * 
     * @param settingId
     *     The settingId
     */
    public void setSettingId(String settingId) {
        this.settingId = settingId;
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
     *     The event
     */
    public String getEvent() {
        return event;
    }

    /**
     * 
     * @param event
     *     The event
     */
    public void setEvent(String event) {
        this.event = event;
    }

    /**
     * 
     * @return
     *     The destination
     */
    public String getDestination() {
        return destination;
    }

    /**
     * 
     * @param destination
     *     The destination
     */
    public void setDestination(String destination) {
        this.destination = destination;
    }

    /**
     * 
     * @return
     *     The customization
     */
    public String getCustomization() {
        return customization;
    }

    /**
     * 
     * @param customization
     *     The customization
     */
    public void setCustomization(String customization) {
        this.customization = customization;
    }

    /**
     * 
     * @return
     *     The distanceTo
     */
    public String getDistanceTo() {
        return distanceTo;
    }

    /**
     * 
     * @param distanceTo
     *     The distance_to
     */
    public void setDistanceTo(String distanceTo) {
        this.distanceTo = distanceTo;
    }

    /**
     * 
     * @return
     *     The distanceFrom
     */
    public String getDistanceFrom() {
        return distanceFrom;
    }

    /**
     * 
     * @param distanceFrom
     *     The distance_from
     */
    public void setDistanceFrom(String distanceFrom) {
        this.distanceFrom = distanceFrom;
    }


    /**
     *
     * @return
     *     The friendsAlert
     */
    public String getFriendsAlert() {
        return friendsAlert;
    }

    /**
     *
     * @param friendsAlert
     *     The friendsAlert
     */
    public void setFriendsAlert(String friendsAlert) {
        this.friendsAlert = friendsAlert;
    }


    /**
     *
     * @return
     *     The chatMessage
     */
    public String getChatMessage() {
        return chatMessage;
    }

    /**
     *
     * @param chatMessage
     *     The chatMessage
     */
    public void setChatMessage(String chatMessage) {
        this.chatMessage = chatMessage;
    }


    /**
     *
     * @return
     *     The main_Notification
     */
    public String getMainNotification() {
        return main_Notification;
    }

    /**
     *
     * @param main_Notification
     *     The main_Notification
     */
    public void setMainNotification(String main_Notification) {
        this.main_Notification = main_Notification;
    }

    /**
     *
     * @return
     *     The deleteDays
     */
    public String getDeleteDays() {
        return deleteDays;
    }

    /**
     *
     * @param deleteDays
     *     The deleteDays
     */
    public void setDeleteDays(String deleteDays) {
        this.deleteDays = deleteDays;
    }

}


