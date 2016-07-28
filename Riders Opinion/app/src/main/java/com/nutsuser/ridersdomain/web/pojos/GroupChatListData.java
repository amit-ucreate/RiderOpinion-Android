
package com.nutsuser.ridersdomain.web.pojos;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GroupChatListData {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("sent")
    @Expose
    private String sent;
    @SerializedName("senderId")
    @Expose
    private String senderId;
    @SerializedName("messageId")
    @Expose
    private String messageId;
    @SerializedName("senderName")
    @Expose
    private String senderName;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("lastactivity")
    @Expose
    private String lastactivity;
    @SerializedName("createdby")
    @Expose
    private String createdby;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("invitedusers")
    @Expose
    private String invitedusers;
    @SerializedName("event_id")
    @Expose
    private String eventId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("event_type")
    @Expose
    private String eventType;
    @SerializedName("start_location")
    @Expose
    private String startLocation;
    @SerializedName("destination_location")
    @Expose
    private String destinationLocation;
    @SerializedName("start_time")
    @Expose
    private String startTime;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("date_created")
    @Expose
    private String dateCreated;
    @SerializedName("base_lat")
    @Expose
    private String baseLat;
    @SerializedName("base_long")
    @Expose
    private String baseLong;
    @SerializedName("dest_lat")
    @Expose
    private String destLat;
    @SerializedName("dest_long")
    @Expose
    private String destLong;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("assistance")
    @Expose
    private String assistance;
    @SerializedName("distance")
    @Expose
    private String distance;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("end_date")
    @Expose
    private String endDate;
    @SerializedName("start_date")
    @Expose
    private String startDate;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("countUser")
    @Expose
    private String countUser;
    @SerializedName("daysCount")
    @Expose
    private Integer daysCount;

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
     *     The sent
     */
    public String getSent() {
        return sent;
    }

    /**
     * 
     * @param sent
     *     The sent
     */
    public void setSent(String sent) {
        this.sent = sent;
    }

    /**
     * 
     * @return
     *     The senderId
     */
    public String getSenderId() {
        return senderId;
    }

    /**
     * 
     * @param senderId
     *     The senderId
     */
    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    /**
     * 
     * @return
     *     The messageId
     */
    public String getMessageId() {
        return messageId;
    }

    /**
     * 
     * @param messageId
     *     The messageId
     */
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    /**
     * 
     * @return
     *     The senderName
     */
    public String getSenderName() {
        return senderName;
    }

    /**
     * 
     * @param senderName
     *     The senderName
     */
    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    /**
     * 
     * @return
     *     The id
     */
    public String getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The name
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return
     *     The lastactivity
     */
    public String getLastactivity() {
        return lastactivity;
    }

    /**
     * 
     * @param lastactivity
     *     The lastactivity
     */
    public void setLastactivity(String lastactivity) {
        this.lastactivity = lastactivity;
    }

    /**
     * 
     * @return
     *     The createdby
     */
    public String getCreatedby() {
        return createdby;
    }

    /**
     * 
     * @param createdby
     *     The createdby
     */
    public void setCreatedby(String createdby) {
        this.createdby = createdby;
    }

    /**
     * 
     * @return
     *     The password
     */
    public String getPassword() {
        return password;
    }

    /**
     * 
     * @param password
     *     The password
     */
    public void setPassword(String password) {
        this.password = password;
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

    /**
     * 
     * @return
     *     The invitedusers
     */
    public String getInvitedusers() {
        return invitedusers;
    }

    /**
     * 
     * @param invitedusers
     *     The invitedusers
     */
    public void setInvitedusers(String invitedusers) {
        this.invitedusers = invitedusers;
    }

    /**
     * 
     * @return
     *     The eventId
     */
    public String getEventId() {
        return eventId;
    }

    /**
     * 
     * @param eventId
     *     The event_id
     */
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    /**
     * 
     * @return
     *     The title
     */
    public String getTitle() {
        return title;
    }

    /**
     * 
     * @param title
     *     The title
     */
    public void setTitle(String title) {
        this.title = title;
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
     *     The eventType
     */
    public String getEventType() {
        return eventType;
    }

    /**
     * 
     * @param eventType
     *     The event_type
     */
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    /**
     * 
     * @return
     *     The startLocation
     */
    public String getStartLocation() {
        return startLocation;
    }

    /**
     * 
     * @param startLocation
     *     The start_location
     */
    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    /**
     * 
     * @return
     *     The destinationLocation
     */
    public String getDestinationLocation() {
        return destinationLocation;
    }

    /**
     * 
     * @param destinationLocation
     *     The destination_location
     */
    public void setDestinationLocation(String destinationLocation) {
        this.destinationLocation = destinationLocation;
    }

    /**
     * 
     * @return
     *     The startTime
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * 
     * @param startTime
     *     The start_time
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
     * 
     * @return
     *     The image
     */
    public String getImage() {
        return image;
    }

    /**
     * 
     * @param image
     *     The image
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * 
     * @return
     *     The dateCreated
     */
    public String getDateCreated() {
        return dateCreated;
    }

    /**
     * 
     * @param dateCreated
     *     The date_created
     */
    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    /**
     * 
     * @return
     *     The baseLat
     */
    public String getBaseLat() {
        return baseLat;
    }

    /**
     * 
     * @param baseLat
     *     The base_lat
     */
    public void setBaseLat(String baseLat) {
        this.baseLat = baseLat;
    }

    /**
     * 
     * @return
     *     The baseLong
     */
    public String getBaseLong() {
        return baseLong;
    }

    /**
     * 
     * @param baseLong
     *     The base_long
     */
    public void setBaseLong(String baseLong) {
        this.baseLong = baseLong;
    }

    /**
     * 
     * @return
     *     The destLat
     */
    public String getDestLat() {
        return destLat;
    }

    /**
     * 
     * @param destLat
     *     The dest_lat
     */
    public void setDestLat(String destLat) {
        this.destLat = destLat;
    }

    /**
     * 
     * @return
     *     The destLong
     */
    public String getDestLong() {
        return destLong;
    }

    /**
     * 
     * @param destLong
     *     The dest_long
     */
    public void setDestLong(String destLong) {
        this.destLong = destLong;
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
     *     The assistance
     */
    public String getAssistance() {
        return assistance;
    }

    /**
     * 
     * @param assistance
     *     The assistance
     */
    public void setAssistance(String assistance) {
        this.assistance = assistance;
    }

    /**
     * 
     * @return
     *     The distance
     */
    public String getDistance() {
        return distance;
    }

    /**
     * 
     * @param distance
     *     The distance
     */
    public void setDistance(String distance) {
        this.distance = distance;
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
     *     The endDate
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * 
     * @param endDate
     *     The end_date
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    /**
     * 
     * @return
     *     The startDate
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * 
     * @param startDate
     *     The start_date
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    /**
     * 
     * @return
     *     The time
     */
    public String getTime() {
        return time;
    }

    /**
     * 
     * @param time
     *     The time
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * 
     * @return
     *     The countUser
     */
    public String getCountUser() {
        return countUser;
    }

    /**
     * 
     * @param countUser
     *     The countUser
     */
    public void setCountUser(String countUser) {
        this.countUser = countUser;
    }

    /**
     * 
     * @return
     *     The daysCount
     */
    public Integer getDaysCount() {
        return daysCount;
    }

    /**
     * 
     * @param daysCount
     *     The daysCount
     */
    public void setDaysCount(Integer daysCount) {
        this.daysCount = daysCount;
    }

}
