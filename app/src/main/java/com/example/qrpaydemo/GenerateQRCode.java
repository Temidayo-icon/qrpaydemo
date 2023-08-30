package com.example.qrpaydemo;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class GenerateQRCode extends AppCompatActivity {

    private TextView gentext;
    private ImageView QRCode;
    private TextInputEditText editdata;
    private Button genbtn;
    private QRGEncoder qrgEncoder;
    private Bitmap bitmap;

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

                try {
                    generateBarcode();
                } catch (WriterException e) {
                    throw new RuntimeException(e);
                }


            }
        });


    }
    public void generateBarcode() throws WriterException {

        String data = editdata.getText().toString();
        if(data.isEmpty()) {
            Toast.makeText(GenerateQRCode.this, "Please enter user id to generate QR code", Toast.LENGTH_SHORT) .show();
        }else{
            WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
            Display display = manager.getDefaultDisplay();
            Point point = new Point();
            display.getSize(point);
            int width = point.x;
            int height = point.y;
            int dimen = width<height ? width:height;
            dimen = dimen * 3/4;

            qrgEncoder = new QRGEncoder(editdata.getText().toString(), null, QRGContents.Type.TEXT,dimen);
            bitmap = qrgEncoder.getBitmap();
            QRCode.setVisibility(View.GONE);
            QRCode.setImageBitmap(bitmap);


        }
    }


}