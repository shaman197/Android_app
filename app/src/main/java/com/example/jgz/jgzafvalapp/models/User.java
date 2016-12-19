package com.example.jgz.jgzafvalapp.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Boy on 13-12-2016.
 */

public class User {

    @SerializedName("id")
    public String id;
    @SerializedName("name")
    public String name;
    @SerializedName("email")
    public String email;
    @SerializedName("dateofbirth")
    public String birthDate;
    @SerializedName("length")
    public int length;
    @SerializedName("gender")
    public String gender;
    @SerializedName("weight")
    public int weight;

    public User(String id, String name, String email, String birthDate, int length, String gender, int weight) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.birthDate = birthDate;
        this.length = length;
        this.gender = gender;
        this.weight = weight;
    }
}
