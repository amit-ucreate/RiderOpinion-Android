
package com.nutsuser.ridersdomain.web.pojos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class ModilyBike {

    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("data")
    @Expose
    private List<ModiflyBikeData> data = new ArrayList<ModiflyBikeData>();
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("bikeId")
    @Expose
    private String bikeId;
    @SerializedName("vehicle")
    @Expose
    private String vehicle;

    @SerializedName("vehicle_type")
    @Expose
    private String vehicle_type;

    @SerializedName("vehicle_year")
    @Expose
    private String vehicle_year;



    /**
     * @return The success
     */
    public Integer getSuccess() {
        return success;
    }

    /**
     * @param success The success
     */
    public void setSuccess(Integer success) {
        this.success = success;
    }

    /**
     * @return The data
     */
    public List<ModiflyBikeData> getData() {
        return data;
    }

    /**
     * @param data The data
     */
    public void setData(List<ModiflyBikeData> data) {
        this.data = data;
    }

    /**
     * @return vehicle_type
     */
    public String getVehicle_type() {
        return vehicle_type;
    }

    /**
     * @param vehicle_type vehicle_type
     */
    public void setVehicle_type(String vehicle_type) {
        this.vehicle_type = vehicle_type;
    }


    /**
     * @return vehicle_year
     */
    public String getVehicle_year() {
        return vehicle_year;
    }

    /**
     * @param vehicle_year vehicle_year
     */
    public void setVehicle_year(String vehicle_year) {
        this.vehicle_year = vehicle_year;
    }


    /**
     * @return vehicle
     */
    public String getvehicle() {
        return vehicle;
    }

    /**
     * @param vehicle vehicle
     */
    public void setvehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    /**
     * @return bikeId
     */
    public String getbikeId() {
        return bikeId;
    }

    /**
     * @param bikeId bikeId
     */
    public void setbikeId(String bikeId) {
        this.message = bikeId;
    }

    /**
     * @return The message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message The message
     */
    public void setMessage(String message) {
        this.message = message;
    }

}
