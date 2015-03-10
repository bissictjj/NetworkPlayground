package com.afrozaar.networkplayground;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;


/**
 * Created by jay on 2/23/15.
 */
public class Server extends AsyncTask<Void,Integer,String> {

    private Context mContext;
    private ServerFragment mServFrag;
    private Song newSong;

    public Server(Context context, ServerFragment mServFrag) {
        this.mContext = context;
        this.mServFrag = mServFrag;

    }

    @Override
    protected String doInBackground(Void... params) {

        try {
            ServerSocket serverSocket = new ServerSocket(8888);
            Socket clientSocket = serverSocket.accept();
            Log.d("DEBUG", "Is client socket connected: " + clientSocket.isConnected());
            InputStream inputStream = clientSocket.getInputStream();
            DataInputStream dis = new DataInputStream(inputStream);

            String titlename = new String(dis.readUTF().getBytes(Charset.forName("UTF-8")), Charset.forName("UTF-8"));
            Log.d("DEBUG", "File Name : "+titlename);
            newSong = new Song((titlename.substring(0,titlename.indexOf(":"))).trim(), (titlename.substring(titlename.indexOf(":")+1)).trim());
            final File f = new File(Environment.getExternalStorageDirectory() + "/" + mContext.getPackageName() +"/" + newSong.getTitle()+".mp3");
            File dirs = new File(f.getParent());
            if (!dirs.exists()) {
                dirs.mkdirs();
            }
            if(f.createNewFile()){
                Log.d("DEBUG", "Made new file");
            }
            FileOutputStream fos = new FileOutputStream(f);
            byte[] buf = new byte[1024*8];

            int len;
            while ((len = dis.read(buf)) > 0) {

                fos.write(buf, 0, len);
            }
            fos.close();
            inputStream.close();
            serverSocket.close();
            return f.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        mServFrag.setProgressUpdate(newSong);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {

        super.onProgressUpdate(values);

    }
}