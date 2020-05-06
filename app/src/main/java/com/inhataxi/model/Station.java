package com.inhataxi.model;

public class Station {

    private String latitude;
    private String longitute;
    private String name;
    private String id;
    private String type;

    public Station(String latitude, String longitute, String name, String id, String type) {
        this.latitude = latitude;
        this.longitute = longitute;
        this.name = name;
        this.id = id;
        this.type = type;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitute() {
        return longitute;
    }

    public void setLongitute(String longitute) {
        this.longitute = longitute;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
