package com.example.qrpaydemo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

    private Button genbtn, Scanbtn;

    private final IntentFilter intentFilter = new IntentFilter();

    WifiP2pManager.Channel channel;
    WifiP2pManager manager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        genbtn = findViewById(R.id.genbtn);
        Scanbtn = findViewById(R.id.Scanbtn);

        showOptionsDialog();


        // Indicates a change in the Wi-Fi Direct status.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);

        // Indicates a change in the list of available peers.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);

        // Indicates the state of Wi-Fi Direct connectivity has changed.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);

        // Indicates this device's details have changed.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);



        genbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this,GenerateQRCode.class);
                startActivity(i);

            }
        });

        Scanbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this,CustomScanner.class);
                startActivity(i);

            }
        });
        manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        channel = manager.initialize(this, getMainLooper(), null);
    }

    private void showOptionsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select an Option")
                .setMessage("Do you want to send or receive?")
                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Switch on hotspot
                        startHotspot();
                    }
                })
                .setNegativeButton("Receive", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Switch on Wi-Fi
                        enableWifi();
                    }
                })
                .show();
    }
    private void startHotspot() {
        // Implement code to start hotspot here
        // Make sure you have the necessary permissions
        // You can use the code from previous responses to enable hotspot
        // For simplicity, you can show a Toast message here indicating that hotspot is being started.
        Toast.makeText(this, "Starting Hotspot...", Toast.LENGTH_SHORT).show();
    }
    private void enableWifi() {
        // Implement code to enable Wi-Fi here
        // Make sure you have the necessary permissions
        // You can use the code from previous responses to enable Wi-Fi
        // For simplicity, you can show a Toast message here indicating that Wi-Fi is being enabled.
        Toast.makeText(this, "Enabling Wi-Fi...", Toast.LENGTH_SHORT).show();
    }
}