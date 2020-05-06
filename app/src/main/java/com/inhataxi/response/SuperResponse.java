package com.inhataxi.response;

import com.google.gson.annotations.SerializedName;

public class SuperResponse {

    @SerializedName("isSuccess")
    public boolean isSuccess;

    @SerializedName("code")
    private int code;

    @SerializedName("message")
    private String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return isSuccess;
    }
}
