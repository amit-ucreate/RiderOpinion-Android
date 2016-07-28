package com.nutsuser.ridersdomain.web.pojos;

/**
 * Created by admin on 15-02-2016.
 */
public class SubcribeGridData {

    String success;
    String message;
    SucribeGridOjectdata data;

    public SucribeGridOjectdata getData() {
        return data;
    }

    public void setData(SucribeGridOjectdata data) {
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
