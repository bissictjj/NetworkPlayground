package com.afrozaar.networkplayground;


import android.app.Activity;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.melnykov.fab.FloatingActionButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

/**
 * Created by jay on 3/11/15.
 */
public class RecorderFragment extends Fragment  {

    private Activity activity;
    private FloatingActionButton btnRecord;
    private FloatingActionButton btnCapture;
    private VideoView mVideoView;
    private ImageView mImageView;
    private MediaController mediaController;
    private Uri imagePathUri;
    private static final String TAG = RecorderFragment.class.getName();

    public static RecorderFragment newInstance(){
        RecorderFragment frag = new RecorderFragment();

        return frag;
    }

    public RecorderFragment(){
        //Empty constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recorder,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnCapture = (FloatingActionButton)view.findViewById(R.id.btnCapture);
        btnRecord = (FloatingActionButton)view.findViewById(R.id.btnRecord);
        mVideoView = (VideoView) view.findViewById(R.id.vv_display);
        mImageView = (ImageView)view.findViewById(R.id.iv_display);

        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVideoView.setVisibility(View.GONE);
                mImageView.setVisibility(View.GONE);
                CameraUtils.PhotoInfo pI = CameraUtils.getCameraPhotoInfo();
                Intent i = pI.getI();
                imagePathUri = pI.getImagePath();

                startActivityForResult(i, CameraUtils.REQUEST_CODE_CAMERA);

            }
        });

        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVideoView.setVisibility(View.GONE);
                mImageView.setVisibility(View.GONE);
                Intent i = CameraUtils.getVideoIntent();
                startActivityForResult(i,CameraUtils.REQUEST_CODE_VIDEO);

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //check for the correct request code
        if(requestCode == CameraUtils.REQUEST_CODE_CAMERA){

            if(resultCode == Activity.RESULT_OK){
                // Image captured and saved to fileUri specified in the Intent
                Toast.makeText(activity, "Image saved to NetworkPlayground Folder", Toast.LENGTH_LONG).show();
                mImageView = (ImageView)getView().findViewById(R.id.iv_display);
                mImageView.setVisibility(View.VISIBLE);
                try {
                    //InputStream in = new FileInputStream(new File(imagePathUri.getPath()));
                    /*String name = imagePathUri.getLastPathSegment();
                    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES), "NetworkPlayground/"+name);
                    Log.d(TAG, "ImagePath : " + imagePathUri);
                    Log.d(TAG, "ImagePath LAst Segment : " + imagePathUri.getLastPathSegment());
                    Log.d(TAG, "File Exists : " + mediaStorageDir.exists());
                    Bitmap b = BitmapFactory.decodeFile(mediaStorageDir.getCanonicalPath());*/
                    Bitmap b = (Bitmap)data.getExtras().get("data");
                    mImageView.setImageBitmap(b);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                //mImageView.setImageBitmap(b);


               // mImageView.setImageURI(imagePathUri);


            }else if(resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(activity, "CAMERA GOT CANCELLED", Toast.LENGTH_LONG).show();
            }

        }else if(requestCode == CameraUtils.REQUEST_CODE_VIDEO){

            if(resultCode == Activity.RESULT_OK){
                // Video captured and saved to fileUri specified in the Intent
                Toast.makeText(activity, "Video saved to:\n" +
                        data.getData(), Toast.LENGTH_LONG).show();
                mVideoView.setVisibility(View.VISIBLE);
                mVideoView.setVideoURI(data.getData());
                mediaController = new MediaController(activity);
                mediaController.setAnchorView(mVideoView);
                mVideoView.setMediaController(mediaController);
                mVideoView.start();

            }else if(resultCode == Activity.RESULT_CANCELED){
                Toast.makeText(activity, "VIDEO GOT CANCELLED",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
