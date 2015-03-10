package com.afrozaar.networkplayground;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;


/**
 * Created by jay on 2/10/15.
 */
public class WiFiP2PBroadcastReceiver extends BroadcastReceiver {

    private Activity mActivity;
    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private WifiP2pManager.PeerListListener mPeerListListener;
    private WifiP2pManager.ConnectionInfoListener mConnectionInfoListener;

    private final static String TAG= WiFiP2PBroadcastReceiver.class.getName();

    public WiFiP2PBroadcastReceiver(WifiP2pManager m, WifiP2pManager.Channel ch, Activity a, WifiP2pManager.PeerListListener p, WifiP2pManager.ConnectionInfoListener c) {
        mChannel = ch;
        mManager = m;
        mActivity = a;
        mPeerListListener = p;
        mConnectionInfoListener =c;

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            Log.d(TAG,"STATE CHANGED ACTION");
            // Determine if Wifi P2P mode is enabled or not, alert
            // the Activity.
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                //activity.setIsWifiP2pEnabled(true);
            } else {
                //activity.setIsWifiP2pEnabled(false);
            }
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            Log.d(TAG,"PEERS CHANGED ACTION");
            if (mManager != null) {
                Log.d(TAG,"Request Peers called within OnReceive");
                mManager.requestPeers(mChannel, mPeerListListener);
            }

        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            Log.d(TAG,"CONNECTION CHANGED ACTION");
            if (mManager == null) {
                return;
            }

            NetworkInfo networkInfo = (NetworkInfo) intent
                    .getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);

            if (networkInfo.isConnected()) {

                // We are connected with the other device, request connection
                // info to find group owner IP
                Log.d(TAG,"Request for ConnectionInfo Called");
                mManager.requestConnectionInfo(mChannel, mConnectionInfoListener);
            }

            } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            Log.d(TAG,"THIS DEVICE CHANGED ACTION");
            /*DeviceListFragment fragment = (DeviceListFragment) activity.getSupportFragmentManager()
                    .findFragmentById(R.id.frag_list);
            fragment.updateThisDevice((WifiP2pDevice) intent.getParcelableExtra(
                    WifiP2pManager.EXTRA_WIFI_P2P_DEVICE));*/

            }
    }
}
