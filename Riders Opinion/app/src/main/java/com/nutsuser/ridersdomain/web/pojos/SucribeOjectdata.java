package com.nutsuser.ridersdomain.web.pojos;

import java.util.ArrayList;

/**
 * Created by admin on 15-02-2016.
 */
public class SucribeOjectdata {
    String destId;
    String destName;
    String videoUrl;
    String rating;
    String favroite;

    String destLongitude;
    String destLatitude;
    ArrayList<SubcribesImages> images = new ArrayList<>();

    public String getDestId() {
        return destId;
    }

    public void setDestId(String destId) {
        this.destId = destId;
    }

    public String getDestName() {
        return destName;
    }

    public void setDestName(String destName) {
        this.destName = destName;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getFavroite() {
        return favroite;
    }

    public void setFavroite(String favroite) {
        this.favroite = favroite;
    }

    public String getDestLongitude() {
        return destLongitude;
    }

    public void setDestLongitude(String destLongitude) {
        this.destLongitude = destLongitude;
    }

    public String getDestLatitude() {
        return destLatitude;
    }

    public void setDestLatitude(String destLatitude) {
        this.destLatitude = destLatitude;
    }

    public ArrayList<SubcribesImages> getImages() {
        return images;
    }

    public void setImages(ArrayList<SubcribesImages> images) {
        this.images = images;
    }
}

