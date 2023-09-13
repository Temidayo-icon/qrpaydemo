package com.example.qrpaydemo;

import static android.os.Looper.getMainLooper;

import static androidx.core.content.ContextCompat.getSystemService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.widget.Toast;

public class WifiBroadcastReceiver extends BroadcastReceiver {
     private WifiP2pManager.Channel channel;
    private WifiP2pManager manager;
    private HomeActivity hActivity;
    private ConnectDirect cActivity;
    WifiP2pManager.PeerListListener myPeerListListener;

    public WifiBroadcastReceiver(WifiP2pManager manager, WifiP2pManager.Channel channel, HomeActivity hActivity) {
        this.channel = channel;
        this.manager = manager;
        this.hActivity = hActivity;
    }
    public WifiBroadcastReceiver(WifiP2pManager manager, WifiP2pManager.Channel channel, ConnectDirect cActivity) {
        this.channel = channel;
        this.manager = manager;
        this.cActivity=cActivity;
    }





    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            // Determine if Wi-Fi Direct mode is enabled or not, alert
            // the Activity.
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
               // activity.setIsWifiP2pEnabled(true);
                Toast.makeText(context, "Wifi is ON", Toast.LENGTH_SHORT).show();
            } else {
               // activity.setIsWifiP2pEnabled(false);
                Toast.makeText(context, "Wifi is OFF", Toast.LENGTH_SHORT).show();
            }
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {

            // The peer list has changed! We should probably do something about
            // that.
            if (manager != null) {
                manager.requestPeers(channel, cActivity.myPeerListListener);
            }

        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {

            // Connection state changed! We should probably do something about
            // that.
            if(manager==null)
            {
                return;
            }
            NetworkInfo networkInfo=intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);

            if(networkInfo.isConnected())
            {
                manager.requestConnectionInfo(channel, cActivity.connectionInfoListener);
            }else{
                cActivity.connectionStatus.setText("Device is connected");
            }

        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
           // DeviceListFragment fragment = (DeviceListFragment) activity.getFragmentManager()
                  //  .findFragmentById(R.id.frag_list);
            //fragment.updateThisDevice((WifiP2pDevice) intent.getParcelableExtra(
                  //  WifiP2pManager.EXTRA_WIFI_P2P_DEVICE));
        }

    }


}

