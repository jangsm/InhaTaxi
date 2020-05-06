package com.inhataxi.model;

import com.google.gson.annotations.SerializedName;

public class TestData {
    @SerializedName("no")
    private int no;

    @SerializedName("name")
    private String name;

    @SerializedName("registeredTimestamp")
    private String registeredTimestamp;

    public String getRegisteredTimestamp() {
        return registeredTimestamp;
    }

    public int getNo() {
        return no;
    }

    public String getName() {
        return name;
    }
}
