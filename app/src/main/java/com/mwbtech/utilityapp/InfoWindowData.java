package com.mwbtech.utilityapp;

public class InfoWindowData {

    String address,latlong;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatlong() {
        return latlong;
    }

    public void setLatlong(String latlong) {
        this.latlong = latlong;
    }

    @Override
    public String toString() {
        return "InfoWindowData{" +
                "address='" + address + '\'' +
                ", latlong='" + latlong + '\'' +
                '}';
    }
}
