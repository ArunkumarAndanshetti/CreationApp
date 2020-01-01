package com.mwbtech.utilityapp;

import android.graphics.Bitmap;

public class AtmLocation {

    String atmName,atmArea,atmLatitube,atmLongitude,atmIcon;

    public AtmLocation(String atmName, String atmArea, String atmLatitube, String atmLongitude,String atmIcon) {
        this.atmName = atmName;
        this.atmArea = atmArea;
        this.atmLatitube = atmLatitube;
        this.atmLongitude = atmLongitude;
        this.atmIcon = atmIcon;
    }

    public String getAtmName() {
        return atmName;
    }

    public String getAtmArea() {
        return atmArea;
    }

    public String getAtmLatitube() {
        return atmLatitube;
    }

    public String getAtmLongitude() {
        return atmLongitude;
    }

    public String getAtmIcon() {
        return atmIcon;
    }

    @Override
    public String toString() {
        return "AtmLocation{" +
                "atmName='" + atmName + '\'' +
                ", atmArea='" + atmArea + '\'' +
                ", atmLatitube='" + atmLatitube + '\'' +
                ", atmLongitude='" + atmLongitude + '\'' +
                '}';
    }
}
