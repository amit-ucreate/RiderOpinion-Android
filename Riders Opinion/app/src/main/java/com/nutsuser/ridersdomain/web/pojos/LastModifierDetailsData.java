
package com.nutsuser.ridersdomain.web.pojos;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class LastModifierDetailsData {

    @SerializedName("venderId")
    @Expose
    private String venderId;
    @SerializedName("company")
    @Expose
    private String company;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("isFavroite")
    @Expose
    private Integer isFavroite;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("videoCode")
    @Expose
    private String videoCode;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("start_time")
    @Expose
    private String startTime;
    @SerializedName("end_time")
    @Expose
    private String endTime;
    @SerializedName("bikeId")
    @Expose
    private String bikeId;
    @SerializedName("offerCount")
    @Expose
    private Integer offerCount;
    @SerializedName("allProductsCount")
    @Expose
    private Integer allProductsCount;
    @SerializedName("products")
    @Expose
    private List<Product> products = new ArrayList<Product>();

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
     *     The isFavroite
     */
    public Integer getIsFavroite() {
        return isFavroite;
    }

    /**
     * 
     * @param isFavroite
     *     The isFavroite
     */
    public void setIsFavroite(Integer isFavroite) {
        this.isFavroite = isFavroite;
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

    /**
     * 
     * @return
     *     The phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 
     * @param phone
     *     The phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * 
     * @return
     *     The startTime
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * 
     * @param startTime
     *     The start_time
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
     * 
     * @return
     *     The endTime
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * 
     * @param endTime
     *     The end_time
     */
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    /**
     * 
     * @return
     *     The bikeId
     */
    public String getBikeId() {
        return bikeId;
    }

    /**
     * 
     * @param bikeId
     *     The bikeId
     */
    public void setBikeId(String bikeId) {
        this.bikeId = bikeId;
    }

    /**
     * 
     * @return
     *     The offerCount
     */
    public Integer getOfferCount() {
        return offerCount;
    }

    /**
     * 
     * @param offerCount
     *     The offerCount
     */
    public void setOfferCount(Integer offerCount) {
        this.offerCount = offerCount;
    }

    /**
     * 
     * @return
     *     The allProductsCount
     */
    public Integer getAllProductsCount() {
        return allProductsCount;
    }

    /**
     * 
     * @param allProductsCount
     *     The allProductsCount
     */
    public void setAllProductsCount(Integer allProductsCount) {
        this.allProductsCount = allProductsCount;
    }

    /**
     * 
     * @return
     *     The products
     */
    public List<Product> getProducts() {
        return products;
    }

    /**
     * 
     * @param products
     *     The products
     */
    public void setProducts(List<Product> products) {
        this.products = products;
    }

}
