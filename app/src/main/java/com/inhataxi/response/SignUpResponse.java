package com.inhataxi.response;

import com.google.gson.annotations.SerializedName;
import com.inhataxi.model.ChatRoom;

import java.util.ArrayList;

public class SignUpResponse {

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
}
