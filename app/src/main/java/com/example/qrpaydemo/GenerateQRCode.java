package com.example.qrpaydemo;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GenerateQRCode extends AppCompatActivity {

    private TextView gentext;
    private ImageView QRCode;
    private TextInputEditText editdata;
    private Button genbtn;
    private Bitmap bitmap;
    private List<User> users = new ArrayList<>();;

    // Track whether the QR code has been generated
    private boolean isQRCodeGenerated = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_qrcode);

        gentext = findViewById(R.id.gentext);
        QRCode = findViewById(R.id.QRCode);
        editdata = findViewById(R.id.editdata);
        genbtn = findViewById(R.id.genbtn);



        genbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isQRCodeGenerated) {
                    generateBarcode();
                    saveQRCode();
                } else {
                    saveQRCode();
                }
            }
        });
        // Dummy data (for demonstration)
        users.add(new User("Elvina", "QRCodeData1"));
        users.add(new User("Mayowa", "QRCodeData2"));
        users.add(new User("Yemi", "QRCodeData3"));
        users.add(new User("Ronke", "QRCodeData4"));

    }


    public void generateBarcode() {
        String data = editdata.getText().toString();
        if (data.isEmpty()) {
            Toast.makeText(GenerateQRCode.this, "Please enter user id to generate QR code", Toast.LENGTH_SHORT).show();
        }

        for (User existingUser : users) {
            if (existingUser.getUserId().equals(data)) {
                Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        try {

                WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
                Display display = manager.getDefaultDisplay();
                Point point = new Point();
                display.getSize(point);
                int width = point.x;
                int height = point.y;
                int dimen = width < height ? width : height;
                dimen = dimen * 3 / 4;

                // Generate QR code data
                String qrCodeData = "User ID " + data;
                // Use ZXing library to generate the QR code
                bitmap = generateQRCode(data, dimen, dimen);


                QRCode.setVisibility(View.VISIBLE);
                gentext.setVisibility(View.GONE);
                QRCode.setImageBitmap(bitmap);

                isQRCodeGenerated = true;

                // Clear the user input
                editdata.setText("");


            // Create a new user and save it
            User newUser = new User(data, qrCodeData);
           users.add(newUser);
            } catch (WriterException e) {
                e.printStackTrace();
            }
        }


    private Bitmap generateQRCode(String data, int width, int height) throws WriterException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, width, height);
        int bmWidth = bitMatrix.getWidth();
        int bmHeight = bitMatrix.getHeight();
        int[] pixels = new int[bmWidth * bmHeight];
        for (int y = 0; y < bmHeight; y++) {
            int offset = y * bmWidth;
            for (int x = 0; x < bmWidth; x++) {
                pixels[offset + x] = bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bmWidth, bmHeight, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, bmWidth, 0, 0, bmWidth, bmHeight);
        return bitmap;
    }

    private void saveQRCode() {
        // Check if the app has permission to write to external storage
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

            // Request the permission if it's not granted
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        } else {
            // Retrieve the QR code from the ImageView
            QRCode.setDrawingCacheEnabled(true);
            Bitmap qrCodeBitmap = QRCode.getDrawingCache();

            // Create a directory for saving the QR codes (you can change the directory as needed)
            String savePath = Environment.getExternalStorageDirectory() + File.separator + "QRCodeDemo";
            File directory = new File(savePath);

            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Generate a unique filename for the QR code based on the current timestamp
            String fileName = "QRCode_" + new Date().getTime() + ".png";

            // Create the file where the QR code will be saved
            File file = new File(directory, fileName);

            try {
                // Save the QR code to the file
                FileOutputStream outputStream = new FileOutputStream(file);
                qrCodeBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                outputStream.close();

                // Add the QR code image to the device's media library (optional)
                MediaStore.Images.Media.insertImage(getContentResolver(), file.getAbsolutePath(), fileName, null);

                // Show a toast message to indicate successful saving
                Toast.makeText(this, "QR Code Saved: " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();

                // Reset the UI
                QRCode.setVisibility(View.GONE);

                isQRCodeGenerated = false;
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to save QR Code", Toast.LENGTH_SHORT).show();
            }
        }

    }




}
