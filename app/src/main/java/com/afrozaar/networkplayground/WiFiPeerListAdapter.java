package com.afrozaar.networkplayground;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pDevice;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jay on 2/11/15.
 */
public class WiFiPeerListAdapter extends ArrayAdapter<WifiP2pDevice> {

    Context mContext;
    private ArrayList<WifiP2pDevice> mList = new ArrayList<>();
    private static final String TAG = WiFiPeerListAdapter.class.getName();

    public WiFiPeerListAdapter(Context context, ArrayList<WifiP2pDevice> objects) {
        super(context, 0, objects);
        mContext = context;
        mList = objects;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.wifipeerlist_item,parent,false);
        }
        if(mList.size()>0) {

            WifiP2pDevice temp = mList.get(position);
            TextView txtDeviceName = (TextView) convertView.findViewById(R.id.tv_deviceName);
            TextView txtDeviceType = (TextView) convertView.findViewById(R.id.tv_deviceType);
            TextView txtConnStatus = (TextView) convertView.findViewById(R.id.tv_connStatus);

            txtDeviceName.setText("Device: "+temp.deviceName);
            txtDeviceType.setText("Type: "+temp.primaryDeviceType);
            txtConnStatus.setText("Status: " + temp.status);

            Log.d(TAG, "DeviceName : " + temp.deviceName + " DeviceType : " + temp.primaryDeviceType + " ConnStatus : " + temp.status);
        }else{
            TextView txtDeviceName = (TextView) convertView.findViewById(R.id.tv_deviceName);
            TextView txtDeviceType = (TextView) convertView.findViewById(R.id.tv_deviceType);
            TextView txtConnStatus = (TextView) convertView.findViewById(R.id.tv_connStatus);

            txtDeviceName.setText("No Devices Found");
            txtDeviceType.setText("No Device Type");
            txtConnStatus.setText("No connection");
        }
        return convertView;
    }
}
