package com.mwbtech.utilityapp.tax_register;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationHolder;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.basgeekball.awesomevalidation.utility.custom.CustomErrorReset;
import com.basgeekball.awesomevalidation.utility.custom.CustomValidation;
import com.basgeekball.awesomevalidation.utility.custom.CustomValidationCallback;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.mwbtech.utilityapp.Preferences.PrefManager;
import com.mwbtech.utilityapp.main_page.MainActivity;
import com.mwbtech.utilityapp.R;
import com.mwbtech.utilityapp.objects.CustomerCreation;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static com.mwbtech.utilityapp.main_page.MainActivity.creation;
import static com.mwbtech.utilityapp.main_page.MainActivity.customerCreation;
import static com.mwbtech.utilityapp.main_page.MainActivity.prefManager;


public class TaxRegistrationActivity extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

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
    Spinner spTax;
    String[] tax = {"Select","Register","Unregister"};
    static int IMAGE_PICKER = 12;
    ArrayAdapter taxAdapter;
    String taxType;
    AwesomeValidation awesomeValidation;
    String result3,result4,gstImage = null,panImage = null;
    Bitmap bitGst,bitPan;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //https://github.com/eddydn/AndroidCameraRecognitionText/blob/master/app/src/main/java/dev/edmt/androidcamerarecognitiontext/MainActivity.java
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
        spTax = taxView.findViewById(R.id.spinnerTax);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        prefManager.getSavedObjectFromPreference(getContext(),"mwb-welcome", "customer",CustomerCreation.class);
        Log.i("12233",prefManager.getSavedObjectFromPreference(getContext(),"mwb-welcome", "customer",CustomerCreation.class).toString());
        //Toast.makeText(mainActivity, ""+prefManager.getSavedObjectFromPreference(getContext(),"mwb-welcome", "customer",CustomerCreation.class), Toast.LENGTH_SHORT).show();
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


        taxAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, tax){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(Color.BLACK);
                text.setTextSize(18);
                return view;
            }
        };
        taxAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTax.setAdapter(taxAdapter);
        spTax.setOnItemSelectedListener(this);

        tvReGST.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(tvGST.getText().toString().equals(tvReGST.getText().toString())){
                    tvReGST.setError("GST Number matched.");
                    tvReGST.setError(null);
                }else {
                    tvReGST.setError("GST Number and Re enter GST Number not matched.");
                }
            }
        });
        tvRePan.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(tvPan.getText().toString().equals(tvRePan.getText().toString())){
                    tvRePan.setError("Pan Number matched.");
                    tvRePan.setError(null);
                }else {
                    tvRePan.setError("Pan Number and Re enter Pan Number not matched.");
                }
            }
        });
        sharePrefernceData();
        imageView.setOnClickListener(this::onClick);
        imageView1.setOnClickListener(this::onClick);
        btnNext.setOnClickListener(this::onClick);

        return taxView;
    }


    private void sharePrefernceData() {
        creation = prefManager.getSavedObjectFromPreference(getContext(),"mwb-welcome","customer", CustomerCreation.class);
        if(creation.getRegistrationType() != null){
            if(creation.getRegistrationType().equals("Select")){
                spTax.setSelection(0);
            }
            else if(creation.getRegistrationType().equals("Register")){
                spTax.setSelection(1);
            }else {
                spTax.setSelection(2);
            }
            tvGST.setText(""+creation.getTinNumber());
            tvReGST.setText(""+creation.getTinNumber());
            tvPan.setText(""+creation.getPanNumber());
            tvRePan.setText(""+creation.getPanNumber());
            try {
                gstDoc.setImageBitmap(decodeBase64(creation.getGstImage()));
                panDoc.setImageBitmap(decodeBase64(creation.getPanImage()));
            }catch (Exception e){
                e.printStackTrace();
            }
        }else {
            spTax.setSelection(0);
            tvGST.setText("");
            tvReGST.setText("");
            tvPan.setText("");
            tvRePan.setText("");
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        validationMethod();
    }

    private void validationMethod() {

        awesomeValidation.addValidation(getActivity(),R.id.gstNumber, RegexTemplate.NOT_EMPTY, R.string.gst_no);
        awesomeValidation.addValidation(getActivity(),R.id.PanNumber,RegexTemplate.NOT_EMPTY, R.string.pan_no);
        awesomeValidation.addValidation(getActivity(),R.id.spinnerTax,new CustomValidation() {
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
        }, R.string.tax_spinner);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == Activity.RESULT_OK) {
                Uri resultUri = result.getUri();
                Log.i("456",resultUri.toString());
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                image = BitmapFactory.decodeFile(resultUri.getPath(),bmOptions);
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), resultUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                int height = (bitmap.getHeight()/4);
                int width = (bitmap.getWidth()/4);
                Log.i("4568",image.toString());

                if(count == 0) {
                    //imgGST.setVisibility(View.VISIBLE);
                    //imgGST.setImageBitmap(image);
                    methodRecongizerGSTPAN(count);
                }else if(count == 1){
                    //imgPan.setVisibility(View.VISIBLE);
                    //imgPan.setImageBitmap(image);
                    methodRecongizerGSTPAN(count);
                }else if(count == 2){
                    gstDoc.setImageBitmap(bitmap);
                    try {
                        result3 = resultUri.toString().replace("file://","");

                         bitGst = Bitmap.createScaledBitmap(bitmap, width, height, true);
                        //customerCreation = new CustomerCreation(customerCreation.getName(), customerCreation.getAddress(), customerCreation.getLocation(), getPathMethod(result3));
                        //prefManager.saveObjectToSharedPreference(getContext(), "mwb-welcome", "customer", customerCreation);
                        //Toast.makeText(getActivity(), "saved", Toast.LENGTH_SHORT).show();
                        //Log.i("823",decodeBase64(customerCreation.getImage()).toString());
                        //panDoc.setImageBitmap(decodeBase64(customerCreation.getImage()));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else {
                    //panDoc.setImageBitmap(decodeBase64(customerCreation.getImage()));
                    panDoc.setImageBitmap(bitmap);
                    try {
                        result4 = resultUri.toString().replace("file://", "");
                        bitPan = Bitmap.createScaledBitmap(bitmap, width, height, true);

                        Log.i("12333", prefManager.getSavedObjectFromPreference(getContext(), "mwb-welcome", "customer", CustomerCreation.class).toString());
                        //Toast.makeText(mainActivity, ""+prefManager.getSavedObjectFromPreference(getContext(),"mwb-welcome", "customer",CustomerCreation.class), Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

            }else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(getActivity(), ""+error, Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
        // THIS METHOD SHOULD BE HERE so that ImagePicker works with fragment
    }
    private String getBase64String(Bitmap bitmap)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String base64String = Base64.encodeToString(imageBytes, Base64.NO_WRAP);
        return base64String;
    }


    private String getPathMethod(String toString) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        Bitmap bitmap = BitmapFactory.decodeFile(toString);
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, baos);
        //int nh = (int) ( bitmap.getHeight() * (512.0 / bitmap.getWidth()));
        //Bitmap.createScaledBitmap(bitmap, 512, nh, true);
        byte[] imageBytes = baos.toByteArray();
        String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        Log.i("Encode",imageString);
        return imageString;
    }


    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    public void methodRecongizerGSTPAN(int count){
        TextRecognizer textRecognizer = new TextRecognizer.Builder(getActivity()).build();
        if (!textRecognizer.isOperational()) {
            new AlertDialog.Builder(getActivity())
                    .setTitle("Text recognizer")
                    .setMessage("Text recognizer could not be set up on your device :(")
                    .show();
            return;
        }

        Frame imageFrame = new Frame.Builder().setBitmap(image)                 // your image bitmap
                .build();

        Log.i("4567",imageFrame.toString());
        String imageText = "";
        SparseArray<TextBlock> textBlocks = textRecognizer.detect(imageFrame);
        for (int i = 0; i < textBlocks.size(); i++) {
            TextBlock textBlock = textBlocks.get(textBlocks.keyAt(i));
            imageText = textBlock.getValue();                   // return string
        }
        Log.i("4569",imageText.toString());
        if(count == 0) {
            tvGST.setText(imageText);
            tvReGST.setText(imageText);
            Toast.makeText(getContext(), "" + imageText, Toast.LENGTH_SHORT).show();
        }else {
            tvPan.setText(imageText);
            tvRePan.setText(imageText);
            Toast.makeText(getActivity(), ""+imageText, Toast.LENGTH_SHORT).show();
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

                if(awesomeValidation.validate()) {

                    if(result3 == null){
                        gstImage = null;
                    }else {
                        gstImage = getBase64String(bitGst);
                    }
                    if(result4 == null) {
                        panImage = null;
                    }else {
                        panImage = getBase64String(bitPan);
                    }
                    try {

                        creation = prefManager.getSavedObjectFromPreference(getContext(),"mwb-welcome","customer", CustomerCreation.class);
                        customerCreation = new CustomerCreation(creation.getLedgerType(), creation.getFirmName(), creation.getCompanyType(), creation.getName(), creation.getEmailID(), creation.getMobileNumber(), creation.getMobileNumber2(), creation.getTelephoneNumber(), creation.getBillingAddress(), creation.getArea(), creation.getCity(), creation.getCityCode(), creation.getState(), creation.getPincode(), creation.getLattitude(), creation.getLangitude(), taxType, tvGST.getText().toString(), tvPan.getText().toString(), gstImage, panImage);
                        prefManager.saveObjectToSharedPreference("customer", customerCreation);
                        Toast.makeText(getActivity(), "Saved", Toast.LENGTH_SHORT).show();
                        callToBankFragment.callingBankingFragment(pos);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                break;

        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        taxType = (String) parent.getItemAtPosition(position);
        Toast.makeText(getActivity(), ""+taxType, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public interface CallToBankFragment {
        void callingBankingFragment(int pos);
    }



}
