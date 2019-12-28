package com.mwbtech.utilityapp;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


import static android.content.Context.LOCATION_SERVICE;

public class BillingAddresssActivity extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, View.OnClickListener {

    Button btnNext,btnMap;
    ImageView search;
    private GoogleMap mMap;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 599;
    LocationManager locationManager;
    Button btnCancel,btnOk;
    View billView;
    int pos = 2;
    AutoCompleteTextView edSearchLocation;
    CallToFragment callToFragment;
    LinearLayout linearLayout;
    public static final String TAG = "AutoCompleteActivity";
    private static final int AUTO_COMP_REQ_CODE = 2;
    public static PlaceAutocompleteFragment autocompleteFragment;
    public static AutocompleteFilter autocompleteFilter;
    EditText spState,spCity;
    ArrayAdapter arrayAdapterState;
    ArrayAdapter arrayAdapterCity;
    List<String> stateList;
    List<String> cityList;
    Dialog mDialog,mDialogState,mDialogCity,mDialogOther;
    EditText dialogState,dialogCity,createCity;
    ListView listViewState,listViewCity;
    String cityName,stateName;
    Button btnCreateCity,submit;
    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        try {

            if(activity instanceof CallToFragment) {
                callToFragment = (CallToFragment) activity;
            }else {
                throw new RuntimeException(activity.toString()
                        + " must implement OnGreenFragmentListener");
            }
        }catch (ClassCastException e){

        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //https://javapapers.com/android/draw-path-on-google-maps-android-api/

        //https://velmm.com/google-places-autocomplete-android-example/
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        billView= inflater.inflate(R.layout.billing_address, null);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        mapFragment.getMapAsync(this);
        checkLocationPermission();
        stateList = new ArrayList<>();
        cityList = new ArrayList<>();
        linearLayout = billView.findViewById(R.id.linear);
        btnMap = billView.findViewById(R.id.btnMap);
        edSearchLocation = billView.findViewById(R.id.editText);
        search = billView.findViewById(R.id.search);
        btnNext = billView.findViewById(R.id.btnNext);
        spState = billView.findViewById(R.id.spinnerState);
        spCity = billView.findViewById(R.id.spinnerCity);
        spState.setOnClickListener(this::onClick);
        spCity.setOnClickListener(this::onClick);
        btnMap.setOnClickListener(this::onClick);
        btnNext.setOnClickListener(this);
        search.setOnClickListener(this::onClick);
        return billView;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        //autocompleteFragment.onActivityResult(requestCode, resultCode, data);
        if(requestCode == AUTO_COMP_REQ_CODE){
            if (resultCode == Activity.RESULT_OK) {
                com.google.android.gms.location.places.Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                Toast.makeText(getActivity(), "place "+place.toString(),
                        Toast.LENGTH_LONG).show();
            }else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getActivity(), data);
                Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == Activity.RESULT_CANCELED) {

            }
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btnNext:
                callToFragment.communicateFragment(pos);
                break;

            case R.id.search:
                searchLocation(v);
                break;
            case R.id.btnMap:


                mDialog = new Dialog(getContext());
                mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                mDialog.getWindow().setBackgroundDrawable(
                        new ColorDrawable(android.graphics.Color.TRANSPARENT));
                mDialog.setContentView(R.layout.dialog_alert);
                mDialog.setCancelable(false);
                mDialog.show();
                btnCancel = mDialog.findViewById(R.id.btnCancel);
                btnOk = mDialog.findViewById(R.id.btnOk);
                btnOk.setOnClickListener(this::onClick);
                btnCancel.setOnClickListener(this::onClick);
                break;
            case R.id.btnCancel:
                mDialog.dismiss();
                break;

