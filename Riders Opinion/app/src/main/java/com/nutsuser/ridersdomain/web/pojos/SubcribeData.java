package com.nutsuser.ridersdomain.web.pojos;

/**
 * Created by admin on 15-02-2016.
 */
public class SubcribeData {

    String success;

    public SucribeOjectdata getData() {
        return data;
    }

    public void setData(SucribeOjectdata data) {
        this.data = data;
    }

    String message;
    SucribeOjectdata data;

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
