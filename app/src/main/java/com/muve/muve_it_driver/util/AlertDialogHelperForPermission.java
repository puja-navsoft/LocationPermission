package com.muve.muve_it_driver.util;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;


public class AlertDialogHelperForPermission {


    public static void showDialogWithYesNoCallback(Context mContext, String title, String description, final OnItemClickReturnBooleanPermissionRationale onItemClickReturnBooleanPermissionRationale){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage(description)
                .setTitle(title);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                onItemClickReturnBooleanPermissionRationale.onItemClick(true);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                onItemClickReturnBooleanPermissionRationale.onItemClick(false);
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

}
