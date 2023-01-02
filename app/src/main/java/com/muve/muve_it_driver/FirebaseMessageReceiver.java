package com.muve.muve_it_driver;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.muve.muve_it_driver.home.HomeActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.util.Map;

public class FirebaseMessageReceiver extends FirebaseMessagingService {

    int status_code =0;
    int status_codeback =0;
    String new_service_id ="";
    String REQUEST_ACCEPT ="com.muve.muve_it_driver";
    String REQUEST_CANCELL ="com.muve.muve_it_driver";
    String REQUEST_MSG ="com.muve.muve_it_driver";
    String REQUEST_Multiple_DEVICE_LOGIN ="com.muve.muve_it_driver";
    String No_Secondary_Driver_Available ="com.muve.muve_it_driver";
    String PrimaryReAssignLogOut ="com.muve.muve_it_driver";
    String PrimaryReAssign ="com.muve.muve_it_driver";
    String NoDriverAvailable ="com.muve.muve_it_driver";


    @Override
    public void
    onMessageReceived(RemoteMessage remoteMessage) {

        try {

            Map<String, String> data = remoteMessage.getData();

            Log.v("data", "data" + data);

            sendNotification(remoteMessage);

        }catch (Exception e){

            e.printStackTrace();
    }


        if (remoteMessage.getData() != null) {
            Log.v("execute11","m11"+remoteMessage.getData().get("message"));

            Map<String, String> data = remoteMessage.getData();

          //  showNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("message"));
        }
    }

    private void sendNotification(RemoteMessage remoteMessage) {

        Map<String, String> data = remoteMessage.getData();
       // status_code = Integer.parseInt(data.get("status_code"));
        Log.v("status_code--","status_code"+status_code) ;
        try {

            JSONObject custom = new JSONObject(data.get("custom"));
            JSONObject customa = new JSONObject(data.get("custom")).getJSONObject("a");
            status_code = new JSONObject(data.get("custom")).getJSONObject("a").getInt("status_code");

            Log.v("custom","custom"+custom);
            Log.v("customa","customa"+customa);
            Log.v("customastatus_code","customastatus_code"+status_code);

        }
        catch (Exception e){
            e.printStackTrace();
        }

        if (status_code == 412){

            LocalBroadcastManager broadcaster = LocalBroadcastManager.getInstance(getBaseContext());

            Intent intent = new Intent(REQUEST_ACCEPT);
            intent.putExtra("status_codeFromPush", status_code);
            broadcaster.sendBroadcast(intent);

        }
        else if (status_code == 350){

            LocalBroadcastManager broadcaster = LocalBroadcastManager.getInstance(getBaseContext());

            Intent intent = new Intent(REQUEST_CANCELL);
            intent.putExtra("status_codeFromPushServiceCancell", status_code);
            broadcaster.sendBroadcast(intent);

        }

        else if (status_code == 201){

            LocalBroadcastManager broadcaster = LocalBroadcastManager.getInstance(getBaseContext());

            Intent intent = new Intent(REQUEST_MSG);
            intent.putExtra("status_codeFromPushMSG", status_code);
            broadcaster.sendBroadcast(intent);

            Log.v("status_codeFromPushMSG","status_codeFromPushMSG"+status_code);
        }


        else if (status_code == 204){

            LocalBroadcastManager broadcaster = LocalBroadcastManager.getInstance(getBaseContext());

            Intent intent = new Intent(REQUEST_Multiple_DEVICE_LOGIN);
            intent.putExtra("status_codeFromPushMultipleDeviceLogin", status_code);
            broadcaster.sendBroadcast(intent);

        }

        else if (status_code == 510){

            LocalBroadcastManager broadcaster = LocalBroadcastManager.getInstance(getBaseContext());

            Intent intent = new Intent(No_Secondary_Driver_Available);
            intent.putExtra("status_codeFromPushNo_Secondary_Driver_Available", status_code);
            broadcaster.sendBroadcast(intent);

        }

        else if (status_code == 855){

            LocalBroadcastManager broadcaster = LocalBroadcastManager.getInstance(getBaseContext());

            Intent intent = new Intent(PrimaryReAssignLogOut);
            intent.putExtra("status_codeFromPushPrimaryReAssignLogOut", status_code);
            broadcaster.sendBroadcast(intent);



        }

        else if (status_code == 866){

           // new_service_id = data.get("new_service_id");

            LocalBroadcastManager broadcaster = LocalBroadcastManager.getInstance(getBaseContext());

            Intent intent = new Intent(PrimaryReAssign);
            intent.putExtra("status_codeFromPushPrimaryReAssign", status_code);
           // intent.putExtra("service_id", new_service_id);
            broadcaster.sendBroadcast(intent);

        }

        else if (status_code == 857){

            // new_service_id = data.get("new_service_id");

            LocalBroadcastManager broadcaster = LocalBroadcastManager.getInstance(getBaseContext());

            Intent intent = new Intent(NoDriverAvailable);
            intent.putExtra("status_codeFromPushNoDriverAvailable", status_code);
            // intent.putExtra("service_id", new_service_id);
            broadcaster.sendBroadcast(intent);

        }



    }

    private RemoteViews getCustomDesign(String title,
                                        String message) {
        RemoteViews remoteViews = new RemoteViews(
                getApplicationContext().getPackageName(),
                R.layout.notificationpopup);
        remoteViews.setTextViewText(R.id.title, title);
        remoteViews.setTextViewText(R.id.message, message);
        remoteViews.setImageViewResource(R.id.icon,
                R.drawable.muve_it_logo);
        return remoteViews;
    }

    // Method to display the notifications
    public void showNotification(String title,
                                 String message) {
        // Pass the intent to switch to the MainActivity
        Intent intent
                = new Intent(this, HomeActivity.class);
        // Assign channel ID
        String channel_id = getResources().getString(R.string.notification_channel_id);
        // Here FLAG_ACTIVITY_CLEAR_TOP flag is set to clear
        // the activities present in the activity stack,
        // on the top of the Activity that is to be launched
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // Pass the intent to PendingIntent to start the
        // next Activity
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, /*PendingIntent.FLAG_ONE_SHOT*/ PendingIntent.FLAG_IMMUTABLE);

        // Create a Builder object using NotificationCompat
        // class. This will allow control over all the flags
          NotificationManager notificationManager = null ;
        String CHANNEL_ID = getResources().getString(R.string.notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //         CharSequence name = getString(R.string.channel_name);
            CharSequence name = "channel_name";
            //        String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel  channel = new NotificationChannel(CHANNEL_ID, name, importance) ;
            //           channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE) ;
            notificationManager.createNotificationChannel(channel);
        } else {
            notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), channel_id)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(
                        BitmapFactory.decodeResource(
                                getResources(),R.mipmap.ic_launcher_round
                        )
                ) //   .setColor(getApplicationContext().getResources().getColor(R.color.colorPrimary))
                .setContentTitle(title)
                .setOnlyAlertOnce(false)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setPriority(Notification.PRIORITY_HIGH)
                .setChannelId(CHANNEL_ID)
                .setStyle(new NotificationCompat.BigPictureStyle().setSummaryText(message));
                notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());


    }
}
