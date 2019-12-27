package com.example.uitest;

public class UserInfo {
    private String name,gender,type,dob;
    private String contact,desc,status,userid;

    public UserInfo(){

    }
    public UserInfo(String name,String gender,String type,String dob,String contact,String desc,String userid,String status){
        this.name=name;
        this.gender=gender;
        this.type=type;
        this.dob=dob;
        this.contact=contact;
        this.desc=desc;
        this.status=status;
        this.userid=userid;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUserid() {
        return userid;
    }

    public String getStatus() {
        return status;
    }

    public String getDesc() {
        return this.desc;
    }

    public String getContact(){
        return this.contact;

    }

}
