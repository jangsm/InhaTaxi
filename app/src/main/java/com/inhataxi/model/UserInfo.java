package com.inhataxi.model;

import com.google.gson.annotations.SerializedName;

public class UserInfo {

    @SerializedName("userNo")
    private int userNo;

    @SerializedName("name")
    private String name;

    public String getName() {
        return name;
    }

    public int getUserNo() {
        return userNo;
    }

}
