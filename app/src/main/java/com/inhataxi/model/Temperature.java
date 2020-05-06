package com.inhataxi.model;

public class Temperature {

    private String tc;
    private String tmax;
    private String tmin;

    public Temperature(String tc, String tmax, String tmin) {
        this.tc = tc;
        this.tmax = tmax;
        this.tmin = tmin;
    }

    public String getTc() {
        return tc;
    }

    public void setTc(String tc) {
        this.tc = tc;
    }

    public String getTmax() {
        return tmax;
    }

    public void setTmax(String tmax) {
        this.tmax = tmax;
    }

    public String getTmin() {
        return tmin;
    }

    public void setTmin(String tmin) {
        this.tmin = tmin;
    }
}
