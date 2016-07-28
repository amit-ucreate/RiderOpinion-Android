
package com.nutsuser.ridersdomain.web.pojos;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FavVendor {

    @SerializedName("venderId")
    @Expose
    private String venderId;
    @SerializedName("company")
    @Expose
    private String company;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("videoCode")
    @Expose
    private String videoCode;
    @SerializedName("bike_id")
    @Expose
    private List<String> bikeId = new ArrayList<String>();
    @SerializedName("productCount")
    @Expose
    private Integer productCount;
    @SerializedName("address")
    @Expose
    private String address;

    /**
     * 
     * @return
     *     The venderId
     */
    public String getVenderId() {
        return venderId;
    }

    /**
     * 
     * @param venderId
     *     The venderId
     */
    public void setVenderId(String venderId) {
        this.venderId = venderId;
    }

    /**
     * 
     * @return
     *     The company
     */
    public String getCompany() {
        return company;
    }

    /**
     * 
     * @param company
     *     The company
     */
    public void setCompany(String company) {
        this.company = company;
    }

    /**
     * 
     * @return
     *     The image
     */
    public String getImage() {
        return image;
    }

    /**
     * 
     * @param image
     *     The image
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * 
     * @return
     *     The description
     */
    public String getDescription() {
        return description;
    }

    /**
     * 
     * @param description
     *     The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 
     * @return
     *     The videoCode
     */
    public String getVideoCode() {
        return videoCode;
    }

    /**
     * 
     * @param videoCode
     *     The videoCode
     */
    public void setVideoCode(String videoCode) {
        this.videoCode = videoCode;
    }

    /**
     * 
     * @return
     *     The bikeId
     */
    public List<String> getBikeId() {
        return bikeId;
    }

    /**
     * 
     * @param bikeId
     *     The bike_id
     */
    public void setBikeId(List<String> bikeId) {
        this.bikeId = bikeId;
    }

    /**
     * 
     * @return
     *     The productCount
     */
    public Integer getProductCount() {
        return productCount;
    }

    /**
     * 
     * @param productCount
     *     The productCount
     */
    public void setProductCount(Integer productCount) {
        this.productCount = productCount;
    }

    /**
     * 
     * @return
     *     The address
     */
    public String getAddress() {
        return address;
    }

    /**
     * 
     * @param address
     *     The address
     */
    public void setAddress(String address) {
        this.address = address;
    }

}
