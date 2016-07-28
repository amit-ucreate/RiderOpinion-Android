
package com.nutsuser.ridersdomain.web.pojos;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class FavouriteDestinationData {

    @SerializedName("destId")
    @Expose
    private String destId;
    @SerializedName("destName")
    @Expose
    private String destName;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("videoUrl")
    @Expose
    private String videoUrl;
    @SerializedName("rating")
    @Expose
    private Float rating;
    @SerializedName("favroite")
    @Expose
    private Integer favroite;
    @SerializedName("destLongitude")
    @Expose
    private String destLongitude;
    @SerializedName("destLatitude")
    @Expose
    private String destLatitude;
    @SerializedName("restaurant")
    @Expose
    private String restaurant;
    @SerializedName("petrolpumps")
    @Expose
    private String petrolpumps;
    @SerializedName("serviceStation")
    @Expose
    private String serviceStation;
    @SerializedName("hospitals")
    @Expose
    private String hospitals;
    @SerializedName("riders")
    @Expose
    private Integer riders;
    @SerializedName("offers")
    @Expose
    private String offers;
    @SerializedName("images")
    @Expose
    private String images;

    /**
     * 
     * @return
     *     The destId
     */
    public String getDestId() {
        return destId;
    }

    /**
     * 
     * @param destId
     *     The destId
     */
    public void setDestId(String destId) {
        this.destId = destId;
    }

    /**
     * 
     * @return
     *     The destName
     */
    public String getDestName() {
        return destName;
    }

    /**
     * 
     * @param destName
     *     The destName
     */
    public void setDestName(String destName) {
        this.destName = destName;
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
     *     The videoUrl
     */
    public String getVideoUrl() {
        return videoUrl;
    }

    /**
     * 
     * @param videoUrl
     *     The videoUrl
     */
    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    /**
     * 
     * @return
     *     The rating
     */
    public Float getRating() {
        return rating;
    }

    /**
     * 
     * @param rating
     *     The rating
     */
    public void setRating(Float rating) {
        this.rating = rating;
    }

    /**
     * 
     * @return
     *     The favroite
     */
    public Integer getFavroite() {
        return favroite;
    }

    /**
     * 
     * @param favroite
     *     The favroite
     */
    public void setFavroite(Integer favroite) {
        this.favroite = favroite;
    }

    /**
     * 
     * @return
     *     The destLongitude
     */
    public String getDestLongitude() {
        return destLongitude;
    }

    /**
     * 
     * @param destLongitude
     *     The destLongitude
     */
    public void setDestLongitude(String destLongitude) {
        this.destLongitude = destLongitude;
    }

    /**
     * 
     * @return
     *     The destLatitude
     */
    public String getDestLatitude() {
        return destLatitude;
    }

    /**
     * 
     * @param destLatitude
     *     The destLatitude
     */
    public void setDestLatitude(String destLatitude) {
        this.destLatitude = destLatitude;
    }

    /**
     * 
     * @return
     *     The restaurant
     */
    public String getRestaurant() {
        return restaurant;
    }

    /**
     * 
     * @param restaurant
     *     The restaurant
     */
    public void setRestaurant(String restaurant) {
        this.restaurant = restaurant;
    }

    /**
     * 
     * @return
     *     The petrolpumps
     */
    public String getPetrolpumps() {
        return petrolpumps;
    }

    /**
     * 
     * @param petrolpumps
     *     The petrolpumps
     */
    public void setPetrolpumps(String petrolpumps) {
        this.petrolpumps = petrolpumps;
    }

    /**
     * 
     * @return
     *     The serviceStation
     */
    public String getServiceStation() {
        return serviceStation;
    }

    /**
     * 
     * @param serviceStation
     *     The serviceStation
     */
    public void setServiceStation(String serviceStation) {
        this.serviceStation = serviceStation;
    }

    /**
     * 
     * @return
     *     The hospitals
     */
    public String getHospitals() {
        return hospitals;
    }

    /**
     * 
     * @param hospitals
     *     The hospitals
     */
    public void setHospitals(String hospitals) {
        this.hospitals = hospitals;
    }

    /**
     * 
     * @return
     *     The riders
     */
    public Integer getRiders() {
        return riders;
    }

    /**
     * 
     * @param riders
     *     The riders
     */
    public void setRiders(Integer riders) {
        this.riders = riders;
    }

    /**
     * 
     * @return
     *     The offers
     */
    public String getOffers() {
        return offers;
    }

    /**
     * 
     * @param offers
     *     The offers
     */
    public void setOffers(String offers) {
        this.offers = offers;
    }

    /**
     * 
     * @return
     *     The images
     */
    public String getImages() {
        return images;
    }

    /**
     * 
     * @param images
     *     The images
     */
    public void setImages(String images) {
        this.images = images;
    }

}
