package com.mwbtech.utilityapp.retrofit_interface;

import com.mwbtech.utilityapp.objects.PostOfficePincode;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CustomerCreationInterface {
    public static String CUSTOMER_CREATION = "https://api.postalpincode.in/";

    @GET("pincode/{code}")
    Call<List<PostOfficePincode>> getPincode(@Path("code")int pincode);

    @GET("postoffice/{name}")
    Call<List<PostOfficePincode>> getPincodeName(@Path("name")String name);
}
