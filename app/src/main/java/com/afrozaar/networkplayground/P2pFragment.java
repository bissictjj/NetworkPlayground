package com.afrozaar.networkplayground;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by jay on 2/10/15.
 */
public class P2pFragment extends Fragment {



    private Activity activity;
    private NetworkUtils networkUtils;
    private boolean discoverOn = false;

    private Button btn_discover;
    private Button btn_register;

    public static P2pFragment newInstance() {
        P2pFragment frag = new P2pFragment();

        return frag;
    }

    public P2pFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        networkUtils = new NetworkUtils(activity);
        networkUtils.initializeServerSocket();
        networkUtils.initializeRegistrationListener();
        networkUtils.initializeDiscoveryListener();
        networkUtils.initializeResolveListener();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_p2p, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btn_discover = (Button)view.findViewById(R.id.btn_discoverpeers);
        btn_register = (Button)view.findViewById(R.id.btn_register);

        btn_discover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(discoverOn){
                    networkUtils.stopDiscovery();
                    discoverOn =false;
                }else {
                    networkUtils.discoverServices();
                    discoverOn = true;
                }
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                networkUtils.registerService(networkUtils.getServerPort());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        /*mReceiver = new WiFiP2PBroadcastReceiver(mManager, mChannel, getActivity());
        activity.registerReceiver(mReceiver, intentFilter);*/
    }

    @Override
    public void onPause() {
        super.onPause();
        /*activity.unregisterReceiver(mReceiver);*/
    }

    @Override
    public void onAttach(Activity activity){ //Apparently this is the best way to reference to an activity through a fragment. Can use getActivity() too, but I prefer an actualy object to work with
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        networkUtils.unregisterService();
        networkUtils.stopDiscovery();
    }
}
