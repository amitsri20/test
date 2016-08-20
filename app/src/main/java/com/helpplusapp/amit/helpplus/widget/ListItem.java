package com.helpplusapp.amit.helpplus.widget;

/**
 * Created by amit on 8/9/2016.
 */
public class ListItem {
    public String heading,content;

    public ListItem() {
    }

    public ListItem(String heading, String content) {
        this.heading = heading;
        this.content = content;
    }

    public String getHeading() {
        return heading;
    }

    public String getContent() {
        return content;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
