package com.example.uitest;

public class CarInfo {
    String plate,brand,model,color;
    public CarInfo(){

    }
    public CarInfo(String plate,String brand,String model,String color){
        this.plate=plate;
        this.brand=brand;
        this.model=model;
        this.color=color;

    }

    public String getPlate(){
        return this.plate;
    }
    public String getBrand(){
        return this.brand;
    }
    public String getModel(){
        return this.model;
    }
    public String getColor(){
        return this.color;
    }
    public String toString(){
        return "Plate Number :"+getPlate()+"\nBrand: "+getBrand()+"\nModel: "+getModel()+"\nColor: "+getColor();
    }
}
