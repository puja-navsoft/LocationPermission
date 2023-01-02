package com.muve.muve_it_driver

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.muve.muve_it_driver.home.HomeActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.json.JSONObject
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class FirebaseMessageReceiver1 : FirebaseMessagingService() {
    var service_id = 0
    var status_code = 0
    var REQUEST_ACCEPT = "com.muve.muve_it_driver"
    var title = ""
    var message = ""
    var bodyText = ""
    var type_code = ""
    var serviceId = ""
    var notificationDetails = ""
    var image = ""
    // Override onMessageReceived() method to extract the
    // title and
    // body from the message passed in FCM


    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.e("device_token", token.toString())

    }
   override fun onMessageReceived(remoteMessage: RemoteMessage) {
        try {
            var finalMessage: JSONObject? = null
           var notifiDetails: JSONObject? = null
            println("RemoteMessage ==>>${remoteMessage}")
            Log.e("Notification", remoteMessage.data.toString())
            Log.d("notif", "remoteMessage1: " + remoteMessage.getFrom())
            Log.d("notif", "remoteMessage2: " + remoteMessage.getData())
            Log.d("notif", "remoteMessage2: " + remoteMessage.getNotification())
            Log.d("notif", "remoteMessage3: " + remoteMessage.getNotification()!!.getTitle())
            if (remoteMessage == null) {
                return
            }
            val params = remoteMessage.data
            finalMessage = JSONObject(params as Map<*, *>)


            if (finalMessage.has("title")) {
                title = finalMessage["title"].toString()
            }
            if (finalMessage.has("message")) {
                message = finalMessage["message"].toString()
            }
            if (finalMessage.has("status_code")) {
                type_code = finalMessage["status_code"].toString()
            }
            if (finalMessage.has("detail")) {
                notificationDetails = finalMessage["detail"].toString()
            }
            if (finalMessage.has("body")) {
                bodyText = finalMessage["body"].toString()
            }
            var obj = JSONObject(notificationDetails)
            notifiDetails = JSONObject(obj as Map<*, *>)

            if (notifiDetails.has("service_id")) {
                serviceId = notifiDetails["service_id"].toString()
            }

            Log.e("Notification","title"+title +",message"+message+",typeCode"+type_code+"body"+bodyText+"serviceId"+serviceId )
            //  Log.v("status_code///","status_code"+jsonObject .getString("status_code")) ;

            // testNotification(remoteMessage.getNotification().getTitle());
            sendNotification(type_code,message)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        //   sendNotification(remoteMessage);


        // Log.d("notif", "remoteMessage: "+remoteMessage);

        // First case when notifications are received via
        // data event
        // Here, 'title' and 'message' are the assumed names
        // of JSON
        // attributes. Since here we do not have any data
        // payload, This section is commented out. It is
        // here only for reference purposes.
        /*if(remoteMessage.getData().size()>0){
            showNotification(remoteMessage.getData().get("title"),
                          remoteMessage.getData().get("message"));
        }*/

        // Second case when notification payload is
        // received.
 /*       if (remoteMessage.getNotification() != null) {
            // Since the notification is received directly from
            // FCM, the title and the body can be fetched
            // directly as below.
            showNotification(
                remoteMessage.getNotification().getTitle(),
                remoteMessage.getNotification().getBody()
            )
        }*/
    }

    /*
    private void testNotification(String title) {

        LocalBroadcastManager broadcaster = LocalBroadcastManager.getInstance(getBaseContext());

        Intent intent = new Intent(REQUEST_ACCEPT);
        intent.putExtra("status_codeFromPush", title);
        broadcaster.sendBroadcast(intent);


    }
*/


    private fun sendNotification(redirectionType: String, message: String) {
        var bitmap: Bitmap? = null
        if (!image.equals("", ignoreCase = true)) {
            bitmap = getBitmapfromUrl(image)
        }

        var intent: Intent? = null
        if(!redirectionType.equals(""))
        {
            intent=getRedirectionIntent(redirectionType.toInt())
        }
        else
        {
            intent=null
        }


        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            val pendingIntent = PendingIntent.getActivity(
                this, 0 /* Request code */, intent,
                PendingIntent.FLAG_CANCEL_CURRENT
            )
            val defaultSoundUri =
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            if (image != "") {
                var notificationManager: NotificationManager? = null
                val CHANNEL_ID = "my_channel_id"
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    //         CharSequence name = getString(R.string.channel_name);
                    val name: CharSequence = "channel_name"
                    //        String description = getString(R.string.channel_description);
                    val importance = NotificationManager.IMPORTANCE_DEFAULT
                    val channel =
                        NotificationChannel(CHANNEL_ID, name, importance)
                    //           channel.setDescription(description);
                    // Register the channel with the system; you can't change the importance
                    // or other notification behaviors after this
                    notificationManager =
                        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    notificationManager!!.createNotificationChannel(channel)
                } else {
                    notificationManager =
                        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                }
                val notificationBuilder: NotificationCompat.Builder = NotificationCompat.Builder(
                    this
                )
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(
                        BitmapFactory.decodeResource(
                            resources,
                            R.mipmap.ic_launcher
                        )
                    ) //   .setColor(getApplicationContext().getResources().getColor(R.color.colorPrimary))
                    .setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setChannelId(CHANNEL_ID)
                    .setStyle(
                        NotificationCompat.BigPictureStyle().bigPicture(bitmap).setSummaryText(
                            message
                        )
                    )
                notificationManager!!.notify(
                    1 /* ID of notification */,
                    notificationBuilder.build()
                )
            } else {
                var notificationManager: NotificationManager? = null
                val CHANNEL_ID = "my_channel_id"
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    //         CharSequence name = getString(R.string.channel_name);
                    val name: CharSequence = "channel_name"
                    //        String description = getString(R.string.channel_description);
                    val importance = NotificationManager.IMPORTANCE_DEFAULT
                    val channel =
                        NotificationChannel(CHANNEL_ID, name, importance)
                    //           channel.setDescription(description);
                    // Register the channel with the system; you can't change the importance
                    // or other notification behaviors after this
                    notificationManager =
                        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    notificationManager!!.createNotificationChannel(channel)
                } else {
                    notificationManager =
                        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                }
                val notificationBuilder: NotificationCompat.Builder = NotificationCompat.Builder(
                    this
                )
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(
                        BitmapFactory.decodeResource(
                            resources,
                            R.mipmap.ic_launcher
                        )
                    ) //   .setColor(getApplicationContext().getResources().getColor(R.color.colorPrimary))
                    .setContentTitle(title)
                    .setStyle(NotificationCompat.BigTextStyle().bigText(message))
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent)
                    .setChannelId(CHANNEL_ID)
                    .setPriority(Notification.PRIORITY_HIGH)
                notificationManager!!.notify(
                    1 /* ID of notification */,
                    notificationBuilder.build()
                )
            }
        } else {
            // normal notification
            val defaultSoundUri =
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            if (image != "") {
                var notificationManager: NotificationManager? = null
                val CHANNEL_ID = "my_channel_id"
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    //         CharSequence name = getString(R.string.channel_name);
                    val name: CharSequence = "channel_name"
                    //        String description = getString(R.string.channel_description);
                    val importance = NotificationManager.IMPORTANCE_DEFAULT
                    val channel =
                        NotificationChannel(CHANNEL_ID, name, importance)
                    //           channel.setDescription(description);
                    // Register the channel with the system; you can't change the importance
                    // or other notification behaviors after this
                    notificationManager =
                        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    notificationManager!!.createNotificationChannel(channel)
                } else {
                    notificationManager =
                        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                }
                val notificationBuilder: NotificationCompat.Builder = NotificationCompat.Builder(
                    this
                )
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(
                        BitmapFactory.decodeResource(
                            resources,
                            R.mipmap.ic_launcher
                        )
                    ) //   .setColor(getApplicationContext().getResources().getColor(R.color.colorPrimary))
                    .setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setChannelId(CHANNEL_ID)
                    .setStyle(
                        NotificationCompat.BigPictureStyle().bigPicture(bitmap).setSummaryText(
                            message
                        )
                    )
                notificationManager!!.notify(
                    1 /* ID of notification */,
                    notificationBuilder.build()
                )
            } else {
                var notificationManager: NotificationManager? = null
                val CHANNEL_ID = "my_channel_id"
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    //         CharSequence name = getString(R.string.channel_name);
                    val name: CharSequence = "channel_name"
                    //        String description = getString(R.string.channel_description);
                    val importance = NotificationManager.IMPORTANCE_DEFAULT
                    val channel =
                        NotificationChannel(CHANNEL_ID, name, importance)
                    //           channel.setDescription(description);
                    // Register the channel with the system; you can't change the importance
                    // or other notification behaviors after this
                    notificationManager =
                        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    notificationManager!!.createNotificationChannel(channel)
                } else {
                    notificationManager =
                        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                }
                val notificationBuilder: NotificationCompat.Builder = NotificationCompat.Builder(
                    this
                )
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(
                        BitmapFactory.decodeResource(
                            resources,
                            R.mipmap.ic_launcher
                        )
                    ) //   .setColor(getApplicationContext().getResources().getColor(R.color.colorPrimary))
                    .setContentTitle(title)
                    .setStyle(NotificationCompat.BigTextStyle().bigText(message))
                    .setContentText(message)
                    .setSound(defaultSoundUri)
                    .setAutoCancel(true)
                    .setChannelId(CHANNEL_ID)
                    .setPriority(Notification.PRIORITY_HIGH)
                notificationManager!!.notify(
                    1 /* ID of notification */,
                    notificationBuilder.build()
                )
            }
        }

    }
    fun getBitmapfromUrl(imageUrl: String): Bitmap? {
        return try {

            val url = URL(imageUrl)
            val connection =
                url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            BitmapFactory.decodeStream(input)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
/*    private fun sendNotification(remoteMessage: RemoteMessage) {
        val data: Map<String, String> = remoteMessage.getData()
        val jobType = data["detail"].toString()
        status_code = data["status_code"]!!.toInt()
        Log.v("status_code--", "status_code$status_code")
        Log.v("data", "data$data")
        if (status_code == 412) {
            val broadcaster: LocalBroadcastManager =
                LocalBroadcastManager.getInstance(getBaseContext())
            val intent = Intent(REQUEST_ACCEPT)
            intent.putExtra("status_codeFromPush", status_code)
            broadcaster.sendBroadcast(intent)
        }
    }*/

    // Method to get the custom Design for the display of
    // notification.
    private fun getCustomDesign(
        title: String,
        message: String
    ): RemoteViews {
        val remoteViews = RemoteViews(
            getApplicationContext().getPackageName(),
            R.layout.notificationpopup
        )
        remoteViews.setTextViewText(R.id.title, title)
        remoteViews.setTextViewText(R.id.message, message)
        remoteViews.setImageViewResource(
            R.id.icon,
            R.drawable.muve_it_logo
        )
        return remoteViews
    }

    // Method to display the notifications
    fun showNotification(
        title: String,
        message: String
    ) {
        // Pass the intent to switch to the MainActivity
        val intent = Intent(this, HomeActivity::class.java)
        // Assign channel ID
        val channel_id: String = getResources().getString(R.string.notification_channel_id)
        // Here FLAG_ACTIVITY_CLEAR_TOP flag is set to clear
        // the activities present in the activity stack,
        // on the top of the Activity that is to be launched
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        // Pass the intent to PendingIntent to start the
        // next Activity
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        // Create a Builder object using NotificationCompat
        // class. This will allow control over all the flags
        var builder: NotificationCompat.Builder = NotificationCompat.Builder(
            getApplicationContext(),
            channel_id
        )
            .setSmallIcon(R.drawable.muve_it_logo)
            .setAutoCancel(true)
            .setVibrate(
                longArrayOf(
                    1000, 1000, 1000,
                    1000, 1000
                )
            )
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)

        // A customized design for the notification can be
        // set only for Android versions 4.1 and above. Thus
        // condition for the same is checked here.
        builder = if (Build.VERSION.SDK_INT
            >= Build.VERSION_CODES.JELLY_BEAN
        ) {
            builder.setContent(
                getCustomDesign(title, message)
            )
        } // If Android Version is lower than Jelly Beans,
        else {
            builder.setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.muve_it_logo)
        }
        // Create an object of NotificationManager class to
        // notify the
        // user of events that happen in the background.
        val notificationManager: NotificationManager = getSystemService(
            Context.NOTIFICATION_SERVICE
        ) as NotificationManager
        // Check if the Android Version is greater than Oreo
        if (Build.VERSION.SDK_INT
            >= Build.VERSION_CODES.O
        ) {
            val notificationChannel = NotificationChannel(
                channel_id, "web_app",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(
                notificationChannel
            )
        }
        notificationManager.notify(0, builder.build())
    }


    fun getRedirectionIntent(redirectionType: Int): Intent?
    {
        var intent:Intent?=null
        when (redirectionType) {
            412 -> {
                val broadcaster: LocalBroadcastManager =
                    LocalBroadcastManager.getInstance(getBaseContext())
                val intent = Intent(REQUEST_ACCEPT)
                intent.putExtra("status_codeFromPush", status_code)
                broadcaster.sendBroadcast(intent)
            }

            else -> {
                print("Nothing")
                intent=null
            }
        }
        return intent
    }
}