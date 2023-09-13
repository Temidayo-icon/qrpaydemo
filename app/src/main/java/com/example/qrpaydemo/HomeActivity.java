package com.example.qrpaydemo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.provider.Settings;
import android.se.omapi.Channel;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.lang.reflect.Method;

public class HomeActivity extends AppCompatActivity {

    WifiP2pManager manager;
    WifiP2pManager.Channel channel;
    BroadcastReceiver receiver;

    private Button genbtn, Scanbtn, connectbtn;

    private final IntentFilter intentFilter = new IntentFilter();


    WifiManager wifiManager;

    WifiP2pManager.PeerListListener myPeerListListener;


    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        genbtn = findViewById(R.id.genbtn);
        Scanbtn = findViewById(R.id.Scanbtn);
        connectbtn = findViewById(R.id.connectbtn);

        //showOptionsDialog();



        manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        channel = manager.initialize(this, getMainLooper(), null);
        receiver = new WifiBroadcastReceiver(manager, channel, this);


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

        connectbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this,ConnectDirect.class);
                startActivity(i);
            }
        });



    }

    private void showOptionsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select an Option")
                .setMessage("Do you want to send or receive?")
                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Switch on hotspot
                       // startHotspot(HomeActivity.this);
                       connectbtn.setVisibility(View.VISIBLE);

                    }
                })
                .setNegativeButton("Receive", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Switch on Wi-Fi
                        //enableWifi();
                        connectbtn.setVisibility(View.GONE);

                        manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
                            @Override
                            public void onSuccess() {

                                Toast.makeText(HomeActivity.this,"Discovery Started",Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onFailure(int i) {

                                Toast.makeText(HomeActivity.this,"Discovery Started",Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                })
                .show();
    }
  /*  public static void startHotspot(Context context) {
        // Implement code to start hotspot here
        // Make sure you have the necessary permissions
        // You can use the code from previous responses to enable hotspot
        // For simplicity, you can show a Toast message here indicating that hotspot is being started.
        // Function to start the Wi-Fi hotspot
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

            if (wifiManager != null) {
                // Check if Wi-Fi is enabled, and if so, disable it temporarily
                boolean wifiEnabled = wifiManager.isWifiEnabled();
                if (wifiEnabled) {
                    wifiManager.setWifiEnabled(false);
                }

                try {
                    // Create a new Wi-Fi configuration for the hotspot
                    WifiConfiguration wifiConfiguration = new WifiConfiguration();
                    wifiConfiguration.SSID = "YourHotspotName"; // Set the hotspot name
                    wifiConfiguration.preSharedKey = "YourHotspotPassword"; // Set the hotspot password
                    wifiConfiguration.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);

                    // Set up the hotspot with the created configuration
                    Method method = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
                    boolean success = (Boolean) method.invoke(wifiManager, wifiConfiguration, true);

                    // If the hotspot is successfully enabled, return true
                    if (success) {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    // Restore the Wi-Fi state to what it was before enabling the hotspot
                    if (wifiEnabled) {
                        wifiManager.setWifiEnabled(true);
                    }
                }
            }

        //Toast.makeText(this, "Starting Hotspot...", Toast.LENGTH_SHORT).show();
    }
   private void enableWifi() {
        // Implement code to enable Wi-Fi here
        // Make sure you have the necessary permissions
        // You can use the code from previous responses to enable Wi-Fi
        // For simplicity, you can show a Toast message here indicating that Wi-Fi is being enabled.
        // Check if Wi-Fi is already enabled
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
            // If Wi-Fi is not enabled, open the Wi-Fi settings screen
            Intent wifiIntent = new Intent(Settings.ACTION_WIFI_SETTINGS);
            startActivity(wifiIntent);
        } else {
            wifiManager.setWifiEnabled(false);
            // Wi-Fi is already enabled, show a message to the user
            Toast.makeText(this, "Wi-Fi is already enabled.", Toast.LENGTH_SHORT).show();
        }

        Toast.makeText(this, "Enabling Wi-Fi...", Toast.LENGTH_SHORT).show();
    } */
}