package com.nutsuser.ridersdomain.web.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 17-02-2016.
 */
public class PlanARideData {
    String eventId;
    String baseLocation;
    String destLocation;
    String startDate;
    String endDate;
    String startTime;
    String baseLat;
    String baseLong;
    String daysCount;
    String riders;
    String mutual;
    @SerializedName("halts")
    @Expose
    private List<Halt> halts = new ArrayList<Halt>();
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

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getBaseLocation() {
        return baseLocation;
    }

    public void setBaseLocation(String baseLocation) {
        this.baseLocation = baseLocation;
    }

    public String getDestLocation() {
        return destLocation;
    }

    public void setDestLocation(String destLocation) {
        this.destLocation = destLocation;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getBaseLat() {
        return baseLat;
    }

    public void setBaseLat(String baseLat) {
        this.baseLat = baseLat;
    }

    public String getBaseLong() {
        return baseLong;
    }

    public void setBaseLong(String baseLong) {
        this.baseLong = baseLong;
    }

    public String getDaysCount() {
        return daysCount;
    }

    public void setDaysCount(String daysCount) {
        this.daysCount = daysCount;
    }


    public String getRiders() {
        return riders;
    }

    public void setRiders(String riders) {
        this.riders = riders;
    }

    public String getMutual() {
        return mutual;
    }

    public void setMutual(String mutual) {
        this.mutual = mutual;
    }
}
