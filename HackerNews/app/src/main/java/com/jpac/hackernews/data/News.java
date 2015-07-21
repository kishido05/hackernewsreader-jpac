package com.jpac.hackernews.data;

import java.io.Serializable;
import java.util.Comparator;

public class News implements Serializable, Comparator<News> {

    protected String by;
    protected String id;
    protected String score;
    protected String text;
    protected String time;
    protected String title;
    protected String type;
    protected String url;
    protected String[] kids;

    public String getBy() {
        return by;
    }

    public String getId() {
        return id;
    }

    public String getScore() {
        return score;
    }

    public String getText() {
        return text;
    }

    public String getTime() {
        return time;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }

    public String[] getKids() {
        return kids;
    }

    @Override
    public int compare(News n1, News n2) {
        return n2.getTime().compareTo(n1.getTime());
    }
}
