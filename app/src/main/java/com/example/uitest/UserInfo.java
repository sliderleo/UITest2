package com.example.uitest;

public class UserInfo {
    private String name,gender,type,dob;
    private String contact,desc;

    public UserInfo(){

    }
    public UserInfo(String name,String gender,String type,String dob,String contact,String desc){
        this.name=name;
        this.gender=gender;
        this.type=type;
        this.dob=dob;
        this.contact=contact;
        this.desc=desc;
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

    public String getDesc() {
        return this.desc;
    }

    public String getContact(){
        return this.contact;

    }

}
