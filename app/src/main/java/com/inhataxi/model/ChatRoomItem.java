package com.inhataxi.model;

public class ChatRoomItem {

    private String name;
    private String dept;
    private int count;
    private String time;
    private double distance;
    private String departure;
    private String destination;
    private String imageUrl;
    private String url;

    public ChatRoomItem(String name, String dept, int count, String time, double distance, String departure, String destination, String imageUrl, String url){
        this.name = name;
        this.dept = dept;
        this. count = count;
        this.time = time;
        this.distance = distance;
        this.departure = departure;
        this.destination = destination;
        this.imageUrl = imageUrl;
        this.url = url;
    }

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
