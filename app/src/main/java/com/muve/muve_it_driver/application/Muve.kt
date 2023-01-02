package com.muve.muve_it_driver.application

import android.app.Application
import com.muve.muve_it_driver.application.Muve
import androidx.multidex.MultiDex

class Muve : Application() {
    private val mTracker: Muve? = null
    override fun onCreate() {
        super.onCreate()
        instance = this
        MultiDex.install(this)

        /*   String android_id = Settings.Secure.getString(instance.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        AppPreferences.getInstance().storeDeviceId(android_id);

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "getInstanceId failed", task.getException());
                            return;
                        }
                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        Log.d("TAG", "Received token: " + token);
                        try {
                            AppPreferences.getInstance().storeFirebaseToken(token);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        // Notify UI that registration has completed, so the progress indicator can be hidden.
                        Intent registrationComplete = new Intent(Config.REGISTRATION_COMPLETE);
                        registrationComplete.putExtra("Authorization", token);
                        LocalBroadcastManager.getInstance(instance).sendBroadcast(registrationComplete);
                    }
                });
*/
    }

    override fun onLowMemory() {
        super.onLowMemory()
        System.gc()
    }

    companion object {
        private var instance: Muve? = null
        fun getInstance(): Muve? {
            return if (instance != null) {
                instance
            } else {
                Muve()
            }
        }
    }
}