
package com.nutsuser.ridersdomain.web.pojos;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class ModiflyBikeFav {

    @SerializedName("fav")
    @Expose
    private Integer fav;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("success")
    @Expose
    private Integer success;

    /**
     * 
     * @return
     *     The fav
     */
    public Integer getFav() {
        return fav;
    }

    /**
     * 
     * @param fav
     *     The fav
     */
    public void setFav(Integer fav) {
        this.fav = fav;
    }

    /**
     * 
     * @return
     *     The message
     */
    public String getMessage() {
        return message;
    }

    /**
     * 
     * @param message
     *     The message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 
     * @return
     *     The success
     */
    public Integer getSuccess() {
        return success;
    }

    /**
     * 
     * @param success
     *     The success
     */
    public void setSuccess(Integer success) {
        this.success = success;
    }

}
