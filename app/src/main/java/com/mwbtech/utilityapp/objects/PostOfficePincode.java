package com.mwbtech.utilityapp.objects;

import com.google.gson.annotations.SerializedName;
import com.mwbtech.utilityapp.objects.Office;

import java.util.List;

public class PostOfficePincode {

    String Name,Pincode;

    @SerializedName("PostOffice")
    List<Office> PostOffice;

    public PostOfficePincode(String name, String pincode) {
        Name = name;
        Pincode = pincode;
    }

    public String getName() {
        return Name;
    }

    public String getPincode() {
        return Pincode;
    }

    public List<Office> getPostOffice() {
        return PostOffice;
    }

    public void setPostOffice(List<Office> postOffice) {
        PostOffice = postOffice;
    }

    @Override
    public String toString() {
        return "PostOfficePincode{" +
                "Name='" + Name + '\'' +
                ", Pincode='" + Pincode + '\'' +
                '}';
    }
}
