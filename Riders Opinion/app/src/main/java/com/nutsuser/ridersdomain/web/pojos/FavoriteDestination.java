package com.nutsuser.ridersdomain.web.pojos;

/**
 * Created by admin on 22-01-2016.
 */


public class FavoriteDestination {
    String success;
    String fav;

    public String getFav() {
        return fav;
    }

    public void setFav(String fav) {
        this.fav = fav;
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

    String message;
}
