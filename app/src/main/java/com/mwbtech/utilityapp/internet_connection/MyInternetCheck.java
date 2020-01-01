package com.mwbtech.utilityapp.internet_connection;

import android.app.Application;

public class MyInternetCheck extends Application {


    private static MyInternetCheck mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
    }

    public static synchronized MyInternetCheck getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
}
