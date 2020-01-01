package com.mwbtech.utilityapp;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.LOCATION_SERVICE;

public class BillingAddresssActivity extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, View.OnClickListener {

    Button btnNext,btnMap,btnSearchPin;
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
    EditText spState,spCity,spPincode;
    ArrayAdapter arrayAdapterState;
    ArrayAdapter arrayAdapterCity;
    List<String> stateList;
    List<String> cityList;
    ArrayAdapter arrayAdapterPincode;
    Dialog mDialog,mDialogState,mDialogCity,mDialogOther,mDialogPincode,mDialogOpen;
    EditText dialogState,dialogCity,createCity,pincode;
    ListView listViewState,listViewCity,listviewPincode;
    String cityName,stateName;
    Button btnCreateCity,submit,btnSearch;

    String areaName;
    TextView tvLatLong;
    PincodeAdapter pincodeAdapter;
    List<PostOfficePincode> pincodeArrayList;
    List<Office> officeList;
    CustomerCreationInterface customerCreationInterface;
    MarkerOptions markerOptions;
    String GOOGLE_API_KEY = "AIzaSyD6Rlv6AD9xaIknkRFLgUsi4mP5wxKVCvc";
    TextView tvAtmName,tvAtmArea,tvAreaLatitude,tvAreaLongitude;
    LinearLayout linear;
    private Map<Marker, Map<String, Object>> markers = new HashMap<>();
    private Map<String, Object> dataModel = new HashMap<>();
    LatLng latLng;
    private final static int PLACE_PICKER_REQUEST = 999;
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

