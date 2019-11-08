package com.example.uitest;

public class UserInfo {
    private String name,gender,type,dob;
    private int contact;

    public UserInfo(){

    }
    public UserInfo(String name,String gender,String type,String dob,int contact){
        this.name=name;
        this.gender=gender;
        this.type=type;
        this.dob=dob;
        this.contact=contact;
    }
    public String getName(){
        return this.name;
    }
    public String getGender(){
        return this.gender;
    }
    public String getType(){
        return this.type;
    }
    public String getDob(){
        return this.dob;
    }
    public int getContact(){
        return this.contact;

    }

}
