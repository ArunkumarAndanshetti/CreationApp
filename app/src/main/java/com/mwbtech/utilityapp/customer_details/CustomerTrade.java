package com.mwbtech.utilityapp.customer_details;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.mwbtech.utilityapp.R;
import com.mwbtech.utilityapp.objects.CustomerCreation;
import com.mwbtech.utilityapp.retrofit_client.Customer_Client;
import com.mwbtech.utilityapp.retrofit_interface.CustomerCreationInterface;
import com.mwbtech.utilityapp.widget.ShowProgressDialog;
import com.myhexaville.smartimagepicker.ImagePicker;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mwbtech.utilityapp.main_page.MainActivity.createdByID;
import static com.mwbtech.utilityapp.main_page.MainActivity.creation;
import static com.mwbtech.utilityapp.main_page.MainActivity.customerCreation;
import static com.mwbtech.utilityapp.main_page.MainActivity.orgID;
import static com.mwbtech.utilityapp.main_page.MainActivity.prefManager;
import static com.mwbtech.utilityapp.main_page.MainActivity.salesmanID;

public class CustomerTrade extends Fragment implements View.OnClickListener{

    public View tradeView;
    public EditText edExpiryDate;
    public Dialog mDialog;
    public CalendarView calendarView;
    public ImageView cancleCalendar;
    AwesomeValidation awesomeValidation;
    EditText edOutStanding,edCreditDays,edCreditLimit,edOpenBal;
    Button btnNext;
    CustomerCreationInterface customerCreationInterface;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        tradeView = inflater.inflate(R.layout.customer_trade, null);
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        edExpiryDate = tradeView.findViewById(R.id.edInsuranceDate);
        edOutStanding = tradeView.findViewById(R.id.edOutStandingBill);
        edCreditDays = tradeView.findViewById(R.id.edCredit);
        edCreditLimit = tradeView.findViewById(R.id.edCreditLimit);
        edOpenBal = tradeView.findViewById(R.id.edBalance);
        btnNext = tradeView.findViewById(R.id.btnNext);
        edExpiryDate.setOnClickListener(this);
        btnNext.setOnClickListener(this::onClick);
        sharePrefernceData();
        return tradeView;
    }

    private void sharePrefernceData() {
        creation = prefManager.getSavedObjectFromPreference(getContext(),"mwb-welcome","customer", CustomerCreation.class);
        if(creation != null){
            edOutStanding.setText(""+creation.getNoofOutstandingBill());
            edCreditLimit.setText(""+creation.getCrLimit());
            edCreditDays.setText(""+creation.getCrDays());
            edOpenBal.setText(""+creation.getOpeningBalance());
        }else {
            edOutStanding.setText("");
            edCreditLimit.setText("");
            edCreditDays.setText("");
            edOpenBal.setText("");
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        validationMethod();
    }

    private void validationMethod() {

        awesomeValidation.addValidation(getActivity(),R.id.edOutStandingBill, RegexTemplate.NOT_EMPTY, R.string.bank_name);
        awesomeValidation.addValidation(getActivity(),R.id.edCredit,RegexTemplate.NOT_EMPTY, R.string.branch);
        awesomeValidation.addValidation(getActivity(),R.id.edCreditLimit,RegexTemplate.NOT_EMPTY, R.string.bank_city);
        awesomeValidation.addValidation(getActivity(),R.id.edBalance,RegexTemplate.NOT_EMPTY, R.string.acc_no);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.edInsuranceDate:
                mDialog = new Dialog(getContext());
                mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                mDialog.getWindow().setBackgroundDrawable(
                        new ColorDrawable(android.graphics.Color.TRANSPARENT));
                mDialog.setContentView(R.layout.dialog_calendar);
                mDialog.setCancelable(false);
                mDialog.show();
                calendarView = (CalendarView) mDialog.findViewById(R.id.calendarView);
                cancleCalendar = mDialog.findViewById(R.id.cancel_calendar);
                cancleCalendar.setOnClickListener(this::onClick);
                calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {

                        String msg = "Selected date Day: " + dayOfMonth + " Month : " + (month + 1) + " Year " + year;
                        String date = String.valueOf(dayOfMonth+"/"+(month + 1)+"/"+year);
                        Toast.makeText(getActivity(), date, Toast.LENGTH_SHORT).show();
                        edExpiryDate.setText(""+date);
                        mDialog.dismiss();
                    }
                });
                break;
            case R.id.cancel_calendar:
                mDialog.dismiss();
                break;

            case R.id.btnNext:

                if(awesomeValidation.validate()){
                    creation = prefManager.getSavedObjectFromPreference(getContext(),"mwb-welcome","customer", CustomerCreation.class);
                    customerCreation = new CustomerCreation(creation.getLedgerType(), creation.getFirmName(), creation.getCompanyType(), creation.getName(), creation.getEmailID(), creation.getMobileNumber(), creation.getMobileNumber2(), creation.getTelephoneNumber(), creation.getBillingAddress(), creation.getArea(), creation.getCity(), creation.getCityCode(), creation.getState(), creation.getPincode(), creation.getLattitude(), creation.getLangitude(), creation.getRegistrationType(), creation.getTinNumber(), creation.getPanNumber(), creation.getGstImage(), creation.getPanImage(),creation.getBankName(),creation.getBankBranch(),creation.getBankCity(),creation.getAccountNo(),creation.getIfscCode(),creation.getSignatureImage(),creation.getComment(),Integer.parseInt(edCreditLimit.getText().toString()),Integer.parseInt(edCreditDays.getText().toString()),Integer.parseInt(edOutStanding.getText().toString()),Integer.parseInt(edOpenBal.getText().toString()),salesmanID,createdByID,orgID);
                    prefManager.saveObjectToSharedPreference("customer", customerCreation);
                    Toast.makeText(getActivity(), "Saved", Toast.LENGTH_SHORT).show();
                    callToServerMethod();
                }

                break;

        }
    }

    private void callToServerMethod() {
        ProgressDialog dialog = ShowProgressDialog.createProgressDialog(getContext());
        customerCreationInterface = Customer_Client.getClientCreation().create(CustomerCreationInterface.class);
        Call<CustomerCreation> creationCall = customerCreationInterface.postCustomerCreationMethod(customerCreation);
        creationCall.enqueue(new Callback<CustomerCreation>() {
            @Override
            public void onResponse(Call<CustomerCreation> call, Response<CustomerCreation> response) {
                Log.i("#######", String.valueOf(customerCreation));
                if (response.code() == 201) {
                    Log.i("#######12", String.valueOf(response.code()));
                    dialog.dismiss();
                    Toast.makeText(getActivity(), "Saved.", Toast.LENGTH_SHORT).show();
                } else {
                    Log.i("#######124", String.valueOf(response.code()));
                    dialog.dismiss();
                    Toast.makeText(getActivity(), "Failed.", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<CustomerCreation> call, Throwable t) {
                Log.i("#######", String.valueOf(t.getMessage()));
                dialog.dismiss();
                Toast.makeText(getActivity(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }




}
