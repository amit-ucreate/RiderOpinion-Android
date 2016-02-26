package com.nutsuser.ridersdomain.web.pojos;

import java.util.ArrayList;

/**
 * Created by admin on 23-02-2016.
 */
public class RidingEvent {
    String success;
    String message;

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

    public ArrayList<RidingEventData> getData() {
        return data;
    }

    public void setData(ArrayList<RidingEventData> data) {
        this.data = data;
    }

    ArrayList<RidingEventData> data=new ArrayList<>();
}
