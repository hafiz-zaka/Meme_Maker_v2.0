package com.mememaker.mememakerpro.creatememe.model;

import android.os.Parcelable;

public class AdModel {
    private String adName;
    private String adId;

    public String getFacebookID() {
        return facebookID;
    }

    public void setFacebookID(String facebookID) {
        this.facebookID = facebookID;
    }

    private String facebookID;

    public AdModel(String adName, String adId, String activityName, String status,String facebookID) {
        this.adName = adName;
        this.adId = adId;
        this.activityName = activityName;
        this.status = status;
        this.facebookID=facebookID;
    }

    private String activityName;

    public AdModel() {
    }

    public String getAdName() {
        return adName;
    }

    public void setAdName(String adName) {
        this.adName = adName;
    }

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String status;

}
