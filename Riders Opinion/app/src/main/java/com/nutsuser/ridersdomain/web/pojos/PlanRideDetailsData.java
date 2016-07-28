
package com.nutsuser.ridersdomain.web.pojos;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class PlanRideDetailsData {

    @SerializedName("eventId")
    @Expose
    private String eventId;
    @SerializedName("baseLocation")
    @Expose
    private String baseLocation;
    @SerializedName("destLocation")
    @Expose
    private String destLocation;
    @SerializedName("startDate")
    @Expose
    private String startDate;
    @SerializedName("endDate")
    @Expose
    private String endDate;
    @SerializedName("startTime")
    @Expose
    private String startTime;
    @SerializedName("baseLat")
    @Expose
    private String baseLat;
    @SerializedName("baseLong")
    @Expose
    private String baseLong;
    @SerializedName("destLat")
    @Expose
    private String destLat;
    @SerializedName("destLong")
    @Expose
    private String destLong;
    @SerializedName("hostedBy")
    @Expose
    private String hostedBy;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("isVehId")
    @Expose
    private Integer isVehId;
    @SerializedName("isJoin")
    @Expose
    private Integer isJoin;
    @SerializedName("canTrack")
    @Expose
    private Integer canTrack;
    @SerializedName("daysCount")
    @Expose
    private Integer daysCount;
    @SerializedName("restaurant")
    @Expose
    private Integer restaurant;
    @SerializedName("petrolpumps")
    @Expose
    private Integer petrolpumps;
    @SerializedName("serviceStation")
    @Expose
    private Integer serviceStation;
    @SerializedName("hospitals")
    @Expose
    private Integer hospitals;
    @SerializedName("riders")
    @Expose
    private Integer riders;
    @SerializedName("mutual")
    @Expose
    private Integer mutual;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("halts")
    @Expose
    private List<Halt> halts = new ArrayList<Halt>();

    @SerializedName("room_id")
    @Expose
    private String roomId;
    @SerializedName("room_name")
    @Expose
    private String roomName;
    @SerializedName("room_password")
    @Expose
    private String roomPassword;

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
     *     The eventId
     */
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    /**
     * 
     * @return
     *     The baseLocation
     */
    public String getBaseLocation() {
        return baseLocation;
    }

    /**
     * 
     * @param baseLocation
     *     The baseLocation
     */
    public void setBaseLocation(String baseLocation) {
        this.baseLocation = baseLocation;
    }

    /**
     * 
     * @return
     *     The destLocation
     */
    public String getDestLocation() {
        return destLocation;
    }

    /**
     * 
     * @param destLocation
     *     The destLocation
     */
    public void setDestLocation(String destLocation) {
        this.destLocation = destLocation;
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
     *     The startDate
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
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
     *     The endDate
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
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
     *     The startTime
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
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
     *     The baseLat
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
     *     The baseLong
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
     *     The destLat
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
     *     The destLong
     */
    public void setDestLong(String destLong) {
        this.destLong = destLong;
    }

    /**
     * 
     * @return
     *     The hostedBy
     */
    public String getHostedBy() {
        return hostedBy;
    }

    /**
     * 
     * @param hostedBy
     *     The hostedBy
     */
    public void setHostedBy(String hostedBy) {
        this.hostedBy = hostedBy;
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
     *     The isVehId
     */
    public Integer getIsVehId() {
        return isVehId;
    }

    /**
     * 
     * @param isVehId
     *     The isVehId
     */
    public void setIsVehId(Integer isVehId) {
        this.isVehId = isVehId;
    }

    /**
     * 
     * @return
     *     The isJoin
     */
    public Integer getIsJoin() {
        return isJoin;
    }

    /**
     * 
     * @param isJoin
     *     The isJoin
     */
    public void setIsJoin(Integer isJoin) {
        this.isJoin = isJoin;
    }

    /**
     * 
     * @return
     *     The canTrack
     */
    public Integer getCanTrack() {
        return canTrack;
    }

    /**
     * 
     * @param canTrack
     *     The canTrack
     */
    public void setCanTrack(Integer canTrack) {
        this.canTrack = canTrack;
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

    /**
     * 
     * @return
     *     The restaurant
     */
    public Integer getRestaurant() {
        return restaurant;
    }

    /**
     * 
     * @param restaurant
     *     The restaurant
     */
    public void setRestaurant(Integer restaurant) {
        this.restaurant = restaurant;
    }

    /**
     * 
     * @return
     *     The petrolpumps
     */
    public Integer getPetrolpumps() {
        return petrolpumps;
    }

    /**
     * 
     * @param petrolpumps
     *     The petrolpumps
     */
    public void setPetrolpumps(Integer petrolpumps) {
        this.petrolpumps = petrolpumps;
    }

    /**
     * 
     * @return
     *     The serviceStation
     */
    public Integer getServiceStation() {
        return serviceStation;
    }

    /**
     * 
     * @param serviceStation
     *     The serviceStation
     */
    public void setServiceStation(Integer serviceStation) {
        this.serviceStation = serviceStation;
    }

    /**
     * 
     * @return
     *     The hospitals
     */
    public Integer getHospitals() {
        return hospitals;
    }

    /**
     * 
     * @param hospitals
     *     The hospitals
     */
    public void setHospitals(Integer hospitals) {
        this.hospitals = hospitals;
    }

    /**
     * 
     * @return
     *     The riders
     */
    public Integer getRiders() {
        return riders;
    }

    /**
     * 
     * @param riders
     *     The riders
     */
    public void setRiders(Integer riders) {
        this.riders = riders;
    }

    /**
     * 
     * @return
     *     The mutual
     */
    public Integer getMutual() {
        return mutual;
    }

    /**
     * 
     * @param mutual
     *     The mutual
     */
    public void setMutual(Integer mutual) {
        this.mutual = mutual;
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
     *     The halts
     */
    public List<Halt> getHalts() {
        return halts;
    }

    /**
     * 
     * @param halts
     *     The halts
     */
    public void setHalts(List<Halt> halts) {
        this.halts = halts;
    }


    /**
     *
     * @return
     *     The roomId
     */
    public String getRoomId() {
        return roomId;
    }

    /**
     *
     * @param roomId
     *     The room_id
     */
    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    /**
     *
     * @return
     *     The roomName
     */
    public String getRoomName() {
        return roomName;
    }

    /**
     *
     * @param roomName
     *     The room_name
     */
    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    /**
     *
     * @return
     *     The roomPassword
     */
    public String getRoomPassword() {
        return roomPassword;
    }

    /**
     *
     * @param roomPassword
     *     The room_password
     */
    public void setRoomPassword(String roomPassword) {
        this.roomPassword = roomPassword;
    }

}