            case R.id.btnOk:
                btnMap.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);
                mDialog.dismiss();
                break;

            case R.id.spinnerState:
                mDialogState = new Dialog(getContext());
                mDialogState.requestWindowFeature(Window.FEATURE_NO_TITLE);
                mDialogState.getWindow().setBackgroundDrawable(
                        new ColorDrawable(android.graphics.Color.TRANSPARENT));
                mDialogState.setContentView(R.layout.dialog_category_list);
                mDialogState.setCancelable(false);

                dialogState = (EditText)mDialogState.findViewById(R.id.edState);
                listViewState = (ListView)mDialogState.findViewById(R.id.recyclerCategory);
                ImageView imageView = (ImageView)mDialogState.findViewById(R.id.cancel_category);
                mDialogState.show();
                imageView.setOnClickListener(this::onClick);
                arrayAdapterState = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_list_item_1, android.R.id.text1, getStateList());
                listViewState.setAdapter(arrayAdapterState);
                listViewState.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String stateName = (String) listViewState.getItemAtPosition(position);
                        //Toast.makeText(getActivity(),stateName , Toast.LENGTH_LONG).show();
                        spState.setText(stateName);
                        mDialogState.dismiss();
                    }
                });
                dialogState.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        try {
                            arrayAdapterState.getFilter().filter(s);
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        try {
                            arrayAdapterState.getFilter().filter(s);
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                break;

            case R.id.spinnerCity:
                mDialogCity = new Dialog(getContext());
                mDialogCity.requestWindowFeature(Window.FEATURE_NO_TITLE);
                mDialogCity.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                mDialogCity.setContentView(R.layout.dialog_child_list);
                mDialogCity.setCancelable(false);
                dialogCity = (EditText)mDialogCity.findViewById(R.id.edCity);
                listViewCity = (ListView)mDialogCity.findViewById(R.id.recyclerChildCategory);
                btnCreateCity = (Button)mDialogCity.findViewById(R.id.childcreate);
                ImageView imageView3 = (ImageView)mDialogCity.findViewById(R.id.cancel_childcategory);
                imageView3.setOnClickListener(this::onClick);
                mDialogCity.show();
                arrayAdapterCity = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_list_item_1, android.R.id.text1, getCityList());
                listViewCity.setAdapter(arrayAdapterCity);
                btnCreateCity.setOnClickListener(this::onClick);
                dialogCity.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        try {
                            arrayAdapterCity.getFilter().filter(s);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        try {
                            arrayAdapterCity.getFilter().filter(s);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });

                listViewCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String cityName = (String) listViewCity.getItemAtPosition(position);
                        //Toast.makeText(getActivity(),cityName , Toast.LENGTH_LONG).show();
                        spCity.setText(cityName);
                        mDialogCity.dismiss();
                    }
                });
                break;

            case R.id.cancel_category:
                    mDialogState.dismiss();
                break;
            case R.id.cancel_childcategory:
                    mDialogCity.dismiss();
                break;
            case R.id.childcreate:
                mDialogOther =  new Dialog(getContext());
                mDialogOther.requestWindowFeature(Window.FEATURE_NO_TITLE);
                mDialogOther.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                mDialogOther.setContentView(R.layout.dialog_city_list);
                mDialogOther.setCancelable(false);
                createCity = mDialogOther.findViewById(R.id.create_child);
                submit = mDialogOther.findViewById(R.id.btnchild);
                ImageView childCancel = mDialogOther.findViewById(R.id.cancel_city);
                mDialogOther.show();
                childCancel.setOnClickListener(this::onClick);
                childCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialogOther.dismiss();
                    }
                });
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(createCity.getText().toString().isEmpty()){
                            createCity.setError("Enter City Name");
                        }else {
                            //createChildCategoryMethodToServer(new ChildCategory(0,dialogCreateChildCategory.getText().toString(),selectedSubCategoryId,1));
                        }
                    }
                });
                break;

            case R.id.cancel_city:
                mDialogOther.dismiss();
                break;

        }
    }


    interface CallToFragment{
        void  communicateFragment(int pos);
    }



    public void searchLocation(View view) {
        String location = edSearchLocation.getText().toString();
        List<Address> addressList = null;

        if (location != null || !location.equals("")) {
            Geocoder geocoder = new Geocoder(getContext());
            try {
                addressList = geocoder.getFromLocationName(location, 1);

            } catch (IOException e) {
                e.printStackTrace();
            }
            Address address = addressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            mMap.addMarker(new MarkerOptions().position(latLng).title(location));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            Toast.makeText(getActivity(),address.getLatitude()+" "+address.getLongitude(),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }
        else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

    }
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission was granted.
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                } else {
                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(getActivity(), "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
            // other 'case' lines to check for other permissions this app might request.
            //You can add here other case statements according to your requirement.
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);


        Log.i("12343", String.valueOf(latLng));

        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(20));
        mMap.getMapType();

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }


    public String getStateJson()
    {
        String json=null;
        try
        {
            InputStream is = getContext().getAssets().open("states.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            return json;
        }
        return json;
    }

    // This add all JSON object's data to the respective lists
    public List<String> getStateList()
    {
        // Exceptions are returned by JSONObject when the object cannot be created
        try
        {
            // Convert the string returned to a JSON object
            JSONObject jsonObject=new JSONObject(getStateJson());
            // Get Json array
            JSONArray array = jsonObject.getJSONArray("states");
            for(int i=0;i<array.length();i++)
            {
                // select the particular JSON data
                JSONObject object=array.getJSONObject(i);
                String state=object.getString("state");
                stateList.add(state);
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return stateList;
    }


    public String getCityJson()
    {
        String json=null;
        try
        {
            InputStream is = getContext().getAssets().open("city.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            return json;
        }
        return json;
    }

    public List<String> getCityList()
    {
        try
        {
            // Convert the string returned to a JSON object
            JSONObject jsonObject=new JSONObject(getCityJson());
            // Get Json array
            JSONArray array = jsonObject.getJSONArray("cities");
            for(int i=0;i<array.length();i++)
            {
                // select the particular JSON data
                JSONObject object=array.getJSONObject(i);
                String city=object.getString("city");
                cityList.add(city);
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return cityList;
    }

}
