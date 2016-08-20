package com.helpplusapp.amit.helpplus.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.HashMap;

/**
 * Created by amit on 8/8/2016.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class HomeWidgetModel {
    private String home_content;
    private HashMap<String,Object> timecreated;

    public HomeWidgetModel() {
    }

    public HomeWidgetModel(String home_content, HashMap<String, Object> timecreated) {
        this.home_content = home_content;
        this.timecreated = timecreated;
    }

    public String getHome_content() {
        return home_content;
    }

    public HashMap<String, Object> getTimecreated() {
        return timecreated;
    }
}
