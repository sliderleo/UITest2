package com.example.uitest;

public class CarInfo {
    String plate,brand,model,color,carId,insurance;
    public CarInfo(){

    }
    public CarInfo(String plate,String brand,String model,String color,String carId,String insurance){
        this.plate=plate;
        this.brand=brand;
        this.model=model;
        this.color=color;
        this.carId=carId;
        this.insurance=insurance;

    }

    public String getCarId(){
        return  this.carId;
    }
    public String getPlate(){
        return this.plate;
    }
    public String getBrand(){
        return this.brand;
    }

    public String getInsurance() {
        return this.insurance;
    }

    public String getModel(){
        return this.model;
    }
    public String getColor(){
        return this.color;
    }
    public String toString(){
        return "Plate Number :"+getPlate()+"\nBrand: "+getBrand()+"\nModel: "+getModel()+"\nColor: "+getColor()+"\nInsurance Provider: "+getInsurance();
    }
}
