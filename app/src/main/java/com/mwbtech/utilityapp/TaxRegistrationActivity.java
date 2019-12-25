package com.mwbtech.utilityapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.theartofdev.edmodo.cropper.CropImage;


public class TaxRegistrationActivity extends Fragment {

    Button btnNext;

    TextView tvGST,tvPan;
    Bitmap image;
    ImageView imageView,imageView1;
    int count = 0;
    ImageView imgGST,imgPan;
    View taxView;
    MainActivity mainActivity;
    CallToBankFragment callToBankFragment;
    int pos = 3;
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
        imageView = taxView.findViewById(R.id.pickGalleryCamera);
        imageView1 = taxView.findViewById(R.id.pickCamera);
        imgGST = taxView.findViewById(R.id.imageGST);
        imgPan = taxView.findViewById(R.id.imagePan);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count = 0;
                imgGST.setVisibility(View.GONE);
                Intent intent = CropImage.activity()
                        .getIntent(getContext());

                startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        });
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count = 1;
                imgPan.setVisibility(View.GONE);
                Intent intent = CropImage.activity()
                        .getIntent(getContext());

                startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        });


        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callToBankFragment.callingBankingFragment(pos);
            }
        });

        return taxView;

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
                    imgGST.setVisibility(View.VISIBLE);
                    imgGST.setImageBitmap(image);
                    methodRecongizerGSTPAN(count);
                }else {
                    imgPan.setVisibility(View.VISIBLE);
                    imgPan.setImageBitmap(image);
                    methodRecongizerGSTPAN(count);
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(getActivity(), ""+error, Toast.LENGTH_SHORT).show();
            }
        }
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
            Toast.makeText(getContext(), "" + imageText, Toast.LENGTH_SHORT).show();
        }else {
            tvPan.setText(""+imageText);
            Toast.makeText(getActivity(), ""+imageText, Toast.LENGTH_SHORT).show();
        }
    }


    interface CallToBankFragment {
        void callingBankingFragment(int pos);
    }

}
