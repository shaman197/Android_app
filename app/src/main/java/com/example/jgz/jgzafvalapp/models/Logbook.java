package com.example.jgz.jgzafvalapp.models;

import com.example.jgz.jgzafvalapp.models.LogItem;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * Created by Boy on 17-11-2016.
 */

public class Logbook {
    @SerializedName("id")
    public String id;
    @SerializedName("userId")
    public String userId;
    @SerializedName("mood")
    public String mood;
    @SerializedName("date")
    public String date;

    public Logbook(String id, String userId, String mood, String date) {
        this.id = id;
        this.userId = userId;
        this.mood = mood;
        this.date = date;
    }
}
