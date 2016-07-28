
package com.nutsuser.ridersdomain.web.pojos;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class RecentChatListData{

    @SerializedName("time")
    @Expose
    private String time;

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("from")
    @Expose
    private String from;
    @SerializedName("to")
    @Expose
    private String to;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("sent")
    @Expose
    private String sent;
    @SerializedName("read")
    @Expose
    private String read;
    @SerializedName("direction")
    @Expose
    private String direction;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("profile_name")
    @Expose
    private String profileName;
    @SerializedName("profile_image")
    @Expose
    private String profileImage;
    @SerializedName("msgcount")
    @Expose
    private String msgcount;

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
     *     The from
     */
    public String getFrom() {
        return from;
    }

    /**
     * 
     * @param from
     *     The from
     */
    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * 
     * @return
     *     The to
     */
    public String getTo() {
        return to;
    }

    /**
     * 
     * @param to
     *     The to
     */
    public void setTo(String to) {
        this.to = to;
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
     *     The read
     */
    public String getRead() {
        return read;
    }

    /**
     * 
     * @param read
     *     The read
     */
    public void setRead(String read) {
        this.read = read;
    }

    /**
     * 
     * @return
     *     The direction
     */
    public String getDirection() {
        return direction;
    }

    /**
     * 
     * @param direction
     *     The direction
     */
    public void setDirection(String direction) {
        this.direction = direction;
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
     *     The profile_name
     */
    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    /**
     * 
     * @return
     *     The profileImage
     */
    public String getProfileImage() {
        return profileImage;
    }

    /**
     * 
     * @param profileImage
     *     The profile_image
     */
    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    /**
     * 
     * @return
     *     The msgcount
     */
    public String getMsgcount() {
        return msgcount;
    }

    /**
     * 
     * @param msgcount
     *     The msgcount
     */
    public void setMsgcount(String msgcount) {
        this.msgcount = msgcount;
    }

}
