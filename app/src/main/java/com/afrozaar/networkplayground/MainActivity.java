package com.afrozaar.networkplayground;

import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Looper;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private FrameLayout container;
    private TextView mWelcome;
    private FragmentManager mFragmentManager;

    private final static String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        container = (FrameLayout)findViewById(R.id.container);
        mWelcome = (TextView)findViewById(R.id.tv_welcome);
        Toolbar toolbar = (Toolbar)findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);
        mFragmentManager = getSupportFragmentManager();
        mWelcome.setVisibility(View.VISIBLE);
        container.setVisibility(View.GONE);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if(id == R.id.action_p2p){
            container.setVisibility(View.VISIBLE);
            mWelcome.setVisibility(View.GONE);
            mFragmentManager.beginTransaction().replace(R.id.container, P2pFragment.newInstance()).commit();
        }

        if( id == R.id.action_peerlist){
            container.setVisibility(View.VISIBLE);
            mWelcome.setVisibility(View.GONE);
            mFragmentManager.beginTransaction().replace(R.id.container,PeerListFragment.newInstance()).commit();
        }
        return super.onOptionsItemSelected(item);
    }

    public void changeFragment(String fragName, String host, int port){
        if(fragName.equalsIgnoreCase("server")){
            mFragmentManager.beginTransaction().replace(R.id.container,ServerFragment.newInstance()).commit();
        }else if(fragName.equalsIgnoreCase("client")){
            mFragmentManager.beginTransaction().replace(R.id.container,ClientFragment.newInstance(host,port)).commit();
        }

    }
}
