package com.mwbtech.utilityapp;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BankDetailsActvity extends Fragment implements View.OnClickListener {

    public Button btnNext,btnClear,btnSave;
    public CalendarView calendarView;
    public EditText edExpiryDate;
    public Dialog mDialog,mDialogSign;
    public ImageView cancleCalendar,cancel_sign;
    public View bankView;
    private LinearLayout canvasLL;
    private View view;
    private Signature mSignature;
    private Bitmap bitmap;
    public File file;
    private static final int READ_STORAGE = 100;
    private static final int WRITE_STORAGE = 200;
    public String DIRECTORY = Environment.getExternalStorageDirectory().getPath() + "/Signature/";
    public String pic_name = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
    public String StoredPath = DIRECTORY + pic_name + ".PNG";
    customerTradeFragment customerTrade;
    int pos = 4;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        try {

            if(activity instanceof customerTradeFragment) {
                customerTrade = (customerTradeFragment) activity;
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
        bankView = inflater.inflate(R.layout.bank_details, null);
        //edExpiryDate = bankView.findViewById(R.id.edInsuranceDate);
        btnNext = bankView.findViewById(R.id.btnNext);
//        edExpiryDate.setOnClickListener(this);
        btnNext.setOnClickListener(this::onClick);
        isReadStoragePermissionGranted();
        isWriteStoragePermissionGranted();
        return bankView;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            /*case R.id.edInsuranceDate:
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
                break;*/
            case R.id.cancel_sign:
                mDialogSign.dismiss();
                break;
            case R.id.btnNext:
                mDialogSign = new Dialog(getContext());
                mDialogSign.requestWindowFeature(Window.FEATURE_NO_TITLE);
                mDialogSign.getWindow().setBackgroundDrawable(
                        new ColorDrawable(android.graphics.Color.TRANSPARENT));
                mDialogSign.setContentView(R.layout.dialog_signature);
                mDialogSign.setCancelable(false);
                mDialogSign.show();
                btnClear = (Button) mDialogSign.findViewById(R.id.btnclear);
                btnSave = (Button) mDialogSign.findViewById(R.id.btnsave);
                canvasLL = (LinearLayout) mDialogSign.findViewById(R.id.canvasLL);
                cancel_sign = mDialogSign.findViewById(R.id.cancel_sign);
                cancel_sign.setOnClickListener(this::onClick);
                mSignature = new Signature(getContext(), null);
                mSignature.setBackgroundColor(Color.WHITE);
                canvasLL.addView(mSignature, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                view = canvasLL;
                btnClear.setOnClickListener(this::onClick);
                btnSave.setOnClickListener(this::onClick);
                break;
            case R.id.btnclear:
                mSignature.clear();
                break;
            case R.id.btnsave:
                view.setDrawingCacheEnabled(true);
                Log.i("path",StoredPath);
                file = new File(DIRECTORY);
                if(!file.exists()){
                    file.mkdirs();
                }
                mSignature.save(view, StoredPath);
                Toast.makeText(getContext(), "Successfully Saved", Toast.LENGTH_SHORT).show();

                customerTrade.callingCustomerTradeFragment(pos);
                mDialogSign.dismiss();
                break;
        }
    }

    public  boolean isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.i("Permission is granted1","");
                return true;
            } else {
                Log.i("Permission is revoked1","");
                ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, READ_STORAGE);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("Permission is granted1","");
            return true;
        }
    }

    public  boolean isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.i("Permission is granted1","");
                return true;
            } else {
                Log.i("Permission is revoked1","");
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_STORAGE);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("Permission is granted1","");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case READ_STORAGE:
                for (int i = 0; i < permissions.length; i++) {
                    String permission = permissions[i];
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        boolean showRationale = ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permission);
                        if (showRationale) {
                            //  Show your own message here
                        } else {
                            showSettingsAlert();
                        }
                    }
                }
            case WRITE_STORAGE:
                for (int i = 0; i < permissions.length; i++) {
                    String permission = permissions[i];
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        boolean showRationale = ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permission);
                        if (showRationale) {
                            //  Show your own message here
                        } else {
                            showSettingsAlert();
                        }
                    }
                }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void showSettingsAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("App needs to access the Camera.");
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "DONT ALLOW",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //finish();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "SETTINGS",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        openAppSettings(getActivity());
                    }
                });
        alertDialog.show();
    }


    public static void openAppSettings(final Activity context) {
        if (context == null) {
            return;
        }
        final Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + context.getPackageName()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(i);
    }

    public class Signature extends View {
        private static final float STROKE_WIDTH = 5f;
        private static final float HALF_STROKE_WIDTH = STROKE_WIDTH / 2;
        private Paint paint = new Paint();
        private Path path = new Path();
        private float lastTouchX;
        private float lastTouchY;
        private final RectF dirtyRect = new RectF();

        public Signature(Context context, AttributeSet attrs) {
            super(context, attrs);
            paint.setAntiAlias(true);
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeWidth(STROKE_WIDTH);
        }

        @SuppressLint("WrongThread")
        public void save(View v, String StoredPath) {
            Log.v("log_tag", "Width: " + v.getWidth());
            Log.v("log_tag", "Height: " + v.getHeight());
            isWriteStoragePermissionGranted();
            if (bitmap == null) {
                bitmap = Bitmap.createBitmap(canvasLL.getWidth(), canvasLL.getHeight(), Bitmap.Config.RGB_565);
            }
            Canvas canvas = new Canvas(bitmap);
            try {
                // Output the file
                FileOutputStream mFileOutStream = new FileOutputStream(StoredPath);
                v.draw(canvas);
                // Convert the output file to Image such as .png
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, mFileOutStream);
               // byte[] imageBytes = mFileOutStream.toByteArray();
                //String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                //Log.i("Encode",imageString);
                mFileOutStream.flush();
                mFileOutStream.close();
            } catch (Exception e) {
                Log.v("log_tag", e.toString());
            }
        }

        public void clear() {
            path.reset();
            invalidate();
        }

        private void setImage(String imgPath)
        {

            try {
                File f=new File(imgPath, "imgName.jpg");
                Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
                /*ImageView img=(ImageView)findViewById(R.id.image);
                img.setImageBitmap(b);*/
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }

        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawPath(path, paint);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float eventX = event.getX();
            float eventY = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    path.moveTo(eventX, eventY);
                    lastTouchX = eventX;
                    lastTouchY = eventY;
                    return true;

                case MotionEvent.ACTION_MOVE:

                case MotionEvent.ACTION_UP:

                    resetDirtyRect(eventX, eventY);
                    int historySize = event.getHistorySize();
                    for (int i = 0; i < historySize; i++) {
                        float historicalX = event.getHistoricalX(i);
                        float historicalY = event.getHistoricalY(i);
                        expandDirtyRect(historicalX, historicalY);
                        path.lineTo(historicalX, historicalY);
                    }
                    path.lineTo(eventX, eventY);
                    break;

                default:
                    debug("Ignored touch event: " + event.toString());
                    return false;
            }

            invalidate((int) (dirtyRect.left - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.top - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.right + HALF_STROKE_WIDTH),
                    (int) (dirtyRect.bottom + HALF_STROKE_WIDTH));

            lastTouchX = eventX;
            lastTouchY = eventY;

            return true;
        }

        private void debug(String string) {

            Log.v("log_tag", string);

        }

        private void expandDirtyRect(float historicalX, float historicalY) {
            if (historicalX < dirtyRect.left) {
                dirtyRect.left = historicalX;
            } else if (historicalX > dirtyRect.right) {
                dirtyRect.right = historicalX;
            }

            if (historicalY < dirtyRect.top) {
                dirtyRect.top = historicalY;
            } else if (historicalY > dirtyRect.bottom) {
                dirtyRect.bottom = historicalY;
            }
        }

        private void resetDirtyRect(float eventX, float eventY) {
            dirtyRect.left = Math.min(lastTouchX, eventX);
            dirtyRect.right = Math.max(lastTouchX, eventX);
            dirtyRect.top = Math.min(lastTouchY, eventY);
            dirtyRect.bottom = Math.max(lastTouchY, eventY);
        }
    }

    interface customerTradeFragment {

        void callingCustomerTradeFragment(int pos);

    }

}
