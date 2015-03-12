package com.afrozaar.networkplayground;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.sql.Struct;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by jay on 3/11/15.
 */
public class CameraUtils {

    private Context context;

    public final static int REQUEST_CODE_CAMERA = 1;
    public final static int REQUEST_CODE_VIDEO = 2;

    private  String lastPhotoTimeStamp= "";

    private final static String TAG = CameraUtils.class.getName();

    public static class PhotoInfo{
        Uri imagePath;
        Intent i;
        public PhotoInfo(Uri u, Intent in){
            this.i = in;
            this.imagePath = u;
        }

        public Uri getImagePath() {
            return imagePath;
        }

        public Intent getI() {
            return i;
        }
    }


    public CameraUtils(){
        //this.context = context;
    }

    public static boolean checkCameraHardware(Context context){
        if(context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            Toast.makeText(context, "YAY you have a camera! Lets use it...", Toast.LENGTH_SHORT).show();

            return true;
        }else{
            Toast.makeText(context,"Haha! No camera to use...Booooo",Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public static PhotoInfo getCameraPhotoInfo(){

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri temp = getOutputMediaFileUri(REQUEST_CODE_CAMERA);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,temp);
        PhotoInfo pI = new PhotoInfo(temp,intent);
        return pI;
    }

    public static Intent getVideoIntent(){
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,getOutputMediaFileUri(REQUEST_CODE_VIDEO));
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY,0);
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT,10);
        return intent;
    }

    private static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "NetworkPlayground");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d(TAG, "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == REQUEST_CODE_CAMERA){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else if(type == REQUEST_CODE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    public static Uri getCameraImageUri(){
        Uri temp = Uri.fromFile(new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "NetworkPlayground"));
        return temp;
    }

    private static List<String> cameraFocusModes(){
        Camera mCamera = Camera.open();
        Camera.Parameters params = mCamera.getParameters();
        List<String> focusModes = params.getSupportedFocusModes();
        return focusModes;
    }
}
