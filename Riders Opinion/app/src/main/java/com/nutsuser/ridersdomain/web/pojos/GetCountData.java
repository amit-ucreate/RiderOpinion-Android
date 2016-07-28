
package com.nutsuser.ridersdomain.web.pojos;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class GetCountData {

    @SerializedName("gasStation")
    @Expose
    private String gasStation;
    @SerializedName("resturants")
    @Expose
    private String resturants;
    @SerializedName("doctor")
    @Expose
    private String doctor;
    @SerializedName("repair")
    @Expose
    private String repair;

    /**
     * 
     * @return
     *     The gasStation
     */
    public String getGasStation() {
        return gasStation;
    }

    /**
     * 
     * @param gasStation
     *     The gasStation
     */
    public void setGasStation(String gasStation) {
        this.gasStation = gasStation;
    }

    /**
     * 
     * @return
     *     The resturants
     */
    public String getResturants() {
        return resturants;
    }

    /**
     * 
     * @param resturants
     *     The resturants
     */
    public void setResturants(String resturants) {
        this.resturants = resturants;
    }

    /**
     * 
     * @return
     *     The doctor
     */
    public String getDoctor() {
        return doctor;
    }

    /**
     * 
     * @param doctor
     *     The doctor
     */
    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    /**
     * 
     * @return
     *     The repair
     */
    public String getRepair() {
        return repair;
    }

    /**
     * 
     * @param repair
     *     The repair
     */
    public void setRepair(String repair) {
        this.repair = repair;
    }

}
