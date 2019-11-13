package com.example.uitest;

import java.util.Date;

public class LocationUpdate {
    private double userlat,userlong;
    public LocationUpdate(){}
    public LocationUpdate(double userlat,double userlong){
        this.userlat=userlat;
        this.userlong=userlong;
    }
    public double getUserlat(){
        return this.userlat;
    }
    public double getUserlong(){
        return this.userlong;
    }


}
