package com.mwbtech.utilityapp;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.myhexaville.smartimagepicker.ImagePicker;

public class CustomerTrade extends Fragment implements View.OnClickListener{

    public View tradeView;
    public EditText edExpiryDate;
    public Dialog mDialog;
    public CalendarView calendarView;
    public ImageView cancleCalendar;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        tradeView = inflater.inflate(R.layout.customer_trade, null);
        edExpiryDate = tradeView.findViewById(R.id.edInsuranceDate);
        edExpiryDate.setOnClickListener(this);
        return tradeView;
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

        }
    }
}
