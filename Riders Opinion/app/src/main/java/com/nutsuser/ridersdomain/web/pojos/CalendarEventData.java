
package com.nutsuser.ridersdomain.web.pojos;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class CalendarEventData {

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
    @SerializedName("daysCount")
    @Expose
    private Integer daysCount;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("halts")
    @Expose
    private List<Halt> halts = new ArrayList<Halt>();
    @SerializedName("start")
    @Expose
    private String start;
    @SerializedName("end")
    @Expose
    private String end;
    @SerializedName("class")
    @Expose
    private String _class;
    @SerializedName("riders")
    @Expose
    private Integer riders;
    @SerializedName("mutual")
    @Expose
    private Integer mutual;

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
     *     The start
     */
    public String getStart() {
        return start;
    }

    /**
     * 
     * @param start
     *     The start
     */
    public void setStart(String start) {
        this.start = start;
    }

    /**
     * 
     * @return
     *     The end
     */
    public String getEnd() {
        return end;
    }

    /**
     * 
     * @param end
     *     The end
     */
    public void setEnd(String end) {
        this.end = end;
    }

    /**
     * 
     * @return
     *     The _class
     */
    public String getClass_() {
        return _class;
    }

    /**
     * 
     * @param _class
     *     The class
     */
    public void setClass_(String _class) {
        this._class = _class;
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

}
