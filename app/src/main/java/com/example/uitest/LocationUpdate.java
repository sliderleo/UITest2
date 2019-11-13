package com.example.uitest;

import java.util.Date;

public class LocationUpdate {
    private double userlat,userlong;
    private Date currentTime;
    public LocationUpdate(){}
    public LocationUpdate(double userlat,double userlong,Date currentTime){
        this.userlat=userlat;
        this.userlong=userlong;
        this.currentTime=currentTime;
    }
    public double getUserlat(){
        return this.userlat;
    }
    public double getUserlong(){
        return this.userlong;
    }
    public Date getCurrentDate(){
        return this.currentTime;
    }

}
