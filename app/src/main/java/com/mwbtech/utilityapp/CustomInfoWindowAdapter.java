package com.mwbtech.utilityapp;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.logging.Logger;

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private Context context;

    public CustomInfoWindowAdapter(Context context){
        this.context = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = ((Activity)context).getLayoutInflater().inflate(R.layout.custominfowindow, null);

        TextView tvTitle = (TextView) view.findViewById(R.id.tv_title1);
        TextView tvSubTitle = (TextView) view.findViewById(R.id.tv_subtitle1);
        InfoWindowData infoWindowData = (InfoWindowData) marker.getTag();

        tvTitle.setText(""+infoWindowData.getAddress());
        tvSubTitle.setText(""+infoWindowData.getLatlong());

        return view;
    }
}
