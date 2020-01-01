package com.mwbtech.utilityapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class DetailsFragment extends AppCompatActivity {

    TextView tvAtmName,tvAtmArea,tvAreaLatitude,tvAreaLongitude;
    ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_fragment);
        tvAtmName = findViewById(R.id.tvAtmName);
        tvAtmArea = findViewById(R.id.tvAtmArea);
        tvAreaLatitude = findViewById(R.id.tvAtmLat);
        tvAreaLongitude = findViewById(R.id.tvAtmlong);
        imageView = findViewById(R.id.tvAtmIcon);

        Picasso.get()
                .load(getIntent().getStringExtra("icon"))
                .resize(50, 50)
                .centerCrop()
                .into(imageView);

        Log.i("Icon",getIntent().getStringExtra("icon"));
        tvAtmName.setText("Name"+getIntent().getStringExtra("title"));
        tvAtmArea.setText("Area"+getIntent().getStringExtra("snippet"));
        tvAreaLatitude.setText("lat"+getIntent().getStringExtra("latitude"));
        tvAreaLongitude.setText("long"+getIntent().getStringExtra("longitude"));

        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,SearchCustomer.class));
        DetailsFragment.this.finish();
    }

    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }
}
