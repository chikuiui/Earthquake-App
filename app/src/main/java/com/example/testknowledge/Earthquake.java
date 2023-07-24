package com.example.testknowledge;

import java.net.URL;

public class Earthquake {
    private double magnitude;
    private String location;
    private long time;

    private String url;


    public Earthquake(double magnitude,String location,long time){
        this.magnitude = magnitude;
        this.location = location;
        this.time = time;
    }

    public Earthquake(double magnitude,String location,long time,String url){
        this.magnitude = magnitude;
        this.location = location;
        this.time = time;
        this.url = url;
    }

    public String getUrl() {return url;}

    public double getMagnitude() {
        return magnitude;
    }

    public String getLocation() {
        return location;
    }

    public long getTime() {return time;}
}
