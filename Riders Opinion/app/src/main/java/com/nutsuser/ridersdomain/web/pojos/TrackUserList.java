package com.nutsuser.ridersdomain.web.pojos;

import java.util.ArrayList;

/**
 * Created by admin on 24-02-2016.
 */
public class TrackUserList {
    String success;
    String message;
    ArrayList<TrackUserListData> data = new ArrayList<>();

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

    public ArrayList<TrackUserListData> getData() {
        return data;
    }

    public void setData(ArrayList<TrackUserListData> data) {
        this.data = data;
    }
}
