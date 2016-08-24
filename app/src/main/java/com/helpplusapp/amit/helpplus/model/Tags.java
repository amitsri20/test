package com.helpplusapp.amit.helpplus.model;

import java.util.HashMap;

/**
 * Created by amit on 7/31/2016.
 */
public class Tags {
    private String tagname;
    private HashMap<String, Object> timestampCreated;

    public Tags() {
    }

    public Tags(String tagname, HashMap<String, Object> timestampCreated) {
        this.tagname = tagname;
        this.timestampCreated = timestampCreated;
    }

    public String getTagname() {
        return tagname;
    }

    public HashMap<String, Object> getTimestampCreated() {
        return timestampCreated;
    }
}
