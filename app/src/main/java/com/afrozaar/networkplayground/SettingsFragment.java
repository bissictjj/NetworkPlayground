package com.afrozaar.networkplayground;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

/**
 * Created by jay on 3/10/15.
 */
public class SettingsFragment extends Fragment {

    private Activity activity;
    private ToggleButton mLocationButton;

    public static SettingsFragment newInstance(){
        SettingsFragment frag = new SettingsFragment();

        return frag;
    }

    public SettingsFragment(){
        //blank constructor.
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        this.activity = activity;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mLocationButton != null) {
            if (NetworkUtils.isLocationEnabled(activity)) {
                mLocationButton.setChecked(true);
            } else {
                mLocationButton.setChecked(false);
            }
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLocationButton = (ToggleButton)view.findViewById(R.id.tb_location);
        if(NetworkUtils.isLocationEnabled(activity)){
            mLocationButton.setChecked(true);
        }else{
            mLocationButton.setChecked(false);
        }

        mLocationButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);

            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings,container,false);

        return view;
    }
}
