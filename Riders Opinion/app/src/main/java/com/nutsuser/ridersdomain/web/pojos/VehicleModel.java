package com.nutsuser.ridersdomain.web.pojos;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 19-01-2016.
 */
public class VehicleModel {

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

    public List<VehicleModelDetails> getData() {
        return data;
    }

    public void setData(List<VehicleModelDetails> data) {
        this.data = data;
    }

    private List<VehicleModelDetails> data = new ArrayList<VehicleModelDetails>();
}
