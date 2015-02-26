package com.afrozaar.networkplayground;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListView;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by jay on 2/23/15.
 */
public class Client extends AsyncTask<Song,Void,Void> {

    private Context mContext;
    private String mHost;
    private int mPort;
    private int mLen;
    private Socket mSocket = new Socket();
    byte buf[] = new byte[1024*8];


    final static String TAG = Client.class.getName();

    public Client(Context c, String h, int p){
        this.mContext = c;
        this.mHost = h;
        this.mPort = p;
    }

    public int connectAndSend(Song currSong) {
        int result = -1;

        try {
            Log.d("DEBUG","STARTED SEND");
            final Uri trackUri = ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, currSong.getId());
            mSocket.bind(null);
            mSocket.connect((new InetSocketAddress(mHost,mPort)),500);
            Log.d("DEBUG","PAST CONNECTED");
            OutputStream os = mSocket.getOutputStream();
            ContentResolver cr = mContext.getContentResolver();
            InputStream is = null;
            is = cr.openInputStream(trackUri);
            DataOutputStream dos = new DataOutputStream(os);
            dos.writeUTF(currSong.getTitle()+" : "+currSong.getArtist());
            Log.d("DEBUG","WRITE CHAR DONE: STARTING MAIN DATA WRITE");
            while((mLen = is.read(buf)) != -1){
                Log.d("DEBUG","LEN : "+mLen);
                dos.write(buf,0,mLen);
            }

            os.close();
            is.close();

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(mSocket!=null){
                if(mSocket.isConnected()){
                    try{
                        mSocket.close();
                    }catch (IOException e){
                       e.printStackTrace();
                    }
                }
            }
        }

        return result;
    }

    @Override
    protected Void doInBackground(Song... params) {
        connectAndSend(params[0]);
        return null;
    }
}
