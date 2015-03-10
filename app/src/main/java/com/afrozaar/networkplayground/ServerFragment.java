package com.afrozaar.networkplayground;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by jay on 2/23/15.
 */
public class ServerFragment extends Fragment {

    private Activity activity;
    private Server mServer;
    private ProgressBar mProgressBar;
    private TextView mTextView;

    public static ServerFragment newInstance(){
        ServerFragment frag = new ServerFragment();

        return frag;
    }

    public ServerFragment(){
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

    }

    @Override
    public void onPause(){
        super.onPause();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_server, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mProgressBar = (ProgressBar)view.findViewById(R.id.pb_server);
        mTextView = (TextView)view.findViewById(R.id.tv_serverTv);
        mTextView.setText("PLEASE WAIT....LOADING");
        mProgressBar.setMax(100);
        mProgressBar.setIndeterminate(true);

        mServer = new Server(activity, this);
        mServer.execute();
    }

    public void setProgressUpdate(Song newSong){
        mTextView.setText("LOAD DONE : \n" + newSong.getTitle() + " : "+newSong.getArtist());
        mProgressBar.setVisibility(View.INVISIBLE);
    }
}
