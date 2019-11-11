package com.example.uitest;

public class Status {
    String name,userId,duty;

    public Status(){

    }
    public Status(String name,String userId,String duty){
        this.name=name;
        this.userId=userId;
        this.duty=duty;
    }

    public String getDuty(){
        return this.duty;
    }
    public String getName(){
        return this.name;
    }
    public String getUserId(){
        return this.userId;
    }
}

