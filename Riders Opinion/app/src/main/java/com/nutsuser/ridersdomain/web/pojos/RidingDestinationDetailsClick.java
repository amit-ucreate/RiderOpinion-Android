package com.nutsuser.ridersdomain.web.pojos;

/**
 * Created by admin on 22-01-2016.
 */
public class RidingDestinationDetailsClick {

    String success;

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



    String message;
    RidingDestinationDetailsClickInfo data;

    public RidingDestinationDetailsClickInfo getData() {
        return data;
    }

    public void setData(RidingDestinationDetailsClickInfo data) {
        this.data = data;
    }
}
