package com.mwbtech.utilityapp;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



public class TaxRegistrationActivity extends Fragment implements View.OnClickListener {

    Button btnNext,btnUploadPan,btnUploadGst;
    EditText tvGST,tvPan,tvReGST,tvRePan;
    Bitmap image;
    ImageView imageView,imageView1;
    int count = 0;
    ImageView imgGST,imgPan;
    View taxView;
    MainActivity mainActivity;
    CallToBankFragment callToBankFragment;
    int pos = 3;
    ImageView gstDoc,panDoc;

    static int IMAGE_PICKER = 12;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        try {

            if(activity instanceof CallToBankFragment) {
                callToBankFragment = (CallToBankFragment) activity;
            }else {
                throw new RuntimeException(activity.toString()
                        + " must implement OnGreenFragmentListener");
            }
        }catch (ClassCastException e){

        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       taxView = inflater.inflate(R.layout.tax_registration, null);
        mainActivity = (MainActivity) this.getActivity();
        btnNext = taxView.findViewById(R.id.btnNext);
        tvGST = taxView.findViewById(R.id.gstNumber);
        tvPan = taxView.findViewById(R.id.PanNumber);
        tvReGST = taxView.findViewById(R.id.regstNumber);
        tvRePan = taxView.findViewById(R.id.rePanNumber);
        imageView = taxView.findViewById(R.id.pickGalleryCamera);
        imageView1 = taxView.findViewById(R.id.pickCamera);
        imgGST = taxView.findViewById(R.id.imageGST);
        imgPan = taxView.findViewById(R.id.imagePan);

        gstDoc = taxView.findViewById(R.id.imageFront);
        panDoc = taxView.findViewById(R.id.imageFront1);

        btnUploadGst = taxView.findViewById(R.id.btnUploadGst);
        btnUploadPan = taxView.findViewById(R.id.btnUploadPan);

        btnUploadGst.setOnClickListener(this);
        btnUploadPan.setOnClickListener(this);

        imageView.setOnClickListener(this::onClick);
        imageView1.setOnClickListener(this::onClick);
        btnNext.setOnClickListener(this::onClick);

        return taxView;

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            Log.i("456",result.toString());
            if (resultCode == Activity.RESULT_OK) {
                Uri resultUri = result.getUri();
                Log.i("456",resultUri.toString());
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                image = BitmapFactory.decodeFile(resultUri.getPath(),bmOptions);
                String targetPath = resultUri.getPath();
                //imageView.setImageBitmap(image);

                Log.i("456",image.toString());

                if(count == 0) {
                    //imgGST.setVisibility(View.VISIBLE);
                    //imgGST.setImageBitmap(image);
                    methodRecongizerGSTPAN(count);
                }else if(count == 1){
                    //imgPan.setVisibility(View.VISIBLE);
                    //imgPan.setImageBitmap(image);
                    methodRecongizerGSTPAN(count);
                }else if(count == 2){
                    gstDoc.setImageBitmap(image);
                }else {
                    panDoc.setImageBitmap(image);
                }

            }else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(getActivity(), ""+error, Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
        // THIS METHOD SHOULD BE HERE so that ImagePicker works with fragment
    }


    public void methodRecongizerGSTPAN(int count){
        TextRecognizer textRecognizer = new TextRecognizer.Builder(getActivity()).build();
        Frame imageFrame = new Frame.Builder()
                .setBitmap(image)                 // your image bitmap
                .build();
        String imageText = "";
        SparseArray<TextBlock> textBlocks = textRecognizer.detect(imageFrame);
        for (int i = 0; i < textBlocks.size(); i++) {
            TextBlock textBlock = textBlocks.get(textBlocks.keyAt(i));
            imageText = textBlock.getValue();                   // return string
        }
        if(count == 0) {
            tvGST.setText(imageText);
            tvReGST.setText(imageText);
            validationGST();
            Toast.makeText(getContext(), "" + imageText, Toast.LENGTH_SHORT).show();
        }else {
            tvPan.setText(imageText);
            tvRePan.setText(imageText);
            validationPAN();
            Toast.makeText(getActivity(), ""+imageText, Toast.LENGTH_SHORT).show();
        }
    }

    private void validationPAN() {
        if(tvPan.getText().toString().equals(tvRePan.getText().toString())){
        }else {
            tvRePan.setError("GST Number and Reenter GST Number not matched.");
        }
    }

    private void validationGST() {

        if(tvGST.getText().toString().equals(tvReGST.getText().toString())){
        }else {
            tvReGST.setError("Pan Number and Reenter Pan Number not matched.");
        }

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.pickGalleryCamera:
                count = 0;
                imgGST.setVisibility(View.GONE);
                intent = CropImage.activity()
                        .getIntent(getContext());

                startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);

                break;

            case R.id.pickCamera:
                count = 1;
                imgPan.setVisibility(View.GONE);
                intent = CropImage.activity()
                        .getIntent(getContext());

                startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);

                break;


            case R.id.btnUploadGst:
                count = 2;
                intent = CropImage.activity()
                        .getIntent(getContext());

                startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
                break;

            case R.id.btnUploadPan:
                count = 3;;
                intent = CropImage.activity()
                        .getIntent(getContext());
                startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);;

                break;
            case R.id.btnNext:

                callToBankFragment.callingBankingFragment(pos);
                break;

        }
    }

    interface CallToBankFragment {
        void callingBankingFragment(int pos);
    }



}
