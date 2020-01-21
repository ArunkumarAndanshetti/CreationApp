package com.mwbtech.utilityapp.retrofit_client;

import com.mwbtech.utilityapp.retrofit_interface.CustomerCreationInterface;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Customer_Client {

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {

        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(CustomerCreationInterface.CUSTOMER_CREATION)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getClientCreation() {

        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(CustomerCreationInterface.CREATION)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
