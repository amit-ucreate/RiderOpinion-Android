
package com.nutsuser.ridersdomain.web.pojos;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class NotificationData {

    @SerializedName("current")
    @Expose
    private List<Current> current = new ArrayList<Current>();
    @SerializedName("previous")
    @Expose
    private List<Previou> previous = new ArrayList<Previou>();
    @SerializedName("older")
    @Expose
    private List<Older> older = new ArrayList<Older>();

    /**
     * 
     * @return
     *     The current
     */
    public List<Current> getCurrent() {
        return current;
    }

    /**
     * 
     * @param current
     *     The current
     */
    public void setCurrent(List<Current> current) {
        this.current = current;
    }

    /**
     * 
     * @return
     *     The previous
     */
    public List<Previou> getPrevious() {
        return previous;
    }

    /**
     * 
     * @param previous
     *     The previous
     */
    public void setPrevious(List<Previou> previous) {
        this.previous = previous;
    }

    /**
     * 
     * @return
     *     The older
     */
    public List<Older> getOlder() {
        return older;
    }

    /**
     * 
     * @param older
     *     The older
     */
    public void setOlder(List<Older> older) {
        this.older = older;
    }

}
