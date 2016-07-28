package com.nutsuser.ridersdomain.web.pojos;

import java.util.ArrayList;

/**
 * Created by admin on 17-02-2016.
 */
public class PlanARide {
    int success;
    String message;
    ArrayList<PlanARideData> data = new ArrayList<>();

    public ArrayList<PlanARideData> getData() {
        return data;
    }

    public void setData(ArrayList<PlanARideData> data) {
        this.data = data;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
