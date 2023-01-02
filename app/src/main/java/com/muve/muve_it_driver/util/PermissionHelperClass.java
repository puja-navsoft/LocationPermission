package com.muve.muve_it_driver.util;

import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

public class PermissionHelperClass {

    public static void checkMultiplePermissionsForActivity(final Activity mActivity, final Boolean finishActivityStatus, final String[] permissionArr, final String rationaleTitle, final String rationaleDescription, final int requestCode, MultiplePermissionHelperInterface multiplePermissionHelperInterface){

        Boolean allPermissionsGranted = true;
        for(int i=0;i<permissionArr.length;i++){
            if (ActivityCompat.checkSelfPermission(mActivity, permissionArr[i]) != PackageManager.PERMISSION_GRANTED){
                allPermissionsGranted = false;
                break;
            }
        }
        if(allPermissionsGranted){
            multiplePermissionHelperInterface.multiplePermissionGivenStatus(true);
        }
        else{
            for(int i=0;i<permissionArr.length;i++){
                if (ActivityCompat.checkSelfPermission(mActivity, permissionArr[i]) != PackageManager.PERMISSION_GRANTED) {
                    if(ActivityCompat.shouldShowRequestPermissionRationale(mActivity,permissionArr[i])){
                        AlertDialogHelperForPermission.showDialogWithYesNoCallback(mActivity, rationaleTitle, rationaleDescription, new OnItemClickReturnBooleanPermissionRationale() {
                            @Override
                            public void onItemClick(Boolean status) {
                                if(status){
                                    ActivityCompat.requestPermissions(mActivity, permissionArr, requestCode);
                                }
                                else{
                                    ShowToast.showShortToast(mActivity,rationaleDescription);
                                    if(finishActivityStatus){
                                        mActivity.finish();
                                    }
                                }
                            }
                        });
                    }
                    else{
                        ActivityCompat.requestPermissions(mActivity,permissionArr,requestCode);
                    }
                    break;
                }
            }
        }
    }


    public static void onRequestPermissionResultForActivity(Activity mActivity, Boolean finishActivityStatus, String rationaleDesc, String rationaleForSettings, String[] permissions, int[] grantResults, final PermissionHelperInterface permissionHelperInterface){

        Boolean allPermissionsGranted = true;
        for(int i=0;i<grantResults.length;i++){
            if(grantResults[i] == PackageManager.PERMISSION_DENIED){
                allPermissionsGranted = false;
                break;
            }
        }
        if(allPermissionsGranted){
            permissionHelperInterface.singlePermissionGivenStatus(true);
        }
        else{
            if(ActivityCompat.shouldShowRequestPermissionRationale(mActivity,permissions[0])){
                ShowToast.showShortToast(mActivity,rationaleDesc);

            }
            else{
                ShowToast.showShortToast(mActivity,rationaleForSettings);
            }
            if(finishActivityStatus){
                mActivity.finish();
            }
        }

    }


}
