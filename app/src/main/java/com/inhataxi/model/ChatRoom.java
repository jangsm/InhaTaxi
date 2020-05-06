package com.inhataxi.model;

import com.google.gson.annotations.SerializedName;

public class ChatRoom {

    @SerializedName("name")
    private String name;

    @SerializedName("dept")
    private String dept;

    @SerializedName("user_count")
    private int count;

    @SerializedName("created_at")
    private String time;

    @SerializedName("distance")
    private double distance;

    @SerializedName("start_string")
    private String departure;

    @SerializedName("end_string")
    private String destination;

    @SerializedName("profile_url")
    private String imageUrl;

    @SerializedName("open_chat_url")
    private String url;

    public String getName() {
        return name;
    }

    public String getDept() {
        return dept;
    }

    public int getCount() {
        return count;
    }

    public String getTime() {
        return time;
    }

    public double getDistance() {
        return distance;
    }

    public String getDeparture() {
        return departure;
    }

    public String getDestination() {
        return destination;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getUrl() {
        return url;
    }
}


