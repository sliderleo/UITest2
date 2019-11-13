package com.example.uitest;

public class RequestInfo {
    String userId,towDriverId,requestId,status,workshopName,car,id;
    double userLat,userLong,workshopLat,workshopLong,fare;

    public RequestInfo(double userLat,double userLong,double workshopLat,double workshopLong,String workshopName,String userId,String towDriverId,String car,double fare,String status,String id){
        this.userLat=userLat;
        this.id=id;
        this.userLong=userLong;
        this.workshopLat=workshopLat;
        this.workshopLong=workshopLong;
        this.workshopName=workshopName;
        this.userId=userId;
        this.fare=fare;
        this.car=car;
        this.towDriverId=towDriverId;
        this.status=status;
    }
    public String getId(){
        return id;
    }

    public double getFare() {
        return fare;
    }

    public double getUserLat() {
        return userLat;
    }

    public double getUserLong() {
        return userLong;
    }

    public String getRequestId() {
        return requestId;
    }

    public double getWorkshopLat() {
        return workshopLat;
    }

    public double getWorkshopLong() {
        return workshopLong;
    }

    public String getStatus() {
        return status;
    }

    public String getTowDriverId() {
        return towDriverId;
    }

    public String getUserId() {
        return userId;
    }

    public String getWorkshopName() {
        return workshopName;
    }
}
