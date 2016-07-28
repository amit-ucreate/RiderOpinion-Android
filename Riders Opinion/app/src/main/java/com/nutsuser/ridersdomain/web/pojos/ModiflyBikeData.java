
package com.nutsuser.ridersdomain.web.pojos;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModiflyBikeData {

    @SerializedName("catId")
    @Expose
    private String catId;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("bikeId")
    @Expose
    private String bikeId;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("vender")
    @Expose
    private String vender;

    /**
     * 
     * @return
     *     The catId
     */
    public String getCatId() {
        return catId;
    }

    /**
     * 
     * @param catId
     *     The catId
     */
    public void setCatId(String catId) {
        this.catId = catId;
    }

    /**
     * 
     * @return
     *     The category
     */
    public String getCategory() {
        return category;
    }

    /**
     * 
     * @param category
     *     The category
     */
    public void setCategory(String category) {
        this.category = category;
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
     *     The vender
     */
    public String getVender() {
        return vender;
    }

    /**
     * 
     * @param vender
     *     The vender
     */
    public void setVender(String vender) {
        this.vender = vender;
    }

}
