package com.afrozaar.networkplayground;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import java.net.InetAddress;
import java.util.ArrayList;

/**
 * Created by jay on 2/12/15.
 */
public class PeerListFragment extends Fragment {

    private final IntentFilter intentFilter = new IntentFilter();
    private WifiP2pManager.Channel mChannel;
    private WifiP2pManager mManager;
    private WiFiP2PBroadcastReceiver mReceiver;
    private final static String TAG = PeerListFragment.class.getName();
    private ArrayList<WifiP2pDevice> peers = new ArrayList<>();


    private WifiP2pManager.ActionListener mActionListener;
    private WiFiPeerListAdapter mAdapter;
    private ListView mListview;
    private Button btnConnect;
    private Button btnDiscover;

    private int selection;

    private Activity activity;

    public static PeerListFragment newInstance(){
        PeerListFragment frag = new PeerListFragment();

        return frag;
    }

    public PeerListFragment(){
        //empty constructor is good practice?
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d(TAG,"Receiver has been created");
        mReceiver = new WiFiP2PBroadcastReceiver(mManager,mChannel,activity,peerListListener,mConnectionInfoListener);
        activity.registerReceiver(mReceiver, intentFilter);
    }

    @Override
    public void onPause(){
        super.onPause();
        activity.unregisterReceiver(mReceiver);
        if(mActionListener != null && mManager !=null) {
            mManager.cancelConnect(mChannel, mActionListener);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_peerlist, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListview = (ListView)view.findViewById(R.id.lv_peerlist);
       // mReceiver = new WiFiP2PBroadcastReceiver(mManager,mChannel,activity,peerListListener);

        btnConnect = (Button)view.findViewById(R.id.btn_connect);
        btnDiscover = (Button)view.findViewById(R.id.btn_discover);

        btnDiscover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                discover();
            }
        });
        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connect();
            }
        });

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //We must add these intent filters to listen for broadcast intents that tell the application when certain events have occurred.
        //  Indicates a change in the Wi-Fi P2P status.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);

        // Indicates a change in the list of available peers.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);

        // Indicates the state of Wi-Fi P2P connectivity has changed.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);

        // Indicates this device's details have changed.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        mManager = (WifiP2pManager)activity.getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(activity, Looper.getMainLooper(),null);

        //Start searching for peers i.e. Initiate Peer Discovery

    }

    public void discover(){
        mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {

            @Override
            public void onSuccess() { //Code for successful discovery initiation
                mManager.requestPeers(mChannel, peerListListener);
                Log.d(TAG, "Discover Peers was Successful");
            }

            @Override
            public void onFailure(int reason) {
                Log.d(TAG, "Discover Peers was Unsuccessful : " + reason);

            }
        });
    }

    public void connect(){
        // Pick the first device found on the network
        if(!peers.isEmpty()) {
            WifiP2pDevice device = (WifiP2pDevice) peers.get(0);

            WifiP2pConfig config = new WifiP2pConfig();
            config.deviceAddress = device.deviceAddress;
            config.wps.setup = WpsInfo.PBC;

            mActionListener = new WifiP2pManager.ActionListener() {
                @Override
                public void onSuccess() {
                    Log.d(TAG, "Connect was successful");
                }

                @Override
                public void onFailure(int reason) {
                    Log.d(TAG, "Connect was Unsuccessful");
                }
            };

            mManager.connect(mChannel, config, mActionListener);
        }
    }

    public WifiP2pManager.ConnectionInfoListener mConnectionInfoListener = new WifiP2pManager.ConnectionInfoListener() {
        @Override
        public void onConnectionInfoAvailable(WifiP2pInfo info) {
            Log.d(TAG, "onConnectionInfoAvailable called");
            // InetAddress from WifiP2pInfo struct.
            InetAddress groupOwnerAddress = (info.groupOwnerAddress);

            // After the group negotiation, we can determine the group owner.
            if (info.groupFormed && info.isGroupOwner) {
                Log.d(TAG, "onConnectionInfoAvailable : Server Here");
                ((MainActivity)activity).changeFragment("server","",0);
                // Do whatever tasks are specific to the group owner.
                // One common case is creating a server thread and accepting
                // incoming connections.
            } else if (info.groupFormed) {
                Log.d(TAG, "onConnectionInfoAvailable : Client Here");
                ((MainActivity)activity).changeFragment("client",info.groupOwnerAddress.getHostAddress(),8888);
                // The other device acts as the client. In this case,
                // you'll want to create a client thread that connects to the group
                // owner.
            }
        }
    };


    public WifiP2pManager.PeerListListener peerListListener = new WifiP2pManager.PeerListListener() {
        @Override
        public void onPeersAvailable(WifiP2pDeviceList peerList) {
            /*Collection<WifiP2pDevice> temp = peerList.getDeviceList();
            Iterator i = temp.iterator();

            while(i.hasNext()){
                peers.add((WifiP2pDevice)i.next());
            }*/
            peers.clear();
            peers.addAll(peerList.getDeviceList());
            mAdapter = new WiFiPeerListAdapter(activity,peers);
            mListview.setAdapter(mAdapter);

            //mAdapter.addAll(peerList.getDeviceList());
            /*peers.clear();
            peers.addAll(peerList.getDeviceList());*/
            //mAdapter.update(peers);

            Log.d(TAG,"onPeersAvailable called : number of peers -> "+ peerList.getDeviceList().size());
            Log.d(TAG,"peers List : size -> "+peers.size());
            // If an AdapterView is backed by this data, notify it
            // of the change.  For instance, if you have a ListView of available
            // peers, trigger an update.
            //mAdapter.notifyDataSetChanged();
            if (peers.size() == 0) {
                Log.d(TAG, "No devices found");
                return;
            }
        }
    };
}
