package com.afrozaar.networkplayground;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.Executor;

/**
 * Created by jay on 2/23/15.
 */
public class ClientFragment extends Fragment {

    private Activity activity;
    private TextView mTextView;
    private Button mBtnSendSong;
    private Button mBtnChoose;
    private Client mClient;
    private ListView mListView;
    private ArrayList<Song> mSongList;
    private int chosen = 0;
    private Executor executor;

    public static ClientFragment newInstance(String host,int port){
        ClientFragment frag = new ClientFragment();
        Bundle args = new Bundle();
        args.putString("host",host);
        args.putInt("port",port);
        frag.setArguments(args);
        return frag;
    }

    public ClientFragment(){
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
        return inflater.inflate(R.layout.fragment_client, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBtnChoose = (Button) view.findViewById(R.id.btn_openChooser);
        mBtnSendSong = (Button)view.findViewById(R.id.btn_sendSong);
        mTextView = (TextView)view.findViewById(R.id.tv_clientTv);
        mListView = (ListView)view.findViewById(R.id.lv_songlist);

        getSongList();
        Collections.sort(mSongList, new Comparator<Song>() {
            public int compare(Song a, Song b) {
                return a.getTitle().compareTo(b.getTitle());
            }
        });
        mClient = new Client(activity,getArguments().getString("host"),getArguments().getInt("port"));
        SongAdapter songAdt = new SongAdapter(activity, mSongList);
        mListView.setAdapter(songAdt);
    }

    public void getSongList(){
        ContentResolver musicResolver = activity.getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);
        mSongList = new ArrayList<>();
        if(musicCursor!=null && musicCursor.moveToFirst()){
            //get columns
            int titleColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ARTIST);
            //add songs to list
            do {
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                if(thisTitle != null && thisArtist !=null) {
                    mSongList.add(new Song(thisId, thisTitle, thisArtist));
                }
            }
            while (musicCursor.moveToNext());
        }
    }

    public class SongAdapter extends BaseAdapter {

        private ArrayList<Song> songs;
        private LayoutInflater songInf;

        public SongAdapter(Context c, ArrayList<Song> theSongs){
            songs=theSongs;
            songInf=LayoutInflater.from(c);
        }
        @Override
        public int getCount() {
            return songs.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //map to song layout
            final LinearLayout songLay = (LinearLayout)songInf.inflate
                    (R.layout.song_item, parent, false);

            //get title and artist views
            TextView songView = (TextView)songLay.findViewById(R.id.tv_song_title);
            TextView artistView = (TextView)songLay.findViewById(R.id.tv_song_artist);
            //get song using position
            final Song currSong = songs.get(position);
            //get title and artist strings
            songView.setText(currSong.getTitle());
            artistView.setText(currSong.getArtist());
            //set position as tag
            songLay.setTag(position);


            songLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClient.execute(currSong);
                    songLay.setBackgroundColor(Color.BLUE);
                }
            });

            return songLay;
        }

    }
}
