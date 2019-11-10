package com.example.uitest;

public class Status {
    String name,userId;
    Boolean duty;
    public Status(){

    }
    public Status(String name,String userId,Boolean duty){
        this.name=name;
        this.userId=userId;
        this.duty=duty;
    }

    public Boolean getDuty(){
        return this.duty;
    }
    public String getName(){
        return this.name;
    }
    public String getUserId(){
        return this.userId;
    }
}

