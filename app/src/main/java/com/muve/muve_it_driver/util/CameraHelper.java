package com.muve.muve_it_driver.util;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import androidx.core.content.FileProvider;

import com.muve.muve_it_driver.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class CameraHelper {

    public static String TAG = "CameraHelper";

    public static File saveAFileInExternalStorageAndGetTheFile(Context mContext, String mimeTypeWithDot){
        if(isExternalStorageWritable()){
            try{
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String fileName = getRandomString()+"_"+timeStamp;
                //LogHelper.logReadyMadeString("aaaaaaaaaa"+Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES));
                //File image = new File(Environment.getExternalStorageDirectory(), fileName+mimeType);
                File storageDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                File image = File.createTempFile(
                        fileName,  /* prefix */
                        mimeTypeWithDot,         /* suffix */
                        storageDir      /* directory */
                );
                if (!image.mkdirs()) {
                    Log.v(TAG, "Directory not created");
                }
                return image;
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
        else{
            ShowToast.showShortToast(mContext,mContext.getString(R.string.externalStorageIsNotMounted));
        }
        return null;
    }

    public static Uri getTheUriOfTheFileToSendInTheCameraIntent(Context mContext, File file){
        return FileProvider.getUriForFile(mContext, "com.facefirst.fileprovider", file);
    }

    private static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    private static String getRandomString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }

}
