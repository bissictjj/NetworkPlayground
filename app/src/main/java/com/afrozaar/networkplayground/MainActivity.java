package com.afrozaar.networkplayground;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.support.v7.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;


public class MainActivity extends ActionBarActivity {

    private FrameLayout container;
    private TextView mWelcome;
    private FragmentManager mFragmentManager;

    public static final int CONTACT_QUERY_LOADER = 0;
    public static final String QUERY_KEY = "query";


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

        Log.d("TAG", "OnCreateOptionsCalled Activity");
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        if(!CameraUtils.checkCameraHardware(this)) {
            MenuItem m = menu.findItem(R.id.action_recorder);
            m.setEnabled(false);
        }
        //MenuItem sv = menu.findItem(R.id.action_search);
        //sv.setVisible(false);
        //sv.setEnabled(false);
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.d("TAG", "OnNewIntent : "+intent.getAction());
        handleIntent(intent);
    }

    /**
     * Assuming this activity was started with a new intent, process the incoming information and
     * react accordingly.
     * @param intent
     */
    private void handleIntent(Intent intent) {
        // Special processing of the incoming intent only occurs if the if the action specified
        // by the intent is ACTION_SEARCH.
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            // SearchManager.QUERY is the key that a SearchManager will use to send a query string
            // to an Activity.
            String query = intent.getStringExtra(SearchManager.QUERY);

            // We need to create a bundle containing the query string to send along to the
            // LoaderManager, which will be handling querying the database and returning results.
            Bundle bundle = new Bundle();
            bundle.putString(QUERY_KEY, query);

            ContactablesLoaderCallbacks loaderCallbacks = new ContactablesLoaderCallbacks(this);

            // Start the loader with the new query, and an object that will handle all callbacks.
            getLoaderManager().restartLoader(CONTACT_QUERY_LOADER, bundle, loaderCallbacks);
        }
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

        if(id==R.id.action_searchFrag){
            container.setVisibility(View.VISIBLE);
            mWelcome.setVisibility(View.GONE);
            mFragmentManager.beginTransaction().replace(R.id.container, SearchFragment.newInstance()).commit();
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

}
