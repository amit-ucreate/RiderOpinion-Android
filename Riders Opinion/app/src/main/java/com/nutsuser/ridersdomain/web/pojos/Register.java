package com.nutsuser.ridersdomain.web.pojos;

/**
 * Created by admin on 19-01-2016.
 */
public class Register {

    String success;
    String message;
    private RegisterDetails data;

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

    public RegisterDetails getData() {
        return data;
    }

    public void setData(RegisterDetails data) {
        this.data = data;
    }
}
