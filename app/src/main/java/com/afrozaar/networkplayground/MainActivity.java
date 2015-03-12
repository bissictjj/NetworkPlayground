package com.afrozaar.networkplayground;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;


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
        if(!NetworkUtils.isLocationEnabled(this)){
            Toast.makeText(this,"Please enabled location services to use the google map section properly",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        if(!CameraUtils.checkCameraHardware(this)){
            MenuItem m = menu.getItem(R.id.action_recorder);
            m.setEnabled(false);
        }
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
            container.setVisibility(View.VISIBLE);
            mWelcome.setVisibility(View.GONE);
            mFragmentManager.beginTransaction().replace(R.id.container, SettingsFragment.newInstance()).commit();
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

        if( id == R.id.action_gmaps_fun){
            container.setVisibility(View.VISIBLE);
            mWelcome.setVisibility(View.GONE);
            /*GoogleMapOptions options = new GoogleMapOptions();
            options.mapType(GoogleMap.MAP_TYPE_NORMAL).compassEnabled(true).rotateGesturesEnabled(true).tiltGesturesEnabled(false).zoomGesturesEnabled(true);*/
            mFragmentManager.beginTransaction().replace(R.id.container,GmapFragment.newInstance()).commit();
        }

        if(id== R.id.action_recorder){
            container.setVisibility(View.VISIBLE);
            mWelcome.setVisibility(View.GONE);

            mFragmentManager.beginTransaction().replace(R.id.container, RecorderFragment.newInstance()).commit();
        }


        return super.onOptionsItemSelected(item);
    }

    public void changeFragment(String fragName, String host, int port) {
        if (fragName.equalsIgnoreCase("server")) {
            mFragmentManager.beginTransaction().replace(R.id.container, ServerFragment.newInstance()).commit();
        } else if (fragName.equalsIgnoreCase("client")) {
            mFragmentManager.beginTransaction().replace(R.id.container, ClientFragment.newInstance(host, port)).commit();
        }

    }

    public FragmentManager getMainFragmentManager(){
        if(mFragmentManager != null){
            return mFragmentManager;
        }
        return getSupportFragmentManager();
    }
}
