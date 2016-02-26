package com.nutsuser.ridersdomain.web.pojos;

import java.util.ArrayList;

/**
 * Created by admin on 19-02-2016.
 */
public class PlanRideDetailsData {
    String eventId;
    String baseLocation;
    String destLocation;
    String startDate;
    String endDate;
    String startTime;
    String baseLat;
    String baseLong;
    String destLat;
    String destLong;
    String hostedBy;
    String description;
    int isVehId;
    int isJoin;

    public int getIsVehId() {
        return isVehId;
    }

    public void setIsVehId(int isVehId) {
        this.isVehId = isVehId;
    }

    public int getIsJoin() {
        return isJoin;
    }

    public void setIsJoin(int isJoin) {
        this.isJoin = isJoin;
    }

    String daysCount;
    String restaurant;
    String petrolpumps;
    String serviceStation;

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

    public String getDestLat() {
        return destLat;
    }

    public void setDestLat(String destLat) {
        this.destLat = destLat;
    }

    public String getDestLong() {
        return destLong;
    }

    public void setDestLong(String destLong) {
        this.destLong = destLong;
    }

    public String getHostedBy() {
        return hostedBy;
    }

    public void setHostedBy(String hostedBy) {
        this.hostedBy = hostedBy;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    public String getDaysCount() {
        return daysCount;
    }

    public void setDaysCount(String daysCount) {
        this.daysCount = daysCount;
    }

    public String getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(String restaurant) {
        this.restaurant = restaurant;
    }

    public String getPetrolpumps() {
        return petrolpumps;
    }

    public void setPetrolpumps(String petrolpumps) {
        this.petrolpumps = petrolpumps;
    }

    public String getServiceStation() {
        return serviceStation;
    }

    public void setServiceStation(String serviceStation) {
        this.serviceStation = serviceStation;
    }

    public String getHospitals() {
        return hospitals;
    }

    public void setHospitals(String hospitals) {
        this.hospitals = hospitals;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ArrayList<PlanRideDetailsDatahalts> getHalts() {
        return halts;
    }

    public void setHalts(ArrayList<PlanRideDetailsDatahalts> halts) {
        this.halts = halts;
    }

    String hospitals;
    String riders;
    String mutual;
    String image;
ArrayList<PlanRideDetailsDatahalts>halts=new ArrayList<>();

}

