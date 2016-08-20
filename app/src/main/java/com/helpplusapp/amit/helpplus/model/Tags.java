package com.helpplusapp.amit.helpplus.model;

import java.util.HashMap;

/**
 * Created by amit on 7/31/2016.
 */
public class Tags {
    private String userid;
    private String tagname;
    private HashMap<String, Object> timestampCreated;

    public Tags() {
    }

    public Tags(String userid, String tagname, HashMap<String, Object> timestampCreated) {
        this.userid = userid;
        this.tagname = tagname;
        this.timestampCreated = timestampCreated;
    }

    public String getUserid() {
        return userid;
    }

    public String getTagname() {
        return tagname;
    }

    public HashMap<String, Object> getTimestampCreated() {
        return timestampCreated;
    }
}
