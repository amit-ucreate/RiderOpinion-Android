
package com.nutsuser.ridersdomain.web.pojos;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Halt {

    @SerializedName("halt")
    @Expose
    private String halt;
    @SerializedName("haltLocation")
    @Expose
    private String haltLocation;

    /**
     * 
     * @return
     *     The halt
     */
    public String getHalt() {
        return halt;
    }

    /**
     * 
     * @param halt
     *     The halt
     */
    public void setHalt(String halt) {
        this.halt = halt;
    }

    /**
     * 
     * @return
     *     The haltLocation
     */
    public String getHaltLocation() {
        return haltLocation;
    }

    /**
     * 
     * @param haltLocation
     *     The haltLocation
     */
    public void setHaltLocation(String haltLocation) {
        this.haltLocation = haltLocation;
    }

}
