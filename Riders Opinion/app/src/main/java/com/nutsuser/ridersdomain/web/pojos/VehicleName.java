package com.nutsuser.ridersdomain.web.pojos;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 19-01-2016.
 */
public class VehicleName {

    String success;
    String message;
    private List<VehicleDetails> data = new ArrayList<VehicleDetails>();

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

    /**
     * @return The data
     */
    public List<VehicleDetails> getData() {
        return data;
    }

    /**
     * @param data The data
     */
    public void setData(List<VehicleDetails> data) {
        this.data = data;
    }

}
