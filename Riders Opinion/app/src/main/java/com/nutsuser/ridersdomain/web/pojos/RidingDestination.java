package com.nutsuser.ridersdomain.web.pojos;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 20-01-2016.
 */
public class RidingDestination {
    String success;
    String message;
    private List<RidingDestinationDetails> data = new ArrayList<RidingDestinationDetails>();

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

    public List<RidingDestinationDetails> getData() {
        return data;
    }

    public void setData(List<RidingDestinationDetails> data) {
        this.data = data;
    }
}
