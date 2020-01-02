package com.mwbtech.utilityapp.objects;

public class CustomerCreation {


    String name,address,location,image;

    public CustomerCreation(){

    }
    public CustomerCreation(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public CustomerCreation(String name, String address,String location) {
        this.name = name;
        this.address = address;
        this.location = location;
    }

    public CustomerCreation(String name, String address,String location,String image){
        this.name = name;
        this.address = address;
        this.location = location;
        this.image = image;
    }


    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getLocation() {
        return location;
    }

    public String getImage() {
        return image;
    }

    @Override
    public String toString() {
        return "CustomerCreation{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", location='" + location + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
