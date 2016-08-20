package com.helpplusapp.amit.helpplus.model;

import java.util.HashMap;

/**
 * Created by amit on 8/8/2016.
 */
public class HomeSampleContent {
    private String homeNotificationText;
    private String homeContent;
    private String actionType;
    private HashMap<String, Object> timestampCreated;



    public HomeSampleContent() {
    }

    public HomeSampleContent(String homeNotificationText, String homeContent, String actionType, HashMap<String, Object> timestampCreated) {
        this.homeNotificationText = homeNotificationText;
        this.homeContent = homeContent;
        this.actionType = actionType;
        this.timestampCreated = timestampCreated;
    }

    public String getHomeContent() {
        return homeContent;
    }

    public HashMap<String, Object> getTimestampCreated() {
        return timestampCreated;
    }

    public String getActionType() {
        return actionType;
    }

    public String getHomeNotificationText() {
        return homeNotificationText;
    }

    public void setTimestampCreated(HashMap<String, Object> timestampCreated) {
        this.timestampCreated = timestampCreated;
    }

    public void setHomeContent(String homeContent) {
        this.homeContent = homeContent;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public void setHomeNotificationText(String homeNotificationText) {
        this.homeNotificationText = homeNotificationText;
    }
}
