package com.mwbtech.utilityapp;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CustomerDetails extends Fragment implements View.OnClickListener {


    Spinner spLedger,spCompanyType,spSalesmanType;
    Button btnNext;
    View customerView;
    int pos = 1;

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

    interface CallToBillFragment {

        void callingBillingFragment(int pos);

    }
}
