package com.mwbtech.utilityapp.customer_details;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationHolder;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.basgeekball.awesomevalidation.utility.custom.CustomErrorReset;
import com.basgeekball.awesomevalidation.utility.custom.CustomValidation;
import com.basgeekball.awesomevalidation.utility.custom.CustomValidationCallback;
import com.mwbtech.utilityapp.Preferences.PrefManager;
import com.mwbtech.utilityapp.R;
import com.mwbtech.utilityapp.objects.CustomerCreation;

import java.util.regex.Pattern;

import static com.mwbtech.utilityapp.main_page.MainActivity.creation;
import static com.mwbtech.utilityapp.main_page.MainActivity.customerCreation;
import static com.mwbtech.utilityapp.main_page.MainActivity.prefManager;

public class CustomerDetails extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {


    Spinner spLedger,spCompanyType,spSalesmanType;
    Button btnNext;
    View customerView;
    int pos = 1;
    String[] ledger = { "Select", "Credit", "Debit"};
    String[] company = {"Select","Proprietorship/Partnership","Pvt Ltd","Public Ltd","Society"};
    ArrayAdapter ledgerAdapter,companyAdapter;
    CallToBillFragment callToBillFragment;


    EditText edFirmName,edOwnerName,edMobileNo,edMobileNo1,edEmailId,edTelephone;

    String ledgerType,companyType;
    AwesomeValidation awesomeValidation;


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
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        edFirmName = customerView.findViewById(R.id.edFirmName);
        edOwnerName = customerView.findViewById(R.id.edCustomer);
        edEmailId = customerView.findViewById(R.id.edEmailId);
        edMobileNo = customerView.findViewById(R.id.edOwnerNo);
        edMobileNo1 = customerView.findViewById(R.id.edMobile);
        btnNext = customerView.findViewById(R.id.btnNext);
        edTelephone = customerView.findViewById(R.id.edtelephone);
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
        sharePreferencesMethod();
        return customerView;
    }


    private boolean isValidEmailId(String email){

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }


    private void sharePreferencesMethod() {

        creation = prefManager.getSavedObjectFromPreference(getContext(),"mwb-welcome","customer", CustomerCreation.class);
        if(creation != null){
            if(creation.getLedgerType().equals("Credit")){
                spLedger.setSelection(1);
            }else {
                spLedger.setSelection(2);
            }

            if(creation.getCompanyType().equals("Proprietorship/Partnership")){
                spCompanyType.setSelection(1);

            }else if(creation.getCompanyType().equals("Pvt Ltd")){
                spCompanyType.setSelection(2);

            }else if(creation.getCompanyType().equals("Public Ltd")){

                spCompanyType.setSelection(3);
            }else {
                spCompanyType.setSelection(4);
            }
            edFirmName.setText(""+creation.getFirmName());
            edOwnerName.setText(""+creation.getName());
            edMobileNo.setText(""+creation.getMobileNumber());
            edMobileNo1.setText(""+creation.getMobileNumber2());
            edTelephone.setText(""+creation.getTelephoneNumber());
            edEmailId.setText(""+creation.getEmailID());

        }


    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        valdationMethod();
    }

    private void valdationMethod() {

        awesomeValidation.addValidation(getActivity(),R.id.edFirmName, RegexTemplate.NOT_EMPTY, R.string.firmname);
        awesomeValidation.addValidation(getActivity(),R.id.edCustomer,RegexTemplate.NOT_EMPTY, R.string.customername);
        awesomeValidation.addValidation(getActivity(),R.id.edEmailId,RegexTemplate.NOT_EMPTY, R.string.email_id);
        awesomeValidation.addValidation(getActivity(),R.id.edEmailId, Patterns.EMAIL_ADDRESS, R.string.validation_email);
        awesomeValidation.addValidation(getActivity(),R.id.edOwnerNo,RegexTemplate.NOT_EMPTY, R.string.owner_no);
        awesomeValidation.addValidation(getActivity(), R.id.edMobile, RegexTemplate.NOT_EMPTY, R.string.mobile_no);
        awesomeValidation.addValidation(getActivity(), R.id.edtelephone, RegexTemplate.NOT_EMPTY, R.string.telephone_bo);

        awesomeValidation.addValidation(getActivity(),R.id.spinnerLedger,new CustomValidation() {
            @Override
            public boolean compare(ValidationHolder validationHolder) {
                if (((Spinner) validationHolder.getView()).getSelectedItem().toString().equals("Select")) {
                    return false;
                } else {
                    return true;
                }
            }
        }, new CustomValidationCallback() {
            @Override
            public void execute(ValidationHolder validationHolder) {
                TextView textViewError = (TextView) ((Spinner) validationHolder.getView()).getSelectedView();
                textViewError.setError(validationHolder.getErrMsg());
                textViewError.setTextColor(Color.RED);
            }
        }, new CustomErrorReset() {
            @Override
            public void reset(ValidationHolder validationHolder) {
                TextView textViewError = (TextView) ((Spinner) validationHolder.getView()).getSelectedView();
                textViewError.setError(null);
                textViewError.setTextColor(Color.BLACK);
            }
        }, R.string.ledger_spinner);
        awesomeValidation.addValidation(getActivity(),R.id.spinnerCompany, new CustomValidation() {
            @Override
            public boolean compare(ValidationHolder validationHolder) {
                if (((Spinner) validationHolder.getView()).getSelectedItem().toString().equals("Select")) {
                    return false;
                } else {
                    return true;
                }
            }
        }, new CustomValidationCallback() {
            @Override
            public void execute(ValidationHolder validationHolder) {
                TextView textViewError = (TextView) ((Spinner) validationHolder.getView()).getSelectedView();
                textViewError.setError(validationHolder.getErrMsg());
                textViewError.setTextColor(Color.RED);
            }
        }, new CustomErrorReset() {
            @Override
            public void reset(ValidationHolder validationHolder) {
                TextView textViewError = (TextView) ((Spinner) validationHolder.getView()).getSelectedView();
                textViewError.setError(null);
                textViewError.setTextColor(Color.BLACK);
            }
        }, R.string.company_spinner);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btnNext:

                if(awesomeValidation.validate()){

                    customerCreation = new CustomerCreation(ledgerType,edFirmName.getText().toString(),companyType,edOwnerName.getText().toString(),edEmailId.getText().toString(),edMobileNo.getText().toString(),edMobileNo1.getText().toString(),edTelephone.getText().toString());
                    prefManager.saveObjectToSharedPreference("customer",customerCreation);
                    Toast.makeText(getContext(), "saved", Toast.LENGTH_SHORT).show();
                    callToBillFragment.callingBillingFragment(pos);
                }
                break;

        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){

            case R.id.spinnerLedger:
                ledgerType = (String) spLedger.getItemAtPosition(position);
                Toast.makeText(getActivity(),ledgerType , Toast.LENGTH_LONG).show();
                break;
            case R.id.spinnerCompany:

                companyType = (String) spCompanyType.getItemAtPosition(position);
                Toast.makeText(getActivity(),companyType, Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public interface CallToBillFragment {

        void callingBillingFragment(int pos);

    }
}
