package com.nutsuser.ridersdomain.web.pojos;

/**
 * Created by admin on 19-02-2016.
 */
public class PlanRideDetails {
    String success;
    String message;
    PlanRideDetailsData data;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public PlanRideDetailsData getData() {
        return data;
    }

    public void setData(PlanRideDetailsData data) {
        this.data = data;
    }
}
