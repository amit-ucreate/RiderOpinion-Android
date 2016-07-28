
package com.nutsuser.ridersdomain.web.pojos;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FavouriteDestinationItem {

    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("data")
    @Expose
    private List<FavouriteDestinationData> data = new ArrayList<FavouriteDestinationData>();
    @SerializedName("favVendor")
    @Expose
    private List<FavVendor> favVendor = new ArrayList<FavVendor>();
    @SerializedName("message")
    @Expose
    private String message;

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

    /**
     * 
     * @return
     *     The data
     */
    public List<FavouriteDestinationData> getData() {
        return data;
    }

    /**
     * 
     * @param data
     *     The data
     */
    public void setData(List<FavouriteDestinationData> data) {
        this.data = data;
    }

    /**
     * 
     * @return
     *     The favVendor
     */
    public List<FavVendor> getFavVendor() {
        return favVendor;
    }

    /**
     * 
     * @param favVendor
     *     The favVendor
     */
    public void setFavVendor(List<FavVendor> favVendor) {
        this.favVendor = favVendor;
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

}
