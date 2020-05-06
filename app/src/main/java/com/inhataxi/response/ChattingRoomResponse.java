package com.inhataxi.response;

import com.google.gson.annotations.SerializedName;
import com.inhataxi.model.ChatRoom;

import java.util.ArrayList;

public class ChattingRoomResponse {

    @SerializedName("isSuccess")
    private boolean isSuccess;

    @SerializedName("code")
    private int code;

    @SerializedName("message")
    private String message;

    @SerializedName("result")
    private ArrayList<ChatRoom> result;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public ArrayList<ChatRoom> getResult() {
        return result;
    }
}