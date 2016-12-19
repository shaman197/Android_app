package com.example.jgz.jgzafvalapp.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Boy on 17-11-2016.
 */
public class LogItem {
    @SerializedName("logbookId")
    public String logbookId;
    @SerializedName("name")
    public String name;
    @SerializedName("category")
    public String category;
    @SerializedName("amount")
    public int amount;

    public LogItem(String logbookId, String name, String category, int amount) {
        this.logbookId = logbookId;
        this.name = name;
        this.category = category;
        this.amount = amount;
    }
}
