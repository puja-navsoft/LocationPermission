package com.muve.muve_it_driver.util;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class ShowToast {

    public static void showShortToast(Context mContext, String message){

        Toast.makeText(mContext,""+message, Toast.LENGTH_SHORT).show();

    }

    public static void showLongToast(Context mContext, String message){

        Toast.makeText(mContext,""+message, Toast.LENGTH_LONG).show();

    }

    public static void showToastGravityCenter(Context context, String message, int gravity){
        Toast toast;
        toast= Toast.makeText(context,message, Toast.LENGTH_SHORT);
        toast.setGravity(gravity,0,0);
        toast.show();
    }

    public static void showSnackBar(Context context, String message, View parentLayout, int durationInMill){
        Snackbar.make(parentLayout, message, durationInMill).show();
    }

}