        //https://api.postalpincode.in/pincode/591104
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
        pincodeArrayList = new ArrayList<>();
        officeList = new ArrayList<>();
        linearLayout = billView.findViewById(R.id.linear);
        linear = billView.findViewById(R.id.linearpin);
        btnMap = billView.findViewById(R.id.btnMap);
        edSearchLocation = billView.findViewById(R.id.editText);
        search = billView.findViewById(R.id.search);
        btnNext = billView.findViewById(R.id.btnNext);
        spState = billView.findViewById(R.id.spinnerState);
        spCity = billView.findViewById(R.id.spinnerCity);
        spPincode = billView.findViewById(R.id.edPincode);
        btnSearchPin = billView.findViewById(R.id.btnSearch);
        btnSearchPin.setOnClickListener(this::onClick);
        //spPincode.setOnClickListener(this::onClick);
        tvLatLong = billView.findViewById(R.id.latlong);
        spState.setOnClickListener(this::onClick);
        spCity.setOnClickListener(this::onClick);
        btnMap.setOnClickListener(this::onClick);
        btnNext.setOnClickListener(this);
        search.setOnClickListener(this::onClick);
        return billView;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //autocompleteFragment.onActivityResult(requestCode, resultCode, data);
        if(requestCode == AUTO_COMP_REQ_CODE){
            if (resultCode == RESULT_OK) {
                com.google.android.gms.location.places.Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                Toast.makeText(getActivity(), "place "+place.toString(),
                        Toast.LENGTH_LONG).show();
            }else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getActivity(), data);
                Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == Activity.RESULT_CANCELED) {

            }

        }else if (resultCode == PLACE_PICKER_REQUEST) {

            Place place = PlacePicker.getPlace(getContext(), data);
            String placeName = String.format("Place: %s", place.getName());
            Toast.makeText(getActivity(), ""+placeName, Toast.LENGTH_SHORT).show();
            double latitude = place.getLatLng().latitude;
            double longitude = place.getLatLng().longitude;
            MarkerOptions markerOptions = new MarkerOptions();
            LatLng coordinate = new LatLng(latitude, longitude);
            markerOptions.position(coordinate);
            markerOptions.title(placeName); //Here Total Address is address which you want to show on marker
            mMap.clear();
            markerOptions.icon(
                    BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

            markerOptions.getPosition();
            mCurrLocationMarker = mMap.addMarker(markerOptions);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(coordinate));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        }
        //super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btnNext:
                callToFragment.communicateFragment(pos);
                break;

            case R.id.search:
                if(edSearchLocation.getText().toString().isEmpty()){
                    edSearchLocation.setError("Enter Area Name");
                }else {
                    searchLocation(v);
                }
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
                tvLatLong.setVisibility(View.VISIBLE);
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
                        android.R.layout.simple_list_item_1, android.R.id.text1, getStateList()){
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);
                        TextView text = (TextView) view.findViewById(android.R.id.text1);
                        text.setTextColor(Color.BLACK);
                        text.setTextSize(18);
                        return view;
                    }
                };
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
                        android.R.layout.simple_list_item_1, android.R.id.text1, getCityList()){
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);

                        /*if (position % 2 == 1) {
                            view.setBackgroundColor(Color.BLUE);
                        } else {
                            view.setBackgroundColor(Color.CYAN);
                        }*/

                        TextView text = (TextView) view.findViewById(android.R.id.text1);
                        text.setTextColor(Color.BLACK);
                        text.setTextSize(18);
                        return view;
                    }
                };
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


            case R.id.edPincode:

                break;

            case R.id.cancel_pincode:
                mDialogPincode.dismiss();
                break;
            case R.id.searhPincode:
                boolean digitsOnly = TextUtils.isDigitsOnly(pincode.getText().toString());
                if (digitsOnly) {
                    if (pincode.getText().toString().length() == 0) {
                        pincode.setError("Enter PostOffice Pincode");
                    } else {
                        getSearchPincodeMethod(pincode.getText().toString());
                    }
                }else {
                    getSearchNameOfPincodeMethod(pincode.getText().toString());
                }

                break;
            case R.id.btnSearch:

                mDialogPincode =  new Dialog(getContext());
                mDialogPincode.requestWindowFeature(Window.FEATURE_NO_TITLE);
                mDialogPincode.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                mDialogPincode.setContentView(R.layout.dialog_pincode_list);
                mDialogPincode.setCancelable(false);
                pincode = mDialogPincode.findViewById(R.id.editPincode);
                listviewPincode = mDialogPincode.findViewById(R.id.recyclerPincode);
                ImageView pincodeImg = mDialogPincode.findViewById(R.id.cancel_pincode);
                btnSearch = mDialogPincode.findViewById(R.id.searhPincode);
                btnSearch.setOnClickListener(this::onClick);
                pincodeImg.setOnClickListener(this::onClick);
                mDialogPincode.show();
                listviewPincode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Office pinName = (Office) parent.getItemAtPosition(position);
                        spPincode.setText(pinName.getPincode());
                        mDialogPincode.dismiss();

                    }
                });

                pincode.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                        try {
                            pincodeAdapter.getFilter().filter(s);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        try {
                            pincodeAdapter.getFilter().filter(s);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
                break;
        }
    }

    private void getSearchNameOfPincodeMethod(String namePincode) {
            if(ConnectivityReceiver.isConnected()) {
                customerCreationInterface = Customer_Client.getClient().create(CustomerCreationInterface.class);
                Call<List<PostOfficePincode>> listCall = customerCreationInterface.getPincodeName(namePincode);
                listCall.enqueue(new Callback<List<PostOfficePincode>>() {
                    @Override
                    public void onResponse(Call<List<PostOfficePincode>> call, Response<List<PostOfficePincode>> response) {

                        if(response.isSuccessful() && response.code() == 200){
                                pincodeArrayList.clear();
                                pincodeArrayList = response.body();
                                Log.i("Pin List", pincodeArrayList.toString());
                                    for (int i = 0; i < pincodeArrayList.size(); i++) {

                                        officeList = pincodeArrayList.get(i).getPostOffice();
                                        try {
                                            Log.i("City List", officeList.toString());
                                            pincodeAdapter = new PincodeAdapter(getActivity(), officeList);
                                            listviewPincode.setAdapter(pincodeAdapter);
                                            pincodeAdapter.notifyDataSetChanged();

                                        }catch (Exception e){
                                            pincodeAdapter = new PincodeAdapter(getActivity(), officeList);
                                            listviewPincode.setAdapter(pincodeAdapter);
                                            pincodeAdapter.notifyDataSetChanged();
                                            Toast.makeText(getActivity(), "Message: No records found", Toast.LENGTH_SHORT).show();
                                        }


                                    }
                        }else {

                        }
                    }
                    @Override
                    public void onFailure(Call<List<PostOfficePincode>> call, Throwable t) {

                    }
                });
        }else {
            Toast.makeText(getActivity(), "Please connect to internet.", Toast.LENGTH_SHORT).show();
        }

    }

    private void getSearchPincodeMethod(String pincode) {

        if(ConnectivityReceiver.isConnected()) {

            customerCreationInterface = Customer_Client.getClient().create(CustomerCreationInterface.class);
            Call<List<PostOfficePincode>> listCall = customerCreationInterface.getPincode(Integer.parseInt(pincode));
            listCall.enqueue(new Callback<List<PostOfficePincode>>() {
                @Override
                public void onResponse(Call<List<PostOfficePincode>> call, Response<List<PostOfficePincode>> response) {

                    if(response.isSuccessful() && response.code() == 200){

                        pincodeArrayList = response.body();
                        for(int i= 0; i < pincodeArrayList.size();i++){

                           officeList= pincodeArrayList.get(i).getPostOffice();
                           try {
                               Log.i("City List", "..." + officeList.toString());
                               pincodeAdapter = new PincodeAdapter(getActivity(), officeList);
                               listviewPincode.setAdapter(pincodeAdapter);
                               pincodeAdapter.notifyDataSetChanged();
                           }catch (Exception e){
                               pincodeAdapter = new PincodeAdapter(getActivity(), officeList);
                               listviewPincode.setAdapter(pincodeAdapter);
                               pincodeAdapter.notifyDataSetChanged();
                               Toast.makeText(getActivity(), "Message: No records found", Toast.LENGTH_SHORT).show();
                           }
                        }

                    }else {

                    }

                }

                @Override
                public void onFailure(Call<List<PostOfficePincode>> call, Throwable t) {

                }
            });
        }else {
            Toast.makeText(getActivity(), "Please connect to internet.", Toast.LENGTH_SHORT).show();
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
            mCurrLocationMarker = mMap.addMarker(new MarkerOptions().position(latLng).title(location));

            dataModel.put("title", location);
            dataModel.put("snipet", "This is my spot!");
            dataModel.put("latitude", latLng);;

            markers.put(mCurrLocationMarker, dataModel);
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

                @Override
                public void onInfoWindowClick(Marker position)
                {

                    Map dataModel = (Map) markers.get(position);
                    String title = (String) dataModel.toString();

                    getCompleteAddressString(address.getLatitude(),address.getLongitude());
                    //Toast.makeText(getActivity(), ""+dataModel.get("latitude"), Toast.LENGTH_SHORT).show();
                    openDialogMethod(location,"This is my spot!",getCompleteAddressString(address.getLatitude(),address.getLongitude()));

                }
            });
            Toast.makeText(getActivity(),address.getLatitude()+" "+address.getLongitude(),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
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

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                try {
                    Map dataModel = (Map) markers.get(marker);
                    String title = (String) dataModel.toString();
                    //Toast.makeText(getContext(), "" + title, Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    e.printStackTrace();
                }

                //openPlacePicker();
                return false;
            }
        });

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker position)
            {

                    Map dataModel = (Map) markers.get(position);
                    String title = (String) dataModel.toString();

                    //Toast.makeText(getActivity(), ""+dataModel.get("latitude"), Toast.LENGTH_SHORT).show();
                    openDialogMethod(dataModel.get("title").toString(),dataModel.get("snippet").toString(),tvLatLong.getText().toString());

            }
        });


    }

    private void openDialogMethod(String title, String snippet, String toString) {

        mDialogOpen = new Dialog(getActivity());
        mDialogOpen.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialogOpen.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialogOpen.setContentView(R.layout.dialog_address);
        mDialogOpen.setCancelable(true);
        mDialogOpen.show();
        tvAtmName = mDialogOpen.findViewById(R.id.tvAtmName);
        tvAtmArea = mDialogOpen.findViewById(R.id.tvAtmArea);
        tvAreaLatitude = mDialogOpen.findViewById(R.id.tvAtmLat);


        tvAtmName.setText("Name : "+areaName);
        tvAtmArea.setText("Address : "+snippet);
        tvAreaLatitude.setText(toString);
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
        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        markerOptions = new MarkerOptions();
        markerOptions.position(latLng);


        try {
            Log.i("12343", String.valueOf(latLng));
            tvLatLong.setText("Your Position: " + latLng + "\n Address: " + getCompleteAddressString(location.getLatitude(), location.getLongitude()));
        }catch (Exception e){
            e.printStackTrace();
        }
        markerOptions.title("Current Position : " +String.valueOf(latLng));
        markerOptions.anchor(0.5f, 0.5f);
        markerOptions.snippet(getCompleteAddressString(location.getLatitude(),location.getLongitude()));
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

        dataModel.put("title", "Current Position");
        dataModel.put("snippet", "This is my spot!");
        dataModel.put("latitude", latLng);;
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        markers.put(mCurrLocationMarker, dataModel);



        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(20));
        mMap.getMapType();

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }




    }
    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                areaName = addresses.get(0).getSubLocality();
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("My loction address", strReturnedAddress.toString());
            } else {
                Log.w("My loction address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My loction address", "Canont get Address!");
        }
        return strAdd;
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
            stateList.clear();
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
            cityList.clear();
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
