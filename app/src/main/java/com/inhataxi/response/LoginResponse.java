package com.inhataxi.response;

import com.google.gson.annotations.SerializedName;
import com.inhataxi.model.UserInfo;

public class LoginResponse {

    @SerializedName("code")
    private int code;

    @SerializedName("message")
    private String message;

    @SerializedName("result")
    private UserInfo result;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public UserInfo getResult(){return  result;}

}
