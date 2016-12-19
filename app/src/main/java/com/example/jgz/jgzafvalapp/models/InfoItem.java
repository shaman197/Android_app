package com.example.jgz.jgzafvalapp.models;

/**
 * Created by Boy on 4-12-2016.
 */
public class InfoItem {

    public String id;
    public String title;
    public String summary;
    public String imageUrl;
    public String url;

    public InfoItem(String id, String title, String summary, String imageUrl, String url) {
        this.id = id;
        this.title = title;
        this.summary = summary;
        this.imageUrl = imageUrl;
        this.url = url;
    }
}
