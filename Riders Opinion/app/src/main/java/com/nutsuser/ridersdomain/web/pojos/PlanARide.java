package com.nutsuser.ridersdomain.web.pojos;

import java.util.ArrayList;

/**
 * Created by admin on 17-02-2016.
 */
public class PlanARide {
    String success;
    String message;
ArrayList<PlanARideData> data=new ArrayList<>();

    public ArrayList<PlanARideData> getData() {
        return data;
    }

    public void setData(ArrayList<PlanARideData> data) {
        this.data = data;
    }

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
}
