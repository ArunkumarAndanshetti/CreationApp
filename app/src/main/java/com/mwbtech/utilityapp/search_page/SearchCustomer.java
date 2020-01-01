package com.mwbtech.utilityapp.search_page;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;
import com.mwbtech.utilityapp.R;
import com.mwbtech.utilityapp.main_page.MainActivity;
import com.mwbtech.utilityapp.objects.AtmLocation;
import com.mwbtech.utilityapp.objects.LatLngBean;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class SearchCustomer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback, LocationListener, View.OnClickListener {

    private ActionBarDrawerToggle actionBarDrawerToggle;
    DrawerLayout drawer;
    NavigationView navigationView;
    Toolbar toolbar;

    double lat,lng;

    EditText edSearch;
    ImageView img;
    SupportMapFragment supportMapFragment;
    private GoogleMap googleMap;
    private ArrayList<LatLng>listLatLng;
    private RelativeLayout rlMapLayout;
    HashMap<Marker, LatLngBean> hashMapMarker = new HashMap<Marker,LatLngBean>();
    HashMap<Marker, AtmLocation> hashMapMarkerAtm = new HashMap<Marker,AtmLocation>();
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;

    ArrayList<AtmLocation> atmLocationArrayList;
    TextView tvAtmName,tvAtmArea,tvAreaLatitude,tvAreaLongitude;
    ImageView imageView;
    Dialog mDialog;

    //https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=15.3581689,75.1335591&radius=1000&types=bank&sensor=true&key=AIzaSyA8szrI9Ue4EwyUwTgz7Nk0c39qMal0pN4
    //https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=15.3581689,75.1335591&radius=1000&types=atm&sensor=true&key=AIzaSyA8szrI9Ue4EwyUwTgz7Nk0c39qMal0pN4

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_customer);
        drawer = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        atmLocationArrayList = new ArrayList<>();
        edSearch = findViewById(R.id.editText);
        img = findViewById(R.id.search);
        img.setOnClickListener(this);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer,toolbar,R.string.open, R.string.close);
        navigationView = findViewById(R.id.nav_view);
        drawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        rlMapLayout=(RelativeLayout) findViewById(R.id.rlMapLayout);
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);
        //setUpMapIfNeeded();

    }

    @Override
    public void onMapReady(GoogleMap googleMap1) {
        googleMap = googleMap1;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                googleMap.setMyLocationEnabled(true);
            }
        }
        else {
            buildGoogleApiClient();
            googleMap.setMyLocationEnabled(true);
        }
        //setData();
        showAtm();
        LoadingGoogleMapAtm(atmLocationArrayList);

    }
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main_drawer, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){

            case R.id.customer_details:
                startActivity(new Intent(this, MainActivity.class));
                SearchCustomer.this.finish();
                break;
            case R.id.search_customer:

                startActivity(new Intent(this,SearchCustomer.class));
                SearchCustomer.this.finish();
                break;
            default:
                return true;


        }


        return true;
    }

    private void setData()
    {
        ArrayList<LatLngBean> arrayList=new ArrayList<LatLngBean>();
        LatLngBean bean=new LatLngBean();
        bean.setTitle("Hubli");
        bean.setSnippet("Hello,Hubli");
        bean.setLatitude("15.3647");
        bean.setLongitude("75.1240");
        arrayList.add(bean);

        LatLngBean bean1=new LatLngBean();
        bean1.setTitle("Dharwad");
        bean1.setSnippet("Hello,Dharwad");
        bean1.setLatitude("15.3778");
        bean1.setLongitude("75.2479");
        arrayList.add(bean1);

        LatLngBean bean2=new LatLngBean();
        bean2.setTitle("Belgaum");
        bean2.setSnippet("Hello,Belgaum");
        bean2.setLatitude("15.8497");
        bean2.setLongitude("74.4977");
        arrayList.add(bean2);

        LoadingGoogleMap(arrayList);
    }




    void LoadingGoogleMap(ArrayList<LatLngBean> arrayList)
    {
        if (googleMap != null)
        {
            googleMap.clear();
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setZoomControlsEnabled(true);

            if(arrayList.size()>0)
            {
                try
                {
                    listLatLng=new ArrayList<LatLng>();
                    for (int i = 0; i < arrayList.size(); i++)
                    {
                        LatLngBean bean=arrayList.get(i);
                        if(bean.getLatitude().length()>0 && bean.getLongitude().length()>0)
                        {
                            double lat=Double.parseDouble(bean.getLatitude());
                            double lon=Double.parseDouble(bean.getLongitude());

                            Marker marker = googleMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(lat,lon))
                                    .title(bean.getTitle())
                                    .snippet(bean.getSnippet())
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

                            //Add Marker to Hashmap
                            hashMapMarker.put(marker,bean);

                            //Set Zoom Level of Map pin
                            LatLng object=new LatLng(lat, lon);
                            listLatLng.add(object);
                        }
                    }
                    SetZoomlevel(listLatLng);
                }
                catch (NumberFormatException e)
                {
                    e.printStackTrace();
                }

                googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

                    @Override
                    public void onInfoWindowClick(Marker position)
                    {
                        LatLngBean bean=hashMapMarker.get(position);
                        Toast.makeText(getApplicationContext(), bean.getTitle(),Toast.LENGTH_SHORT).show();





                    }
                });
            }
        }

        else
        {
            Toast.makeText(getApplicationContext(),"Sorry! unable to create maps", Toast.LENGTH_SHORT).show();
        }
    }


    public void  SetZoomlevel(ArrayList<LatLng> listLatLng)
    {
        if (listLatLng != null && listLatLng.size() == 1)
        {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(listLatLng.get(0), 10));
        }
        else if (listLatLng != null && listLatLng.size() > 1)
        {
            final LatLngBounds.Builder builder = LatLngBounds.builder();
            for (int i = 0; i < listLatLng.size(); i++)
            {
                builder.include(listLatLng.get(i));
            }

            final ViewTreeObserver treeObserver = rlMapLayout.getViewTreeObserver();
            treeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener()
            {
                @SuppressWarnings("deprecation")
                @Override
                public void onGlobalLayout()
                {
                    if(googleMap != null){
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), findViewById(R.id.map)
                                .getWidth(), findViewById(R.id.map).getHeight(), 80));
                        rlMapLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }
                }
            });

        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest,  this);
        }
        //setData();
        showAtm();
        LoadingGoogleMapAtm(atmLocationArrayList);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        //googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        lat = location.getLatitude();
        lng = location.getLongitude();
        googleMap.getMapType();
        LoadingGoogleMapAtm(atmLocationArrayList);
    }


    public void showAtm(){
        String getAtmUrl ="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+lat+","+lng+"&radius=1000&types=atm&sensor=true &key=AIzaSyA8szrI9Ue4EwyUwTgz7Nk0c39qMal0pN4";
        try{
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder().url(getAtmUrl).build();
            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    SearchCustomer.this.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(getBaseContext(), "Request to atm ", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws
                        IOException {
                    Log.i("response ", "onResponse(): " + response);
                    String result = response.body().string();
                    Log.i("result",result);
                    try{
                        atmLocationArrayList.clear();
                        JSONObject jsonObject = new JSONObject(result);
                        String resultData = jsonObject.getString("results");
                        JSONArray urlDetails = new JSONArray(resultData);
                        for (int i = 0 ; i < urlDetails.length(); i++){
                            JSONObject json = urlDetails.getJSONObject(i);
                            String geometry = json.getString("geometry");
                            String vicinity = json.getString("vicinity");
                            JSONObject jsonGeometry = new JSONObject(geometry);
                            String geoLocation = jsonGeometry.getString("location");
                            JSONObject jsonLatLng = new JSONObject(geoLocation);
                            double atmLat = jsonLatLng.getDouble("lat");
                            double atmLong = jsonLatLng.getDouble("lng");
                            String atmName = json.getString("name");
                            String atmIcon = json.getString("icon");
                            Log.i("JsonArrayAtm", "" + atmName);
                            Log.i("JsonArrayGeometry",geometry);
                            Log.i("LatLong",""+atmLat+" , "+atmLong);
                            Log.i("Vicinity", vicinity);
                            atmLocationArrayList.add(new AtmLocation(atmName,vicinity,String.valueOf(atmLat),String.valueOf(atmLong),atmIcon));
                            //googleMap.notifyAll();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    LoadingGoogleMapAtm(atmLocationArrayList);
                                }
                            });
                        }

                        //LoadingGoogleMapAtm(atmLocationArrayList);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    void LoadingGoogleMapAtm(ArrayList<AtmLocation> arrayList)
    {
        try {
            if (googleMap != null) {
            /*googleMap.clear();
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setZoomControlsEnabled(true);*/

                if (arrayList.size() > 0) {
                    try {
                        listLatLng = new ArrayList<LatLng>();
                        for (int i = 0; i < arrayList.size(); i++) {
                            AtmLocation bean = arrayList.get(i);
                            if (bean.getAtmLatitube().length() > 0 && bean.getAtmLongitude().length() > 0) {
                                double lat = Double.parseDouble(bean.getAtmLatitube());
                                double lon = Double.parseDouble(bean.getAtmLongitude());

                                Marker marker = googleMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(lat, lon))
                                        .title(bean.getAtmName())
                                        .snippet(bean.getAtmArea())
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

                                //Add Marker to Hashmap
                                hashMapMarkerAtm.put(marker, bean);

                                //Set Zoom Level of Map pin
                                LatLng object = new LatLng(lat, lon);
                                listLatLng.add(object);
                            }
                        }
                        SetZoomlevel(listLatLng);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }

                    googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

                        @Override
                        public void onInfoWindowClick(Marker position) {
                            AtmLocation bean = hashMapMarkerAtm.get(position);
                            Toast.makeText(getApplicationContext(), bean.getAtmName(), Toast.LENGTH_SHORT).show();
                            /*Intent intent = new Intent(SearchCustomer.this,DetailsFragment.class);
                            intent.putExtra("title",bean.getAtmName());
                            intent.putExtra("snippet",bean.getAtmArea());
                            intent.putExtra("latitude",bean.getAtmLatitube());
                            intent.putExtra("longitude",bean.getAtmLongitude());
                            intent.putExtra("icon",bean.getAtmIcon());
                            startActivity(intent);*/
                            openDialogMethod(bean.getAtmName(),bean.getAtmArea(),bean.getAtmLatitube(),bean.getAtmLongitude(),bean.getAtmIcon());
                        }
                    });
                }
            } else {
                Toast.makeText(getApplicationContext(), "Sorry! unable to create maps", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void openDialogMethod(String atmName, String atmArea, String atmLatitube, String atmLongitude, String atmIcon) {
        mDialog = new Dialog(this);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialog.setContentView(R.layout.details_fragment);
        mDialog.setCancelable(true);
        mDialog.show();
        tvAtmName = mDialog.findViewById(R.id.tvAtmName);
        tvAtmArea = mDialog.findViewById(R.id.tvAtmArea);
        tvAreaLatitude = mDialog.findViewById(R.id.tvAtmLat);
        tvAreaLongitude = mDialog.findViewById(R.id.tvAtmlong);
        imageView = mDialog.findViewById(R.id.tvAtmIcon);

        Picasso.get()
                .load(atmIcon)
                .resize(50, 50)
                .centerCrop()
                .into(imageView);
        tvAtmName.setText("Name : "+atmName);
        tvAtmArea.setText("Address : "+atmArea);
        tvAreaLatitude.setText("latitude : "+atmLatitube);
        tvAreaLongitude.setText("longitude : "+atmLongitude);
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

    public Bitmap resizeMapIcons(String iconName){
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier(iconName, "drawable", getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, 50, 50, false);
        return resizedBitmap;
    }

    @Override
    public void onClick(View v) {

        searchLocation(v);

    }

    public void searchLocation(View view) {
        String location = edSearch.getText().toString();
        List<Address> addressList = null;

        if (location != null || !location.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);

            } catch (IOException e) {
                e.printStackTrace();
            }
            Address address = addressList.get(0);
            showAtm1(address.getLatitude(), address.getLongitude());

        }
    }

    private void showAtm1(double latitude, double longitude) {

        String getAtmUrl ="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+latitude+","+longitude+"&radius=1000&types=atm&sensor=true &key=AIzaSyA8szrI9Ue4EwyUwTgz7Nk0c39qMal0pN4";
        try{
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder().url(getAtmUrl).build();
            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    SearchCustomer.this.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(getBaseContext(), "Request to atm ", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws
                        IOException {
                    Log.i("response ", "onResponse(): " + response);
                    String result = response.body().string();
                    Log.i("result",result);
                    atmLocationArrayList.clear();
                    try{
                        JSONObject jsonObject = new JSONObject(result);
                        String resultData = jsonObject.getString("results");
                        JSONArray urlDetails = new JSONArray(resultData);
                        for (int i = 0 ; i < urlDetails.length(); i++){
                            JSONObject json = urlDetails.getJSONObject(i);
                            String geometry = json.getString("geometry");
                            String vicinity = json.getString("vicinity");
                            JSONObject jsonGeometry = new JSONObject(geometry);
                            String geoLocation = jsonGeometry.getString("location");
                            JSONObject jsonLatLng = new JSONObject(geoLocation);
                            double atmLat = jsonLatLng.getDouble("lat");
                            double atmLong = jsonLatLng.getDouble("lng");
                            String atmName = json.getString("name");
                            String atmIcon = json.getString("icon");
                            Log.i("JsonArrayAtm", "" + atmName);
                            Log.i("JsonArrayGeometry",geometry);
                            Log.i("LatLong",""+atmLat+" , "+atmLong);
                            Log.i("Vicinity", vicinity);
                            atmLocationArrayList.add(new AtmLocation(atmName,vicinity,String.valueOf(atmLat),String.valueOf(atmLong),atmIcon));
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    LoadingGoogleMapAtm(atmLocationArrayList);
                                }
                            });
                        }

                        //LoadingGoogleMapAtm(atmLocationArrayList);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
