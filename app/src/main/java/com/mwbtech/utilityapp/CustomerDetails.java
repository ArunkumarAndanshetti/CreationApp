package com.mwbtech.utilityapp;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CustomerDetails extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {


    Spinner spLedger,spCompanyType,spSalesmanType;
    Button btnNext;
    View customerView;
    int pos = 1;
    String[] ledger = { "Select", "Credit", "Debit"};
    String[] company = {"Select","Proprietorship/Partnership","Pvt Ltd","Public Ltd","Society"};
    ArrayAdapter ledgerAdapter,companyAdapter;
    CallToBillFragment callToBillFragment;
    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        try {

            if(activity instanceof CallToBillFragment) {
                callToBillFragment = (CallToBillFragment) activity;
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
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        customerView = inflater.inflate(R.layout.customer_details, null);
        btnNext = customerView.findViewById(R.id.btnNext);
        spLedger = customerView.findViewById(R.id.spinnerLedger);
        spCompanyType = customerView.findViewById(R.id.spinnerCompany);
        ledgerAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, ledger){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(Color.BLACK);
                text.setTextSize(18);
                return view;
            }
        };
        ledgerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spLedger.setAdapter(ledgerAdapter);

        companyAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, company){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(Color.BLACK);
                text.setTextSize(18);
                return view;
            }
        };
        companyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCompanyType.setAdapter(companyAdapter);

        spCompanyType.setOnItemSelectedListener(this);
        spLedger.setOnItemSelectedListener(this);


        btnNext.setOnClickListener(this);
        return customerView;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btnNext:
                callToBillFragment.callingBillingFragment(pos);
                break;

        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){

            case R.id.spinnerLedger:
                String countryName = (String) spLedger.getItemAtPosition(position);
                Toast.makeText(getActivity(),countryName , Toast.LENGTH_LONG).show();
                break;
            case R.id.spinnerCompany:

                String companyName = (String) spCompanyType.getItemAtPosition(position);
                Toast.makeText(getActivity(),companyName , Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    interface CallToBillFragment {

        void callingBillingFragment(int pos);

    }
}
