package com.example.uitest;

public class RequestInfo {
    String userId,towDriverId,requestId,status,workshopName,car,id,userName,userContact,towDriverName,insurance,payment;
    double userLat,userLong,workshopLat,workshopLong,fare;

    public RequestInfo(String userName,String userContact,double userLat,double userLong,double workshopLat,double workshopLong,String workshopName,String userId,String towDriverId,String towDriverName,String car,double fare,String status,String id,String insurance,String payment){
        this.userLat=userLat;
        this.towDriverName=towDriverName;
        this.id=id;
        this.userName=userName;
        this.userContact=userContact;
        this.userLong=userLong;
        this.workshopLat=workshopLat;
        this.workshopLong=workshopLong;
        this.workshopName=workshopName;
        this.userId=userId;
        this.fare=fare;
        this.payment=payment;
        this.car=car;
        this.towDriverId=towDriverId;
        this.status=status;
        this.insurance = insurance;
    }
     public String getUserContact(){
        return this.userContact;
     }
     public String getCar(){
        return car;
     }

    public String getTowDriverName() {
        return towDriverName;
    }

    public String getInsurance() {
        return insurance;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setTowDriverId(String towDriverId) {
        this.towDriverId = towDriverId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setWorkshopName(String workshopName) {
        this.workshopName = workshopName;
    }

    public void setCar(String car) {
        this.car = car;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserContact(String userContact) {
        this.userContact = userContact;
    }

    public void setTowDriverName(String towDriverName) {
        this.towDriverName = towDriverName;
    }

    public void setInsurance(String insurance) {
        this.insurance = insurance;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public void setUserLat(double userLat) {
        this.userLat = userLat;
    }

    public void setUserLong(double userLong) {
        this.userLong = userLong;
    }

    public void setWorkshopLat(double workshopLat) {
        this.workshopLat = workshopLat;
    }

    public void setWorkshopLong(double workshopLong) {
        this.workshopLong = workshopLong;
    }

    public void setFare(double fare) {
        this.fare = fare;
    }

    public String getId(){
        return this.id;
    }
    public String getUserName(){
        return this.userName;
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
