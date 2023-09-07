package com.example.qrpaydemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;
import com.journeyapps.barcodescanner.camera.CameraSettings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ScanQRcode extends AppCompatActivity {


    private DecoratedBarcodeView barcodeView;
    private BeepManager beepManager;
    private String lastText;

    private ArrayList<User> users = new ArrayList<>();

    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if(result.getText() == null || result.getText().equals(lastText)) {
                // Prevent duplicate scans
                return;
            }

            lastText = result.getText();
            barcodeView.setStatusText(result.getText());

            // Call a method to process the scanned QR code
            processScannedQRCode(result.getText());

            beepManager.playBeepSoundAndVibrate();

            // Check if the scanned QR code is assigned to a user
            if (isValidUserQRCode(result.getText())) {
                // Navigate to the Wallet Balance activity
                Intent intent = new Intent(ScanQRcode.this, WalletBalanceActivity.class);
                startActivity(intent);
            }

            //Added preview of scanned barcode
            ImageView imageView = findViewById(R.id.barcodePreview);
            imageView.setImageBitmap(result.getBitmapWithResultPoints(Color.YELLOW));
        }
        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qrcode);

        // Retrieve the users data from the Intent


        ArrayList<User> users = getIntent().getParcelableArrayListExtra("users");

            barcodeView = findViewById(R.id.barcode_scanner);
            Collection<BarcodeFormat> formats = Arrays.asList(BarcodeFormat.QR_CODE, BarcodeFormat.CODE_39);
            barcodeView.getBarcodeView().setDecoderFactory(new DefaultDecoderFactory(formats));
            barcodeView.initializeFromIntent(getIntent());
            barcodeView.decodeContinuous(callback);

            beepManager = new BeepManager(this);


        }

    private void processScannedQRCode(String scannedData) {
        // Check if the scanned QR code data matches any user data
        for (User user : users) {
            if (user.getQrCodeData().equals(scannedData)) {
                // User found, open user details activity
                openUserDetailsActivity(user.getUserId());
                return;
            }
        }

        // User not found, show a message or take appropriate action
        Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
    }

    private void openUserDetailsActivity(String userId) {
        Intent intent = new Intent(this, WalletBalanceActivity.class);

        // Pass the user ID to the WalletBalanceActivity
        intent.putExtra("userId", userId);

        // Pass the users data to the scanning activity
        intent.putParcelableArrayListExtra("users", (ArrayList<? extends Parcelable>) users);

        startActivity(intent);
    }


    @Override
        protected void onResume() {
            super.onResume();

            barcodeView.resume();
        }

        @Override
        protected void onPause() {
            super.onPause();

            barcodeView.pause();
        }

        public void pause(View view) {
            barcodeView.pause();
        }

        public void resume(View view) {
            barcodeView.resume();
        }

        public void triggerScan(View view) {
            barcodeView.decodeSingle(callback);
        }

        @Override
        public boolean onKeyDown(int keyCode, KeyEvent event) {
            return barcodeView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
        }
        // Check if the scanned QR code is associated with a valid user
    private boolean isValidUserQRCode(String qrCodeData) {
        // In this example, we assume a list of valid user IDs.
        List<String> validUserIds = Arrays.asList("Ronke", "Mayowa", "Yemi"); // Replace with your actual user IDs.

        // Check if the scanned QR code data is in the list of valid user IDs
        return validUserIds.contains(qrCodeData);
    }
    }
